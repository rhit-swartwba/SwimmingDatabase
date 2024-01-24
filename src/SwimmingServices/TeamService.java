package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class TeamService {

	private DatabaseConnectionService dbService = null;
	
	public TeamService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addTeam(String teamName, String teamRegion) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_team(?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, teamName);
			stmt.setString(3, teamRegion);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given team already exists in the database.");
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
	
	public boolean removeTeam(String teamName) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call delete_team(?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, teamName);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given team does not exist in the database.");
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
	
	public boolean updateTeam(String teamName, String teamRegion) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call update_team(?, ?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, teamName);
			stmt.setString(3, teamRegion);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given team does not exist in the database.");
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
	
	public ArrayList<String> getTeam() {
		String query = "SELECT TeamName FROM Team";
		try {
			Statement stmt = dbService.getConnection().createStatement();
			ResultSet results = stmt.executeQuery(query);
			ArrayList<String> team = new ArrayList<String>();
			while(results.next())
			{
				team.add(results.getString("TeamName"));
			}
			return team;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
