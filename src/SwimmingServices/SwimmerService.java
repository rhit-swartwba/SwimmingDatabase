package SwimmingServices;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import mainApp.SwimmingGUI;

public class SwimmerService {
	
	DatabaseConnectionService dbService;
	
	public SwimmerService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}
	
	public boolean isSwimmer(int id) {
		Connection conn = this.dbService.getConnection();
		
		CallableStatement locatestmt;
		try {
			locatestmt = conn.prepareCall("select ID from Swimmer where ID = ?");
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
	public boolean addSwimmer(String fName, String lName, int height, int weight) {
		PersonService tempsvc = new PersonService(dbService);
		//int id = tempsvc.identify(fName, lName);
		int id = SwimmingGUI.getID(fName, lName, dbService);
		
		try {
			Connection conn = this.dbService.getConnection();
			
			CallableStatement stmt = conn.prepareCall("{ ? = call insert_swimmer(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.setInt(3, height);
			stmt.setInt(4, weight);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			switch(returnValue) {
			case 0:
				return true;
			case 1:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was not selected. Please contact the developers.");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was selected incorrectly. Please contact the developers.");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}
	
	public boolean updateSwimmer(int id, int height, int weight) {
		try {
			Connection conn = this.dbService.getConnection();
			CallableStatement stmt = conn.prepareCall("{ ? = call update_swimmer(?, ?, ?) }");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, id);
			stmt.setInt(3, height);
			stmt.setInt(4, weight);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			switch(returnValue) {
			case 0:
				return true;
			case 1:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was not selected. Please contact the developers.");
				break;
			case 2:
				JOptionPane.showMessageDialog(null, "INTERNAL ERROR: ID was selected incorrectly. Please contact the developers.");
				break;
			case 3:
				JOptionPane.showMessageDialog(null, "ERROR: Specified person is not a swimmer.");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");	
		}
		return false;
	}

}