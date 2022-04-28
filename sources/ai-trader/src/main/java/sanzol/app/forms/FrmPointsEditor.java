package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.task.SignalService;

public class FrmPointsEditor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Points editor";

	private static boolean isOpen = false;

	private JPanel contentPane;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel lblError;

	private JButton btnGenerate;
	private JButton btnSave;

	public FrmPointsEditor()
	{
		initComponents();

		load();
		isOpen = true;
	}

	private void initComponents() 
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 480, 517);
		setMinimumSize(new Dimension(480, 500));
		setTitle(TITLE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FrmTrader.class.getResource("/resources/rulepen.png")));
	
		textArea = new JTextArea();
		textArea.setBackground(Styles.COLOR_TEXT_AREA);
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

		scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		btnGenerate = new JButton("GENERATE");
		btnGenerate.setOpaque(true);
		btnGenerate.setBackground(Styles.COLOR_BTN);

		btnSave = new JButton("SAVE");
		btnSave.setOpaque(true);
		btnSave.setBackground(Styles.COLOR_BTN);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JCheckBox chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setSelected(true);

		JPanel pnlBottom = new JPanel();
		pnlBottom.setBorder(Styles.BORDER_UP);
		pnlBottom.setLayout(new BorderLayout(0, 0));
		lblError = new JLabel();
		lblError.setBorder(new EmptyBorder(5, 0, 5, 5));		
		lblError.setMinimumSize(new Dimension(100, 20));
		pnlBottom.add(lblError);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnGenerate)
							.addGap(18)
							.addComponent(chkOnlyFavorites)
							.addPreferredGap(ComponentPlacement.RELATED, 181, Short.MAX_VALUE)
							.addComponent(btnSave))
						.addComponent(pnlBottom, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnGenerate)
						.addComponent(chkOnlyFavorites))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pnlBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4))
		);

		contentPane.setLayout(gl_contentPane);

		pack();

		// -----------------------------------------------------------------

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				isOpen = false;
			}
		});

		btnGenerate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				generate();
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

	// ------------------------------------------------------------------------

	private void generate()
	{
		try
		{
			INFO("GENERATING SHOCKPOINTS...");
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

			SignalService.searchShocks();
			SignalService.saveShocks();
			textArea.setText(SignalService.toStringShocks());

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			INFO("New points generated !");
		}
		catch (Exception e)
		{
			ERROR(e);
		}
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
					FrmPointsEditor frame = new FrmPointsEditor();
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
