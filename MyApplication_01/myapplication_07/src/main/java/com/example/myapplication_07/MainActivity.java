package com.example.myapplication_07;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 获取输入的用户名
                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                // 获取输入的密码
                String password = ((EditText) findViewById(R.id.password1)).getText().toString();
                // 获取输入的确认密码
                String repassword = ((EditText) findViewById(R.id.password2)).getText().toString();
                // 获取输入的E-mail地址
                String email = ((EditText) findViewById(R.id.email)).getText().toString();

                if (!"".equals(username) && !"".equals(password) && !"".equals(email)) {
                    if (!password.equals(repassword)) {
                        Toast.makeText(MainActivity.this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_LONG).show();
                        // 清空"密码"编辑框
                        ((EditText) findViewById(R.id.password1)).setText("");
                        // 清空"确认密码"编辑框
                        ((EditText) findViewById(R.id.password2)).setText("");
                        // 让"密码"编辑框获得焦点
                        ((EditText) findViewById(R.id.password1)).requestFocus();
                    } else {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        Bundle bundle = new Bundle();
                        // 保存用户名
                        bundle.putCharSequence("username", username);
                        // 保存密码
                        bundle.putCharSequence("password", password);
                        // 保存确认密码
                        bundle.putCharSequence("repassword", repassword);
                        // 保存E-mail地址
                        bundle.putCharSequence("email", email);
                        // 将Bundle对象添加到Intent对象中
                        intent.putExtras(bundle);
                        //跳转至注册信息页面
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "请将注册信息输入完整！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
