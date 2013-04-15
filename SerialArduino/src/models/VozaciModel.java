
/*     ********* Arduino projekt - Projekt putnog racunala *********
      
       **************************************************************************************
       * Ovaj softver je dozvoljeno koristiti i mijenjati bez trazenja dozvole autora, samo *
       *           ako se koristi ovo zaglavlje i u njega dodaju autori izmjena             *  
       **************************************************************************************
       
       
 Autori : Dimitrij Vukelic, Sanjin Mavrinac, Zoran Lukic, Barbara Lesko, Natalija Bomestar.
 
 Autor Arduino i Java softvera : Dimitrij Vukelic , e-mail : dvukeli1@veleri.hr ,  mitja.vukelic@gmail.com 
 
 Mentor: Marino Franusic
 Veleuciliste u Rijeci
 Strucni studij Telematike, II. godina, izvanredni
 Rijeka 2012.
 
 ---------------------------------------------------------------------------------------------------------
 
 * Model vozaci sadrzi podatke kao jake tablice u bazi + vrijeme koje salje kada i podatke.
 */

package models;

public class VozaciModel {

	private String ime_prezime="Marino Franusic";
	private String pin="2222";
	private int ID_Vozaca = 5;
	private String vozilo;
	private int id_vozilo=2;
	
	
	public String getIme_prezime() {
		return ime_prezime;
	}
	public void setIme_prezime(String ime_prezime) {
		this.ime_prezime = ime_prezime;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getVozilo() {
		return vozilo;
	}
	public void setVozilo(String vozilo) {
		this.vozilo = vozilo;
	}
	public int getId_vozilo() {
		return id_vozilo;
	}
	public void setId_vozilo(int id_vozilo) {
		this.id_vozilo = id_vozilo;
	}
	public int getID_Vozaca() {
		return ID_Vozaca;
	}
	public void setID_Vozaca(int iD_Vozaca) {
		ID_Vozaca = iD_Vozaca;
	}
}
