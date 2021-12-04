package com.wiyixiao.connectedsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.wiyixiao.connectedlib.ConnectedLib;
import com.wiyixiao.connectedlib.ConnectedLibBuilder;
import com.wiyixiao.connectedlib.IFindConnectedListener;
import com.wiyixiao.connectedlib.bean.ConnectedInfoBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchBtn;
    private ListView deviceLv;

    private DeviceAdapter deviceAdatper;
    private ArrayList<ConnectedInfoBean> deviceList = new ArrayList<>();

    private IFindConnectedListener findConnectedListener = new IFindConnectedListener() {
        @Override
        public void findUpdate(ConnectedInfoBean bean) {
            deviceList.add(bean);
            deviceAdatper.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBtn = findViewById(R.id.search_btn);
        deviceLv = findViewById(R.id.device_lv);

        searchBtn.setOnClickListener(this);

        ConnectedLibBuilder builder = new ConnectedLibBuilder(this);
        builder.setFindConnectedListener(findConnectedListener);

        ConnectedLib.getInstance().init(builder);

        deviceAdatper = new DeviceAdapter(this.getApplicationContext(), deviceList);
        deviceLv.setAdapter(deviceAdatper);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectedLib.getInstance().release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_btn:
                clearDevice(); //清除
                ConnectedLib.getInstance().findConnected();
                break;
            default:
                break;
        }
    }

    private void clearDevice(){
        if(deviceList.size() > 0){
            deviceList.clear();
            deviceAdatper.notifyDataSetChanged();
        }
    }
}