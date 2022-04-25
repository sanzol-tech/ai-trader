package sanzol.app.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.decimal4j.util.DoubleRounder;

import com.binance.client.model.trade.AccountBalance;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.config.Styles;
import sanzol.app.model.SignalEntry;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;
import sanzol.app.util.Convert;

public class FrmMain extends JFrame
{
	private static final long serialVersionUID = 1L;

	private List<SignalEntry> lstShockStatus = null;

	private JPanel contentPane;

	private JButton btnPositions;
	private JButton btnCalcOrder;
	private JButton btnNewGrid;
	private JButton btnShockMonitor;
	private JButton btnSaveConfig;
	private JButton btnSaveKey;
	private JButton btnShockEditor;

	private JList<String> listFavorites;
	private JList<String> listSignals;

	private JTextField txtIterations;
	private JTextField txtPriceIncr;
	private JTextField txtTProfit;
	private JTextField txtBalanceStartPosition;
	private JTextField txtDistBeforeSL;
	private JTextField txtCoinsIncr;
	private JTextField txtBalance;
	private JTextField txtWithdrawal;
	private JTextField txtFavCoins;
	private JTextField txtError;

	private JPasswordField txtSecretKey;
	private JPasswordField txtApiKey;
	private JTextField txtLeverage;
	private JTextField txtBalanceMinAvailable;

