package requetes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequeteAbonne {

	/**
	 * Ajouter un nouvel abonne
	 * @param conn la connexion a la base de donnees
	 * @param numAbonne le numero du client qui devient abonne
	 * @param nom le nom du nouvel abonne
	 * @param prenom le prenom du nouvel abonne
	 * @param dateNaissance la date de naissance du nouvel abonne
	 * @param sexe le sexe du nouvel abonne
	 * @param adresse l'adresse du nouvel abonne
	 * @throws SQLException en cas d'erreur d'acces a la base de donnees
	 */
	public static void ajouterAbonne(Connection conn, int numAbonne, String nom, String prenom, String dateNaissance, String sexe, String adresse) throws SQLException {

		  String dateCourante = dateCourante();
		  
	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("INSERT INTO Abonne " 
	    		  + "VALUES (" + numAbonne + ", '" + nom + 
	    		  "', '" + prenom + "', TO_DATE('" + dateNaissance + 
	    		  "', 'dd/mm/yyyy'), '" + sexe + 
	    		  "', '" + adresse + "', TO_DATE('" + dateCourante +
	    		  "', 'dd/mm/yyyy'), 'enCours' )");

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	public static String dateCourante(){
		  Date actuelle = new Date();
		  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		  String dateCourante = dateFormat.format(actuelle);
		  return dateCourante;
	}
	
	
	public static void reabonnement(Connection conn, int numAbonne) throws SQLException {

		  Date actuelle = new Date();
		  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		  String dateCourante = dateFormat.format(actuelle);
		
	      // Get a statement from the connection
	      Statement stmt = conn.createStatement();

	      // Execute the query
	      stmt.executeUpdate("UPDATE Abonne " 
	    		  + "SET etatAbo = 'enCours' , " + 
	    		  "dateAbo = TO_DATE('" + dateCourante + "', 'dd/mm/yyyy')"   
	    		  + "WHERE numAbonne = " + numAbonne);

	      // Close the result set, statement and the connection
	      stmt.close() ;
		
	}
	
	public static String ajouterUnAnDate(String date){
		
		String dateDecoupee[] = new String[3];
		dateDecoupee = date.split("/");
		dateDecoupee[2] = Integer.toString(Integer.parseInt(dateDecoupee[2])+1);
		
		return dateDecoupee[0] + "/" + dateDecoupee[1] + "/" + dateDecoupee[2];
	}
	
	public static String trouverDateFinAbo(Connection conn, int numAbonne) throws SQLException {
		String dateAbo;
		String dateFinAbo;
		
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		  
		// Execute the query
		ResultSet dateAbonnement = stmt.executeQuery("SELECT to_char(dateAbo, 'dd/mm/yyyy') FROM Abonne where numAbonne = " + numAbonne);
		dateAbonnement.next();
		  
		dateAbo = dateAbonnement.getString(1);
		
		dateFinAbo = ajouterUnAnDate(dateAbo);
		  
		// Close the result set, statement and the connection
	    stmt.close();
	    
	    return dateFinAbo;
	}

	public static String etatAbonnement(Connection conn, int numAbonne) throws SQLException {
		
		  // Get a statement from the connection
		  Statement stmt = conn.createStatement();
		  
		  // Execute the query
		  ResultSet etat = stmt.executeQuery("SELECT etatAbo FROM Abonne WHERE numAbonne = " + numAbonne);
		  etat.next();
		  
		  String etatAbo = etat.getString(1);
		  
		  return etatAbo;
	}
	
}
