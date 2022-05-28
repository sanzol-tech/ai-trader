package sanzol.app.forms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
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

public class FrmConfig extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static FrmConfig myJFrame = null;

	private JLabel lblError;

	private JButton btnSaveConfig;
	private JButton btnSaveKey;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JPasswordField txtApiKey;
	private JPasswordField txtSecretKey;
	private JTextField txtBalanceMinAvailable;
	private JTextField txtCoinsIncr;
	private JTextField txtDistBeforeSL;
	private JTextField txtFavCoins;
	private JTextField txtIterations;
	private JTextField txtLeverage;
	private JTextField txtPositionQty;
	private JTextField txtPositionQtyMax;
	private JTextField txtPositionsMax;
	private JTextField txtPriceIncr;
	private JTextField txtTProfit;
	private JTextField txtBSMinVolume;
	private JTextField txtBSMaxChange24h;
	private JTextField txtWAMaxAccum;
	private JTextField txtWAMaxDist;
	
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
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);
		btnSaveConfig = new JButton();
		btnSaveConfig.setText("SAVE");

		lblError = new JLabel();
		
		JPanel panelPositions = new JPanel();
		panelPositions.setLayout(null);
		panelPositions.setBorder(new TitledBorder(null, " Balance / Positions ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelPositions.setBounds(425, 145, 400, 128);
		pnlContent.add(panelPositions);
		
		JLabel lblQty = new JLabel("Min qty %");
		lblQty.setBounds(117, 26, 80, 14);
		panelPositions.add(lblQty);
		
		txtPositionQty = new JTextField();
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(117, 43, 72, 20);
		panelPositions.add(txtPositionQty);
		
		txtLeverage = new JTextField();
		txtLeverage.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLeverage.setEditable(false);
		txtLeverage.setColumns(10);
		txtLeverage.setBounds(209, 43, 72, 20);
		panelPositions.add(txtLeverage);
		
		txtBalanceMinAvailable = new JTextField();
		txtBalanceMinAvailable.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalanceMinAvailable.setColumns(10);
		txtBalanceMinAvailable.setBounds(25, 86, 72, 20);
		panelPositions.add(txtBalanceMinAvailable);
		
		JLabel lblLeverage = new JLabel("Leverage");
		lblLeverage.setBounds(209, 26, 80, 14);
		panelPositions.add(lblLeverage);
		
		JLabel lblAvailable = new JLabel("Min balance %");
		lblAvailable.setBounds(25, 70, 80, 14);
		panelPositions.add(lblAvailable);
		
		JLabel lblPositionsMax = new JLabel("Max positions");
		lblPositionsMax.setBounds(25, 26, 80, 14);
		panelPositions.add(lblPositionsMax);
		
		JLabel lblQtyMax = new JLabel("Max qty %");
		lblQtyMax.setBounds(117, 70, 80, 14);
		panelPositions.add(lblQtyMax);
		
		txtPositionsMax = new JTextField();
		txtPositionsMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionsMax.setColumns(10);
		txtPositionsMax.setBounds(25, 43, 72, 20);
		panelPositions.add(txtPositionsMax);

		txtPositionQtyMax = new JTextField();
		txtPositionQtyMax.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQtyMax.setColumns(10);
		txtPositionQtyMax.setBounds(117, 86, 72, 20);
		panelPositions.add(txtPositionQtyMax);

		JPanel panelKey = new JPanel();
		panelKey.setLayout(null);
		panelKey.setBorder(UIManager.getBorder("TextField.border"));
		panelKey.setBounds(15, 304, 810, 60);
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
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, pnlTopBarLayout.createSequentialGroup()
					.addContainerGap(773, Short.MAX_VALUE)
					.addComponent(btnSaveConfig)
					.addContainerGap())
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(btnSaveConfig)
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
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		pnlContent.setLayout(null);
		
		JPanel pnlSymbols = new JPanel();
		pnlSymbols.setBorder(new TitledBorder(null, " Symbols ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSymbols.setBounds(15, 11, 400, 149);
		pnlContent.add(pnlSymbols);
		pnlSymbols.setLayout(null);
				
		txtBSMinVolume = new JTextField();
		txtBSMinVolume.setEditable(false);
		txtBSMinVolume.setBounds(24, 111, 86, 20);
		pnlSymbols.add(txtBSMinVolume);
		txtBSMinVolume.setColumns(10);
		
		txtBSMaxChange24h = new JTextField();
		txtBSMaxChange24h.setEditable(false);
		txtBSMaxChange24h.setBounds(113, 111, 86, 20);
		pnlSymbols.add(txtBSMaxChange24h);
		txtBSMaxChange24h.setColumns(10);
		
		JLabel lblVolume = new JLabel("Min Volume");
		lblVolume.setBounds(24, 94, 86, 14);
		pnlSymbols.add(lblVolume);
		
		JLabel lblChange = new JLabel("Max Change 24h");
		lblChange.setBounds(113, 94, 100, 14);
		pnlSymbols.add(lblChange);
		
		JLabel lblNewLabel = new JLabel("Better symbols");
		lblNewLabel.setBounds(24, 75, 175, 14);
		pnlSymbols.add(lblNewLabel);
		
		JLabel lblFavCoins = new JLabel("Favorite coins");
		lblFavCoins.setBounds(24, 27, 90, 14);
		pnlSymbols.add(lblFavCoins);
		lblFavCoins.setHorizontalAlignment(SwingConstants.LEFT);
		
		txtFavCoins = new JTextField();
		txtFavCoins.setBounds(24, 44, 350, 20);
		pnlSymbols.add(txtFavCoins);
		txtFavCoins.setColumns(10);

		JPanel pnlGrid = new JPanel();
		pnlGrid.setBorder(new TitledBorder(null, " Grid ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGrid.setBounds(425, 11, 400, 123);
		pnlContent.add(pnlGrid);
		pnlGrid.setLayout(null);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(25, 21, 80, 14);
		pnlGrid.add(lblItarations);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		
		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(117, 21, 80, 14);
		pnlGrid.add(lblPriceIncr);
		
		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setBounds(209, 21, 80, 14);
		pnlGrid.add(lblTProfit);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		
		txtTProfit = new JTextField();
		txtTProfit.setBounds(209, 38, 72, 20);
		pnlGrid.add(txtTProfit);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
	
		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(117, 38, 72, 20);
		pnlGrid.add(txtPriceIncr);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);

		txtIterations = new JTextField();
		txtIterations.setBounds(25, 38, 72, 20);
		pnlGrid.add(txtIterations);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
								
		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(25, 65, 80, 14);
		pnlGrid.add(lblCoinsIncr);
		
		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setBounds(25, 81, 72, 20);
		pnlGrid.add(txtCoinsIncr);
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		
		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(117, 65, 90, 14);
		pnlGrid.add(lblDistSL);
		
		txtDistBeforeSL = new JTextField();
		txtDistBeforeSL.setBounds(117, 81, 72, 20);
		pnlGrid.add(txtDistBeforeSL);
		txtDistBeforeSL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtDistBeforeSL.setColumns(10);
		
		JPanel pnlOBook = new JPanel();
		pnlOBook.setBorder(new TitledBorder(null, " O.Book ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlOBook.setBounds(15, 171, 400, 101);
		pnlContent.add(pnlOBook);
		pnlOBook.setLayout(null);
		
		txtWAMaxAccum = new JTextField();
		txtWAMaxAccum.setEditable(false);
		txtWAMaxAccum.setColumns(10);
		txtWAMaxAccum.setBounds(22, 63, 86, 20);
		pnlOBook.add(txtWAMaxAccum);
		
		JLabel lblSum = new JLabel("Max accum %");
		lblSum.setBounds(22, 46, 86, 14);
		pnlOBook.add(lblSum);
		
		JLabel lblDistance = new JLabel("Max dist %");
		lblDistance.setBounds(111, 46, 86, 14);
		pnlOBook.add(lblDistance);
		
		txtWAMaxDist = new JTextField();
		txtWAMaxDist.setEditable(false);
		txtWAMaxDist.setColumns(10);
		txtWAMaxDist.setBounds(111, 63, 86, 20);
		pnlOBook.add(txtWAMaxDist);
		
		JLabel lblWeightedAverage = new JLabel("Weighted average");
		lblWeightedAverage.setBounds(22, 26, 173, 14);
		pnlOBook.add(lblWeightedAverage);
		
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
		txtBSMinVolume.setText(BigDecimal.valueOf(Config.BETTER_SYMBOLS_MIN_VOLUME).toPlainString());
		txtBSMaxChange24h.setText(String.valueOf(Config.BETTER_SYMBOLS_MAX_CHANGE));

		txtWAMaxAccum.setText(String.valueOf(Config.WEIGHTED_AVERAGE_MAX_ACCUM));
		txtWAMaxDist.setText(String.valueOf(Config.WEIGHTED_AVERAGE_MAX_DIST));
		
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
	}

	public static void launch()
	{
		if (myJFrame != null)
		{
			myJFrame.toFront();
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
