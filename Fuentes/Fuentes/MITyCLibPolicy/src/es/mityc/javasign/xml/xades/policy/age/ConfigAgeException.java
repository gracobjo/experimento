/**
 * Copyright 2015 Ministerio de Industria, Energía y Turismo
 *
 * Este fichero es parte de "Componentes de Firma XAdES 1.1.7".
 *
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por la Comisión Europea– versiones posteriores de la EUPL (la Licencia);
 * Solo podrá usarse esta obra si se respeta la Licencia.
 *
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL»,
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia.
 */
package es.mityc.javasign.xml.xades.policy.age;

import es.mityc.javasign.xml.xades.policy.PolicyException;

/** 
 * <p>Excepción lanzada cuando se detecta un error en la configuración de los managers de políticas de la AGE.</p>
 * 
 */
public class ConfigAgeException extends PolicyException {
	
	/** SerialVersionUID. */
	static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor.</p>
	 */
	public ConfigAgeException() {
		super();
	}

	/**
	 * <p>Constructor.</P>
	 * @param message Mensaje de error
	 * @param cause Excepción que causa que se lance esta
	 */
	public ConfigAgeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * <p>Constructor.</p>
	 * @param message Mensaje de error
	 */
	public ConfigAgeException(final String message) {
		super(message);
	}

	/**
	 * <p>Constructor.</p>
	 * @param cause Excepción que causa que se lance esta
	 */
	public ConfigAgeException(final Throwable cause) {
		super(cause);
	}

}
