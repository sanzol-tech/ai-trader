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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.math.BigDecimal;

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

import com.binance.client.model.event.SymbolTickerEvent;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.listener.PriceListener;
import sanzol.app.service.OBookService;
import sanzol.app.service.Symbol;
import sanzol.app.task.PriceService;
import sanzol.app.util.Convert;
import sanzol.app.util.PriceUtil;
import sanzol.lib.util.BeepUtils;

public class FrmCoin extends JFrame implements PriceListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol coin;
	private OBookService obService = null;
	private boolean beepDone = false;	

	private JPanel contentPane;
	private JLabel lblError;

	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JPanel pnlBB;
	private JPanel pnlFP;
	private JPanel pnlWA;

	private JButton btnSearch;
	private JButton btnRefresh;
	private JButton btnExport;
	private JButton btnLongShockBB;
	private JButton btnLongShockFP;
	private JButton btnLongShockWA;
	private JButton btnShortShockBB;
	private JButton btnShortShockFP;
	private JButton btnShortShockWA;

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
	private JTextField txtLongDistFP;
	private JTextField txtLongDistWA;
	private JTextField txtLongPrice;
	private JTextField txtLongPriceFP;
	private JTextField txtLongPriceWA;
	private JTextField txtLow;
	private JTextField txtShortDist;
	private JTextField txtShortDistFP;
	private JTextField txtShortDistWA;
	private JTextField txtShortPrice;
	private JTextField txtShortPriceFP;
	private JTextField txtShortPriceWA;
	private JTextField txtVolume;

	public FrmCoin()
	{
		initComponents();
		PriceService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
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
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);
		
		lblError = new JLabel();
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(lblError, BorderLayout.CENTER);
		
		btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(30, 69, 178, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(30, 41, 86, 20);
		contentPane.add(txtSymbolLeft);

		JLabel lblSymbol = new JLabel("COIN");
		lblSymbol.setBounds(30, 22, 86, 14);
		contentPane.add(lblSymbol);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(122, 41, 86, 20);
		contentPane.add(txtSymbolRight);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setFont(new Font("Courier New", Font.BOLD, 12));
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(440, 270, 360, 20);
		contentPane.add(txtMarkPrice);
		
		scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookAsk.setBounds(440, 54, 360, 210);
		scrollOBookAsk.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollOBookAsk);

		txtOBookAsk = new JTextArea();
		txtOBookAsk.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
		txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookAsk.setEditable(false);
		scrollOBookAsk.setViewportView(txtOBookAsk);

		scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookBid.setBounds(440, 296, 360, 210);
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
		pnlBB.setBounds(30, 144, 387, 95);
		contentPane.add(pnlBB);

		txtShortPrice = new JTextField();
		txtShortPrice.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPrice.setEditable(false);
		txtShortPrice.setColumns(10);
		txtShortPrice.setBounds(86, 32, 110, 20);
		pnlBB.add(txtShortPrice);

		txtShortDist = new JTextField();
		txtShortDist.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDist.setEditable(false);
		txtShortDist.setColumns(10);
		txtShortDist.setBounds(206, 32, 80, 20);
		pnlBB.add(txtShortDist);

		btnShortShockBB = new JButton("\u2193");
		btnShortShockBB.setToolTipText("Create Short in this price");
		btnShortShockBB.setOpaque(true);
		btnShortShockBB.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShockBB.setBounds(296, 31, 46, 22);
		pnlBB.add(btnShortShockBB);

		btnLongShockBB = new JButton("\u2191");
		btnLongShockBB.setToolTipText("Create Long in this price");
		btnLongShockBB.setOpaque(true);
		btnLongShockBB.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShockBB.setBounds(296, 62, 46, 22);
		pnlBB.add(btnLongShockBB);

		txtLongDist = new JTextField();
		txtLongDist.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDist.setEditable(false);
		txtLongDist.setColumns(10);
		txtLongDist.setBounds(206, 63, 80, 20);
		pnlBB.add(txtLongDist);

		txtLongPrice = new JTextField();
		txtLongPrice.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPrice.setEditable(false);
		txtLongPrice.setColumns(10);
		txtLongPrice.setBounds(86, 63, 110, 20);
		pnlBB.add(txtLongPrice);

		lblShortBB = new JLabel("SHORT");
		lblShortBB.setBounds(24, 35, 52, 14);
		pnlBB.add(lblShortBB);

		lblLongBB = new JLabel("LONG");
		lblLongBB.setBounds(24, 66, 52, 14);
		pnlBB.add(lblLongBB);

		lblPriceBB = new JLabel("Price");
		lblPriceBB.setBounds(86, 11, 46, 14);
		pnlBB.add(lblPriceBB);

		lblDistBB = new JLabel("Dist %");
		lblDistBB.setBounds(206, 11, 46, 14);
		pnlBB.add(lblDistBB);

		lblTitlePoints0 = new JLabel("Biggest block");
		lblTitlePoints0.setBounds(30, 122, 178, 14);
		contentPane.add(lblTitlePoints0);

		lblTitlePoints1 = new JLabel("Fixes points");
		lblTitlePoints1.setBounds(30, 390, 178, 14);
		contentPane.add(lblTitlePoints1);

		pnlFP = new JPanel();
		pnlFP.setLayout(null);
		pnlFP.setBorder(UIManager.getBorder("TextField.border"));
		pnlFP.setBounds(30, 411, 387, 95);
		contentPane.add(pnlFP);

		txtShortPriceFP = new JTextField();
		txtShortPriceFP.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPriceFP.setEditable(false);
		txtShortPriceFP.setColumns(10);
		txtShortPriceFP.setBounds(86, 32, 110, 20);
		pnlFP.add(txtShortPriceFP);

		txtShortDistFP = new JTextField();
		txtShortDistFP.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDistFP.setEditable(false);
		txtShortDistFP.setColumns(10);
		txtShortDistFP.setBounds(206, 32, 80, 20);
		pnlFP.add(txtShortDistFP);

		btnShortShockFP = new JButton("\u2193");
		btnShortShockFP.setToolTipText("Create Short in this price");
		btnShortShockFP.setOpaque(true);
		btnShortShockFP.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShockFP.setBounds(296, 31, 46, 22);
		pnlFP.add(btnShortShockFP);

		btnLongShockFP = new JButton("\u2191");
		btnLongShockFP.setToolTipText("Create Long in this price");
		btnLongShockFP.setOpaque(true);
		btnLongShockFP.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShockFP.setBounds(296, 62, 46, 22);
		pnlFP.add(btnLongShockFP);

		txtLongDistFP = new JTextField();
		txtLongDistFP.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDistFP.setEditable(false);
		txtLongDistFP.setColumns(10);
		txtLongDistFP.setBounds(206, 63, 80, 20);
		pnlFP.add(txtLongDistFP);

		txtLongPriceFP = new JTextField();
		txtLongPriceFP.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPriceFP.setEditable(false);
		txtLongPriceFP.setColumns(10);
		txtLongPriceFP.setBounds(86, 63, 110, 20);
		pnlFP.add(txtLongPriceFP);

		lblShortFP = new JLabel("SHORT");
		lblShortFP.setBounds(24, 35, 52, 14);
		pnlFP.add(lblShortFP);

		lblLongFP = new JLabel("LONG");
		lblLongFP.setBounds(24, 66, 52, 14);
		pnlFP.add(lblLongFP);

		lblPriceFP = new JLabel("Price");
		lblPriceFP.setBounds(86, 11, 46, 14);
		pnlFP.add(lblPriceFP);

		lblDistFP = new JLabel("Dist %");
		lblDistFP.setBounds(206, 11, 46, 14);
		pnlFP.add(lblDistFP);

		lblTitlePoints2 = new JLabel("Weighted average (Max accum 50% / Max dist 20%)");
		lblTitlePoints2.setBounds(30, 258, 370, 14);
		contentPane.add(lblTitlePoints2);

		pnlWA = new JPanel();
		pnlWA.setLayout(null);
		pnlWA.setBorder(UIManager.getBorder("TextField.border"));
		pnlWA.setBounds(30, 280, 387, 95);
		contentPane.add(pnlWA);

		txtShortPriceWA = new JTextField();
		txtShortPriceWA.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortPriceWA.setEditable(false);
		txtShortPriceWA.setColumns(10);
		txtShortPriceWA.setBounds(86, 32, 110, 20);
		pnlWA.add(txtShortPriceWA);

		txtShortDistWA = new JTextField();
		txtShortDistWA.setForeground(Styles.COLOR_TEXT_SHORT);
		txtShortDistWA.setEditable(false);
		txtShortDistWA.setColumns(10);
		txtShortDistWA.setBounds(206, 32, 80, 20);
		pnlWA.add(txtShortDistWA);

		btnShortShockWA = new JButton("\u2193");
		btnShortShockWA.setToolTipText("Create Short in this price");
		btnShortShockWA.setOpaque(true);
		btnShortShockWA.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShockWA.setBounds(296, 31, 46, 22);
		pnlWA.add(btnShortShockWA);

		btnLongShockWA = new JButton("\u2191");
		btnLongShockWA.setToolTipText("Create Long in this price");
		btnLongShockWA.setOpaque(true);
		btnLongShockWA.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShockWA.setBounds(296, 62, 46, 22);
		pnlWA.add(btnLongShockWA);

		txtLongDistWA = new JTextField();
		txtLongDistWA.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongDistWA.setEditable(false);
		txtLongDistWA.setColumns(10);
		txtLongDistWA.setBounds(206, 63, 80, 20);
		pnlWA.add(txtLongDistWA);

		txtLongPriceWA = new JTextField();
		txtLongPriceWA.setForeground(Styles.COLOR_TEXT_LONG);
		txtLongPriceWA.setEditable(false);
		txtLongPriceWA.setColumns(10);
		txtLongPriceWA.setBounds(86, 63, 110, 20);
		pnlWA.add(txtLongPriceWA);

		lblShortWA = new JLabel("SHORT");
		lblShortWA.setBounds(24, 35, 52, 14);
		pnlWA.add(lblShortWA);

		lblLongWA = new JLabel("LONG");
		lblLongWA.setBounds(24, 66, 52, 14);
		pnlWA.add(lblLongWA);

		lblPriceWA = new JLabel("Price");
		lblPriceWA.setBounds(86, 11, 46, 14);
		pnlWA.add(lblPriceWA);

		lblDistWA = new JLabel("Dist %");
		lblDistWA.setBounds(206, 11, 46, 14);
		pnlWA.add(lblDistWA);

		btnExport = new JButton("Export");
		btnExport.setOpaque(true);
		btnExport.setBounds(710, 22, 87, 22);
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
		
		btnRefresh = new JButton("Full O.Book");
		btnRefresh.setOpaque(true);
		btnRefresh.setBounds(440, 22, 100, 22);
		contentPane.add(btnRefresh);

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

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnShortShockBB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "SHORT", txtShortPrice.getText());
			}
		});
		btnLongShockBB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "LONG", txtLongPrice.getText());
			}
		});
		btnShortShockFP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "SHORT", txtShortPriceFP.getText());
			}
		});
		btnLongShockFP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "LONG", txtLongPriceFP.getText());
			}
		});
		btnShortShockWA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "SHORT", txtShortPriceWA.getText());
			}
		});
		btnLongShockWA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch(coin.getNameLeft(), "LONG", txtLongPriceWA.getText());
			}
		});


		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadOBook(true);
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
				
				loadOBook(false);
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

	private void loadOBook(boolean isFull)
	{
		if (coin == null)
		{
			ERROR("No coin selected");
			return;
		}

		try
		{
			if (isFull)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				obService = OBookService.getInstance(coin).request().subscribeDiffDepthEvent();
				Thread.sleep(30000);
				obService.calc();

				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			else
			{
				obService = OBookService.getInstance(coin).request().calc();
			}
			
			txtOBookAsk.setText(obService.printAsksGrp());
			txtOBookBid.setText(obService.printBidsGrp());
			txtOBookBid.setCaretPosition(0);

			txtShortPrice.setText(coin.priceToStr(obService.getShortPriceBBlk()));
			txtLongPrice.setText(coin.priceToStr(obService.getLongPriceBBlk()));

			txtShortPriceWA.setText(coin.priceToStr(obService.getShortPriceWAvg()));
			txtLongPriceWA.setText(coin.priceToStr(obService.getLongPriceWAvg()));
			
			txtShortPriceFP.setText(coin.priceToStr(obService.getShortPriceFixed()));
			txtLongPriceFP.setText(coin.priceToStr(obService.getLongPriceFixed()));
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

			BigDecimal distSh = PriceUtil.priceDistUp(mrkPrice, obService.getShortPriceBBlk(), false);
			txtShortDist.setText(Convert.dblToStrPercent(distSh) + " %");

			BigDecimal distShWA = PriceUtil.priceDistUp(mrkPrice, obService.getShortPriceWAvg(), false);
			txtShortDistWA.setText(Convert.dblToStrPercent(distShWA) + " %");

			BigDecimal distShFP = PriceUtil.priceDistUp(mrkPrice, obService.getShortPriceFixed(), false);
			txtShortDistFP.setText(Convert.dblToStrPercent(distShFP) + " %");

			BigDecimal distLg = PriceUtil.priceDistDown(mrkPrice, obService.getLongPriceBBlk(), false);
			txtLongDist.setText(Convert.dblToStrPercent(distLg) + " %");

			BigDecimal distLgWA = PriceUtil.priceDistDown(mrkPrice, obService.getLongPriceWAvg(), false);
			txtLongDistWA.setText(Convert.dblToStrPercent(distLgWA) + " %");

			BigDecimal distLgFP = PriceUtil.priceDistDown(mrkPrice, obService.getLongPriceFixed(), false);
			txtLongDistFP.setText(Convert.dblToStrPercent(distLgFP) + " %");

			if (distSh.doubleValue() <= 0.002 || distShWA.doubleValue() <= 0.002 ||
				distLg.doubleValue() <= 0.002 || distLgWA.doubleValue() <= 0.002)
			{
				if(!beepDone)
				{
					BeepUtils.beep();
					beepDone = true;
				}
			}

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
			obService.export();

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
