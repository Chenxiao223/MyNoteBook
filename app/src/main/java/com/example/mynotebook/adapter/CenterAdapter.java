package com.example.mynotebook.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mynotebook.R;
import com.example.mynotebook.been.Content;


public class CenterAdapter extends BaseQuickAdapter<Content, BaseViewHolder> {
    int pageNo = 1;

    public CenterAdapter(int layoutResId) {
        super(layoutResId);
    }


    public int fristPagNo() {

        pageNo = 1;

        return pageNo;
    }

    public int nextPageNo() {

        ++pageNo;

        return pageNo;
    }

    @Override
    protected void convert(BaseViewHolder helper, Content item) {
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());
        helper.setText(R.id.tv_time, item.getTime());
        switch (item.getFlag()) {
            case 1:
                helper.setText(R.id.tv_flag, "生活");
                break;
            case 2:
                helper.setText(R.id.tv_flag, "学习");
                break;
            case 3:
                helper.setText(R.id.tv_flag, "工作");
                break;
            case 4:
                helper.setText(R.id.tv_flag, "已完成");
                break;
        }

        helper.addOnClickListener(R.id.tv_alter);
        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_completed);
        helper.addOnClickListener(R.id.ly_item);
    }

}
