/*      ********* Arduino projekt - Projekt putnog racunala *********

 **************************************************************************************
 * Ovaj softver je dozvoljeno koristiti i mijenjati bez trazenja dozvole autora, samo *
 *           ako se koristi ovo zaglavlje i u njega dodaju autori izmjena             *  
 **************************************************************************************


 Autori projekta: Dimitrij Vukelic, Sanjin Mavrinac, Zoran Lukic, Barbara Lesko, Natalija Bomestar.

 Autor Arduino i Java softvera : Dimitrij Vukelic , e-mail : dvukeli1@veleri.hr ,  mitja.vukelic@gmail.com 

 Mentor: Marino Franusic
 Veleuciliste u Rijeci
 Strucni studij Telematike, II. godina, izvanredni
 Rijeka 2012.

 ---------------------------------------------------------------------------------------------------------

 *  Controller za spajanje na bazu podataka i upis senzora u bazu, nije napravljeno citanje iz baze 
 *  sto moze biti prosirenje, tako da se postavi pin(koji je predvidjen) pri paljenju programa
 *  i on sluzi za identifikaciju vozaca u vozilu.
 *  Za to je potrebno dodati ispis iz baze podataka o vozacu, tj. njegovu identifikaciju kao i identifikaciju
 *  vozila iz baze podataka. Arduino sada kao identifikator vozila salje 1 kao ID vozila u bazi, ako se taj
 *  identifikator promjeni, promjenit ce se i vozilo za koje se vrsi evidencija.
 *  Pin vozaca je sada 1111 i on ga identificira imenom i prezimenom u bazi, promjenom pina u modelu vozaci, promjeniti 
 *  ce se i ime vozaca koji koristi vozilo
 */

package controllers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseController {

	private String url = "jdbc:mysql://DB.URL.path";
	private String userName = "DB_username"; 
	private String password = "DB_password";


	Connection conn;
	Statement stmt;
	//ResultSet rs;

	// metoda za upisivanje u bazu
	public int insertPodatci(double senzor1,double senzor2,double senzor3,double senzor4,double senzor5,double senzor6,int vozilo, int vozac){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e1) {
			System.out.println("Greska!!");
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			conn = DriverManager.getConnection(url,userName,password);
		} catch (SQLException e1) {
			System.out.println("GRESKA U SQL-u");
		}



		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {

			stmt.executeUpdate("INSERT INTO PTK.Podatci (ID_Voz,ID_Vozila,senzor1,senzor2,senzor3,senzor4,senzor5,senzor6) " +
					"VALUES ("+vozac+","+vozilo+","+senzor1+","+senzor2+","+senzor3+","+senzor4+","+senzor5+","+senzor6+")");


			conn.close();
			//rs.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("GRESKA U SQL-u");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}


} 