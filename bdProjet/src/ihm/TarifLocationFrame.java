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

import requetes.RequeteTarif;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TarifLocationFrame extends JFrame {

	private JPanel contentPane;
	private Connection conn;
	private JTextField textField;


	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TarifLocationFrame frame = new TarifLocationFrame(conn);
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
	public TarifLocationFrame(final Connection conn) {
		
		setResizable(false);
		setTitle("Consultation tarif");
		this.conn = conn;
		
		int tarifAnneeEnCours = 0;
		
		try {
			tarifAnneeEnCours = RequeteTarif.trouverTarifAnnee(conn, RequeteTarif.anneeCourante());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVoiciLeMontant = new JLabel("Voici le montant en euros d'une location a l'année");
		lblVoiciLeMontant.setBounds(12, 68, 364, 15);
		contentPane.add(lblVoiciLeMontant);
		
		
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AbonnementReabonnementFrame.main(null);
			}
		});
		btnRetour.setBounds(285, 219, 117, 25);
		contentPane.add(btnRetour);
		
		JLabel lblNewLabel = new JLabel(Integer.toString(tarifAnneeEnCours));
		lblNewLabel.setBounds(159, 95, 194, 15);
		contentPane.add(lblNewLabel);
	}

}
