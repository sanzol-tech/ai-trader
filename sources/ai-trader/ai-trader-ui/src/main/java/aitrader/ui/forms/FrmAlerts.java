package aitrader.ui.forms;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import aitrader.core.model.Alert;
import aitrader.core.model.Symbol;
import aitrader.core.model.enums.OrderSide;
import aitrader.core.service.signals.AlertService;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.DateTimeUtils;
import aitrader.util.observable.Handler;

public class FrmAlerts extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME + " - Alerts";

	private static FrmAlerts myJFrame = null;

	private DefaultTableModel tableModelShort;
	private DefaultTableModel tableModelLong;

	private CtrlError ctrlError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JScrollPane scrollShort;
	private JTable tableShort;
	private JScrollPane scrollLong;
	private JTable tableLong;

	private Handler<Alert> alertServiceHandler = e -> { onAlertsUptade(e); };
	
	public FrmAlerts()
	{
		initComponents();
		
		createTable();

		AlertService.attachObserver(alertServiceHandler);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmAlerts.class.getResource("/resources/bell.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		ctrlError = new CtrlError();

		JButton btnAdd = new JButton("Add");

        scrollShort = new JScrollPane();
        scrollLong = new JScrollPane();

        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContentLayout.setHorizontalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(pnlContentLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(scrollShort, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        				.addComponent(scrollLong, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE))
        			.addContainerGap())
        );
        pnlContentLayout.setVerticalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollShort, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(scrollLong, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
        			.addContainerGap())
        );

        tableLong = new JTable();
        tableLong.setForeground(Styles.COLOR_TEXT_LONG);
        tableLong.setShowVerticalLines(true);
        tableLong.setShowHorizontalLines(true);
        tableLong.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        scrollLong.setViewportView(tableLong);

        tableShort = new JTable();
        tableShort.setForeground(Styles.COLOR_TEXT_SHORT);
        tableShort.setShowVerticalLines(true);
        tableShort.setShowHorizontalLines(true);
        tableShort.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        scrollShort.setViewportView(tableShort);

        pnlContent.setLayout(pnlContentLayout);

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAdd)
					.addContainerGap(681, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAdd)
					.addContainerGap(13, Short.MAX_VALUE))
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

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				AlertService.deattachObserver(alertServiceHandler);
				myJFrame = null;
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmAlertAdd.launch(null);
			}
		});

	}

	// --------------------------------------------------------------------

	private void onAlertsUptade(Alert alert)
	{
		loadTable();
	}

	// --------------------------------------------------------------------

	private class TableModel extends DefaultTableModel
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	}

    private void createTable()
    {
		try
		{
	    	tableModelShort = new TableModel();
	    	tableModelShort.addColumn("SYMBOL");
	    	tableModelShort.addColumn("STATUS");
	    	tableModelShort.addColumn("ALERT");
	    	tableModelShort.addColumn("LIMIT");
	    	tableModelShort.addColumn("DIST %");
	    	tableModelShort.addColumn("QTY TYPE");
	    	tableModelShort.addColumn("QTY");
	    	tableModelShort.addColumn("TIME OUT");
	    	tableShort.setModel(tableModelShort);

	    	tableModelLong = new TableModel();
	    	tableModelLong.addColumn("SYMBOL");
	    	tableModelLong.addColumn("STATUS");
	    	tableModelLong.addColumn("ALERT");
	    	tableModelLong.addColumn("LIMIT");
	    	tableModelLong.addColumn("DIST %");
	    	tableModelLong.addColumn("QTY TYPE");
	    	tableModelLong.addColumn("QTY");
	    	tableModelLong.addColumn("TIME OUT");
			tableLong.setModel(tableModelLong);

	        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

	        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

	        tableShort.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	        tableShort.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

	        tableShort.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

	        tableLong.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);

	        tableLong.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
    }

    private void loadTable_() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
    	tableModelShort.setRowCount(0);

		List<Alert> lstShortAlers = AlertService.getAlerts(OrderSide.SELL.name());
		for (Alert entry : lstShortAlers)
		{
			Symbol symbol = entry.getSymbol();
        	Object row[] = {
    				entry.getSymbol(),
    				entry.isAlerted() ? "ALERTED" : "",
					symbol.priceToStr(entry.getAlertPrice()),
					symbol.priceToStr(entry.getLimitPrice()),
					String.format(Locale.US, "%.2f %%", entry.getLimitDistance()),
					entry.getQuantityType() != null ? entry.getQuantityType().name() : null,
					entry.getQuantity() != null ? symbol.qtyToStr(entry.getQuantity()) : null,
    				entry.getExpirationAt() != null ? DateTimeUtils.millisToReadable(entry.getExpirationAt() - System.currentTimeMillis()) : "---"
        		};

        	tableModelShort.addRow(row);
		}

    	tableModelLong.setRowCount(0);

    	List<Alert> lstLongAlers = AlertService.getAlerts(OrderSide.BUY.name());
		for (Alert entry : lstLongAlers)
		{
			Symbol symbol = entry.getSymbol();
        	Object row[] = {
    				entry.getSymbol(),
    				entry.isAlerted() ? "ALERTED" : "",
					symbol.priceToStr(entry.getAlertPrice()),
					symbol.priceToStr(entry.getLimitPrice()),
					String.format(Locale.US, "%.2f %%", entry.getLimitDistance()),
					entry.getQuantityType() != null ? entry.getQuantityType().name() : null,
					entry.getQuantity() != null ? symbol.qtyToStr(entry.getQuantity()) : null,
    				entry.getExpirationAt() != null ? DateTimeUtils.millisToReadable(entry.getExpirationAt() - System.currentTimeMillis()) : "---"
        		};

        	tableModelLong.addRow(row);
		}
	}

	private void loadTable()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			loadTable_();
			tableModelShort.fireTableDataChanged();
			tableModelLong.fireTableDataChanged();
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
					myJFrame = new FrmAlerts();
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
