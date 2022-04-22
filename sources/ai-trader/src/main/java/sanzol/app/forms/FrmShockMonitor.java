package sanzol.app.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.task.SignalService;

public class FrmShockMonitor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_PERIOD_MILLIS = 2000;
	private static final String TITLE = "Shock monitor";

	private static boolean isOpen = false;

	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JTextField txtError;
	
	private JButton btnCopy;

	public FrmShockMonitor()
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
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmTrader.class.getResource("/resources/monitor.png")));
	
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		txtError = new javax.swing.JTextField();
		txtError.setEditable(false);
		txtError.setForeground(new Color(255, 0, 0));

		btnCopy = new javax.swing.JButton();
		btnCopy.setText("Copy toclipboard");
		btnCopy.setOpaque(true);
		btnCopy.setBackground(new Color(220, 220, 220));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(txtError, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addPreferredGap(ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
					.addComponent(btnCopy))
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCopy))
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

		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(textArea.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
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
				getSignals();
			}
		};

		Timer timer1 = new Timer(DEFAULT_PERIOD_MILLIS, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void getSignals()
	{
		try
		{
			textArea.setText(SignalService.toStringStatus());
			setTitle(TITLE + " - " + SignalService.getModified());
		}
		catch (Exception e)
		{
			ERROR(e);
			e.printStackTrace();
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
					FrmShockMonitor frame = new FrmShockMonitor();
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
