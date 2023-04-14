/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

public class JMSOperationException extends RuntimeException{

	private static final long serialVersionUID = 1231516254373656587L;


	public JMSOperationException(String message) {
		super(message);
	}

	public JMSOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
