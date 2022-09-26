package sanzol.aitrader.ui.forms;

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
import java.math.BigDecimal;
import java.util.Map;

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

import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.model.Alert;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.service.AlertListener;
import sanzol.aitrader.be.service.AlertService;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.DateTimeUtils;
import sanzol.util.ExceptionUtils;

public class FrmAlerts extends JFrame implements AlertListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Alerts";

	private static FrmAlerts myJFrame = null;

	private Symbol symbol;

    private DefaultTableModel tableModel;

	private JLabel lblError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtLongAlert;
	private JTextField txtLongLimit;
	private JTextField txtShortLimit;
	private JTextField txtShortAlert;

	private JLabel lblLastPrice;

	private JButton btnSearch;
	private JButton btnUpdate;

	private JTable table;

	public FrmAlerts()
	{
		initComponents();

		createTable();

		AlertService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		setMinimumSize(new Dimension(700, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmAlerts.class.getResource("/resources/bell.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

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
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
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

		// --------------------------------------------------------------------
        JPanel panel = new JPanel();

        javax.swing.GroupLayout pnlTopBarLayout = new javax.swing.GroupLayout(pnlTopBar);
        pnlTopBarLayout.setHorizontalGroup(
        	pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlTopBarLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlTopBarLayout.setVerticalGroup(
        	pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(pnlTopBarLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
        			.addContainerGap())
        );
        panel.setLayout(null);

        txtSymbolLeft = new JTextField();
        txtSymbolLeft.setBounds(10, 31, 86, 20);
        panel.add(txtSymbolLeft);

        txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Config.DEFAULT_SYMBOL_RIGHT);
        txtSymbolRight.setBounds(102, 31, 86, 20);
        panel.add(txtSymbolRight);

        JLabel lblShort = new JLabel();
        lblShort.setHorizontalAlignment(SwingConstants.TRAILING);
        lblShort.setText("Short");
        lblShort.setBounds(214, 32, 34, 14);
        panel.add(lblShort);

        JLabel lblLong = new JLabel();
        lblLong.setHorizontalAlignment(SwingConstants.TRAILING);
        lblLong.setText("Long");
        lblLong.setBounds(214, 58, 34, 14);
        panel.add(lblLong);

        txtLongAlert = new JTextField();
        txtLongAlert.setHorizontalAlignment(SwingConstants.TRAILING);
        txtLongAlert.setBounds(258, 57, 80, 20);
        panel.add(txtLongAlert);

        txtLongLimit = new JTextField();
        txtLongLimit.setHorizontalAlignment(SwingConstants.TRAILING);
        txtLongLimit.setBounds(348, 57, 80, 20);
        panel.add(txtLongLimit);

        txtShortLimit = new JTextField();
        txtShortLimit.setHorizontalAlignment(SwingConstants.TRAILING);
        txtShortLimit.setBounds(348, 31, 80, 20);
        panel.add(txtShortLimit);

        txtShortAlert = new JTextField();
        txtShortAlert.setHorizontalAlignment(SwingConstants.TRAILING);
        txtShortAlert.setBounds(258, 31, 80, 20);
        panel.add(txtShortAlert);

        JLabel lblAlert = new JLabel();
        lblAlert.setText("Alert");
        lblAlert.setBounds(258, 11, 80, 14);
        panel.add(lblAlert);

        JLabel lblLimit = new JLabel();
        lblLimit.setText("Limit");
        lblLimit.setBounds(348, 11, 80, 14);
        panel.add(lblLimit);

        btnUpdate = new JButton();
        btnUpdate.setText("Update");
        btnUpdate.setBounds(449, 31, 70, 49);
        panel.add(btnUpdate);

        JLabel lblSymbol = new JLabel();
        lblSymbol.setText("SYMBOL");
        lblSymbol.setBounds(10, 11, 86, 14);
        panel.add(lblSymbol);

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
        btnSearch.setOpaque(true);
        btnSearch.setBounds(10, 57, 178, 22);
        panel.add(btnSearch);

        lblLastPrice = new JLabel();
        lblLastPrice.setHorizontalAlignment(SwingConstants.TRAILING);
        lblLastPrice.setForeground(Styles.COLOR_TEXT_ALT1);
        lblLastPrice.setBounds(102, 11, 86, 14);
        panel.add(lblLastPrice);

        pnlTopBar.setLayout(pnlTopBarLayout);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlTopBar, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
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
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
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
				AlertService.deattachRefreshObserver(myJFrame);
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

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

        btnUpdate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		addAlert();
        	}
        });

	}

	private void search()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbolLeft = txtSymbolLeft.getText();
			String symbolRight = txtSymbolRight.getText();
			symbol = Symbol.getInstance(symbolLeft + symbolRight);

			if (symbol != null)
			{
				BigDecimal lastPrice = PriceService.getLastPrice(symbol);
				lblLastPrice.setText(symbol.priceToStr(lastPrice));

				txtShortAlert.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.valueOf(1.015))));
				txtShortLimit.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.valueOf(1.02))));
				txtLongAlert.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.valueOf(0.985))));
				txtLongLimit.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.valueOf(0.98))));
			}
			else
			{
				ERROR("Symbol not found");
			}
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void addAlert()
	{
		try
		{
			BigDecimal shortAlert = new BigDecimal(txtShortAlert.getText());
			BigDecimal shortLimit = new BigDecimal(txtShortLimit.getText());
			BigDecimal longAlert = new BigDecimal(txtLongAlert.getText());
			BigDecimal longLimit = new BigDecimal(txtLongLimit.getText());

			Alert alert = new Alert(symbol, shortAlert, shortLimit, longAlert, longLimit);
			AlertService.add(alert);

			loadTable();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

	@Override
	public void onAlertsUptade()
	{
		if (symbol != null)
		{
			BigDecimal lastPrice = PriceService.getLastPrice(symbol);
			lblLastPrice.setText(symbol.priceToStr(lastPrice));
		}

		loadTable();
	}

	@Override
	public void onAlert(Alert alert)
	{
		if (alert != null)
		{
			INFO (alert.getSymbol().getPair() + " " + alert.getAlertState());
		}
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
	    	tableModel.addColumn("ALERT");
	    	tableModel.addColumn("SH ALERT");
	    	tableModel.addColumn("SH LIMIT");
	    	tableModel.addColumn("LG ALERT");
	    	tableModel.addColumn("LG LIMIT");
	    	tableModel.addColumn("TIME OUT");

			table.setModel(tableModel);

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

	private void loadTable()
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		try
		{
			Map<String, Alert> mapAlerts = AlertService.getMapAlerts();

	    	tableModel.setRowCount(0);

			for (Alert entry : mapAlerts.values())
			{
				Symbol eSymbol = entry.getSymbol();
	        	Object row[] = {
	    				entry.getSymbol().getNameLeft(),
	    				entry.getAlertState(),
	    				eSymbol.priceToStr(entry.getShortAlert()),
	    				eSymbol.priceToStr(entry.getShortLimit()),
	    				eSymbol.priceToStr(entry.getLongAlert()),
	    				eSymbol.priceToStr(entry.getLongLimit()),
	    				DateTimeUtils.millisToReadable(entry.getTimeOut() - System.currentTimeMillis())
	        		};

				tableModel.addRow(row);
			}

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
					myJFrame = new FrmAlerts();
					myJFrame.setVisible(true);
					myJFrame.loadTable();
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

}
