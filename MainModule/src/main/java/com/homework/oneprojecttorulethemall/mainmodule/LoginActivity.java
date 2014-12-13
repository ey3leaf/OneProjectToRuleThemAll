package com.homework.oneprojecttorulethemall.mainmodule;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            UserSingleton.getInstance().setUser(currentUser);
            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            this.finish();
        }
    }

    public void login(View v) {
        final Dialog loginDialog = new Dialog(this);
        loginDialog.setContentView(R.layout.login);
        loginDialog.setTitle("Login");

        final Button loginButton = (Button) loginDialog.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                pd.setTitle("Login");
                pd.setMessage("Logging in...");
                pd.show();

                EditText login = (EditText) loginDialog.findViewById(R.id.loginField);
                EditText password = (EditText) loginDialog.findViewById(R.id.passwordField);

                ParseUser.logInInBackground(login.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e == null) {
                            UserSingleton.getInstance().setUser(parseUser);
                            pd.cancel();
                            loginDialog.cancel();
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            LoginActivity.this.finish();
                        } else {
                            pd.cancel();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        Button cancelButton = (Button) loginDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.cancel();
            }
        });
        loginDialog.show();
    }

    public void signup(View v) {
        final Dialog signUpDialog = new Dialog(this);
        signUpDialog.setContentView(R.layout.signup);
        signUpDialog.setTitle("Sign Up");

        Button signUpButton = (Button) signUpDialog.findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Signing Up...");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                EditText login = (EditText) signUpDialog.findViewById(R.id.loginField);
                EditText email = (EditText) signUpDialog.findViewById(R.id.emailField);
                EditText password = (EditText) signUpDialog.findViewById(R.id.passwordField);
                EditText confPassword = (EditText) signUpDialog.findViewById(R.id.confirmPasswordField);

                if (password.getText().toString().equals(confPassword.getText().toString())) {
                    final ParseUser user = new ParseUser();
                    user.setUsername(login.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(password.getText().toString());
                    if (login.getText().toString().equals("") ||
                            login.getText().toString().equals("") ||
                            login.getText().toString().equals("") ||
                            login.getText().toString().equals("")) {
                        progressDialog.cancel();
                        Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                    } else {
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    UserSingleton.getInstance().setUser(user);
                                    Toast.makeText(getApplicationContext(), "Welcome to Her S Goru Solutions!", Toast.LENGTH_LONG).show();
                                    progressDialog.cancel();
                                    signUpDialog.cancel();
                                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                    //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println();
                                    progressDialog.cancel();
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Different passwords. Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancelButton = (Button) signUpDialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpDialog.cancel();
            }
        });
        signUpDialog.show();
    }

}
