package sanzol.aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.config.PrivateConfig;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.telegram.TelegramConfig;

public class FrmConfigSecure extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmConfigSecure myJFrame = null;

	private JPanel contentPane;
	private CtrlError ctrlError;

	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;

	private JPasswordField txtTelegramToken;
	private JTextField txtTelegramChatId;
	
	private JButton btnSaveSecret;
	private JButton btnSaveTelegram;

	public FrmConfigSecure()
	{
		setResizable(false);
		initComponents();
		pageload();
	}

	private void pageload()
	{
		try
		{
			txtApiKey.setText(PrivateConfig.getApiKey());
			txtSecretKey.setText(PrivateConfig.getSecretKey());
			txtTelegramToken.setText(TelegramConfig.getApiToken());
			txtTelegramChatId.setText(TelegramConfig.getChatId());
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void initComponents()
	{
		setTitle(Constants.APP_NAME + " - Secure");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 300);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfigSecure.class.getResource("/resources/key.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(10, 228, 444, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);
		
		ctrlError = new CtrlError();
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(ctrlError, BorderLayout.CENTER);
		
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 444, 208);
		contentPane.add(tabbedPane);
		
		JPanel pnlBinance = new JPanel();
		tabbedPane.addTab("Binance API", null, pnlBinance, null);
		pnlBinance.setLayout(null);
		
		JLabel lblApiKey = new JLabel("Api Key");
		lblApiKey.setBounds(10, 20, 80, 14);
		pnlBinance.add(lblApiKey);
		
		txtApiKey = new JPasswordField();
		txtApiKey.setBounds(10, 40, 420, 20);
		txtApiKey.setColumns(10);
		pnlBinance.add(txtApiKey);
		
		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(10, 70, 80, 14);
		pnlBinance.add(lblSecretKey);
		
		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(10, 90, 420, 20);
		txtSecretKey.setColumns(10);
		pnlBinance.add(txtSecretKey);
		
		btnSaveSecret = new JButton("Save");
		btnSaveSecret.setBounds(340, 142, 89, 23);
		pnlBinance.add(btnSaveSecret);

		JPanel pnlTelegram = new JPanel();
		tabbedPane.addTab("Telegram Bot API", null, pnlTelegram, null);
		pnlTelegram.setLayout(null);
		
		JLabel lblTelegramToken = new JLabel("Token");
		lblTelegramToken.setBounds(10, 20, 80, 14);
		pnlTelegram.add(lblTelegramToken);
		
		txtTelegramToken = new JPasswordField();
		txtTelegramToken.setBounds(10, 40, 420, 20);
		txtTelegramToken.setColumns(10);
		pnlTelegram.add(txtTelegramToken);
		
		JLabel lblTelegramChatId = new JLabel("Chat Id");
		lblTelegramChatId.setBounds(10, 70, 80, 14);
		pnlTelegram.add(lblTelegramChatId);

		txtTelegramChatId = new JTextField();
		txtTelegramChatId.setBounds(10, 90, 420, 20);
		txtTelegramChatId.setColumns(10);
		pnlTelegram.add(txtTelegramChatId);
		
		btnSaveTelegram = new JButton("Save");
		btnSaveTelegram.setBounds(340, 142, 89, 23);
		pnlTelegram.add(btnSaveTelegram);

		// ---------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});

		btnSaveSecret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSecret();
			}
		});

		btnSaveTelegram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTelegram();
			}
		});

	}

	private void saveSecret()
	{
		ctrlError.CLEAN();
		try
		{
			int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				PrivateConfig.setApiKey(String.valueOf(txtApiKey.getPassword()));
				PrivateConfig.setSecretKey(String.valueOf(txtSecretKey.getPassword()));
				PrivateConfig.save();

				ctrlError.INFO("KEY SAVED. PLEASE RESTART !!!");
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void saveTelegram()
	{
		ctrlError.CLEAN();
		try
		{
			int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				TelegramConfig.setApiToken(String.valueOf(txtTelegramToken.getPassword()));
				TelegramConfig.setChatId(String.valueOf(txtTelegramChatId.getText()));
				TelegramConfig.save();

				ctrlError.INFO("TELEGRAM API BOT FIELDS SAVED");
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
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
					myJFrame = new FrmConfigSecure();
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
