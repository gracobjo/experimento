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
package es.mityc.javasign.xml.xades.policy.facturae;

/** 
 * Constantes del módulo políticas de FacturaE.
 * 
 */
public final class ConstantsFacturaePolicy {
	
	/**
	 * Constructor.
	 */
	private ConstantsFacturaePolicy() { };
	
	/** Propiedad del algoritmo de hash. */
	public static final String PROPNAME_HASH_ID = "policy.digest.id.";
	/** Propiedad del valor del hash. */
	public static final String PROPNAME_HASH_VALUE = "policy.digest.value.";
	/** Propiedad de la uri del schema xades. */ 
	public static final String PROPNAME_SCHEMA_URI = "xades.schema.uri.";
	/** Propiedad del identificador de la política. */
	public static final String PROPNAME_POLICY_ID = "policy.id";
	/** Propiedad del identificador del validador. */
	public static final String PROPNAME_POLICY_ID_VALIDADOR = "policy.idValidator";
	/** Propiedad de la descripción del validador. */
	public static final String PROPNAME_POLICY_DESCRIPTION = "policy.description";
	/** Propiedad del digest del escritor. */
	public static final String PROPNAME_WRITER_HASH = "policy.writer.digest";
	
	// Contantes de internacionalización

	/** El certificado firmante no está recogido en los apartados a) ó c) del artículo 18 del Reglamento que está recogido en R.D. 1496/2003 del 28 de Noviembre.. */
	public static final String I18N_POLICY_FACTURAE_1 = "i18n.mityc.policy.facturae.1";
	/** La factura no tiene forma enveloped. */
	public static final String I18N_POLICY_FACTURAE_2 = "i18n.mityc.policy.facturae.2";
	/** La transformada produce una estructura que se desconoce si es una enveloped aceptada. */
	public static final String I18N_POLICY_FACTURAE_3 = "i18n.mityc.policy.facturae.3";
	/** La transformada produce cambios en el nodo de certificado de firma que se desconoce si altera su contenido. */
	public static final String I18N_POLICY_FACTURAE_4 = "i18n.mityc.policy.facturae.4";
}
