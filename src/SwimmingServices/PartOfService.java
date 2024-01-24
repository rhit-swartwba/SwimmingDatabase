package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class PartOfService {

	private DatabaseConnectionService dbService = null;
	
	public PartOfService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addPartOf(int PID, String teamName, String groupName, int hoursPerWeek) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_partof(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, PID);
			stmt.setString(3, teamName);
			stmt.setString(4, groupName);
			stmt.setInt(5, hoursPerWeek);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
				return false;
			}
			else if(returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is already on the team in the database.");
				return false;
			} else if(returnValue == 3) {
				JOptionPane.showMessageDialog(null, "ERROR: The specified team does not exist.");
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}
	
	public boolean removePartOf(int PID, String tname) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call delete_partof(?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, PID);
			stmt.setString(3, tname);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is not currently on the team in the database.");
			}
			else
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}
	
	public boolean updatePartOf(int personID, String groupName, int hoursPerWeek, int teamID) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call update_partof(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, personID);
			stmt.setString(3, groupName);
			stmt.setInt(4, hoursPerWeek);
			stmt.setInt(5, teamID);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is not on the team in the database.");
			}
			else
			{
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return false;
	}
}
