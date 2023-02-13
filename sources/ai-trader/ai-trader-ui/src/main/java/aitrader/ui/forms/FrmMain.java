package aitrader.ui.forms;

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
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Alert;
import aitrader.core.model.SignalShow;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.core.model.enums.GridStrategy;
import aitrader.core.model.enums.OrderSide;
import aitrader.core.service.position.BalanceService;
import aitrader.core.service.position.PositionService;
import aitrader.core.service.signals.AlertService;
import aitrader.core.service.signals.SignalService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConfig;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.BeepUtils;
import aitrader.util.Convert;
import aitrader.util.observable.Handler;
import aitrader.util.price.PriceUtil;

public class FrmMain extends JFrame
{
	private static final long serialVersionUID = 1L;

	private List<SignalShow> lstShortSignals = null;
	private List<SignalShow> lstLongSignals = null;

	private JButton btnBot;
	private JButton btnSignals;
	private JButton btnSymbols;
	private JButton btnCoin;
	private JButton btnGrid;
	private JButton btnPositions;
	private JButton btnShoot;

	private JButton btnAlerts;
	private JButton btnConfig;
	private JButton btnApiKey;
	private JButton btnLog;
	private JButton btnRestart;
	private JButton btnSkin;

	private JCheckBox chkOnlyFavorites;
	private JCheckBox chkOnlyBetters;

	private CtrlError ctrlError;
	private JLabel lnkGitHub;

	private JList<String> listFavorites;
	private JList<String> listShortSignals;
	private JList<String> listLongSignals;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JLabel lblStrategy;
	private JComboBox<GridStrategy> cmbStrategy;

	private JTextField txtBalance;
	private JTextField txtPair;
	private JTextField txtWithdrawal;

	private JPanel panel;
	private JLabel lblNewLabel;
	private JLabel lblChange14d;
	private JLabel lblStoch24h;
	private JLabel lblChange24h;
	private JLabel lblBtcPrice;
	private JLabel txtChange14d;
	private JLabel txtStoch24h;
	private JLabel txtChange24h;
	private JLabel txtBtcPrice;
	private JPanel pnlPositions;
	private JLabel txtShorts;
	private JLabel lblShorts;
	private JLabel lblLongs;
	private JLabel txtLongs;

	private Handler<Void> priceServiceHandler = e -> { onPriceUpdate(); };
	private Handler<Void> signalServiceHandler = e -> { onSignalUpdate(); };
	private Handler<Alert> alertServiceHandler = e -> { onAlertsUptade(e); };
	private Handler<Void> balanceServiceHandler = e -> { onBalanceUpdate(); };
	private Handler<Void> positionServiceHandler = e -> { onPositionUpdate(); };

	public FrmMain()
	{
		initComponents();
		pageload();

		onPriceUpdate();
		SymbolInfoService.attachObserver(priceServiceHandler); 

		onSignalUpdate();
		SignalService.attachObserver(signalServiceHandler);

		AlertService.attachObserver(alertServiceHandler);

		onBalanceUpdate();
		BalanceService.attachObserver(balanceServiceHandler);

		PositionService.attachObserver(positionServiceHandler);
	}

	private void initComponents()
	{
		setTitle(UIConstants.APP_NAME + " - " + "unknown");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 940, 593);
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
		btnCoin.setText("SYMBOL");
		btnGrid = new JButton();
		btnGrid.setText("GRID");
		btnShoot = new JButton();
		btnShoot.setText("POST");

		btnAlerts = new JButton(Styles.IMAGE_BELL);
		btnAlerts.setToolTipText("Alerts");
		btnConfig = new JButton(Styles.IMAGE_WRENCH);
		btnConfig.setToolTipText("Config");
		btnApiKey = new JButton(Styles.IMAGE_KEY);
		btnApiKey.setToolTipText("Api Key");
		btnLog = new JButton(Styles.IMAGE_BUG);
		btnLog.setToolTipText("View log messages");
		btnRestart = new JButton(Styles.IMAGE_REDO);
		btnRestart.setToolTipText("Restart");
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

		ctrlError = new CtrlError();

		JScrollPane scrollFavorites = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollFavorites.setBorder(UIManager.getBorder("TextField.border"));
		scrollFavorites.setBounds(15, 42, 337, 307);
		pnlContent.add(scrollFavorites);

		listFavorites = new JList<String>();
		listFavorites.setFont(new Font("Courier New", Font.PLAIN, 12));
		listFavorites.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listFavorites.setForeground(Styles.COLOR_TEXT_AREA_FG);
		scrollFavorites.setViewportView(listFavorites);

