package src;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	public static String hash(String input) {
		byte[] bytes;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e) {
			System.err.println("Hashing failure: " + e);
			return null;
		}
		bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
		BigInteger number = new BigInteger(1, bytes);

		StringBuilder hexString = new StringBuilder(number.toString(16));
		while (hexString.length() < 64)
			hexString.insert(0, '0');

		return hexString.toString();
	}
}
