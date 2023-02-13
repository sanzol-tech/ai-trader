package aitrader.ui.forms;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.commons.lang3.StringUtils;
import org.decimal4j.util.DoubleRounder;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.GridOrder;
import aitrader.core.model.GridPosition;
import aitrader.core.model.Position;
import aitrader.core.model.PriceQty;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.core.model.enums.GridStrategy;
import aitrader.core.model.enums.OpType;
import aitrader.core.model.enums.PriceIncrType;
import aitrader.core.model.enums.QtyIncrType;
import aitrader.core.model.enums.QuantityType;
import aitrader.core.service.position.PositionService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.core.service.trade.GridTrade;
import aitrader.core.service.trade.GridTrade.PostStyle;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.BeepUtils;
import aitrader.util.Convert;
import aitrader.util.constants.CharConstants;
import aitrader.util.observable.Handler;
import binance.futures.enums.PositionSide;

public class FrmGrid extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME;

	private Symbol symbol;
	private GridTrade gridTrade;
	private PositionSide gridSide = null;
	private boolean isOpenPosition = false;

	private JPanel contentPane;
	private JPanel pnlPosition;
	private JPanel pnlLinks;

	private JButton btnSearch;
	private JButton btnGenGrid;
	private JButton btnShort;
	private JButton btnLong;
	private JButton btnPostFirst;
	private JButton btnPostOthers;
	private JButton btnClean;

	private JLabel lblPrice;
	private JLabel lblQty;

	private JLabel lnkBtnDefault;
	private JLabel lnkBtnSimple;
	private JLabel lnkBtnBTC;
	private JLabel lnkBtnAltcoin;
	private JLabel lnkBtnClassic1;
	private JLabel lnkBtnClassic2;
	private JLabel lnkBtnIncremental1;
	private JLabel lnkBtnIncremental2;

	private JTextArea txtResult;

	private JRadioButton rbArithmetic;
	private JRadioButton rbGeometric;
	private JRadioButton rbOverLastOrder;
	private JRadioButton rbOverPosition;
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
	private JTextField txtGPrice9;
	private JTextField txtGPrice10;

	private JTextField txtGQty1;
	private JTextField txtGQty2;
	private JTextField txtGQty3;
	private JTextField txtGQty4;
	private JTextField txtGQty5;
	private JTextField txtGQty6;
	private JTextField txtGQty7;
	private JTextField txtGQty8;
	private JTextField txtGQty9;
	private JTextField txtGQty10;

	private CtrlError ctrlError;

	private JTextField txtInQty;
	private JTextField txtInPrice;
	private JTextField txtPriceShow;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtTProfit;
	private JTextField txtPriceIncr;
	private JTextField txtIterations;
	private JTextField txtCoinsIncr;
	private JTextField txtStopLoss;

	private JCheckBox chkStopLoss;
	private JCheckBox chkTProfit;

	private Handler<Void> priceServiceHandler = e -> { onPriceUpdate(); };
	private Handler<Void> PositionServiceHandler = e -> { onPositionUpdate(); };
	
	public FrmGrid()
	{
		initComponents();

		txtSymbolRight.setText(CoreConfig.getDefaultSymbolRight());

		SymbolInfoService.attachObserver(priceServiceHandler);
		PositionService.attachObserver(PositionServiceHandler);
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

		btnGenGrid = new JButton(CharConstants.ARROW_DOWN);
		btnGenGrid.setBounds(546, 118, 45, 26);
		contentPane.add(btnGenGrid);

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

		ctrlError = new CtrlError();
		ctrlError.setBounds(16, 570, 998, 20);
		contentPane.add(ctrlError);

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

		txtGPrice9 = new JTextField();
		txtGPrice9.setBounds(627, 18, 60, 20);
		txtGPrice9.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice9.setColumns(10);
		pnlGridTempl.add(txtGPrice9);

		txtGQty9 = new JTextField();
		txtGQty9.setBounds(627, 46, 60, 20);
		txtGQty9.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty9.setColumns(10);
		pnlGridTempl.add(txtGQty9);

		txtGPrice10 = new JTextField();
		txtGPrice10.setBounds(695, 18, 60, 20);
		txtGPrice10.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGPrice10.setColumns(10);
		pnlGridTempl.add(txtGPrice10);

		txtGQty10 = new JTextField();
		txtGQty10.setBounds(695, 46, 60, 20);
		txtGQty10.setHorizontalAlignment(SwingConstants.RIGHT);
		txtGQty10.setColumns(10);
		pnlGridTempl.add(txtGQty10);
		
		btnShort = new JButton("SHORT");
		btnShort.setBounds(16, 138, 105, 32);
		btnShort.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnShort.setBackground(Styles.COLOR_BTN_SHORT);
		btnShort.setOpaque(true);
		contentPane.add(btnShort);

		pnlPosition = new JPanel();
		pnlPosition.setLayout(null);
		pnlPosition.setBorder(UIManager.getBorder("TextField.border"));
		pnlPosition.setBounds(252, 16, 228, 111);
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
		cmbQtyType.setBounds(116, 77, 92, 22);
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
		pnlGridConfig.setBounds(490, 16, 460, 111);
		contentPane.add(pnlGridConfig);

		JLabel lblItarations = new JLabel("Iterations");
		lblItarations.setHorizontalAlignment(SwingConstants.LEFT);
		lblItarations.setBounds(14, 11, 80, 14);
		pnlGridConfig.add(lblItarations);

		JLabel lblPriceIncr = new JLabel("Price Incr %");
		lblPriceIncr.setBounds(280, 11, 80, 14);
		pnlGridConfig.add(lblPriceIncr);

		txtTProfit = new JTextField();
		txtTProfit.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTProfit.setColumns(10);
		txtTProfit.setBounds(360, 81, 72, 20);
		pnlGridConfig.add(txtTProfit);

		txtPriceIncr = new JTextField();
		txtPriceIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtPriceIncr.setColumns(10);
		txtPriceIncr.setBounds(280, 30, 72, 20);
		pnlGridConfig.add(txtPriceIncr);

		txtIterations = new JTextField();
		txtIterations.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIterations.setColumns(10);
		txtIterations.setBounds(14, 30, 72, 20);
		pnlGridConfig.add(txtIterations);

		JLabel lblCoinsIncr = new JLabel("Qty Incr %");
		lblCoinsIncr.setBounds(280, 62, 80, 14);
		pnlGridConfig.add(lblCoinsIncr);

		txtCoinsIncr = new JTextField();
		txtCoinsIncr.setHorizontalAlignment(SwingConstants.RIGHT);
		txtCoinsIncr.setColumns(10);
		txtCoinsIncr.setBounds(280, 81, 72, 20);
		pnlGridConfig.add(txtCoinsIncr);

		txtStopLoss = new JTextField();
		txtStopLoss.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopLoss.setColumns(10);
		txtStopLoss.setBounds(360, 30, 72, 20);
		pnlGridConfig.add(txtStopLoss);

		rbArithmetic = new JRadioButton("Arithmetic");
		rbArithmetic.setSelected(true);
		rbArithmetic.setBounds(13, 56, 90, 23);
		pnlGridConfig.add(rbArithmetic);

		rbGeometric = new JRadioButton("Geometric");
		rbGeometric.setSelected(false);
		rbGeometric.setBounds(13, 77, 90, 23);
		pnlGridConfig.add(rbGeometric);

		ButtonGroup bg3 = new javax.swing.ButtonGroup();
		bg3.add(rbArithmetic);
		bg3.add(rbGeometric);

		rbOverLastOrder = new JRadioButton("Over last order qty");
		rbOverLastOrder.setSelected(true);
		rbOverLastOrder.setBounds(112, 56, 140, 23);
		pnlGridConfig.add(rbOverLastOrder);

		rbOverPosition = new JRadioButton("Over position qty");
		rbOverPosition.setSelected(false);
		rbOverPosition.setBounds(112, 77, 140, 23);
		pnlGridConfig.add(rbOverPosition);

		ButtonGroup bg4 = new ButtonGroup();
		bg4.add(rbOverLastOrder);
		bg4.add(rbOverPosition);

		JScrollPane scroll_1 = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_1.setBorder(UIManager.getBorder("TextField.border"));
		scroll_1.setBounds(37, -89, 437, 28);
		pnlGridConfig.add(scroll_1);
		
		chkStopLoss = new JCheckBox("Stop loss");
		chkStopLoss.setSelected(true);
		chkStopLoss.setBounds(357, 7, 80, 23);
		pnlGridConfig.add(chkStopLoss);

		chkTProfit = new JCheckBox("T. Profit");
		chkTProfit.setSelected(true);
		chkTProfit.setBounds(357, 57, 80, 23);
		pnlGridConfig.add(chkTProfit);

		pnlLinks = new JPanel();
		pnlLinks.setBorder(UIManager.getBorder("TextField.border"));
		pnlLinks.setBounds(960, 16, 158, 111);
		contentPane.add(pnlLinks);
		pnlLinks.setLayout(null);

		lnkBtnDefault = new JLabel("Default");
		lnkBtnDefault.setBounds(14, 11, 62, 18);
		lnkBtnDefault.setForeground(Styles.COLOR_LINK);
		lnkBtnDefault.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnDefault);

		lnkBtnSimple = new JLabel("Simple");
		lnkBtnSimple.setBounds(82, 11, 62, 18);
		lnkBtnSimple.setForeground(Styles.COLOR_LINK);
		lnkBtnSimple.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnSimple);

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

		lnkBtnClassic2 = new JLabel("Classic 8");
		lnkBtnClassic2.setBounds(82, 61, 62, 18);
		lnkBtnClassic2.setForeground(Styles.COLOR_LINK);
		lnkBtnClassic2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnClassic2);

		lnkBtnIncremental1 = new JLabel("Incrmtl 1");
		lnkBtnIncremental1.setBounds(14, 86, 62, 18);
		lnkBtnIncremental1.setForeground(Styles.COLOR_LINK);
		lnkBtnIncremental1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLinks.add(lnkBtnIncremental1);

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

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				SymbolInfoService.deattachObserver(priceServiceHandler);
				PositionService.deattachObserver(PositionServiceHandler);
			}
		});

		lnkBtnDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGridDefault();
			}
		});
		lnkBtnSimple.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.SIMPLE);
			}
		});

		lnkBtnBTC.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.BTC);
			}
		});
		lnkBtnAltcoin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.ALTCOIN);
			}
		});

		lnkBtnClassic1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.CLASSIC6);
			}
		});
		lnkBtnClassic2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.CLASSIC8);
			}
		});

		lnkBtnIncremental1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.INCREMENTAL1);
			}
		});
		lnkBtnIncremental2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				generateGrid(GridStrategy.INCREMENTAL2);
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnGenGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateGrid();
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

		btnClean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clean();
			}
		});

	}

	public static void launch()
	{
		launch(null, null, null, null, null, null);
	}

	public static void launch(String symbolLeft, String side, String price, GridStrategy gridStrategy)
	{
		launch(symbolLeft, side, price, null, null, gridStrategy);
	}

	public static void launch(String symbolLeft, String side, String price, BigDecimal stopLoss, BigDecimal takeProfit, GridStrategy gridStrategy)
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
				}

				try
				{
					if (gridStrategy == GridStrategy.SIGNAL && stopLoss != null && takeProfit != null)
						frame.generateGridSignal(stopLoss.doubleValue(), takeProfit.doubleValue());
					else
						if ("BTC".equals(symbolLeft))
							frame.generateGrid(GridStrategy.BTC);
						else
							if (gridStrategy == GridStrategy.CUSTOM || gridStrategy == null)
								frame.generateGridDefault();
							else
								frame.generateGrid(gridStrategy);

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
				}
				catch (Exception e)
				{
					frame.ctrlError.ERROR(e);
				}
			}
		});
	}

	// --------------------------------------------------------------------

	private void onPriceUpdate()
	{
		try
		{
			if (symbol != null)
			{
				BigDecimal price = SymbolInfoService.getLastPrice(symbol);
				txtPriceShow.setText(symbol.priceToStr(price));
				if (chkLastPrice.isSelected())
				{
					txtInPrice.setText(symbol.priceToStr(price));
				}
			}
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void onPositionUpdate()
	{
		try
		{
			searchPosition();
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

	private SymbolInfo searchSymbolInfo()
	{
		txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());

		String symbolLeft = txtSymbolLeft.getText();
		String symbolRight = txtSymbolRight.getText();

		SymbolInfo symbolInfo = SymbolInfoService.getSymbolInfo(symbolLeft + symbolRight);

		if (symbolInfo == null)
		{
			return null;
		}

		return symbolInfo;
	}

	private void search()
	{
		ctrlError.CLEAN();
		try
		{
			SymbolInfo symbolInfo = searchSymbolInfo();
			if (symbolInfo == null)
			{
				ctrlError.ERROR("Symbol not found");
				return;
			}

			symbol = symbolInfo.getSymbol();
			setTitle(TITLE + " - " + symbol.getNameLeft());

			BigDecimal lastPrice = symbolInfo.getLastPrice();
			txtPriceShow.setText(symbol.priceToStr(lastPrice));
			if (chkLastPrice.isSelected())
			{
				txtInPrice.setText(symbol.priceToStr(lastPrice));
			}

			searchPosition();
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void searchPosition() // TODO : check this logic well
	{
		List<Position> lstPosition = PositionService.getPositions(symbol.getPair());
		for (Position position : lstPosition)
		{
			if (position.isOpen())
			{
				if (isOpenPosition)
				{
					txtInPrice.setText(symbol.priceToStr(position.getEntryPrice()));
					double amt = position.getQuantity().abs().doubleValue();
					txtInQty.setText(symbol.qtyToStr(amt));
					cmbQtyType.setSelectedItem(QuantityType.COIN);
				}
				else
				{
					isOpenPosition = (position != null && position.getQuantity().compareTo(BigDecimal.ZERO) != 0);
					if (isOpenPosition)
					{
						txtInPrice.setText(symbol.priceToStr(position.getEntryPrice()));
						double amt = position.getQuantity().abs().doubleValue();
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
		}
	}

	// --------------------------------------------------------------------

	private void generateGrid(GridStrategy gridStrategy)
	{
		try
		{
			generateGrid(
				gridStrategy.getQuantityType(),
				gridStrategy.getInQty(),
				gridStrategy.getIterations(),
				gridStrategy.getPriceIncrType(),
				gridStrategy.getQtyIncrType(),
				gridStrategy.getPriceIncr(),
				gridStrategy.getQtyIncr(),
				gridStrategy.getStopLoss(),
				gridStrategy.getTakeProfit());
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void generateGridDefault()
	{
		try
		{
			generateGrid(
				CoreConfig.getQuantityType(),
				CoreConfig.getInQty(),
				CoreConfig.getIterations(),
				CoreConfig.getPriceIncrType(),
				CoreConfig.getQtyIncrType(),
				CoreConfig.getPriceIncr(),
				CoreConfig.getQtyIncr(),
				CoreConfig.getStopLoss(),
				CoreConfig.getTakeProfit());
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void generateGridSignal(double stopLoss, double takeProfit)
	{
		try
		{
			double noFee = 1 - 0.006;

			stopLoss = Math.max(stopLoss + 0.001, 0.005);
			takeProfit = takeProfit - 0.001;

			double usdInQty;
			if (CoreConfig.getQuantityType() == QuantityType.USD)
				usdInQty = DoubleRounder.round((CoreConfig.getInQty() / (100 * stopLoss)) * noFee, 2);
			else
				usdInQty = DoubleRounder.round((50.0 / (100 * stopLoss)) * noFee, 2);

			generateGrid(
				QuantityType.USD,
				usdInQty,
				GridStrategy.SIGNAL.getIterations(),
				GridStrategy.SIGNAL.getPriceIncrType(),
				GridStrategy.SIGNAL.getQtyIncrType(),
				GridStrategy.SIGNAL.getPriceIncr(),
				GridStrategy.SIGNAL.getQtyIncr(),
				stopLoss,
				takeProfit);

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void generateGrid(QuantityType quantityType, Double inQty, Integer iterations,
							  PriceIncrType priceIncrType, QtyIncrType qtyIncrType,
							  Double priceIncr, Double qtyIncr, Double stopLoss, Double takeProfit)
	{
		if (!isOpenPosition)
		{
			cmbQtyType.setSelectedItem(quantityType);
			txtInQty.setText(inQty.toString());
		}

		txtIterations.setText(iterations == null ? "" : iterations.toString());
		rbArithmetic.setSelected(priceIncrType == PriceIncrType.ARITHMETIC);
		rbGeometric.setSelected(priceIncrType == PriceIncrType.GEOMETRIC);
		rbOverLastOrder.setSelected(qtyIncrType == QtyIncrType.ORDER);
		rbOverPosition.setSelected(qtyIncrType == QtyIncrType.POSITION);
		txtPriceIncr.setText(Convert.toStrPercent(priceIncr));
		txtCoinsIncr.setText(Convert.toStrPercent(qtyIncr));
		txtStopLoss.setText(Convert.toStrPercent(stopLoss));
		txtTProfit.setText(Convert.toStrPercent(takeProfit));

		generateGrid();
	}

	private void generateGrid()
	{
		generateGridStandard();

		if (gridSide != null)
		{
			if (gridSide.equals(PositionSide.SHORT))
				createShort();
			else if (gridSide.equals(PositionSide.LONG))
				createLong();
		}
	}

	private void generateGridStandard()
	{
		int iterations = Integer.valueOf(txtIterations.getText());
		double priceIncr = Convert.strPercentToDbl(txtPriceIncr.getText());
		double qtyIncr = Convert.strPercentToDbl(txtCoinsIncr.getText());

		if (iterations >= 1)
		{
			txtGPrice1.setText(Convert.toStrPercent(priceIncr));
			txtGQty1.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice1.setText("");
			txtGQty1.setText("");
		}
		if (iterations >= 2)
		{
			txtGPrice2.setText(Convert.toStrPercent(priceIncr));
			txtGQty2.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice2.setText("");
			txtGQty2.setText("");
		}
		if (iterations >= 3)
		{
			txtGPrice3.setText(Convert.toStrPercent(priceIncr));
			txtGQty3.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice3.setText("");
			txtGQty3.setText("");
		}
		if (iterations >= 4)
		{
			txtGPrice4.setText(Convert.toStrPercent(priceIncr));
			txtGQty4.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice4.setText("");
			txtGQty4.setText("");
		}
		if (iterations >= 5)
		{
			txtGPrice5.setText(Convert.toStrPercent(priceIncr));
			txtGQty5.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice5.setText("");
			txtGQty5.setText("");
		}
		if (iterations >= 6)
		{
			txtGPrice6.setText(Convert.toStrPercent(priceIncr));
			txtGQty6.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice6.setText("");
			txtGQty6.setText("");
		}
		if (iterations >= 7)
		{
			txtGPrice7.setText(Convert.toStrPercent(priceIncr));
			txtGQty7.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice7.setText("");
			txtGQty7.setText("");
		}
		if (iterations >= 8)
		{
			txtGPrice8.setText(Convert.toStrPercent(priceIncr));
			txtGQty8.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice8.setText("");
			txtGQty8.setText("");
		}
		if (iterations >= 9)
		{
			txtGPrice9.setText(Convert.toStrPercent(priceIncr));
			txtGQty9.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice9.setText("");
			txtGQty9.setText("");
		}
		if (iterations >= 10)
		{
			txtGPrice10.setText(Convert.toStrPercent(priceIncr));
			txtGQty10.setText(Convert.toStrPercent(qtyIncr));
		}
		else
		{
			txtGPrice10.setText("");
			txtGQty10.setText("");
		}
	}

	// --------------------------------------------------------------------

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

		if (isNullOrEmpty(txtGPrice9.getText()) || isNullOrEmpty(txtGQty9.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice9.getText()),
			Convert.strPercentToDbl(txtGQty9.getText())));

		if (isNullOrEmpty(txtGPrice10.getText()) || isNullOrEmpty(txtGQty10.getText()))
		{
			return lstPriceQty;
		}

		lstPriceQty.add(new PriceQty(
			Convert.strPercentToDbl(txtGPrice10.getText()),
			Convert.strPercentToDbl(txtGQty10.getText())));

		return lstPriceQty;
	}

	private GridPosition acquireArguments(PositionSide gridSide) throws Exception
	{
		Double stopLoss = Convert.strPercentToDbl(txtStopLoss.getText());
		Double takeProfit = Convert.strPercentToDbl(txtTProfit.getText());
		List<PriceQty> lstPriceQty = acquireGrid();

		GridPosition gridPosition = new GridPosition(symbol, gridSide, stopLoss, chkStopLoss.isSelected(), takeProfit, chkTProfit.isSelected(), lstPriceQty);
		gridPosition.setPriceIncrType(rbArithmetic.isSelected() ? PriceIncrType.ARITHMETIC : PriceIncrType.GEOMETRIC);
		gridPosition.setQtyIncrType(rbOverLastOrder.isSelected() ? QtyIncrType.ORDER : QtyIncrType.POSITION);

		if (chkLastPrice.isSelected())
		{
			gridPosition.withLastPrice();
		}
		else
		{
			gridPosition.withInPrice(Double.valueOf(txtInPrice.getText()));
		}

		if (cmbQtyType.getSelectedItem().equals(QuantityType.USD))
		{
			double usd = Double.valueOf(txtInQty.getText());
			gridPosition.withUsdAmount(usd);
		}
		else if (cmbQtyType.getSelectedItem().equals(QuantityType.BALANCE))
		{
			double balancePercent = Convert.strPercentToDbl(txtInQty.getText());
			gridPosition.withBalancePercent(balancePercent);
		}
		else
		{
			double inQty = Double.valueOf(txtInQty.getText());
			gridPosition.withInQty(inQty);
		}

		return gridPosition;
	}

	private static boolean isNullOrEmpty(String txt)
	{
		return (txt == null || txt.isBlank());
	}

	// --------------------------------------------------------------------

	private void createShort()
	{
		if (symbol == null)
		{
			ctrlError.ERROR("Select symbol");
			return;
		}

		ctrlError.CLEAN();
		try
		{
			gridSide = PositionSide.SHORT;
			GridPosition gridPosition = acquireArguments(PositionSide.SHORT);
			gridTrade = new GridTrade(gridPosition);
			gridTrade.createShort();

			//-----------------------------------------------------------------
			txtResult.setForeground(Styles.COLOR_TEXT_SHORT);
			txtResult.setText(toString(gridPosition));

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void createLong()
	{
		if (symbol == null)
		{
			ctrlError.ERROR("Select symbol");
			return;
		}

		ctrlError.CLEAN();
		try
		{
			gridSide = PositionSide.LONG;
			GridPosition gridPosition = acquireArguments(PositionSide.LONG);
			gridTrade = new GridTrade(gridPosition);
			gridTrade.createLong();

			//-----------------------------------------------------------------
			txtResult.setForeground(Styles.COLOR_TEXT_LONG);
			txtResult.setText(toString(gridPosition));

			btnPostFirst.setEnabled(!isOpenPosition);
			btnPostOthers.setEnabled(isOpenPosition);
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void clean()
	{
		gridSide = null;
		gridTrade = null;
		ctrlError.CLEAN();
		txtResult.setText("");

		btnShort.setEnabled(true);
		btnLong.setEnabled(true);
		btnPostFirst.setEnabled(false);
		btnPostOthers.setEnabled(false);
	}
	
	// --------------------------------------------------------------------
	
	private void post(PostStyle postStyle)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		ctrlError.CLEAN();
		try
		{
			String result = gridTrade.post(postStyle);
			if (result != null)
			{
				ctrlError.ERROR(result);
			}

			txtResult.setForeground(gridTrade.getGridPosition().getPositionSide() == PositionSide.SHORT ? Styles.COLOR_TEXT_SHORT : Styles.COLOR_TEXT_LONG);
			txtResult.setText(toString(gridTrade.getGridPosition()));
			save(symbol.getPair() + "_" + gridTrade.getGridPosition().getPositionSide().name());

			// --------------------------------------------------------------------
			btnPostFirst.setEnabled(false);
			btnPostOthers.setEnabled(postStyle.equals(PostStyle.FIRST));
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// --------------------------------------------------------------------

	public String toString(GridPosition gridPosition)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(String.format("%s %s - in Price: %s - in Coins: %s", gridPosition.getPositionSide().name(), gridPosition.getSymbol().getPair(), gridPosition.getInPriceStr(), gridPosition.getInQtyStr()));
		sb.append("\n\n");

		String labels = String.format("%-3s %8s %8s %12s %12s %12s %12s %12s %12s | %12s %10s %12s %12s", "#", "TYPE", CharConstants.ARROW_UP + " %", "PRICE", "QTY", "USD", CharConstants.SIGNA + " QTY", CharConstants.SIGNA + " USD", "USD LOST", "AVG-PRICE", CharConstants.ARROW_UP + CharConstants.ARROW_DOWN + " %", "TP-PRICE", "PROFIT");

		sb.append(labels);
		sb.append("\n");
		sb.append(StringUtils.repeat("-",151));
		sb.append("\n");

		for (GridOrder entry : gridPosition.getLstOrders())
		{
			if(entry.getOpType() == OpType.STOP_LOSS)
			{
				sb.append(StringUtils.repeat("-",151));
				sb.append("\n");
			}

			String line = String.format("%-3s %8s %8.2f %12s %12s %12s %12s %12s %12s | %12s %10.2f %12s %12s",
					entry.getNumber(),
					entry.getOpType() == OpType.ORDER ? entry.getOrderSide().name() : entry.getOpType() ==  OpType.STOP_LOSS ? "S.LOSS" : entry.getOpType() ==  OpType.TAKE_PROFIT ? "T.PROFIT" : "",
					entry.getDistance() * 100,
					symbol.priceToStr(entry.getPrice()),
					symbol.qtyToStr(entry.getQuantity()),
					Convert.usdToStr(entry.getUsd()),
					symbol.qtyToStr(entry.getSumCoins()),
					Convert.usdToStr(entry.getSumUsd()),
					Convert.usdToStr(entry.getLost()),
					symbol.priceToStr(entry.getNewPrice()),
					entry.getRecoveryNeeded() * 100,
					symbol.priceToStr(entry.getTakeProfit()),
					Convert.usdToStr(entry.getProfit()));

			sb.append(line);
			sb.append("\n");

			if (entry.getStatus() != null)
			{
				sb.append(entry.getStatus());
				if (entry.getResult() != null)
				{
					sb.append(" / " + entry.getResult());
				}
				sb.append("\n\n");
			}

		}

		return sb.toString();
	}

	// --------------------------------------------------------------------

	private void save(String filename)
	{
		try
		{
			File path = new File(UIConstants.DEFAULT_USER_FOLDER, UIConstants.DEFAULT_LOG_FOLDER);

			if (!path.exists())
			{
				path.mkdirs();
			}

			File logFile = new File(path, filename + ".log");
			FileUtils.writeStringToFile(logFile, txtResult.getText(), StandardCharsets.UTF_8);
		}
		catch (IOException e)
		{
			UILog.error(e);
		}
	}

}
