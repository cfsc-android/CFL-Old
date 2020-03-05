package com.xiandao.android.entity;

import java.io.Serializable;

/**
 * Created by ZXL on 2019/4/2.
 * Describe:
 */
public class CarRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * car_code :
     * in_time :
     * out_time :
     * time_lenth :
     */

    private String car_code;
    private String in_time;
    private String out_time;
    private String time_lenth;

    public String getCar_code() {
        return car_code;
    }

    public void setCar_code(String car_code) {
        this.car_code = car_code;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getTime_lenth() {
        return time_lenth;
    }

    public void setTime_lenth(String time_lenth) {
        this.time_lenth = time_lenth;
    }
}
