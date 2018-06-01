package com.ns.common.kit;

import com.jfinal.kit.StrKit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlKit {
    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<String, String>();
        if (StrKit.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        String key = "";
        String value = "";
        for (String temp : params) {
            key = temp.substring(0, temp.indexOf("="));
            value = temp.substring(temp.indexOf("=") + 1, temp.length());
            map.put(key, value);
        }

        return map;
    }

    /**
     * 原map喊UrlCode值
     * @param origin 返回URL解码后的集合
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> deCodeUrl (Map<String, String> origin) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();

        Iterator<Map.Entry<String, String>> entries = origin.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            result.put(entry.getKey(),urlDecode(entry.getValue()));
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        return result;
    }

    /**
     * Url 解码
     * @param orgin
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String urlDecode(String orgin) throws UnsupportedEncodingException {

        return URLDecoder.decode(orgin == null ? "" : orgin, "utf-8");
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
/*    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }*/
    /**
     * 将map转换为请求字符串
     * <p>data=xxx&msg_type=xxx</p>
     *
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String getUrlParamsByMap(Map<String, Object> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuffer data = new StringBuffer();
        boolean flag = false;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (flag) {
                data.append("&");
            } else {
                flag = true;
            }
            data.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), charset));
        }

        return data.toString();

    }
}
