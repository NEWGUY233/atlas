package com.atlas.crmapp.common;

import android.content.Context;

import com.atlas.crmapp.Atlas;
import com.atlas.crmapp.model.BusinesseModel;
import com.atlas.crmapp.model.PersonInfoJson;
import com.atlas.crmapp.model.ResourceJson;
import com.atlas.crmapp.util.ACache;
import com.atlas.crmapp.util.StringUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Harry on 2017-03-22.
 */

public class GlobalParams {

//    public final static boolean production = true; //设置为true时为生产模式否则为开发模式
    public final static boolean production = false; //设置为true时为生产模式否则为开发模式

    public static GlobalParams instance = null;

    public static GlobalParams getInstance() {
        if (instance == null){
            instance = new GlobalParams();
        }
        return instance;
    }

    public static void setGlobalParams(GlobalParams globalParams){
        instance = globalParams;
    }

    public class RequestUrl {

        /*public  String APPSERVER = production ? "https://app.crm.atlasoffice.cn/" : "https://testorder.crm.atlasoffice.cn/";////
        public  String AUTHSERVER = production ? "https://portal.crm.atlasoffice.cn/" : "https://testorder.crm.atlasoffice.cn/";////
        public final String SERVER_NORMAL = APPSERVER + "app/v1/";
        public final String SERVER_OAUTH = AUTHSERVER + "authz/oauth/token?";
        public final String SERVER_OSS = production ? "https://oss-cn-shenzhen.aliyuncs.com" : "https://oss-cn-shenzhen.aliyuncs.com";//
        public  String OSS_DOWNLOAD_URL = production ? "http://appassets.crm.atlasoffice.cn/" : "http://testassets.crm.atlasoffice.cn/";
        public final String AUTH_SECRET_KEY = production ? "e60dbd3a4cbe4974a291b64de786f279" : "mobile";*/




        public String baseUrl = "https://testorder.crm.atlasoffice.cn/";//https://testorder.crm.atlasoffice.cn/

        public  String APPSERVER = production ? "https://app.crm.atlasoffice.cn/" : baseUrl;
        public  String AUTHSERVER = production ? "https://portal.crm.atlasoffice.cn/" : baseUrl;
        public  String SERVER_NORMAL = APPSERVER + "app/v1/";
        public  String SERVER_NORMAL_V2 = APPSERVER + "app/v2/";
        public  String SERVER_NORMAL_V3 = APPSERVER + "app/v3/";
        public  String SERVER_OAUTH = AUTHSERVER + "authz/oauth/token?";
        public final String SERVER_OSS = production ? "https://oss-cn-shenzhen.aliyuncs.com" : "https://oss-cn-shenzhen.aliyuncs.com";//
        public  String OSS_DOWNLOAD_URL = production ? "http://appassets.crm.atlasoffice.cn/" : "http://testassets.crm.atlasoffice.cn/";
        public final String AUTH_SECRET_KEY = production ? "e60dbd3a4cbe4974a291b64de786f279" : "mobile";

        public RequestUrl(){
        }

        public void setBaseUrl(){
            APPSERVER = production ? "https://app.crm.atlasoffice.cn/" : baseUrl;////
            AUTHSERVER = production ? "https://portal.crm.atlasoffice.cn/" : baseUrl;////
            SERVER_NORMAL = APPSERVER + "app/v1/";
            SERVER_NORMAL_V2 = APPSERVER + "app/v2/";
            SERVER_NORMAL_V3 = APPSERVER + "app/v3/";
            SERVER_OAUTH = AUTHSERVER + "authz/oauth/token?";
            OSS_DOWNLOAD_URL = production ? "http://appassets.crm.atlasoffice.cn/" : baseUrl;
        }

        //public final String APPSERVER = production ? "https://app.crm.atlasoffice.cn/" : "https://testorder.crm.atlasoffice.cn/";////
        //public final String AUTHSERVER = production ? "https://portal.crm.atlasoffice.cn/" : "https://testorder.crm.atlasoffice.cn/";////
    }

    public RequestUrl requestUrl = new RequestUrl();
    private final String ACACHE_KEY_GLOBAL = "jsonGlobal";

    public String SharedPreferenceName = "com.atlas.crmapp.user_preferences";

 //   public static MySqliteHelper myDBHelper;
    private PersonInfoJson personInfoJson;//用户信息
    private boolean mHasContract = false; //是否有企业账户
    private boolean mIsLogin = false; //判断是否有登陆
    private String mAccessToken = "";
    private String mRefreshToken = "";

