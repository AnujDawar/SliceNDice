package com.example.anujdawar.slicendice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    protected Button login;
    protected EditText username;
    protected EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        checkDetails();
    }

    private void checkDetails() {

        login = (Button) findViewById(R.id.loginButton);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        setLoginOnClickListener();
    }

    private void setLoginOnClickListener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("sliceice") && password.getText().toString().equals("12flavors"))
                {
                    Intent configIntent = new Intent("com.example.anujdawar.slicendice.configClass");
                    startActivity(configIntent);
                    finish();
                }

                else
                    Toast.makeText(getApplication(), "INVALID details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
