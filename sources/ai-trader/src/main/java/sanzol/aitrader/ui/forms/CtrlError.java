package sanzol.aitrader.ui.forms;

import javax.swing.JLabel;

import sanzol.aitrader.ui.config.Styles;
import sanzol.util.ExceptionUtils;

public class CtrlError extends JLabel
{
	private static final long serialVersionUID = 1L;

	public void ERROR(Exception e)
	{
		ERROR(ExceptionUtils.getMessage(e));
	}

	public void ERROR(String msg)
	{
		setForeground(Styles.COLOR_TEXT_ERROR);
		setText(" " + msg);
	}

	public void INFO(String msg)
	{
		setForeground(Styles.COLOR_TEXT_INFO);
		setText(" " + msg);
	}

	public void CLEAN()
	{
		setForeground(Styles.COLOR_TEXT_INFO);
		setText("");
	}

}
