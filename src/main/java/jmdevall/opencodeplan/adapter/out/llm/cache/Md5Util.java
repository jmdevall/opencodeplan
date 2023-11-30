package jmdevall.opencodeplan.adapter.out.llm.cache;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String md5sum(String input) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unable to compute md5sum for string", ex);
        }
        assert (digest != null);
        digest.update(input.getBytes());
        BigInteger hash = new BigInteger(1, digest.digest());
        return (hash.toString(16));
    }
	
}
