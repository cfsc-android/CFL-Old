package com.xiandao.android.entity.smart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Loong on 2020/2/14.
 * Version: 1.0
 * Describe:
 */
public class QrCodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String qrCodeUrl;

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
