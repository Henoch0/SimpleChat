package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.AuditLogServer.AuditLogServer;
import edu.hm.dako.chat.AuditLogServer.TcpThread;
import edu.hm.dako.chat.tcp.TcpServerSocket;

/**
 * Thread für die TCP Implementierung
 * 
 *
 */
public class TcpThread extends Thread {
	private static Log log = LogFactory.getLog(TcpThread.class);

	private boolean active = false;
	private TcpServerSocket tcpSocket = null;
	private AuditLogServer auditLogServer;
	private Connection connection;
	private boolean connected = false;

	/**
	 * Konstruktor
	 * 
	 * @param tcpSocket      TCP Socket
	 * @param auditLogServer
	 */
	public TcpThread(TcpServerSocket tcpSocket, AuditLogServer auditLogServer) {
		this.tcpSocket = tcpSocket;
		this.auditLogServer = auditLogServer;
	}
	/**
	 * gibt an ob der Thread aktiv ist
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	public void activate() {
		active = true;
		start();
	}
	/**
	 * Stopp den Thread
	 */
//	public void deactivate() {
//		active = false;
//		try {
//			this.interrupt();
//			//connection.close();
//		} catch (Exception ex) {
//			log.error("Fehler beim beenden des TCPAuditLogServerListenerThreads");
//		}
//
//	}

	@Override
	public void run() {
		while (this.isActive() && !Thread.currentThread().isInterrupted()) {
			this.handleIncomingMessage();
			log.debug("Thread läuft");
		}
		
	}

	/**
	 * Verarbeitung einer ankommenden Nachricht
	 */
	protected void handleIncomingMessage() {
		AuditLogPDU auditLogPdu = null;
		try {

			if (!connected) {
				connection = tcpSocket.accept();
				log.debug("Mit ChatServer verbunden.");
				connected = true;
			} else {
				auditLogPdu = (AuditLogPDU) connection.receive();

				switch (auditLogPdu.getPduType()) {
				case LOGIN_REQUEST:
					log.debug("Login Request angekommen");
					this.handlePdu(auditLogPdu);
					break;
				case LOGOUT_REQUEST:
					log.debug("Logout Request angekommen");
					this.handlePdu(auditLogPdu);
					break;
				case CHAT_MESSAGE_REQUEST:
					log.debug("Message Reqeust angekommen");
					this.handlePdu(auditLogPdu);
					break;
				default:
					log.debug("Undefined Request");
					break;
				}
			}

		} catch (Exception e) {
			connected = false;
			System.out.println("Verbindung zum Server abbgebaut");
			System.out.println("Warten auf Verbindungsanfrage...");
		}
	}

	/**
	 * Empfangene PDU wird in den Buffer hinzugefügt
	 * 
	 * @param auditLogPDU
	 */
	private void handlePdu(AuditLogPDU auditLogPDU) {
		auditLogServer.addToBuffer(auditLogPDU);
	}
}