	public FrmMain()
	{
		initComponents();

		try
		{
			pageload();
			startTimer();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void initComponents() 
	{
		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 610);
		setTitle(Constants.APP_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/logo.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel panelConfig = new JPanel();
		panelConfig.setBorder(new LineBorder(Styles.COLOR_BORDER_LINE, 1, true));
		panelConfig.setBounds(16, 307, 810, 114);
		panelConfig.setLayout(null);
		contentPane.add(panelConfig);

		JPanel panelKey = new JPanel();
		panelKey.setBorder(new LineBorder(Styles.COLOR_BORDER_LINE, 1, true));
		panelKey.setBounds(16, 440, 810, 60);
		panelKey.setLayout(null);
		contentPane.add(panelKey);

		JLabel lblNewLabel_2 = new JLabel("Api Key");
		lblNewLabel_2.setBounds(12, 11, 80, 14);
		panelKey.add(lblNewLabel_2);

		txtApiKey = new JPasswordField();
		txtApiKey.setBounds(12, 30, 340, 20);
		txtApiKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtApiKey.setColumns(10);
		panelKey.add(txtApiKey);

		JLabel lblNewLabel_3 = new JLabel("Secret Key");
		lblNewLabel_3.setBounds(358, 11, 80, 14);
		panelKey.add(lblNewLabel_3);

		btnPositions = new JButton("POSITIONS");
		btnPositions.setToolTipText("Edit shock points");
		btnPositions.setOpaque(true);
		btnPositions.setBackground(Styles.COLOR_BTN);
		btnPositions.setBounds(294, 11, 120, 28);
		contentPane.add(btnPositions);

		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(358, 30, 340, 20);
		txtSecretKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtSecretKey.setColumns(10);
		panelKey.add(txtSecretKey);

		btnSaveKey = new JButton("SAVE");
		btnSaveKey.setBounds(726, 30, 72, 20);
		panelKey.add(btnSaveKey);
		btnSaveKey.setOpaque(true);
		btnSaveKey.setBackground(Styles.COLOR_BTN);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(10, 59, 90, 14);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfig.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setBounds(110, 55, 60, 20);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		panelConfig.add(txtIterations);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setBounds(462, 55, 60, 20);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		panelConfig.add(txtCoinsIncr);

		JLabel lblCoinsIncr = new JLabel("Coins Incr");
		lblCoinsIncr.setBounds(374, 59, 80, 14);
		lblCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		panelConfig.add(lblCoinsIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(286, 55, 60, 20);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		panelConfig.add(txtPriceIncr);

		JLabel lblPriceIncr = new JLabel("Price Incr");
		lblPriceIncr.setBounds(198, 59, 80, 14);
		lblPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		panelConfig.add(lblPriceIncr);

		JLabel lblTProfit = new JLabel("Take profit");
		lblTProfit.setBounds(10, 83, 90, 14);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfig.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setBounds(110, 81, 60, 20);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		panelConfig.add(txtTProfit);

		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setBounds(638, 55, 60, 20);
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		panelConfig.add(txtDistBeforeSL);

		JLabel lblDistSL = new JLabel("Last to SL");
		lblDistSL.setBounds(550, 59, 80, 14);
		lblDistSL.setHorizontalAlignment(SwingConstants.RIGHT);
		panelConfig.add(lblDistSL);

		JLabel lblPriceIncr_1 = new JLabel("Balance");
		lblPriceIncr_1.setBounds(198, 83, 80, 14);
		lblPriceIncr_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panelConfig.add(lblPriceIncr_1);

		txtBalanceStartPosition = new JTextField();
		txtBalanceStartPosition.setBounds(286, 81, 60, 20);
		txtBalanceStartPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceStartPosition.setColumns(10);
		panelConfig.add(txtBalanceStartPosition);

		txtFavCoins = new JTextField();
		txtFavCoins.setBounds(110, 11, 588, 20);
		txtFavCoins.setColumns(10);
		panelConfig.add(txtFavCoins);

		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		lblFavCoins.setBounds(10, 14, 90, 14);
		panelConfig.add(lblFavCoins);

		txtLeverage = new JTextField();
		txtLeverage.setEditable(false);
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setBounds(638, 81, 60, 20);
		panelConfig.add(txtLeverage);
		txtLeverage.setColumns(10);

		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setBounds(462, 81, 60, 20);
		panelConfig.add(txtBalanceMinAvailable);
		txtBalanceMinAvailable.setColumns(10);

		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLeverage.setBounds(550, 83, 80, 14);
		panelConfig.add(lblLeverage);

		JLabel lblAvailable = new JLabel("Min available");
		lblAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAvailable.setBounds(374, 83, 80, 14);
		panelConfig.add(lblAvailable);

		btnSaveConfig = new JButton("SAVE");
		btnSaveConfig.setBounds(726, 81, 72, 20);
		panelConfig.add(btnSaveConfig);
		btnSaveConfig.setOpaque(true);
		btnSaveConfig.setBackground(Styles.COLOR_BTN);

		JLabel lblPercetSymbol = new JLabel("%");
		lblPercetSymbol.setBounds(173, 83, 25, 14);
		panelConfig.add(lblPercetSymbol);

		JLabel lblPercetSymbol_1 = new JLabel("%");
		lblPercetSymbol_1.setBounds(349, 59, 25, 14);
		panelConfig.add(lblPercetSymbol_1);

		JLabel lblPercetSymbol_2 = new JLabel("%");
		lblPercetSymbol_2.setBounds(349, 83, 25, 14);
		panelConfig.add(lblPercetSymbol_2);

		JLabel lblPercetSymbol_3 = new JLabel("%");
		lblPercetSymbol_3.setBounds(525, 59, 25, 14);
		panelConfig.add(lblPercetSymbol_3);

		JLabel lblPercetSymbol_4 = new JLabel("%");
		lblPercetSymbol_4.setBounds(525, 83, 25, 14);
		panelConfig.add(lblPercetSymbol_4);

		JLabel lblPercetSymbol_5 = new JLabel("%");
		lblPercetSymbol_5.setBounds(701, 59, 25, 14);
		panelConfig.add(lblPercetSymbol_5);

		JScrollPane scrollFavorites = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFavorites.setBounds(16, 67, 140, 190);
		contentPane.add(scrollFavorites);

		listFavorites = new JList<String>();
		listFavorites.setBackground(Styles.COLOR_TEXT_AREA);
		listFavorites.setFont(new Font("Courier New", Font.PLAIN, 12));
		listFavorites.setForeground(Styles.COLOR_TEXT_ALT2);
		scrollFavorites.setViewportView(listFavorites);

		JScrollPane scrollSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollSignals.setBounds(183, 104, 643, 180);
		contentPane.add(scrollSignals);

		listSignals = new JList<String>();
		listSignals.setBackground(Styles.COLOR_TEXT_AREA);
		listSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		listSignals.setForeground(Styles.COLOR_TEXT_ALT2);
		scrollSignals.setViewportView(listSignals);

		btnShockMonitor = new JButton("MONITOR ALL");
		btnShockMonitor.setToolTipText("Monitor shock points");
		btnShockMonitor.setOpaque(true);
		btnShockMonitor.setBackground(Styles.COLOR_BTN);
		btnShockMonitor.setBounds(556, 66, 130, 28);
		contentPane.add(btnShockMonitor);

		txtBalance = new JTextField();
		txtBalance.setBounds(640, 537, 86, 20);
		txtBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalance.setForeground(Styles.COLOR_TEXT_ALT1);
		txtBalance.setEditable(false);
		txtBalance.setColumns(10);
		contentPane.add(txtBalance);

		JLabel lblBalanceL = new JLabel("Balance");
		lblBalanceL.setBounds(640, 518, 86, 14);
		lblBalanceL.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblBalanceL);

		txtWithdrawal = new JTextField();
		txtWithdrawal.setBounds(740, 537, 86, 20);
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);
		txtWithdrawal.setColumns(10);
		contentPane.add(txtWithdrawal);

		JLabel lblWithdrawalL = new JLabel("Withdrawal");
		lblWithdrawalL.setBounds(743, 518, 83, 14);
		lblWithdrawalL.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(lblWithdrawalL);

		btnNewGrid = new JButton("NEW GRID");
		btnNewGrid.setBounds(16, 11, 120, 28);
		btnNewGrid.setOpaque(true);
		btnNewGrid.setBackground(Styles.COLOR_BTN_ALT1);
		contentPane.add(btnNewGrid);

		JLabel lblConfig = new JLabel("Default values");
		lblConfig.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConfig.setBounds(16, 281, 140, 20);
		contentPane.add(lblConfig);

		txtError = new JTextField();
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setBounds(16, 517, 600, 40);
		contentPane.add(txtError);

		btnShockEditor = new JButton("EDIT POINTS");
		btnShockEditor.setToolTipText("Edit shock points");
		btnShockEditor.setOpaque(true);
		btnShockEditor.setBackground(Styles.COLOR_BTN);
		btnShockEditor.setBounds(696, 66, 130, 28);
		contentPane.add(btnShockEditor);

		JLabel lblSignals = new JLabel("Short or Long Entries");
		lblSignals.setHorizontalAlignment(SwingConstants.LEFT);
		lblSignals.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSignals.setBounds(184, 76, 200, 20);
		contentPane.add(lblSignals);

		btnCalcOrder = new JButton("ADD ORDER");
		btnCalcOrder.setOpaque(true);
		btnCalcOrder.setBackground(Styles.COLOR_BTN_ALT1);
		btnCalcOrder.setBounds(146, 11, 120, 28);
		contentPane.add(btnCalcOrder);
		
		JLabel lblTitle = new JLabel("ai-trader on GitHub");
		lblTitle.setForeground(new Color(0, 0, 139));
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTitle.setBounds(616, 15, 210, 14);
		lblTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lblTitle);

		lblTitle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/sanzol-tech/ai-trader"));
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		});
		
		listFavorites.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				if (e.getClickCount() == 2)
				{
					int index = list.locationToIndex(e.getPoint());
					String item = (String) list.getModel().getElementAt(index);
					String symbolLeft = item.substring(0, item.indexOf(" "));
					FrmTrader.launch(symbolLeft, null, null);
				}

			}
		});

		listSignals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				if (e.getClickCount() == 2)
				{
					int index = list.locationToIndex(e.getPoint());
					tradeFromSignal(index);
				}
			}
		});

		btnPositions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPositions();
			}
		});

		btnCalcOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalcOrder();
			}
		});

		btnShockEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editShockPoints();
			}
		});

		btnNewGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmTrader.launch();
			}
		});

		btnShockMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmPointsMonitor.launch();
			}
		});

		btnSaveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfig();
			}
		});

		btnSaveKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveKey();
			}
		});

	}

	private void pageload()
	{
		try
		{
			ERROR(Application.getError());

			txtApiKey.setText(PrivateConfig.API_KEY);
			txtSecretKey.setText(PrivateConfig.SECRET_KEY);

			loadConfig();
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void loadConfig() throws JsonSyntaxException, JsonIOException, FileNotFoundException
	{
		//listFavorites.setModel(toListModel(Config.getLstFavSymbols()));
		txtFavCoins.setText(Config.getFavorite_symbols());

		txtLeverage.setText(String.valueOf(Config.getLeverage()));
		txtIterations.setText(String.valueOf(Config.getIterations()));

		txtPriceIncr.setText(dblToStrPercent(Config.getPrice_increment())); 
		txtCoinsIncr.setText(dblToStrPercent(Config.getCoins_increment()));
		txtDistBeforeSL.setText(dblToStrPercent(Config.getStoploss_increment()));
		txtTProfit.setText(dblToStrPercent(Config.getTakeprofit()));

		txtBalanceStartPosition.setText(dblToStrPercent(Config.getBalance_start_position()));
		txtBalanceMinAvailable.setText(dblToStrPercent(Config.getBalance_min_available()));
	}

	private void showPositions()
	{
		FrmPositions.launch();
	}

	private void showCalcOrder()
	{
		FrmAddOrder.launch();
	}

	private void editShockPoints()
	{
		FrmPointsEditor.launch();
	}

	public static void launch()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				FrmMain frame = null;

				try
				{
					frame = new FrmMain();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
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
		Timer timer1 = new Timer(3000, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void refresh()
	{
		try
		{
			AccountBalance balance = BalanceService.getAccountBalance();
			if (balance != null)
			{
				txtBalance.setText(Convert.usdToStr(balance.getBalance().doubleValue()));
				txtWithdrawal.setText(Convert.usdToStr(balance.getWithdrawAvailable().doubleValue()));
			}

			// ----------------------------------------------------------------

			loadListSignals();
			
			// ----------------------------------------------------------------
			listFavorites.setModel(toListModel(PriceService.getSymbols(true)));			
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

	private void loadListSignals()
	{
		lstShockStatus = SignalService.getShockStatus();

		AbstractListModel<String> listModel = new AbstractListModel<String>()
		{
			private static final long serialVersionUID = 1L;

			public int getSize()
			{
				return lstShockStatus.size();
			}

			public String getElementAt(int index)
			{
				return lstShockStatus.get(index).toStringSmart();
			}
		};

		listSignals.setModel(listModel);
	}

	private void tradeFromSignal(int index)
	{
		SignalEntry entry = lstShockStatus.get(index);

		if (entry.getAction().startsWith("SHORT"))
		{
			Double price = Math.max(entry.getPrice().doubleValue(), entry.getShShock().doubleValue());
			FrmTrader.launch(entry.getCoin().getNameLeft(), "SHORT", entry.getCoin().priceToStr(price));
		}
		else
		{
			Double price = Math.min(entry.getPrice().doubleValue(), entry.getLgShock().doubleValue());
			FrmTrader.launch(entry.getCoin().getNameLeft(), "LONG", entry.getCoin().priceToStr(price));
		}
	}

	// ------------------------------------------------------------------------

	private void saveConfig()
	{
		try
		{
			Config.setFavorite_symbols(txtFavCoins.getText());

			Config.setLeverage(txtLeverage.getText());
			Config.setIterations(txtIterations.getText());

			Config.setPrice_increment(strPercentToDbl(txtPriceIncr.getText()));
			Config.setCoins_increment(strPercentToDbl(txtCoinsIncr.getText()));
			Config.setStoploss_increment(strPercentToDbl(txtDistBeforeSL.getText()));
			Config.setTakeprofit(strPercentToDbl(txtTProfit.getText()));

			Config.setBalance_start_position(strPercentToDbl(txtBalanceStartPosition.getText()));
			Config.setBalance_min_available(strPercentToDbl(txtBalanceMinAvailable.getText()));

			Config.save();
			INFO("CONFIG SAVED");
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	@SuppressWarnings("deprecation")
	private void saveKey()
	{
		try
		{
			PrivateConfig.setKey(txtApiKey.getText(), txtSecretKey.getText());
			INFO("KEY SAVED");
		}
		catch (IOException e)
		{
			ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

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


	private static AbstractListModel<String> toListModel(List<String> values)
	{
		AbstractListModel<String> listModel = new AbstractListModel<String>()
		{
			private static final long serialVersionUID = 1L;

			public int getSize()
			{
				return values.size();
			}

			public String getElementAt(int index)
			{
				return values.get(index);
			}
		};

		return listModel;
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
