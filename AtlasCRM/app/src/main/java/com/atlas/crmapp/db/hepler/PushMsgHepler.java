package com.atlas.crmapp.db.hepler;

import com.atlas.crmapp.common.Constants;
import com.atlas.crmapp.db.greendao.PushMsgDao;
import com.atlas.crmapp.db.model.PushMsg;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoda on 2017/7/5.
 */

public class PushMsgHepler {
    public static final String TYPE_KITCHEN = "coffee-order";

    public static final String TYPE_WORKPLACE_PRINT = "print-job";
    public static final String TYPE_WORKPLACE_VISITOR = "invitation";

    public static final String TYPE_FITNESS = "entry";

    public static final String TYPE_BONUSPOINTS  = "bonuspoints";
    public static final String TYPE_COUPON_BIND  = "coupon-bind ";
    public static final String TYPE_NORMAL   = "normal";


    //插入消息
    public static void insertPushMsg(String title, String content, String actionType, String action, int isReaded) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setTitle(title);
        pushMsg.setContent(content);
        pushMsg.setAction(action);
        pushMsg.setActionType(actionType);
        pushMsg.setType(Constants.PushMsgTpye.REAL_MSG);
        pushMsg.setIsRead(isReaded);
        pushMsg.setDate(System.currentTimeMillis());
        GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().insert(pushMsg);
    }


    public static int selectUnreadMsgNumber(String msgType){
        List<PushMsg> pushMsgs = selectUnreadMsgsType(msgType);
        if(pushMsgs == null){
            return 0;
        }else{
            return pushMsgs.size();
        }
    }


    public static int selectAllUnreadMsgNumber(){
        List<PushMsg> pushMsgs = selectUnreadMsgs();
        if(pushMsgs == null){
            return 0;
        }else{
            return pushMsgs.size();
        }
    }

    public static void updateUnReadMsgTypeToRead(String msgType){
        List<PushMsg> pushMsgs = selectUnreadMsgsType(msgType);
        for (PushMsg pushMsg : pushMsgs){
            pushMsg.setIsRead(Constants.PushMsgTpye.READER);
        }
        GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().updateInTx(pushMsgs);
    }

    public static void updateUnReadMsgToRead(){
        List<PushMsg> pushMsgs = selectUnreadMsgs();
        for (PushMsg pushMsg : pushMsgs){
            pushMsg.setIsRead(Constants.PushMsgTpye.READER);
        }
        GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().updateInTx(pushMsgs);
    }

    public static List<PushMsg> selectUnreadMsgsType(String msgType){
        PushMsgDao pushMsgDao = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao();
        List<PushMsg> pushMsgs = pushMsgDao.queryBuilder().where(PushMsgDao.Properties.ActionType.eq(msgType)).list();
        List<PushMsg> unReadPushMsgs = new ArrayList<>();
        for (PushMsg pushMsg : pushMsgs){
            if(Constants.PushMsgTpye.UNREADER == pushMsg.getIsRead()){
                unReadPushMsgs.add(pushMsg);
            }
        }
        return unReadPushMsgs;
    }

    public static  List<PushMsg> selectUnreadMsgs(){
        return GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder().where(PushMsgDao.Properties.IsRead.eq(Constants.PushMsgTpye.UNREADER)).list();
    }



    public static List<PushMsg> selectAlterMsgs(){
        return GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder().where(PushMsgDao.Properties.Action.eq(Constants.PushMsgTpye.ATLER)).list();
    }

    public static void updateAlterMsgs(List<PushMsg> pushMsgs){
         GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().updateInTx(pushMsgs);
    }

    //插入优惠券
    public static void insertCoupon( long uid, String title, String content, long date, String thumbnail, String price, String remark) {
        PushMsg pushMsg = new PushMsg();
        pushMsg.setCouponId(uid);//CouponId
        pushMsg.setTitle(title);
        pushMsg.setContent(content);
        pushMsg.setDate(date);
        pushMsg.setThumbnail(thumbnail);
        pushMsg.setPrice(price);
        pushMsg.setRemark(remark);
        pushMsg.setType(Constants.PushMsgTpye.COUPON_BIND_MSG);
        GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().insert(pushMsg);
    }

    public static List<PushMsg> getAllMsg() {
        List<PushMsg> allMsg;
        allMsg = getRealMsg();
        allMsg.addAll(getCouponMsg());
        return  allMsg;
    }
    public static List<PushMsg> getRealMsg(){
        QueryBuilder queryBuilder = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder();
        List<PushMsg> realMsg = queryBuilder.orderDesc(PushMsgDao.Properties.Date)
                .where(PushMsgDao.Properties.Type.eq(Constants.PushMsgTpye.REAL_MSG))
                .list();
        return  realMsg;
    }

    public static List<PushMsg> getCouponMsg(){
        QueryBuilder queryBuilder = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder();
        List<PushMsg> couponMsg = queryBuilder.where(PushMsgDao.Properties.Type.eq(Constants.PushMsgTpye.COUPON_BIND_MSG)).list();
        return  couponMsg;
    }

    public static void removeAllCoupon() {
        DeleteQuery deleteQuery = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder().where(PushMsgDao.Properties.Type.eq(Constants.PushMsgTpye.COUPON_BIND_MSG)).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

    public static void removeAllMsg() {
        DeleteQuery deleteQuery = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder().where(PushMsgDao.Properties.Type.eq(Constants.PushMsgTpye.REAL_MSG)).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }


    public static void removeOldMsg(long timeInterval) {
        DeleteQuery deleteQuery = GreenDaoManager.getInstance().getmDaoSession().getPushMsgDao().queryBuilder()
                .where(PushMsgDao.Properties.Date.lt(System.currentTimeMillis()- timeInterval), PushMsgDao.Properties.Type.eq(Constants.PushMsgTpye.REAL_MSG))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }


}
