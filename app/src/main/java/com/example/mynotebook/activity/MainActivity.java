package com.example.mynotebook.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mynotebook.MyApplication;
import com.example.mynotebook.R;
import com.example.mynotebook.adapter.CenterAdapter;
import com.example.mynotebook.been.Content;
import com.example.mynotebook.been.User;
import com.example.mynotebook.greendao.ContentDao;
import com.example.mynotebook.greendao.UserDao;
import com.example.mynotebook.util.TwCog;
import com.example.mynotebook.view.CustomDialog;
import com.example.mynotebook.view.SlideRecyclerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtool.RxFileTool;
import com.vondear.rxtool.RxPhotoTool;
import com.vondear.rxtool.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leefeng.promptlibrary.PromptDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.vondear.rxtool.RxPhotoTool.GET_IMAGE_BY_CAMERA;
import static com.vondear.rxtool.RxPhotoTool.GET_IMAGE_FROM_PHONE;

/**
 * 主页
 */
public class MainActivity extends BaseActivity {
    private View view;
    private LinearLayout left_layout;
    private DrawerLayout drawerLayout;
    private TextView tv_username;
    private ContentDao contentDao;
    private UserDao userDao;
    private SlideRecyclerView recyclerView;
    private CenterAdapter adapter;
    public PromptDialog promptDialog;
    private EditText et_search;
    private CircleImageView iv_head;
    private CustomDialog photo_dialog;
    private RxPermissions rxPermissions;
    private String photo_path;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        promptDialog = new PromptDialog(this);
        contentDao = MyApplication.getInstance().getDaoSession().getContentDao();
        userDao = MyApplication.getInstance().getDaoSession().getUserDao();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        List<User> list = userDao.queryBuilder().where(UserDao.Properties.Name.eq(TwCog.UserName)).list();
        mUser = list.get(0);
        String psth = mUser.getImagePath();
        if (!TextUtils.isEmpty(psth)) {
            Glide.with(this).asBitmap().load(psth)
                    .thumbnail(0.5f)
                    .into(iv_head);
        }
    }

    private void initView() {
        rxPermissions = new RxPermissions(this);
        view = findViewById(R.id.view);
        initImmersionBar(view);

        left_layout = findViewById(R.id.left_layout);
        drawerLayout = findViewById(R.id.drawerLayout);
        tv_username = findViewById(R.id.tv_username);
        tv_username.setText(TwCog.UserName);
        et_search = findViewById(R.id.et_search);
        iv_head = findViewById(R.id.iv_head);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CenterAdapter(R.layout.item_content);
        recyclerView.setAdapter(adapter);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//禁止手势滑动

    }

    public void showLoad() {
        promptDialog.showLoading("请稍后");
    }

    public void closeLoad() {
        promptDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSearch(1);
    }

    public void getSearch(int type) {
        try {
            showLoad();
            ArrayList<Content> list = new ArrayList<>();
            switch (type) {
                case 1://查全部
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId)).orderDesc(ContentDao.Properties.Time).list();
                    break;
                case 2://查生活
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId), ContentDao.Properties.Flag.eq(1)).orderDesc(ContentDao.Properties.Time).list();
                    break;
                case 3://查学习
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId),ContentDao.Properties.Flag.eq(2)).orderDesc(ContentDao.Properties.Time).list();
                    break;
                case 4://查工作
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId),ContentDao.Properties.Flag.eq(3)).orderDesc(ContentDao.Properties.Time).list();
                    break;
                case 5://查标题
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId),ContentDao.Properties.Title.eq(et_search.getText().toString().trim())).orderDesc(ContentDao.Properties.Time).list();
                    break;
                case 6://查已完成
                    list = (ArrayList<Content>) contentDao.queryBuilder().where(ContentDao.Properties.Userid.eq(TwCog.UserId),ContentDao.Properties.Flag.eq(4)).orderDesc(ContentDao.Properties.Time).list();
                    break;
                default:
                    break;
            }
            closeLoad();
            adapter.setNewData(list);
            adapter.notifyDataSetChanged();
            drawerLayout.closeDrawers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Content content = (Content) adapter.getItem(position);
                Intent intent;
                switch (view.getId()) {
                    case R.id.tv_alter:
                        if (content.getFlag() == 4) {
                            Toast.makeText(MainActivity.this, "已完成无法修改", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent = new Intent(MainActivity.this, EditorialsActivity.class);
                        intent.putExtra("title", "修改备忘录");
                        intent.putExtra("id", content.getId());
                        startActivity(intent);
                        break;
                    case R.id.tv_delete:
                        contentDao.deleteByKey(content.getId());
                        getSearch(1);//删除之后刷新
                        break;
                    case R.id.ly_item:
                        intent = new Intent(MainActivity.this, DetailsActivity.class);
                        intent.putExtra("id", content.getId());
                        startActivity(intent);
                        break;
                    case R.id.tv_completed:
                        if (content.getFlag() == 4) {
                            Toast.makeText(MainActivity.this, "已完成", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        content.setFlag(4);
                        contentDao.update(content);
                        getSearch(1);//刷新
                        break;
                }
            }
        });

        //点击搜索键的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    MainActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    //实现自己的搜索逻辑
                    getSearch(5);
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.ly_completed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch(6);
            }
        });

        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });

        findViewById(R.id.iv_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });

        findViewById(R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorialsActivity.class);
                intent.putExtra("title", "新增备忘录");
                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });

        findViewById(R.id.ly_live).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch(2);
            }
        });

        findViewById(R.id.ly_study).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch(3);
            }
        });

        findViewById(R.id.ly_work).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch(4);
            }
        });

        findViewById(R.id.ly_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch(1);
            }
        });

        findViewById(R.id.ly_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
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
                                    RxPhotoTool.openCameraImage(MainActivity.this);
                                    photo_dialog.dismiss();
                                }
                            });

                            break;

                        case R.id.tv_photo:
                            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                                if (aBoolean) {
                                    RxPhotoTool.openLocalImage(MainActivity.this);
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
                                    Glide.with(MainActivity.this).load(photo_path).thumbnail(0.5f).into(iv_head);
                                    alterUser();
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
                                    Glide.with(MainActivity.this).load(photo_path).thumbnail(0.5f).into(iv_head);
                                    alterUser();
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

    public void alterUser() {
        User user = new User(mUser.getId(), mUser.getName(), mUser.getPwd(), photo_path);
        userDao.update(user);
    }
}
