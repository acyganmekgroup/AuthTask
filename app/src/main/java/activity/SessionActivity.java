package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SessionActivity extends Activity {
    public static final long DISCONNECT_TIMEOUT = 15000; // 30 sec = 30 * 1000 ms

    private Handler disconnectHandler = new Handler();

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {

            FirebaseAuth.getInstance().signOut();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    SessionActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Zgasła sesja!");
            alertDialog
                    .setMessage("\n" +
                            "Limit czasu sesji, naciśnij OK, aby przejść do poprzedniego ekranu.");
            alertDialog.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SessionActivity.this,
                                    LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}
