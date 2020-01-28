package fit.fitapps.authtask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import enums.Kind;
import enums.Role;
import finals.AppData;
import models.Items;
import models.User;

public class InitTestDataActivity extends AppCompatActivity { // todo activity jest utworzone tylko żeby zainicjalizować dane testowe

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTestData.run();
        startActivity(new Intent(InitTestDataActivity.this, LoginActivity.class));
    }

    private Runnable initTestData = new Runnable() {
        @Override
        public void run() {
            String INIT_DATA_PREF = "initDataPref";
            SharedPreferences initDataPref = getSharedPreferences(INIT_DATA_PREF, MODE_PRIVATE);
            String INIT_DATA = "initData";
            boolean initData = initDataPref.getBoolean(INIT_DATA, false);

            if (!initData) {
                FirebaseApp.initializeApp(InitTestDataActivity.this);
                mAuth = FirebaseAuth.getInstance();

                fillStartData();

                SharedPreferences.Editor editor = getSharedPreferences(INIT_DATA_PREF, MODE_PRIVATE).edit();
                editor.putBoolean(INIT_DATA, true);
                editor.apply();
            }
        }
    };

    private void fillStartData() {
        fillItems();

        mAuth.fetchSignInMethodsForEmail(AppData.kelnerEmail)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            final User waiter = new User(AppData.kelnerEmail, AppData.kelnerPassword, Role.KELNER);
                            createNewUser(waiter);
                        }
                    }
                });

        mAuth.fetchSignInMethodsForEmail(AppData.managerEmail)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                        if (isNewUser) {
                            final User manager = new User(AppData.managerEmail, AppData.managerPassword, Role.MANAGER);
                            createNewUser(manager);
                        }
                    }
                });
    }

    private void fillItems() {
        Items items = new Items(
                125f
                , "https://odkupieniewin.yourtechnicaldomain.com/data/gfx/icons/versions/9/0/9.jpg"
                , "Cuvee Blanc de Blancs"
                , "Wino o delikatnej zielonej barwie z żółtymi refleksami. "
                , Kind.WINE
        );

        FirebaseDatabase.getInstance().getReference("items")
                .setValue(items).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(InitTestDataActivity.this, "Testowe dane poprawnie uzupełnione.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(InitTestDataActivity.this, "Błąd: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createNewUser(final User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user.getUserRole()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(InitTestDataActivity.this, "Użytkownicy zostały poprawnie utworzone.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(InitTestDataActivity.this, "Błąd: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(InitTestDataActivity.this, "Wystąpił błąd: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
