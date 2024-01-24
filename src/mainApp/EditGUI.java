package mainApp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import SwimmingServices.CoachService;
import SwimmingServices.CompetesInService;
import SwimmingServices.EquipmentService;
import SwimmingServices.PartOfService;
import SwimmingServices.PersonService;
import SwimmingServices.TeamService;
import SwimmingServices.TimeStandardService;
import SwimmingServices.PersonService.Sex;
import SwimmingServices.SwimmerService;
import SwimmingServices.DatabaseConnectionService;

public class EditGUI {
	
	private final int EDIT_FRAME_WIDTH = 1500;
	private final int EDIT_FRAME_HEIGHT = 400;
	private JFrame frame = new JFrame();
	JPanel buttonPanel1 = new JPanel();
	JPanel buttonPanel2 = new JPanel();
	JPanel buttonPanel3 = new JPanel();
	JPanel buttonPanel4 = new JPanel();
	JPanel buttonPanel5 = new JPanel();
	JPanel buttonPanel6 = new JPanel();
	private int PID;
	private boolean isCoach;
	DatabaseConnectionService databaseCS = null;
	public DataTypeConverter DTC = new DataTypeConverter();

	public EditGUI(DatabaseConnectionService dcs, int PID, boolean isCoach)
	{
		this.databaseCS = dcs;
		this.PID = PID;
		System.out.println(PID);
		this.isCoach = isCoach;
		frame.setTitle("Edit Swimming Database");
		frame.setPreferredSize(new Dimension(EDIT_FRAME_WIDTH, EDIT_FRAME_HEIGHT));
		frame.pack();
		frame.setVisible(true);
		
		//EditType
		JPanel changePanel = new JPanel();
		changePanel.setBackground( Color.LIGHT_GRAY );
		
		JPanel editTypeDropDown = new JPanel();
		editTypeDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel editTypeDDLabel = new JLabel("EditType   ");
		editTypeDropDown.add(editTypeDDLabel);
		String[] editTypeOptions = { "Add", "Remove", "Update"};
		JComboBox<String> editTypeSelector = new JComboBox<String>(editTypeOptions);
		editTypeSelector.setMaximumSize(editTypeSelector.getPreferredSize());
		editTypeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		editTypeDropDown.add(editTypeSelector);
		editTypeDropDown.setMaximumSize(editTypeDropDown.getPreferredSize());
		
		//INSERT BUTTON
		JButton changeTypeButton = new JButton("Change Editing Type");
		changeTypeButton.addActionListener(new ChangeTypeActionListener(this, editTypeSelector));
		
		changePanel.add(editTypeDropDown);
		changePanel.add(changeTypeButton);
		frame.add(changePanel);
		
		displayInsert();
	}
	
