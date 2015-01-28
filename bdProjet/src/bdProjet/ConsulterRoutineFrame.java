package bdProjet;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;


public class ConsulterRoutineFrame extends JFrame {

	private JPanel contentPane;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final String log) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConsulterRoutineFrame frame = new ConsulterRoutineFrame(conn, log);
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
	public ConsulterRoutineFrame(final Connection conn, final String log) {
		setTitle("Consulter Routine");
		this.conn=conn;

		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVhicule = new JLabel("Véhicule :");
		lblVhicule.setBounds(97, 12, 70, 15);
		contentPane.add(lblVhicule);
		
		JLabel lblXx = new JLabel(log);
		lblXx.setBounds(179, 12, 70, 15);
		contentPane.add(lblXx);
		
		JLabel lblListeDesRoutines = new JLabel("Liste des routines :");
		lblListeDesRoutines.setBounds(97, 54, 152, 15);
		contentPane.add(lblListeDesRoutines);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(97, 81, 262, 180);
		contentPane.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
	}
}
