package ihm;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import requetes.RequeteAbonne;
import javax.swing.JTextArea;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;

public class ReAbonnementFrame extends JFrame {

	private JPanel contentPane;
	private JButton btnValider;
	private JButton btnRetour;
	private final Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReAbonnementFrame frame = new ReAbonnementFrame(conn);
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
	public ReAbonnementFrame(final Connection conn) {
		setResizable(false);
		setTitle("Re-abonnement");
		this.conn = conn;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblnumClient = new JLabel("Veuillez indiquer votre numero de client");
		lblnumClient.setBounds(12, 43, 310, 52);
		contentPane.add(lblnumClient);
		
		final JTextArea textAreaNumClient = new JTextArea();
		textAreaNumClient.setBounds(111, 107, 200, 15);
		contentPane.add(textAreaNumClient);
		
		btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numClient = Integer.parseInt(textAreaNumClient.getText());
				try {
					String etatAbo = RequeteAbonne.etatAbonnement(conn, numClient);
					
					if (etatAbo.equals("enCours")){
							dispose();
							ReAbonnementInterditFrame.main(conn, numClient);
					}
					else{
						dispose();
						ReAbonnementAutoriseFrame.main(conn, numClient);
					}
					
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnValider.setBounds(50, 222, 117, 25);
		contentPane.add(btnValider);
		
		btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				AbonnementReabonnementFrame.main(null);
			}
		});
		btnRetour.setBounds(273, 222, 117, 25);
		contentPane.add(btnRetour);
		
		
		
	}
}
