package com.zfy.pay.payment;

import android.app.Activity;

import com.zfy.pay.OnPayStateListener;
import com.zfy.pay.PayObj;

/**
 * CreateAt : 2018/6/11
 * Describe : 支付类型基类
 *
 * @author chendong
 */
public interface IPayment {

    void pay(Activity act, PayObj obj, OnPayStateListener listener);

    void release();
}
