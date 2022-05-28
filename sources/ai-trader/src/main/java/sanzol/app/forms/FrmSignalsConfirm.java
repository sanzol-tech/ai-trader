package sanzol.app.forms;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sanzol.app.task.SignalService;

import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmSignalsConfirm extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JCheckBox chkOnlyFavorites;
	private JCheckBox chkOnlyBetters;

	public FrmSignalsConfirm()
	{
		setTitle("Confirm");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 380, 150);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		chkOnlyFavorites = new JCheckBox("Only favorites");
		chkOnlyFavorites.setBounds(30, 25, 130, 23);
		contentPanel.add(chkOnlyFavorites);

		chkOnlyBetters = new JCheckBox("Only betters");
		chkOnlyBetters.setBounds(169, 25, 130, 23);
		contentPanel.add(chkOnlyBetters);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnGenerate = new JButton("GENERATE");
		btnGenerate.setActionCommand("OK");
		buttonPane.add(btnGenerate);
		
		getRootPane().setDefaultButton(btnGenerate);
		
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generate();
			}
		});
		
	}

	private void generate()
	{
		try
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
			SignalService.searchShocks(chkOnlyFavorites.isSelected(), chkOnlyBetters.isSelected());
			SignalService.saveShocks();

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			dispose();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			FrmSignalsConfirm dialog = new FrmSignalsConfirm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
