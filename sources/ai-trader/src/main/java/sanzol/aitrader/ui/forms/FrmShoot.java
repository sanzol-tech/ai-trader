package sanzol.aitrader.ui.forms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

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

import api.client.futures.model.AccountBalance;
import api.client.futures.model.PositionRisk;
import api.client.model.event.SymbolTickerEvent;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.model.GOrder;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.be.service.BalanceFuturesService;
import sanzol.aitrader.be.service.PositionFuturesService;
import sanzol.aitrader.be.service.PriceListener;
import sanzol.aitrader.be.service.PriceService;
import sanzol.aitrader.be.trade.SimpleTrade;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.Convert;
import sanzol.util.ExceptionUtils;

public class FrmShoot extends JFrame implements PriceListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Shoot";

	private static boolean isOpen = false;

	private Symbol coin;

	private JLabel lblError;
	private JPanel pnlContent;
	private JPanel pnlStatusBar;

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
	private JTextField txtShootDist;

	public FrmShoot()
	{
		initComponents();
		PriceService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 500);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmShoot.class.getResource("/resources/upDown.png")));
		setLocationRelativeTo(null);
		setResizable(false);

		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		lblError = new JLabel();


		txtShootPrice = new JTextField();
		txtShootPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootPrice.setBounds(108, 175, 86, 20);
		pnlContent.add(txtShootPrice);
		txtShootPrice.setColumns(10);

		txtShootQty = new JTextField();
		txtShootQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootQty.setBounds(204, 175, 86, 20);
		pnlContent.add(txtShootQty);
		txtShootQty.setColumns(10);

		txtPositionPrice = new JTextField();
		txtPositionPrice.setEditable(false);
		txtPositionPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionPrice.setBounds(108, 145, 86, 20);
		pnlContent.add(txtPositionPrice);
		txtPositionPrice.setColumns(10);

		JButton btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(31, 69, 178, 22);
		pnlContent.add(btnSearch);

		JLabel lblNewLabel = new JLabel("Price");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(108, 122, 86, 14);
		pnlContent.add(lblNewLabel);

		JLabel lblQuantity = new JLabel("Quantity");
		lblQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
		lblQuantity.setBounds(204, 122, 86, 14);
		pnlContent.add(lblQuantity);

		JLabel lblShoot = new JLabel("SHOOT");
		lblShoot.setBounds(31, 179, 67, 14);
		pnlContent.add(lblShoot);

		JLabel lblPosition = new JLabel("POSITION");
		lblPosition.setBounds(31, 149, 67, 14);
		pnlContent.add(lblPosition);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(31, 43, 86, 20);
		pnlContent.add(txtSymbolLeft);

		txtPositionQty = new JTextField();
		txtPositionQty.setEditable(false);
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(204, 145, 86, 20);
		pnlContent.add(txtPositionQty);

		JLabel lblSymbol = new JLabel("SYMBOL");
		lblSymbol.setBounds(31, 24, 86, 14);
		pnlContent.add(lblSymbol);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Config.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(123, 43, 86, 20);
		pnlContent.add(txtSymbolRight);

		JLabel lblResult = new JLabel("RESULT");
		lblResult.setBounds(31, 209, 67, 14);
		pnlContent.add(lblResult);

		txtResultPrice = new JTextField();
		txtResultPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultPrice.setEditable(false);
		txtResultPrice.setColumns(10);
		txtResultPrice.setBounds(108, 205, 86, 20);
		pnlContent.add(txtResultPrice);

		txtResultQty = new JTextField();
		txtResultQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultQty.setEditable(false);
		txtResultQty.setColumns(10);
		txtResultQty.setBounds(204, 205, 86, 20);
		pnlContent.add(txtResultQty);

		txtShootUsd = new JTextField();
		txtShootUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtShootUsd.setEditable(false);
		txtShootUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootUsd.setColumns(10);
		txtShootUsd.setBounds(300, 175, 86, 20);
		pnlContent.add(txtShootUsd);

		JLabel lblUsd = new JLabel("USD");
		lblUsd.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsd.setBounds(300, 122, 86, 14);
		pnlContent.add(lblUsd);

		txtPositionDist = new JTextField();
		txtPositionDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionDist.setEditable(false);
		txtPositionDist.setColumns(10);
		txtPositionDist.setBounds(403, 145, 86, 20);
		pnlContent.add(txtPositionDist);

		txtResultDist = new JTextField();
		txtResultDist.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtResultDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultDist.setEditable(false);
		txtResultDist.setColumns(10);
		txtResultDist.setBounds(403, 205, 86, 20);
		pnlContent.add(txtResultDist);

		JLabel lblDistPercent = new JLabel("Dist %");
		lblDistPercent.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDistPercent.setBounds(403, 122, 86, 14);
		pnlContent.add(lblDistPercent);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(300, 43, 86, 20);
		pnlContent.add(txtMarkPrice);

		JLabel lblMarkPrice = new JLabel("Price");
		lblMarkPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMarkPrice.setBounds(301, 24, 85, 14);
		pnlContent.add(lblMarkPrice);

		rbShort = new JRadioButton("SHORT");
		rbShort.setHorizontalAlignment(SwingConstants.LEFT);
		rbShort.setBounds(327, 265, 76, 23);
		pnlContent.add(rbShort);

		rbLong = new JRadioButton("LONG");
		rbLong.setHorizontalAlignment(SwingConstants.RIGHT);
		rbLong.setBounds(413, 265, 76, 23);
		pnlContent.add(rbLong);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbShort);
		bg1.add(rbLong);

		btnCalc05 = new JButton("x 0.5");
		btnCalc05.setOpaque(true);
		btnCalc05.setBounds(31, 265, 70, 23);
		pnlContent.add(btnCalc05);

		btnCalc075 = new JButton("x 0.75");
		btnCalc075.setOpaque(true);
		btnCalc075.setBounds(108, 265, 70, 23);
		pnlContent.add(btnCalc075);

		btnX1 = new JButton("x 1");
		btnX1.setOpaque(true);
		btnX1.setBounds(187, 265, 70, 23);
		pnlContent.add(btnX1);

		btnX2 = new JButton("x 2");
		btnX2.setOpaque(true);
		btnX2.setBounds(187, 299, 70, 23);
		pnlContent.add(btnX2);

		btnCalc = new JButton("CALC");
		btnCalc.setBounds(31, 332, 226, 23);
		btnCalc.setOpaque(true);
		pnlContent.add(btnCalc);

		btnPost = new JButton("POST ORDER");
		btnPost.setIcon(Styles.IMAGE_EXECUTE);
		btnPost.setBounds(327, 305, 162, 40);
		btnPost.setOpaque(true);
		pnlContent.add(btnPost);

		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(403, 43, 86, 20);
		pnlContent.add(txt24h);

		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl24Hs.setBounds(404, 24, 85, 14);
		pnlContent.add(lbl24Hs);

		txtPositionUsd = new JTextField();
		txtPositionUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtPositionUsd.setEditable(false);
		txtPositionUsd.setColumns(10);
		txtPositionUsd.setBounds(300, 145, 86, 20);
		pnlContent.add(txtPositionUsd);

		txtResultUsd = new JTextField();
		txtResultUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtResultUsd.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtResultUsd.setEditable(false);
		txtResultUsd.setColumns(10);
		txtResultUsd.setBounds(300, 206, 86, 20);
		pnlContent.add(txtResultUsd);

		txtShootDist = new JTextField();
		txtShootDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtShootDist.setEditable(false);
		txtShootDist.setColumns(10);
		txtShootDist.setBounds(403, 175, 86, 20);
		pnlContent.add(txtShootDist);

		btnCalc125 = new JButton("x 1.25");
		btnCalc125.setOpaque(true);
		btnCalc125.setBounds(31, 299, 70, 23);
		pnlContent.add(btnCalc125);

		btnCalc150 = new JButton("x 1.5");
		btnCalc150.setOpaque(true);
		btnCalc150.setBounds(108, 299, 70, 23);
		pnlContent.add(btnCalc150);


		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
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
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
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

		// ---------------------------------------------------------------------

		FrmShoot thisFrm = this;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PriceService.deattachRefreshObserver(thisFrm);
				isOpen = false;
			}
		});

		rbShort.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					btnPost.setBackground(Styles.COLOR_BTN_SHORT);
					btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
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
	}

	private void enableControls(boolean isPosition)
	{
		btnCalc05.setEnabled(isPosition);
		btnCalc075.setEnabled(isPosition);
		btnCalc125.setEnabled(isPosition);
		btnCalc150.setEnabled(isPosition);
		btnX1.setEnabled(isPosition);
		btnX2.setEnabled(isPosition);
		btnCalc.setEnabled(isPosition);

		rbShort.setEnabled(!isPosition);
		rbLong.setEnabled(!isPosition);
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

				PositionRisk positionRisk = PositionFuturesService.getPositionRisk(coin.getPair());
				boolean isPosition = positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0;
				enableControls(isPosition);

				if (isPosition)
				{
					txtPositionPrice.setText(coin.priceToStr(positionRisk.getEntryPrice()));

					double amt = Math.abs(positionRisk.getPositionAmt().doubleValue());
					txtPositionQty.setText(coin.qtyToStr(amt));

					if (positionRisk.getPositionAmt().doubleValue() < 0)
					{
						BigDecimal posDist = positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
						txtPositionDist.setText(Convert.toStrPercent(posDist));

						rbShort.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_SHORT);
						btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);

					}
					else
					{
						BigDecimal posDist = BigDecimal.ONE.subtract(positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP));
						txtPositionDist.setText(Convert.toStrPercent(posDist));

						rbLong.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_LONG);
						btnPost.setIcon(Styles.IMAGE_EXECUTE_LIGHT);
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
				shootQty = posQty.multiply(x).setScale(coin.getQtyPrecision(), RoundingMode.UP);

			// --- CALC -------------------------------------------------------
			Map<String, GOrder> mapPosition = SimpleTrade.calc(coin, side, posPrice, posQty, shootPrice, shootQty);

			// ----------------------------------------------------------------
			txtPositionUsd.setText(Convert.usdToStr(mapPosition.get("POS").getUsd()));
			txtPositionDist.setText(Convert.toStrPercent(mapPosition.get("POS").getDist()));

			txtShootPrice.setText(coin.priceToStr(mapPosition.get("SHOOT").getPrice()));
			txtShootQty.setText(coin.qtyToStr(mapPosition.get("SHOOT").getCoins()));
			txtShootUsd.setText(Convert.usdToStr(mapPosition.get("SHOOT").getUsd()));
			txtShootDist.setText(Convert.toStrPercent(mapPosition.get("SHOOT").getDist()));

			txtResultPrice.setText(coin.priceToStr(mapPosition.get("RESULT").getPrice()));
			txtResultQty.setText(coin.qtyToStr(mapPosition.get("RESULT").getCoins()));
			txtResultUsd.setText(Convert.usdToStr(mapPosition.get("RESULT").getUsd()));
			txtResultDist.setText(Convert.toStrPercent(mapPosition.get("RESULT").getDist()));
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private static boolean insufficientBalance(Double usdt)
	{
		AccountBalance accBalance = BalanceFuturesService.getAccountBalanceNow();
		double balance = accBalance.getBalance().doubleValue();
		double withdrawAvailable = accBalance.getWithdrawAvailable().doubleValue();

		return  (withdrawAvailable - (usdt / Config.getLeverage()) < balance * Config.getBalanceMinAvailable());
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
			if (insufficientBalance(price.doubleValue() * coins.doubleValue()))
			{
				ERROR("Insufficient withdrawal available");
				return;
			}

			// ----------------------------------------------------------------
			String msg = String.format("Post order %s  /  %s  /  %s  /  %s ? *The price can be better than the selected one", coin.getPair(), side, coin.priceToStr(price), coin.qtyToStr(coins));

			int resultOption = JOptionPane.showConfirmDialog(null, msg, "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				if (("SHORT".equals(side) && mrkPrice.doubleValue() > price.doubleValue()) ||
					("LONG".equals(side) && mrkPrice.doubleValue() < price.doubleValue()))
				{
					price = mrkPrice;
				}

				// String result = String.format("Post order %s  /  %s  /  %s  /  %s", coin.getName(), side.name(), coin.priceToStr(price), coin.coinsToStr(coins));
				String result = SimpleTrade.postOrder(coin, side, price, coins);

				INFO(result);
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
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
					FrmShoot frame = new FrmShoot();
					frame.setVisible(true);
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
