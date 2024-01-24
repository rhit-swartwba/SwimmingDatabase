package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import SwimmingServices.PersonService.Sex;
import mainApp.Main;

public class AddUserService {

	private DatabaseConnectionService dbService = null;

	public AddUserService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean addUser(String firstName, String lastName, String sex, Date dob, int height, int weight,
			int experience, String style, String teamName, String groupName, int hoursPerWeek, String username,
			String saltAsString, String hashPass) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{? = call AddUser(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, firstName);
			stmt.setString(3, lastName);
			stmt.setString(4, sex);
			stmt.setDate(5, dob);
			stmt.setInt(6, height);
			stmt.setInt(7, weight);
			stmt.setInt(8, experience);
			stmt.setString(9, style);
			stmt.setString(10, teamName);
			stmt.setString(11, groupName);
			stmt.setInt(12, hoursPerWeek);
			stmt.setString(13, username);
			stmt.setString(14, saltAsString);
			stmt.setString(15, hashPass); 
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: Invalid Registration.");
			}
			else
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error: Invalid Registration.");	
			try {
				dbService.getConnection().close();
				dbService.connect(Main.serverUsername, Main.serverPassword);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return false;
	}
	

}
