package edu.hm.dako.chat.AuditLogServer;

import edu.hm.dako.chat.server.ChatServerInterface;

import java.util.List;
import java.util.Vector;

import edu.hm.dako.chat.common.*;

/**
 * AuditLogServer mit Methode für den Puffer
 * 
 *
 */
public class AuditLogServer implements ChatServerInterface {

	private int bufferSize = 500000; // maximale Anzahl der PDU's im Buffer
	private final List<AuditLogPDU> pduBuffer = new Vector<AuditLogPDU>(); // Puffer

	/**
	 * Fügt die PDU in den Puffer
	 * @param pdu 
	 */
	public void addToBuffer(AuditLogPDU pdu) {
		synchronized (this.pduBuffer) { // durch synchronized wird dieses Objekt von anderen Threads geschützt	
			if (pduBuffer.size() < bufferSize) {
				pduBuffer.add(pdu);
			}
		}
	}

	/**
	 * Nimmt die pdu aus dem Puffer
	 * @return pdu
	 */
	public AuditLogPDU takeFromBuffer() {
		synchronized (this.pduBuffer) { 
			AuditLogPDU pdu = pduBuffer.get(0);
			pduBuffer.remove(pdu);
			return pdu;
		}
	}

	/**
	 * 
	 * @return Anzahl der im Buffer vorhandenen PDU's
	 */
	public int getBufferSize() {
		return pduBuffer.size();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() throws Exception {
		this.stop();
		
	}



}
