package multiplexer.lab.takeout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import multiplexer.lab.takeout.R;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SignUpActivity extends AppCompatActivity {

    EditText etFullname, etEmail, etPassword, etConPassword, etPhone;
    RadioButton rMale, rFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etFullname = findViewById(R.id.et_signup_fullname);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        etConPassword = findViewById(R.id.et_signup_conpassword);
        etPhone = findViewById(R.id.et_signup_phoneno);
        rMale = findViewById(R.id.Radiobtnmale);
        rFemale = findViewById(R.id.Radiobtnfemale);
    }

    public void btnSignUp(View view) {
        if (validation()) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            YoYo.with(Techniques.Shake)
                    .duration(1000)
                    .repeat(1)
                    .playOn(findViewById(R.id.ll_signup));
        }
    }

    private boolean validation() {
        boolean error = true;
        etFullname.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etConPassword.setError(null);
        etPhone.setError(null);

        String fullname = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String conpassword = etConPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullname.isEmpty()) {
            etFullname.setError("Name is missing");
            error = false;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email Address is missing");
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
        if (conpassword.isEmpty()) {
            etConPassword.setError("Confirm Password is Empty");
            error = false;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Need your Phoneno");
            error = false;
        } else if (phone.length() != 11) {
            etPhone.setError("Enter a valid Phone No.");
            error = false;
        }
        if (!password.equalsIgnoreCase(conpassword)) {
            etConPassword.setError("Password did not match");
            error = false;
        }
        return error;

    }

}
