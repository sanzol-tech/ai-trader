package sanzol.aitrader.ui.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.be.service.PositionFuturesService;
import sanzol.aitrader.be.service.PositionListener;
import sanzol.aitrader.ui.config.Styles;
import sanzol.util.log.LogService;

public class FrmPositions extends JFrame implements PositionListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Positions";

	private static FrmPositions myJFrame = null;

	private JScrollPane scrollPane;
	private JTextArea textArea;

    private JRadioButton rbPositions;
    private JRadioButton rbOnlyPositions;
    private JRadioButton rbOrdersWithoutPosition;
	
	private CtrlError ctrlError;

	public FrmPositions()
	{
		initComponents();

		onPositionUpdate();
		PositionFuturesService.attachRefreshObserver(this);
	}

	private void initComponents()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 778, 595);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

        ctrlError = new CtrlError();

        textArea = new JTextArea();
		textArea.setBackground(Styles.COLOR_TEXT_AREA_BG);
		textArea.setForeground(Styles.COLOR_TEXT_AREA_FG);
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        
        rbPositions = new JRadioButton("Positions with orders");
        rbPositions.setSelected(true);
        rbOnlyPositions = new JRadioButton("Only positions");
        rbOrdersWithoutPosition = new JRadioButton("Orders without position");

        ButtonGroup bg1 = new javax.swing.ButtonGroup();
		bg1.add(rbPositions);
		bg1.add(rbOnlyPositions);
		bg1.add(rbOrdersWithoutPosition);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(ctrlError, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        					.addGap(18)
        					.addComponent(rbPositions)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(rbOnlyPositions)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(rbOrdersWithoutPosition)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(rbOrdersWithoutPosition)
        					.addComponent(rbOnlyPositions)
        					.addComponent(rbPositions))
        				.addComponent(ctrlError, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

		pack();

		// -----------------------------------------------------------------

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				PositionFuturesService.deattachRefreshObserver(myJFrame);
				myJFrame = null;
			}
		});
		
        rbPositions.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		onPositionUpdate();
        	}
        });
        rbOnlyPositions.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		onPositionUpdate();
        	}
        });
        rbOrdersWithoutPosition.addItemListener(new ItemListener() {
        	public void itemStateChanged(ItemEvent e) {
        		onPositionUpdate();
        	}
        });

	}

	// ------------------------------------------------------------------------

	@Override
	public void onPositionUpdate()
	{
		try
		{
			String text;
			
			if (rbOrdersWithoutPosition.isSelected())
				text = PositionFuturesService.toStringOrdersWithoutPosition();
			else if (rbOnlyPositions.isSelected())
				text = PositionFuturesService.toStringPositions(false);
			else
				text = PositionFuturesService.toStringPositions(true);

			textArea.setText(text);
		}
		catch (Exception e)
		{
			ctrlError.ERROR(e);
		}
	}

	// ------------------------------------------------------------------------

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
					LogService.error(e);
				}
			}
		});
	}
}
