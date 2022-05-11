package sanzol.app.forms;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.binance.client.model.trade.AccountBalance;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.config.Styles;
import sanzol.app.listener.BalanceListener;
import sanzol.app.listener.PriceListener;
import sanzol.app.listener.SignalListener;
import sanzol.app.model.SignalEntry;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;
import sanzol.app.util.Convert;
import sanzol.lib.util.ExceptionUtils;

public class FrmMain extends JFrame implements PriceListener, SignalListener, BalanceListener
{
	private static final long serialVersionUID = 1L;

	private List<SignalEntry> lstShockStatus = null;

	private JList<String> listFavorites;
	private JList<String> listSignals;

	private JPanel contentPane;

	private JButton btnLookFeel;
	private JButton btnPositions;
	private JButton btnCalcOrder;
	private JButton btnNewGrid;
	private JButton btnShockMonitor;
	private JButton btnSaveConfig;
	private JButton btnSaveKey;
	private JButton btnShockEditor;
	private JButton btnCoin;
	private JButton btnBot;

	private JTextField txtIterations;
	private JTextField txtPriceIncr;
	private JTextField txtTProfit;
	private JTextField txtPositionQty;
	private JTextField txtDistBeforeSL;
	private JTextField txtCoinsIncr;
	private JTextField txtBalance;
	private JTextField txtWithdrawal;
	private JTextField txtFavCoins;
	private JTextField txtError;
	private JTextField txtLeverage;
	private JTextField txtBalanceMinAvailable;
	private JTextField txtPositionsMax;
	private JTextField txtPositionQtyMax;
	
	private JLabel lnkGitHub;

	private JPasswordField txtSecretKey;
	private JPasswordField txtApiKey;

	public FrmMain()
	{
		initComponents();
		pageload();
		PriceService.attachRefreshObserver(this);
		SignalService.attachRefreshObserver(this);
		BalanceService.attachRefreshObserver(this);
	}

	private void initComponents() 
	{
		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 610);
		setTitle(Constants.APP_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/logo.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		btnCoin = new JButton("COIN");
		btnCoin.setOpaque(true);
		btnCoin.setBounds(16, 11, 92, 28);
		contentPane.add(btnCoin);

		btnNewGrid = new JButton("GRID");
		btnNewGrid.setBounds(118, 11, 92, 28);
		btnNewGrid.setOpaque(true);
		contentPane.add(btnNewGrid);

		btnCalcOrder = new JButton("SHOOT");
		btnCalcOrder.setOpaque(true);
		btnCalcOrder.setBounds(220, 11, 92, 28);
		contentPane.add(btnCalcOrder);

		btnShockMonitor = new JButton("MONITOR ALL");
		btnShockMonitor.setOpaque(true);
		btnShockMonitor.setBounds(556, 66, 130, 28);
		contentPane.add(btnShockMonitor);

		btnShockEditor = new JButton("EDIT POINTS");
		btnShockEditor.setOpaque(true);
		btnShockEditor.setBounds(696, 66, 130, 28);
		contentPane.add(btnShockEditor);

		btnBot = new JButton("BOT");
		btnBot.setOpaque(true);
		btnBot.setBounds(442, 11, 92, 28);
		contentPane.add(btnBot);

		btnLookFeel = new JButton("Light");
		btnCoin.setOpaque(true);
		btnLookFeel.setBounds(590, 15, 68, 22);
		contentPane.add(btnLookFeel);

		lnkGitHub = new JLabel("ai-trader on GitHub");
		lnkGitHub.setForeground(Styles.COLOR_LINK);
		lnkGitHub.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lnkGitHub.setHorizontalAlignment(SwingConstants.RIGHT);
		lnkGitHub.setBounds(669, 15, 157, 22);
		lnkGitHub.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkGitHub);

