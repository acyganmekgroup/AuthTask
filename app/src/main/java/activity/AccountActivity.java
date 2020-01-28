package activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;

import enums.Role;
import fit.fitapps.authtask.R;
import wrappers.ManagerItemsWrapper;
import wrappers.WaiterItemsWrapper;

public class AccountActivity extends SessionActivity {

    private TextView nameTxt, shortDescTxt, priceNetTxt, kindTxt;
    private ImageView image;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        nameTxt = findViewById(R.id.nameTxt);
        shortDescTxt = findViewById(R.id.shortDescTxt);
        priceNetTxt = findViewById(R.id.priceNetTxt);
        kindTxt = findViewById(R.id.kindTxt);
        image = findViewById(R.id.image);

        database = FirebaseDatabase.getInstance();

        database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Role role = dataSnapshot.getValue(Role.class);
                getItemsByUserId(role);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getItemsByUserId(final Role role) {
        database.getReference("items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (Role.MANAGER == role) {
                    ManagerItemsWrapper items = dataSnapshot.getValue(ManagerItemsWrapper.class);
                    nameTxt.setText(items.getName());
                    shortDescTxt.setText(items.getShortDesc());
                    priceNetTxt.setText(String.format("%s", items.getPriceNet()));
                    kindTxt.setText(items.getKind().name());
                    new DownLoadImageTask(image).execute("https://odkupieniewin.yourtechnicaldomain.com/data/gfx/icons/versions/9/0/9.jpg");
                } else {
                    WaiterItemsWrapper items = dataSnapshot.getValue(WaiterItemsWrapper.class);
                    nameTxt.setText(items.getName());
                    shortDescTxt.setText(items.getShortDesc());
                    kindTxt.setText(items.getKind().name());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(AccountActivity.this, LoginActivity.class));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
