package sanzol.aitrader.ui.forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import sanzol.aitrader.be.config.Constants;
import sanzol.aitrader.ui.config.Styles;

public class FrmMonitor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JTextArea txtResult;

	public FrmMonitor()
	{
		setTitle(Constants.APP_NAME + " - Monitor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmCoin.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		txtResult = new JTextArea();
		txtResult.setFont(new Font("Courier New", Font.PLAIN, 12));
		txtResult.setBackground(Styles.COLOR_TEXT_AREA_BG);
		txtResult.setForeground(Styles.COLOR_TEXT_AREA_FG);
		txtResult.setEditable(false);

		scrollPane.setViewportView(txtResult);
	}

	public static void launch(String title, String text)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					FrmMonitor frame = new FrmMonitor();
					frame.setVisible(true);
					frame.setTitle(Constants.APP_NAME + " - Monitor - " + title);
					frame.txtResult.setText(text);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
