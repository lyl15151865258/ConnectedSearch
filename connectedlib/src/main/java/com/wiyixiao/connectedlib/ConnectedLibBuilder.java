package com.wiyixiao.connectedlib;

import android.content.Context;

public class ConnectedLibBuilder {
    protected IFindConnectedListener findConnectedListener;
    protected Context context;

    public ConnectedLibBuilder(Context context) {
        this.context = context;
    }

    public ConnectedLibBuilder setFindConnectedListener(IFindConnectedListener findConnectedListener) {
        this.findConnectedListener = findConnectedListener;
        return this;
    }
}
