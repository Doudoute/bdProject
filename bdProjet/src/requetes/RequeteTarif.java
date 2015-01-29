package requetes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequeteTarif {
	public static String anneeCourante(){
		  Date actuelle = new Date();
		  DateFormat dateFormat = new SimpleDateFormat("yyyy");
		  String dateCourante = dateFormat.format(actuelle);
		  System.out.println(dateCourante);
		  return dateCourante;
	}
	
	public static int trouverTarifAnnee(Connection conn, String anneeCourante) throws SQLException {
		
		int tarif;
		
		// Get a statement from the connection
		Statement stmt = conn.createStatement();
		  
		// Execute the query
		ResultSet montantAboAnnee = stmt.executeQuery("SELECT montantLocation FROM Tarif where to_char(annee, 'yyyy') = " + anneeCourante);
		montantAboAnnee.next();
		  
		tarif = Integer.parseInt(montantAboAnnee.getString(1));
		
		  
		// Close the result set, statement and the connection
	    stmt.close();
	    
	    return tarif;
	}
}
