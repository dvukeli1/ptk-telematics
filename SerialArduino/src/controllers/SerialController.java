/*				 ********* Arduino projekt - Projekt putnog racunala *********

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

 * SerialConntroler sluzi sa serijsku komunikaciju izmedju Arduina i aplikacije. Sada je izvedena samo jednosmjerna 
 * komunikacija, Arduino salje podatke aplikaciji koja ih obradjuje.
 * Moguce prosirenje je slanje Arduinu koda za provjeru da li je "ziv" i tada skupiti podatke sa njega.Kao i mogucnost
 * slanja naredbe za paljenje svijetla, trube, paljenje "vozila" ( Arduino onda mora ici na zasebno napajanje, a 
 * ventilator se starta pomocu releja 5/12V ili pomocu optokaplera koji je spojen na Arduino i na relej 12V).
 * 
 * U ovoj klasi se rade i sve jednostavne matematicke operacije koje su potrebne za tocan izracun, tj. pretvaranje podataka
 * koje salje Arduino u korisne informacije kao i komunikacija sa metodama iz drugih klasa za kompliciranije matematicke
 * izracune i komunikaciju sa bazom podataka.
 * 
 * Reference: http://rxtx.qbang.org/wiki/index.php/Deploying_JAVA_with_RXTX#Simpler_approach_for_Windows
 *            na ovome linku se nalaze detaljna uputstva za serial na linuxu i windows operativnom sustavu i 
 *            kompletan wiki vezan za ovaj nacin serijske komunikacije
 */

package controllers;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import controllers.DatabaseController;
import models.SenzoriModel;
import models.VozaciModel;


public class SerialController  {


	public SerialController()
	{

		super();

	}

