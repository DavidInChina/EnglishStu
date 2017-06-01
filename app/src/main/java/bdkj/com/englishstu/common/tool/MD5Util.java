package bdkj.com.englishstu.common.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by younminx on 2017/2/13.
 * MD5加密
 */

public class MD5Util
    {
        /**
         * 32位MD5加密方法
         *
         * @param encodeStr 待加密内容
         * @return 加密字段
         * 16位小写加密只需getMd5Value("xxx").substring(8, 24);即可
         */
        public static String md5Encode(String encodeStr)
            {
                try
                    {
                        MessageDigest bmd5 = MessageDigest.getInstance("MD5");
                        bmd5.update(encodeStr.getBytes());
                        int i;
                        StringBuffer buf = new StringBuffer();
                        byte[] b = bmd5.digest();// 加密
                        for (int offset = 0; offset < b.length; offset++)
                            {
                                i = b[offset];
                                if (i < 0)
                                    i += 256;
                                if (i < 16)
                                    buf.append("0");
                                buf.append(Integer.toHexString(i));
                            }
                        return buf.toString();
                    } catch (NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                return "";
            }
    }
