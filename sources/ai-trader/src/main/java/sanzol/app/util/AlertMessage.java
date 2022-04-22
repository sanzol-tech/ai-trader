package sanzol.app.util;

import java.awt.Color;

import javax.swing.JTextField;

public class AlertMessage
{
	private JTextField txtError;

	public AlertMessage(JTextField txtError)
	{
		this.txtError = txtError;
	}

	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
	}

	public void ERROR(String msg)
	{
		txtError.setForeground(new Color(255, 0, 0));
		txtError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		txtError.setForeground(new Color(51, 107, 255));
		txtError.setText(" " + msg);
	}
}
