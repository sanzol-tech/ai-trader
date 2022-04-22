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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Constants;
import sanzol.app.model.Symbol;
import sanzol.app.service.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.util.Convert;

public class FrmHammerCalc extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Hammer Calc";

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

	private JRadioButton rbShort;
	private JRadioButton rbLong;
	private JLabel lblArrow;


	public FrmHammerCalc()
	{
		initComponents();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 460, 380);
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

		JButton btnSearch = new JButton(CharConstants.ARROW_MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBackground(new Color(220, 220, 220));
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

		JButton btnCalc = new JButton("Calc");
		btnCalc.setBounds(252, 272, 86, 23);
		btnCalc.setOpaque(true);
		btnCalc.setBackground(new Color(220, 220, 220));
		contentPane.add(btnCalc);

		JButton btnExec = new JButton("Exec");
		btnExec.setBounds(348, 272, 86, 23);
		btnExec.setOpaque(true);
		btnExec.setBackground(new Color(220, 220, 220));
		contentPane.add(btnExec);

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
		txtUsdResult.setHorizontalAlignment(SwingConstants.RIGHT);
		txtUsdResult.setEditable(false);
		txtUsdResult.setColumns(10);
		txtUsdResult.setBounds(299, 185, 86, 20);
		contentPane.add(txtUsdResult);

		txtError = new JTextField();
		txtError.setEditable(false);
		txtError.setColumns(10);
		txtError.setBounds(10, 310, 424, 20);
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

		btnCalc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calc();
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
			setTitle(TITLE + " - " + symbol);
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));

			BigDecimal price = PriceService.getLastPrice(coin);
			txtMarkPrice.setText(coin.priceToStr(price));
			txtPriceShoot.setText(coin.priceToStr(price));

			// --------------------------------------------------------------
			PositionRisk positionRisk = PositionService.getPosition(symbol);
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

	private void calc()
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
			double priceNew = Double.valueOf(txtPriceShoot.getText());
			double amtNew = Double.valueOf(txtAmtShoot.getText());
			double usdNew = priceNew * amtNew;
			txtUsdShoot.setText(Convert.usdToStr(usdNew));

			// ----------------------------------------------------------
			double amtResult = amtPos + amtNew;
			double usdResult = usdPos + usdNew;
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

		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void exec()
	{
		INFO ("Not implemented yet");
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
					FrmHammerCalc frame = new FrmHammerCalc();
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
		txtError.setForeground(new Color(255, 0, 0));
		txtError.setText(" " + msg);
	}

	public void INFO(String msg)
	{
		txtError.setForeground(new Color(51, 107, 255));
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
