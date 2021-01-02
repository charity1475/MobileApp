package com.anga.alcoholcompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordLayout;
    private ProgressBar progressBar;
    private AppPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AppPreferences(this);
        if (preferences.isLoggedIn()){
            startMainActivity();
            finish();
        }
        setContentView(R.layout.activity_login);
        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordLayout = findViewById(R.id.textPasswordInput);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.onLoginClicked();
            }
        });
        Objects.requireNonNull(textUsernameLayout.getEditText())
                .addTextChangedListener(createTextWatcher(textUsernameLayout));
        Objects.requireNonNull(textPasswordLayout.getEditText())
                .addTextChangedListener(createTextWatcher(textPasswordLayout));

        progressBar = findViewById(R.id.progressBar);
    }

    private TextWatcher createTextWatcher(final TextInputLayout textPasswordInput){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textPasswordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }
    public void onLoginClicked(){
        String username = textUsernameLayout.getEditText().getText().toString();
        String password = textPasswordLayout.getEditText().getText().toString();
        if (username.isEmpty()){
            textUsernameLayout.setError("User name can't be empty");
        }else if (password.isEmpty()){
            textPasswordLayout.setError("Password can't be empty");
        }else if (!username.equals("charitymbisi@gmail.com") || !password.equals("1475charity")){
            showErrorDialog();
        }else {
            performLogin();
        }
    }

    private void performLogin() {
        preferences.setLoggedIn(true);
        textUsernameLayout.setEnabled(false);
        textPasswordLayout.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginActivity.this.startMainActivity();
                LoginActivity.this.finish();
            }
        }, 2000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("ok",(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }))
                .show();
    }

}
