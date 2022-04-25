package sanzol.app.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.model.enums.PositionSide;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.OrderBookInfo;
import sanzol.app.model.Position;
import sanzol.app.model.Symbol;
import sanzol.app.service.OrderBookService;
import sanzol.app.task.PriceService;
import sanzol.app.trader.PositionTrader;
import sanzol.app.trader.PositionTrader.PostStyle;
import sanzol.app.trader.PositionTrader.TestMode;

public class FrmTrader extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol coin;
	private PositionTrader pMaker;

	private JPanel contentPane;

	private JButton btnClean;
	private JButton btnLong;
	private JButton btnPostOthers;
	private JButton btnSearch;
	private JButton btnShort;
	private JButton btnPostFirst;
	private JButton btnLongShock;
	private JButton btnShortShock;

	private JRadioButton rbCoins;
	private JRadioButton rbCoinsAuto;
	private JRadioButton rbPrice;
	private JRadioButton rbPriceNow;

	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;
	private JTextArea txtShowResult;

	private JTextField textCoins;
	private JTextField txtCoinsIncr;
	private JTextField txtBalanceStartPosition;
	private JTextField txtDistBeforeSL;
	private JTextField txtIterations;
	private JTextField textPrice;
	private JTextField txtPriceIncr;
	private JTextField textPriceShow;
	private JTextField txtTProfit;
	private JTextField txtError;
	private JTextField txtMaxAsk;
	private JTextField txtMaxBid;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JLabel lblPercetSymbol;
	private JLabel lblPercetSymbol_1;
	private JLabel lblPercetSymbol_2;
	private JLabel lblPercetSymbol_3;
	private JLabel lblPercetSymbol_4;

	public FrmTrader()
	{
		initComponents();

		pageload();
		loadConfig();
	}

	private void initComponents() 
	{
		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1152, 652);
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmTrader.class.getResource("/resources/logo.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnLong = new JButton("LONG");
		btnLong.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLong.setBackground(Styles.COLOR_BTN_LONG);
		btnLong.setOpaque(true);
		btnLong.setBounds(408, 102, 111, 34);
		contentPane.add(btnLong);

		btnShort = new JButton("SHORT");
		btnShort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShort.setBackground(Styles.COLOR_BTN_SHORT);
		btnShort.setOpaque(true);
		btnShort.setBounds(408, 144, 111, 34);
		contentPane.add(btnShort);

		btnLongShock = new JButton(CharConstants.ARROW_UP);
		btnLongShock.setToolTipText("Create Long in this price");
		btnLongShock.setOpaque(true);
		btnLongShock.setBackground(Styles.COLOR_BTN_LONG);
		btnLongShock.setBounds(1072, 19, 46, 22);
		contentPane.add(btnLongShock);

		btnShortShock = new JButton(CharConstants.ARROW_DOWN);
		btnShortShock.setToolTipText("Create Short in this price");
		btnShortShock.setOpaque(true);
		btnShortShock.setBackground(Styles.COLOR_BTN_SHORT);
		btnShortShock.setBounds(782, 192, 46, 22);
		contentPane.add(btnShortShock);

		btnClean = new JButton("CLEAN");
		btnClean.setBackground(Styles.COLOR_BTN);
		btnClean.setOpaque(true);
		btnClean.setBounds(257, 219, 78, 31);
		contentPane.add(btnClean);

		textPrice = new JTextField();
		textPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		textPrice.setBounds(408, 20, 111, 20);
		contentPane.add(textPrice);
		textPrice.setColumns(10);

		textCoins = new JTextField();
		textCoins.setHorizontalAlignment(SwingConstants.RIGHT);
		textCoins.setBounds(408, 51, 111, 20);
		contentPane.add(textCoins);
		textCoins.setColumns(10);

		rbPriceNow = new JRadioButton("NOW");
		rbPriceNow.setBounds(249, 19, 72, 23);
		contentPane.add(rbPriceNow);

		rbPrice = new JRadioButton("PRICE");
		rbPrice.setSelected(true);
		rbPrice.setBounds(323, 19, 72, 23);
		contentPane.add(rbPrice);

		rbCoinsAuto = new JRadioButton("AUTO");
		rbCoinsAuto.setSelected(true);
		rbCoinsAuto.setBounds(249, 50, 72, 23);
		contentPane.add(rbCoinsAuto);

		rbCoins = new JRadioButton("COINS");
		rbCoins.setBounds(323, 50, 72, 23);
		contentPane.add(rbCoins);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbPriceNow);
		bg1.add(rbPrice);

		ButtonGroup bg2 = new javax.swing.ButtonGroup();
		bg2.add(rbCoinsAuto);
		bg2.add(rbCoins);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setBounds(132, 28, 86, 22);
		contentPane.add(txtSymbolRight);
		txtSymbolRight.setColumns(10);

		JLabel lblPriceIncr = new JLabel("Price Incr");
		lblPriceIncr.setHorizontalAlignment(SwingConstants.LEFT);
		lblPriceIncr.setBounds(15, 135, 70, 14);
		contentPane.add(lblPriceIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setBounds(91, 134, 60, 20);
		contentPane.add(txtPriceIncr);
		txtPriceIncr.setColumns(10);

		JLabel lblCoinsIncr = new JLabel("Coins Incr");
		lblCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCoinsIncr.setBounds(170, 108, 86, 14);
		contentPane.add(lblCoinsIncr);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setBounds(266, 106, 60, 20);
		contentPane.add(txtCoinsIncr);
		txtCoinsIncr.setColumns(10);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		lblItarations.setBounds(15, 108, 70, 14);
		contentPane.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setBounds(91, 106, 60, 20);
		contentPane.add(txtIterations);
		txtIterations.setColumns(10);

		JLabel lblTProfit = new JLabel("T. Profit");
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		lblTProfit.setBounds(15, 164, 70, 14);
		contentPane.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setBounds(91, 162, 60, 20);
		contentPane.add(txtTProfit);
		txtTProfit.setColumns(10);

		txtShowResult = new JTextArea();
		txtShowResult.setEditable(false);
		txtShowResult.setFont(new Font("Courier New", Font.PLAIN, 12));

		JScrollPane scroll = new JScrollPane(txtShowResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(15, 261, 1102, 330);
		contentPane.add(scroll);

		btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setBackground(Styles.COLOR_BTN);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(15, 59, 106, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setBounds(15, 28, 106, 22);
		contentPane.add(txtSymbolLeft);
		txtSymbolLeft.setColumns(10);

		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		txtDistBeforeSL.setBounds(266, 134, 60, 20);
		contentPane.add(txtDistBeforeSL);

		JLabel lblDistSL = new JLabel("Last to SL");
		lblDistSL.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDistSL.setBounds(180, 135, 76, 14);
		contentPane.add(lblDistSL);

		txtBalanceStartPosition = new JTextField();
		txtBalanceStartPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceStartPosition.setColumns(10);
		txtBalanceStartPosition.setBounds(266, 162, 60, 20);
		contentPane.add(txtBalanceStartPosition);

		JLabel lblPriceIncr_1 = new JLabel("Balance");
		lblPriceIncr_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPriceIncr_1.setBounds(180, 164, 76, 14);
		contentPane.add(lblPriceIncr_1);

		scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookAsk.setBounds(557, 19, 270, 162);
		contentPane.add(scrollOBookAsk);

		txtOBookAsk = new JTextArea();
		txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
		txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookAsk.setEditable(false);
		scrollOBookAsk.setViewportView(txtOBookAsk);

		scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookBid.setBounds(847, 51, 270, 162);
		contentPane.add(scrollOBookBid);

		txtOBookBid = new JTextArea();
		txtOBookBid.setForeground(Styles.COLOR_TEXT_LONG);
		txtOBookBid.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookBid.setEditable(false);
		scrollOBookBid.setViewportView(txtOBookBid);

		txtMaxAsk = new JTextField();
		txtMaxAsk.setForeground(new Color(220, 20, 60));
		txtMaxAsk.setEditable(false);
		txtMaxAsk.setBounds(557, 193, 220, 20);
		contentPane.add(txtMaxAsk);
		txtMaxAsk.setColumns(10);

		txtMaxBid = new JTextField();
		txtMaxBid.setForeground(new Color(46, 139, 87));
		txtMaxBid.setEditable(false);
		txtMaxBid.setColumns(10);
		txtMaxBid.setBounds(847, 20, 220, 20);
		contentPane.add(txtMaxBid);

		txtError = new JTextField();
		txtError.setHorizontalAlignment(SwingConstants.RIGHT);
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setBounds(359, 229, 758, 20);
		contentPane.add(txtError);
		txtError.setColumns(10);

		btnPostFirst = new JButton("First");
		btnPostFirst.setEnabled(false);
		btnPostFirst.setToolTipText("Post only first order");
		btnPostFirst.setOpaque(true);
		btnPostFirst.setBackground(Styles.COLOR_BTN_ALT2);
		btnPostFirst.setBounds(15, 219, 74, 31);
		contentPane.add(btnPostFirst);

		btnPostOthers = new JButton("Others");
		btnPostOthers.setEnabled(false);
		btnPostOthers.setToolTipText("Post the other orders");
		btnPostOthers.setOpaque(true);
		btnPostOthers.setBackground(Styles.COLOR_BTN_ALT2);
		btnPostOthers.setBounds(99, 219, 74, 31);
		contentPane.add(btnPostOthers);

		textPriceShow = new JTextField();
		textPriceShow.setForeground(Styles.COLOR_TEXT_ALT1);
		textPriceShow.setHorizontalAlignment(SwingConstants.CENTER);
		textPriceShow.setFont(new Font("Tahoma", Font.BOLD, 12));
		textPriceShow.setEditable(false);
		textPriceShow.setColumns(10);
		textPriceShow.setBounds(132, 60, 86, 22);
		contentPane.add(textPriceShow);

		JLabel lblNewLabel = new JLabel("C O I N");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(16, 10, 203, 14);
		contentPane.add(lblNewLabel);

		lblPercetSymbol = new JLabel("%");
		lblPercetSymbol.setBounds(156, 135, 25, 14);
		contentPane.add(lblPercetSymbol);

		lblPercetSymbol_1 = new JLabel("%");
		lblPercetSymbol_1.setBounds(156, 164, 25, 14);
		contentPane.add(lblPercetSymbol_1);

		lblPercetSymbol_2 = new JLabel("%");
		lblPercetSymbol_2.setBounds(331, 108, 25, 14);
		contentPane.add(lblPercetSymbol_2);

		lblPercetSymbol_3 = new JLabel("%");
		lblPercetSymbol_3.setBounds(331, 135, 25, 14);
		contentPane.add(lblPercetSymbol_3);

		lblPercetSymbol_4 = new JLabel("%");
		lblPercetSymbol_4.setBounds(331, 164, 25, 14);
		contentPane.add(lblPercetSymbol_4);

		// ---------------------------------------------------------------------

		rbPriceNow.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					textPrice.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					textPrice.setEnabled(true);
				}
			}
		});

		rbCoinsAuto.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					textCoins.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					textCoins.setEnabled(true);
				}
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchCoin();
			}
		});

		btnShort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createShort();
			}
		});

		btnLong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createLong();
			}
		});

		btnShortShock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createShortWithShock();
			}
		});

		btnLongShock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createLongWithShock();
			}
		});

		btnPostFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				postOrders(PostStyle.FIRST);
			}
		});

		btnPostOthers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				postOrders(PostStyle.OTHERS);
			}
		});

		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});

	}

	private void pageload()
	{
		INFO("");
		try
		{
			ERROR(Application.getError());
			PositionTrader.TEST_MODE = TestMode.TEST;
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void loadConfig()
	{
		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr.setText(dblToStrPercent(Config.getPrice_increment())); 
		txtCoinsIncr.setText(dblToStrPercent(Config.getCoins_increment()));
		txtDistBeforeSL.setText(dblToStrPercent(Config.getStoploss_increment()));
		txtTProfit.setText(dblToStrPercent(Config.getTakeprofit()));
		txtBalanceStartPosition.setText(dblToStrPercent(Config.getBalance_start_position()));
	}

	public static void launch()
	{
		launch(null, null, null);
	}

	public static void launch(String symbolLeft, String side, String price)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				FrmTrader frame = null;

				try
				{
					frame = new FrmTrader();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}

				try
				{
					frame.pageload();
					frame.startTimer();

					if (symbolLeft != null && !symbolLeft.isEmpty())
					{
						frame.txtSymbolLeft.setText(symbolLeft);
						frame.searchCoin();
					}
					if (side != null && price != null)
					{
						frame.textPrice.setText(price);
						if ("SHORT".equals(side))
							frame.createShort();
						else
							frame.createLong();
					}
				}
				catch (Exception e)
				{
					frame.ERROR(e);
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
				refreshAuto(true, false);
			}
		};
		Timer timer1 = new Timer(2000, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	// ----------------------------------------------------------------------------------

	private void refreshAuto(boolean rfCoin, boolean rfDepth)
	{
		refresh(rfCoin, rfDepth);
	}

	private void refresh(boolean rfCoin, boolean rfDepth)
	{
		try
		{
			if (rfCoin && coin != null)
			{
				BigDecimal price = PriceService.getLastPrice(coin);
				textPriceShow.setText(coin.priceToStr(price));
			}

			if (rfDepth && coin != null)
			{
				OrderBookInfo points = OrderBookService.getShoks(coin);
				txtOBookAsk.setText(OrderBookService.toStringInv(coin, points.getAsksGrp()));
				txtMaxAsk.setText("SHORT " + points.getStrShShock());
				txtOBookBid.setText(OrderBookService.toString(coin, points.getBidsGrp()));
				txtOBookBid.setCaretPosition(0);
				txtMaxBid.setText("LONG " + points.getStrLgShock());

				String distShLg = coin.priceToStr((points.getShShock().doubleValue() / points.getLgShock().doubleValue() - 1.0) * 100.0);
				txtMaxAsk.setToolTipText("Short-Long : " +  distShLg  + "%");
				txtMaxBid.setToolTipText("Short-Long : " +  distShLg  + "%");
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	private void searchCoin()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			setTitle(TITLE + " - " + symbol);
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

			BigDecimal price = PriceService.getLastPrice(coin);
			textPriceShow.setText(coin.priceToStr(price));
			textPrice.setText(coin.priceToStr(price));

			refresh(false, true);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	private void createShort()
	{
		INFO("");
		try
		{
			//------------------------------------------------------------------
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

			//------------------------------------------------------------------
			Position position = Position.getInstance(coin, PositionSide.SHORT);

			position.setIterations(Integer.valueOf(txtIterations.getText()));
			position.setPriceIncr(strPercentToDbl(txtPriceIncr.getText()));
			position.setCoinsIncr(strPercentToDbl(txtCoinsIncr.getText()));
			position.setDistBeforeSL(strPercentToDbl(txtDistBeforeSL.getText()));
			position.setTakeProfit(strPercentToDbl(txtTProfit.getText()));

			if (rbPriceNow.isSelected())
			{
				position.withPriceAuto();
			}
			else
			{
				if (!NumberUtils.isCreatable(textPrice.getText()))
				{
					ERROR("Must enter the price");
					return;
				}
				double price = Double.valueOf(textPrice.getText());
				position.setInPrice(price);
			}

			if (rbCoinsAuto.isSelected())
			{
				double balancePercent = strPercentToDbl(txtBalanceStartPosition.getText());
				position.withCoinAuto(balancePercent);
			}
			else
			{
				if (!NumberUtils.isCreatable(textCoins.getText()))
				{
					ERROR("Must enter the coins");
					return;
				}
				double coins = Double.valueOf(textCoins.getText());
				position.setInCoins(coins);
			}

			//------------------------------------------------------------------
			pMaker = new PositionTrader(position);
			pMaker.createShort();

			//------------------------------------------------------------------
			txtShowResult.setForeground(Styles.COLOR_TEXT_SHORT);
			txtShowResult.setText(position.toString());

			btnPostFirst.setEnabled(true);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void createLong()
	{
		INFO("");
		try
		{
			//------------------------------------------------------------------
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

			//------------------------------------------------------------------
			Position position = Position.getInstance(coin, PositionSide.LONG);

			position.setIterations(Integer.valueOf(txtIterations.getText()));
			position.setPriceIncr(strPercentToDbl(txtPriceIncr.getText()));
			position.setCoinsIncr(strPercentToDbl(txtCoinsIncr.getText()));
			position.setDistBeforeSL(strPercentToDbl(txtDistBeforeSL.getText()));
			position.setTakeProfit(strPercentToDbl(txtTProfit.getText()));

			if (rbPriceNow.isSelected())
			{
				position.withPriceAuto();
			}
			else
			{
				if (!NumberUtils.isCreatable(textPrice.getText()))
				{
					ERROR("Must enter the price");
					return;
				}
				double price = Double.valueOf(textPrice.getText());
				position.setInPrice(price);
			}

			if (rbCoinsAuto.isSelected())
			{
				double balancePercent = strPercentToDbl(txtBalanceStartPosition.getText());
				position.withCoinAuto(balancePercent);
			}
			else
			{
				if (!NumberUtils.isCreatable(textCoins.getText()))
				{
					ERROR("Must enter the coins");
					return;
				}
				double coins = Double.valueOf(textCoins.getText());
				position.setInCoins(coins);
			}

			//------------------------------------------------------------------
			pMaker = new PositionTrader(position);
			pMaker.createLong();

			//------------------------------------------------------------------
			txtShowResult.setForeground(Styles.COLOR_TEXT_LONG);
			txtShowResult.setText(position.toString());

			btnPostFirst.setEnabled(true);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	public void createShortWithShock()
	{
		try
		{
			rbPrice.setSelected(true);
			textPrice.setText(txtMaxAsk.getText().split(" ")[1]);
			createShort();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	public void createLongWithShock()
	{
		try
		{
			rbPrice.setSelected(true);
			textPrice.setText(txtMaxBid.getText().split(" ")[1]);
			createLong();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void clean()
	{
		pMaker = null;
		INFO("");
		txtShowResult.setText("");

		btnPostFirst.setEnabled(false);
		btnPostOthers.setEnabled(false);
	}

	// ----------------------------------------------------------------------------------

	private void postOrders(PostStyle postStyle)
	{
		INFO("");
		try
		{
			if (JOptionPane.showConfirmDialog(null, "Do you like post this position ?") == 0)
			{
				PositionTrader.TEST_MODE = TestMode.PROD;

				String result = pMaker.post(postStyle);
				if (result != null)
				{
					ERROR(result);
				}

				txtShowResult.setForeground(pMaker.getPosition().getSide() == PositionSide.SHORT ? Styles.COLOR_TEXT_SHORT : Styles.COLOR_TEXT_LONG);
				txtShowResult.setText(pMaker.getPosition().toString());
				save(coin.getName() + "_" + pMaker.getPosition().getSide().name());

				// -------------------------------------------------------------
				if (PositionTrader.TEST_MODE.equals(TestMode.PROD))
				{
					btnPostFirst.setEnabled(false);
					btnPostOthers.setEnabled(postStyle.equals(PostStyle.FIRST));
				}
			}
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	public void save(String filename)
	{
		try
		{
			File path = new File(Constants.DEFAULT_USER_FOLDER);
			if (!path.exists()) 
			{
				path.mkdirs();
			}

			File logFile = new File(path, filename + ".log");
			FileUtils.writeStringToFile(logFile, txtShowResult.getText(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	// ----------------------------------------------------------------------------------

	public static void createDirectory(File fileProcessPath) 
	{
		if (!fileProcessPath.exists()) 
		{
			fileProcessPath.mkdirs();
		}
	}

	private static String dblToStrPercent(Double d)
	{
		if (d == null)
			return "";

		return String.valueOf(DoubleRounder.round(d * 100, 2));
	}

	private static Double strPercentToDbl(String str)
	{
		if (str == null)
			return null;

		return DoubleRounder.round(Double.valueOf(str) / 100, 4);
	}

	// ----------------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
	}

	public void ERROR(String msg)
	{
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		txtError.setForeground(Styles.COLOR_TEXT_INFO);
		txtError.setText(" " + msg);
	}

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		Application.initialize();
		Application.initializeUI();
		launch();
	}

}
