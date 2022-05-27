package sanzol.app.forms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.decimal4j.util.DoubleRounder;

import com.binance.client.model.enums.PositionSide;
import com.binance.client.model.trade.AccountBalance;
import com.binance.client.model.trade.PositionRisk;

import sanzol.app.config.Application;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.listener.PositionListener;
import sanzol.app.listener.PriceListener;
import sanzol.app.model.Position;
import sanzol.app.model.PriceQty;
import sanzol.app.service.PositionTrader;
import sanzol.app.service.PositionTrader.PostStyle;
import sanzol.app.service.Symbol;
import sanzol.app.task.BalanceService;
import sanzol.app.task.PositionService;
import sanzol.app.task.PriceService;
import sanzol.app.util.Convert;
import sanzol.lib.util.ExceptionUtils;

public class FrmGrid extends JFrame implements PriceListener, PositionListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol symbol;
	private PositionTrader pMaker;
	private boolean isBotMode = false;
	private boolean isOpenPosition = false;

	private JPanel contentPane;
	private JPanel pnlConfig;
	private JPanel pnlPosition;

	private JButton btnClean;
	private JButton btnLong;
	private JButton btnPostFirst;
	private JButton btnPostOthers;
	private JButton btnSearch;
	private JButton btnShort;

	private JLabel lblPrice;
	private JLabel lblQty;

	private JLabel lnkBtnExtreme;
	private JLabel lnkBtnStrong;
	private JLabel lnkBtnNormal;
	private JLabel lnkBtnEternal;
	private JLabel lnkBtnLarge;
	private JLabel lnkBtnToy;

	private JRadioButton rbPriceLimit;
	private JRadioButton rbPriceMark;
	private JRadioButton rbQty;
	private JRadioButton rbQtyBalance;

	private JTextArea txtResult;

	private JTextField txtGPrice1;
	private JTextField txtGPrice2;
	private JTextField txtGPrice3;
	private JTextField txtGPrice4;
	private JTextField txtGPrice5;
	private JTextField txtGPrice6;
	private JTextField txtGPrice7;
	private JTextField txtGPrice8;

	private JTextField txtGQty1;
	private JTextField txtGQty2;
	private JTextField txtGQty3;
	private JTextField txtGQty4;
	private JTextField txtGQty5;
	private JTextField txtGQty6;
	private JTextField txtGQty7;
	private JTextField txtGQty8;

	private JTextField txtError;
	private JTextField txtBalancePercent;
	private JTextField txtIterations;
	private JTextField txtMarkPrice;
	private JTextField txtPositionPrice;
	private JTextField txtPositionQty;
	private JTextField txtPrice;
	private JTextField txtPriceIncr;
	private JTextField txtPriceShow;
	private JTextField txtQty;
	private JTextField txtQtyIncr;
	private JTextField txtSLossDist;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtTProfit;

	public FrmGrid()
	{
		initComponents();
		loadConfig();
		PriceService.attachRefreshObserver(this);
		PositionService.attachRefreshObserver(this);
	}

	private void initComponents() 
	{
		setType(Type.POPUP);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1152, 640);
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/upDown.png")));
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		btnLong = new JButton("LONG");
		btnLong.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLong.setBackground(Styles.COLOR_BTN_LONG);
		btnLong.setOpaque(true);
		btnLong.setBounds(1015, 116, 105, 32);
		contentPane.add(btnLong);

		btnClean = new JButton("CLEAN");
		btnClean.setOpaque(true);
		btnClean.setBounds(1040, 561, 78, 31);
		contentPane.add(btnClean);

		txtPrice = new JTextField();
		txtPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPrice.setBounds(368, 28, 90, 22);
		contentPane.add(txtPrice);
		txtPrice.setColumns(10);

		txtQty = new JTextField();
		txtQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQty.setBounds(586, 28, 90, 22);
		contentPane.add(txtQty);
		txtQty.setColumns(10);

		rbPriceMark = new JRadioButton("MARK");
		rbPriceMark.setBounds(292, 58, 68, 23);
		contentPane.add(rbPriceMark);

		rbPriceLimit = new JRadioButton("PRICE");
		rbPriceLimit.setSelected(true);
		rbPriceLimit.setBounds(292, 28, 68, 23);
		contentPane.add(rbPriceLimit);

		rbQtyBalance = new JRadioButton("BALANCE %");
		rbQtyBalance.setSelected(true);
		rbQtyBalance.setBounds(480, 58, 100, 23);
		contentPane.add(rbQtyBalance);

		rbQty = new JRadioButton("COINS QTY");
		rbQty.setBounds(480, 28, 100, 23);
		contentPane.add(rbQty);

		ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbPriceMark);
		bg1.add(rbPriceLimit);

		ButtonGroup bg2 = new javax.swing.ButtonGroup();
		bg2.add(rbQtyBalance);
		bg2.add(rbQty);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setBounds(132, 28, 104, 22);
		contentPane.add(txtSymbolRight);
		txtSymbolRight.setColumns(10);
		
		lnkBtnExtreme = new JLabel("Extreme");
		lnkBtnExtreme.setBounds(228, 100, 60, 14);
		lnkBtnExtreme.setForeground(Styles.COLOR_LINK);
		lnkBtnExtreme.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnExtreme);

		lnkBtnStrong = new JLabel("Strong");
		lnkBtnStrong.setBounds(298, 100, 60, 14);
		lnkBtnStrong.setForeground(Styles.COLOR_LINK);
		lnkBtnStrong.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnStrong);
		
		lnkBtnNormal = new JLabel("Normal");
		lnkBtnNormal.setBounds(368, 100, 60, 14);
		lnkBtnNormal.setForeground(Styles.COLOR_LINK);
		lnkBtnNormal.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnNormal);
		
		lnkBtnLarge = new JLabel("Large");
		lnkBtnLarge.setBounds(438, 100, 60, 14);
		lnkBtnLarge.setForeground(Styles.COLOR_LINK);
		lnkBtnLarge.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnLarge);

		lnkBtnEternal = new JLabel("Eternal");
		lnkBtnEternal.setBounds(508, 100, 60, 14);
		lnkBtnEternal.setForeground(Styles.COLOR_LINK);
		lnkBtnEternal.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnEternal);
	
		lnkBtnToy = new JLabel("Toy");
		lnkBtnToy.setForeground(Styles.COLOR_LINK);
		lnkBtnToy.setBounds(578, 100, 50, 14);
		lnkBtnToy.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(lnkBtnToy);
		
		txtResult = new JTextArea();
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));

		JScrollPane scroll = new JScrollPane(txtResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(16, 218, 1102, 330);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scroll);

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setOpaque(true);
		btnSearch.setBounds(15, 59, 106, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setBounds(15, 28, 106, 22);
		contentPane.add(txtSymbolLeft);
		txtSymbolLeft.setColumns(10);

		txtBalancePercent = new JTextField();
		txtBalancePercent.setHorizontalAlignment(SwingConstants.RIGHT);
		txtBalancePercent.setColumns(10);
		txtBalancePercent.setBounds(586, 58, 90, 22);
		contentPane.add(txtBalancePercent);

		txtError = new JTextField();
		txtError.setHorizontalAlignment(SwingConstants.RIGHT);
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setBounds(16, 570, 998, 20);
		contentPane.add(txtError);
		txtError.setColumns(10);

		btnPostFirst = new JButton("First");
		btnPostFirst.setIcon(Styles.IMAGE_EXECUTE);
		btnPostFirst.setEnabled(false);
		btnPostFirst.setToolTipText("Post only first order");
		btnPostFirst.setOpaque(true);
		btnPostFirst.setBounds(900, 164, 105, 32);
		contentPane.add(btnPostFirst);

		btnPostOthers = new JButton("Others");
		btnPostOthers.setIcon(Styles.IMAGE_EXECUTE);
		btnPostOthers.setEnabled(false);
		btnPostOthers.setToolTipText("Post the other orders");
		btnPostOthers.setOpaque(true);
		btnPostOthers.setBounds(1015, 164, 105, 32);
		contentPane.add(btnPostOthers);

		txtPriceShow = new JTextField();
		txtPriceShow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtPriceShow.setHorizontalAlignment(SwingConstants.CENTER);
		txtPriceShow.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtPriceShow.setEditable(false);
		txtPriceShow.setColumns(10);
		txtPriceShow.setBounds(132, 60, 104, 22);
		contentPane.add(txtPriceShow);

		JLabel lblNewLabel = new JLabel("C O I N");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setBounds(16, 10, 203, 14);
		contentPane.add(lblNewLabel);
		
		txtMarkPrice = new JTextField();
		txtMarkPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMarkPrice.setForeground((Color) null);
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(368, 58, 90, 22);
		contentPane.add(txtMarkPrice);
		
		JPanel pndGrid = new JPanel();
		pndGrid.setBorder(UIManager.getBorder("TextField.border"));
		pndGrid.setBounds(228, 116, 642, 80);
		contentPane.add(pndGrid);
		pndGrid.setLayout(null);
		
		JLabel lblGrdPrice = new JLabel("Price %");
		lblGrdPrice.setBounds(20, 20, 53, 14);
		pndGrid.add(lblGrdPrice);
		
		JLabel lblGrdQty = new JLabel("Qty %");
		lblGrdQty.setBounds(20, 47, 53, 14);
		pndGrid.add(lblGrdQty);
		
		txtGPrice1 = new JTextField();
		txtGPrice1.setBounds(83, 18, 60, 20);
		txtGPrice1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice1.setColumns(10);
		pndGrid.add(txtGPrice1);
		
		txtGPrice2 = new JTextField();
		txtGPrice2.setBounds(151, 18, 60, 20);
		txtGPrice2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice2.setColumns(10);
		pndGrid.add(txtGPrice2);
		
		txtGQty2 = new JTextField();
		txtGQty2.setBounds(151, 46, 60, 20);
		txtGQty2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty2.setColumns(10);
		pndGrid.add(txtGQty2);
		
		txtGQty1 = new JTextField();
		txtGQty1.setBounds(83, 46, 60, 20);
		txtGQty1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty1.setColumns(10);
		pndGrid.add(txtGQty1);
		
		txtGPrice3 = new JTextField();
		txtGPrice3.setBounds(219, 18, 60, 20);
		txtGPrice3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice3.setColumns(10);
		pndGrid.add(txtGPrice3);
		
		txtGQty3 = new JTextField();
		txtGQty3.setBounds(219, 46, 60, 20);
		txtGQty3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty3.setColumns(10);
		pndGrid.add(txtGQty3);
		
		txtGPrice4 = new JTextField();
		txtGPrice4.setBounds(287, 18, 60, 20);
		txtGPrice4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice4.setColumns(10);
		pndGrid.add(txtGPrice4);
		
		txtGQty4 = new JTextField();
		txtGQty4.setBounds(287, 46, 60, 20);
		txtGQty4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty4.setColumns(10);
		pndGrid.add(txtGQty4);
		
		txtGPrice5 = new JTextField();
		txtGPrice5.setBounds(355, 18, 60, 20);
		txtGPrice5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice5.setColumns(10);
		pndGrid.add(txtGPrice5);
		
		txtGQty5 = new JTextField();
		txtGQty5.setBounds(355, 46, 60, 20);
		txtGQty5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty5.setColumns(10);
		pndGrid.add(txtGQty5);
		
		txtGPrice6 = new JTextField();
		txtGPrice6.setBounds(423, 18, 60, 20);
		txtGPrice6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice6.setColumns(10);
		pndGrid.add(txtGPrice6);
		
		txtGQty6 = new JTextField();
		txtGQty6.setBounds(423, 46, 60, 20);
		txtGQty6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty6.setColumns(10);
		pndGrid.add(txtGQty6);
		
		txtGPrice7 = new JTextField();
		txtGPrice7.setBounds(491, 18, 60, 20);
		txtGPrice7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice7.setColumns(10);
		pndGrid.add(txtGPrice7);
		
		txtGQty7 = new JTextField();
		txtGQty7.setBounds(491, 46, 60, 20);
		txtGQty7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty7.setColumns(10);
		pndGrid.add(txtGQty7);
		
		txtGPrice8 = new JTextField();
		txtGPrice8.setBounds(559, 18, 60, 20);
		txtGPrice8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice8.setColumns(10);
		pndGrid.add(txtGPrice8);
		
		txtGQty8 = new JTextField();
		txtGQty8.setBounds(559, 46, 60, 20);
		txtGQty8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty8.setColumns(10);
		pndGrid.add(txtGQty8);

		btnShort = new JButton("SHORT");
		btnShort.setBounds(900, 116, 105, 32);
		btnShort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShort.setBackground(Styles.COLOR_BTN_SHORT);
		btnShort.setOpaque(true);
		contentPane.add(btnShort);

		pnlConfig = new JPanel();
		pnlConfig.setBorder(UIManager.getBorder("TextField.border"));
		pnlConfig.setBounds(714, 28, 404, 73);
		pnlConfig.setLayout(null);
		contentPane.add(pnlConfig);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setBounds(20, 12, 60, 14);
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblItarations);

		txtIterations = new JTextField();
		txtIterations.setBounds(20, 35, 60, 20);
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		pnlConfig.add(txtIterations);
		
		JLabel lblPriceIncr = new JLabel("Price %");
		lblPriceIncr.setBounds(96, 11, 60, 14);
		lblPriceIncr.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblPriceIncr);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setBounds(96, 35, 60, 20);
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		pnlConfig.add(txtPriceIncr);

		txtQtyIncr = new JTextField();
		txtQtyIncr.setBounds(172, 35, 60, 20);
		txtQtyIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtQtyIncr.setColumns(10);
		pnlConfig.add(txtQtyIncr);

		JLabel lblQtyIncr = new JLabel("Qty %");
		lblQtyIncr.setBounds(172, 11, 60, 14);
		lblQtyIncr.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblQtyIncr);

		JLabel lblDistSL = new JLabel("SL %");
		lblDistSL.setBounds(248, 12, 60, 14);
		pnlConfig.add(lblDistSL);

		txtSLossDist = new JTextField();
		txtSLossDist.setBounds(248, 35, 60, 20);
		txtSLossDist.setHorizontalAlignment(SwingConstants.RIGHT);
		txtSLossDist.setColumns(10);
		pnlConfig.add(txtSLossDist);

		JLabel lblTProfit = new JLabel("TP %");
		lblTProfit.setBounds(324, 12, 60, 14);
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		pnlConfig.add(lblTProfit);

		txtTProfit = new JTextField();
		txtTProfit.setBounds(324, 35, 60, 20);
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		pnlConfig.add(txtTProfit);
		
		pnlPosition = new JPanel();
		pnlPosition.setLayout(null);
		pnlPosition.setBorder(new TitledBorder(null, " Position ", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlPosition.setBounds(16, 107, 190, 91);
		contentPane.add(pnlPosition);

		lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setBounds(21, 26, 45, 14);
		pnlPosition.add(lblPrice);

		txtPositionPrice = new JTextField();
		txtPositionPrice.setEditable(false);
		txtPositionPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionPrice.setColumns(10);
		txtPositionPrice.setBounds(76, 26, 92, 20);
		pnlPosition.add(txtPositionPrice);

		lblQty = new JLabel("Qty");
		lblQty.setHorizontalAlignment(SwingConstants.LEFT);
		lblQty.setBounds(21, 54, 45, 14);
		pnlPosition.add(lblQty);

		txtPositionQty = new JTextField();
		txtPositionQty.setEditable(false);
		txtPositionQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPositionQty.setColumns(10);
		txtPositionQty.setBounds(76, 54, 92, 20);
		pnlPosition.add(txtPositionQty);
		
		// --------------------------------------------------------------------

		FrmGrid thisFrm = this;

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PriceService.deattachRefreshObserver(thisFrm);
				PositionService.deattachRefreshObserver(thisFrm);
			}
		});

		rbPriceMark.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtPrice.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					txtPrice.setEnabled(true);
				}
			}
		});
		rbQtyBalance.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					txtQty.setEnabled(false);
				}
				else if (e.getStateChange() == ItemEvent.DESELECTED) {
					txtQty.setEnabled(true);
				}
			}
		});

		txtIterations.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		txtPriceIncr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		txtQtyIncr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				generateGrid();
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnShort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createShort();
			}
		});
		
		btnLong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createLong();
			}
		});

		btnPostFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resultOption = JOptionPane.showConfirmDialog(null, "Do you like post this position ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (resultOption == 0)
				{
					post(PostStyle.FIRST);
				}
			}
		});

		btnPostOthers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int resultOption = JOptionPane.showConfirmDialog(null, "Do you like post orders ?", "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (resultOption == 0)
				{
					post(PostStyle.OTHERS);
				}
			}
		});

		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});

		lnkBtnExtreme.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(1);
			}
		});
		lnkBtnStrong.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(2);
			}
		});
		lnkBtnNormal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(3);
			}
		});
		lnkBtnLarge.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(4);
			}
		});
		lnkBtnEternal.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(5);
			}
		});		
		lnkBtnToy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(6);
			}
		});		

	}
	
	private void loadGridTemplate(int templaneNro)
	{
		try
		{
			if (templaneNro == 1)
			{
				txtBalancePercent.setText("1");
				txtIterations.setText("5");
				txtPriceIncr.setText("1.8");
				txtSLossDist.setText("10");
				txtQtyIncr.setText("125");
			}
			else if (templaneNro == 2)
			{
				txtBalancePercent.setText("3");
				txtIterations.setText("6");
				txtPriceIncr.setText("2");
				txtSLossDist.setText("10");
				txtQtyIncr.setText("100");
			}
			else if (templaneNro == 3)
			{
				txtBalancePercent.setText("3");
				txtIterations.setText("5");
				txtPriceIncr.setText("2");
				txtSLossDist.setText("10");
				txtQtyIncr.setText("40");
			}
			else if (templaneNro == 4)
			{
				txtBalancePercent.setText("2");
				txtIterations.setText("7");
				txtPriceIncr.setText("3");
				txtSLossDist.setText("10");
				txtQtyIncr.setText("40");
			}
			else if (templaneNro == 5)
			{
				txtBalancePercent.setText("1");
				txtIterations.setText("8");
				txtPriceIncr.setText("5");
				txtSLossDist.setText("10");
				txtQtyIncr.setText("40");
			}
			else if (templaneNro == 6)
			{
				txtBalancePercent.setText("1");
				txtIterations.setText("3");
				txtPriceIncr.setText("2");
				txtSLossDist.setText("1.5");
				txtQtyIncr.setText("40");
			}
			else
			{
				return;
			}

			generateGrid();
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void loadConfig()
	{
		txtIterations.setText(String.valueOf(Config.getIterations()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPriceIncrement())); 
		txtQtyIncr.setText(Convert.dblToStrPercent(Config.getCoinsIncrement()));
		txtSLossDist.setText(Convert.dblToStrPercent(Config.getStoplossIncrement()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));
		txtBalancePercent.setText(Convert.dblToStrPercent(Config.getPositionStartQty()));

		generateGrid();
	}

	public static void launch()
	{
		launch(null, null, null, false);
	}

	public static void launch(String symbolLeft, String side, String price, boolean isBotMode)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				FrmGrid frame = null;

				try
				{
					frame = new FrmGrid();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}

				try
				{
					if (symbolLeft != null && !symbolLeft.isEmpty())
					{
						frame.txtSymbolLeft.setText(symbolLeft);
						frame.search();
					}
					if (side != null && price != null)
					{
						frame.txtPrice.setText(price);
						if ("SHORT".equals(side))
							frame.createShort();
						else
							frame.createLong();
					}

					frame.isBotMode = isBotMode;
				}
				catch (Exception e)
				{
					frame.ERROR(e);
				}
			}
		});
	}

	// ------------------------------------------------------------------------

	@Override
	public void onPriceUpdate()
	{
		try
		{
			if (symbol != null)
			{
				BigDecimal price = PriceService.getLastPrice(symbol);
				txtPriceShow.setText(symbol.priceToStr(price));
				txtMarkPrice.setText(symbol.priceToStr(price));
				
				searchPosition();
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	@Override
	public void onPositionUpdate()
	{
		try
		{
			if (symbol != null)
			{
				searchPosition();
				autoPostOthers();
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}	

	// ------------------------------------------------------------------------

	private void search()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbolLeft = txtSymbolLeft.getText();
			symbol = Symbol.getInstance(Symbol.getFullSymbol(symbolLeft));

			if (symbol != null)
			{
				setTitle(TITLE + " - " + symbol.getNameLeft());

				BigDecimal price = PriceService.getLastPrice(symbol);
				txtPriceShow.setText(symbol.priceToStr(price));
				txtMarkPrice.setText(symbol.priceToStr(price));
				
				searchPosition();
			}
			else
			{
				ERROR("Symbol not found");
			}

		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void searchPosition()
	{
		PositionRisk positionRisk = PositionService.getPositionRisk(symbol.getName());
		isOpenPosition = (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0);
		if (isOpenPosition)
		{
			txtPositionPrice.setText(symbol.priceToStr(positionRisk.getEntryPrice()));
			double amt = positionRisk.getPositionAmt().doubleValue();
			txtPositionQty.setText(symbol.qtyToStr(amt));

			if (amt > 0)
				btnShort.setEnabled(false);
			if (amt < 0)
				btnLong.setEnabled(false);

			rbPriceLimit.setEnabled(false);
			rbPriceMark.setEnabled(false);
			txtPrice.setEnabled(false);
			txtMarkPrice.setEnabled(false);
			rbQty.setEnabled(false);
			rbQtyBalance.setEnabled(false);
			txtQty.setEnabled(false);
			txtBalancePercent.setEnabled(false);
		}
		else
		{
			btnShort.setEnabled(true);
			btnLong.setEnabled(true);

			rbPriceLimit.setEnabled(true);
			rbPriceMark.setEnabled(true);
			txtPrice.setEnabled(true);
			txtMarkPrice.setEnabled(true);
			rbQty.setEnabled(true);
			rbQtyBalance.setEnabled(true);
			txtQty.setEnabled(true);
			txtBalancePercent.setEnabled(true);
		}
	}

	private void autoPostOthers()
	{
		if (isBotMode)
		{
			if (!PositionService.existsPosition(symbol.getName()))
			{
				post(PostStyle.FIRST);
			}
			else
			{
				/*
				PositionRisk positionRisk = PositionService.getPositionRisk(symbol.getName());
				double inQty = pMaker.getPosition().getInQty();
				double posQty = positionRisk.getPositionAmt().doubleValue();
				if (posQty == inQty)
				{
					post(PostStyle.OTHERS);
				}
				*/
			}
		}
	}

	// ------------------------------------------------------------------------

	private void generateGrid()
	{
		int iterations = Integer.valueOf(txtIterations.getText());
		double priceIncr = Convert.strPercentToDbl(txtPriceIncr.getText());
		double qtyIncr = Convert.strPercentToDbl(txtQtyIncr.getText());

		if (iterations >= 1)
		{
			txtGPrice1.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty1.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice1.setText("");
			txtGQty1.setText("");
		}
		if (iterations >= 2)
		{
			txtGPrice2.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty2.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice2.setText("");
			txtGQty2.setText("");
		}
		if (iterations >= 3)
		{
			txtGPrice3.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty3.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice3.setText("");
			txtGQty3.setText("");
		}
		if (iterations >= 4)
		{
			txtGPrice4.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty4.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice4.setText("");
			txtGQty4.setText("");
		}
		if (iterations >= 5)
		{
			txtGPrice5.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty5.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice5.setText("");
			txtGQty5.setText("");
		}
		if (iterations >= 6)
		{
			txtGPrice6.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty6.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice6.setText("");
			txtGQty6.setText("");
		}
		if (iterations >= 7)
		{
			txtGPrice7.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty7.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice7.setText("");
			txtGQty7.setText("");
		}
		if (iterations >= 8)
		{
			txtGPrice8.setText(Convert.dblToStrPercent(priceIncr));
			txtGQty8.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice8.setText("");
			txtGQty8.setText("");
		}
	}
	
	private List<PriceQty> acquireGrid()
	{
		List<PriceQty> lstPriceQty = new ArrayList<PriceQty>();
		
		if (isNullOrEmpty(txtGPrice1.getText()) || isNullOrEmpty(txtGQty1.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice1.getText()),
			Convert.strPercentToDbl(txtGQty1.getText())));

		if (isNullOrEmpty(txtGPrice2.getText()) || isNullOrEmpty(txtGQty2.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice2.getText()),
			Convert.strPercentToDbl(txtGQty2.getText())));

		if (isNullOrEmpty(txtGPrice3.getText()) || isNullOrEmpty(txtGQty3.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice3.getText()),
			Convert.strPercentToDbl(txtGQty3.getText())));
		
		if (isNullOrEmpty(txtGPrice4.getText()) || isNullOrEmpty(txtGQty4.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice4.getText()),
			Convert.strPercentToDbl(txtGQty4.getText())));

		if (isNullOrEmpty(txtGPrice5.getText()) || isNullOrEmpty(txtGQty5.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice5.getText()),
			Convert.strPercentToDbl(txtGQty5.getText())));

		if (isNullOrEmpty(txtGPrice6.getText()) || isNullOrEmpty(txtGQty6.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice6.getText()),
			Convert.strPercentToDbl(txtGQty6.getText())));

		if (isNullOrEmpty(txtGPrice7.getText()) || isNullOrEmpty(txtGQty7.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice7.getText()),
			Convert.strPercentToDbl(txtGQty7.getText())));

		if (isNullOrEmpty(txtGPrice8.getText()) || isNullOrEmpty(txtGQty8.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice8.getText()),
			Convert.strPercentToDbl(txtGQty8.getText())));
		
		return lstPriceQty;
	}

	private Position acquireArguments(PositionSide side) throws Exception
	{
		Double distBeforeSL = Convert.strPercentToDbl(txtSLossDist.getText());
		Double tProfit = Convert.strPercentToDbl(txtTProfit.getText());
		List<PriceQty> lstPriceQty = acquireGrid();

		Position position = new Position(symbol, side, distBeforeSL, tProfit, lstPriceQty);

		Double inPrice = getInPrice();
		position.setInPrice(inPrice);
		position.setMarkPrice(rbPriceMark.isSelected());

		Double inQty = getInQty(inPrice);
		position.setInQty(inQty);

		return position;
	}

	private Double getInPrice() throws Exception
	{
		if (isOpenPosition)
		{
			double price = Double.valueOf(txtPositionPrice.getText());
			return price;
		}
		else if (rbPriceLimit.isSelected())
		{
			if (NumberUtils.isCreatable(txtPrice.getText()))
			{
				double price = Double.valueOf(txtPrice.getText());
				return price;
			}
		}
		else
		{
			BigDecimal lastPrice = PriceService.getLastPrice(symbol);
			return lastPrice.doubleValue();
		}

		ERROR("Invalid price");
		return null;
	}

	private Double getInQty(double inPrice)
	{
		if (isOpenPosition)
		{
			double qty = Math.abs(Double.valueOf(txtPositionQty.getText()));
			return qty;
		}
		if (rbQty.isSelected())
		{
			if (NumberUtils.isCreatable(txtQty.getText()))
			{
				double qty = Double.valueOf(txtQty.getText());
				return qty;
			}
		}
		else
		{
			double balancePercent = Convert.strPercentToDbl(txtBalancePercent.getText());
			AccountBalance accBalance = BalanceService.getAccountBalance();
			double balance = accBalance.getBalance().doubleValue();
			return DoubleRounder.round(Math.max(5, balance * balancePercent) / inPrice, symbol.getQuantityPrecision(), RoundingMode.CEILING);
		}

		ERROR("Invalid quantity");
		return null;
	}

	private static boolean isNullOrEmpty(String txt)
	{
		return (txt == null || txt.isEmpty());
	}

	// ------------------------------------------------------------------------

	private void createShort()
	{
		if (symbol == null)
		{
			ERROR("Select symbol");
			return;
		}

		INFO("");
		try
		{
			Position position = acquireArguments(PositionSide.SHORT);
			pMaker = new PositionTrader(position);
			pMaker.createShort();

			//-----------------------------------------------------------------
			txtResult.setForeground(Styles.COLOR_TEXT_SHORT);
			txtResult.setText(position.toString());

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void createLong()
	{
		if (symbol == null)
		{
			ERROR("Select symbol");
			return;
		}

		INFO("");
		try
		{
			Position position = acquireArguments(PositionSide.LONG);
			pMaker = new PositionTrader(position);
			pMaker.createLong();

			//-----------------------------------------------------------------
			txtResult.setForeground(Styles.COLOR_TEXT_LONG);
			txtResult.setText(position.toString());

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
		{
			ERROR(e);
		}
	}

	private void clean()
	{
		pMaker = null;
		INFO("");
		txtResult.setText("");

		btnPostFirst.setEnabled(false);
		btnPostOthers.setEnabled(false);
	}

	// ------------------------------------------------------------------------
	
	private void post(PostStyle postStyle)
	{
		INFO("");
		try
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			String result = pMaker.post(postStyle);
			if (result != null)
			{
				ERROR(result);
			}

			txtResult.setForeground(pMaker.getPosition().getSide() == PositionSide.SHORT ? Styles.COLOR_TEXT_SHORT : Styles.COLOR_TEXT_LONG);
			txtResult.setText(pMaker.getPosition().toString());
			save(symbol.getName() + "_" + pMaker.getPosition().getSide().name());

			// ------------------------------------------------------------
			btnPostFirst.setEnabled(false);
			btnPostOthers.setEnabled(postStyle.equals(PostStyle.FIRST));

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		catch(Exception e)
		{
			ERROR(e);
		}	
	}

	public void save(String filename)
	{
		try
		{
			File path = new File(Constants.DEFAULT_LOG_FOLDER);
			if (!path.exists()) 
			{
				path.mkdirs();
			}

			File logFile = new File(path, filename + ".log");
			FileUtils.writeStringToFile(logFile, txtResult.getText(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
		}
	}

	// ------------------------------------------------------------------------

	public static void createDirectory(File fileProcessPath) 
	{
		if (!fileProcessPath.exists()) 
		{
			fileProcessPath.mkdirs();
		}
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
		launch();
	}

}
