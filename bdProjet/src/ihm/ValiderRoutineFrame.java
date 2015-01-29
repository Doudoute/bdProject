package ihm;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import requetes.RequeteRoutine;
import requetes.RequeteVehiculeRegulation;

public class ValiderRoutineFrame extends JFrame {

	private JPanel contentPane;
	private Connection conn;
	private JTable table;
	/**
	 * Launch the application.
	 */
	public static void main(final Connection conn, final String log) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ValiderRoutineFrame frame = new ValiderRoutineFrame(conn, log);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 *//** Map qui associe au indexes de colonnes, dont on souhaite définir la largeur, la valeur de la largeur. */
	private final Map<Integer, Integer> largeursColonnes = new HashMap<Integer, Integer>();
	
	private String[][] resultatRequete = {};
	
	public ValiderRoutineFrame(final Connection conn, final String log) {
		setTitle("Valider Routine");
		this.conn=conn;

		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblVhicule = new JLabel("Véhicule :");
		lblVhicule.setBounds(97, 12, 70, 15);
		contentPane.add(lblVhicule);
		
		JLabel lblXx = new JLabel(log);
		lblXx.setBounds(179, 12, 70, 15);
		contentPane.add(lblXx);
		
		JLabel lblListeDesRoutines = new JLabel("Liste des routines :");
		lblListeDesRoutines.setBounds(97, 54, 152, 15);
		contentPane.add(lblListeDesRoutines);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 78, 500, 200);
		contentPane.add(scrollPane_1);
		
		
		try {
			resultatRequete = RequeteVehiculeRegulation.getRoutine(conn, Integer.parseInt(log));
		
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] titreColonnes = {"Etape","Etat"};
				
		largeursColonnes.put(1,100);
		//largeursColonnes.put(2, 5);
				
		table = new JTable(resultatRequete,titreColonnes);
		table.setBounds(10, 70, 500, 200);
		// Désactivation de la création automatique des colonnes à partir du modèle. A appeler avant d'affecter le modèle à la table.
		table.setAutoCreateColumnsFromModel(false);
		ajusterLargeursColonnes(table);
		scrollPane_1.setViewportView(table);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				ConducteurFrame.main(conn, log);
			}
		});		
		btnRetour.setBounds(460, 330, 117, 25);
		contentPane.add(btnRetour);
		
		JButton btnValider = new JButton("Valider");
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RequeteRoutine.setEtatRoutine(conn, resultatRequete[table.getSelectedRow()][table.getSelectedColumn()], "valide");
					myRepaint();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});		
		btnValider.setBounds(10, 330, 117, 25);
		contentPane.add(btnValider);
		
		JButton btnNotifier = new JButton("Notifier");
		btnNotifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RequeteRoutine.setEtatRoutine(conn, resultatRequete[table.getSelectedRow()][table.getSelectedColumn()], "annule");
					myRepaint();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});		
		btnNotifier.setBounds(150, 330, 117, 25);
		contentPane.add(btnNotifier);
		
		setResizable(false);
		
		
	}
	
	private void myRepaint(){
		this.repaint();
	}
	
	private void ajusterLargeursColonnes(final JTable jTable) {
		// Largeur disponible pour les colonnes pour lesquelles on ne désire pas affecter de largeur
		int largeurDispo = jTable.getWidth();
		for (final Integer largeur : largeursColonnes.values()) {
		  if (largeur != null) {
		    largeurDispo = largeurDispo - largeur;
		  }
		}
		 
		if (largeurDispo < 0) {
		  largeurDispo = 0;
		}
		 
		final TableModel model = jTable.getModel();
		final int nbColonnesAAjuster = model.getColumnCount() - largeursColonnes.size();
		 
		final int largeurParDefaut;
		int resteLargeurDispo;
		if (nbColonnesAAjuster != 0) {
		  largeurParDefaut = largeurDispo / nbColonnesAAjuster;
		  // Reste à répartir
		  resteLargeurDispo = largeurDispo % nbColonnesAAjuster;
		} else {
		  largeurParDefaut = 0;
		  resteLargeurDispo = 0;
		}
		 
		// On anticipe le redimensionnement automatique en repartissant les largeurs
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		final TableColumnModel columns = table.getColumnModel();
		for (int count = model.getColumnCount(), i = 0; i < count; i++) {
		  final TableColumn column = columns.getColumn(i);
		  Integer largeurColonne = largeursColonnes.get(i);
		  if (largeurColonne == null) {
		    // La colonne n'a pas de largeur spécifique
		    largeurColonne = largeurParDefaut;
		    if (resteLargeurDispo != 0) {
		      // On répartie une partie du reste de la largeur disponible
		      largeurColonne = largeurColonne + 1;
		      resteLargeurDispo = resteLargeurDispo - 1;
		    }
		  }
		 
		  if (largeurColonne < column.getMinWidth()) {
		  column.setMinWidth(largeurColonne);
		}
		 
		column.setPreferredWidth(largeurColonne);
		}
		 
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
	}
}
