package com.zfy.pay;

import android.app.Activity;

import com.zfy.pay.payment.IPayment;
import com.zfy.pay.payment.ali.AliPay;
import com.zfy.pay.payment.wx.WxPay;

/**
 * CreateAt : 2019/6/13
 * Describe :
 *
 * @author chendong
 */
public class PayManager {

    public static final int PAY_ALI     = 0x12;
    public static final int PAY_WX      = 0x13;
    public static final int PAY_WX_SCAN = 0x14;

    private static IPayment sPayment;

    public static void pay(Activity act, int type, PayObj obj, OnPayStateListener listener) {
        OnPayStateListener stateListener = result -> {
            listener.onState(result);
            listener.onState(new PayResult(PayResult.STATE_COMPLETE));
            release();
        };
        stateListener.onState(new PayResult(PayResult.STATE_START));
        switch (type) {
            case PAY_ALI:
                sPayment = new AliPay();
                break;
            case PAY_WX:
                sPayment = new WxPay();
                break;
        }
        if (sPayment == null) {
            PayResult result = new PayResult(PayResult.STATE_FAIL);
            result.setErrCode(PayResult.ERR_INSTANCE);
            stateListener.onState(result);
        }
        sPayment.pay(act, obj, stateListener);
    }

    public static void release() {
        if (sPayment != null) {
            sPayment.release();
        }
        sPayment = null;
    }

    public static IPayment getPayment() {
        return sPayment;
    }
}
