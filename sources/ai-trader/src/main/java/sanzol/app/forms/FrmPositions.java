package sanzol.app.forms;

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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.task.PositionService;

public class FrmPositions extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_PERIOD_MILLIS = 5000;
	private static boolean isOpen = false;

	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JTextField txtError;
	private JLabel lblTProfit;
	private JTextField txtTProfit;
	private JCheckBox chkTPRearrang;
	
	private JButton btnSave;
	private JLabel lblTPPercent;
	
	private JCheckBox chkIncludeOrders;

	public FrmPositions()
	{
		initComponents();

		startTimer();
		isOpen = true;
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
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);

		lblTProfit = new javax.swing.JLabel();
		lblTProfit.setText("T.Profit");
		
		txtTProfit = new javax.swing.JTextField();
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setText("0");

		chkTPRearrang = new javax.swing.JCheckBox();
		chkTPRearrang.setText("Rearrangement");

		btnSave = new javax.swing.JButton();
		btnSave.setText("Save change");
		btnSave.setOpaque(true);
		btnSave.setBackground(Styles.COLOR_BTN);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		lblTPPercent = new JLabel();
		lblTPPercent.setText("%");
		
		chkIncludeOrders = new JCheckBox("Include orders");
		chkIncludeOrders.setSelected(true);
		chkIncludeOrders.setHorizontalAlignment(SwingConstants.RIGHT);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(txtError, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addComponent(lblTProfit, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtTProfit, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTPPercent, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chkTPRearrang, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSave)
					.addPreferredGap(ComponentPlacement.RELATED, 297, Short.MAX_VALUE)
					.addComponent(chkIncludeOrders))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTProfit)
						.addComponent(txtTProfit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTPPercent)
						.addComponent(chkTPRearrang)
						.addComponent(btnSave)
						.addComponent(chkIncludeOrders))
					.addGap(18)
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
			String text = PositionService.toStringPositions(chkIncludeOrders.isSelected());
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

	// -----------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
	}

	public void ERROR(String msg)
	{
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		txtError.setForeground(Styles.COLOR_TEXT_INFO);
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
