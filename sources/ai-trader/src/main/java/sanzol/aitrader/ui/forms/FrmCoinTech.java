package sanzol.aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
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

import api.client.futures.model.enums.IntervalType;
import sanzol.aitrader.be.config.Config;
import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.indicators.HighLowChange;
import sanzol.aitrader.be.indicators.TechnicalStatus;
import sanzol.aitrader.be.model.Symbol;
import sanzol.aitrader.ui.config.Styles;
import sanzol.aitrader.ui.controls.CtrlError;
import sanzol.aitrader.ui.controls.CtrlStatus;
import sanzol.util.price.PriceUtil;

public class FrmCoinTech extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private Symbol symbol;

	private JPanel contentPane;

	private JPanel pnlBottom;
	private CtrlError ctrlError;

	private JTextField txtChgOC30m;
	private JTextField txtVolume30m;
	private JTextField txtStochastic30m;
	private JTextField txtChgHL30m;
	private JTextField txtWilliamsR30m;
	private JTextField txtRsi30m;
	private JTextField txtStochRsi30m;
	private JTextField txtUO30m;

	private CtrlStatus statusVolume30m;
	private CtrlStatus statusChange30m;
	private CtrlStatus statusHiLo30m;
	private CtrlStatus statusWilliamsR30m;
	private CtrlStatus statusRsi30m;
	private CtrlStatus statusStoch30m;
	private CtrlStatus statusStochRsi30m;
	private CtrlStatus statusUO30m;

	private JTextField txtChgOC24h;
	private JTextField txtVolume24h;
	private JTextField txtStochastic24h;
	private JTextField txtChgHL24h;
	private JTextField txtWilliamsR24h;
	private JTextField txtRsi24h;
	private JTextField txtStochRsi24h;
	private JTextField txtUO24h;

	private CtrlStatus statusVolume24h;
	private CtrlStatus statusChange24h;
	private CtrlStatus statusHiLo24h;
	private CtrlStatus statusWilliamsR24h;
	private CtrlStatus statusRsi24h;
	private CtrlStatus statusStoch24h;
	private CtrlStatus statusStochRsi24h;
	private CtrlStatus statusUO24h;

	private JTextField txtChgOC14d;
	private JTextField txtVolume14d;
	private JTextField txtStochastic14d;
	private JTextField txtChgHL14d;
	private JTextField txtWilliamsR14d;
	private JTextField txtRsi14d;
	private JTextField txtStochRsi14d;
	private JTextField txtUO14d;

	private CtrlStatus statusVolume14d;
	private CtrlStatus statusChange14d;
	private CtrlStatus statusHiLo14d;
	private CtrlStatus statusWilliamsR14d;
	private CtrlStatus statusRsi14d;
	private CtrlStatus statusStoch14d;
	private CtrlStatus statusStochRsi14d;
	private CtrlStatus statusUO14d;

	public FrmCoinTech()
	{
		setTitle(TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setBounds(100, 100, 800, 372);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(20, 300, 742, 22);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnlBottom);

		ctrlError = new CtrlError();
		pnlBottom.add(ctrlError, BorderLayout.CENTER);
		ctrlError.setMinimumSize(new Dimension(100, 20));
		ctrlError.setBorder(new EmptyBorder(5, 0, 5, 5));

		JPanel panel30m = new JPanel();
		panel30m.setLayout(null);
		panel30m.setBorder(UIManager.getBorder("TextField.border"));
		panel30m.setBounds(20, 32, 234, 250);
		contentPane.add(panel30m);

		JPanel panel24h = new JPanel();
		panel24h.setLayout(null);
		panel24h.setBorder(UIManager.getBorder("TextField.border"));
		panel24h.setBounds(274, 32, 234, 250);
		contentPane.add(panel24h);

		JPanel panel14d = new JPanel();
		panel14d.setLayout(null);
		panel14d.setBorder(UIManager.getBorder("TextField.border"));
		panel14d.setBounds(528, 32, 234, 250);
		contentPane.add(panel14d);

		JLabel lblLast30m = new JLabel("Last 30m");
		lblLast30m.setBounds(20, 11, 132, 16);
		contentPane.add(lblLast30m);

		JLabel lblLast24h = new JLabel("Last 24h");
		lblLast24h.setBounds(274, 11, 132, 16);
		contentPane.add(lblLast24h);

		JLabel lblLast14d = new JLabel("Last 14d");
		lblLast14d.setBounds(528, 11, 132, 16);
		contentPane.add(lblLast14d);

		JLabel lblVolume30m = new JLabel("Volume");
		lblVolume30m.setBounds(20, 23, 62, 14);
		panel30m.add(lblVolume30m);

		JLabel lblChgOC30m = new JLabel("OC Chg");
		lblChgOC30m.setBounds(20, 54, 62, 14);
		panel30m.add(lblChgOC30m);

		JLabel lblChgHL30m = new JLabel("HL Chg");
		lblChgHL30m.setBounds(20, 79, 62, 14);
		panel30m.add(lblChgHL30m);

		JLabel lblRsi30m = new JLabel("RSI");
		lblRsi30m.setBounds(20, 135, 62, 14);
		panel30m.add(lblRsi30m);

		JLabel lblStochastic30m = new JLabel("Stochastic");
		lblStochastic30m.setBounds(20, 110, 62, 14);
		panel30m.add(lblStochastic30m);

		txtChgHL30m = new JTextField();
		txtChgHL30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgHL30m.setForeground((Color) null);
		txtChgHL30m.setEditable(false);
		txtChgHL30m.setBounds(94, 76, 86, 20);
		panel30m.add(txtChgHL30m);

		txtRsi30m = new JTextField();
		txtRsi30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi30m.setForeground((Color) null);
		txtRsi30m.setEditable(false);
		txtRsi30m.setBounds(94, 132, 86, 20);
		panel30m.add(txtRsi30m);

		txtChgOC30m = new JTextField();
		txtChgOC30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC30m.setForeground((Color) null);
		txtChgOC30m.setEditable(false);
		txtChgOC30m.setColumns(10);
		txtChgOC30m.setBounds(94, 51, 86, 20);
		panel30m.add(txtChgOC30m);

		txtVolume30m = new JTextField();
		txtVolume30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume30m.setForeground((Color) null);
		txtVolume30m.setEditable(false);
		txtVolume30m.setColumns(10);
		txtVolume30m.setBounds(94, 20, 86, 20);
		panel30m.add(txtVolume30m);

		txtStochastic30m = new JTextField();
		txtStochastic30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic30m.setForeground((Color) null);
		txtStochastic30m.setEditable(false);
		txtStochastic30m.setBounds(94, 107, 86, 20);
		panel30m.add(txtStochastic30m);

		statusVolume30m = new CtrlStatus();
		statusVolume30m.setBounds(186, 20, 30, 19);
		panel30m.add(statusVolume30m);

		statusChange30m = new CtrlStatus();
		statusChange30m.setBounds(186, 51, 30, 19);
		panel30m.add(statusChange30m);

		statusHiLo30m = new CtrlStatus();
		statusHiLo30m.setBounds(186, 76, 30, 19);
		panel30m.add(statusHiLo30m);

		statusRsi30m = new CtrlStatus();
		statusRsi30m.setBounds(186, 132, 30, 19);
		panel30m.add(statusRsi30m);

		statusStoch30m = new CtrlStatus();
		statusStoch30m.setBounds(186, 107, 30, 19);
		panel30m.add(statusStoch30m);

		JLabel lblStochRsi30m = new JLabel("Stoch RSI");
		lblStochRsi30m.setBounds(20, 159, 62, 14);
		panel30m.add(lblStochRsi30m);

		txtStochRsi30m = new JTextField();
		txtStochRsi30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi30m.setForeground((Color) null);
		txtStochRsi30m.setEditable(false);
		txtStochRsi30m.setBounds(94, 156, 86, 20);
		panel30m.add(txtStochRsi30m);

		statusStochRsi30m = new CtrlStatus();
		statusStochRsi30m.setBounds(186, 156, 30, 19);
		panel30m.add(statusStochRsi30m);

		JLabel lblNivelMacd30m = new JLabel("UO");
		lblNivelMacd30m.setBounds(20, 216, 62, 14);
		panel30m.add(lblNivelMacd30m);

		txtUO30m = new JTextField();
		txtUO30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO30m.setForeground((Color) null);
		txtUO30m.setEditable(false);
		txtUO30m.setBounds(94, 213, 86, 20);
		panel30m.add(txtUO30m);

		statusUO30m = new CtrlStatus();
		statusUO30m.setBounds(186, 213, 30, 19);
		panel30m.add(statusUO30m);

		txtWilliamsR30m = new JTextField();
		txtWilliamsR30m.setBounds(94, 184, 86, 20);
		panel30m.add(txtWilliamsR30m);
		txtWilliamsR30m.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR30m.setForeground((Color) null);
		txtWilliamsR30m.setEditable(false);

		statusWilliamsR30m = new CtrlStatus();
		statusWilliamsR30m.setBounds(186, 184, 30, 19);
		panel30m.add(statusWilliamsR30m);

		JLabel lblWilliamsR30m = new JLabel("Williams R");
		lblWilliamsR30m.setBounds(20, 187, 62, 14);
		panel30m.add(lblWilliamsR30m);

		JLabel lblVolume24h = new JLabel("Volume");
		lblVolume24h.setBounds(20, 23, 62, 14);
		panel24h.add(lblVolume24h);

		JLabel lblChgOC24h = new JLabel("OC Chg");
		lblChgOC24h.setBounds(20, 54, 62, 14);
		panel24h.add(lblChgOC24h);

		JLabel lblChgHL24h = new JLabel("HL Chg");
		lblChgHL24h.setBounds(20, 79, 62, 14);
		panel24h.add(lblChgHL24h);

		JLabel lblRsi24h = new JLabel("RSI");
		lblRsi24h.setBounds(20, 135, 62, 14);
		panel24h.add(lblRsi24h);

		JLabel lblStochastic24h = new JLabel("Stochastic");
		lblStochastic24h.setBounds(20, 110, 62, 14);
		panel24h.add(lblStochastic24h);

		txtChgHL24h = new JTextField();
		txtChgHL24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgHL24h.setForeground((Color) null);
		txtChgHL24h.setEditable(false);
		txtChgHL24h.setBounds(94, 76, 86, 20);
		panel24h.add(txtChgHL24h);

		txtRsi24h = new JTextField();
		txtRsi24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi24h.setForeground((Color) null);
		txtRsi24h.setEditable(false);
		txtRsi24h.setBounds(94, 132, 86, 20);
		panel24h.add(txtRsi24h);

		txtChgOC24h = new JTextField();
		txtChgOC24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC24h.setForeground((Color) null);
		txtChgOC24h.setEditable(false);
		txtChgOC24h.setColumns(10);
		txtChgOC24h.setBounds(94, 51, 86, 20);
		panel24h.add(txtChgOC24h);

		txtVolume24h = new JTextField();
		txtVolume24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume24h.setForeground((Color) null);
		txtVolume24h.setEditable(false);
		txtVolume24h.setColumns(10);
		txtVolume24h.setBounds(94, 20, 86, 20);
		panel24h.add(txtVolume24h);

		txtStochastic24h = new JTextField();
		txtStochastic24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic24h.setForeground((Color) null);
		txtStochastic24h.setEditable(false);
		txtStochastic24h.setBounds(94, 107, 86, 20);
		panel24h.add(txtStochastic24h);

		statusVolume24h = new CtrlStatus();
		statusVolume24h.setBounds(186, 20, 30, 19);
		panel24h.add(statusVolume24h);

		statusChange24h = new CtrlStatus();
		statusChange24h.setBounds(186, 51, 30, 19);
		panel24h.add(statusChange24h);

		statusHiLo24h = new CtrlStatus();
		statusHiLo24h.setBounds(186, 76, 30, 19);
		panel24h.add(statusHiLo24h);

		statusRsi24h = new CtrlStatus();
		statusRsi24h.setBounds(186, 132, 30, 19);
		panel24h.add(statusRsi24h);

		statusStoch24h = new CtrlStatus();
		statusStoch24h.setBounds(186, 107, 30, 19);
		panel24h.add(statusStoch24h);

		JLabel lblStochRsi24h = new JLabel("Stoch RSI");
		lblStochRsi24h.setBounds(20, 158, 62, 14);
		panel24h.add(lblStochRsi24h);

		txtStochRsi24h = new JTextField();
		txtStochRsi24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi24h.setForeground((Color) null);
		txtStochRsi24h.setEditable(false);
		txtStochRsi24h.setBounds(94, 155, 86, 20);
		panel24h.add(txtStochRsi24h);

		statusStochRsi24h = new CtrlStatus();
		statusStochRsi24h.setBounds(186, 155, 30, 19);
		panel24h.add(statusStochRsi24h);

		JLabel lblNivelMacd24h = new JLabel("UO");
		lblNivelMacd24h.setBounds(20, 216, 62, 14);
		panel24h.add(lblNivelMacd24h);

		txtUO24h = new JTextField();
		txtUO24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO24h.setForeground((Color) null);
		txtUO24h.setEditable(false);
		txtUO24h.setBounds(94, 213, 86, 20);
		panel24h.add(txtUO24h);

		statusUO24h = new CtrlStatus();
		statusUO24h.setBounds(186, 213, 30, 19);
		panel24h.add(statusUO24h);

		JLabel lblWilliamsR24h = new JLabel("Williams R");
		lblWilliamsR24h.setBounds(20, 186, 62, 14);
		panel24h.add(lblWilliamsR24h);

		txtWilliamsR24h = new JTextField();
		txtWilliamsR24h.setBounds(94, 183, 86, 20);
		panel24h.add(txtWilliamsR24h);
		txtWilliamsR24h.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR24h.setForeground((Color) null);
		txtWilliamsR24h.setEditable(false);

		statusWilliamsR24h = new CtrlStatus();
		statusWilliamsR24h.setBounds(186, 183, 30, 19);
		panel24h.add(statusWilliamsR24h);

		JLabel lblVolume14d = new JLabel("Volume");
		lblVolume14d.setBounds(20, 23, 62, 14);
		panel14d.add(lblVolume14d);

		JLabel lblChgOC14d = new JLabel("OC Chg");
		lblChgOC14d.setBounds(20, 54, 62, 14);
		panel14d.add(lblChgOC14d);

		JLabel lblChgHL14d = new JLabel("HL Chg");
		lblChgHL14d.setBounds(20, 79, 62, 14);
		panel14d.add(lblChgHL14d);

		JLabel lblRsi14d = new JLabel("RSI");
		lblRsi14d.setBounds(20, 135, 62, 14);
		panel14d.add(lblRsi14d);

		JLabel lblStochastic14d = new JLabel("Stochastic");
		lblStochastic14d.setBounds(20, 110, 62, 14);
		panel14d.add(lblStochastic14d);

		txtChgHL14d = new JTextField();
		txtChgHL14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgHL14d.setForeground((Color) null);
		txtChgHL14d.setEditable(false);
		txtChgHL14d.setBounds(94, 76, 86, 20);
		panel14d.add(txtChgHL14d);

		txtRsi14d = new JTextField();
		txtRsi14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtRsi14d.setForeground((Color) null);
		txtRsi14d.setEditable(false);
		txtRsi14d.setBounds(94, 132, 86, 20);
		panel14d.add(txtRsi14d);

		txtChgOC14d = new JTextField();
		txtChgOC14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtChgOC14d.setForeground((Color) null);
		txtChgOC14d.setEditable(false);
		txtChgOC14d.setColumns(10);
		txtChgOC14d.setBounds(94, 51, 86, 20);
		panel14d.add(txtChgOC14d);

		txtVolume14d = new JTextField();
		txtVolume14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtVolume14d.setForeground((Color) null);
		txtVolume14d.setEditable(false);
		txtVolume14d.setColumns(10);
		txtVolume14d.setBounds(94, 20, 86, 20);
		panel14d.add(txtVolume14d);

		txtStochastic14d = new JTextField();
		txtStochastic14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochastic14d.setForeground((Color) null);
		txtStochastic14d.setEditable(false);
		txtStochastic14d.setBounds(94, 107, 86, 20);
		panel14d.add(txtStochastic14d);

		statusVolume14d = new CtrlStatus();
		statusVolume14d.setBounds(186, 20, 30, 19);
		panel14d.add(statusVolume14d);

		statusChange14d = new CtrlStatus();
		statusChange14d.setBounds(186, 51, 30, 19);
		panel14d.add(statusChange14d);

		statusHiLo14d = new CtrlStatus();
		statusHiLo14d.setBounds(186, 76, 30, 19);
		panel14d.add(statusHiLo14d);

		statusRsi14d = new CtrlStatus();
		statusRsi14d.setBounds(186, 132, 30, 19);
		panel14d.add(statusRsi14d);

		statusStoch14d = new CtrlStatus();
		statusStoch14d.setBounds(186, 107, 30, 19);
		panel14d.add(statusStoch14d);

		JLabel lblStochRsi14d = new JLabel("Stoch RSI");
		lblStochRsi14d.setBounds(20, 163, 62, 14);
		panel14d.add(lblStochRsi14d);

		txtStochRsi14d = new JTextField();
		txtStochRsi14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtStochRsi14d.setForeground((Color) null);
		txtStochRsi14d.setEditable(false);
		txtStochRsi14d.setBounds(94, 160, 86, 20);
		panel14d.add(txtStochRsi14d);

		statusStochRsi14d = new CtrlStatus();
		statusStochRsi14d.setBounds(186, 160, 30, 19);
		panel14d.add(statusStochRsi14d);

		JLabel lblNivelMacd14d = new JLabel("UO");
		lblNivelMacd14d.setBounds(20, 216, 62, 14);
		panel14d.add(lblNivelMacd14d);

		txtUO14d = new JTextField();
		txtUO14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtUO14d.setForeground((Color) null);
		txtUO14d.setEditable(false);
		txtUO14d.setBounds(94, 213, 86, 20);
		panel14d.add(txtUO14d);

		statusUO14d = new CtrlStatus();
		statusUO14d.setBounds(186, 213, 30, 19);
		panel14d.add(statusUO14d);

		JLabel lblWilliamsR14d = new JLabel("Williams R");
		lblWilliamsR14d.setBounds(20, 191, 62, 14);
		panel14d.add(lblWilliamsR14d);

		txtWilliamsR14d = new JTextField();
		txtWilliamsR14d.setBounds(94, 188, 86, 20);
		panel14d.add(txtWilliamsR14d);
		txtWilliamsR14d.setHorizontalAlignment(SwingConstants.TRAILING);
		txtWilliamsR14d.setForeground((Color) null);
		txtWilliamsR14d.setEditable(false);

		statusWilliamsR14d = new CtrlStatus();
		statusWilliamsR14d.setBounds(186, 188, 30, 19);
		panel14d.add(statusWilliamsR14d);

	}

	private void search()
	{
		ctrlError.CLEAN();
		load30m();
		load24h();
		load14d();
	}

	public void load24h()
	{
		try
		{
			final int periods = 24;
			TechnicalStatus status = TechnicalStatus.create(symbol, IntervalType._1h, periods).readCandles().calculate();

			double open = status.getCandleMerged().getOpenPrice();
			double close = status.getCandleMerged().getClosePrice();
			double usdVolume = status.getCandleMerged().getQuoteVolume();

			double openCloseChange = ((close - open) / open) * 100;
			double highLowChange = HighLowChange.calculate(status.getCandles(), periods);

			txtChgOC24h.setText(String.format(Locale.US, "%.2f %%", openCloseChange));
			txtVolume24h.setText(PriceUtil.cashFormat(usdVolume));
			txtChgHL24h.setText(String.format(Locale.US, "%.2f %%", highLowChange));
			txtStochastic24h.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi24h.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR24h.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi24h.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO24h.setText(String.format(Locale.US, "%.2f", status.getUo()));

			if (usdVolume < Config.getBetterSymbolsMinVolume())
				statusVolume24h.setStatus(CtrlStatus.Status.RISK);
			else
				statusVolume24h.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(openCloseChange) < 1)
				statusChange24h.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(openCloseChange) > Config.getBetterSymbolsMaxChange())
				statusChange24h.setStatus(CtrlStatus.Status.RISK);
			else
				statusChange24h.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(highLowChange) < 1)
				statusHiLo24h.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(highLowChange) > Config.getBetterSymbolsMaxChange())
				statusHiLo24h.setStatus(CtrlStatus.Status.RISK);
			else
				statusHiLo24h.setStatus(CtrlStatus.Status.OK);

			if (status.getStoch() < 20)
				statusStoch24h.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStoch() > 80)
				statusStoch24h.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStoch24h.setStatus(CtrlStatus.Status.NONE);

			if (status.getStochRsi() < 20)
				statusStochRsi24h.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStochRsi() > 80)
				statusStochRsi24h.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStochRsi24h.setStatus(CtrlStatus.Status.NONE);

			if (status.getRsi() < 30)
				statusRsi24h.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getRsi() > 70)
				statusRsi24h.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusRsi24h.setStatus(CtrlStatus.Status.NONE);

			if (status.getWilliamsR() < -80)
				statusWilliamsR24h.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getWilliamsR() > -20)
				statusWilliamsR24h.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusWilliamsR24h.setStatus(CtrlStatus.Status.NONE);

			if (status.getUo() < 30)
				statusUO24h.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getUo() > 70)
				statusUO24h.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusUO24h.setStatus(CtrlStatus.Status.NONE);

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	public void load30m()
	{
		try
		{
			final int periods = 30;
			TechnicalStatus status = TechnicalStatus.create(symbol, IntervalType._1m, periods).readCandles().calculate();

			double open = status.getCandleMerged().getOpenPrice();
			double close = status.getCandleMerged().getClosePrice();
			double usdVolume = status.getCandleMerged().getQuoteVolume();

			double openCloseChange = ((close - open) / open) * 100;
			double highLowChange = HighLowChange.calculate(status.getCandles(), periods);

			txtChgOC30m.setText(String.format(Locale.US, "%.2f %%", openCloseChange));
			txtVolume30m.setText(PriceUtil.cashFormat(usdVolume));
			txtChgHL30m.setText(String.format(Locale.US, "%.2f %%", highLowChange));
			txtStochastic30m.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi30m.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR30m.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi30m.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO30m.setText(String.format(Locale.US, "%.2f", status.getUo()));

			if (usdVolume * 48 < Config.getBetterSymbolsMinVolume())
				statusVolume30m.setStatus(CtrlStatus.Status.RISK);
			else
				statusVolume30m.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(openCloseChange) < 1)
				statusChange30m.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(openCloseChange) > Config.getBetterSymbolsMaxChange())
				statusChange30m.setStatus(CtrlStatus.Status.RISK);
			else
				statusChange30m.setStatus(CtrlStatus.Status.OK);

			if (Math.abs(highLowChange) < 1)
				statusHiLo30m.setStatus(CtrlStatus.Status.WARN);
			else if (Math.abs(highLowChange) > Config.getBetterSymbolsMaxChange())
				statusHiLo30m.setStatus(CtrlStatus.Status.RISK);
			else
				statusHiLo30m.setStatus(CtrlStatus.Status.OK);

			if (status.getStoch() < 20)
				statusStoch30m.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStoch() > 80)
				statusStoch30m.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStoch30m.setStatus(CtrlStatus.Status.NONE);

			if (status.getStochRsi() < 20)
				statusStochRsi30m.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStochRsi() > 80)
				statusStochRsi30m.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStochRsi30m.setStatus(CtrlStatus.Status.NONE);

			if (status.getRsi() < 30)
				statusRsi30m.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getRsi() > 70)
				statusRsi30m.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusRsi30m.setStatus(CtrlStatus.Status.NONE);

			if (status.getWilliamsR() < -80)
				statusWilliamsR30m.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getWilliamsR() > -20)
				statusWilliamsR30m.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusWilliamsR30m.setStatus(CtrlStatus.Status.NONE);

			if (status.getUo() < 30)
				statusUO30m.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getUo() > 70)
				statusUO30m.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusUO30m.setStatus(CtrlStatus.Status.NONE);

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
			final int periods = 14;
			TechnicalStatus status = TechnicalStatus.create(symbol, IntervalType._1d, periods).readCandles().calculate();

			double open = status.getCandleMerged().getOpenPrice();
			double close = status.getCandleMerged().getClosePrice();
			double usdVolume = status.getCandleMerged().getVolume();

			double openCloseChange = ((close - open) / open) * 100;
			double highLowChange = HighLowChange.calculate(status.getCandles(), periods);

			txtChgOC14d.setText(String.format(Locale.US, "%.2f %%", openCloseChange));
			txtVolume14d.setText(PriceUtil.cashFormat(usdVolume));
			txtChgHL14d.setText(String.format(Locale.US, "%.2f %%", highLowChange));
			txtStochastic14d.setText(String.format(Locale.US, "%.2f %%", status.getStoch()));
			txtRsi14d.setText(String.format(Locale.US, "%.2f %%", status.getRsi()));
			txtWilliamsR14d.setText(String.format(Locale.US, "%.2f %%", status.getWilliamsR()));
			txtStochRsi14d.setText(String.format(Locale.US, "%.2f %%", status.getStochRsi()));
			txtUO14d.setText(String.format(Locale.US, "%.2f", status.getUo()));

			statusVolume14d.setStatus(CtrlStatus.Status.NULL);
			statusChange14d.setStatus(CtrlStatus.Status.NULL);
			statusHiLo14d.setStatus(CtrlStatus.Status.NULL);

			if (status.getStoch() < 20)
				statusStoch14d.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStoch() > 80)
				statusStoch14d.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStoch14d.setStatus(CtrlStatus.Status.NONE);

			if (status.getStochRsi() < 20)
				statusStochRsi14d.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getStochRsi() > 80)
				statusStochRsi14d.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusStochRsi14d.setStatus(CtrlStatus.Status.NONE);

			if (status.getRsi() < 30)
				statusRsi14d.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getRsi() > 70)
				statusRsi14d.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusRsi14d.setStatus(CtrlStatus.Status.NONE);

			if (status.getWilliamsR() < -80)
				statusWilliamsR14d.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getWilliamsR() > -20)
				statusWilliamsR14d.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusWilliamsR14d.setStatus(CtrlStatus.Status.NONE);

			if (status.getUo() < 30)
				statusUO14d.setStatus(CtrlStatus.Status.LOW_OK);
			else if (status.getUo() > 70)
				statusUO14d.setStatus(CtrlStatus.Status.HIGH_OK);
			else
				statusUO14d.setStatus(CtrlStatus.Status.NONE);

		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
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
