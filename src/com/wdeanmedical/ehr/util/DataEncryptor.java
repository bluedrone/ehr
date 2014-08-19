package com.wdeanmedical.ehr.util;

import java.security.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class DataEncryptor {

  private static final String ALGORITHM = "AES";
  private static byte[] encryptionKey;

  public static void setEncryptionKey(String key) throws Exception {
    encryptionKey = key.getBytes("UTF-8");
  }


  public static String encrypt(String valueToEnc) throws Exception {
    String encryptedValue = null;

    if (valueToEnc != null) {
      Key key = generateKey();
      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.ENCRYPT_MODE, key);
      byte[] encValue = c.doFinal(valueToEnc.getBytes());
      encryptedValue = new BASE64Encoder().encode(encValue);
    } 
    else {
      return "";
    }
    return encryptedValue;
  }
  
  
  

  public static String decrypt(String encryptedValue) throws Exception {
    String decryptedValue = null;

    if (encryptedValue != null) {
      Key key = generateKey();
      Cipher c = Cipher.getInstance(ALGORITHM);
      c.init(Cipher.DECRYPT_MODE, key);
      byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
      byte[] decValue = c.doFinal(decordedValue);
      decryptedValue = new String(decValue);
    } 
    else {
      return "";
    }
    return decryptedValue;
  }
  
  

  private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(encryptionKey, ALGORITHM);
    return key;
  }
  
  
}
