package k.kilg.creditapp;

//checked

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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD";
    private static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    private static final String ERROR_USER_DISABLED = "ERROR_USER_DISABLED";
    private static final String ERROR_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE";
    private static final String ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD";
    private static final String ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL";
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
                            showErrorMessage(task);
                            updateUI(null);
                        }
                    }
                });
    }

    private void showErrorMessage(Task<AuthResult> task) {
        try {
            throw task.getException();
        } catch (FirebaseAuthException e) {
            String ERROR_CODE = e.getErrorCode();
            switch (ERROR_CODE) {
                //todo: вынести сообщения в ресурсы, сделать локализацию на русском.
                case ERROR_WEAK_PASSWORD:
                    showToast("The given password is invalid. [ Password should be at least 6 characters ]");
                    break;
                case ERROR_USER_NOT_FOUND:
                    showToast("There is no user record corresponding to this identifier. The user may have been deleted.");
                    break;
                case ERROR_USER_DISABLED:
                    showToast("The user account has been disabled by an administrator.");
                    break;
                case ERROR_EMAIL_ALREADY_IN_USE:
                    showToast("The email address is already in use by another account.");
                    break;
                case ERROR_WRONG_PASSWORD:
                    showToast("The password is invalid or the user does not have a password");
                    break;
                case ERROR_INVALID_EMAIL:
                    showToast("The email address is badly formatted");
                    break;
                default:
                    showToast(e.getMessage());
            }
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
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
                            showErrorMessage(task);
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
                            showToast(task.getException().getMessage());
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
            if (!user.isEmailVerified()) {
                //todo: локализовать
                showToast("Please, verify email.");
                return;
            }
            Intent intent = new Intent(this, CreditActivity.class);
            startActivity(intent);
        }
    }
}
