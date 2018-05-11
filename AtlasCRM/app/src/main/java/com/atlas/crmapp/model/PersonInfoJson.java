package com.atlas.crmapp.model;

import com.atlas.crmapp.common.Constants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harry on 2017-03-26.
 */

public class PersonInfoJson implements Serializable{

    private static final long serialVersionUID = -8278850862112055996L;
    public long id;
    public String uid;
    private String zipCode;
    public String mobile;
    public String nick;
    public String avatar;
    public long birthday;
    public String company;
    public String address;
    public String gender;
    public int age;
    public String easemobPwd;
    public int noCountPassword;
    public String openfingerprint  = Constants.ORDER_FINGER_PAY.NO_AGREE_FINGER_PAY;
    /**
     * id : 2945
     * noCountPassword : -1
     * email : xxx@163.com
     * interestList : [{"id":1,"name":"职场","createTime":1517826592000,"order":1,"img":""}]
     * industry : {"id":1,"name":"TMT (科技、媒体和通信)","createTime":1517826592000,"order":1}
     * job : job
     * skill : skill
     * otherIndustry : 其他行业
     * favourites :
     */

    private String email;
    private IndustryBean industry;
    private String job;
    private String skill;
    private String otherIndustry;
    private String favourites;
    private List<InterestListBean> interestList;


    public String getOpenfingerprint() {
        return openfingerprint;
    }

    public void setOpenfingerprint(String openfingerprint) {
        this.openfingerprint = openfingerprint;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEasemobPwd() {
        return easemobPwd;
    }

    public void setEasemobPwd(String easemobPwd) {
        this.easemobPwd = easemobPwd;
    }

    public int getNoCountPassword() {
        return noCountPassword;
    }

    public void setNoCountPassword(int noCountPassword) {
        this.noCountPassword = noCountPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public IndustryBean getIndustry() {
        return industry;
    }

    public void setIndustry(IndustryBean industry) {
        this.industry = industry;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
    }

    public String getFavourites() {
        return favourites;
    }

    public void setFavourites(String favourites) {
        this.favourites = favourites;
    }

    public List<InterestListBean> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<InterestListBean> interestList) {
        this.interestList = interestList;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public static class IndustryBean implements Serializable{
        /**
         * id : 1
         * name : TMT (科技、媒体和通信)
         * createTime : 1517826592000
         * order : 1
         */

        @SerializedName("id")
        private int idX;
        private String name;
        private long createTime;
        private int order;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

    public static class InterestListBean implements Serializable{
        /**
         * id : 1
         * name : 职场
         * createTime : 1517826592000
         * order : 1
         * img :
         */

        @SerializedName("id")
        private int idX;
        private String name;
        private long createTime;
        private int order;
        private String img;

        public int getIdX() {
            return idX;
        }

        public void setIdX(int idX) {
            this.idX = idX;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
