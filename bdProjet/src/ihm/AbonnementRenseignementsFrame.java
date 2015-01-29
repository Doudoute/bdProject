package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import requetes.RequeteAbonne;
import requetes.RequeteClient;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import java.awt.Color;

public class AbonnementRenseignementsFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldNom;
	private JTextField textFieldPrenom;
	private JTextField textFieldJourNaissance;
	private JTextField textFieldMoisNaissance;
	private JTextField textFieldAnneeNaissance;
	private JTextField textFieldAdresse;
	private JTextField textFieldNumeroCarte;
	private JTextField textFieldCodeSecret;

	private final Connection conn;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	
	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AbonnementRenseignementsFrame frame = new AbonnementRenseignementsFrame(conn);
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
	public AbonnementRenseignementsFrame(final Connection conn) {
		setResizable(false);
		setTitle("Renseignements Abonnement");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNom = new JLabel("Nom");
		lblNom.setBounds(12, 12, 70, 15);
		contentPane.add(lblNom);
		
		JLabel lblPrnom = new JLabel("Prénom");
		lblPrnom.setBounds(12, 39, 70, 15);
		contentPane.add(lblPrnom);
		
		JLabel lblDateDeNaissance = new JLabel("Date de naissance");
		lblDateDeNaissance.setBounds(12, 66, 145, 15);
		contentPane.add(lblDateDeNaissance);
		
		JLabel lblSexe = new JLabel("Sexe");
		lblSexe.setBounds(12, 93, 70, 15);
		contentPane.add(lblSexe);
		
		JLabel lblAdresse = new JLabel("Adresse");
		lblAdresse.setBounds(12, 120, 70, 15);
		contentPane.add(lblAdresse);
		
		JLabel lblNumroDeCarte = new JLabel("Numéro de carte bancaire");
		lblNumroDeCarte.setBounds(12, 184, 192, 15);
		contentPane.add(lblNumroDeCarte);
		
		JLabel lblCodeSecret = new JLabel("Code secret");
		lblCodeSecret.setBounds(12, 211, 108, 15);
		contentPane.add(lblCodeSecret);
		
		textFieldNom = new JTextField();
		textFieldNom.setBounds(59, 10, 293, 19);
		contentPane.add(textFieldNom);
		textFieldNom.setColumns(10);
		
		textFieldPrenom = new JTextField();
		textFieldPrenom.setBounds(79, 39, 286, 19);
		contentPane.add(textFieldPrenom);
		textFieldPrenom.setColumns(10);
		
		textFieldJourNaissance = new JTextField();
		textFieldJourNaissance.setBounds(158, 64, 32, 19);
		contentPane.add(textFieldJourNaissance);
		textFieldJourNaissance.setColumns(10);
		
		JLabel label = new JLabel("/");
		label.setBounds(195, 66, 17, 15);
		contentPane.add(label);
		
		textFieldMoisNaissance = new JTextField();
		textFieldMoisNaissance.setColumns(10);
		textFieldMoisNaissance.setBounds(213, 64, 32, 19);
		contentPane.add(textFieldMoisNaissance);
		
		JLabel label_1 = new JLabel("/");
		label_1.setBounds(257, 66, 17, 15);
		contentPane.add(label_1);
		
		textFieldAnneeNaissance = new JTextField();
		textFieldAnneeNaissance.setColumns(10);
		textFieldAnneeNaissance.setBounds(275, 66, 57, 19);
		contentPane.add(textFieldAnneeNaissance);
		
		JLabel lblJjmmaaaa = new JLabel("(JJ/MM/AAAA)");
		lblJjmmaaaa.setBounds(338, 66, 96, 15);
		contentPane.add(lblJjmmaaaa);
		
		final JRadioButton rdbtnFminin = new JRadioButton("Féminin");
		buttonGroup.add(rdbtnFminin);
		rdbtnFminin.setBounds(79, 89, 149, 23);
		contentPane.add(rdbtnFminin);
		
		final JRadioButton rdbtnMasculin = new JRadioButton("Masculin");
		buttonGroup.add(rdbtnMasculin);
		rdbtnMasculin.setBounds(250, 89, 149, 23);
		contentPane.add(rdbtnMasculin);
		
		textFieldAdresse = new JTextField();
		textFieldAdresse.setBounds(90, 120, 293, 52);
		contentPane.add(textFieldAdresse);
		textFieldAdresse.setColumns(10);
		
		textFieldNumeroCarte = new JTextField();
		textFieldNumeroCarte.setBounds(213, 182, 221, 19);
		contentPane.add(textFieldNumeroCarte);
		textFieldNumeroCarte.setColumns(10);
		
		textFieldCodeSecret = new JTextField();
		textFieldCodeSecret.setBounds(114, 211, 117, 19);
		contentPane.add(textFieldCodeSecret);
		textFieldCodeSecret.setColumns(10);
		
		JLabel lbluniquementDesChiffres = new JLabel("(Uniquement des chiffres)");
		lbluniquementDesChiffres.setBounds(236, 211, 198, 15);
		contentPane.add(lbluniquementDesChiffres);
		
		final JLabel lblAttentionChampsInvalides = new JLabel("Attention, champ(s) invalide(s)");
		lblAttentionChampsInvalides.setForeground(Color.RED);
		lblAttentionChampsInvalides.setBounds(12, 246, 274, 15);
		contentPane.add(lblAttentionChampsInvalides);
		lblAttentionChampsInvalides.setVisible(false);
				
		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((! rdbtnFminin.isSelected()) & (! rdbtnMasculin.isSelected())){
					lblAttentionChampsInvalides.setVisible(true);
				}
				else {
					lblAttentionChampsInvalides.setVisible(false);
					if ((textFieldNom.getText().equals("")) | (textFieldPrenom.getText().equals("")) | (textFieldJourNaissance.getText().equals("")) | (textFieldMoisNaissance.getText().equals("")) | (textFieldAnneeNaissance.getText().equals("")) | (textFieldAdresse.getText().equals("")) | (textFieldNumeroCarte.getText().equals("")) | (textFieldCodeSecret.getText().equals(""))){
						lblAttentionChampsInvalides.setVisible(true);
					}
					else{
						lblAttentionChampsInvalides.setVisible(false);
					}
				}
				if(! lblAttentionChampsInvalides.isVisible()){
					String nom = textFieldNom.getText();
					String prenom = textFieldPrenom.getText();
					String dateNaissance = textFieldJourNaissance.getText() + "/" + textFieldMoisNaissance.getText() + "/" + textFieldAnneeNaissance.getText();
					String sexe;
					if(rdbtnFminin.isSelected()){
						sexe = "feminin";
					}
					else {
						sexe = "masculin";
					}
					String adresse = textFieldAdresse.getText();
					String numCB = textFieldNumeroCarte.getText();
					int codeSecret = Integer.parseInt(textFieldCodeSecret.getText());
					dispose();
					AbonnementRealisationFrame.main(conn, nom, prenom, dateNaissance, sexe, adresse, numCB, codeSecret);
				}
			}
		});
		btnValider.setBounds(317, 236, 117, 25);
		contentPane.add(btnValider);
		
		
	}
}
