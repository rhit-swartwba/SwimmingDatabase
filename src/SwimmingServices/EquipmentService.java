package SwimmingServices;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class EquipmentService {

	private DatabaseConnectionService dbService = null;
	
	public EquipmentService(DatabaseConnectionService dbService) {
		this.dbService = dbService;
	}

	public boolean addEquipment(String eModel, String eBrand, String eType) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call insert_equipment(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, eModel);
			stmt.setString(3, eBrand);
			stmt.setString(4, eType);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given equipment already exists in the database.");
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
	
	public boolean removeEquipment(String model) {
		try {

			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call delete_equipment(?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, model);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The equipment given does not exist in the database.");
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

	
	public boolean updateEquipment(String eModel, String eBrand, String eType) {
		try {
			CallableStatement stmt = this.dbService.getConnection().prepareCall("{ ? = call update_equipment(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, eModel);
			stmt.setString(3, eBrand);
			stmt.setString(4, eType);
			stmt.execute();
			int returnValue = stmt.getInt(1);
			if(returnValue == 1)
			{
				JOptionPane.showMessageDialog(null, "ERROR: None of the given fields can be null or empty.");
			}
			else if(returnValue == 2)
			{
				JOptionPane.showMessageDialog(null, "ERROR: The given equipment does not exist in the database.");
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
	
	public ArrayList<String> getEquipment() {
		String query = "SELECT Model FROM Equipment";
		try {
			Statement stmt = dbService.getConnection().createStatement();
			ResultSet results = stmt.executeQuery(query);
			ArrayList<String> equip = new ArrayList<String>();
			while(results.next())
			{
				equip.add(results.getString("name"));
			}
			return equip;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
