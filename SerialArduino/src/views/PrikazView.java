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


 * Glavni program aplikacije, koristi se Java swing za prikaz radi lakse kompatibilnosti sa Linux operativnim sistemom. 
 * Kao vizualni prikaz se koriste eu.hansolo.stellseries indikatori i pokazivaci.
 * 
 * Prosirenja su brojna...
 * 
 * Reference: http://harmoniccode.blogspot.com/2011/05/steelseries-392-update.html
 * 
 */

package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import models.SenzoriModel;
import controllers.SerialController;
import controllers.SerialController.SerialReader;
import eu.hansolo.steelseries.extras.Indicator;
import eu.hansolo.steelseries.gauges.Radial;
import eu.hansolo.steelseries.gauges.Radial2Top;
import eu.hansolo.steelseries.tools.BackgroundColor;
import eu.hansolo.steelseries.tools.ColorDef;
import eu.hansolo.steelseries.tools.FrameDesign;
import eu.hansolo.steelseries.tools.FrameEffect;
import eu.hansolo.steelseries.tools.GaugeType;
import eu.hansolo.steelseries.tools.KnobStyle;
import eu.hansolo.steelseries.tools.KnobType;
import eu.hansolo.steelseries.tools.LcdColor;
import eu.hansolo.steelseries.tools.LedColor;
import eu.hansolo.steelseries.tools.NumberFormat;
import eu.hansolo.steelseries.tools.PointerType;
import eu.hansolo.steelseries.tools.SymbolType;
import eu.hansolo.steelseries.tools.TickmarkType;

public class PrikazView {

	private JFrame frmPtk;
	SerialReader serial1 = new SerialReader(null); // Stvaranje instance klase seriala
	SenzoriModel model = serial1.getPodatci(); // preuzimanje instance modela podataka napravljene u klasi seriala

	// inicijalizacija varijabli 

	double br_ok;
	double brzina;
	double temperatura;
	double gear;
	double odo;
	
	int postotak;
	
	boolean a= false;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PrikazView window = new PrikazView();
					window.frmPtk.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PrikazView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		//inicijalizacija okvira

		frmPtk = new JFrame();
		frmPtk.setTitle("PTK - Projekt putnog kompjutora");
		frmPtk.getContentPane().setBackground(Color.BLACK);


		SerialController.main(null); // Pozivanje metode maina kontrolera komunikacije seriala


		frmPtk.setBounds(100, 100, 892, 590);
		frmPtk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPtk.getContentPane().setLayout(null);

