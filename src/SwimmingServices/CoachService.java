package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import mainApp.SwimmingGUI;

public class CoachService {
	
	DatabaseConnectionService dbService;
	
	public CoachService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean isCoach(int id) {
		Connection conn = this.dbService.getConnection();
		
		CallableStatement locatestmt;
		try {
			locatestmt = conn.prepareCall("select ID from Coach where ID = ?");
			locatestmt.setInt(1, id);
			ResultSet rs = locatestmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");		
		}
		return false;
	}
	
	// TODO: eventually this should be implemented to properly allow sorting and filtering
	public boolean addCoach(String fName, String lName, int experience, String style) {
		PersonService tempsvc = new PersonService(dbService);
		int id = SwimmingGUI.getID(fName, lName, dbService);
		
		try {
			Connection conn = this.dbService.getConnection();
			
			CallableStatement stmt = conn.prepareCall("{ ? = call insert_coach(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.setInt(3, experience);
			stmt.setString(4, style);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			switch(returnValue) {
			case 0:
				return true;
			case 1:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was not selected. Please contact the developers.");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "ERROR: Experience cannot be null.");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was selected incorrectly. Please contact the developers.");
				break;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;	
	}	
		
	public boolean updateCoach(int CID, int experience, String style) {
		PersonService tempsvc = new PersonService(dbService);
		
		try {
			Connection conn = this.dbService.getConnection();
			
			CallableStatement stmt = conn.prepareCall("{ ? = call update_coach(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, CID);
			stmt.setInt(3, experience);
			stmt.setString(4, style);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			switch(returnValue) {
			case 0:
				return true;
			case 1:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was not selected. Please contact the developers.");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "ERROR: The Person does not exist.");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: This person is not a coach.");
				break;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}

}