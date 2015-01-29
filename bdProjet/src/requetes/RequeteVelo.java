package requetes;
import java.sql.Connection;
import java.sql.Date;
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
	      Statement stmt = conn.createStatement() ;


	      // Execute the query
	      String query = "SELECT v.numPuceRFID, v.etatVelo "
	    		  		+ "FROM Velo v, Bornette b, Stocke s "
	    		  		+ "WHERE v.numPuceRFID = s.numVelo "
	    		  		+ "AND s.numBornette = b.numBornette "
	    		  		+" AND b.adresse like '"+nomStation +"%' "
	    		  		+ "AND v.etatVelo = 'enStation' ";
	      
	      ResultSet rs = stmt.executeQuery(query);
	      while( rs.next() ) {
	    	  result.add(new Velo(rs.getInt("numPuceRFID"), rs.getString("etatVelo")));
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	
	//renvoie une liste de vélos HS dans la station donnée
	public static ArrayList<Velo> getVelosHS(Connection conn, String nomStation) throws SQLException {
		ArrayList<Velo> result= new ArrayList<Velo>();
		  // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;


	      // Execute the query
	      String query = "SELECT v.numPuceRFID, v.etatVelo "
	    		  		+ "FROM Velo v, Bornette b, Stocke s "
	    		  		+ "WHERE v.numPuceRFID = s.numVelo "
	    		  		+ "AND s.numBornette = b.numBornette "
	    		  		+" AND b.adresse like '"+nomStation +"%' "
	    		  		+ "AND v.etatVelo = 'enPanne' ";
	      
	      ResultSet rs = stmt.executeQuery(query);
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

	
	
	
	public static void retirerVelo(Connection conn, int numVelo) throws SQLException {
		
		 // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Stocke WHERE numVelo = ?");
	      stmt.setInt(1, numVelo);
	      
	      // Execute the query
	      stmt.execute();

	      // Close the result set, statement and the connection
	      stmt.close() ;
	}

	public static ArrayList<Velo> getVelosLoues(Connection conn) throws SQLException {
		ArrayList<Velo> result= new ArrayList<Velo>();
		  // Get a statement from the connection
	      Statement stmt = conn.createStatement() ;


	      // Execute the query
	      String query = "SELECT numPuceRFID, etatVelo "
	    		  		+ "FROM Velo  "
	    		  		+ "WHERE etatVelo = 'loue' ";
	      
	      ResultSet rs = stmt.executeQuery(query);
	      while( rs.next() ) {
	    	  result.add(new Velo(rs.getInt("numPuceRFID"), rs.getString("etatVelo")));
	      }
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}


	public static boolean isSecretCodeCheckedForVelo(Connection conn, int code, int numVelo) throws SQLException {
		 // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT c.codeSecret FROM Velo v, Loue l, Client c"
		      		+ " WHERE v.numPuceRFID = l.numVelo"
		      		+ " AND l.numClient = c.numClient"
		      		+ " AND v.numPuceRFID = ?"
		      		+ " AND c.codeSecret = ?"
		      		+ " AND l.dateFin IS NULL");
	      stmt.setInt(1, numVelo);
	      stmt.setInt(2, code);

	      ResultSet rs = stmt.executeQuery();
	      boolean result = rs.next();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;

		return result;
	}

	//retirer le vélo des vélos loués
	public static void removeVeloFromLocation(Connection conn, int numVelo) throws SQLException{
		 // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("DELETE FROM Loue WHERE numVelo = ?");
	      stmt.setInt(1, numVelo);

	      ResultSet rs = stmt.executeQuery();
	      
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;

	}

	// retourne le premier numéro de bornette LIBRE dans la station donnée
	public static int getNumBornetteLibre(Connection conn, String station) throws SQLException{
		int result = -1;
		// Get a statement from the connection
	      Statement stmt = conn.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT b.numBornette "
							+ " FROM Bornette b"
							+ " WHERE b.adresse like '%"+ station+"%'"
							+ " AND b.numBornette NOT IN("
							+ "SELECT b.numBornette"
							+ " FROM Bornette b, Velo v, Stocke s"
							+ " WHERE b.numBornette = s.numBornette"
							+ " AND s.numVelo = v.numPuceRFID) ");
	      	      
	      if(rs.next()){
	    	  result = rs.getInt("numBornette");
	      }
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}

	// mettre le vélo dans la station (créer une relation dans "Stocke")
	public static void putVeloInStation(Connection conn, int numVelo, int numBornette) throws SQLException{
		// Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("INSERT INTO Stocke values(?,?)");

	      stmt.setInt(1, numVelo);
	      stmt.setInt(2, numBornette);
	      
	       stmt.execute();

	      // Close the result set, statement and the connection

	      stmt.close() ;
	      
	}

	public static Timestamp getDateDebutLocation(Connection conn, int numVelo) throws SQLException{
  		  Timestamp result = null;
		  // Get a statement from the connection
	      PreparedStatement stmt = conn.prepareStatement("SELECT dateDebut"
	      												+ " FROM Loue"
	      												+ " WHERE numVelo = ?"
	      												+ " AND dateFin IS NULL");
	      stmt.setInt(1, numVelo);
	      ResultSet rs = stmt.executeQuery();
	      
	      if(rs.next()){
	    	  result = rs.getTimestamp("dateDebut");
	      }
	      // Close the result set, statement and the connection
	      rs.close() ;
	      stmt.close() ;
	      
	      return result;
	}
	

	// crééer l'amende et la lie à la location
	public static void creerAmende(Connection conn, int numVelo) throws SQLException {

	     // Get a statement from the connection
	     PreparedStatement stmt = conn.prepareStatement("SELECT MAX(numAmende) FROM Amende");
		 ResultSet rs = stmt.executeQuery();
		 
		 int numAmende = rs.getInt("numAmende");
		 if(rs.next()){
			  numAmende = rs.getInt("numAmende");
		 }
		 else{
			 numAmende = 1;
		 }
		 
		 // créer l'amende
	     stmt = conn.prepareStatement("INSERT INTO Amende Values(?,?,?)");
	     stmt.setInt(1, numAmende);
	     stmt.setDate(2, new Date(System.currentTimeMillis()));
	     stmt.setString(3, "apayer");
	     stmt.execute();
	     
	     // la lier à la location
	     stmt = conn.prepareStatement("UPDATE Loue"
	     							+ " SET numAmende = ?"
	     							+ " WHERE numVelo = ?"
	     							+ " AND dateFin IS NULL");
	     stmt.setInt(1, numAmende);
	     stmt.setInt(2, numVelo);
	     stmt.execute();

	      // Close the result set, statement and the connection
	      rs.close();
	      stmt.close() ;
	}

	public static void mettreFinLocation(Connection conn, Timestamp now, int numVelo) throws SQLException{
		  // Get a statement from the connection
	     PreparedStatement stmt = conn.prepareStatement("UPDATE Loue"
					+ " SET dateFin = ?"
					+ " WHERE numVelo = ?"
					+ " AND dateFin IS NULL");
	     stmt.setTimestamp(1, now);
	     stmt.setInt(2, numVelo);
		 stmt.execute();
		 
	      // Close the result set, statement and the connection
	      stmt.close();
	}

	public static void setVeloEnPanne(Connection conn, int numVelo) throws SQLException {
		 // Get a statement from the connection
	     PreparedStatement stmt = conn.prepareStatement("UPDATE Velo"
					+ " SET etat = 'enPanne'"
					+ " WHERE numVelo = ?");
	     stmt.setInt(1, numVelo);
		 stmt.execute();
		 
	      // Close the result set, statement and the connection
	      stmt.close();
	}

}