    //OSS CONFIG
    private String mAccessKeyId = "";
    private String mAccessKeySecret = "";
    private final String mOSSBucketName = production ? "byron-atlas-app-resources" : "test-byron-atlas-resources";//


    private ArrayList<BizCode> mBizCodes = new ArrayList<BizCode>();
    private BizCode mCurrentBizCode;
    //Atlas中心
    private long mAtlasId = 0;
    private String mAtlasName = "";
    private boolean keyfree;// 门锁是否 keyfree
    //Coffee信息
    private long mCoffeeId = 0;
    private String mCoffeeName = "";
    private String mCoffeeCode = "";
    //Kitchen信息
    private long mKitchenId = 0;
    private String mKitchenName = "";
    private String mKitchenCode = "";
    //Fitness信息
    private long mFitnessId = 0;
    private String mFitnessName = "";
    private String mFitnessCode = "";
    //Gogreen信息
    private long mGogreenId = 0;
    private String mGogreenName = "";
    private String mGogreenCode = "";
    //Studio信息
    private long mStudioId = 0;
    private String mStudioName = "";
    private String mStudioCode = "";
    //Workplace信息
    private long mWorkplaceId = 0;
    private String mWorkplaceName = "";
    private String mWorkplaceCode = "";

    private Map<String, String > mapBusinesse = new HashMap<>();
    private List<BusinesseModel> businesses;

    //主页遮盖图
    private List<ResourceJson.ResourceMedia> mMaskList;
    //主页标题
    private List<ResourceJson.ResourceMedia> mTitleList;
    //主页内容
    private List<ResourceJson.ResourceMedia> mContentList;
    //推广页面
    private List<ResourceJson.ResourceMedia> mAdList;
    //主页加载广告图
    private List<ResourceJson.ResourceMedia> mResList;

    public void setBusinesses(List<BusinesseModel> businesses){
        this.businesses = businesses;
        for(BusinesseModel businesseModel : businesses){
            mapBusinesse.put(businesseModel.getCode(), businesseModel.getName());
        }
        saveGlobalParamsToCache();
    }

    public List<BusinesseModel> getBusinesses(){
        resIsNullSetGlobalParamsFromCache();
        return businesses;
    }

    public String getBusinesseName(String businesseCode){
        resIsNullSetGlobalParamsFromCache();
        return mapBusinesse == null? "": mapBusinesse.get(businesseCode);
    }

    public String getCurrentBusinesseName(){
        resIsNullSetGlobalParamsFromCache();
        return  getBusinesseName(getCurrentBizCode().getBizCode());
    }

    public List<ResourceJson.ResourceMedia> getResList() {
        resIsNullSetGlobalParamsFromCache();
        return mResList;
    }

    public void setResList(List<ResourceJson.ResourceMedia> resList) {
        mResList = resList;
        saveGlobalParamsToCache();
    }

    public List<ResourceJson.ResourceMedia> getmMaskList() {
        resIsNullSetGlobalParamsFromCache();
        return mMaskList;
    }

    public void setmMaskList(List<ResourceJson.ResourceMedia> mMaskList) {
        this.mMaskList = mMaskList;
        saveGlobalParamsToCache();
    }

    public List<ResourceJson.ResourceMedia> getmTitleList() {
        resIsNullSetGlobalParamsFromCache();
        return mTitleList;
    }

    public void setmTitleList(List<ResourceJson.ResourceMedia> mTitleList) {
        this.mTitleList = mTitleList;
        saveGlobalParamsToCache();
    }

    public List<ResourceJson.ResourceMedia> getmContentList() {
        resIsNullSetGlobalParamsFromCache();
        return mContentList;
    }

    public void setmContentList(List<ResourceJson.ResourceMedia> mContentList) {
        this.mContentList = mContentList;
        saveGlobalParamsToCache();
    }

    public List<ResourceJson.ResourceMedia> getmAdList() {
        resIsNullSetGlobalParamsFromCache();
        return mAdList;
    }

    public void setmAdList(List<ResourceJson.ResourceMedia> mAdList) {
        this.mAdList = mAdList;
        saveGlobalParamsToCache();
    }

    public boolean isLogin() {
        resIsNullSetGlobalParamsFromCache();
        return mIsLogin;
    }

    public void setIsLogin(boolean isLogin) {
        mIsLogin = isLogin;
    }

    public String getAccessToken() {
        resIsNullSetGlobalParamsFromCache();
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
        saveGlobalParamsToCache();
    }

    public String getRefreshToken() {
        resIsNullSetGlobalParamsFromCache();
        return mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
        saveGlobalParamsToCache();
    }

