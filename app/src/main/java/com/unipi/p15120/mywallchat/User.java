package com.unipi.p15120.mywallchat;

public class User {//class that returns the objects that will be inserted in database
    public String Uname;
    public String Uid;

    //Constructor which is called in MainActivity
    public User(String UserId, String Username) {
        this.Uid = UserId;
        this.Uname = Username;
    }

    public User() {
    }


}



