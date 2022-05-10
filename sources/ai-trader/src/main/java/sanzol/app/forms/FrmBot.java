package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.listener.BotListener;
import sanzol.app.task.BotService;
import sanzol.lib.util.ExceptionUtils;

public class FrmBot extends JFrame implements BotListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - BOT";

	private static boolean isOpen = false;

	private JPanel contentPane;
	private JLabel lblError;

	private JButton btnSave;
	private JButton btnTPSave;
	private JButton btnSLSave;

	private JCheckBox chkAuto1;
	private JCheckBox chkAuto2;
	private JCheckBox chkTPRearrangement;
	private JCheckBox chkSLRearrangement;

	private JScrollPane scrollResult;
	private JTextArea txtResult;

	private JTextField txtTPPercent;
	private JTextField txtSLPercent;

	public FrmBot()
	{
		initComponents();
		BotService.attachRefreshObserver(this);
		isOpen = true;
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 600);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/bot.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(30, 532, 770, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);
		
		lblError = new JLabel();
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(lblError, BorderLayout.CENTER);
		
		scrollResult = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollResult.setBounds(30, 31, 770, 303);
		scrollResult.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollResult);

		txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setEditable(false);
		scrollResult.setViewportView(txtResult);
		
		JLabel lblBot = new JLabel();
		lblBot.setText("Bot");
		lblBot.setBounds(30, 359, 100, 14);
		contentPane.add(lblBot);

		JPanel pnlBot = new JPanel();
		pnlBot.setBorder(UIManager.getBorder("TextField.border"));
		pnlBot.setBounds(30, 377, 375, 130);
		pnlBot.setLayout(null);
		contentPane.add(pnlBot);
		
		chkAuto1 = new JCheckBox("Auto star position from signal");
		chkAuto1.setBounds(22, 22, 280, 23);
		pnlBot.add(chkAuto1);
		
		chkAuto2 = new JCheckBox("Auto post grid orders when position filled");
		chkAuto2.setBounds(22, 48, 280, 23);
		pnlBot.add(chkAuto2);
		
		btnSave = new JButton("SAVE");
		btnSave.setOpaque(true);
		btnSave.setBounds(280, 96, 80, 20);
		pnlBot.add(btnSave);
		
		JLabel lblTakeProfit = new JLabel();
		lblTakeProfit.setBounds(425, 359, 100, 14);
		contentPane.add(lblTakeProfit);
		lblTakeProfit.setText("Take profit");

		JPanel pnlTProfit = new JPanel();
		pnlTProfit.setBorder(UIManager.getBorder("TextField.border"));
		pnlTProfit.setLayout(null);
		pnlTProfit.setBounds(425, 377, 375, 50);
		contentPane.add(pnlTProfit);
		
		chkTPRearrangement = new JCheckBox();
		chkTPRearrangement.setText("Rearrangement %");
		chkTPRearrangement.setSelected(false);
		chkTPRearrangement.setBounds(16, 14, 128, 23);
		pnlTProfit.add(chkTPRearrangement);

		txtTPPercent = new JTextField();
		txtTPPercent.setText("0");
		txtTPPercent.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPPercent.setBounds(156, 16, 68, 20);
		pnlTProfit.add(txtTPPercent);
	
		btnTPSave = new JButton("SAVE");
		btnTPSave.setOpaque(true);
		btnTPSave.setBounds(280, 16, 80, 20);
		pnlTProfit.add(btnTPSave);
		
		JLabel lblStopLoss = new JLabel();
		lblStopLoss.setText("Stop loss");
		lblStopLoss.setBounds(425, 438, 100, 14);
		contentPane.add(lblStopLoss);

		JPanel pnlStopLoss = new JPanel();
		pnlStopLoss.setLayout(null);
		pnlStopLoss.setBorder(UIManager.getBorder("TextField.border"));
		pnlStopLoss.setBounds(425, 456, 375, 50);
		contentPane.add(pnlStopLoss);
		
		chkSLRearrangement = new JCheckBox();
		chkSLRearrangement.setText("Rearrangement %");
		chkSLRearrangement.setSelected(false);
		chkSLRearrangement.setBounds(16, 14, 128, 23);
		pnlStopLoss.add(chkSLRearrangement);
		
		txtSLPercent = new JTextField();
		txtSLPercent.setText("0");
		txtSLPercent.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLPercent.setBounds(156, 16, 68, 20);
		pnlStopLoss.add(txtSLPercent);
		
		btnSLSave = new JButton("SAVE");
		btnSLSave.setOpaque(true);
		btnSLSave.setBounds(280, 16, 80, 20);
		pnlStopLoss.add(btnSLSave);

		// ---------------------------------------------------------------------

		FrmBot thisFrm = this;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				BotService.deattachRefreshObserver(thisFrm);
				isOpen = false;
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

	// ----------------------------------------------------------------------------------

	private void saveBotConfig()
	{
		BotService.info("BOT", "Under construction");
		INFO("Under construction !");
	}

	private void saveTPConfig()
	{
		BotService.setTpRearrangement(chkTPRearrangement.isSelected());
		INFO("TP rearrangement updated !");
	}

	private void saveSLConfig()
	{
		BotService.setSlRearrangement(chkSLRearrangement.isSelected());		
		INFO("Under construction !");
	}

	// ----------------------------------------------------------------------------------

	@Override
	public void onBotUpdate()
	{
		txtResult.setText(BotService.getLOG());
	}

	public static void launch()
	{
		if (isOpen)
		{
			return;
		}

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmBot frame = new FrmBot();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	// ----------------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(ExceptionUtils.getMessage(e));
	}

	public void ERROR(String msg)
	{
		lblError.setForeground(Styles.COLOR_TEXT_ERROR);
		lblError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		lblError.setForeground(Styles.COLOR_TEXT_INFO);
		lblError.setText(" " + msg);
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		Application.initialize();
		Application.initializeUI();
		launch();
	}

}
