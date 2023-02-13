package aitrader.ui.forms;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import aitrader.core.config.CoreApp;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;

public class FrmServices extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmServices myJFrame = null;

	private JPanel contentPane;

	private JCheckBox chkExchange;
	private JCheckBox chkPrices;
	private JCheckBox chkTechnical;
	private JCheckBox chkDepthCache;
	private JCheckBox chkSignals;
	private JCheckBox chkAlerts;
	private JCheckBox chkBalance;
	private JCheckBox chkPositions;
	private JCheckBox chkSymbolInfo;
	private JCheckBox chkUserData;

	public FrmServices()
	{
		setResizable(false);
		setTitle(UIConstants.APP_NAME + " - Restart");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 280);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmServices.class.getResource("/resources/wrench.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		chkSymbolInfo = new JCheckBox("Symbol Info");
		chkSymbolInfo.setSelected(true);
		chkSymbolInfo.setBounds(33, 26, 97, 23);
		contentPane.add(chkSymbolInfo);
		
		chkExchange = new JCheckBox("Exchange info");
		chkExchange.setEnabled(false);
		chkExchange.setSelected(true);
		chkExchange.setBounds(44, 56, 97, 23);
		contentPane.add(chkExchange);
		
		chkPrices = new JCheckBox("Ticker info");
		chkPrices.setEnabled(false);
		chkPrices.setSelected(true);
		chkPrices.setBounds(44, 82, 97, 23);
		contentPane.add(chkPrices);
		
		chkTechnical = new JCheckBox("Technical info");
		chkTechnical.setEnabled(false);
		chkTechnical.setSelected(true);
		chkTechnical.setBounds(44, 108, 97, 23);
		contentPane.add(chkTechnical);

		chkDepthCache = new JCheckBox("Depth Cache");
		chkDepthCache.setSelected(true);
		chkDepthCache.setBounds(33, 142, 97, 23);
		contentPane.add(chkDepthCache);

		chkSignals = new JCheckBox("Signals");
		chkSignals.setSelected(true);
		chkSignals.setBounds(188, 26, 97, 23);
		contentPane.add(chkSignals);

		chkAlerts = new JCheckBox("Alerts");
		chkAlerts.setSelected(true);
		chkAlerts.setBounds(188, 56, 97, 23);
		contentPane.add(chkAlerts);
		
		chkUserData = new JCheckBox("User Data");
		chkUserData.setSelected(true);
		chkUserData.setBounds(188, 86, 97, 23);
		contentPane.add(chkUserData);
		
		chkBalance = new JCheckBox("Balance");
		chkBalance.setEnabled(false);
		chkBalance.setSelected(true);
		chkBalance.setBounds(199, 116, 97, 23);
		contentPane.add(chkBalance);

		chkPositions = new JCheckBox("Positions");
		chkPositions.setEnabled(false);
		chkPositions.setSelected(true);
		chkPositions.setBounds(199, 142, 97, 23);
		contentPane.add(chkPositions);

		JButton btnRestart = new JButton("Restart");
		btnRestart.setBounds(107, 196, 89, 23);
		contentPane.add(btnRestart);

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});

		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});

	}

	private void restart()
	{
		int resultOption = JOptionPane.showConfirmDialog(null, "Do you like to restart services ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resultOption == 0)
		{
			try
			{
				CoreApp.servicesRestart(chkSymbolInfo.isSelected(), chkDepthCache.isSelected(), chkSignals.isSelected(), chkAlerts.isSelected(), chkUserData.isSelected());
			}
			catch (Exception ex)
			{
				UILog.error(ex);
			}

			setVisible(false);
			myJFrame = null;
			dispose();
		}
	}

	public static void launch()
	{
		if (myJFrame != null)
		{
			myJFrame.toFront();
			myJFrame.setState(Frame.NORMAL);
			return;
		}

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					myJFrame = new FrmServices();
					myJFrame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
