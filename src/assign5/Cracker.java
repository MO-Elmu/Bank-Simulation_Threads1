package assign5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;


public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	private byte[] hashToCrack;
	
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	public static String generateHash(String passwd) {
		String hashValue;
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
			byte[] hash = mDigest.digest(passwd.getBytes());
			hashValue = Cracker.hexToString(hash);
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			hashValue = "";
		}
		return hashValue;
	}
	
	public static void main(String[] args) {
		// Mode 1 Generation Mode
		if(args.length == 1) {
			String hashVal = Cracker.generateHash(args[0]);
			System.out.println(hashVal);
		}
		
		
		// Mode 2 Cracking Mode
		else if(args.length == 3) {
			Cracker cracker = new Cracker();
			int numOfWorkers = Integer.valueOf(args[2]);
			if(numOfWorkers > CHARS.length) {
				System.err.println("ERROR : Too many Threads \n");
				System.out.println(CHARS.length + " Threads maximum");
				return;
			}
			int passwdLength = Integer.valueOf(args[1]);
			int step = CHARS.length/numOfWorkers;
			int stepMOD = CHARS.length%numOfWorkers;
			cracker.hashToCrack = Cracker.hexToArray(args[0]);
			CountDownLatch latch = new CountDownLatch(numOfWorkers);
			for(int i=0; i<numOfWorkers; i++) {
				int strtIndx = i*step;
				int endIndx = strtIndx+step-1;
				if(i == (numOfWorkers-1)) endIndx += stepMOD;
				Worker w = cracker.new Worker(strtIndx, endIndx, passwdLength, latch);
				w.start();
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
			System.out.println("all done");
		}
		
		// Wrong Input
		else {
			System.err.println("ERROR : Wrong number of parameters \n");
			System.out.println("1 or 3 arguments required");
			System.out.println("Quitting ......" );
			return;
		}
		
	}
	
	private class Worker extends Thread{
		private final int startIndx, endIndx, passwdLen;
		private final CountDownLatch latch;
		public Worker(int startIndx, int endIndx, int passwdLen, CountDownLatch latch) {
			this.startIndx = startIndx;
			this.endIndx = endIndx;
			this.passwdLen = passwdLen;
			this.latch = latch;
		}
		
		public void run() {
			for(int i=startIndx; i<endIndx+1; i++) {
				String passwd = String.valueOf(CHARS[i]);
				checkPassword(passwd);
				checkPermutation (passwdLen-1, passwd);
			}
			
			latch.countDown();
		}
		
	}
	
	private void checkPassword(String passwd) {
		String hashVal = generateHash(passwd);
		byte[] hash = hexToArray(hashVal);
		if(Arrays.equals(hash, hashToCrack)) {
			System.out.println(passwd);
		}
		
	}
	
	private void checkPermutation (int passwdLen, String passwd) {
		if(passwdLen <1) return;
		for(int j=0; j<CHARS.length; j++) {
			String newPass = passwd;
			newPass += CHARS[j];
			checkPassword(newPass);
			checkPermutation(passwdLen-1, newPass);
		}
	}
	
	// possible test values:
	// a ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb
	// fm 440f3041c89adee0f2ad780704bcc0efae1bdb30f8d77dc455a2f6c823b87ca0
	// a! 242ed53862c43c5be5f2c5213586d50724138dea7ae1d8760752c91f315dcd31
	// xyz 3608bca1e44ea6c4d268eb6db02260269892c0b42b86bbf1e77a6fa16c3c9282

}
