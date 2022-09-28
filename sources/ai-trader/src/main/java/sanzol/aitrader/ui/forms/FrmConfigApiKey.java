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
import javax.swing.border.EmptyBorder;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.config.PrivateConfig;
import sanzol.aitrader.ui.config.Styles;

public class FrmConfigApiKey extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmConfigApiKey myJFrame = null;

	private JPanel contentPane;
	private CtrlError ctrlError;

	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;

	private JButton btnSave;

	public FrmConfigApiKey()
	{
		initComponents();
		pageload();
	}

	private void pageload()
	{
		try
		{
			txtApiKey.setText(PrivateConfig.API_KEY);
			txtSecretKey.setText(PrivateConfig.SECRET_KEY);
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void initComponents()
	{
		setTitle(Constants.APP_NAME + " - ApiKey");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 230);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfigApiKey.class.getResource("/resources/key.png")));
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		btnSave = new JButton("Save");
		btnSave.setBounds(365, 124, 89, 23);
		contentPane.add(btnSave);

		JLabel lblApiKey = new JLabel("Api Key");
		lblApiKey.setBounds(10, 11, 80, 14);
		contentPane.add(lblApiKey);

		txtApiKey = new JPasswordField();
		txtApiKey.setText("");
		txtApiKey.setColumns(10);
		txtApiKey.setBounds(10, 30, 444, 20);
		contentPane.add(txtApiKey);

		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(10, 61, 80, 14);
		contentPane.add(lblSecretKey);

		txtSecretKey = new JPasswordField();
		txtSecretKey.setText("");
		txtSecretKey.setColumns(10);
		txtSecretKey.setBounds(10, 80, 444, 20);
		contentPane.add(txtSecretKey);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(10, 158, 444, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);

		ctrlError = new CtrlError();
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(ctrlError, BorderLayout.CENTER);

		// ---------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

	}

	private void save()
	{
		ctrlError.CLEAN();
		try
		{
			int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				String apiKey = String.valueOf(txtApiKey.getPassword());
				String secretKey = String.valueOf(txtSecretKey.getPassword());

				PrivateConfig.setKey(apiKey, secretKey);
				ctrlError.INFO("KEY SAVED. PLEASE RESTART !!!");
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
					myJFrame = new FrmConfigApiKey();
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
