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
