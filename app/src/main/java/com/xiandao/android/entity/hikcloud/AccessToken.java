package com.xiandao.android.entity.hikcloud;

import java.io.Serializable;

public class AccessToken implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * access_token : 2ebdef39-77d9-484f-9dfc-e28edd0ea554
     * token_type : bearer
     * refresh_token :
     * scope : app
     * expires_in : 601563
     */

    private String access_token;
    private String token_type;
    private String expire_time;
    private String scope;
    private int expires_in;
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

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String refresh_token) {
        this.expire_time = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
