package ihm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import mapping.Velo;
import requetes.RequeteVelo;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RendreVelo {

	private JFrame frmVpickClient;
	private Connection conn;
	private JPasswordField passwordField;
	private Application_Borne ab;
	private String station;

	/**
	 * Create the application.
	 */
	public RendreVelo(Application_Borne ab, Connection conn, String station) {
		this.conn=conn;
		this.ab = ab;
		this.station = station;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVpickClient = new JFrame();
		frmVpickClient.setTitle("Vépick - Client");
		frmVpickClient.setBounds(100, 100, 450, 300);
		frmVpickClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVpickClient.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Rendre un vélo");
		lblNewLabel.setBounds(124, 12, 222, 15);
		frmVpickClient.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Code secret : ");
		lblNewLabel_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(12, 81, 136, 15);
		frmVpickClient.getContentPane().add(lblNewLabel_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(100, 77, 136, 24);
		frmVpickClient.getContentPane().add(passwordField);
		
		JLabel lblNewLabel_3 = new JLabel("Velo rendu");
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(12, 123, 96, 15);
		frmVpickClient.getContentPane().add(lblNewLabel_3);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		try {
			ArrayList<Velo> listeVelos = RequeteVelo.getVelosLoues(conn);
			String[] listeVelosTab = new String[listeVelos.size()];
			for(int i=0; i<listeVelos.size(); i++){
				listeVelosTab[i] = new Integer(listeVelos.get(i).getNumRFID()).toString();
			}
			comboBox.setModel(new DefaultComboBoxModel<String>( listeVelosTab ));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		comboBox.setBounds(100, 113, 70, 24);
		frmVpickClient.getContentPane().add(comboBox);
		
		JButton button = new JButton("< Retour");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Accueil();
			}
		});
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		button.setBounds(12, 236, 117, 25);
		frmVpickClient.getContentPane().add(button);
		
		JButton btnRendre = new JButton("Rendre");
		
		btnRendre.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRendre.setBounds(317, 236, 117, 25);
		frmVpickClient.getContentPane().add(btnRendre);
		
		btnRendre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!passwordField.getPassword().equals("")){
					int code = Integer.parseInt(new String(passwordField.getPassword()));
					int numVelo = Integer.parseInt((String)comboBox.getSelectedItem());
					Rendre(code, numVelo);
				}
			}
		});
	}
	
	//retourne à l'accueil
	private void Accueil() {
		this.ab.setVisible(true);
		this.frmVpickClient.dispose();
	}

	public void setVisible(boolean b) {
		this.frmVpickClient.setVisible(b);
	}
	
	private void Rendre(int code, int numVelo) {
		try {
			ArrayList<Velo> velosDispo = RequeteVelo.getVelosDispo(conn, station);
			int numBorne = RequeteVelo.retrieveNumBornetteByNumVelo(conn, numVelo);
			
			Timestamp now = new Timestamp(System.currentTimeMillis());
			Timestamp loc = RequeteVelo.getDateDebutLocation(conn, numVelo); 
			
			

			if(RequeteVelo.isSecretCodeCheckedForVelo(conn, code, numVelo)){
				
			    //retire le vélo des locations
				RequeteVelo.removeVeloFromLocation(conn, numVelo);

				
				int numBornette = RequeteVelo.getNumBornetteLibre(conn, station);

				if(numBornette!=-1){
				    // insérer le vélo dans les stocks
					RequeteVelo.putVeloInStation(conn, numVelo, numBornette);
					System.out.println("Put OK ");
					
				    // changer l'état du vélo
					RequeteVelo.miseAJourVelo(conn, numVelo, "enStation");
					System.out.println("MàJ OK");
					
				    // mettre l'amende si jamais la durée excede 12h (43200000 millisecondes)
					if ((now.getTime() - loc.getTime()) > 43200000 ){
						// créer l'amende
						RequeteVelo.creerAmende(conn, numVelo);
					}
					
					//mettre la date de fin à la location
					RequeteVelo.mettreFinLocation(conn, now, numVelo);
					JOptionPane.showMessageDialog(frmVpickClient, "Vous avez rendu le vélo : "+ numVelo);

					
				}
				else{
					JOptionPane.showMessageDialog(frmVpickClient, "Plus aucune bornette libre !");
				}

				
			}
			else{
				JOptionPane.showMessageDialog(frmVpickClient, "Erreur de code secret !");
			}
			
			
						
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(frmVpickClient, "Erreur dans le rendu du Velo. Veuillez contacter le support si le problème se répète. \n" + e.toString() );
		}
	}
}
