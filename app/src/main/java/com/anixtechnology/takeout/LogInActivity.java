package com.anixtechnology.takeout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LogInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    LinearLayout loginLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        loginLayout = findViewById(R.id.LL_input);
    }


    public void btnLogIn(View view) {
        if (validation()) {
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
        }else {
            YoYo.with(Techniques.Shake)
                    .duration(1000)
                    .repeat(1)
                    .playOn(loginLayout);
        }
    }

    public void btnSignUp(View view) {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public boolean validation() {
        boolean error = true;
        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email Address is missing!");
            error = false;
        } else if (!email.endsWith(".com")) {
            etEmail.setError("Enter a valid Email Address");
            error = false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Neeed a Password");
            error = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password is too SHORT!!");
            error = false;
        }
        return error;

    }
}
