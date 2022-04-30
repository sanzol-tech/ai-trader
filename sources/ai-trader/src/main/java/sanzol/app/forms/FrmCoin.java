package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.OrderBookElement;
import sanzol.app.model.OrderBookInfo;
import sanzol.app.model.Symbol;
import sanzol.app.service.OrderBookService;
import sanzol.app.task.PriceService;
import sanzol.app.util.Convert;
import sanzol.app.util.PriceUtil;

public class FrmCoin extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private static boolean isOpen = false;

	private Symbol coin;
	private OrderBookInfo obInfo = null;

	private JPanel contentPane;
	private JLabel lblError;

	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtMarkPrice;
	private JTextField txt24h;

	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;
	private JPanel panel_0;
	private JTextField txtShortPrice;
	private JTextField txtShortDist;
	private JButton btnShortShock0;
	private JButton btnLongShock0;
	private JTextField txtLongDist;
	private JTextField txtLongPrice;
	private JLabel lblTitlePoints0;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblTitlePoints1;
	private JPanel panel_1;
	private JTextField txtShortPrice1;
	private JTextField txtShortDist1;
	private JButton btnShortShock1;
	private JButton btnLongShock1;
	private JTextField txtLongDist1;
	private JTextField txtLongPrice1;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;
	private JLabel lblNewLabel_9;
	private JLabel lblTitlePoints2;
	private JPanel panel_2;
	private JTextField txtShortPrice2;
	private JTextField txtShortDist2;
	private JButton btnShortShock2;
	private JButton btnLongShock2;
	private JTextField txtLongDist2;
	private JTextField txtLongPrice2;
	private JLabel lblNewLabel_11;
	private JLabel lblNewLabel_12;
	private JLabel lblNewLabel_13;
	private JLabel lblNewLabel_14;
	private JButton btnExport;
	private JLabel lbl24Hs;
	private JTextField txtLow;
	private JTextField txtHigh;
	private JLabel lblHigh;
	private JLabel lblLow;

	public FrmCoin()
	{
		setResizable(false);
		initComponents();
		startTimer();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 601);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(31, 528, 768, 22);
		contentPane.add(pnlBottom);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		
		lblError = new JLabel();
		pnlBottom.add(lblError, BorderLayout.CENTER);
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		
		JButton btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(31, 69, 178, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(31, 43, 86, 20);
		contentPane.add(txtSymbolLeft);

		JLabel lblNewLabel_2_1 = new JLabel("COIN");
		lblNewLabel_2_1.setBounds(31, 24, 86, 14);
		contentPane.add(lblNewLabel_2_1);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(123, 43, 86, 20);
		contentPane.add(txtSymbolRight);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setFont(new Font("Courier New", Font.BOLD, 12));
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(440, 280, 360, 20);
		contentPane.add(txtMarkPrice);
		
		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(234, 43, 86, 20);
		contentPane.add(txt24h);

		scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookAsk.setBounds(440, 74, 360, 200);
		scrollOBookAsk.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollOBookAsk);

		txtOBookAsk = new JTextArea();
		txtOBookAsk.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
		txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookAsk.setEditable(false);
		scrollOBookAsk.setViewportView(txtOBookAsk);

		scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookBid.setBounds(440, 306, 360, 200);
		scrollOBookBid.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollOBookBid);

		txtOBookBid = new JTextArea();
		txtOBookBid.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtOBookBid.setForeground(Styles.COLOR_TEXT_LONG);
		txtOBookBid.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookBid.setEditable(false);
		scrollOBookBid.setViewportView(txtOBookBid);

		panel_0 = new JPanel();
		panel_0.setLayout(null);
		panel_0.setBorder(UIManager.getBorder("TextField.border"));
		panel_0.setBounds(29, 144, 387, 95);
		contentPane.add(panel_0);

		txtShortPrice = new JTextField();
		txtShortPrice.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPrice.setEditable(false);
		txtShortPrice.setColumns(10);
		txtShortPrice.setBounds(86, 32, 110, 20);
		panel_0.add(txtShortPrice);

		txtShortDist = new JTextField();
		txtShortDist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDist.setEditable(false);
		txtShortDist.setColumns(10);
		txtShortDist.setBounds(206, 32, 80, 20);
		panel_0.add(txtShortDist);

		btnShortShock0 = new JButton("\u2193");
		btnShortShock0.setToolTipText("Create Short in this price");
		btnShortShock0.setOpaque(true);
		btnShortShock0.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShock0.setBounds(296, 31, 46, 22);
		panel_0.add(btnShortShock0);

		btnLongShock0 = new JButton("\u2191");
		btnLongShock0.setToolTipText("Create Long in this price");
		btnLongShock0.setOpaque(true);
		btnLongShock0.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShock0.setBounds(296, 62, 46, 22);
		panel_0.add(btnLongShock0);

		txtLongDist = new JTextField();
		txtLongDist.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDist.setEditable(false);
		txtLongDist.setColumns(10);
		txtLongDist.setBounds(206, 63, 80, 20);
		panel_0.add(txtLongDist);

		txtLongPrice = new JTextField();
		txtLongPrice.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPrice.setEditable(false);
		txtLongPrice.setColumns(10);
		txtLongPrice.setBounds(86, 63, 110, 20);
		panel_0.add(txtLongPrice);

		lblNewLabel_1 = new JLabel("SHORT");
		lblNewLabel_1.setBounds(24, 35, 52, 14);
		panel_0.add(lblNewLabel_1);

		lblNewLabel_2 = new JLabel("LONG");
		lblNewLabel_2.setBounds(24, 66, 52, 14);
		panel_0.add(lblNewLabel_2);

		lblNewLabel_3 = new JLabel("Price");
		lblNewLabel_3.setBounds(86, 11, 46, 14);
		panel_0.add(lblNewLabel_3);

		lblNewLabel_4 = new JLabel("Dist %");
		lblNewLabel_4.setBounds(206, 11, 46, 14);
		panel_0.add(lblNewLabel_4);

		lblTitlePoints0 = new JLabel("Biggest block");
		lblTitlePoints0.setBounds(29, 119, 178, 14);
		contentPane.add(lblTitlePoints0);

		lblTitlePoints1 = new JLabel("Min between 30% and 40%");
		lblTitlePoints1.setBounds(29, 253, 178, 14);
		contentPane.add(lblTitlePoints1);

		panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(UIManager.getBorder("TextField.border"));
		panel_1.setBounds(29, 278, 387, 95);
		contentPane.add(panel_1);

		txtShortPrice1 = new JTextField();
		txtShortPrice1.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPrice1.setEditable(false);
		txtShortPrice1.setColumns(10);
		txtShortPrice1.setBounds(86, 32, 110, 20);
		panel_1.add(txtShortPrice1);

		txtShortDist1 = new JTextField();
		txtShortDist1.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDist1.setEditable(false);
		txtShortDist1.setColumns(10);
		txtShortDist1.setBounds(206, 32, 80, 20);
		panel_1.add(txtShortDist1);

		btnShortShock1 = new JButton("\u2193");
		btnShortShock1.setToolTipText("Create Short in this price");
		btnShortShock1.setOpaque(true);
		btnShortShock1.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShock1.setBounds(296, 31, 46, 22);
		panel_1.add(btnShortShock1);

		btnLongShock1 = new JButton("\u2191");
		btnLongShock1.setToolTipText("Create Long in this price");
		btnLongShock1.setOpaque(true);
		btnLongShock1.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShock1.setBounds(296, 62, 46, 22);
		panel_1.add(btnLongShock1);

		txtLongDist1 = new JTextField();
		txtLongDist1.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDist1.setEditable(false);
		txtLongDist1.setColumns(10);
		txtLongDist1.setBounds(206, 63, 80, 20);
		panel_1.add(txtLongDist1);

		txtLongPrice1 = new JTextField();
		txtLongPrice1.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPrice1.setEditable(false);
		txtLongPrice1.setColumns(10);
		txtLongPrice1.setBounds(86, 63, 110, 20);
		panel_1.add(txtLongPrice1);

		lblNewLabel_6 = new JLabel("SHORT");
		lblNewLabel_6.setBounds(24, 35, 52, 14);
		panel_1.add(lblNewLabel_6);

		lblNewLabel_7 = new JLabel("LONG");
		lblNewLabel_7.setBounds(24, 66, 52, 14);
		panel_1.add(lblNewLabel_7);

		lblNewLabel_8 = new JLabel("Price");
		lblNewLabel_8.setBounds(86, 11, 46, 14);
		panel_1.add(lblNewLabel_8);

		lblNewLabel_9 = new JLabel("Dist %");
		lblNewLabel_9.setBounds(206, 11, 46, 14);
		panel_1.add(lblNewLabel_9);

		lblTitlePoints2 = new JLabel("Weighted average (Max accum 50% / Max dist 20%)");
		lblTitlePoints2.setBounds(29, 386, 370, 14);
		contentPane.add(lblTitlePoints2);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(UIManager.getBorder("TextField.border"));
		panel_2.setBounds(29, 411, 387, 95);
		contentPane.add(panel_2);

		txtShortPrice2 = new JTextField();
		txtShortPrice2.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPrice2.setEditable(false);
		txtShortPrice2.setColumns(10);
		txtShortPrice2.setBounds(86, 32, 110, 20);
		panel_2.add(txtShortPrice2);

		txtShortDist2 = new JTextField();
		txtShortDist2.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDist2.setEditable(false);
		txtShortDist2.setColumns(10);
		txtShortDist2.setBounds(206, 32, 80, 20);
		panel_2.add(txtShortDist2);

		btnShortShock2 = new JButton("\u2193");
		btnShortShock2.setToolTipText("Create Short in this price");
		btnShortShock2.setOpaque(true);
		btnShortShock2.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShock2.setBounds(296, 31, 46, 22);
		panel_2.add(btnShortShock2);

		btnLongShock2 = new JButton("\u2191");
		btnLongShock2.setToolTipText("Create Long in this price");
		btnLongShock2.setOpaque(true);
		btnLongShock2.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShock2.setBounds(296, 62, 46, 22);
		panel_2.add(btnLongShock2);

		txtLongDist2 = new JTextField();
		txtLongDist2.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDist2.setEditable(false);
		txtLongDist2.setColumns(10);
		txtLongDist2.setBounds(206, 63, 80, 20);
		panel_2.add(txtLongDist2);

		txtLongPrice2 = new JTextField();
		txtLongPrice2.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPrice2.setEditable(false);
		txtLongPrice2.setColumns(10);
		txtLongPrice2.setBounds(86, 63, 110, 20);
		panel_2.add(txtLongPrice2);

		lblNewLabel_11 = new JLabel("SHORT");
		lblNewLabel_11.setBounds(24, 35, 52, 14);
		panel_2.add(lblNewLabel_11);

		lblNewLabel_12 = new JLabel("LONG");
		lblNewLabel_12.setBounds(24, 66, 52, 14);
		panel_2.add(lblNewLabel_12);

		lblNewLabel_13 = new JLabel("Price");
		lblNewLabel_13.setBounds(86, 11, 46, 14);
		panel_2.add(lblNewLabel_13);

		lblNewLabel_14 = new JLabel("Dist %");
		lblNewLabel_14.setBounds(206, 11, 46, 14);
		panel_2.add(lblNewLabel_14);

		btnExport = new JButton("Export");
		btnExport.setOpaque(true);
		btnExport.setBounds(710, 42, 87, 22);
		contentPane.add(btnExport);
		
		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(234, 24, 85, 14);
		contentPane.add(lbl24Hs);
		
		txtLow = new JTextField();
		txtLow.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtLow.setEditable(false);
		txtLow.setColumns(10);
		txtLow.setBounds(330, 94, 86, 20);
		contentPane.add(txtLow);
		
		txtHigh = new JTextField();
		txtHigh.setEditable(false);
		txtHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		txtHigh.setForeground(Styles.COLOR_TEXT_ALT1);
		txtHigh.setColumns(10);
		txtHigh.setBounds(330, 43, 86, 20);
		contentPane.add(txtHigh);
		
		lblHigh = new JLabel("HIGH");
		lblHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHigh.setBounds(330, 24, 85, 14);
		contentPane.add(lblHigh);
		
		lblLow = new JLabel("LOW");
		lblLow.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLow.setBounds(330, 75, 85, 14);
		contentPane.add(lblLow);

		// ---------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				isOpen = false;
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});


		btnShortShock0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "SHORT", txtShortPrice.getText());
			}
		});
		btnLongShock0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "LONG", txtLongPrice.getText());
			}
		});
		btnShortShock1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "SHORT", txtShortPrice1.getText());
			}
		});
		btnLongShock1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "LONG", txtLongPrice1.getText());
			}
		});
		btnShortShock2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "SHORT", txtShortPrice2.getText());
			}
		});
		btnLongShock2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch(coin.getNameLeft(), "LONG", txtLongPrice2.getText());
			}
		});


		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		
	}

	// ----------------------------------------------------------------------------------

	private void search()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

			if (coin != null)
			{
				setTitle(TITLE + " - " + symbol);
				
				loadDepth();				
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

	private void loadDepth()
	{
		double minPercent = 0.3;
		double maxPercent = 0.4;

		double maxAccumPercent = 0.5;
		double maxDist = 0.2;

		try
		{
			obInfo = OrderBookService.getShoks(coin);

			txtOBookAsk.setText(OrderBookService.toStringInv(coin, obInfo.getAsksGrp()));
			txtShortPrice.setText(obInfo.getStrShShock());

			BigDecimal shShock1 = obInfo.getAskPriceBetween(minPercent, maxPercent);
			txtShortPrice1.setText(shShock1.toPlainString());

			BigDecimal shShock2 = OrderBookService.weightedAverage(obInfo.getAsks(), maxAccumPercent, maxDist);
			txtShortPrice2.setText(shShock2.toPlainString());

			txtOBookBid.setText(OrderBookService.toString(coin, obInfo.getBidsGrp()));
			txtOBookBid.setCaretPosition(0);

			txtLongPrice.setText(obInfo.getStrLgShock());

			BigDecimal lgShock1 = obInfo.getBidPriceBetween(minPercent, maxPercent);
			txtLongPrice1.setText(lgShock1.toPlainString());

			BigDecimal lgShock2 = OrderBookService.weightedAverage(obInfo.getBids(), maxAccumPercent, maxDist);
			txtLongPrice2.setText(lgShock2.toPlainString());

			BigDecimal mrkPrice = PriceService.getLastPrice(coin);

			String distSh = Convert.dblToStrPercent(PriceUtil.priceDist(obInfo.getShShock(), mrkPrice));
			txtShortDist.setText(distSh + " %");

			String distSh1 = Convert.dblToStrPercent(PriceUtil.priceDist(shShock1, mrkPrice));
			txtShortDist1.setText(distSh1 + " %");

			String distSh2 = Convert.dblToStrPercent(PriceUtil.priceDist(shShock2, mrkPrice));
			txtShortDist2.setText(distSh2 + " %");

			String distLg = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, obInfo.getLgShock()));
			txtLongDist.setText(distLg + " %");

			String distLg1 = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, lgShock1));
			txtLongDist1.setText(distLg1 + " %");

			String distLg2 = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, lgShock2));
			txtLongDist2.setText(distLg2 + " %");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	public void export()
	{
		try
		{
			// ----------------------------------------------------------------
			List<OrderBookElement> asks = obInfo.getAsks();
			if (asks != null && !asks.isEmpty())
			{
				StringBuilder sbAsks = new StringBuilder();
				for (OrderBookElement ele : asks)
				{
					sbAsks.append(ele.toString());
					sbAsks.append("\n");
				}
				File fileExportAsks = new File(Constants.DEFAULT_USER_FOLDER, coin.getNameLeft() + "_depth_asks.csv");
				FileUtils.writeStringToFile(fileExportAsks, sbAsks.toString(), StandardCharsets.UTF_8);
			}
	
			// ----------------------------------------------------------------
			List<OrderBookElement> bids = obInfo.getBids();
			if (bids != null && !bids.isEmpty())
			{
				StringBuilder sbBids = new StringBuilder();
				for (OrderBookElement ele : bids)
				{
					sbBids.append(ele.toString());
					sbBids.append("\n");
				}
				File fileExportBids = new File(Constants.DEFAULT_USER_FOLDER, coin.getNameLeft() + "_depth_bids.csv");
				FileUtils.writeStringToFile(fileExportBids, sbBids.toString(), StandardCharsets.UTF_8);
			}
			
			INFO("Exported to " + Constants.DEFAULT_USER_FOLDER.toString());
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	public static void launch()
	{
		launch(null);
	}

	public static void launch(String symbolLeft)
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
					FrmCoin frame = new FrmCoin();
					frame.setVisible(true);
					
					if (symbolLeft != null && !symbolLeft.isEmpty())
					{
						frame.txtSymbolLeft.setText(symbolLeft);
						frame.search();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void startTimer()
	{
		ActionListener taskPerformer1 = new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				refresh();
			}
		};
		Timer timer1 = new Timer(2000, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void refresh()
	{
		try
		{
			if (coin != null)
			{
				SymbolTickerEvent symbolTicker = PriceService.getSymbolTickerEvent(coin);
				if (symbolTicker != null)
				{
					BigDecimal mrkPrice = PriceService.getLastPrice(coin);
					txtMarkPrice.setText(coin.priceToStr(mrkPrice));
					String priceChangePercent = String.format("%.2f", symbolTicker.getPriceChangePercent());
					txt24h.setText(priceChangePercent);
					txtHigh.setText(symbolTicker.getHigh().toPlainString());
					txtLow.setText(symbolTicker.getLow().toPlainString());
				}
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}
	
	// ----------------------------------------------------------------------------------

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
