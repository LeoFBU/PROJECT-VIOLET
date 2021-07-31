package com.example.projectviolet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity.java:";

    private Button btnLogin;
    EditText etUsername;
    EditText etPassword;
    private TextView tvCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        if( ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button pressed");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCreateAccountActivity();
            }
        });
    }

    private void loginUser(String username, String password){

        Log.i(TAG, "Attempting to login user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with login\nInvalid Username and Password",e);
                    Toast.makeText(LoginActivity.this, "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // Nothing here in order to disable back button upon logout
    }

    private void goMainActivity() {

        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    private void goCreateAccountActivity(){
        Intent i = new Intent(this, CreateAccountActivity.class);
        finish();
        startActivity(i);
    }


}