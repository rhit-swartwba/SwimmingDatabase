package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TimeStandardService {

	private DatabaseConnectionService dbService = null;
	
	public TimeStandardService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addTimeStandard(int distance, String stroke, String unit, double maleTime, double femaleTime, String level) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_TimeStandard(?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, distance);
			stmt.setString(3, stroke);
			stmt.setString(4, unit);
			stmt.setDouble(5, maleTime);
			stmt.setDouble(6, femaleTime);
			stmt.setString(7, level);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The Time Standard is already in the database.");
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
	
	public boolean updateTimeStandard(int distance, String stroke, String unit, double maleTime, double femaleTime, String level) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call update_TimeStandard(?,?,?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, distance);
			stmt.setString(3, stroke);
			stmt.setString(4, unit);
			stmt.setDouble(5, maleTime);
			stmt.setDouble(6, femaleTime);
			stmt.setString(7, level);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The Time Standard is already in the database.");
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
	
	public boolean deleteTimeStandard(int distance, String stroke, String unit, String level) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call delete_TimeStandard(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, distance);
			stmt.setString(3, unit);
			stmt.setString(4, stroke);
			stmt.setString(5, level);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The Time Standard is already in the database.");
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
	
	public ResultSet getTimeStandardInfo(String level, int distance, String stroke, String unit) {
		ResultSet rs = null;
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call getTimeStandardInfo(?,?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, distance);
			stmt.setString(3, unit);
			stmt.setString(4, stroke);
			stmt.setString(5, level);
			rs = stmt.executeQuery();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The Time Standard is already in the database.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect and call to the database.");			
		}
		return rs;
	}
}

