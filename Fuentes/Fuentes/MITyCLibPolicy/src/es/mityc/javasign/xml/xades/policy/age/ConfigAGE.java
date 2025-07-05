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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mityc.firmaJava.libreria.xades.elementos.xades.DigestAlgAndValueType;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyHash;
import es.mityc.javasign.i18n.I18nFactory;
import es.mityc.javasign.i18n.II18nManager;
import es.mityc.javasign.xml.xades.policy.ConstantsPolicy;
import es.mityc.javasign.xml.xades.policy.facturae.ConstantsFacturaePolicy;

/**
 * <p>Esta clase centraliza la configuración de funcionamiento de un validador de política de la AGE.</p>
 *  
 */
public class ConfigAGE {
	/** Logger. */
	private static final Log LOG = LogFactory.getLog(ConfigAGE.class);
	/** Internacionalizador. */
	private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsPolicy.LIB_NAME);

	/** Punto. */
	private static final String STRING_POINT = ".";
	/** Cadena vacía. */
	private static final String STRING_EMPTY = "";

	private static final String SP_URI_KEY = "policy.spuri";
	/** Identificador de la policy que se espera encontrar en la firma. */
	private URI policyIdXades = null;
	/** */
	private URI spUri = null;
	/** Cadena de identificación de la policy. */ 
	private String policyIdValidador = null;
	/** Cadena de descripción de la policy. */
	private String policyDescription = null;
	/** Hash's que se consideran válidos de la policy. */
	private ArrayList<DigestAlgAndValueType> huellas = null;
	/** Número de digest que utilizar para el escritor de policy. */
	private int policyWriterId = -1;

	
	/**
	 * <p>Constrcutor.</p>
	 * @param props Propiedades de configuración
	 * @param prefix Prefijo de propiedades que afectan a esta configuración
	 * @throws ConfigAgeException Lanzada cuando faltan datos de configuración o están mal formados
	 */
	public ConfigAGE(final Properties props, final String prefix) throws ConfigAgeException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Se carga la configuración de políticas de la AGE: " + prefix);
		}
		String prep = STRING_EMPTY;
		if ((prefix != null) && (!STRING_EMPTY.equals(prefix.trim()))) {
			prep = prefix + STRING_POINT;
		}
		// carga los datos de identidad
		try {
			URI policyIdXadesStr = new URI(props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_POLICY_ID));
			policyIdValidador = props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_POLICY_ID_VALIDADOR);
			if ((policyIdXadesStr == null) || (policyIdValidador == null)) {
				LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_1));
				throw new ConfigAgeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_2));
			}
			policyIdXades = policyIdXadesStr;
		} catch (URISyntaxException ex) {
			LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_3));
			throw new ConfigAgeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_2), ex);
		}
		// carga la descripcion de la policy
		try  {
			policyDescription = props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_POLICY_ID_VALIDADOR);
		} catch (MissingResourceException ex) {
			if (LOG.isTraceEnabled()) {
				LOG.trace(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_4, prep));
			}
		}
		
		// Carga la spUri de descarga
		try {
			String value = props.getProperty(prep + SP_URI_KEY);
			spUri = new URI(value);
		} catch (URISyntaxException ex) {
			LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_3));
			throw new ConfigAgeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_2), ex);
		}
		
		// Carga la huellas
		huellas = new ArrayList<DigestAlgAndValueType>();
		int i = 0;
		while (true) {
			String hashId = props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_HASH_ID + i);
			String hashValue = props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_HASH_VALUE + i);
			if ((hashId != null) && (hashValue != null)) {
				huellas.add(new SigPolicyHash(null, hashId, hashValue));
				i++;
			} else {
				break;
			}
		}
		// carga el número de hash que se utilizará en el escritor 
		try  {
			String policyWriterIdStr = props.getProperty(prep + ConstantsFacturaePolicy.PROPNAME_WRITER_HASH);
			if (policyWriterIdStr == null) {
				if (LOG.isTraceEnabled()) {
					LOG.trace(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_6, prep));
				}
			} else {
				policyWriterId = Integer.parseInt(policyWriterIdStr);
				if (policyWriterId >= huellas.size()) {
					policyWriterId = -1;
					LOG.error(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_5));
				}
			}
		} catch (NumberFormatException ex) {
			LOG.error(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_5), ex);
		}
	}
	
	/**
	 * Devuelve la URI que identifica esta política.
	 * @return policyIdXades
	 */
	public URI getPolicyIdXades() {
		return policyIdXades;
	}
	
	/**
	 * 
	 */
	public URI getSpUri() {
		return spUri;
	}
	
	/**
	 * Devuelve una cadena que identifica al manager.
	 * @return policyIdValidador
	 */
	public String getPolicyIdValidador() {
		return policyIdValidador;
	}

	/**
	 * Devuelve una cadena descriptiva de la política.
	 * @return policyDescription
	 */
	public String getPolicyDescription() {
		return policyDescription;
	}
	
	/**
	 * Devuelve el número de hash de los configurados configurado.
	 * @return policyWriterId
	 */
	public int getPolicyWriterId() {
		return policyWriterId;
	}
	
	/**
	 * Huellas configuradas que identifican la política.
	 * @return huellas
	 */
	public ArrayList<DigestAlgAndValueType> getHuellas() {
		return huellas;
	}
}
