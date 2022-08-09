package sanzol.app.forms;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.PrivateConfig;
import sanzol.app.config.Styles;
import sanzol.app.task.LogService;
import sanzol.app.util.Convert;
import sanzol.lib.util.ExceptionUtils;
import javax.swing.JCheckBox;

public class FrmConfig extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmConfig myJFrame = null;

	private JLabel lblError;

	private JButton btnSaveConfig;
	private JButton btnSaveKey;

	private JPanel pnlContent;
	private JPanel pnlContent2;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JRadioButton rbArithmetic;
	private JRadioButton rbGeometric;
	private JRadioButton rbQtyUsd;
	private JRadioButton rbQtyBalance;
	
	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;

	private JTextArea txtFavCoins;
	private JTextField txtBSMinVolume;
	private JTextField txtBSMaxChange24h;
	private JTextField txtBlocksToAnalyzeBB;
	private JTextField txtBlocksToAnalyzeWA;
	private JTextField txtIterations;
	private JTextField txtPriceIncr1;
	private JTextField txtCoinsIncr1;
	private JTextField txtPIF;
	private JTextField txtPriceIncr;
	private JTextField txtCoinsIncr;
	private JTextField txtDistBeforeSL;
	private JTextField txtTProfit;
	private JTextField txtInQtyUsd;
	private JTextField txtInQtyBalance;
	private JTextField txtLeverage;
	private JTextField txtPositionsMax;
	private JTextField txtBalanceMinAvailable;
	private JCheckBox chkPIF;
	
	public FrmConfig()
	{
		initComponents();
		pageload();
	}

	private void initComponents()
	{
		setTitle(Constants.APP_NAME);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmConfig.class.getResource("/resources/wrench.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 860, 600);
		setLocationRelativeTo(null);
		setResizable(false);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_UP);
		pnlContent = new JPanel();
		pnlContent.setBorder(Styles.BORDER_DOWN);
		pnlContent2 = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		lblError = new JLabel();

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 840, Short.MAX_VALUE)
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 46, Short.MAX_VALUE)
		);
		pnlTopBar.setLayout(pnlTopBarLayout);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlContent2, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
				.addComponent(pnlTopBar, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(pnlContent2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
		);
		pnlContent2.setLayout(null);
		
		txtApiKey = new JPasswordField();
		txtApiKey.setBounds(24, 30, 340, 20);
		pnlContent2.add(txtApiKey);
		txtApiKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtApiKey.setColumns(10);
		
		JLabel lblApiKey = new JLabel("Api Key");
		lblApiKey.setBounds(24, 11, 80, 14);
		pnlContent2.add(lblApiKey);
		
		JLabel lblSecretKey = new JLabel("Secret Key");
		lblSecretKey.setBounds(380, 11, 80, 14);
		pnlContent2.add(lblSecretKey);
		
		txtSecretKey = new JPasswordField();
		txtSecretKey.setBounds(380, 30, 340, 20);
		pnlContent2.add(txtSecretKey);
		txtSecretKey.setFont(new Font("Courier New", Font.PLAIN, 8));
		txtSecretKey.setColumns(10);
		
		btnSaveKey = new JButton("SAVE");
		btnSaveKey.setBounds(753, 27, 72, 22);
		pnlContent2.add(btnSaveKey);
		btnSaveKey.setOpaque(true);

		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);
		
		JPanel pnlSymbols = new JPanel();
		pnlSymbols.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Symbols ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSymbols.setBounds(15, 11, 490, 145);
		pnlContent.add(pnlSymbols);
		pnlSymbols.setLayout(null);

		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setBounds(24, 27, 90, 14);
		pnlSymbols.add(lblFavCoins);
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);

		txtFavCoins = new JTextArea();
		txtFavCoins.setLineWrap(true);
		txtFavCoins.setWrapStyleWord(true);

		JScrollPane scroll = new JScrollPane(txtFavCoins, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(UIManager.getBorder("TextField.border"));		
		scroll.setBounds(24, 46, 440, 72);
		pnlSymbols.add(scroll);

		JPanel pnlGrid = new JPanel();
		pnlGrid.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Grid ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGrid.setBounds(15, 174, 490, 145);
		pnlContent.add(pnlGrid);
		pnlGrid.setLayout(null);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(24, 27, 80, 14);
		pnlGrid.add(lblItarations);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);

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

		txtCoinsIncr1 = new JTextField();
		txtCoinsIncr1.setBounds(216, 97, 72, 20);
		txtCoinsIncr1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr1.setColumns(10);
		pnlGrid.add(txtCoinsIncr1);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setBounds(310, 97, 72, 20);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		pnlGrid.add(txtCoinsIncr);

		JLabel lblCoinsIncr1 = new JLabel("1\u00B0 Qty Incr %");
		lblCoinsIncr1.setBounds(216, 78, 80, 14);
		pnlGrid.add(lblCoinsIncr1);

		txtPriceIncr1 = new JTextField();
		txtPriceIncr1.setText("2.0");
		txtPriceIncr1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr1.setColumns(10);
		txtPriceIncr1.setBounds(216, 46, 72, 20);
		pnlGrid.add(txtPriceIncr1);

		JLabel lblPriceIncr_1 = new JLabel("1\u00B0 Price Incr %");
		lblPriceIncr_1.setBounds(216, 27, 80, 14);
		pnlGrid.add(lblPriceIncr_1);

		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setBounds(400, 46, 72, 20);
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		pnlGrid.add(txtDistBeforeSL);

		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(400, 27, 90, 14);
		pnlGrid.add(lblDistSL);

		rbArithmetic = new JRadioButton("Arithmetic");
		rbArithmetic.setBounds(23, 75, 90, 23);
		pnlGrid.add(rbArithmetic);

		rbGeometric = new JRadioButton("Geometric");
		rbGeometric.setBounds(23, 98, 90, 23);
		pnlGrid.add(rbGeometric);

		ButtonGroup bg2 = new javax.swing.ButtonGroup();

		ButtonGroup bg3 = new javax.swing.ButtonGroup();
		bg3.add(rbArithmetic);
		bg3.add(rbGeometric);

		scroll = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		scroll.setBounds(37, -89, 437, 28);
		pnlGrid.add(scroll);
		
		txtPIF = new JTextField();
		txtPIF.setBounds(114, 46, 72, 20);
		pnlGrid.add(txtPIF);
		txtPIF.setText("2.0");
		txtPIF.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPIF.setColumns(10);
		
		chkPIF = new JCheckBox("P.I.F. %");
		chkPIF.setBounds(112, 23, 72, 23);
		pnlGrid.add(chkPIF);

		JPanel pnlOBook = new JPanel();
		pnlOBook.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " O.Book ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlOBook.setBounds(675, 11, 150, 145);
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
		pnlOBook.add(txtBlocksToAnalyzeBB);
		txtBlocksToAnalyzeBB.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBlocksToAnalyzeBB.setColumns(10);

		JPanel panelPositions = new JPanel();
		panelPositions.setBounds(515, 174, 310, 145);
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
		
		rbQtyUsd = new JRadioButton("Usd");
		rbQtyUsd.setBounds(214, 27, 90, 23);
		panelPositions.add(rbQtyUsd);
		bg2.add(rbQtyUsd);
		
		txtInQtyUsd = new JTextField();
		txtInQtyUsd.setBounds(216, 50, 72, 20);
		panelPositions.add(txtInQtyUsd);
		txtInQtyUsd.setText("10.0");
		txtInQtyUsd.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInQtyUsd.setColumns(10);
	
		rbQtyBalance = new JRadioButton("Balance %");
		rbQtyBalance.setBounds(214, 78, 90, 23);
		panelPositions.add(rbQtyBalance);
		bg2.add(rbQtyBalance);

		txtInQtyBalance = new JTextField();
		txtInQtyBalance.setBounds(216, 101, 72, 20);
		panelPositions.add(txtInQtyBalance);
		txtInQtyBalance.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInQtyBalance.setColumns(10);

		JPanel pnlBetterSymbols = new JPanel();
		pnlBetterSymbols.setBounds(515, 11, 150, 145);
		pnlBetterSymbols.setLayout(null);
		pnlBetterSymbols.setBorder(new TitledBorder(UIManager.getBorder("TextField.border"), " Better symbols ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlContent.add(pnlBetterSymbols);

		JLabel lblVolume = new JLabel("Min Volume");
		lblVolume.setBounds(24, 27, 100, 14);
		pnlBetterSymbols.add(lblVolume);

		JLabel lblChange = new JLabel("Max Change 24h");
		lblChange.setBounds(24, 78, 100, 14);
		pnlBetterSymbols.add(lblChange);

		txtBSMaxChange24h = new JTextField();
		txtBSMaxChange24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBSMaxChange24h.setBounds(24, 97, 86, 20);
		pnlBetterSymbols.add(txtBSMaxChange24h);
		txtBSMaxChange24h.setColumns(10);

		txtBSMinVolume = new JTextField();
		txtBSMinVolume.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBSMinVolume.setBounds(24, 46, 86, 20);
		pnlBetterSymbols.add(txtBSMinVolume);
		txtBSMinVolume.setColumns(10);
		btnSaveConfig = new JButton();
		btnSaveConfig.setBounds(753, 332, 72, 22);
		pnlContent.add(btnSaveConfig);
		btnSaveConfig.setText("SAVE");

		// --------------------------------------------------------------------
		GroupLayout pnlStatusBarLayout = new GroupLayout(pnlStatusBar);
		pnlStatusBarLayout.setHorizontalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, pnlStatusBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(16)
					.addComponent(lblError)
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

		txtBSMinVolume.setText(BigDecimal.valueOf(Config.getBetterSymbolsMinVolume()).toPlainString());
		txtBSMaxChange24h.setText(String.valueOf(Config.getBetterSymbolsMaxChange()));

		txtBlocksToAnalyzeBB.setText(String.valueOf(Config.getBlocksToAnalizeBB()));
		txtBlocksToAnalyzeWA.setText(String.valueOf(Config.getBlocksToAnalizeWA()));

		rbArithmetic.setSelected("A".equals(Config.getGridType()));
		rbGeometric.setSelected("G".equals(Config.getGridType()));

		chkPIF.setSelected(false); // TODO
		txtPIF.setText(Convert.dblToStrPercent(Config.getPif()));

		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr1.setText(Convert.dblToStrPercent(Config.getPriceIncrement1()));
		txtCoinsIncr1.setText(Convert.dblToStrPercent(Config.getCoinsIncrement1()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPriceIncrement()));
		txtCoinsIncr.setText(Convert.dblToStrPercent(Config.getCoinsIncrement()));
		txtDistBeforeSL.setText(Convert.dblToStrPercent(Config.getStoplossIncrement()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));

		rbQtyUsd.setSelected("U".equals(Config.getInQtyType()));
		rbQtyBalance.setSelected("B".equals(Config.getInQtyType()));
		txtInQtyUsd.setText(rbQtyUsd.isSelected() ? Config.getInQty().toString() : "");
		txtInQtyBalance.setText(rbQtyBalance.isSelected() ? Convert.dblToStrPercent(Config.getInQty()) : "");

		txtLeverage.setText(String.valueOf(Config.getLeverage()));
		txtPositionsMax.setText(String.valueOf(Config.getPositionsMax()));
		txtBalanceMinAvailable.setText(Convert.dblToStrPercent(Config.getBalanceMinAvailable()));
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
		try
		{
			Config.setFavoriteSymbols(txtFavCoins.getText());
			Config.setBetterSymbolsMinVolume(txtBSMinVolume.getText());
			Config.setBetterSymbolsMaxChange(txtBSMaxChange24h.getText());
			Config.setBlocksToAnalizeBB(txtBlocksToAnalyzeBB.getText());
			Config.setBlocksToAnalizeWA(txtBlocksToAnalyzeWA.getText());

			Config.setIterations(txtIterations.getText());
			Config.setGridType(rbArithmetic.isSelected() ? "A" : "G");
			Config.setPriceIncrement1(Convert.strPercentToDbl(txtPriceIncr1.getText()));
			Config.setCoinsIncrement1(Convert.strPercentToDbl(txtCoinsIncr1.getText()));
			Config.setPif(Convert.strPercentToDbl(txtPIF.getText()));
			Config.setPriceIncrement(Convert.strPercentToDbl(txtPriceIncr.getText()));
			Config.setCoinsIncrement(Convert.strPercentToDbl(txtCoinsIncr.getText()));
			Config.setStoplossIncrement(Convert.strPercentToDbl(txtDistBeforeSL.getText()));
			Config.setTakeprofit(Convert.strPercentToDbl(txtTProfit.getText()));

			if (rbQtyUsd.isSelected())
			{
				Config.setInQtyType("U");
				Config.setInQty(txtInQtyUsd.getText());
			}
			else
			{
				Config.setInQtyType("B");
				Config.setInQty(Convert.strPercentToDbl(txtInQtyBalance.getText()));
			}

			Config.setLeverage(txtLeverage.getText());
			Config.setPositionsMax(txtPositionsMax.getText());
			Config.setBalanceMinAvailable(Convert.strPercentToDbl(txtBalanceMinAvailable.getText()));

			Config.save();
			INFO("CONFIG SAVED");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	@SuppressWarnings("deprecation")
	private void saveKey()
	{
		try
		{
			int resultOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to continue ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (resultOption == 0)
			{
				PrivateConfig.setKey(txtApiKey.getText(), txtSecretKey.getText());
				INFO("KEY SAVED. PLEASE RESTART !!!");
			}
		}
		catch (IOException e)
		{
			ERROR(e);
		}
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
