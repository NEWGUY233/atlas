package com.atlas.crmapp.db.hepler;

import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.db.greendao.ChatCountDao;
import com.atlas.crmapp.db.model.ChatCount;
import com.atlas.crmapp.util.ContextUtil;
import com.atlas.crmapp.util.SpUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */

public class ChatCountHelper {

    public static long getUnread(String identify){
        ChatCount bean = exist(identify);
        if (bean == null)
            return 0;
        else
            return bean.getUnRead();
    }

    public static void update(String identify,long unread){
        if (!GlobalParams.getInstance().isLogin())
            return;

        ChatCount bean = exist(identify);
        if (bean == null){
            bean = new ChatCount();
            bean.setIdentify(identify);
            bean.setUnRead(unread);
            bean.setUserId(String.valueOf(GlobalParams.getInstance().getPersonInfoJson().getId()));
            GreenDaoManager.getInstance().getmDaoSession().getChatCountDao().insert(bean);
        }else {
            bean.setUnRead(unread);
            GreenDaoManager.getInstance().getmDaoSession().getChatCountDao().update(bean);
        }
    }

    public static void remove(String identify){
        ChatCount bean = exist(identify);
        if (bean == null){
            bean = new ChatCount();
            bean.setIdentify(identify);
            bean.setUnRead(0);
            GreenDaoManager.getInstance().getmDaoSession().getChatCountDao().insert(bean);
        }else {
            bean.setUnRead(0);
            GreenDaoManager.getInstance().getmDaoSession().getChatCountDao().update(bean);
        }
    }

    public static ChatCount exist(String identify){
        List<ChatCount> list = GreenDaoManager.getInstance().getmDaoSession().getChatCountDao().queryBuilder().where(ChatCountDao.Properties.Identify.eq(identify)
                ,ChatCountDao.Properties.UserId.eq(String.valueOf(SpUtil.getLong(ContextUtil.getUtil().getContext(),SpUtil.ID,0)))).list();
        if (list == null || list.size() == 0)
            return null;
        else
            return list.get(0);
    }
}
