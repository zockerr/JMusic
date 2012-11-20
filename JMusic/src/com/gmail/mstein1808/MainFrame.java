 package com.gmail.mstein1808;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import java.awt.Cursor;
import java.awt.Window.Type;
import java.awt.Dimension;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom2.JDOMException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainFrame {

	private JFrame frmMusicSync;
	private JTextField txtPath;
	private JProgressBar progressBar;
	static SyncCore Engine;
	private JTable table;
	JLabel lblNewLabel ;

	/**
	 * Launch the application.
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Engine=new SyncCore();
					MainFrame window = new MainFrame();
					window.frmMusicSync.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
		} 
			catch (Exception e) {
				e.printStackTrace();
		}
		frmMusicSync = new JFrame();
		frmMusicSync.setTitle(Messages.getString("MainFrame.frmMusicSync.title")); //$NON-NLS-1$
		frmMusicSync.setResizable(false);
		frmMusicSync.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frmMusicSync.getContentPane().setLayout(new BorderLayout(0, 0));
		
		progressBar = new JProgressBar();
		frmMusicSync.getContentPane().add(progressBar, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		frmMusicSync.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblNewLabel = new JLabel(Messages.getString("MainFrame.lblNewLabel.text")); //$NON-NLS-1$
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		panel.add(lblNewLabel, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JButton btnSyncNow = new JButton(Messages.getString("MainFrame.btnSyncNow.text"));
		btnSyncNow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Engine.setSyncTarget(txtPath.getText());
				Engine.setStatusBar(progressBar);
				Engine.setStatusField(lblNewLabel);
				Thread syncThread = new Thread(Engine);
				syncThread.start();
			}
		});
		panel_3.add(btnSyncNow, BorderLayout.SOUTH);
		
		JLabel lblPlaylists = new JLabel(Messages.getString("MainFrame.lblPlaylists.text")); //$NON-NLS-1$
		lblPlaylists.setBorder(new EmptyBorder(5, 0, 5, 0));
		lblPlaylists.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_3.add(lblPlaylists, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		panel_3.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable(new PlaylistModel(Engine.Playlists));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(Color.GRAY));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		txtPath = new JTextField();
		txtPath.setText(Messages.getString("MainFrame.txtPath.text")); //$NON-NLS-1$
		panel_2.add(txtPath);
		txtPath.setColumns(10);
		
		JButton btnBrowse = new JButton(Messages.getString("MainFrame.btnBrowse.text")); //$NON-NLS-1$
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.showOpenDialog(chooser);
				File f=chooser.getSelectedFile();
				String Path=f.getAbsolutePath();
				txtPath.setText(Path);
			}
		});
		panel_2.add(btnBrowse);
		frmMusicSync.setBounds(100, 100, 250, 300);
		frmMusicSync.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
