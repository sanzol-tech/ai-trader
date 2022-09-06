package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import api.client.futures.async.PriceListener;
import api.client.futures.async.PriceService;
import api.client.futures.async.DepthClient.DepthMode;
import api.client.futures.async.model.SymbolTickerEvent;
import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.SymbolInfo;
import sanzol.app.service.DepthService;
import sanzol.app.service.LogService;
import sanzol.app.service.Symbol;
import sanzol.app.util.Convert;
import sanzol.app.util.PriceUtil;
import sanzol.lib.util.BeepUtils;
import sanzol.lib.util.ExceptionUtils;

public class FrmCoin extends JFrame implements PriceListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol symbol;
	private DepthService depth = null;
	private boolean beepDone = false;	

	private JPanel contentPane;
	private JLabel lblError;

	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JPanel pnlBB;
	private JPanel pnlWA;

	private JButton btnExport;
	private JButton btnLongBidPointBB1;
	private JButton btnLongBidPointBB2;
	private JButton btnLongBidPointWA1;
	private JButton btnSearch;
	private JButton btnShortBidPointBB1;
	private JButton btnShortBidPointBB2;
	private JButton btnShortBidPointWA1;
	private JButton btnCalcBB;
	private JButton btnCalcWA;	

	private JLabel lnkBinance;
	private JLabel lnkTradingview;

	private JLabel lbl24Hs;
	private JLabel lblAsk;
	private JLabel lblBid;
	private JLabel lblBid_1;
	private JLabel lblDistBB;
	private JLabel lblDistWA;
	private JLabel lblHigh;
	private JLabel lblLongBB;
	private JLabel lblLongWA;
	private JLabel lblLow;
	private JLabel lblPriceBB;
	private JLabel lblPriceWA;
	private JLabel lblShort;
	private JLabel lblShortBB;
	private JLabel lblShortWA;
	private JLabel lblTitlePoints0;
	private JLabel lblTitlePoints2;
	private JLabel lblVolume;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;

	private JTextField txt24h;
	private JTextField txtAskPointBB1;
	private JTextField txtAskPointBB1Dist;
	private JTextField txtAskPointBB2;
	private JTextField txtAskPointBB2Dist;
	private JTextField txtBidPointBB1;
	private JTextField txtBidPointBB1Dist;
	private JTextField txtBidPointBB2;
	private JTextField txtBidPointBB2Dist;
	private JTextField txtHigh;
	private JTextField txtLow;
	private JTextField txtMarkPrice;
	private JTextField txtSymbolInfo;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtVolume;
	private JTextField txtAskPointWA1;
	private JTextField txtAskPointWA1Dist;
	private JTextField txtAskPointWA2;
	private JTextField txtAskPointWA2Dist;
	private JTextField txtAskPointWA3;
	private JTextField txtAskPointWA3Dist;
	private JTextField txtBidPointWA1;
	private JTextField txtBidPointWA1Dist;
	private JTextField txtBidPointWA2;
	private JTextField txtBidPointWA2Dist;
	private JTextField txtBidPointWA3;
	private JTextField txtBidPointWA3Dist;
	private JTextField txtBBBlocks;
	private JTextField txtWADist;

	public FrmCoin()
	{
		initComponents();
		PriceService.attachRefreshObserver(this);
		
		txtBBBlocks.setText(String.valueOf(Config.getBlocksToAnalizeBB()));
		txtWADist.setText("0.05");
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 770, 620);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(30, 550, 700, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);
		
		lblError = new JLabel();
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(lblError, BorderLayout.CENTER);

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(30, 69, 178, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(30, 41, 86, 20);
		contentPane.add(txtSymbolLeft);

		JLabel lblSymbol = new JLabel("SYMBOL");
		lblSymbol.setBounds(30, 22, 86, 14);
		contentPane.add(lblSymbol);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Config.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(122, 41, 86, 20);
		contentPane.add(txtSymbolRight);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setFont(new Font("Courier New", Font.BOLD, 12));
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(430, 284, 147, 20);
		contentPane.add(txtMarkPrice);
		
		txtSymbolInfo = new JTextField();
		txtSymbolInfo.setHorizontalAlignment(SwingConstants.TRAILING);
		txtSymbolInfo.setFont(new Font("Courier New", Font.BOLD, 12));
		txtSymbolInfo.setEditable(false);
		txtSymbolInfo.setColumns(10);
		txtSymbolInfo.setBounds(583, 284, 147, 20);
		contentPane.add(txtSymbolInfo);
		
		scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookAsk.setBounds(430, 54, 300, 220);
		scrollOBookAsk.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollOBookAsk);

		txtOBookAsk = new JTextArea();
		txtOBookAsk.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
		txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookAsk.setEditable(false);
		scrollOBookAsk.setViewportView(txtOBookAsk);

		scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookBid.setBounds(430, 314, 300, 220);
		scrollOBookBid.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollOBookBid);

		txtOBookBid = new JTextArea();
		txtOBookBid.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtOBookBid.setForeground(Styles.COLOR_TEXT_LONG);
		txtOBookBid.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookBid.setEditable(false);
		scrollOBookBid.setViewportView(txtOBookBid);
		
		pnlBB = new JPanel();
		pnlBB.setLayout(null);
		pnlBB.setBorder(UIManager.getBorder("TextField.border"));
		pnlBB.setBounds(30, 156, 370, 144);
		contentPane.add(pnlBB);

		txtAskPointBB1 = new JTextField();
		txtAskPointBB1.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointBB1.setEditable(false);
		txtAskPointBB1.setColumns(10);
		txtAskPointBB1.setBounds(86, 58, 110, 20);
		pnlBB.add(txtAskPointBB1);

		txtAskPointBB1Dist = new JTextField();
		txtAskPointBB1Dist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointBB1Dist.setEditable(false);
		txtAskPointBB1Dist.setColumns(10);
		txtAskPointBB1Dist.setBounds(206, 58, 80, 20);
		pnlBB.add(txtAskPointBB1Dist);

		btnShortBidPointBB1 = new JButton("\u2193");
		btnShortBidPointBB1.setToolTipText("Create Short in this price");
		btnShortBidPointBB1.setOpaque(true);
		btnShortBidPointBB1.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortBidPointBB1.setBounds(296, 58, 46, 22);
		pnlBB.add(btnShortBidPointBB1);

		btnLongBidPointBB1 = new JButton("\u2191");
		btnLongBidPointBB1.setToolTipText("Create Long in this price");
		btnLongBidPointBB1.setOpaque(true);
		btnLongBidPointBB1.setBackground(Styles.COLOR_BTN_LONG);
		btnLongBidPointBB1.setBounds(296, 84, 46, 22);
		pnlBB.add(btnLongBidPointBB1);

		txtBidPointBB1Dist = new JTextField();
		txtBidPointBB1Dist.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointBB1Dist.setEditable(false);
		txtBidPointBB1Dist.setColumns(10);
		txtBidPointBB1Dist.setBounds(206, 84, 80, 20);
		pnlBB.add(txtBidPointBB1Dist);

		txtBidPointBB1 = new JTextField();
		txtBidPointBB1.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointBB1.setEditable(false);
		txtBidPointBB1.setColumns(10);
		txtBidPointBB1.setBounds(86, 84, 110, 20);
		pnlBB.add(txtBidPointBB1);

		lblShortBB = new JLabel("ASK 1");
		lblShortBB.setBounds(24, 58, 52, 14);
		pnlBB.add(lblShortBB);

		lblLongBB = new JLabel("BID 1");
		lblLongBB.setBounds(24, 84, 52, 14);
		pnlBB.add(lblLongBB);

		lblPriceBB = new JLabel("Price");
		lblPriceBB.setBounds(86, 11, 46, 14);
		pnlBB.add(lblPriceBB);

		lblDistBB = new JLabel("Dist %");
		lblDistBB.setBounds(206, 11, 46, 14);
		pnlBB.add(lblDistBB);
		
		txtAskPointBB2 = new JTextField();
		txtAskPointBB2.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointBB2.setEditable(false);
		txtAskPointBB2.setColumns(10);
		txtAskPointBB2.setBounds(86, 32, 110, 20);
		pnlBB.add(txtAskPointBB2);
		
		txtAskPointBB2Dist = new JTextField();
		txtAskPointBB2Dist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointBB2Dist.setEditable(false);
		txtAskPointBB2Dist.setColumns(10);
		txtAskPointBB2Dist.setBounds(206, 32, 80, 20);
		pnlBB.add(txtAskPointBB2Dist);
		
		txtBidPointBB2 = new JTextField();
		txtBidPointBB2.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointBB2.setEditable(false);
		txtBidPointBB2.setColumns(10);
		txtBidPointBB2.setBounds(86, 110, 110, 20);
		pnlBB.add(txtBidPointBB2);
		
		txtBidPointBB2Dist = new JTextField();
		txtBidPointBB2Dist.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointBB2Dist.setEditable(false);
		txtBidPointBB2Dist.setColumns(10);
		txtBidPointBB2Dist.setBounds(206, 110, 80, 20);
		pnlBB.add(txtBidPointBB2Dist);
		
		lblAsk = new JLabel("ASK 2");
		lblAsk.setBounds(24, 35, 52, 14);
		pnlBB.add(lblAsk);
		
		lblBid_1 = new JLabel("BID 2");
		lblBid_1.setBounds(24, 110, 52, 14);
		pnlBB.add(lblBid_1);

		lblTitlePoints0 = new JLabel("Biggest block");
		lblTitlePoints0.setBounds(30, 136, 178, 14);
		contentPane.add(lblTitlePoints0);

		lblTitlePoints2 = new JLabel("Weighted average");
		lblTitlePoints2.setBounds(30, 320, 178, 14);
		contentPane.add(lblTitlePoints2);

		pnlWA = new JPanel();
		pnlWA.setLayout(null);
		pnlWA.setBorder(UIManager.getBorder("TextField.border"));
		pnlWA.setBounds(30, 340, 370, 194);
		contentPane.add(pnlWA);

		txtAskPointWA1 = new JTextField();
		txtAskPointWA1.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA1.setEditable(false);
		txtAskPointWA1.setColumns(10);
		txtAskPointWA1.setBounds(84, 84, 110, 20);
		pnlWA.add(txtAskPointWA1);

		txtAskPointWA1Dist = new JTextField();
		txtAskPointWA1Dist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA1Dist.setEditable(false);
		txtAskPointWA1Dist.setColumns(10);
		txtAskPointWA1Dist.setBounds(204, 84, 80, 20);
		pnlWA.add(txtAskPointWA1Dist);

		txtBidPointWA1Dist = new JTextField();
		txtBidPointWA1Dist.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA1Dist.setEditable(false);
		txtBidPointWA1Dist.setColumns(10);
		txtBidPointWA1Dist.setBounds(204, 110, 80, 20);
		pnlWA.add(txtBidPointWA1Dist);

		txtBidPointWA1 = new JTextField();
		txtBidPointWA1.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA1.setEditable(false);
		txtBidPointWA1.setColumns(10);
		txtBidPointWA1.setBounds(84, 110, 110, 20);
		pnlWA.add(txtBidPointWA1);

		lblLongWA = new JLabel("BID 1");
		lblLongWA.setBounds(22, 110, 52, 14);
		pnlWA.add(lblLongWA);

		lblPriceWA = new JLabel("Price");
		lblPriceWA.setBounds(84, 12, 46, 14);
		pnlWA.add(lblPriceWA);

		lblDistWA = new JLabel("Dist %");
		lblDistWA.setBounds(204, 12, 46, 14);
		pnlWA.add(lblDistWA);
		
		txtBidPointWA2 = new JTextField();
		txtBidPointWA2.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA2.setEditable(false);
		txtBidPointWA2.setColumns(10);
		txtBidPointWA2.setBounds(84, 136, 110, 20);
		pnlWA.add(txtBidPointWA2);
		
		txtBidPointWA2Dist = new JTextField();
		txtBidPointWA2Dist.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA2Dist.setEditable(false);
		txtBidPointWA2Dist.setColumns(10);
		txtBidPointWA2Dist.setBounds(204, 136, 80, 20);
		pnlWA.add(txtBidPointWA2Dist);

		txtBidPointWA3 = new JTextField();
		txtBidPointWA3.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA3.setEditable(false);
		txtBidPointWA3.setColumns(10);
		txtBidPointWA3.setBounds(84, 162, 110, 20);
		pnlWA.add(txtBidPointWA3);
		
		txtBidPointWA3Dist = new JTextField();
		txtBidPointWA3Dist.setForeground(Styles.COLOR_TEXT_LONG);
		txtBidPointWA3Dist.setEditable(false);
		txtBidPointWA3Dist.setColumns(10);
		txtBidPointWA3Dist.setBounds(204, 162, 80, 20);
		pnlWA.add(txtBidPointWA3Dist);
		
		txtAskPointWA3 = new JTextField();
		txtAskPointWA3.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA3.setEditable(false);
		txtAskPointWA3.setColumns(10);
		txtAskPointWA3.setBounds(84, 32, 110, 20);
		pnlWA.add(txtAskPointWA3);
		
		txtAskPointWA3Dist = new JTextField();
		txtAskPointWA3Dist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA3Dist.setEditable(false);
		txtAskPointWA3Dist.setColumns(10);
		txtAskPointWA3Dist.setBounds(204, 32, 80, 20);
		pnlWA.add(txtAskPointWA3Dist);

		txtAskPointWA2 = new JTextField();
		txtAskPointWA2.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA2.setEditable(false);
		txtAskPointWA2.setColumns(10);
		txtAskPointWA2.setBounds(84, 58, 110, 20);
		pnlWA.add(txtAskPointWA2);
		
		txtAskPointWA2Dist = new JTextField();
		txtAskPointWA2Dist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtAskPointWA2Dist.setEditable(false);
		txtAskPointWA2Dist.setColumns(10);
		txtAskPointWA2Dist.setBounds(204, 58, 80, 20);
		pnlWA.add(txtAskPointWA2Dist);
		
		btnShortBidPointBB2 = new JButton("\u2193");
		btnShortBidPointBB2.setToolTipText("Create Short in this price");
		btnShortBidPointBB2.setOpaque(true);
		btnShortBidPointBB2.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortBidPointBB2.setBounds(296, 31, 46, 22);
		pnlBB.add(btnShortBidPointBB2);

		btnLongBidPointBB2 = new JButton("\u2191");
		btnLongBidPointBB2.setToolTipText("Create Long in this price");
		btnLongBidPointBB2.setOpaque(true);
		btnLongBidPointBB2.setBackground(Styles.COLOR_BTN_LONG);
		btnLongBidPointBB2.setBounds(296, 110, 46, 22);
		pnlBB.add(btnLongBidPointBB2);

		btnShortBidPointWA1 = new JButton("\u2193");
		btnShortBidPointWA1.setToolTipText("Create Short in this price");
		btnShortBidPointWA1.setOpaque(true);
		btnShortBidPointWA1.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortBidPointWA1.setBounds(294, 84, 46, 22);
		pnlWA.add(btnShortBidPointWA1);

		btnLongBidPointWA1 = new JButton("\u2191");
		btnLongBidPointWA1.setToolTipText("Create Long in this price");
		btnLongBidPointWA1.setOpaque(true);
		btnLongBidPointWA1.setBackground(Styles.COLOR_BTN_LONG);
		btnLongBidPointWA1.setBounds(294, 110, 46, 22);
		pnlWA.add(btnLongBidPointWA1);
		
		lblShortWA = new JLabel("ASK 1");
		lblShortWA.setBounds(22, 84, 52, 14);
		pnlWA.add(lblShortWA);

		lblShort = new JLabel("ASK 3");
		lblShort.setBounds(22, 32, 52, 14);
		pnlWA.add(lblShort);

		lblShort = new JLabel("ASK 2");
		lblShort.setBounds(22, 58, 52, 14);
		pnlWA.add(lblShort);
		
		lblBid = new JLabel("BID 2");
		lblBid.setBounds(22, 136, 52, 14);
		pnlWA.add(lblBid);

		lblBid = new JLabel("BID 3");
		lblBid.setBounds(22, 162, 52, 14);
		pnlWA.add(lblBid);

		btnExport = new JButton("Export");
		btnExport.setOpaque(true);
		btnExport.setBounds(430, 24, 87, 22);
		contentPane.add(btnExport);

		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(240, 25, 62, 14);
		contentPane.add(lbl24Hs);

		lblVolume = new JLabel("Volume");
		lblVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		lblVolume.setBounds(242, 50, 62, 14);
		contentPane.add(lblVolume);

		lblHigh = new JLabel("HIGH");
		lblHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHigh.setBounds(240, 75, 62, 14);
		contentPane.add(lblHigh);
		
		lblLow = new JLabel("LOW");
		lblLow.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLow.setBounds(240, 100, 62, 14);
		contentPane.add(lblLow);

		txt24h = new JTextField();
		txt24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(314, 22, 86, 20);
		contentPane.add(txt24h);

		txtVolume = new JTextField();
		txtVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume.setForeground(Styles.COLOR_TEXT_ALT1);
		txtVolume.setEditable(false);
		txtVolume.setColumns(10);
		txtVolume.setBounds(314, 47, 86, 20);
		contentPane.add(txtVolume);

		txtLow = new JTextField();
		txtLow.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtLow.setEditable(false);
		txtLow.setColumns(10);
		txtLow.setBounds(314, 97, 86, 20);
		contentPane.add(txtLow);
		
		txtHigh = new JTextField();
		txtHigh.setHorizontalAlignment(SwingConstants.TRAILING);
		txtHigh.setForeground(Styles.COLOR_TEXT_ALT1);
		txtHigh.setEditable(false);
		txtHigh.setColumns(10);
		txtHigh.setBounds(314, 72, 86, 20);
		contentPane.add(txtHigh);
		
		lnkBinance = new JLabel("Binance");
		lnkBinance.setForeground(Styles.COLOR_LINK);
		lnkBinance.setBounds(30, 96, 87, 14);
		lnkBinance.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBinance);

		lnkTradingview = new JLabel("TradingView");
		lnkTradingview.setHorizontalAlignment(SwingConstants.TRAILING);
		lnkTradingview.setForeground(Styles.COLOR_LINK);
		lnkTradingview.setBounds(121, 96, 87, 14);
		lnkTradingview.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkTradingview);

		txtBBBlocks = new JTextField();
		txtBBBlocks.setText("0");
		txtBBBlocks.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBBBlocks.setBounds(300, 131, 50, 20);
		contentPane.add(txtBBBlocks);

		btnCalcBB = new JButton(Styles.IMAGE_REFRESH);
		btnCalcBB.setOpaque(true);
		btnCalcBB.setBounds(361, 130, 40, 22);
		contentPane.add(btnCalcBB);

		txtWADist = new JTextField();
		txtWADist.setText("0");
		txtWADist.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWADist.setBounds(300, 315, 50, 20);
		contentPane.add(txtWADist);
		
		btnCalcWA = new JButton(Styles.IMAGE_REFRESH);
		btnCalcWA.setOpaque(true);
		btnCalcWA.setBounds(361, 314, 40, 22);
		contentPane.add(btnCalcWA);

		// ---------------------------------------------------------------------

		FrmCoin thisFrm = this;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PriceService.deattachRefreshObserver(thisFrm);
			}
		});

		lnkBinance.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://www.binance.com/es/futures/" + symbol.getName()));
				} catch (Exception ex) {
					LogService.error(ex);
				}
			}
		});

		lnkTradingview.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://es.tradingview.com/chart/?symbol=BINANCE%3A" + symbol.getName()));
				} catch (Exception ex) {
					LogService.error(ex);
				}
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		
		btnShortBidPointBB2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", txtAskPointBB2.getText(), false);
			}
		});
		btnShortBidPointBB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", txtAskPointBB1.getText(), false);
			}
		});
		btnLongBidPointBB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "LONG", txtBidPointBB1.getText(), false);
			}
		});
		btnLongBidPointBB2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "LONG", txtBidPointBB2.getText(), false);
			}
		});
		btnShortBidPointWA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", txtAskPointWA1.getText(), false);
			}
		});
		btnLongBidPointWA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "LONG", txtBidPointWA1.getText(), false);
			}
		});
		
		
		btnCalcWA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BigDecimal dist = new BigDecimal(txtWADist.getText());
				depth.calcWAvg2(dist);
				loadPoint();
			}
		});

		btnCalcBB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BigDecimal blocks = new BigDecimal(txtBBBlocks.getText());
				depth.calcClassic(blocks);
				loadPoint();
			}
		});

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		
	}

	// ----------------------------------------------------------------------------------

	@Override
	public void onPriceUpdate()
	{
		try
		{
			if (symbol != null)
			{
				SymbolTickerEvent symbolTicker = PriceService.getSymbolTickerEvent(symbol);
				if (symbolTicker != null)
				{
					SymbolInfo symbolInfo = new SymbolInfo(symbolTicker);
					
					BigDecimal mrkPrice = symbolInfo.getLastPrice();
					txtMarkPrice.setText(symbol.priceToStr(mrkPrice));
					txtSymbolInfo.setText(symbolInfo.isBestShort() ? "WAVG HIGH" : symbolInfo.isBestLong() ? "WAVG LOW" : "");

					String priceChangePercent = String.format("%.2f", symbolInfo.getPriceChangePercent());
					txt24h.setText(priceChangePercent);
					
					BigDecimal usdVolume24h = symbolInfo.getUsdVolume();
					txtVolume.setText(PriceUtil.cashFormat(usdVolume24h));

					txtHigh.setText(symbolInfo.getHigh().toPlainString());
					txtLow.setText(symbolInfo.getLow().toPlainString());

					getDistances();
				}
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	private void search()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbolLeft = txtSymbolLeft.getText();
			symbol = Symbol.getInstance(Symbol.getFullSymbol(symbolLeft));
			
			if (symbol != null)
			{
				setTitle(TITLE + " - " + symbol.getNameLeft());

				// ----------------------------------------------------------------
				depth = DepthService.getInstance(symbol).request(DepthMode.snapshot_only, 0).calc();
				// ----------------------------------------------------------------

				loadOBook();
				loadPoint();
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

	private void loadOBook() throws KeyManagementException, NoSuchAlgorithmException
	{
		txtOBookAsk.setText(depth.printAsksGrp());
		txtOBookBid.setText(depth.printBidsGrp());
		txtOBookBid.setCaretPosition(0);
	}

	private void loadPoint()
	{
		txtAskPointBB2.setText(symbol.priceToStr(depth.getAskBBlkPoint2()));
		txtAskPointBB1.setText(symbol.priceToStr(depth.getAskBBlkPoint1()));
		txtBidPointBB1.setText(symbol.priceToStr(depth.getBidBBlkPoint1()));
		txtBidPointBB2.setText(symbol.priceToStr(depth.getBidBBlkPoint2()));

		txtAskPointWA3.setText(symbol.priceToStr(depth.getAskWAvgPoint3()));
		txtAskPointWA2.setText(symbol.priceToStr(depth.getAskWAvgPoint2()));
		txtAskPointWA1.setText(symbol.priceToStr(depth.getAskWAvgPoint1()));
		
		txtBidPointWA1.setText(symbol.priceToStr(depth.getBidWAvgPoint1()));
		txtBidPointWA2.setText(symbol.priceToStr(depth.getBidWAvgPoint2()));
		txtBidPointWA3.setText(symbol.priceToStr(depth.getBidWAvgPoint3()));
	}

	private void getDistances()
	{
		if (depth == null)
		{
			return;	
		}

		try
		{
			BigDecimal mrkPrice = PriceService.getLastPrice(symbol);

			BigDecimal distSh2 = PriceUtil.priceDistUp(mrkPrice, depth.getAskBBlkPoint2(), false);
			txtAskPointBB2Dist.setText(Convert.dblToStrPercent(distSh2) + " %");

			BigDecimal distSh1 = PriceUtil.priceDistUp(mrkPrice, depth.getAskBBlkPoint1(), false);
			txtAskPointBB1Dist.setText(Convert.dblToStrPercent(distSh1) + " %");
			
			BigDecimal distLg1 = PriceUtil.priceDistDown(mrkPrice, depth.getBidBBlkPoint1(), false);
			txtBidPointBB1Dist.setText(Convert.dblToStrPercent(distLg1) + " %");
			
			BigDecimal distLg2 = PriceUtil.priceDistDown(mrkPrice, depth.getBidBBlkPoint2(), false);
			txtBidPointBB2Dist.setText(Convert.dblToStrPercent(distLg2) + " %");

			BigDecimal distShWA3 = PriceUtil.priceDistUp(mrkPrice, depth.getAskWAvgPoint3(), false);
			txtAskPointWA3Dist.setText(Convert.dblToStrPercent(distShWA3) + " %");

			BigDecimal distShWA2 = PriceUtil.priceDistUp(mrkPrice, depth.getAskWAvgPoint2(), false);
			txtAskPointWA2Dist.setText(Convert.dblToStrPercent(distShWA2) + " %");

			BigDecimal distShWA1 = PriceUtil.priceDistUp(mrkPrice, depth.getAskWAvgPoint1(), false);
			txtAskPointWA1Dist.setText(Convert.dblToStrPercent(distShWA1) + " %");

			BigDecimal distLgWA1 = PriceUtil.priceDistDown(mrkPrice, depth.getBidWAvgPoint1(), false);
			txtBidPointWA1Dist.setText(Convert.dblToStrPercent(distLgWA1) + " %");

			BigDecimal distLgWA2 = PriceUtil.priceDistDown(mrkPrice, depth.getBidWAvgPoint2(), false);
			txtBidPointWA2Dist.setText(Convert.dblToStrPercent(distLgWA2) + " %");

			BigDecimal distLgWA3 = PriceUtil.priceDistDown(mrkPrice, depth.getBidWAvgPoint3(), false);
			txtBidPointWA3Dist.setText(Convert.dblToStrPercent(distLgWA3) + " %");

			// ----------------------------------------------------------------
			// ----------------------------------------------------------------
			BigDecimal distAsk1To2 = PriceUtil.priceDistUp(depth.getAskFixedPoint1(), depth.getAskFixedPoint2(), false);
			BigDecimal distBid1To2 = PriceUtil.priceDistUp(depth.getBidFixedPoint1(), depth.getBidFixedPoint2(), false);

			BigDecimal distShortLong = PriceUtil.priceDistDown(depth.getAskFixedPoint1(), depth.getBidFixedPoint1(), false);
			BigDecimal distLongShort = PriceUtil.priceDistUp(depth.getBidFixedPoint1(), depth.getAskFixedPoint1(), false);

			BigDecimal distShort = PriceUtil.priceDistUp(mrkPrice, depth.getAskFixedPoint1(), false);
			BigDecimal distLong = PriceUtil.priceDistDown(mrkPrice, depth.getBidFixedPoint1(), false);

			if (distShort.doubleValue() <= 0.0025 && distShortLong.doubleValue() >= 0.01 && distAsk1To2.doubleValue() < distShortLong.doubleValue())
			{
				if(!beepDone)
				{
					INFO(String.format("SHORT %f", depth.getAskFixedPoint1()));

					BeepUtils.beep();
					beepDone = true;
				}
			}
			else if (distLong.doubleValue() <= 0.0025 && distLongShort.doubleValue() >= 0.01 && distBid1To2.doubleValue() < distLongShort.doubleValue())
			{
				if(!beepDone)
				{
					INFO(String.format("LONG %f", depth.getBidFixedPoint1()));

					BeepUtils.beep();
					beepDone = true;
				}
			}

			// ----------------------------------------------------------------
			// ----------------------------------------------------------------

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
			depth.export();

			File file = new File(Constants.DEFAULT_EXPORT_FOLDER);
			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);

			INFO("Exported to " + Constants.DEFAULT_EXPORT_FOLDER.toString());
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

	// ----------------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(ExceptionUtils.getMessage(e));
		e.printStackTrace();
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
		launch();
	}
}
