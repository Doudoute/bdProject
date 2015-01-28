package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JButton;

import requetes.RequeteAbonne;

public class ReAbonnementAutoriseFrame extends JFrame {

	private JPanel contentPane;
	private final Connection conn;


	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final int numClient) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReAbonnementAutoriseFrame frame = new ReAbonnementAutoriseFrame(conn, numClient);
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
	public ReAbonnementAutoriseFrame(final Connection conn, final int numClient) {
		
		setResizable(false);
		setTitle("Re-abonnement Autorisé");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVotreRabonnementA = new JLabel("Votre ré-abonnement a bien été pris en compte.");
		lblVotreRabonnementA.setBounds(12, 24, 381, 15);
		contentPane.add(lblVotreRabonnementA);
		
		JLabel lblDateDeFin = new JLabel("Date de fin d'abonnement");
		lblDateDeFin.setBounds(12, 134, 237, 15);
		contentPane.add(lblDateDeFin);
	
		String dateCourante = RequeteAbonne.dateCourante();
		String dateFinAbo = RequeteAbonne.ajouterUnAnDate(dateCourante);
		
		JLabel lblNewLabel = new JLabel(dateFinAbo);
		lblNewLabel.setBounds(176, 181, 117, 15);
		contentPane.add(lblNewLabel);
		
		try {
			RequeteAbonne.reabonnement(conn, numClient);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnQuitter.setBounds(317, 236, 117, 25);
		contentPane.add(btnQuitter);
	}

}
