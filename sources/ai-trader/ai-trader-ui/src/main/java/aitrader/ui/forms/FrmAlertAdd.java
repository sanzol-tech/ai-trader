package aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Alert;
import aitrader.core.model.Symbol;
import aitrader.core.model.SymbolInfo;
import aitrader.core.model.enums.OrderSide;
import aitrader.core.model.enums.QuantityType;
import aitrader.core.service.signals.AlertService;
import aitrader.core.service.symbol.SymbolInfoService;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.controls.CtrlError;
import aitrader.util.Convert;

public class FrmAlertAdd extends JFrame
{
	private static final long serialVersionUID = 1L;

	enum TTLInterval
	{
		none("none", TimeUnit.DAYS.toMillis(365)), 
		_5m("5 min", TimeUnit.MINUTES.toMillis(5)), 
		_15m("15 min", TimeUnit.MINUTES.toMillis(15)), 
		_30m("30 min", TimeUnit.MINUTES.toMillis(30)),
		_1h("1 hour", TimeUnit.HOURS.toMillis(1)), 
		_2h("2 hours", TimeUnit.HOURS.toMillis(2)), 
		_4h("4 hours", TimeUnit.HOURS.toMillis(4)), 
		_8h("8 hours", TimeUnit.HOURS.toMillis(8)),
		_1d("1 day", TimeUnit.DAYS.toMillis(1)), 
		_3d("3 days", TimeUnit.DAYS.toMillis(3)), 
		_7d("7 days", TimeUnit.DAYS.toMillis(7)), 
		_14d("14 days", TimeUnit.DAYS.toMillis(14)), 
		_30d("30 days", TimeUnit.DAYS.toMillis(30));

		private final String name;
		private final Long millis;

		TTLInterval(String name, Long millis)
		{
			this.name = name;
			this.millis = millis;
		}

