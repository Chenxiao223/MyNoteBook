package com.example.mynotebook.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Content {
    @Id(autoincrement = true)
    private Long id;
    private Long userid;
    private String title;
    private String content;
    private String imagePath;
    private int flag;
    private String time;
    private String timeStamp;
    private String date;
    @Generated(hash = 1228327582)
    public Content(Long id, Long userid, String title, String content,
            String imagePath, int flag, String time, String timeStamp,
            String date) {
        this.id = id;
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.flag = flag;
        this.time = time;
        this.timeStamp = timeStamp;
        this.date = date;
    }
    @Generated(hash = 940998559)
    public Content() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserid() {
        return this.userid;
    }
    public void setUserid(Long userid) {
        this.userid = userid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public int getFlag() {
        return this.flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getTimeStamp() {
        return this.timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    

}
