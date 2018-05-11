package com.atlas.crmapp.common;

/**
 * @author Sean Zhu
 *         Email : seanzhuwx@gmail.com
 *         Date : 2017/3/18
 *         Description :
 */

public class Constants {
    public interface  DateFormatStr{
        String DATE_END_MINUTE = "yyyy-MM-dd HH:mm";
    }

    public interface FileDir{
        String ATLAS_DIR ="ATLAS";

    }

    public interface Sex{
        String FEMALE = "FEMALE";//女性
        String MALE = "MALE";
    }

    public interface PayChannel{
        String ALIPAY= "ALIPAY";
        String WXPAY = "WXPAY";
        String ACCOUNT = "ACCOUNT";
    }

    public interface CorporationAuthTpye{
        String OWNER ="OWNER";//法人
        String AUTHORIZED="AUTHORIZED";// 授权人
        String COMMON = "COMMON";//普通人
    }

    //扫码判断 跳转页面
    public interface CodeSkipType{
        String TO_USER = "/u/";//到个人名片页面
        String TO_RECHARGE ="/rr/";//充值页面
        String TO_ACTIVITY_VERIFI = "/activity/";//我的预约 活动
        String LOGIN_PC = "/cloudprint/mobile/";
//        String LOGIN_PC = "https://app.crm.atlasoffice.cn/hp/";
        String UNLOCK_PRINT = "/cloudprint/view/";
    }


    public interface  ToPaySuccessType{
        String RECHARGE ="RECHARGE";//充值成功
        String ORDER_SUCCESS = "ORDER_SUCCESS"; //订单支付成功

    }

    //刷新控件 时间
    public interface ToRefreshTime{
        int REFRESH_TIME = 500;
    }

    //余额明细 类型
    public interface TransactionType{
        String ORDER = "ORDER";//消费
        String ADJUST = "ADJUST";//系统
        String RECHARGE = "RECHARGE";//充值
        String REDRAW = "REDRAW"; //退款
        String REDRAW_R = "REDRAW_R";//撤销充值
    }

    public interface  Appointment{
        String SUB_STATE_COMPLETE = "COMPLETE";//已完成
        String SUB_STATE_OFF = "OFF";//进行中 未开始
        String SUB_STATE_ON = "ON";//进行中 进行中
    }

    //预约类型
    public interface AppointmentType{
        String MEETING = "MEETING";//MEETING,FITNESS_L,ACTIVITY
        String FITNESS_L = "FITNESS_L";
        String ACTIVITY = "ACTIVITY";
    }

    public interface NetWorkCode{
        int NO_NET_WORK = -1001;//无网络是错误码
    }

    //数据库信息
    public interface DataBaseInfo{
        int DB_VERSION = 1;
        String DB_NAME = "atlas.db";
    }

    public interface SpKey{
        String MAIN_GUIDE_LOOKED = "MAIN_GUIDE_LOOKED"; // type boolean
        String ACQUIRE_COUPON_UNREAD = "ACQUIRE_COUPON_UNREAD";// tyep boolean 用于app置于后台判断是否有未读的推送优惠券，是否弹出 获得优惠券层。
        String COUPON_UNREAD = "COUPON_UNREAD";// 判断是否有未读的推送优惠券 ,用于小红点的显示
        String KEY_CENTER_INFO = "KEY_CENTER_INFO";//中心的信息。
        String KEY_IS_FIRST_INSTALL ="isFirstInstall";// 是否首次安装应用
        String KEY_IS_FIRST_OPENT_BLUE = "IS_FIRST_OPENT_BLUE";//是否第一次打开蓝牙开门页面 ，用于显示引导
        String KEY_DEVICE_ID = "KEY_DEVICE_ID";// uuid 设备唯一号

    }

    //优惠券类型
    public interface CouponType{
        String REACH = "REACH";//满减
        String DISCOUNT ="DISCOUNT";//折扣
        String FIX ="FIX";//免费兑换
        String BXGY ="BXGY";//买送
        String QUOTA = "QUOTA";// 余
    }

    public interface CouponUserType{
        String ALLOWUSER = "ALLOWUSER"; //权益券
    }

    //插入数据库 消息类型
    public interface PushMsgTpye {
        String REAL_MSG = "REAL_MSG" ;// 真实接收到 的消息
        String COUPON_BIND_MSG = "coupon-bind";//优惠券 消息
        String ENTRY = "entry"; //健身3小时未签退
        String NORMAl  = "normal"; // 打开activity 等 推广

        int READER = 1;
        int UNREADER = 2;
        String ATLER = "alter";
    }

    //Eventbus 事件类型
    public interface EventType{
        String ORDER_COMPLETE = "ORDER_COMPLETE";
        String ORDER_LOOK_DETAIL = "ORDER_LOOK_DETAIL";//订单完成页面点击查看详情
        String CLICK_COMPLETE_OR_BACK = "CLICK_COMPLETE_OR_BACK";//点击完成 或按返回。
        String RECHARGE_LOOK_CODE = "RECHARGE_LOOK_CODE"; //充值时 点击完成 或按返回
        String CLICK_SELECT_CENTER_BACK = "CLICK_SELECT_CENTER_BACK"; //点击访客邀请 选择中心，返回。
        String ADDED_VALUE_BACK = "ADDED_VALUE_BACK"; // 增值服务返回
        String CANCEL_VISIT_INVITE = "CANCEL_VISIT_INVITE"; //取消邀请
    }

