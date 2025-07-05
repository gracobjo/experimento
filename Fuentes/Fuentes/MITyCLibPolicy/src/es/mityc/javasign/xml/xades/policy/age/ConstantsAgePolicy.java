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

/** 
 * Constantes del módulo políticas de AGE.
 * 
 */
public final class ConstantsAgePolicy {

    /**
     * Constructor.
     */
    private ConstantsAgePolicy() { };
    
    // Contantes de internacionalización

    /** El certificado firmante no está aceptado */
    public static final String I18N_POLICY_AGE_1 = "i18n.mityc.policy.age.1";
    /** La firma tiene un formato incorrecto. */
    public static final String I18N_POLICY_AGE_2 = "i18n.mityc.policy.age.2";
    /** La transformada produce una estructura que se desconoce si es una enveloped aceptada. */
    public static final String I18N_POLICY_AGE_3 = "i18n.mityc.policy.age.3";
    /** La transformada produce cambios en el nodo de certificado de firma que se desconoce si altera su contenido. */
    public static final String I18N_POLICY_AGE_4 = "i18n.mityc.policy.age.4";

}
