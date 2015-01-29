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
import javax.swing.JTextField;
import javax.swing.JButton;

import requetes.RequeteAbonne;
import requetes.RequeteClient;

public class AbonnementRealisationFrame extends JFrame {

	private JPanel contentPane;
	private final Connection conn;
	private JTextField textField;
	private JTextField textField_1;


	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final String nom, final String prenom, final String dateNaissance, final String sexe, final String adresse, final String numCB, final int codeSecret) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbonnementRealisationFrame frame = new AbonnementRealisationFrame(conn, nom, prenom, dateNaissance, sexe, adresse, numCB, codeSecret);
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
	public AbonnementRealisationFrame(final Connection conn, final String nom, final String prenom, final String dateNaissance, final String sexe, final String adresse, final String numCB, final int codeSecret) {
		
		setResizable(false);
		setTitle("Abonnement");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVotreAbonnementA = new JLabel("Votre abonnement a bien été pris en compte.");
		lblVotreAbonnementA.setBounds(12, 12, 404, 15);
		contentPane.add(lblVotreAbonnementA);
		
		JLabel lblVotreNumroDe = new JLabel("Votre numéro de client est le suivant");
		lblVotreNumroDe.setBounds(12, 55, 304, 15);
		contentPane.add(lblVotreNumroDe);
		
		
		JLabel lblVoustreAbonn = new JLabel("Vous être abonné pour une durée d'un an.");
		lblVoustreAbonn.setBounds(12, 126, 335, 15);
		contentPane.add(lblVoustreAbonn);
		
		JLabel lblDateDeFin = new JLabel("Date de fin d'abonnement");
		lblDateDeFin.setBounds(12, 150, 227, 15);
		contentPane.add(lblDateDeFin);
		
		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnValider.setBounds(317, 236, 117, 25);
		contentPane.add(btnValider);
		
		int numClient = 0;
		try {
			numClient = RequeteClient.attribuerNumClient(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			RequeteClient.ajouterClient(conn, numClient, numCB, codeSecret);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			RequeteAbonne.ajouterAbonne(conn, numClient, nom, prenom, dateNaissance, sexe, adresse);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lblNumClient = new JLabel(Integer.toString(numClient));
		lblNumClient.setBounds(83, 82, 246, 15);
		contentPane.add(lblNumClient);
		
		String dateFinAbo = RequeteAbonne.ajouterUnAnDate(RequeteAbonne.dateCourante());
		
		JLabel lblDateFinAbo = new JLabel(dateFinAbo);
		lblDateFinAbo.setBounds(83, 178, 246, 15);
		contentPane.add(lblDateFinAbo);
	}
}
