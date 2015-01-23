package bdProjet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Test {

	private static final String configurationFile = "BD.properties";
	 

	 public static void main( String args[] ) {
	  try {
		  String jdbcDriver, dbUrl;
		  
		  DatabaseAccessProperties dap = new DatabaseAccessProperties(configurationFile);
		  jdbcDriver	= dap.getJdbcDriver();
		  dbUrl			= dap.getDatabaseUrl();
//		  username		= dap.getUsername();
//		  password		= dap.getPassword();
		  
	      // Load the database driver
	      Class.forName(jdbcDriver) ;

	      // Get a connection to the database
	      Connection conn = DriverManager.getConnection(dbUrl, "meunierk", "bd2015");

		  // Print information about connection warnings
		  SQLWarningsExceptions.printWarnings(conn);

		  
		  /********************************************* STATION *********************************************/
		  // Ajouter une station
//		  RequeteStation.ajouterStation(conn, "adresseTest", "Vplus");
		  
		  // Supprimer une station
//		  RequeteStation.supprimerStation(conn, "adresseTest");
		  
		  // Mettre a jour une station
//		  RequeteStation.miseAJourStation(conn, "Rue Neuve-des-Capucins - 44000 Nantes", "Vnul");
		  
		  // Afficher les stations VPlus
//		  RequeteStation.afficherStationsVplus(conn);
		  
		  // Afficher les stations Vmoins
//		  RequeteStation.afficherStationsVmoins(conn);
		  
		  /***************************************************************************************************/
		  
		  
		  /********************************************* BORNETTE *********************************************/
		  // Ajouter une bornette
//		  RequeteBornette.ajouterBornette(conn, 76, "OK", "Rue Neuve-des-Capucins - 44000 Nantes");
		 
		  // Supprimer une bornette
//		  RequeteBornette.supprimerBornette(conn, 76);
		  
		  // Mise a jour de l'etat d'une bornette
//		  RequeteBornette.miseAJourBornette(conn, 76, "HS");
		  
		  // Affichage des bornettes se trouvant dans l'etat OK
//		  RequeteBornette.afficherLesBornettesOK(conn);
		  /***************************************************************************************************/
		  
		  
		  /********************************************* CLIENT *********************************************/
		  // Ajouter un nouveau client
//		  RequeteClient.ajouterClient(conn, 97, "1234567891234567", 1587);
		  /***************************************************************************************************/

		  
	      conn.close() ;
	  }
	  catch( SQLException se ) {
		  
		  // Print information about SQL exceptions
		  SQLWarningsExceptions.printExceptions(se);
		  
	      return;
	  }
	  catch( Exception e ) {
	      System.err.println( "Exception: " + e.getMessage()) ;
	      e.printStackTrace();
	      return;
	  }
	  }
	
}
