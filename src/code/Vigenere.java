package code;

/*
 * Nicholas An
 * 
 */

import java.util.Scanner;

public class Vigenere {
    
	public static Scanner input = new Scanner(System.in);
	
    public static void main(String[] args) {
    	
    	System.out.println("Please enter in the key 'RELATIONS': ");
    	String key = input.nextLine();
    	//Some people can't follow instructions
    	//automatically changes to upper case
    	key = key.toUpperCase();
    	
    	System.out.println("Please enter in the Hamlet quote 'TO BE OR NOT TO BE THAT IS THE QUESTION':");
    	String hamlet = input.nextLine();
    	hamlet = hamlet.toUpperCase();
    	
    	System.out.println("Hamlet's soliloquy started with: " + hamlet);
    	System.out.println("The encrypted message is: " + vigenere_encrypt(hamlet, key));
    	System.out.println("The decrypted message is: " + vigenere_decrypt(vigenere_encrypt(hamlet, key), key));
        
    }
	
    //Encryption
	public static String vigenere_encrypt(String text, String key)	{
		
		//Create arrays for the plain text, key, length text and the encrypted message
		char pt[] = text.toCharArray();
		int length = pt.length;
		char kr[] = new char[length];
		char encrypted[] = new char[length];
		
		//Fills the key array with a generated key based on the original keyword
		//Basically, fills with RELATIONSRELATIONSRELATIONS
		for(int i =0, j = 0; i < length ; ++i, ++j) {
			
			if(j == key.length()) {
				j = 0;
			}
			kr[i] = key.charAt(j);
		}
		
		//Using the array values, use the encryption equation (Ci = (Pi + Ki) modulus 26)
		//Also note; A is added onto the end so we don't end up with a value of 0
		for(int i = 0; i < length; ++i) {
			encrypted[i] = (char) (((pt[i] + kr[i]) % 26) + 'A');
		}
		
		//Turns the array into a single string that can be used
		String result = String.valueOf(encrypted);	 
		return result;
    
	}
 
	
	//Decryption
    public static String vigenere_decrypt(String text, String key) {
        
    	//Same arrays need to be made with the decrypt
    	char et[] = text.toCharArray();
    	int length = et.length;
    	char kr[] = new char[length];
    	char decrypted[] = new char[length];
    	
		for(int i =0, j = 0; i < length ; ++i, ++j) {
			
			if(j == key.length()) {
				j = 0;
			}
			kr[i] = key.charAt(j);
		}
    	
    	for(int i = 0; i < length; ++i) {
    		decrypted[i] = (char)((((et[i] - kr[i]) + 26) % 26) + 'A');	
    	}
     
    	//Turns the array into a single string that can be used
    	String result = String.valueOf(decrypted);	
    	return result;
    }
 
}