package com.example.mynotebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mynotebook.MyApplication;
import com.example.mynotebook.R;
import com.example.mynotebook.adapter.CenterAdapter;
import com.example.mynotebook.been.Content;
import com.example.mynotebook.greendao.ContentDao;
import com.example.mynotebook.util.TwCog;
import com.example.mynotebook.view.SlideRecyclerView;

import java.util.ArrayList;

/**
 * 日历查询
 */
public class CalendarActivity extends BaseActivity {
    private View view;
    private TextView tv_title;
    private CalendarView calendarView;
    private SlideRecyclerView recyclerView;
    private CenterAdapter adapter;
    private ContentDao contentDao;
    private ArrayList<Content> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        contentDao = MyApplication.getInstance().getDaoSession().getContentDao();
        initView();
        initListener();
    }

    private void initView() {
        view = findViewById(R.id.view);
        initImmersionBar(view);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("日历查询");
        calendarView = findViewById(R.id.calendarView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CenterAdapter(R.layout.item_content);
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String str = String.valueOf(month + 1).length() == 1 ? "0" + (month + 1) : String.valueOf(month + 1);
                String time = year + "-" + str + "-" + dayOfMonth;
                list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId), ContentDao.Properties.Date.eq(time)).orderDesc(ContentDao.Properties.Time).list();
                adapter.setNewData(list);
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Content content = (Content) adapter.getItem(position);
                Intent intent;
                switch (view.getId()) {
                    case R.id.tv_alter:
                        if (content.getFlag() == 4) {
                            Toast.makeText(CalendarActivity.this, "已完成无法修改", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent = new Intent(CalendarActivity.this, EditorialsActivity.class);
                        intent.putExtra("title", "修改备忘录");
                        intent.putExtra("id", content.getId());
                        startActivity(intent);
                        break;
                    case R.id.tv_delete:
                        contentDao.deleteByKey(content.getId());
                        list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId)).orderDesc(ContentDao.Properties.Time).list();
                        adapter.setNewData(list);
                        adapter.notifyDataSetChanged();
                        break;
                    case R.id.ly_item:
                        intent = new Intent(CalendarActivity.this, DetailsActivity.class);
                        intent.putExtra("id", content.getId());
                        startActivity(intent);
                        break;
                    case R.id.tv_completed:
                        if (content.getFlag() == 4) {
                            Toast.makeText(CalendarActivity.this, "已完成", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        content.setFlag(4);
                        contentDao.update(content);
                        list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId)).orderDesc(ContentDao.Properties.Time).list();
                        adapter.setNewData(list);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }
}
