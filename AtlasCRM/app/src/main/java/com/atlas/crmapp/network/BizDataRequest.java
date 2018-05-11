package com.atlas.crmapp.network;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.atlas.crmapp.activity.base.BaseActivity;
import com.atlas.crmapp.bean.ChatDynamicBean;
import com.atlas.crmapp.bean.ChatNoticeBean;
import com.atlas.crmapp.bean.CircleDetailBean;
import com.atlas.crmapp.bean.DynamicCommentBean;
import com.atlas.crmapp.bean.DynamicSuccessBean;
import com.atlas.crmapp.bean.IndexMomentBean;
import com.atlas.crmapp.bean.LivingBizBean;
import com.atlas.crmapp.bean.LocationBean;
import com.atlas.crmapp.bean.RegionCodeBean;
import com.atlas.crmapp.bean.TagBean;
import com.atlas.crmapp.common.GlobalParams;
import com.atlas.crmapp.common.statusLayout.IStatusLayout;
import com.atlas.crmapp.model.ActivitesUserInfoJson;
import com.atlas.crmapp.model.ActivitiesBindinfoJson;
import com.atlas.crmapp.model.ActivityJson;
import com.atlas.crmapp.model.ActivityUsersJson;
import com.atlas.crmapp.model.ActvityApplyJson;
import com.atlas.crmapp.model.AddedValueJson;
import com.atlas.crmapp.model.AllowanceContractCouponsListJson;
import com.atlas.crmapp.model.BillingItemsJson;
import com.atlas.crmapp.model.BillingsJson;
import com.atlas.crmapp.model.BusinesseModel;
import com.atlas.crmapp.model.CartJson;
import com.atlas.crmapp.model.CenterUnitJson;
import com.atlas.crmapp.model.CheckCodeJson;
import com.atlas.crmapp.model.CheckUserAccountPasswordJson;
import com.atlas.crmapp.model.CityCenterJson;
import com.atlas.crmapp.model.ClassJson;
import com.atlas.crmapp.model.ClassesJson;
import com.atlas.crmapp.model.CoachsListJson;
import com.atlas.crmapp.model.CollectLockItem;
import com.atlas.crmapp.model.CompanyMembersJson;
import com.atlas.crmapp.model.CompnayInfoJson;
import com.atlas.crmapp.model.ConsumedJson;
import com.atlas.crmapp.model.ContractProductJson;
import com.atlas.crmapp.model.ContractsJson;
import com.atlas.crmapp.model.CorporationMoneyJson;
import com.atlas.crmapp.model.CouponModel;
import com.atlas.crmapp.model.GeteamJson;
import com.atlas.crmapp.model.HabitsJson;
import com.atlas.crmapp.model.IndustryJson;
import com.atlas.crmapp.model.LessonJson;
import com.atlas.crmapp.model.LessonsListJson;
import com.atlas.crmapp.model.LockJson;
import com.atlas.crmapp.model.MeetingRoomJson;
import com.atlas.crmapp.model.MyScoreJson;
import com.atlas.crmapp.model.NoBillingItemsJson;
import com.atlas.crmapp.model.OnlineSaleProductsJson;
import com.atlas.crmapp.model.OpenOrderJson;
import com.atlas.crmapp.model.PayInfoJson;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.PrintDataJson;
import com.atlas.crmapp.model.PrintJson;
import com.atlas.crmapp.model.PrintLogin;
import com.atlas.crmapp.model.PrintStateJson;
import com.atlas.crmapp.model.ProductCategoryJson;
import com.atlas.crmapp.model.ProductInfoJson;
import com.atlas.crmapp.model.RemoteOpenLockJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.model.ResponseMeetingRoomJson;
import com.atlas.crmapp.model.ResponseMyAppointmentJson;
import com.atlas.crmapp.model.ResponseMyCodeJson;
import com.atlas.crmapp.model.ResponseMyInfoJson;
import com.atlas.crmapp.model.ResponseOpenOrderJson;
import com.atlas.crmapp.model.SKUJson;
import com.atlas.crmapp.model.SaleContractProductsJson;
import com.atlas.crmapp.model.SaleMeetingRoomsJson;
import com.atlas.crmapp.model.ScoreDetailJson;
import com.atlas.crmapp.model.ScoreGetDetailJson;
import com.atlas.crmapp.model.ScoreGetJson;
import com.atlas.crmapp.model.SuggestResponseModel;
import com.atlas.crmapp.model.TransactionsJson;
import com.atlas.crmapp.model.UnitBizJson;
import com.atlas.crmapp.model.UseableCouponsModel;
import com.atlas.crmapp.model.VThreadsJson;
import com.atlas.crmapp.model.VersionInfoJson;
import com.atlas.crmapp.model.VisibleActivitiesJson;
import com.atlas.crmapp.model.VisibleCompanysJson;
import com.atlas.crmapp.model.VisibleThreadsJson;
import com.atlas.crmapp.model.VisitInviteRecordJson;
import com.atlas.crmapp.model.MeetingRoomComboJson;
import com.atlas.crmapp.model.bean.AddedValueModel;
import com.atlas.crmapp.util.AppUtil;
import com.atlas.crmapp.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.id;
import static com.atlas.crmapp.util.DateUtil.dateToStringWithoutSplit;

/**
 * Created by Harry on 2017-03-25.
 */


public class BizDataRequest {
    public static String SERVER_NORMAL = GlobalParams.getInstance().requestUrl.SERVER_NORMAL;
    public static String SERVER_NORMAL_V2 = GlobalParams.getInstance().requestUrl.SERVER_NORMAL_V2;
    public static String SERVER_NORMAL_V3 = GlobalParams.getInstance().requestUrl.SERVER_NORMAL_V3;

    public static String language_CN_CN = "lang=zh_CN";
    public static String language_EN = "lang=en_US";
    public static String language_CN_GD = "lang=zh_HK";


    public BizDataRequest(){
        initBaseUrl();
    }

    public static void initBaseUrl(){
        SERVER_NORMAL = GlobalParams.getInstance().requestUrl.SERVER_NORMAL;
        SERVER_NORMAL_V2 = GlobalParams.getInstance().requestUrl.SERVER_NORMAL_V2;
        SERVER_NORMAL_V3 = GlobalParams.getInstance().requestUrl.SERVER_NORMAL_V3;
    }

    private static String getAccessToken(){
        return "?access_token=" + GlobalParams.getInstance().getAccessToken();
    }




    public static interface OnRequestResult {

        public void onSuccess();

        public void onError(DcnException error);
    }

    public static interface OnRequestNearestCenterUnit {

        public void onSuccess(CenterUnitJson centerUnitJson);

        public void onError(DcnException error);
    }

    public static interface OnRequestUnitBiz {

        public void onSuccess(UnitBizJson unitBizJson);

        public void onError(DcnException error);
    }

    public static interface OnRequestValidCode {

        public void onSuccess(PersonInfoJson personInfoJson);

        public void onError(DcnException error);
    }

    public static interface OnRequestRegionCode {

        public void onSuccess(List<RegionCodeBean> regionCodeBeanList);

        public void onError(DcnException error);
    }

    public static interface OnRequestTimSig {

        public void onSuccess(String string);

        public void onError(DcnException error);
    }

    public static interface OnRequestResource {

        public void onSuccess(ResourceJson resourceJson);

        public void onError(DcnException error);
    }

    public static interface OnRequestProductCategory {

        public void onSuccess(ProductCategoryJson productCategoryJson);

        public void onError(DcnException error);
    }

    public static interface OnOnlineSaleProducts {

        public void onSuccess(OnlineSaleProductsJson onlineSaleProductsJson);

        public void onError(DcnException error);
    }

    public static interface OnProductSku {

        public void onSuccess(SKUJson skuJson);

        public void onError(DcnException error);
    }

    public static interface OnProductInfo {

        public void onSuccess(ProductInfoJson productInfoJson);

        public void onError(DcnException error);
    }

    public static interface OnCompanyInfo {

        public void onSuccess(CompnayInfoJson compnayInfoJson);

        public void onError(DcnException error);
    }

    public static interface OnSaleMeetingRooms {

        public void onSuccess(SaleMeetingRoomsJson saleMeetingRoomsJson);

        public void onError(DcnException error);
    }

    public static interface OnMeetingRoomInfo {

        public void onSuccess(MeetingRoomJson meetingRoomJson);

        public void onError(DcnException error);
    }

    public static interface OnSaleContractProducts {

        public void onSuccess(SaleContractProductsJson saleContractProductsJson);

        public void onError(DcnException error);
    }

    public static interface OnContractProduct {

        public void onSuccess(ContractProductJson contractProductJson);

        public void onError(DcnException error);
    }

    public static interface OnLoginUserInfo {

        public void onSuccess(PersonInfoJson personInfoJson);

        public void onError(DcnException error);

    }

    public static interface HabitsInfo {

        public void onSuccess(List<HabitsJson> list);

        public void onError(DcnException error);

    }

    public static interface IndustryInfo {

        public void onSuccess(List<IndustryJson> list);

        public void onError(DcnException error);

    }

    public static interface PrintLoginInfo {

        public void onSuccess(PrintLogin bean);

        public void onError(DcnException error);

    }

    public static interface LivingBizBeanInfo {

        public void onSuccess(LivingBizBean bean);

        public void onError(DcnException error);

    }

    public static interface PrintUnlockInfo {

        public void onSuccess();

        public void onError(DcnException error);

    }

    public static interface OnVisibleThreads {

        public void onSuccess(VisibleThreadsJson visibleThreadsJson);

        public void onError(DcnException error);

    }

    public static interface CircleDetail {

        public void onSuccess(CircleDetailBean bean);

        public void onError(DcnException error);

    }

    public static interface OnVThreads {

        public void onSuccess(VThreadsJson vThreadsJson);

        public void onError(DcnException error);

    }

    public static interface OnVisibleActivities {

        public void onSuccess(VisibleActivitiesJson visibleActivitiesJson);

        public void onError(DcnException error);

    }

    public static interface OnActivity {

        public void onSuccess(ActivityJson activityJson);

        public void onError(DcnException error);

    }

    public static interface OnActivityUsers {

        public void onSuccess(ActivityUsersJson activityUsersJson);

        public void onError(DcnException error);

    }

    public static interface OnResponseOpenOrderJson {

        public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson);

