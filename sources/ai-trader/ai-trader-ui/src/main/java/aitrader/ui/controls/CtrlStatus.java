package aitrader.ui.controls;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import aitrader.ui.config.Styles;
import aitrader.util.constants.CharConstants;

public class CtrlStatus extends JLabel
{
	private static final long serialVersionUID = 1L;

	public CtrlStatus()
	{
		setText(CharConstants.DOT);
		setForeground(Color.GRAY);
		setHorizontalAlignment(SwingConstants.CENTER);
		setFont(new Font("Tahoma", Font.BOLD, 14));		
	}

	public enum Status
	{
		NULL,
		NONE,
		OK,
		WARN,
		RISK, 
		HIGH_OK, 
		LOW_OK, 
		HIGH_RISK, 
		LOW_RISK,
		HIGH_LOW_OK,
		HIGH_LOW_RISK
	}

	public void setStatus(Status status)
	{
		if (status == Status.NONE)
		{
			setText(CharConstants.DOT);
			setForeground(Color.GRAY);
		}
		else if (status == Status.OK)
		{
			setText(CharConstants.DOT);
			setForeground(Styles.COLOR_TEXT_LONG);
		}
		else if (status == Status.WARN)
		{
			setText("!");
			setForeground(new Color(255, 204, 0));
		}
		else if (status == Status.RISK)
		{
			setText(CharConstants.DOT);
			setForeground(Styles.COLOR_TEXT_ERROR);
		}
		else if (status == Status.HIGH_OK)
		{
			setText(CharConstants.ARROW_UP);
			setForeground(Styles.COLOR_TEXT_LONG);
		}
		else if (status == Status.LOW_OK)
		{
			setText(CharConstants.ARROW_DOWN);
			setForeground(Styles.COLOR_TEXT_LONG);
		}
		else if (status == Status.HIGH_RISK)
		{
			setText(CharConstants.ARROW_UP);
			setForeground(Styles.COLOR_TEXT_ERROR);
		}
		else if (status == Status.LOW_RISK)
		{
			setText(CharConstants.ARROW_DOWN);
			setForeground(Styles.COLOR_TEXT_ERROR);
		}
		else if (status == Status.HIGH_LOW_OK)
		{
			setText(CharConstants.ARROW_UP + CharConstants.ARROW_DOWN);
			setForeground(Styles.COLOR_TEXT_LONG);
		}
		else if (status == Status.HIGH_LOW_RISK)
		{
			setText(CharConstants.ARROW_UP + CharConstants.ARROW_DOWN);
			setForeground(Styles.COLOR_TEXT_ERROR);
		}
		else
		{
			setText("");
			setForeground(null);
		}
	}

}
