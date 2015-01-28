package ihm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import mapping.*;
import requetes.*;

public class Location {

	private Application_Borne app = null;
	private Connection conn;
	
	private Client client ;
	
	private JFrame frmVpickClient;
	private JPasswordField passwordField;
	private String station;
	
	
	public void setVisible(boolean isVisible){
		this.frmVpickClient.setVisible(isVisible);
	}


	/**
	 * Create the application.
	 * @param application_Borne 
	 */
	public Location(Application_Borne application_Borne, Connection conn, String station) {
		this.station = station;
		this.app = application_Borne;
		this.conn = conn;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVpickClient = new JFrame();
		frmVpickClient.setTitle("VéPick - Client");
		frmVpickClient.setBounds(100, 100, 450, 300);
		frmVpickClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVpickClient.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Location d'un Vélo");
		lblNewLabel.setBounds(93, 12, 246, 15);
		frmVpickClient.getContentPane().add(lblNewLabel);
		
		JLabel lblNewVoustes = new JLabel("Entrez votre code secret :");
		lblNewVoustes.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewVoustes.setBounds(12, 86, 179, 15);
		frmVpickClient.getContentPane().add(lblNewVoustes);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(187, 84, 112, 19);
		frmVpickClient.getContentPane().add(passwordField);
		JLabel lblNumeroDeCarte = new JLabel("Numero de carte bleu"
				+ "e : ");
		lblNumeroDeCarte.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNumeroDeCarte.setBounds(12, 59, 164, 15);
		frmVpickClient.getContentPane().add(lblNumeroDeCarte);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"7894929078949290", "9264323592643235"}));
		comboBox.setBounds(187, 54, 112, 24);
		frmVpickClient.getContentPane().add(comboBox);
		
		JButton btnVzaluider = new JButton("Valider");
		btnVzaluider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String numCB = (String)(comboBox.getSelectedItem());
				int code = Integer.parseInt(new String(passwordField.getPassword()));
				try {
					client = RequeteClient.retrieveClientByCBandSecretCode(conn, code, numCB);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				Next(code,numCB);
			}
		});
		btnVzaluider.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnVzaluider.setBounds(317, 81, 117, 25);
		frmVpickClient.getContentPane().add(btnVzaluider);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(298, 196, 1, 15);
		frmVpickClient.getContentPane().add(textArea);
		
		JLabel lblVousNtes = new JLabel("Vous n'êtes pas encore abonné ?");
		lblVousNtes.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblVousNtes.setBounds(12, 133, 225, 15);
		frmVpickClient.getContentPane().add(lblVousNtes);
		

		
		JButton btnMeGnrerUn = new JButton("Me générer un code ");
		btnMeGnrerUn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String numCB = (String)(comboBox.getSelectedItem());
				int code = GenererCode();
				JOptionPane.showMessageDialog(frmVpickClient, "Votre code est : "+code+"\n RETENEZ LE BIEN");
				Next(code, numCB);
			}
		});
		
		btnMeGnrerUn.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnMeGnrerUn.setBounds(235, 128, 199, 25);
		frmVpickClient.getContentPane().add(btnMeGnrerUn);
		
		JButton button = new JButton("< Retour");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Accueil();
			}

		});
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		button.setBounds(12, 236, 90, 25);
		frmVpickClient.getContentPane().add(button);
		

	}
	
	//retourne à l'accueil
	private void Accueil() {
		this.app.setVisible(true);
		this.frmVpickClient.dispose();
	}
	
	// poursuit la location avec le code secret du client 
	// ou le code généré par l'application
	private void Next(int code, String numCB) {

		try {
			ArrayList<Velo> velosDispo = RequeteVelo.getVelosDispo(conn, station);
			int numVelo = velosDispo.get(0).getNumRFID();
			int numBorne = RequeteVelo.retrieveNumBornetteByNumVelo(conn, numVelo);

			RequeteVelo.creerLocation(conn, velosDispo.get(0).getNumRFID(), client.getNumClient());
			JOptionPane.showMessageDialog(frmVpickClient, "Location créée : \n"
														+ "Client N°" +client.getNumClient()+
														"\n Velo N°"+numVelo+
														"\n Allez le chercher à la BORNE N°"+numBorne);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frmVpickClient, "Erreur dans la location du Velo. Veuillez contacter le support si le problème se répète. \n" + e.toString() );
		}
	}
	
	// Genere un code automatiquement pour un client non Abonné
	private int GenererCode() {
		
		// TODO Auto-generated method stub
		
		/* METHODE SALE (mais pratique)
		 * Générer un code aléatoire
		 * vérifier que personne d'autre ne l'a 
		 * recommencer
		 * CREER LE CLIENT dans la base
		 * et initialiser le Client client dans cette classe
		 */

		return 0;
	}
}
