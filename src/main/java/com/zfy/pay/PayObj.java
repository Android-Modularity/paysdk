package com.zfy.pay;

/**
 * CreateAt : 2019/6/13
 * Describe : 支付参数对象
 *
 * @author chendong
 */
public class PayObj {

    private String appId;
    private String packageName;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String sign;

    private String aliOrderStr;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPackageValue() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAliOrderStr() {
        return aliOrderStr;
    }

    public void setAliOrderStr(String aliOrderStr) {
        this.aliOrderStr = aliOrderStr;
    }
}
