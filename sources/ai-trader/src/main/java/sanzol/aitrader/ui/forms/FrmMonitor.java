package sanzol.aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.ui.config.Styles;

public class FrmMonitor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = Constants.APP_NAME + " - Monitor";

	private JTextArea txtResult;

	public FrmMonitor()
	{
		setTitle(TITLE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 380, 620);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);
		txtResult.setBorder(new EmptyBorder(10, 10, 10, 10));

		scrollPane.setViewportView(txtResult);
	}

	public static void launch(String subtitle, String text, int x, int y)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmMonitor frame = new FrmMonitor();
					frame.setVisible(true);
					frame.setLocation(x, y);
					frame.setTitle(TITLE + " - " + subtitle);
					frame.txtResult.setText(text);
					frame.txtResult.setCaretPosition(0);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
