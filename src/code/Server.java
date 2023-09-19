package code;

/**
 * @author Nicholas An
 * For this lab, INITIATOR A is the client and initiates the chat to the server
 * RESPONDER B is the server and replies to INITATIOR A
 * Run the Server.java, then run Client.java to start the chat session
 * I like to use edgar allen poe as my quotes and keys
 */

import java.net.*;
import java.io.*;

import javax.crypto.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class Server {
    
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        int portNumber = 3141;
        String server_id = "RESPONDER B", word = "NETWORK SECURITY", ks = "EDGARALLENPOE", serverMessage = "While I pondered weak and weary";
        String text, client_id, clientMessage;
        
        int nonce;
        int receivedNonce;
        int time;
        
        ServerSocket serverConnection;
        Socket server;
        
        byte[] sendCipher;
        byte[] receiveCipher = null;
        byte[] outputDecrypt;
        byte[] textBytes;
        
        SecretKey pass;
        SecretKey sessionK;
        
        serverConnection = new ServerSocket(portNumber);
        System.out.println("Attempting to make a connection to the client " + serverConnection.getLocalPort() + "... \n");
        server = serverConnection.accept();
        System.out.println("Connected to " + server.getRemoteSocketAddress() + "\n");
        
        DataInputStream input = new DataInputStream(server.getInputStream());
        time = input.readInt();
        if(time > 0) receiveCipher = new byte[time];
        
        input.read(receiveCipher, 0, time);
        Generator.receivedCipher(receiveCipher);
        pass = Generator.obtainKey(word);
        textBytes = Generator.getDESPlainBytes(pass, receiveCipher);
        Generator.receivedDecrypt(textBytes);
        
        text = new String(textBytes);
        String[] arrayDecrypt = text.split("\\-");
        
        System.out.println("Nonce Value obtained: " + arrayDecrypt[0] + "\n");
        System.out.println("Client ID from the client side: " + arrayDecrypt[1] + "\n");
        
        receivedNonce = Integer.parseInt(arrayDecrypt[0]);
        client_id = arrayDecrypt[1];
        
        nonce = Generator.getNonce();
        text = receivedNonce + "-" + nonce;
        
        System.out.println("Nonce was generated for the server: " + nonce + "\n");
        textBytes = text.getBytes();
        
        sendCipher = Generator.getDESCipher(pass, textBytes);
        Generator.messageSent(text, sendCipher);
        
        DataOutputStream output = new DataOutputStream(server.getOutputStream());
        
        output.writeInt(sendCipher.length);
        output.write(sendCipher);
        
        time = input.readInt();
        
        if(time > 0) receiveCipher = new byte[time];
        
        input.read(receiveCipher, 0, time);
        Generator.receivedCipher(receiveCipher);
        textBytes = Generator.getDESPlainBytes(pass, receiveCipher);
        Generator.receivedDecrypt(textBytes);
        receivedNonce = Integer.parseInt(new String(textBytes));
        
        if(Generator.nonceConfirm(nonce, receivedNonce)){
            System.out.println("Sending client session key... \n");
        }
        
        time = input.readInt();
        if(time > 0) receiveCipher = new byte[time];
        
        input.read(receiveCipher, 0, time);
        Generator.receivedCipher(receiveCipher);
        textBytes = Generator.getDESPlainBytes(pass, receiveCipher);
        text = new String(textBytes);
        sessionK = Generator.obtainKey(text);
        Generator.receivedDecrypt(textBytes);
        System.out.println("The session key has been created.... \n");
        System.out.println("The client and server can now begin their communication. \n");
        System.out.println("Beginning the chat session... \n");
        
        time = input.readInt();
        if(time > 0) receiveCipher = new byte[time];
        
        input.read(receiveCipher, 0, time);
        
        Generator.receivedCipher(receiveCipher);
        textBytes = Generator.getDESPlainBytes(sessionK, receiveCipher);
        Generator.receivedDecrypt(textBytes);
        
        text = new String(textBytes);
        arrayDecrypt = text.split("\\-");
        
        System.out.println("Client message: " + arrayDecrypt[0] + "\n");
        System.out.println("Value of nonce: " + arrayDecrypt[1] + "\n");

        
        receivedNonce = Integer.parseInt(arrayDecrypt[1]);
        clientMessage = (arrayDecrypt[0]);
        
        nonce = Generator.getNonce();
        serverMessage = serverMessage + "-" + nonce + "-" + receivedNonce;
        
        sendCipher = Generator.getDESCipher(sessionK, serverMessage.getBytes());
        Generator.messageSent(serverMessage, sendCipher);
        output.writeInt(sendCipher.length);
        output.write(sendCipher);

        input.close();
        output.close();
        server.close();
    }
}
