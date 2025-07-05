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
 * <p>Implementación de la política de la AGE v 1.8 (disponible en 
 * http://administracionelectronica.gob.es/es/ctt/politicafirma).</p>
 * 
 */
public class Age18Manager extends AgeManager {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(Age18Manager.class);
	/** Internacionalizador. */
	private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsPolicy.LIB_NAME);

	/** Prefijo de las propiedades que afectan al manager que gestiona las políticas de AGE. */
	private static final String PREFIX_POLICY_PROP = "age18";
	
	/** Configuración asociada al manager. */
	private static ConfigAGE config = null;
	
	static {
		try {
			config = AgeManager.loadConfig(PREFIX_POLICY_PROP);
		} catch (ConfigAgeException ex) {
			LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_7), ex);
		}
	}
	
	/** 
	 * Constructor.
	 * @throws InstantiationException Lanzada cuando no se puede instanciar
	 */
	public Age18Manager() throws InstantiationException {
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
	protected ConfigAGE getConfig() {
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
	 * @param nodoFirma Nodo que se corresponde con la firma que tiene la política
	 * @param resultadoValidacion resultado de la validación de la firma
	 * @return Resultado de la validación de la política
	 */
	public PolicyResult validaPolicy(final Element nodoFirma, final ResultadoValidacion resultadoValidacion) {
		PolicyResult pr = new PolicyResult();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Validando política de AGE");
		}
		try {
			URI id = config.getPolicyIdXades();
			
			try {
				id = new URI(getFormatedMessage(PREFIX_POLICY_PROP, URIEncoder.encode(config.getPolicyIdValidador(), UTF_8)));
			} catch (URISyntaxException ex) {
				LOG.warn(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_9), ex);
			} catch (UnsupportedEncodingException ex) {
				LOG.warn(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_9), ex);
			}
			
			pr.setPolicyID(id);

			// incluye URI de descarga de la política de AGE
			pr.setDownloable(new PolicyResult.DownloadPolicy[] { pr.newDownloadPolicy(config.getSpUri(), PolicyResult.StatusValidation.unknown) });
			
			checkSchema(nodoFirma, resultadoValidacion);
			
			checkPolicyHash(nodoFirma, resultadoValidacion);
			
			checkNodes(nodoFirma, resultadoValidacion);
			
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
	 * <p>En el caso de AGE 1.8 esto implica versión XAdES distinta a 1.1.1 y XMLDSig</p>
	 * 
	 * @param nodoFirma Nodo donde está la firma en la que se comprueba la política
	 * @param rs Resultado de la validación de esta firma
	 * @throws PolicyException si es el esquema de la firma no es el esperado
	 */
	protected void checkSchema(final Element nodoFirma, final ResultadoValidacion rs) throws PolicyException {
		DatosFirma df = rs.getDatosFirma();
		if (df != null) {
			XAdESSchemas schema = df.getEsquema();
			if (XAdESSchemas.XAdES_111.equals(schema) || XAdESSchemas.XMLDSIG.equals(schema)) {
				throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_24));
			}
		}
		else {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_23));
		}
	}
	
	/**
	 * <p>Escribe el nodo de la política de AGE 1.8.</p>
	 * @param signNode Nodo de firma
	 * @param namespaceDS Namespace de Digital signature
	 * @param namespaceXAdES Namespace de XAdES
	 * @param schema Versión de schema XAdES
	 * @throws PolicyException Lanzada cuando no se puede escribir el nodo de la política
	 * @see es.mityc.javasign.xml.xades.policy.IFirmaPolicy#writePolicyNode(org.w3c.dom.Element, java.lang.String, java.lang.String, es.mityc.firmaJava.libreria.xades.XAdESSchemas)
	 */
	public void writePolicyNode(final Element signNode, final String namespaceDS, final String namespaceXAdES, final XAdESSchemas schema) throws PolicyException {
		escribePolicy(signNode, namespaceDS, namespaceXAdES, schema);
	}
	
	/**
	 * <p>Establece el administrador de confianza a emplear.</p>
	 */
	public void setTruster(TrustAbstract truster) {
		if (truster != null)
			this.truster = truster;
	}
}
