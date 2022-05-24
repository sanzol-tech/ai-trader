package sanzol.app.forms;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
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

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.listener.SignalListener;
import sanzol.app.model.Price;
import sanzol.app.model.SignalEntry;
import sanzol.app.service.Symbol;
import sanzol.app.task.SignalService;
import sanzol.lib.util.ExceptionUtils;

public class FrmSignals extends JFrame implements SignalListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Signals";

	private static boolean isOpen = false;
	
    DefaultTableModel tableModel;

    private JLabel lblError;
	
	private JButton btnGenerate;
	private JButton btnEdit;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;
	private JTextField txtWithdrawal;
	private JTable table;

	public FrmSignals()
	{
		initComponents();
		isOpen = true;
		
		createTable();
		
		SignalService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);
		btnGenerate = new JButton();
		btnGenerate.setText("GENERATE");
		btnEdit = new JButton();
		btnEdit.setText("EDIT");

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();
        
        JScrollPane scrollPane = new JScrollPane();

        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContentLayout.setHorizontalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlContentLayout.setVerticalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        			.addContainerGap())
        );

        table = new JTable();
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        scrollPane.setViewportView(table);
        pnlContent.setLayout(pnlContentLayout);

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnGenerate, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnEdit)
					.addContainerGap(555, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnGenerate)
						.addComponent(btnEdit))
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

		FrmSignals thisFrm = this;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				SignalService.deattachRefreshObserver(thisFrm);
				isOpen = false;
			}
		});

        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int index = table.getSelectedRow();
					tradeFromSignal(index);
                }
            }
        });
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmPointsEditor.launch();
			}
		});
		
	}

	// ------------------------------------------------------------------------

	private void tradeFromSignal(int index)
	{
		try
		{
			boolean isBotMode = false;

			Symbol symbol = (Symbol) table.getValueAt(index, 0);
			Price shShort = (Price) table.getValueAt(index, 1);
			Price cellPrice = (Price) table.getValueAt(index, 2);
			Price lgShock = (Price) table.getValueAt(index, 3);
			String cellAction = (String) table.getValueAt(index, 7);

			if (cellAction.startsWith("SHORT"))
			{
				BigDecimal price = cellPrice.toBigDecimal().max(shShort.toBigDecimal());
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", symbol.priceToStr(price), isBotMode);
			}
			else
			{
				BigDecimal price = cellPrice.toBigDecimal().max(lgShock.toBigDecimal());
				FrmGrid.launch(symbol.getNameLeft(), "LONG", symbol.priceToStr(price), isBotMode);
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

	private void generate()
	{
		try
		{
			ConfirmGenPoints dialog = new ConfirmGenPoints();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
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

	public class TableModel extends DefaultTableModel
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
	    	tableModel.addColumn("SHORT");
	    	tableModel.addColumn("PRICE");
	    	tableModel.addColumn("LONG");
	    	tableModel.addColumn("DIST%");
	    	tableModel.addColumn("SH%");
	    	tableModel.addColumn("LG%");
	    	tableModel.addColumn("ACTION");

			table.setModel(tableModel);

	        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	        
	        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

	        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

	        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		}
		catch (Exception e)
		{
			ERROR(e);
		}
    }

    private void loadTable(List<SignalEntry> lstSignalEntry)
	{
    	tableModel.setRowCount(0);
    	
		for (SignalEntry entry : lstSignalEntry)
		{
			Symbol symbol = entry.getSymbol();
        	Object row[] = {
        			entry.getSymbol(),
    				new Price(symbol, entry.getShShock()),
    				new Price(symbol, entry.getPrice()),
    				new Price(symbol, entry.getLgShock()),
    				String.format("%.2f %%", entry.getDistShLg()),
    				String.format("%.2f %%", entry.getDistShort()),
    				String.format("%.2f %%", entry.getDistLong()),
    				entry.getAction()
        		};

			tableModel.addRow(row);
        }
	}

	private void loadTable()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			List<SignalEntry> lstSignalEntry = SignalService.getLstShockStatus();
			loadTable(lstSignalEntry);
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
					FrmSignals frame = new FrmSignals();
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

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		Application.initialize();
		Application.initializeUI();
		launch();
	}
}
