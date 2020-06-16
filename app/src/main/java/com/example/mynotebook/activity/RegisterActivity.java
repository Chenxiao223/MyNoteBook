package com.example.mynotebook.activity;

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

import java.util.List;


/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {
    private TextView tv_title;
    private EditText et_name;
    private EditText et_pwd;
    private EditText et_pwd2;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initListener();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd2 = findViewById(R.id.et_pwd2);
        view = findViewById(R.id.view);
        initImmersionBar(view);
        tv_title.setText("注册");
    }

    public String getName() {
        return et_name.getText().toString().trim();
    }

    public String getPwd() {
        return et_pwd.getText().toString().trim();
    }

    public String getPwd2() {
        return et_pwd2.getText().toString().trim();
    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> list = MyApplication.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq(getName())).list();
                if (!list.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "此账号已存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getName())) {
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getPwd())) {
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getPwd2())) {
                    Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (getPwd().equals(getPwd2())) {
                    User user = new User(null, getName(), getPwd(), null);
                    MyApplication.getInstance().getDaoSession().getUserDao().insert(user);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "密码要一致", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
