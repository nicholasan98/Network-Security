package code;

/**
 * @author Nicholas An
 */

import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Generator {
	
    public static int getNonce(){
        Random random = new Random();
        int limit = 150;
        int nonce = random.nextInt(limit);
        return nonce;
    }
    
    public static SecretKey obtainKey(String key) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
        byte[] key_in_bytes = key.getBytes();
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey DESkey = keyFactory.generateSecret(new DESKeySpec(key_in_bytes));
        return DESkey;
    }
    
    public static byte[] getDESPlainBytes(SecretKey key, byte[] encryptInput) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher DESCipher;
        DESCipher = Cipher.getInstance("DES");
        DESCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] outputDecrypt = DESCipher.doFinal(encryptInput);
        return outputDecrypt;
    }
    
    public static byte[] getDESCipher(SecretKey key, byte[] textInput) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher DESCipher;
        DESCipher = Cipher.getInstance("DES");
        DESCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] outputEncrypt = DESCipher.doFinal(textInput);
        return outputEncrypt;
    }
    
    public static void receivedCipher(byte[] receivedCipher){
        String receivedCipherString = receivedCipher.toString();
        System.out.println("The received cipher bytes are: " + receivedCipherString + "\n");
        System.out.println("The cipher itself is received as: " + new String(receivedCipher) + "\n");
    }
    
    public static void receivedDecrypt(byte[] outputDecrypt){
        String outputDecryptString = outputDecrypt.toString();
        System.out.println("The decrypted message was received as: " + new String(outputDecrypt) + "\n");
    }
    
    public static void messageSent(String plainText, byte[] cipherOut){
        String cipherOutString = cipherOut.toString();
        System.out.println("The following message will be encrypted: " + plainText + "\n");
        System.out.println("The encrypted message was sent as: " + cipherOutString + "\n");
    }
    
       public static boolean nonceConfirm(int givenNonce, int receivedNonce){
        if(givenNonce == receivedNonce){
            System.out.println("Both the nonces match. \n");
            return true;
        }
        else if(givenNonce != receivedNonce){
            System.out.println("The nonce does not match, it is possible to be attacked \n");
            return false;
        }
        else{
            System.out.println("Not even sure what happened. Please restart \n");
            return false;
        }
    }
    
}
