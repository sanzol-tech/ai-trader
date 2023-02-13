package aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import aitrader.core.config.CoreConfig;
import aitrader.core.model.Symbol;
import aitrader.core.service.market.TechnicalStatus;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.controls.CtrlError;
import aitrader.ui.controls.CtrlStatus;
import aitrader.util.price.PriceUtil;
import binance.futures.enums.IntervalType;
import technicals.indicators.complex.TechnicalRatings;
import technicals.model.TechCandle;
import technicals.util.CandleUtils;

public class FrmCoinTech extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME;

	private Symbol symbol;

	private JPanel contentPane;

	private JPanel pnlBottom;
	private CtrlError ctrlError;

	private JTextField txtADX4h;
	private JTextField txtAO4h;
	private JTextField txtBBP4h;
	private JTextField txtCCI4h;
	private JTextField txtChgOC4h;
	private JTextField txtRsi4h;
	private JTextField txtStochastic4h;
	private JTextField txtStochRsi4h;
	private JTextField txtUO4h;
	private JTextField txtVolume4h;
	private JTextField txtWilliamsR4h;
	private JTextField txtMACD4h;
	private JTextField txtEMA1_4h;
	private JTextField txtEMA2_4h;
	private JTextField txtEMA3_4h;
	
	private CtrlStatus statusADX4h;
	private CtrlStatus statusAO4h;
	private CtrlStatus statusBBP4h;
	private CtrlStatus statusCCI4h;
	private CtrlStatus statusChange4h;
	private CtrlStatus statusMACD4h;
	private CtrlStatus statusRsi4h;
	private CtrlStatus statusStoch4h;
	private CtrlStatus statusStochRsi4h;
	private CtrlStatus statusUO4h;
	private CtrlStatus statusVolume4h;
	private CtrlStatus statusWilliamsR4h;
	private CtrlStatus statusEMA1_4h;
	private CtrlStatus statusEMA2_4h;
	private CtrlStatus statusEMA3_4h;
	
	private JTextField txtADX24h;
	private JTextField txtAO24h;
	private JTextField txtBBP24h;
	private JTextField txtCCI24h;
	private JTextField txtChgOC24h;
	private JTextField txtRsi24h;
	private JTextField txtStochastic24h;
	private JTextField txtStochRsi24h;
	private JTextField txtUO24h;
	private JTextField txtVolume24h;
	private JTextField txtWilliamsR24h;
	private JTextField txtMACD24h;
	private JTextField txtEMA1_24h;
	private JTextField txtEMA2_24h;
	private JTextField txtEMA3_24h;
	
	private CtrlStatus statusADX24h;
	private CtrlStatus statusAO24h;
	private CtrlStatus statusBBP24h;
	private CtrlStatus statusCCI24h;
	private CtrlStatus statusChange24h;
	private CtrlStatus statusMACD24h;
	private CtrlStatus statusRsi24h;
	private CtrlStatus statusStoch24h;
	private CtrlStatus statusStochRsi24h;
	private CtrlStatus statusUO24h;
	private CtrlStatus statusVolume24h;
	private CtrlStatus statusWilliamsR24h;
	private CtrlStatus statusEMA1_24h;
	private CtrlStatus statusEMA2_24h;
	private CtrlStatus statusEMA3_24h;

	private JTextField txtADX14d;
	private JTextField txtAO14d;
	private JTextField txtBBP14d;
	private JTextField txtCCI14d;
	private JTextField txtChgOC14d;
	private JTextField txtRsi14d;
	private JTextField txtStochastic14d;
	private JTextField txtStochRsi14d;
	private JTextField txtUO14d;
	private JTextField txtVolume14d;
	private JTextField txtWilliamsR14d;
	private JTextField txtMACD14d;
	private JTextField txtEMA1_14d;
	private JTextField txtEMA2_14d;
	private JTextField txtEMA3_14d;
	
	private CtrlStatus statusADX14d;
	private CtrlStatus statusAO14d;
	private CtrlStatus statusBBP14d;
	private CtrlStatus statusCCI14d;
	private CtrlStatus statusChange14d;
	private CtrlStatus statusMACD14d;
	private CtrlStatus statusRsi14d;
	private CtrlStatus statusStoch14d;
	private CtrlStatus statusStochRsi14d;
	private CtrlStatus statusUO14d;
	private CtrlStatus statusVolume14d;
	private CtrlStatus statusWilliamsR14d;
	private CtrlStatus statusEMA1_14d;
	private CtrlStatus statusEMA2_14d;
	private CtrlStatus statusEMA3_14d;
	
	public FrmCoinTech()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(20, 528, 742, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);

		ctrlError = new CtrlError();
		pnlBottom.add(ctrlError, BorderLayout.CENTER);
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));

		JPanel panel4h = new JPanel();
		panel4h.setLayout(null);
		panel4h.setBorder(UIManager.getBorder("TextField.border"));
		panel4h.setBounds(20, 32, 234, 478);
		contentPane.add(panel4h);

		JPanel panel24h = new JPanel();
		panel24h.setLayout(null);
		panel24h.setBorder(UIManager.getBorder("TextField.border"));
		panel24h.setBounds(274, 32, 234, 478);
		contentPane.add(panel24h);

		JPanel panel14d = new JPanel();
		panel14d.setLayout(null);
		panel14d.setBorder(UIManager.getBorder("TextField.border"));
		panel14d.setBounds(528, 32, 234, 478);
		contentPane.add(panel14d);
		
		JLabel lblLast4h = new JLabel("5m");
		lblLast4h.setBounds(20, 11, 132, 16);
		contentPane.add(lblLast4h);

		JLabel lblLast24h = new JLabel("1h");
		lblLast24h.setBounds(274, 11, 132, 16);
		contentPane.add(lblLast24h);

		JLabel lblLast14d = new JLabel("1d");
		lblLast14d.setBounds(528, 11, 132, 16);
		contentPane.add(lblLast14d);

		
		
		JLabel lblVolume4h = new JLabel("Volume 4h");
		lblVolume4h.setBounds(20, 23, 68, 14);
		panel4h.add(lblVolume4h);

		JLabel lblChgOC4h = new JLabel("Change 4h");
		lblChgOC4h.setBounds(20, 48, 68, 14);
		panel4h.add(lblChgOC4h);

		JLabel lblRsi4h = new JLabel("RSI");
		lblRsi4h.setBounds(20, 133, 62, 14);
		panel4h.add(lblRsi4h);

		JLabel lblStochastic4h = new JLabel("Stochastic");
		lblStochastic4h.setBounds(20, 108, 62, 14);
		panel4h.add(lblStochastic4h);

		JLabel lblStochRsi4h = new JLabel("Stoch RSI");
		lblStochRsi4h.setBounds(20, 158, 62, 14);
		panel4h.add(lblStochRsi4h);

		JLabel lblNivelMacd4h = new JLabel("UO");
		lblNivelMacd4h.setBounds(20, 208, 62, 14);
		panel4h.add(lblNivelMacd4h);

		JLabel lblWilliamsR4h = new JLabel("Williams R");
		lblWilliamsR4h.setBounds(20, 183, 62, 14);
		panel4h.add(lblWilliamsR4h);
		
		JLabel lblMacd_4h = new JLabel("MACD");
		lblMacd_4h.setBounds(20, 233, 62, 14);
		panel4h.add(lblMacd_4h);

		JLabel lblAdx_4h = new JLabel("ADX");
		lblAdx_4h.setBounds(20, 258, 62, 14);
		panel4h.add(lblAdx_4h);
		
		JLabel lblCci_4h = new JLabel("CCI");
		lblCci_4h.setBounds(20, 283, 62, 14);
		panel4h.add(lblCci_4h);
		
		JLabel lblAo_4h = new JLabel("AO");
		lblAo_4h.setBounds(20, 308, 62, 14);
		panel4h.add(lblAo_4h);
		
		JLabel lblBbp_4h = new JLabel("BBP");
		lblBbp_4h.setBounds(20, 333, 62, 14);
		panel4h.add(lblBbp_4h);
		
		JLabel lblEma1_4h = new JLabel("EMA 10");
		lblEma1_4h.setBounds(20, 363, 62, 14);
		panel4h.add(lblEma1_4h);
		
		JLabel lblEma2_4h = new JLabel("EMA 30");
		lblEma2_4h.setBounds(20, 388, 62, 14);
		panel4h.add(lblEma2_4h);
		
		JLabel lblEma3_4h = new JLabel("EMA 100");
		lblEma3_4h.setBounds(20, 413, 62, 14);
		panel4h.add(lblEma3_4h);

		txtRsi4h = new JTextField();
		txtRsi4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi4h.setEditable(false);
		txtRsi4h.setBounds(94, 130, 86, 20);
		panel4h.add(txtRsi4h);

		txtChgOC4h = new JTextField();
		txtChgOC4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC4h.setEditable(false);
		txtChgOC4h.setColumns(10);
		txtChgOC4h.setBounds(94, 45, 86, 20);
		panel4h.add(txtChgOC4h);

		txtVolume4h = new JTextField();
		txtVolume4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume4h.setEditable(false);
		txtVolume4h.setColumns(10);
		txtVolume4h.setBounds(94, 20, 86, 20);
		panel4h.add(txtVolume4h);

		txtStochastic4h = new JTextField();
		txtStochastic4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic4h.setEditable(false);
		txtStochastic4h.setBounds(94, 105, 86, 20);
		panel4h.add(txtStochastic4h);

		txtStochRsi4h = new JTextField();
		txtStochRsi4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi4h.setEditable(false);
		txtStochRsi4h.setBounds(94, 155, 86, 20);
		panel4h.add(txtStochRsi4h);

		txtUO4h = new JTextField();
		txtUO4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO4h.setEditable(false);
		txtUO4h.setBounds(94, 205, 86, 20);
		panel4h.add(txtUO4h);

		txtWilliamsR4h = new JTextField();
		txtWilliamsR4h.setBounds(94, 180, 86, 20);
		txtWilliamsR4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR4h.setEditable(false);
		panel4h.add(txtWilliamsR4h);
		
		txtMACD4h = new JTextField();
		txtMACD4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMACD4h.setEditable(false);
		txtMACD4h.setBounds(94, 230, 86, 20);
		panel4h.add(txtMACD4h);

		txtCCI4h = new JTextField();
		txtCCI4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtCCI4h.setEditable(false);
		txtCCI4h.setBounds(94, 280, 86, 20);
		panel4h.add(txtCCI4h);
		
		txtADX4h = new JTextField();
		txtADX4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtADX4h.setEditable(false);
		txtADX4h.setBounds(94, 255, 86, 20);
		panel4h.add(txtADX4h);
		
		txtAO4h = new JTextField();
		txtAO4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtAO4h.setEditable(false);
		txtAO4h.setBounds(94, 305, 86, 20);
		panel4h.add(txtAO4h);
		
		txtEMA3_4h = new JTextField();
		txtEMA3_4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA3_4h.setEditable(false);
		txtEMA3_4h.setBounds(94, 410, 86, 20);
		panel4h.add(txtEMA3_4h);
		
		txtEMA2_4h = new JTextField();
		txtEMA2_4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA2_4h.setEditable(false);
		txtEMA2_4h.setBounds(94, 385, 86, 20);
		panel4h.add(txtEMA2_4h);
		
		txtEMA1_4h = new JTextField();
		txtEMA1_4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA1_4h.setEditable(false);
		txtEMA1_4h.setBounds(94, 360, 86, 20);
		panel4h.add(txtEMA1_4h);
		
		txtBBP4h = new JTextField();
		txtBBP4h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBBP4h.setEditable(false);
		txtBBP4h.setBounds(94, 330, 86, 20);
		panel4h.add(txtBBP4h);
		
		statusStochRsi4h = new CtrlStatus();
		statusStochRsi4h.setBounds(186, 155, 30, 19);
		panel4h.add(statusStochRsi4h);

		statusUO4h = new CtrlStatus();
		statusUO4h.setBounds(186, 205, 30, 19);
		panel4h.add(statusUO4h);

		statusWilliamsR4h = new CtrlStatus();
		statusWilliamsR4h.setBounds(186, 180, 30, 19);
		panel4h.add(statusWilliamsR4h);
		
		statusVolume4h = new CtrlStatus();
		statusVolume4h.setBounds(186, 20, 30, 19);
		panel4h.add(statusVolume4h);

		statusChange4h = new CtrlStatus();
		statusChange4h.setBounds(186, 45, 30, 19);
		panel4h.add(statusChange4h);

		statusRsi4h = new CtrlStatus();
		statusRsi4h.setBounds(186, 130, 30, 19);
		panel4h.add(statusRsi4h);

		statusStoch4h = new CtrlStatus();
		statusStoch4h.setBounds(186, 105, 30, 19);
		panel4h.add(statusStoch4h);
		
		statusADX4h = new CtrlStatus();
		statusADX4h.setBounds(186, 255, 30, 19);
		panel4h.add(statusADX4h);
		
		statusCCI4h = new CtrlStatus();
		statusCCI4h.setBounds(186, 280, 30, 19);
		panel4h.add(statusCCI4h);

		statusAO4h = new CtrlStatus();
		statusAO4h.setBounds(186, 305, 30, 19);
		panel4h.add(statusAO4h);
		
		statusBBP4h = new CtrlStatus();
		statusBBP4h.setBounds(186, 330, 30, 19);
		panel4h.add(statusBBP4h);
		
		statusEMA1_4h = new CtrlStatus();
		statusEMA1_4h.setBounds(186, 360, 30, 19);
		panel4h.add(statusEMA1_4h);
		
		statusEMA3_4h = new CtrlStatus();
		statusEMA3_4h.setBounds(186, 385, 30, 19);
		panel4h.add(statusEMA3_4h);
		
		statusEMA2_4h = new CtrlStatus();
		statusEMA2_4h.setBounds(186, 410, 30, 19);
		panel4h.add(statusEMA2_4h);
		
		statusMACD4h = new CtrlStatus();
		statusMACD4h.setBounds(186, 230, 30, 19);
		panel4h.add(statusMACD4h);
	
		
		JLabel lblVolume24h = new JLabel("Volume 3d");
		lblVolume24h.setBounds(20, 23, 68, 14);
		panel24h.add(lblVolume24h);

		JLabel lblChgOC24h = new JLabel("Change 3d");
		lblChgOC24h.setBounds(20, 48, 68, 14);
		panel24h.add(lblChgOC24h);

		JLabel lblRsi24h = new JLabel("RSI");
		lblRsi24h.setBounds(20, 133, 62, 14);
		panel24h.add(lblRsi24h);

		JLabel lblStochastic24h = new JLabel("Stochastic");
		lblStochastic24h.setBounds(20, 108, 62, 14);
		panel24h.add(lblStochastic24h);

		JLabel lblStochRsi24h = new JLabel("Stoch RSI");
		lblStochRsi24h.setBounds(20, 158, 62, 14);
		panel24h.add(lblStochRsi24h);

		JLabel lblNivelMacd24h = new JLabel("UO");
		lblNivelMacd24h.setBounds(20, 208, 62, 14);
		panel24h.add(lblNivelMacd24h);

		JLabel lblWilliamsR24h = new JLabel("Williams R");
		lblWilliamsR24h.setBounds(20, 183, 62, 14);
		panel24h.add(lblWilliamsR24h);
		
		JLabel lblMacd_24h = new JLabel("MACD");
		lblMacd_24h.setBounds(20, 233, 62, 14);
		panel24h.add(lblMacd_24h);

		JLabel lblAdx_24h = new JLabel("ADX");
		lblAdx_24h.setBounds(20, 258, 62, 14);
		panel24h.add(lblAdx_24h);
		
		JLabel lblCci_24h = new JLabel("CCI");
		lblCci_24h.setBounds(20, 283, 62, 14);
		panel24h.add(lblCci_24h);
		
		JLabel lblAo_24h = new JLabel("AO");
		lblAo_24h.setBounds(20, 308, 62, 14);
		panel24h.add(lblAo_24h);
		
		JLabel lblBbp_24h = new JLabel("BBP");
		lblBbp_24h.setBounds(20, 333, 62, 14);
		panel24h.add(lblBbp_24h);
		
		JLabel lblEma1_24h = new JLabel("EMA 10");
		lblEma1_24h.setBounds(20, 363, 62, 14);
		panel24h.add(lblEma1_24h);
		
		JLabel lblEma2_24h = new JLabel("EMA 30");
		lblEma2_24h.setBounds(20, 388, 62, 14);
		panel24h.add(lblEma2_24h);
		
		JLabel lblEma3_24h = new JLabel("EMA 100");
		lblEma3_24h.setBounds(20, 413, 62, 14);
		panel24h.add(lblEma3_24h);

		txtRsi24h = new JTextField();
		txtRsi24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi24h.setEditable(false);
		txtRsi24h.setBounds(94, 130, 86, 20);
		panel24h.add(txtRsi24h);

		txtChgOC24h = new JTextField();
		txtChgOC24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC24h.setEditable(false);
		txtChgOC24h.setColumns(10);
		txtChgOC24h.setBounds(94, 45, 86, 20);
		panel24h.add(txtChgOC24h);

		txtVolume24h = new JTextField();
		txtVolume24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume24h.setEditable(false);
		txtVolume24h.setColumns(10);
		txtVolume24h.setBounds(94, 20, 86, 20);
		panel24h.add(txtVolume24h);

		txtStochastic24h = new JTextField();
		txtStochastic24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic24h.setEditable(false);
		txtStochastic24h.setBounds(94, 105, 86, 20);
		panel24h.add(txtStochastic24h);

		txtStochRsi24h = new JTextField();
		txtStochRsi24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi24h.setEditable(false);
		txtStochRsi24h.setBounds(94, 155, 86, 20);
		panel24h.add(txtStochRsi24h);

		txtUO24h = new JTextField();
		txtUO24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO24h.setEditable(false);
		txtUO24h.setBounds(94, 205, 86, 20);
		panel24h.add(txtUO24h);

		txtWilliamsR24h = new JTextField();
		txtWilliamsR24h.setBounds(94, 180, 86, 20);
		txtWilliamsR24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR24h.setEditable(false);
		panel24h.add(txtWilliamsR24h);
		
		txtMACD24h = new JTextField();
		txtMACD24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMACD24h.setEditable(false);
		txtMACD24h.setBounds(94, 230, 86, 20);
		panel24h.add(txtMACD24h);

		txtCCI24h = new JTextField();
		txtCCI24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtCCI24h.setEditable(false);
		txtCCI24h.setBounds(94, 280, 86, 20);
		panel24h.add(txtCCI24h);
		
		txtADX24h = new JTextField();
		txtADX24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtADX24h.setEditable(false);
		txtADX24h.setBounds(94, 255, 86, 20);
		panel24h.add(txtADX24h);
		
		txtAO24h = new JTextField();
		txtAO24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtAO24h.setEditable(false);
		txtAO24h.setBounds(94, 305, 86, 20);
		panel24h.add(txtAO24h);
		
		txtEMA3_24h = new JTextField();
		txtEMA3_24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA3_24h.setEditable(false);
		txtEMA3_24h.setBounds(94, 410, 86, 20);
		panel24h.add(txtEMA3_24h);
		
		txtEMA2_24h = new JTextField();
		txtEMA2_24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA2_24h.setEditable(false);
		txtEMA2_24h.setBounds(94, 385, 86, 20);
		panel24h.add(txtEMA2_24h);
		
		txtEMA1_24h = new JTextField();
		txtEMA1_24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA1_24h.setEditable(false);
		txtEMA1_24h.setBounds(94, 360, 86, 20);
		panel24h.add(txtEMA1_24h);
		
		txtBBP24h = new JTextField();
		txtBBP24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBBP24h.setEditable(false);
		txtBBP24h.setBounds(94, 330, 86, 20);
		panel24h.add(txtBBP24h);
		
		statusStochRsi24h = new CtrlStatus();
		statusStochRsi24h.setBounds(186, 155, 30, 19);
		panel24h.add(statusStochRsi24h);

		statusUO24h = new CtrlStatus();
		statusUO24h.setBounds(186, 205, 30, 19);
		panel24h.add(statusUO24h);

		statusWilliamsR24h = new CtrlStatus();
		statusWilliamsR24h.setBounds(186, 180, 30, 19);
		panel24h.add(statusWilliamsR24h);
		
		statusVolume24h = new CtrlStatus();
		statusVolume24h.setBounds(186, 20, 30, 19);
		panel24h.add(statusVolume24h);

		statusChange24h = new CtrlStatus();
		statusChange24h.setBounds(186, 45, 30, 19);
		panel24h.add(statusChange24h);

		statusRsi24h = new CtrlStatus();
		statusRsi24h.setBounds(186, 130, 30, 19);
		panel24h.add(statusRsi24h);

		statusStoch24h = new CtrlStatus();
		statusStoch24h.setBounds(186, 105, 30, 19);
		panel24h.add(statusStoch24h);
		
		statusADX24h = new CtrlStatus();
		statusADX24h.setBounds(186, 255, 30, 19);
		panel24h.add(statusADX24h);
		
		statusCCI24h = new CtrlStatus();
		statusCCI24h.setBounds(186, 280, 30, 19);
		panel24h.add(statusCCI24h);

		statusAO24h = new CtrlStatus();
		statusAO24h.setBounds(186, 305, 30, 19);
		panel24h.add(statusAO24h);
		
		statusBBP24h = new CtrlStatus();
		statusBBP24h.setBounds(186, 330, 30, 19);
		panel24h.add(statusBBP24h);
		
		statusEMA1_24h = new CtrlStatus();
		statusEMA1_24h.setBounds(186, 360, 30, 19);
		panel24h.add(statusEMA1_24h);
		
		statusEMA3_24h = new CtrlStatus();
		statusEMA3_24h.setBounds(186, 385, 30, 19);
		panel24h.add(statusEMA3_24h);
		
		statusEMA2_24h = new CtrlStatus();
		statusEMA2_24h.setBounds(186, 410, 30, 19);
		panel24h.add(statusEMA2_24h);
		
		statusMACD24h = new CtrlStatus();
		statusMACD24h.setBounds(186, 230, 30, 19);
		panel24h.add(statusMACD24h);
		
		
		JLabel lblVolume14d = new JLabel("Volume 30d");
		lblVolume14d.setBounds(20, 23, 68, 14);
		panel14d.add(lblVolume14d);

		JLabel lblChgOC14d = new JLabel("Change 30d");
		lblChgOC14d.setBounds(20, 48, 68, 14);
		panel14d.add(lblChgOC14d);

		JLabel lblRsi14d = new JLabel("RSI");
		lblRsi14d.setBounds(20, 133, 62, 14);
		panel14d.add(lblRsi14d);

		JLabel lblStochastic14d = new JLabel("Stochastic");
		lblStochastic14d.setBounds(20, 108, 62, 14);
		panel14d.add(lblStochastic14d);

		JLabel lblStochRsi14d = new JLabel("Stoch RSI");
		lblStochRsi14d.setBounds(20, 158, 62, 14);
		panel14d.add(lblStochRsi14d);

		JLabel lblNivelMacd14d = new JLabel("UO");
		lblNivelMacd14d.setBounds(20, 208, 62, 14);
		panel14d.add(lblNivelMacd14d);

		JLabel lblWilliamsR14d = new JLabel("Williams R");
		lblWilliamsR14d.setBounds(20, 183, 62, 14);
		panel14d.add(lblWilliamsR14d);
		
		JLabel lblMacd_14d = new JLabel("MACD");
		lblMacd_14d.setBounds(20, 233, 62, 14);
		panel14d.add(lblMacd_14d);

		JLabel lblAdx_14d = new JLabel("ADX");
		lblAdx_14d.setBounds(20, 258, 62, 14);
		panel14d.add(lblAdx_14d);
		
		JLabel lblCci_14d = new JLabel("CCI");
		lblCci_14d.setBounds(20, 283, 62, 14);
		panel14d.add(lblCci_14d);
		
		JLabel lblAo_14d = new JLabel("AO");
		lblAo_14d.setBounds(20, 308, 62, 14);
		panel14d.add(lblAo_14d);
		
		JLabel lblBbp_14d = new JLabel("BBP");
		lblBbp_14d.setBounds(20, 333, 62, 14);
		panel14d.add(lblBbp_14d);
		
		JLabel lblEma1_14d = new JLabel("EMA 10");
		lblEma1_14d.setBounds(20, 363, 62, 14);
		panel14d.add(lblEma1_14d);
		
		JLabel lblEma2_14d = new JLabel("EMA 30");
		lblEma2_14d.setBounds(20, 388, 62, 14);
		panel14d.add(lblEma2_14d);
		
		JLabel lblEma3_14d = new JLabel("EMA 100");
		lblEma3_14d.setBounds(20, 413, 62, 14);
		panel14d.add(lblEma3_14d);

		txtRsi14d = new JTextField();
		txtRsi14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi14d.setEditable(false);
		txtRsi14d.setBounds(94, 130, 86, 20);
		panel14d.add(txtRsi14d);

		txtChgOC14d = new JTextField();
		txtChgOC14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC14d.setEditable(false);
		txtChgOC14d.setColumns(10);
		txtChgOC14d.setBounds(94, 45, 86, 20);
		panel14d.add(txtChgOC14d);

		txtVolume14d = new JTextField();
		txtVolume14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume14d.setEditable(false);
		txtVolume14d.setColumns(10);
		txtVolume14d.setBounds(94, 20, 86, 20);
		panel14d.add(txtVolume14d);

		txtStochastic14d = new JTextField();
		txtStochastic14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic14d.setEditable(false);
		txtStochastic14d.setBounds(94, 105, 86, 20);
		panel14d.add(txtStochastic14d);

		txtStochRsi14d = new JTextField();
		txtStochRsi14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi14d.setEditable(false);
		txtStochRsi14d.setBounds(94, 155, 86, 20);
		panel14d.add(txtStochRsi14d);

		txtUO14d = new JTextField();
		txtUO14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO14d.setEditable(false);
		txtUO14d.setBounds(94, 205, 86, 20);
		panel14d.add(txtUO14d);

		txtWilliamsR14d = new JTextField();
		txtWilliamsR14d.setBounds(94, 180, 86, 20);
		txtWilliamsR14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR14d.setEditable(false);
		panel14d.add(txtWilliamsR14d);
		
		txtMACD14d = new JTextField();
		txtMACD14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtMACD14d.setEditable(false);
		txtMACD14d.setBounds(94, 230, 86, 20);
		panel14d.add(txtMACD14d);

		txtCCI14d = new JTextField();
		txtCCI14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtCCI14d.setEditable(false);
		txtCCI14d.setBounds(94, 280, 86, 20);
		panel14d.add(txtCCI14d);
		
		txtADX14d = new JTextField();
		txtADX14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtADX14d.setEditable(false);
		txtADX14d.setBounds(94, 255, 86, 20);
		panel14d.add(txtADX14d);
		
		txtAO14d = new JTextField();
		txtAO14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtAO14d.setEditable(false);
		txtAO14d.setBounds(94, 305, 86, 20);
		panel14d.add(txtAO14d);
		
		txtEMA3_14d = new JTextField();
		txtEMA3_14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA3_14d.setEditable(false);
		txtEMA3_14d.setBounds(94, 410, 86, 20);
		panel14d.add(txtEMA3_14d);
		
		txtEMA2_14d = new JTextField();
		txtEMA2_14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA2_14d.setEditable(false);
		txtEMA2_14d.setBounds(94, 385, 86, 20);
		panel14d.add(txtEMA2_14d);
		
		txtEMA1_14d = new JTextField();
		txtEMA1_14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtEMA1_14d.setEditable(false);
		txtEMA1_14d.setBounds(94, 360, 86, 20);
		panel14d.add(txtEMA1_14d);
		
		txtBBP14d = new JTextField();
		txtBBP14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtBBP14d.setEditable(false);
		txtBBP14d.setBounds(94, 330, 86, 20);
		panel14d.add(txtBBP14d);
		
		statusStochRsi14d = new CtrlStatus();
		statusStochRsi14d.setBounds(186, 155, 30, 19);
		panel14d.add(statusStochRsi14d);

		statusUO14d = new CtrlStatus();
		statusUO14d.setBounds(186, 205, 30, 19);
		panel14d.add(statusUO14d);

		statusWilliamsR14d = new CtrlStatus();
		statusWilliamsR14d.setBounds(186, 180, 30, 19);
		panel14d.add(statusWilliamsR14d);
		
		statusVolume14d = new CtrlStatus();
		statusVolume14d.setBounds(186, 20, 30, 19);
		panel14d.add(statusVolume14d);

		statusChange14d = new CtrlStatus();
		statusChange14d.setBounds(186, 45, 30, 19);
		panel14d.add(statusChange14d);

		statusRsi14d = new CtrlStatus();
		statusRsi14d.setBounds(186, 130, 30, 19);
		panel14d.add(statusRsi14d);

		statusStoch14d = new CtrlStatus();
		statusStoch14d.setBounds(186, 105, 30, 19);
		panel14d.add(statusStoch14d);
		
		statusADX14d = new CtrlStatus();
		statusADX14d.setBounds(186, 255, 30, 19);
		panel14d.add(statusADX14d);
		
		statusCCI14d = new CtrlStatus();
		statusCCI14d.setBounds(186, 280, 30, 19);
		panel14d.add(statusCCI14d);

		statusAO14d = new CtrlStatus();
		statusAO14d.setBounds(186, 305, 30, 19);
		panel14d.add(statusAO14d);
		
		statusBBP14d = new CtrlStatus();
		statusBBP14d.setBounds(186, 330, 30, 19);
		panel14d.add(statusBBP14d);
		
		statusEMA1_14d = new CtrlStatus();
		statusEMA1_14d.setBounds(186, 360, 30, 19);
		panel14d.add(statusEMA1_14d);
		
		statusEMA3_14d = new CtrlStatus();
		statusEMA3_14d.setBounds(186, 385, 30, 19);
		panel14d.add(statusEMA3_14d);
		
		statusEMA2_14d = new CtrlStatus();
		statusEMA2_14d.setBounds(186, 410, 30, 19);
		panel14d.add(statusEMA2_14d);
		
		statusMACD14d = new CtrlStatus();
		statusMACD14d.setBounds(186, 230, 30, 19);
		panel14d.add(statusMACD14d);		
		
	}

	private void search()
	{
		ctrlError.CLEAN();
		load4h();
		load24h();
		load14d();
	}

	public void load4h()
	{
		try
		{
			TechnicalRatings status = TechnicalStatus.load(symbol, IntervalType._5m);

			int periods = 80; // _5m * 80 = 4h
			TechCandle candleMerged = CandleUtils.mergeLastCandles(status.getCandles(), periods);

			double open = candleMerged.getOpenPrice();
			double close = candleMerged.getClosePrice();
			double usdVolume = candleMerged.getVolume();
			double changePercent = ((close - open) / open) * 100;

			txtChgOC4h.setText(String.format(Locale.US, "%.2f %%", changePercent));
			txtVolume4h.setText(PriceUtil.cashFormat(usdVolume));

			txtStochastic4h.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi4h.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR4h.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi4h.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO4h.setText(String.format(Locale.US, "%.2f", status.getUo()));
			txtAO4h.setText(String.format(Locale.US, "%f", status.getAo()));
			txtMACD4h.setText(String.format(Locale.US, "%f", status.getMacd()));
			txtCCI4h.setText(String.format(Locale.US, "%f", status.getCci()));
			txtADX4h.setText(String.format(Locale.US, "%f", status.getAdx()));
			txtBBP4h.setText(String.format(Locale.US, "%f", status.getBbp()));
			
			txtEMA1_4h.setText(symbol.priceToStr(status.getEma()[0]));			
			txtEMA2_4h.setText(symbol.priceToStr(status.getEma()[2]));			
			txtEMA3_4h.setText(symbol.priceToStr(status.getEma()[4]));			
			
			if (usdVolume * 6 < CoreConfig.getMinVolume24h())
				statusVolume4h.setStatus(CtrlStatus.Status.RISK);
			else
				statusVolume4h.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(changePercent) < 1)
				statusChange4h.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(changePercent) > CoreConfig.getMinChange24h())
				statusChange4h.setStatus(CtrlStatus.Status.RISK);
			else
				statusChange4h.setStatus(CtrlStatus.Status.OK);

			setStatus(statusStoch4h, status.getStochStatus());
			setStatus(statusStochRsi4h, status.getStochRsiStatus());
			setStatus(statusRsi4h, status.getRsiStatus());
			setStatus(statusWilliamsR4h, status.getWilliamsRStatus());
			setStatus(statusUO4h, status.getUoStatus());
			setStatus(statusAO4h, status.getAoStatus());
			setStatus(statusMACD4h, status.getMacdStatus());
			setStatus(statusCCI4h, status.getCciStatus());
			setStatus(statusADX4h, status.getAdxStatus());
			setStatus(statusBBP4h, status.getBbpStatus());

			setStatus(statusEMA1_4h, status.getEmaTrend()[0]);
			setStatus(statusEMA2_4h, status.getEmaTrend()[2]);
			setStatus(statusEMA3_4h, status.getEmaTrend()[4]);
			
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}
	
	public void load24h()
	{
		try
		{
			TechnicalRatings status = TechnicalStatus.load(symbol, IntervalType._1h);

			int periods = 72; // _1h * 72 = 3d
			TechCandle candleMerged = CandleUtils.mergeLastCandles(status.getCandles(), periods);

			double open = candleMerged.getOpenPrice();
			double close = candleMerged.getClosePrice();
			double usdVolume = candleMerged.getVolume();
			double changePercent = ((close - open) / open) * 100;

			txtChgOC24h.setText(String.format(Locale.US, "%.2f %%", changePercent));
			txtVolume24h.setText(PriceUtil.cashFormat(usdVolume));

			txtStochastic24h.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi24h.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR24h.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi24h.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO24h.setText(String.format(Locale.US, "%.2f", status.getUo()));
			txtAO24h.setText(String.format(Locale.US, "%f", status.getAo()));
			txtMACD24h.setText(String.format(Locale.US, "%f", status.getMacd()));
			txtCCI24h.setText(String.format(Locale.US, "%f", status.getCci()));
			txtADX24h.setText(String.format(Locale.US, "%f", status.getAdx()));
			txtBBP24h.setText(String.format(Locale.US, "%f", status.getBbp()));
			
			txtEMA1_24h.setText(symbol.priceToStr(status.getEma()[0]));			
			txtEMA2_24h.setText(symbol.priceToStr(status.getEma()[2]));			
			txtEMA3_24h.setText(symbol.priceToStr(status.getEma()[4]));			

			if (usdVolume < CoreConfig.getMinVolume24h())
				statusVolume24h.setStatus(CtrlStatus.Status.RISK);
			else
				statusVolume24h.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(changePercent) < 1)
				statusChange24h.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(changePercent) > CoreConfig.getMinChange24h())
				statusChange24h.setStatus(CtrlStatus.Status.RISK);
			else
				statusChange24h.setStatus(CtrlStatus.Status.OK);

			setStatus(statusStoch24h, status.getStochStatus());
			setStatus(statusStochRsi24h, status.getStochRsiStatus());
			setStatus(statusRsi24h, status.getRsiStatus());
			setStatus(statusWilliamsR24h, status.getWilliamsRStatus());
			setStatus(statusUO24h, status.getUoStatus());
			setStatus(statusAO24h, status.getAoStatus());
			setStatus(statusMACD24h, status.getMacdStatus());
			setStatus(statusCCI24h, status.getCciStatus());
			setStatus(statusADX24h, status.getAdxStatus());
			setStatus(statusBBP24h, status.getBbpStatus());

			setStatus(statusEMA1_24h, status.getEmaTrend()[0]);
			setStatus(statusEMA2_24h, status.getEmaTrend()[2]);
			setStatus(statusEMA3_24h, status.getEmaTrend()[4]);
			
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	public void load14d()
	{
		try
		{
			TechnicalRatings status = TechnicalStatus.load(symbol, IntervalType._1d);

			int periods = 30; // _1d * 30 = 30d
			TechCandle candleMerged = CandleUtils.mergeLastCandles(status.getCandles(), periods);

			double open = candleMerged.getOpenPrice();
			double close = candleMerged.getClosePrice();
			double usdVolume = candleMerged.getVolume();
			double changePercent = ((close - open) / open) * 100;

			txtChgOC14d.setText(String.format(Locale.US, "%.2f %%", changePercent));
			txtVolume14d.setText(PriceUtil.cashFormat(usdVolume));

			txtStochastic14d.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi14d.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR14d.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi14d.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO14d.setText(String.format(Locale.US, "%.2f", status.getUo()));
			txtAO14d.setText(String.format(Locale.US, "%f", status.getAo()));
			txtMACD14d.setText(String.format(Locale.US, "%f", status.getMacd()));
			txtCCI14d.setText(String.format(Locale.US, "%f", status.getCci()));
			txtADX14d.setText(String.format(Locale.US, "%f", status.getAdx()));
			txtBBP14d.setText(String.format(Locale.US, "%f", status.getBbp()));
			
			txtEMA1_14d.setText(symbol.priceToStr(status.getEma()[0]));			
			txtEMA2_14d.setText(symbol.priceToStr(status.getEma()[2]));			
			txtEMA3_14d.setText(symbol.priceToStr(status.getEma()[4]));			
			
			statusVolume14d.setStatus(CtrlStatus.Status.NULL);
			statusChange14d.setStatus(CtrlStatus.Status.NULL);

			setStatus(statusStoch14d, status.getStochStatus());
			setStatus(statusStochRsi14d, status.getStochRsiStatus());
			setStatus(statusRsi14d, status.getRsiStatus());
			setStatus(statusWilliamsR14d, status.getWilliamsRStatus());
			setStatus(statusUO14d, status.getUoStatus());
			setStatus(statusAO14d, status.getAoStatus());
			setStatus(statusMACD14d, status.getMacdStatus());
			setStatus(statusCCI14d, status.getCciStatus());
			setStatus(statusADX14d, status.getAdxStatus());
			setStatus(statusBBP14d, status.getBbpStatus());

			setStatus(statusEMA1_14d, status.getEmaTrend()[0]);
			setStatus(statusEMA2_14d, status.getEmaTrend()[2]);
			setStatus(statusEMA3_14d, status.getEmaTrend()[4]);

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}
	
	private static void setStatus (CtrlStatus ctrl, int status)
	{
		ctrl.setStatus(status == 1 ? CtrlStatus.Status.HIGH_OK : status == 1 ? CtrlStatus.Status.LOW_RISK : CtrlStatus.Status.NONE);
	}

	public static void launch(Symbol symbol)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmCoinTech frame = new FrmCoinTech();
					frame.setVisible(true);

					if (symbol != null)
					{
						frame.symbol = symbol;
						frame.setTitle(TITLE + " - " + symbol.getNameLeft());
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
