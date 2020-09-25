package com.karmaya.fulvila.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Random;


@Component
public class Util {
	
	public char[] generateOTP(int len) 
    { 
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] otp = new char[len]; 
  
        for (int i = 0; i < len; i++)
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            otp[i] = 
            numbers.charAt(rndm_method.nextInt(numbers.length())); 
        }
        return otp; 
    }

    public String generateOTP_(int len) {
        String  rndnumber="";
        Random rnd= new Random();
        for (int i = 0; i < len; i++) {
            rndnumber = rndnumber + String.valueOf(rnd.nextInt(9));
        }
        return rndnumber;
    }
	
	public Timestamp generateDateAndTime() {
	    // print the date to the console
	    return new Timestamp(System.currentTimeMillis());
	}

	public boolean isTokenValid(String tokenGenerationTime) {
        long differenceTime = generateDateAndTime().getTime() - Timestamp.valueOf( tokenGenerationTime ).getTime();
        if(differenceTime > 120000) {
            return false;
        }
        return true;
    }
}
