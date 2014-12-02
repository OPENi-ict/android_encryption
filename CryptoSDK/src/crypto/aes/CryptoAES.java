package crypto.aes;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.content.Context;

import android.widget.Toast;

/**
 * Encryption methods
 * @author CGI
 *
 */
public class CryptoAES {
	//Number of iterations for the key generation
	private int pswdIterations = 65536  ;
	//length of vector key
	private int keySize = 256;
	private IvParameterSpec ivParams; 
	//length of vector iv
	private int tamiv=16;
	private static byte [] salt = new byte[10];	
	private CryptoSqlite con;
	//private Context context;
	
	/**
     * The algorithm used for encryption and decryption. AES is only available through
     * the BouncyCastle library.
     */
    private static final String ENCRYPTION_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";
    
    /**
     * The Bouncycastle Providername
     */
    private static final String CRYPTOPROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    
    /**
     * Initializing iv parameter
     */
	public CryptoAES(Context context){
	
		this.ivParams=new IvParameterSpec(CryptoBase64.generateIv((tamiv)));
		this.con=new CryptoSqlite(context,"crypto", null, 0);
	}
	/**
	 * Creates derived key from user's entered password
	 * @param password user's basic string for generating the key
	 * @return Base64 encoded Key 
	 */
	public  String createDeriveKey(String password){

		PKCS12ParametersGenerator gen = new PKCS12ParametersGenerator(new MD5Digest());
	
        gen.init(PBEParametersGenerator.PKCS12PasswordToBytes(password.toCharArray()), salt, 1024);
        KeyParameter param=(KeyParameter) gen.generateDerivedParameters(keySize);
        byte[] derivedKey=param.getKey();
       
        return CryptoBase64.toBase64(derivedKey);
        
	}
	/**
	 * Creates SecretKey for encryption/decryption from user's derived key
	 * @param derivedKey
	 * @param idUserCloud
	 */
	public void createSecrectkeyIdCloud(String derivedKey, int idUserCloud){
		    SecretKey key;
		try {
			
			key = this.newSecrectkey(derivedKey);
			String secrectkey=CryptoBase64.toBase64(key.getEncoded()); 
			this.setSecrectkey(con, secrectkey, idUserCloud);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException
				| NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Retrieve SecretKey from local database in the device
	 * @param idUserCloud
	 * @return SecretKey as String
	 */
	public String getSecrectkeyIdCloud(int idUserCloud){
		return 	con.findKey(idUserCloud, con.getReadableDatabase());	
		
	}
    /**
     * Encryption method.
     * 
     * The key is retrieved from a local database on the device
     * 
     * @param plainText text to encrypt
     * @param secretKey
     * @throws InvalidKeyException,NoSuchAlgorithmException,NoSuchPaddingException,InvalidAlgorithmParameterException
     * ,IllegalBlockSizeException,BadPaddingException,UnsupportedEncodingException,NoSuchProviderException,InvalidKeySpecException
     * @return encryptedText.
     */
	public String encrypt(String plainText,String secretKey){
		
		
		 //Log.d("debb", "clave"+secrectkey);
		byte[] encryptedTextBytes=null;
		try {
			
			
			SecretKeySpec secret = new SecretKeySpec(CryptoBase64.fromBase64(secretKey), "AES");
		

			Cipher cipher=this.newCipher(Cipher.ENCRYPT_MODE, secret);
			encryptedTextBytes=cipher.doFinal(plainText.getBytes("UTF-8"));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return CryptoBase64.toBase64(encryptedTextBytes);

	}
	/**
	 * Decryption method
	 * @param textocifrado
	 * @param secretKey
	 * @throws InvalidKeyException,NoSuchAlgorithmException,NoSuchPaddingException,InvalidAlgorithmParameterException
     * ,IllegalBlockSizeException,BadPaddingException,NoSuchProviderException
	 * @return decrypted text
	 * 
	 */
	public String descrypt(String textocifrado,String secretKey) throws UnsupportedEncodingException{
		byte[] decryptedTextBytes = null;
		byte[] encryptedTextBytes =CryptoBase64.fromBase64(textocifrado);
		SecretKeySpec secret = new SecretKeySpec(CryptoBase64.fromBase64(secretKey), "AES");

		try {
			Cipher cipher=this.newCipher(Cipher.DECRYPT_MODE, secret);
			decryptedTextBytes=cipher.doFinal(encryptedTextBytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
		return new String(decryptedTextBytes, "UTF-8");

	}
	/**
	 * Creating a instance of object SecrectKey from the derived user's password
	 * @param password
	 * @throws NoSuchAlgorithmException,InvalidKeySpecException,NoSuchProviderException 
	 * @return secretKey
	 */
	private SecretKey newSecrectkey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{
		SecretKeyFactory factory = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM, CRYPTOPROVIDER);
		PBEKeySpec spec = new PBEKeySpec(
				password.toCharArray(), 
				salt, 
				pswdIterations, 
				keySize
				);

		SecretKey secretKey = factory.generateSecret(spec);
		return secretKey;

	}
	
    
	/**
	 * SecretKey object and password are stored into the device's database. 
	 * @param con: connection to the device database
	 * @param password
	 * @param secrectkey
	 * 
	 */
	private void setSecrectkey(CryptoSqlite con,String secrectkey,int idUserCloud){

		con.insertInd(secrectkey,idUserCloud, con.getWritableDatabase());

	}
	/**
	 * Creating a cipher object that receives the mode of operation (encryption or decryption) and secretkey.
	 * @param modo
	 * @param key
	 * @throws NoSuchAlgorithmException,NoSuchPaddingException,InvalidKeyException,InvalidAlgorithmParameterException,NoSuchProviderException 
	 * @return cipher
	 */
	private Cipher  newCipher(int modo,SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchProviderException{
		Cipher cipher = null;
		cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM, CRYPTOPROVIDER);
		cipher.init(modo, key, this.ivParams);
		return cipher;


	}
	
	/**
	 * Check if the given derived key is the same that the stored one.
	 */
	public boolean comproClaveDerivada(String claveDrivada,Context context) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException{
		SecretKey key=this.newSecrectkey(claveDrivada);
		boolean resul=true;
		 if(this.con.findDerivedKey(CryptoBase64.toBase64(key.getEncoded()), this.con.getReadableDatabase())==null){
			 resul=false;
			 Toast toast1 =
			            Toast.makeText(context,
			                    "Incorrect password", Toast.LENGTH_SHORT);		 
			             toast1.show();
		 }
		return resul;
		
	}
	/**
	 * Registrer a User into the local database in the mobile device 
	 * @param idUserCloud
	 */
	public void registrerUser(int idUserCloud){
		this.con.registrerUser( idUserCloud, this.con.getReadableDatabase());
	}

}
