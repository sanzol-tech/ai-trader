package sanzol.aitrader.ui.forms;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
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

import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.model.SymbolInfo;
import sanzol.aitrader.be.service.PriceListener;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.ExceptionUtils;
import sanzol.util.log.LogService;
import sanzol.util.price.PriceUtil;

public class FrmSymbols extends JFrame implements PriceListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Symbols";

	private static FrmSymbols myJFrame = null;

    private DefaultTableModel tableModel;
	
	private JLabel lblError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;
	private JTextField txtWithdrawal;
	private JTable table;
	private JCheckBox chkOnlyBetters;
	private JCheckBox chkOnlyFavorites;

	public FrmSymbols()
	{
		initComponents();

		createTable();

		PriceService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmSymbols.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();
        
        table = new JTable();
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

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

		chkOnlyBetters = new JCheckBox("Only betters");
		chkOnlyFavorites = new JCheckBox("Only favorites");

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(15)
					.addComponent(chkOnlyFavorites)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chkOnlyBetters)
					.addContainerGap(585, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chkOnlyFavorites)
						.addComponent(chkOnlyBetters))
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

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PriceService.deattachRefreshObserver(myJFrame);				
				myJFrame = null;
			}
		});

        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int index = table.getSelectedRow();
					String symbolName = (String) table.getValueAt(index, 0);
					FrmCoin.launch(symbolName);
                }
            }
        });
		
	}

	// ------------------------------------------------------------------------
	
	@Override
	public void onPriceUpdate()
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
	    	tableModel = new TableModel();

	    	tableModel.addColumn("SYMBOL");
	    	tableModel.addColumn("PRICE");
	    	tableModel.addColumn("VOLUME 24h");
	    	tableModel.addColumn("");
	    	tableModel.addColumn("CHANGE 24h");
	    	tableModel.addColumn("");
	    	tableModel.addColumn("MORE");

			table.setModel(tableModel);
			
	        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	        
	        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

	        int tableWidth = table.getWidth();
	        		
	        table.getColumnModel().getColumn(0).setPreferredWidth((int)(tableWidth * 0.17));

	        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(1).setPreferredWidth((int)(tableWidth * 0.17));

	        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(2).setPreferredWidth((int)(tableWidth * 0.125));
	        
	        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(3).setPreferredWidth((int)(tableWidth * 0.08));
	        
	        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(4).setPreferredWidth((int)(tableWidth * 0.125));
	        
	        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(5).setPreferredWidth((int)(tableWidth * 0.08));

	        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(6).setPreferredWidth((int)(tableWidth * 0.125));

	        //table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		}
		catch (Exception e)
		{
			ERROR(e);
		}
    }

    private void loadTable(List<SymbolInfo> lstSymbolsInfo)
	{
    	tableModel.setRowCount(0);

		for (SymbolInfo entry : lstSymbolsInfo)
		{
        	Object row[] = { 
    				entry.getSymbol().getNameLeft(),
					entry.getSymbol().priceToStr(entry.getLastPrice()),
    				PriceUtil.cashFormat(entry.getUsdVolume()),
					!entry.isLowVolume() ? "" : "< " + PriceUtil.cashFormat(Config.getBetterSymbolsMinVolume()),
    				String.format("%.2f %%", entry.getPriceChangePercent()),
					!entry.isHighMove() ? "" : "RISKY",
					entry.isBestShort() ? "24H HIGH" : entry.isBestLong() ? "24H LOW" : ""
        		};

			tableModel.addRow(row);
        }
    }

	private void loadTable()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			List<SymbolInfo> lstSymbolsInfo = PriceService.getLstSymbolsInfo(chkOnlyFavorites.isSelected(), chkOnlyBetters.isSelected());
			loadTable(lstSymbolsInfo);
			tableModel.fireTableDataChanged();
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
					myJFrame = new FrmSymbols();
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
