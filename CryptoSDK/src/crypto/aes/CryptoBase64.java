package crypto.aes;
   import java.security.SecureRandom;

	import android.util.Base64;

	/**
	 * Basic conversion methods
	 * @author CGI
	 *
	 */
	public class CryptoBase64 {
		public static SecureRandom random = new SecureRandom();
		
		/**
		 * Convert @param bytes to Hexadecimal format
		 * @param bytes
		 * @return String
		 */
	    public static String toHex(byte[] bytes) {
	        StringBuffer buff = new StringBuffer();
	        for (byte b : bytes) {
	            buff.append(String.format("%02X", b));
	        }

	        return buff.toString();
	    }

	    /**
	     * Converts @param bytes to Base64
	     * @param bytes
	     * @return String
	     */
	    public static String toBase64(byte[] bytes) {
	        return Base64.encodeToString(bytes, Base64.NO_WRAP);
	    }

	    /**
	     * Convert @param from Base64 to byte[]
	     * @param base64
	     * @return byte[]
	     */
	    public static byte[] fromBase64(String base64) {
	        return Base64.decode(base64, Base64.NO_WRAP);
	    }
		
	    /**
	     * Creating IV
	     * @param length
	     * @return new iv  
	     */
	    public static byte[] generateIv(int length) {
	        byte[] b = new byte[length];
	        random.nextBytes(b);

	        return b;
	    }
	}