    public String getAccessKeyId() {
        resIsNullSetGlobalParamsFromCache();
        return mAccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        mAccessKeyId = accessKeyId;
        saveGlobalParamsToCache();
    }

    public String getAccessKeySecret() {
        resIsNullSetGlobalParamsFromCache();
        return mAccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        mAccessKeySecret = accessKeySecret;
        saveGlobalParamsToCache();
    }

    public String getOssBucketName() {
        resIsNullSetGlobalParamsFromCache();
        return mOSSBucketName;
    }

    public void init(Context context) {
        resIsNullSetGlobalParamsFromCache();
        Utils.readAccountToken(context);
    }

    public long getAtlasId() {
        resIsNullSetGlobalParamsFromCache();
        return mAtlasId;
    }

    public void setAtlasId(long atlasId) {
        mAtlasId = atlasId;
        saveGlobalParamsToCache();
    }

    public String getAtlasName() {
        resIsNullSetGlobalParamsFromCache();
        return mAtlasName;
    }

    public void setAtlasName(String atlasName) {
        mAtlasName = atlasName;
        saveGlobalParamsToCache();
    }

    public boolean isKeyfree() {
        resIsNullSetGlobalParamsFromCache();
        return keyfree;
    }

    public void setKeyfree(boolean keyfree) {
        this.keyfree = keyfree;
        saveGlobalParamsToCache();
    }

