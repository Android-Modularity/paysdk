package com.zfy.pay.payment.ali;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.zfy.pay.OnPayStateListener;
import com.zfy.pay.PayObj;
import com.zfy.pay.PayResult;
import com.zfy.pay.payment.IPayment;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CreateAt : 2018/6/11
 * Describe :
 *
 * @author chendong
 */
public final class AliPay implements IPayment {

    private static ExecutorService sService;

    private OnPayStateListener mListener;
    private PayTask            mPayTask;

    @Override
    public void pay(Activity act, PayObj obj, OnPayStateListener listener) {
        if (sService == null) {
            sService = Executors.newSingleThreadExecutor();
        }
        String order = obj.getAliOrderStr();
        mListener = listener;
        mPayTask = new PayTask(act);
        sService.execute(new PayRunnable(this, order));
    }


    static class PayRunnable extends WeakReference<AliPay> implements Runnable {

        private String order;

        PayRunnable(AliPay referent, String order) {
            super(referent);
            this.order = order;
        }

        @Override
        public void run() {
            AliPay aliPay = get();
            if (aliPay == null) {
                return;
            }
            Map<String, String> result = aliPay.mPayTask.payV2(order, true);
            aliPay.onResult(new AliPayResult(result));
        }
    }


    protected void onResult(AliPayResult resp) {

        PayResult result = new PayResult();

        String info = resp.getResult(); // 同步返回需要验证的信息
        String status = resp.getResultStatus();
        switch (status) {
            default:
            case "4000": // 系统异常
                result.setState(PayResult.STATE_FAIL);
                result.setErrCode(PayResult.ERR_COMM);
                result.appendMsg("系统异常，status = " + status + ", info = " + info);
                mListener.onState(result);
                break;
            case "6001": // 用户中途取消支付操作
                result.setState(PayResult.STATE_CANCEL);
                mListener.onState(result);
                break;
            case "6002": // 网络连接出错
                result.setState(PayResult.STATE_FAIL);
                result.setErrCode(PayResult.ERR_COMM);
                result.appendMsg("网络出错，status = " + status + ", info = " + info);
                mListener.onState(result);
                break;
            case "9000":
                result.setState(PayResult.STATE_SUCCESS);
                mListener.onState(result);
                break;
        }
    }


    @Override
    public void release() {
        sService.shutdownNow();
        mPayTask = null;
        mListener = null;
    }

}
