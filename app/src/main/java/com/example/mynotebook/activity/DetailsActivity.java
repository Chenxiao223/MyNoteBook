package com.example.mynotebook.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mynotebook.MyApplication;
import com.example.mynotebook.R;
import com.example.mynotebook.been.Content;
import com.example.mynotebook.greendao.ContentDao;
import com.example.mynotebook.util.TwCog;

import java.util.ArrayList;

/**
 * 详情页
 */
public class DetailsActivity extends BaseActivity {
    private View view;
    private TextView tv_title;
    private TextView tv_titles;
    private TextView tv_content;
    private TextView tv_time;
    private TextView tv_flag;
    private TextView tv_time_stamp;
    private ImageView image;
    private Long id;
    private ContentDao contentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        contentDao = MyApplication.getInstance().getDaoSession().getContentDao();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        view = findViewById(R.id.view);
        initImmersionBar(view);
        id = getIntent().getExtras().getLong("id");
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("详情");
        tv_titles = findViewById(R.id.tv_titles);
        tv_content = findViewById(R.id.tv_content);
        tv_time = findViewById(R.id.tv_time);
        tv_flag = findViewById(R.id.tv_flag);
        tv_time_stamp = findViewById(R.id.tv_time_stamp);
        image = findViewById(R.id.image);
    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        ArrayList<Content> list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId), ContentDao.Properties.Id.eq(id)).list();
        Content content = list.get(0);
        tv_titles.setText(content.getTitle());
        tv_content.setText(content.getContent());
        tv_time.setText(content.getTime());
        tv_time_stamp.setText(content.getTimeStamp());
        String path = content.getImagePath();
        if (!TextUtils.isEmpty(path)) {
            Glide.with(this).asBitmap().load(path)
                    .thumbnail(0.5f)
                    .into(image);
        } else {
            image.setVisibility(View.GONE);
        }
        int type = content.getFlag();
        switch (type) {
            case 1:
                tv_flag.setText("生活");
                break;
            case 2:
                tv_flag.setText("学习");
                break;
            case 3:
                tv_flag.setText("工作");
                break;
            case 4:
                tv_flag.setText("已完成");
                break;
        }

    }
}
