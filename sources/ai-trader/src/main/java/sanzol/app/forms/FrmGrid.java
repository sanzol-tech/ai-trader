package sanzol.app.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.Position;
import sanzol.app.model.PriceQty;
import sanzol.app.model.Symbol;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.trader.PositionTrader;
import sanzol.app.trader.PositionTrader.PostStyle;
import sanzol.app.util.Convert;

public class FrmGrid extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol coin;
	private PositionTrader pMaker;
	private boolean isOpenPosition = false;

	private Timer timer1;

	private JPanel contentPane;
	private JPanel pnlConfig;
	private JPanel pnlPosition;

	private JButton btnClean;
	private JButton btnLong;
	private JButton btnPostFirst;
	private JButton btnPostOthers;
	private JButton btnSearch;
	private JButton btnShort;

	private JLabel lblPrice;
	private JLabel lblQty;

	private JRadioButton rbPriceLimit;
	private JRadioButton rbPriceMark;
	private JRadioButton rbQty;
	private JRadioButton rbQtyBalance;

	private JTextArea txtShowResult;

	private JTextField txtGPrice1;
	private JTextField txtGPrice2;
	private JTextField txtGPrice3;
	private JTextField txtGPrice4;
	private JTextField txtGPrice5;
	private JTextField txtGPrice6;
	private JTextField txtGPrice7;
	private JTextField txtGPrice8;

	private JTextField txtGQty1;
	private JTextField txtGQty2;
	private JTextField txtGQty3;
	private JTextField txtGQty4;
	private JTextField txtGQty5;
	private JTextField txtGQty6;
	private JTextField txtGQty7;
	private JTextField txtGQty8;

	private JTextField txtError;
	private JTextField txtBalancePercent;
	private JTextField txtIterations;
	private JTextField txtMarkPrice;
	private JTextField txtPositionPrice;
	private JTextField txtPositionQty;
	private JTextField txtPrice;
	private JTextField txtPriceIncr;
	private JTextField txtPriceShow;
	private JTextField txtQty;
	private JTextField txtQtyIncr;
	private JTextField txtSLossDist;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtTProfit;

	public FrmGrid()
	{
		initComponents();
		loadConfig();
		startTimer();
	}

	private void initComponents() 
	{
		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1152, 640);
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/upDown.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnLong = new JButton("LONG");
		btnLong.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLong.setBackground(Styles.COLOR_BTN_LONG);
		btnLong.setOpaque(true);
		btnLong.setBounds(1015, 116, 105, 32);
		contentPane.add(btnLong);

		btnClean = new JButton("CLEAN");
		btnClean.setOpaque(true);
		btnClean.setBounds(1040, 561, 78, 31);
		contentPane.add(btnClean);

		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPrice.setBounds(368, 28, 90, 22);
		contentPane.add(txtPrice);
		txtPrice.setColumns(10);

		txtQty = new JTextField();
		txtQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQty.setBounds(581, 28, 90, 22);
		contentPane.add(txtQty);
		txtQty.setColumns(10);

		rbPriceMark = new JRadioButton("MARK");
		rbPriceMark.setBounds(292, 58, 68, 23);
		contentPane.add(rbPriceMark);

		rbPriceLimit = new JRadioButton("PRICE");
		rbPriceLimit.setSelected(true);
		rbPriceLimit.setBounds(292, 28, 68, 23);
		contentPane.add(rbPriceLimit);

		rbQtyBalance = new JRadioButton("BALANCE %");
		rbQtyBalance.setSelected(true);
		rbQtyBalance.setBounds(480, 58, 95, 23);
		contentPane.add(rbQtyBalance);

		rbQty = new JRadioButton("COINS QTY");
		rbQty.setBounds(480, 28, 95, 23);
		contentPane.add(rbQty);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbPriceMark);
		bg1.add(rbPriceLimit);

		ButtonGroup bg2 = new javax.swing.ButtonGroup();
		bg2.add(rbQtyBalance);
		bg2.add(rbQty);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setBounds(132, 28, 104, 22);
		contentPane.add(txtSymbolRight);
		txtSymbolRight.setColumns(10);

		txtShowResult = new JTextArea();
		txtShowResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtShowResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtShowResult.setEditable(false);
		txtShowResult.setFont(new Font("Courier New", Font.PLAIN, 12));

		JScrollPane scroll = new JScrollPane(txtShowResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(16, 218, 1102, 330);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scroll);

		btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(15, 59, 106, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setBounds(15, 28, 106, 22);
		contentPane.add(txtSymbolLeft);
		txtSymbolLeft.setColumns(10);

		txtBalancePercent = new JTextField();
		txtBalancePercent.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalancePercent.setColumns(10);
		txtBalancePercent.setBounds(581, 58, 90, 22);
		contentPane.add(txtBalancePercent);

		txtError = new JTextField();
		txtError.setHorizontalAlignment(SwingConstants.RIGHT);
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setBounds(16, 570, 998, 20);
		contentPane.add(txtError);
		txtError.setColumns(10);

		btnPostFirst = new JButton("First");
		btnPostFirst.setEnabled(false);
		btnPostFirst.setToolTipText("Post only first order");
		btnPostFirst.setOpaque(true);
		btnPostFirst.setBounds(900, 164, 105, 32);
		contentPane.add(btnPostFirst);

		btnPostOthers = new JButton("Others");
		btnPostOthers.setEnabled(false);
		btnPostOthers.setToolTipText("Post the other orders");
		btnPostOthers.setOpaque(true);
		btnPostOthers.setBounds(1015, 164, 105, 32);
		contentPane.add(btnPostOthers);

		txtPriceShow = new JTextField();
		txtPriceShow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtPriceShow.setHorizontalAlignment(SwingConstants.CENTER);
		txtPriceShow.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtPriceShow.setEditable(false);
		txtPriceShow.setColumns(10);
		txtPriceShow.setBounds(132, 60, 104, 22);
		contentPane.add(txtPriceShow);

		JLabel lblNewLabel = new JLabel("C O I N");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(16, 10, 203, 14);
		contentPane.add(lblNewLabel);
		
		txtMarkPrice = new JTextField();
		txtMarkPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMarkPrice.setForeground((Color) null);
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(368, 58, 90, 22);
		contentPane.add(txtMarkPrice);
		
		JPanel pndGrid = new JPanel();
		pndGrid.setBorder(UIManager.getBorder("TextField.border"));
		pndGrid.setBounds(228, 116, 642, 80);
		contentPane.add(pndGrid);
		pndGrid.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Price %");
		lblNewLabel_2.setBounds(20, 20, 53, 14);
		pndGrid.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel("Qty %");
		lblNewLabel_1.setBounds(20, 47, 53, 14);
		pndGrid.add(lblNewLabel_1);
		
		txtGPrice1 = new JTextField();
		txtGPrice1.setBounds(83, 18, 60, 20);
		txtGPrice1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice1.setColumns(10);
		pndGrid.add(txtGPrice1);
		
		txtGPrice2 = new JTextField();
		txtGPrice2.setBounds(151, 18, 60, 20);
		txtGPrice2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice2.setColumns(10);
		pndGrid.add(txtGPrice2);
		
		txtGQty2 = new JTextField();
		txtGQty2.setBounds(151, 46, 60, 20);
		txtGQty2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty2.setColumns(10);
		pndGrid.add(txtGQty2);
		
		txtGQty1 = new JTextField();
		txtGQty1.setBounds(83, 46, 60, 20);
		txtGQty1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty1.setColumns(10);
		pndGrid.add(txtGQty1);
		
		txtGPrice3 = new JTextField();
		txtGPrice3.setBounds(219, 18, 60, 20);
		txtGPrice3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice3.setColumns(10);
		pndGrid.add(txtGPrice3);
		
		txtGQty3 = new JTextField();
		txtGQty3.setBounds(219, 46, 60, 20);
		txtGQty3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty3.setColumns(10);
		pndGrid.add(txtGQty3);
		
		txtGPrice4 = new JTextField();
		txtGPrice4.setBounds(287, 18, 60, 20);
		txtGPrice4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice4.setColumns(10);
		pndGrid.add(txtGPrice4);
		
		txtGQty4 = new JTextField();
		txtGQty4.setBounds(287, 46, 60, 20);
		txtGQty4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty4.setColumns(10);
		pndGrid.add(txtGQty4);
		
		txtGPrice5 = new JTextField();
		txtGPrice5.setBounds(355, 18, 60, 20);
		txtGPrice5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice5.setColumns(10);
		pndGrid.add(txtGPrice5);
		
		txtGQty5 = new JTextField();
		txtGQty5.setBounds(355, 46, 60, 20);
		txtGQty5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty5.setColumns(10);
		pndGrid.add(txtGQty5);
		
		txtGPrice6 = new JTextField();
		txtGPrice6.setBounds(423, 18, 60, 20);
		txtGPrice6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice6.setColumns(10);
		pndGrid.add(txtGPrice6);
		
		txtGQty6 = new JTextField();
		txtGQty6.setBounds(423, 46, 60, 20);
		txtGQty6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty6.setColumns(10);
		pndGrid.add(txtGQty6);
		
		txtGPrice7 = new JTextField();
		txtGPrice7.setBounds(491, 18, 60, 20);
		txtGPrice7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice7.setColumns(10);
		pndGrid.add(txtGPrice7);
		
		txtGQty7 = new JTextField();
		txtGQty7.setBounds(491, 46, 60, 20);
		txtGQty7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty7.setColumns(10);
		pndGrid.add(txtGQty7);
		
		txtGPrice8 = new JTextField();
		txtGPrice8.setBounds(559, 18, 60, 20);
		txtGPrice8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice8.setColumns(10);
		pndGrid.add(txtGPrice8);
		
		txtGQty8 = new JTextField();
		txtGQty8.setBounds(559, 46, 60, 20);
		txtGQty8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty8.setColumns(10);
		pndGrid.add(txtGQty8);

		btnShort = new JButton("SHORT");
		btnShort.setBounds(900, 116, 105, 32);
		btnShort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShort.setBackground(Styles.COLOR_BTN_SHORT);
		btnShort.setOpaque(true);
		contentPane.add(btnShort);

		pnlConfig = new JPanel();
		pnlConfig.setBorder(UIManager.getBorder("TextField.border"));
		pnlConfig.setBounds(714, 28, 404, 73);
		pnlConfig.setLayout(null);
		contentPane.add(pnlConfig);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(20, 12, 60, 14);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setBounds(20, 35, 60, 20);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		pnlConfig.add(txtIterations);
		
		JLabel lblPriceIncr = new JLabel("Price %");
		lblPriceIncr.setBounds(96, 11, 60, 14);
		lblPriceIncr.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblPriceIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(96, 35, 60, 20);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		pnlConfig.add(txtPriceIncr);

		txtQtyIncr = new JTextField();
		txtQtyIncr.setBounds(172, 35, 60, 20);
		txtQtyIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQtyIncr.setColumns(10);
		pnlConfig.add(txtQtyIncr);

		JLabel lblCoinsIncr = new JLabel("Qty %");
		lblCoinsIncr.setBounds(172, 11, 60, 14);
		lblCoinsIncr.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblCoinsIncr);

		JLabel lblDistSL = new JLabel("SL %");
		lblDistSL.setBounds(248, 12, 60, 14);
		pnlConfig.add(lblDistSL);

		txtSLossDist = new JTextField();
		txtSLossDist.setBounds(248, 35, 60, 20);
		txtSLossDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLossDist.setColumns(10);
		pnlConfig.add(txtSLossDist);

		JLabel lblTProfit = new JLabel("TP %");
		lblTProfit.setBounds(324, 12, 60, 14);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setBounds(324, 35, 60, 20);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		pnlConfig.add(txtTProfit);
		
		pnlPosition = new JPanel();
		pnlPosition.setLayout(null);
		pnlPosition.setBorder(new TitledBorder(null, " Position ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlPosition.setBounds(16, 107, 190, 91);
		contentPane.add(pnlPosition);

		lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setBounds(21, 26, 45, 14);
		pnlPosition.add(lblPrice);

		txtPositionPrice = new JTextField();
		txtPositionPrice.setEditable(false);
		txtPositionPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionPrice.setColumns(10);
		txtPositionPrice.setBounds(76, 26, 92, 20);
		pnlPosition.add(txtPositionPrice);

		lblQty = new JLabel("Qty");
		lblQty.setHorizontalAlignment(SwingConstants.LEFT);
		lblQty.setBounds(21, 54, 45, 14);
		pnlPosition.add(lblQty);

		txtPositionQty = new JTextField();
		txtPositionQty.setEditable(false);
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(76, 54, 92, 20);
		pnlPosition.add(txtPositionQty);

		btnShort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createShort();
			}
		});

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				timer1.stop();
			}
		});

		rbPriceMark.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtPrice.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					txtPrice.setEnabled(true);
				}
			}
		});
		rbQtyBalance.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtQty.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					txtQty.setEnabled(true);
				}
			}
		});

		txtIterations.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		txtPriceIncr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		txtQtyIncr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnLong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createLong();
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

	private void loadConfig()
	{
		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPrice_increment())); 
		txtQtyIncr.setText(Convert.dblToStrPercent(Config.getCoins_increment()));
		txtSLossDist.setText(Convert.dblToStrPercent(Config.getStoploss_increment()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));
		txtBalancePercent.setText(Convert.dblToStrPercent(Config.getPosition_start_qty()));

		generateGrid();
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
				FrmGrid frame = null;

				try
				{
					frame = new FrmGrid();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}

				try
				{
					if (symbolLeft != null && !symbolLeft.isEmpty())
					{
						frame.txtSymbolLeft.setText(symbolLeft);
						frame.search();
					}
					if (side != null && price != null)
					{
						frame.txtPrice.setText(price);
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
				refresh();
			}
		};
		timer1 = new Timer(3000, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	// ------------------------------------------------------------------------

	private void refresh()
	{
		try
		{
			if (coin != null)
			{
				BigDecimal price = PriceService.getLastPrice(coin);
				txtPriceShow.setText(coin.priceToStr(price));
				txtMarkPrice.setText(coin.priceToStr(price));
				
				searchPosition();
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

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

				BigDecimal price = PriceService.getLastPrice(coin);
				txtPriceShow.setText(coin.priceToStr(price));
				txtMarkPrice.setText(coin.priceToStr(price));
				
				searchPosition();
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

	private void searchPosition()
	{
		//PositionRisk positionRisk = PositionService.getPositionRisk("BTCBUSD");
		PositionRisk positionRisk = PositionService.getPositionRisk(coin.getName());
		isOpenPosition = (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0);
		if (isOpenPosition)
		{
			txtPositionPrice.setText(coin.priceToStr(positionRisk.getEntryPrice()));
			double amt = positionRisk.getPositionAmt().doubleValue();
			txtPositionQty.setText(coin.qtyToStr(amt));

			if (amt > 0)
				btnShort.setEnabled(false);
			if (amt < 0)
				btnLong.setEnabled(false);

			rbPriceLimit.setEnabled(false);
			rbPriceMark.setEnabled(false);
			txtPrice.setEnabled(false);
			txtMarkPrice.setEnabled(false);
			rbQty.setEnabled(false);
			rbQtyBalance.setEnabled(false);
			txtQty.setEnabled(false);
			txtBalancePercent.setEnabled(false);
		}
		else
		{
			btnShort.setEnabled(true);
			btnLong.setEnabled(true);

			rbPriceLimit.setEnabled(true);
			rbPriceMark.setEnabled(true);
			txtPrice.setEnabled(true);
			txtMarkPrice.setEnabled(true);
			rbQty.setEnabled(true);
			rbQtyBalance.setEnabled(true);
			txtQty.setEnabled(true);
			txtBalancePercent.setEnabled(true);
		}
	}

	// ------------------------------------------------------------------------

	private void generateGrid()
	{
		int iterations = Integer.valueOf(txtIterations.getText());
		double priceIncr = Convert.strPercentToDbl(txtPriceIncr.getText());
		double qtyIncr = Convert.strPercentToDbl(txtQtyIncr.getText());

		if (iterations >= 1)
		{
			txtGPrice1.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty1.setText("0.0");
		}
		else
		{
			txtGPrice1.setText("");
			txtGQty1.setText("");
		}
		if (iterations >= 2)
		{
			txtGPrice2.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty2.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice2.setText("");
			txtGQty2.setText("");
		}
		if (iterations >= 3)
		{
			txtGPrice3.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty3.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice3.setText("");
			txtGQty3.setText("");
		}
		if (iterations >= 4)
		{
			txtGPrice4.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty4.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice4.setText("");
			txtGQty4.setText("");
		}
		if (iterations >= 5)
		{
			txtGPrice5.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty5.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice5.setText("");
			txtGQty5.setText("");
		}
		if (iterations >= 6)
		{
			txtGPrice6.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty6.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice6.setText("");
			txtGQty6.setText("");
		}
		if (iterations >= 7)
		{
			txtGPrice7.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty7.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice7.setText("");
			txtGQty7.setText("");
		}
		if (iterations >= 8)
		{
			txtGPrice8.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty8.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice8.setText("");
			txtGQty8.setText("");
		}
	}
	
	private List<PriceQty> acquireGrid()
	{
		List<PriceQty> lstPriceQty = new ArrayList<PriceQty>();
		
		if (isNullOrEmpty(txtGPrice1.getText()) || isNullOrEmpty(txtGQty1.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice1.getText()),
			Convert.strPercentToDbl(txtGQty1.getText())));

		if (isNullOrEmpty(txtGPrice2.getText()) || isNullOrEmpty(txtGQty2.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice2.getText()),
			Convert.strPercentToDbl(txtGQty2.getText())));

		if (isNullOrEmpty(txtGPrice3.getText()) || isNullOrEmpty(txtGQty3.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice3.getText()),
			Convert.strPercentToDbl(txtGQty3.getText())));
		
		if (isNullOrEmpty(txtGPrice4.getText()) || isNullOrEmpty(txtGQty4.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice4.getText()),
			Convert.strPercentToDbl(txtGQty4.getText())));

		if (isNullOrEmpty(txtGPrice5.getText()) || isNullOrEmpty(txtGQty5.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice5.getText()),
			Convert.strPercentToDbl(txtGQty5.getText())));

		if (isNullOrEmpty(txtGPrice6.getText()) || isNullOrEmpty(txtGQty6.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice6.getText()),
			Convert.strPercentToDbl(txtGQty6.getText())));

		if (isNullOrEmpty(txtGPrice7.getText()) || isNullOrEmpty(txtGQty7.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice7.getText()),
			Convert.strPercentToDbl(txtGQty7.getText())));

		if (isNullOrEmpty(txtGPrice8.getText()) || isNullOrEmpty(txtGQty8.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice8.getText()),
			Convert.strPercentToDbl(txtGQty8.getText())));
		
		return lstPriceQty;
	}

	private Position acquireArguments(PositionSide side) throws Exception
	{
		Double distBeforeSL = Convert.strPercentToDbl(txtSLossDist.getText());
		Double tProfit = Convert.strPercentToDbl(txtTProfit.getText());
		List<PriceQty> lstPriceQty = acquireGrid();

		Position position = new Position(coin, side, distBeforeSL, tProfit, lstPriceQty);

		Double inPrice = getInPrice();
		position.setInPrice(inPrice);
		position.setMarkPrice(rbPriceMark.isSelected());

		Double inQty = getInQty(inPrice);
		position.setInQty(inQty);

		return position;
	}

	private Double getInPrice() throws Exception
	{
		if (isOpenPosition)
		{
			double price = Double.valueOf(txtPositionPrice.getText());
			return price;
		}
		else if (rbPriceLimit.isSelected())
		{
			if (NumberUtils.isCreatable(txtPrice.getText()))
			{
				double price = Double.valueOf(txtPrice.getText());
				return price;
			}
		}
		else
		{
			BigDecimal lastPrice = PriceService.getLastPrice(coin);
			return lastPrice.doubleValue();
		}

		ERROR("Invalid price");
		return null;
	}

	private Double getInQty(double inPrice)
	{
		if (isOpenPosition)
		{
			double qty = Math.abs(Double.valueOf(txtPositionQty.getText()));
			return qty;
		}
		if (rbQty.isSelected())
		{
			if (NumberUtils.isCreatable(txtQty.getText()))
			{
				double qty = Double.valueOf(txtQty.getText());
				return qty;
			}
		}
		else
		{
			double balancePercent = Convert.strPercentToDbl(txtBalancePercent.getText());
			AccountBalance accBalance = BalanceService.getAccountBalance();
			double balance = accBalance.getBalance().doubleValue();
			return DoubleRounder.round(Math.max(5, balance * balancePercent) / inPrice, coin.getQuantityPrecision(), RoundingMode.CEILING);
		}

		ERROR("Invalid quantity");
		return null;
	}

	private static boolean isNullOrEmpty(String txt)
	{
		return (txt == null || txt.isEmpty());
	}

	// ------------------------------------------------------------------------

	private void createShort()
	{
		if (coin == null)
		{
			ERROR("Select symbol");
			return;
		}

		INFO("");
		try
		{
			Position position = acquireArguments(PositionSide.SHORT);
			pMaker = new PositionTrader(position);
			pMaker.createShort();

			//-----------------------------------------------------------------
			txtShowResult.setForeground(Styles.COLOR_TEXT_SHORT);
			txtShowResult.setText(position.toString());

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void createLong()
	{
		if (coin == null)
		{
			ERROR("Select symbol");
			return;
		}

		INFO("");
		try
		{
			Position position = acquireArguments(PositionSide.LONG);
			pMaker = new PositionTrader(position);
			pMaker.createLong();

			//-----------------------------------------------------------------
			txtShowResult.setForeground(Styles.COLOR_TEXT_LONG);
			txtShowResult.setText(position.toString());

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
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

	// ------------------------------------------------------------------------

	private void postOrders(PostStyle postStyle)
	{
		INFO("");
		try
		{
			if (JOptionPane.showConfirmDialog(null, "Do you like post this position ?") == 0)
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				String result = pMaker.post(postStyle);
				if (result != null)
				{
					ERROR(result);
				}

				txtShowResult.setForeground(pMaker.getPosition().getSide() == PositionSide.SHORT ? Styles.COLOR_TEXT_SHORT : Styles.COLOR_TEXT_LONG);
				txtShowResult.setText(pMaker.getPosition().toString());
				save(coin.getName() + "_" + pMaker.getPosition().getSide().name());

				// ------------------------------------------------------------
				btnPostFirst.setEnabled(false);
				btnPostOthers.setEnabled(postStyle.equals(PostStyle.FIRST));

				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
			File path = new File(Constants.DEFAULT_LOG_FOLDER);
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

	// ------------------------------------------------------------------------

	public static void createDirectory(File fileProcessPath) 
	{
		if (!fileProcessPath.exists()) 
		{
			fileProcessPath.mkdirs();
		}
	}

	// ------------------------------------------------------------------------

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
