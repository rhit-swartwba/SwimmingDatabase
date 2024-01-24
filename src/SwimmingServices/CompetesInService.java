package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class CompetesInService {

	private DatabaseConnectionService dbService = null;
	
	public CompetesInService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addCompetesIn(int PID, String model, int distance, String stroke, String unit, double time) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_CompetesIn(?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, PID);
			stmt.setString(3, model);
			stmt.setInt(4, distance);
			stmt.setString(5, stroke);
			stmt.setString(6, unit);
			stmt.setDouble(7, time);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
				return false;
			} else if(returnValue == 2) {
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer already has a time for this event.");
				return false;
			} else if(returnValue == 3) {
				JOptionPane.showMessageDialog(null, "ERROR: No such event exists.");
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
	
	public boolean updateCompetesIn(int ID, String model, int distance, String stroke, String unit,
			double time) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call update_CompetesIn(?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, ID);
			stmt.setInt(3, distance);
			stmt.setString(4, stroke);
			stmt.setString(5, unit);
			stmt.setDouble(6, time);
			stmt.setString(7, model);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer already has a time for this event.");
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
	
	public boolean deleteCompetesIn(int ID, int distance, String stroke, String unit) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call delete_CompetesIn(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, ID);
			stmt.setInt(3, distance);
			stmt.setString(4, unit);
			stmt.setString(5, stroke);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The swimmer is not on the database.");
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
