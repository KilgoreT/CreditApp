package k.kilg.creditapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText mEtEmail;
    private EditText mEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

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
                            AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                            builder
                                    .setTitle(R.string.ac_launch_confirm_email_title)
                                    .setMessage(R.string.ac_launch_confirm_email_body)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if (currentUser != null) {
                                                sendEmailVerification(currentUser);
                                                updateUI(currentUser);
                                            }
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.ac_launch_alert_register_failed, Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getBaseContext(), R.string.ac_launch_alert_auth_failed, Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void sendEmailVerification(FirebaseUser currentUser) {
        currentUser
                .sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), R.string.ac_launch_alert_email_sent_successful, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.ac_launch_alert_email_sent_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEtEmail.setError(getResources().getString(R.string.ac_launch_alert_email_required));
            valid = false;
        } else {
            mEtEmail.setError(null);
            valid = Patterns.EMAIL_ADDRESS.matcher(mEtEmail.getText().toString()).matches();
            if (!valid) {
                mEtEmail.setError(getResources().getString(R.string.ac_launch_alert_email_not_valid));
            }
        }

        String password = mEtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mEtPassword.setError(getResources().getString(R.string.ac_launch_alert_password_required));
            valid = false;
        } else {
            mEtPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, CreditActivity.class);
            startActivity(intent);
        }
    }
}
