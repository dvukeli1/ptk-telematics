
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

 * Klasa definira model senzora koje Arduino salje, double se koristi zbog nacina prikaza u view. Moguce je 
 * dodavati sentore po potrebi, preporuka, ne nazivati ih senzor1, senzor2 zbog lakseg snalazenja.
 */

package models;

public class SenzoriModel {


	private double rpm=0.00;
	private double brzina=0.00;
	private double temp=0.00;
	private double stprij=0.00; 
	private double odo=0.00;
	private double tps;

	public Double getRpm() {
		return rpm;
	}
	public void setRpm(Double rpm) {
		this.rpm = rpm;
	}
	public Double getBrzina() {
		return brzina;
	}
	public void setBrzina(Double brzina) {
		this.brzina = brzina;
	}
	public Double getTemp() {
		return temp;
	}
	public void setTemp(Double temp) {
		this.temp = temp;
	}
	public Double getStprij() {
		return stprij;
	}
	public void setStprij(Double stprij) {
		this.stprij = stprij;
	}
	public Double getOdo() {
		return odo;
	}
	public void setOdo(Double odo) {
		this.odo = odo;
	}

	public double getTps() {
		return tps;
	}
	public void setTps(double tps) {
		this.tps = tps;
	}


}
