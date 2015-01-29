package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;

import requetes.RequeteBornette;
import requetes.RequeteVehiculeRegulation;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class DebarquementFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final String log) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebarquementFrame frame = new DebarquementFrame(conn, log);
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
	public DebarquementFrame(final Connection conn, final String log) {
		setTitle("Debarquement velo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 38, 165, 172);
		contentPane.add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		String[] listeVeloEmbarque = new String[30];
		try {
			listeVeloEmbarque = RequeteVehiculeRegulation.getVeloEmbarques(conn,log);
			DefaultListModel model = new DefaultListModel();
			for (int i = 0; i < listeVeloEmbarque.length; i++) {
				model.addElement(listeVeloEmbarque[i]);
			}
			list.setModel(model);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(243, 38, 165, 172);
		contentPane.add(scrollPane_1);
		
		JList list_1 = new JList();
		scrollPane_1.setViewportView(list_1);
		
		String[] listeBornesStr = new String[30];
		try {
			listeBornesStr = RequeteBornette.getAdresseBornes(conn);
			DefaultListModel model2 = new DefaultListModel();
			for (int i = 0; i < listeBornesStr.length; i++) {
				model2.addElement(listeBornesStr[i]);
			}
			list_1.setModel(model2);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JButton btnAssocier = new JButton("Associer");
		btnAssocier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnAssocier.setBounds(60, 236, 117, 25);
		contentPane.add(btnAssocier);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ConducteurFrame.main(conn, log);
			}
		});
		btnRetour.setBounds(243, 236, 117, 25);
		contentPane.add(btnRetour);
		
		JLabel lblVlos = new JLabel("Vélos");
		lblVlos.setBounds(60, 24, 70, 15);
		contentPane.add(lblVlos);
		
		JLabel lblStations = new JLabel("Stations");
		lblStations.setBounds(290, 24, 70, 15);
		contentPane.add(lblStations);
	}
}
