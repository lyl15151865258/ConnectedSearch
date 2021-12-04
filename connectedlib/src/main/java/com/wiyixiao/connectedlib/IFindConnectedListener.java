package com.wiyixiao.connectedlib;

import com.wiyixiao.connectedlib.bean.ConnectedInfoBean;

public interface IFindConnectedListener {

    /**
     * 更新查找结果
     * @param bean info
     */
    void findUpdate(ConnectedInfoBean bean);
}