		JScrollPane scrollShortSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollShortSignals.setBorder(UIManager.getBorder("TextField.border"));
		scrollShortSignals.setBounds(376, 42, 547, 145);
		pnlContent.add(scrollShortSignals);

		listShortSignals = new JList<String>();
		listShortSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		listShortSignals.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listShortSignals.setForeground(Styles.COLOR_TEXT_SHORT);
		scrollShortSignals.setViewportView(listShortSignals);

		JScrollPane scrollLongSignals = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollLongSignals.setBorder(UIManager.getBorder("TextField.border"));
		scrollLongSignals.setBounds(376, 203, 547, 145);
		pnlContent.add(scrollLongSignals);

		listLongSignals = new JList<String>();
		listLongSignals.setFont(new Font("Courier New", Font.PLAIN, 11));
		listLongSignals.setBackground(Styles.COLOR_TEXT_AREA_BG);
		listLongSignals.setForeground(Styles.COLOR_TEXT_LONG);
		scrollLongSignals.setViewportView(listLongSignals);

		JLabel lblSignals = new JLabel("SHORTS / LONGS");
		lblSignals.setHorizontalAlignment(SwingConstants.LEFT);
		lblSignals.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSignals.setBounds(376, 12, 141, 20);
		pnlContent.add(lblSignals);

		chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setHorizontalAlignment(SwingConstants.LEADING);
		chkOnlyFavorites.setBounds(15, 12, 99, 23);
		pnlContent.add(chkOnlyFavorites);

