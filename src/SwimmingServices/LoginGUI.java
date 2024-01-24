package SwimmingServices;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mainApp.SwimmingGUI;

public class LoginGUI {

	private UserService userS = null;
	private final int LOGIN_FRAME_WIDTH = 350;
	private final int LOGIN_FRAME_HEIGHT = 170;
	private JFrame frame = new JFrame();

	public LoginGUI(UserService userService) {

		this.userS = userService;

		frame.setTitle("Login to Swimming Database");
		frame.setPreferredSize(new Dimension(LOGIN_FRAME_WIDTH, LOGIN_FRAME_HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FlowLayout layout = new FlowLayout();
		frame.setLayout(layout);

		JPanel buttonPanel1 = new JPanel();
		JPanel buttonPanel2 = new JPanel();
		JPanel buttonPanel3 = new JPanel();

		// FirstName TEXTBOX
		JLabel usernameLabel = new JLabel("Username   ");
		JTextField usernameField = new JTextField(20);
		usernameField.setText("");

		// LastName TEXTBOX
		JLabel passwordLabel = new JLabel("Password    ");
		JTextField passwordField = new JTextField(20);
		passwordField.setText("");

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = passwordField.getText();
				
				if(username.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Username must be provided.");
					return;
				}
				try {
					if (userS.login(username, password)) {
						frame.dispose();
						int userPID = userS.getPID(username);
						if(username.equals("Admin"))
						{
							userPID = -1;
						}
						new SwimmingGUI(userS.getConnect(), userPID);
					}
				} catch(IllegalArgumentException err) {
					// probably this should be stealthier so people can't snoop for existing user accounts
					JOptionPane.showMessageDialog(null, "User does not exist.");
					return;
				}
			}
		});

		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RegisterGUI(userS.getConnect(), userS);
			}
		});

		buttonPanel1.add(usernameLabel);
		buttonPanel1.add(usernameField);
		buttonPanel2.add(passwordLabel);
		buttonPanel2.add(passwordField);
		buttonPanel3.add(loginButton);
		buttonPanel3.add(registerButton);

		frame.add(buttonPanel1);
		frame.add(buttonPanel2);
		frame.add(buttonPanel3);

		frame.pack();
		frame.setVisible(true);

	}

}
