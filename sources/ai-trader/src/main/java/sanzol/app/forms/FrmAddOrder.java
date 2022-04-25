package sanzol.app.forms;

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
import javax.swing.border.EmptyBorder;

import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.GOrder;
import sanzol.app.model.Symbol;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.trader.SimpleTrader;
import sanzol.app.trader.SimpleTrader.Side;
import sanzol.app.util.Convert;

public class FrmAddOrder extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Add order";

	private static boolean isOpen = false;

	private Symbol coin;

	private JPanel contentPane;
	private JTextField txtPriceShoot;
	private JTextField txtQtyShoot;
	private JTextField txtPricePosition;
	private JTextField txtSymbolLeft;
	private JTextField txtQtyPosition;
	private JTextField txtSymbolRight;
	private JTextField txtPriceResult;
	private JTextField txtQtyResult;
	private JTextField txtUsdShoot;
	private JTextField txtError;
	private JTextField txtDistPos;
	private JTextField txtDistResult;
	private JTextField txtMarkPrice;

	private JButton btnCalc05;
	private JButton btnCalc075;
	private JButton btnX1;
	private JButton btnX2;
	private JButton btnCalc;
	private JButton btnPost;
	
	private JRadioButton rbShort;
	private JRadioButton rbLong;
	private JTextField txtOthersPrice;
	private JTextField txtOthersCoins;
	private JTextField txtOthersUsd;
	private JTextField txtSLUsd;
	private JTextField txtSLCoins;
	private JTextField txtSLPrice;
	private JTextField txtPnlSL;
	private JTextField txt24h;
	private JLabel lbl24Hs;
	private JLabel lblTprofit;
	private JTextField txtTPPrice;
	private JTextField txtTPCoins;
	private JTextField txtTPUsd;
	private JTextField textField;
	private JTextField textField_1;


	public FrmAddOrder()
	{
		setResizable(false);
		initComponents();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 528, 560);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/hammer.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		txtPriceShoot = new JTextField();
		txtPriceShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceShoot.setBounds(108, 175, 86, 20);
		contentPane.add(txtPriceShoot);
		txtPriceShoot.setColumns(10);

		txtQtyShoot = new JTextField();
		txtQtyShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQtyShoot.setBounds(204, 175, 86, 20);
		contentPane.add(txtQtyShoot);
		txtQtyShoot.setColumns(10);

		txtPricePosition = new JTextField();
		txtPricePosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPricePosition.setBounds(108, 145, 86, 20);
		contentPane.add(txtPricePosition);
		txtPricePosition.setColumns(10);

		JButton btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBackground(Styles.COLOR_BTN);
		btnSearch.setBounds(31, 69, 178, 22);
		contentPane.add(btnSearch);

		JLabel lblNewLabel = new JLabel("Price");
		lblNewLabel.setBounds(108, 122, 86, 14);
		contentPane.add(lblNewLabel);

		JLabel lblQuantity = new JLabel("Quantity");
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

		txtQtyPosition = new JTextField();
		txtQtyPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQtyPosition.setColumns(10);
		txtQtyPosition.setBounds(204, 145, 86, 20);
		contentPane.add(txtQtyPosition);

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

		txtPriceResult = new JTextField();
		txtPriceResult.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPriceResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceResult.setEditable(false);
		txtPriceResult.setColumns(10);
		txtPriceResult.setBounds(108, 205, 86, 20);
		contentPane.add(txtPriceResult);

		txtQtyResult = new JTextField();
		txtQtyResult.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtQtyResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQtyResult.setEditable(false);
		txtQtyResult.setColumns(10);
		txtQtyResult.setBounds(204, 205, 86, 20);
		contentPane.add(txtQtyResult);

		txtUsdShoot = new JTextField();
		txtUsdShoot.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtUsdShoot.setEditable(false);
		txtUsdShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUsdShoot.setColumns(10);
		txtUsdShoot.setBounds(300, 175, 86, 20);
		contentPane.add(txtUsdShoot);

		JLabel lblUsd = new JLabel("USD");
		lblUsd.setBounds(300, 122, 86, 14);
		contentPane.add(lblUsd);

		txtError = new JTextField();
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setColumns(10);
		txtError.setBounds(31, 487, 458, 20);
		contentPane.add(txtError);

		txtDistPos = new JTextField();
		txtDistPos.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistPos.setEditable(false);
		txtDistPos.setColumns(10);
		txtDistPos.setBounds(403, 145, 86, 20);
		contentPane.add(txtDistPos);

		txtDistResult = new JTextField();
		txtDistResult.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtDistResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistResult.setEditable(false);
		txtDistResult.setColumns(10);
		txtDistResult.setBounds(403, 205, 86, 20);
		contentPane.add(txtDistResult);

		JLabel lblDistPercent = new JLabel("Dist %");
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
		btnCalc05.setBackground(Styles.COLOR_BTN);
		btnCalc05.setBounds(31, 369, 70, 23);
		contentPane.add(btnCalc05);

		btnCalc075 = new JButton("x 0.75");
		btnCalc075.setOpaque(true);
		btnCalc075.setBackground(Styles.COLOR_BTN);
		btnCalc075.setBounds(108, 369, 70, 23);
		contentPane.add(btnCalc075);
		
		btnX1 = new JButton("x 1");
		btnX1.setOpaque(true);
		btnX1.setBackground(Styles.COLOR_BTN);
		btnX1.setBounds(31, 403, 70, 23);
		contentPane.add(btnX1);

		btnX2 = new JButton("x 2");
		btnX2.setOpaque(true);
		btnX2.setBackground(Styles.COLOR_BTN);
		btnX2.setBounds(108, 403, 70, 23);
		contentPane.add(btnX2);

		btnCalc = new JButton("CALC");
		btnCalc.setBounds(31, 436, 147, 23);
		btnCalc.setOpaque(true);
		btnCalc.setBackground(Styles.COLOR_BTN);
		contentPane.add(btnCalc);

		btnPost = new JButton("POST ORDER");
		btnPost.setBounds(327, 409, 162, 40);
		btnPost.setOpaque(true);
		btnPost.setBackground(Styles.COLOR_BTN);
		contentPane.add(btnPost);
		
		txtOthersPrice = new JTextField();
		txtOthersPrice.setEditable(false);
		txtOthersPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersPrice.setColumns(10);
		txtOthersPrice.setBounds(108, 245, 86, 20);
		contentPane.add(txtOthersPrice);
		
		txtOthersCoins = new JTextField();
		txtOthersCoins.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersCoins.setEditable(false);
		txtOthersCoins.setColumns(10);
		txtOthersCoins.setBounds(205, 245, 86, 20);
		contentPane.add(txtOthersCoins);
		
		txtOthersUsd = new JTextField();
		txtOthersUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtOthersUsd.setEditable(false);
		txtOthersUsd.setColumns(10);
		txtOthersUsd.setBounds(300, 245, 86, 20);
		contentPane.add(txtOthersUsd);
		
		txtSLUsd = new JTextField();
		txtSLUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLUsd.setEditable(false);
		txtSLUsd.setColumns(10);
		txtSLUsd.setBounds(300, 285, 86, 20);
		contentPane.add(txtSLUsd);
		
		txtSLCoins = new JTextField();
		txtSLCoins.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLCoins.setEditable(false);
		txtSLCoins.setColumns(10);
		txtSLCoins.setBounds(205, 285, 86, 20);
		contentPane.add(txtSLCoins);
		
		txtSLPrice = new JTextField();
		txtSLPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLPrice.setColumns(10);
		txtSLPrice.setBounds(108, 285, 86, 20);
		contentPane.add(txtSLPrice);
		
		JLabel lblOthers = new JLabel("OTHERS");
		lblOthers.setBounds(31, 249, 67, 14);
		contentPane.add(lblOthers);
		
		JLabel lblStopLss = new JLabel("S-MARKET");
		lblStopLss.setBounds(31, 289, 67, 14);
		contentPane.add(lblStopLss);
		
		txtPnlSL = new JTextField();
		txtPnlSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPnlSL.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPnlSL.setEditable(false);
		txtPnlSL.setColumns(10);
		txtPnlSL.setBounds(403, 285, 86, 20);
		contentPane.add(txtPnlSL);
		
		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(403, 43, 86, 20);
		contentPane.add(txt24h);
		
		lbl24Hs = new JLabel("24h %");
		lbl24Hs.setBounds(404, 24, 85, 14);
		contentPane.add(lbl24Hs);
		
		lblTprofit = new JLabel("T. PROFIT");
		lblTprofit.setBounds(31, 319, 67, 14);
		contentPane.add(lblTprofit);
		
		txtTPPrice = new JTextField();
		txtTPPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPPrice.setColumns(10);
		txtTPPrice.setBounds(108, 315, 86, 20);
		contentPane.add(txtTPPrice);
		
		txtTPCoins = new JTextField();
		txtTPCoins.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPCoins.setEditable(false);
		txtTPCoins.setColumns(10);
		txtTPCoins.setBounds(205, 315, 86, 20);
		contentPane.add(txtTPCoins);
		
		txtTPUsd = new JTextField();
		txtTPUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTPUsd.setEditable(false);
		txtTPUsd.setColumns(10);
		txtTPUsd.setBounds(300, 315, 86, 20);
		contentPane.add(txtTPUsd);
		
		textField = new JTextField();
		textField.setText("---");
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(300, 145, 86, 20);
		contentPane.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setText("---");
		textField_1.setHorizontalAlignment(SwingConstants.RIGHT);
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(300, 206, 86, 20);
		contentPane.add(textField_1);
		

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


		btnCalc05.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(0.5);
			}
		});
		btnCalc075.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(0.75);
			}
		});
		btnX1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(1.0);
			}
		});
		btnX2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc(2.0);
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
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				txtMarkPrice.setText(coin.priceToStr(mrkPrice));
				String priceChangePercent = String.format("%.2f%%", PriceService.PriceChangePercent(coin));
				txt24h.setText(priceChangePercent);
				txtPriceShoot.setText(coin.priceToStr(mrkPrice));
	
				PositionRisk positionRisk = PositionService.getPositionRisk(coin.getName());
				if (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0)
				{
					txtPricePosition.setText(coin.priceToStr(positionRisk.getEntryPrice()));
		
					double amt = Math.abs(positionRisk.getPositionAmt().doubleValue());
					txtQtyPosition.setText(coin.coinsToStr(amt));
					txtQtyShoot.setText(coin.coinsToStr(amt));
		
					if (positionRisk.getPositionAmt().doubleValue() < 0)
					{
						BigDecimal posDist = positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP).subtract(BigDecimal.ONE);
						txtDistPos.setText(Convert.dblToStrPercent(posDist));
		
						rbShort.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_SHORT);
					}
					else
					{
						BigDecimal posDist = BigDecimal.ONE.subtract(positionRisk.getEntryPrice().divide(mrkPrice, RoundingMode.HALF_UP));
						txtDistPos.setText(Convert.dblToStrPercent(posDist));
		
						rbLong.setSelected(true);
						btnPost.setBackground(Styles.COLOR_BTN_LONG);
					}
				}
			}
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void calc(Double x)
	{
		INFO("");
		try
		{
			String side = rbLong.isSelected() ? "BUY" : "SELL";
			BigDecimal posPrice = new BigDecimal(txtPricePosition.getText());  
			BigDecimal posQty = new BigDecimal(txtQtyPosition.getText());
			BigDecimal shootPrice = new BigDecimal(txtPriceShoot.getText());
			BigDecimal shootQty = new BigDecimal(txtQtyShoot.getText());
			Map<String, GOrder> mapPosition = PositionService.calc(coin, side, posPrice, posQty, shootPrice, shootQty);

			txtDistPos.setText(Convert.dblToStrPercent(mapPosition.get("POS").getDist()));

			txtPriceResult.setText(coin.priceToStr(mapPosition.get("RESULT").getPrice()));
			txtQtyResult.setText(coin.coinsToStr(mapPosition.get("RESULT").getCoins()));
			txtDistResult.setText(Convert.dblToStrPercent(mapPosition.get("RESULT").getDist()));

			txtSLPrice.setText(coin.priceToStr(mapPosition.get("SL").getPrice()));
			txtSLCoins.setText(coin.coinsToStr(mapPosition.get("SL").getCoins()));

			txtTPPrice.setText(coin.priceToStr(mapPosition.get("TP").getPrice()));
			txtTPCoins.setText(coin.coinsToStr(mapPosition.get("TP").getCoins()));

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
			Side side = rbLong.isSelected() ? Side.LONG : Side.SHORT;
			BigDecimal price = new BigDecimal(txtPriceShoot.getText());
			BigDecimal coins = new BigDecimal(txtQtyShoot.getText());

			String msg = String.format("Post order %s  /  %s  /  %s  /  %s ? *The price can be better than the selected one", coin.getName(), side.name(), coin.priceToStr(price), coin.coinsToStr(coins));			

			if (JOptionPane.showConfirmDialog(null, msg) == 0)
			{
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				if ((side == Side.SHORT && mrkPrice.doubleValue() > price.doubleValue()) ||
					(side == Side.LONG && mrkPrice.doubleValue() < price.doubleValue()))
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
