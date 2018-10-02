package br.com.aimcol.fallalertapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import br.com.aimcol.fallalertapp.R;
import br.com.aimcol.fallalertapp.model.User;
import br.com.aimcol.fallalertapp.service.UserService;
import br.com.aimcol.fallalertapp.util.BroadcastReceiverUtils;
import br.com.aimcol.fallalertapp.util.CrudAction;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private FirebaseAuth mAuth;
    private Gson gson;
    private BroadcastReceiver mBroadcastReceiver;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onStart() {
        super.onStart();
        
        if (this.gson == null) {
            this.gson = new Gson();
        }

        FirebaseUser currentUser = this.mAuth.getCurrentUser();
        if (currentUser != null) {
            User user = UserService.toUser(currentUser);
            String userJson = LoginActivity.this.gson.toJson(user);

            this.mBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //fixme change to a better location
                    String updateUserJson = intent.getStringExtra(User.USER_JSON);
                    //UserService.startUserService(updateUserJson, CrudAction.UPDATE, LoginActivity.this);
                    LoginActivity.super.unregisterReceiver(LoginActivity.this.mBroadcastReceiver);
                    MainActivity.startMainActivitySendUser(updateUserJson, LoginActivity.this);
                }
            };
            BroadcastReceiverUtils.registerBroadcastReceiver(LoginActivity.this, LoginActivity.this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_LOAD);
            UserService.startUserService(userJson, CrudAction.READ, this);
            this.showProgress(true);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);

        if (this.gson == null) {
            this.gson = new Gson();
        }

        // Get instance of FirebaseAuth
        this.mAuth = FirebaseAuth.getInstance();

        // Set up the login form.
        this.mEmailView = super.findViewById(R.id.email);

        this.mPasswordView = super.findViewById(R.id.password);
        this.mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    LoginActivity.this.attemptLogin();
                    return true;
                }
                return false;
            }
        });

        this.mPasswordConfirmationView = super.findViewById(R.id.password_confirmation);

        Button mEmailSignInButton = super.findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.attemptLogin();
            }
        });

        Button mEmailSignUpButton = super.findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.this.mPasswordConfirmationView.getVisibility() == View.GONE) {
                    LoginActivity.this.mPasswordConfirmationView.setVisibility(View.VISIBLE);
                } else {
                    LoginActivity.this.attemptSignUp();
                }
            }
        });

        this.mLoginFormView = super.findViewById(R.id.login_form);
        this.mProgressView = super.findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        this.mEmailView.setError(null);
        this.mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = this.mEmailView.getText().toString();
        String password = this.mPasswordView.getText().toString();

        if (this.isEmailAndPasswordValid(email, password, null)) {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            this.showProgress(true);
            this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d("login", "signInWithEmail:success");
                        FirebaseUser currentUser = LoginActivity.this.mAuth.getCurrentUser();
                        User user = UserService.toUser(currentUser);
                        String userJson = LoginActivity.this.gson.toJson(user);

                        LoginActivity.this.mBroadcastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                //fixme change to a better location
                                String updateUserJson = intent.getStringExtra(User.USER_JSON);
                                //UserService.startUserService(updateUserJson, CrudAction.UPDATE, LoginActivity.this);
                                LoginActivity.super.unregisterReceiver(LoginActivity.this.mBroadcastReceiver);
                                MainActivity.startMainActivitySendUser(updateUserJson, LoginActivity.this);
                            }
                        };

                        BroadcastReceiverUtils.registerBroadcastReceiver(LoginActivity.this, LoginActivity.this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_LOAD);
                        UserService.startUserService(userJson, CrudAction.READ, LoginActivity.this);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("login", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        LoginActivity.this.showProgress(false);
                        LoginActivity.this.mPasswordView.setText("");
                    }
                }
            });
        }
    }

    private void attemptSignUp() {
        String email = this.mEmailView.getText().toString();
        String password = this.mPasswordView.getText().toString();
        String passwordConfirmation = this.mPasswordConfirmationView.getText().toString();

        if (this.isEmailAndPasswordValid(email, password, passwordConfirmation)) {
            // Show a progress spinner, and kick off a background task to perform the user sign up attempt.
            this.showProgress(true);
            this.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign up success
                        Log.d("signup", "createUserWithEmail:success");
                        FirebaseUser currentUser = LoginActivity.this.mAuth.getCurrentUser();
                        User user = UserService.toUser(currentUser);
                        String userJson = LoginActivity.this.gson.toJson(user);
                        LoginActivity.this.mBroadcastReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                //fixme change to a better location
                                String updateUserJson = intent.getStringExtra(User.USER_JSON);
                                LoginActivity.super.unregisterReceiver(LoginActivity.this.mBroadcastReceiver);
                                MainActivity.startMainActivitySendUser(updateUserJson, LoginActivity.this);
                                LoginActivity.this.showProgress(true);
                            }
                        };

                        BroadcastReceiverUtils.registerBroadcastReceiver(LoginActivity.this, LoginActivity.this.mBroadcastReceiver, UserService.USER_SERVICE_ACTION_UPDATE);
                        UserService.startUserService(userJson, CrudAction.CREATE, LoginActivity.this);

                    } else {
                        // If sign up fails, display a message to the user.
                        Log.w("signup", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        LoginActivity.this.showProgress(false);
                        LoginActivity.this.mPasswordView.setText("");
                        LoginActivity.this.mPasswordConfirmationView.setText("");
                    }
                }
            });
        }
    }

    private boolean isEmailAndPasswordValid(String email,
                                            String password,
                                            String passwordConfirmation) {

        boolean valid = true;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            this.mPasswordView.setError(super.getString(R.string.error_field_required));
            focusView = this.mPasswordView;
            valid = false;
        } else if (!this.isPasswordValid(password)) {
            this.mPasswordView.setError(super.getString(R.string.error_invalid_password));
            focusView = this.mPasswordView;
            valid = false;
        }

        // Check if password confirmation matches the password
        if (this.mPasswordConfirmationView.getVisibility() == View.VISIBLE && !password.equals(passwordConfirmation)) {
            this.mPasswordView.setError(super.getString(R.string.error_passwords_dont_match));
            this.mPasswordConfirmationView.setError(super.getString(R.string.error_passwords_dont_match));
            focusView = this.mPasswordConfirmationView;
            valid = false;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            this.mEmailView.setError(super.getString(R.string.error_field_required));
            focusView = this.mEmailView;
            valid = false;
        } else if (!this.isEmailValid(email)) {
            this.mEmailView.setError(super.getString(R.string.error_invalid_email));
            focusView = this.mEmailView;
            valid = false;
        }
        if (!valid) {
            // There was an error; don't attempt sign in/up and focus the first form field with an error.
            focusView.requestFocus();
        }

        return valid;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = super.getResources().getInteger(android.R.integer.config_shortAnimTime);

        this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        this.mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LoginActivity.this.mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        this.mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        this.mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                LoginActivity.this.mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i,
                                         Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader,
                               Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}

