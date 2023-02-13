package aitrader.ui.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.log.LogAppenders;
import aitrader.util.log.LogService;
import aitrader.util.observable.Handler;

public class FrmLogs extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME + " - Logs";

	private static FrmLogs myJFrame = null;

	private CtrlError ctrlError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JRadioButton rbUI;
	private JRadioButton rbCore;
	private JRadioButton rbApi;

	private JTextArea txtResult;
	
	private Handler<Void> logServiceHandler1 = e -> { onLogUpdate(); };
	private Handler<Void> logServiceHandler2 = e -> { onLogUpdate(); };
	private Handler<Void> logServiceHandler3 = e -> { onLogUpdate(); };

	public FrmLogs() throws Exception
	{
		initComponents();

		onLogUpdate();
		
		LogService logAPI = LogAppenders.getAppender("API");
		LogService logCORE = LogAppenders.getAppender("CORE");
		LogService logUI = LogAppenders.getAppender("UI");

		if (logAPI == null || logCORE == null || logUI == null)
		{
			throw new Exception("Not all logs are ready yet");
		}

		logAPI.attachObserver(logServiceHandler1);
		logCORE.attachObserver(logServiceHandler2);
		logUI.attachObserver(logServiceHandler3);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmLogs.class.getResource("/resources/bug.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		ctrlError = new CtrlError();

        txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(txtResult);

		// --------------------------------------------------------------------
        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContentLayout.setHorizontalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlContentLayout.setVerticalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlContent.setLayout(pnlContentLayout);

		rbUI = new JRadioButton(" UI");
		rbUI.setSelected(true);
		rbCore = new JRadioButton(" CORE");
		rbApi = new JRadioButton(" API");

        ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbUI);
		bg1.add(rbCore);
		bg1.add(rbApi);
		
		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(rbUI)
					.addGap(18)
					.addComponent(rbCore)
					.addGap(18)
					.addComponent(rbApi)
					.addContainerGap(579, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rbCore)
						.addComponent(rbApi)
						.addComponent(rbUI))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		pnlTopBar.setLayout(pnlTopBarLayout);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlTopBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);

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
				LogAppenders.getAppender("API").deattachObserver(logServiceHandler1);
				LogAppenders.getAppender("CORE").deattachObserver(logServiceHandler2);
				LogAppenders.getAppender("UI").deattachObserver(logServiceHandler3);
				myJFrame = null;
			}
		});

		rbUI.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onLogUpdate();
				}
			}
		});
		rbCore.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onLogUpdate();
				}
			}
		});
		rbApi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onLogUpdate();
				}
			}
		});		

	}

	// --------------------------------------------------------------------

	public void onLogUpdate()
	{
		try
		{
			if (rbUI.isSelected())
				txtResult.setText(LogAppenders.getAppender("UI").getLOG());
			else if (rbCore.isSelected())
				txtResult.setText(LogAppenders.getAppender("CORE").getLOG());
			else
				txtResult.setText(LogAppenders.getAppender("API").getLOG());
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

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
					myJFrame = new FrmLogs();
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
