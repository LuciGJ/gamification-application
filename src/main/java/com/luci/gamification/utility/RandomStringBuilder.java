package com.luci.gamification.utility;

import java.util.Random;

public class RandomStringBuilder {

	// class used to generate a random alphanumeric string
	// used to generate the tokens
	public static String buildRandomString(int size) {
		// create a string containing all the upper case and lower case letters and
		// digits
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;
		StringBuilder sb = new StringBuilder();
		Random random = new Random();

		// build the token by appending random characters from the alphanumeric string

		for (int i = 0; i < size; i++) {

			int index = random.nextInt(alphaNumeric.length());

			char randomChar = alphaNumeric.charAt(index);

			sb.append(randomChar);
		}

		String randomString = sb.toString();
		return randomString;
	}
}
