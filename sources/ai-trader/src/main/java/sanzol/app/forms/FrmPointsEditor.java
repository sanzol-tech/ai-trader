package sanzol.app.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.task.LogService;
import sanzol.app.task.SignalService;
import sanzol.lib.util.ExceptionUtils;

public class FrmPointsEditor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Points editor";

	private static FrmPointsEditor myJFrame = null;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel lblError;
	private JButton btnSave;

	public FrmPointsEditor()
	{
		initComponents();

		load();
	}

	private void initComponents() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 500);
		setMinimumSize(new Dimension(480, 400));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmGrid.class.getResource("/resources/monitor.png")));
		setLocationRelativeTo(null);
	
		lblError = new JLabel();
		
		textArea = new JTextArea();
		textArea.setBackground(Styles.COLOR_TEXT_AREA_BG);
		textArea.setForeground(Styles.COLOR_TEXT_AREA_FG);
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(UIManager.getBorder("TextField.border"));
		
		btnSave = new JButton("SAVE");
		btnSave.setOpaque(true);
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(lblError, GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        					.addGap(18)
        					.addComponent(btnSave)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(lblError, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnSave))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();

		// -----------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				myJFrame = null;
			}
		});

		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				save();
			}
		});

	}

	private void load()
	{
		INFO("");
		try
		{
			Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, Constants.SHOCKPOINTS_FILENAME);
			if (path.toFile().exists())
			{
				String content = new String(Files.readAllBytes(path));

				textArea.setText(content);

				String modified = (new SimpleDateFormat("dd/MM HH:mm")).format(new Date(path.toFile().lastModified()));
				INFO("Last modification: " + modified);
			}
			else
			{
				ERROR("Missing Points");
			}
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

	private void save()
	{
		try
		{
			String content = textArea.getText();
			Path path = Paths.get(Constants.DEFAULT_USER_FOLDER, Constants.SHOCKPOINTS_FILENAME);
			Files.write(path, content.getBytes(StandardCharsets.UTF_8));

			SignalService.loadShocks();

			INFO("Saved points !");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
	}

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
					myJFrame = new FrmPointsEditor();
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

	// ------------------------------------------------------------------------

	public static void main(String[] args)
	{
		Application.initialize();
		launch();
	}
}