		public Long getMillis()
		{
			return millis;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
	
	private static final String TITLE = UIConstants.APP_NAME + " - Alert";

	private Symbol symbol;

	private CtrlError ctrlError;
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;

	private JButton btnAuto;
	
	private JCheckBox chkShort;
	private JCheckBox chkLong;
	private JTextField txtShortAlert;
	private JTextField txtShortLimit;
	private JTextField txtLongAlert;
	private JTextField txtLongLimit;

	private JCheckBox chkOpenOrder; 
	private JComboBox<QuantityType> cmbQtyType;
	private JComboBox<TTLInterval> cmbTTL;
	private JTextField txtQuantity;

	public FrmAlertAdd()
	{
		initComponents();
		
		txtSymbolRight.setText(CoreConfig.getDefaultSymbolRight());
		
		txtQuantity = new JTextField();
		txtQuantity.setHorizontalAlignment(SwingConstants.TRAILING);
		txtQuantity.setBounds(226, 215, 86, 20);
		getContentPane().add(txtQuantity);
		txtQuantity.setColumns(10);
	}

	private void initComponents()
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 390);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmAlertAdd.class.getResource("/resources/bell.png")));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(22, 310, 290, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		getContentPane().add(pnlBottom);

		ctrlError = new CtrlError();
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(ctrlError, BorderLayout.CENTER);
		
		JLabel lblSymbol = new JLabel("SYMBOL");
		lblSymbol.setBounds(22, 28, 46, 14);
		getContentPane().add(lblSymbol);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setBounds(22, 45, 86, 20);
		getContentPane().add(txtSymbolLeft);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setBounds(118, 45, 86, 20);
		getContentPane().add(txtSymbolRight);

		txtShortAlert = new JTextField();
		txtShortAlert.setBounds(121, 144, 86, 20);
		getContentPane().add(txtShortAlert);

		txtShortLimit = new JTextField();
		txtShortLimit.setBounds(226, 144, 86, 20);
		getContentPane().add(txtShortLimit);

		JButton btnSearch = new JButton(Styles.IMAGE_SEARCH);
		btnSearch.setBounds(22, 76, 182, 23);
		getContentPane().add(btnSearch);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(212, 263, 100, 23);
		getContentPane().add(btnSave);
		
		JLabel lblAlert = new JLabel("Alert");
		lblAlert.setBounds(121, 124, 46, 14);
		getContentPane().add(lblAlert);

		JLabel lblLimit = new JLabel("Limit");
		lblLimit.setBounds(226, 124, 46, 14);
		getContentPane().add(lblLimit);

		chkOpenOrder = new JCheckBox("Post order");
		chkOpenOrder.setBounds(22, 214, 92, 23);
		getContentPane().add(chkOpenOrder);
		
		txtLongAlert = new JTextField();
		txtLongAlert.setBounds(121, 175, 86, 20);
		getContentPane().add(txtLongAlert);
		
		txtLongLimit = new JTextField();
		txtLongLimit.setBounds(226, 175, 86, 20);
		getContentPane().add(txtLongLimit);

		chkShort = new JCheckBox("SHORT");
		chkShort.setBounds(22, 143, 80, 23);
		getContentPane().add(chkShort);

		chkLong = new JCheckBox("LONG");
		chkLong.setBounds(22, 174, 80, 23);
		getContentPane().add(chkLong);

		JLabel lblTTL = new JLabel("TTL");
		lblTTL.setBounds(28, 267, 30, 14);
		getContentPane().add(lblTTL);

		cmbQtyType = new JComboBox<QuantityType>();
		cmbQtyType.setBounds(121, 214, 86, 22);
		cmbQtyType.setModel(new DefaultComboBoxModel<QuantityType>(QuantityType.values()));
		getContentPane().add(cmbQtyType);
		
		cmbTTL = new JComboBox<TTLInterval>();
		cmbTTL.setBounds(71, 263, 80, 22);
		cmbTTL.setModel(new DefaultComboBoxModel<TTLInterval>(TTLInterval.values()));
		getContentPane().add(cmbTTL);

		btnAuto = new JButton("Auto");
		btnAuto.setEnabled(false);
		btnAuto.setBounds(226, 45, 86, 55);
		getContentPane().add(btnAuto);

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

		btnAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				auto();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});

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
			
			btnAuto.setEnabled(true);
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void auto()
	{
		try
		{
			if (symbol != null)
			{
				BigDecimal shLimit = SymbolInfoService.getSymbolInfo(symbol.getPair()).getHigh24h();
				BigDecimal shAlert = shLimit.multiply(BigDecimal.valueOf(0.997)).setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);
				txtShortAlert.setText(shAlert.toString());
				txtShortLimit.setText(shLimit.toString());
				chkShort.setSelected(true);

				BigDecimal lgLimit = SymbolInfoService.getSymbolInfo(symbol.getPair()).getLow24h();
				BigDecimal lgAlert = lgLimit.multiply(BigDecimal.valueOf(1.003)).setScale(symbol.getPricePrecision(), RoundingMode.HALF_UP);
				txtLongAlert.setText(lgAlert.toString());
				txtLongLimit.setText(lgLimit.toString());
				chkLong.setSelected(true);
			}
		}
		catch(Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	private void save()
	{
		Long expirationAt = ((TTLInterval) cmbTTL.getSelectedItem()).getMillis();

		QuantityType quantityType = null;
		BigDecimal quantity = null;
		if (chkOpenOrder.isSelected())
		{
			quantityType = (QuantityType) cmbQtyType.getSelectedItem();
			quantity = Convert.toBigDecimal(txtQuantity.getText());
		}

		if (chkShort.isSelected())
		{
			BigDecimal alertPrice = Convert.toBigDecimal(txtShortAlert.getText());
			BigDecimal limitPrice = Convert.toBigDecimal(txtShortLimit.getText());
			Alert alert = new Alert(symbol, OrderSide.SELL.name(), alertPrice, limitPrice, quantityType, quantity, expirationAt);
			AlertService.addAlert(alert);
		}

		if (chkLong.isSelected())
		{
			BigDecimal alertPrice = Convert.toBigDecimal(txtLongAlert.getText());
			BigDecimal limitPrice = Convert.toBigDecimal(txtLongLimit.getText());
			Alert alert = new Alert(symbol, OrderSide.BUY.name(), alertPrice, limitPrice, quantityType, quantity, expirationAt);
			AlertService.addAlert(alert);
		}

		ctrlError.INFO("Alert created");
	}

	// --------------------------------------------------------------------

	public static void launch(Symbol symbol)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmAlertAdd frame = new FrmAlertAdd();
					frame.setVisible(true);
					
					if (symbol != null)
					{
						frame.symbol = symbol;
						frame.txtSymbolLeft.setText(symbol.getNameLeft());
						frame.search();
					}
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
