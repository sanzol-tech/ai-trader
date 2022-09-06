package sanzol.app.forms;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.decimal4j.util.DoubleRounder;

import api.client.enums.PositionSide;
import api.client.futures.async.PriceListener;
import api.client.futures.async.PriceService;
import api.client.futures.sync.model.AccountBalance;
import api.client.futures.sync.model.PositionRisk;
import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Config;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.Position;
import sanzol.app.model.PriceQty;
import sanzol.app.model.enums.GridTemplate;
import sanzol.app.model.enums.QuantityType;
import sanzol.app.service.BalanceService;
import sanzol.app.service.PositionListener;
import sanzol.app.service.PositionService;
import sanzol.app.service.PositionTrader;
import sanzol.app.service.PositionTrader.PostStyle;
import sanzol.app.service.Symbol;
import sanzol.app.util.Convert;
import sanzol.lib.util.BeepUtils;
import sanzol.lib.util.ExceptionUtils;

public class FrmGrid extends JFrame implements PriceListener, PositionListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol symbol;
	private PositionTrader pMaker;
	private PositionSide positionSide = null;
	private boolean isBotMode = false;
	private boolean isOpenPosition = false;

	private JPanel contentPane;
	private JPanel pnlPosition;
	private JPanel pnlLinks;

	private JButton btnSearch;
	private JButton btnShort;
	private JButton btnLong;
	private JButton btnPostFirst;
	private JButton btnPostOthers;
	private JButton btnClean;

	private JLabel lblPrice;
	private JLabel lblQty;

	private JLabel lnkBtnSimple;
	private JLabel lnkBtnClassic1;
	private JLabel lnkBtnClassic2;
	private JLabel lnkBtnIncremental1;
	private JLabel lnkBtnIncremental2;
	private JLabel lnkBtnFast;

	private JTextArea txtResult;

	private JRadioButton rbArithmetic;
	private JRadioButton rbGeometric;
	
	private JCheckBox chkPIF;	
	private JCheckBox chkLastPrice;
	private JComboBox<QuantityType> cmbQtyType;

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
	private JTextField txtInQty;
	private JTextField txtInPrice;
	private JTextField txtPriceShow;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtTProfit;
	private JTextField txtPriceIncr;
	private JTextField txtIterations;
	private JTextField txtCoinsIncr1;
	private JTextField txtCoinsIncr;
	private JTextField txtPriceIncr1;
	private JTextField txtStopLoss;
	private JTextField txtPIF;
	private JLabel lnkBtnBTC;
	private JLabel lnkBtnAltcoin;

	public FrmGrid()
	{
		initComponents();
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
		
		JButton btnGenGrid = new JButton(CharConstants.ARROW_DOWN);
		btnGenGrid.setBounds(580, 118, 45, 26);
		contentPane.add(btnGenGrid);
		
		btnGenGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateGrid();
			}
		});
		
		btnLong = new JButton("LONG");
		btnLong.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLong.setBackground(Styles.COLOR_BTN_LONG);
		btnLong.setOpaque(true);
		btnLong.setBounds(131, 138, 105, 32);
		contentPane.add(btnLong);
		
		btnClean = new JButton("CLEAN");
		btnClean.setOpaque(true);
		btnClean.setBounds(1040, 561, 78, 31);
		contentPane.add(btnClean);
		
		txtResult = new JTextArea();
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));

		JScrollPane scroll = new JScrollPane(txtResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(16, 226, 1102, 322);
		scroll.setBorder(UIManager.getBorder("TextField.border"));
		contentPane.add(scroll);

		txtError = new JTextField();
		txtError.setHorizontalAlignment(SwingConstants.RIGHT);
		txtError.setForeground(Styles.COLOR_TEXT_ERROR);
		txtError.setEditable(false);
		txtError.setBounds(16, 570, 998, 20);
		contentPane.add(txtError);
		txtError.setColumns(10);

		btnPostOthers = new JButton("Others");
		btnPostOthers.setIcon(Styles.IMAGE_EXECUTE);
		btnPostOthers.setEnabled(false);
		btnPostOthers.setToolTipText("Post the other orders");
		btnPostOthers.setOpaque(true);
		btnPostOthers.setBounds(131, 182, 105, 32);
		contentPane.add(btnPostOthers);

		JPanel pnlGridTempl = new JPanel();
		pnlGridTempl.setBorder(UIManager.getBorder("TextField.border"));
		pnlGridTempl.setBounds(252, 136, 866, 80);
		contentPane.add(pnlGridTempl);
		pnlGridTempl.setLayout(null);

		JLabel lblGrdPrice = new JLabel("Price %");
		lblGrdPrice.setBounds(20, 20, 53, 14);
		pnlGridTempl.add(lblGrdPrice);

		JLabel lblGrdQty = new JLabel("Qty %");
		lblGrdQty.setBounds(20, 47, 53, 14);
		pnlGridTempl.add(lblGrdQty);

		txtGPrice1 = new JTextField();
		txtGPrice1.setBounds(83, 18, 60, 20);
		txtGPrice1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice1.setColumns(10);
		pnlGridTempl.add(txtGPrice1);

		txtGPrice2 = new JTextField();
		txtGPrice2.setBounds(151, 18, 60, 20);
		txtGPrice2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice2.setColumns(10);
		pnlGridTempl.add(txtGPrice2);
		
		txtGQty2 = new JTextField();
		txtGQty2.setBounds(151, 46, 60, 20);
		txtGQty2.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty2.setColumns(10);
		pnlGridTempl.add(txtGQty2);
		
		txtGQty1 = new JTextField();
		txtGQty1.setBounds(83, 46, 60, 20);
		txtGQty1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty1.setColumns(10);
		pnlGridTempl.add(txtGQty1);
		
		txtGPrice3 = new JTextField();
		txtGPrice3.setBounds(219, 18, 60, 20);
		txtGPrice3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice3.setColumns(10);
		pnlGridTempl.add(txtGPrice3);
		
		txtGQty3 = new JTextField();
		txtGQty3.setBounds(219, 46, 60, 20);
		txtGQty3.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty3.setColumns(10);
		pnlGridTempl.add(txtGQty3);
		
		txtGPrice4 = new JTextField();
		txtGPrice4.setBounds(287, 18, 60, 20);
		txtGPrice4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice4.setColumns(10);
		pnlGridTempl.add(txtGPrice4);
		
		txtGQty4 = new JTextField();
		txtGQty4.setBounds(287, 46, 60, 20);
		txtGQty4.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty4.setColumns(10);
		pnlGridTempl.add(txtGQty4);
		
		txtGPrice5 = new JTextField();
		txtGPrice5.setBounds(355, 18, 60, 20);
		txtGPrice5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice5.setColumns(10);
		pnlGridTempl.add(txtGPrice5);
		
		txtGQty5 = new JTextField();
		txtGQty5.setBounds(355, 46, 60, 20);
		txtGQty5.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty5.setColumns(10);
		pnlGridTempl.add(txtGQty5);
		
		txtGPrice6 = new JTextField();
		txtGPrice6.setBounds(423, 18, 60, 20);
		txtGPrice6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice6.setColumns(10);
		pnlGridTempl.add(txtGPrice6);
		
		txtGQty6 = new JTextField();
		txtGQty6.setBounds(423, 46, 60, 20);
		txtGQty6.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty6.setColumns(10);
		pnlGridTempl.add(txtGQty6);
		
		txtGPrice7 = new JTextField();
		txtGPrice7.setBounds(491, 18, 60, 20);
		txtGPrice7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice7.setColumns(10);
		pnlGridTempl.add(txtGPrice7);
		
		txtGQty7 = new JTextField();
		txtGQty7.setBounds(491, 46, 60, 20);
		txtGQty7.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty7.setColumns(10);
		pnlGridTempl.add(txtGQty7);
		
		txtGPrice8 = new JTextField();
		txtGPrice8.setBounds(559, 18, 60, 20);
		txtGPrice8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice8.setColumns(10);
		pnlGridTempl.add(txtGPrice8);
		
		txtGQty8 = new JTextField();
		txtGQty8.setBounds(559, 46, 60, 20);
		txtGQty8.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty8.setColumns(10);
		pnlGridTempl.add(txtGQty8);

		btnShort = new JButton("SHORT");
		btnShort.setBounds(16, 138, 105, 32);
		btnShort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShort.setBackground(Styles.COLOR_BTN_SHORT);
		btnShort.setOpaque(true);
		contentPane.add(btnShort);
		
		pnlPosition = new JPanel();
		pnlPosition.setLayout(null);
		pnlPosition.setBorder(UIManager.getBorder("TextField.border"));
		pnlPosition.setBounds(252, 16, 200, 111);
		contentPane.add(pnlPosition);

		lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setBounds(14, 11, 45, 14);
		pnlPosition.add(lblPrice);

		lblQty = new JLabel("Qty");
		lblQty.setHorizontalAlignment(SwingConstants.LEFT);
		lblQty.setBounds(14, 60, 45, 14);
		pnlPosition.add(lblQty);
		
		chkLastPrice = new JCheckBox("Last");
		chkLastPrice.setBounds(112, 28, 60, 23);
		pnlPosition.add(chkLastPrice);

		cmbQtyType = new JComboBox<QuantityType>();
		cmbQtyType.setBounds(116, 77, 65, 22);
		pnlPosition.add(cmbQtyType);
		cmbQtyType.setModel(new DefaultComboBoxModel<QuantityType>(QuantityType.values()));
	
		txtInPrice = new JTextField();
		txtInPrice.setBounds(14, 29, 92, 20);
		pnlPosition.add(txtInPrice);
		txtInPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInPrice.setColumns(10);
	
		txtInQty = new JTextField();
		txtInQty.setBounds(14, 78, 92, 20);
		pnlPosition.add(txtInQty);
		txtInQty.setHorizontalAlignment(SwingConstants.RIGHT);
		txtInQty.setColumns(10);
		
		btnPostFirst = new JButton("First");
		btnPostFirst.setBounds(16, 182, 105, 32);
		contentPane.add(btnPostFirst);
		btnPostFirst.setIcon(Styles.IMAGE_EXECUTE);
		btnPostFirst.setEnabled(false);
		btnPostFirst.setToolTipText("Post only first order");
		btnPostFirst.setOpaque(true);
		
		JPanel pnlGridConfig = new JPanel();
		pnlGridConfig.setLayout(null);
		pnlGridConfig.setBorder(UIManager.getBorder("TextField.border"));
		pnlGridConfig.setBounds(466, 16, 480, 111);
		contentPane.add(pnlGridConfig);
		
		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		lblItarations.setBounds(14, 11, 80, 14);
		pnlGridConfig.add(lblItarations);
		
		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(300, 11, 80, 14);
		pnlGridConfig.add(lblPriceIncr);
		
		JLabel lblTProfit = new JLabel("Take profit %");
		lblTProfit.setHorizontalAlignment(SwingConstants.LEFT);
		lblTProfit.setBounds(390, 62, 80, 14);
		pnlGridConfig.add(lblTProfit);
		
		txtTProfit = new JTextField();
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		txtTProfit.setBounds(390, 81, 72, 20);
		pnlGridConfig.add(txtTProfit);
		
		txtPriceIncr = new JTextField();
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		txtPriceIncr.setBounds(300, 30, 72, 20);
		pnlGridConfig.add(txtPriceIncr);
		
		txtIterations = new JTextField();
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		txtIterations.setBounds(14, 30, 72, 20);
		pnlGridConfig.add(txtIterations);
		
		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(300, 62, 80, 14);
		pnlGridConfig.add(lblCoinsIncr);
		
		txtCoinsIncr1 = new JTextField();
		txtCoinsIncr1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr1.setColumns(10);
		txtCoinsIncr1.setBounds(206, 81, 72, 20);
		pnlGridConfig.add(txtCoinsIncr1);
		
		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		txtCoinsIncr.setBounds(300, 81, 72, 20);
		pnlGridConfig.add(txtCoinsIncr);
		
		JLabel lblCoinsIncr1 = new JLabel("1\u00B0 Qty Incr %");
		lblCoinsIncr1.setBounds(206, 62, 80, 14);
		pnlGridConfig.add(lblCoinsIncr1);
		
		txtPriceIncr1 = new JTextField();
		txtPriceIncr1.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr1.setColumns(10);
		txtPriceIncr1.setBounds(206, 30, 72, 20);
		pnlGridConfig.add(txtPriceIncr1);

		JLabel lblPriceIncr_1 = new JLabel("1\u00B0 Price Incr %");
		lblPriceIncr_1.setBounds(206, 11, 80, 14);
		pnlGridConfig.add(lblPriceIncr_1);

		txtStopLoss = new JTextField();
		txtStopLoss.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopLoss.setColumns(10);
		txtStopLoss.setBounds(390, 30, 72, 20);
		pnlGridConfig.add(txtStopLoss);

		JLabel lblDistSL = new JLabel("SL after last %");
		lblDistSL.setBounds(390, 11, 90, 14);
		pnlGridConfig.add(lblDistSL);

		rbArithmetic = new JRadioButton("Arithmetic");
		rbArithmetic.setSelected(true);
		rbArithmetic.setBounds(13, 59, 90, 23);
		pnlGridConfig.add(rbArithmetic);

		rbGeometric = new JRadioButton("Geometric");
		rbGeometric.setSelected(false);
		rbGeometric.setBounds(13, 80, 90, 23);
		pnlGridConfig.add(rbGeometric);

		ButtonGroup bg3 = new javax.swing.ButtonGroup();
		bg3.add(rbArithmetic);
		bg3.add(rbGeometric);

		JScrollPane scroll_1 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_1.setBorder(UIManager.getBorder("TextField.border"));
		scroll_1.setBounds(37, -89, 437, 28);
		pnlGridConfig.add(scroll_1);

		txtPIF = new JTextField();
		txtPIF.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPIF.setColumns(10);
		txtPIF.setBounds(104, 30, 72, 20);
		pnlGridConfig.add(txtPIF);

		chkPIF = new JCheckBox("PIF");
		chkPIF.setBounds(102, 6, 72, 23);
		pnlGridConfig.add(chkPIF);

		pnlLinks = new JPanel();
		pnlLinks.setBorder(UIManager.getBorder("TextField.border"));
		pnlLinks.setBounds(960, 16, 158, 111);
		contentPane.add(pnlLinks);
		pnlLinks.setLayout(null);

		lnkBtnSimple = new JLabel("Simple");
		lnkBtnSimple.setBounds(14, 11, 62, 18);
		lnkBtnSimple.setForeground(Styles.COLOR_LINK);
		lnkBtnSimple.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnSimple);

		lnkBtnFast = new JLabel("Fast");
		lnkBtnFast.setBounds(82, 11, 62, 18);
		lnkBtnFast.setForeground(Styles.COLOR_LINK);
		lnkBtnFast.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnFast);

		lnkBtnBTC = new JLabel("BTC");
		lnkBtnBTC.setBounds(14, 36, 62, 18);
		lnkBtnBTC.setForeground(Styles.COLOR_LINK);
		lnkBtnBTC.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnBTC);

		lnkBtnAltcoin = new JLabel("Altcoin");
		lnkBtnAltcoin.setBounds(82, 36, 62, 18);
		lnkBtnAltcoin.setForeground(Styles.COLOR_LINK);
		lnkBtnAltcoin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnAltcoin);

		lnkBtnClassic1 = new JLabel("Classic 6");
		lnkBtnClassic1.setBounds(14, 61, 62, 18);
		lnkBtnClassic1.setForeground(Styles.COLOR_LINK);
		lnkBtnClassic1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnClassic1);

		lnkBtnIncremental1 = new JLabel("Incrmtl 1");
		lnkBtnIncremental1.setBounds(14, 86, 62, 18);
		lnkBtnIncremental1.setForeground(Styles.COLOR_LINK);
		lnkBtnIncremental1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnIncremental1);

		lnkBtnClassic2 = new JLabel("Classic 8");
		lnkBtnClassic2.setBounds(82, 61, 62, 18);
		lnkBtnClassic2.setForeground(Styles.COLOR_LINK);
		lnkBtnClassic2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnClassic2);

		lnkBtnIncremental2 = new JLabel("Incrmtl 2");
		lnkBtnIncremental2.setBounds(82, 86, 62, 18);
		lnkBtnIncremental2.setForeground(Styles.COLOR_LINK);
		lnkBtnIncremental2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnIncremental2);

		JPanel pnlSymbol = new JPanel();
		pnlSymbol.setBorder(UIManager.getBorder("TextField.border"));
		pnlSymbol.setBounds(16, 16, 220, 111);
		contentPane.add(pnlSymbol);
		pnlSymbol.setLayout(null);

		JLabel lblSymbol = new JLabel("SYMBOL");
		lblSymbol.setBounds(14, 11, 90, 14);
		pnlSymbol.add(lblSymbol);
		lblSymbol.setHorizontalAlignment(SwingConstants.LEFT);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setBounds(14, 29, 90, 22);
		txtSymbolLeft.setColumns(10);
		pnlSymbol.add(txtSymbolLeft);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setBounds(114, 29, 90, 22);
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Config.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		pnlSymbol.add(txtSymbolRight);

		txtPriceShow = new JTextField();
		txtPriceShow.setBounds(114, 59, 90, 22);
		txtPriceShow.setForeground(Styles.COLOR_TEXT_ALT1);
		txtPriceShow.setHorizontalAlignment(SwingConstants.CENTER);
		txtPriceShow.setFont(new Font("Tahoma", Font.BOLD, 12));
		txtPriceShow.setEditable(false);
		txtPriceShow.setColumns(10);
		pnlSymbol.add(txtPriceShow);

		btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setBounds(14, 60, 90, 22);
		btnSearch.setOpaque(true);
		pnlSymbol.add(btnSearch);
		
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

		lnkBtnSimple.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.SIMPLE);
			}
		});
		lnkBtnFast.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.FAST);
			}
		});		

		lnkBtnBTC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.BTC);
			}
		});
		lnkBtnAltcoin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.ALTCOIN);
			}
		});		

		lnkBtnClassic1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.CLASSIC6);
			}
		});
		lnkBtnClassic2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.CLASSIC8);
			}
		});

		lnkBtnIncremental1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.INCREMENTAL1);
			}
		});
		lnkBtnIncremental2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				loadGridTemplate(GridTemplate.INCREMENTAL2);
			}
		});	

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
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

	}

	public static void launch()
	{
		launch(null, null, null, null, null, false);
	}

	public static void launch(String symbolLeft, String side, String price, boolean isBotMode)
	{
		launch(symbolLeft, side, price, null, null, isBotMode);
	}

	public static void launch(String symbolLeft, String side, String price, String stopLoss, String takeProfit, boolean isBotMode)
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
					if (stopLoss != null && takeProfit != null)
					{
						frame.generateGridSimple(stopLoss, takeProfit);
					}
					else
					{
						if ("BTC".equals(symbolLeft))
							frame.loadGridTemplate(GridTemplate.BTC);
						else
							frame.loadGridTemplate(GridTemplate.DEFAULT);
					}

					if (symbolLeft != null && !symbolLeft.isEmpty())
					{
						frame.txtSymbolLeft.setText(symbolLeft);
						frame.search();
					}
					
					if (side != null && price != null)
					{
						frame.txtInPrice.setText(price);
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
				if (chkLastPrice.isSelected())
				{
					txtInPrice.setText(symbol.priceToStr(price));
				}
				
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
				if (chkLastPrice.isSelected())
				{
					txtInPrice.setText(symbol.priceToStr(price));
				}
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
		if (isOpenPosition)
		{
			txtInPrice.setText(symbol.priceToStr(positionRisk.getEntryPrice()));
			double amt = positionRisk.getPositionAmt().doubleValue();
			txtInQty.setText(symbol.qtyToStr(amt));
			cmbQtyType.setSelectedItem(QuantityType.COIN);
		}
		else
		{
			isOpenPosition = (positionRisk != null && positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) != 0);
			if (isOpenPosition)
			{
				txtInPrice.setText(symbol.priceToStr(positionRisk.getEntryPrice()));
				double amt = positionRisk.getPositionAmt().doubleValue();
				txtInQty.setText(symbol.qtyToStr(amt));
				cmbQtyType.setSelectedItem(QuantityType.COIN);

				if (amt != 0)
				{
					btnShort.setEnabled(false);
					btnLong.setEnabled(false);
					btnPostFirst.setEnabled(false);
					btnPostOthers.setEnabled(true);
				}

				txtInPrice.setEnabled(false);
				txtInQty.setEnabled(false);
				chkLastPrice.setEnabled(false);
				cmbQtyType.setEnabled(false);

				BeepUtils.beep3();
			}
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
	
	private void loadGridTemplate(GridTemplate template)
	{
		try
		{
			if (template.equals(GridTemplate.DEFAULT))
			{
				// Default
				generateGridDefault();
			}
			if (template.equals(GridTemplate.SIMPLE))
			{
				// Simple
				generateGrid(false, "0", false, "", "2.0", "0.0", "2.0", "100", "1.0");
			}
			else if (template.equals(GridTemplate.FAST))
			{
				// Fast
				generateGrid(false, "6", false, "", "1.8", "125.0", "1.8", "125.0", "0.5");
			}
			else if (template.equals(GridTemplate.BTC))
			{
				// BTC
				generateGrid(false, "5", false, "", "2.0", "0.0", "2.0", "100", "0.5");
			}
			else if (template.equals(GridTemplate.ALTCOIN))
			{
				// Altcoin
				generateGrid(true, "6", false, "", "2.5", "2.5", "2.5", "80", "1.5");
			}
			else if (template.equals(GridTemplate.CLASSIC6))
			{
				// Classic 6
				generateGrid(false, "6", false, "", "2.0", "40.0", "2.0", "40.0", "1.0");
			}
			else if (template.equals(GridTemplate.CLASSIC8))
			{
				// Classic 8
				generateGrid(false, "8", false, "", "2.0", "40.0", "2.0", "40.0", "1.0");
			}
			else if (template.equals(GridTemplate.INCREMENTAL1))
			{
				// Incremental 1
				generateGrid(true, "6", true, "110.0", "2.5", "2.5", "2.5", "40.0", "2.0");
			}
			else if (template.equals(GridTemplate.INCREMENTAL2))
			{
				// Incremental 2
				generateGrid(true, "7", true, "110.0", "2.5", "2.5", "2.5", "50.0", "2.0");
			}
			else
			{
				return;
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void generateGridDefault()
	{
		txtInQty.setText(String.valueOf(Config.getInQty()));
		
		if ("U".equals(Config.getInQtyType()))
		{
			cmbQtyType.setSelectedItem(QuantityType.USD);	
		} else if ("B".equals(Config.getInQtyType()))
		{
			cmbQtyType.setSelectedItem(QuantityType.BALANCE);	
		} else if ("C".equals(Config.getInQtyType()))
		{
			cmbQtyType.setSelectedItem(QuantityType.COIN);	
		}

		rbArithmetic.setSelected("A".equals(Config.getGridType()));
		rbGeometric.setSelected("G".equals(Config.getGridType()));
		txtIterations.setText(String.valueOf(Config.getIterations()));
		chkPIF.setSelected(false); // TODO
		txtPIF.setText(Convert.dblToStrPercent(Config.getPif()));

		txtPriceIncr1.setText(Convert.dblToStrPercent(Config.getPriceIncrement1()));
		txtCoinsIncr1.setText(Convert.dblToStrPercent(Config.getCoinsIncrement1()));
		txtPriceIncr.setText(Convert.dblToStrPercent(Config.getPriceIncrement()));
		txtCoinsIncr.setText(Convert.dblToStrPercent(Config.getCoinsIncrement()));
		
		txtStopLoss.setText(Convert.dblToStrPercent(Config.getStoplossIncrement()));
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));
		
		generateGrid();
	}

	private void generateGridSimple(String stopLoss, String takeProfit)
	{
		BigDecimal noFee = BigDecimal.ONE.subtract(BigDecimal.valueOf(0.006));
		BigDecimal sl = new BigDecimal(stopLoss);
		BigDecimal tp = new BigDecimal(takeProfit);

		sl = sl.add(BigDecimal.valueOf(0.1)).max(BigDecimal.valueOf(0.5));
		tp = tp.subtract(BigDecimal.valueOf(0.1));

		BigDecimal usd = BigDecimal.valueOf(50).divide(sl, 2, RoundingMode.DOWN).multiply(noFee);
		if ("U".equals(Config.getInQtyType()))
		{
			usd = BigDecimal.valueOf(Config.getInQty()).divide(sl, 2, RoundingMode.DOWN).multiply(noFee);		
		}

		if (!isOpenPosition)
		{
			txtInQty.setText(Convert.usdToStr(usd));
			cmbQtyType.setSelectedItem(QuantityType.USD);
		}

		rbArithmetic.setSelected(false);
		rbGeometric.setSelected(true);
		txtIterations.setText("0");
		chkPIF.setSelected(false);
		txtPIF.setText("0.0");

		txtPriceIncr1.setText("1.0");
		txtCoinsIncr1.setText("0.0");
		txtPriceIncr.setText("1.0");
		txtCoinsIncr.setText("100.0");

		txtStopLoss.setText(String.format(Locale.US, "%.2f", sl));
		txtTProfit.setText(String.format(Locale.US, "%.2f", tp));

		generateGrid();
	}
	
	private void generateGrid(boolean isAritmetic, String iterations, boolean isPif, String pif, String priceIncr1, String coinsIncr1, String priceIncr, String coinsIncr, String stopLoss)
	{
		if (!isOpenPosition)
		{
			txtInQty.setText("5.0");
			cmbQtyType.setSelectedItem(QuantityType.USD);
		}

		rbArithmetic.setSelected(isAritmetic);
		rbGeometric.setSelected(!isAritmetic);
		txtIterations.setText(iterations);
		chkPIF.setSelected(isPif);
		txtPIF.setText(pif);

		txtPriceIncr1.setText(priceIncr1);
		txtCoinsIncr1.setText(coinsIncr1);
		txtPriceIncr.setText(priceIncr);
		txtCoinsIncr.setText(coinsIncr);

		txtStopLoss.setText(stopLoss);
		txtTProfit.setText(Convert.dblToStrPercent(Config.getTakeprofit()));

		generateGrid();
	}

	private void generateGrid()
	{
		if (chkPIF.isSelected())
			generateGridPIF();
		else
			generateGridStandard();

		if (positionSide != null)
		{
			if (positionSide.equals(PositionSide.SHORT))
				createShort();
			else if (positionSide.equals(PositionSide.LONG))
				createLong();
		}
	}

	private void generateGridStandard()
	{
		int iterations = Integer.valueOf(txtIterations.getText());
		double priceIncr1 = Convert.strPercentToDbl(txtPriceIncr1.getText());
		double qtyIncr1 = Convert.strPercentToDbl(txtCoinsIncr1.getText());
		double priceIncr = Convert.strPercentToDbl(txtPriceIncr.getText());
		double qtyIncr = Convert.strPercentToDbl(txtCoinsIncr.getText());

		if (iterations >= 1)
		{
			txtGPrice1.setText(Convert.dblToStrPercent(priceIncr1));
			txtGQty1.setText(Convert.dblToStrPercent(qtyIncr1));
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

	private void generateGridPIF()
	{
		int iterations = Integer.valueOf(txtIterations.getText());
		double priceIncr = Convert.strPercentToDbl(txtPriceIncr1.getText());
		double qtyIncr = Convert.strPercentToDbl(txtCoinsIncr.getText());
		double pif = Convert.strPercentToDbl(txtPIF.getText());

		if (iterations >= 1)
		{
			txtGPrice1.setText(txtPriceIncr1.getText());
			txtGQty1.setText(txtCoinsIncr1.getText());
		}
		else
		{
			txtGPrice1.setText("");
			txtGQty1.setText("");
		}

		if (iterations >= 2)
		{
			txtGPrice2.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 1)));
			txtGQty2.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice2.setText("");
			txtGQty2.setText("");
		}
		if (iterations >= 3)
		{
			txtGPrice3.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 2)));
			txtGQty3.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice3.setText("");
			txtGQty3.setText("");
		}
		if (iterations >= 4)
		{
			txtGPrice4.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 3)));
			txtGQty4.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice4.setText("");
			txtGQty4.setText("");
		}
		if (iterations >= 5)
		{
			txtGPrice5.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 4)));
			txtGQty5.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice5.setText("");
			txtGQty5.setText("");
		}
		if (iterations >= 6)
		{
			txtGPrice6.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 5)));
			txtGQty6.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice6.setText("");
			txtGQty6.setText("");
		}
		if (iterations >= 7)
		{
			txtGPrice7.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 6)));
			txtGQty7.setText(Convert.dblToStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice7.setText("");
			txtGQty7.setText("");
		}
		if (iterations >= 8)
		{
			txtGPrice8.setText(Convert.dblToStrPercent(priceIncr * Math.pow(pif, 7)));
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
		Double distBeforeSL = Convert.strPercentToDbl(txtStopLoss.getText());
		Double tProfit = Convert.strPercentToDbl(txtTProfit.getText());
		List<PriceQty> lstPriceQty = acquireGrid();

		Position position = new Position(symbol, side, distBeforeSL, tProfit, lstPriceQty);
		position.setArithmetic(rbArithmetic.isSelected());

		Double inPrice = getInPrice();
		position.setInPrice(inPrice);
		position.setMarkPrice(chkLastPrice.isSelected());

		Double inQty = getInQty(inPrice);
		position.setInQty(inQty);

		return position;
	}

	private Double getInPrice() throws Exception
	{
		if (isOpenPosition)
		{
			double price = Double.valueOf(txtInPrice.getText());
			return price;
		}
		else if (chkLastPrice.isSelected())
		{
			BigDecimal lastPrice = PriceService.getLastPrice(symbol);
			return lastPrice.doubleValue();
		}
		else
		{
			if (NumberUtils.isCreatable(txtInPrice.getText()))
			{
				double price = Double.valueOf(txtInPrice.getText());
				return price;
			}
		}

		ERROR("Invalid price");
		return null;
	}

	private Double getInQty(double inPrice)
	{
		if (isOpenPosition)
		{
			double qty = Math.abs(Double.valueOf(txtInQty.getText()));
			return qty;
		}
		if (cmbQtyType.getSelectedItem().equals(QuantityType.COIN))
		{
			if (NumberUtils.isCreatable(txtInQty.getText()))
			{
				double qty = Double.valueOf(txtInQty.getText());
				return qty;
			}
		}
		else if (cmbQtyType.getSelectedItem().equals(QuantityType.BALANCE))
		{
			if (NumberUtils.isCreatable(txtInQty.getText()))
			{
				double balancePercent = Convert.strPercentToDbl(txtInQty.getText());
				AccountBalance accBalance = BalanceService.getAccountBalance();
				double balance = accBalance.getBalance().doubleValue();
				return DoubleRounder.round(Math.max(5, balance * balancePercent) / inPrice, symbol.getQuantityPrecision(), RoundingMode.CEILING);
			}
		}
		else if (cmbQtyType.getSelectedItem().equals(QuantityType.USD))
		{
			if (NumberUtils.isCreatable(txtInQty.getText()))
			{
				double usd = Double.valueOf(txtInQty.getText());
				return DoubleRounder.round(usd / inPrice, symbol.getQuantityPrecision(), RoundingMode.CEILING);
			}
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
			positionSide = PositionSide.SHORT;
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
			positionSide = PositionSide.LONG;
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
		positionSide = null;
		pMaker = null;
		INFO("");
		txtResult.setText("");

		btnShort.setEnabled(true);
		btnLong.setEnabled(true);
		btnPostFirst.setEnabled(false);
		btnPostOthers.setEnabled(false);
	}

	// ------------------------------------------------------------------------
	
	private void post(PostStyle postStyle)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		INFO("");
		try
		{
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
		}
		catch(Exception e)
		{
			ERROR(e);
		}	

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
