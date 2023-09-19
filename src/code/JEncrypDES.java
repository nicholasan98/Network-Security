package code;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/*
 * Nicholas An
 * 
 */

import java.util.Scanner;

public class JEncrypDES {
	
	public static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
	
		System.out.println("Please enter the message 'No body can see me':");
		String message = input.nextLine();
		
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			SecretKey myDESKey = kg.generateKey();
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			
		}
		catch(Exception e) {
			
		}
		
		
	}

}