		//inicijalizacija okvira oko pokazivaca 

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalGlue.setBounds(408, 315, 1, 1);
		frmPtk.getContentPane().add(horizontalGlue);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.GRAY);
		panel_1.setBackground(Color.BLACK);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.setBounds(242, 438, 326, 67);
		frmPtk.getContentPane().add(panel_1);

		Indicator indicator = new Indicator();
		panel_1.add(indicator);
		indicator.setSymbolType(SymbolType.LOW_BEAM);
		indicator.setOnColor(ColorDef.WHITE);
		indicator.setOn(true);
		indicator.setInitialized(true);

		final Indicator duga_sv = new Indicator();
		panel_1.add(duga_sv);
		duga_sv.setOnColor(ColorDef.BLUE);
		duga_sv.setSymbolType(SymbolType.FULL_BEAM);
		duga_sv.setOn(false);
		duga_sv.setInitialized(true);

		Indicator indicator_2 = new Indicator();
		panel_1.add(indicator_2);
		indicator_2.setOnColor(ColorDef.YELLOW);
		indicator_2.setSymbolType(SymbolType.TURN_LIGHT);
		indicator_2.setOn(true);
		indicator_2.setInitialized(true);

		Indicator indicator_Akumulator = new Indicator();
		panel_1.add(indicator_Akumulator);
		indicator_Akumulator.setOn(true);
		indicator_Akumulator.setSymbolType(SymbolType.BATTERY);
		indicator_Akumulator.setInitialized(true);

		Indicator indicator_3 = new Indicator();
		panel_1.add(indicator_3);
		indicator_3.setOnColor(ColorDef.ORANGE);
		indicator_3.setSymbolType(SymbolType.ATTENTION);
		indicator_3.setOn(true);
		indicator_3.setInitialized(true);

		// inicijalizacija pokazivaca broja okretaja

		final Radial rpm = new Radial();
		rpm.setLcdUnitStringVisible(true);
		rpm.setCustomLcdUnitFont(new Font("Times New Roman", Font.BOLD, 9));
		rpm.setCustomLcdUnitFontEnabled(true);
		rpm.setLabelNumberFormat(NumberFormat.FRACTIONAL);
		rpm.setLcdInfoFont(new Font("Verdana", Font.PLAIN, 8));
		rpm.setLcdInfoString("Stupanj prijenosa");
		rpm.setBounds(440, 128, 298, 310);
		frmPtk.getContentPane().add(rpm);
		rpm.setTrackVisible(true);
		rpm.setStdTimeToValue(400L);
		rpm.setValueCoupled(false);
		rpm.setRtzTimeToValue(500L);
		rpm.setBackground(Color.WHITE);
		rpm.setTitleAndUnitFont(new Font("Verdana", Font.BOLD, 12));
		rpm.setLcdUnitFont(new Font("Times New Roman", Font.BOLD, 5));
		rpm.setThresholdBehaviourInverted(true);
		rpm.setThreshold(140.0);
		rpm.setTrackStart(6.0);
		rpm.setTrackStartColor(Color.RED);
		rpm.setTrackStop(8.0);
		rpm.setTitleAndUnitFontEnabled(true);
		rpm.setTickmarkSectionsVisible(true);
		rpm.setRangeOfMeasuredValuesVisible(true);
		rpm.setGlowing(true);
		rpm.setHighlightSection(true);
		rpm.setHighlightArea(true);
		rpm.setBackgroundColor(BackgroundColor.BEIGE);
		rpm.setExpandedSectionsEnabled(true);
		rpm.setFrameBaseColorEnabled(true);
		rpm.setGlowColor(Color.CYAN);
		rpm.setInitialized(true);
		rpm.setLcdColor(LcdColor.STANDARD_GREEN_LCD);
		rpm.setLcdThreshold(70.0);
		rpm.setLcdThresholdVisible(true);
		rpm.setLcdThresholdBehaviourInverted(true);
		rpm.setMajorTickmarkType(TickmarkType.TRIANGLE);
		rpm.setSectionsVisible(true);
		rpm.setTransparentSectionsEnabled(true);
		rpm.setTransparentAreasEnabled(true);
		rpm.setSection3DEffectVisible(true);
		rpm.setPointerType(PointerType.TYPE14);
		rpm.setMinorTickmarkType(TickmarkType.CIRCLE);
		rpm.setMaxMeasuredValueVisible(true);
		rpm.setLcdUnitString("Gear    ");
		rpm.setKnobStyle(KnobStyle.BRASS);
		rpm.setKnobType(KnobType.METAL_KNOB);
		rpm.setGlowPulsating(true);
		rpm.setGlowVisible(true);
		rpm.setGaugeType(GaugeType.TYPE3);
		rpm.setFrameEffect(FrameEffect.EFFECT_CONE);
		rpm.setFrameDesign(FrameDesign.GLOSSY_METAL);
		rpm.setAreasVisible(true);
		rpm.setArea3DEffectVisible(true);
		rpm.setTrackSectionColor(Color.RED);
		rpm.setTrackSection(6.0);
		rpm.setPeakValue(140.0);
		rpm.setPeakValueVisible(true);
		rpm.setMinMeasuredValueVisible(true);
		rpm.setUnitString("RPM");
		rpm.setLedColor(LedColor.BLUE_LED);
		rpm.setTitle("x1000/min");
		rpm.doLayout();
		rpm.setMaxValue(8.0);
		rpm.setValueAnimated(0.0);
		rpm.setUnitString("RPM");
		rpm.setTickmarkColor(Color.BLUE);

		//inicijalizacija prikaza kilometar sata

		final Radial kmh = new Radial();
		kmh.setMajorTickmarkType(TickmarkType.TRIANGLE);
		kmh.setLcdColor(LcdColor.STANDARD_GREEN_LCD);
		kmh.setCustomLcdUnitFont(new Font("Verdana", Font.BOLD, 8));
		kmh.setLcdValueFont(new Font("Dialog", Font.PLAIN, 8));
		kmh.setLcdDecimals(1);
		kmh.setLcdInfoString("Trip1");
		kmh.setLcdValueAnimated(0.0);
		kmh.setMinorTickmarkType(TickmarkType.CIRCLE);
		kmh.setFrameDesign(FrameDesign.SHINY_METAL);
		kmh.setFrameEffect(FrameEffect.EFFECT_CONE);
		kmh.setMaxNoOfMajorTicks(20);
		kmh.setLcdUnitStringVisible(true);
		kmh.setLedVisible(false);
		kmh.setValueCoupled(false);
		kmh.setValueAnimated(0.0);
		kmh.setUnitString("Km/h");
		kmh.setTrackStop(8.0);
		kmh.setTrackStartColor(Color.RED);
		kmh.setTrackStart(6.0);
		kmh.setTrackSectionColor(Color.RED);
		kmh.setTrackSection(6.0);
		kmh.setTitleAndUnitFontEnabled(true);
		kmh.setTitleAndUnitFont(new Font("Verdana", Font.BOLD, 12));
		kmh.setTitle("Arduino");
		kmh.setTickmarkSectionsVisible(true);
		kmh.setTickmarkColor(Color.BLUE);
		kmh.setThresholdBehaviourInverted(true);
		kmh.setThreshold(160.0);
		kmh.setStdTimeToValue(400L);
		kmh.setSectionsVisible(true);
		kmh.setSection3DEffectVisible(true);
		kmh.setRtzTimeToValue(500L);
		kmh.setRangeOfMeasuredValuesVisible(true);
		kmh.setPeakValueVisible(true);
		kmh.setPeakValue(140.0);
		kmh.setMaxValue(250.0);
		kmh.setMaxMeasuredValueVisible(true);
		kmh.setLcdUnitString("Km");
		kmh.setLcdUnitFont(new Font("Arial", Font.PLAIN, 8));
		kmh.setLcdThreshold(70.0);
		kmh.setInitialized(true);
		kmh.setHighlightSection(true);
		kmh.setHighlightArea(true);
		kmh.setGlowing(true);
		kmh.setGlowVisible(true);
		kmh.setGlowColor(Color.CYAN);
		kmh.setFrameBaseColorEnabled(true);
		kmh.setExpandedSectionsEnabled(true);
		kmh.setBackground(Color.WHITE);
		kmh.setAreasVisible(true);
		kmh.setArea3DEffectVisible(true);
		kmh.setBounds(65, 59, 380, 373);
		frmPtk.getContentPane().add(kmh);

		// inicijalizacija pokazivaca temperature

		final Radial2Top temp = new Radial2Top();
		temp.setFrameEffect(FrameEffect.EFFECT_TORUS);
		temp.setUnitString(" \u00B0C");
		temp.setTitle("Temp");
		temp.setMajorTickmarkType(TickmarkType.TRIANGLE);
		temp.setGaugeType(GaugeType.TYPE2);
		temp.setLcdVisible(false);
		temp.setMinorTickmarkType(TickmarkType.CIRCLE);
		temp.setMaxNoOfMinorTicks(5);
		temp.setMinorTickSpacing(10.0);
		temp.setBounds(379, 0, 216, 225);
		temp.setLedVisible(false);
		frmPtk.getContentPane().add(temp);


		// key listener za duga svijetla - malo slovo L -  za moguca prosirenja (stavljen kao primjer - samo pali lampicu na panelu)

		frmPtk.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == 'l' && a== false ){
					duga_sv.setOn(true);
					a=true;
				}
				else {
					duga_sv.setOn(false);
					a=false;
				}
			}

		});



		/*
		 * Otvaranje novog thread-a ( dretve) koja paralelno sa view komunicira sa modelom podataka,
		 * provjerava promjenu u zapisu i salje vrijednosti varijabli grafickim pokazivacima
		 */

		Thread prikaz = new Thread(new Runnable() {
			public void run() {
				while(true){ //neprekidna petlja 

					//Preuzimanje vrijednosti u varijable iz modela podataka					
					brzina = model.getBrzina();
					br_ok=model.getRpm();
					gear = model.getStprij();
					odo = model.getOdo();
					temperatura=model.getTemp();

					try {
						Thread.sleep(100); // blokiranje thread-a na 100 ms zbog gladjeg prikaza

						//Upis vrijednosti u graficke pokazivace
						rpm.setValueAnimated(br_ok);
						kmh.setValueAnimated(brzina);
						temp.setValueAnimated(temperatura);
						rpm.setLcdValue(gear);
						kmh.setLcdValue(odo);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					/*
					 * uvijet kada ce pocet "blinkati" lampica na prikazu broja okretaja kao upozorenje da 
					 * je potrebno promjeniti brzinu
					 */
					if(rpm.getValue()>=5.5){
						rpm.setLedBlinking(true);
					}
					else{
						rpm.setLedBlinking(false);
					}


				}
			}
		});prikaz.start(); // pokretanje thread-a 

	}
}


