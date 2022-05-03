package sanzol.app.forms;

import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Styles;
import sanzol.app.task.SignalService;

public class FrmPointsMonitor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_PERIOD_MILLIS = 2000;
	private static final String TITLE = "Points monitor";

	private static boolean isOpen = false;

	private Timer timer1;
	
	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel lblError;
	
	private JButton btnCopy;

	public FrmPointsMonitor()
	{
		initComponents();

		startTimer();
		isOpen = true;
	}

	private void initComponents() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 578);
		setMinimumSize(new Dimension(800, 600));
		setTitle(TITLE);
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

		btnCopy = new javax.swing.JButton();
		btnCopy.setText("Copy to clipboard");
		btnCopy.setOpaque(true);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 0, 5, 5));
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		lblError = new JLabel();
		lblError.setBorder(new EmptyBorder(5, 5, 5, 5));
		lblError.setMinimumSize(new Dimension(100, 20));
		pnlBottom.add(lblError);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
						.addComponent(pnlBottom, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
						.addComponent(btnCopy, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCopy)
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
				timer1.stop();
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

		timer1 = new Timer(DEFAULT_PERIOD_MILLIS, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void getSignals()
	{
		try
		{
			textArea.setText(SignalService.toStringStatus());
			INFO("Last modification: " + SignalService.getModified());
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
					FrmPointsMonitor frame = new FrmPointsMonitor();
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
