package sanzol.app.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.CharConstants;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.model.OrderBookInfo;
import sanzol.app.model.Symbol;
import sanzol.app.service.OrderBookService;
import sanzol.app.task.PriceService;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class FrmCoin extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME;

	private static boolean isOpen = false;

	private Symbol coin;

	private JPanel contentPane;
	private JLabel lblError;
	
	private JTextField txtSymbolLeft;
	private JTextField txtSymbolRight;
	private JTextField txtMarkPrice;
	private JTextField txt24h;

	private JTextField txtMaxAsk;
	private JTextField txtMaxBid;
	
	private JScrollPane scrollOBookAsk;
	private JScrollPane scrollOBookBid;

	private JTextArea txtOBookAsk;
	private JTextArea txtOBookBid;


	public FrmCoin()
	{
		setResizable(false);
		initComponents();
		startTimer();
	}

	private void initComponents() 
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 560);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmMain.class.getResource("/resources/upDown.png")));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setBounds(10, 490, 604, 22);
		contentPane.add(pnlBottom);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		
		lblError = new JLabel();
		lblError.setMinimumSize(new Dimension(100, 20));
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));
		pnlBottom.add(lblError, BorderLayout.CENTER);
		
		JButton btnSearch = new JButton(CharConstants.MAGNIFIER);
		btnSearch.setOpaque(true);
		btnSearch.setBackground(Styles.COLOR_BTN);
		btnSearch.setBounds(31, 69, 178, 22);
		contentPane.add(btnSearch);

		txtSymbolLeft = new JTextField();
		txtSymbolLeft.setColumns(10);
		txtSymbolLeft.setBounds(31, 43, 86, 20);
		contentPane.add(txtSymbolLeft);

		JLabel lblNewLabel_2_1 = new JLabel("COIN");
		lblNewLabel_2_1.setBounds(31, 24, 86, 14);
		contentPane.add(lblNewLabel_2_1);

		txtSymbolRight = new JTextField();
		txtSymbolRight.setEditable(false);
		txtSymbolRight.setText(Constants.DEFAULT_SYMBOL_RIGHT);
		txtSymbolRight.setColumns(10);
		txtSymbolRight.setBounds(123, 43, 86, 20);
		contentPane.add(txtSymbolRight);

		JLabel lblMarkPrice = new JLabel("Price");
		lblMarkPrice.setBounds(403, 24, 85, 14);
		contentPane.add(lblMarkPrice);

		JLabel lbl24Hs = new JLabel("24h %");
		lbl24Hs.setBounds(506, 24, 85, 14);
		contentPane.add(lbl24Hs);

		txtMarkPrice = new JTextField();
		txtMarkPrice.setEditable(false);
		txtMarkPrice.setForeground(Styles.COLOR_TEXT_ALT1);
		txtMarkPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMarkPrice.setColumns(10);
		txtMarkPrice.setBounds(402, 43, 86, 20);
		contentPane.add(txtMarkPrice);
		
		txt24h = new JTextField();
		txt24h.setForeground(Styles.COLOR_TEXT_ALT1);
		txt24h.setHorizontalAlignment(SwingConstants.RIGHT);
		txt24h.setEditable(false);
		txt24h.setColumns(10);
		txt24h.setBounds(505, 43, 86, 20);
		contentPane.add(txt24h);
		
		scrollOBookAsk = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookAsk.setBounds(31, 172, 270, 162);
		contentPane.add(scrollOBookAsk);

		txtOBookAsk = new JTextArea();
		txtOBookAsk.setBackground(Styles.COLOR_TEXT_AREA);
		txtOBookAsk.setForeground(Styles.COLOR_TEXT_SHORT);
		txtOBookAsk.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookAsk.setEditable(false);
		scrollOBookAsk.setViewportView(txtOBookAsk);

		scrollOBookBid = new JScrollPane((Component) null, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollOBookBid.setBounds(321, 204, 270, 162);
		contentPane.add(scrollOBookBid);

		txtOBookBid = new JTextArea();
		txtOBookBid.setBackground(Styles.COLOR_TEXT_AREA);
		txtOBookBid.setForeground(Styles.COLOR_TEXT_LONG);
		txtOBookBid.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtOBookBid.setEditable(false);
		scrollOBookBid.setViewportView(txtOBookBid);

		txtMaxAsk = new JTextField();
		txtMaxAsk.setForeground(new Color(220, 20, 60));
		txtMaxAsk.setEditable(false);
		txtMaxAsk.setBounds(31, 346, 220, 20);
		contentPane.add(txtMaxAsk);
		txtMaxAsk.setColumns(10);

		txtMaxBid = new JTextField();
		txtMaxBid.setForeground(new Color(46, 139, 87));
		txtMaxBid.setEditable(false);
		txtMaxBid.setColumns(10);
		txtMaxBid.setBounds(321, 173, 220, 20);
		contentPane.add(txtMaxBid);
		
		// ---------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				isOpen = false;
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});

	}

	// ----------------------------------------------------------------------------------

	private void search()
	{
		INFO("");
		try
		{
			txtSymbolLeft.setText(txtSymbolLeft.getText().toUpperCase());
			String symbol = txtSymbolLeft.getText();
			coin = Symbol.getInstance(Symbol.getFullSymbol(symbol));
			if (coin != null)
			{
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				txtMarkPrice.setText(coin.priceToStr(mrkPrice));
				String priceChangePercent = String.format("%.2f%%", PriceService.PriceChangePercent(coin));
				txt24h.setText(priceChangePercent);
				
				loadDepth();				
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

	private void loadDepth()
	{
		try
		{
			OrderBookInfo points = OrderBookService.getShoks(coin);
			txtOBookAsk.setText(OrderBookService.toStringInv(coin, points.getAsksGrp()));
			txtMaxAsk.setText("SHORT " + points.getStrShShock());
			txtOBookBid.setText(OrderBookService.toString(coin, points.getBidsGrp()));
			txtOBookBid.setCaretPosition(0);
			txtMaxBid.setText("LONG " + points.getStrLgShock());

			String distShLg = coin.priceToStr((points.getShShock().doubleValue() / points.getLgShock().doubleValue() - 1.0) * 100.0);
			txtMaxAsk.setToolTipText("Short-Long : " + distShLg + "%");
			txtMaxBid.setToolTipText("Short-Long : " + distShLg + "%");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	public static void launch()
	{
		if (isOpen)
		{
			return;
		}

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmCoin frame = new FrmCoin();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private void startTimer()
	{
		ActionListener taskPerformer1 = new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				refresh();
			}
		};
		Timer timer1 = new Timer(2000, taskPerformer1);
		timer1.setInitialDelay(0);
		timer1.setRepeats(true);
		timer1.start();
	}

	private void refresh()
	{
		try
		{
			if (coin != null)
			{
				BigDecimal mrkPrice = PriceService.getLastPrice(coin);
				txtMarkPrice.setText(coin.priceToStr(mrkPrice));
				String priceChangePercent = String.format("%.2f%%", PriceService.PriceChangePercent(coin));
				txt24h.setText(priceChangePercent);
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	// ----------------------------------------------------------------------------------

	public void ERROR(Exception e)
	{
		ERROR(e.getMessage());
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
		Application.initializeUI();
		launch();
	}
}
