
package org.xutils;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;


 /**
 * 类名：CharsetUtils
 * 作者：CJ
 * 创建时间：2014-10-24-上午11:07:49
 * 修改记录：
 * 修改人　　		修改时间　　		版本		描述
 *----------------------------------------------------------
 *
 *
 *----------------------------------------------------------	
 * Copyright (c)-2014烈焰鸟网络科技有限公司
 */
 
public class CharsetUtils {

    private CharsetUtils() {
    }

    public static String toCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String oldCharset = getEncoding(str, judgeCharsetLength);
            return new String(str.getBytes(oldCharset), charset);
        } catch (Throwable ex) {
            LogUtils.w(ex);
            return str;
        }
    }

    public static String getEncoding(final String str, int judgeCharsetLength) {
        String encode = CharsetUtils.DEFAULT_ENCODING_CHARSET;
        for (String charset : SUPPORT_CHARSET) {
            if (isCharset(str, charset, judgeCharsetLength)) {
                encode = charset;
                break;
            }
        }
        return encode;
    }

    public static boolean isCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String temp = str.length() > judgeCharsetLength ? str.substring(0, judgeCharsetLength) : str;
            return temp.equals(new String(temp.getBytes(charset), charset));
        } catch (Throwable e) {
            return false;
        }
    }

    public static final String DEFAULT_ENCODING_CHARSET = HTTP.DEFAULT_CONTENT_CHARSET;

    public static final List<String> SUPPORT_CHARSET = new ArrayList<String>();

    static {
        SUPPORT_CHARSET.add("ISO-8859-1");

        SUPPORT_CHARSET.add("GB2312");
        SUPPORT_CHARSET.add("GBK");
        SUPPORT_CHARSET.add("GB18030");

        SUPPORT_CHARSET.add("US-ASCII");
        SUPPORT_CHARSET.add("ASCII");

        SUPPORT_CHARSET.add("ISO-2022-KR");

        SUPPORT_CHARSET.add("ISO-8859-2");

        SUPPORT_CHARSET.add("ISO-2022-JP");
        SUPPORT_CHARSET.add("ISO-2022-JP-2");

        SUPPORT_CHARSET.add("UTF-8");
    }
}
