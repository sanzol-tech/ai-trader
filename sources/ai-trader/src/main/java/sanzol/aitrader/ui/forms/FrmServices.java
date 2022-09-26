package sanzol.aitrader.ui.forms;

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

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.config.ServerApp;
import sanzol.util.log.LogService;

public class FrmServices extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmServices myJFrame = null;

	private JPanel contentPane;

	private JCheckBox chkPrices;
	private JCheckBox chkLast30mBtc;
	private JCheckBox chkDepthCache;
	private JCheckBox chkSignals;
	private JCheckBox chkAlerts;
	private JCheckBox chkBalance;
	private JCheckBox chkPositions;

	public FrmServices()
	{
		setTitle(Constants.APP_NAME + " - Restart");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 250);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmBot.class.getResource("/resources/wrench.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		chkPrices = new JCheckBox("Prices");
		chkPrices.setSelected(true);
		chkPrices.setBounds(35, 27, 97, 23);
		contentPane.add(chkPrices);

		chkLast30mBtc = new JCheckBox("Last 30m / Btc");
		chkLast30mBtc.setSelected(true);
		chkLast30mBtc.setBounds(35, 57, 97, 23);
		contentPane.add(chkLast30mBtc);

		chkDepthCache = new JCheckBox("Depth Cache");
		chkDepthCache.setSelected(true);
		chkDepthCache.setBounds(35, 87, 97, 23);
		contentPane.add(chkDepthCache);

		chkSignals = new JCheckBox("Signals");
		chkSignals.setSelected(true);
		chkSignals.setBounds(172, 27, 97, 23);
		contentPane.add(chkSignals);

		chkAlerts = new JCheckBox("Alerts");
		chkAlerts.setSelected(true);
		chkAlerts.setBounds(172, 57, 97, 23);
		contentPane.add(chkAlerts);

		chkBalance = new JCheckBox("Balance");
		chkBalance.setSelected(true);
		chkBalance.setBounds(172, 87, 97, 23);
		contentPane.add(chkBalance);

		chkPositions = new JCheckBox("Positions");
		chkPositions.setSelected(true);
		chkPositions.setBounds(172, 117, 97, 23);
		contentPane.add(chkPositions);

		JButton btnRestart = new JButton("Restart");
		btnRestart.setBounds(106, 177, 89, 23);
		contentPane.add(btnRestart);

		JButton btnOnOff = new JButton("On / Off");
		btnOnOff.setBounds(205, 177, 89, 23);
		contentPane.add(btnOnOff);

		// ---------------------------------------------------------------------

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

		btnOnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOff();
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
				ServerApp.servicesRestart(chkPrices.isSelected(), chkLast30mBtc.isSelected(), chkDepthCache.isSelected(), chkSignals.isSelected(), chkAlerts.isSelected(), chkBalance.isSelected(), chkPositions.isSelected());
			}
			catch (Exception ex)
			{
				LogService.error(ex);
			}

			setVisible(false);
			myJFrame = null;
			dispose();
		}
	}

	private void onOff()
	{
		//
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

	public static void main(String[] args)
	{
		launch();
	}
}
