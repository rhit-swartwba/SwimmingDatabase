package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class EventService {

	private DatabaseConnectionService dbService = null;
	
	public EventService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addEvent(int distance, String stroke, String unit) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_event(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setInt(2, distance);
			stmt.setString(3, stroke);
			stmt.setString(4, unit);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given event already exists in the database.");
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
	
//	public ArrayList<String> getEvent() {
//		String query = "SELECT Model FROM Event";
//		try {
//			Statement stmt = dbService.getConnection().createStatement();
//			ResultSet results = stmt.executeQuery(query);
//			ArrayList<String> event = new ArrayList<String>();
//			while(results.next())
//			{
//				event.add(results.getString("name"));
//			}
//			return event;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
}
