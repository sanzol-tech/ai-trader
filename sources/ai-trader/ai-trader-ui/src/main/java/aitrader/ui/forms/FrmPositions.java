package aitrader.ui.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.commons.lang3.StringUtils;

import aitrader.core.model.Position;
import aitrader.core.model.PositionName;
import aitrader.core.model.PositionOrder;
import aitrader.core.model.Symbol;
import aitrader.core.service.position.PositionService;
import aitrader.ui.config.Styles;
import aitrader.ui.config.UIConstants;
import aitrader.ui.config.UILog;
import aitrader.ui.controls.CtrlError;
import aitrader.util.Convert;
import aitrader.util.observable.Handler;

public class FrmPositions extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = UIConstants.APP_NAME + " - Positions";

	private static FrmPositions myJFrame = null;

	private CtrlError ctrlError;

	private JPanel pnlContent;
	private JPanel pnlStatusBar;
	private JPanel pnlTopBar;

	private JRadioButton rbPositions;
	private JRadioButton rbOnlyPositions;
	private JRadioButton rbOrdersWithoutPosition;

	private JTextArea txtResult;
	
	private Handler<Void> positionServiceHandler = e -> { onPositionUpdate(); };

	public FrmPositions()
	{
		initComponents();

		onPositionUpdate();
		PositionService.attachObserver(positionServiceHandler);
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

		ctrlError = new CtrlError();

        txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(txtResult);

		// --------------------------------------------------------------------
        GroupLayout pnlContentLayout = new GroupLayout(pnlContent);
        pnlContentLayout.setHorizontalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlContentLayout.setVerticalGroup(
        	pnlContentLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, pnlContentLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        			.addContainerGap())
        );
        pnlContent.setLayout(pnlContentLayout);

		rbPositions = new JRadioButton("Positions with orders");
		rbPositions.setSelected(true);
		rbOnlyPositions = new JRadioButton("Only positions");
		rbOnlyPositions.setSelected(true);
		rbOrdersWithoutPosition = new JRadioButton("Orders without position");

        ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbPositions);
		bg1.add(rbOnlyPositions);
		bg1.add(rbOrdersWithoutPosition);
		
		// --------------------------------------------------------------------
		GroupLayout pnlTopBarLayout = new GroupLayout(pnlTopBar);
		pnlTopBarLayout.setHorizontalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(rbPositions)
					.addGap(18)
					.addComponent(rbOnlyPositions)
					.addGap(18)
					.addComponent(rbOrdersWithoutPosition)
					.addContainerGap(383, Short.MAX_VALUE))
		);
		pnlTopBarLayout.setVerticalGroup(
			pnlTopBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlTopBarLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(pnlTopBarLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(rbPositions)
						.addComponent(rbOnlyPositions)
						.addComponent(rbOrdersWithoutPosition))
					.addContainerGap(11, Short.MAX_VALUE))
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
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
					.addContainerGap())
		);
		pnlStatusBarLayout.setVerticalGroup(
			pnlStatusBarLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(pnlStatusBarLayout.createSequentialGroup()
					.addGap(7)
					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
					.addGap(7))
		);
		pnlStatusBar.setLayout(pnlStatusBarLayout);

		pack();

		// --------------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				PositionService.deattachObserver(positionServiceHandler);
				myJFrame = null;
			}
		});

		rbPositions.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onPositionUpdate();
				}
			}
		});
		rbOnlyPositions.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onPositionUpdate();
				}
			}
		});
		rbOrdersWithoutPosition.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onPositionUpdate();
				}
			}
		});

	}

	// --------------------------------------------------------------------

	public static String toStringPositions(Map<PositionName, Position> mapPositions, boolean openPositions, boolean includeOrders) throws KeyManagementException, NoSuchAlgorithmException, IOException
	{
		if (mapPositions == null || mapPositions.isEmpty())
		{
			return "No open positions found";
		}

		StringBuilder sbBody = new StringBuilder();
		for (Position entry : mapPositions.values())
		{
			Symbol symbol = entry.getSymbol();
			if (openPositions && entry.isOpen())
			{
				String side = entry.getQuantity().doubleValue() > 0 ? "LONG" : "SHORT";

				sbBody.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
											entry.getSymbol(),
											side + " " + entry.getMarginType() + " " + entry.getLeverage(),
											entry.getQuantity(),
											symbol.priceToStr(entry.getMarkPrice()),
											symbol.priceToStr(entry.getEntryPrice()),
											Convert.usdToStr(entry.getPnl().doubleValue())));
			}
			else if (!openPositions && !entry.isOpen())
			{
				String side = entry.getPositionSide().name();

				sbBody.append(String.format("%-22s %-20s %10s %14s %12s %14s\n",
											entry.getSymbol(),
											side + " " + "---",
											"---",
											"---",
											"---",
											"---"));
			}

			if (includeOrders && openPositions == entry.isOpen())
			{
				sbBody.append(StringUtils.repeat("-",97));
				sbBody.append("\n");
				sbBody.append(toStringOrders(entry.getLstOrders()));
				sbBody.append(StringUtils.repeat("-",97));
				sbBody.append("\n");
			}
		}

		if (sbBody.length() == 0)
		{
			return "No open positions found";
		}

		StringBuilder sb  = new StringBuilder();
		sb.append(String.format("%-22s %-20s %10s %14s %12s %14s\n", "SYMBOL", "TYPE", "AMOUNT", "PRICE", "AVG PRICE", "PNL"));
		sb.append(StringUtils.repeat("-", 97));
		sb.append("\n");
		sb.append(sbBody);

		return sb.toString();
	}

	public static String toStringOrders(List<PositionOrder> lstPositionOrders)
	{
		StringBuilder sb = new StringBuilder();

		for (PositionOrder entry : lstPositionOrders)
		{
			sb.append(String.format("%-22s %-6s %-13s %10s %14s %12s %14s\n", 
					entry.getTime().format(DateTimeFormatter.ofPattern("MM-dd HH:MM:ss")), 
					entry.getOrderSide(), 
					entry.getOpType(), 
					entry.getQuantity(), 
					entry.getPrice(), 
					"",
					entry.getReduceOnly() ? "R.Only" : ""));
		}

		return sb.toString();
	}

	// --------------------------------------------------------------------

	private void onPositionUpdate()
	{
		try
		{
			String text;
			
			Map<PositionName, Position> mapPositions = PositionService.getMapPositions();
			if (rbOrdersWithoutPosition.isSelected())
			{
				text = toStringPositions(mapPositions, false, true);
			}
			else if (rbOnlyPositions.isSelected())
			{
				text = toStringPositions(mapPositions, true, false);
			}
			else
			{
				text = toStringPositions(mapPositions, true, true);
			}

			txtResult.setText(text);
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// --------------------------------------------------------------------

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
					myJFrame = new FrmPositions();
					myJFrame.setVisible(true);
				}
				catch (Exception e)
				{
					UILog.error(e);
				}
			}
		});
	}

}
