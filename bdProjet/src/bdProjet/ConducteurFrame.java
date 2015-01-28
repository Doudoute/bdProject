package bdProjet;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;

import javax.swing.SwingConstants;


public class ConducteurFrame extends JFrame {

	private JPanel contentPane;
	private final Connection conn;

	/**
	 * Launch the application.
	 * @param log 
	 */
	public static void main(final Connection conn, final String log) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConducteurFrame frame = new ConducteurFrame(conn, log);
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
	public ConducteurFrame(final Connection conn, final String log) {
		setResizable(false);
		setTitle("MENU");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnVloEndommag = new JButton("Modif.État Vélos");
		btnVloEndommag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				VeloEndommage.main(conn, log);
			}
		});
		btnVloEndommag.setBounds(35, 12, 180, 90);
		contentPane.add(btnVloEndommag);
		
		JButton btnConsulterRoutine = new JButton("Consulter Routine");
		btnConsulterRoutine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ConsulterRoutineFrame.main(conn, log);
			}
		});
		btnConsulterRoutine.setBounds(240, 12, 180, 90);
		contentPane.add(btnConsulterRoutine);
		
		JButton btnDplacerVlos = new JButton("Déplacer Vélos");
		btnDplacerVlos.setBounds(35, 111, 180, 90);
		contentPane.add(btnDplacerVlos);
		
		JButton btnValiderRoutine = new JButton("Valider Routine");
		btnValiderRoutine.setBounds(240, 111, 180, 90);
		contentPane.add(btnValiderRoutine);
		
		JButton btnRenseignerRparations = new JButton("Rens.Réparations");
		btnRenseignerRparations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRenseignerRparations.setBounds(35, 213, 180, 90);
		contentPane.add(btnRenseignerRparations);
		
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
