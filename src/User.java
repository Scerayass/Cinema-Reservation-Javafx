import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;

public class User implements userInterface {
	String userName;
	private String password;
	public String hashPassword;
	boolean admin = false;
	boolean clubMember = false;
	LinkedHashMap<String, Double> prices =  new LinkedHashMap<>();
	
	
	public User(String userName,String password,boolean clubMember , boolean admin) {
		this.userName =  userName;
		
		this.hashPassword = password;
		if(password.endsWith("==")) {
			this.password = password;
		}
		else {
			this.password = hashPassword(password);
		}
		
		this.admin = admin;
		this.clubMember = clubMember;
		
	}
	String getPassword() {
		return password;
	}
	public String getUserName() {
		return userName;
	}
	public String getAdmin() {
		if (admin == false) {
			return "false";
		}
		return "true";
	}
	public String getClubMember() {
		if(clubMember == false) {
			return "false";
		}
		return "true";
	}
	
	static String hashPassword(String password) {
		byte[] bytesofPassword =  password.getBytes(StandardCharsets.UTF_8);
		byte[] md5Digest =  new byte[0];
		try {
			md5Digest =  MessageDigest.getInstance("MD5").digest(bytesofPassword);
		} catch (NoSuchAlgorithmException e) {
			return null;
			// TODO: handle exception
		}
		return Base64.getEncoder().encodeToString(md5Digest);
	}
	
	
}
