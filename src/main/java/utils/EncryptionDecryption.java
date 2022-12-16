package utils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import constants.Constants;


public class EncryptionDecryption {

	public String handHeldPassword=null;
	public String SECRET_KEY_1 = "ssdkF$HUy2A#D%kd";
	public String SECRET_KEY_2 = "weJiSEvR5yAC5ftB";

	public  IvParameterSpec ivParameterSpec;
	public  SecretKeySpec secretKeySpec;
	public  Cipher cipher;

	public FileOperations fileOperationsObj = new FileOperations();
	public Constants constants=new Constants();

	/**
	 * Encrypt the string with this internal algorithm.
	 *
	 * @param toBeEncrypt string object to be encrypt.
	 * @return returns encrypted string.
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */

	public  String encrypt(String toBeEncrypt) {
		try{
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
			return Base64.encodeBase64String(encrypted);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Decrypt this string with the internal algorithm. The passed argument should be encrypted using
	 * {@link #encrypt(String) encrypt} method of this class.
	 *
	 * @param encrypted encrypted string that was encrypted using {@link #encrypt(String) encrypt} method.
	 * @return decrypted string.
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */

	public String decrypt(String encrypted)  {
		try{
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
			byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(decryptedBytes);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public  void setRequiredVariables()  {
		try{
			ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
			secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public  void encryptPassword() throws IOException, ConfigurationException {
		setRequiredVariables();
		String encryptPassword = encrypt( fileOperationsObj.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "password"));
		System.out.println("****************************************************** THE ENCRYPTED PASSWORD IS : "+ encryptPassword);
		fileOperationsObj.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "password", encryptPassword);
	}

	//	@Test
	public  String decryptPassword(String passwordToDecrypt) {
		setRequiredVariables();
		//		String decryptPassword = decrypt( fileOperationsObj.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, passwordToDecrypt));
		String decryptPassword = decrypt(passwordToDecrypt);
		return decryptPassword;
		//		System.out.println("****************************************************** THE DECRYPTED PASSWORD IS : "+ decryptPassword);
	}

}
