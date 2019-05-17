package com.example.myapplication_07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        TextView username = (TextView) findViewById(R.id.show_username);
        username.setText("用户名：" + bundle.getString("username"));

        TextView password = (TextView) findViewById(R.id.show_password);
        password.setText("密码：" + bundle.getString("password"));

        TextView email = (TextView) findViewById(R.id.show_email);
        email.setText("E-mail：" + bundle.getString("email"));
    }
}
