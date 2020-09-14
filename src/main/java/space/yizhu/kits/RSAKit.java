package space.yizhu.kits;/* Created by xiuxi on 2018/7/19.*/

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAKit {


    public static KeyPair creatmyKey() {
        KeyPair myPair;
        long mySeed;
        mySeed = System.currentTimeMillis() * 78451;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(mySeed);
            keyGen.initialize(1024, random);
            myPair = keyGen.generateKeyPair();

        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
        return myPair;
    }


    private static byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            // 加密时超过117字节就报错。为此采用分段加密的办法来加密
            byte[] enBytes = null;
            for (int i = 0; i < data.length; i += 117) {
// 注意要使用2的倍数,否则会出现加密后的内容再解密时为乱码
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 117));
                enBytes = ArrayUtils.addAll(enBytes, doFinal);
            }
            return enBytes;
        } catch (Exception e) {
            return null;
        }
    }


    private static String decryptByRSA(byte[] privKeyInByte, byte[] data) {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(
                    privKeyInByte);
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
                sb.append(new String(doFinal));
            }
            return sb.toString();
        } catch (Exception e) {
            SysKit.print(e);
            return null;
        }

    }


    /**
     * 计算字符串的SHA数字摘要，以byte[]形式返回
     */
    public static byte[] MdigestSHA(String source) {
        //byte[] nullreturn = { 0 };
        try {
            MessageDigest thisMD = MessageDigest.getInstance("SHA");
            byte[] digest = thisMD.digest(source.getBytes("UTF-8"));
            return digest;
        } catch (Exception e) {
            return null;
        }
    }

    //加密方法
    public static String encryption(String value) {

        String PUBLICKEY1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDkRlA29arKpFR7vj4I7D2srJZJ\n" +
                "XM504HxEk/S07p+j7fFfni75MZ1G3d0wYT1m7x1e3pyWsvE5XbZr7vVg72GeFoOU\n" +
                "V/UaUELQLoaCY8eoiASHfDtxcH0+8m9IEsSeyRHTqernOfTBawDiSp1wEBsvu4RZ\n" +
                "sdH69cVswneqTWhiIwIDAQAB";

        String encrStr = "";


        //使用公钥对摘要进行加密 获得密文
        byte[] signpub_pri = encryptByRSA(Base64.decodeBase64(PUBLICKEY1.getBytes()), value.getBytes());
        encrStr = new String(Base64.encodeBase64(signpub_pri));

        return encrStr;
    }


    //解密方法
    public static String decryption(String encrypt) {
//已转成pck8私钥
        String PRIVATEKEY1 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALNebnEoRA7AG1gZ\n" +
                "rIIhMas2TCi1ndxUnA+JXHgBQS/+NUb9ARMzm0D9efgBhbD9Vk0uXVr3UvO75aiW\n" +
                "gjwF1zaLG9Q1MYSm82wajttCmzcfMctIeolUvbN1OB9srVq7hlM0UabBSTCpx4hk\n" +
                "PkVUROcpG4tQjzLViUxcIprBkY1ZAgMBAAECgYEAoDf9lv5MrJBU3IEyYpqb7z91\n" +
                "D/gKpkC9lfMwsdENFX2QwXRFfY1fZNIGHM3fSZVBfXeo1BSP+D8iiVDHzC27afAw\n" +
                "QGFzwWi2tkNc7/xi4k8n2IaL38FtmSf71A2QR8gAOJRPACQUC4bjw5n4nHWmunnO\n" +
                "MvJaG+qd/D5MTsEoZgECQQDuCIFBFXMNtcd6eGzKbj1d3QmsIpHyCVlIez30kJcA\n" +
                "jMA0w4NncV0uPbYa/6GGmWgfosP4G8Dgyn68WgnOFWjJAkEAwOhcRP5WOAkVSMT9\n" +
                "exWYFjHBbOKeK0huTS6HM+iLTIdG4wtqg4JKWPWLsnFJdpRIi83XLHQDMQhzakEr\n" +
                "zdzYEQJAJGlGpFcMn3dECVbshVaxqm8KcLtlCdomPzEi/As2Hg5pxGh37FXqBA3K\n" +
                "knyyD63dwKEcSxKrwXcYReWcBFtACQJAFYja5AjQbyj0a08yTM84TyH2ycTyizpm\n" +
                "tc7/4NeQ1VY0n5vipBOjYRLoKV9kKAydnJ2564h34r1ixPXJgg2pQQJBAJUMz/ll\n" +
                "zMzBPfPCzR6b3M+IZFkh+4pLJqTMTZqZlBOwiFBkJuykpNsidGYB4c3FHy6tI9W+\n" +
                "ljYhsd9OTLioWhc=";


        String value = "";

        //使用私钥对密文进行解密 返回解密后的数据
        try {
            value = decryptByRSA(Base64.decodeBase64(PRIVATEKEY1.getBytes()), Base64.decodeBase64(encrypt.getBytes()));

        } catch (Exception ignored) {
            value = encrypt;

        }
        if (value == null) SysKit.print("解密失败:" + encrypt);
        return value;
    }

    private static byte[] removeMSZero(byte[] data) {
        byte[] data1;
        int len = data.length;
        if (data[0] == 0) {
            data1 = new byte[data.length - 1];
            System.arraycopy(data, 1, data1, 0, len - 1);
        } else
            data1 = data;

        return data1;
    }

    public static void main(String[] args) {
        String msg = decryption("\\\"JxQ4KRC3TSrf8PqHvfoC9nJ6VXsbNRy5crlS1hGVBJqx48h+DsgmDml0RKGofZmlTzFzEcN25oLCM1DjUnKnGhNPBywnLKY6vinvIUAnxt7IHTTmzdHJF7GfhPNmz2wmA0Sxdru7GpPJ5pPna\\\\\\/lEMYSfsPtmLmHEeNGuaXoryRc=\\\"");

        SysKit.print(msg);

     /*

     Map   map = new Gson().fromJson(msg, new TypeToken<Map<String, String>>() {
        }.getType());
        String callbackurl = String.valueOf(map.get("callbackurl"));
        String domain="";
        if (null!=callbackurl&&callbackurl.contains("callbackapi"))
             domain = callbackurl.substring(0, callbackurl.indexOf("callbackapi") + 12);
        SysKit.print(domain);*/

/*        KeyPair mykey = creatmyKey();
        new  String(Base64.encodeBase64(mykey.getPrivate().getEncoded()));
        new  String(Base64.encodeBase64(mykey.getPublic().getEncoded()));*/

    }


}