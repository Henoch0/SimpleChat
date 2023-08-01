package edu.hm.dako.chat.AuditLogServer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.tcp.TcpServerSocket;

/**
 * AuditLogServer Implementierung auf Basis von TCP
 * 
 *
 */
public class AuditLogTcpServer extends AuditLogServer {

	private static Logger log = Logger.getLogger(AuditLogTcpServer.class);

	// Serverport fuer AuditLog-Service
	static final int AUDIT_LOG_SERVER_PORT = 40001;

	// Standard-Puffergroessen fuer Serverport in Bytes
	static final int DEFAULT_SENDBUFFER_SIZE = 30000;
	static final int DEFAULT_RECEIVEBUFFER_SIZE = 800000;

	// Server
	private static AuditLogServer auditLogServer;
	private AuditServerThread auditServerThread;
	private TcpThread tcpThread;
	private TcpServerSocket tcpSocket;

	/**
	 * Konstruktor
	 * 
	 * @param tcpSocket
	 */
	public AuditLogTcpServer(TcpServerSocket tcpSocket) {
		this.tcpSocket = tcpSocket;
	}

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configureAndWatch("log4j.auditLogServer_tcp.properties", 60 * 1000);
		System.out.println("AuditLog-TcpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);
		log.info("AuditLog-TcpServer gestartet, Port: " + AUDIT_LOG_SERVER_PORT);

		// TODO: Implementierung des AuditLogServers auf TCP-Basis hier ergaenzen

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
	 * Methode die den AuditLogServer & TCP Socket erschafft mit dem übergebenen
	 * Port und Puffergröße
	 * 
	 * @param serverPort
	 * @param sendBufferSize
	 * @param receiveBufferSize
	 * @return AuditLogServer
	 */
	public static AuditLogServer createAuditLogServer(int serverPort, int sendBufferSize, int receiveBufferSize) {

		try {
			// Erzeugen eines neuen TCP-Server Sockets
			TcpServerSocket tcpSocket = new TcpServerSocket(serverPort, sendBufferSize, receiveBufferSize);
			System.out.println("TCPSocket wurde erstellt");
			return new AuditLogTcpServer(tcpSocket);

		} catch (Exception e) {
			log.error("Fehler beim Erzeugen des TCPAuditLogServers");
		}

		return null;
	}

	/**
	 * Start Methode des TCP Threads
	 */
	public void start() {
		this.tcpThread = new TcpThread(tcpSocket, this);
		this.auditServerThread = new AuditServerThread(this);
		this.tcpThread.activate();
		this.auditServerThread.activate();
	}

	/**
	 * Stopt Threads
	 */
	
//	public void stop() {
//		this.auditServerThread.deactivate();
//		this.tcpThread.deactivate();
//		System.out.println("Threads gestoppt");
//		
//		try {
//			this.tcpThread.join();
//			this.auditServerThread.join();
//		} catch (Exception e) {
//			System.out.println("Fehler beim Stoppen des TCP AuditLogServers");
//			log.error("Fehler beim stoppen des TCP AuditLogServers");
//		}
//
//	}

}
