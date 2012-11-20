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
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame {

	private JFrame frmMusicSync;
	private JTextField txtPath;
	private JProgressBar progressBar;
	static SyncCore Engine;
	private JTable table;
	JLabel lblNewLabel ;
	private JComboBox comboBox;
	private JLabel lblNewLabel_1;

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
		frmMusicSync.getContentPane().setLayout(new MigLayout("", "[450px,grow]", "[][][][][][][][14px]"));
		
		txtPath = new JTextField();
		frmMusicSync.getContentPane().add(txtPath, "flowx,cell 0 0,growx");
		txtPath.setText(Messages.getString("MainFrame.txtPath.text"));
		txtPath.setColumns(10);
		
		JLabel lblPlaylists = new JLabel(Messages.getString("MainFrame.lblPlaylists.text")); //$NON-NLS-1$
		frmMusicSync.getContentPane().add(lblPlaylists, "cell 0 1");
		lblPlaylists.setBorder(new EmptyBorder(5, 0, 5, 0));
		lblPlaylists.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JScrollPane scrollPane = new JScrollPane();
		frmMusicSync.getContentPane().add(scrollPane, "cell 0 2,grow");
		
		table = new JTable(new PlaylistModel(Engine.Playlists));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(Color.GRAY));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		lblNewLabel_1 = new JLabel(Messages.getString("MainFrame.lblNewLabel_1.text")); //$NON-NLS-1$
		frmMusicSync.getContentPane().add(lblNewLabel_1, "flowx,cell 0 3");
		
		comboBox = new JComboBox(new String[] {
				Messages.getString("FS.ArtistAlbumSong"),
				Messages.getString("FS.AlbumSong"),
				Messages.getString("FS.ArtistSong"),
				Messages.getString("FS.Song"),
		});
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Action performed!");
				JComboBox source = (JComboBox)e.getSource();
				Engine.setSelectedIndex(source.getSelectedIndex());
				System.out.println(source.getSelectedIndex());
			}
		});

		frmMusicSync.getContentPane().add(comboBox, "cell 0 3,alignx right");
		
		JButton btnSyncNow = new JButton(Messages.getString("MainFrame.btnSyncNow.text"));
		frmMusicSync.getContentPane().add(btnSyncNow, "cell 0 5,growx");
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
		
		lblNewLabel = new JLabel(Messages.getString("MainFrame.lblNewLabel.text")); //$NON-NLS-1$
		frmMusicSync.getContentPane().add(lblNewLabel, "cell 0 6,growx");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		progressBar = new JProgressBar();
		frmMusicSync.getContentPane().add(progressBar, "cell 0 7,growx,aligny top");
		
		JButton btnBrowse = new JButton(Messages.getString("MainFrame.btnBrowse.text")); //$NON-NLS-1$
		frmMusicSync.getContentPane().add(btnBrowse, "cell 0 0");
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
		frmMusicSync.setBounds(100, 100, 250, 350);
		frmMusicSync.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
