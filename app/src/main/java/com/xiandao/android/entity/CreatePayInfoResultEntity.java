package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * 此类描述的是:支付信息对象
 * {
 * "data": {
 * "id": null,
 * "payid": "74D5B5506C074A8D887BC9F0E77D417B",
 * "operationuser": "20",
 * "paylistids": "21",
 * "roomid": "14",
 * "payamount": "1",
 * "paytype": "通联支付",
 * "iphonetype": "0",
 * "createdate": "2017-12-12 14:26:40",
 * "ispay": "0"
 * },
 * "resultCode": "0",
 * "msg": "创建成功"
 * }
 *
 * @author TanYong
 *         create at 2017/5/18 10:33
 */
public class CreatePayInfoResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private PayInfoEntity data;
    private String resultCode;
    private String msg;

    public PayInfoEntity getData() {
        return data;
    }

    public void setData(PayInfoEntity data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
