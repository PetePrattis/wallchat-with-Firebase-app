package com.unipi.p15120.mywallchat;

import java.util.Calendar;
import java.text.*;

public class Message {//class that returns the objects that will be inserted in database
    public String msgText;
    public String msgUser;
    public String msgId;
    public String msgTime;

    //Constructor which is called in MainActivity
    public Message(String messageId, String messageText, String messageUser) {
        this.msgId = messageId;
        this.msgText = messageText;
        this.msgUser = messageUser;

        msgTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public Message() {
    }

    //the methods that are called to get specific objects of database
    public String getMsgId() {
        return msgId;
    }
    public void setMsgId(String messageId){this.msgId = messageId;}

    public String getMsgText() {
        return msgText;
    }
    public void setMsgText(String messageText){this.msgText = messageText;}


    public String getMsgUser() {
        return msgUser;
    }
    public void setMsgUser(String messageUser){this.msgUser = messageUser;}


    public String getMsgTime() {
        return msgTime;
    }
    public void setMsgTime(String messageTime){this.msgTime = messageTime;}





}