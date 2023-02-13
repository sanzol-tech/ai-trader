package aitrader.ui.forms;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import aitrader.core.config.CoreConfig;
import aitrader.core.service.position.BalanceService;
import aitrader.core.service.position.PositionsBot;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.Convert;
import aitrader.util.observable.Handler;

public class FrmBot extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME + " - BOT";

	private static FrmBot myJFrame = null;

	private CtrlError ctrlError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JScrollPane scrollResult;
	private JLabel lblBot;
	private JLabel lblTakeProfit;
	private JPanel pnlBot;
	private JCheckBox chkAuto1;
	private JCheckBox chkAuto2;
	private JButton btnSave;
	private JPanel pnlTProfit;
	private JCheckBox chkTPRearrangement;
	private JTextField txtTProfit;
	private JButton btnTPSave;
	private JLabel lblStopLoss;
	private JPanel pnlStopLoss;
	private JCheckBox chkSLRearrangement;
	private JTextField txtSlUsd;
	private JButton btnSLSave;
	private JTextArea txtResult;
	private JLabel lblTpPercent;
	private JLabel lblSlUsd;

	private Handler<Void> botServiceHandler = e -> { onBotUpdate(); };
	
	public FrmBot()
	{
		initComponents();
		pageload();
		txtResult.setText(PositionsBot.getLOG());
		PositionsBot.attachObserver(botServiceHandler);
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmBot.class.getResource("/resources/bot.png")));
		setLocationRelativeTo(null);
		setResizable(false);

		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		ctrlError = new CtrlError();

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);

		scrollResult = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollResult.setBorder(UIManager.getBorder("TextField.border"));
		scrollResult.setBounds(10, 11, 770, 282);
		pnlContent.add(scrollResult);

		txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
		txtResult.setBorder(new EmptyBorder(10, 10, 10, 10));
		scrollResult.setViewportView(txtResult);

		lblBot = new JLabel();
		lblBot.setText("Bot");
		lblBot.setBounds(10, 304, 100, 14);
		pnlContent.add(lblBot);

		lblTakeProfit = new JLabel();
		lblTakeProfit.setText("Take profit");
		lblTakeProfit.setBounds(400, 304, 100, 14);
		pnlContent.add(lblTakeProfit);

		pnlBot = new JPanel();
		pnlBot.setLayout(null);
		pnlBot.setBorder(UIManager.getBorder("TextField.border"));
		pnlBot.setBounds(10, 322, 380, 130);
		pnlContent.add(pnlBot);

		chkAuto1 = new JCheckBox("Auto star position from signal");
		chkAuto1.setBounds(22, 22, 280, 23);
		pnlBot.add(chkAuto1);

		chkAuto2 = new JCheckBox("Auto post grid orders when position filled");
		chkAuto2.setBounds(22, 48, 280, 23);
		pnlBot.add(chkAuto2);

		btnSave = new JButton("SAVE");
		btnSave.setOpaque(true);
		btnSave.setBounds(290, 99, 80, 20);
		pnlBot.add(btnSave);

		pnlTProfit = new JPanel();
		pnlTProfit.setLayout(null);
		pnlTProfit.setBorder(UIManager.getBorder("TextField.border"));
		pnlTProfit.setBounds(400, 322, 380, 50);
		pnlContent.add(pnlTProfit);

		chkTPRearrangement = new JCheckBox();
		chkTPRearrangement.setText("Rearrangement");
		chkTPRearrangement.setSelected(false);
		chkTPRearrangement.setBounds(16, 14, 128, 23);
		pnlTProfit.add(chkTPRearrangement);

		txtTProfit = new JTextField();
		txtTProfit.setText("0");
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setBounds(156, 16, 68, 20);
		pnlTProfit.add(txtTProfit);

		btnTPSave = new JButton("SAVE");
		btnTPSave.setOpaque(true);
		btnTPSave.setBounds(290, 15, 80, 20);
		pnlTProfit.add(btnTPSave);

		lblTpPercent = new JLabel("%");
		lblTpPercent.setBounds(230, 18, 20, 14);
		pnlTProfit.add(lblTpPercent);

		lblStopLoss = new JLabel();
		lblStopLoss.setText("Stop loss");
		lblStopLoss.setBounds(400, 383, 100, 14);
		pnlContent.add(lblStopLoss);

		pnlStopLoss = new JPanel();
		pnlStopLoss.setLayout(null);
		pnlStopLoss.setBorder(UIManager.getBorder("TextField.border"));
		pnlStopLoss.setBounds(400, 401, 380, 50);
		pnlContent.add(pnlStopLoss);

		chkSLRearrangement = new JCheckBox();
		chkSLRearrangement.setText("Rearrangement");
		chkSLRearrangement.setSelected(false);
		chkSLRearrangement.setBounds(16, 14, 128, 23);
		pnlStopLoss.add(chkSLRearrangement);

		txtSlUsd = new JTextField();
		txtSlUsd.setText("0");
		txtSlUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSlUsd.setBounds(156, 16, 68, 20);
		pnlStopLoss.add(txtSlUsd);

		btnSLSave = new JButton("SAVE");
		btnSLSave.setOpaque(true);
		btnSLSave.setBounds(290, 15, 80, 20);
		pnlStopLoss.add(btnSLSave);

		lblSlUsd = new JLabel("usd");
		lblSlUsd.setBounds(230, 18, 38, 14);
		pnlStopLoss.add(lblSlUsd);

		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(7)
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
					.addGap(7))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PositionsBot.deattachObserver(botServiceHandler);
				myJFrame = null;
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveBotConfig();
			}
		});

		btnTPSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTPConfig();
			}
		});

		btnSLSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSLConfig();
			}
		});

	}

	private void pageload()
	{
		try
		{
			chkTPRearrangement.setSelected(PositionsBot.isTpRearrangement());
			if (PositionsBot.isTpRearrangement())
				txtTProfit.setText(Convert.toStrPercent(PositionsBot.getTpPercent()));
			else
				txtTProfit.setText(Convert.toStrPercent(CoreConfig.getTakeProfit()));

			chkSLRearrangement.setSelected(PositionsBot.isSlRearrangement());
			if (PositionsBot.isSlRearrangement())
				txtSlUsd.setText(PositionsBot.getSlUsd().toPlainString());
			else
				txtSlUsd.setText("0");
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	private void saveBotConfig()
	{
		PositionsBot.info("BOT under construction !");
		ctrlError.INFO("BOT under construction !");
	}

	private void saveTPConfig()
	{
		try
		{
			BigDecimal value = Convert.strPercentToBigDecimal(txtTProfit.getText());
			if (value.doubleValue() <= 0 || value.doubleValue() > 100)
			{
				ctrlError.ERROR("TP rearrangement is not valid");
				return;
			}
	
			if (chkTPRearrangement.isSelected())
				PositionsBot.tpRearrangementOn(value);
			else
				PositionsBot.tpRearrangementOff();
	
			ctrlError.INFO("TP rearrangement updated !");
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void saveSLConfig()
	{
		try
		{
			BigDecimal maxValue = BalanceService.getAccountBalance().getBalance();
			BigDecimal value = Convert.toBigDecimal(txtSlUsd.getText());
			if (value.doubleValue() <= 0 || value.doubleValue() > maxValue.doubleValue())
			{
				ctrlError.ERROR("SL rearrangement is not valid");
				return;
			}
	
			if (chkSLRearrangement.isSelected())
				PositionsBot.slRearrangementOn(value);
			else
				PositionsBot.slRearrangementOff();

			ctrlError.INFO("SL rearrangement updated !");
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	private void onBotUpdate()
	{
		txtResult.setText(PositionsBot.getLOG());
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
					myJFrame = new FrmBot();
					myJFrame.setVisible(true);
				}
				catch (Exception e)
				{
					UILog.error(e);
				}
			}
		});
	}

}
