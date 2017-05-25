package com.movebeans.lib.common.tool;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * ClassName: RequestParamsUtils
 * Description: 请求参数获取工具
 * Creator: chenwei
 * Date: 16/8/29 12:54
 * Version: 1.0
 */
public class RequestParamsUtils
    {

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 组成请求参数
     *
     * @param publicMap
     * @param privateMap
     * @return
     * @throws JSONException
     */
    public static String buildRequestParams(Map<String, Object> publicMap, Map<String, Object> privateMap) throws JSONException {
        JSONObject privateParams = new JSONObject();
        String privateJson = null;
        if (privateMap != null) {
            Iterator<Map.Entry<String, Object>> entries = privateMap.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                privateParams.put(entry.getKey(), entry.getValue());
            }
            privateJson = privateParams.toString().replaceAll("\\\\/","/");
        }
        JSONObject publicObject = new JSONObject();
        if (publicMap != null) {
            publicMap.put("body", privateJson == null ? "{}" : privateJson);
            Map<Object, Object> sortParams = new TreeMap<Object, Object>(publicMap);
            Iterator<Map.Entry<String, Object>> entries = publicMap.entrySet().iterator();

            while (entries.hasNext()) {
                Map.Entry<String, Object> entry = entries.next();
                String key = entry.getKey();
                if (!"body".equals(key)) {
                    publicObject.put(key, entry.getValue());
                }
            }
            String sign = createSign("utf-8", sortParams);
            publicObject.put("sign", sign);
        }
        publicObject.put("body", privateParams.length() <= 0 ? new JSONObject() : privateParams);
        return publicObject.toString().replaceAll("\\\\/","/");
//        return publicObject.toString();
    }

    /**
     * 生成sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, Map<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=b389e664-6cc3-11e6-8b77-86f30ca893d3");
        Log.d("RequestParamsUtils", sb.toString());
        String sign = MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    public static void main(String[] args) {
        String sign = MD5Encode("apiVer=1.0&body={\\n  \\\"cityCode\\\" : \\\"320100\\\",\\n  \\\"provinceCode\\\" : \\\"320000\\\",\\n  \\\"areaCode\\\" : \\\"\\\",\\n  \\\"avatar\\\" : \\\"http:\\\\/\\\\/avatar.expop.com.cn\\\\/201610021475402480329.png\\\",\\n  \\\"birthday\\\" : \\\"1992-09-16\\\",\\n  \\\"sex\\\" : 0,\\n  \\\"nickName\\\" : \\\"Yuanlaw\\\"\\n}&clientToken=68567FF5-460A-4367-9A00-92B65A54F0E2&clientType=1&clientVer=1.3.0&deviceToken=d9fe9134951e708d2fd1bf1a63e305c9497afce187b0a639aa0ae67389432182&timestamp=1476435264365&userId=3&userToken=1afaafcad22dc1b93db0157907c80dae&key=b389e664-6cc3-11e6-8b77-86f30ca893d3", "utf-8").toUpperCase();
        System.out.println(sign);
    }

    /**
     * MD5加密
     * @param origin
     * @param charsetname
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
}
