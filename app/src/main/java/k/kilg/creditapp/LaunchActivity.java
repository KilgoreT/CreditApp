package k.kilg.creditapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button btnSignIn;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);


        //mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                signIn(mEtEmail.getText().toString(), mEtPassword.getText().toString());
                break;
            case R.id.btnRegister:
                registerAccount(mEtEmail.getText().toString(), mEtPassword.getText().toString());
                break;
        }
    }

    private void registerAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        } else {
                            //TODO: fix message
                            Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);
                        } else {
                            //TODO: fix message
                            Toast.makeText(getBaseContext(), "Auth failed", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {
                            //TODO: create and add status field in layoit
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    //todo: fix email verification
   /* private void sendEmailVerification(FirebaseUser currentUser) {
        btnEmailVerification.setEnabled(false);
        if (currentUser == null) {
            btnEmailVerification.setEnabled(true);
            return;
        }
        currentUser
                .sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btnEmailVerification.setEnabled(true);
                        if (task.isSuccessful()) {
                            //TODO: fix message
                            Toast.makeText(getBaseContext(), "Verification sent!", Toast.LENGTH_LONG).show();
                        } else {
                            //TODO: fix message
                            Toast.makeText(getBaseContext(), "Fail send verification", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/

    private boolean validateForm() {
        return true;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (!user.isEmailVerified()) {
                Log.d("###", ">>> user not verified: " + user.getEmail());
               // sendEmailVerification(user);
            } else {
                Log.d("###", ">>> User verified: " + user.getEmail());
            }
            Intent intent = new Intent(this, CreditActivity.class);
            startActivity(intent);
            //signOut();
        } else {
            mEtEmail.setVisibility(View.VISIBLE);
            mEtPassword.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);

        }
    }
}
