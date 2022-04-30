package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private Symbol coin;
	private OrderBookInfo obInfo = null;

	private JPanel contentPane;
	private JLabel lblError;

	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JPanel panel_0;
	private JPanel panel_1;
	private JPanel panel_2;

	private JButton btnSearch;
	private JButton btnExport;
	private JButton btnLongShock0;
	private JButton btnLongShock1;
	private JButton btnLongShock2;
	private JButton btnShortShock0;
	private JButton btnShortShock1;
	private JButton btnShortShock2;

	private JLabel lbl24Hs;
	private JLabel lblHigh;
	private JLabel lblLow;
	private JLabel lblShortBB;
	private JLabel lblShortWA;
	private JLabel lblLongWA;
	private JLabel lblPriceWA;
	private JLabel lblDistWA;
	private JLabel lblLongBB;
	private JLabel lblPriceBB;
	private JLabel lblDistBB;
	private JLabel lblShortFP;
	private JLabel lblLongFP;
	private JLabel lblPriceFP;
	private JLabel lblDistFP;
	private JLabel lblTitlePoints0;
	private JLabel lblTitlePoints1;
	private JLabel lblTitlePoints2;
	private JLabel lblVolume;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;

	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtMarkPrice;
	private JTextField txt24h;
	private JTextField txtHigh;
	private JTextField txtLongDist;
	private JTextField txtLongDist1;
	private JTextField txtLongDist2;
	private JTextField txtLongPrice;
	private JTextField txtLongPrice1;
	private JTextField txtLongPrice2;
	private JTextField txtLow;
	private JTextField txtShortDist;
	private JTextField txtShortDist1;
	private JTextField txtShortDist2;
	private JTextField txtShortPrice;
	private JTextField txtShortPrice1;
	private JTextField txtShortPrice2;
	private JTextField txtVolume;

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
		setBounds(100, 100, 840, 600);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(30, 532, 770, 22);
		contentPane.add(pnlBottom);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		
		lblError = new JLabel();
		pnlBottom.add(lblError, BorderLayout.CENTER);
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		
		btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(30, 69, 178, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(30, 43, 86, 20);
		contentPane.add(txtSymbolLeft);

		JLabel lblSymbol = new JLabel("COIN");
		lblSymbol.setBounds(30, 24, 86, 14);
		contentPane.add(lblSymbol);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(122, 43, 86, 20);
		contentPane.add(txtSymbolRight);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setFont(new Font("Courier New", Font.BOLD, 12));
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(440, 280, 360, 20);
		contentPane.add(txtMarkPrice);
		
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
		panel_0.setBounds(30, 144, 387, 95);
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

		lblShortBB = new JLabel("SHORT");
		lblShortBB.setBounds(24, 35, 52, 14);
		panel_0.add(lblShortBB);

		lblLongBB = new JLabel("LONG");
		lblLongBB.setBounds(24, 66, 52, 14);
		panel_0.add(lblLongBB);

		lblPriceBB = new JLabel("Price");
		lblPriceBB.setBounds(86, 11, 46, 14);
		panel_0.add(lblPriceBB);

		lblDistBB = new JLabel("Dist %");
		lblDistBB.setBounds(206, 11, 46, 14);
		panel_0.add(lblDistBB);

		lblTitlePoints0 = new JLabel("Biggest block");
		lblTitlePoints0.setBounds(30, 122, 178, 14);
		contentPane.add(lblTitlePoints0);

		lblTitlePoints1 = new JLabel("Fixes points");
		lblTitlePoints1.setBounds(30, 390, 178, 14);
		contentPane.add(lblTitlePoints1);

		panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(UIManager.getBorder("TextField.border"));
		panel_1.setBounds(30, 411, 387, 95);
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

		lblShortFP = new JLabel("SHORT");
		lblShortFP.setBounds(24, 35, 52, 14);
		panel_1.add(lblShortFP);

		lblLongFP = new JLabel("LONG");
		lblLongFP.setBounds(24, 66, 52, 14);
		panel_1.add(lblLongFP);

		lblPriceFP = new JLabel("Price");
		lblPriceFP.setBounds(86, 11, 46, 14);
		panel_1.add(lblPriceFP);

		lblDistFP = new JLabel("Dist %");
		lblDistFP.setBounds(206, 11, 46, 14);
		panel_1.add(lblDistFP);

		lblTitlePoints2 = new JLabel("Weighted average (Max accum 50% / Max dist 20%)");
		lblTitlePoints2.setBounds(30, 258, 370, 14);
		contentPane.add(lblTitlePoints2);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(UIManager.getBorder("TextField.border"));
		panel_2.setBounds(30, 280, 387, 95);
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

		lblShortWA = new JLabel("SHORT");
		lblShortWA.setBounds(24, 35, 52, 14);
		panel_2.add(lblShortWA);

		lblLongWA = new JLabel("LONG");
		lblLongWA.setBounds(24, 66, 52, 14);
		panel_2.add(lblLongWA);

		lblPriceWA = new JLabel("Price");
		lblPriceWA.setBounds(86, 11, 46, 14);
		panel_2.add(lblPriceWA);

		lblDistWA = new JLabel("Dist %");
		lblDistWA.setBounds(206, 11, 46, 14);
		panel_2.add(lblDistWA);

		btnExport = new JButton("Export");
		btnExport.setOpaque(true);
		btnExport.setBounds(710, 42, 87, 22);
		contentPane.add(btnExport);

		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(257, 28, 62, 14);
		contentPane.add(lbl24Hs);

		lblVolume = new JLabel("Volume");
		lblVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		lblVolume.setBounds(259, 53, 62, 14);
		contentPane.add(lblVolume);

		lblHigh = new JLabel("HIGH");
		lblHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHigh.setBounds(257, 78, 62, 14);
		contentPane.add(lblHigh);
		
		lblLow = new JLabel("LOW");
		lblLow.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLow.setBounds(247, 103, 72, 14);
		contentPane.add(lblLow);
		
		txt24h = new JTextField();
		txt24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(331, 25, 86, 20);
		contentPane.add(txt24h);

		txtVolume = new JTextField();
		txtVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume.setForeground(Styles.COLOR_TEXT_ALT1);
		txtVolume.setEditable(false);
		txtVolume.setColumns(10);
		txtVolume.setBounds(331, 50, 86, 20);
		contentPane.add(txtVolume);

		txtLow = new JTextField();
		txtLow.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtLow.setEditable(false);
		txtLow.setColumns(10);
		txtLow.setBounds(331, 100, 86, 20);
		contentPane.add(txtLow);
		
		txtHigh = new JTextField();
		txtHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		txtHigh.setForeground(Styles.COLOR_TEXT_ALT1);
		txtHigh.setEditable(false);
		txtHigh.setColumns(10);
		txtHigh.setBounds(331, 75, 86, 20);
		contentPane.add(txtHigh);

		// ---------------------------------------------------------------------

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
				
				getDepth();
				getDistances();
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
					txtVolume.setText(PriceUtil.cashFormat(symbolTicker.getTotalTradedQuoteAssetVolume().doubleValue(), 0));
					txtHigh.setText(symbolTicker.getHigh().toPlainString());
					txtLow.setText(symbolTicker.getLow().toPlainString());
					
					getDistances();
				}
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}
	
	private void getDepth()
	{
		try
		{
			obInfo = OrderBookService.getObInfo(coin);

			txtOBookAsk.setText(OrderBookService.toStringInv(coin, obInfo.getAsksGrp()));
			txtOBookBid.setText(OrderBookService.toString(coin, obInfo.getBidsGrp()));
			txtOBookBid.setCaretPosition(0);
			
			txtShortPrice.setText(obInfo.getStrShShock());
			txtLongPrice.setText(obInfo.getStrLgShock());

			txtShortPrice1.setText(obInfo.getStrShShockFixed());
			txtLongPrice1.setText(obInfo.getStrLgShockFixed());

			txtShortPrice2.setText(obInfo.getStrShShockWAvg());
			txtLongPrice2.setText(obInfo.getStrLgShockWAvg());
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void getDistances()
	{
		try
		{
			BigDecimal mrkPrice = PriceService.getLastPrice(coin);

			String distSh = Convert.dblToStrPercent(PriceUtil.priceDist(obInfo.getShShock(), mrkPrice));
			txtShortDist.setText(distSh + " %");

			String distSh1 = Convert.dblToStrPercent(PriceUtil.priceDist(obInfo.getShShockFixed(), mrkPrice));
			txtShortDist1.setText(distSh1 + " %");

			String distSh2 = Convert.dblToStrPercent(PriceUtil.priceDist(obInfo.getShShockWAvg(), mrkPrice));
			txtShortDist2.setText(distSh2 + " %");

			String distLg = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, obInfo.getLgShock()));
			txtLongDist.setText(distLg + " %");

			String distLg1 = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, obInfo.getLgShockFixed()));
			txtLongDist1.setText(distLg1 + " %");

			String distLg2 = Convert.dblToStrPercent(PriceUtil.priceDist(mrkPrice, obInfo.getLgShockWAvg()));
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
