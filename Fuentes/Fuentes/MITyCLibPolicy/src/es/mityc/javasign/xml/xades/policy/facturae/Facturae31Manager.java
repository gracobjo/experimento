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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import es.mityc.firmaJava.libreria.utilidades.URIEncoder;
import es.mityc.firmaJava.libreria.xades.DatosFirma;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.errores.PolicyException;
import es.mityc.javasign.i18n.I18nFactory;
import es.mityc.javasign.i18n.II18nManager;
import es.mityc.javasign.trust.TrustAbstract;
import es.mityc.javasign.xml.xades.policy.ConstantsPolicy;
import es.mityc.javasign.xml.xades.policy.PolicyResult;
import es.mityc.javasign.xml.xades.policy.UnknownPolicyException;

/**
 * <p>Implementación de la política de factura electrónica v 3.1 (disponible en
 * http://www.facturae.es/politica de firma formato facturae/politica de firma formato facturae v3_1.pdf).</p>
 * 
 */
public class Facturae31Manager extends FacturaeManager {
	/** Logger. */
	private static final Log LOG = LogFactory.getLog(Facturae31Manager.class);
	/** Internacionalizador. */
	private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsPolicy.LIB_NAME);

	/** Prefijo de las propiedades que afectan al manager que gestiona las políticas de FacturaE v3.1. */
	private static final String PREFIX_POLICY_PROP = "facturae31";
	/** Configuración asociada al manager. */
	private static ConfigFacturae config = null;
	
	static {
		try {
			config = FacturaeManager.loadConfig(PREFIX_POLICY_PROP);
		} catch (ConfigFacturaeException ex) {
			LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_7), ex);
		}
	}
	
	/** 
	 * Constructor.
	 * @throws InstantiationException Lanzada cuando no se puede instanciar
	 */
	public Facturae31Manager() throws InstantiationException {
		super();
		if (config == null) {
			throw new InstantiationException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_8));
		}
	}

	/**
	 * <p>Devuelve la configuración específica de este validador de política.</p>
	 * @return configuración asociada
	 * @see es.mityc.javasign.xml.xades.policy.facturae.FacturaeManager#getConfig()
	 */
	@Override
	protected ConfigFacturae getConfig() {
		return config;
	}

	/**
	 * <p>Devueve una cadena identificativa del manager.</p>
	 * <p>En este caso la cadena se obtiene a través de la configuración de este manager.</p> 
	 * @return cadena identificativa
	 * @see es.mityc.javasign.xml.xades.policy.IValidacionPolicy#getIdentidadPolicy()
	 */
	public String getIdentidadPolicy() {
		return config.getPolicyIdValidador();
	}

	/**
	 * <p>Valida que se cumpla la policy especificada en el documento (disponible en 
	 * http://www.facturae.es/politica de firma formato facturae/politica de firma formato facturae v3_1.pdf).</p>
	 * 
	 * <p>Detalles para firma básica:<ul>
	 *   <li>La firma ha de ser enveloped</li>
	 *   <li>El certificado firmante ha de estar en un elemento KeyInfo</li>
	 *   <li>El rol debe estar vacío, o ser de tipo ClaimedRol y contener uno de los siguientes valores:<ul>
	 *     <li>"emisor" o "supplier" si la firma la realiza el emisor</li>
	 *     <li>"receptor" o "customer" si la firma la realiza el receptor</li>
	 *     <li>"tercero" o "third party" si la firma la realiza una persona o entidad distinta al emisor o receptor de la factura</li>
	 *     Este validador sólo comprobará que la semántica de los nombres es la correcta.
	 *   </ul></li>
	 *   <li>Que haya un elemento SignaturePolicyIdentifier (XADES-EPES) con los valores:<ul>
	 *     <li>&lt;xades:SigPolicyId&gt;&lt;xades:Identifier&gt;http://www.facturae.es/politica de firma formato facturae/politica de firma formato facturae v3_0.pdf&lt;/xades:Identifier&gt;&lt;/xades:SigPolicyId&gt;</li>
	 *     <li>la huella digital del documento de policy en un elemento &lt;xades:SigPolicyHash&gt;
	 *   </ul></li>
	 *   <li>Si la firma es <i>menor</i> a XADES-C (no contiene información de validación) validar el certificado firmante. Este validador no implementa esta característica</li>
	 * </ul></p>
	 * Detalles para firma avanzada (XADES-XL):<ul>
	 *   <li>El sello de tiempo debe estar a menos de tres días de la fecha del campo xades:SigningTime y no puede superar a la fecha de caducidad del certificado firmante</li>
	 *   <li>La información del estado del certificado firmante ha de ser posterior a 24 después de la fecha indicada en SigningTime</li>
	 *   <li>La ruta de certificación ha de ser completa</li>
	 * </ul>
	 * <p>Certificados electrónicos:<ul>
	 *   <li>Los certificados han de cumplir lo indicado en los apartados a) ó c) del artículo 18 del Reglamento que está recogido en R.D. 1496/2003 del 28 de Noviembre.</li>
	 * </ul></p>
	 * <p>Sellos de tiempo:<ul>
	 *   <li>Se admiten los sellos de tiempo expedidos por aquellas Autoridades de Sellado de Tiempo que cumplan con la norma ETSI TS 102 023 "Policy requirements for time-stamping authorities".
	 *       Este validador no implementa esta comprobación.</li>
	 * </ul></p>
	 * 
	 * @param nodoFirma Nodo que se corresponde con la firma que tiene la política
	 * @param resultadoValidacion resultado de la validación de la firma
	 * @return Resultado de la validación de la política
	 */
	public PolicyResult validaPolicy(final Element nodoFirma, final ResultadoValidacion resultadoValidacion) {
		PolicyResult pr = new PolicyResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Validando política de factura 3.1");
		}
		try {
			URI id = config.getPolicyIdXades();
			
			try {
				id = new URI(getFormatedMessage(URI_ID_POLICY, URIEncoder.encode(config.getPolicyIdValidador(), UTF_8)));
			} catch (URISyntaxException ex) {
				LOG.warn(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_9), ex);
			} catch (UnsupportedEncodingException ex) {
				LOG.warn(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_9), ex);
			}
			
			pr.setPolicyID(id);
			
			// incluye URI de descargar de la política de facturae 3.1
			pr.setDownloable(new PolicyResult.DownloadPolicy[] { pr.newDownloadPolicy(config.getPolicyIdXades(), PolicyResult.StatusValidation.unknown) });

			checkSchema(nodoFirma, resultadoValidacion);
			
			checkPolicyHash(nodoFirma, resultadoValidacion);
	
			checkEnveloped(nodoFirma, resultadoValidacion);
			
			checkCertificateInKeyInfoNode(nodoFirma, resultadoValidacion);
			
			checkRoles(nodoFirma, resultadoValidacion);
			
			checkTimestamp(nodoFirma, resultadoValidacion);
			
			checkStatusCertificate(nodoFirma, resultadoValidacion);
			
			checkTrustSigningCertificate(nodoFirma, resultadoValidacion);
			
			checkTrustTsa(nodoFirma, resultadoValidacion);
			
			pr.setResult(PolicyResult.StatusValidation.valid);
		} catch (UnknownPolicyException ex) {
			pr.setResult(PolicyResult.StatusValidation.unknown);
			pr.setDescriptionResult(ex.getMessage());
		} catch (PolicyException ex) {
			pr.setResult(PolicyResult.StatusValidation.invalid);
			pr.setDescriptionResult(ex.getMessage());
		}
		
		return pr;
	}
	
	/**
	 * <p>Comprueba que el esquema de la firma es el esperado.</p>
	 * <p>En el caso de FacturaE 3.1 esto implica versión XAdES 1.2.2 o superior. Esta validador comprueba que la versión de esquema sea 1.2.2 ó 1.3.2.</p>
	 * 
	 * @param nodoFirma Elemento que es la firma con la política que se valida
	 * @param rs Resultado de la validación de la firma
	 * @throws PolicyException si es el esquema de la firma no es el esperado
	 */
	protected void checkSchema(final Element nodoFirma, final ResultadoValidacion rs) throws PolicyException {
		DatosFirma df = rs.getDatosFirma();
		if (df != null) {
			XAdESSchemas schema = df.getEsquema();
			if    (!XAdESSchemas.XAdES_122.equals(schema) 
				&& !XAdESSchemas.XAdES_132.equals(schema)) {
				throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_24));
			}
		}
		else {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_23));
		}
	}
	
	/**
	 * <p>Escribe el nodo de la política de facturae 3.1.</p>
	 * @param nodoFirma Nodo de firma
	 * @param namespaceDS Namespace de Digital signature
	 * @param namespaceXAdES Namespace de XAdES
	 * @param schema Versión de schema XAdES
	 * @throws PolicyException Lanzada cuando no se puede escribir el nodo de la política
	 * @see es.mityc.javasign.xml.xades.policy.IFirmaPolicy#writePolicyNode(org.w3c.dom.Element, java.lang.String, java.lang.String, es.mityc.firmaJava.libreria.xades.XAdESSchemas)
	 */
	public void writePolicyNode(final Element nodoFirma, final String namespaceDS, final String namespaceXAdES, final XAdESSchemas schema) throws PolicyException {
		escribePolicy(nodoFirma, namespaceDS, namespaceXAdES, schema);
	}

	/**
	 * <p>Establece el administrador de confianza a emplear.</p>
	 */
	public void setTruster(TrustAbstract truster) {
		if (truster != null)
			super.truster = truster;
	}
}
