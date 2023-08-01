	package edu.hm.dako.chat.AuditLogServer;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.AuditLogPDU;
import edu.hm.dako.chat.AuditLogServer.UdpThread;
import edu.hm.dako.chat.udp.UdpSocket;
/**
 *  Thread für die UDP Implementierung
 *  
 *
 */
public class UdpThread extends Thread {
	private static Log log = LogFactory.getLog(UdpThread.class);

	private boolean active = false;
	private final UdpSocket udpSocket;
	private final AuditLogServer auditLogServer;

	/**
	 * @param udpSocket
	 * @param auditLogServer
	 */
	public UdpThread(UdpSocket udpSocket, AuditLogServer auditLogServer) {
		this.udpSocket = udpSocket;
		this.auditLogServer = auditLogServer;
	}

	/**
	 * Gibt an ob der Thread aktiv ist
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Startet den Thread.
	 */
	public void activate() {
		active = true;
		start();
	}

	/**
	 * Stoppt den Thread.
	 */
	
//	public void deactivate() {
//		active = false;
//		try {
//			udpSocket.close();
//			auditLogServer.stop();
//		} catch (Exception e) {
//			System.out.println("Fehler beim deaktiveren des UDPAuditLogServerListenerThread");
//		}
//
//	}

	@Override
	public void run() {
		while (this.isActive()) {
			this.handleIncomingMessage();
			log.debug("Thread läuft");
		}
	}

	/**
	 * Wartet und akzeptiert die AuditLogPDU.
	 */
	private void handleIncomingMessage() {
		AuditLogPDU auditLogPdu = null;
		try {
			auditLogPdu = (AuditLogPDU) this.udpSocket.receive(200000); // wartet auf PDU mit einem Timer

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

		} catch (Exception e) {
			System.out.println("Verbindung zum Server abbgebaut");
			System.out.println("Warten auf Verbindungsanfrage...");
		}
	}

	/**
	 * Empfangene PDU wird in den Buffer hinzugefüt
	 * 
	 * @param auditLogPDU
	 */
	private void handlePdu(AuditLogPDU auditLogPDU) {
		auditLogServer.addToBuffer(auditLogPDU);
	}
}
