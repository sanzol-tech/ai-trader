package sanzol.app.forms;

import java.awt.BorderLayout;
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
import javax.swing.UIManager;
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

	private JLabel lblError;
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
		setBounds(100, 100, 804, 614);
		setMinimumSize(new Dimension(800, 600));
		setTitle(Constants.APP_NAME + " - Positions");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmTrader.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);
	
		textArea = new JTextArea();
		textArea.setBackground(Styles.COLOR_TEXT_AREA_BG);
		textArea.setForeground(Styles.COLOR_TEXT_AREA_FG);
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(UIManager.getBorder("TextField.border"));

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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		lblTPPercent = new JLabel();
		lblTPPercent.setText("%");
		
		chkIncludeOrders = new JCheckBox("Include orders");
		chkIncludeOrders.setSelected(true);
		chkIncludeOrders.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		lblError = new JLabel();
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		lblError.setMinimumSize(new Dimension(100, 20));
		pnlBottom.add(lblError);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(lblTProfit, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(txtTProfit, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblTPPercent, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chkTPRearrang, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
							.addComponent(chkIncludeOrders))
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
						.addComponent(pnlBottom, GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(chkIncludeOrders)
						.addComponent(lblTProfit)
						.addComponent(btnSave)
						.addComponent(chkTPRearrang)
						.addComponent(lblTPPercent)
						.addComponent(txtTProfit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pnlBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4))
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
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveChanges();
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

	private void saveChanges()
	{
		INFO("Under construction");
	}

	// -----------------------------------------------------------------------
	
	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
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
