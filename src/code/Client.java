package code;

/**
 * @author Nicholas An
 * For this lab, INITIATOR A is the client and RESPONDER B is the server
 * Run the Server.java first and then Client.java
 * I like to use edgar allen poe as my quotes and keys
 */

import java.io.*;
import java.net.*;

import javax.crypto.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Client {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        byte[] sendCipher = null;
        byte[] receiveCipher = null;
        byte[] decrypt = null;
        byte[] textBytes;
        
        String id = "INITIATOR A", course =  "NETWORK SECURITY", key = "EDGARALLENPOE", message = "Once upon a midnight dreary";
        String text;
        
        // Pi
        int portNumber = 3141;
        int nonce;
        int nonceValue;
        int tempNonce;
        
        Socket client;
        
        SecretKey sessionKey;
        SecretKey courseKey;
        
        courseKey = Generator.obtainKey(course);
        nonce = Generator.getNonce();
        
        client = new Socket("localhost", portNumber);
        System.out.println("Connected to " + client.getRemoteSocketAddress() + "\n");
        
        DataOutputStream output = new DataOutputStream(client.getOutputStream());
        text = nonce + "-" + id;
        sendCipher = Generator.getDESCipher(courseKey, text.getBytes());
        
        Generator.messageSent(text, sendCipher);
        output.writeInt(sendCipher.length);
        output.write(sendCipher);
        
        DataInputStream input = new DataInputStream(client.getInputStream());
        int length = input.readInt();
        
        if(length > 0) receiveCipher = new byte[length];
        
        input.read(receiveCipher, 0, length);
        Generator.receivedCipher(receiveCipher);
        decrypt = Generator.getDESPlainBytes(courseKey, receiveCipher);
        Generator.receivedDecrypt(decrypt);
        
        text = new String(decrypt);
        String[] arrayDecrypt = text.split("\\-");
        
        nonceValue = Integer.parseInt(arrayDecrypt[1]);
        tempNonce = Integer.parseInt(arrayDecrypt[0]);

        if(Generator.nonceConfirm(nonce, tempNonce)){
            System.out.println("Nonce has worked, Encryption was used with the public key \n");
            sendCipher = Generator.getDESCipher(courseKey, arrayDecrypt[1].getBytes());
            Generator.messageSent((String)arrayDecrypt[1], sendCipher);
            output.writeInt(sendCipher.length);
            output.write(sendCipher);
        }
        else{
            System.out.println("Nonce has not worked, replay attacks are possible");
        }
        
        sessionKey = Generator.obtainKey(key);
        sendCipher = Generator.getDESCipher(courseKey, key.getBytes());
       
        System.out.println("Private key has been sent, the communication will commence \n");
        Generator.messageSent(key, sendCipher);
        output.writeInt(sendCipher.length);
        output.write(sendCipher);
        
        nonce = Generator.getNonce();
        message = message + "-" + nonce;
        sendCipher = Generator.getDESCipher(sessionKey, message.getBytes());
        Generator.messageSent(message, sendCipher);
        
        output.writeInt(sendCipher.length);
        output.write(sendCipher);
        
        length = input.readInt();
        if(length > 0) receiveCipher = new byte[length];
        
        input.read(receiveCipher,0,length);
        Generator.receivedCipher(receiveCipher);
        
        textBytes = Generator.getDESPlainBytes(sessionKey, receiveCipher);
        Generator.receivedDecrypt(textBytes);
        
        text = new String(textBytes);
        arrayDecrypt = text.split("\\-");
        
        System.out.println("Extracted host message: " + arrayDecrypt[0] + "\n");
        System.out.println("Value of host nonce: " + arrayDecrypt[1] + "\n");
        System.out.println("Value of authorized nonce: " + arrayDecrypt[2] + "\n");
        
        String hostMessage = arrayDecrypt[0];
        nonceValue = Integer.parseInt(arrayDecrypt[1]);
        tempNonce = Integer.parseInt(arrayDecrypt[2]);

        input.close();
        output.close();
        client.close();
    }
    
}
