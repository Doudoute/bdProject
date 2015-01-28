package bdProjet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequeteVehiculeRegulation {
	/**
	 * Ajout d'un Vehicule dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numVehicule numero de la puce du vélo
	 * @param modele -> modele(pliant, VTT, electrique, route)
	 * @param dateMES -> Date de mise en service du vélo, format DD/MM/YYYY
	 * @param numRoutine -> Le numero de la routine suivi par le vehicule
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterVehicule(Connection conn, int numVehicule, String modele, int numRoutine, String dateMES) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO VehiculeRegulation " 
	    		  + "VALUES (" + numVehicule + ", '"+ modele +", '"+ numRoutine +"', to_date('" + dateMES + "','dd/mm/yyyy'), '");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	/**
	 * Suppression d'un Vehicule dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numVehicule le numero de la puce correspondant au Véhicule a supprimer
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void supprimerVehicule(Connection conn, int numVehicule) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("DELETE FROM VehiculeRegulation " 
	    		  + "WHERE numVehicule = " + numVehicule);

	      // Close the result set, statement and the connection
	      stmt.close() ;
	}
		
	public static void afficherLesRoutine(Connection conn, int numVehicule) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;

	      // Execute the query	      
	      ResultSet rs = stmt.executeQuery( " Select O.rang ,T.description,O.etatOrdre"+
	    		  							" From Tache T, Ordre O, VehiculeRegulation V"+
	    		  							" Where V.numVehicule = "+numVehicule+
	    		  							" AND V.numRoutine = O.numRoutine"+
	    		  							" AND O.numTache = T.numTache ORDER BY O.rang") ;
	      System.out.println("Liste des tâches associé au véhicule avec leurs états : " ) ;
	      // Loop through the result set
	      while( rs.next() ) {
	         System.out.print(rs.getString("RANG")) ;
	         System.out.print(" ; "); 
	         System.out.println(rs.getString("DESCRIPTION"));
	         System.out.print(" ; "); 
	         System.out.println(rs.getString("ETATORDRE"));
	      }

	      System.out.println();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	}
	
	public static String[][] getRoutine(Connection conn, int numVehicule) throws SQLException{
		// Get a statement from the connection
	      Statement stmt = conn.createStatement() ;
	    // Execute the query
	    ResultSet rs = stmt.executeQuery( " Select O.rang ,T.description,O.etatOrdre"+
					" From Tache T, Ordre O, VehiculeRegulation V"+
					" Where V.numVehicule = "+numVehicule+
					" AND V.numRoutine = O.numRoutine"+
					" AND O.numTache = T.numTache ORDER BY O.rang");
	    
	    String[][] resultat = new String[20][20];
	    int i = 0;
	    int j = 0;
	    while( rs.next() ) {
	        resultat[i][j] = rs.getString("description");
	        j++;
	        resultat[i][j] = rs.getString("etatOrdre");
	        i++;
	      }
		return resultat;
	}
}
