package bdkj.com.englishstu.common.tool;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by younminx on 2017/2/14.
 * 3des加解密工具类
 */

public class DES3Util
    {
        private static String keyString = "abcdefghabcdefghabcdefgh"; //密钥24位
        private static String iv = "abcdefgh";//偏移向量
        private static String cString = "DESede/CBC/PKCS7Padding";//补码方式

        /**
         * DES加密
         *
         * @param encryptString 待加密内容
         * @return 加密后内容
         * @throws Exception 密钥异常
         */
        public static String encryptDES(String encryptString)
                throws Exception
            {
                Key deskey = new SecretKeySpec(keyString.getBytes(), cString);
                Cipher cipher = Cipher.getInstance(cString);
                IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
                byte[] bOut = cipher.doFinal(encryptString.getBytes());
                return Base64.encodeToString(bOut, Base64.DEFAULT);
            }

        /**
         * DES解密
         *
         * @param decryptString 待解密内容
         * @return 解密后内容
         * @throws Exception 密钥异常
         */
        public static String decryptDES(String decryptString)
                throws Exception
            {
                byte[] data = Base64.decode(decryptString, Base64.DEFAULT);
                Key deskey = new SecretKeySpec(keyString.getBytes(), cString);
                Cipher cipher = Cipher.getInstance(cString);
                IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
                byte decryptedData[] = cipher.doFinal(data);
                return new String(decryptedData);
            }
    }
