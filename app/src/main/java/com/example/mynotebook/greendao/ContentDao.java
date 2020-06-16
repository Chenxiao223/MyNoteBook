package com.example.mynotebook.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.mynotebook.been.Content;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTENT".
*/
public class ContentDao extends AbstractDao<Content, Long> {

    public static final String TABLENAME = "CONTENT";

    /**
     * Properties of entity Content.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Userid = new Property(1, Long.class, "userid", false, "USERID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property ImagePath = new Property(4, String.class, "imagePath", false, "IMAGE_PATH");
        public final static Property Flag = new Property(5, int.class, "flag", false, "FLAG");
        public final static Property Time = new Property(6, String.class, "time", false, "TIME");
        public final static Property TimeStamp = new Property(7, String.class, "timeStamp", false, "TIME_STAMP");
        public final static Property Date = new Property(8, String.class, "date", false, "DATE");
    }


    public ContentDao(DaoConfig config) {
        super(config);
    }
    
    public ContentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USERID\" INTEGER," + // 1: userid
                "\"TITLE\" TEXT," + // 2: title
                "\"CONTENT\" TEXT," + // 3: content
                "\"IMAGE_PATH\" TEXT," + // 4: imagePath
                "\"FLAG\" INTEGER NOT NULL ," + // 5: flag
                "\"TIME\" TEXT," + // 6: time
                "\"TIME_STAMP\" TEXT," + // 7: timeStamp
                "\"DATE\" TEXT);"); // 8: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Content entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userid = entity.getUserid();
        if (userid != null) {
            stmt.bindLong(2, userid);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String imagePath = entity.getImagePath();
        if (imagePath != null) {
            stmt.bindString(5, imagePath);
        }
        stmt.bindLong(6, entity.getFlag());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
 
        String timeStamp = entity.getTimeStamp();
        if (timeStamp != null) {
            stmt.bindString(8, timeStamp);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(9, date);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Content entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userid = entity.getUserid();
        if (userid != null) {
            stmt.bindLong(2, userid);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String imagePath = entity.getImagePath();
        if (imagePath != null) {
            stmt.bindString(5, imagePath);
        }
        stmt.bindLong(6, entity.getFlag());
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(7, time);
        }
 
        String timeStamp = entity.getTimeStamp();
        if (timeStamp != null) {
            stmt.bindString(8, timeStamp);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(9, date);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Content readEntity(Cursor cursor, int offset) {
        Content entity = new Content( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // content
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // imagePath
            cursor.getInt(offset + 5), // flag
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // time
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // timeStamp
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Content entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserid(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImagePath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFlag(cursor.getInt(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTimeStamp(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDate(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Content entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Content entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Content entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
