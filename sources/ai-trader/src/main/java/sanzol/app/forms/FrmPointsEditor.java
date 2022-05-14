package sanzol.app.forms;

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

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sanzol.app.config.Application;
import sanzol.app.config.Constants;
import sanzol.app.config.Styles;
import sanzol.app.task.SignalService;
import sanzol.lib.util.ExceptionUtils;

public class FrmPointsEditor extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Points editor";

	private static boolean isOpen = false;

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private JLabel lblError;

	private JCheckBox chkOnlyFavorites;

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

		btnGenerate = new JButton("GENERATE");
		btnGenerate.setOpaque(true);

		chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setSelected(true);
		
		btnSave = new JButton("SAVE");
		btnSave.setOpaque(true);
		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrollPane, Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnGenerate)
                        .addGap(6, 6, 6)
                        .addComponent(chkOnlyFavorites, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblError, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(chkOnlyFavorites)
                    .addComponent(btnGenerate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(lblError, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave))
                .addContainerGap())
        );

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

			SignalService.searchShocks(chkOnlyFavorites.isSelected());
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
