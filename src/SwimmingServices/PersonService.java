package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import mainApp.SwimmingGUI;

public class PersonService {
	public enum Sex {
		M,
		F
	};
	
	DatabaseConnectionService dbService;

	public PersonService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean addPerson(String fName, String lName, Sex sex, java.sql.Date dob) {
		
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_person(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, fName);
			stmt.setString(3, lName);
			stmt.setNString(4, sex == Sex.M ? "M" : "F");
			stmt.setDate(5, dob);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			} else if(returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is already on the team in the database.");
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}
	
	public boolean updatePerson(String fName, String lName, Sex sex, java.sql.Date dob) {
		//int id = identify(fName, lName);
		int id = SwimmingGUI.getID(fName, lName, dbService);
		Connection conn = this.dbService.getConnection();
		try {
			CallableStatement stmt = conn.prepareCall("{ ? = call update_person(?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.setString(3, fName);
			stmt.setString(4, lName);
			stmt.setNString(5, sex == Sex.M ? "M" : "F");
			stmt.setDate(6, dob);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			} else if(returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is already on the team in the database.");
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");
		}
		return false;
	}
	
	public boolean deletePerson(String fName, String lName)
	{
		int id = SwimmingGUI.getID(fName, lName, dbService);
		Connection conn = this.dbService.getConnection();
		try {
			CallableStatement stmt = conn.prepareCall("{ ? = call delete_person(?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			} else if(returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: The person is not in the database.");
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");
		}
		return false;
	}

}