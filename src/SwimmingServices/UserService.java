package SwimmingServices;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.JOptionPane;

public class UserService {
	private static final Random RANDOM = new SecureRandom();
	private static final Base64.Encoder enc = Base64.getEncoder();
	private static final Base64.Decoder dec = Base64.getDecoder();
	private DatabaseConnectionService dbService = null;

	public UserService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public void loginGUI() {
		new LoginGUI(this);
	}

	public boolean useApplicationLogins() {
		return true;
	}
	
	public DatabaseConnectionService getConnect()
	{
		return this.dbService;
	}
	
	public boolean login(String username, String password) {
		String query = "SELECT PasswordSalt, PasswordHash \n FROM [User] \n WHERE username = ?";
		try {
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			String correctPassSalt = "";
			String correctPassHash = "";
			while (rs.next()) 
			{
					correctPassSalt = rs.getString("PasswordSalt");
					correctPassHash = rs.getString("PasswordHash");
			 }
			byte[] decodedBytes = dec.decode(correctPassSalt);
			String userPassHash = hashPassword(decodedBytes, password);
			if(userPassHash.equals(correctPassHash))
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Login Failed.");
		return false;

	}
	


	public boolean register(String username, String password, int PID) {
		try {
			byte[] salt = getNewSalt();
			String saltAsString = getStringFromBytes(salt);
			String hashPass = hashPassword(salt, password);
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call Register(?,?,?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, username);
			stmt.setString(3, saltAsString);
			stmt.setString(4, hashPass);
			stmt.setInt(5, PID);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "Username cannot be null or empty.");
			}
			else if(returnValue == 4)
			{
				JOptionPane.showMessageDialog(null, "ERROR: Username already exists.");
			}
			else if(returnValue == 3)
			{
				JOptionPane.showMessageDialog(null, "PasswordHash cannot be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "PasswordSalt cannot be null or empty.");
			}
			else
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Registration failed.");			
		return false;
	}
	
	public byte[] getNewSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	public String getStringFromBytes(byte[] data) {
		return enc.encodeToString(data);
	}

	public String hashPassword(byte[] salt, String password) {

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory f;
		byte[] hash = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			JOptionPane.showMessageDialog(null, "An error occurred during password hashing. See stack trace.");
			e.printStackTrace();
		}
		return getStringFromBytes(hash);
	}

	public int getPID(String username) {
		String query = "SELECT PID \n FROM [User] \n WHERE username = ?";
		try {
			PreparedStatement stmt = this.dbService.getConnection().prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			int PID = 0;
			while (rs.next()) 
			{
					PID = Integer.parseInt(rs.getString("PID"));
			 }
			return PID;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

}

