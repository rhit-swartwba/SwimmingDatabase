/**
 * 
 */
package DataImport;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import java.sql.Types;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import SwimmingServices.CoachService;
import SwimmingServices.CompetesInService;
import SwimmingServices.EquipmentService;
import SwimmingServices.EventService;
import SwimmingServices.PartOfService;
import SwimmingServices.PersonService;
import SwimmingServices.PersonService.Sex;
import SwimmingServices.SwimmerService;
import SwimmingServices.TeamService;
import SwimmingServices.TimeStandardService;
import SwimmingServices.UserService;
import mainApp.DataTypeConverter;
import mainApp.Main;
import mainApp.SwimmingGUI;
import SwimmingServices.DatabaseConnectionService;

/**
 * @author beaslebf
 *
 */


public class MainDataImport<T> {
	
	
	//CONNECTION

	

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws SQLServerException   
	{  
		
		DatabaseConnectionService dcs = new DatabaseConnectionService(Main.serverName, Main.databaseName);
		dcs.connect(Main.serverUsername, Main.serverPassword);
		DataTypeConverter DTC = new DataTypeConverter();


		//

		//OBJECTS
		EquipmentService addEquipment = new EquipmentService(dcs);
		TeamService addTeam = new TeamService(dcs);
		PartOfService addPartOf = new PartOfService(dcs);
		EventService addEvent = new EventService(dcs);
		TimeStandardService addTimeStandard = new TimeStandardService(dcs);
		CompetesInService addCompetesIn = new CompetesInService(dcs);
		PersonService addPerson = new PersonService(dcs);
		SwimmerService addSwimmer = new SwimmerService(dcs);
		CoachService addCoach = new CoachService(dcs);
		UserService addUser = new UserService(dcs);
		//
		
		ArrayList<String> Logins = new ArrayList<String>();
		Collections.addAll(Logins, "Username", "Password", "FirstName", "LastName");
		ArrayList<String> Person = new ArrayList<String>();
		Collections.addAll(Person, "FirstName", "LastName", "Sex", "DOB");
		ArrayList<String> Swimmer = new ArrayList<String>();
		Collections.addAll(Swimmer, "FirstName", "LastName", "Height", "Weight");
		ArrayList<String> Coach = new ArrayList<String>();
		Collections.addAll(Coach, "FirstName", "LastName", "Experience", "Style");
		ArrayList<String> PartOf = new ArrayList<String>();
		Collections.addAll(PartOf, "FirstName", "LastName", "TeamName", "Group", "Hours");
		ArrayList<String> Team = new ArrayList<String>();
		Collections.addAll(Team, "TeamName", "Region");
		ArrayList<String> Equipment = new ArrayList<String>();
		Collections.addAll(Equipment, "Model", "Brand", "Type");
		ArrayList<String> TimeStandard = new ArrayList<String>();
		Collections.addAll(TimeStandard, "CutLevel", "CutDistance", "CutStroke", "CutUnit", "MaleCutTime", "FemaleCutTime");
		ArrayList<String> Event = new ArrayList<String>();
		Collections.addAll(Event, "Distance", "Stroke", "Unit");
		ArrayList<String> CompetesIn = new ArrayList<String>();
		Collections.addAll(CompetesIn, "FirstName", "LastName", "Model", "Distance", "Stroke", "Unit", "Time");
		ArrayList<ArrayList> allTables = new ArrayList<ArrayList>();
		Collections.addAll(allTables, Person, Logins, Swimmer, Coach, Team, PartOf, Event, Equipment, TimeStandard,
				 CompetesIn);
		ArrayList<String> allTablesStrings = new ArrayList<String>();
		Collections.addAll(allTablesStrings, "Person","Logins", "Swimmer", "Coach", "Team", "PartOf", 
				"Event", "Equipment", "TimeStandard","CompetesIn");
		
		
		try  
		{  
			File file = new File("C:\\Users\\beaslebf\\OneDrive_-_Rose-Hulman_Institute_of_Technology\\CSSE_333\\Swimming DB Project\\Swimming Database - Sample Information.xlsx");   //creating a new file instance 
			//File file = new File("/S3G3 - Swimming Information Database/src/DataImport/Swimming Database - Sample Information.xlsx");
			FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
			//creating Workbook instance that refers to .xlsx file  
			XSSFWorkbook wb = new XSSFWorkbook(fis);   
			XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
			Iterator<Row> itr = sheet.iterator();    //iterating over excel file 
			HashMap<String, Integer> attributes = new HashMap<String, Integer>();
				
			Row attribute = itr.next();
			if(attribute.getRowNum() == 0) {
				Iterator<Cell> attributeIterator = attribute.cellIterator();
				while (attributeIterator.hasNext())   
				{  
					Cell cell = attributeIterator.next(); 
					if(!attributes.containsKey(cell.getStringCellValue()) &&
							cell.getStringCellValue() != null && !cell.getStringCellValue().equals("")) {
						attributes.put(cell.getStringCellValue(), cell.getColumnIndex());
					}
				} 
			}
			for(int i = 0; i < allTables.size(); i++) {
					Iterator<Row> attributeIterator = sheet.iterator();
					attributeIterator.next();
					ArrayList<String> table = allTables.get(i);
					ArrayList<Integer> columnIndicies = new ArrayList<Integer>();
					for(String elem : table) {
						columnIndicies.add(attributes.get(elem));
					}
					Collections.sort(columnIndicies);
					Set<List> allTuples = new HashSet<List>();
					
					while (attributeIterator.hasNext())                 
					{  
						List<Object> tuple = new ArrayList<>();
						Row row = attributeIterator.next();
						Iterator<Cell> cellIterator = row.cellIterator();
						int k = 0;
						while (cellIterator.hasNext())   
						{  
							Cell cell = cellIterator.next();  
							if(k >= columnIndicies.size() || k < 0) {
								if(!tuple.isEmpty()) {
									allTuples.add(tuple);
								}
								//tuple.clear();
								break;
							}
							if(cell.getColumnIndex() == columnIndicies.get(k)) {
								
								switch(cell.getCellType()) {
								
								case Cell.CELL_TYPE_NUMERIC : 
					                	tuple.add(cell.getNumericCellValue());
					                    break;
					                
								case Cell.CELL_TYPE_STRING : 
					                	tuple.add(cell.getStringCellValue());
					                    break;
					                 
								case Cell.CELL_TYPE_BLANK : 
					                	tuple.clear();
				                		k = -2;
								}
							k++;
							}
						}
					}
					
					//Insert tuples into tables//
					String tableName = allTablesStrings.get(i);
					if(tableName.equals("Person")) {
						for(List elem : allTuples) {
							addPerson.addPerson(elem.get(0).toString(), elem.get(1).toString(), 
									Sex.valueOf(elem.get(2).toString()),java.sql.Date.valueOf(elem.get(3).toString()));
						}
						
					}
					else if(tableName.equals("Logins")) {
						for(List elem : allTuples) {
							if(elem.get(0).toString().equals("Admin")) {
								addUser.register(elem.get(0).toString(),elem.get(1).toString(), -1);
							}else {
								addUser.register(elem.get(0).toString(),elem.get(1).toString(), SwimmingGUI.getID(elem.get(2).toString(), elem.get(3).toString(), dcs));
							}
						}
					}
					else if(tableName.equals("Swimmer")) {
						for(List elem : allTuples) {
							addSwimmer.addSwimmer(elem.get(0).toString(), elem.get(1).toString(), 
									Integer.parseInt(elem.get(2).toString()), Integer.parseInt(elem.get(3).toString()));
						}
					}
					else if(tableName.equals("Coach")) {
						for(List elem : allTuples) {
							addCoach.addCoach(elem.get(0).toString(), elem.get(1).toString(), 
									(int)Double.parseDouble(elem.get(2).toString()), elem.get(3).toString());
						}
					}
					else if(tableName.equals("Team")) {
						for(List elem : allTuples) {
							addTeam.addTeam(elem.get(0).toString(), elem.get(1).toString());
						}
						
					}
					else if(tableName.equals("PartOf")) {
						for(List elem : allTuples) {
							String fName = elem.get(0).toString();
							String lName = elem.get(1).toString();
							int id = SwimmingGUI.getID(fName, lName, dcs);
							addPartOf.addPartOf(id, elem.get(2).toString(),
									elem.get(3).toString(), (int)Double.parseDouble(elem.get(4).toString()));
						}
					}
					else if(tableName.equals("Equipment")) {
						for(List elem : allTuples) {
							addEquipment.addEquipment(elem.get(0).toString(), elem.get(1).toString(), 
								elem.get(2).toString());
						}
						
					}
					else if(tableName.equals("Event")) {
						//insert into Event
						for(List elem : allTuples) {
							addEvent.addEvent((int)Double.parseDouble(elem.get(0).toString()), elem.get(1).toString(), 
									elem.get(2).toString());
						}
					}
					else if(tableName.equals("TimeStandard")) {
						//insert into TimeStandard
						for(List elem : allTuples) {
							addTimeStandard.addTimeStandard((int)Double.parseDouble(elem.get(1).toString()), elem.get(2).toString(), 
									elem.get(3).toString(), DTC.stringToDecimal(elem.get(4).toString()), 
									DTC.stringToDecimal(elem.get(5).toString()), elem.get(0).toString());
						}
					}		
					else if(tableName.equals("CompetesIn")) {
						//insert into CompetesIn
						for(List elem : allTuples) {
							String fName = elem.get(0).toString();
							String lName = elem.get(1).toString();
							int id = SwimmingGUI.getID(fName, lName, dcs);
							addCompetesIn.addCompetesIn(id, 
									elem.get(6).toString(), (int) Double.parseDouble(elem.get(2).toString()), elem.get(3).toString(),
									elem.get(4).toString(), DTC.stringToDecimal(elem.get(5).toString()));
						}
					}
					
					columnIndicies.clear();
					allTuples.clear();
			}
		}  
		catch(Exception e)  
		{  
			e.printStackTrace();  
		}
		System.out.println("completed the import");
		
		
	//AT THE VERY END
	dcs.closeConnection();
	
	}
}