package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etUsername = findViewById(R.id.etCreateUsername);
        etPassword = findViewById(R.id.etCreatePassword);
        btnCreateAccount = findViewById(R.id.btnSignInCreate);

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        // Set custom properties
        //user.put("phone", "650-253-0000");
        // Invoke signUpInBackground
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            goMainActivity();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                        }
                    }
                });
            }
        });



    }


    private void goMainActivity() {

        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }
}