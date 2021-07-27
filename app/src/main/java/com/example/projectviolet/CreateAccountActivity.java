package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG = "CreateAccountActivity";

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

        ParseUser user = new ParseUser();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!lengthVerified()){
                    Toast.makeText(CreateAccountActivity.this, "Username/Password must be at least 3 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        // On success for Parse, e is null
                        if (e == null) {
                            goMainActivity();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Log.e(TAG, "done: ", e);
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

    private boolean lengthVerified() {

        if(etUsername.getText().toString().length() < 3){
            return false;
        }
        if(etPassword.getText().toString().length() < 3){
            return false;
        }
        else
        return true;
    }
}