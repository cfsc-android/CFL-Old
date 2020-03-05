package com.xiandao.android.bean;

import com.xiandao.android.base.BaseParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * 此类描述的是:接口方法
 *
 * @author TanYong
 *         create at 2017/4/21 10:23
 */
public class ParametBean extends BaseParameter {

    public static Map<String, Object> getParameter(String[] key, Object[] val) {
        Map<String, Object> map = getOverallSituationParameter(new HashMap<String, Object>(), key, val);
        return map;
    }

    public static Map<String, Object> getNoLoginParameter(String[] key, Object[] val) {
        Map<String, Object> map = getOverallNoLoginParameter(new HashMap<String, Object>(), key, val);
        return map;
    }

    public static Map<String, Object> getAccesTokenParameter(String[] key, Object[] val) {
        Map<String, Object> map = getAccesTokennParameter(new HashMap<String, Object>(), key, val);
        return map;
    }

    public static Map<String, Object> getLoginParameter(String[] key, Object[] val) {
        Map<String, Object> map = getLoginOverallSituationParameter(new HashMap<String, Object>(), key, val);
        return map;
    }

}
