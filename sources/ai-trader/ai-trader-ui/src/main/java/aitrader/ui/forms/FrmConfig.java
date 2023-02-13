package aitrader.ui.forms;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.enums.GridStrategy;
import aitrader.core.model.enums.PriceIncrType;
import aitrader.core.model.enums.QtyIncrType;
import aitrader.core.model.enums.QuantityType;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.Convert;
import technicals.indicators.depth.DephMergedPoints;
import technicals.indicators.depth.DephMergedPoints.MergeMode;

public class FrmConfig extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static FrmConfig myJFrame = null;

	private JPanel contentPane;

	private CtrlError ctrlError;
	
	private JRadioButton rbArithmetic;
	private JRadioButton rbGeometric;
	private JRadioButton rbOverLastOrder;
	private JRadioButton rbOverPosition;
	private JRadioButton rbQtyUsd;
	private JRadioButton rbQtyBalance;

	private JComboBox<DephMergedPoints.MergeMode> cmbMergeMode;
	private JComboBox<GridStrategy> cmbStrategy;

	private JCheckBox chkFavoriteSymbols;
	private JCheckBox chkBetterSymbols;
	
	private JTextArea txtWhiteList;
	private JTextArea txtBlackList;
	
	private JTextField txtMinChange24h;
	private JTextField txtMinVolume24h;
	private JTextField txtLowStoch24h;
	private JTextField txtWADist2;
	private JTextField txtWADist1;
	private JTextField txtBBBlocks;
	private JTextField txtMinShortLongDist;
	private JTextField txtMaxShortLongDist;
	private JTextField txtMinRatio;
	private JTextField txtTProfit;
	private JTextField txtPriceIncr;
	private JTextField txtIterations;
	private JTextField txtCoinsIncr;
	private JTextField txtStopLoss;
	private JTextField txtLeverage;
	private JTextField txtBalanceMinAvailable;
	private JTextField txtPositionsMax;
	private JTextField txtInQtyUsd;
	private JTextField txtInQtyBalance;
	private JTextField txtHighStoch24h;
	private JTextField txtWADist3;

	public FrmConfig()
	{
		initComponents();
		pageload();
	}

	private void initComponents()
	{
		setTitle(UIConstants.APP_NAME + " - Config");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfig.class.getResource("/resources/wrench.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 630, 410);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		ctrlError = new CtrlError();
		ctrlError.setBounds(10, 335, 473, 20);
		contentPane.add(ctrlError);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 593, 310);
		contentPane.add(tabbedPane);
		
		JPanel pnlMarket = new JPanel();
		tabbedPane.addTab("Market", null, pnlMarket, null);
		pnlMarket.setLayout(null);
		
		JLabel lblFavCoins = new JLabel("White list");
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		lblFavCoins.setBounds(25, 21, 90, 14);
		pnlMarket.add(lblFavCoins);
		
		JScrollPane scroll = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		scroll.setBounds(25, 41, 252, 96);
		pnlMarket.add(scroll);
		
		txtWhiteList = new JTextArea();
		scroll.setViewportView(txtWhiteList);
		
		JPanel pnlBetterSymbols = new JPanel();
		pnlBetterSymbols.setLayout(null);
		pnlBetterSymbols.setBorder(UIManager.getBorder("TextField.border"));
		pnlBetterSymbols.setBounds(25, 180, 537, 86);
		pnlMarket.add(pnlBetterSymbols);
		
		JLabel lblVolume = new JLabel("Min Volume 24h");
		lblVolume.setBounds(24, 27, 100, 14);
		pnlBetterSymbols.add(lblVolume);
		
		JLabel lblChange = new JLabel("Min Change 24h");
		lblChange.setBounds(134, 27, 100, 14);
		pnlBetterSymbols.add(lblChange);
		
		JLabel lblFavCoins_1 = new JLabel("Black list");
		lblFavCoins_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblFavCoins_1.setBounds(310, 21, 90, 14);
		pnlMarket.add(lblFavCoins_1);
		
		JScrollPane scroll_1 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_1.setBorder(UIManager.getBorder("TextField.border"));
		scroll_1.setBounds(310, 41, 252, 96);
		pnlMarket.add(scroll_1);
		
		txtBlackList = new JTextArea();
		scroll_1.setViewportView(txtBlackList);
		
		JLabel lblBetterSymbols = new JLabel("Better symbols");
		lblBetterSymbols.setHorizontalAlignment(SwingConstants.LEFT);
		lblBetterSymbols.setBounds(25, 160, 90, 14);
		pnlMarket.add(lblBetterSymbols);
		
		txtMinChange24h = new JTextField();
		txtMinChange24h.setText("10.0");
		txtMinChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMinChange24h.setColumns(10);
		txtMinChange24h.setBounds(134, 46, 86, 20);
		pnlBetterSymbols.add(txtMinChange24h);
		
		txtMinVolume24h = new JTextField();
		txtMinVolume24h.setText("180000000");
		txtMinVolume24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMinVolume24h.setColumns(10);
		txtMinVolume24h.setBounds(24, 46, 86, 20);
		pnlBetterSymbols.add(txtMinVolume24h);
		
		JLabel lblStochh = new JLabel("Stochastic 24h");
		lblStochh.setHorizontalAlignment(SwingConstants.CENTER);
		lblStochh.setBounds(369, 27, 146, 14);
		pnlBetterSymbols.add(lblStochh);
		
		txtLowStoch24h = new JTextField();
		txtLowStoch24h.setText("20");
		txtLowStoch24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtLowStoch24h.setColumns(10);
		txtLowStoch24h.setBounds(369, 47, 50, 20);
		pnlBetterSymbols.add(txtLowStoch24h);
		
		JLabel lblStochh_1 = new JLabel("> || <");
		lblStochh_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStochh_1.setBounds(427, 50, 31, 14);
		pnlBetterSymbols.add(lblStochh_1);
		
		txtHighStoch24h = new JTextField();
		txtHighStoch24h.setText("80");
		txtHighStoch24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtHighStoch24h.setColumns(10);
		txtHighStoch24h.setBounds(465, 47, 50, 20);
		pnlBetterSymbols.add(txtHighStoch24h);

		JPanel pnlSignals = new JPanel();
		tabbedPane.addTab("Signals", null, pnlSignals, null);
		pnlSignals.setLayout(null);
		
		JLabel lblPointsMode = new JLabel("Points Mode");
		lblPointsMode.setBounds(22, 209, 86, 14);
		pnlSignals.add(lblPointsMode);
		
		cmbMergeMode = new JComboBox<MergeMode>();
		cmbMergeMode.setBounds(22, 228, 86, 20);
		cmbMergeMode.setModel(new DefaultComboBoxModel<MergeMode>(MergeMode.values()));
		pnlSignals.add(cmbMergeMode);
		
		chkFavoriteSymbols = new JCheckBox("White & black list");
		chkFavoriteSymbols.setBounds(16, 29, 118, 23);
		pnlSignals.add(chkFavoriteSymbols);
		
		chkBetterSymbols = new JCheckBox("Better Symbols");
		chkBetterSymbols.setBounds(149, 29, 118, 23);
		pnlSignals.add(chkBetterSymbols);
		
		JPanel pnlOBWPoints = new JPanel();
		pnlOBWPoints.setLayout(null);
		pnlOBWPoints.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " O.B. Weighted points ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlOBWPoints.setBounds(10, 84, 390, 87);
		pnlSignals.add(pnlOBWPoints);
		
		JLabel lblMinDistShLg = new JLabel("Min Dist SH-LG");
		lblMinDistShLg.setBounds(255, 209, 86, 14);
		pnlSignals.add(lblMinDistShLg);

		txtMinShortLongDist = new JTextField();
		txtMinShortLongDist.setBounds(255, 228, 86, 20);
		txtMinShortLongDist.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlSignals.add(txtMinShortLongDist);

		JLabel lblMaxDistShLg = new JLabel("Max Dist SH-LG");
		lblMaxDistShLg.setBounds(365, 209, 86, 14);
		pnlSignals.add(lblMaxDistShLg);
		
		txtMaxShortLongDist = new JTextField();
		txtMaxShortLongDist.setBounds(365, 228, 86, 20);
		txtMaxShortLongDist.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlSignals.add(txtMaxShortLongDist);
		
		JLabel lblMinRatio = new JLabel("Min Ratio");
		lblMinRatio.setBounds(475, 209, 86, 14);
		pnlSignals.add(lblMinRatio);
		
		txtMinRatio = new JTextField();
		txtMinRatio.setBounds(475, 228, 86, 20);
		txtMinRatio.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlSignals.add(txtMinRatio);
		
		JPanel pnlOBBBlocks = new JPanel();
		pnlOBBBlocks.setLayout(null);
		pnlOBBBlocks.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " O.B. Best block ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlOBBBlocks.setBounds(410, 84, 168, 87);
		pnlSignals.add(pnlOBBBlocks);
		
		JLabel lblDist1ToAnalyzeWA = new JLabel("% Distance 1");
		lblDist1ToAnalyzeWA.setBounds(21, 26, 86, 14);
		pnlOBWPoints.add(lblDist1ToAnalyzeWA);
		
		JLabel lblDist2ToAnalyzeWA = new JLabel("% Distance 2");
		lblDist2ToAnalyzeWA.setBounds(131, 26, 86, 14);
		pnlOBWPoints.add(lblDist2ToAnalyzeWA);
		
		JLabel lblDist2ToAnalyzeWA_1 = new JLabel("% Distance 3");
		lblDist2ToAnalyzeWA_1.setBounds(241, 26, 86, 14);
		pnlOBWPoints.add(lblDist2ToAnalyzeWA_1);
	
		txtWADist1 = new JTextField();
		txtWADist1.setBounds(21, 45, 86, 20);
		txtWADist1.setText("0.5");
		txtWADist1.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlOBWPoints.add(txtWADist1);

		txtWADist2 = new JTextField();
		txtWADist2.setBounds(131, 45, 86, 20);
		txtWADist2.setText("0.5");
		txtWADist2.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlOBWPoints.add(txtWADist2);

		txtWADist3 = new JTextField();
		txtWADist3.setBounds(241, 45, 86, 20);
		txtWADist3.setText("0.5");
		txtWADist3.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlOBWPoints.add(txtWADist3);
		
		JLabel lblBlocksToAnalyzeBB = new JLabel("# Blocks");
		lblBlocksToAnalyzeBB.setBounds(22, 23, 118, 14);
		pnlOBBBlocks.add(lblBlocksToAnalyzeBB);
		
		txtBBBlocks = new JTextField();
		txtBBBlocks.setBounds(22, 42, 86, 20);
		txtBBBlocks.setText("8");
		txtBBBlocks.setHorizontalAlignment(SwingConstants.TRAILING);
		pnlOBBBlocks.add(txtBBBlocks);
		
		JPanel pnlTrade = new JPanel();
		tabbedPane.addTab("Trade", null, pnlTrade, null);
		pnlTrade.setLayout(null);
		
		JPanel pnlGrid = new JPanel();
		pnlGrid.setLayout(null);
		pnlGrid.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Custom Grid Strategy ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGrid.setBounds(15, 15, 554, 145);
		pnlTrade.add(pnlGrid);
		
		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		lblItarations.setBounds(24, 27, 80, 14);
		pnlGrid.add(lblItarations);
		
		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(266, 27, 80, 14);
		pnlGrid.add(lblPriceIncr);
		
		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		lblTProfit.setBounds(356, 78, 80, 14);
		pnlGrid.add(lblTProfit);
		
		txtTProfit = new JTextField();
		txtTProfit.setText((String) null);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		txtTProfit.setBounds(356, 97, 72, 20);
		pnlGrid.add(txtTProfit);
		
		txtPriceIncr = new JTextField();
		txtPriceIncr.setText((String) null);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		txtPriceIncr.setBounds(266, 46, 72, 20);
		pnlGrid.add(txtPriceIncr);
		
		txtIterations = new JTextField();
		txtIterations.setText("2");
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		txtIterations.setBounds(24, 46, 72, 20);
		pnlGrid.add(txtIterations);
		
		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(266, 78, 80, 14);
		pnlGrid.add(lblCoinsIncr);
		
		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setText((String) null);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		txtCoinsIncr.setBounds(266, 97, 72, 20);
		pnlGrid.add(txtCoinsIncr);
		
		txtStopLoss = new JTextField();
		txtStopLoss.setText((String) null);
		txtStopLoss.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopLoss.setColumns(10);
		txtStopLoss.setBounds(356, 46, 72, 20);
		pnlGrid.add(txtStopLoss);
		
		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(356, 27, 90, 14);
		pnlGrid.add(lblDistSL);
		
		rbArithmetic = new JRadioButton("Arithmetic");
		rbArithmetic.setSelected(false);
		rbArithmetic.setBounds(23, 75, 90, 23);
		pnlGrid.add(rbArithmetic);
		
		rbGeometric = new JRadioButton("Geometric");
		rbGeometric.setSelected(true);
		rbGeometric.setBounds(23, 98, 90, 23);
		pnlGrid.add(rbGeometric);
		
		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(rbArithmetic);
		bg1.add(rbGeometric);
		
		rbOverLastOrder = new JRadioButton("Over last order qty");
		rbOverLastOrder.setSelected(false);
		rbOverLastOrder.setBounds(124, 78, 140, 23);
		pnlGrid.add(rbOverLastOrder);
		
		rbOverPosition = new JRadioButton("Over position qty");
		rbOverPosition.setSelected(true);
		rbOverPosition.setBounds(124, 99, 140, 23);
		pnlGrid.add(rbOverPosition);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(rbOverLastOrder);
		bg2.add(rbOverPosition);
		
		rbQtyUsd = new JRadioButton("Usd");
		rbQtyUsd.setBounds(451, 23, 90, 23);
		pnlGrid.add(rbQtyUsd);
		rbQtyUsd.setSelected(true);

		rbQtyBalance = new JRadioButton("Balance %");
		rbQtyBalance.setBounds(451, 74, 90, 23);
		pnlGrid.add(rbQtyBalance);
		rbQtyBalance.setSelected(false);

		ButtonGroup bg3 = new ButtonGroup();
		bg3.add(rbQtyUsd);
		bg3.add(rbQtyBalance);
		
		txtInQtyUsd = new JTextField();
		txtInQtyUsd.setBounds(453, 46, 72, 20);
		txtInQtyUsd.setText("10.0");
		txtInQtyUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlGrid.add(txtInQtyUsd);

		txtInQtyBalance = new JTextField();
		txtInQtyBalance.setBounds(453, 97, 72, 20);
		txtInQtyBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		pnlGrid.add(txtInQtyBalance);
		
		JLabel lblStrategy = new JLabel("Default Grid Strategy");
		lblStrategy.setBounds(449, 215, 120, 14);
		pnlTrade.add(lblStrategy);
		
		cmbStrategy = new JComboBox<GridStrategy>();
		cmbStrategy.setBounds(449, 235, 116, 22);
		cmbStrategy.setModel(new DefaultComboBoxModel<GridStrategy>(GridStrategy.values()));
		pnlTrade.add(cmbStrategy);
		
		JPanel panelPositions = new JPanel();
		panelPositions.setLayout(null);
		panelPositions.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Balance / Positions ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPositions.setBounds(15, 180, 310, 87);
		pnlTrade.add(panelPositions);
		
		txtLeverage = new JTextField();
		txtLeverage.setText("10");
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setEditable(false);
		txtLeverage.setBounds(24, 46, 72, 20);
		panelPositions.add(txtLeverage);
		
		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setText((String) null);
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setBounds(205, 46, 72, 20);
		panelPositions.add(txtBalanceMinAvailable);
		
		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setBounds(24, 27, 80, 14);
		panelPositions.add(lblLeverage);
		
		JLabel lblAvailable = new JLabel("Min balance %");
		lblAvailable.setBounds(205, 27, 80, 14);
		panelPositions.add(lblAvailable);
		
		txtPositionsMax = new JTextField();
		txtPositionsMax.setText("5");
		txtPositionsMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionsMax.setBounds(113, 46, 72, 20);
		panelPositions.add(txtPositionsMax);
		
		JLabel lblPositionsMax = new JLabel("Max positions");
		lblPositionsMax.setBounds(115, 27, 80, 14);
		panelPositions.add(lblPositionsMax);

		JPanel pnlOthers = new JPanel();
		tabbedPane.addTab("Others", null, pnlOthers, null);
		pnlOthers.setLayout(null);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.setBounds(510, 334, 89, 23);
		contentPane.add(btnSave);

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});		

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfig();
			}
		});
		
	}

	private void pageload()
	{
		try
		{
			loadConfig();
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private static Double toDouble(String value)
	{
		return Double.valueOf(value);
	}

	private static Long toLong(String value)
	{
		return Long.valueOf(value);
	}

	private static Integer toInteger(String value)
	{
		return Integer.valueOf(value);
	}

	private static String toString(Number value)
	{
		return value == null ? "" : String.valueOf(value);
	}

	public static void launch()
	{
		if (myJFrame != null)
		{
			myJFrame.toFront();
			myJFrame.setState(Frame.NORMAL);
			return;
		}

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					myJFrame = new FrmConfig();
					myJFrame.setVisible(true);
				}
				catch (Exception e)
				{
					UILog.error(e);
				}

			}
		});
	}

	private void loadConfig()
	{
		txtWhiteList.setText(CoreConfig.getWhiteList());
		txtBlackList.setText(CoreConfig.getBlackList());
		txtMinVolume24h.setText(toString(CoreConfig.getMinVolume24h()));
		txtMinChange24h.setText(toString(CoreConfig.getMinChange24h()));
		txtHighStoch24h.setText(toString(CoreConfig.getHighStoch24h()));
		txtLowStoch24h.setText(toString(CoreConfig.getLowStoch24h()));

		chkFavoriteSymbols.setSelected(CoreConfig.getFavoriteSymbols());
		chkBetterSymbols.setSelected(CoreConfig.getBetterSymbols());
		txtBBBlocks.setText(toString(CoreConfig.getBlocksToAnalizeBB()));
		txtWADist1.setText(toString(CoreConfig.getDist1ToAnalizeWA()));
		txtWADist2.setText(toString(CoreConfig.getDist2ToAnalizeWA()));
		txtWADist3.setText(toString(CoreConfig.getDist3ToAnalizeWA()));
		cmbMergeMode.setSelectedItem(CoreConfig.getMergeMode());
		txtMinShortLongDist.setText(toString(CoreConfig.getMinShortLongDist()));
		txtMaxShortLongDist.setText(toString(CoreConfig.getMaxShortLongDist()));
		txtMinRatio.setText(toString(CoreConfig.getMinRatio()));
		
		txtIterations.setText(toString(CoreConfig.getIterations()));
		rbArithmetic.setSelected(CoreConfig.getPriceIncrType() == PriceIncrType.ARITHMETIC);
		rbGeometric.setSelected(CoreConfig.getPriceIncrType() == PriceIncrType.GEOMETRIC);
		rbOverLastOrder.setSelected(CoreConfig.getQtyIncrType() == QtyIncrType.ORDER);
		rbOverPosition.setSelected(CoreConfig.getQtyIncrType() == QtyIncrType.POSITION);
		txtPriceIncr.setText(Convert.toStrPercent(CoreConfig.getPriceIncr()));
		txtCoinsIncr.setText(Convert.toStrPercent(CoreConfig.getQtyIncr()));
		txtStopLoss.setText(Convert.toStrPercent(CoreConfig.getStopLoss()));
		txtTProfit.setText(Convert.toStrPercent(CoreConfig.getTakeProfit()));

		rbQtyUsd.setSelected(CoreConfig.getQuantityType() == QuantityType.USD);
		txtInQtyUsd.setText(toString(CoreConfig.getInQty()));
		rbQtyBalance.setSelected(CoreConfig.getQuantityType() == QuantityType.BALANCE);
		txtInQtyBalance.setText(Convert.toStrPercent(CoreConfig.getInQty()));

		txtLeverage.setText(toString(CoreConfig.getLeverage()));
		txtPositionsMax.setText(toString(CoreConfig.getPositionsMax()));
		txtBalanceMinAvailable.setText(Convert.toStrPercent(CoreConfig.getBalanceMinAvailable()));

		cmbStrategy.setSelectedItem(CoreConfig.getGridStrategy());
	}

	private void saveConfig()
	{
		ctrlError.CLEAN();
		try
		{
			CoreConfig.setWhiteList(txtWhiteList.getText());
			CoreConfig.setBlackList(txtBlackList.getText());
			CoreConfig.setMinVolume24h(toLong(txtMinVolume24h.getText()));
			CoreConfig.setMinChange24h(toDouble(txtMinChange24h.getText()));
			CoreConfig.setHighStoch24h(toDouble(txtHighStoch24h.getText()));
			CoreConfig.setLowStoch24h(toDouble(txtLowStoch24h.getText()));

			CoreConfig.setFavoriteSymbols(chkFavoriteSymbols.isSelected());
			CoreConfig.setBetterSymbols(chkBetterSymbols.isSelected());
			CoreConfig.setBlocksToAnalizeBB(toInteger(txtBBBlocks.getText()));
			CoreConfig.setDist1ToAnalizeWA(toDouble(txtWADist1.getText()));
			CoreConfig.setDist2ToAnalizeWA(toDouble(txtWADist2.getText()));
			CoreConfig.setDist3ToAnalizeWA(toDouble(txtWADist3.getText()));
			CoreConfig.setMergeMode((MergeMode)cmbMergeMode.getSelectedItem());
			CoreConfig.setMinShortLongDist(toDouble(txtMinShortLongDist.getText()));
			CoreConfig.setMaxShortLongDist(toDouble(txtMaxShortLongDist.getText()));
			CoreConfig.setMinRatio(toDouble(txtMinRatio.getText()));

			CoreConfig.setIterations(toInteger(txtIterations.getText()));
			CoreConfig.setPriceIncrType(rbArithmetic.isSelected() ? PriceIncrType.ARITHMETIC : PriceIncrType.GEOMETRIC);
			CoreConfig.setQtyIncrType(rbOverLastOrder.isSelected() ? QtyIncrType.ORDER : QtyIncrType.POSITION);
			CoreConfig.setPriceIncr(Convert.strPercentToDbl(txtPriceIncr.getText()));
			CoreConfig.setQtyIncr(Convert.strPercentToDbl(txtCoinsIncr.getText()));
			CoreConfig.setStopLoss(Convert.strPercentToDbl(txtStopLoss.getText()));
			CoreConfig.setTakeProfit(Convert.strPercentToDbl(txtTProfit.getText()));

			if (rbQtyUsd.isSelected())
			{
				CoreConfig.setQuantityType(QuantityType.USD);
				CoreConfig.setInQty(toDouble(txtInQtyUsd.getText()));
			}
			else
			{
				CoreConfig.setQuantityType(QuantityType.BALANCE);
				CoreConfig.setInQty(Convert.strPercentToDbl(txtInQtyBalance.getText()));
			}

			CoreConfig.setLeverage(toInteger(txtLeverage.getText()));
			CoreConfig.setPositionsMax(toInteger(txtPositionsMax.getText()));
			CoreConfig.setBalanceMinAvailable(Convert.strPercentToDbl(txtBalanceMinAvailable.getText()));			
			
			CoreConfig.setGridStrategy((GridStrategy) cmbStrategy.getSelectedItem());

			CoreConfig.save();
			ctrlError.INFO("CONFIG SAVED");
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}
}
