package com.example.mynotebook.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynotebook.MyApplication;
import com.example.mynotebook.R;
import com.example.mynotebook.been.User;
import com.example.mynotebook.greendao.UserDao;
import com.example.mynotebook.util.TwCog;

import java.util.List;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private TextView tv_register;
    private EditText et_name;
    private EditText et_pwd;
    public PromptDialog promptDialog;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initImmersionBar(view);
        initView();
        initListener();
    }

    private void initView() {
        promptDialog = new PromptDialog(this);

        tv_register = findViewById(R.id.tv_register);
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        view = findViewById(R.id.view);

    }

    public String getName() {
        return et_name.getText().toString().trim();
    }

    public String getPwd() {
        return et_pwd.getText().toString().trim();
    }

    private void initListener() {
        //注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        //登录
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(getName()) && !TextUtils.isEmpty(getPwd())) {
                    promptDialog.showLoading("正在登陆");
                    List<User> list = MyApplication.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(getName())).list();
                    if (list.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "无此账号", Toast.LENGTH_SHORT).show();
                        promptDialog.dismiss();
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getPwd().equals(getPwd())) {
                            TwCog.UserName = list.get(i).getName();
                            TwCog.UserId = list.get(i).getId();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请输入账号密码", Toast.LENGTH_SHORT).show();
                }
                promptDialog.dismiss();
            }
        });
    }
}
