package SwimmingServices;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import GuiElements.DateField;
import SwimmingServices.PersonService.Sex;

public class RegisterGUI {

	private final int EDIT_FRAME_WIDTH = 650;
	private final int EDIT_FRAME_HEIGHT = 350;
	private JFrame frame = new JFrame();
	private DatabaseConnectionService databaseCS = null;
	private UserService userS = null;

	
	public RegisterGUI(DatabaseConnectionService dcs, UserService userS)
	{
		this.databaseCS = dcs;
		this.userS = userS;
		frame.setTitle("Register Database");
		frame.setPreferredSize(new Dimension(EDIT_FRAME_WIDTH, EDIT_FRAME_HEIGHT));
		FlowLayout layout = new FlowLayout();
		frame.setLayout(layout);
		
		JPanel buttonPanel0 = new JPanel();

		JLabel insertPerson = new JLabel("Register yourself to the database!");
		buttonPanel0.add(insertPerson);
		frame.add(buttonPanel0);
		
		addToDatabaseInsert();
		
		
		frame.pack();
		frame.setVisible(true);

	}
	
	
	
	public void addToDatabaseInsert()
	{
		
		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel buttonPanel3 = new JPanel();
		JPanel buttonPanel4 = new JPanel();
		JPanel buttonPanel5 = new JPanel();
		JPanel buttonPanel6 = new JPanel();
		JPanel buttonPanel7 = new JPanel();
//

		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");
		
		//SEX DROPBOX
		JPanel sexDropDown = new JPanel();
		JLabel sexDDLabel = new JLabel("Sex   ");
		sexDropDown.add(sexDDLabel);
		String[] sexOptions = { "M", "F"};
		JComboBox<String> sexSelector = new JComboBox<String>(sexOptions);
		sexSelector.setMaximumSize(sexSelector.getPreferredSize());
		sexSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		sexDropDown.add(sexSelector);
		sexDropDown.setMaximumSize(sexDropDown.getPreferredSize());
		
		//DOB
		JLabel dobLabel = new JLabel("DOB   ");
		DateField dobField = new DateField(10);
		dobField.setText("");
		
		//HEIGHT
		JLabel heightLabel = new JLabel("Height (in)   ");
		JTextField heightField = new JTextField(3);
		//WEIGHT
		JLabel weightLabel = new JLabel("Weight   ");
		JTextField weightField = new JTextField(3);

		//Experience
		JLabel expLabel = new JLabel("Experience   ");
		JTextField expField = new JTextField(10);
		//Style
		JLabel styleLabel = new JLabel("Style   ");
		JTextField styleField = new JTextField(10);
		
		// IS SWIMMER?
		
		JCheckBox swimmerBox = new JCheckBox("Swimmer ? ");
		swimmerBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(swimmerBox.isSelected())
				{
					heightLabel.setVisible(true);
					heightField.setVisible(true);
					weightLabel.setVisible(true);
					weightField.setVisible(true);
				}
				else
				{
					heightLabel.setVisible(false);
					heightField.setVisible(false);
					weightLabel.setVisible(false);
					weightField.setVisible(false);
				}


			}

		});
		
		// IS COACH ?
		JCheckBox coachBox = new JCheckBox("Coach ? ");
		coachBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(coachBox.isSelected())
				{
					expLabel.setVisible(true);
					expField.setVisible(true);
					styleLabel.setVisible(true);
					styleField.setVisible(true);
				}
				else
				{
					expLabel.setVisible(false);
					expField.setVisible(false);
					styleLabel.setVisible(false);
					styleField.setVisible(false);
				}
			}

		});
		
		//PARTOF
		JPanel eDropDown = new JPanel();
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
		
		JLabel usernameLabel = new JLabel("Username   ");
		JTextField usernameField = new JTextField(20);
		usernameField.setText("");

		JLabel passwordLabel = new JLabel("Password    ");
		JTextField passwordField = new JTextField(20);
		passwordField.setText("");
		
		JButton registerToDatabaseButton = new JButton("Register to the database");
		registerToDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//NEED TO MAKE ALL FAIL OR ALL PASS TOGETHER
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String sex = (String)sexSelector.getSelectedItem();
				if(firstName.isEmpty() || lastName.isEmpty() || sex.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Name information must be provided.");
					return;
				}
				Date dob = dobField.valueOf();
				if(dob == null) {
					return;
				}
				int height = 0;
				int weight = 0;
				int experience = -1;
				String style = "NONE";
				String teamName = (String) eSelector.getSelectedItem();
				String groupName = groupField.getText();
				int hoursPerWeek = -1;
				if(!hoursField.getText().isEmpty() && !teamName.isEmpty() && !groupName.isEmpty()) {
					hoursPerWeek = Integer.parseInt(hoursField.getText());
				} else {
					JOptionPane.showMessageDialog(null, "Team information must be provided.");
					return;
				}

				if(!swimmerBox.isSelected() && !coachBox.isSelected())
				{
					JOptionPane.showMessageDialog(null, "At least one of swimmer and coach checkbox must be selected.");
					return;
				}
				if(swimmerBox.isSelected()) {
					if(!heightField.getText().isEmpty() && !weightField.getText().isEmpty()) {
						height = Integer.parseInt(heightField.getText());
						weight = Integer.parseInt(weightField.getText());
					} else {
						JOptionPane.showMessageDialog(null, "Swimmer data must be provided.");
						return;
					}
				}
				if(coachBox.isSelected())
				{				
					if(!expField.getText().isEmpty() && !styleField.getText().isEmpty()) {
						experience = Integer.parseInt(expField.getText());
						style = styleField.getText();
					} else {
						JOptionPane.showMessageDialog(null, "Coach data must be provided.");
						return;
					}
				}
				
				//REGISTER
				String password, username, saltAsString, hashPass;
				if(!passwordField.getText().isEmpty() && !usernameField.getText().isEmpty()) {
					password = passwordField.getText();
					username = usernameField.getText();
					byte[] salt = userS.getNewSalt();
					saltAsString = userS.getStringFromBytes(salt);
					hashPass = userS.hashPassword(salt, password);
				} else {
					JOptionPane.showMessageDialog(null, "Username and password must be provided.");
					return;
				}
				
				AddUserService aus = new AddUserService(databaseCS);
				if(dob != null) {
					aus.addUser(firstName, lastName, sex, dob, height, weight, experience, style, 
							teamName, groupName, hoursPerWeek, username, saltAsString, hashPass);
				}	
			}
		});
		
		buttonPanel1.add(firstNameLabel);
		buttonPanel1.add(firstNameField);
		buttonPanel1.add(lastNameLabel);
		buttonPanel1.add(lastNameField);
		buttonPanel1.add(sexDropDown);
		buttonPanel1.add(dobLabel);
		buttonPanel1.add(dobField);
		buttonPanel2.add(swimmerBox);
		buttonPanel2.add(heightLabel);
		buttonPanel2.add(heightField);
		buttonPanel2.add(weightLabel);
		buttonPanel2.add(weightField);
		buttonPanel3.add(coachBox);
		buttonPanel3.add(expLabel);
		buttonPanel3.add(expField);
		buttonPanel3.add(styleLabel);
		buttonPanel3.add(styleField);
		buttonPanel4.add(eDropDown);
		buttonPanel4.add(groupLabel);
		buttonPanel4.add(groupField);
		buttonPanel4.add(hoursLabel);
		buttonPanel4.add(hoursField);
		buttonPanel5.add(usernameLabel);
		buttonPanel5.add(usernameField);
		buttonPanel5.add(passwordLabel);
		buttonPanel5.add(passwordField);

		buttonPanel7.add(registerToDatabaseButton);
		
		heightLabel.setVisible(false);
		heightField.setVisible(false);
		weightLabel.setVisible(false);
		weightField.setVisible(false);
		
		expLabel.setVisible(false);
		expField.setVisible(false);
		styleLabel.setVisible(false);
		styleField.setVisible(false);
		
		
		frame.add(buttonPanel1);
		frame.add(buttonPanel2);
		frame.add(buttonPanel3);
		frame.add(buttonPanel4);
		frame.add(buttonPanel5);		
		frame.add(buttonPanel6);
		frame.add(buttonPanel7);

	}
}
