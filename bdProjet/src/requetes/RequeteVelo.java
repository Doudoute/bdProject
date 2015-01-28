package requetes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import mapping.*;


public class RequeteVelo {
	
	
	/*
	 * REQUETES SUR LES VELOS
	 */

	/**
	 * Ajout d'un Velo dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numPuceRFID numero de la puce du vélo
	 * @param modele -> modele(pliant, VTT, electrique, route)
	 * @param dateMES -> Date de mise en service du vélo, format DD/MM/YYYY
	 * @param etatVelo -> string de l'etat du vélo ( enstation, enPanne, embarque)
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterVelo(Connection conn, int numPuceRFID, String modele, String dateMES, String etatVelo) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO Velo " 
	    		  + "VALUES (" + numPuceRFID + ", '"+ modele +"', to_date('" + dateMES + "','dd/mm/yyyy'), '" + etatVelo + "')");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	/**
	 * Suppression d'un Vélo dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numPuceRFID le numero de la puce correspondant au vélo a supprimer
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void supprimerVelo(Connection conn, int numPuceRFID) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("DELETE FROM Velo " 
	    		  + "WHERE numpucerfid = " + numPuceRFID);

	      // Close the result set, statement and the connection
	      stmt.close() ;
	}
	
	/**
	 * Mise a jour de l'etat d'un Vélo dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numPuceRFID le numero de la puce correspondant au vélo a mettre a jour
	 * @param etat le nouvel etat du vélo a mettre a jour
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void miseAJourVelo(Connection conn, int numPuceRFID, String etat) throws SQLException {

	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("UPDATE Velo " 
	    		  + "SET etatvelo = '" + etat + "' "
	    		  + "WHERE numPuceRFID = " + numPuceRFID);

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	public static String getEtat(Connection conn, int numpucerfid) throws SQLException{
		
		// Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      ResultSet num = stmt.executeQuery("SELECT EtatVelo " 
	    		  + "FROM Velo "
	    		  + "WHERE numPuceRFID = " + numpucerfid);
	      num.next();
	      String resultat = num.getString(1);
	      // Close the result set, statement and the connection
	      stmt.close() ;
	     
	      return resultat;
	}
	
	public static void afficherLesVelos(Connection conn) throws SQLException {

		  // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;

	      // Execute the query
	      
	      
	      ResultSet num = stmt.executeQuery("SELECT count(numPuceRFID) FROM Velo WHERE etatvelo = 'enStation'") ;
	      num.next();
	      System.out.println("Il y a actuellement "+ num.getString(1) +" Vélos en station" ) ;
	      
	      ResultSet rs = stmt.executeQuery("SELECT numPuceRFID, etatvelo FROM Velo WHERE etatvelo = 'enStation'") ;
	      System.out.println("Liste des vélos actuellement en station avec leur numero : " ) ;
	      // Loop through the result set
	      while( rs.next() ) {
	         System.out.print(rs.getString("numPuceRFID")) ;
	         System.out.print(" ; "); 
	         System.out.println(rs.getString("etatVelo"));
	      }

	      System.out.println();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
		
	}
	

	//renvoie une liste de vélos disponibles dans la station donnée
	public static ArrayList<Velo> getVelosDispo(Connection conn, String nomStation) throws SQLException {
		ArrayList<Velo> result= new ArrayList<Velo>();
		  // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT count(numPuceRFID) FROM Velo v, Bornette b, Stock s"
		      		+ "  WHERE v.numVelo = s.numVelo"
		      		+ "AND s.numBornette = b.numBornette"
		      		+ "AND b.adresse  like '?%'"
		    		+ "AND v.etatvelo = 'enStation'") ;
	      stmt.setString(1, nomStation);

	      // Execute the query
	      ResultSet rs = stmt.executeQuery();
	      while( rs.next() ) {
	    	  result.add(new Velo(rs.getInt("numPuceRFID"), rs.getString("etatVelo")));
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	
	//renvoie le numéro de la boprne associée à un numéro
	public static int retrieveNumBornetteByNumVelo (Connection conn, int numPuceRFID) throws SQLException {
		  int result = -1;
		  // Get a statement from the connection
		  PreparedStatement stmt = conn.prepareStatement("SELECT numBornette FROM Stocke WHERE numVelo=?");
	      stmt.setInt(1, numPuceRFID);


	      // Execute the query
	      ResultSet rs = stmt.executeQuery(); ;
	      if( rs.next() ) {
	    	 result = rs.getInt("numBornette");
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	
	/*
	 * REQUETES SUR LES LOCATIONS
	 */
	
	/**
	 * Mise a jour de l'etat d'un Vélo dans la base de donnees
	 * 
	 * @param conn connexion a la base de donnees
	 * @param numPuceRFID le numero de la puce correspondant au vélo a mettre a jour
	 * @param etat le nouvel etat du vélo a mettre a jour
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void creerLocation(Connection conn, int numPuceRFID, int numClient) throws SQLException {

		Timestamp date = new Timestamp(System.currentTimeMillis());
		
	      // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("INSERT INTO Loue VALUES(?,?,?,?,?)");
	      stmt.setInt(1, numPuceRFID);
	      stmt.setInt(2,numClient);
	      stmt.setTimestamp(3, date);
	      stmt.setNull(4, Types.NULL); // pas d'amende à la base
	      stmt.setNull(5, Types.NULL); // pas de date de fin de rendu à la base
	      
	      // Execute the query
	      stmt.execute();
	      
	      // mettreà  jour l'état du vélo à "loue"
	  	  miseAJourVelo(conn, numPuceRFID, "loue");


	      // Close the result set, statement and the connection
	      stmt.close() ;
	}

}