    public long getCoffeeId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getCoffeeCode());
    }

    public void setCoffeeId(long coffeeId) {
        mCoffeeId = coffeeId;
        saveGlobalParamsToCache();
    }

    public String getCoffeeCode() {
        resIsNullSetGlobalParamsFromCache();
        return "coffee";
    }

    public void setCoffeeCode(String coffeeCode) {
        mCoffeeCode = coffeeCode;
        saveGlobalParamsToCache();
    }

    public long getKitchenId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getKitchenCode());
    }

    public void setKitchenId(long kitchenId) {
        mKitchenId = kitchenId;
        saveGlobalParamsToCache();
    }

    public String getKitchenCode() {
        resIsNullSetGlobalParamsFromCache();
        return "kitchen";
    }

    public void setKitchenCode(String kitchenCode) {
        mKitchenCode = kitchenCode;
        saveGlobalParamsToCache();
    }

    public long getFitnessId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getFitnessCode());
    }

    public void setFitnessId(long fitnessId) {
        mFitnessId = fitnessId;
        saveGlobalParamsToCache();
    }

    public String getFitnessCode() {
        resIsNullSetGlobalParamsFromCache();
        return "fitness";
    }

    public void setFitnessCode(String fitnessCode) {
        mFitnessCode = fitnessCode;
        saveGlobalParamsToCache();
    }

    public long getWorkplaceId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getWorkplaceCode());
    }

    public void setWorkplaceId(long workplaceId) {
        mWorkplaceId = workplaceId;
        saveGlobalParamsToCache();
    }

    public String getWorkplaceCode() {
        resIsNullSetGlobalParamsFromCache();
        return "workplace";
    }

    public void setWorkplaceCode(String workplaceCode) {
        mWorkplaceCode = workplaceCode;
        saveGlobalParamsToCache();
    }


    public void setPersonInfoJson(PersonInfoJson personInfoJson) {
        this.personInfoJson = personInfoJson;
        saveGlobalParamsToCache();
    }

    public PersonInfoJson getPersonInfoJson() {
        resIsNullSetGlobalParamsFromCache();
        if(personInfoJson == null){
            personInfoJson = new PersonInfoJson();
        }
        return personInfoJson;
    }

    public long getGogreenId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getGogreenCode());
    }

    public void setGogreenId(long gogreenId) {
        mGogreenId = gogreenId;
        saveGlobalParamsToCache();
    }

    public String getGogreenCode() {
        resIsNullSetGlobalParamsFromCache();
        return "gogreen";
    }

    public void setGogreenCode(String gogreenCode) {
        mGogreenCode = gogreenCode;
        saveGlobalParamsToCache();
    }

    public long getStudioId() {
        resIsNullSetGlobalParamsFromCache();
        return getUnitId(getStudioCode());
    }

    public void setStudioId(long studioId) {
        mStudioId = studioId;
        saveGlobalParamsToCache();
    }

    public String getStudioCode() {
        resIsNullSetGlobalParamsFromCache();
        return "studio";
    }

    public void setStudioCode(String studioCode) {
        mStudioCode = studioCode;
        saveGlobalParamsToCache();
    }

    public ArrayList<BizCode> getBizCodes() {
        resIsNullSetGlobalParamsFromCache();
        return mBizCodes;
    }

    public void setBizCodes(ArrayList<BizCode> bizCodes) {
        mBizCodes = bizCodes;
        saveGlobalParamsToCache();
    }

    public BizCode getCurrentBizCode()
    {
        resIsNullSetGlobalParamsFromCache();
        return mCurrentBizCode;
    }

    public void setCurrentBizCode(BizCode currentBizCode) {
        mCurrentBizCode = currentBizCode;
        saveGlobalParamsToCache();
    }

    public boolean isHasContract() {
        resIsNullSetGlobalParamsFromCache();
        return mHasContract;
    }

    public void setHasContract(boolean hasContract) {
        mHasContract = hasContract;
        saveGlobalParamsToCache();
    }

    private long getUnitId(String bizCode) {
        resIsNullSetGlobalParamsFromCache();
        for (int i=0; i<mBizCodes.size(); i++) {
            if (mBizCodes.get(i).getBizCode().equals(bizCode)) {
                return mBizCodes.get(i).getUnitId();
            }
        }
        return 0;
    }

    public String getBizName(String bizCode) {
        resIsNullSetGlobalParamsFromCache();
        for (int i=0; i<mBizCodes.size(); i++) {
            if (mBizCodes.get(i).getBizCode().equals(bizCode)) {
                return mBizCodes.get(i).getBizName();
            }
        }
        return "";
    }

    public String getCoffeeName() {
        return getBizName(getCoffeeCode());
    }

    public void setCoffeeName(String coffeeName) {
        mCoffeeName = coffeeName;
        saveGlobalParamsToCache();
    }

    public String getKitchenName() {
        return getBizName(getKitchenCode());
    }

    public void setKitchenName(String kitchenName) {
        mKitchenName = kitchenName;
        saveGlobalParamsToCache();
    }

    public String getFitnessName() {
        resIsNullSetGlobalParamsFromCache();
        return getBizName(getFitnessCode());
    }

    public void setFitnessName(String fitnessName) {
        mFitnessName = fitnessName;
        saveGlobalParamsToCache();
    }

    public String getGogreenName() {
        resIsNullSetGlobalParamsFromCache();
        return getBizName(getGogreenCode());
    }

    public void setGogreenName(String gogreenName) {
        mGogreenName = gogreenName;
        saveGlobalParamsToCache();
    }

    public String getStudioName() {
        resIsNullSetGlobalParamsFromCache();
        return getBizName(getStudioCode());
    }

    public void setStudioName(String studioName) {
        mStudioName = studioName;
        saveGlobalParamsToCache();
    }

    public String getWorkplaceName() {
        resIsNullSetGlobalParamsFromCache();
        return getBizName(getWorkplaceCode());
    }

    public void setWorkplaceName(String workplaceName) {
        mWorkplaceName = workplaceName;
        saveGlobalParamsToCache();
    }

    public void saveGlobalParamsToCache(){
        if(maskResIsNull()){
            return;
        }
        Gson gson = new Gson();
        GlobalParams globalParams = GlobalParams.getInstance();
        if(globalParams.getmMaskList() != null){
            String jsonGlobal = gson.toJson(globalParams);
            ACache aCache = ACache.get(Atlas.getAppContext());
            aCache.put(ACACHE_KEY_GLOBAL, jsonGlobal, 2 * ACache.TIME_DAY);
        }else{
            Logger.d("saveGlobalParamsToCache GlobalParams is null");
        }
    }

    public void resIsNullSetGlobalParamsFromCache(){
        if(!maskResIsNull()){
            return;
        }

        String currentActivityName = Atlas.getCurrentActivityName();
        if(StringUtils.isNotEmpty(currentActivityName)){
            if(currentActivityName.contains("WelcomeActivity")){
                return;
            }
        }
        ACache aCache = ACache.get(Atlas.getAppContext());
        String jsonGlobal = aCache.getAsString(ACACHE_KEY_GLOBAL);
        if(StringUtils.isNotEmpty(jsonGlobal)){
            Logger.d("jsonGlobal---" + jsonGlobal);
            Gson gson = new Gson();
            GlobalParams globalParams = (GlobalParams) gson.fromJson(jsonGlobal, GlobalParams.class);
            if(globalParams.getmMaskList() != null){
                setGlobalParams(globalParams);
            }else{
                Logger.d("setGlobalParamsFromCache  globalParams is null");
            }
        }
    }

    public boolean maskResIsNull(){
        if(mMaskList == null || mMaskList.size() == 0){
            Logger.d("mMaskList  is null ");
            return true;
        }else {
            return false;
        }
    }

}
