package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.udp.UdpSocket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * AuditLogServer Implementierung auf Basis von UDP
 *
 */
public class AuditLogUdpServer extends AuditLogServer {

	private static Logger log = Logger.getLogger(AuditLogUdpServer.class);

	// UDP-Serverport fuer AuditLog-Service
	static final int AUDIT_LOG_SERVER_PORT = 40001;

	// Standard-Puffergroessen fuer Serverport in Bytes
	static final int DEFAULT_SENDBUFFER_SIZE = 30000;
	static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

	// Server
	private static AuditLogServer auditLogServer;
	private UdpThread udpThread;
	private AuditServerThread auditServerThread;
	private UdpSocket udpSocket;

	/**
	 * Konstruktor für Implementierung auf UDP-Basis
	 * 
	 * @param udpSocket
	 */
	public AuditLogUdpServer(UdpSocket udpSocket) {
		this.udpSocket = udpSocket;
	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_udp.properties", 60 * 1000);
		System.out.println("AuditLog-UdpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
		log.info("AuditLog-UdpServer gestartet, Port" + AUDIT_LOG_SERVER_PORT);

		// TODO: Implementierung des AuditLogServers auf UDP-Basis hier ergaenzen

		try {
			// Erzeugen des Servers
			auditLogServer = createAuditLogServer(AUDIT_LOG_SERVER_PORT, DEFAULT_SENDBUFFER_SIZE,
					DEFAULT_RECEIVEBUFFER_SIZE);
			auditLogServer.start(); // Starten des Servers
			
		} catch (Exception e) {
			ExceptionHandler.logException(e);
			throw new Exception(e);
		}
	}

	/**
	 * Methode die den AuditLogServer & UDP Socket erschafft mit dem übergebenen
	 * Port und Puffergröße
	 * 
	 * @param serverPort
	 * @param sendBufferSize
	 * @param receiveBufferSize
	 * @return AuditLogServer
	 */
	public static AuditLogServer createAuditLogServer(int serverPort, int sendBufferSize, int receiveBufferSize) {

		try {
			// Erzeugen eines neuen UDP-Server Sockets
			UdpSocket udpSocket = new UdpSocket(serverPort, sendBufferSize, receiveBufferSize);
			System.out.println("UDPSocket wurde erstellt");
			return new AuditLogUdpServer(udpSocket);
		} catch (Exception e) {
			System.out.println("Fehler beim Erzeugen des UDPAuditLogServers");
			log.error("Fehler beim Erzeugen des UDPAuditLogServers");
		}
		return null;

	}

	/**
	 * Start Methode des UDP Threads
	 */
	public void start() {
		this.udpThread = new UdpThread(udpSocket, this);
		this.auditServerThread = new AuditServerThread(this);
		this.udpThread.activate();
		this.auditServerThread.activate();
		log.debug("Threads gestartet");
	}

	/**
	 * Stop Methode des UDP Threads
	 */
//	public void stop() {
//		this.auditServerThread.deactivate();
//		this.udpThread.deactivate();
//
//		try {
//			this.udpThread.join();
//			this.auditServerThread.join();
//		} catch (Exception e) {
//			log.error("Fehler beim Stoppen des UDPAuditLogServers");
//		}
//
//	}
}