        public void onError(DcnException error);
    }

    public static interface OnSuggestDiscountJson {

        public void onSuccess();

        public void onError(DcnException error);
    }

    //购买会籍
    public static interface OnResponseCreateContractOrderJson {

        public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson);

        public void onError(DcnException error);
    }

    //我的订单
    public static interface OnResponseMyOrderJson {

        public void onSuccess(List<ResponseOpenOrderJson> responseOpenOrderJson);

        public void onError(DcnException error);
    }

    //我的预约
    public static interface OnResponseMyAppointmentJson {

        public void onSuccess(ResponseMyAppointmentJson responseMyAppointmentJson);

        public void onError(DcnException error);
    }

    //创建预约会议室
    public static interface OnCreateMeetingRoomBookingJson {

        public void onSuccess(ResponseMeetingRoomJson responseOpenOrderJson);

        public void onError(DcnException error);
    }

    //创建预约课时
    public static interface OnreateClassBookingJson {

        public void onSuccess(ResponseMeetingRoomJson responseOpenOrderJson);

        public void onError(DcnException error);
    }

    //支付订单
    public static interface OnPayOrderRequestResult {

        public void onSuccess(String jsondata);

        public void onError(DcnException error);
    }

    //账户钱包信息
    public static interface OnResponseMyInfoJson {

        public void onSuccess(ResponseMyInfoJson responseMyInfoJson);

        public void onError(DcnException error);
    }

    //积分
    public static interface OnResponseMyScoreJson {

        public void onSuccess(MyScoreJson responseMyScoreJson);

        public void onError(DcnException error);
    }

    //积分兑换
    public static interface OnResponseScoreListJson {

        public void onSuccess(List<ScoreGetJson> obj);

        public void onError(DcnException error);
    }

    //兑换积分
    public static interface OnResponseScoreTranJson {

        public void onSuccess();

        public void onError(DcnException error);
    }

    //积分兑换记录
    public static interface OnResponseScoreGetListJson {

        public void onSuccess(ScoreGetDetailJson obj);

        public void onError(DcnException error);
    }


    //积分明细
    public static interface OnResponseScoreDetailListJson {

        public void onSuccess(ScoreDetailJson obj);

        public void onError(DcnException error);
    }

    //我的二维码
    public static interface OnResponseMyCodeJson {

        public void onSuccess(ResponseMyCodeJson responseMyCodeJson);

        public void onError(DcnException error);
    }

    //我的订单推荐优惠卷
    public static interface OnSuggestRequestResult {

        public void onSuccess(SuggestResponseModel suggestResponseModel);

        public void onError(DcnException error);
    }

    //我的优惠卷
    public static interface OnMyCouponsRequestResult {

        public void onSuccess(List<UseableCouponsModel> useableCouponsList);

        public void onError(DcnException error);
    }

    //领卷中心
    public static interface OnCouponsCenterRequestResult {

        public void onSuccess(List<CouponModel> couponModelsList);

        public void onError(DcnException error);
    }

    public static  interface OnCoachsRequestResult{

        public void onSuccess(CoachsListJson coachsListJson);

        public void onError(DcnException error);

    }

    //课程列表
    public static interface OnLessonsRequestResult {

        public void onSuccess(LessonsListJson lessonsJsons);

        public void onError(DcnException error);
    }

    //课程
    public static interface OnLessonRequestResult {

        public void onSuccess(LessonJson lessonJson);

        public void onError(DcnException error);
    }

    //课时列表
    public static interface OnClassesRequestResult {

        public void onSuccess(ClassesJson classesJson);

        public void onError(DcnException error);
    }

    //课时
    public static interface OnClassRequestResult {

        public void onSuccess(ClassJson classJson);

        public void onError(DcnException error);
    }

    //可见企业列表
    public static interface OnVisibleCompanysRequestResult {

        public void onSuccess(VisibleCompanysJson visibleCompanysJson);

        public void onError(DcnException error);
    }

    //企业成员列表
    public static interface OnCompanyMembersRequestResult {

        public void onSuccess(CompanyMembersJson companyMembersJson);

        public void onError(DcnException error);
    }

    //合同列表
    public static interface OnContractsRequestRescult {

        public void onSuccess(ContractsJson contractsJson);

        public void onError(DcnException error);
    }

    //企业账单列表
    public static interface OnBillingsRequestRescult {

        public void onSuccess(BillingsJson billingsJson);

        public void onError(DcnException error);
    }

    //企业账单详细列表
    public static interface OnBillingItemsRequestRescult {

        public void onSuccess(BillingItemsJson billingItemsJson);

        public void onError(DcnException error);
    }

    //企业账单详细列表
    public static interface OnNoBillingItemsRequestRescult {

        public void onSuccess(NoBillingItemsJson noBillingItemsJson);

        public void onError(DcnException error);
    }

    //额度查询
    public static interface CorporationMoneyJsonRequestResult {

        public void onSuccess(CorporationMoneyJson corporationMoneyJson);

        public void onError(DcnException error);
    }


    //打印记录
    public static interface MyPrintRequestRescult {

        public void onSuccess(PrintDataJson printJason);

        public void onError(DcnException error);
    }

    //打印机状态
    public static interface PrintStateRequestRescult {

        public void onSuccess(PrintStateJson printJason);

        public void onError(DcnException error);
    }

    //打印机状态
    public static interface TagListRequestRescult {

        public void onSuccess(List<TagBean> printJason);

        public void onError(DcnException error);
    }
    //首页动态
    public static interface IndexMomentBeanRequestRescult {

        public void onSuccess(IndexMomentBean bean);

        public void onError(DcnException error);
    }
    //首页坐标
    public static interface LocationBeanRequestRescult {

        public void onSuccess(List<LocationBean> list);

        public void onError(DcnException error);
    }

    //动态评论
    public static interface DynamicCommentBeanRequestRescult {

        public void onSuccess(DynamicCommentBean bean);

        public void onError(DcnException error);
    }
    //动态消息
    public static interface ChatDynamicBeanRequestRescult {

        public void onSuccess(ChatDynamicBean bean);

        public void onError(DcnException error);
    }

    //IM 消息
    public static interface ChatChatBeanRequestRescult {

        public void onSuccess(ChatNoticeBean bean);

        public void onError(DcnException error);
    }

    //动态详情
    public static interface DynamicDetailBeanRequestRescult {

        public void onSuccess(IndexMomentBean.RowsBean bean);

        public void onError(DcnException error);
    }
    //动态详情
    public static interface DynamicSuccessBeanRequestRescult {

        public void onSuccess(DynamicSuccessBean bean);

        public void onError(DcnException error);
    }


    // UID用户信息
    public static interface OnUidUserInfo {

        public void onSuccess(List<PersonInfoJson> personList);

        public void onError(DcnException error);

    }

    public static interface OnUidUserInfo_ {

        public void onSuccess(PersonInfoJson personList);

        public void onError(DcnException error);

    }

    //获取企业权益优惠券列表
    public static interface OnListAllowanceContractCouponsRequestResult {

        public void onSuccess(AllowanceContractCouponsListJson allowanceContractCouponsListJson);

        public void onError(DcnException error);
    }

    //获取支付信息
    public static interface OnResponsePayInfoJson {

        public void onSuccess(PayInfoJson payInfoJson);

        public void onError(DcnException error);
    }


    //获取app版本 信息
    public static interface OnResponseVersionInfoJson{

        public void onSuccess(VersionInfoJson versionInfoJson);

        public void onError(DcnException error);
    }


    //我的订单
    public static interface OnResponseTransactionsJson {

        public void onSuccess(List<TransactionsJson> transactionsJsons);

        public void onError(DcnException error);
    }

    // 上传 设备信息
    public static interface  OnResponseUploadDevice{
        public void onSuccess();
        public void onError(DcnException error);
    }

    //全部业态用于领券中心  订单业态用于订单
    public static interface  OnResponseGetBusinesses{
        public void onSuccess(List<BusinesseModel> businesseModels);
        public void onError(DcnException error);
    }

    //是否有健身合同或权益
    public static interface OnResponseBoolean{
        public void onSuccess(boolean is);
        public void onError(DcnException error);

    }

    //获取中心列表centerSwitchApi/getCenterList
    public static interface OnResponseGetCenterList{
        public void onSuccess(List<CityCenterJson> centerListJsons);
        public void onError(DcnException error);
    }


    //获取支付校验ma码PaymentCheckCode
    public static interface OnResponsePaymentCheckCode{
        public void onSuccess(String checkCode);
        public void onError(DcnException error);
    }


    //检验 支付密码
    public static interface OnResponseCheckUserAccountPassword{
        public void onSuccess(int result);
        public void onError(DcnException error);
    }


    //获取目前用户有权限打开 的 UUID列表
    public static interface OnResponseFindUUidList{
        public void onSuccess(List<LockJson> lockJsons);
        public void onError(DcnException error);
    }


    //根据token和搜索到的UUID列表获取当前用户可开门列表
    public static interface OnResponseFindLockList{
        public void onSuccess(List<LockJson> lockJsons);
        public void onError(DcnException error);
    }

    //根据门禁ID开锁
    public static interface OnResponseRemoteOpenLock{
        public void onSuccess(RemoteOpenLockJson remoteOpenLockJson);
        public void onError(DcnException error);
    }

    //获取收藏的门列表
    public static interface OnResponseFindCollectList{
        public void onSuccess(List<LockJson> lockJsons);
        public void onError(DcnException error);
    }

    //收藏门锁
    public static interface OnResponseCollectDoor{
        public void onSuccess();
        public void onError(DcnException error);
    }

    //最近门锁
    public static interface OnResponsefindLastestOpenList{
        public void onSuccess(List<LockJson> lockJsons);
        public void onError(DcnException error);
    }

    //活动核销
    public static interface OnResponseConsumed{
        public void onSuccess(ConsumedJson consumedJson);
        public void onError(DcnException error);
    }

    //获取活动订单二维码 、 活动概要
    public static interface OnResponseBindinfo{
        public void onSuccess(ActivitiesBindinfoJson bindinfoJson);
        public void onError(DcnException error);
    }

    //获取活动联系人
    public static interface OnResponseActivityUserinfo{
        public void onSuccess(ActivitesUserInfoJson userInfoJson);
        public void onError(DcnException error);
    }


    //获取以往参加活动联系人信息、用于回显
    public static interface OnResponseActivityUsers{
        public void onSuccess(List<ActivitesUserInfoJson> userInfoJsons);
        public void onError(DcnException error);
    }

    //活动报名v2
    public static interface OnResponseV2ApplyActivity{
        public void onSuccess(ResponseOpenOrderJson responseOpenOrderJson);
        public void onError(DcnException error);
    }


    //访客邀请
    public static interface OnResponseVisitorInvite{
        public void onSuccess(VisitInviteRecordJson.RowsBean rowsBean);
        public void onError(DcnException error);
    }




    //访客邀请
    public static interface OnResponseAppUserGeteam{
        public void onSuccess(boolean isGeteam);
        public void onError(DcnException error);
    }


    //邀请记录
    public static interface OnResponseVisitorInviteRecord{
        public void onSuccess(VisitInviteRecordJson visitInviteRecordJson);
        public void onError(DcnException error);
    }


    //取消邀请
    public static interface OnResponseCancelInvite{
        public void onSuccess();
        public void onError(DcnException error);
    }



    //获取可用的增值服务
    public static interface OnResponseAddedValue{
        public void onSuccess(List<AddedValueJson> addedValueJsons);
        public void onError(DcnException error);
    }

    //MeetingroomCombo

    //获取可用的增值服务
    public static interface OnResponseMeetingroomCombo{
        public void onSuccess(List<MeetingRoomComboJson> combos);
        public void onError(DcnException error);
    }

    //获取订单详情
    public static interface OnResponseOrderDetail{
        public void onSuccess(ResponseOpenOrderJson orderJson);
        public void onError(DcnException error);
    }


    public static interface OnResponseAppDate{
        public void onBefore(BaseRequest request);

        public void onSuccess(File file, Call call, Response response);

        public void onError(Call call, @Nullable Response response, @Nullable Exception e);

        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed);
    }

    //获得最近中心的id
    public static void requestNearestCenterUnit(final Context context, String lat, String lng, final OnRequestNearestCenterUnit onRequestNearestCenterUnit) {

        String url = SERVER_NORMAL + "common/getNearestCenterUnit";
        HashMap params = new HashMap();
        params.put("lat", lat);
        params.put("lng", lng);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CenterUnitJson centerUnitJson = gson.fromJson(jsonData, CenterUnitJson.class);
                GlobalParams.getInstance().setAtlasId(centerUnitJson.id);
                GlobalParams.getInstance().setAtlasName(centerUnitJson.name);
                onRequestNearestCenterUnit.onSuccess(centerUnitJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestNearestCenterUnit.onError(error);
            }
        });
    }

    //获得指定中心下的业态ID
    public static void requestUnits(final Context context, long unitId, final OnRequestUnitBiz onRequestUnitBiz) {

        String url = SERVER_NORMAL + "common/getChildrenUnits";
        HashMap params = new HashMap();
        params.put("id", unitId);
        //params.put("id", "4");
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                UnitBizJson unitBizJson = gson.fromJson(jsonData, UnitBizJson.class);
                for (int i = 0; i < unitBizJson.recordsTotal; i++) {
                    if (unitBizJson.rows.get(i).bizCode.equalsIgnoreCase("coffee")) {
                        ((BaseActivity)context).getGlobalParams().setCoffeeId(unitBizJson.rows.get(i).id);
                        ((BaseActivity)context).getGlobalParams().setCoffeeCode(unitBizJson.rows.get(i).bizCode);
                    } else if (unitBizJson.rows.get(i).bizCode.equalsIgnoreCase("kitchen")&&((BaseActivity)context).getGlobalParams().getKitchenId()==0) {
                        ((BaseActivity)context).getGlobalParams().setKitchenId(unitBizJson.rows.get(i).id);
                        ((BaseActivity)context).getGlobalParams().setKitchenCode(unitBizJson.rows.get(i).bizCode);
                    } else if (unitBizJson.rows.get(i).bizCode.equalsIgnoreCase("fitness")) {
                        ((BaseActivity)context).getGlobalParams().setFitnessId(unitBizJson.rows.get(i).id);
                        ((BaseActivity)context).getGlobalParams().setFitnessCode(unitBizJson.rows.get(i).bizCode);
                    } else if (unitBizJson.rows.get(i).bizCode.equalsIgnoreCase("workplace")) {
                        ((BaseActivity)context).getGlobalParams().setWorkplaceId(unitBizJson.rows.get(i).id);
                        ((BaseActivity)context).getGlobalParams().setWorkplaceCode(unitBizJson.rows.get(i).bizCode);
                    }

                }

                onRequestUnitBiz.onSuccess(unitBizJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestUnitBiz.onError(error);
            }
        });
    }

    //发送短信验证码
    public static void requestSendCode(Context context,String zipCode, String mobile, final OnRequestResult onRequestResult) {

        String url = SERVER_NORMAL + "common/sendCode";
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        params.put("zipCode", zipCode);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //发送短信验证码
    public static void requestSendCode(Context context,String zipCode, String mobile, final OnRequestResult onRequestResult,String smsType) {

        String url = SERVER_NORMAL + "common/sendCode";
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        params.put("zipCode", zipCode);
        params.put("sendType", smsType);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //验证短信验证码
    public static void requestValidCode(Context context,String zipCode, String mobile, String code, final OnRequestValidCode onValidCode) {

        String url = SERVER_NORMAL_V2 + "common/validCode";
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        params.put("zipCode", zipCode);
        params.put("deviceId", AppUtil.getUUID(context));
        params.put("code", code);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PersonInfoJson personInfoJson = gson.fromJson(jsonData, PersonInfoJson.class);
                onValidCode.onSuccess(personInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onValidCode.onError(error);
            }
        });
    }

    //获取区号
    public static void getRegionCode(Context context, final OnRequestRegionCode onRequestRegionCode) {

        String url = SERVER_NORMAL + "common/getRegionCode";
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params,true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<RegionCodeBean> personInfoJson = gson.fromJson(jsonData, new TypeToken<List<RegionCodeBean>>() {}.getType());
                onRequestRegionCode.onSuccess(personInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestRegionCode.onError(error);
            }
        });
    }

    //获取签名
    public static void getTimSig(Context context,String id, final OnRequestTimSig onRequestTimSig) {

        String url = SERVER_NORMAL + "tls/getSign" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params,true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestTimSig.onSuccess(jsonData);
            }

            @Override
            public void onError(DcnException error) {

                onRequestTimSig.onError(error);
            }
        });
    }


    //获取当前应用App内容--
    public static void requestResource(Context context, String unitId, String uri, final OnRequestResource onRequestResource) {
        requestResource(context, unitId, uri, null, onRequestResource);
    }

    //获取当前应用App内容--
    public static void requestResource(Context context, String unitId, String uri, IStatusLayout statusLayout, final OnRequestResource onRequestResource) {

        String url = SERVER_NORMAL + "contentManagement/getApplyResourceConfigs";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("uris", uri);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResourceJson resourceJson = gson.fromJson(jsonData, ResourceJson.class);
                onRequestResource.onSuccess(resourceJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResource.onError(error);
            }
        });
    }

    //获得业态分类
    public static void requestProductCategory(Context context, String bizCode, IStatusLayout statusLayout, final OnRequestProductCategory onRequestProductCategory) {

        String url = SERVER_NORMAL + "product/getProductCategoryTree";
        HashMap params = new HashMap();
        params.put("bizCode", bizCode);
        NetworkUtil.requestJson(context, url, params, statusLayout,  new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ProductCategoryJson productCategoryJson = gson.fromJson(jsonData, ProductCategoryJson.class);
                onRequestProductCategory.onSuccess(productCategoryJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestProductCategory.onError(error);
            }
        });
    }

    //获得在线销售产品列表
    public static void requestOnlineSaleProducts(Context context, int pageIndex, int pageSize, long unitId, long categoryId, IStatusLayout statusLayout, final OnOnlineSaleProducts onOnlineSaleProducts) {

        String url = SERVER_NORMAL + "product/getOnSaleProducts";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("categoryId", categoryId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, false, statusLayout,new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                OnlineSaleProductsJson onlineSaleProductsJson = gson.fromJson(jsonData, OnlineSaleProductsJson.class);
                onOnlineSaleProducts.onSuccess(onlineSaleProductsJson);
            }

            @Override
            public void onError(DcnException error) {

                onOnlineSaleProducts.onError(error);
            }
        });
    }

    //获得产品SKU
    public static void requestProductSku(Context context, String productId, final OnProductSku onProductSku) {

        String url = SERVER_NORMAL + "product/getProductSku";
        HashMap params = new HashMap();
        params.put("id", productId);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                SKUJson skuJson = gson.fromJson(jsonData, SKUJson.class);
                onProductSku.onSuccess(skuJson);
            }

            @Override
            public void onError(DcnException error) {

                onProductSku.onError(error);
            }
        });
    }

    //获得产品详细信息
    public static void requestProductInfo(Context context, String productId, IStatusLayout statusLayout, final OnProductInfo onProductInfo) {

        String url = SERVER_NORMAL + "product/getProduct";
        HashMap params = new HashMap();
        params.put("id", productId);
        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ProductInfoJson productInfoJson = gson.fromJson(jsonData, ProductInfoJson.class);
                onProductInfo.onSuccess(productInfoJson);
            }

            @Override
            public void onError(DcnException error) {
                onProductInfo.onError(error);
            }
        });
    }

    //获取线上销售会议室列表
    public static void requestOnlineSaleMeetingRooms(Context context, int pageIndex, int pageSize, long unitId, String date,int capacity, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnSaleMeetingRooms onSaleMeetingRooms) {

        String url = SERVER_NORMAL + "workplace/getOnSaleMeetingRooms";
        HashMap params = new HashMap();
        params.put("capacity", capacity);
        params.put("unitId", unitId);
        params.put("date", date);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                SaleMeetingRoomsJson saleMeetingRoomsJson = gson.fromJson(jsonData, SaleMeetingRoomsJson.class);
                onSaleMeetingRooms.onSuccess(saleMeetingRoomsJson);
            }

            @Override
            public void onError(DcnException error) {

                onSaleMeetingRooms.onError(error);
            }
        });
    }

    //获得会议室详细信息
    public static void requestMeetingRoomInfo(Context context, String productId,String date, IStatusLayout statusLayout, final OnMeetingRoomInfo onMeetingRoomInfo) {

        String url = SERVER_NORMAL + "workplace/getMeetingRoom";
        HashMap params = new HashMap();
        params.put("id", productId);
        params.put("date", date);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                MeetingRoomJson meetingRoomJson = gson.fromJson(jsonData, MeetingRoomJson.class);
                onMeetingRoomInfo.onSuccess(meetingRoomJson);
            }

            @Override
            public void onError(DcnException error) {
                onMeetingRoomInfo.onError(error);
            }
        });
    }


    //获取在线销售合同产品列表
    public static void requestSaleContractProducts(Context context, int pageIndex, int pageSize, long unitId, IStatusLayout statusLayout, final OnSaleContractProducts onSaleContractProducts) {

        String url = SERVER_NORMAL + "contract/getOnSaleContractProducts";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                SaleContractProductsJson saleContractProductsJson = gson.fromJson(jsonData, SaleContractProductsJson.class);
                onSaleContractProducts.onSuccess(saleContractProductsJson);
            }

            @Override
            public void onError(DcnException error) {
                onSaleContractProducts.onError(error);
            }
        });
    }

    //获得合同产品详细信息
    public static void requestContractProductInfo(Context context, String productId, IStatusLayout statusLayout, final OnContractProduct onContractProduct) {

        String url = SERVER_NORMAL + "contract/getContractProduct";
        HashMap params = new HashMap();
        params.put("id", productId);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ContractProductJson contractProductJson = gson.fromJson(jsonData, ContractProductJson.class);
                onContractProduct.onSuccess(contractProductJson);
            }

            @Override
            public void onError(DcnException error) {

                onContractProduct.onError(error);
            }
        });
    }

    //获得企业详细信息
    public static void requestCompanyInfo(Context context, String companyId, final OnCompanyInfo onCompanyInfo) {

        String url = SERVER_NORMAL + "contract/getCompany";
        HashMap params = new HashMap();
        params.put("id", companyId);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CompnayInfoJson compnayInfo = gson.fromJson(jsonData, CompnayInfoJson.class);
                onCompanyInfo.onSuccess(compnayInfo);
            }

            @Override
            public void onError(DcnException error) {

                onCompanyInfo.onError(error);
            }
        });
    }

    //获得当前用户信息
    public static void requestLoginUserInfo(Context context, final OnLoginUserInfo onLoginUserInfo) {

        String url = SERVER_NORMAL_V2 + "appUser/getInfo" + getAccessToken();
        HashMap params = new HashMap();
        params.put("deviceId", AppUtil.getUUID(context));

        Log.i("WelcomeAct_","WelcomeAct 4 url = " + url + "; d = " + AppUtil.getUUID(context));
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PersonInfoJson personInfoJson = gson.fromJson(jsonData, PersonInfoJson.class);
                onLoginUserInfo.onSuccess(personInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onLoginUserInfo.onError(error);
            }
        });
    }

    //获得兴趣
    public static void requestHabits(Context context, final HabitsInfo habitsInfo) {

        String url = SERVER_NORMAL_V2 + "common/listInterest" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<HabitsJson> list = gson.fromJson(jsonData,new TypeToken<List<HabitsJson>>() {}.getType());
                habitsInfo.onSuccess(list);
            }

            @Override
            public void onError(DcnException error) {

                habitsInfo.onError(error);
            }
        });
    }

    //获得行业
    public static void requestIndustry(Context context, final IndustryInfo industryInfo) {

        String url = SERVER_NORMAL_V2 + "common/listIndustry" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<IndustryJson> list = gson.fromJson(jsonData,new TypeToken<List<IndustryJson>>() {}.getType());
                industryInfo.onSuccess(list);
            }

            @Override
            public void onError(DcnException error) {
                industryInfo.onError(error);
            }
        });
    }




    //修改用户基本信息
    public static void requestModifyUserInfo(Context context, PersonInfoJson personInfoJson, final OnRequestResult onRequestResult) {

        String url = SERVER_NORMAL_V2 + "appUser/updateInfo" + getAccessToken();
        HashMap params = new HashMap();
        params.put("deviceId", AppUtil.getUUID(context));
        params.put("nick", personInfoJson.nick);
        params.put("avatar", personInfoJson.avatar);
        params.put("gender", personInfoJson.gender);
        params.put("company", personInfoJson.company);
        params.put("noCountPassword", personInfoJson.noCountPassword);
        params.put("openfingerprint", personInfoJson.openfingerprint);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    public static void requestModifyUserInfo(Context context, HashMap params, final OnRequestResult onRequestResult ){
        String url = SERVER_NORMAL_V2 + "appUser/updateInfo" + getAccessToken();
        params.put("deviceId", AppUtil.getUUID(context));
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {

                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //获取用户手机验证码
    public static void requestSendSmsByUser(Context context,String zipCode, String mobile, final OnRequestResult onRequestResult) {
        requestSendCode(context,zipCode,mobile,onRequestResult,"PASSWORD");
//        String url = SERVER_NORMAL + "appUser/sendSmsByUser" + getAccessToken();
//        HashMap params = new HashMap();
//         NetworkUtil.requestJson(context, url, params, new OnResponseData() {
//            @Override
//            public void onSuccess(String jsonData, int responseCode) {
//                onRequestResult.onSuccess();
//            }
//
//            @Override
//            public void onError(DcnException error) {
//                onRequestResult.onError(error);
//            }
//        });
    }

    //修改用户帐号支付密码
    public static void requestUpdateUserAccountPassword(Context context, String password, String code, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/updateUserAccountPassword" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("password", password);
        params.put("code", code);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }


    //获取可见话题列表（访客）
    public static void requestVisibleThreadsForVisitor(Context context, long unitId, long maxId,  boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnVisibleThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getVisibleThreads";
        else
            url = SERVER_NORMAL + "appUser/getVisibleThreads" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("maxId", maxId);
        NetworkUtil.requestJson(context, url, params, isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取可见话题列表
    public static void requestVisibleThreads(Context context, long unitId, long maxId, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnVisibleThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getVisibleThreads";
        else
            url = SERVER_NORMAL + "appUser/getVisibleThreads" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("maxId", maxId);
        NetworkUtil.requestJson(context, url, params,isShowProgreeDialog , statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取可见话题列表
    public static void requestVisibleThreads(Context context,long forumId, long unitId, long maxId, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnVisibleThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getVisibleThreads";
        else
            url = SERVER_NORMAL + "appUser/getVisibleThreads" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("maxId", maxId);
        params.put("forumId", forumId);
        NetworkUtil.requestJson(context, url, params,isShowProgreeDialog , statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取话题详细信息（访客）
    public static void requestGetThreadForVisitor(Context context, long id, IStatusLayout statusLayout, final OnVThreads onRequestResult) {

        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getThread";
        else
            url = SERVER_NORMAL + "appUser/getThread" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VThreadsJson vThreadsJson = gson.fromJson(json, VThreadsJson.class);
                onRequestResult.onSuccess(vThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取话题详细信息
    public static void requestGetThread(Context context, long id, IStatusLayout statusLayout, final OnVThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getThread";
        else
            url = SERVER_NORMAL + "appUser/getThread" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VThreadsJson vThreadsJson = gson.fromJson(json, VThreadsJson.class);
                onRequestResult.onSuccess(vThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取话题回复列表（访客）
    public static void requestGetReplyThreadsForVisitor(Context context, long id, long maxId,String type, final OnVisibleThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getReplyThreads";
        else
            url = SERVER_NORMAL + "appUser/getReplyThreads" + getAccessToken();


//        String url = SERVER_NORMAL + "thread/getReplyThreads";
        HashMap params = new HashMap();
        params.put("id", id);
        params.put("maxId", maxId);
        params.put("sort", type);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取话题回复列表（访客）
    public static void requestGetReplyThreadsForVisitor_V2(Context context, long id, long page,String[] type, final OnVisibleThreads onRequestResult) {
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL_V2 + "thread/anon/getReplyThreads";
        else
            url = SERVER_NORMAL_V2 + "thread/getReplyThreads" + getAccessToken();


//        String url = SERVER_NORMAL + "thread/getReplyThreads";
        HashMap params = new HashMap();
        params.put("id", id);
        params.put("page", page);
        params.put("size", 10);
        params.put("sort", type);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取话题回复列表
    public static void requestGetReplyThreads(Context context, long id, long maxId, final OnVisibleThreads onRequestResult) {
//        String url = SERVER_NORMAL + "appUser/getReplyThreads" + getAccessToken();;
        String url = "";
        if (!GlobalParams.getInstance().isLogin())
            url = SERVER_NORMAL + "thread/getReplyThreads";
        else
            url = SERVER_NORMAL + "appUser/getReplyThreads" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id", id);
        params.put("maxId", maxId);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleThreadsJson visibleThreadsJson = gson.fromJson(json, VisibleThreadsJson.class);
                onRequestResult.onSuccess(visibleThreadsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取圈子详情列表
    public static void getCircleDetail(Context context, long id, final CircleDetail onRequestResult) {
        String url = "";
        url = SERVER_NORMAL + "forum/findForum" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                CircleDetailBean bean = gson.fromJson(json, CircleDetailBean.class);
                onRequestResult.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //回复话题
    public static void requestReplyThread(Context context, long id, String content, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/replyThread" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("replyTo", id);
        params.put("content", content);
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //关注话题
    public static void collectTag(Context context,boolean isSelected, long id, final OnRequestResult onRequestResult) {
        String url = "";
        if (isSelected)
            url = SERVER_NORMAL + "appUser/setThreadFocus" + getAccessToken();
        else
            url = SERVER_NORMAL + "appUser/cancelThreadFocus" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //话题点赞
    public static void requestSetThreadLike(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/setThreadLike" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //话题取消点赞
    public static void requestCancelThreadLike(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/cancelThreadLike" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取可见活动列表（访客）
    public static void requestGetVisibleActivitysForVisitor(Context context, long unitId, long maxId,String bizCode, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnVisibleActivities onRequestResult) {
        String url = SERVER_NORMAL + "activity/getVisibleActivitys";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("maxId", maxId);
        params.put("bizCode", bizCode);

        NetworkUtil.requestJson(context, url, params, isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleActivitiesJson visibleActivitiesJson = gson.fromJson(json, VisibleActivitiesJson.class);
                onRequestResult.onSuccess(visibleActivitiesJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取可见活动列表
    public static void requestGetVisibleActivitys(Context context, long unitId, long maxId,String bizCode, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnVisibleActivities onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getVisibleActivitys" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("maxId", maxId);
        if(StringUtils.isNotEmpty(bizCode)){
            params.put("bizCode", bizCode);
        }
        params.put("page", 0);
        params.put("size",20);
        params.put("draw", 0);

        NetworkUtil.requestJson(context, url, params, isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                VisibleActivitiesJson visibleActivitiesJson = gson.fromJson(json, VisibleActivitiesJson.class);
                onRequestResult.onSuccess(visibleActivitiesJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取活动详细信息（访客）
    public static void requestGetActivityForVisitor(Context context, long id, final OnActivity onRequestResult) {
        String url = SERVER_NORMAL + "activity/getActivity";
        HashMap params = new HashMap();
        params.put("id", id);

        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                ActivityJson activityJson = gson.fromJson(json, ActivityJson.class);
                onRequestResult.onSuccess(activityJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取活动详细信息
    public static void requestGetActivity(Context context, long id, final OnActivity onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getActivity" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);

        NetworkUtil.requestJson(context, url, params,  new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                ActivityJson activityJson = gson.fromJson(json, ActivityJson.class);
                onRequestResult.onSuccess(activityJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取活动参与人列表
    public static void requestGetActivityUsers(Context context, int pageIndex, int pageSize, long id, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnActivityUsers onRequestResult) {
        String url = SERVER_NORMAL + "activity/getActivityUsers";
        HashMap params = new HashMap();
        params.put("id", id);

        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                String json = jsonData;
                Gson gson = new Gson();
                ActivityUsersJson activityUsersJson = gson.fromJson(json, ActivityUsersJson.class);
                onRequestResult.onSuccess(activityUsersJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }



    //活动报名
    public static void requestApplyActivity(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/applyActivity" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //活动点赞
    public static void requestSetActivityLike(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/setActivityLike" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }


    //活动报名  v2
    public static void requestV2ApplyActivity(Context context, ActvityApplyJson actvityApplyJson, IStatusLayout statusLayout, final OnResponseV2ApplyActivity onResponseV2ApplyActivity) {
        String url = SERVER_NORMAL_V2 + "activity/apply" + getAccessToken();

        HashMap params = new HashMap();
        HashMap userInfo = new HashMap();
        params.put("activityId", actvityApplyJson.getActivityId());
        params.put("channel", actvityApplyJson.getChannel());
        params.put("comboId", actvityApplyJson.getComboId());
        ActivitesUserInfoJson userInfoJson = actvityApplyJson.getUserInfo();
        if(userInfoJson.getId() != 0){
            userInfo.put("id", userInfoJson.getId());
        }
        if(StringUtils.isNotEmpty(userInfoJson.getAppuserId())){
            userInfo.put("appuserId", userInfoJson.getAppuserId());
        }
        if(StringUtils.isNotEmpty(userInfoJson.getOpenId())){
            userInfo.put("openId", userInfoJson.getOpenId());
        }
        userInfo.put("name", userInfoJson.getName());
        userInfo.put("phone", userInfoJson.getPhone());
        userInfo.put("remark", userInfoJson.getRemark());
        userInfo.put("wechat", userInfoJson.getWechat());
        userInfo.put("name", userInfoJson.getName());
        params.put("userInfo", userInfo);


        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson openOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onResponseV2ApplyActivity.onSuccess(openOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseV2ApplyActivity.onError(error);
            }
        });
    }

    //活动取消点赞
    public static void requestCancelActivityLike(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/cancelActivityLike" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //创建订单
    public static void requestOpenOrder(Context context, OpenOrderJson openOrderJson, final OnResponseOpenOrderJson onResponseOpenOrderJson) {

        String url = SERVER_NORMAL + "order/open" + getAccessToken();;
        ArrayList<HashMap> carts = new ArrayList<HashMap>();
        for (int i=0; i<openOrderJson.items.size(); i++) {
            CartJson cartJson = openOrderJson.items.get(i);
            HashMap params = new HashMap();
            params.put("skuId", cartJson.skuId);
            params.put("count", cartJson.count);
            carts.add(params);
        }
        HashMap params = new HashMap();
        params.put("unitId", openOrderJson.unitId);
        params.put("amount", openOrderJson.amount);

        params.put("items", carts);

        Log.e("Tag",params.toString());

        //params.put("city", openOrderJson.city);
        //params.put("address", openOrderJson.address);
        //params.put("receiver", openOrderJson.receiver);
        //String s = new Gson().toJson(carts);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson responseOpenOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onResponseOpenOrderJson.onSuccess(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseOpenOrderJson.onError(error);
            }
        });
    }

    //创建会籍订单
    public static void requestCreateContractOrder(Context context, long productId,int count,int month,long unitId,long startTime, final OnResponseCreateContractOrderJson onResponseCreateContractOrderJson) {

        String url = SERVER_NORMAL + "appUser/createContract" + getAccessToken();
        HashMap params = new HashMap();
        params.put("productId", productId);
        params.put("count", count);
        params.put("month", month);
        params.put("unitId", unitId);
        params.put("startTime",startTime);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson responseOpenOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onResponseCreateContractOrderJson.onSuccess(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseCreateContractOrderJson.onError(error);
            }
        });
    }

    //确认订单
    public static void requestConfirmOrder(Context context, long orderId, double amount,
                                           double discount, double actualAmount, long promoId,
                                           String promoType, String paymentMedthod, long contractId, double deduction, final OnPayOrderRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "order/confirm" + getAccessToken();
        HashMap params = new HashMap();
        params.put("orderId", orderId);
        params.put("amount", amount);
        params.put("discount", discount);
        params.put("actualAmount", actualAmount);
        params.put("deduction", deduction);

        if(promoId > 0){
            params.put("promoId", promoId);
            params.put("promoType", promoType);
        }

        if (contractId > 0) {
            params.put("contractId", contractId);
        }

        params.put("paymentMethod", paymentMedthod);

        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {

                onRequestResult.onSuccess(jsonData);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }


    //我的订单 旧接口 order/list 没有open 状态（咖啡厨房确认状态）
    public static void requestMyOrder(Context context, String bizCode, long id, IStatusLayout statusLayout, final OnResponseMyOrderJson onResponseMyOrderJson){
        String url = SERVER_NORMAL + "order/newList" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        if (bizCode.length() > 0) {
            params.put("bizCode", bizCode);
        }

        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<ResponseOpenOrderJson> myOrderJson = gson.fromJson(jsonData,new TypeToken<List<ResponseOpenOrderJson>>() {}.getType());
                onResponseMyOrderJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseMyOrderJson.onError(error);
            }
        });
    }

    // 获取订单详情
    public static void requestOrderDetail(Context context, long orderId, IStatusLayout statusLayout, final OnResponseOrderDetail onResponseOrderDetail){
        String url = SERVER_NORMAL + "order/detail" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", orderId);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson orderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onResponseOrderDetail.onSuccess(orderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseOrderDetail.onError(error);
            }
        });
    }

    //创建预约会议室
    public static void requestCreateMeetingRoomBooking(Context context, long facilityId, String title, long startTime, long endTime, String peopleNum, String remark, List<AddedValueModel> addedValueModels, IStatusLayout statusLayout, final OnResponseOpenOrderJson onCreateMeetingRoomBookingJson) {

        String url = SERVER_NORMAL + "booking/createMeetingRoomBooking"  + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", facilityId);
        if(StringUtils.isNotEmpty(title)){
            params.put("title", title);
        }
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        if(StringUtils.isNotEmpty(peopleNum)){
            params.put("peopleNum",peopleNum);
        }
        if(StringUtils.isNotEmpty(remark)){
            params.put("remark", remark);
        }
        if(addedValueModels != null && addedValueModels.size() > 0){
            params.put("addedValues", addedValueModels);
        }
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson responseOpenOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onCreateMeetingRoomBookingJson.onSuccess(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onCreateMeetingRoomBookingJson.onError(error);
            }
        });
    }

    //我的预约
    public static void requestMyAppointment(Context context,int pageIndex, int pageSize,int draw,String type,String date,final OnResponseMyAppointmentJson onResponseMyAppointmentJson){
        String url = SERVER_NORMAL + "booking/getMyBookings" + getAccessToken();
        HashMap params = new HashMap();
        params.put("page", pageIndex);
        params.put("size", pageSize);
        params.put("draw", draw);
        params.put("type", type);
        params.put("date", date);

        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyAppointmentJson myOrderJson = gson.fromJson(jsonData,ResponseMyAppointmentJson.class);
                onResponseMyAppointmentJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyAppointmentJson.onError(error);
            }
        });
    }

    //我的预约按状态查
    public static void requestMyAppointment(Context context, String type, String state, int pageIndex, int pageSize, int draw, IStatusLayout statusLayout,final OnResponseMyAppointmentJson onResponseMyAppointmentJson){
        String url = SERVER_NORMAL + "booking/getMyBookings" + getAccessToken();
        HashMap params = new HashMap();
        params.put("page", pageIndex);
        params.put("size", pageSize);
        params.put("draw", draw);
        params.put("type", type);
        params.put("state", state);

        NetworkUtil.requestJson(context, url, params,statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyAppointmentJson myOrderJson = gson.fromJson(jsonData,ResponseMyAppointmentJson.class);
                onResponseMyAppointmentJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyAppointmentJson.onError(error);
            }
        });
    }

    //我的预约
    public static void requestMyAppointment(Context context, String date, String type, final OnResponseMyAppointmentJson onResponseMyAppointmentJson){
        String url = SERVER_NORMAL + "booking/getMyBookings" + getAccessToken();
        HashMap params = new HashMap();
        params.put("page", 0);
        params.put("size", 100000);
        params.put("draw", 0);
        params.put("date", date);
        params.put("type", type);

       /* params.put("startTime", startTime);
        params.put("endTime", endTime);
*/
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyAppointmentJson myOrderJson = gson.fromJson(jsonData,ResponseMyAppointmentJson.class);
                onResponseMyAppointmentJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyAppointmentJson.onError(error);
            }
        });
    }

    //我的余额，我的账户信息
    public static void requestMyInfo(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseMyInfoJson onResponseMyInfoJson){
        String url = SERVER_NORMAL + "appUser/getAccount" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyInfoJson myInfoJson = gson.fromJson(jsonData,new TypeToken<ResponseMyInfoJson>() {}.getType());
                onResponseMyInfoJson.onSuccess(myInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyInfoJson.onError(error);
            }
        });
    }

    //我的积分
    public static void requestMyScore(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseMyScoreJson OnResponseMyScoreJson){
        String url = SERVER_NORMAL + "appUser/bonuspoints" + getAccessToken();
        HashMap params = new HashMap();
//        params.put("","");
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                MyScoreJson myInfoJson = gson.fromJson(jsonData,new TypeToken<MyScoreJson>() {}.getType());
                OnResponseMyScoreJson.onSuccess(myInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                OnResponseMyScoreJson.onError(error);
            }
        });
    }

    //兑换积分
    public static void requestMyScoreTrans(Context context, boolean showProgreeDialog, IStatusLayout statusLayout,int id, final OnResponseScoreTranJson OnResponseScoreTranJson){
        String url = SERVER_NORMAL + "bonuspointscoupon/transform" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("requestMyScoreTrans","jsonData = " + jsonData);
//                if (responseCode == 0)
                    OnResponseScoreTranJson.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                OnResponseScoreTranJson.onError(error);
            }
        });
    }

    //分享积分
    public static void shareScore(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseScoreTranJson OnResponseScoreTranJson){
        String url = SERVER_NORMAL + "gainbonuspoints/gainbysharewechat" + getAccessToken();
        HashMap params = new HashMap();
//        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("requestMyScoreTrans","jsonData = " + jsonData);
                if (responseCode == 0)
                    OnResponseScoreTranJson.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

//                OnResponseScoreTranJson.onError(error);
            }
        });
    }

    //蓝牙开门积分
    public static void doorScore(Context context, boolean showProgreeDialog,String id, IStatusLayout statusLayout){
        String url = SERVER_NORMAL + "gainbonuspoints/gainbyopendoor" + getAccessToken();
        HashMap params = new HashMap();
        params.put("unitId",id);
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
            }

            @Override
            public void onError(DcnException error) {

//                OnResponseScoreTranJson.onError(error);
            }
        });
    }

    //l蓝牙开门状态
    public static void doorState(Context context,long doorId,String result, boolean showProgreeDialog, IStatusLayout statusLayout){
        String url = SERVER_NORMAL + "openlockapi/saveOpenLog" + getAccessToken();
        HashMap params = new HashMap();
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        params.put("doorId",doorId);
        params.put("result",result);
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
            }

            @Override
            public void onError(DcnException error) {

//                OnResponseScoreTranJson.onError(error);
            }
        });
    }
    //积分明细
    public static void requestMyScoreDetail(Context context, boolean showProgreeDialog, IStatusLayout statusLayout,int page, final OnResponseScoreDetailListJson OnResponseScoreDetailListJson){
        String url = SERVER_NORMAL + "bonuspoints/history" + getAccessToken();
        Log.i("requestMyScore","url = " + url);
        HashMap params = new HashMap();
        params.put("page",page);
        params.put("size",10);
        params.put("sort",new String[]{"createTime_DESC"});
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("requestMyScore","jsonData = " + jsonData);
                Gson gson = new Gson();
                ScoreDetailJson myInfoJson = gson.fromJson(jsonData,new TypeToken<ScoreDetailJson>() {}.getType());
                OnResponseScoreDetailListJson.onSuccess(myInfoJson);
            }

            @Override
            public void onError(DcnException error) {
                OnResponseScoreDetailListJson.onError(error);
            }
        });
    }

    //积分兑换
    public static void requestMyScore(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseScoreListJson onResponseScoreListJson){
        String url = SERVER_NORMAL + "bonuspointscoupon/all" + getAccessToken();
        Log.i("requestMyScore","url = " + url);
        HashMap params = new HashMap();
//        params.put("","");
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("requestMyScore","jsonData = " + jsonData);
                Gson gson = new Gson();
                List<ScoreGetJson> myInfoJson = gson.fromJson(jsonData,new TypeToken<List<ScoreGetJson>>() {}.getType());
                onResponseScoreListJson.onSuccess(myInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseScoreListJson.onError(error);
            }
        });
    }


    //积分兑换记录
    public static void requestMyScoreGetDetail(Context context, boolean showProgreeDialog, IStatusLayout statusLayout,int page, final OnResponseScoreGetListJson OnResponseScoreGetListJson){
        String url = SERVER_NORMAL + "bonuspointscoupon/history" + getAccessToken();
        Log.i("requestMyScore","url = " + url);
        HashMap params = new HashMap();
        params.put("page",page);
        params.put("size",10);
//        params.put("sort","[\"createTime_DESC\"]");
        params.put("sort",new String[]{"createTime_DESC"});
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("requestMyScore","jsonData = " + jsonData);
                Gson gson = new Gson();
                ScoreGetDetailJson myInfoJson = gson.fromJson(jsonData,new TypeToken<ScoreGetDetailJson>() {}.getType());
                OnResponseScoreGetListJson.onSuccess(myInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                OnResponseScoreGetListJson.onError(error);
            }
        });
    }

    //我的二维码
    public static void requestMyCode(Context context, final OnResponseMyCodeJson onResponseMyCodeJson){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "token/me" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestFullJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyCodeJson myCodeJson = gson.fromJson(jsonData,ResponseMyCodeJson.class);
                onResponseMyCodeJson.onSuccess(myCodeJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyCodeJson.onError(error);
            }
        });
    }


    //二维码订单查询
    public static void requestCodeOrder(Context context, long time, long contractId, final OnResponseOpenOrderJson onResponseOpenOrderJson){
        String url = SERVER_NORMAL + "order/query" + getAccessToken();
        HashMap params = new HashMap();
        params.put("timestamp", time);
        if (contractId > 0) {
            params.put("contractId", contractId);
        }
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                if (jsonData != null) {
                    Gson gson = new Gson();
                    ResponseOpenOrderJson myOrderJson = gson.fromJson(jsonData, new TypeToken<ResponseOpenOrderJson>() {
                    }.getType());
                    onResponseOpenOrderJson.onSuccess(myOrderJson);
                } else {
                    onResponseOpenOrderJson.onSuccess(null);
                }
            }

            @Override
            public void onError(DcnException error) {

                onResponseOpenOrderJson.onError(error);
            }
        });
    }

    //获取订单折扣优惠券
    public static void requestSuggest(Context context, long orderId, long contractId, IStatusLayout statusLayout, final OnSuggestRequestResult onSuggestRequestResult){
        String url = SERVER_NORMAL + "order/suggest" + getAccessToken();
        HashMap params = new HashMap();
        if (contractId > 0) {
            params.put("contractId", contractId);
        }
        params.put("orderId", orderId);
        NetworkUtil.requestJson(context, url, params,  statusLayout,  new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                SuggestResponseModel suggestResponseModel = gson.fromJson(jsonData,SuggestResponseModel.class);
                onSuggestRequestResult.onSuccess(suggestResponseModel);
            }

            @Override
            public void onError(DcnException error) {
                onSuggestRequestResult.onError(error);
            }
        });
    }

    //我的优惠券
    public static void requestMyCoupon(Context context, int state, IStatusLayout statusLayout, final OnMyCouponsRequestResult onMyCouponsRequestResult){
        String url = SERVER_NORMAL + "marketing/binded" + getAccessToken();
        HashMap params = new HashMap();
        params.put("state", state);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<UseableCouponsModel> myCouponJson = gson.fromJson(jsonData,new TypeToken<List<UseableCouponsModel>>() {}.getType());
                onMyCouponsRequestResult.onSuccess(myCouponJson);
            }

            @Override
            public void onError(DcnException error) {
                onMyCouponsRequestResult.onError(error);

            }
        });
    }

    //领卷中心已登陆
    public static void requestCouponsCenterWithToken(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnCouponsCenterRequestResult onCouponsCenterRequestResult){
        String url = SERVER_NORMAL + "marketing/bindable" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<CouponModel> couponListJson = gson.fromJson(jsonData,new TypeToken<List<CouponModel>>() {}.getType());
                onCouponsCenterRequestResult.onSuccess(couponListJson);
            }

            @Override
            public void onError(DcnException error) {

                onCouponsCenterRequestResult.onError(error);
            }
        });
    }

    //领卷中心
    public static void requestCouponsCenter(Context context,final OnCouponsCenterRequestResult onCouponsCenterRequestResult){
        String url = SERVER_NORMAL + "marketing/open/bindable";
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<CouponModel> couponListJson = gson.fromJson(jsonData,new TypeToken<List<CouponModel>>() {}.getType());
                onCouponsCenterRequestResult.onSuccess(couponListJson);
            }

            @Override
            public void onError(DcnException error) {

                onCouponsCenterRequestResult.onError(error);
            }
        });
    }

    //领取优惠券
    public static void requestTakeCoupon(Context context, long couponId, final OnRequestResult onRequestResult){
        String url = SERVER_NORMAL + "marketing/bind" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", couponId);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //用户充值
    public static void requestRechange(Context context, BigDecimal amount,String referral, final OnResponseOpenOrderJson onResponseOpenOrderJson) {

        String url = SERVER_NORMAL + "order/recharge"  + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        params.put("amount", amount);
        params.put("referral", referral);

        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson responseOpenOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onResponseOpenOrderJson.onSuccess(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseOpenOrderJson.onError(error);
            }
        });
    }

    //充值记录
    public static void requestMyChargeOrder(Context context,long maxId,final OnResponseMyOrderJson onResponseMyOrderJson){
        String url = SERVER_NORMAL + "order/recharges" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", maxId);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<ResponseOpenOrderJson> myOrderJson = gson.fromJson(jsonData,new TypeToken<List<ResponseOpenOrderJson>>() {}.getType());
                onResponseMyOrderJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {

                onResponseMyOrderJson.onError(error);
            }
        });
    }


    //余额明细
    public static void requestTransactions(Context context,long maxId, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseTransactionsJson onResponseTransactionsJson){
        String url = SERVER_NORMAL + "appUser/transactions" + getAccessToken();
        HashMap params = new HashMap();
        if(id > 0){
            params.put("id", maxId);
        }
        NetworkUtil.requestJson(context, url, params, showProgreeDialog,  statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<TransactionsJson> myOrderJson = gson.fromJson(jsonData,new TypeToken<List<TransactionsJson>>() {}.getType());
                onResponseTransactionsJson.onSuccess(myOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseTransactionsJson.onError(error);
            }
        });
    }



    //余额支付
    public static void requestAccountPay(Context context,long orderId, String pwd, long contractId, final OnRequestResult onRequestResult){
        String url = SERVER_NORMAL + "payment/confirm/accountPay" + getAccessToken();
        HashMap params = new HashMap();
        params.put("orderId", orderId);
        params.put("password", pwd);
        if (contractId > 0) {
            params.put("contractId", contractId);
        }
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<ResponseOpenOrderJson> myOrderJson = gson.fromJson(jsonData,new TypeToken<List<ResponseOpenOrderJson>>() {}.getType());
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //余额支付
    public static void requestAccountPayV2(Context context,long orderId, String pwd, long contractId, String payPasswordStatus, String checkCode, final OnRequestResult onRequestResult){
        String url = SERVER_NORMAL_V3 + "payment/confirm/accountPay" + getAccessToken();
        HashMap params = new HashMap();
        params.put("orderId", orderId);
        params.put("password", pwd);
        params.put("payPasswordStatus", payPasswordStatus);
        params.put("checkCode", checkCode);
        params.put("deviceId", AppUtil.getUUID(context));
        if (contractId > 0) {
            params.put("contractId", contractId);
        }
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<ResponseOpenOrderJson> myOrderJson = gson.fromJson(jsonData,new TypeToken<List<ResponseOpenOrderJson>>() {}.getType());
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }


    //请求校验码
    public static void requestPaymentCheckCode(Context context, long orderId, final OnResponsePaymentCheckCode onResponsePaymentCheckCode){
        String url = SERVER_NORMAL + "payment/checkCode" + getAccessToken();
        HashMap params = new HashMap();
        params.put("orderId", orderId);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CheckCodeJson checkCodeJson = gson.fromJson(jsonData, CheckCodeJson.class);
                onResponsePaymentCheckCode.onSuccess(checkCodeJson.getCheckCode());
            }

            @Override
            public void onError(DcnException error) {
                onResponsePaymentCheckCode.onError(error);
            }
        });
    }



    //私教 列表
    public static void requestGetCoachs(Context context, int pageIndex, int pageSize, long unitId, boolean isShowProgreeDialog, IStatusLayout statusLayout, final OnCoachsRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "fit/getCoachs";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        //params.put("draw", 1);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize,isShowProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CoachsListJson coachsListJson = gson.fromJson(jsonData, CoachsListJson.class);
                onRequestResult.onSuccess(coachsListJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //获取课程列表 返回七天内的课程预约
    public static void requestGetLessons(Context context, int pageIndex, int pageSize, long unitId, long startTime, long endTime, boolean showProgreeDialog, IStatusLayout statusLayout, final OnLessonsRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "fit/getLessons";
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                LessonsListJson lessonsJson = gson.fromJson(jsonData, LessonsListJson.class);
                onRequestResult.onSuccess(lessonsJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //有健身合同或权益
    public static void requestCheckFitContractOrAllowance(Context context, IStatusLayout statusLayout, final OnResponseBoolean onResponseBoolean){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "appUser/checkFitContractOrAllowance"+ getAccessToken();;
        HashMap params = new HashMap();

        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                boolean is = gson.fromJson(jsonData, Boolean.class);
                onResponseBoolean.onSuccess(is);
            }

            @Override
            public void onError(DcnException error) {
                onResponseBoolean.onError(error);
            }
        });


    }

    //获取课程详细信息
    public static void requestGetLesson(Context context, long id, final OnLessonRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "fit/getLesson";
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                LessonJson lessonJson = gson.fromJson(jsonData, LessonJson.class);
                onRequestResult.onSuccess(lessonJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //获取课时列表
    public static void requestGetClasses(Context context, int pageIndex, int pageSize, long lessonId, IStatusLayout statusLayout, final OnClassesRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "fit/getClasses";
        HashMap params = new HashMap();
        params.put("lessonId", lessonId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, true , statusLayout,new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ClassesJson classesJson = gson.fromJson(jsonData, ClassesJson.class);
                onRequestResult.onSuccess(classesJson);
            }

            @Override
            public void onError(DcnException error) {

                onRequestResult.onError(error);
            }
        });
    }

    //创建课时
    public static void requestCreateClassBooking(Context context, long id, String title, final OnResponseOpenOrderJson onRequestResult) {

        String url = SERVER_NORMAL + "booking/createClassBooking" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        params.put("title", title);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseOpenOrderJson responseOpenOrderJson = gson.fromJson(jsonData, ResponseOpenOrderJson.class);
                onRequestResult.onSuccess(responseOpenOrderJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //预约服务
    public static void requestService(Context context, long unitId, long productId, final OnRequestResult onRequestResult) {

        String url = SERVER_NORMAL + "appUser/requestService"  + getAccessToken();;
        HashMap params = new HashMap();
        params.put("unitId", unitId);
        params.put("productId", productId);
        NetworkUtil.requestJson(context, url, params,true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取我是法人或授权人的企业列表
    public static void requestGetVisibleCompanys (Context context,boolean showProgreeDialog, IStatusLayout statusLayout, final OnVisibleCompanysRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getVisibleCompanys" + getAccessToken();;
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                VisibleCompanysJson visibleCompanysJson = gson.fromJson(jsonData, VisibleCompanysJson.class);
                onRequestResult.onSuccess(visibleCompanysJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //修改企业信息
    public static void requestUpdateCompany(Context context, long id, String name, String contact, String phone, String fax, String city, String address, String thumbnail, String description, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/updateCompany" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        params.put("name", name);
        params.put("contact", contact);
        params.put("phone", phone);
        params.put("fax", fax);
        params.put("city", city);
        params.put("address", address);
        params.put("thumbnail", thumbnail);
        params.put("description", description);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取某企业成员列表
    public static void requestGetCompanyMembers (Context context, long id, IStatusLayout statusLayout, final OnCompanyMembersRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getCompanyMembers" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CompanyMembersJson companyMembersJson = gson.fromJson(jsonData, CompanyMembersJson.class);
                onRequestResult.onSuccess(companyMembersJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //设置授权人
    public static void requestSetAuthMember(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/setAuthMember" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //取消授权人
    public static void requestCancelAuthMember(Context context, long id, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/cancelAuthMember" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取某公司下生效合同列表
    public static void requestGetCompanyContracts (Context context, long id, final OnContractsRequestRescult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getCompanyContracts" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ContractsJson contractsJson = gson.fromJson(jsonData, ContractsJson.class);
                onRequestResult.onSuccess(contractsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //通过中心ID获取我是法人或授权人的合同列表
    public static void requestMyContracts(Context context, long unitId, boolean showProgreeDialog, IStatusLayout statusLayout, final OnContractsRequestRescult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getVisibleContracts" + getAccessToken();;
        HashMap params = new HashMap();

        NetworkUtil.requestJson(context, url, params, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ContractsJson contractsJson = gson.fromJson(jsonData, ContractsJson.class);
                onRequestResult.onSuccess(contractsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取某合同的企业账单列表
    public static void requestGetContractBillings (Context context, int pageIndex, int pageSize, long contractId, boolean showProgreeDialog, IStatusLayout statusLayout, final OnBillingsRequestRescult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getContractBillings" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("contractId", contractId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, showProgreeDialog, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                BillingsJson billingsJson = gson.fromJson(jsonData, BillingsJson.class);
                onRequestResult.onSuccess(billingsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取某企业账单详细列表
    public static void requestGetBillingItems (Context context, int pageIndex, int pageSize, long billingId, IStatusLayout statusLayout, final OnBillingItemsRequestRescult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getBillingItems" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("billingId", billingId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                BillingItemsJson billingItemsJson = gson.fromJson(jsonData, BillingItemsJson.class);
                onRequestResult.onSuccess(billingItemsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //获取未出账单列表
    public static void requestGetNoBillingItems (Context context, int pageIndex, int pageSize, long contractId, IStatusLayout statusLayout, final OnNoBillingItemsRequestRescult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/getNoBillingItems" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("contractId", contractId);
        NetworkUtil.requestJsonRow(context, url, params, pageIndex, pageSize, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                NoBillingItemsJson nobillingItemsJson = gson.fromJson(jsonData, NoBillingItemsJson.class);
                onRequestResult.onSuccess(nobillingItemsJson);
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //额度查询
    public static void requestGetCompanyLimit(Context context, long id,  IStatusLayout statusLayout, final CorporationMoneyJsonRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "contract/getContractAccount" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", id);
//        params.put("contractId", id);

        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                CorporationMoneyJson nobillingItemsJson = gson.fromJson(jsonData, CorporationMoneyJson.class);
                onRequestResult.onSuccess(nobillingItemsJson);
            }

            @Override
            public void onError(DcnException error) {
//                onRequestResult.onError(error);
            }
        });

    }

    //打印记录页面
    public static void getPrint(Context context,int page,String type, IStatusLayout statusLayout, final MyPrintRequestRescult myPrintRequestRescult){
        String url = SERVER_NORMAL + "printer/pringjobdetail" + getAccessToken();
        HashMap params = new HashMap();
        params.put("size",10);
        params.put("page",page);
        params.put("type",type);
        params.put("sort",new String[]{"createTime_DESC"});
        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PrintDataJson printJason = gson.fromJson(jsonData,new TypeToken<PrintDataJson>() {}.getType());
                myPrintRequestRescult.onSuccess(printJason);
            }

            @Override
            public void onError(DcnException error) {
                myPrintRequestRescult.onError(error);
            }
        });
    }


    //修改手机号码
    public static void requestUpdateMobile(Context context,String zipCode, String mobile, String code, final OnRequestValidCode onValidCode) {

        String url = SERVER_NORMAL + "appUser/updateMobile" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("zipCode", zipCode);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PersonInfoJson personInfoJson = gson.fromJson(jsonData, PersonInfoJson.class);
                onValidCode.onSuccess(personInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onValidCode.onError(error);
            }
        });
    }

    //意见反馈
    public static void requestFeedback(Context context, String content, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/addFeedback" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("content", content);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    public static void requestFeedbackNotLogin(Context context, String content, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "appUser/addFeedbackNotLogin";
        HashMap params = new HashMap();
        params.put("content", content);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }

    //版本更新检测
    public static void requestUpdateApp(Context context, final OnRequestResult onRequestResult) {
        String url = SERVER_NORMAL + "common/getVersionInfo";
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onRequestResult.onError(error);
            }
        });
    }


    //注册环信
    public static void requestRegisterIM(Context context,final OnRequestResult onRequestResult){
    String url =  SERVER_NORMAL+"appUser/createEasemobUser" + getAccessToken();
        HashMap<String,Object> params = new HashMap<>();
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Log.i("Test","注册环信成功----");
                onRequestResult.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Log.i("Test","注册环信失败----");
                onRequestResult.onError(error);
            }
        });
    }

    //优惠券二维码
    public static void requestCouponcode(Context context, String  tag, long id, final OnResponseMyCodeJson onResponseMyCodeJson) {

        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "token/couponbind" + getAccessToken();
        HashMap params = new HashMap();
        params.put("id", id);
        NetworkUtil.requestFullJson(context, tag ,url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ResponseMyCodeJson myCodeJson = gson.fromJson(jsonData,ResponseMyCodeJson.class);
                onResponseMyCodeJson.onSuccess(myCodeJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseMyCodeJson.onError(error);
            }
        });
    }

    public static void asyncUploadFile(Context context, String fileName, String uploadName, final AliOSS.OnUpload onUpload) {
        GlobalParams globalParams = GlobalParams.getInstance();

        String tokenUrl = SERVER_NORMAL+"appUser/getOSSToken";
        String userAccessToken = globalParams.getAccessToken();
        String endPoint = globalParams.requestUrl.SERVER_OSS;
        String bucketName = globalParams.getOssBucketName();
        String downloadUrlPrefix = globalParams.requestUrl.OSS_DOWNLOAD_URL;

        Logger.e(tokenUrl + "  " + userAccessToken  + "  " +  endPoint   + "  " +  bucketName   + "  " + downloadUrlPrefix   + "  " +fileName + "  " +  uploadName);
        AliOSS.asyncUploadDataToOSS(context, tokenUrl, userAccessToken, endPoint,
                bucketName, uploadName, fileName,
                downloadUrlPrefix, new AliOSS.OnUpload() {
                    @Override
                    public void onSuccess(String downloadUrl) {
                        onUpload.onSuccess(downloadUrl);
                    }

                    @Override
                    public void onError(DcnException error) {
                        onUpload.onError(error);
                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize) {
                        onUpload.onProgress(currentSize, totalSize);
                    }
                });
    }


    public static void asyncUploadAvata(Context context, String fileName, final AliOSS.OnUpload onUpload) {
        String upName = UUID.randomUUID().toString().substring(26).toLowerCase();
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        String uploadName = "atlas/avata/"+dateToStringWithoutSplit(new Date()) + "/" + upName + prefix;
        asyncUploadFile(context, fileName, uploadName, new AliOSS.OnUpload() {
            @Override
            public void onSuccess(String downloadUrl) {
                onUpload.onSuccess(downloadUrl);
            }

            @Override
            public void onError(DcnException error) {
                onUpload.onError(error);
            }

            @Override
            public void onProgress(long currentSize, long totalSize) {
                onUpload.onProgress(currentSize, totalSize);
            }
        });
    }


    //二维码获取用户信息
    public static void requestCodeUserInfo(Context context, String code,IStatusLayout statusLayout, final OnLoginUserInfo onLoginUserInfo) {

        String url = SERVER_NORMAL + "appUser/findByUserQRCode" + getAccessToken();
        HashMap params = new HashMap();
        params.put("qrcode", code);
        NetworkUtil.requestJson(context, url, params,statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PersonInfoJson personInfoJson = gson.fromJson(jsonData, PersonInfoJson.class);
                onLoginUserInfo.onSuccess(personInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onLoginUserInfo.onError(error);
            }
        });
    }

    //根据UID获取用户信息
    public static void requestUidUserInfo(Context context, ArrayList<String> arrayList, final OnUidUserInfo onUidUserInfo) {

        String url = ((com.atlas.crmapp.huanxin.BaseActivity)context).getGlobalParams().requestUrl.SERVER_NORMAL + "appUser/getUsers" + getAccessToken();
        NetworkUtil.requestJson(context, url, arrayList,false , new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<PersonInfoJson> personList = gson.fromJson(jsonData,new TypeToken<List<PersonInfoJson>>() {}.getType());

                onUidUserInfo.onSuccess(personList);
            }

            @Override
            public void onError(DcnException error) {

                onUidUserInfo.onError(error);
            }
        });
    }

    //根据UID获取用户信息
    public static void requestUidUserInfoNormal(Context context, ArrayList<String> arrayList, final OnUidUserInfo onUidUserInfo) {

        String url = SERVER_NORMAL + "appUser/getUsers" + getAccessToken();
        NetworkUtil.requestJson(context, url, arrayList,false , new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<PersonInfoJson> personList = gson.fromJson(jsonData,new TypeToken<List<PersonInfoJson>>() {}.getType());

                onUidUserInfo.onSuccess(personList);
            }

            @Override
            public void onError(DcnException error) {

                onUidUserInfo.onError(error);
            }
        });
    }

    //根据UID获取用户信息
    public static void requestUidUserInfoNormal(Context context, String mine,String others, final OnUidUserInfo_ onUidUserInfo) {

        String url = SERVER_NORMAL + "tls/anon/getRelateInfo" ;
        HashMap params = new HashMap();
        if (!StringUtils.isEmpty(mine))
            params.put("myId",mine);
        params.put("otherId",others);
        Log.i("UserCardActivity"," requestUidUserInfoNormal = " + others);
        NetworkUtil.requestJson(context, url, params,false , new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PersonInfoJson personList = gson.fromJson(jsonData,PersonInfoJson.class);

                onUidUserInfo.onSuccess(personList);
            }

            @Override
            public void onError(DcnException error) {

                onUidUserInfo.onError(error);
            }
        });
    }

    //获取企业权益优惠券列表
    public static void requestListAllowanceContractCoupons(Context context, long contractId, final OnListAllowanceContractCouponsRequestResult onListAllowanceContractCouponsRequestResult) {
        String url = SERVER_NORMAL + "appUser/listAllowanceContractCoupons" + getAccessToken();;
        HashMap params = new HashMap();
        params.put("id", contractId);
        NetworkUtil.requestJson(context, url, params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                AllowanceContractCouponsListJson allowanceContractCouponsListJson = gson.fromJson(jsonData, AllowanceContractCouponsListJson.class);
                onListAllowanceContractCouponsRequestResult.onSuccess(allowanceContractCouponsListJson);
            }

            @Override
            public void onError(DcnException error) {
                onListAllowanceContractCouponsRequestResult.onError(error);
            }
        });
    }

    //获取支付信息
    public static void requestPayInfo(Context context, final OnResponsePayInfoJson onPayInfo) {

        String url = SERVER_NORMAL + "paySetting/getSetting" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url,params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PayInfoJson payInfoJson = gson.fromJson(jsonData,PayInfoJson.class);

                onPayInfo.onSuccess(payInfoJson);
            }

            @Override
            public void onError(DcnException error) {

                onPayInfo.onError(error);
            }
        });
    }

    //上传设备信息
    public static void requestUploadDevice(Context context, String deviceId, final  OnResponseUploadDevice onResponseUploadDevice){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "appUser/uploadDevice" + getAccessToken();
        HashMap params = new HashMap();
        params.put("deviceId" , deviceId);
        params.put("type" ,"android");
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onResponseUploadDevice.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                onResponseUploadDevice.onError(error);
            }
        });

    }



    //  订单业态用于订单
    public static void requestOnGetOrderBusinesses(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseGetBusinesses onResponseGetBusinesses){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "order/getBusinesses" + getAccessToken();
        HashMap params = new HashMap();

        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<BusinesseModel> businesseModels = gson.fromJson(jsonData,new TypeToken<List<BusinesseModel>>() {}.getType());
                onResponseGetBusinesses.onSuccess(businesseModels);
            }

            @Override
            public void onError(DcnException error) {
                onResponseGetBusinesses.onError(error);
            }
        });
    }

    //全部业态用于领券中心
    public static void requestOnGetCommonBusinesses(Context context, boolean showProgreeDialog, IStatusLayout statusLayout, final OnResponseGetBusinesses onResponseGetBusinesses){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "common/getBusinesses" ;
        HashMap params = new HashMap();

        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<BusinesseModel> businesseModels = gson.fromJson(jsonData,new TypeToken<List<BusinesseModel>>() {}.getType());
                onResponseGetBusinesses.onSuccess(businesseModels);
            }

            @Override
            public void onError(DcnException error) {
                onResponseGetBusinesses.onError(error);
            }
        });
    }


    //获取app更新信息
    public static void requestVersionInfo(Context context,final OnResponseVersionInfoJson onResponseVersionInfoJson){
        String url = "https://testorder.crm.atlasoffice.cn/app/v1" + "/common/getVersionInfo";
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                VersionInfoJson versionInfoJson = gson.fromJson(jsonData,VersionInfoJson.class);
                onResponseVersionInfoJson.onSuccess(versionInfoJson);
            }

            @Override
            public void onError(DcnException error) {
                onResponseVersionInfoJson.onError(error);
            }
        });
    }

    //获取城市列表
    public static void requestCityCenterList(Context context,IStatusLayout statusLayout , final OnResponseGetCenterList onResponseGetCenterList){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "centerSwitchApi/getCenterList" ;
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url,  params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<CityCenterJson> cityCenterJsons = gson.fromJson(jsonData, new TypeToken<List<CityCenterJson>>() {}.getType());
                onResponseGetCenterList.onSuccess(cityCenterJsons);
            }

            @Override
            public void onError(DcnException error) {
                onResponseGetCenterList.onError(error);
            }
        });
    }






    //检验 支付密码
    public static void requestCheckUserAccountPassword(Context context, String pws, final OnResponseCheckUserAccountPassword onResponseCheckUserAccountPassword){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "appUser/checkUserAccountPassword" + getAccessToken();
        HashMap params = new HashMap();
        params.put("password", pws);//需要md5加密
        NetworkUtil.requestJson(context, url,  params, true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();

                /*"isVerify": 1  校验成功
                    "isVerify": 0 校验失败*/
                CheckUserAccountPasswordJson accountPasswordJson = gson.fromJson(jsonData, CheckUserAccountPasswordJson.class);
                onResponseCheckUserAccountPassword.onSuccess(accountPasswordJson.getIsVerify());
            }

            @Override
            public void onError(DcnException error) {
                onResponseCheckUserAccountPassword.onError(error);
            }
        });
    }

    //获取目前用户有权限打开 的 UUID列表
    public static void requestFindUUidList(Context context, String doorType, final OnResponseFindUUidList onResponseFindUUidList){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/findUUidList" + getAccessToken();
        HashMap params = new HashMap();
        params.put("doorType",doorType);
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url,  params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<LockJson> lockJsons = gson.fromJson(jsonData, new TypeToken<List<LockJson>>() {}.getType());
                onResponseFindUUidList.onSuccess(lockJsons);
            }

            @Override
            public void onError(DcnException error) {
                onResponseFindUUidList.onError(error);
            }
        });
    }

    //根据token和搜索到的UUID列表获取当前用户可开门列表
    public static void requestFindLockList(Context context, String uuid, final OnResponseFindLockList onResponseFindLockList){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/findLockList" + getAccessToken();
        //{"uuid":"00096547,00096864"}
        HashMap params = new HashMap();
        params.put("uuid",uuid);
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url,  params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<LockJson> lockJsons = gson.fromJson(jsonData, new TypeToken<List<LockJson>>() {}.getType());
                onResponseFindLockList.onSuccess(lockJsons);
            }
            @Override
            public void onError(DcnException error) {
                onResponseFindLockList.onError(error);
            }
        });
    }

    //根据门禁ID开锁
    public static void requestRemoteOpenLock(Context context, long doorId, String powner, final OnResponseRemoteOpenLock onResponseRemoteOpenLock){
        HashMap params = new HashMap();
        params.put("doorId", doorId);
        if(StringUtils.isNotEmpty(powner)){
            params.put("power", powner);
        }
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/RemoteOpenLock" + getAccessToken();
        NetworkUtil.requestJson(context, url,  params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                RemoteOpenLockJson remoteOpenLockJson = gson.fromJson(jsonData, RemoteOpenLockJson.class);
                onResponseRemoteOpenLock.onSuccess(remoteOpenLockJson);
            }
            @Override
            public void onError(DcnException error) {
                onResponseRemoteOpenLock.onError(error);
            }
        });
    }


    //获取收藏的门列表
    public static void requestFindCollectList(Context context, final  OnResponseFindCollectList onResponseFindCollectList){
        HashMap params = new HashMap();
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/findCollectList" + getAccessToken();
        NetworkUtil.requestJson(context, url,  params,  true, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<LockJson> lockJsons = gson.fromJson(jsonData, new TypeToken<List<LockJson>>() {}.getType());
                onResponseFindCollectList.onSuccess(lockJsons);
            }
            @Override
            public void onError(DcnException error) {
                onResponseFindCollectList.onError(error);
            }
        });
    }

    //收藏门锁
    public static void requestCollectDoor(Context context, List<CollectLockItem> collectLockItems , IStatusLayout statusLayout, final OnResponseCollectDoor onResponseCollectDoor){
        HashMap params = new HashMap();
        Gson g = new Gson();
        params.put("itemDto", collectLockItems);
        params.put("unitId", GlobalParams.getInstance().getAtlasId());
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/collectDoor" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onResponseCollectDoor.onSuccess();
            }
            @Override
            public void onError(DcnException error) {
                onResponseCollectDoor.onError(error);
            }
        });
    }

    //最近门锁
    public static void requestFindLastestOpenList(Context context, long unitId, IStatusLayout statusLayout, final  OnResponsefindLastestOpenList onResponsefindLastestOpenList){
        HashMap params = new HashMap();
        Gson g = new Gson();
        params.put("unitId", unitId);
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "openlockapi/findLastestOpenList" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<LockJson> lockJsons = gson.fromJson(jsonData, new TypeToken<List<LockJson>>() {}.getType());
                onResponsefindLastestOpenList.onSuccess(lockJsons);
            }
            @Override
            public void onError(DcnException error) {
                onResponsefindLastestOpenList.onError(error);
            }
        });
    }


    //活动核销
    public static void requestActivityConsumed(Context context, int verificationNum, String serialNum,  IStatusLayout statusLayout, final  OnResponseConsumed onResponseConsumed){
        HashMap params = new HashMap();
        params.put("verificationNum", verificationNum);
        params.put("code", serialNum);
        String url = SERVER_NORMAL_V2 + "activity/consumed" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ConsumedJson consumedJson = gson.fromJson(jsonData, ConsumedJson.class);
                onResponseConsumed.onSuccess(consumedJson);

            }
            @Override
            public void onError(DcnException error) {
                onResponseConsumed.onError(error);
            }
        });
    }

    //获取活动订单二维码 、 活动概要 /v2/activity/bindinfo
    public static void requestActivityBindinfo(Context context, long bookingId, long activityId, String serialNum,  IStatusLayout statusLayout, final  OnResponseBindinfo onResponseBindinfo){
        HashMap params = new HashMap();
        if(StringUtils.isNotEmpty(serialNum)){
            params.put("serialNum", serialNum);//二维码字段。与订单ID互斥，查询活动ID不传
        }
        if(bookingId != 0){
            params.put("bookingId", bookingId);//活动订单的id ,当使用订单查询时请传入(可选)。当以二维码查询时请不要传
        }

        if(activityId != 0){
            params.put("activityId", activityId);// 当bookingId，serialNum都为空时，传入activityId会跟据当前用户查询出参加活动的情况，未参加时会返回空
        }
        String url = SERVER_NORMAL_V2 + "activity/bindinfo" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ActivitiesBindinfoJson bindinfoJson = gson.fromJson(jsonData, ActivitiesBindinfoJson.class);
                onResponseBindinfo.onSuccess(bindinfoJson);
            }
            @Override
            public void onError(DcnException error) {
                onResponseBindinfo.onError(error);
            }
        });
    }

    //获取活动联系人
    public static void requestActivityUserinfo(Context context, long bookingId, IStatusLayout statusLayout, final  OnResponseActivityUserinfo onResponseActivityUserinfo){
        HashMap params = new HashMap();
        params.put("id", bookingId);
        String url = SERVER_NORMAL_V2 + "activity/userinfo" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ActivitesUserInfoJson userInfoJson = gson.fromJson(jsonData, ActivitesUserInfoJson.class);
                onResponseActivityUserinfo.onSuccess(userInfoJson);
            }
            @Override
            public void onError(DcnException error) {
                onResponseActivityUserinfo.onError(error);
            }
        });
    }



    //获取以往参加活动联系人信息、用于回显
    public static void requestActivityUsers(Context context,  IStatusLayout statusLayout, final  OnResponseActivityUsers onResponseActivityUsers){
        HashMap params = new HashMap();
        String url = SERVER_NORMAL_V2 + "activity/user" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<ActivitesUserInfoJson> userInfoJsons = gson.fromJson(jsonData, new TypeToken<List<ActivitesUserInfoJson>>() {}.getType());
                onResponseActivityUsers.onSuccess(userInfoJsons);
            }
            @Override
            public void onError(DcnException error) {
                onResponseActivityUsers.onError(error);
            }
        });
    }


    //获取用户的标签（判断扫码用户是否是csr角色）
    public static void requestAppUserGeteam(Context context, final  OnResponseAppUserGeteam onResponseAppUserGeteam){
        HashMap params = new HashMap();
        String url = SERVER_NORMAL + "appUser/geteam" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                GeteamJson geteamJson = gson.fromJson(jsonData, GeteamJson.class);
                onResponseAppUserGeteam.onSuccess(geteamJson.isGeteam());
            }
            @Override
            public void onError(DcnException error) {
                onResponseAppUserGeteam.onError(error);
            }
        });
    }

    //访客邀请
    public static void requestVisitorInvite(Context context, long unitId, String purpose, int visitorNum, long bookDate, IStatusLayout statusLayout, final OnResponseVisitorInvite onResponseVisitorInvite){
        HashMap params = new HashMap();
        String url = SERVER_NORMAL + "invitation" + getAccessToken();
        params.put("unitId", unitId);
        params.put("purpose", purpose);
        params.put("visitorNum", visitorNum);
        params.put("bookDate", bookDate);
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {

                Gson gson = new Gson();
                VisitInviteRecordJson.RowsBean bean = gson.fromJson(jsonData, VisitInviteRecordJson.RowsBean .class);
                onResponseVisitorInvite.onSuccess(bean);
            }
            @Override
            public void onError(DcnException error) {
                onResponseVisitorInvite.onError(error);
            }
        });
    }


    // 邀请记录
    public static void requestInvitationSearch(Context context,  IStatusLayout statusLayout, String state,long page, long size, final OnResponseVisitorInviteRecord onResponseVisitorInviteRecord){
        HashMap params = new HashMap();
        params.put("state", state);
        params.put("page", page);
        params.put("size", size);
        params.put("draw", "0");
        String url = SERVER_NORMAL + "invitation/search" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {

                Gson gson = new Gson();
                VisitInviteRecordJson inviteRecordJson = gson.fromJson(jsonData,VisitInviteRecordJson.class);
                onResponseVisitorInviteRecord.onSuccess(inviteRecordJson);
            }
            @Override
            public void onError(DcnException error) {
                onResponseVisitorInviteRecord.onError(error);
            }
        });
    }


    // 取消邀请
    public static void requestCancelInvite(Context context,  IStatusLayout statusLayout, long id, final OnResponseCancelInvite onResponseCancelInvite){
        HashMap params = new HashMap();
        params.put("id", id);
        String url = SERVER_NORMAL + "invitation/cancel" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                onResponseCancelInvite.onSuccess();
            }
            @Override
            public void onError(DcnException error) {
                onResponseCancelInvite.onError(error);
            }
        });
    }



    //获取可用的增值服务
    public static void requestAddedValue(Context context, String id,  String peroid, IStatusLayout statusLayout, final OnResponseAddedValue onResponseAddedValue){
        HashMap params = new HashMap();
        params.put("id", id);
        if(StringUtils.isNotEmpty(peroid)){
            params.put("peroid", peroid);
        }
        String url = SERVER_NORMAL + "workplace/meetingroom/addedvalue" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<AddedValueJson> addedValueJsons = gson.fromJson(jsonData, new TypeToken<List<AddedValueJson>>() {}.getType());
                onResponseAddedValue.onSuccess(addedValueJsons);
            }
            @Override
            public void onError(DcnException error) {
                onResponseAddedValue.onError(error);
            }
        });
    }

    //  /v1/workplace/meetingroom/combo



    //获取可用的增值服务
    public static void requestMeetingroomCombo(Context context, String id, IStatusLayout statusLayout, final OnResponseMeetingroomCombo onResponseMeetingroomCombo){
        HashMap params = new HashMap();
        params.put("id", id);
        String url = SERVER_NORMAL + "workplace/meetingroom/combo" + getAccessToken();
        NetworkUtil.requestJson(context, url, params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<MeetingRoomComboJson> comboses = gson.fromJson(jsonData, new TypeToken<List<MeetingRoomComboJson>>() {}.getType());
                onResponseMeetingroomCombo.onSuccess(comboses);
            }
            @Override
            public void onError(DcnException error) {
                onResponseMeetingroomCombo.onError(error);
            }
        });
    }


    //获取会籍中心城市列表
    public static void requestVisitCityCenterList(Context context,IStatusLayout statusLayout , final OnResponseGetCenterList onResponseGetCenterList){
        String url = GlobalParams.getInstance().requestUrl.SERVER_NORMAL + "center/getCenterList"+ getAccessToken() ;
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url,  params, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<CityCenterJson> cityCenterJsons = gson.fromJson(jsonData, new TypeToken<List<CityCenterJson>>() {}.getType());
                onResponseGetCenterList.onSuccess(cityCenterJsons);
            }

            @Override
            public void onError(DcnException error) {
                onResponseGetCenterList.onError(error);
            }
        });
    }


    //登陆打印机
    public static void requestPrintLogin(Context context, PrintJson bean ,final PrintLoginInfo info) {

        String url = SERVER_NORMAL+ "printer/login" + getAccessToken();
        HashMap params = new HashMap();
        params.put("code",bean.getCode());
        params.put("type",bean.getType());
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PrintLogin plBean = gson.fromJson(jsonData,new TypeToken<PrintLogin>() {}.getType());
                info.onSuccess(plBean);
            }

            @Override
            public void onError(DcnException error) {
                info.onError(error);
            }
        });
    }

    //解锁打印机
    public static void requestPrintUnlock(Context context, String entId,String stationId ,
                       String userId, String ip,final PrintUnlockInfo info) {

        String url = SERVER_NORMAL + "printer/unlocl" + getAccessToken();
        HashMap params = new HashMap();
        params.put("entId",entId);
        params.put("stationId",stationId);
        params.put("userId",userId);
        params.put("ip",ip);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                info.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                info.onError(error);
            }
        });
    }

    //解锁打印机
    public static void requestPrintUnlock(final Context context, String stationId , String ip, final PrintUnlockInfo info) {

        String url = SERVER_NORMAL + "printer/unlocl" + getAccessToken();
        HashMap params = new HashMap();
//        params.put("entId",entId);
        params.put("stationId",stationId);
//        params.put("stationId",665);
//        params.put("userId",userId);
        params.put("ip",ip);
        NetworkUtil.requestJson(context, url, params, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
//                Toast.makeText(context,"jsonData" + jsonData,Toast.LENGTH_LONG).show();
                info.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_LONG).show();
                info.onError(error);
            }
        });
    }


    //查询打印机状态
    public static void getPrintState(Context context, IStatusLayout statusLayout, final PrintStateRequestRescult myPrintRequestRescult){
        String url = SERVER_NORMAL + "printer/queryPrinterState" + getAccessToken();
        HashMap params = new HashMap();
        NetworkUtil.requestJson(context, url, params, false, statusLayout, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                PrintStateJson printJason = gson.fromJson(jsonData,new TypeToken<PrintStateJson>() {}.getType());
                myPrintRequestRescult.onSuccess(printJason);
            }

            @Override
            public void onError(DcnException error) {
                myPrintRequestRescult.onError(error);
            }
        });
    }

    //话题 - 话题list
    public static void getTagList(Context context,String id, final TagListRequestRescult tagListRequestRescult){
        String url = SERVER_NORMAL + "forum/getAll" + getAccessToken();
        HashMap params = new HashMap();
        params.put("unitId",GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<TagBean> list = gson.fromJson(jsonData,new TypeToken<List<TagBean>>() {}.getType());
                tagListRequestRescult.onSuccess(list);
            }

            @Override
            public void onError(DcnException error) {
                tagListRequestRescult.onError(error);
            }
        });
    }

    public static void getIndexList(Context context,String uid,long createTime, final IndexMomentBeanRequestRescult req){
        String url = "";
        if (GlobalParams.getInstance().isLogin()){
            url = SERVER_NORMAL + "moment/getMomentMsgs" + getAccessToken();
        }else {
            url = SERVER_NORMAL + "moment/anon/getMomentMsgs" ;
        }

        HashMap params = new HashMap();
        params.put("unitId",uid);
        params.put("pageSize",10);
        if (createTime != 0)
            params.put("createTime",createTime);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                IndexMomentBean bean = gson.fromJson(jsonData,IndexMomentBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }

    public static void getMyIndexList(Context context,long createTime, final IndexMomentBeanRequestRescult req){
        String url = "";
            url = SERVER_NORMAL + "moment/getMyMomentMsgs" + getAccessToken();

        HashMap params = new HashMap();
        params.put("pageSize",10);
        if (createTime != 0)
            params.put("createTime",createTime);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                IndexMomentBean bean = gson.fromJson(jsonData,IndexMomentBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }

    //    common/getMainFilterUnits
    public static void getIndexLocationList(Context context, final LocationBeanRequestRescult req){
        String url = "";
        url = SERVER_NORMAL + "common/getMainFilterUnits" ;

        HashMap params = new HashMap();
        params.put("id",GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                List<LocationBean> bean = gson.fromJson(jsonData,new TypeToken<List<LocationBean>>() {}.getType());
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }

    public static void getDynamicDetailComment(Context context,String id,String[] type,long page, final DynamicCommentBeanRequestRescult req){
        String url = "";
        if (GlobalParams.getInstance().isLogin()){
            url = SERVER_NORMAL + "moment/getMomentComments" + getAccessToken();
        }else {
            url = SERVER_NORMAL + "moment/anon/getMomentComments" ;
        }

        HashMap params = new HashMap();
        params.put("id",id);
        params.put("size",10);
        params.put("sort",type);
        params.put("page",page);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                DynamicCommentBean bean = gson.fromJson(jsonData,DynamicCommentBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }

    public static void getDynamicDetail(Context context,String id, final DynamicDetailBeanRequestRescult req){
        String url = "";
        if (GlobalParams.getInstance().isLogin()){
            url = SERVER_NORMAL + "moment/findMomentMsg" + getAccessToken();
        }else {
            url = SERVER_NORMAL + "moment/anon/findMomentMsg" ;
        }

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                IndexMomentBean.RowsBean bean = gson.fromJson(jsonData,IndexMomentBean.RowsBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }

    public static void setDynamicDetailComment(Context context,String id,String comment, final OnRequestResult req){
        String url = "";
            url = SERVER_NORMAL + "moment/sendComment" + getAccessToken();

        HashMap params = new HashMap();
        params.put("momentId",id);
        params.put("content",comment);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                req.onError(error);
            }
        });
    }


    public static void likeDynamic(final Context context, String id, final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "moment/setMomentLike" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void likeDynamicComment(final Context context, String id, final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "comment/setCommentLike" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void postDynamic(final Context context,String content, List<String> imgs
            ,String actionUri,String actionType
            , final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "moment/sendMomentMsg" + getAccessToken();
        HashMap params = new HashMap();

        if (imgs != null && imgs.size() != 0)
            params.put("imgs",imgs.toArray());
        params.put("content",content);
        params.put("unitId",GlobalParams.getInstance().getAtlasId());
        if (!StringUtils.isEmpty(actionUri) && !StringUtils.isEmpty(actionType)){
            params.put("actionUri",actionUri);
            params.put("actionType",actionType);
        }

        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }
    public static void postDynamic(final Context context,String content, List<String> imgs
            ,String actionUri,String actionType
            , final DynamicSuccessBeanRequestRescult req){
        String url = "";
        url = SERVER_NORMAL + "moment/sendMomentMsg" + getAccessToken();
        HashMap params = new HashMap();

        if (imgs != null && imgs.size() != 0)
            params.put("imgs",imgs.toArray());
        params.put("content",content);
        params.put("unitId",GlobalParams.getInstance().getAtlasId());
        if (!StringUtils.isEmpty(actionUri) && !StringUtils.isEmpty(actionType)){
            params.put("actionUri",actionUri);
            params.put("actionType",actionType);
        }

        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                DynamicSuccessBean bean = gson.fromJson(jsonData,DynamicSuccessBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void deleteDynamic(final Context context, String id, final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "moment/deleteMomentMsg" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void deleteTagComment(final Context context, String id, final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "appUser/deleteThreadComment" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void deleteDynamicComment(final Context context, String id, final OnRequestResult req){
        String url = "";
        url = SERVER_NORMAL + "moment/deleteComment" + getAccessToken();

        HashMap params = new HashMap();
        params.put("id",id);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                req.onSuccess();
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void checkBiz(final Context context, String bizCode, final LivingBizBeanInfo req){
        String url = "";
        url = SERVER_NORMAL + "common/getNearestBiz" + getAccessToken();

        HashMap params = new HashMap();
        params.put("bizCode",bizCode);
        params.put("unitId",GlobalParams.getInstance().getAtlasId());
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                LivingBizBean bean = gson.fromJson(jsonData,LivingBizBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }


    public static void getChatDynamic(final Context context,long createTime, final ChatDynamicBeanRequestRescult req){
        String url = "";
        url = SERVER_NORMAL + "moment/getDynamicMsgs" + getAccessToken();
        HashMap params = new HashMap();
        if(createTime != 0)
            params.put("createTime",createTime);
        params.put("pageSize",10);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ChatDynamicBean bean = gson.fromJson(jsonData,ChatDynamicBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }

    public static void getNotice(final Context context,long page,String id, final ChatChatBeanRequestRescult req){
        String url = "";
        url = SERVER_NORMAL + "tls/getNotices" + getAccessToken();
        HashMap params = new HashMap();
        params.put("page",page);
        params.put("imAccount",id);
        params.put("size",10);
        NetworkUtil.requestJson(context, url, params, false, new OnResponseData() {
            @Override
            public void onSuccess(String jsonData, int responseCode) {
                Gson gson = new Gson();
                ChatNoticeBean bean = gson.fromJson(jsonData,ChatNoticeBean.class);
                req.onSuccess(bean);
            }

            @Override
            public void onError(DcnException error) {
                Toast.makeText(context,error.getDescription(),Toast.LENGTH_SHORT).show();
                req.onError(error);
            }
        });
    }
}
