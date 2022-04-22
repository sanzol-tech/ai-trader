package sanzol.app.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.service.PositionService;
import sanzol.lib.util.BeepUtils;

public class FrmPositions extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_PERIOD_MILLIS = 5000;
	private static boolean isOpen = false;
	private PositionService service;

	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JTextField txtError;

	private JLabel lblStopLoss;
	private JLabel lblTProfit;
	private JTextField txtStopLoss;
	private JTextField txtTProfit;
	private JCheckBox chkSLRearrang;
	private JCheckBox chkTPRearrang;
	
	private JButton btnSave;

	public FrmPositions()
	{
		initComponents();

		startTimer();
		isOpen = true;
		service = new PositionService();
	}

	private void initComponents() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		setMinimumSize(new Dimension(800, 600));
		setTitle(Constants.APP_NAME + " - Positions");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmTrader.class.getResource("/resources/upDown.png")));
	
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		txtError = new javax.swing.JTextField();
		txtError.setEditable(false);
		txtError.setForeground(new Color(255, 0, 0));
		
		lblStopLoss = new javax.swing.JLabel();
		lblStopLoss.setText("Stop loss");

		lblTProfit = new javax.swing.JLabel();
		lblTProfit.setText("T.Profit");
		
		txtStopLoss = new javax.swing.JTextField();
		txtStopLoss.setText("0");
		
		txtTProfit = new javax.swing.JTextField();
		txtTProfit.setText("0");

		chkSLRearrang = new javax.swing.JCheckBox();
		chkSLRearrang.setText("Rearrangement");

		chkTPRearrang = new javax.swing.JCheckBox();
		chkTPRearrang.setText("Rearrangement");

		btnSave = new javax.swing.JButton();
		btnSave.setText("Save change");
		btnSave.setOpaque(true);
		btnSave.setBackground(new Color(220, 220, 220));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(txtError, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblStopLoss)
						.addComponent(lblTProfit))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(txtTProfit)
						.addComponent(txtStopLoss, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
					.addGap(30)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(chkTPRearrang)
						.addComponent(chkSLRearrang))
					.addPreferredGap(ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
					.addComponent(btnSave))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStopLoss)
						.addComponent(txtStopLoss, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chkSLRearrang))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(lblTProfit)
						.addComponent(txtTProfit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chkTPRearrang))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtError, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);

		contentPane.setLayout(gl_contentPane);

		pack();

		// -----------------------------------------------------------------

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isOpen = false;
			}
		});

	}

	// ------------------------------------------------------------------------

	private void startTimer()
	{
		ActionListener taskPerformer1 = new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				getPosition();
			}
		};

		Timer timer1 = new Timer(DEFAULT_PERIOD_MILLIS, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void getPosition()
	{
		try
		{
			String text = service.getPosition().toStringPositions();

			if (service.wasChange())
			{
				BeepUtils.tone(5000, 300);
			}

			textArea.setText(text);
		}
		catch (Exception e)
		{
			ERROR(e);
		}
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
					FrmPositions frame = new FrmPositions();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	// ------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
	}

	public void ERROR(String msg)
	{
		txtError.setForeground(new Color(255, 0, 0));
		txtError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		txtError.setForeground(new Color(51, 107, 255));
		txtError.setText(" " + msg);
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		Application.initialize();
		Application.initializeUI();
		launch();
	}

}
