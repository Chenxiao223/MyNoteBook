package com.example.mynotebook.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.mynotebook.MyApplication;
import com.example.mynotebook.R;
import com.example.mynotebook.been.Content;
import com.example.mynotebook.greendao.ContentDao;
import com.example.mynotebook.util.TwCog;
import com.example.mynotebook.view.CustomDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxFileTool;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.view.RxToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.leefeng.promptlibrary.PromptDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.vondear.rxtool.RxPhotoTool.GET_IMAGE_BY_CAMERA;
import static com.vondear.rxtool.RxPhotoTool.GET_IMAGE_FROM_PHONE;

/**
 * 新增或修改
 */
public class EditorialsActivity extends BaseActivity implements View.OnClickListener {
    private View view;
    private EditText et_title;
    private TextView tv_text_right;
    private TextView tv_title;
    public PromptDialog promptDialog;
    private ImageView iv_back;
    private TextView et_content;
    private ImageView image;
    private CustomDialog photo_dialog;
    private RxPermissions rxPermissions;
    private TextView tv_live;
    private TextView tv_study;
    private TextView tv_work;
    private String photo_path;
    private int flag = 0;
    private Long id;
    private ContentDao contentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorials);
        contentDao = MyApplication.getInstance().getDaoSession().getContentDao();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        if (id != 0) {
            ArrayList<Content> list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId), ContentDao.Properties.Userid.eq(TwCog.UserId), ContentDao.Properties.Id.eq(id)).list();
            Content content = list.get(0);
            et_title.setText(content.getTitle());
            et_content.setText(content.getContent());
            int type = content.getFlag();
            changeState(flag = type);
            String path = content.getImagePath();
            if (!TextUtils.isEmpty(path)) {
                Glide.with(this).asBitmap().load(path)
                        .thumbnail(0.5f)
                        .into(image);
            }
        }
    }

    private void initView() {
        rxPermissions = new RxPermissions(this);
        promptDialog = new PromptDialog(this);
        id = getIntent().getExtras().getLong("id");

        view = findViewById(R.id.view);
        initImmersionBar(view);
        tv_title = findViewById(R.id.tv_title);
        tv_text_right = findViewById(R.id.tv_text_right);
        iv_back = findViewById(R.id.iv_back);
        et_content = findViewById(R.id.et_content);
        image = findViewById(R.id.image);
        tv_live = findViewById(R.id.tv_live);
        tv_study = findViewById(R.id.tv_study);
        tv_work = findViewById(R.id.tv_work);
        et_title = findViewById(R.id.et_title);

        tv_title.setText(getIntent().getExtras().getString("title"));
        tv_text_right.setVisibility(View.VISIBLE);
        tv_text_right.setText("保存");
    }

    private void initListener() {
        tv_text_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        image.setOnClickListener(this);
        tv_live.setOnClickListener(this);
        tv_study.setOnClickListener(this);
        tv_work.setOnClickListener(this);
    }

    public String getContent() {
        return et_content.getText().toString().trim();
    }

    public String gettitle() {
        return et_title.getText().toString().trim();
    }

    public String getTime() {
        long currentTime = System.currentTimeMillis();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
    }

    public String getDate() {
        long currentTime = System.currentTimeMillis();
        return new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_text_right:
                if (TextUtils.isEmpty(gettitle())) {
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(getContent())) {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (flag == 0) {
                    Toast.makeText(this, "请选择标签", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id != 0) {
                    Content content = new Content(id, TwCog.UserId, gettitle(), getContent(), photo_path, flag, getTime(), dateToStamp(getTime()), getDate());
                    contentDao.update(content);
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Content content = new Content(null, TwCog.UserId, gettitle(), getContent(), photo_path, flag, getTime(), dateToStamp(getTime()), getDate());
                    contentDao.insert(content);
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
            case R.id.image:
                showPhotoDialog();
                break;
            case R.id.tv_live:
                changeState(flag = 1);
                break;
            case R.id.tv_study:
                changeState(flag = 2);
                break;
            case R.id.tv_work:
                changeState(flag = 3);
                break;
        }
    }

    public void changeState(int i) {
        switch (i) {
            case 1:
                tv_live.setBackgroundResource(R.drawable.recharge_shape);
                tv_study.setBackgroundResource(R.drawable.recharge_shape2);
                tv_work.setBackgroundResource(R.drawable.recharge_shape2);
                break;
            case 2:
                tv_live.setBackgroundResource(R.drawable.recharge_shape2);
                tv_study.setBackgroundResource(R.drawable.recharge_shape);
                tv_work.setBackgroundResource(R.drawable.recharge_shape2);
                break;
            case 3:
                tv_live.setBackgroundResource(R.drawable.recharge_shape2);
                tv_study.setBackgroundResource(R.drawable.recharge_shape2);
                tv_work.setBackgroundResource(R.drawable.recharge_shape);
                break;
        }
    }

    public void showPhotoDialog() {
        if (null == photo_dialog) {
            photo_dialog = new CustomDialog(this, R.layout.photo_dialog, new int[]{R.id.tv_camera, R.id.tv_photo, R.id.tv_cancel}, false, Gravity.BOTTOM);
            photo_dialog.setOnDialogItemClickListener(new CustomDialog.OnCustomDialogItemClickListener() {
                @Override
                public void OnCustomDialogItemClick(CustomDialog dialog, View view) {
                    switch (view.getId()) {
                        case R.id.tv_cancel:
                            photo_dialog.dismiss();
                            break;
                        case R.id.tv_camera:
                            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                                if (aBoolean) {
                                    RxPhotoTool.openCameraImage(EditorialsActivity.this);
                                    photo_dialog.dismiss();
                                }
                            });

                            break;

                        case R.id.tv_photo:
                            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                                if (aBoolean) {
                                    RxPhotoTool.openLocalImage(EditorialsActivity.this);
                                    photo_dialog.dismiss();
                                }
                            });
                            break;
                    }
                }
            });
        }
        photo_dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_IMAGE_BY_CAMERA:

                    String dz = RxPhotoTool.getRealFilePath(this, RxPhotoTool.imageUriFromCamera);

                    Luban.with(this).load(dz).ignoreBy(100).setTargetDir(RxFileTool.getSDCardPath()).filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))).setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(File file) {

                            if (file.exists()) {
                                if (file.exists()) {
                                    photo_path = file.getPath();
                                    Glide.with(EditorialsActivity.this).load(photo_path).thumbnail(0.5f).into(image);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxToast.error(e.getMessage());
                        }
                    }).launch();

                    break;

                case GET_IMAGE_FROM_PHONE://图库

                    if (null != data.getData()) {

                        File files = new File(RxPhotoTool.getImageAbsolutePath(this, data.getData()));

                        Luban.with(this).load(files).setTargetDir(RxFileTool.getSDCardPath()).filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))).setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onSuccess(File file) {

                                if (file.exists()) {
                                    photo_path = file.getPath();
                                    Glide.with(EditorialsActivity.this).load(photo_path).thumbnail(0.5f).into(image);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error(e.getMessage());
                            }
                        }).launch();
                    }
                    break;
            }
        }
    }
}
