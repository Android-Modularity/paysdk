package com.zfy.pay.payment.wx;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zfy.pay.OnPayStateListener;
import com.zfy.pay.PayManager;
import com.zfy.pay.PayObj;
import com.zfy.pay.PayResult;
import com.zfy.pay.payment.IPayment;

/**
 * CreateAt : 2018/6/11
 * Describe :
 * 微信支付流程 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_5
 * 客户端相关：
 * 1. 选择商品，请求自己的服务器，生成自己的业务订单
 * 2. 点击支付按钮时，调用自己服务的微信支付接口，拿到 prepay_id,sign 等
 * 3. 客户端发起支付，用户跳转微信
 * 4. 从回调获悉支付结果
 * 5. 去自己的服务器校验支付结果
 * 6. 展示真实的支付结果
 *
 * @author chendong
 */
public class WxPay implements IPayment {

    private IWXAPI             mWxApi;
    private OnPayStateListener mListener;

    private IWXAPI makeWxApi(Activity activity, String appId) {
        if (mWxApi == null) {
            mWxApi = WXAPIFactory.createWXAPI(activity, appId, true);
            mWxApi.registerApp(appId);
        }
        return mWxApi;
    }


    public void handleIntent4UI(Activity activity) {
        if (activity instanceof IWXAPIEventHandler && mWxApi != null) {
            mWxApi.handleIntent(activity.getIntent(), (IWXAPIEventHandler) activity);
        }
    }


    public void onResult4UI(Activity activity, Object resp) {
        PayResult result = new PayResult();

        if (!(resp instanceof BaseResp)) {
            result.setState(PayResult.STATE_FAIL);
            result.setErrCode(PayResult.ERR_COMM);
            mListener.onState(result);
            return;
        }

        BaseResp baseResp = (BaseResp) resp;
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result.setState(PayResult.STATE_SUCCESS);
                    mListener.onState(result);
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    // 错误
                    result.setState(PayResult.STATE_FAIL);
                    result.setErrCode(PayResult.ERR_COMM);
                    mListener.onState(result);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    // 用户取消
                    result.setState(PayResult.STATE_CANCEL);
                    mListener.onState(result);
                    break;
            }
        }
        if (activity instanceof ActionActivity) {
            ((ActionActivity) activity).checkFinish();
        }

    }


    @Override
    public void pay(Activity act, PayObj obj, OnPayStateListener listener) {

        mListener = listener;

        String appId = obj.getAppId();
        String partnerId = obj.getPartnerId();
        String prepayId = obj.getPrepayId();
        String packageValue = obj.getPackageValue();
        String nonceStr = obj.getNonceStr();
        String timeStamp = obj.getTimeStamp();
        String sign = obj.getSign();

        if (TextUtils.isEmpty(appId)
                || TextUtils.isEmpty(partnerId)
                || TextUtils.isEmpty(prepayId)
                || TextUtils.isEmpty(packageValue)
                || TextUtils.isEmpty(nonceStr)
                || TextUtils.isEmpty(timeStamp)
                || TextUtils.isEmpty(sign)) {
            PayResult result = new PayResult(PayResult.STATE_FAIL);
            result.setErrCode(PayResult.ERR_PARAM);
            listener.onState(result);
            return;
        }

        IWXAPI iwxapi = makeWxApi(act, appId);
        if (iwxapi == null) {
            PayResult result = new PayResult(PayResult.STATE_FAIL);
            result.setErrCode(PayResult.ERR_INSTANCE);
            listener.onState(result);
            return;
        }
        if (!iwxapi.isWXAppInstalled()) {
            PayResult result = new PayResult(PayResult.STATE_FAIL);
            result.setErrCode(PayResult.ERR_NOT_INSTALL);
            listener.onState(result);
            return;
        }

        PayReq request = new PayReq();
        request.appId = appId;
        request.packageValue = packageValue;
        request.timeStamp = timeStamp;
        request.partnerId = partnerId;
        request.prepayId = prepayId;
        request.nonceStr = nonceStr;
        request.sign = sign;
        iwxapi.sendReq(request);
    }

    @Override
    public void release() {
        if (mWxApi != null) {
            mWxApi.unregisterApp();
            mWxApi.detach();
            mWxApi = null;
        }
        mListener = null;
    }
}
