package edu.hm.dako.chat.AuditLogServer;


import java.io.IOException;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.exceptions.CsvException;


	/**
	 * Diese Klasse liest die ChatLogDatei(ChatLog.csv) aus und gibt die Anzahl der Logins, Logouts und Nachrichten an.
	 *
	 */

public class ChatLogReader {

	private static Log log = LogFactory.getLog(ChatLogReader.class);
	public static int loginCounter = 0;
	public static int logoutCounter = 0;
	public static int messageCounter = 0;

	

	/**
	 * Auslesen der Log Datei
	 */
	public static void readFile() throws CsvException {
		try {
			
			Reader reader = Files.newBufferedReader(Paths.get("ChatLog.csv"));
			CSVReader csvReader = new CSVReaderHeaderAwareBuilder(reader).withSkipLines(0).build(); // reader überspringt
																									// den vorhandenen Header
			List<String[]> csv = csvReader.readAll();
			for (String[] read : csv) {
				if (read[0].contains("Login")) {
					loginCounter++;
				} else if (read[0].contains("Logout")) {
					logoutCounter++;
				} else if (read[0].contains("Chat")) {
					messageCounter++;
				}
			}
			csvReader.close();
			log.debug("Reader geschlossen");

		} catch (IOException e) {
			System.out.println("Fehler beim Auslesen der Log Datei");
			log.error("Fehler beim Auslesen der Log Datei");
		}

	}
	



	/**
	 * Anzeigen der Auswertung in der Kommandozeile
	 * @param pdu
	 */
	public static void	print () {
		
		System.out.println("Anzahl der Logins: " + loginCounter);
		System.out.println("Anzahl der Nachrichten: " + messageCounter);
		System.out.println("Anzahl der Logouts:" + logoutCounter);
		System.out.println("-------------------------------------");
		loginCounter=0;
		messageCounter=0;
		logoutCounter=0;
	}

}

