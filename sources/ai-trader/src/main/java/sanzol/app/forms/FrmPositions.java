package sanzol.app.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.service.LogService;
import sanzol.app.service.PositionListener;
import sanzol.app.service.PositionService;
import sanzol.lib.util.ExceptionUtils;

public class FrmPositions extends JFrame implements PositionListener
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Positions";

	private static FrmPositions myJFrame = null;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel lblError;
	
	private JCheckBox chkIncludeOrders;

	public FrmPositions()
	{
		initComponents();

		onPositionUpdate();
		PositionService.attachRefreshObserver(this);
	}

	private void initComponents() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 500);
		setMinimumSize(new Dimension(760, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

        lblError = new JLabel();

        textArea = new JTextArea();
		textArea.setBackground(Styles.COLOR_TEXT_AREA_BG);
		textArea.setForeground(Styles.COLOR_TEXT_AREA_FG);
		textArea.setEditable(false);
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);

		chkIncludeOrders = new JCheckBox("Include orders");
		chkIncludeOrders.setSelected(true);
		chkIncludeOrders.setHorizontalAlignment(SwingConstants.RIGHT);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblError, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(chkIncludeOrders)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblError, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkIncludeOrders))
                .addContainerGap())
        );

		pack();

		// -----------------------------------------------------------------

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				PositionService.deattachRefreshObserver(myJFrame);
				myJFrame = null;
			}
		});

	}
	
	// ------------------------------------------------------------------------

	@Override
	public void onPositionUpdate()
	{
		try
		{
			String text = PositionService.toStringPositions(chkIncludeOrders.isSelected());
			textArea.setText(text);
		}
		catch (Exception e)
		{
			ERROR(e);
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

}
