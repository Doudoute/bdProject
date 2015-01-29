package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import requetes.RequeteBornette;
import requetes.RequeteStation;
import requetes.RequeteTarif;
import requetes.RequeteVehiculeRegulation;
import requetes.RequeteVelo;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;

import mapping.Velo;

public class ConsulterVeloFrame extends JFrame {

	protected static JLabel lblNbVelo = null;
	protected static JLabel lblNbHS = null;
	protected static JLabel lblNbLibre = null;
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
					ConsulterVeloFrame frame = new ConsulterVeloFrame(conn);
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
	public ConsulterVeloFrame(final Connection conn) {
		
		setResizable(false);
		setTitle("Consultation vélo");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblStation = new JLabel("Station :");
		lblStation.setBounds(50, 12, 200, 15);
		contentPane.add(lblStation);
		
		final JComboBox<String> lblXx = new JComboBox<String>();
		
		String[] listeStation = null;
		try {
			ArrayList<String> myArrayList = RequeteStation.getAllStationAdresses(conn);
			listeStation = myArrayList.toArray(new String[myArrayList.size()]);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lblXx.setModel(new DefaultComboBoxModel<String>(listeStation));
		lblXx.setBounds(200, 12, 200, 25);
		lblXx.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String adresseSelectionnee = (String) lblXx.getSelectedItem();
				try {
					lblNbVelo.setText(String.valueOf(RequeteVelo.getVelosDispo(conn, adresseSelectionnee).size()));
					lblNbHS.setText(String.valueOf(RequeteVelo.getVelosHS(conn, adresseSelectionnee).size()));
					lblNbLibre.setText(String.valueOf(RequeteStation.getAllBornetteLibre(conn).size()));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				repaint();
		    }
		});
		contentPane.add(lblXx);
		
		JLabel lblVelo = new JLabel("Nombre de vélo dispo :");
		lblVelo.setBounds(50, 40, 200, 15);
		contentPane.add(lblVelo);
		
		lblNbVelo = new JLabel("");
		lblNbVelo.setBounds(300, 40, 200, 15);
		contentPane.add(lblNbVelo);
		
		JLabel lblVeloHS = new JLabel("Nombre de vélos endommagés :");
		lblVeloHS.setBounds(50, 60, 200, 15);
		contentPane.add(lblVeloHS);
		
		lblNbHS = new JLabel("");
		lblNbHS.setBounds(300, 60, 200, 15);
		contentPane.add(lblNbHS);
		
		JLabel lblLibre = new JLabel("Nombre de places libres");
		lblLibre.setBounds(50, 80, 200, 15);
		contentPane.add(lblLibre);
		
		lblNbLibre = new JLabel("");
		lblNbLibre.setBounds(300, 80, 200, 15);
		contentPane.add(lblNbLibre);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				SuperviseurFrame.main(null);
			}
		});
		btnRetour.setBounds(285, 219, 117, 25);
		contentPane.add(btnRetour);
	}

}
