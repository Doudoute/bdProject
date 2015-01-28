package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;

import requetes.RequeteAbonne;

public class ReAbonnementInterditFrame extends JFrame {

	private JPanel contentPane;
	private final Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final int numClient) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReAbonnementInterditFrame frame = new ReAbonnementInterditFrame(conn, numClient);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public ReAbonnementInterditFrame(final Connection conn, final int numClient) throws SQLException {
		
		setResizable(false);
		setTitle("Re-abonnement Interdit");
		this.conn = conn;
		
		setTitle("Interdiction de ré-abonnement");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLaDateDe = new JLabel("La date de fin d'abonnement est : ");
		lblLaDateDe.setBounds(12, 122, 268, 45);
		contentPane.add(lblLaDateDe);
		
		String dateFinAbonnement = RequeteAbonne.trouverDateFinAbo(conn, numClient);
		
		JLabel lblNewLabel = new JLabel(dateFinAbonnement);
		lblNewLabel.setBounds(127, 179, 138, 15);
		contentPane.add(lblNewLabel);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnQuitter.setBounds(317, 236, 117, 25);
		contentPane.add(btnQuitter);
		
		JLabel lblVotreRabonnementEst = new JLabel("Votre ré-abonnement est interdit.");
		lblVotreRabonnementEst.setBounds(12, 12, 408, 31);
		contentPane.add(lblVotreRabonnementEst);
		
		JLabel lblVoustesEncore = new JLabel("Vous êtes encore en cours d'abonnement.");
		lblVoustesEncore.setBounds(12, 75, 333, 15);
		contentPane.add(lblVoustesEncore);
		
		JLabel lblIlNaPas = new JLabel("Il n'a pas été pris en compte.");
		lblIlNaPas.setBounds(12, 44, 243, 15);
		contentPane.add(lblIlNaPas);
	}
}
