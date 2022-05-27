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
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.binance.client.model.event.SymbolTickerEvent;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.listener.BalanceListener;
import sanzol.app.listener.BtcChangeListener;
import sanzol.app.listener.PositionListener;
import sanzol.app.listener.PriceListener;
import sanzol.app.listener.SignalListener;
import sanzol.app.model.SignalEntry;
import sanzol.app.service.Symbol;
import sanzol.app.task.BalanceService;
import sanzol.app.task.BtcChangeService;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;
import sanzol.app.util.Convert;
import sanzol.lib.util.ExceptionUtils;

public class FrmMain extends JFrame implements PriceListener, SignalListener, BalanceListener, BtcChangeListener, PositionListener
{
	private static final long serialVersionUID = 1L;

	private List<SignalEntry> lstShockStatus = null;

	private JButton btnBot;
	private JButton btnSignals;
	private JButton btnSymbols;
	private JButton btnCoin;
	private JButton btnGrid;
	private JButton btnPositions;
	private JButton btnShoot;
	private JButton btnConfig;
	private JButton btnSkin;
	private JButton btnLog;

	private JCheckBox chkOnlyFavorites;
	private JCheckBox chkOnlyBetters;

	private JLabel lblError;
	private JLabel lnkGitHub;

	private JList<String> listFavorites;
	private JList<String> listSignals;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JTextField txtBalance;
	private JTextField txtPair;
	private JTextField txtWithdrawal;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblChangem;
	private JLabel lblChangeh;
	private JLabel lblPrice;
	private JLabel txtChange30m;
	private JLabel txtChange24h;
	private JLabel txtBtcPrice;
	private JPanel pnlPositions;
	private JLabel txtShorts;
	private JLabel lblShorts;
	private JLabel lblLongs;
	private JLabel txtLongs;

