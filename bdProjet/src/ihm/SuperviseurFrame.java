package ihm;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import bdProjet.DatabaseAccessProperties;
import bdProjet.SQLWarningsExceptions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class SuperviseurFrame extends JFrame {

	private JPanel contentPane;
	private final JTextField textField = new JTextField();
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SuperviseurFrame frame = new SuperviseurFrame();
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
	public SuperviseurFrame() {
		
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
		
		
		setResizable(false);
		setTitle("Menu Superviseur");
		
		final String log = "1";
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnConsulterRoutine = new JButton("Consulter Routine");
		btnConsulterRoutine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ConsulterToutesRoutineFrame.main(conn);
			}
		});
		btnConsulterRoutine.setBounds(240, 12, 180, 90);
		contentPane.add(btnConsulterRoutine);
		
		JButton btnVlo = new JButton("Info. vélos");
		btnVlo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				ConsulterVeloFrame.main(conn);
			}
		});
		btnVlo.setBounds(35, 12, 180, 90);
		contentPane.add(btnVlo);
				
		JButton btnNewButton = new JButton("Quitter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(240, 213, 180, 90);
		contentPane.add(btnNewButton);
	}
}
