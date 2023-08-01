package edu.hm.dako.chat.AuditLogServer;

import java.io.FileWriter;


import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opencsv.CSVWriter;

import edu.hm.dako.chat.common.AuditLogPDU;
/**
 * Main Thread des AuditLogServers. Erstellt den Writer & CSV Datei und speichert die PDU's, aus dem Puffer, in die CSV Datei
 * 
 *
 */
public class AuditServerThread extends Thread {
	private static Log log = LogFactory.getLog(AuditServerThread.class);
	
	private AuditLogServer auditLogServer = new AuditLogServer();
	public boolean headerWritten = false; // gibt an ob schon ein Header in csv Datei vorhanden ist

	/**
	 * Konstruktor
	 * 
	 * @param auditLogServer
	 */
	public AuditServerThread(AuditLogServer auditLogServer) {
		this.auditLogServer = auditLogServer;
	}

	/**
	 * Startet den AuditLogServer Thread.
	 */
	public void activate() {
		start();
		log.debug("Main Thread wird gestartet");
	}

	
	public void deactivate() {
	
	}

	/**
	 * Thread run Method
	 */
	public void run() {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("ChatLog.csv", false)); // Erstellung des Writers und CSV Datei

			while (this.auditLogServer.getBufferSize() >= 0) {
				if (this.auditLogServer.getBufferSize() > 0) {
					this.addDataToCSV(this.auditLogServer.takeFromBuffer()); // schreibt die PDU vom Puffer in die CSV Datei
																				
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}
	}

	/**
	 * Schreibt die Daten einer PDU in die CSV Datei.
	 * 
	 * @param pdu
	 */
	private void addDataToCSV(AuditLogPDU pdu) throws Exception {

		CSVWriter writer = new CSVWriter(new FileWriter("ChatLog.csv", true));

		if (headerWritten == false) { // schreibt Header in die Log Datei falls noch keiner vorhanden ist
			String[] header = "Pdu-Typ; Username; Message; Time ".split(";");
			writer.writeNext(header);
			headerWritten = true;
		}

		Date a = new Date(pdu.getAuditTime());
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); // Format für die Zeitangabe der PDU
		String time = timeFormat.format(a) + " GMT";
		
		if (pdu.getPduType().getId() == 3) { // ID 3 = Message
			String[] message = { pdu.getPduType().toString(), pdu.getUserName(), pdu.getMessage(), time };
			writer.writeNext(message);
			writer.flush();
			writer.close();
		} else { // Login / Logout
			String[] message = { pdu.getPduType().toString(), pdu.getUserName(), " ", time, };
			writer.writeNext(message);
			writer.flush();
			writer.close();
		}

		// bei jedem Logout wird die Anzahl der bisherigen Logins, Logouts und versendeten Nachrichten angezeigt
		if (pdu.getPduType().getId() == 2) { // ID 2 = Logout
			ChatLogReader.readFile();
			ChatLogReader.print();
		}
	}

}