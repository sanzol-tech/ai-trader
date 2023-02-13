package aitrader.ui.controls;

import javax.swing.JLabel;

import aitrader.ui.config.Styles;
import aitrader.ui.config.UILog;
import aitrader.util.ExceptionUtils;

public class CtrlError extends JLabel
{
	private static final long serialVersionUID = 1L;

	public void ERROR(Exception e)
	{
		UILog.error(e);
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
