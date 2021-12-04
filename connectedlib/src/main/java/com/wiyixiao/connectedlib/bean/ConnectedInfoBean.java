package com.wiyixiao.connectedlib.bean;

import android.text.TextUtils;
import android.util.Log;

import java.io.Serializable;

public class ConnectedInfoBean implements Serializable {
    private final String TAG = this.getClass().getSimpleName();

    private String ip;
    private String mac;
    private String state;

    public ConnectedInfoBean(String info) {
        if(TextUtils.isEmpty(info)){
            ip = "0.0.0.0";
            mac = "null";
            state = "-327";
        }

        //192.168.215.138 dev wlan0 lladdr 5c:77:76:74:b1:1c STALE
        Log.e(TAG, "wlan_info - " + info);
        if(info.contains("lladdr")){
            String[] temp = info.split("lladdr");
            String[] temp1 = temp[1].trim().split(" "); //空格

            mac = temp1[0];
            state = temp1[1];
        }else{
            mac = "null";
            state = "-327";
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
