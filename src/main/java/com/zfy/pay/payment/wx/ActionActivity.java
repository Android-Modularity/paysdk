package com.zfy.pay.payment.wx;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.zfy.pay.PayManager;
import com.zfy.pay.payment.IPayment;

public class ActionActivity extends Activity implements IWXAPIEventHandler {

    private boolean mIsNotFirstResume;

    public WxPay getWxPay() {
        IPayment payment = PayManager.getPayment();
        if (payment instanceof WxPay) {
            return (WxPay) payment;
        }
        checkFinish();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWxPay() != null) {
            getWxPay().handleIntent4UI(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getWxPay() != null) {
            getWxPay().handleIntent4UI(this);
        }
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (getWxPay() != null) {
            getWxPay().onResult4UI(this, resp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsNotFirstResume) {
            if (getWxPay() != null) {
                getWxPay().handleIntent4UI(this);
            }
            checkFinish();
        } else {
            mIsNotFirstResume = true;
        }
    }

    public void checkFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!isFinishing() && !isDestroyed()) {
                finish();
                overridePendingTransition(0, 0);
            }
        } else {
            if (!isFinishing()) {
                finish();
                overridePendingTransition(0, 0);
            }
        }
    }
}