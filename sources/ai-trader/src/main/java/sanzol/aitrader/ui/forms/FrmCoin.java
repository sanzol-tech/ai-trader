package sanzol.aitrader.ui.forms;

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
import java.math.RoundingMode;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import api.client.impl.config.ApiConfig;
import api.client.model.DepthEntry;
import api.client.model.event.SymbolTickerEvent;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.enums.DepthMode;
import sanzol.aitrader.be.enums.GridStrategy;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.model.SymbolInfo;
import sanzol.aitrader.be.service.DepthCache;
import sanzol.aitrader.be.service.OrderBookService;
import sanzol.aitrader.be.service.OrderBookService.BBType;
import sanzol.aitrader.be.service.OrderBookService.WAType;
import sanzol.aitrader.be.service.PriceListener;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.ui.config.Styles;
import sanzol.aitrader.ui.controls.CtrlError;
import sanzol.util.BeepUtils;
import sanzol.util.Convert;
import sanzol.util.log.LogService;
import sanzol.util.price.PriceUtil;

public class FrmCoin extends JFrame implements PriceListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol symbol;
	private OrderBookService obService = null;
	private boolean beepDone = false;

	private JPanel contentPane;
	private CtrlError ctrlError;

	private JPanel pnlBB;
	private JPanel pnlWA;

	private JButton btnExport;
	private JButton btnTechnical;
	private JButton btnAsyncCache;
	private JButton btnLongBidPointBB1;
	private JButton btnLongBidPointWA1;
	private JButton btnSearch;
	private JButton btnShortBidPointBB1;
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

	private JLabel lblRatioBBSh;
	private JLabel lblRatioBBLg;
	private JLabel lblRatioWASh;
	private JLabel lblRatioWALg;

	private JRadioButton rbBBClassic;
	private JRadioButton rbBBDisplaced;
	private JRadioButton rbWAPrice;
	private JRadioButton rbWAPrevious;

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
	private JTextField txtMarkPrice1;
	private JTextField txtMarkPrice2;
	private JTextField txtStochastic;
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
	private JTextField txtWABlocks;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;
	private JTextArea txtOBookAsk2;
	private JTextArea txtOBookBid2;
	private JTextArea txtOBookAsk3;
	private JTextArea txtOBookBid3;
	
	public FrmCoin()
	{
		initComponents();
		PriceService.attachRefreshObserver(this);

		txtBBBlocks.setText(String.valueOf(Config.getBlocksToAnalizeBB()));
		txtWABlocks.setText(String.valueOf(Config.getBlocksToAnalizeWA()));
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 842, 700);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(30, 630, 765, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);

		ctrlError = new CtrlError();
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(ctrlError, BorderLayout.CENTER);

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(30, 69, 133, 22);
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

		pnlBB = new JPanel();
		pnlBB.setLayout(null);
		pnlBB.setBorder(UIManager.getBorder("TextField.border"));
		pnlBB.setBounds(30, 198, 370, 144);
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
		lblTitlePoints0.setBounds(30, 176, 86, 16);
		contentPane.add(lblTitlePoints0);

		lblTitlePoints2 = new JLabel("Weighted average");
		lblTitlePoints2.setBounds(30, 360, 100, 16);
		contentPane.add(lblTitlePoints2);

		pnlWA = new JPanel();
		pnlWA.setLayout(null);
		pnlWA.setBorder(UIManager.getBorder("TextField.border"));
		pnlWA.setBounds(30, 382, 370, 194);
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

		lblRatioBBSh = new JLabel("1:x");
		lblRatioBBSh.setHorizontalAlignment(SwingConstants.CENTER);
		lblRatioBBSh.setForeground(Styles.COLOR_TEXT_SHORT);
		lblRatioBBSh.setBounds(296, 35, 46, 14);
		pnlBB.add(lblRatioBBSh);

		lblRatioBBLg = new JLabel("1:x");
		lblRatioBBLg.setHorizontalAlignment(SwingConstants.CENTER);
		lblRatioBBLg.setForeground(Styles.COLOR_TEXT_LONG);
		lblRatioBBLg.setBounds(296, 113, 46, 14);
		pnlBB.add(lblRatioBBLg);

		lblRatioWASh = new JLabel("1:x");
		lblRatioWASh.setHorizontalAlignment(SwingConstants.CENTER);
		lblRatioWASh.setForeground(Styles.COLOR_TEXT_SHORT);
		lblRatioWASh.setBounds(294, 61, 46, 14);
		pnlWA.add(lblRatioWASh);

		lblRatioWALg = new JLabel("1:x");
		lblRatioWALg.setHorizontalAlignment(SwingConstants.CENTER);
		lblRatioWALg.setForeground(Styles.COLOR_TEXT_LONG);
		lblRatioWALg.setBounds(294, 139, 46, 14);
		pnlWA.add(lblRatioWALg);

		btnExport = new JButton("Export");
		btnExport.setOpaque(true);
		btnExport.setBounds(425, 582, 120, 22);
		contentPane.add(btnExport);

		btnTechnical = new JButton(Styles.IMAGE_CHART);
		btnTechnical.setToolTipText("Technical indicators");
		btnTechnical.setOpaque(true);
		btnTechnical.setBounds(168, 69, 40, 22);
		contentPane.add(btnTechnical);

		btnAsyncCache = new JButton("Async cache");
		btnAsyncCache.setOpaque(true);
		btnAsyncCache.setBounds(675, 582, 120, 22);
		contentPane.add(btnAsyncCache);

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
		
		JLabel lblStochastic = new JLabel("Stochastic");
		lblStochastic.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStochastic.setBounds(240, 125, 62, 14);
		contentPane.add(lblStochastic);

		txtStochastic = new JTextField();
		txtStochastic.setBounds(314, 122, 86, 20);
		txtStochastic.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic.setForeground(Styles.COLOR_TEXT_ALT1);
		txtStochastic.setEditable(false);
		txtStochastic.setColumns(10);
		contentPane.add(txtStochastic);
		
		lnkBinance = new JLabel("Binance");
		lnkBinance.setForeground(Styles.COLOR_LINK);
		lnkBinance.setBounds(30, 102, 133, 16);
		lnkBinance.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBinance);

		lnkTradingview = new JLabel("TradingView");
		lnkTradingview.setForeground(Styles.COLOR_LINK);
		lnkTradingview.setBounds(30, 123, 133, 16);
		lnkTradingview.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkTradingview);

		rbBBClassic = new JRadioButton("classic");
		rbBBClassic.setToolTipText("round number blocks");
		rbBBClassic.setHorizontalAlignment(SwingConstants.TRAILING);
		rbBBClassic.setSelected(true);
		rbBBClassic.setBounds(136, 172, 76, 23);
		contentPane.add(rbBBClassic);

		rbBBDisplaced = new JRadioButton("displaced");
		rbBBDisplaced.setToolTipText("blocks from last price");
		rbBBDisplaced.setHorizontalAlignment(SwingConstants.TRAILING);
		rbBBDisplaced.setBounds(216, 172, 76, 23);
		contentPane.add(rbBBDisplaced);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(rbBBClassic);
		bg1.add(rbBBDisplaced);

		txtBBBlocks = new JTextField();
		txtBBBlocks.setText("0");
		txtBBBlocks.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBBBlocks.setBounds(300, 173, 50, 20);
		contentPane.add(txtBBBlocks);

		btnCalcBB = new JButton(Styles.IMAGE_REFRESH);
		btnCalcBB.setToolTipText("recalculate");
		btnCalcBB.setOpaque(true);
		btnCalcBB.setBounds(360, 172, 40, 22);
		contentPane.add(btnCalcBB);

		rbWAPrice = new JRadioButton("price");
		rbWAPrice.setToolTipText("average since last price");
		rbWAPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		rbWAPrice.setSelected(true);
		rbWAPrice.setBounds(136, 356, 76, 23);
		contentPane.add(rbWAPrice);

		rbWAPrevious = new JRadioButton("previous");
		rbWAPrevious.setToolTipText("average since previous point");
		rbWAPrevious.setHorizontalAlignment(SwingConstants.TRAILING);
		rbWAPrevious.setBounds(216, 356, 76, 23);
		contentPane.add(rbWAPrevious);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(rbWAPrice);
		bg2.add(rbWAPrevious);

		txtWABlocks = new JTextField();
		txtWABlocks.setText("0");
		txtWABlocks.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWABlocks.setBounds(298, 357, 50, 20);
		contentPane.add(txtWABlocks);

		btnCalcWA = new JButton(Styles.IMAGE_REFRESH);
		btnCalcWA.setToolTipText("recalculate");
		btnCalcWA.setOpaque(true);
		btnCalcWA.setBounds(360, 356, 40, 22);
		contentPane.add(btnCalcWA);


		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//tabbedPane.setBorder(UIManager.getBorder("TextField.border"));
		tabbedPane.setBounds(425, 16, 370, 560);
		contentPane.add(tabbedPane);

		JPanel pnlOBook = new JPanel();
		pnlOBook.setBorder(UIManager.getBorder("TextField.border"));
		tabbedPane.addTab("Order Book", null, pnlOBook, null);
		pnlOBook.setLayout(null);
		{
			JScrollPane scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookAsk.setBounds(30, 23, 300, 220);
			pnlOBook.add(scrollOBookAsk);
			scrollOBookAsk.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookAsk = new JTextArea();
			txtOBookAsk.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
			txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookAsk.setEditable(false);
			scrollOBookAsk.setViewportView(txtOBookAsk);

			JScrollPane scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookBid.setBounds(30, 285, 300, 220);
			pnlOBook.add(scrollOBookBid);
			scrollOBookBid.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookBid = new JTextArea();
			txtOBookBid.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookBid.setForeground(Styles.COLOR_TEXT_LONG);
			txtOBookBid.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookBid.setEditable(false);
			scrollOBookBid.setViewportView(txtOBookBid);		

			
			txtMarkPrice1 = new JTextField();
			txtMarkPrice1.setBounds(30, 254, 300, 20);
			txtMarkPrice1.setFont(new Font("Courier New", Font.BOLD, 12));
			txtMarkPrice1.setEditable(false);
			txtMarkPrice1.setForeground(Styles.COLOR_TEXT_ALT1);
			txtMarkPrice1.setColumns(10);
			pnlOBook.add(txtMarkPrice1);
		}

		JPanel pnlBiggest = new JPanel();
		pnlBiggest.setBorder(UIManager.getBorder("TextField.border"));
		tabbedPane.addTab("Biggest", null, pnlBiggest, null);
		pnlBiggest.setLayout(null);
		{
			JScrollPane scrollOBookAsk2 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookAsk2.setBounds(30, 24, 300, 220);
			pnlBiggest.add(scrollOBookAsk2);
			scrollOBookAsk2.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookAsk2 = new JTextArea();
			txtOBookAsk2.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookAsk2.setForeground(Styles.COLOR_TEXT_SHORT);
			txtOBookAsk2.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookAsk2.setEditable(false);
			scrollOBookAsk2.setViewportView(txtOBookAsk2);

			JScrollPane scrollOBookBid2 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookBid2.setBounds(30, 285, 300, 220);
			pnlBiggest.add(scrollOBookBid2);
			scrollOBookBid2.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookBid2 = new JTextArea();
			txtOBookBid2.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookBid2.setForeground(Styles.COLOR_TEXT_LONG);
			txtOBookBid2.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookBid2.setEditable(false);
			scrollOBookBid2.setViewportView(txtOBookBid2);

			txtMarkPrice2 = new JTextField();
			txtMarkPrice2.setBounds(30, 254, 300, 20);
			txtMarkPrice2.setFont(new Font("Courier New", Font.BOLD, 12));
			txtMarkPrice2.setEditable(false);
			txtMarkPrice2.setForeground(Styles.COLOR_TEXT_ALT1);
			txtMarkPrice2.setColumns(10);
			pnlBiggest.add(txtMarkPrice2);
		}

		JPanel pnlAsksVsBids = new JPanel();
		pnlAsksVsBids.setBorder(UIManager.getBorder("TextField.border"));
		tabbedPane.addTab("Balance", null, pnlAsksVsBids, null);
		pnlAsksVsBids.setLayout(null);
		{
			JScrollPane scrollOBookAsk3 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookAsk3.setBounds(14, 12, 340, 152);
			pnlAsksVsBids.add(scrollOBookAsk3);
			scrollOBookAsk3.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookAsk3 = new JTextArea();
			txtOBookAsk3.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookAsk3.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookAsk3.setEditable(false);
			scrollOBookAsk3.setViewportView(txtOBookAsk3);

			JScrollPane scrollOBookBid3 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollOBookBid3.setBounds(14, 178, 340, 152);
			pnlAsksVsBids.add(scrollOBookBid3);
			scrollOBookBid3.setBorder(UIManager.getBorder("TextField.border"));

			txtOBookBid3 = new JTextArea();
			txtOBookBid3.setBackground(Styles.COLOR_TEXT_AREA_BG);
			txtOBookBid3.setFont(new Font("Courier New", Font.PLAIN, 12));
			txtOBookBid3.setEditable(false);
			scrollOBookBid3.setViewportView(txtOBookBid3);
		}
		
		
		
		
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
					Desktop.getDesktop().browse(new URI("https://www.binance.com/es/futures/" + symbol.getPair()));
				} catch (Exception ex) {
					LogService.error(ex);
				}
			}
		});

		lnkTradingview.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://es.tradingview.com/chart/?symbol=BINANCE%3A" + symbol.getPair() + "PERP"));
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
		btnShortBidPointBB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", txtAskPointBB1.getText(), GridStrategy.CUSTOM, false);
			}
		});
		btnLongBidPointBB1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "LONG", txtBidPointBB1.getText(), GridStrategy.CUSTOM, false);
			}
		});
		btnShortBidPointWA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "SHORT", txtAskPointWA1.getText(), GridStrategy.CUSTOM, false);
			}
		});
		btnLongBidPointWA1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(symbol.getNameLeft(), "LONG", txtBidPointWA1.getText(), GridStrategy.CUSTOM, false);
			}
		});

		btnCalcBB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					calc();
				}
				catch (Exception ex)
				{
					ctrlError.ERROR(ex);
				}
			}
		});
		btnCalcWA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					calc();
				}
				catch (Exception ex)
				{
					ctrlError.ERROR(ex);
				}
			}
		});

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});

		btnTechnical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				more();
			}
		});

		btnAsyncCache.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        if (symbol != null)
				{
		        	if (btnAsyncCache.getText().endsWith("OFF"))
					{
		        		// Disable
				        if (DepthCache.containsKey(symbol.getPair()))
						{
				        	DepthCache.remove(symbol.getPair());
				        	btnAsyncCache.setText("Async cache ON");
						}
					}
					else
					{
			        	// Enable
				        if (!DepthCache.containsKey(symbol.getPair()))
						{
				        	DepthCache.add(symbol.getPair());
				        	btnAsyncCache.setText("Async cache OFF");
						}
					}
				}
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
					SymbolInfo symbolInfo = SymbolInfo.getInstance(symbolTicker);

					BigDecimal mrkPrice = symbolInfo.getLastPrice();
					txtMarkPrice1.setText(symbol.priceToStr(mrkPrice));
					txtMarkPrice2.setText(symbol.priceToStr(mrkPrice));

					String stochastic = String.format("%.2f %%", symbolInfo.getStochastic());
					txtStochastic.setText(stochastic);

					String priceChangePercent = String.format("%.2f %%", symbolInfo.getPriceChangePercent());
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
			ctrlError.ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	public void calc() throws KeyManagementException, NoSuchAlgorithmException
	{
		BBType bbType = rbBBClassic.isSelected() ? BBType.classic : BBType.displaced;
		Integer bbBlocks = Integer.valueOf(txtBBBlocks.getText());

		WAType waType = rbWAPrice.isSelected() ? WAType.price : WAType.previous;
		Integer waBlocks = Integer.valueOf(txtWABlocks.getText());

		obService.calc(bbType, bbBlocks, waType, waBlocks);

		loadOBook();
		loadPoints();
	}

	private void search()
	{
		ctrlError.CLEAN();

		btnTechnical.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbolLeft = txtSymbolLeft.getText();
			String symbolRight = txtSymbolRight.getText();
			symbol = Symbol.fromPair(symbolLeft + symbolRight);

			if (symbol != null)
			{
				setTitle(TITLE + " - " + symbol.getNameLeft());

				// ----------------------------------------------------------------
				obService = OrderBookService.getInstance(symbol).request(DepthMode.snapshot_only, 0);
				// ----------------------------------------------------------------

				calc();

				boolean isInCache = DepthCache.containsKey(symbol.getPair());
				btnAsyncCache.setText("Async cache " + (isInCache ? "OFF" : "ON"));
			}
			else
			{
				ctrlError.ERROR("Symbol not found");
			}
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
		finally
		{
			btnTechnical.setEnabled(true);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}		
	}

	private void loadOBook() throws KeyManagementException, NoSuchAlgorithmException
	{
		txtOBookAsk.setText(obService.printAsksGrp());
		txtOBookBid.setText(obService.printBidsGrp());
		txtOBookBid.setCaretPosition(0);
		
		StringBuilder sb = new StringBuilder();
		sb.append("PRICE                    QTY      DIST\n");
		sb.append("--------------------------------------\n");
		List<DepthEntry> listSa = obService.searchSuperAskBlocks(12);
		sb.append(obService.printSuperBlks(listSa));
		txtOBookAsk2.setText(sb.toString());
		
		sb = new StringBuilder();
		sb.append("PRICE                    QTY      DIST\n");
		sb.append("--------------------------------------\n");
		List<DepthEntry> listSb = obService.searchSuperBidBlocks(12);
		sb.append(obService.printSuperBlks(listSb));
		txtOBookBid2.setText(sb.toString());

		sb = new StringBuilder();
		sb.append("Asks vs Bids - Coin\n");
		sb.append("-------------------------------------------\n");
		sb.append("   dist         asks      diff         bids\n");
		sb.append("-------------------------------------------\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.03), false)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.05), false)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.15), false)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.25), false)).append("\n");
		sb.append(obService.calcDepthDiff(null, false));
		txtOBookAsk3.setText(sb.toString());

		sb = new StringBuilder();
		sb.append("Asks vs Bids - USD\n");
		sb.append("-------------------------------------------\n");
		sb.append("   dist         asks      diff         bids\n");
		sb.append("-------------------------------------------\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.03), true)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.05), true)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.15), true)).append("\n");
		sb.append(obService.calcDepthDiff(BigDecimal.valueOf(0.25), true)).append("\n");
		sb.append(obService.calcDepthDiff(null, true));
		txtOBookBid3.setText(sb.toString());
	}

	private void loadPoints()
	{
		txtAskPointBB2.setText(symbol.priceToStr(obService.getAskBBlkPoint2()));
		txtAskPointBB1.setText(symbol.priceToStr(obService.getAskBBlkPoint1()));
		txtBidPointBB1.setText(symbol.priceToStr(obService.getBidBBlkPoint1()));
		txtBidPointBB2.setText(symbol.priceToStr(obService.getBidBBlkPoint2()));

		txtAskPointWA3.setText(symbol.priceToStr(obService.getAskWAvgPoint3()));
		txtAskPointWA2.setText(symbol.priceToStr(obService.getAskWAvgPoint2()));
		txtAskPointWA1.setText(symbol.priceToStr(obService.getAskWAvgPoint1()));

		txtBidPointWA1.setText(symbol.priceToStr(obService.getBidWAvgPoint1()));
		txtBidPointWA2.setText(symbol.priceToStr(obService.getBidWAvgPoint2()));
		txtBidPointWA3.setText(symbol.priceToStr(obService.getBidWAvgPoint3()));

		loadRatiosBB(obService.getAskBBlkPoint1(), obService.getBidBBlkPoint1(), obService.getAskBBlkPoint2(), obService.getBidBBlkPoint2());
		loadRatiosWA(obService.getAskWAvgPoint1(), obService.getBidWAvgPoint1(), obService.getAskWAvgPoint2(), obService.getBidWAvgPoint2());
	}

	public void loadRatiosBB(BigDecimal shortPrice, BigDecimal longPrice, BigDecimal shShock2, BigDecimal lgShock2)
	{
		BigDecimal shortTProfit = PriceUtil.priceDistDown(shortPrice, longPrice, true);
		BigDecimal longTProfit = PriceUtil.priceDistUp(longPrice, shortPrice, true);

		BigDecimal shortSLoss = PriceUtil.priceDistDown(shortPrice, shShock2, true);
		BigDecimal longTSLoss = PriceUtil.priceDistUp(longPrice, lgShock2, true);

		if (shortSLoss.compareTo(BigDecimal.ZERO) == 0)
		{
			lblRatioBBSh.setText("1:0");
		}
		else
		{
			lblRatioBBSh.setText("1:" + shortTProfit.divide(shortSLoss, 1, RoundingMode.HALF_UP).abs());
		}

		if (longTSLoss.compareTo(BigDecimal.ZERO) == 0)
		{
			lblRatioBBLg.setText("1:0");
		}
		else
		{
			lblRatioBBLg.setText("1:" + longTProfit.divide(longTSLoss, 1, RoundingMode.HALF_UP).abs());
		}
	}

	public void loadRatiosWA(BigDecimal shortPrice, BigDecimal longPrice, BigDecimal shShock2, BigDecimal lgShock2)
	{
		BigDecimal shortTProfit = PriceUtil.priceDistDown(shortPrice, longPrice, true);
		BigDecimal longTProfit = PriceUtil.priceDistUp(longPrice, shortPrice, true);

		BigDecimal shortSLoss = PriceUtil.priceDistDown(shortPrice, shShock2, true);
		BigDecimal longTSLoss = PriceUtil.priceDistUp(longPrice, lgShock2, true);

		if (shortSLoss.compareTo(BigDecimal.ZERO) == 0)
		{
			lblRatioWASh.setText("1:0");
		}
		else
		{
			lblRatioWASh.setText("1:" + shortTProfit.divide(shortSLoss, 1, RoundingMode.HALF_UP).abs());
		}

		if (longTSLoss.compareTo(BigDecimal.ZERO) == 0)
		{
			lblRatioWALg.setText("1:0");
		}
		else
		{
			lblRatioWALg.setText("1:" + longTProfit.divide(longTSLoss, 1, RoundingMode.HALF_UP).abs());
		}
	}

	private void getDistances()
	{
		if (obService == null)
		{
			return;
		}

		try
		{
			BigDecimal mrkPrice = PriceService.getLastPrice(symbol);

			BigDecimal distSh2 = PriceUtil.priceDistUp(mrkPrice, obService.getAskBBlkPoint2(), false);
			txtAskPointBB2Dist.setText(Convert.toStrPercent(distSh2) + " %");

			BigDecimal distSh1 = PriceUtil.priceDistUp(mrkPrice, obService.getAskBBlkPoint1(), false);
			txtAskPointBB1Dist.setText(Convert.toStrPercent(distSh1) + " %");

			BigDecimal distLg1 = PriceUtil.priceDistDown(mrkPrice, obService.getBidBBlkPoint1(), false);
			txtBidPointBB1Dist.setText(Convert.toStrPercent(distLg1) + " %");

			BigDecimal distLg2 = PriceUtil.priceDistDown(mrkPrice, obService.getBidBBlkPoint2(), false);
			txtBidPointBB2Dist.setText(Convert.toStrPercent(distLg2) + " %");

			BigDecimal distShWA3 = PriceUtil.priceDistUp(mrkPrice, obService.getAskWAvgPoint3(), false);
			txtAskPointWA3Dist.setText(Convert.toStrPercent(distShWA3) + " %");

			BigDecimal distShWA2 = PriceUtil.priceDistUp(mrkPrice, obService.getAskWAvgPoint2(), false);
			txtAskPointWA2Dist.setText(Convert.toStrPercent(distShWA2) + " %");

			BigDecimal distShWA1 = PriceUtil.priceDistUp(mrkPrice, obService.getAskWAvgPoint1(), false);
			txtAskPointWA1Dist.setText(Convert.toStrPercent(distShWA1) + " %");

			BigDecimal distLgWA1 = PriceUtil.priceDistDown(mrkPrice, obService.getBidWAvgPoint1(), false);
			txtBidPointWA1Dist.setText(Convert.toStrPercent(distLgWA1) + " %");

			BigDecimal distLgWA2 = PriceUtil.priceDistDown(mrkPrice, obService.getBidWAvgPoint2(), false);
			txtBidPointWA2Dist.setText(Convert.toStrPercent(distLgWA2) + " %");

			BigDecimal distLgWA3 = PriceUtil.priceDistDown(mrkPrice, obService.getBidWAvgPoint3(), false);
			txtBidPointWA3Dist.setText(Convert.toStrPercent(distLgWA3) + " %");

			// ----------------------------------------------------------------
			// ----------------------------------------------------------------
			BigDecimal distAsk1To2 = PriceUtil.priceDistUp(obService.getAskFixedPoint1(), obService.getAskFixedPoint2(), false);
			BigDecimal distBid1To2 = PriceUtil.priceDistUp(obService.getBidFixedPoint1(), obService.getBidFixedPoint2(), false);

			BigDecimal distShortLong = PriceUtil.priceDistDown(obService.getAskFixedPoint1(), obService.getBidFixedPoint1(), false);
			BigDecimal distLongShort = PriceUtil.priceDistUp(obService.getBidFixedPoint1(), obService.getAskFixedPoint1(), false);

			BigDecimal distShort = PriceUtil.priceDistUp(mrkPrice, obService.getAskFixedPoint1(), false);
			BigDecimal distLong = PriceUtil.priceDistDown(mrkPrice, obService.getBidFixedPoint1(), false);

			if (distShort.doubleValue() <= 0.0025 && distShortLong.doubleValue() >= 0.01 && distAsk1To2.doubleValue() < distShortLong.doubleValue())
			{
				if(!beepDone)
				{
					ctrlError.INFO(String.format("SHORT %f", obService.getAskFixedPoint1()));

					BeepUtils.beep();
					beepDone = true;
				}
			}
			else if (distLong.doubleValue() <= 0.0025 && distLongShort.doubleValue() >= 0.01 && distBid1To2.doubleValue() < distLongShort.doubleValue())
			{
				if(!beepDone)
				{
					ctrlError.INFO(String.format("LONG %f", obService.getBidFixedPoint1()));

					BeepUtils.beep();
					beepDone = true;
				}
			}

			// ----------------------------------------------------------------
			// ----------------------------------------------------------------

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void more()
	{
		FrmCoinTech.launch(symbol);
	}

	// ----------------------------------------------------------------------------------

	public void export()
	{
		try
		{
			obService.export();

			File basepath = new File(Constants.DEFAULT_USER_FOLDER, ApiConfig.MARKET_TYPE.toString());
			File path = new File(basepath, Constants.DEFAULT_EXPORT_FOLDER);

			Desktop desktop = Desktop.getDesktop();
			desktop.open(path);

			ctrlError.INFO("Exported to " + path.getAbsolutePath());
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
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
}
