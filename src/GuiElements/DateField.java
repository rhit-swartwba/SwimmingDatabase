package GuiElements;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.Date;

public class DateField extends JTextField {
	public DateField(int columns) {
		super(columns);
	}

	public Date valueOf() {
		try {
			return Date.valueOf(getText());
		} catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Please use a date formatted as YYYY-MM-DD");
			return null;
		}
	}
}
