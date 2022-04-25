package sanzol.app.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

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

public class FrmCalcOrder extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Calc order";

	private static boolean isOpen = false;

	private Symbol coin;

	private JPanel contentPane;
	private JTextField txtPriceShoot;
	private JTextField txtAmtShoot;
	private JTextField txtPricePosition;
	private JTextField txtSymbolLeft;
	private JTextField txtAmtPosition;
	private JTextField txtSymbolRight;
	private JTextField txtPriceResult;
	private JTextField txtAmtResult;
	private JTextField txtUsdShoot;
	private JTextField txtUsdPosition;
	private JTextField txtUsdResult;
	private JTextField txtError;
	private JTextField txtPnlOld;
	private JTextField txtPnlNew;
	private JTextField txtMarkPrice;

	private JButton btnCalc05;
	private JButton btnCalc075;
	private JButton btnX1;
	private JButton btnX2;
	private JButton btnCalc;
	private JButton btnExec;
	
	private JRadioButton rbShort;
	private JRadioButton rbLong;
	private JLabel lblArrow;
	private JTextField txtSLPrice;
	private JTextField txtSLCoins;
	private JLabel lblSlUsdt;
	private JTextField txtSLUsd;
	private JLabel lblSLUsd;


	public FrmCalcOrder()
	{
		setResizable(false);
		initComponents();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 470);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/hammer.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		txtPriceShoot = new JTextField();
		txtPriceShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceShoot.setBounds(107, 154, 86, 20);
		contentPane.add(txtPriceShoot);
		txtPriceShoot.setColumns(10);

		txtAmtShoot = new JTextField();
		txtAmtShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAmtShoot.setBounds(203, 154, 86, 20);
		contentPane.add(txtAmtShoot);
		txtAmtShoot.setColumns(10);

		txtPricePosition = new JTextField();
		txtPricePosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPricePosition.setBounds(107, 123, 86, 20);
		contentPane.add(txtPricePosition);
		txtPricePosition.setColumns(10);

		JButton btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBackground(Styles.COLOR_BTN);
		btnSearch.setBounds(299, 24, 86, 22);
		contentPane.add(btnSearch);

		JLabel lblNewLabel = new JLabel("Price");
		lblNewLabel.setBounds(107, 99, 86, 14);
		contentPane.add(lblNewLabel);

		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setBounds(203, 99, 86, 14);
		contentPane.add(lblAmount);

		JLabel lblShoot = new JLabel("Shoot");
		lblShoot.setBounds(30, 157, 67, 14);
		contentPane.add(lblShoot);

		JLabel lblPosition = new JLabel("Position");
		lblPosition.setBounds(30, 126, 67, 14);
		contentPane.add(lblPosition);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(107, 25, 86, 20);
		contentPane.add(txtSymbolLeft);

		txtAmtPosition = new JTextField();
		txtAmtPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAmtPosition.setColumns(10);
		txtAmtPosition.setBounds(203, 123, 86, 20);
		contentPane.add(txtAmtPosition);

		JLabel lblNewLabel_2_1 = new JLabel("COIN");
		lblNewLabel_2_1.setBounds(30, 28, 67, 14);
		contentPane.add(lblNewLabel_2_1);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(203, 25, 86, 20);
		contentPane.add(txtSymbolRight);

		JLabel lblResult = new JLabel("Result");
		lblResult.setBounds(30, 188, 67, 14);
		contentPane.add(lblResult);

		txtPriceResult = new JTextField();
		txtPriceResult.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPriceResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceResult.setEditable(false);
		txtPriceResult.setColumns(10);
		txtPriceResult.setBounds(107, 185, 86, 20);
		contentPane.add(txtPriceResult);

		txtAmtResult = new JTextField();
		txtAmtResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtAmtResult.setEditable(false);
		txtAmtResult.setColumns(10);
		txtAmtResult.setBounds(203, 185, 86, 20);
		contentPane.add(txtAmtResult);

		txtUsdShoot = new JTextField();
		txtUsdShoot.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtUsdShoot.setForeground(Color.RED);
		txtUsdShoot.setEditable(false);
		txtUsdShoot.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUsdShoot.setColumns(10);
		txtUsdShoot.setBounds(299, 154, 86, 20);
		contentPane.add(txtUsdShoot);

		JLabel lblUsd = new JLabel("USD");
		lblUsd.setBounds(299, 99, 86, 14);
		contentPane.add(lblUsd);

		txtUsdPosition = new JTextField();
		txtUsdPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUsdPosition.setEditable(false);
		txtUsdPosition.setColumns(10);
		txtUsdPosition.setBounds(299, 123, 86, 20);
		contentPane.add(txtUsdPosition);

		txtUsdResult = new JTextField();
		txtUsdResult.setForeground(Color.RED);
		txtUsdResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUsdResult.setEditable(false);
		txtUsdResult.setColumns(10);
		txtUsdResult.setBounds(299, 185, 86, 20);
		contentPane.add(txtUsdResult);

		txtError = new JTextField();
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setColumns(10);
		txtError.setBounds(10, 400, 464, 20);
		contentPane.add(txtError);

		txtPnlOld = new JTextField();
		txtPnlOld.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPnlOld.setEditable(false);
		txtPnlOld.setColumns(10);
		txtPnlOld.setBounds(134, 227, 86, 20);
		contentPane.add(txtPnlOld);

		txtPnlNew = new JTextField();
		txtPnlNew.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtPnlNew.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPnlNew.setEditable(false);
		txtPnlNew.setColumns(10);
		txtPnlNew.setBounds(275, 227, 86, 20);
		contentPane.add(txtPnlNew);

		JLabel lblPNL = new JLabel("PNL");
		lblPNL.setBounds(86, 230, 38, 14);
		contentPane.add(lblPNL);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(107, 56, 86, 20);
		contentPane.add(txtMarkPrice);

		JLabel lblMarkPrice = new JLabel("Mrk.Price");
		lblMarkPrice.setBounds(30, 59, 67, 14);
		contentPane.add(lblMarkPrice);

		rbShort = new JRadioButton("SHORT");
		rbShort.setBounds(203, 52, 86, 23);
		contentPane.add(rbShort);

		rbLong = new JRadioButton("LONG");
		rbLong.setSelected(true);
		rbLong.setBounds(299, 53, 86, 23);
		contentPane.add(rbLong);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbShort);
		bg1.add(rbLong);

		lblArrow = new JLabel("-->");
		lblArrow.setFont(new Font("Courier New", Font.PLAIN, 11));
		lblArrow.setHorizontalAlignment(SwingConstants.CENTER);
		lblArrow.setBounds(223, 230, 48, 14);
		contentPane.add(lblArrow);
		
		btnCalc05 = new JButton("x 0.5");
		btnCalc05.setOpaque(true);
		btnCalc05.setBackground(Styles.COLOR_BTN);
		btnCalc05.setBounds(10, 280, 70, 23);
		contentPane.add(btnCalc05);

		btnCalc075 = new JButton("x 0.75");
		btnCalc075.setOpaque(true);
		btnCalc075.setBackground(Styles.COLOR_BTN);
		btnCalc075.setBounds(90, 280, 70, 23);
		contentPane.add(btnCalc075);
		
		btnX1 = new JButton("x 1");
		btnX1.setOpaque(true);
		btnX1.setBackground(Styles.COLOR_BTN);
		btnX1.setBounds(170, 280, 67, 23);
		contentPane.add(btnX1);

		btnX2 = new JButton("x 2");
		btnX2.setOpaque(true);
		btnX2.setBackground(Styles.COLOR_BTN);
		btnX2.setBounds(250, 280, 70, 23);
		contentPane.add(btnX2);

		btnCalc = new JButton("CALC");
		btnCalc.setBounds(330, 280, 144, 23);
		btnCalc.setOpaque(true);
		btnCalc.setBackground(Styles.COLOR_BTN);
		contentPane.add(btnCalc);

		btnExec = new JButton("POST ORDER");
		btnExec.setBounds(327, 334, 147, 40);
		btnExec.setOpaque(true);
		btnExec.setBackground(Styles.COLOR_BTN);
		contentPane.add(btnExec);
		
		txtSLPrice = new JTextField();
		txtSLPrice.setEditable(false);
		txtSLPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLPrice.setColumns(10);
		txtSLPrice.setBounds(10, 353, 86, 20);
		contentPane.add(txtSLPrice);
		
		JLabel lblStopLoss = new JLabel("SL - Price");
		lblStopLoss.setForeground(new Color(165, 42, 42));
		lblStopLoss.setHorizontalAlignment(SwingConstants.LEFT);
		lblStopLoss.setBounds(10, 332, 86, 14);
		contentPane.add(lblStopLoss);
		
		txtSLCoins = new JTextField();
		txtSLCoins.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLCoins.setEditable(false);
		txtSLCoins.setColumns(10);
		txtSLCoins.setBounds(107, 353, 86, 20);
		contentPane.add(txtSLCoins);
		
		lblSlUsdt = new JLabel("SL - Amount");
		lblSlUsdt.setForeground(new Color(165, 42, 42));
		lblSlUsdt.setHorizontalAlignment(SwingConstants.LEFT);
		lblSlUsdt.setBounds(107, 332, 86, 14);
		contentPane.add(lblSlUsdt);
		
		txtSLUsd = new JTextField();
		txtSLUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLUsd.setEditable(false);
		txtSLUsd.setColumns(10);
		txtSLUsd.setBounds(203, 353, 86, 20);
		contentPane.add(txtSLUsd);
		
		lblSLUsd = new JLabel("SL - USDT");
		lblSLUsd.setForeground(new Color(165, 42, 42));
		lblSLUsd.setHorizontalAlignment(SwingConstants.LEFT);
		lblSLUsd.setBounds(203, 332, 86, 14);
		contentPane.add(lblSLUsd);
		

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

		btnExec.addActionListener(new ActionListener() {
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

			BigDecimal mrkPrice = PriceService.getLastPrice(coin);
			txtMarkPrice.setText(coin.priceToStr(mrkPrice));
			txtPriceShoot.setText(coin.priceToStr(mrkPrice));

			// --------------------------------------------------------------
			PositionRisk positionRisk = PositionService.getPositionRisk(coin.getName());

			txtPricePosition.setText(coin.priceToStr(positionRisk.getEntryPrice()));

			double amt = Math.abs(positionRisk.getPositionAmt().doubleValue());
			txtAmtPosition.setText(coin.coinsToStr(amt));
			txtAmtShoot.setText(coin.coinsToStr(amt));

			rbShort.setSelected(positionRisk.getPositionAmt().doubleValue() < 0);
			rbLong.setSelected(positionRisk.getPositionAmt().doubleValue() > 0);
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
			double price = PriceService.getLastPrice(coin).doubleValue();
 
			// ----------------------------------------------------------
			double pricePos = Double.valueOf(txtPricePosition.getText());
			double amtPos = Double.valueOf(txtAmtPosition.getText());
			double usdPos = pricePos * amtPos;
			txtUsdPosition.setText(Convert.usdToStr(usdPos));

			// ----------------------------------------------------------
			if (x != null)
			{
				txtAmtShoot.setText(coin.coinsToStr(amtPos * x));
			}
			// ----------------------------------------------------------

			double priceShoot = Double.valueOf(txtPriceShoot.getText());
			double amtShoot = Double.valueOf(txtAmtShoot.getText());
			double usdShoot = priceShoot * amtShoot;
			txtUsdShoot.setText(Convert.usdToStr(usdShoot));

			// ----------------------------------------------------------
			double amtResult = amtPos + amtShoot;
			double usdResult = usdPos + usdShoot;
			double priceResult = usdResult / amtResult;
			txtPriceResult.setText(coin.priceToStr(priceResult));
			txtAmtResult.setText(coin.coinsToStr(amtResult));
			txtUsdResult.setText(Convert.usdToStr(usdResult));

			// ----------------------------------------------------------
			if (rbLong.isSelected())
			{
				double pnlOld = 1 - (pricePos / price);
				double pnlNew = 1 - (priceResult / price);
				txtPnlOld.setText(Convert.dblToStrPercent(pnlOld));
				txtPnlNew.setText(Convert.dblToStrPercent(pnlNew));
			}
			else
			{
				double pnlOld = (pricePos / price) - 1;
				double pnlNew = (priceResult / price) - 1;
				txtPnlOld.setText(Convert.dblToStrPercent(pnlOld));
				txtPnlNew.setText(Convert.dblToStrPercent(pnlNew));
			}

			// ----------------------------------------------------------
			GOrder gOrder = PositionService.recalcSL(coin.getName(), BigDecimal.valueOf(priceShoot), BigDecimal.valueOf(amtShoot));
			txtSLPrice.setText(coin.priceToStr(gOrder.getPrice()));
			txtSLCoins.setText(coin.coinsToStr(gOrder.getCoins()));
			txtSLUsd.setText(Convert.usdToStr(gOrder.getUsd()));

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
			BigDecimal coins = new BigDecimal(txtAmtShoot.getText());

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
					FrmCalcOrder frame = new FrmCalcOrder();
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
