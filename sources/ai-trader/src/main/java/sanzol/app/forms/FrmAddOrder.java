package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.GOrder;
import sanzol.app.model.Symbol;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.trader.SimpleTrader;
import sanzol.app.util.Convert;

public class FrmAddOrder extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Add order";

	private static boolean isOpen = false;

	private Symbol coin;

	private Timer timer1;

	private JPanel contentPane;
	private JLabel lblError;

	private JButton btnCalc05;
	private JButton btnCalc075;
	private JButton btnCalc125;
	private JButton btnCalc150;
	private JButton btnCalc;
	private JButton btnX1;
	private JButton btnX2;
	private JButton btnPost;

	private JRadioButton rbShort;
	private JRadioButton rbLong;

	private JLabel lbl24Hs;
	private JLabel lblTprofit;

	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtMarkPrice;
	private JTextField txt24h;

	private JTextField txtPositionDist;
	private JTextField txtPositionPrice;
	private JTextField txtPositionQty;
	private JTextField txtPositionUsd;
	private JTextField txtShootPrice;
	private JTextField txtShootQty;
	private JTextField txtShootUsd;
	private JTextField txtResultDist;
	private JTextField txtResultPrice;
	private JTextField txtResultQty;
	private JTextField txtResultUsd;
	private JTextField txtOthersQty;
	private JTextField txtOthersPrice;
	private JTextField txtOthersUsd;
	private JTextField txtSLDist;
	private JTextField txtSLPrice;
	private JTextField txtSLQty;
	private JTextField txtSLUsd;
	private JTextField txtTPDist;
	private JTextField txtTPPrice;
	private JTextField txtTPQty;
	private JTextField txtTPUsd;
	private JTextField txtShootDist;
	private JTextField txtOthersDist;

	public FrmAddOrder()
	{
		setResizable(false);
		initComponents();
		startTimer();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 560);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/upDown.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(10, 490, 514, 22);
		contentPane.add(pnlBottom);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		
		lblError = new JLabel();
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(lblError, BorderLayout.CENTER);
		
		txtShootPrice = new JTextField();
		txtShootPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootPrice.setBounds(108, 175, 86, 20);
		contentPane.add(txtShootPrice);
		txtShootPrice.setColumns(10);

		txtShootQty = new JTextField();
		txtShootQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootQty.setBounds(204, 175, 86, 20);
		contentPane.add(txtShootQty);
		txtShootQty.setColumns(10);

		txtPositionPrice = new JTextField();
		txtPositionPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionPrice.setBounds(108, 145, 86, 20);
		contentPane.add(txtPositionPrice);
		txtPositionPrice.setColumns(10);

		JButton btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(31, 69, 178, 22);
		contentPane.add(btnSearch);

		JLabel lblNewLabel = new JLabel("Price");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(108, 122, 86, 14);
		contentPane.add(lblNewLabel);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
		lblQuantity.setBounds(204, 122, 86, 14);
		contentPane.add(lblQuantity);

		JLabel lblShoot = new JLabel("SHOOT");
		lblShoot.setBounds(31, 179, 67, 14);
		contentPane.add(lblShoot);

		JLabel lblPosition = new JLabel("POSITION");
		lblPosition.setBounds(31, 149, 67, 14);
		contentPane.add(lblPosition);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(31, 43, 86, 20);
		contentPane.add(txtSymbolLeft);

		txtPositionQty = new JTextField();
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(204, 145, 86, 20);
		contentPane.add(txtPositionQty);

		JLabel lblNewLabel_2_1 = new JLabel("COIN");
		lblNewLabel_2_1.setBounds(31, 24, 86, 14);
		contentPane.add(lblNewLabel_2_1);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(123, 43, 86, 20);
		contentPane.add(txtSymbolRight);

		JLabel lblResult = new JLabel("RESULT");
		lblResult.setBounds(31, 209, 67, 14);
		contentPane.add(lblResult);

		txtResultPrice = new JTextField();
		txtResultPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultPrice.setEditable(false);
		txtResultPrice.setColumns(10);
		txtResultPrice.setBounds(108, 205, 86, 20);
		contentPane.add(txtResultPrice);

		txtResultQty = new JTextField();
		txtResultQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultQty.setEditable(false);
		txtResultQty.setColumns(10);
		txtResultQty.setBounds(204, 205, 86, 20);
		contentPane.add(txtResultQty);

		txtShootUsd = new JTextField();
		txtShootUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtShootUsd.setEditable(false);
		txtShootUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootUsd.setColumns(10);
		txtShootUsd.setBounds(300, 175, 86, 20);
		contentPane.add(txtShootUsd);

		JLabel lblUsd = new JLabel("USD");
		lblUsd.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsd.setBounds(300, 122, 86, 14);
		contentPane.add(lblUsd);

		txtPositionDist = new JTextField();
		txtPositionDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionDist.setEditable(false);
		txtPositionDist.setColumns(10);
		txtPositionDist.setBounds(403, 145, 86, 20);
		contentPane.add(txtPositionDist);

		txtResultDist = new JTextField();
		txtResultDist.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultDist.setEditable(false);
		txtResultDist.setColumns(10);
		txtResultDist.setBounds(403, 205, 86, 20);
		contentPane.add(txtResultDist);

		JLabel lblDistPercent = new JLabel("Dist %");
		lblDistPercent.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDistPercent.setBounds(403, 122, 86, 14);
		contentPane.add(lblDistPercent);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(300, 43, 86, 20);
		contentPane.add(txtMarkPrice);

		JLabel lblMarkPrice = new JLabel("Price");
		lblMarkPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMarkPrice.setBounds(301, 24, 85, 14);
		contentPane.add(lblMarkPrice);

		rbShort = new JRadioButton("SHORT");
		rbShort.setHorizontalAlignment(SwingConstants.LEFT);
		rbShort.setBounds(327, 369, 76, 23);
		contentPane.add(rbShort);

		rbLong = new JRadioButton("LONG");
		rbLong.setHorizontalAlignment(SwingConstants.RIGHT);
		rbLong.setSelected(true);
		rbLong.setBounds(413, 369, 76, 23);
		contentPane.add(rbLong);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbShort);
		bg1.add(rbLong);
		
		btnCalc05 = new JButton("x 0.5");
		btnCalc05.setOpaque(true);
		btnCalc05.setBounds(31, 369, 70, 23);
		contentPane.add(btnCalc05);

		btnCalc075 = new JButton("x 0.75");
		btnCalc075.setOpaque(true);
		btnCalc075.setBounds(108, 369, 70, 23);
		contentPane.add(btnCalc075);
		
		btnX1 = new JButton("x 1");
		btnX1.setOpaque(true);
		btnX1.setBounds(187, 369, 70, 23);
		contentPane.add(btnX1);

		btnX2 = new JButton("x 2");
		btnX2.setOpaque(true);
		btnX2.setBounds(187, 403, 70, 23);
		contentPane.add(btnX2);

		btnCalc = new JButton("CALC");
		btnCalc.setBounds(31, 436, 226, 23);
		btnCalc.setOpaque(true);
		contentPane.add(btnCalc);

		btnPost = new JButton("POST ORDER");
		btnPost.setBounds(327, 409, 162, 40);
		btnPost.setOpaque(true);
		contentPane.add(btnPost);
		
		txtOthersPrice = new JTextField();
		txtOthersPrice.setEditable(false);
		txtOthersPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersPrice.setColumns(10);
		txtOthersPrice.setBounds(108, 235, 86, 20);
		contentPane.add(txtOthersPrice);
		
		txtOthersQty = new JTextField();
		txtOthersQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersQty.setEditable(false);
		txtOthersQty.setColumns(10);
		txtOthersQty.setBounds(205, 235, 86, 20);
		contentPane.add(txtOthersQty);
		
		txtOthersUsd = new JTextField();
		txtOthersUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersUsd.setEditable(false);
		txtOthersUsd.setColumns(10);
		txtOthersUsd.setBounds(300, 235, 86, 20);
		contentPane.add(txtOthersUsd);
		
		txtSLUsd = new JTextField();
		txtSLUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLUsd.setEditable(false);
		txtSLUsd.setColumns(10);
		txtSLUsd.setBounds(300, 275, 86, 20);
		contentPane.add(txtSLUsd);
		
		txtSLQty = new JTextField();
		txtSLQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLQty.setEditable(false);
		txtSLQty.setColumns(10);
		txtSLQty.setBounds(205, 275, 86, 20);
		contentPane.add(txtSLQty);
		
		txtSLPrice = new JTextField();
		txtSLPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLPrice.setColumns(10);
		txtSLPrice.setBounds(108, 275, 86, 20);
		contentPane.add(txtSLPrice);
		
		JLabel lblOthers = new JLabel("OTHERS");
		lblOthers.setBounds(31, 239, 67, 14);
		contentPane.add(lblOthers);
		
		JLabel lblStopLss = new JLabel("S-MARKET");
		lblStopLss.setBounds(31, 279, 67, 14);
		contentPane.add(lblStopLss);
		
		txtSLDist = new JTextField();
		txtSLDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLDist.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtSLDist.setEditable(false);
		txtSLDist.setColumns(10);
		txtSLDist.setBounds(403, 275, 86, 20);
		contentPane.add(txtSLDist);
		
		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(403, 43, 86, 20);
		contentPane.add(txt24h);
		
		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(404, 24, 85, 14);
		contentPane.add(lbl24Hs);
		
		lblTprofit = new JLabel("T. PROFIT");
		lblTprofit.setBounds(31, 309, 67, 14);
		contentPane.add(lblTprofit);
		
		txtTPPrice = new JTextField();
		txtTPPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPPrice.setColumns(10);
		txtTPPrice.setBounds(108, 305, 86, 20);
		contentPane.add(txtTPPrice);
		
		txtTPQty = new JTextField();
		txtTPQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPQty.setEditable(false);
		txtTPQty.setColumns(10);
		txtTPQty.setBounds(205, 305, 86, 20);
		contentPane.add(txtTPQty);
		
		txtTPUsd = new JTextField();
		txtTPUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPUsd.setEditable(false);
		txtTPUsd.setColumns(10);
		txtTPUsd.setBounds(300, 305, 86, 20);
		contentPane.add(txtTPUsd);
		
		txtPositionUsd = new JTextField();
		txtPositionUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtPositionUsd.setEditable(false);
		txtPositionUsd.setColumns(10);
		txtPositionUsd.setBounds(300, 145, 86, 20);
		contentPane.add(txtPositionUsd);
		
		txtResultUsd = new JTextField();
		txtResultUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtResultUsd.setEditable(false);
		txtResultUsd.setColumns(10);
		txtResultUsd.setBounds(300, 206, 86, 20);
		contentPane.add(txtResultUsd);
		
		txtTPDist = new JTextField();
		txtTPDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPDist.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtTPDist.setEditable(false);
		txtTPDist.setColumns(10);
		txtTPDist.setBounds(403, 305, 86, 20);
		contentPane.add(txtTPDist);
		
		txtShootDist = new JTextField();
		txtShootDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootDist.setEditable(false);
		txtShootDist.setColumns(10);
		txtShootDist.setBounds(403, 175, 86, 20);
		contentPane.add(txtShootDist);
		
		txtOthersDist = new JTextField();
		txtOthersDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersDist.setEditable(false);
		txtOthersDist.setColumns(10);
		txtOthersDist.setBounds(403, 235, 86, 20);
		contentPane.add(txtOthersDist);
		
		btnCalc125 = new JButton("x 1.25");
		btnCalc125.setOpaque(true);
		btnCalc125.setBounds(31, 403, 70, 23);
		contentPane.add(btnCalc125);
		
		btnCalc150 = new JButton("x 1.5");
		btnCalc150.setOpaque(true);
		btnCalc150.setBounds(108, 403, 70, 23);
		contentPane.add(btnCalc150);

		// ---------------------------------------------------------------------
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				timer1.stop();
				isOpen = false;
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});


		btnCalc05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(0.5));
			}
		});
		btnCalc075.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(0.75));
			}
		});
		btnX1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(1.0));
			}
		});
		btnCalc125.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(1.25));
			}
		});
		btnCalc150.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(1.5));
			}
		});
		btnX2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(BigDecimal.valueOf(2.0));
			}
		});

		
		btnCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(null);
			}
		});

		btnPost.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exec();
			}
		});

	}

	// ----------------------------------------------------------------------------------

	private void clean()
	{
		txtPositionDist.setText("");
		txtPositionPrice.setText("");
		txtPositionQty.setText("");
		txtPositionUsd.setText("");

		txtShootPrice.setText("");
		txtShootQty.setText("");
		txtShootUsd.setText("");
		txtShootDist.setText("");

		txtResultDist.setText("");
		txtResultPrice.setText("");
		txtResultQty.setText("");
		txtResultUsd.setText("");

		txtOthersQty.setText("");
		txtOthersPrice.setText("");
		txtOthersUsd.setText("");
		txtOthersDist.setText("");

		txtSLDist.setText("");
		txtSLPrice.setText("");
		txtSLQty.setText("");
		txtSLUsd.setText("");

		txtTPDist.setText("");
		txtTPPrice.setText("");
		txtTPQty.setText("");
		txtTPUsd.setText("");
	}

	private void search()
	{
		INFO("");
		try
		{
			clean();

			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));
			if (coin != null)
			{
				SymbolTickerEvent symbolTicker = PriceService.getSymbolTickerEvent(coin);
				if (symbolTicker == null)
				{
					return;
				}

				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				txtMarkPrice.setText(coin.priceToStr(mrkPrice));
				String priceChangePercent = String.format("%.2f", symbolTicker.getPriceChangePercent());
				txt24h.setText(priceChangePercent);
	
				PositionRisk positionRisk = PositionService.getPositionRisk(coin.getName());
				if (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0)
				{
					txtPositionPrice.setText(coin.priceToStr(positionRisk.getEntryPrice()));
		
					double amt = Math.abs(positionRisk.getPositionAmt().doubleValue());
					txtPositionQty.setText(coin.qtyToStr(amt));
		
					if (positionRisk.getPositionAmt().doubleValue() < 0)
					{
						BigDecimal posDist = positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
						txtPositionDist.setText(Convert.dblToStrPercent(posDist));
		
						rbShort.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_SHORT);
					}
					else
					{
						BigDecimal posDist = BigDecimal.ONE.subtract(positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP));
						txtPositionDist.setText(Convert.dblToStrPercent(posDist));
		
						rbLong.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_LONG);
					}
				}
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

	private void calc(BigDecimal x)
	{
		INFO("");
		try
		{
			String side = rbLong.isSelected() ? "BUY" : "SELL";

			// --- POSITION ---------------------------------------------------
			BigDecimal posPrice = new BigDecimal(txtPositionPrice.getText());  
			BigDecimal posQty = new BigDecimal(txtPositionQty.getText());

			// --- SHOOT ------------------------------------------------------
			BigDecimal shootPrice;
			if (txtShootPrice.getText() != null && !txtShootPrice.getText().isEmpty())
				shootPrice = new BigDecimal(txtShootPrice.getText());
			else
				shootPrice = PriceService.getLastPrice(coin);

			BigDecimal shootQty;
			if (x == null)
				shootQty = new BigDecimal(txtShootQty.getText());
			else
				shootQty = posQty.multiply(x).setScale(coin.getQuantityPrecision(), RoundingMode.UP);
			
			// --- CALC -------------------------------------------------------
			Map<String, GOrder> mapPosition = PositionService.calc(coin, side, posPrice, posQty, shootPrice, shootQty);

			// ----------------------------------------------------------------
			txtPositionUsd.setText(Convert.usdToStr(mapPosition.get("POS").getUsd()));
			txtPositionDist.setText(Convert.dblToStrPercent(mapPosition.get("POS").getDist()));

			txtShootPrice.setText(coin.priceToStr(mapPosition.get("SHOOT").getPrice()));
			txtShootQty.setText(coin.qtyToStr(mapPosition.get("SHOOT").getCoins()));
			txtShootUsd.setText(Convert.usdToStr(mapPosition.get("SHOOT").getUsd()));
			txtShootDist.setText(Convert.dblToStrPercent(mapPosition.get("SHOOT").getDist()));

			txtResultPrice.setText(coin.priceToStr(mapPosition.get("RESULT").getPrice()));
			txtResultQty.setText(coin.qtyToStr(mapPosition.get("RESULT").getCoins()));
			txtResultUsd.setText(Convert.usdToStr(mapPosition.get("RESULT").getUsd()));
			txtResultDist.setText(Convert.dblToStrPercent(mapPosition.get("RESULT").getDist()));

			if (mapPosition.containsKey("OTHERS"))
			{
				txtOthersPrice.setText(coin.priceToStr(mapPosition.get("OTHERS").getPrice()));
				txtOthersQty.setText(coin.qtyToStr(mapPosition.get("OTHERS").getCoins()));
				txtOthersUsd.setText(Convert.usdToStr(mapPosition.get("OTHERS").getUsd()));
				txtOthersDist.setText(Convert.dblToStrPercent(mapPosition.get("OTHERS").getDist()));
			}

			if (mapPosition.containsKey("SL"))
			{
				txtSLPrice.setText(coin.priceToStr(mapPosition.get("SL").getPrice()));
				txtSLQty.setText(coin.qtyToStr(mapPosition.get("SL").getCoins()));
				txtSLUsd.setText(Convert.usdToStr(mapPosition.get("SL").getUsd()));
				txtSLDist.setText(Convert.dblToStrPercent(mapPosition.get("SL").getDist()));
			}

			if (mapPosition.containsKey("TP"))
			{
				txtTPPrice.setText(coin.priceToStr(mapPosition.get("TP").getPrice()));
				txtTPQty.setText(coin.qtyToStr(mapPosition.get("TP").getCoins()));
				txtTPUsd.setText(Convert.usdToStr(mapPosition.get("TP").getUsd()));
				txtTPDist.setText(Convert.dblToStrPercent(mapPosition.get("TP").getDist()));
			}

		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void exec()
	{
		INFO("");
		try
		{
			String side = rbLong.isSelected() ? "LONG" : "SHORT";
			BigDecimal price = new BigDecimal(txtShootPrice.getText());
			BigDecimal coins = new BigDecimal(txtShootQty.getText());

			// ----------------------------------------------------------------
			AccountBalance accBalance = BalanceService.getAccountBalanceNow();
			double balance = accBalance.getBalance().doubleValue();
			double withdrawAvailable = accBalance.getWithdrawAvailable().doubleValue();
			if (withdrawAvailable - (price.doubleValue() * coins.doubleValue()) < balance * Config.getBalance_min_available())
			{
				ERROR("Insufficient withdrawal available");
				return;
			}

			// ----------------------------------------------------------------
			String msg = String.format("Post order %s  /  %s  /  %s  /  %s ? *The price can be better than the selected one", coin.getName(), side, coin.priceToStr(price), coin.qtyToStr(coins));			

			if (JOptionPane.showConfirmDialog(null, msg) == 0)
			{
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				if (("SHORT".equals(side) && mrkPrice.doubleValue() > price.doubleValue()) ||
					("LONG".equals(side) && mrkPrice.doubleValue() < price.doubleValue()))
				{
					price = mrkPrice;
				}

				// String result = String.format("Post order %s  /  %s  /  %s  /  %s", coin.getName(), side.name(), coin.priceToStr(price), coin.coinsToStr(coins));
				String result = SimpleTrader.postOrder(coin, side, price, coins);

				INFO(result);
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

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
					FrmAddOrder frame = new FrmAddOrder();
					frame.setVisible(true);
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
		timer1 = new Timer(2000, taskPerformer1);
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
					String priceChangePercent = String.format("%.2f%%", symbolTicker.getPriceChangePercent());
					txt24h.setText(priceChangePercent);
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
