package com.xiandao.android.entity.smart;

import java.io.Serializable;

/**
 * Created by Loong on 2020/2/28.
 * Version: 1.0
 * Describe:
 */
public class TokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * access_token : 2b368d4c-b115-4c40-b706-981d750d4fae
     * token_type : bearer
     * refresh_token : 7c325630-67db-4a0c-8313-055353874ebc
     * expires_in : 169389
     * scope : all
     */

    private String access_token;
    private String token_type;
    private String refresh_token;
    private int expires_in;
    private String scope;
    private long init_time;

    public long getInit_time() {
        return init_time;
    }

    public void setInit_time(long init_time) {
        this.init_time = init_time;
    }


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
