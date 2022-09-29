package sanzol.aitrader.ui.forms;

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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.enums.PriceIncrType;
import sanzol.aitrader.be.enums.QtyIncrType;
import sanzol.aitrader.be.enums.QuantityType;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.Convert;
import sanzol.util.log.LogService;
import javax.swing.JComboBox;
import sanzol.aitrader.be.enums.GridStrategy;

public class FrmConfig extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static FrmConfig myJFrame = null;

	private CtrlError ctrlError;

	private JButton btnSaveConfig;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;

	private JRadioButton rbArithmetic;
	private JRadioButton rbGeometric;
	private JRadioButton rbOverLastOrder;
	private JRadioButton rbOverPosition;
	private JRadioButton rbQtyUsd;
	private JRadioButton rbQtyBalance;

	private JTextArea txtFavCoins;
	private JTextField txtBSMinVolume;
	private JTextField txtBSMaxChange24h;
	private JTextField txtBlocksToAnalyzeBB;
	private JTextField txtBlocksToAnalyzeWA;
	private JTextField txtIterations;
	private JTextField txtPriceIncr;
	private JTextField txtCoinsIncr;
	private JTextField txtStopLoss;
	private JTextField txtTProfit;
	private JTextField txtInQtyUsd;
	private JTextField txtInQtyBalance;
	private JTextField txtLeverage;
	private JTextField txtPositionsMax;
	private JTextField txtBalanceMinAvailable;
	private JCheckBox chkPIP;
	private JTextField txtPIPBase;
	private JTextField txtPIPCoef;

	private JComboBox<GridStrategy> cmbStrategy;

	public FrmConfig()
	{
		initComponents();
		pageload();
	}

	private void initComponents()
	{
		setTitle(Constants.APP_NAME + " - Config");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfig.class.getResource("/resources/wrench.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 860, 600);
		setLocationRelativeTo(null);
		setResizable(false);

		pnlContent = new JPanel();

		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		ctrlError = new CtrlError();

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
		);

		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);

		JPanel pnlSymbols = new JPanel();
		pnlSymbols.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Symbols ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSymbols.setBounds(15, 25, 490, 145);
		pnlSymbols.setLayout(null);
		pnlContent.add(pnlSymbols);

		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setBounds(24, 27, 90, 14);
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		pnlSymbols.add(lblFavCoins);

		txtFavCoins = new JTextArea();
		txtFavCoins.setLineWrap(true);
		txtFavCoins.setWrapStyleWord(true);

		JScrollPane scroll = new JScrollPane(txtFavCoins, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		scroll.setBounds(24, 46, 440, 72);
		pnlSymbols.add(scroll);

		JPanel pnlGrid = new JPanel();
		pnlGrid.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Custom Grid Strategy ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGrid.setBounds(15, 188, 490, 145);
		pnlGrid.setLayout(null);
		pnlContent.add(pnlGrid);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(24, 27, 80, 14);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		pnlGrid.add(lblItarations);

		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(310, 27, 80, 14);
		pnlGrid.add(lblPriceIncr);

		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setBounds(400, 78, 80, 14);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		pnlGrid.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setBounds(400, 97, 72, 20);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		pnlGrid.add(txtTProfit);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(310, 46, 72, 20);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		pnlGrid.add(txtPriceIncr);

		txtIterations = new JTextField();
		txtIterations.setBounds(24, 46, 72, 20);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		pnlGrid.add(txtIterations);

		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(310, 78, 80, 14);
		pnlGrid.add(lblCoinsIncr);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setBounds(310, 97, 72, 20);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		pnlGrid.add(txtCoinsIncr);

		txtStopLoss = new JTextField();
		txtStopLoss.setBounds(400, 46, 72, 20);
		txtStopLoss.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopLoss.setColumns(10);
		pnlGrid.add(txtStopLoss);

		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(400, 27, 90, 14);
		pnlGrid.add(lblDistSL);

		rbArithmetic = new JRadioButton("Arithmetic");
		rbArithmetic.setBounds(23, 75, 90, 23);
		pnlGrid.add(rbArithmetic);

		rbGeometric = new JRadioButton("Geometric");
		rbGeometric.setBounds(23, 98, 90, 23);
		pnlGrid.add(rbGeometric);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(rbArithmetic);
		bg1.add(rbGeometric);

		rbOverLastOrder = new JRadioButton("Over last order qty");
		rbOverLastOrder.setBounds(124, 78, 140, 23);
		pnlGrid.add(rbOverLastOrder);

		rbOverPosition = new JRadioButton("Over position qty");
		rbOverPosition.setBounds(124, 99, 140, 23);
		pnlGrid.add(rbOverPosition);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(rbOverLastOrder);
		bg2.add(rbOverPosition);

		scroll = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		scroll.setBounds(37, -89, 437, 28);
		pnlGrid.add(scroll);

		chkPIP = new JCheckBox("Progr. price increase");
		chkPIP.setBounds(122, 23, 142, 23);
		pnlGrid.add(chkPIP);

		txtPIPBase = new JTextField();
		txtPIPBase.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPIPBase.setColumns(10);
		txtPIPBase.setBounds(122, 46, 72, 20);
		pnlGrid.add(txtPIPBase);

		txtPIPCoef = new JTextField();
		txtPIPCoef.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPIPCoef.setColumns(10);
		txtPIPCoef.setBounds(200, 46, 72, 20);
		pnlGrid.add(txtPIPCoef);

		JPanel pnlOBook = new JPanel();
		pnlOBook.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " O.Book ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlOBook.setBounds(675, 25, 150, 145);
		pnlContent.add(pnlOBook);
		pnlOBook.setLayout(null);

		JLabel lblBlocksToAnalyzeBB = new JLabel("Blocks to analyze BB");
		lblBlocksToAnalyzeBB.setBounds(24, 27, 118, 14);
		pnlOBook.add(lblBlocksToAnalyzeBB);

		JLabel lblBlocksToAnalyzeWA = new JLabel("Blocks to analyze WA");
		lblBlocksToAnalyzeWA.setBounds(24, 78, 118, 14);
		pnlOBook.add(lblBlocksToAnalyzeWA);

		txtBlocksToAnalyzeWA = new JTextField();
		txtBlocksToAnalyzeWA.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBlocksToAnalyzeWA.setColumns(10);
		txtBlocksToAnalyzeWA.setBounds(24, 97, 86, 20);
		pnlOBook.add(txtBlocksToAnalyzeWA);

		txtBlocksToAnalyzeBB = new JTextField();
		txtBlocksToAnalyzeBB.setBounds(24, 46, 86, 20);
		txtBlocksToAnalyzeBB.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBlocksToAnalyzeBB.setColumns(10);
		pnlOBook.add(txtBlocksToAnalyzeBB);

		JPanel panelPositions = new JPanel();
		panelPositions.setBounds(515, 188, 310, 145);
		panelPositions.setLayout(null);
		panelPositions.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Balance / Positions ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlContent.add(panelPositions);

		txtLeverage = new JTextField();
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setEditable(false);
		txtLeverage.setColumns(10);
		txtLeverage.setBounds(24, 46, 72, 20);
		panelPositions.add(txtLeverage);

		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setColumns(10);
		txtBalanceMinAvailable.setBounds(24, 97, 72, 20);
		panelPositions.add(txtBalanceMinAvailable);

		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setBounds(24, 27, 80, 14);
		panelPositions.add(lblLeverage);

		JLabel lblAvailable = new JLabel("Min balance %");
		lblAvailable.setBounds(24, 78, 80, 14);
		panelPositions.add(lblAvailable);

		txtPositionsMax = new JTextField();
		txtPositionsMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionsMax.setColumns(10);
		txtPositionsMax.setBounds(113, 46, 72, 20);
		panelPositions.add(txtPositionsMax);

		JLabel lblPositionsMax = new JLabel("Max positions");
		lblPositionsMax.setBounds(115, 27, 80, 14);
		panelPositions.add(lblPositionsMax);

		txtInQtyUsd = new JTextField();
		txtInQtyUsd.setBounds(216, 50, 72, 20);
		txtInQtyUsd.setText("10.0");
		txtInQtyUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInQtyUsd.setColumns(10);
		panelPositions.add(txtInQtyUsd);

		rbQtyUsd = new JRadioButton("Usd");
		rbQtyUsd.setBounds(214, 27, 90, 23);
		panelPositions.add(rbQtyUsd);

		rbQtyBalance = new JRadioButton("Balance %");
		rbQtyBalance.setBounds(214, 78, 90, 23);
		panelPositions.add(rbQtyBalance);

		ButtonGroup bg3 = new ButtonGroup();
		bg3.add(rbQtyUsd);
		bg3.add(rbQtyBalance);

		txtInQtyBalance = new JTextField();
		txtInQtyBalance.setBounds(216, 101, 72, 20);
		txtInQtyBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInQtyBalance.setColumns(10);
		panelPositions.add(txtInQtyBalance);

		JPanel pnlBetterSymbols = new JPanel();
		pnlBetterSymbols.setBounds(515, 25, 150, 145);
		pnlBetterSymbols.setLayout(null);
		pnlBetterSymbols.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Better symbols ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlContent.add(pnlBetterSymbols);

		JLabel lblVolume = new JLabel("Min Volume 24h");
		lblVolume.setBounds(24, 27, 100, 14);
		pnlBetterSymbols.add(lblVolume);

		JLabel lblChange = new JLabel("Max Change 24h");
		lblChange.setBounds(24, 78, 100, 14);
		pnlBetterSymbols.add(lblChange);

		txtBSMaxChange24h = new JTextField();
		txtBSMaxChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBSMaxChange24h.setBounds(24, 97, 86, 20);
		txtBSMaxChange24h.setColumns(10);
		pnlBetterSymbols.add(txtBSMaxChange24h);

		txtBSMinVolume = new JTextField();
		txtBSMinVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBSMinVolume.setBounds(24, 46, 86, 20);
		txtBSMinVolume.setColumns(10);
		pnlBetterSymbols.add(txtBSMinVolume);

		btnSaveConfig = new JButton();
		btnSaveConfig.setBounds(753, 350, 72, 22);
		btnSaveConfig.setText("SAVE");
		pnlContent.add(btnSaveConfig);
		
		JLabel lblStrategy = new JLabel("Default Grid Strategy");
		lblStrategy.setBounds(20, 345, 120, 14);
		pnlContent.add(lblStrategy);
		
		cmbStrategy = new JComboBox<GridStrategy>();
		cmbStrategy.setBounds(142, 342, 116, 20);
		cmbStrategy.setModel(new DefaultComboBoxModel<GridStrategy>(GridStrategy.values()));
		pnlContent.add(cmbStrategy);

		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(16)
					.addComponent(ctrlError)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});

		btnSaveConfig.addActionListener(new ActionListener() {
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

	private void loadConfig()
	{
		txtFavCoins.setText(Config.getFavoriteSymbols());

		txtBSMinVolume.setText(toString(Config.getBetterSymbolsMinVolume()));
		txtBSMaxChange24h.setText(toString(Config.getBetterSymbolsMaxChange()));

		txtBlocksToAnalyzeBB.setText(toString(Config.getBlocksToAnalizeBB()));
		txtBlocksToAnalyzeWA.setText(toString(Config.getBlocksToAnalizeWA()));

		cmbStrategy.setSelectedItem(Config.getGridStrategy());

		txtIterations.setText(toString(Config.getIterations()));
		rbArithmetic.setSelected(Config.getPriceIncrType() == PriceIncrType.ARITHMETIC);
		rbGeometric.setSelected(Config.getPriceIncrType() == PriceIncrType.GEOMETRIC);
		rbOverLastOrder.setSelected(Config.getQtyIncrType() == QtyIncrType.ORDER);
		rbOverPosition.setSelected(Config.getQtyIncrType() == QtyIncrType.POSITION);
		chkPIP.setSelected(false);
		txtPIPBase.setText(Convert.toStrPercent(Config.getPipBase()));
		txtPIPCoef.setText(Convert.toStrPercent(Config.getPipCoef()));
		txtPriceIncr.setText(Convert.toStrPercent(Config.getPriceIncr()));
		txtCoinsIncr.setText(Convert.toStrPercent(Config.getQtyIncr()));
		txtStopLoss.setText(Convert.toStrPercent(Config.getStopLoss()));
		txtTProfit.setText(Convert.toStrPercent(Config.getTakeProfit()));

		rbQtyUsd.setSelected(Config.getQuantityType() == QuantityType.USD);
		rbQtyBalance.setSelected(Config.getQuantityType() == QuantityType.BALANCE);

		txtInQtyUsd.setText(rbQtyUsd.isSelected() ? Config.getInQty().toString() : "");
		txtInQtyBalance.setText(rbQtyBalance.isSelected() ? Convert.toStrPercent(Config.getInQty()) : "");

		txtLeverage.setText(toString(Config.getLeverage()));
		txtPositionsMax.setText(toString(Config.getPositionsMax()));
		txtBalanceMinAvailable.setText(Convert.toStrPercent(Config.getBalanceMinAvailable()));
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
					LogService.error(e);
				}

			}
		});
	}

	private void saveConfig()
	{
		ctrlError.CLEAN();
		try
		{
			Config.setFavoriteSymbols(txtFavCoins.getText());
			Config.setBetterSymbolsMinVolume(txtBSMinVolume.getText());
			Config.setBetterSymbolsMaxChange(txtBSMaxChange24h.getText());
			Config.setBlocksToAnalizeBB(txtBlocksToAnalyzeBB.getText());
			Config.setBlocksToAnalizeWA(txtBlocksToAnalyzeWA.getText());

			Config.setGridStrategy((GridStrategy) cmbStrategy.getSelectedItem());

			Config.setIterations(txtIterations.getText());
			Config.setPipBase(Convert.strPercentToDbl(txtPIPBase.getText()));
			Config.setPipCoef(Convert.strPercentToDbl(txtPIPCoef.getText()));
			Config.setPriceIncrType(rbArithmetic.isSelected() ? PriceIncrType.ARITHMETIC : PriceIncrType.GEOMETRIC);
			Config.setQtyIncrType(rbOverLastOrder.isSelected() ? QtyIncrType.ORDER : QtyIncrType.POSITION);
			Config.setPriceIncr(Convert.strPercentToDbl(txtPriceIncr.getText()));
			Config.setQtyIncr(Convert.strPercentToDbl(txtCoinsIncr.getText()));
			Config.setStopLoss(Convert.strPercentToDbl(txtStopLoss.getText()));
			Config.setTakeProfit(Convert.strPercentToDbl(txtTProfit.getText()));

			if (rbQtyUsd.isSelected())
			{
				Config.setQuantityType(QuantityType.USD);
				Config.setInQty(txtInQtyUsd.getText());
			}
			else
			{
				Config.setQuantityType(QuantityType.BALANCE);
				Config.setInQty(Convert.strPercentToDbl(txtInQtyBalance.getText()));
			}

			Config.setLeverage(txtLeverage.getText());
			Config.setPositionsMax(txtPositionsMax.getText());
			Config.setBalanceMinAvailable(Convert.strPercentToDbl(txtBalanceMinAvailable.getText()));

			Config.save();
			ctrlError.INFO("CONFIG SAVED");
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}
}
