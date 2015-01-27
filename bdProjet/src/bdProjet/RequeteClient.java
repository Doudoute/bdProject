package bdProjet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class RequeteClient {

	/**
	 * Ajout d'un nouveau client dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numCB le numero de CB du client a ajouter
	 * @param codeSecret le code secret du client a ajouter
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterClient(Connection conn, String numCB, int codeSecret) throws SQLException {

		  // Get a statement from the connection
		  Statement stmt = conn.createStatement();
		  // Execute the query
		  int dernierNumClientUtilise = stmt.executeUpdate("SELECT MAX(numClient) FROM Client");
		  int numNouveauClient = dernierNumClientUtilise + 1;
		
	      // Get a statement from the connection
	      Statement stmt2 = conn.createStatement();

	      // Execute the query
	      stmt2.executeUpdate("INSERT INTO Client " 
	    		  + "VALUES (" + numNouveauClient + ", '" + numCB + "', " + codeSecret + ")");

	      // Close the result set, statement and the connection
	      stmt2.close() ;
	      stmt.close();
		
	}
	
	
	/**
	 * Suppression d'un client dans la base de données
	 *  
	 * @param conn connexion a la base de donnees
	 * @param numClient le numero de client du client a supprimer
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void supprimerClient(Connection conn, int numClient) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("DELETE FROM Client " 
	    		  + "WHERE numClient = " + numClient);

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
	/**
	 * Mettre a jour le numero de CB d'un client
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numClient le numero de client dont il faut modifier le numero de CB
	 * @param numCB le nouveau numero de CB
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void miseAJourNumCB(Connection conn, int numClient, String numCB) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("UPDATE Client " 
	    		  + "SET numCB = '" + numCB + "' "
	    		  + "WHERE numClient = " + numClient);

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
}