    public interface LoadImageType{
        String SJ = "-sj";
        String MJ = "-mj";
        String LJ = "-lj";
    }
    
    public interface ResourceConfigUrl{
        final String resUri="app/home/ad/main/";
        final String titleUri ="app/home/ad/title/";
        final String contentUri ="app/home/ad/content/";
        final String maskUri = "app/home/ad/mask/";
        final String adUri = "app/home/ad";

        String resStr = resUri+"workplace,"+resUri+"coffee,"+resUri+"kitchen,"+resUri+"fitness,"+resUri+"gogreen,"+resUri+"studio";
        String titleStr = titleUri+"workplace,"+titleUri+"coffee,"+titleUri+"kitchen,"+titleUri+"fitness,"+titleUri+"gogreen,"+titleUri+"studio";
        String contentStr = contentUri+"workplace,"+contentUri+"coffee,"+contentUri+"kitchen,"+contentUri+"fitness,"+contentUri+"gogreen,"+contentUri+"studio";
        String maskStr = maskUri+ GlobalParams.getInstance().getWorkplaceCode()+","+maskUri+ GlobalParams.getInstance().getCoffeeCode()+","+maskUri+ GlobalParams.getInstance().getKitchenCode()+","+maskUri+ GlobalParams.getInstance().getFitnessCode()+","+maskUri+ GlobalParams.getInstance().getGogreenCode()+","+maskUri+ GlobalParams.getInstance().getStudioCode();
        String resAllUrl= resStr+","+titleStr+","+contentStr+","+maskStr +","+ adUri;


        String rechargeUri = "app/recharge";
    }


    public interface CUSTOM_URL{
        String INSERT_VIP = "http://m.atlasworkplace.com/app";// 办公空间预约参观|| 加入会籍http://m.atlasworkplace.com/office.html
        String RESERVE_EXPERIENCE = "http://m.atlasworkplace.com/appointment?type=fitness&city=guangzhou"; //健身  预约体验http://m.atlasworkplace.com/make/yyty.html
        String REG_USER_CONTACT = "http://www.atlasworkplace.com/agreement.html";// 注册协议
        String VIP_BENFIT = "http://a2.rabbitpre.com/m/mqqMbmI?lc=1&sui=JUnt2ls7#from=share"; //会员福利
        String VISIT_URL = GlobalParams.getInstance().requestUrl.APPSERVER + "app/h5/invitation/";
        String ABOUT_ATLAS  = "http://m.atlasworkplace.com/about";//关于寰图
    }

    public interface ORDER_TYPE{
        String FITNESS = "fitness";  // 健身课程
        String MR = "mr"; //办公空间 会议室
        String MS = "ms";
    }


    public interface PUSH_ENVIRONMENT{ //environment
        String DEBUG = "debug";
        String RELEASE = "release";
    }

    public interface ORDER_STATE{
        String COMPLETE = "COMPLETE";//已完成
        String CONFIRM = "CONFIRM";//待支付
        String OPEN = "OPEN";// 在线点餐  待支付
        String REDRAW = "REDRAW";//已退款
    }

    public interface ORDER_FINGER_PAY{
        String AGREE_FINGER_PAY = "1";
        String NO_AGREE_FINGER_PAY = "0";
        int MAX_AVAILABLE_TIMES = 3;
    }


    //账户支付 采用的方式
    public interface PAY_PASSWORD_STATUS{
        String NOPASSWORD = "NOPASSWORD"; //免密支付
        String FINGER = "FINGER";// 指纹支付
        String PASSWORD = "PASSWORD";//密码支付
    }

    public interface BIZ_CODE {
        String COFFEE = "coffee";
    }

    public interface STATUS_BAR_ALPHA{
        int BAR_ALPHA  = 50;
    }

    public interface DOOR_TYPE{
        String ALL = "ALL";//全部门
        String GZZK = "GZZK";//过道门
        String EGCLOUD = "EGCLOUD";//酒店门
    }


    public interface ACTIVITIES_APPLY_STATUS{

        // UNPAID- 未付款，PAID - 已支付，COMPLETE - 已核消，EXPIRED- 过期
        String UNPAID = "UNPAID";//未付款
        String PAID = "PAID";//已支付
        String COMPLETE = "COMPLETE";//已核消
        String EXPIRED = "EXPIRED";//过期

    }



    public interface VISIT_INVITE_RECORD {
        //NORMAL 未来访、 CANCEL 取消、COMPLETE 已来访
        String NORMAL = "NORMAL";
        String CANCEL = "CANCEL";
        String COMPLETE = "COMPLETE";
    }



}