		JPanel panelConfig = new JPanel();
		panelConfig.setBorder(new TitledBorder(null, " Default values ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelConfig.setBounds(16, 300, 810, 123);
		panelConfig.setLayout(null);
		contentPane.add(panelConfig);

		JPanel panelKey = new JPanel();
		panelKey.setBorder(UIManager.getBorder("TextField.border"));

		panelKey.setBounds(16, 440, 810, 60);
		panelKey.setLayout(null);
		contentPane.add(panelKey);

		JLabel lblApiKey = new JLabel("Api Key");
		lblApiKey.setBounds(12, 11, 80, 14);
		panelKey.add(lblApiKey);

		txtApiKey = new JPasswordField();
		txtApiKey.setBounds(12, 30, 340, 20);
		txtApiKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtApiKey.setColumns(10);
		panelKey.add(txtApiKey);

		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(358, 11, 80, 14);
		panelKey.add(lblSecretKey);

		btnPositions = new JButton("POSITIONS");
		btnPositions.setToolTipText("Edit shock points");
		btnPositions.setOpaque(true);
		btnPositions.setBounds(322, 11, 110, 28);
		contentPane.add(btnPositions);

		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(358, 30, 340, 20);
		txtSecretKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtSecretKey.setColumns(10);
		panelKey.add(txtSecretKey);

		btnSaveKey = new JButton("SAVE");
		btnSaveKey.setBounds(728, 30, 72, 20);
		btnSaveKey.setOpaque(true);
		panelKey.add(btnSaveKey);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(20, 26, 80, 14);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfig.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setBounds(20, 43, 72, 20);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		panelConfig.add(txtIterations);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setBounds(20, 86, 72, 20);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		panelConfig.add(txtCoinsIncr);

		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(20, 70, 80, 14);
		panelConfig.add(lblCoinsIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(112, 43, 72, 20);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		panelConfig.add(txtPriceIncr);

		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(112, 26, 80, 14);
		panelConfig.add(lblPriceIncr);

		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setBounds(204, 26, 80, 14);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		panelConfig.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setBounds(204, 43, 72, 20);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		panelConfig.add(txtTProfit);

		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setBounds(112, 86, 72, 20);
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		panelConfig.add(txtDistBeforeSL);

		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(112, 70, 90, 14);
		panelConfig.add(lblDistSL);

		JLabel lblQty = new JLabel("Min qty %");
		lblQty.setBounds(422, 26, 80, 14);
		panelConfig.add(lblQty);

		txtPositionQty = new JTextField();
		txtPositionQty.setBounds(422, 43, 72, 20);
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		panelConfig.add(txtPositionQty);

		txtFavCoins = new JTextField();
		txtFavCoins.setBounds(640, 43, 154, 20);
		txtFavCoins.setColumns(10);
		panelConfig.add(txtFavCoins);

		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		lblFavCoins.setBounds(640, 26, 90, 14);
		panelConfig.add(lblFavCoins);

		txtLeverage = new JTextField();
		txtLeverage.setEditable(false);
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setBounds(514, 43, 72, 20);
		txtLeverage.setColumns(10);
		panelConfig.add(txtLeverage);

		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setBounds(330, 86, 72, 20);
		txtBalanceMinAvailable.setColumns(10);
		panelConfig.add(txtBalanceMinAvailable);

		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setBounds(514, 26, 80, 14);
		panelConfig.add(lblLeverage);

		JLabel lblAvailable = new JLabel("Min balance %");
		lblAvailable.setBounds(330, 70, 80, 14);
		panelConfig.add(lblAvailable);

		btnSaveConfig = new JButton("SAVE");
		btnSaveConfig.setBounds(722, 86, 72, 20);
		panelConfig.add(btnSaveConfig);
		btnSaveConfig.setOpaque(true);
		
		JLabel lblPositionsMax = new JLabel("Max positions");
		lblPositionsMax.setBounds(330, 26, 80, 14);
		panelConfig.add(lblPositionsMax);
		
		JLabel lblQtyMax = new JLabel("Max qty %");
		lblQtyMax.setBounds(422, 70, 80, 14);
		panelConfig.add(lblQtyMax);
		
		txtPositionsMax = new JTextField();
		txtPositionsMax.setText("3.0");
		txtPositionsMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionsMax.setColumns(10);
		txtPositionsMax.setBounds(330, 43, 72, 20);
		panelConfig.add(txtPositionsMax);
		
		txtPositionQtyMax = new JTextField();
		txtPositionQtyMax.setText("10");
		txtPositionQtyMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQtyMax.setColumns(10);
		txtPositionQtyMax.setBounds(422, 86, 72, 20);
		panelConfig.add(txtPositionQtyMax);

		JScrollPane scrollFavorites = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFavorites.setBounds(16, 67, 235, 203);
		scrollFavorites.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollFavorites);

		listFavorites = new JList<String>();
		listFavorites.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listFavorites.setForeground(Styles.COLOR_TEXT_AREA_FG);
		listFavorites.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollFavorites.setViewportView(listFavorites);

		JScrollPane scrollSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollSignals.setBounds(276, 104, 550, 180);
		scrollSignals.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scrollSignals);

		listSignals = new JList<String>();
		listSignals.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listSignals.setForeground(Styles.COLOR_TEXT_AREA_FG);
		listSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		scrollSignals.setViewportView(listSignals);

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

		txtError = new JTextField();
		txtError.setEditable(false);
		txtError.setBounds(16, 517, 600, 40);
		contentPane.add(txtError);
		
		JLabel lblSignals = new JLabel("Short or Long Entries");
		lblSignals.setHorizontalAlignment(SwingConstants.LEFT);
		lblSignals.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSignals.setBounds(276, 76, 200, 20);
		contentPane.add(lblSignals);

		// --------------------------------------------------------------------

		FrmMain thisFrm = this;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PriceService.deattachRefreshObserver(thisFrm);
				SignalService.deattachRefreshObserver(thisFrm);
				BalanceService.deattachRefreshObserver(thisFrm);
			}
		});
		
		lnkGitHub.addMouseListener(new MouseAdapter() {
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
					FrmCoin.launch(symbolLeft);
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

		btnLookFeel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookFeel(!Styles.isNight);
			}
		});
		
		btnPositions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPositions();
			}
		});

		btnCoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCoin();
			}
		});
		
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBot();
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
				FrmGrid.launch();
			}
		});

		btnShockMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmSignalsMonitor.launch();
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

	private void setLookFeel(boolean isNight)
	{
		try
		{
			if (isNight)
			{
				Styles.setNight();
			} else {
				Styles.setLight();
			}
			dispose();
			FrmMain.launch();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void pageload()
	{
		try
		{
			ERROR(Application.getError());
			
			btnLookFeel.setText(Styles.isNight ? "Light" : "Night");

			txtApiKey.setText(PrivateConfig.API_KEY);
			txtSecretKey.setText(PrivateConfig.SECRET_KEY);

			loadConfig();
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void loadConfig()
	{
		txtFavCoins.setText(Config.getFavorite_symbols());

		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPrice_increment())); 
		txtCoinsIncr.setText(Convert.dblToStrPercent(Config.getCoins_increment()));
		txtDistBeforeSL.setText(Convert.dblToStrPercent(Config.getStoploss_increment()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));

		txtPositionsMax.setText(String.valueOf(Config.getPositions_max()));
		txtPositionQty.setText(Convert.dblToStrPercent(Config.getPosition_start_qty()));
		txtPositionQtyMax.setText(Convert.dblToStrPercent(Config.getPosition_start_qty_max()));
		txtBalanceMinAvailable.setText(Convert.dblToStrPercent(Config.getBalance_min_available()));
		txtLeverage.setText(String.valueOf(Config.getLeverage()));
	}

	private void showPositions()
	{
		FrmPositions.launch();
	}

	private void showCoin()
	{
		FrmCoin.launch();
	}

	private void showCalcOrder()
	{
		FrmAddOrder.launch();
	}

	private void showBot()
	{
		FrmBot.launch();
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

	// ------------------------------------------------------------------------

	@Override
	public void onBalanceUpdate()
	{
		try
		{
			AccountBalance balance = BalanceService.getAccountBalance();
			if (balance != null)
			{
				txtBalance.setText(Convert.usdToStr(balance.getBalance().doubleValue()));
				txtWithdrawal.setText(Convert.usdToStr(balance.getWithdrawAvailable().doubleValue()));
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	@Override
	public void onSignalUpdate()
	{
		try
		{
			loadListSignals();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	@Override
	public void onPriceUpdate()
	{
		try
		{
			listFavorites.setModel(toListModel(PriceService.getSymbols(true)));

			setTitle(Constants.APP_NAME + " - "+ PriceService.btcLabel());
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
			FrmGrid.launch(entry.getCoin().getNameLeft(), "SHORT", entry.getCoin().priceToStr(price));
		}
		else
		{
			Double price = Math.min(entry.getPrice().doubleValue(), entry.getLgShock().doubleValue());
			FrmGrid.launch(entry.getCoin().getNameLeft(), "LONG", entry.getCoin().priceToStr(price));
		}
	}

	// ------------------------------------------------------------------------

	private void saveConfig()
	{
		try
		{
			Config.setFavorite_symbols(txtFavCoins.getText());

			Config.setIterations(txtIterations.getText());
			Config.setPrice_increment(Convert.strPercentToDbl(txtPriceIncr.getText()));
			Config.setCoins_increment(Convert.strPercentToDbl(txtCoinsIncr.getText()));
			Config.setStoploss_increment(Convert.strPercentToDbl(txtDistBeforeSL.getText()));
			Config.setTakeprofit(Convert.strPercentToDbl(txtTProfit.getText()));
			Config.setPositions_max(txtPositionsMax.getText());
			Config.setPosition_start_qty(Convert.strPercentToDbl(txtPositionQty.getText()));
			Config.setPosition_start_qty_max(Convert.strPercentToDbl(txtPositionQtyMax.getText()));
			Config.setBalance_min_available(Convert.strPercentToDbl(txtBalanceMinAvailable.getText()));
			Config.setLeverage(txtLeverage.getText());

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
		ERROR(ExceptionUtils.getMessage(e));
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
