package com.wiyixiao.connectedlib;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.wiyixiao.connectedlib.bean.ConnectedInfoBean;
import com.wiyixiao.connectedlib.utils.NetUtil;

import org.wiyixiao.threadpool.LocalThreadPools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectedLib {

    private final String TAG = this.getClass().getSimpleName();

    private final static int CODE_UPDATE_INFO = 0x01;

    private ConnectedLibBuilder builder;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Bundle b = msg.getData();
            switch (msg.what){
                case CODE_UPDATE_INFO:
                    ConnectedInfoBean bean = (ConnectedInfoBean) b.getSerializable("find");
                    builder.findConnectedListener.findUpdate(bean);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    public ConnectedLib() {
    }

    private static class SingleHelper{
        private final static ConnectedLib INSTANCE = new ConnectedLib();
    }

    public static ConnectedLib getInstance() {
        return SingleHelper.INSTANCE;
    }

    public void init(ConnectedLibBuilder builder){
        this.builder = builder;
    }

    public void release(){
        this.builder = null;
    }

    public String getLocalAddress(){
        return NetUtil.getIpAddressString();
    }

    public void findConnected(){
        String localIp = getLocalAddress();

        Runtime runtime = Runtime.getRuntime();
        ExecutorService pool = Executors.newCachedThreadPool();

        String[] mip = localIp.split("\\.");
        if(mip.length != 4){return;} //ipv4

        String wlan_info[] = getConnectInfo().split("\n");
        if(wlan_info.length <= 0){return;}

        for(int i=0;i<=255;i++){
            String ip_first = String.format("%s.%s.%s.", mip[0], mip[1], mip[2]);
            String ip_last = mip[3];

            String fp = String.valueOf(i);
            Runnable pingRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Process p = runtime.exec("ping -c 2 -w " + 2 + " " + ip_first + fp);
                        int ret = p.waitFor();
                        if (ret == 0) {
                            final String final_ip = String.format("%s%s",ip_first, fp);
                            if(fp.equals(ip_last)){
                                Log.e(TAG, "ping成功,本地IP - " + final_ip);
                            }else{
                                Log.e(TAG, "ping成功 - " + final_ip);
                                for (String info: wlan_info
                                ) {
                                    if(info.contains(final_ip)){
                                        ConnectedInfoBean infoBean = new ConnectedInfoBean(info);
                                        infoBean.setIp(final_ip);
                                        //更新设备列表
                                        Log.e(TAG, "ip: " + infoBean.getIp());
                                        Log.e(TAG, "mac: " + infoBean.getMac());
                                        Log.e(TAG, "state: " + infoBean.getState());

                                        Message message = handler.obtainMessage();
                                        message.what = CODE_UPDATE_INFO;
                                        Bundle b = new Bundle();
                                        b.putSerializable("find", infoBean);
                                        message.setData(b);
                                        message.sendToTarget();

                                        break;
                                    }
                                }
                            }
                        } else {
//                            Log.e(TAG, "ping失败 - " + fp);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            pool.execute(pingRunnable);
        }

    }

    private String getConnectInfo(){
        String info = null;
        ExeCommand cmd = new ExeCommand(false).run("ip neigh", 3000);
        while(cmd.isRunning())
        {
            try {
                Thread.sleep(300);
            } catch (Exception e) {
                e.printStackTrace();
            }
            info = cmd.getResult();
        }

        return info;
    }
}