		chkOnlyBetters = new JCheckBox("Only betters");
		chkOnlyBetters.setSelected(true);
		chkOnlyBetters.setBounds(116, 12, 99, 23);
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
					.addComponent(btnAlerts)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnConfig)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnApiKey)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRestart)
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
							.addComponent(btnAlerts))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnConfig))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnApiKey))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnLog))
						.addGroup(pnlTopBarLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(btnRestart))
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
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
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
		panel.setBounds(15, 370, 559, 70);
		panel.setLayout(null);
		pnlContent.add(panel);

		lblNewLabel = new JLabel("BTC");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblNewLabel.setBounds(10, 24, 66, 14);
		panel.add(lblNewLabel);

		lblBtcPrice = new JLabel("PRICE");
		lblBtcPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBtcPrice.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBtcPrice.setBounds(101, 15, 105, 14);
		panel.add(lblBtcPrice);

		lblChange24h = new JLabel("CHANGE 24h");
		lblChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChange24h.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblChange24h.setBounds(211, 15, 105, 14);
		panel.add(lblChange24h);

		lblStoch24h = new JLabel("STOCH 24h");
		lblStoch24h.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStoch24h.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblStoch24h.setBounds(321, 15, 105, 14);
		panel.add(lblStoch24h);

		lblChange14d = new JLabel("CHANGE 14d");
		lblChange14d.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChange14d.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblChange14d.setBounds(431, 15, 105, 14);
		panel.add(lblChange14d);

		txtChange24h = new JLabel("---");
		txtChange24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txtChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChange24h.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtChange24h.setBounds(211, 38, 105, 14);
		panel.add(txtChange24h);

		txtStoch24h = new JLabel("---");
		txtStoch24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txtStoch24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStoch24h.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtStoch24h.setBounds(321, 38, 105, 14);
		panel.add(txtStoch24h);

		txtChange14d = new JLabel("---");
		txtChange14d.setForeground(Styles.COLOR_TEXT_ALT1);
		txtChange14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChange14d.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtChange14d.setBounds(431, 38, 105, 14);
		panel.add(txtChange14d);

		txtBtcPrice = new JLabel("---");
		txtBtcPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtBtcPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBtcPrice.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtBtcPrice.setBounds(101, 38, 105, 14);
		panel.add(txtBtcPrice);

		pnlPositions = new JPanel();
		pnlPositions.setBorder(UIManager.getBorder("TextField.border"));
		pnlPositions.setBounds(597, 370, 326, 70);
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

		btnSymbols = new JButton(Styles.IMAGE_EXPAND);
		btnSymbols.setToolTipText("Symbols");
		btnSymbols.setBounds(310, 10, 42, 26);
		pnlContent.add(btnSymbols);

		btnSignals = new JButton(Styles.IMAGE_EXPAND);
		btnSymbols.setToolTipText("Signals");
		btnSignals.setBounds(881, 10, 42, 26);
		pnlContent.add(btnSignals);

		lblStrategy = new JLabel("Strategy");
		lblStrategy.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStrategy.setBounds(670, 16, 70, 14);
		pnlContent.add(lblStrategy);

		cmbStrategy = new JComboBox<GridStrategy>();
		cmbStrategy.setBounds(750, 14, 116, 20);
		cmbStrategy.setModel(new DefaultComboBoxModel<GridStrategy>(GridStrategy.values()));
		pnlContent.add(cmbStrategy);

		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
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
							.addComponent(ctrlError)))
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
					UILog.error(ex);
				}
			}
		});

		btnAlerts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmAlerts.launch();
			}
		});

		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmConfig.launch();
			}
		});

		btnApiKey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmConfigSecure.launch();
			}
		});

		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrmServices.launch();
			}
		});

		btnSkin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("night".equalsIgnoreCase(UIConfig.getSkinMode()))
					setLookFeel("light");
				else
					setLookFeel("night");
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

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				SymbolInfoService.deattachObserver(priceServiceHandler);
				SignalService.deattachObserver(signalServiceHandler);
				AlertService.deattachObserver(alertServiceHandler);
				BalanceService.deattachObserver(balanceServiceHandler);
				PositionService.deattachObserver(positionServiceHandler);
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
					if (index > 0)
					{
						String item = (String) list.getModel().getElementAt(index);
						String symbolLeft = item.substring(0, item.indexOf(" "));
						FrmCoin.launch(symbolLeft);
					}
				}

			}
		});

		listShortSignals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				if (e.getClickCount() == 2)
				{
					int index = list.locationToIndex(e.getPoint());
					tradeFromSignal(OrderSide.SELL, index);
				}
			}
		});

		listLongSignals.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) e.getSource();
				if (e.getClickCount() == 2)
				{
					int index = list.locationToIndex(e.getPoint());
					tradeFromSignal(OrderSide.BUY, index);
				}
			}
		});

	}

	private void pageload()
	{
		try
		{
			btnSkin.setIcon("night".equalsIgnoreCase(UIConfig.getSkinMode()) ? Styles.IMAGE_SUN : Styles.IMAGE_MOON);

			loadConfig();
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void loadConfig()
	{
		cmbStrategy.setSelectedItem(CoreConfig.getGridStrategy());
		txtPair.setText(CoreConfig.getDefaultSymbolRight());
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
		FrmPost.launch(null);
	}

	private void showSignals()
	{
		FrmSignals.launch();
	}

	private void showBot()
	{
		FrmBot.launch();
	}

	private void setLookFeel(String skinMode)
	{
		try
		{
			UIConfig.setSkinMode(skinMode);
			UIConfig.save();
			Styles.applyStyle();

			dispose();
			FrmMain.launch(true);
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	public static void launch(boolean invokeLater) throws InvocationTargetException, InterruptedException
	{
		Runnable runnable = new Runnable()
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
					System.exit(-1);
				}
			}
		};

		if (invokeLater)
			EventQueue.invokeLater(runnable);
		else
			EventQueue.invokeAndWait(runnable);
	}

	// --------------------------------------------------------------------

	private void onBalanceUpdate()
	{
		try
		{
			BigDecimal balance = BalanceService.getBalance();
			if (balance != null)
				txtBalance.setText(Convert.usdToStr(balance.doubleValue()));
			
			BigDecimal withdrawAvailable = BalanceService.getWithdrawAvailable();
			if (withdrawAvailable != null)
				txtWithdrawal.setText(Convert.usdToStr(withdrawAvailable.doubleValue()));
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void onSignalUpdate()
	{
		try
		{
			loadListSignals();
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void onAlertsUptade(Alert alert)
	{
		try
		{
			if (alert != null)
			{
				ctrlError.INFO (alert.getSymbol() + " " + alert.getAlertPrice() + " / " + alert.getLimitPrice());
				BeepUtils.beep4();
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void onPriceUpdate()
	{
		try
		{
			listFavorites.setModel(toListModel(getSymbolsMini(chkOnlyFavorites.isSelected(), chkOnlyBetters.isSelected())));
			btcInfo();
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	public void btcInfo() throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		final String btcSymbolPair = "BTC" + CoreConfig.getDefaultSymbolRight();

		SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(btcSymbolPair);
		
		if (symbolInfo != null)
		{
			Symbol BTC = symbolInfo.getSymbol();
			
			txtBtcPrice.setText(BTC.priceToStr(symbolInfo.getLastPrice()));
			
			txtChange24h.setText(String.format("%.2f %%", symbolInfo.getChange24h()));
			txtChange24h.setForeground(symbolInfo.getChange24h().doubleValue() > 0 ? Styles.COLOR_TEXT_LONG : Styles.COLOR_TEXT_SHORT);
			txtStoch24h.setText(String.format("%.2f %%", symbolInfo.getStoch24h()));
			
			if (symbolInfo.getHigh14d() != null)
			{
				txtChange14d.setText(String.format("%.2f %%", symbolInfo.getChange14d()));
				txtChange14d.setForeground(symbolInfo.getChange14d().doubleValue() > 0 ? Styles.COLOR_TEXT_LONG : Styles.COLOR_TEXT_SHORT);
			}
		}
	}

	private void onPositionUpdate()
	{
		try
		{
			int shortCount = PositionService.getShortCount();
			txtShorts.setText(String.valueOf(shortCount));

			int longCount = PositionService.getLongCount();
			txtLongs.setText(String.valueOf(longCount));
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	public static List<String> getSymbolsMini(boolean onlyFavorites, boolean onlyBetters) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		List<String> list = new ArrayList<String>();
		list.add(String.format("%-10s %12s %10s %10s", "SYMBOL", "PRICE", "VOLUME", "CHANGE"));

		List<SymbolInfo> lstSymbolsInfo = SymbolInfoService.getLstSymbolsInfo(onlyFavorites, onlyBetters, onlyBetters);
		for (SymbolInfo entry : lstSymbolsInfo)
		{
			Symbol symbol = entry.getSymbol();
			list.add(String.format("%-10s %12s %10s %8.2f %%", symbol.getNameLeft(), symbol.priceToStr(entry.getLastPrice()), PriceUtil.cashFormat(entry.getQuoteVolume24h()), entry.getChange24h()));
		}

		return list;
	}

	private void loadListSignals()
	{
		lstShortSignals = SignalService.getLstShortSignalShow();
		AbstractListModel<String> listModel = new AbstractListModel<String>()
		{
			private static final long serialVersionUID = 1L;

			public int getSize()
			{
				return lstShortSignals.size();
			}

			public String getElementAt(int index)
			{
				SignalShow entry = lstShortSignals.get(index);

				String line = String.format(Locale.US, "%-8s %6.2f%% %12s     RATIO 1:%-4.1f    VOL %4s    CHG %6.2f%%\n", 
						entry.getSymbolInfo().getSymbol().getNameLeft(),
						entry.getDistance(),
						entry.getSignal().getLimitPrice(),
						entry.getSignal().getRatio(),
						PriceUtil.cashFormat(entry.getSymbolInfo().getQuoteVolume24h()),
						entry.getSymbolInfo().getChange24h());

				return line;
			}
		};
		listShortSignals.setModel(listModel);

		// --------------------------------------------------------------------

		lstLongSignals = SignalService.getLstLongSignalShow();
		AbstractListModel<String> listModel2 = new AbstractListModel<String>()
		{
			private static final long serialVersionUID = 1L;

			public int getSize()
			{
				return lstLongSignals.size();
			}

			public String getElementAt(int index)
			{
				SignalShow entry = lstLongSignals.get(index);

				String line = String.format(Locale.US, "%-8s %6.2f%% %12s     RATIO 1:%-4.1f    VOL %4s    CHG %6.2f%%\n", 
						entry.getSymbolInfo().getSymbol().getNameLeft(),
						entry.getDistance(),
						entry.getSignal().getLimitPrice(),
						entry.getSignal().getRatio(),
						PriceUtil.cashFormat(entry.getSymbolInfo().getQuoteVolume24h()),
						entry.getSymbolInfo().getChange24h());

				return line;
			}
		};
		listLongSignals.setModel(listModel2);
	}

	private void tradeFromSignal(OrderSide orderSide, int index)
	{
		GridStrategy gridStrategy = (GridStrategy) cmbStrategy.getSelectedItem();

		if (orderSide == OrderSide.SELL)
		{
			SignalShow entry = lstShortSignals.get(index);
			Symbol symbol = entry.getSymbolInfo().getSymbol();
			BigDecimal price = entry.getSignal().getLimitPrice();
			BigDecimal slDist = entry.getSignal().getSlDist();
			BigDecimal tpDist = entry.getSignal().getTpDist();

			FrmGrid.launch(symbol.getNameLeft(), "SHORT", symbol.priceToStr(price), slDist, tpDist, gridStrategy);
		}
		else
		{
			SignalShow entry = lstLongSignals.get(index);
			Symbol symbol = entry.getSymbolInfo().getSymbol();
			BigDecimal price = entry.getSignal().getLimitPrice();
			BigDecimal slDist = entry.getSignal().getSlDist();
			BigDecimal tpDist = entry.getSignal().getTpDist();

			FrmGrid.launch(symbol.getNameLeft(), "LONG", symbol.priceToStr(price), slDist, tpDist, gridStrategy);
		}
	}

	// --------------------------------------------------------------------

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

}
