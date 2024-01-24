package mainApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import SwimmingServices.DatabaseConnectionService;

/**
 * Class: SwimmingGUI
 * 
 * @author Blaise Swartwood, Brian Beasley, Ben Graham
 *         Purpose: Used to create the GUI to display the swimming information database
 */
public class SwimmingGUI {

	private final int FRAME_WIDTH = 1350;
	private final int FRAME_HEIGHT = 800;
	private DatabaseConnectionService databaseCS = null;
	int PID;
	DataTypeConverter DTC = new DataTypeConverter();
	JFrame frame = new JFrame();
	JPanel resultsPanel = new JPanel();
	JPanel resultsTablePanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JPanel swimmerInfoPanel = new JPanel();
	JPanel coachInfoPanel = new JPanel();
	JPanel swimmerTeamInfoPanel = new JPanel();
	JPanel coachTeamInfoPanel = new JPanel();
	
	Color skyBlue = new Color(135, 206, 235);
	Color lightBlue = new Color(173, 216, 230);
	Color coral = new Color(255,127,80);
	Color tomato = new Color(255,99,71);
	
	boolean isCoach = false;
	boolean swimmerInfoDisplayed = false;

	/**
	 * ensures: the appropriate displays are shown on the EvoGUI
	 */
	public SwimmingGUI(DatabaseConnectionService dcs, int userID) {

		this.databaseCS = dcs;
		this.PID = userID;
		//if PID = -1, then we know that the person is an admin!!!, otherwise thats the person ID
		//System.out.println(this.PID);
		if(PID != -1) {
			try {
				CallableStatement stmt = this.databaseCS.getConnection().prepareCall("{ ? = call isCoach(?)}");
				stmt.registerOutParameter(1, Types.INTEGER);
				stmt.setInt(2, PID);
				stmt.execute();
				int returnValue = stmt.getInt(1);
				if(returnValue == 1) {
					isCoach = true;
				} else if(returnValue == 3) {
					JOptionPane.showMessageDialog(null, "The PID cannot be null");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "The");			
			}
		}
		
		
		frame.setTitle("SwimmingMain");
		frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		swimmerInfoPanel.setLayout(new BoxLayout(swimmerInfoPanel, BoxLayout.Y_AXIS));
		coachInfoPanel.setLayout(new BoxLayout(coachInfoPanel, BoxLayout.Y_AXIS));
		swimmerTeamInfoPanel.setLayout(new BoxLayout(swimmerTeamInfoPanel, BoxLayout.Y_AXIS));
		coachTeamInfoPanel.setLayout(new BoxLayout(coachTeamInfoPanel, BoxLayout.Y_AXIS));
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		resultsTablePanel.setLayout(new BoxLayout(resultsTablePanel, BoxLayout.Y_AXIS));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.setBackground( Color.LIGHT_GRAY );
		
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		
		// Creating all labels and text boxes
		
		//FirstName TEXTBOX
		JLabel firstNameLabel = new JLabel("FirstName:   ");
		JTextField firstNameField = new JTextField(10);
		firstNameField.setText("");
		
		//LastName TEXTBOX
		JLabel lastNameLabel = new JLabel("LastName:   ");
		JTextField lastNameField = new JTextField(10);
		lastNameField.setText("");

		//EVENT DROPBOX
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
		
		JCheckBox allTimesBox = new JCheckBox("All Events");
		allTimesBox.setBackground(Color.LIGHT_GRAY);
		allTimesBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}

		});

		JButton simButton = new JButton("Search For Times");
		simButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Parse code
				if(PID == -1 || isCoach) {
					swimmerInfoPanel.removeAll();
					swimmerInfoPanel.repaint();
					swimmerInfoPanel.revalidate();
				}
				resultsTablePanel.removeAll();
				resultsTablePanel.repaint();
				resultsTablePanel.revalidate();
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				if(PID == -1 || isCoach) {
					displayInfo(getID(firstName, lastName, databaseCS), false);
				}
				if(!allTimesBox.isSelected())
				{
					int distance = (int)distanceSelector.getSelectedItem();
					String stroke = (String)strokeSelector.getSelectedItem();
					String unit = (String)unitSelector.getSelectedItem();
					if(PID != -1 && !isCoach) {
						displayResults(PID, distance, stroke, unit);
					}else {
						displayResults(getID(firstName, lastName, databaseCS), distance, stroke, unit);
					}
				}
				else
				{
					if(PID != -1 && !isCoach) {
						displayResults(PID, null, null, null);
					}else {
						displayResults(getID(firstName, lastName, databaseCS), null, null, null);
					}
				}

				}
			}
		);
		
		JPanel eDropDown = new JPanel();
		
		eDropDown.setLayout(new BoxLayout(eDropDown, BoxLayout.X_AXIS));
		eDropDown.setBackground( Color.LIGHT_GRAY );
		JLabel eDDLabel = new JLabel("Cut Level      ");
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
		
		eSelector.setAlignmentX(Component.LEFT_ALIGNMENT);
		eDropDown.add(eSelector);
		eSelector.setMaximumSize(eSelector.getPreferredSize());
		eDropDown.setMaximumSize(eDropDown.getPreferredSize());
		JButton timeOffSearch = new JButton("Search For Time Off A Cut");
		timeOffSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(PID == -1 || isCoach) {
					swimmerInfoPanel.removeAll();
					swimmerInfoPanel.repaint();
					swimmerInfoPanel.revalidate();
				}
				resultsTablePanel.removeAll();
				resultsTablePanel.repaint();
				resultsTablePanel.revalidate();
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				String level = (String) eSelector.getSelectedItem();
				if(PID == -1 || isCoach) {
					displayInfo(getID(firstName, lastName, databaseCS), false);
				}
				if(!allTimesBox.isSelected())
				{
					int distance = (int)distanceSelector.getSelectedItem();
					String stroke = (String)strokeSelector.getSelectedItem();
					String unit = (String)unitSelector.getSelectedItem();
					if(PID != -1 && !isCoach) {
						displayTimeOffOneEvent(PID, distance, stroke, unit, level);
					}else {
						displayTimeOffOneEvent(getID(firstName, lastName, databaseCS), distance, stroke, unit, level);
					}
				}
				else
				{
					if(PID != -1 && !isCoach) {
						displayTimeOffAllEvents(PID, level);
					}else {
						displayTimeOffAllEvents(getID(firstName, lastName, databaseCS), level);
					}
				}
			}
		});
		
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				swimmerInfoPanel.removeAll();
				coachInfoPanel.removeAll();
				
				resultsPanel.removeAll();
				resultsPanel.repaint();
				resultsPanel.revalidate();
				
				resultsTablePanel.removeAll();
				resultsTablePanel.repaint();
				resultsTablePanel.revalidate();
				
				if(PID != -1) {
					displayInfo(PID, isCoach);
				}
				
				String firstName = firstNameField.getText();
				String lastName = lastNameField.getText();
				if(PID == -1) {
					displayInfo(getID(firstName, lastName, databaseCS), false);
				}
				
			}
		});
		
		JButton updateButton = new JButton("Edit User");
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new EditGUI(dcs, PID, isCoach);
				}
			}
		);
		
		JButton viewTimeStandardsButton = new JButton("View All Time Standards");
		viewTimeStandardsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewTimeStandards();
			}

		});
		
		if(PID != -1) {
			displayInfo(PID, isCoach);
		}

		searchPanel.add(firstNameLabel);
		searchPanel.add(firstNameField);
		searchPanel.add(lastNameLabel);
		searchPanel.add(lastNameField);
		searchPanel.add(distanceDropDown);
		searchPanel.add(strokeDropDown);
		searchPanel.add(unitDropDown);
		searchPanel.add(allTimesBox);
		searchPanel.add(simButton);
		searchPanel.add(Box.createHorizontalStrut(10));
		searchPanel.add(eDropDown);
		searchPanel.add(Box.createHorizontalStrut(40));
		searchPanel.add(timeOffSearch);
		
		buttonPanel.add(updateButton);
		buttonPanel.add(viewTimeStandardsButton);
		buttonPanel.add(refreshButton);
		
		if(PID != -1 && !isCoach)
		{
			firstNameLabel.setVisible(false);
			firstNameField.setVisible(false);
			lastNameLabel.setVisible(false);
			lastNameField.setVisible(false);
		}

		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(searchPanel, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);

	}

	protected void viewTimeStandards() {
		JFrame timeStandardFrame = new JFrame();
		timeStandardFrame.setTitle("Time Standards");
		timeStandardFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		timeStandardFrame.setVisible(true);
		timeStandardFrame.setLayout(new BoxLayout(timeStandardFrame.getContentPane(), BoxLayout.Y_AXIS));
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM viewTimeStandards\nORDER BY Level, Unit, Stroke, Distance";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
		
			DefaultTableModel model = new DefaultTableModel(); 
			JTable results = new JTable(model); 
			model.addColumn("Distance");
			model.addColumn("Stroke");
			model.addColumn("Unit");
			model.addColumn("Cut Level");
			model.addColumn("Male Time");
			model.addColumn("Female Time");

			
			while(rs.next()) {
				model.addRow(new Object[]{((Integer)rs.getInt("Distance")).toString(), rs.getString("Stroke"),
						rs.getString("Unit"), rs.getString("Level"), DTC.decimalToString(rs.getDouble("MaleTime")), 
						DTC.decimalToString(rs.getDouble("FemaleTime"))});
			}


			
			timeStandardFrame.add(results.getTableHeader());
			timeStandardFrame.add(results);
			timeStandardFrame.pack();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
	}

	protected void displayTimeOffAllEvents(int pid, String level) {
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM get_cutComparedTo(?,?)\n";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, pid);
			if(level != null || !level.equals("")) {
				stmt.setString(2, level);
			}else {
				JOptionPane.showMessageDialog(null, "Must provide a cut level to search for");
				return;
			}
			ResultSet rs = stmt.executeQuery();
		
			DefaultTableModel model = new DefaultTableModel(); 
			JTable results = new JTable(model); 
			model.addColumn("Distance");
			model.addColumn("Stroke");
			model.addColumn("Unit");
			model.addColumn("Cut Level");
			model.addColumn("Swimmer Time");
			model.addColumn("Cut Time");
			model.addColumn("Time Off");
			
			while(rs.next()) {
				model.addRow(new Object[]{((Integer)rs.getInt("Distance")).toString(), rs.getString("Stroke"),
						rs.getString("Unit"), rs.getString("Level"), DTC.decimalToString(rs.getDouble("Time")), 
						DTC.decimalToString(rs.getDouble("CutTime")), DTC.decimalToString(rs.getDouble("TimeDiff"))});
			}

		    TableColumn col = results.getColumnModel().getColumn(6);
		    col.setCellRenderer(new MyRenderer(model));

			
			results.setFillsViewportHeight(true);
			resultsTablePanel.add(results.getTableHeader());
			resultsTablePanel.add(results);
			resultsPanel.add(resultsTablePanel);
			frame.add(resultsPanel);
			frame.pack();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		
	}

	protected void displayTimeOffOneEvent(int pid, int distance, String stroke, String unit, String level) {
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM getTimeOffCut(?,?,?,?,?)\n";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, pid);
			stmt.setInt(2, distance);
			stmt.setString(3, stroke);
			stmt.setString(4, unit);
			stmt.setString(5, level);
			
			ResultSet rs = stmt.executeQuery();
		
			DefaultTableModel model = new DefaultTableModel(); 
			JTable results = new JTable(model); 
			model.addColumn("Distance");
			model.addColumn("Stroke");
			model.addColumn("Unit");
			model.addColumn("Cut Level");
			model.addColumn("Swimmer Time");
			model.addColumn("Cut Time");
			model.addColumn("Time Off");
			
			while(rs.next()) {
				if(rs.getDouble("Time") != 0.0 || rs.getDouble("CutTime") != 0.0) {
					model.addRow(new Object[]{((Integer)rs.getInt("Distance")).toString(), rs.getString("Stroke"),
							rs.getString("Unit"), rs.getString("Level"), DTC.decimalToString(rs.getDouble("Time")), 
							DTC.decimalToString(rs.getDouble("CutTime")), DTC.decimalToString(rs.getDouble("TimeOff"))});
				}
			}
			
			TableColumn col = results.getColumnModel().getColumn(6);
		    col.setCellRenderer(new MyRenderer(model));
			
			results.setFillsViewportHeight(true);
			resultsTablePanel.add(results.getTableHeader());
			resultsTablePanel.add(results);
			resultsPanel.add(resultsTablePanel);
			frame.add(resultsPanel);
			frame.pack();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
		
	}

	protected void displayInfo(int personID, boolean isCoach) {
		if(isCoach) {
			try {
				PreparedStatement stmt = null;
				String query = "SELECT * \nFROM getCoachInfo(?)\n";
				stmt = this.databaseCS.getConnection().prepareStatement(query);
				stmt.setInt(1, personID);
				ResultSet rs = stmt.executeQuery();
				DefaultTableModel model = new DefaultTableModel(); 
				JTable CoachInfo = new JTable(model); 
				model.addColumn("First Name");
				model.addColumn("Last Name");
				model.addColumn("Experience (years)");
				model.addColumn("Style");
				if(rs.next()) {
					model.addRow(new Object[]{rs.getString("FName"), rs.getString("LName"), rs.getInt("experience"), 
							rs.getString("style")});
				}
				CoachInfo.setFillsViewportHeight(true);
				CoachInfo.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
				JTableHeader cols = CoachInfo.getTableHeader();
				cols.setBackground(tomato);
				coachInfoPanel.add(cols);
				CoachInfo.setBackground(coral);
				for (int column = 0; column < CoachInfo.getColumnCount(); column++)
				{
				    TableColumn tableColumn = CoachInfo.getColumnModel().getColumn(column);
				    int preferredWidth = tableColumn.getMinWidth();
				    int maxWidth = tableColumn.getMaxWidth();

				    for (int row = 0; row < CoachInfo.getRowCount(); row++)
				    {
				        TableCellRenderer cellRenderer = CoachInfo.getCellRenderer(row, column);
				        Component c = CoachInfo.prepareRenderer(cellRenderer, row, column);
				        int width = c.getPreferredSize().width + CoachInfo.getIntercellSpacing().width;
				        preferredWidth = Math.max(preferredWidth, width);

				        //  We've exceeded the maximum width, no need to check other rows

				        if (preferredWidth >= maxWidth)
				        {
				            preferredWidth = maxWidth;
				            break;
				        }
				    }

				    tableColumn.setPreferredWidth( preferredWidth );
				}
				
				coachInfoPanel.add(CoachInfo);
				
				try {
					PreparedStatement statement = null;
					String query2 = "SELECT * \nFROM getTeamInfo(?)\n";
					statement = this.databaseCS.getConnection().prepareStatement(query2);
					statement.setInt(1, personID);
					ResultSet rs2 = statement.executeQuery();
					DefaultTableModel model2 = new DefaultTableModel(); 
					JTable teamInfo = new JTable(model2); 
					model2.addColumn("Team Name");
					model2.addColumn("Group");
					model2.addColumn("Hours Per Week");
					while(rs2.next()) {
						model2.addRow(new Object[]{rs2.getString("TeamName"), rs2.getString("Group"), rs2.getInt("HoursPerWeek")});
					}
					teamInfo.setFillsViewportHeight(true);
					teamInfo.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
					JTableHeader cols2 = teamInfo.getTableHeader();
					cols2.setBackground(tomato);
					coachInfoPanel.add(cols2);
					teamInfo.setBackground(coral);
					for (int column = 0; column < teamInfo.getColumnCount(); column++)
					{
					    TableColumn tableColumn = teamInfo.getColumnModel().getColumn(column);
					    int preferredWidth = tableColumn.getMinWidth();
					    int maxWidth = tableColumn.getMaxWidth();

					    for (int row = 0; row < teamInfo.getRowCount(); row++)
					    {
					        TableCellRenderer cellRenderer = teamInfo.getCellRenderer(row, column);
					        Component c = teamInfo.prepareRenderer(cellRenderer, row, column);
					        int width = c.getPreferredSize().width + teamInfo.getIntercellSpacing().width;
					        preferredWidth = Math.max(preferredWidth, width);

					        //  We've exceeded the maximum width, no need to check other rows

					        if (preferredWidth >= maxWidth)
					        {
					            preferredWidth = maxWidth;
					            break;
					        }
					    }

					    tableColumn.setPreferredWidth( preferredWidth );
					}
					
				
					coachInfoPanel.add(teamInfo);
					resultsPanel.add(coachInfoPanel);
					frame.pack();
				}
				catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Failed to find the info for this swimmer");
					ex.printStackTrace();
					return;
				}
				resultsPanel.add(coachInfoPanel);
				frame.pack();
			}
			catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Failed to find the info for this swimmer");
				ex.printStackTrace();
				return;
			}
		}else {
			try {
				PreparedStatement stmt = null;
				String query = "SELECT * \nFROM get_swimmerinfo(?)\n";
				stmt = this.databaseCS.getConnection().prepareStatement(query);
				stmt.setInt(1, personID);
				ResultSet rs = stmt.executeQuery();
				DefaultTableModel model = new DefaultTableModel(); 
				JTable SwimmerInfo = new JTable(model); 
				model.addColumn("Firstname");
				model.addColumn("Lastname");
				model.addColumn("Age (years)");
				model.addColumn("Height (inches)");
				model.addColumn("Weight");
				model.addColumn("Age Group");
				if(rs.next()) {
					model.addRow(new Object[]{rs.getString("FName"), rs.getString("LName"), rs.getInt("Age"), 
							rs.getInt("Height"), rs.getInt("Weight"), rs.getString("AgeGroup")});
				}
				SwimmerInfo.setFillsViewportHeight(true);
				SwimmerInfo.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
				JTableHeader cols = SwimmerInfo.getTableHeader();
				cols.setBackground(skyBlue);
				swimmerInfoPanel.add(cols);
				SwimmerInfo.setBackground(lightBlue);
				for (int column = 0; column < SwimmerInfo.getColumnCount(); column++)
				{
				    TableColumn tableColumn = SwimmerInfo.getColumnModel().getColumn(column);
				    int preferredWidth = tableColumn.getMinWidth();
				    int maxWidth = tableColumn.getMaxWidth();
	
				    for (int row = 0; row < SwimmerInfo.getRowCount(); row++)
				    {
				        TableCellRenderer cellRenderer = SwimmerInfo.getCellRenderer(row, column);
				        Component c = SwimmerInfo.prepareRenderer(cellRenderer, row, column);
				        int width = c.getPreferredSize().width + SwimmerInfo.getIntercellSpacing().width;
				        preferredWidth = Math.max(preferredWidth, width);
	
				        //  We've exceeded the maximum width, no need to check other rows
	
				        if (preferredWidth >= maxWidth)
				        {
				            preferredWidth = maxWidth;
				            break;
				        }
				    }
	
				    tableColumn.setPreferredWidth( preferredWidth );
				}
				
			
				swimmerInfoPanel.add(SwimmerInfo);
				
				try {
					PreparedStatement statement = null;
					String query2 = "SELECT * \nFROM getTeamInfo(?)\n";
					statement = this.databaseCS.getConnection().prepareStatement(query2);
					statement.setInt(1, personID);
					ResultSet rs2 = statement.executeQuery();
					DefaultTableModel model2 = new DefaultTableModel(); 
					JTable teamInfo = new JTable(model2); 
					model2.addColumn("Team Name");
					model2.addColumn("Group");
					model2.addColumn("Hours Per Week");
					while(rs2.next()) {
						model2.addRow(new Object[]{rs2.getString("TeamName"), rs2.getString("Group"), rs2.getInt("HoursPerWeek")});
					}
					teamInfo.setFillsViewportHeight(true);
					teamInfo.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
					JTableHeader cols2 = teamInfo.getTableHeader();
					cols2.setBackground(skyBlue);
					swimmerInfoPanel.add(cols2);
					teamInfo.setBackground(lightBlue);
					for (int column = 0; column < teamInfo.getColumnCount(); column++)
					{
					    TableColumn tableColumn = teamInfo.getColumnModel().getColumn(column);
					    int preferredWidth = tableColumn.getMinWidth();
					    int maxWidth = tableColumn.getMaxWidth();

					    for (int row = 0; row < teamInfo.getRowCount(); row++)
					    {
					        TableCellRenderer cellRenderer = teamInfo.getCellRenderer(row, column);
					        Component c = teamInfo.prepareRenderer(cellRenderer, row, column);
					        int width = c.getPreferredSize().width + teamInfo.getIntercellSpacing().width;
					        preferredWidth = Math.max(preferredWidth, width);

					        //  We've exceeded the maximum width, no need to check other rows

					        if (preferredWidth >= maxWidth)
					        {
					            preferredWidth = maxWidth;
					            break;
					        }
					    }

					    tableColumn.setPreferredWidth( preferredWidth );
					}
					
				
					swimmerInfoPanel.add(teamInfo);
					resultsPanel.add(swimmerInfoPanel);
					frame.pack();
				}
				catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Failed to find the info for this swimmer");
					ex.printStackTrace();
					return;
				}
				
				resultsPanel.add(swimmerInfoPanel);
				frame.pack();
			}
			catch (SQLException ex) {
				JOptionPane.showMessageDialog(null, "Failed to find the info for this swimmer");
				ex.printStackTrace();
				return;
			}
		}
		
		frame.add(resultsPanel);
	}

	
	
	
	protected void displayResults(int pid, Integer distance, String stroke, String unit) {
		try {
			PreparedStatement stmt = null;
			String query = "SELECT * \nFROM get_swimmerTimes(?,?,?,?)\n ORDER BY Unit, Stroke, Distance";
			stmt = this.databaseCS.getConnection().prepareStatement(query);
			stmt.setInt(1, pid);
			if(distance != null) {
				stmt.setInt(2, distance);
				stmt.setString(3, stroke);
				stmt.setString(4, unit);
			}else {
				stmt.setInt(2, -1);
				stmt.setString(3, "All");
				stmt.setString(4, "All");
			}
			
			ResultSet rs = stmt.executeQuery();
		
			DefaultTableModel model = new DefaultTableModel(); 
			JTable results = new JTable(model); 
			model.addColumn("Distance");
			model.addColumn("Stroke");
			model.addColumn("Unit");
			model.addColumn("Time");
			model.addColumn("Equipment");
			model.addColumn("Brand");
			
			while(rs.next()) {
				model.addRow(new Object[]{((Integer)rs.getInt("Distance")).toString(), rs.getString("Stroke"),
						rs.getString("Unit"), DTC.decimalToString(rs.getDouble("Time")), rs.getString("Equip"), rs.getString("Brand")});
			}
			
			results.setFillsViewportHeight(true);
			resultsTablePanel.add(results.getTableHeader());
			resultsTablePanel.add(results);
			resultsPanel.add(resultsTablePanel);
			frame.add(resultsPanel);
			frame.pack();
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Failed to find the times for this swimmer");
			ex.printStackTrace();
			return;
		}
	}
	
	public static int getID(String fName, String lName, DatabaseConnectionService dcs) {
		int personID = 0;
		try {
			CallableStatement stmt = dcs.getConnection().prepareCall("{? = call getPersonID(?,?,?)}");
			stmt.registerOutParameter(1, Types.INTEGER);
			stmt.setString(2, fName);
			stmt.setString(3, lName);
			stmt.registerOutParameter(4, Types.INTEGER);
			stmt.execute();
			int returnVal = stmt.getInt(1);
			personID = stmt.getInt(4);
			if(returnVal == 1) {
				JOptionPane.showMessageDialog(null, "ERROR: Person not in database.");	
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "ERROR: Failed to retrieve the ID");			
		}
		return personID;	
	}
	
	class MyRenderer extends DefaultTableCellRenderer 
	{
		DefaultTableModel model;
	    public MyRenderer(DefaultTableModel model) {
	       super();
	       this.model = model;
	    }
	    public Component getTableCellRendererComponent(JTable table, Object 
	    value, boolean isSelected, boolean hasFocus, int row, int column) 
	    {
	       Component cell = super.getTableCellRendererComponent(table, value, 
	       isSelected, hasFocus, row, column);
	       double time = DTC.stringToDecimal(model.getValueAt(row, column).toString());
	       if(time > 0) {
	    	   cell.setBackground(Color.red);
	       }else {
	    	   cell.setBackground(Color.green);
	       }
	       return cell;
	    }
	}

}// end