	void connect ( String portName ) throws Exception //Metoda za otvaranje konekcije
	{


		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.out.println("Error: Port is currently in use");
		}
		else
		{

			CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
				// Postavke identicne kao i na Arduinu za serial port
				serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialWriter(out))).start();

				serialPort.addEventListener(new SerialReader(in));
				serialPort.notifyOnDataAvailable(true);

			}
			else
			{
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}     
	}


	/**
	 * Handles the input coming from the serial port. A new line character
	 * is treated as the end of a block in this example. 
	 */
	public static class SerialReader implements SerialPortEventListener  // Metoda Serial listenera, "slusaca" na serial portu
	{

		static SenzoriModel podatci = new SenzoriModel(); // stvaranje instance senzora
		static DatabaseController baza = new DatabaseController(); // stvaranje instance baze
		static VozaciModel vozaci = new VozaciModel(); // stvaranje instance vozaca
		static MatematikaController izracun = new MatematikaController();

		int brojac=0;
		int brojac1=0;
		double odometar=0;
		double odometar1=0;
		double odo_fin=0;
		// Povezivanje instance modela senzora sa view klasom 
		public SenzoriModel getPodatci() {
			return podatci;
		}

		@SuppressWarnings("static-access")
		public void setPodatci(SenzoriModel podatci) {
			this.podatci = podatci;
		}

		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader ( InputStream in )
		{
			this.in = in;
		}


		public void serialEvent(SerialPortEvent arg0) {  // metoda koja se poziva ako se desi event na serial portu
			int data;
			double brzina;
			try
			{
				int len = 0;
				while ( ( data = in.read()) > -1 )
				{
					if ( data == '\n' ) {
						break;
					}
					buffer[len++] = (byte) data;
				}
				String buff=new String(buffer);
				/*
				 * Prvo se delimiterom : odvaja prefix koji sluzi za switch-case , a nakon
				 * toga delimiter \n koji oznacava kraj poslanog podatka - koristenje Serial.println
				 * naredbe u Arduino kodu koji kao kraj stringa koristi \n (novi red)
				 */
				String[] rezultat = buff.split(":");
				
				int izb=Integer.valueOf(rezultat[0]);
				
				String dobijeno = String.valueOf(rezultat[1]);

				String[] fin = dobijeno.split("\n");

				String finf = String.valueOf(fin[0]);
				//System.out.println(finf);
				//int rez=Integer.valueOf(finf);
				//  System.out.println("test "+ Double.valueOf(finf));
				switch (izb){

				case 1 :   podatci.setRpm( Double.valueOf(finf)/1000); //Upisuje senzor1  dijeli ga sa 1000 zbog prikaza

				

				/*
				 * Moguce prosirenje -  sada se racuna samo trip time, a ne ukupna predjena kilometraza vozila
				 * 					    potrebno je pri ukljucenju vozila iz Arduina ucitati ukupnu predjenu 
				 * 						kilometrazu vozila i po zavrsetku voznje poslati Arduinu i bazi ukupnu
				 * 					    predjenu kilometrazu, Arduino je cuva na EPROM-u, kako bi se zastitila
				 * 						zlouporaba, tj. namjestanje kilometraze(odometra)
				 * */

				brzina=izracun.brzina(podatci.getStprij(), podatci.getRpm()); // metoda matematickog kontrolera
				podatci.setBrzina(brzina);


				if(brojac%2==0 ){ // svaku sekundu racuna predjeni broj kilometara  - modulo od 2

					odometar=(podatci.getBrzina()/3600);

					odo_fin = odometar+odo_fin;

					podatci.setOdo(odo_fin);


				}



				// Poziva metodu za komunikaciju sa bazom
				if (brojac%240==0 ){ /*timer za vrijeme slanja	podataka u bazu (svake 2 minute - 2*60 sec*2 = 240 - modulo od 240 = 0 ).*/
					baza.insertPodatci(podatci.getRpm()*1000, podatci.getBrzina(), podatci.getOdo(),podatci.getStprij()
							,podatci.getTemp(), podatci.getTps(),vozaci.getId_vozilo(), vozaci.getID_Vozaca());
					brojac=0;
				}


				brojac=brojac+1; /*Povecava brojac za 1 svakih pola sekunde tj. kada dodje podatak o RPM iskoristeno za 
					timer.*/


				break;

				case 2 :  double primljeno1 = Double.valueOf(finf)/1000;
					vozaci.setId_vozilo((int)primljeno1);

				break;

				case 3 :  double primljeno= Double.valueOf(finf);

				double temp=izracun.temperatura(primljeno); // // metoda matematickog kontrolera

				podatci.setTemp(temp/10);

				break;

				case 4 : double stprij = (Double.valueOf(finf)/1000)-1; //djeli se sa 1000 - arduino salje tako i oduzima se 1 da bi se dobila neutralna brzina 0;
				podatci.setStprij(stprij);

				brzina=izracun.brzina(podatci.getStprij(), podatci.getRpm());  // Salje se u matematicki kontroler
				podatci.setBrzina(brzina);

				break;

				case 5 :podatci.setTps(Double.valueOf(finf)); // sprema TPS u model
				break;

				default: 
					break;
				};

			}

			catch ( IOException e )
			{
				e.printStackTrace();
				System.exit(-1);
			}  


		}


	}

	/** */
	public static class SerialWriter implements Runnable   // Metoda slanja iz aplikacije prema uredjaju serialom
	{
		OutputStream out;

		public SerialWriter ( OutputStream out )
		{
			this.out = out;
		}

		public void run ()
		{
			try
			{                
				int c = 0;
				while ( ( c = System.in.read()) > -1 )
				{
					this.out.write(c);
				}                
			}
			catch ( IOException e )
			{
				e.printStackTrace();
				System.exit(-1);
			}            
		}
	}



	public static void main ( String[] args ) // Main metoda klase
	{ 

		try
		{
			(new SerialController ()).connect("COM16"); // Arduino je spojen na COM16 - potrebno izmjeniti ako je drugi port

		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

	}








}