package sanzol.app.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.binance.client.model.event.SymbolTickerEvent;

import javax.swing.SwingConstants;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.task.PriceService;
import sanzol.app.task.SignalService;
import sanzol.app.util.PriceUtil;
import sanzol.lib.util.ExceptionUtils;

public class FrmSignalsAlt extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Signals";

	private static boolean isOpen = false;

	private JLabel lblError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;
	private JTextField txtWithdrawal;
	private JTextArea txtResult;

	public FrmSignalsAlt()
	{
		initComponents();
		isOpen = true;
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		pnlTopBar = new JPanel();
		pnlTopBar.setBorder(Styles.BORDER_DOWN);
		pnlContent = new JPanel();
		pnlStatusBar = new JPanel();
		pnlStatusBar.setBorder(Styles.BORDER_UP);

		JLabel lblWithdrawal = new JLabel();
		lblWithdrawal.setText("Withdrawal");

		txtWithdrawal = new JTextField();
		txtWithdrawal.setHorizontalAlignment(SwingConstants.RIGHT);
		txtWithdrawal.setForeground(Styles.COLOR_TEXT_ALT1);
		txtWithdrawal.setEditable(false);

		lblError = new JLabel();

		JButton btnCopy = new JButton(Styles.IMAGE_COPY);
		btnCopy.setToolTipText("Copy to clipboard");
		btnCopy.setOpaque(true);

		// --------------------------------------------------------------------
        txtResult = new javax.swing.JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
        txtResult.setColumns(20);
        txtResult.setRows(5);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(txtResult);

        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContent.setLayout(pnlContentLayout);
        pnlContentLayout.setHorizontalGroup(
            pnlContentLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(pnlContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlContentLayout.setVerticalGroup(
            pnlContentLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(pnlContentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
		
		JButton btnNewButton = new JButton("Calc");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSignals();
			}
		});

		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addPreferredGap(ComponentPlacement.RELATED, 658, Short.MAX_VALUE)
					.addComponent(btnCopy)
					.addContainerGap())
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton)
						.addComponent(btnCopy))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlTopBar.setLayout(pnlTopBarLayout);

		// --------------------------------------------------------------------
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
				.addComponent(pnlStatusBar, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
				.addComponent(pnlTopBar, GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(pnlTopBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlContent, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlStatusBar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(layout);
		
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
					.addGap(7)
					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
					.addGap(7))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isOpen = false;
			}
		});

		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(txtResult.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		
	}

	// ------------------------------------------------------------------------

	private String calc()
	{
		List<String> lst = new ArrayList<String>();

		for (SymbolTickerEvent entry : PriceService.getMapTickers().values())
		{
			double lastPrice = entry.getLastPrice().doubleValue();
			double volume = entry.getTotalTradedQuoteAssetVolume().doubleValue(); 
			String priceChangePercent = String.format("%.2f", entry.getPriceChangePercent());
			
			double high = entry.getHigh().doubleValue();
			double low = entry.getLow().doubleValue();
			double avgPrice = entry.getWeightedAvgPrice().doubleValue();

			double avgHigh = (avgPrice + high) / 2;
			double avgLow =  (avgPrice + low) / 2;

			if (lastPrice > 50)
				lst.add(String.format("HIGH PRICE   %-12s %8s %%  %12f   vol %s", entry.getSymbol(), priceChangePercent, lastPrice, PriceUtil.cashFormat(volume, 0)));
			else
			if (volume < 80000000)
				lst.add(String.format("LOW VOLUME   %-12s %8s %%  %12f   vol %s", entry.getSymbol(), priceChangePercent, lastPrice, PriceUtil.cashFormat(volume, 0)));
			else
			if (lastPrice > avgHigh)
				lst.add(String.format("SHORT        %-12s %8s %%  %12f   vol %s", entry.getSymbol(), priceChangePercent, lastPrice, PriceUtil.cashFormat(volume, 0)));
			else
			if (lastPrice < avgLow)
				lst.add(String.format("LONG         %-12s %8s %%  %12f   vol %s", entry.getSymbol(), priceChangePercent, lastPrice, PriceUtil.cashFormat(volume, 0)));
			else
				lst.add(String.format("N/A          %-12s %8s %%  %12f   vol %s", entry.getSymbol(), priceChangePercent, lastPrice, PriceUtil.cashFormat(volume, 0)));
		}

		java.util.Collections.sort(lst);

		String result = "";
		for (String line : lst)
		{
			result += line + "\n";
		}
		
		return result;
	}

	private void getSignals()
	{
		try
		{
			txtResult.setText(calc());
			INFO("Last modification: " + SignalService.getModified());
		}
		catch (Exception e)
		{
			ERROR(e);
			e.printStackTrace();
		}
	}

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
					FrmSignalsAlt frame = new FrmSignalsAlt();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
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
		Application.initializeUI();
		launch();
	}
}
