package bdProjet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class RequeteClient {

	/**
	 * Ajout d'un nouveau client dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numClient le numero de client du client a ajouter
	 * @param numCB le numero de CB du client a ajouter
	 * @param codeSecret le code secret du client a ajouter
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterClient(Connection conn, int numClient, String numCB, int codeSecret) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO Client " 
	    		  + "VALUES (" + numClient + ", '" + numCB + "', " + codeSecret + ")");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
}
