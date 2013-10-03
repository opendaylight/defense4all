/**
 * Copyright (c) <2013> <Radware Ltd.> and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * @author Gera Goft 
 * @version 0.1
 */

package org.opendaylight.defense4all.framework.core;

public class ExceptionRepoFactoryInternalError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExceptionRepoFactoryInternalError() {super();}
	public ExceptionRepoFactoryInternalError(String message, Throwable cause) {super(message, cause);}
	public ExceptionRepoFactoryInternalError(String message) {super(message);}
	public ExceptionRepoFactoryInternalError(Throwable cause) {super(cause);}
}