	void displayInsert() {
		
		System.out.println("ADD");
		
		this.reset();
		frame.setTitle("ADD Into Swimming Database");

		
		buttonPanel1 = new JPanel();
		buttonPanel1.setBackground( Color.LIGHT_GRAY );

		
		//ADD TEAM
		buttonPanel2 = new JPanel();
		buttonPanel2.setBackground( Color.LIGHT_GRAY );
				
		JLabel insertTeam = new JLabel("Add Team:  ");

		//teamName TEXTBOX
		JLabel teamNameLabel = new JLabel("TeamName   ");
		JTextField teamNameField = new JTextField(10);
		teamNameField.setText("");
		
		//teamRegion TEXTBOX
		JLabel teamRegionLabel = new JLabel("TeamRegion   ");
		JTextField teamRegionField = new JTextField(10);
		teamRegionField.setText("");
		
		JButton insertTeamButton = new JButton("Add Team");
		insertTeamButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String teamName = teamNameField.getText();
				String teamRegion = teamRegionField.getText();
				TeamService team = new TeamService(databaseCS);
				team.addTeam(teamName, teamRegion);
				System.out.println(teamName);
				System.out.println(teamRegion);
			}
		});
		
		buttonPanel2.add(insertTeam);
		buttonPanel2.add(teamNameLabel);
		buttonPanel2.add(teamNameField);
		buttonPanel2.add(teamRegionLabel);
		buttonPanel2.add(teamRegionField);
		buttonPanel2.add(insertTeamButton);
		
		
		//ADD PARTOF
		buttonPanel3 = new JPanel();
		buttonPanel3.setBackground( Color.LIGHT_GRAY );
		
		JLabel insertPersonToTeam = new JLabel("Add Person to Team:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel2 = new JLabel("FirstName   ");
		JTextField firstNameField2 = new JTextField(10);
		firstNameField2.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel2 = new JLabel("LastName   ");
		JTextField lastNameField2 = new JTextField(10);
		lastNameField2.setText("");
		
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Team Name   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> cuts = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllTeamNames()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				cuts.add(rs.getString("TeamName"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = cuts.toArray(new String[cuts.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());
		
		JLabel groupLabel = new JLabel("Group   ");
		JTextField groupField = new JTextField(10);
		groupField.setText("");
		
		JLabel hoursLabel = new JLabel("Hours   ");
		JTextField hoursField = new JTextField(10);
		hoursField.setText("");
		
		JButton insertPartOf = new JButton("Add Person to Team");
		insertPartOf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Parse code
				String teamName = (String) eSelector.getSelectedItem();
				String groupName = groupField.getText();
				int hoursPerWeek;
				try {
					hoursPerWeek = Integer.parseInt(hoursField.getText());
				} catch(NumberFormatException doesntmatter) {
					JOptionPane.showMessageDialog(null, "Hours provided must be an integer.");
					return;
				}
				PartOfService aPO = new PartOfService(databaseCS);
				String firstName = firstNameField2.getText();
				String lastName = lastNameField2.getText();
				int pIDToUse = -1;
				if(PID == -1) {
					pIDToUse = SwimmingGUI.getID(firstName, lastName, databaseCS);
				}else {
					pIDToUse = PID;
				}
				
				
				if(teamName.isEmpty() || groupName.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Team and group must be provided.");
					return;
				}

				aPO.addPartOf(pIDToUse, teamName, groupName, hoursPerWeek);
			}
		});
		

		buttonPanel3.add(insertPersonToTeam);
		buttonPanel3.add(firstNameLabel2);
		buttonPanel3.add(firstNameField2);
		buttonPanel3.add(lastNameLabel2);
		buttonPanel3.add(lastNameField2);
		buttonPanel3.add(eDropDown);
		buttonPanel3.add(groupLabel);
		buttonPanel3.add(groupField);
		buttonPanel3.add(hoursLabel);
		buttonPanel3.add(hoursField);
		buttonPanel3.add(insertPartOf);
		
		if(PID != -1)
		{
			firstNameLabel2.setVisible(false);
			firstNameField2.setVisible(false);
			lastNameLabel2.setVisible(false);
			lastNameField2.setVisible(false);
		}
		// ADD Equipment
		buttonPanel4 = new JPanel();
		buttonPanel4.setBackground( Color.LIGHT_GRAY );
		
		JLabel insertEquipment = new JLabel("Add Equipment:  ");

		//EquipmentModel 
		JLabel equipModelLabel = new JLabel("Equipment Model   ");
		JTextField equipModelField = new JTextField(10);
		equipModelField.setText("");
		
		//EquipmentBrand
		JLabel equipBrandLabel = new JLabel("Equipment Brand   ");
		JTextField equipBrandField = new JTextField(10);
		equipBrandField.setText("");
		
		//EquipmentBrand
		JLabel equipTypeLabel = new JLabel("Equipment Type   ");
		JTextField equipTypeField = new JTextField(10);
		equipTypeField.setText("");
		
		JButton insertEquip = new JButton("Add Equipment");
		insertEquip.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String eModel = equipModelField.getText();
				String eBrand = equipBrandField.getText();
				String eType = equipTypeField.getText();
				//TEST TO CHECK CONNECTIVITY
				EquipmentService aes = new EquipmentService(databaseCS);
				aes.addEquipment(eModel, eBrand, eType);
				displayInsert();

				}
			}
		);
		
		buttonPanel4.add(insertEquipment);
		buttonPanel4.add(equipModelLabel);
		buttonPanel4.add(equipModelField);
		buttonPanel4.add(equipBrandLabel);
		buttonPanel4.add(equipBrandField);
		buttonPanel4.add(equipTypeLabel);
		buttonPanel4.add(equipTypeField);
		buttonPanel4.add(insertEquip);
		

		buttonPanel5 = new JPanel();
		buttonPanel5.setBackground( Color.LIGHT_GRAY );
	
		
		JLabel competesIn = new JLabel("Add Best Race:  ");

		//FName 
		JLabel fNameLabel = new JLabel("FirstName   ");
		JTextField fNameField = new JTextField(10);
		fNameField.setText("");
		
		//LName 
		JLabel lNameLabel = new JLabel("LastName   ");
		JTextField lNameField = new JTextField(10);
		lNameField.setText("");
		
		JPanel eDropDown2 = new JPanel();
		eDropDown2.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel2 = new JLabel("Equipment   ");
		eDropDown2.add(eDDLabel2);
		ArrayList<String> events = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllSuitModels()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				events.add(rs.getString("Model"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions2 = events.toArray(new String[events.size()]);
		JComboBox<String> eSelector2 = new JComboBox<String>(eOptions2);
		eSelector2.setMaximumSize(eSelector2.getPreferredSize());
		eSelector2.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown2.add(eSelector2);
		eDropDown2.setMaximumSize(eDropDown2.getPreferredSize());
		
		
		//Distance
		JPanel distanceDropDown = new JPanel();
		distanceDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel distanceDDLabel = new JLabel("Event Distance   ");
		distanceDropDown.add(distanceDDLabel);
		Integer[] distanceOptions = {50, 100, 200, 400};
		JComboBox<Integer> distanceSelector = new JComboBox<Integer>(distanceOptions);
		distanceSelector.setMaximumSize(distanceSelector.getPreferredSize());
		distanceSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		distanceDropDown.add(distanceSelector);
		distanceDropDown.setMaximumSize(distanceDropDown.getPreferredSize());

		//STROKE DROPBOX
		JPanel strokeDropDown = new JPanel();
		strokeDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel strokeDDLabel = new JLabel("Stroke   ");
		strokeDropDown.add(strokeDDLabel);
		String[] strokeOptions = { "Butterfly", "Backstroke", "Breaststroke", "Freestyle", "IM"};
		JComboBox<String> strokeSelector = new JComboBox<String>(strokeOptions);
		strokeSelector.setMaximumSize(strokeSelector.getPreferredSize());
		strokeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		strokeDropDown.add(strokeSelector);
		strokeDropDown.setMaximumSize(strokeDropDown.getPreferredSize());
		
		//Units DROPBOX
		JPanel unitDropDown = new JPanel();
		unitDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel unitDDLabel = new JLabel("Unit   ");
		unitDropDown.add(unitDDLabel);
		String[] unitOptions = { "SCY", "SCM", "LCM"};
		JComboBox<String> unitSelector = new JComboBox<String>(unitOptions);
		unitSelector.setMaximumSize(unitSelector.getPreferredSize());
		unitSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		unitDropDown.add(unitSelector);
		unitDropDown.setMaximumSize(unitDropDown.getPreferredSize());
		
		//Time
		JLabel timeLabel = new JLabel("Time   ");
		JTextField timeField = new JTextField(10);
		timeField.setText("");
		
		JButton competesInButton = new JButton("Add Best Race");
		competesInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String fName = fNameField.getText();
				String lName = lNameField.getText();
				String eModel = (String)eSelector2.getSelectedItem();
				int distance = (int)distanceSelector.getSelectedItem();
				String stroke = (String)strokeSelector.getSelectedItem();
				String unit = (String)unitSelector.getSelectedItem();
				String time = timeField.getText();
				
				int pIDToUse = PID;
				if(PID == -1 || isCoach)
				{
					pIDToUse = SwimmingGUI.getID(fName, lName, databaseCS);

				}
				CompetesInService aci = new CompetesInService(databaseCS);
				aci.addCompetesIn(pIDToUse, eModel, distance, stroke, unit, DTC.stringToDecimal(time));
				}


			}
		);
		
		buttonPanel5.add(competesIn);
		buttonPanel5.add(fNameLabel);
		buttonPanel5.add(fNameField);
		buttonPanel5.add(lNameLabel);
		buttonPanel5.add(lNameField);
		buttonPanel5.add(eDropDown2);
		buttonPanel5.add(distanceDropDown);
		buttonPanel5.add(strokeDropDown);
		buttonPanel5.add(unitDropDown);
		buttonPanel5.add(timeLabel);
		buttonPanel5.add(timeField);
		buttonPanel5.add(competesInButton);
		
		if(PID != -1 && !isCoach)
		{
			fNameLabel.setVisible(false);
			fNameField.setVisible(false);
			lNameLabel.setVisible(false);
			lNameField.setVisible(false);
		}

		if(PID != -1)
		{
			buttonPanel1.setVisible(false);
			buttonPanel2.setVisible(false);
			buttonPanel4.setVisible(false);
		}
		
		frame.add(buttonPanel2);
		frame.add(buttonPanel3);
		frame.add(buttonPanel4);
		frame.add(buttonPanel5);
		frame.pack();
		frame.setVisible(true);

		
	}
	


	void displayUpdate()
	{
		System.out.println("UPDATE");
		this.reset();		
		frame.setTitle("Update Swimming Database");
		
		buttonPanel1.removeAll();
		buttonPanel2.removeAll();
		buttonPanel3.removeAll();
		buttonPanel1.setBackground( Color.LIGHT_GRAY ); 
		

		JButton editCompetesInButton = new JButton("Update Best Time");
		JButton editPersonButton = new JButton("Update Person Info");
		JButton editPartOfButton = new JButton("Update Team Membership");
		JButton editTimeStandardButton = new JButton("Update Time Standard");
		
		editCompetesInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				updateCompetesIn();
				}
			}
		);
		
		editPersonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				if(PID == -1) {
					updatePerson();
				}else {
					buttonPanel3.removeAll();
					displayPersonToUpdate(PID);
				}
				
			}
		});
		
		editPartOfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				updatePartOf();
				}
			}
		);
		
		editTimeStandardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				updateTimeStandard();
				}
			}
		);
		
		
		buttonPanel1.add(editCompetesInButton);
		buttonPanel1.add(editPersonButton);
		buttonPanel1.add(editPartOfButton);
		buttonPanel1.add(editTimeStandardButton);
		//any other buttonPanel adds, put a copy of this in here
		if(PID != -1)
		{
			editTimeStandardButton.setVisible(false);
		}
		
		frame.add(buttonPanel1);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
	protected void updateTimeStandard() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		JLabel GetStandard = new JLabel("Search for Time Standard:  ");

		//level TEXTBOX
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Cut Level   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> cuts = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllCutLevels()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				cuts.add(rs.getString("Level"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = cuts.toArray(new String[cuts.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());
		//Distance
		JPanel distanceDropDown = new JPanel();
		distanceDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel distanceDDLabel = new JLabel("Event Distance   ");
		distanceDropDown.add(distanceDDLabel);
		Integer[] distanceOptions = { 50, 100, 200, 400};
		JComboBox<Integer> distanceSelector = new JComboBox<Integer>(distanceOptions);
		distanceSelector.setMaximumSize(distanceSelector.getPreferredSize());
		distanceSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		distanceDropDown.add(distanceSelector);
		distanceDropDown.setMaximumSize(distanceDropDown.getPreferredSize());

		//STROKE DROPBOX
		JPanel strokeDropDown = new JPanel();
		strokeDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel strokeDDLabel = new JLabel("Stroke   ");
		strokeDropDown.add(strokeDDLabel);
		String[] strokeOptions = { "Butterfly", "Backstroke", "Breaststroke", "Freestyle", "IM"};
		JComboBox<String> strokeSelector = new JComboBox<String>(strokeOptions);
		strokeSelector.setMaximumSize(strokeSelector.getPreferredSize());
		strokeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		strokeDropDown.add(strokeSelector);
		strokeDropDown.setMaximumSize(strokeDropDown.getPreferredSize());
						
		//Units DROPBOX
		JPanel unitDropDown = new JPanel();
		unitDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel unitDDLabel = new JLabel("Unit   ");
		unitDropDown.add(unitDDLabel);
		String[] unitOptions = { "SCY", "SCM", "LCM"};
		JComboBox<String> unitSelector = new JComboBox<String>(unitOptions);
		unitSelector.setMaximumSize(unitSelector.getPreferredSize());
		unitSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		unitDropDown.add(unitSelector);
		unitDropDown.setMaximumSize(unitDropDown.getPreferredSize());
						
				
		JButton updateTimeStandardButton = new JButton("Search For Time Standard");
		updateTimeStandardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String level = (String) eSelector.getSelectedItem();
				int distance = (int)distanceSelector.getSelectedItem();
				String stroke = (String)strokeSelector.getSelectedItem();
				String unit = (String)unitSelector.getSelectedItem();
						
				buttonPanel3.removeAll();
				displayTimeStandardToUpdate(level, distance, stroke, unit);

				}
			}
		);
				
		buttonPanel2.add(GetStandard);
		buttonPanel2.add(eDropDown);
		buttonPanel2.add(distanceDropDown);
		buttonPanel2.add(strokeDropDown);
		buttonPanel2.add(unitDropDown);
		buttonPanel2.add(updateTimeStandardButton);
		
				
		frame.add(buttonPanel2);
		frame.pack();
	}



	protected void displayTimeStandardToUpdate(String level, int distance, String stroke, String unit) {
		buttonPanel3.setBackground(Color.LIGHT_GRAY);
		Double oldMaleTime = null;
		Double oldFemaleTime = null;
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getTimeStandardInfo(?,?,?,?)\n";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, distance);
			stmt.setString(2, stroke);
			stmt.setString(3, unit);
			stmt.setString(4, level);
			
			ResultSet rs = stmt.executeQuery();
			int maleTimeIndex = rs.findColumn("MaleTime");
			int femaleTimeIndex = rs.findColumn("FemaleTime");
			rs.next();
			oldMaleTime = rs.getDouble(maleTimeIndex);
			oldFemaleTime = rs.getDouble(femaleTimeIndex);
			
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		
		String distanceString = ((Integer)distance).toString();
		JLabel selectedEvent = new JLabel(distanceString + " " + stroke + " " + unit);
		
		JLabel maleTimeLabel = new JLabel("Male New Time   ");
		JTextField maleTimeField = new JTextField(10);
		maleTimeField.setText(DTC.decimalToString(oldMaleTime));
		
		JLabel femaleTimeLabel = new JLabel("Female New Time   ");
		JTextField femaleTimeField = new JTextField(10);
		femaleTimeField.setText(DTC.decimalToString(oldFemaleTime));
		
		JButton UpdateTimeStandardButton = new JButton("Update");
		UpdateTimeStandardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String maletime = maleTimeField.getText();
				String femaletime = femaleTimeField.getText();
				TimeStandardService TSS = new TimeStandardService(databaseCS);
				TSS.updateTimeStandard(distance, stroke, unit, DTC.stringToDecimal(maletime), DTC.stringToDecimal(femaletime), level);

				}
			}
		);
		
		buttonPanel3.add(selectedEvent);
		buttonPanel3.add(maleTimeLabel);
		buttonPanel3.add(maleTimeField);
		buttonPanel3.add(femaleTimeLabel);
		buttonPanel3.add(femaleTimeField);
		buttonPanel3.add(UpdateTimeStandardButton);
	
		frame.add(buttonPanel3);
		frame.pack();
		
	}



	void updateCompetesIn() {
		reset();
		frame.add(buttonPanel1);
		// UPDATE SWIMMER
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		System.out.println(PID);
		JLabel GetPerson = new JLabel("Search for Time:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
				
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");
						
		//Distance
		JPanel distanceDropDown = new JPanel();
		distanceDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel distanceDDLabel = new JLabel("Event Distance   ");
		distanceDropDown.add(distanceDDLabel);
		Integer[] distanceOptions = { 50, 100, 200, 400};
		JComboBox<Integer> distanceSelector = new JComboBox<Integer>(distanceOptions);
		distanceSelector.setMaximumSize(distanceSelector.getPreferredSize());
		distanceSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		distanceDropDown.add(distanceSelector);
		distanceDropDown.setMaximumSize(distanceDropDown.getPreferredSize());

		//STROKE DROPBOX
		JPanel strokeDropDown = new JPanel();
		strokeDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel strokeDDLabel = new JLabel("Stroke   ");
		strokeDropDown.add(strokeDDLabel);
		String[] strokeOptions = { "Butterfly", "Backstroke", "Breaststroke", "Freestyle", "IM"};
		JComboBox<String> strokeSelector = new JComboBox<String>(strokeOptions);
		strokeSelector.setMaximumSize(strokeSelector.getPreferredSize());
		strokeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		strokeDropDown.add(strokeSelector);
		strokeDropDown.setMaximumSize(strokeDropDown.getPreferredSize());
						
		//Units DROPBOX
		JPanel unitDropDown = new JPanel();
		unitDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel unitDDLabel = new JLabel("Unit   ");
		unitDropDown.add(unitDDLabel);
		String[] unitOptions = { "SCY", "SCM", "LCM"};
		JComboBox<String> unitSelector = new JComboBox<String>(unitOptions);
		unitSelector.setMaximumSize(unitSelector.getPreferredSize());
		unitSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		unitDropDown.add(unitSelector);
		unitDropDown.setMaximumSize(unitDropDown.getPreferredSize());
						
				
		JButton updateCompetesInButton = new JButton("Search For Time");
		updateCompetesInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				int distance = (int)distanceSelector.getSelectedItem();
				String stroke = (String)strokeSelector.getSelectedItem();
				String unit = (String)unitSelector.getSelectedItem();
				int personID = PID;
				if(PID == -1 || isCoach)
				{
					personID = SwimmingGUI.getID(firstName, lastName, databaseCS);
				}
				buttonPanel3.removeAll();
				displaySwimmerTimesToUpdate(personID, distance, stroke, unit);

				}
			}
		);
				
		buttonPanel2.add(GetPerson);
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(distanceDropDown);
		buttonPanel2.add(strokeDropDown);
		buttonPanel2.add(unitDropDown);
		buttonPanel2.add(updateCompetesInButton);
		
		if(PID != -1 && !isCoach)
		{
			firstNameLabel.setVisible(false);
			firstNameField.setVisible(false);
			lastNameLabel.setVisible(false);
			lastNameField.setVisible(false);
		}
				
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	void updatePerson() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		// INSERT PERSON
		
		JLabel updatePerson = new JLabel("Update Person:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");

		//INSERT BUTTON
		JButton updatePersonButton = new JButton("Search");
		updatePersonButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				int personID = SwimmingGUI.getID(firstName, lastName, databaseCS);
				buttonPanel3.removeAll();
				displayPersonToUpdate(personID);

			}
		});
		
		buttonPanel2.add(updatePerson);
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(updatePersonButton);
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	
	void updatePartOf() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		
		// INSERT PERSON
		JLabel insertPerson = new JLabel("Update Team Membership:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");
		
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Team Name   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> cuts = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllTeamNames()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				cuts.add(rs.getString("TeamName"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = cuts.toArray(new String[cuts.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());

		//INSERT BUTTON
		JButton updatePartOfButton = new JButton("Update Team Membership");
		updatePartOfButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String teamName = (String) eSelector.getSelectedItem();
				int personID = PID;
				if(PID == -1)
				{
					personID = SwimmingGUI.getID(firstName, lastName, databaseCS);
				}
				buttonPanel3.removeAll();
				displayPartOfTeamToUpdate(personID, teamName);

			}
		});
		
		buttonPanel2.add(insertPerson);
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(eDropDown);
		buttonPanel2.add(updatePartOfButton);
		
		if(PID != -1)
		{
			firstNameLabel.setVisible(false);
			firstNameField.setVisible(false);
			lastNameLabel.setVisible(false);
			lastNameField.setVisible(false);
		}
	
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	void displaySwimmerTimesToUpdate(int personID, int distance, String stroke, String unit) {
		buttonPanel3.setBackground(Color.LIGHT_GRAY);
		Double oldTime = null;
		String oldModel = null;
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM get_swimmerTimes(?,?,?,?)\n";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, personID);
			stmt.setInt(2, distance);
			stmt.setString(3, stroke);
			stmt.setString(4, unit);
			
			ResultSet rs = stmt.executeQuery();
			int timeIndex = rs.findColumn("Time");
			int modelIndex = rs.findColumn("Equip");
			rs.next();
			oldTime = rs.getDouble(timeIndex);
			oldModel = rs.getString(modelIndex);
			
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		
		
		JLabel timeLabel = new JLabel("New Time   ");
		JTextField timeField = new JTextField(10);
		timeField.setText(DTC.decimalToString(oldTime));
		
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Equipment   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> events = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllSuitModels()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				events.add(rs.getString("Model"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = events.toArray(new String[events.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());
		eSelector.setSelectedItem(oldModel);
		
		JButton UpdateCompetesInButton = new JButton("Update");
		UpdateCompetesInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String time = timeField.getText();
				String model = (String) eSelector.getSelectedItem();
				CompetesInService UCI = new CompetesInService(databaseCS);
				UCI.updateCompetesIn(personID, model, distance, stroke, unit, DTC.stringToDecimal(time));
				}
			}
		);
		
		buttonPanel3.add(eDropDown);
		buttonPanel3.add(timeLabel);
		buttonPanel3.add(timeField);
		buttonPanel3.add(UpdateCompetesInButton);
	
		frame.add(buttonPanel3);
		frame.pack();
	}
	
	
	void displayPersonToUpdate(int id) {
		buttonPanel3.setBackground(Color.LIGHT_GRAY);
		
		PersonService ps = new PersonService(databaseCS);
		SwimmerService ss = new SwimmerService(databaseCS);
		CoachService cs = new CoachService(databaseCS);
		
		boolean isSwimmer = ss.isSwimmer(id);
		boolean isCoach = cs.isCoach(id);
		
		String oldStyle = null;
		Integer oldExperience = null;
		Integer oldHeight = null;
		Integer oldWeight = null;
		
		JTextField heightField = new JTextField(3);
		JTextField weightField = new JTextField(3);
		JTextField expField = new JTextField(3);
		JTextField styleField = new JTextField(10);
		
		
		if(isSwimmer) {
			try {
				Connection conn = databaseCS.getConnection();
				PreparedStatement swimstmt = conn.prepareStatement("select * from Swimmer where ID = ?");
				swimstmt.setInt(1, id);
				ResultSet swimresults = swimstmt.executeQuery();
				if(swimresults.next()) {
					oldHeight = swimresults.getInt("Height");
					oldWeight = swimresults.getInt("Weight");
				}
				
			}
			catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Error in reading information for this person");
				ex.printStackTrace();
				return;
			}
			
			
			//HEIGHT
			JLabel heightLabel = new JLabel("Height (in)   ");
			if(oldHeight != null) {
				if(oldHeight != 0) {
					heightField.setText(oldHeight.toString());
					buttonPanel3.add(heightLabel);
					buttonPanel3.add(heightField);
				}
			}
			
			//WEIGHT
			JLabel weightLabel = new JLabel("Weight   ");
			if(oldWeight != null) {
				if(oldWeight != 0) {
					weightField.setText(oldWeight.toString());
					buttonPanel3.add(weightLabel);
					buttonPanel3.add(weightField);
				}
			}
		}
		
		if(isCoach) {
			try {
				Connection conn = databaseCS.getConnection();
				PreparedStatement coachstmt = conn.prepareStatement("select * from Coach where ID = ?");
				coachstmt.setInt(1, id);
				ResultSet coachresults = coachstmt.executeQuery();
				coachresults.next();
				oldStyle = coachresults.getString("Style");
				oldExperience = coachresults.getInt("Experience");
			}
			catch(SQLException ex) {
				JOptionPane.showMessageDialog(null, "Error in reading information for this person");
				ex.printStackTrace();
				return;
			}
			
			//Experience
			JLabel expLabel = new JLabel("Experience   ");
			if(oldExperience != null) {
				expField.setText(oldExperience.toString());
				buttonPanel3.add(expLabel);
				buttonPanel3.add(expField);
				
			}
			
			//Style
			JLabel styleLabel = new JLabel("Style   ");
			if(oldStyle != null) {
				styleField.setText(oldStyle);
				buttonPanel3.add(styleLabel);
				buttonPanel3.add(styleField);
			}
		}
		
		JButton UpdatePersonButton = new JButton("Update");
		UpdatePersonButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// update person sproc
			
				if(isSwimmer) {
					int height = Integer.parseInt(heightField.getText());
					int weight = Integer.parseInt(weightField.getText());
					
					ss.updateSwimmer(id, height, weight);
				}
				if(isCoach) {
					int experience = Integer.parseInt(expField.getText());
					String style = styleField.getText();
					
					cs.updateCoach(id, experience, style);
				}
			}
		});
		
		buttonPanel3.add(UpdatePersonButton);
		
		frame.add(buttonPanel3);
		frame.pack();
	}
	
	Integer personID = null;Integer oldTeam = null;
	
	
	
	void displayPartOfTeamToUpdate(int perID, String teamName) {
		buttonPanel3.setBackground(Color.LIGHT_GRAY);
		String oldHours = null;
		String oldGroup = null;
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getPartOf(?,?)\n";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, perID);
			stmt.setString(2, teamName);
			
			ResultSet rs = stmt.executeQuery();
			
			int teamIDIndex = rs.findColumn("TeamID");
			int personIDIndex = rs.findColumn("PersonID");
			int groupIndex = rs.findColumn("Group");
			int hoursIndex = rs.findColumn("HoursPerWeek");
			rs.next();
			
			personID = rs.getInt(personIDIndex);
			oldTeam = rs.getInt(teamIDIndex);
			oldHours = rs.getString(hoursIndex);
			oldGroup = rs.getString(groupIndex);
			
			
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		
		JLabel groupLabel = new JLabel("New Group   ");
		JTextField groupField = new JTextField(10);
		groupField.setText(oldGroup);
		
		JLabel hoursLabel = new JLabel("New Hours   ");
		JTextField hoursField = new JTextField(10);
		hoursField.setText(oldHours);
		
		JButton UpdatePartOfButton = new JButton("Update");
		UpdatePartOfButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String group = groupField.getText();
				int hours = Integer.parseInt(hoursField.getText());
				PartOfService POS = new PartOfService(databaseCS);
				System.out.println(oldTeam.toString());
				POS.updatePartOf(perID, group, hours, oldTeam);

				}
			}
		);
		
		buttonPanel3.add(groupLabel);
		buttonPanel3.add(groupField);
		buttonPanel3.add(hoursLabel);
		buttonPanel3.add(hoursField);
		buttonPanel3.add(UpdatePartOfButton);
		
		frame.add(buttonPanel3);
		frame.pack();
	}
	
	void displayDelete()
	{
		System.out.println("DELETE");
		this.reset();
		frame.setTitle("Delete Swimming Database");
		
		buttonPanel1.removeAll();
		buttonPanel2.removeAll();
		buttonPanel3.removeAll();
		buttonPanel1.setBackground( Color.LIGHT_GRAY ); 
		

		JButton deletePersonButton = new JButton("Remove Person");
		JButton deleteCompetesInButton = new JButton("Remove Best Time");
		JButton deleteTeamButton = new JButton("Remove Team");
		JButton deleteEquipmentButton = new JButton("Remove Equipment");
		JButton deletePartOfButton = new JButton("Remove Person From Team");
		
		deletePersonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				deletePerson();
				}
			}
		);
		
		deleteCompetesInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				deleteCompetesIn();
				}
			}
		);

		
		deleteTeamButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				deleteTeam();
				}
			}
		);
		
		deleteEquipmentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				deleteEquipment();
				}
			}
		);
		
		deletePartOfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonPanel2.removeAll();
				deletePartOf();
				}
			}
		);
		
		
		buttonPanel1.add(deletePersonButton);
		buttonPanel1.add(deleteCompetesInButton);
		buttonPanel1.add(deleteTeamButton);
		buttonPanel1.add(deleteEquipmentButton);
		buttonPanel1.add(deletePartOfButton);

		
		if(PID != -1)
		{
			deletePersonButton.setVisible(false);
			deleteTeamButton.setVisible(false);
			deleteEquipmentButton.setVisible(false);
			if(!isCoach) {
				deletePartOfButton.setVisible(false);
			}
		}
		

		
		frame.add(buttonPanel1);
		
		frame.pack();
		frame.setVisible(true);
		//Prepare buttons and functionality for delete
	}


	public void deleteEquipment() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.removeAll();
		buttonPanel2.repaint();
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Equipment   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> events = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllSuitModels()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				events.add(rs.getString("Model"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = events.toArray(new String[events.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());
		
		JButton removeEquipmentButton = new JButton("Remove");
		removeEquipmentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String model = (String)eSelector.getSelectedItem();
				EquipmentService ES = new EquipmentService(databaseCS);
				ES.removeEquipment(model);
				deleteEquipment();

				}
			}
		);

		buttonPanel2.add(eDropDown);
		buttonPanel2.add(removeEquipmentButton);
		
		frame.add(buttonPanel2);
		frame.pack();
	}



	void deleteCompetesIn() {
		reset();
		frame.add(buttonPanel1);
		// DELETE SWIMMER
		buttonPanel2.setBackground(Color.LIGHT_GRAY);

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
				
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");
						
		//Distance
		JPanel distanceDropDown = new JPanel();
		distanceDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel distanceDDLabel = new JLabel("Event Distance   ");
		distanceDropDown.add(distanceDDLabel);
		Integer[] distanceOptions = { 50, 100, 200, 400};
		JComboBox<Integer> distanceSelector = new JComboBox<Integer>(distanceOptions);
		distanceSelector.setMaximumSize(distanceSelector.getPreferredSize());
		distanceSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		distanceDropDown.add(distanceSelector);
		distanceDropDown.setMaximumSize(distanceDropDown.getPreferredSize());

		//STROKE DROPBOX
		JPanel strokeDropDown = new JPanel();
		strokeDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel strokeDDLabel = new JLabel("Stroke   ");
		strokeDropDown.add(strokeDDLabel);
		String[] strokeOptions = { "Butterfly", "Backstroke", "Breaststroke", "Freestyle", "IM"};
		JComboBox<String> strokeSelector = new JComboBox<String>(strokeOptions);
		strokeSelector.setMaximumSize(strokeSelector.getPreferredSize());
		strokeSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		strokeDropDown.add(strokeSelector);
		strokeDropDown.setMaximumSize(strokeDropDown.getPreferredSize());
						
		//Units DROPBOX
		JPanel unitDropDown = new JPanel();
		unitDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel unitDDLabel = new JLabel("Unit   ");
		unitDropDown.add(unitDDLabel);
		String[] unitOptions = { "SCY", "SCM", "LCM"};
		JComboBox<String> unitSelector = new JComboBox<String>(unitOptions);
		unitSelector.setMaximumSize(unitSelector.getPreferredSize());
		unitSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		unitDropDown.add(unitSelector);
		unitDropDown.setMaximumSize(unitDropDown.getPreferredSize());
						
				
		JButton removeCompetesInButton = new JButton("Remove");
		removeCompetesInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				
				
				
				int personID = PID;
				if( PID == -1 || isCoach)
				{
					personID = SwimmingGUI.getID(firstName, lastName, databaseCS);
				}
				int distance = (int)distanceSelector.getSelectedItem();
				String stroke = (String)strokeSelector.getSelectedItem();
				String unit = (String)unitSelector.getSelectedItem();
				System.out.println(PID);
				CompetesInService cis = new CompetesInService(databaseCS);
				cis.deleteCompetesIn(personID, distance, stroke, unit);
						
				System.out.println(firstName);
				System.out.println(lastName);
				System.out.println(distance);
				System.out.println(stroke);
				System.out.println(unit);
						

				}
			}
		);
		
		if(PID != -1 && !isCoach)
		{
			firstNameLabel.setVisible(false);
			firstNameField.setVisible(false);
			lastNameLabel.setVisible(false);
			lastNameField.setVisible(false);
		}
				
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(distanceDropDown);
		buttonPanel2.add(strokeDropDown);
		buttonPanel2.add(unitDropDown);
		buttonPanel2.add(removeCompetesInButton);
				
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	void deletePerson() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		// INSERT PERSON
		
		JLabel removePerson = new JLabel("Remove Person:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");

		//INSERT BUTTON
		JButton removePersonButton = new JButton("Remove");
		removePersonButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				PersonService ps = new PersonService(databaseCS);
				ps.deletePerson(firstName, lastName);
				buttonPanel3.removeAll();
				//displayPersonToUpdate(firstName, lastName);

			}
		});
		
		buttonPanel2.add(removePerson);
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(removePersonButton);
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	void deleteTeam() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.removeAll();
		buttonPanel2.repaint();
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		// INSERT PERSON
		
		JLabel removeTeam = new JLabel("Remove Team:  ");

		//FirstName TEXTBOX
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Team Name   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> cuts = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllTeamNames()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				cuts.add(rs.getString("TeamName"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = cuts.toArray(new String[cuts.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());

		//INSERT BUTTON
		JButton removeTeamButton = new JButton("Remove");
		removeTeamButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String teamName = (String) eSelector.getSelectedItem();
				TeamService ts = new TeamService(databaseCS);
				ts.removeTeam(teamName);
				deletePartOf();

			}
		});
		
		buttonPanel2.add(removeTeam);
		buttonPanel2.add(eDropDown);

		buttonPanel2.add(removeTeamButton);
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	
	void deletePartOf() {
		reset();
		frame.add(buttonPanel1);
		buttonPanel2.removeAll();
		buttonPanel2.repaint();
		buttonPanel2.setBackground(Color.LIGHT_GRAY);
		// INSERT PERSON
		
		JLabel removePartOf = new JLabel("Remove Person From Team:  ");

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");
		
		//FirstName TEXTBOX
		JPanel eDropDown = new JPanel();
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Team Name   ");
		eDropDown.add(eDDLabel);
		ArrayList<String> cuts = new ArrayList<String>();
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getAllTeamNames()";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
	
			while(rs.next()) {
				cuts.add(rs.getString("TeamName"));
			}
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		String[] eOptions = cuts.toArray(new String[cuts.size()]);
		JComboBox<String> eSelector = new JComboBox<String>(eOptions);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());

		//INSERT BUTTON
		JButton removePartOfButton = new JButton("Remove");
		removePartOfButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				String teamName = (String) eSelector.getSelectedItem();
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				PartOfService pos = new PartOfService(databaseCS);
				pos.removePartOf(SwimmingGUI.getID(firstName, lastName, databaseCS), teamName);
				deleteTeam();

			}
		});
		
		buttonPanel2.add(removePartOf);
		buttonPanel2.add(firstNameLabel);
		buttonPanel2.add(firstNameField);
		buttonPanel2.add(lastNameLabel);
		buttonPanel2.add(lastNameField);
		buttonPanel2.add(eDropDown);

		buttonPanel2.add(removePartOfButton);
		frame.add(buttonPanel2);
		frame.pack();
	}
	
	void reset() {
		frame.remove(buttonPanel1);
		frame.remove(buttonPanel2);
		frame.remove(buttonPanel3);
		frame.remove(buttonPanel4);
		frame.remove(buttonPanel5);
		buttonPanel1.setVisible(true);
		buttonPanel2.setVisible(true);
		buttonPanel3.setVisible(true);
		buttonPanel4.setVisible(true);
		buttonPanel5.setVisible(true);
		buttonPanel6.setVisible(true);

		frame.repaint();
		BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
		frame.setLayout(layout);
	}

}
