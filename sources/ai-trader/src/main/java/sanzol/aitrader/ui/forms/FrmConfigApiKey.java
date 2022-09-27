package sanzol.aitrader.ui.forms;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.config.PrivateConfig;

public class FrmConfigApiKey extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmConfigApiKey myJFrame = null;

	private JPanel contentPane;
	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;

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
			// ERROR(e);
		}
	}

	private void initComponents()
	{	
		setResizable(false);
		setTitle(Constants.APP_NAME + " - ApiKey");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 210);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfigApiKey.class.getResource("/resources/key.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(365, 137, 89, 23);
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

	@SuppressWarnings("deprecation")
	private void save()
	{
		try
		{
			int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				PrivateConfig.setKey(txtApiKey.getText(), txtSecretKey.getText());
				// INFO("KEY SAVED. PLEASE RESTART !!!");
			}
		}
		catch (IOException e)
		{
			// ERROR(e);
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

	public static void main(String[] args)
	{
		launch();
	}
}
