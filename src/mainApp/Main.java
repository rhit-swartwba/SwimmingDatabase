package mainApp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import SwimmingServices.DatabaseConnectionService;
import SwimmingServices.UserService;

public class Main {

	
	public static String serverUsername = "beaslebf";
	public static String serverPassword = "r0seCart06-C09";
	public static String databaseName= "SID_TEST2";
	public static String serverName = "titan.csse.rose-hulman.edu";
	
	
	public static void main(String[] args) {
		
		//SwimmingInformationDatabaseBBB
		DatabaseConnectionService dcs = new DatabaseConnectionService(serverName, databaseName);
		try {
			dcs.connect(serverUsername, serverPassword);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UserService logs = new UserService(dcs);
		if(logs.useApplicationLogins())
		{
			logs.loginGUI();
		}		
	}
	
}
