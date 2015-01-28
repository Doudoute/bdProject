package ihm;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import bdProjet.DatabaseAccessProperties;
import bdProjet.SQLWarningsExceptions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;


public class Application_Borne {

	private JFrame frmVpick;
	private Connection conn;
	private static final String configurationFile = "BD.properties";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application_Borne window = new Application_Borne();
					window.frmVpick.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application_Borne() {
		conn = DBConnection();
		initialize();
	}
	
	/**
	 * Connect to the database
	 * @return
	 */
	private Connection DBConnection() {
		try{
		String jdbcDriver, dbUrl;
		  
		  DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
		  jdbcDriver	= dap.getJdbcDriver();
		  dbUrl			= dap.getDatabaseUrl();
		  
	      // Load the database driver
	      Class.forName(jdbcDriver) ;

	      // Get a connection to the database
	      Connection conn = DriverManager.getConnection(dbUrl, "meunierk", "bd2015");

		  // Print information about connection warnings
		  SQLWarningsExceptions.printWarnings(conn);
		  return conn;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Set visible or not whether b is true or false
	 * @param b says if the frame needs to be visible or not
	 */
	
	public void setVisible(boolean b) {
		this.frmVpick.setVisible(b);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVpick = new JFrame();
		frmVpick.setTitle("VéPick - Client");
		frmVpick.setBounds(100, 100, 450, 444);
		frmVpick.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVpick.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Bienvenue sur l'application VéPick");
		lblNewLabel.setBounds(111, 12, 249, 15);
		frmVpick.getContentPane().add(lblNewLabel);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quitter(conn);
			}
		});
		btnQuitter.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnQuitter.setBounds(206, 198, 154, 122);
		frmVpick.getContentPane().add(btnQuitter);
		
		JButton btnLouerUnVlo = new JButton("Louer un Vélo");
		btnLouerUnVlo.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnLouerUnVlo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Location(conn);
			}
		});
		btnLouerUnVlo.setBounds(23, 53, 144, 122);
		frmVpick.getContentPane().add(btnLouerUnVlo);
		
		JButton btnNewButton = new JButton("Réserver un Vélo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Reservation(conn);
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton.setBounds(206, 53, 154, 122);
		frmVpick.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Rendre un Vélo");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Rendu(conn);
			}
		});
		btnNewButton_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_1.setBounds(23, 198, 144, 122);
		frmVpick.getContentPane().add(btnNewButton_1);
	}
	
	// Methode lancée lorsque l'on clique sur le bouton "Louer".
	// Cache la fenêtre principale / ouvre la fenêtre de Location
	private void Location(Connection conn) {
		Location loc = new Location(this, conn);
		loc.setVisible(true);
		this.setVisible(false);
	}

	// Methode lancée lorsque l'on clique sur le bouton "Reserver".
	// Cache la fenêtre principale / ouvre la fenêtre de Reservation
	private void Reservation(Connection conn) {
		//TODO
	}
	
	// Methode lancée lorsque l'on clique sur le bouton "Rendre".
	// Cache la fenêtre principale / ouvre la fenêtre de Rendu de vélo
	private void Rendu(Connection conn) {
		//TODO
	}
	
	//Quitter l'application
	private void Quitter(Connection conn){
		frmVpick.dispose();
	}
}
