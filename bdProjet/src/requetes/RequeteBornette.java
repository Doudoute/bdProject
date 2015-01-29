package requetes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class RequeteBornette {
	
	/**
	 * Ajout d'une bornette dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param adresse adresse d'une station a ajouter
	 * @param categorie categorie (Vplus, Vnul ou Vmoins) de la station a ajouter
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterBornette(Connection conn, int numBornette, String etat, String adresse) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO Bornette " 
	    		  + "VALUES (" + numBornette + ", '"+ etat +"', '" + adresse + "')");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
	/**
	 * Suppression d'une bornette dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numBornette le numero de la bornette a supprimer
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void supprimerBornette(Connection conn, int numBornette) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("DELETE FROM Bornette " 
	    		  + "WHERE numBornette = " + numBornette);

	      // Close the result set, statement and the connection
	      stmt.close() ;
	}
	
	
	/**
	 * Mise a jour de l'etat d'une bornette dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numBornette le numero de la bornette a mettre a jour
	 * @param etat le nouvel etat de la bornette a mettre a jour
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void miseAJourBornette(Connection conn, int numBornette, String etat) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("UPDATE Bornette " 
	    		  + "SET etat = '" + etat + "' "
	    		  + "WHERE numBornette = " + numBornette);

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
	public static void afficherLesBornettesOK(Connection conn) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;

	      // Execute the query
	      ResultSet rs = stmt.executeQuery("SELECT numBornette, adresse FROM Bornette WHERE etat = 'OK'") ;

	      System.out.println("Liste des bornettes OK avec leur numero et adresse : " ) ;
	      // Loop through the result set
	      while( rs.next() ) {
	         System.out.print(rs.getString("numBornette")) ;
	         System.out.print(" ; "); 
	         System.out.println(rs.getString("adresse"));
	      }

	      System.out.println();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
		
	}
	
	public static String[] getAdresseBornes(Connection conn) throws SQLException {
		// Get a statement from the connection
	      Statement stmt = conn.createStatement() ;
	    // Execute the query
	    ResultSet rs = stmt.executeQuery("SELECT distinct(adresse)FROM Bornette");
	    
	    String[] resultat = new String[10];
	    int i = 0;
	    while( rs.next() ) {
	        resultat[i] = rs.getString("adresse").split(" - ")[0];
	        i++;
	      }
	    return resultat;	    
	}
	
	public static String[] getVeloAssocieBorne(Connection conn, String adresse) throws SQLException{
		// Get a statement from the connection
	      Statement stmt = conn.createStatement() ;
	    // Execute the query
	    ResultSet rs = stmt.executeQuery("select numvelo "
	    								+"from stocke "
	    								+"where numbornette "
	    								+ "IN (select numbornette "
	    									+ "from bornette "
	    									+ "where adresse like '"+adresse+"%')");
	    
	    String[] resultat = new String[30];
	    int i = 0;
	    while( rs.next() ) {
	        resultat[i] = rs.getString("numvelo");
	        i++;
	      }
	    return resultat;
	}
		
}
