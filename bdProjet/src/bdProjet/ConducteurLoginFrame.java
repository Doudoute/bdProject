package bdProjet;
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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ConducteurLoginFrame extends JFrame {

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
					ConducteurLoginFrame frame = new ConducteurLoginFrame();
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
	public ConducteurLoginFrame() {
		setResizable(false);
		setTitle("LOGIN");
		
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
		
		textField.setBounds(125, 98, 180, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel LOGIN = new JLabel("LOGIN");
		LOGIN.setBounds(189, 71, 70, 15);
		contentPane.add(LOGIN);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				String log = textField.getText();
				ConducteurFrame.main(conn,log);
			}
		});
		btnOk.setBounds(158, 129, 117, 25);
		contentPane.add(btnOk);
	}
}
