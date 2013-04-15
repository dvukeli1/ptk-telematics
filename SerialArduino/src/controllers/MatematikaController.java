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


 * Matematicki kontroler aplikacije, svi kompliciraniji matematicki izracuni se izvrsavaju u ovom controlleru - izracun brzine
 * i pretvaranje ocitane vrijednosti termistora u stupnjeve celzijusa
 */


package controllers;



public class MatematikaController {

	/*
	 * Po promjeni stupnja prijenosa vozila ili promjeni broja okretaja poziva se ova metoda za izracun trenutne brzine
	 * Metoda koristi slijedecu  formulu za izracun brzine:
	 *((((broj okretaja * visina kotaca / odnos mjenjaca ) / odnos mjenjaèa i radilice )/ 210( za km/h ,  336 za Mp/h))
	 * izvor  formule : http://www.ratwell.com/technical/Transaxle.html 
	 * izvor odnosa mjenjaca: http://www.boosttown.com/gearbox_differential/speed_calculator.php
	 * 
	 * izracun je naravno aproksimativan i sluzi samo kao vrijednost u projektu, 
	 * moguce prosirenje je promjena visine kotaca i odnosa mjenjaca kako bi se vidjela razlika (recimo za 
	 * testiranje razlicitih konfiguracija vozila)
	 * 
	 */

	public double brzina(double stpr,double rpm){

		double brzina=0.00;
		double rpm1=rpm*1000;
		double rezultat=0.00;



		switch ((int)stpr) {

		case 0: rezultat=0.00;

		break;

		case 1: brzina = ((((rpm1 * 22)/ 3.300) / 4.100) / 210 );

		rezultat=brzina;
		
		break;

		case 2: brzina = ((((rpm1 * 22)/ 2.250) / 4.100) / 210 );

		rezultat=brzina;
		break; 

		case 3: brzina = ((((rpm1 * 22)/ 1.750) / 4.100) / 210 );

		rezultat=brzina;
		break;

		case 4:  brzina = ((((rpm1 * 22)/ 1.00) / 4.100) / 210 );

		rezultat=brzina;
		break;

		case 5:  brzina = ((((rpm1 * 22)/ 0.850) / 4.100) / 210 );

		rezultat=brzina;
		break;

		case 6:  brzina = ((((rpm1 * 22)/ 0.800) / 4.100) / 210 );

		rezultat=brzina;
		break;
		}
		return rezultat;
	}

	/*
	 * Po promjeni vrijednosti termistora poziva se slijedeca metoda za preracun u stupnjeve celzijusa.
	 * Izvor: http://arduino.cc/playground/ComponentLib/Thermistor2 
	 */

	public double temperatura(double temperatura){


		double rezultat=0.00;

		double log=(10240000/temperatura) - 10000;
		double temp = Math.log10(log);
		
		temp = 1 / (0.001129148 + ((0.000234125*temp) + (0.0000000876741 * temp * temp * temp ))); 
		rezultat= temp - 273.15;  // Konvertira Kelvine u Celziuse

		return rezultat;
	}

}


