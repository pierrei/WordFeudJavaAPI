package nu.mrpi.util;

/**
 * @author Pierre Ingmansson
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

    private static String convertToHex(final byte[] data) {
        final StringBuilder buf = new StringBuilder();
        for (final byte aByte : data) {
            int halfByte = (aByte >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfByte) && (halfByte <= 9))
                    buf.append((char) ('0' + halfByte));
                else
                    buf.append((char) ('a' + (halfByte - 10)));
                halfByte = aByte & 0x0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }

    public static String sha1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");

        md.update(text.getBytes("iso-8859-1"), 0, text.length());

        return convertToHex(md.digest());
    }
}
