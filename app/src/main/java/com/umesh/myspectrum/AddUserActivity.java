package com.sipra.myspectrum;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sipra.myspectrum.database.DatabaseHandler;

import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class AddUserActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private ImageView iv_Back;

    // UI references.
    private EditText mUserName;
    private EditText mPasswordView;
    private View mProgressView;
    Button mEmailSignInButton;
    /**
     * Data base Handler object
     */
    DatabaseHandler dbHelper;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Set up the login form.
        mUserName = (EditText) findViewById(R.id.userName);
        mPasswordView = (EditText) findViewById(R.id.password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        iv_Back = (ImageView) toolbar.findViewById(R.id.iv_Back);
        iv_Back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbHelper = new DatabaseHandler(this);


        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserName.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserName.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("This fields is required");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password) + " must be between 5 to 12 characters in length");
            focusView = mPasswordView;
            cancel = true;
        } else if (!Utility.isValidPassword(password, Pattern.compile("(?=.*\\d).{1,}"))) {
            mPasswordView.setError("Password must contain at list one number");
            focusView = mPasswordView;
            cancel = true;
        } else if (!Utility.isValidPassword(password, Pattern.compile("(?=.*?[a-zA-Z]).{1,}"))) {
            mPasswordView.setError("Password must contain at list one alphabetical characters");
            focusView = mPasswordView;
            cancel = true;
        } else if (Utility.isValidPassword(password, Pattern.compile("(.+)\\1"))) {
            mPasswordView.setError("Password must not contain any repeating substring");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getString(R.string.error_field_required));
            focusView = mUserName;
            cancel = true;
        } else if (!isPasswordValid(userName)) {
            mUserName.setError("UserName must be between 5 to 12 characters in length");
            focusView = mUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(userName, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() > 4 && password.length() < 13);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
        mEmailSignInButton.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mEmailSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Long> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String userName, String password) {
            mUsername = userName;
            mPassword = password;
        }

        @Override
        protected Long doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            CustomerModel customerModel = new CustomerModel();
            customerModel.setName(mUsername);
            customerModel.setPass(mPassword);
            return dbHelper.addCustomer(customerModel);

        }

        @Override
        protected void onPostExecute(final Long success) {
            mAuthTask = null;
            showProgress(false);

            if (success == 0 || success == -1) {
                mUserName.setError("Something has wrong, Please try again!");
                Toast.makeText(AddUserActivity.this, "Something has wrong, Please try again!", Toast.LENGTH_SHORT).show();
                mUserName.requestFocus();
            } else if (success == -2) {
                mUserName.setError("User name is already exist");
                Toast.makeText(AddUserActivity.this, "User name is already exist", Toast.LENGTH_SHORT).show();
                mUserName.requestFocus();
            } else {
                Toast.makeText(AddUserActivity.this, "User saved successfully!!", Toast.LENGTH_SHORT).show();
                setResult(102);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

