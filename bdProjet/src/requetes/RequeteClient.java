package requetes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mapping.Client;



public class RequeteClient {

	/**
	 * Ajout d'un nouveau client dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
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
	      stmt.close();
		
	}
	
	
	/**
	 * Attribution d'un numero client pour un nouveau client
	 *  
	 * @param conn la connexion a la base de donnees
	 * @return le numero de client
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static int attribuerNumClient(Connection conn) throws SQLException  {
		
		  // Get a statement from the connection
		  Statement stmt = conn.createStatement();
		  
		  // Execute the query
		  ResultSet dernierNumClientUtilise = stmt.executeQuery("SELECT MAX(numClient) FROM Client");
		  dernierNumClientUtilise.next();
		  
		  int numNouveauClient = Integer.parseInt(dernierNumClientUtilise.getString(1)) + 1;
		  
		  // Close the result set, statement and the connection
	      stmt.close();
	      return numNouveauClient;
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
	
	
	//Retrouve le client à partir du numéro de CB et du code secret
	public static Client retrieveClientByCBandSecretCode(Connection conn, int code, String numCB) throws SQLException {
		  Client result = null;
	      // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Client WHERE codeSecret = ? AND numCB=?");
	      stmt.setInt(1, code);
	      stmt.setString(2, numCB);

	      // Execute the query
	      ResultSet rs = stmt.executeQuery();
	      if( rs.next() ) {
	    	  result = new Client(rs.getInt("numClient"), rs.getInt("codeSecret"),rs.getString("numCB"));
	      }

	      // Close the result set, statement and the connection
	      stmt.close() ;
	      
	      return result;
	}
	
	
}
