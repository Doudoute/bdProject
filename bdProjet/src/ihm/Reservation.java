package ihm;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import requetes.RequeteClient;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

public class Reservation {

	private JFrame frame;
	private final JLabel lblNewLabel = new JLabel("Réserver votre Vélo");
	private JPasswordField passwordField;
	private Application_Borne ab;
	private Connection conn;
	private String station;


	/**
	 * Create the application.
	 */
	public Reservation(Application_Borne ab, Connection conn, String station) {
		this.ab = ab;
		this.conn = conn;
		this.station = station;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
			try {
			frame = new JFrame();
			frame.setBounds(100, 100, 450, 322);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			lblNewLabel.setBounds(98, 12, 214, 31);
			frame.getContentPane().add(lblNewLabel);
			
			JLabel label = new JLabel("Numero de carte bleue : ");
			label.setFont(new Font("Dialog", Font.PLAIN, 12));
			label.setBounds(12, 109, 164, 15);
			frame.getContentPane().add(label);
			
			JLabel label_1 = new JLabel("Entrez votre code secret :");
			label_1.setFont(new Font("Dialog", Font.PLAIN, 12));
			label_1.setBounds(12, 136, 179, 15);
			frame.getContentPane().add(label_1);
			
			final JComboBox<String> comboBox = new JComboBox<String>();
			comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"7894929078949290", "9264323592643235"}));
			comboBox.setBounds(187, 104, 112, 24);
			frame.getContentPane().add(comboBox);
			
			passwordField = new JPasswordField();
			passwordField.setBounds(187, 134, 112, 19);
			frame.getContentPane().add(passwordField);
			
			JButton btnReserver = new JButton("Reserver >");
			
			btnReserver.setFont(new Font("Dialog", Font.PLAIN, 12));
			btnReserver.setBounds(317, 258, 117, 25);
			frame.getContentPane().add(btnReserver);
			
			JButton button_2 = new JButton("< Retour");
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Accueil();
				}


			});
			button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
			button_2.setBounds(12, 258, 90, 25);
			frame.getContentPane().add(button_2);
			
			JLabel lblNumeroDuClient = new JLabel("Numero du Client :");
			lblNumeroDuClient.setFont(new Font("Dialog", Font.PLAIN, 12));
			lblNumeroDuClient.setBounds(12, 82, 164, 15);
			frame.getContentPane().add(lblNumeroDuClient);
			
			final JComboBox<String> comboBox_1 = new JComboBox<String>();
			ArrayList<String> numAbo;
			numAbo = RequeteClient.getAllNumAbo(conn);
		
			String[] numAboTab = new String[numAbo.size()];
			
			for(int i =0; i<numAbo.size(); i++){
				numAboTab[i] = numAbo.get(i);
			}

			
			comboBox_1.setModel(new DefaultComboBoxModel<String>(numAboTab));
			comboBox_1.setBounds(187, 77, 50, 24);
			frame.getContentPane().add(comboBox_1);
			
			final JRadioButton rdbtnNewRadioButton = new JRadioButton("Pour une journée");
			rdbtnNewRadioButton.setSelected(true);
			rdbtnNewRadioButton.setBounds(187, 164, 149, 23);
			frame.getContentPane().add(rdbtnNewRadioButton);
			
			final JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Pour une période");
			rdbtnNewRadioButton_1.setBounds(187, 191, 149, 23);
			frame.getContentPane().add(rdbtnNewRadioButton_1);
			
			ButtonGroup group = new ButtonGroup();
		    group.add(rdbtnNewRadioButton);
		    group.add(rdbtnNewRadioButton_1);
		    
		    btnReserver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int numClient = Integer.parseInt((String)(comboBox_1.getSelectedItem()));
					if(rdbtnNewRadioButton.isSelected()){
						ReservationDay(numClient);
					}
					else{
						ReservationPeriod(numClient);
					}
				}
			});
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	//retourne à l'accueil
	private void Accueil() {
		this.ab.setVisible(true);
		this.frame.dispose();
	}
	
	//etape 2 de la réservation (un jour)
	private void ReservationDay(int numClient) {
		ReservationDay rsd = new ReservationDay(this, conn, station, numClient);
		rsd.setVisible(true);
		this.setVisible(false);
	}
	
	//etape 2 de la réservation (une période)
		private void ReservationPeriod(int numClient) {
			ReservationPeriod rsp = new ReservationPeriod(this, conn, station, numClient);
			rsp.setVisible(true);
			this.setVisible(false);
		}
				
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
}