	public FrmMain()
	{
		initComponents();
		pageload();

		onPriceUpdate();
		PriceService.attachRefreshObserver(this);

		onBtcChangeUpdate();
		BtcChangeService.attachRefreshObserver(this);
		
		onSignalUpdate();
		SignalService.attachRefreshObserver(this);
		
		onBalanceUpdate();
		BalanceService.attachRefreshObserver(this);
		
		PositionService.attachRefreshObserver(this);
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
		btnBot = new JButton();
		btnBot.setText("BOT");
		btnCoin = new JButton();
		btnCoin.setText("COIN");
		btnGrid = new JButton();
		btnGrid.setText("GRID");
		btnShoot = new JButton();
		btnShoot.setText("SHOOT");
		btnConfig = new JButton(Styles.IMAGE_WRENCH);
		btnConfig.setToolTipText("Config");
		btnSkin = new JButton(Styles.IMAGE_MOON);
		btnSkin.setToolTipText("Skin mode");
		btnLog = new JButton(Styles.IMAGE_BUG);
		btnLog.setToolTipText("View log messages");

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
		scrollFavorites.setBounds(15, 42, 299, 307);
		pnlContent.add(scrollFavorites);

		listFavorites = new JList<String>();
		listFavorites.setFont(new Font("Courier New", Font.PLAIN, 12));
		listFavorites.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listFavorites.setForeground(Styles.COLOR_TEXT_AREA_FG);
		scrollFavorites.setViewportView(listFavorites);
		
		JScrollPane scrollSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollSignals.setBorder(UIManager.getBorder("TextField.border"));
		scrollSignals.setBounds(340, 42, 483, 307);
		pnlContent.add(scrollSignals);

		listSignals = new JList<String>();
		listSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		listSignals.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listSignals.setForeground(Styles.COLOR_TEXT_AREA_FG);
		scrollSignals.setViewportView(listSignals);
		
		JLabel lblSignals = new JLabel("Short or Long Entries");
		lblSignals.setHorizontalAlignment(SwingConstants.LEFT);
		lblSignals.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSignals.setBounds(342, 15, 200, 20);
		pnlContent.add(lblSignals);

		chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setSelected(true);
		chkOnlyFavorites.setHorizontalAlignment(SwingConstants.LEADING);
		chkOnlyFavorites.setBounds(15, 15, 99, 23);
		pnlContent.add(chkOnlyFavorites);

		chkOnlyBetters = new JCheckBox("Only betters");
		chkOnlyBetters.setSelected(true);
		chkOnlyBetters.setBounds(116, 15, 99, 23);
		pnlContent.add(chkOnlyBetters);
		
		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnCoin)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnGrid)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnShoot)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnBot)
					.addPreferredGap(ComponentPlacement.RELATED, 271, Short.MAX_VALUE)
					.addComponent(lnkGitHub, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnConfig)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSkin)
					.addContainerGap())
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCoin)
								.addComponent(btnGrid)
								.addComponent(btnShoot)
								.addComponent(btnBot)))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnConfig))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnLog))
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
		
		panel = new JPanel();
		panel.setBorder(UIManager.getBorder("TextField.border"));
		panel.setBounds(15, 370, 459, 70);
		pnlContent.add(panel);
		panel.setLayout(null);
		
		lblNewLabel = new JLabel("BTC");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNewLabel.setBounds(10, 24, 66, 14);
		panel.add(lblNewLabel);
		
		lblChangem = new JLabel("CHANGE 30m");
		lblChangem.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChangem.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblChangem.setBounds(181, 11, 124, 14);
		panel.add(lblChangem);
		
		lblChangeh = new JLabel("CHANGE 24h");
		lblChangeh.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChangeh.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblChangeh.setBounds(315, 11, 122, 14);
		panel.add(lblChangeh);
		
		lblPrice = new JLabel("PRICE");
		lblPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPrice.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPrice.setBounds(84, 11, 87, 14);
		panel.add(lblPrice);
		
		txtChange30m = new JLabel("---");
		txtChange30m.setForeground(Styles.COLOR_TEXT_ALT1);
		txtChange30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChange30m.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtChange30m.setBounds(181, 36, 124, 14);
		panel.add(txtChange30m);
		
		txtChange24h = new JLabel("---");
		txtChange24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txtChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChange24h.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtChange24h.setBounds(315, 36, 122, 14);
		panel.add(txtChange24h);
		
		txtBtcPrice = new JLabel("---");
		txtBtcPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtBtcPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBtcPrice.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtBtcPrice.setBounds(84, 36, 87, 14);
		panel.add(txtBtcPrice);
		
		pnlPositions = new JPanel();
		pnlPositions.setBorder(UIManager.getBorder("TextField.border"));
		pnlPositions.setBounds(497, 370, 326, 70);
		pnlPositions.setLayout(null);
		pnlContent.add(pnlPositions);

		lblShorts = new JLabel("SHORTS");
		lblShorts.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblShorts.setBounds(24, 16, 86, 14);
		pnlPositions.add(lblShorts);
		
		lblLongs = new JLabel("LONGS");
		lblLongs.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblLongs.setBounds(120, 16, 86, 14);
		pnlPositions.add(lblLongs);

		txtShorts = new JLabel("--");
		txtShorts.setForeground(Styles.COLOR_TEXT_ALT1);
		txtShorts.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtShorts.setBounds(24, 41, 86, 14);
		pnlPositions.add(txtShorts);
		
		txtLongs = new JLabel("--");
		txtLongs.setForeground(Styles.COLOR_TEXT_ALT1);
		txtLongs.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtLongs.setBounds(120, 41, 86, 14);
		pnlPositions.add(txtLongs);

		btnPositions = new JButton();
		btnPositions.setBounds(216, 36, 100, 23);
		btnPositions.setText("POSITIONS");
		pnlPositions.add(btnPositions);
		
		btnSymbols = new JButton();
		btnSymbols.setBounds(222, 12, 92, 23);
		btnSymbols.setText("SYMBOLS");
		pnlContent.add(btnSymbols);
		
		btnSignals = new JButton();
		btnSignals.setBounds(724, 12, 99, 23);
		btnSignals.setText("SIGNALS");
		pnlContent.add(btnSignals);
		
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

		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmConfig.launch();
			}
		});

		btnSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookFeel(!Config.isDarkMode());
			}
		});

		btnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmLogs.launch();
			}
		});
		
		btnBot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showBot();
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

		btnPositions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPositions();
			}
		});

		btnSignals.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSignals();
			}
		});
		
		btnSymbols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmSymbols.launch();
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
				BtcChangeService.deattachRefreshObserver(thisFrm);
				PositionService.deattachRefreshObserver(thisFrm);
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
		
	}

	private void pageload()
	{
		try
		{
			btnSkin.setIcon(Config.isDarkMode() ? Styles.IMAGE_SUN : Styles.IMAGE_MOON);

			loadConfig();
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void loadConfig()
	{

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
			listFavorites.setModel(toListModel(Symbol.getLstSymbolsMini(chkOnlyFavorites.isSelected(), chkOnlyBetters.isSelected())));
			btcInfo();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	@Override
	public void onBtcChangeUpdate()
	{
		try
		{
			double percent = BtcChangeService.getBtcChange30m();
			txtChange30m.setText(percent + " %");
			txtChange30m.setForeground(percent > 0 ? Styles.COLOR_TEXT_LONG : Styles.COLOR_TEXT_SHORT);
			txtBtcPrice.setForeground(percent > 0 ? Styles.COLOR_TEXT_LONG : Styles.COLOR_TEXT_SHORT);
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}
	
	public void btcInfo()
	{
		final String BTC_PAIR_SYMBOL = "BTC" + Constants.DEFAULT_SYMBOL_RIGHT;
		Symbol coin = Symbol.getInstance(BTC_PAIR_SYMBOL);
		if (!PriceService.getMapTickers().containsKey(coin.getName()))
		{
			return;
		}

		SymbolTickerEvent ticker = PriceService.getMapTickers().get(BTC_PAIR_SYMBOL);
		txtBtcPrice.setText(coin.priceToStr(ticker.getLastPrice()));
		txtChange24h.setText(String.format("%.2f %%", ticker.getPriceChangePercent()));
		txtChange24h.setForeground(ticker.getPriceChangePercent().doubleValue() > 0 ? Styles.COLOR_TEXT_LONG : Styles.COLOR_TEXT_SHORT);
	}

	@Override
	public void onPositionUpdate()
	{
		try
		{
			int shortCount = 0;
			int longCount = 0;
			List<PositionRisk> lstPositionRisk = PositionService.getLstPositionRisk();

			if (lstPositionRisk != null && !lstPositionRisk.isEmpty())
			{
				for (PositionRisk entry : lstPositionRisk)
				{
					if (entry.getPositionAmt().doubleValue() < 0)
					{
						shortCount++;
					}
					else if (entry.getPositionAmt().doubleValue() > 0)
					{
						longCount++;
					}
				}
			}
			txtShorts.setText(String.valueOf(shortCount));
			txtLongs.setText(String.valueOf(longCount));
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
			FrmGrid.launch(entry.getSymbol().getNameLeft(), "SHORT", entry.getSymbol().priceToStr(price), isBotMode);
		}
		else
		{
			Double price = Math.min(entry.getPrice().doubleValue(), entry.getLgShock().doubleValue());
			FrmGrid.launch(entry.getSymbol().getNameLeft(), "LONG", entry.getSymbol().priceToStr(price), isBotMode);
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
		launch();
	}

}
