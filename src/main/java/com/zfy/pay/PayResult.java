package com.zfy.pay;

/**
 * CreateAt : 2019/6/13
 * Describe : 支付结果对象
 *
 * @author chendong
 */
public class PayResult {

    public static final int STATE_START    = 0x11;
    public static final int STATE_CANCEL   = 0x12;
    public static final int STATE_SUCCESS  = 0x13;
    public static final int STATE_FAIL     = 0x14;
    public static final int STATE_COMPLETE = 0x15;

    public static final int ERR_OK               = 0;
    public static final int ERR_COMM             = 1;
    public static final int ERR_PARAM            = 2;
    public static final int ERR_INSTANCE         = 3;
    public static final int ERR_NOT_INSTALL      = 4;
    public static final int ERR_PAY_REPEAT       = 5;
    public static final int ERR_ORDER_FAIL       = 6;
    public static final int ERR_ACTION_PAY       = 7;
    public static final int ERR_SYSTEM           = 8;
    public static final int ERR_CHECK_PAY_STATUS = 9;


    private int    errCode = ERR_OK;
    private String errMsg  = "";
    private String msg     = "";
    private int    state;

    private Object tag;

    public void setErrCode(int errCode) {
        this.errCode = errCode;
        switch (errCode) {
            case ERR_OK:
                this.errMsg = "支付成功";
                break;
            case ERR_PARAM:
                this.errMsg = "参数错误";
                break;
            case ERR_INSTANCE:
                this.errMsg = "实例创建错误";
                break;
            case ERR_NOT_INSTALL:
                this.errMsg = "应用未安装";
                break;
            case ERR_PAY_REPEAT:
                this.errMsg = "重复支付";
                break;
            case ERR_ORDER_FAIL:
                this.errMsg = "订单创建失败";
                break;
            case ERR_ACTION_PAY:
                this.errMsg = "发起支付失败";
                break;
            case ERR_SYSTEM:
                this.errMsg = "支付平台错误";
                break;
            case ERR_CHECK_PAY_STATUS:
                this.errMsg = "支付结果检测失败";
                break;
        }
        this.msg = this.errMsg;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void appendMsg(String msg) {
        this.errMsg = this.errMsg + "  " + msg;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getState() {
        return state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public PayResult(int state) {
        this.state = state;
    }

    public PayResult() {
    }
}
