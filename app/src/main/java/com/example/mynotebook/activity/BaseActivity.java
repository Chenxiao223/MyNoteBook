package com.example.mynotebook.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initImmersionBar(View view) {
        ImmersionBar.with(this)
                .statusBarView(view)
                .autoDarkModeEnable(true)
                .init();
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = "";
        if (!"".equals(s)) {
            try {
                res = String.valueOf(sdf.parse(s).getTime() / 1000);
            } catch (Exception e) {
            }
        } else {
            long time = System.currentTimeMillis();
            res = String.valueOf(time / 1000);
        }

        return res;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(int time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date(time * 1000L));
//	    System.out.println("日期格式---->" + times);
        return times;
    }

}
