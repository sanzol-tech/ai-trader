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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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

	private JButton btnBot;
	private JButton btnSignals;
	private JButton btnSignalsAlt;
	private JButton btnCoin;
	private JButton btnGrid;
	private JButton btnPositions;
	private JButton btnSaveConfig;
	private JButton btnSaveKey;
	private JButton btnShoot;
	private JButton btnSkin;

	private JCheckBox chkOnlyFavorites;

	private JLabel lblError;
	private JLabel lnkGitHub;

	private JList<String> listFavorites;
	private JList<String> listSignals;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;

	private JTextField txtBalance;
	private JTextField txtBalanceMinAvailable;
	private JTextField txtCoinsIncr;
	private JTextField txtDistBeforeSL;
	private JTextField txtFavCoins;
	private JTextField txtIterations;
	private JTextField txtLeverage;
	private JTextField txtPair;
	private JTextField txtPositionQty;
	private JTextField txtPositionQtyMax;
	private JTextField txtPositionsMax;
	private JTextField txtPriceIncr;
	private JTextField txtTProfit;
	private JTextField txtWithdrawal;

	public FrmMain()
	{
		initComponents();
		pageload();

		onPriceUpdate();
		PriceService.attachRefreshObserver(this);
		
		onSignalUpdate();
		SignalService.attachRefreshObserver(this);
		
		onBalanceUpdate();
		BalanceService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setTitle(Constants.APP_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 600);
		setLocationRelativeTo(null);
		setResizable(false);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		btnSignals = new JButton();
		btnSignals.setText("SIGNALS");
		btnSignalsAlt = new JButton();
		btnSignalsAlt.setText("LAB");
		btnBot = new JButton();
		btnBot.setText("BOT");
		btnCoin = new JButton();
		btnCoin.setText("COIN");
		btnGrid = new JButton();
		btnGrid.setText("GRID");
		btnPositions = new JButton();
		btnPositions.setText("POSITIONS");
		btnShoot = new JButton();
		btnShoot.setText("SHOOT");
		btnSkin = new JButton(Styles.IMAGE_MOON);
		btnSkin.setToolTipText("Skin mode");

		lnkGitHub = new JLabel();
		lnkGitHub.setText("ai-trader on GitHub");
		lnkGitHub.setForeground(Styles.COLOR_LINK);
		lnkGitHub.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lnkGitHub.setHorizontalAlignment(SwingConstants.CENTER);
		lnkGitHub.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JLabel lblPair = new JLabel();
		lblPair.setText("Pair");

		txtPair = new JTextField();
		txtPair.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPair.setForeground(Styles.COLOR_TEXT_ALT1);
		txtPair.setEditable(false);

		JLabel lblBalance = new JLabel();
		lblBalance.setText("Balance");

		txtBalance = new JTextField();
		txtBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalance.setForeground(Styles.COLOR_TEXT_ALT1);
		txtBalance.setEditable(false);

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();

		JScrollPane scrollFavorites = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFavorites.setBorder(UIManager.getBorder("TextField.border"));
		scrollFavorites.setBounds(15, 38, 235, 190);
		pnlContent.add(scrollFavorites);

		listFavorites = new JList<String>();
		listFavorites.setFont(new Font("Courier New", Font.PLAIN, 12));
		listFavorites.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listFavorites.setForeground(Styles.COLOR_TEXT_AREA_FG);
		scrollFavorites.setViewportView(listFavorites);
		
		JScrollPane scrollSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollSignals.setBorder(UIManager.getBorder("TextField.border"));
		scrollSignals.setBounds(273, 38, 550, 190);
		pnlContent.add(scrollSignals);

		listSignals = new JList<String>();
		listSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		listSignals.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listSignals.setForeground(Styles.COLOR_TEXT_AREA_FG);
		scrollSignals.setViewportView(listSignals);
		
		JLabel lblSignals = new JLabel("Short or Long Entries");
		lblSignals.setHorizontalAlignment(SwingConstants.LEFT);
		lblSignals.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSignals.setBounds(275, 12, 200, 20);
		pnlContent.add(lblSignals);
		
		JPanel panelConfig = new JPanel();
		panelConfig.setLayout(null);
		panelConfig.setBorder(new TitledBorder(null, " Default values ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelConfig.setBounds(15, 245, 810, 123);
		pnlContent.add(panelConfig);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		lblItarations.setBounds(20, 26, 80, 14);
		panelConfig.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		txtIterations.setBounds(20, 43, 72, 20);
		panelConfig.add(txtIterations);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		txtCoinsIncr.setBounds(20, 86, 72, 20);
		panelConfig.add(txtCoinsIncr);

		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(20, 70, 80, 14);
		panelConfig.add(lblCoinsIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		txtPriceIncr.setBounds(112, 43, 72, 20);
		panelConfig.add(txtPriceIncr);

		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(112, 26, 80, 14);
		panelConfig.add(lblPriceIncr);
		
		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		lblTProfit.setBounds(204, 26, 80, 14);
		panelConfig.add(lblTProfit);
		
		txtTProfit = new JTextField();
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		txtTProfit.setBounds(204, 43, 72, 20);
		panelConfig.add(txtTProfit);
		
		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		txtDistBeforeSL.setBounds(112, 86, 72, 20);
		panelConfig.add(txtDistBeforeSL);
		
		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(112, 70, 90, 14);
		panelConfig.add(lblDistSL);
		
		JLabel lblQty = new JLabel("Min qty %");
		lblQty.setBounds(422, 26, 80, 14);
		panelConfig.add(lblQty);
		
		txtPositionQty = new JTextField();
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(422, 43, 72, 20);
		panelConfig.add(txtPositionQty);
		
		txtFavCoins = new JTextField();
		txtFavCoins.setColumns(10);
		txtFavCoins.setBounds(640, 43, 154, 20);
		panelConfig.add(txtFavCoins);

		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		lblFavCoins.setBounds(640, 26, 90, 14);
		panelConfig.add(lblFavCoins);
		
		txtLeverage = new JTextField();
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setEditable(false);
		txtLeverage.setColumns(10);
		txtLeverage.setBounds(514, 43, 72, 20);
		panelConfig.add(txtLeverage);
		
		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setColumns(10);
		txtBalanceMinAvailable.setBounds(330, 86, 72, 20);
		panelConfig.add(txtBalanceMinAvailable);
		
		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setBounds(514, 26, 80, 14);
		panelConfig.add(lblLeverage);
		
		JLabel lblAvailable = new JLabel("Min balance %");
		lblAvailable.setBounds(330, 70, 80, 14);
		panelConfig.add(lblAvailable);
		
		btnSaveConfig = new JButton("SAVE");
		btnSaveConfig.setOpaque(true);
		btnSaveConfig.setBounds(722, 86, 72, 20);
		panelConfig.add(btnSaveConfig);
		
		JLabel lblPositionsMax = new JLabel("Max positions");
		lblPositionsMax.setBounds(330, 26, 80, 14);
		panelConfig.add(lblPositionsMax);
		
		JLabel lblQtyMax = new JLabel("Max qty %");
		lblQtyMax.setBounds(422, 70, 80, 14);
		panelConfig.add(lblQtyMax);
		
		txtPositionsMax = new JTextField();
		txtPositionsMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionsMax.setColumns(10);
		txtPositionsMax.setBounds(330, 43, 72, 20);
		panelConfig.add(txtPositionsMax);

		txtPositionQtyMax = new JTextField();
		txtPositionQtyMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQtyMax.setColumns(10);
		txtPositionQtyMax.setBounds(422, 86, 72, 20);
		panelConfig.add(txtPositionQtyMax);

		chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setSelected(true);
		chkOnlyFavorites.setHorizontalAlignment(SwingConstants.TRAILING);
		chkOnlyFavorites.setBounds(130, 12, 120, 23);
		pnlContent.add(chkOnlyFavorites);

		JPanel panelKey = new JPanel();
		panelKey.setLayout(null);
		panelKey.setBorder(UIManager.getBorder("TextField.border"));
		panelKey.setBounds(15, 385, 810, 60);
		pnlContent.add(panelKey);

		JLabel lblApiKey = new JLabel("Api Key");
		lblApiKey.setBounds(12, 11, 80, 14);
		panelKey.add(lblApiKey);

		txtApiKey = new JPasswordField();
		txtApiKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtApiKey.setColumns(10);
		txtApiKey.setBounds(12, 30, 340, 20);
		panelKey.add(txtApiKey);

		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(358, 11, 80, 14);
		panelKey.add(lblSecretKey);

		txtSecretKey = new JPasswordField();
		txtSecretKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtSecretKey.setColumns(10);
		txtSecretKey.setBounds(358, 30, 340, 20);
		panelKey.add(txtSecretKey);

		btnSaveKey = new JButton("SAVE");
		btnSaveKey.setOpaque(true);
		btnSaveKey.setBounds(728, 30, 72, 20);
		panelKey.add(btnSaveKey);
		
		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnCoin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGrid)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnShoot)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnPositions)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSignals)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBot)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSignalsAlt)
					.addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
					.addComponent(lnkGitHub, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnSkin)
					.addContainerGap())
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnCoin))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnGrid))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnShoot))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnPositions))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnSignals))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnBot)
								.addComponent(btnSignalsAlt)))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnSkin))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(lnkGitHub)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlTopBar.setLayout(pnlTopBarLayout);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlTopBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);
		
		JLabel lblSymbols = new JLabel("Symbols");
		lblSymbols.setHorizontalAlignment(SwingConstants.LEFT);
		lblSymbols.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSymbols.setBounds(17, 12, 100, 20);
		pnlContent.add(lblSymbols);
		
		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(lblPair)
					.addGap(10)
					.addComponent(txtPair, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblBalance)
					.addGap(18)
					.addComponent(txtBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblWithdrawal)
					.addGap(10)
					.addComponent(txtWithdrawal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGroup(pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(16)
							.addComponent(lblPair))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(txtPair, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(16)
							.addComponent(lblBalance))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(txtBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(16)
							.addComponent(lblWithdrawal))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(13)
							.addComponent(txtWithdrawal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(pnlStatusBarLayout.createSequentialGroup()
							.addGap(16)
							.addComponent(lblError)))
					.addContainerGap(13, Short.MAX_VALUE))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

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

		btnSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookFeel(!Config.isDarkMode());
			}
		});
					
		btnSignals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSignals();
			}
		});

		btnSignalsAlt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSignalsAlt();
			}
		});
		
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBot();
			}
		});
	
		btnPositions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPositions();
			}
		});
		
		btnShoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalcOrder();
			}
		});

		btnGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmGrid.launch();
			}
		});

		btnCoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCoin();
			}
		});

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
			
			btnSkin.setIcon(Config.isDarkMode() ? Styles.IMAGE_SUN : Styles.IMAGE_MOON);

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
		txtFavCoins.setText(Config.getFavoriteSymbols());

		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPriceIncrement())); 
		txtCoinsIncr.setText(Convert.dblToStrPercent(Config.getCoinsIncrement()));
		txtDistBeforeSL.setText(Convert.dblToStrPercent(Config.getStoplossIncrement()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));

		txtPositionsMax.setText(String.valueOf(Config.getPositionsMax()));
		txtPositionQty.setText(Convert.dblToStrPercent(Config.getPositionStartQty()));
		txtPositionQtyMax.setText(Convert.dblToStrPercent(Config.getPositionStartQtyMax()));
		txtBalanceMinAvailable.setText(Convert.dblToStrPercent(Config.getBalanceMinAvailable()));
		txtLeverage.setText(String.valueOf(Config.getLeverage()));

		txtPair.setText(Constants.DEFAULT_SYMBOL_RIGHT);
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
		FrmShoot.launch();
	}

	private void showSignals()
	{
		FrmSignals.launch();
	}

	private void showSignalsAlt()
	{
		FrmSignalsAlt.launch();
	}

	private void showBot()
	{
		FrmBot.launch();
	}

	private void setLookFeel(boolean isDarkMode)
	{
		try
		{
			Config.setDarkMode(isDarkMode);
			Config.save();
			Styles.applyStyle();

			dispose();
			FrmMain.launch();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
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
			listFavorites.setModel(toListModel(PriceService.getSymbols(chkOnlyFavorites.isSelected())));

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
		boolean isBotMode = false;
		
		SignalEntry entry = lstShockStatus.get(index);

		if (entry.getAction().startsWith("SHORT"))
		{
			Double price = Math.max(entry.getPrice().doubleValue(), entry.getShShock().doubleValue());
			FrmGrid.launch(entry.getCoin().getNameLeft(), "SHORT", entry.getCoin().priceToStr(price), isBotMode);
		}
		else
		{
			Double price = Math.min(entry.getPrice().doubleValue(), entry.getLgShock().doubleValue());
			FrmGrid.launch(entry.getCoin().getNameLeft(), "LONG", entry.getCoin().priceToStr(price), isBotMode);
		}
	}

	// ------------------------------------------------------------------------

	private void saveConfig()
	{
		try
		{
			Config.setFavoriteSymbols(txtFavCoins.getText());

			Config.setIterations(txtIterations.getText());
			Config.setPriceIncrement(Convert.strPercentToDbl(txtPriceIncr.getText()));
			Config.setCoinsIncrement(Convert.strPercentToDbl(txtCoinsIncr.getText()));
			Config.setStoplossIncrement(Convert.strPercentToDbl(txtDistBeforeSL.getText()));
			Config.setTakeprofit(Convert.strPercentToDbl(txtTProfit.getText()));
			Config.setPositionsMax(txtPositionsMax.getText());
			Config.setPositionStartQty(Convert.strPercentToDbl(txtPositionQty.getText()));
			Config.setPositionStartQtyMax(Convert.strPercentToDbl(txtPositionQtyMax.getText()));
			Config.setBalanceMinAvailable(Convert.strPercentToDbl(txtBalanceMinAvailable.getText()));
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
