package mainApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class ChangeTypeActionListener implements ActionListener {

	private EditGUI egui;
	JComboBox<String> editTypeSelector;
	public ChangeTypeActionListener(EditGUI egui, JComboBox<String> editTypeSelector)
	{
		this.egui = egui;
		this.editTypeSelector = editTypeSelector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String type = (String)editTypeSelector.getSelectedItem();
		if(type.equals("Remove"))
		{
			egui.displayDelete();
		}
		else if(type.equals("Update"))
		{
			egui.displayUpdate();
		}
		else
		{
			egui.displayInsert();
		}

		}
	}


