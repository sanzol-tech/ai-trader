package sanzol.app.forms;

import java.awt.Cursor;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.CandlestickInterval;
import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.market.Candlestick;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.service.Symbol;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;
import sanzol.app.util.PriceUtil;
import sanzol.lib.util.ExceptionUtils;

public class FrmSignalsAlt extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Signals";

	private static boolean isOpen = false;

	private JLabel lblError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;
	private JTextField txtWithdrawal;
	private JTextArea txtResult;

	public FrmSignalsAlt()
	{
		initComponents();
		isOpen = true;
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

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();

		JButton btnCopy = new JButton(Styles.IMAGE_COPY);
		btnCopy.setToolTipText("Copy to clipboard");
		btnCopy.setOpaque(true);

		// --------------------------------------------------------------------
        txtResult = new javax.swing.JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
        txtResult.setColumns(20);
        txtResult.setRows(5);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(txtResult);

        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContent.setLayout(pnlContentLayout);
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
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
		
		JButton btnNewButton = new JButton("Calc");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSignals();
			}
		});

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED, 658, Short.MAX_VALUE)
					.addComponent(btnCopy)
					.addContainerGap())
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton)
						.addComponent(btnCopy))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
				isOpen = false;
			}
		});

		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(txtResult.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		
	}

	// ------------------------------------------------------------------------

	private static Candlestick lastCandlestick_5m(Symbol symbol)
	{
		RequestOptions options = new RequestOptions();
		SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY, options);

		Candlestick candlestick = syncRequestClient.getCandlestick(symbol.getName(), CandlestickInterval.FIVE_MINUTES, null, null, 1).get(0);

		return candlestick;
	}

	private static BigDecimal distance(Candlestick candlestick)
	{
		return (candlestick.getHigh().divide(candlestick.getLow(), 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).multiply(BigDecimal.valueOf(100));
	}
	
	private String calc()
	{
		final double MIN_USDT = 50; 
		final double MIN_VOLUME = 50000000;
		
		List<String> lst = new ArrayList<String>();
		
		List<SymbolTickerEvent> lstSymbolTickerEvent = new ArrayList<SymbolTickerEvent>();
		lstSymbolTickerEvent.addAll(PriceService.getMapTickers().values());
		for (SymbolTickerEvent entry : lstSymbolTickerEvent)
		{
			Symbol symbol = Symbol.getInstance(entry.getSymbol());

			double lastPrice = entry.getLastPrice().doubleValue();
			double volume = entry.getTotalTradedQuoteAssetVolume().doubleValue(); 
			String priceChangePercent = String.format("%.2f", entry.getPriceChangePercent());

			double high = entry.getHigh().doubleValue();
			double low = entry.getLow().doubleValue();
			double avgPrice = entry.getWeightedAvgPrice().doubleValue();

			double avgHigh = (avgPrice + high) / 2;
			double avgLow =  (avgPrice + low) / 2;

			double min_usdt = symbol.getMinQty().doubleValue() * lastPrice;

			if (min_usdt > MIN_USDT)
			{
				lst.add(String.format("6 MIN QTY      %-12s  vol %5s  24Hs %8s %%  Price %12f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice));
			}
			else
			if (volume < MIN_VOLUME)
			{
				lst.add(String.format("4 LOW VOLUME   %-12s  vol %5s  24Hs %8s %%  Price %12f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice));
			}
			else
			if (entry.getPriceChangePercent().abs().doubleValue() > 8)
			{
				lst.add(String.format("5 HIGH MOVE    %-12s  vol %5s  24Hs %8s %%  Price %12f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice));
			}
			else
			if (lastPrice > avgHigh)
			{
				double dist_5m = distance(lastCandlestick_5m(symbol)).doubleValue();
				lst.add(String.format("1 SHORT        %-12s  vol %5s  24Hs %8s %%  Price %12f 5m %f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice, dist_5m));
			}
			else
			if (lastPrice < avgLow)
			{
				double dist_5m = distance(lastCandlestick_5m(symbol)).doubleValue();
				lst.add(String.format("2 LONG         %-12s  vol %5s  24Hs %8s %%  Price %12f 5m %f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice, dist_5m));
			}
			else
			{
				lst.add(String.format("3 N/A          %-12s  vol %5s  24Hs %8s %%  Price %12f", symbol.getNameLeft(), PriceUtil.cashFormat(volume, 0), priceChangePercent, lastPrice));
			}
		}

		java.util.Collections.sort(lst);

		String result = "";
		for (String line : lst)
		{
			result += line + "\n";
		}
		
		return result;
	}

	private void getSignals()
	{
		try
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			txtResult.setText(calc());
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
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
					FrmSignalsAlt frame = new FrmSignalsAlt();
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
