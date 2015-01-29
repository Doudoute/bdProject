package requetes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequeteEmbarque {

	public static void setEmbarquement(Connection conn, String numVelo, String numVehicule, String adresseDep ) throws SQLException{
		 
		Date actuelle = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String dateCourante = dateFormat.format(actuelle);
		
		  // Get a statement from the connection
	      Statement stmt = conn.createStatement();    
	      
	      
	      stmt.executeUpdate("INSERT INTO Embarque VALUES ( "
								+numVelo+ ", "
								+"TO_DATE('"+dateCourante+"','dd/MM/yyyy'), "
								+ numVehicule +",'"
								+ adresseDep+"', 'null')");
	      
	      stmt.executeUpdate("DELETE FROM Stocke " 
	    		  + "WHERE numVelo = " + numVelo);
	      
	      stmt.close();
	    	    
	}	
}
