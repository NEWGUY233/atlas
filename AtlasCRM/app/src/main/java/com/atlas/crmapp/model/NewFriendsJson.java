package com.atlas.crmapp.model;

/**
 * Created by Alex on 2017/6/2.
 */

public class NewFriendsJson {


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NewFriend getUser() {
        return user;
    }

    public void setUser(NewFriend user) {
        this.user = user;
    }

    private String message;
    private NewFriend user;

    private class NewFriend{
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        private String nickname;
        private String username;
        private String gender;
        private String company;
        private String imageUrl;
    }
}
