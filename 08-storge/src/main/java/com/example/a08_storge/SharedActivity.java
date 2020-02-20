package com.example.a08_storge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SharedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);

        final EditText accEdt = findViewById(R.id.acc_edt);
        final EditText pwdEdt = findViewById(R.id.pwd_edt);

        /**
         * 存储
         */
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accEdt.getText().toString();
                String passwd = pwdEdt.getText().toString();

                if (account.equals("admin") && passwd.equals("123")) {

                    // 1.获取SharedPreference对象
                    // 参数1：存储的xml文件的名称，参数2：存储类型
                    SharedPreferences share = getSharedPreferences("myshare", MODE_PRIVATE);

                    // 2.获取Edit对象
                    SharedPreferences.Editor edt = share.edit();

                    // 3.存储信息
                    edt.putString("account", account);
                    edt.putString("passwd", passwd);

                    // 4.提交
                    edt.commit();

                    Toast.makeText(SharedActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SharedActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 读取
         */
        SharedPreferences share = getSharedPreferences("myshare", MODE_PRIVATE);

        // 参数1：key，参数2：当key不存在的时候返回的值
        String account = share.getString("account", "");
        final String passwd = share.getString("passwd", "");

        accEdt.setText(account);
        pwdEdt.setText(passwd);


        findViewById(R.id.clean_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences share = getSharedPreferences("myshare", MODE_PRIVATE);
                SharedPreferences.Editor edt = share.edit();
                edt.clear();
                edt.commit();
                Toast.makeText(SharedActivity.this, "保存的登录信息已删除", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
