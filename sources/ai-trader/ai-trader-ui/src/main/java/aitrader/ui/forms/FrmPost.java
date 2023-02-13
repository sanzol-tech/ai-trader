package aitrader.ui.forms;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Position;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.core.service.position.PositionService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.trade.OrderHelper;
import aitrader.core.service.trade.PositionModeService;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.controls.CtrlError;
import aitrader.util.BeepUtils;
import aitrader.util.Convert;
import aitrader.util.observable.Handler;
import aitrader.util.price.PriceUtil;
import binance.futures.enums.OrderSide;
import binance.futures.enums.PositionMode;
import binance.futures.enums.PositionSide;

public class FrmPost extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME + " - Post";

	private static boolean isOpen = false;

	private Symbol symbol;
	private List<Position> lstPositions;
	private Position shortPosition;
	private Position longPosition;

	private CtrlError ctrlError;
	private JPanel pnlContent;
	private JPanel pnlStatusBar;

	private JButton btnSearch;
	private JButton btnLast;
	private JButton btnPrice05;
	private JButton btnPrice1;
	private JButton btnPrice2;
	private JButton btnPrice4;
	
	private JButton btnCalc05;
	private JButton btnCalc075;
	private JButton btnCalc100;
	private JButton btnCalc125;
	private JButton btnCalc150;
	private JButton btnCalc200;
	
	private JButton btnCalc;
	private JButton btnMagic;
	
	private JButton btnPost;

	private JRadioButton rbShort;
	private JRadioButton rbLong;

	private JLabel lbl24Hs;
	private JLabel lblNewLabel_6;
	private JLabel lblNewLabel_7;
	private JLabel lblNewLabel_8;

	private JLabel lblHedgeMode;
	
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtMarkPrice;
	private JTextField txt24h;

	private JTextField txtPrice;
	private JTextField txtQty;

	private JTextField txtShortDist;
	private JTextField txtShortEntryPrice;
	private JTextField txtShortQty;
	private JTextField txtShortUSD;

	private JTextField txtLongDist;
	private JTextField txtLongEntryPrice;
	private JTextField txtLongQty;
	private JTextField txtLongUSD;
	
	private JTextField txtPostDist;
	private JTextField txtPostEntryPrice;
	private JTextField txtPostQty;
	private JTextField txtPostUSD;
	
	private JTextField txtResultDist;
	private JTextField txtResultEntryPrice;
	private JTextField txtResultQty;
	private JTextField txtResultUSD;
	
	private Handler<Void> priceServiceHandler = e -> { onPriceUpdate(); };

	public FrmPost()
	{
		initComponents();
		
		SymbolInfoService.attachObserver(priceServiceHandler);
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 570);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmPost.class.getResource("/resources/upDown.png")));
		setLocationRelativeTo(null);
		setResizable(false);

		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		ctrlError = new CtrlError();

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(31, 69, 178, 22);
		pnlContent.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(31, 43, 86, 20);
		pnlContent.add(txtSymbolLeft);

		JLabel lblSymbol = new JLabel("SYMBOL");
		lblSymbol.setBounds(31, 24, 86, 14);
		pnlContent.add(lblSymbol);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(CoreConfig.getDefaultSymbolRight());
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(123, 43, 86, 20);
		pnlContent.add(txtSymbolRight);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(420, 62, 86, 20);
		pnlContent.add(txtMarkPrice);

		JLabel lblMarkPrice = new JLabel("Price");
		lblMarkPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMarkPrice.setBounds(421, 43, 85, 14);
		pnlContent.add(lblMarkPrice);

		rbShort = new JRadioButton("SHORT");
		rbShort.setBounds(31, 146, 76, 23);
		pnlContent.add(rbShort);

		rbLong = new JRadioButton("LONG");
		rbLong.setBounds(31, 174, 76, 23);
		pnlContent.add(rbLong);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbShort);
		bg1.add(rbLong);

		btnCalc05 = new JButton("x 0.5");
		btnCalc05.setOpaque(true);
		btnCalc05.setBounds(232, 338, 70, 23);
		pnlContent.add(btnCalc05);

		btnCalc075 = new JButton("x 0.75");
		btnCalc075.setOpaque(true);
		btnCalc075.setBounds(309, 338, 70, 23);
		pnlContent.add(btnCalc075);

		btnCalc100 = new JButton("x 1");
		btnCalc100.setOpaque(true);
		btnCalc100.setBounds(232, 372, 70, 23);
		pnlContent.add(btnCalc100);

		btnCalc200 = new JButton("x 2");
		btnCalc200.setOpaque(true);
		btnCalc200.setBounds(309, 405, 70, 23);
		pnlContent.add(btnCalc200);

		btnCalc = new JButton("CALC");
		btnCalc.setBounds(435, 354, 113, 23);
		btnCalc.setOpaque(true);
		pnlContent.add(btnCalc);

		btnMagic = new JButton("!");
		btnMagic.setToolTipText("Position is increased 0.5 every 1% of distance");
		btnMagic.setBounds(563, 354, 46, 23);
		btnMagic.setOpaque(true);
		pnlContent.add(btnMagic);
		
		btnPost = new JButton("POST ORDER");
		btnPost.setIcon(Styles.IMAGE_EXECUTE);
		btnPost.setBounds(435, 388, 174, 40);
		btnPost.setOpaque(true);
		pnlContent.add(btnPost);

		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(523, 62, 86, 20);
		pnlContent.add(txt24h);

		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(524, 43, 85, 14);
		pnlContent.add(lbl24Hs);

		btnCalc125 = new JButton("x 1.25");
		btnCalc125.setOpaque(true);
		btnCalc125.setBounds(309, 372, 70, 23);
		pnlContent.add(btnCalc125);

		btnCalc150 = new JButton("x 1.5");
		btnCalc150.setOpaque(true);
		btnCalc150.setBounds(232, 405, 70, 23);
		pnlContent.add(btnCalc150);
		
		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtPrice.setBounds(31, 305, 86, 20);
		pnlContent.add(txtPrice);
		txtPrice.setColumns(10);
		
		btnLast = new JButton("Last");
		btnLast.setOpaque(true);
		btnLast.setBounds(31, 338, 147, 23);
		pnlContent.add(btnLast);
		
		txtQty = new JTextField();
		txtQty.setHorizontalAlignment(SwingConstants.TRAILING);
		txtQty.setColumns(10);
		txtQty.setBounds(232, 305, 86, 20);
		pnlContent.add(txtQty);
		
		JLabel lblNewLabel = new JLabel("Price");
		lblNewLabel.setBounds(31, 288, 46, 14);
		pnlContent.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Qty");
		lblNewLabel_1.setBounds(232, 288, 46, 14);
		pnlContent.add(lblNewLabel_1);
		
		btnPrice05 = new JButton("+ 0.5%");
		btnPrice05.setOpaque(true);
		btnPrice05.setBounds(31, 372, 70, 23);
		pnlContent.add(btnPrice05);
		
		btnPrice1 = new JButton("+ 1%");
		btnPrice1.setOpaque(true);
		btnPrice1.setBounds(108, 372, 70, 23);
		pnlContent.add(btnPrice1);
		
		btnPrice2 = new JButton("+ 2%");
		btnPrice2.setOpaque(true);
		btnPrice2.setBounds(31, 405, 70, 23);
		pnlContent.add(btnPrice2);
		
		btnPrice4 = new JButton("+ 4%");
		btnPrice4.setOpaque(true);
		btnPrice4.setBounds(108, 405, 70, 23);
		pnlContent.add(btnPrice4);
		
		JLabel lblNewLabel_2 = new JLabel("Entry Price");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_2.setBounds(113, 127, 86, 14);
		pnlContent.add(lblNewLabel_2);
		
		txtShortEntryPrice = new JTextField();
		txtShortEntryPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtShortEntryPrice.setEditable(false);
		txtShortEntryPrice.setColumns(10);
		txtShortEntryPrice.setBounds(113, 147, 86, 20);
		pnlContent.add(txtShortEntryPrice);
		
		JLabel lblNewLabel_3 = new JLabel("Quantity");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_3.setBounds(209, 127, 86, 14);
		pnlContent.add(lblNewLabel_3);
		
		txtShortQty = new JTextField();
		txtShortQty.setHorizontalAlignment(SwingConstants.TRAILING);
		txtShortQty.setEditable(false);
		txtShortQty.setColumns(10);
		txtShortQty.setBounds(209, 147, 86, 20);
		pnlContent.add(txtShortQty);
		
		JLabel lblNewLabel_4 = new JLabel("USD");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_4.setBounds(305, 127, 86, 14);
		pnlContent.add(lblNewLabel_4);
		
		txtShortUSD = new JTextField();
		txtShortUSD.setHorizontalAlignment(SwingConstants.TRAILING);
		txtShortUSD.setEditable(false);
		txtShortUSD.setColumns(10);
		txtShortUSD.setBounds(305, 147, 86, 20);
		pnlContent.add(txtShortUSD);
		
		txtLongEntryPrice = new JTextField();
		txtLongEntryPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLongEntryPrice.setEditable(false);
		txtLongEntryPrice.setColumns(10);
		txtLongEntryPrice.setBounds(113, 175, 86, 20);
		pnlContent.add(txtLongEntryPrice);
		
		txtLongQty = new JTextField();
		txtLongQty.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLongQty.setEditable(false);
		txtLongQty.setColumns(10);
		txtLongQty.setBounds(209, 175, 86, 20);
		pnlContent.add(txtLongQty);
		
		txtLongUSD = new JTextField();
		txtLongUSD.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLongUSD.setEditable(false);
		txtLongUSD.setColumns(10);
		txtLongUSD.setBounds(305, 175, 86, 20);
		pnlContent.add(txtLongUSD);
		
		lblHedgeMode = new JLabel("Hedge mode: XX");
		lblHedgeMode.setHorizontalAlignment(SwingConstants.CENTER);
		lblHedgeMode.setBounds(258, 59, 122, 16);
		pnlContent.add(lblHedgeMode);
		
		txtShortDist = new JTextField();
		txtShortDist.setHorizontalAlignment(SwingConstants.TRAILING);
		txtShortDist.setEditable(false);
		txtShortDist.setColumns(10);
		txtShortDist.setBounds(401, 147, 86, 20);
		pnlContent.add(txtShortDist);
		
		lblNewLabel_6 = new JLabel("Dististance %");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_6.setBounds(401, 127, 86, 14);
		pnlContent.add(lblNewLabel_6);
		
		txtLongDist = new JTextField();
		txtLongDist.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLongDist.setEditable(false);
		txtLongDist.setColumns(10);
		txtLongDist.setBounds(401, 175, 86, 20);
		pnlContent.add(txtLongDist);
		
		txtPostEntryPrice = new JTextField();
		txtPostEntryPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtPostEntryPrice.setEditable(false);
		txtPostEntryPrice.setColumns(10);
		txtPostEntryPrice.setBounds(113, 206, 86, 20);
		pnlContent.add(txtPostEntryPrice);
		
		txtPostQty = new JTextField();
		txtPostQty.setHorizontalAlignment(SwingConstants.TRAILING);
		txtPostQty.setEditable(false);
		txtPostQty.setColumns(10);
		txtPostQty.setBounds(209, 206, 86, 20);
		pnlContent.add(txtPostQty);
		
		txtPostUSD = new JTextField();
		txtPostUSD.setHorizontalAlignment(SwingConstants.TRAILING);
		txtPostUSD.setEditable(false);
		txtPostUSD.setColumns(10);
		txtPostUSD.setBounds(305, 206, 86, 20);
		pnlContent.add(txtPostUSD);
		
		txtPostDist = new JTextField();
		txtPostDist.setHorizontalAlignment(SwingConstants.TRAILING);
		txtPostDist.setEditable(false);
		txtPostDist.setColumns(10);
		txtPostDist.setBounds(401, 206, 86, 20);
		pnlContent.add(txtPostDist);
		
		txtResultEntryPrice = new JTextField();
		txtResultEntryPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtResultEntryPrice.setEditable(false);
		txtResultEntryPrice.setColumns(10);
		txtResultEntryPrice.setBounds(113, 237, 86, 20);
		pnlContent.add(txtResultEntryPrice);
		
		txtResultQty = new JTextField();
		txtResultQty.setHorizontalAlignment(SwingConstants.TRAILING);
		txtResultQty.setEditable(false);
		txtResultQty.setColumns(10);
		txtResultQty.setBounds(209, 237, 86, 20);
		pnlContent.add(txtResultQty);
		
		txtResultUSD = new JTextField();
		txtResultUSD.setHorizontalAlignment(SwingConstants.TRAILING);
		txtResultUSD.setEditable(false);
		txtResultUSD.setColumns(10);
		txtResultUSD.setBounds(305, 237, 86, 20);
		pnlContent.add(txtResultUSD);
		
		txtResultDist = new JTextField();
		txtResultDist.setHorizontalAlignment(SwingConstants.TRAILING);
		txtResultDist.setEditable(false);
		txtResultDist.setColumns(10);
		txtResultDist.setBounds(401, 237, 86, 20);
		pnlContent.add(txtResultDist);
		
		lblNewLabel_7 = new JLabel("Post");
		lblNewLabel_7.setBounds(40, 209, 46, 14);
		pnlContent.add(lblNewLabel_7);
		
		lblNewLabel_8 = new JLabel("Result");
		lblNewLabel_8.setBounds(40, 240, 46, 14);
		pnlContent.add(lblNewLabel_8);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlContent, GroupLayout.PREFERRED_SIZE, 460, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);

		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(7)
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
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
				SymbolInfoService.deattachObserver(priceServiceHandler);
				isOpen = false;
			}
		});

		rbShort.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtPrice.setText(txtShortEntryPrice.getText());
					txtQty.setText(txtShortQty.getText());
					btnPost.setBackground(Styles.COLOR_BTN_SHORT);
					btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
				}
			}
		});
		rbLong.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtPrice.setText(txtLongEntryPrice.getText());
					txtQty.setText(txtLongQty.getText());
					btnPost.setBackground(Styles.COLOR_BTN_LONG);
					btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
				}
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcPrice(null);
			}
		});
		btnPrice05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcPrice(BigDecimal.valueOf(0.005));
			}
		});
		btnPrice1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcPrice(BigDecimal.valueOf(0.01));
			}
		});
		btnPrice2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcPrice(BigDecimal.valueOf(0.02));
			}
		});
		btnPrice4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcPrice(BigDecimal.valueOf(0.04));
			}
		});

		btnCalc05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(0.5));
			}
		});
		btnCalc075.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(0.75));
			}
		});
		btnCalc100.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(1.0));
			}
		});
		btnCalc125.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(1.25));
			}
		});
		btnCalc150.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(1.5));
			}
		});
		btnCalc200.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(BigDecimal.valueOf(2.0));
			}
		});

		btnCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc();
			}
		});

		btnMagic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calcQuantity(getMagicCoef());
			}
		});

		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exec();
			}
		});

	}

	// --------------------------------------------------------------------

	private void clean()
	{
		symbol = null;
		lstPositions = null;
		enableControls();
		
		txtPrice.setText("");
		txtQty.setText("");
		
		txtShortEntryPrice.setText("");
		txtShortQty.setText("");
		txtShortUSD.setText("");
		txtShortDist.setText("");

		txtLongEntryPrice.setText("");
		txtLongQty.setText("");
		txtLongUSD.setText("");
		txtLongDist.setText("");

		txtPostEntryPrice.setText("");
		txtPostQty.setText("");
		txtPostUSD.setText("");
		txtPostDist.setText("");
		
		txtResultEntryPrice.setText("");
		txtResultQty.setText("");
		txtResultUSD.setText("");
		txtResultDist.setText("");
	}

	private void enableControls()
	{
		boolean isSymbol = symbol != null;
		boolean isPosition = lstPositions != null && !lstPositions.isEmpty();
		
		rbShort.setEnabled(isSymbol);
		rbLong.setEnabled(isSymbol);

		btnLast.setEnabled(isSymbol);
		btnPrice05.setEnabled(isSymbol);
		btnPrice1.setEnabled(isSymbol);
		btnPrice2.setEnabled(isSymbol);
		btnPrice4.setEnabled(isSymbol);

		btnCalc05.setEnabled(isSymbol && isPosition);
		btnCalc075.setEnabled(isSymbol && isPosition);
		btnCalc125.setEnabled(isSymbol && isPosition);
		btnCalc150.setEnabled(isSymbol && isPosition);
		btnCalc100.setEnabled(isSymbol && isPosition);
		btnCalc200.setEnabled(isSymbol && isPosition);
		
		btnCalc.setEnabled(isSymbol);
		btnMagic.setEnabled(isPosition);
		btnPost.setEnabled(isSymbol);
	}

	private SymbolInfo searchSymbolInfo()
	{
		txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());

		String symbolLeft = txtSymbolLeft.getText();
		String symbolRight = txtSymbolRight.getText();

		SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(symbolLeft + symbolRight);

		if (symbolInfo == null)
		{
			return null;
		}

		return symbolInfo;
	}

	private void search()
	{
		ctrlError.CLEAN();

		try
		{
			clean();

			PositionMode positionMode = PositionModeService.getPositionMode();
			lblHedgeMode.setText("HedgeMode: " + ((positionMode == PositionMode.HEDGE) ? "ON" : "OFF"));

			SymbolInfo symbolInfo = searchSymbolInfo();
			if (symbolInfo == null)
			{
				ctrlError.ERROR("Symbol not found");
				return;
			}

			symbol = symbolInfo.getSymbol();
			setTitle(TITLE + " - " + symbol.getNameLeft());

			BigDecimal lastPrice = symbolInfo.getLastPrice();
			txtMarkPrice.setText(symbol.priceToStr(lastPrice));
			String change24h = String.format("%.2f", symbolInfo.getChange24h());
			txt24h.setText(change24h);

			searchPosition();
			enableControls();

		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void searchPosition()
	{
		shortPosition = null;
		longPosition = null;
		
		lstPositions = PositionService.getPositions(symbol.getPair());
		for (Position position : lstPositions)
		{
			if (position.isOpen())
			{
				boolean isPosition = position != null && position.getQuantity().compareTo(BigDecimal.ZERO) != 0;
		
				BigDecimal entryPrice = position.getEntryPrice();
				BigDecimal qty = position.getQuantity();
				BigDecimal usd = position.getUsd();

				if (isPosition)
				{
					if (qty.doubleValue() < 0)
					{
						shortPosition = position;

						txtShortEntryPrice.setText(symbol.priceToStr(entryPrice));
						txtShortQty.setText(symbol.qtyToStr(qty.abs()));
						txtShortUSD.setText(Convert.usdToStr(usd));
						txtShortDist.setText(Convert.toStrPercent(position.getDistance()));

						rbShort.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_SHORT);
						btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
					}
					else
					{
						longPosition = position;

						txtLongEntryPrice.setText(symbol.priceToStr(entryPrice));
						txtLongQty.setText(symbol.qtyToStr(qty.abs()));
						txtLongUSD.setText(Convert.usdToStr(usd));
						txtLongDist.setText(Convert.toStrPercent(position.getDistance()));
		
						rbLong.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_LONG);
						btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
					}
		
					BeepUtils.beep3();
				}
			}
		}
	}

	private void calcPrice(BigDecimal dist)
	{
		ctrlError.CLEAN();
		try
		{
			if (dist == null)
			{
				BigDecimal lastPrice = SymbolInfoService.getLastPrice(symbol);
				txtPrice.setText(symbol.priceToStr(lastPrice));
			}
			else
			{
				if (rbShort.isSelected())
				{
					if (shortPosition != null)
					{
						BigDecimal shortPrice = shortPosition.getEntryPrice();
						txtPrice.setText(symbol.priceToStr(shortPrice.multiply(BigDecimal.ONE.add(dist))));
					}
					else
					{
						BigDecimal lastPrice = SymbolInfoService.getLastPrice(symbol);
						txtPrice.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.ONE.add(dist))));
					}
				}
				else if (rbLong.isSelected())
				{
					if (longPosition != null)
					{
						BigDecimal longPrice = longPosition.getEntryPrice();
						txtPrice.setText(symbol.priceToStr(longPrice.multiply(BigDecimal.ONE.subtract(dist))));
					}
					else
					{
						BigDecimal lastPrice = SymbolInfoService.getLastPrice(symbol);
						txtPrice.setText(symbol.priceToStr(lastPrice.multiply(BigDecimal.ONE.subtract(dist))));
					}
				}
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void calcQuantity(BigDecimal mult) 
	{
		ctrlError.CLEAN();
		try
		{
			if (mult != null)
			{
				if (rbShort.isSelected() && shortPosition != null)
				{
					txtQty.setText(symbol.qtyToStr(shortPosition.getQuantity().multiply(mult)));
				}
				else if (rbLong.isSelected() && longPosition != null)
				{
					txtQty.setText(symbol.qtyToStr(longPosition.getQuantity().multiply(mult)));
				}
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}		
	}
 
	private void calc()
	{
		BigDecimal postEntryPrice = Convert.toBigDecimal(txtPrice.getText());
		BigDecimal postQty = Convert.toBigDecimal(txtQty.getText());
		BigDecimal postUsd = postEntryPrice.multiply(postQty).setScale(2, RoundingMode.HALF_UP); 

		if (postEntryPrice != null && postQty != null)
		{
			boolean isPosition = (rbShort.isSelected() && shortPosition != null) || (rbLong.isSelected() && longPosition != null);
			if (isPosition)
			{
				if (rbShort.isSelected())
				{
					BigDecimal postDist = PriceUtil.priceDistDown(postEntryPrice, shortPosition.getEntryPrice(), false);

					txtPostEntryPrice.setText(symbol.priceToStr(postEntryPrice));
					txtPostQty.setText(symbol.qtyToStr(postQty));
					txtPostUSD.setText(Convert.usdToStr(postUsd));
					txtPostDist.setText(Convert.toStrPercent(postDist));
	
					BigDecimal resultQty = shortPosition.getQuantity().add(postQty);
					BigDecimal resultUsd = (shortPosition.getEntryPrice().multiply(shortPosition.getQuantity())).add((postEntryPrice.multiply(postQty)));
					BigDecimal resultPrice = resultUsd.divide(resultQty, symbol.getPricePrecision(), RoundingMode.HALF_UP);
					BigDecimal resultDist = PriceUtil.priceDistDown(postEntryPrice, resultPrice, false);

					txtResultEntryPrice.setText(symbol.priceToStr(resultPrice));
					txtResultQty.setText(symbol.qtyToStr(resultQty));
					txtResultUSD.setText(Convert.usdToStr(resultUsd));
					txtResultDist.setText(Convert.toStrPercent(resultDist));
				}
				else
				{
					BigDecimal postDist = PriceUtil.priceDistUp(postEntryPrice, longPosition.getEntryPrice(), false);

					txtPostEntryPrice.setText(symbol.priceToStr(postEntryPrice));
					txtPostQty.setText(symbol.qtyToStr(postQty));
					txtPostUSD.setText(Convert.usdToStr(postUsd));
					txtPostDist.setText(Convert.toStrPercent(postDist));
	
					BigDecimal resultQty = longPosition.getQuantity().add(postQty);
					BigDecimal resultUsd = (longPosition.getEntryPrice().multiply(longPosition.getQuantity())).add((postEntryPrice.multiply(postQty)));
					BigDecimal resultPrice = resultUsd.divide(resultQty, symbol.getPricePrecision(), RoundingMode.HALF_UP);
					BigDecimal resultDist = PriceUtil.priceDistUp(postEntryPrice, resultPrice, false);
					
					txtResultEntryPrice.setText(symbol.priceToStr(resultPrice));
					txtResultQty.setText(symbol.qtyToStr(resultQty));
					txtResultUSD.setText(Convert.usdToStr(resultUsd));
					txtResultDist.setText(Convert.toStrPercent(resultDist));
				}
			}
			else
			{
				txtPostEntryPrice.setText(symbol.priceToStr(postEntryPrice));
				txtPostQty.setText(symbol.qtyToStr(postQty));
				txtPostUSD.setText(Convert.usdToStr(postUsd));
				txtPostDist.setText(Convert.toStrPercent(0.0));

				txtResultEntryPrice.setText("");
				txtResultQty.setText("");
				txtResultUSD.setText("");
				txtResultDist.setText("");
			}
		}
	}

	private BigDecimal getMagicCoef()
	{
		final BigDecimal percentChange = BigDecimal.valueOf(2);

		BigDecimal postPrice = Convert.toBigDecimal(txtPostEntryPrice.getText());
		if (postPrice == null)
		{
			postPrice = SymbolInfoService.getLastPrice(symbol);
		}

		if (rbShort.isSelected() && shortPosition != null && shortPosition.getEntryPrice().doubleValue() < postPrice.doubleValue())
		{
			BigDecimal dist = PriceUtil.priceDistUp(shortPosition.getEntryPrice(), postPrice, true);
			return dist.divide(percentChange, RoundingMode.HALF_UP);
		}
		else if (rbLong.isSelected() && longPosition != null && longPosition.getEntryPrice().doubleValue() > postPrice.doubleValue())
		{
			BigDecimal dist = PriceUtil.priceDistDown(longPosition.getEntryPrice(), postPrice, true);
			return dist.divide(percentChange, RoundingMode.HALF_UP);
		}
		else
		{
			return BigDecimal.ZERO;
		}
	}
	
	private void exec()
	{
		ctrlError.CLEAN();
		try
		{
			PositionSide positionSide; 
			OrderSide orderSide;
			if (rbShort.isSelected())
			{
				positionSide = PositionSide.SHORT; 
				orderSide = OrderSide.SELL;
			}
			else if (rbLong.isSelected())
			{
				positionSide = PositionSide.LONG; 
				orderSide = OrderSide.BUY;
			}
			else
			{
				ctrlError.ERROR("Must select SHORT or LONG");
				return;
			}
			
			BigDecimal price = Convert.toBigDecimal(txtPrice.getText());
			BigDecimal qty = Convert.toBigDecimal(txtQty.getText());

			String msg = String.format("Post order %s  /  %s  /  %s  /  %s ?", orderSide.name(), symbol.getPair(), symbol.priceToStr(price), symbol.qtyToStr(qty));
			int resultOption = JOptionPane.showConfirmDialog(null, msg, "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				BigDecimal mrkPrice = SymbolInfoService.getLastPrice(symbol);
				if (orderSide == OrderSide.SELL && mrkPrice.doubleValue() > price.doubleValue() ||
					orderSide == OrderSide.BUY && mrkPrice.doubleValue() < price.doubleValue())
				{
					price = mrkPrice;
				}

				OrderHelper.postOrder(symbol, positionSide, orderSide, price, qty);
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	private void onPriceUpdate()
	{
		try
		{
			if (symbol != null)
			{
				SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(symbol.getPair());
				BigDecimal price = symbolInfo.getLastPrice();
				txtMarkPrice.setText(symbol.priceToStr(price));
				String change24h = String.format(Locale.US, "%.2f%%", symbolInfo.getChange24h());
				txt24h.setText(change24h);
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	public static void launch(Symbol symbol)
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
					FrmPost frame = new FrmPost();
					frame.enableControls();
					frame.setVisible(true);

					if (symbol != null)
					{
						frame.symbol = symbol;
						frame.txtSymbolLeft.setText(symbol.getNameLeft());
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
