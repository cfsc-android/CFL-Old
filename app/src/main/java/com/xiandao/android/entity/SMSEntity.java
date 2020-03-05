package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:发送验证码对象
 * {"code":200,"msg":"6","obj":"6372"}
 *
 * @author TanYong
 *         create at 2017/5/8 10:51
 */
public class SMSEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String obj;//验证码
    private String msg;//包括成功以及异常原因
    private String resultCode;//0 标示成功, 其它表示失败

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
