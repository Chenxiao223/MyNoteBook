package com.example.mynotebook.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.mynotebook.been.Content;
import com.example.mynotebook.been.User;

import com.example.mynotebook.greendao.ContentDao;
import com.example.mynotebook.greendao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig contentDaoConfig;
    private final DaoConfig userDaoConfig;

    private final ContentDao contentDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        contentDaoConfig = daoConfigMap.get(ContentDao.class).clone();
        contentDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        contentDao = new ContentDao(contentDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Content.class, contentDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        contentDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public ContentDao getContentDao() {
        return contentDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}