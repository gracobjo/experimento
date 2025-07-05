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
package es.mityc.javasign.xml.xades.policy;

/**
 * <p>Constantes del módulo de Políticas.</p>
 * 
 */
public final class ConstantsPolicy {
	/**
	 * Constructor.
	 */
	private ConstantsPolicy() { };
	
	/** Nombre de la librería. */
	public static final String LIB_NAME = "MITyCLibPolicy";

	// No Transforms
	/** Hay transformadas aplicadas al contenido firmado que podrían estar alterando su valor. */
	public static final String I18N_POLICY_NO_TRANSFORMS_1 = "i18n.mityc.policy.notransforms.1";
	/** Política de seguridad restrictiva: Transformadas no permitidas. */
	public static final String I18N_POLICY_NO_TRANSFORMS_2 = "i18n.mityc.policy.notransforms.2";

	// MITyc Trust
	/** No hay disponible un validador de confianza de entidades admitidas por MITyC. */
	public static final String I18N_POLICY_MITYC_TRUST_1 = "i18n.mityc.policy.mityc.1";
	/** Política de confianza MITyC. */
	public static final String I18N_POLICY_MITYC_TRUST_2 = "i18n.mityc.policy.mityc.2";
	/** No hay certificados disponibles para comprobar confianza. */
	public static final String I18N_POLICY_MITYC_TRUST_3 = "i18n.mityc.policy.mityc.3";
	/** El certificado firmante no está recogido en los apartados a) ó c) del artículo 18 del Reglamento que está recogido en R.D. 1496/2003 del 28 de Noviembre.. */
	public static final String I18N_POLICY_MITYC_TRUST_4 = "i18n.mityc.policy.mityc.4";

	// Generales
	/** Error en la carga de la configuración del validador de general. */
    public static final String I18N_POLICY_GENERAL_1 = "i18n.mityc.policy.general.1";
    /** Error en la configuración. */
    public static final String I18N_POLICY_GENERAL_2 = "i18n.mityc.policy.general.2";
    /** Identificador de la policy indicada inválido. */
    public static final String I18N_POLICY_GENERAL_3 = "i18n.mityc.policy.general.3";
    /** No hay descripción para esta policy: {0}. */
    public static final String I18N_POLICY_GENERAL_4 = "i18n.mityc.policy.general.4";
    /** Error al indicar número de hash a escribir. */
    public static final String I18N_POLICY_GENERAL_5 = "i18n.mityc.policy.general.5";
    /** No hay indicado hash para el escritor: {0}. */
    public static final String I18N_POLICY_GENERAL_6 = "i18n.mityc.policy.general.6";
    /** No se pudo cargar la configuracion del validador. */
    public static final String I18N_POLICY_GENERAL_7 = "i18n.mityc.policy.general.7";
    /** No hay configuración disponible. */
    public static final String I18N_POLICY_GENERAL_8 = "i18n.mityc.policy.general.8";
    /** No se puede convertir ID de validador en mensaje de identidad. */
    public static final String I18N_POLICY_GENERAL_9 = "i18n.mityc.policy.general.9";
    /** Versión de esquema XAdES no permitido. */
    public static final String I18N_POLICY_GENERAL_10 = "i18n.mityc.policy.general.10";
    /** Huella de la política incorrecta. */
    public static final String I18N_POLICY_GENERAL_11 = "i18n.mityc.policy.general.11";
    /** No se ha podido cargar fichero de configuración de validadores de general. */
    public static final String I18N_POLICY_GENERAL_12 = "i18n.mityc.policy.general.12";
    /** No hay fichero de configuración disponible. */
    public static final String I18N_POLICY_GENERAL_13 = "i18n.mityc.policy.general.13";
    /** No se ha encontrado política. */
    public static final String I18N_POLICY_GENERAL_14 = "i18n.mityc.policy.general.14";
    /** La política encontrada es implícita. */
    public static final String I18N_POLICY_GENERAL_15 = "i18n.mityc.policy.general.15";
    /** Error obteniendo digest/value de la policy. */
    public static final String I18N_POLICY_GENERAL_16 = "i18n.mityc.policy.general.16";
    /** Algoritmo de hash de la policy no soportado. */
    public static final String I18N_POLICY_GENERAL_17 = "i18n.mityc.policy.general.17";
    /** Configuración inadecuada para escribir la policy. */
    public static final String I18N_POLICY_GENERAL_18 = "i18n.mityc.policy.general.18";
    /** Error en la configuración de escritura de la policy. */
    public static final String I18N_POLICY_GENERAL_19 = "i18n.mityc.policy.general.19";
    /** No hay certificados disponibles para comprobar confianza. */
    public static final String I18N_POLICY_GENERAL_20 = "i18n.mityc.policy.general.20";
    /** No hay datos de firma para realizar la comprobación de roles. */
    public static final String I18N_POLICY_GENERAL_21 = "i18n.mityc.policy.general.21";
    /** Rol no se ajusta a uno de los definidos en la política. */
    public static final String I18N_POLICY_GENERAL_22 = "i18n.mityc.policy.general.22";
    /** No hay datos de firma para realizar la comprobación del esquema de firma. */
    public static final String I18N_POLICY_GENERAL_23 = "i18n.mityc.policy.general.23";
    /** Esquema de firma no se ajusta a los definidos en la política. */
    public static final String I18N_POLICY_GENERAL_24 = "i18n.mityc.policy.general.24";
    /** No se dispone de información del esquema para poder comprobar el hash de la política de firma. */
    public static final String I18N_POLICY_GENERAL_25 = "i18n.mityc.policy.general.25";
    /** Error en los nodos XML que definen de política de firma. */
    public static final String I18N_POLICY_GENERAL_26 = "i18n.mityc.policy.general.26";
    /** Hash de política de firma desconocido. */
    public static final String I18N_POLICY_GENERAL_27 = "i18n.mityc.policy.general.27";
    /** Error en los nodos XML que definen de política de firma: {0}. */
    public static final String I18N_POLICY_GENERAL_28 = "i18n.mityc.policy.general.28";
    /** No hay disponible un validador de confianza de entidades admitidas por MITyC. */
    public static final String I18N_POLICY_GENERAL_29 = "i18n.mityc.policy.general.29";
    /** El certificado firmante no se encuentra disponible en el nodo KeyInfo. */
    public static final String I18N_POLICY_GENERAL_30 = "i18n.mityc.policy.general.30";

}
