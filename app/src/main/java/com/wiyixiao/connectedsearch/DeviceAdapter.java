package com.wiyixiao.connectedsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wiyixiao.connectedlib.bean.ConnectedInfoBean;

import java.util.ArrayList;

public class DeviceAdapter extends BaseAdapter {
    private int mCurrentItem=0;
    private boolean isClick=false;

    private Context mContext;
    private ArrayList<ConnectedInfoBean> mList;

    public DeviceAdapter(Context context, ArrayList<ConnectedInfoBean> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.item_wifidevice_list,null);
        }

        ConnectedInfoBean bean = mList.get(i);

        TextView textView= (TextView) view.findViewById(R.id.wifi_item_tv);
        textView.setText(String.format("%s\n%s -- %s", bean.getIp(), bean.getMac(), bean.getState()));

//        if (mCurrentItem==i&&isClick){
//            textView.setTextColor(Color.parseColor("#ff6600"));
//        }else{
//            textView.setTextColor(Color.parseColor("#000000"));
//        }

        return view;
    }

    public void setCurrentItem(int currentItem){
        this.mCurrentItem=currentItem;
    }

    public void setClick(boolean click){
        this.isClick=click;
    }
}
