package ihm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JList;

import requetes.RequeteBornette;
import requetes.RequeteEmbarque;
import requetes.RequeteVelo;


public class VeloEndommage extends JFrame {

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
					VeloEndommage frame = new VeloEndommage(conn, log);
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
	
	private String adresseSelectionnee;
	private String numeroVelo;
	
	public VeloEndommage(final Connection conn, final String log) {
		setResizable(false);
		setTitle("Declaration dégradations");
		
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblStations = new JLabel("Stations");
		lblStations.setBounds(12, 12, 70, 15);
		contentPane.add(lblStations);
		
		JScrollPane listeBornes = new JScrollPane();
		listeBornes.setBounds(12, 32, 203, 229);
		contentPane.add(listeBornes);
		
		final JList list = new JList();
		listeBornes.setViewportView(list);
		
		String[] listeBornesStr = new String[30];
		try {
			listeBornesStr = RequeteBornette.getAdresseBornes(conn);
			DefaultListModel model = new DefaultListModel();
			for (int i = 0; i < listeBornesStr.length; i++) {
				model.addElement(listeBornesStr[i]);
			}
			list.setModel(model);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JLabel lblVlos = new JLabel("Vélos");
		lblVlos.setBounds(284, 12, 70, 15);
		contentPane.add(lblVlos);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(284, 32, 70, 229);
		contentPane.add(scrollPane_1);
		
		
		final JLabel lblEtatmodif = new JLabel("");
		lblEtatmodif.setBounds(390, 209, 224, 15);
		contentPane.add(lblEtatmodif);
		
		
		final JList list_1 = new JList();
		scrollPane_1.setViewportView(list_1);
		
		
		

		JButton button = new JButton(">");
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adresseSelectionnee = (String) list.getSelectedValue();
				System.out.println(adresseSelectionnee);
				String[] listeVelosStr = new String[50];
				try {
					listeVelosStr = RequeteBornette.getVeloAssocieBorne(conn, adresseSelectionnee);
					DefaultListModel model2 = new DefaultListModel();
					for (int i = 0; i < listeVelosStr.length; i++) {
						model2.addElement(listeVelosStr[i]);
					}
					list_1.setModel(model2);
										
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				repaint();
			}
		});
		button.setBounds(227, 81, 45, 39);
		contentPane.add(button);		
		
		
		
		JLabel lblNumro = new JLabel("Numéro :");
		lblNumro.setBounds(429, 34, 70, 15);
		contentPane.add(lblNumro);
		
		final JLabel lblNewLabel = new JLabel(" ");
		lblNewLabel.setBounds(511, 34, 70, 15);
		contentPane.add(lblNewLabel);
		

		JLabel lblEtat = new JLabel("Etat :");
		lblEtat.setBounds(429, 67, 45, 15);
		contentPane.add(lblEtat);
		
		ButtonGroup etatVelo = new ButtonGroup();
		
		final JRadioButton rdbtnEmbarqu = new JRadioButton("Embarqué");
		rdbtnEmbarqu.setSelected(true);
		rdbtnEmbarqu.setBounds(429, 90, 149, 23);
		contentPane.add(rdbtnEmbarqu);
		
		final JRadioButton rdbtnEnPanne = new JRadioButton("En Panne");
		rdbtnEnPanne.setBounds(429, 117, 149, 23);
		contentPane.add(rdbtnEnPanne);
		
		final JRadioButton rdbtnEnStation = new JRadioButton("En Station");
		rdbtnEnStation.setSelected(true);
		rdbtnEnStation.setBounds(429, 144, 149, 23);
		contentPane.add(rdbtnEnStation);
		
		final JRadioButton rdbtnEnRparation = new JRadioButton("En Réparation");
		rdbtnEnRparation.setBounds(429, 171, 149, 23);
		contentPane.add(rdbtnEnRparation);
		
		etatVelo.add(rdbtnEnStation);
		etatVelo.add(rdbtnEnPanne);
		etatVelo.add(rdbtnEmbarqu);
		etatVelo.add(rdbtnEnRparation);

		JButton button_1 = new JButton(">");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numeroVelo = (String) list_1.getSelectedValue();
				String numStr = new Integer(numeroVelo).toString();
				lblNewLabel.setText(numStr);
				try {
					String etatVelo = RequeteVelo.getEtat(conn, Integer.parseInt(numeroVelo));
					switch (etatVelo) {
					case "enStation":
						rdbtnEnStation.setSelected(true);
						break;
					case "enPanne":
						rdbtnEnPanne.setSelected(true);
						break;
					case "reparation":
						rdbtnEnRparation.setSelected(true);
						break;
					default:
						rdbtnEmbarqu.setSelected(true);
						break;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				lblEtatmodif.setText("");
				repaint();
			}
		});
		button_1.setBounds(366, 81, 45, 39);
		contentPane.add(button_1);
				
				
		
		JButton btnModifier = new JButton("Modifier");
		btnModifier.setToolTipText("Vélos Endommagés");
		btnModifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rdbtnEnStation.isSelected()) {
						RequeteVelo.miseAJourVelo(conn, Integer.parseInt(numeroVelo), "enStation");
					}
					else if (rdbtnEnPanne.isSelected()) {
						RequeteVelo.miseAJourVelo(conn, Integer.parseInt(numeroVelo), "enPanne");
					}
					else if (rdbtnEmbarqu.isSelected()){
						RequeteVelo.miseAJourVelo(conn, Integer.parseInt(numeroVelo), "embarque");
						RequeteEmbarque.setEmbarquement(conn, numeroVelo, log, adresseSelectionnee);
					}				
					else if (rdbtnEnRparation.isSelected()){
						RequeteVelo.miseAJourVelo(conn, Integer.parseInt(numeroVelo), "reparation");
					}
					
					lblEtatmodif.setText("Modification effectuée");
					lblEtatmodif.setForeground(new Color(0, 255, 0));
					
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnModifier.setBounds(366, 236, 117, 25);
		contentPane.add(btnModifier);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ConducteurFrame.main(conn, log);
			}
		});
		btnRetour.setBounds(497, 236, 117, 25);
		contentPane.add(btnRetour);
		
	}
}
