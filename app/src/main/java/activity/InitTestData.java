package activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import enums.Kind;
import enums.Role;
import finals.AppData;
import models.Items;
import models.User;

public class InitTestData implements Runnable { // todo klasa jest utworzona tylko dlatego, żeby zainicjalizować testowe dane

    private FirebaseAuth mAuth;

    @Override
    public void run() {
        mAuth = FirebaseAuth.getInstance();
        fillStartData();
    }

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
                    System.out.println("Testowe dane poprawnie uzupełnione.");
                } else {
                    System.out.println("Błąd: " + task.getException().getMessage());
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
                                System.out.println("Użytkownicy zostały poprawnie utworzone.");
                            } else {
                                System.out.println("Błąd: " + task.getException().getMessage());
                            }
                        }
                    });
                } else {
                    System.out.println("Wystąpił błąd: " + task.getException().getMessage());
                }
            }
        });
    }
}
