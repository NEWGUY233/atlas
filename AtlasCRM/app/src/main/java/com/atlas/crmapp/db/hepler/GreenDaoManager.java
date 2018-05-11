package com.atlas.crmapp.db.hepler;

import com.atlas.crmapp.Atlas;
import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.greendao.DaoMaster;
import com.atlas.crmapp.db.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by hoda on 2017/7/5.
 */

public class GreenDaoManager
{
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private GreenDaoManager()
    {
        init();
    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder
    {
        private static final GreenDaoManager INSTANCE = new GreenDaoManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static GreenDaoManager getInstance()
    {
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 初始化数据
     */
    private void init()
    {
        boolean isDebug = !GlobalParams.production;
        QueryBuilder.LOG_SQL = isDebug;
        QueryBuilder.LOG_VALUES = isDebug;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(Atlas.getAppContext(), Constants.DataBaseInfo.DB_NAME);
        mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoMaster getmDaoMaster()
    {
        return mDaoMaster;
    }
    public DaoSession getmDaoSession()
    {
        return mDaoSession;
    }
    public DaoSession getNewSession()
    {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}