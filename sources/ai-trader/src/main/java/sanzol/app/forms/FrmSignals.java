package sanzol.app.forms;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.Price;
import sanzol.app.model.Signal;
import sanzol.app.service.LogService;
import sanzol.app.service.SignalListener;
import sanzol.app.service.SignalService;
import sanzol.app.service.Symbol;
import sanzol.app.util.PriceUtil;
import sanzol.lib.util.ExceptionUtils;

public class FrmSignals extends JFrame implements SignalListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Signals";

	private static FrmSignals myJFrame = null;
	
    private DefaultTableModel tableModelShort;
    private DefaultTableModel tableModelLong;

    private JLabel lblError;
	
	private JButton btnRestart;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;
	private JTextField txtWithdrawal;
	private JTable tableShort;
	private JScrollPane scrollLong;
	private JTable tableLong;

	public FrmSignals()
	{
		initComponents();
		
		createTable();
		
		SignalService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmSignals.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);
		btnRestart = new JButton();
		btnRestart.setText("RESTART");

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();
        
        JScrollPane scrollShort = new JScrollPane();
        
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
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnRestart, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(620, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(btnRestart)
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
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(7)
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
					.addGap(7))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				SignalService.deattachRefreshObserver(myJFrame);
				myJFrame = null;
			}
		});

        tableShort.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int index = table.getSelectedRow();
					tradeFromSignal("SHORT", index);
                }
            }
        });

        tableLong.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int index = table.getSelectedRow();
					tradeFromSignal("LONG", index);
                }
            }
        });
		
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});

	}

	// ------------------------------------------------------------------------

	private void tradeFromSignal(String type, int index)
	{
		try
		{
			boolean isBotMode = false;

			if ("SHORT".equals(type))
			{
				Symbol symbol = (Symbol) tableShort.getValueAt(index, 0);
				Price shShort = new Price(symbol, (String) tableShort.getValueAt(index, 2));
				String tp = ((String) tableShort.getValueAt(index, 3)).replace(" %","");
				String sl = ((String) tableShort.getValueAt(index, 4)).replace(" %","").replace("-", "");

				if (!Config.getIterations().equals(0))
					FrmGrid.launch(symbol.getNameLeft(), "SHORT", shShort.toString(), isBotMode);
				else
					FrmGrid.launch(symbol.getNameLeft(), "SHORT", shShort.toString(), sl, tp, isBotMode);
			}
			else
			{
				Symbol symbol = (Symbol) tableLong.getValueAt(index, 0);
				Price lgShock = new Price(symbol, (String) tableLong.getValueAt(index, 2));
				String tp = ((String) tableLong.getValueAt(index, 3)).replace(" %","");
				String sl = ((String) tableLong.getValueAt(index, 4)).replace(" %","").replace("-", "");

				if (!Config.getIterations().equals(0))
					FrmGrid.launch(symbol.getNameLeft(), "LONG", lgShock.toString(), isBotMode);
				else
					FrmGrid.launch(symbol.getNameLeft(), "LONG", lgShock.toString(), sl, tp, isBotMode);
			}

		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

	private void restart()
	{
		try
		{
			SignalService.restartShocks();
			INFO("Restarting signals...");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public void onSignalUpdate()
	{
		loadTable();
	}

	// ------------------------------------------------------------------------

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
	    	tableModelShort.addColumn("SHORT");
	    	tableModelShort.addColumn("DIST %");
	    	tableModelShort.addColumn("PRICE");
	    	tableModelShort.addColumn("TP %");
	    	tableModelShort.addColumn("SL %");
	    	tableModelShort.addColumn("RATIO");
	    	tableModelShort.addColumn("VOLUME 24h");
	    	tableModelShort.addColumn("CHANGE 24h");
	    	tableModelShort.addColumn("MORE");
			tableShort.setModel(tableModelShort);

	    	tableModelLong = new TableModel();
	    	tableModelLong.addColumn("LONG");
	    	tableModelLong.addColumn("DIST %");
	    	tableModelLong.addColumn("PRICE");
	    	tableModelLong.addColumn("TP %");
	    	tableModelLong.addColumn("SL %");
	    	tableModelLong.addColumn("RATIO");
	    	tableModelLong.addColumn("VOLUME 24h");
	    	tableModelLong.addColumn("CHANGE 24h");
	    	tableModelLong.addColumn("MORE");
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
	        tableShort.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

	        tableShort.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

	        tableLong.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
	        tableLong.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

	        tableLong.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		}
		catch (Exception e)
		{
			ERROR(e);
		}
    }

    private void loadTable_()
	{
    	tableModelShort.setRowCount(0);

		for (Signal entry : SignalService.getLstShortSignals())
		{
        	Object row[] = {
        			entry.getSymbol(),
    				String.format(Locale.US, "%.2f %%", entry.getDistance()),
        			entry.getStrTargetPrice(),
        			String.format(Locale.US, "%.2f %%", entry.getTakeProfit()),
        			String.format(Locale.US, "%.2f %%", entry.getStopLoss()),
    				String.format(Locale.US, "1 : %.1f", entry.getRatio()),
    				PriceUtil.cashFormat(entry.getVolume()),
    				String.format(Locale.US, "%.2f %%", entry.getChange24h()),
    				entry.getBestSide()
        		};

        	tableModelShort.addRow(row);
        }

    	tableModelLong.setRowCount(0);

    	for (Signal entry : SignalService.getLstLongSignals())
		{
        	Object row[] = {
        			entry.getSymbol(),
    				String.format(Locale.US, "%.2f %%", entry.getDistance()),
        			entry.getStrTargetPrice(),
        			String.format(Locale.US, "%.2f %%", entry.getTakeProfit()),
        			String.format(Locale.US, "%.2f %%", entry.getStopLoss()),
    				String.format(Locale.US, "1 : %.1f", entry.getRatio()),
    				PriceUtil.cashFormat(entry.getVolume()),
    				String.format(Locale.US, "%.2f %%", entry.getChange24h()),
    				entry.getBestSide()
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
			ERROR(e);
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// ------------------------------------------------------------------------

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
					myJFrame = new FrmSignals();
					myJFrame.setVisible(true);
				}
				catch (Exception e)
				{
					LogService.error(e);
				}
			}
		});
	}

	// ------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(ExceptionUtils.getMessage(e));
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

}
