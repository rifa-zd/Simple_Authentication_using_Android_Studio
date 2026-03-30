package com.example.cse489_2022_1_60_265;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button btnExit, btnReset, btnGo, btnSignup;
    private CheckBox cbRemLogin, cbRemUSer;
    private EditText etMail, etPass;
    private TextView errorMsg;
    private String existingUserId = "", existingPass = "";
    private SharedPreferences sp;
    Handler handler = new Handler(Looper.getMainLooper());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etMail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        errorMsg = findViewById(R.id.tvError);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        cbRemUSer = findViewById(R.id.cbRemUser);

        Intent i = this.getIntent();
        //if find user id then set it in email
        if(i != null){
            if(i.hasExtra("_USER_ID_")){
                existingUserId = i.getStringExtra("_USER_ID_");
            }
            if(i.hasExtra("_PASS_")){
                existingPass = i.getStringExtra("_PASS_");
            }

            System.out.println(existingUserId);
            System.out.println(existingPass);
            if(i.hasExtra("_USER_REMEM")){
                boolean isUserRemember = i.getBooleanExtra("_USER_REMEM", false);
                System.out.println(isUserRemember);
                if(isUserRemember){
                    etMail.setText(existingUserId);
                    cbRemUSer.setChecked(true);
                }

            }
        }

        btnSignup = findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Signup button has been Clicked");

                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
//            handover this object to android OS
                startActivity(i);
            }
        });

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Exit button has been Clicked");
                finishAffinity();
            }
        });

        btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Go button has been Clicked");
                getFieldValues();
            }
        });

        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("reset button has been Clicked");

                clearALlInfo();
                errorMsg.setText("!!! Password Reset!!!");
                errorMsg.setVisibility(View.VISIBLE);

                // Hide it
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        errorMsg.setText("");
                        errorMsg.setVisibility(View.GONE);
                    }
                }, 1500);
            }
        });
    }
    private void getFieldValues() {
        String email = etMail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        ArrayList<String> errors = new ArrayList<>();

        if (!isValidEmailAddress(email)) {
            errors.add("Email not valid");
        }
        if (pass.length() < 4) {
            errors.add("Password must be at least 4 characters long.");
        }

        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String err : errors) {
                sb.append("! ").append(err).append("\n");
            }
            errorMsg.setText(sb.toString().trim());
            errorMsg.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean shouldRememberUser = cbRemUSer.isChecked();
        boolean shouldRememberLogin = cbRemLogin.isChecked();

        if (!email.equals(existingUserId)) {
            errorMsg.setText("Email does not match or User do no Exist.");
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }
        if (!pass.equals(existingPass)) {
            errorMsg.setText("Wrong password");
            errorMsg.setVisibility(View.VISIBLE);
            return;
        }
        sp = this.getSharedPreferences("rifa_azad", MODE_PRIVATE);
        SharedPreferences.Editor prEdit = sp.edit();
        prEdit.putBoolean("_LOGIN_REMEM", shouldRememberLogin);
        prEdit.putBoolean("_USER_REMEM", shouldRememberUser);
        prEdit.apply();

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("_USER_ID", email);
        startActivity(i);
        finishAffinity();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void clearALlInfo() {
//        etMail.setText("");
        etPass.setText("");
        cbRemLogin.setChecked(false);
        errorMsg.setText("");
    }
}