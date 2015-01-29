package requetes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class RequeteStation {

	
	/**
	 * Ajout d'une station dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param adresse adresse d'une station a ajouter
	 * @param categorie categorie (Vplus, Vnul ou Vmoins) de la station a ajouter
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterStation(Connection conn, String adresse, String categorie) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO Station " 
	    		  + "VALUES ('" + adresse +"', '" + categorie + "')");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	/**
	 * Suppression d'une station dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param adresse adresse de la station a supprimer
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void supprimerStation(Connection conn, String adresse) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("DELETE FROM Station " 
	    		  + "WHERE adresse = '" + adresse + "'");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
	/**
	 * Mise a jour d'une station dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param adresse l'adresse de la station a modifier
	 * @param categorie la nouvelle categorie de la station a mettre a jour
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void miseAJourStation(Connection conn, String adresse, String categorie) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("UPDATE Station " 
	    		  + "SET categorie = '" + categorie + "' "
	    		  + "WHERE adresse = '" + adresse + "'");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	
	/**
	 * Affichage des stations Vplus
	 * 
	 * @param conn connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficherStationsVplus(Connection conn) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;

	      // Execute the query
	      ResultSet rs = stmt.executeQuery("SELECT adresse FROM Station WHERE categorie = 'Vplus'") ;

	      System.out.println("Liste des stations Vplus : " ) ;
	      // Loop through the result set
	      while( rs.next() ) {
	         System.out.println(rs.getString("adresse")) ;
	      }
	      
	      System.out.println();

	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
		
	}
	
	
	/**
	 * Affichage des stations Vmoins
	 * 
	 * @param conn connexion a la base de donnees
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void afficherStationsVmoins(Connection conn) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;

	      // Execute the query
	      ResultSet rs = stmt.executeQuery("SELECT adresse FROM Station WHERE categorie = 'Vmoins'") ;

	      System.out.println("Liste des stations Vmoins : " ) ;
	      // Loop through the result set
	      while( rs.next() ) {
	         System.out.println(rs.getString("adresse")) ;
	      }

	      System.out.println();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
		
	}
	
	public static ArrayList<String> getAllStationAdresses(Connection conn) throws SQLException{

		ArrayList<String> result = new ArrayList<String>();
	      // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT adresse FROM Station") ;

	      // Execute the query
	      ResultSet rs = stmt.executeQuery() ;

	      // Loop through the result set
	      while( rs.next() ) {
		         result.add(rs.getString("adresse")) ;
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	
	public static ArrayList<Integer> getAllBornetteLibre(Connection conn) throws SQLException{

		ArrayList<Integer> result = new ArrayList<Integer>();
	      // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT numbornette FROM Station natural join Bornette WHERE numbornette NOT IN (SELECT numbornette from STOCKE)") ;

	      // Execute the query
	      ResultSet rs = stmt.executeQuery() ;

	      // Loop through the result set
	      while( rs.next() ) {
		         result.add(rs.getInt("numbornette")) ;
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	
	
}
