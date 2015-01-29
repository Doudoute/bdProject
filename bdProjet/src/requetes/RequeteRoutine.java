package requetes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RequeteRoutine {

	public static void setEtatRoutine(Connection conn, String description, String etatOrdre) throws SQLException{
		 // Get a statement from the connection
	      Statement stmt = conn.createStatement();    
	      
	      
	      ResultSet rs = stmt.executeQuery("select O.numTache from ordre O, tache T where T.numTache=O.numtache and T.description like '"+description+"%'");
	      rs.next();
	      
	      String numTache = rs.getString(1);
	      
	      // Execute the query
	      stmt.executeUpdate( " UPDATE Ordre "+
					" SET etatOrdre ='"+ etatOrdre +"'"+
					" Where numTache = '"+numTache+"'");
	      
	      stmt.close();
	    	    
	}	
}
