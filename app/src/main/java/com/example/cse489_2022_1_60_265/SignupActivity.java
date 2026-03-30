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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    private Button btnLogin, btnExit, btnGo;
    private CheckBox cbRemLogin, cbRemUSer;
    private EditText etName, etMail, etPass, etRePass;

    private TextView tvError;

    //decalre sharedPreferences here
    private SharedPreferences sp;
    Handler handler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Open SharedPreferences files here
        sp = this.getSharedPreferences("rifa_azad", MODE_PRIVATE);
        //check if profile already exists
        String userId = sp.getString("_USER_ID_", "");
            //if exists then
        if(!userId.isEmpty()){ //profile created
            boolean isLoginRemembered = sp.getBoolean("_LOGIN_REMEM", false);
            //check if logIn remembered
            if(isLoginRemembered) {
                System.out.println("Moving to Home page.....");


//        Move to home Page
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("_USER_ID", userId);
                startActivity(i);
                finishAffinity();
            }
            else {
                //get password
//           send user id & password to login page
                boolean isUserRemembered = sp.getBoolean("_USER_REMEM", false);
                String pass = sp.getString("_PASS_", "");
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("_USER_ID_", userId);
                i.putExtra("_USER_REMEM", isUserRemembered);
                i.putExtra("_PASS_", pass);
                Toast.makeText(this, "User remembered thus staying in log in page!", Toast.LENGTH_SHORT).show();

                startActivity(i);
                finish(); //to remove from stack
            }
            return; //end of onCreate, onStart started
        }


        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        cbRemLogin = findViewById(R.id.cbRemLogin);
        cbRemUSer = findViewById(R.id.cbRemUser);
        tvError = findViewById(R.id.tvError);


        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Login button has been Clicked");

                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
//            handover this object to android OS
                Toast.makeText(SignupActivity.this, "No user yet thus can move to sign up page!", Toast.LENGTH_SHORT).show();
                startActivity(i);
//                finish(); //remove all activity of thid page
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
    }

    private void getFieldValues(){
        new Thread(() -> {
            String name = etName.getText().toString().trim();
            String email = etMail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();
            String rePass = etRePass.getText().toString().trim();

            ArrayList<String> errors = new ArrayList<>();

            if (name.length() < 5) {
                errors.add("Name length must be greater than 5.");
            }
            if (!isValidEmailAddress(email)) {
                errors.add("Email is not valid.");
            }
            if (pass.length() < 4) {
                errors.add("Password must be at least 4 characters long.");
            }
            if (!pass.equals(rePass)) {
                errors.add("Passwords do not match.");
            }

            if (!errors.isEmpty()) {
                handler.post(() -> {
                    StringBuilder sb = new StringBuilder();
                    for (String err : errors) {
                        sb.append("• ").append(err).append("\n");
                    }
                    tvError.setText(sb.toString().trim());
                    tvError.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please fix the errors", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            boolean shouldRememberUser = cbRemUSer.isChecked();
            boolean shouldRememberLogin = cbRemLogin.isChecked();

            // store all information in SharedPreferences
            SharedPreferences.Editor prEdit = sp.edit();
            prEdit.putString("_USER_ID_", email);
            prEdit.putString("_PASS_", pass);
            prEdit.putBoolean("_LOGIN_REMEM", shouldRememberLogin);
            prEdit.putBoolean("_USER_REMEM", shouldRememberUser);
            prEdit.apply(); //without it code won't be written

            handler.post(() -> {
                Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("_USER_ID_", email);
                i.putExtra("_PASS_", pass);
                i.putExtra("_USER_REMEM", shouldRememberUser);
                startActivity(i);
                finishAffinity();
            });
        }).start();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}