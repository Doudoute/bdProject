package ihm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;

import bdProjet.DatabaseAccessProperties;
import bdProjet.SQLWarningsExceptions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbonnementReabonnementFrame extends JFrame {

	private JPanel contentPane;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbonnementReabonnementFrame frame = new AbonnementReabonnementFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AbonnementReabonnementFrame() {
		setResizable(false);
		setTitle("Abonnement / Reabonnement");
		
		/**OUVERTURE BDD**/
		try{
			String jdbcDriver, dbUrl;
			DatabaseAccessProperties dap = new DatabaseAccessProperties("BD.properties");
			jdbcDriver	= dap.getJdbcDriver();
			dbUrl			= dap.getDatabaseUrl();
			 
		    // Load the database driver
		    Class.forName(jdbcDriver) ;
	
		    // Get a connection to the database
		    Connection conn = DriverManager.getConnection(dbUrl, "meunierk", "bd2015");
		    this.conn = conn;
			// Print information about connection warnings
			SQLWarningsExceptions.printWarnings(conn);
		}
		catch( SQLException se ) {
		  
		  // Print information about SQL exceptions
		  SQLWarningsExceptions.printExceptions(se);
		  
	      return;
		}
		catch( Exception e ) {
	      System.err.println( "Exception: " + e.getMessage()) ;
	      e.printStackTrace();
	      return;
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel choix = new JLabel("Que souhaitez-vous faire ?");
		choix.setBounds(128, 12, 201, 40);
		contentPane.add(choix);
		
		JButton btnAbonnement = new JButton("Abonnement");
		btnAbonnement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AbonnementRenseignementsFrame.main(conn);
			}
		});
		btnAbonnement.setBounds(12, 97, 201, 120);
		contentPane.add(btnAbonnement);
		
		JButton btnReAbo = new JButton("Re-abonnement");
		btnReAbo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ReAbonnementFrame.main(conn);
			}
		});
		btnReAbo.setBounds(241, 97, 193, 120);
		contentPane.add(btnReAbo);
		
		JButton btnVisualiserLeTarif = new JButton("Visualiser le tarif de l'année en cours");
		btnVisualiserLeTarif.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				TarifLocationFrame.main(conn);
			}
		});
		btnVisualiserLeTarif.setBounds(60, 229, 312, 25);
		contentPane.add(btnVisualiserLeTarif);

	}
}
