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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DatosFirma;
import es.mityc.firmaJava.libreria.xades.DatosNodosFirmados;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.DigestAlgAndValueType;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SPURI;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyHash;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyId;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyQualifier;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SigPolicyQualifiers;
import es.mityc.firmaJava.libreria.xades.elementos.xades.SignaturePolicyIdentifier;
import es.mityc.firmaJava.libreria.xades.errores.InvalidInfoNodeException;
import es.mityc.firmaJava.libreria.xades.errores.PolicyException;
import es.mityc.javasign.i18n.I18nFactory;
import es.mityc.javasign.i18n.II18nManager;
import es.mityc.javasign.trust.ConstantsTrust;
import es.mityc.javasign.trust.TrustAbstract;
import es.mityc.javasign.trust.TrustException;
import es.mityc.javasign.trust.TrustFactory;
import es.mityc.javasign.xml.xades.TransformProxy;
import es.mityc.javasign.xml.xades.policy.ConstantsPolicy;
import es.mityc.javasign.xml.xades.policy.IFirmaPolicy;
import es.mityc.javasign.xml.xades.policy.IValidacionPolicy;
import es.mityc.javasign.xml.xades.policy.PoliciesTool;
import es.mityc.javasign.xml.xades.policy.UnknownPolicyException;
import es.mityc.javasign.xml.xades.policy.facturae.ConfigFacturaeException;

/**
 * <p>
 * Implementación de la política de la AGE (disponible en
 * http://administracionelectronica.gob.es/es/ctt/politicafirma).
 * </p>
 * 
 */
public abstract class AgeManager implements IValidacionPolicy, IFirmaPolicy {
	/** Logger. */
	private static final Log LOG = LogFactory.getLog(AgeManager.class);
	/** Internacionalizador. */
	private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsPolicy.LIB_NAME);

	/** Nombre del fichero de configuración. */
	private static final String CONFIG_FILENAME = "age.properties";

	/** Formato de la uri que define la ID de la política. */
	protected static final String URI_ID_POLICY = "text:{0}";

	/** Codificación UTF-8. */
	protected static final String UTF_8 = "UTF-8";

	/*
	 * private static final String MATCH_ROLES = "[eE][mM][iI][sS][oO][rR]" + //
	 * emisor "|[sS][uU][pP][pP][lL][iI][eE][rR]" + // supplier
	 * "|[rR][eE][cC][eE][pP][tT][oO][rR]" + // receptor
	 * "|[cC][uU][sS][tT][oO][mM][eE][rR]" + // customer
	 * "|[tT][eE][rR][cC][eE][rR][oO]" + // tercero
	 * "|[tT][hH][iI][rR][dD] [pP][aA][rR][tT][yY]"; // third party
	 */
	/** Propiedades de configuración de los managers de facturae. */
	private static Properties rb = null;

	/** Validador de confianza de los elementos de la factura electrónica. */
	protected TrustAbstract truster = null;

	static {
		try {
			InputStream is = AgeManager.class.getResourceAsStream(CONFIG_FILENAME);
			if (is != null) {
				rb = new Properties();
				rb.load(is);
			} else {
				LOG.error("No se encontró configuración sobre políticas");
			}
		} catch (IOException ex) {
			LOG.fatal(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_12), ex);
		}
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @throws InstantiationException Lanzada cuando no se tienen los requisitos
	 * necesarios para poder realizar las labores de validación de política
	 */
	protected AgeManager() throws InstantiationException {
		// Carga el validador de emisores de certificados por defecto
		truster = TrustFactory.getInstance().getTruster(ConstantsTrust.KEY_MITYC);
		if (truster == null) {
			throw new InstantiationException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_29));
		}
	}

	/**
	 * <p>
	 * Carga la configuración asociada a un prefijo específico.
	 * </p>
	 * 
	 * @param prefix Prefijo que identifica la configuración buscada
	 * @return Objeto con la configuración cargada
	 * @throws ConfigFacturaeException Lanzada cuando no se puede obtener acceso
	 * a la configuración
	 */
	protected static ConfigAGE loadConfig(final String prefix) throws ConfigAgeException {
		if (rb == null) {
			throw new ConfigAgeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_13));
		}
		return new ConfigAGE(rb, prefix);
	}

	/**
	 * <p>
	 * Devuelve los DigestAlgAndValueType de la configuración que estén
	 * relacionados con el algoritmo indicado.
	 * </p>
	 * 
	 * @param algorithm Algoritmo buscado
	 * @return <code>null</code> si no se encuentra ningún digest asociado al
	 * algoritmo en la configuración
	 */
	private ArrayList<DigestAlgAndValueType> getDigestsRelated(final String algorithm) {
		ArrayList<DigestAlgAndValueType> daavtList = new ArrayList<DigestAlgAndValueType>();
		Iterator<DigestAlgAndValueType> it = getConfig().getHuellas().iterator();
		while (it.hasNext()) {
			DigestAlgAndValueType temp = it.next();
			if (temp.getDigestMethod().getAlgorithm().equals(algorithm)) {
				daavtList.add(temp);
			}
		}
		return daavtList;
	}

	/**
	 * <p>
	 * Comprueba los hashes de la policy del documento de firma.
	 * </p>
	 * 
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de la validación de la firma
	 * @throws PolicyException Lanzada cuando el hash no se ajusta a la de la
	 * policy chequeada
	 */
	protected void checkPolicyHash(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		XAdESSchemas schema = rs.getDatosFirma().getEsquema();
		if (schema == null) {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_25));
		}
		String esquema = schema.getSchemaUri();

		// Nodo SignaturePolicyIdentifier
		NodeList signaturePolicyList = signatureNode.getElementsByTagNameNS(esquema, ConstantesXADES.SIGNATURE_POLICY_IDENTIFIER);
		if (signaturePolicyList.getLength() != 1) {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_26));
		}
		if (signaturePolicyList.item(0).getNodeType() != Node.ELEMENT_NODE) {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_26));
		}

		try {
			SignaturePolicyIdentifier signaturePolicyIdentifier = new SignaturePolicyIdentifier(schema);
			if (!signaturePolicyIdentifier.isThisNode(signaturePolicyList.item(0))) {
				throw new InvalidInfoNodeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_14));
			}
			signaturePolicyIdentifier.load((Element) signaturePolicyList.item(0));

			if (signaturePolicyIdentifier.isImplied()) {
				throw new InvalidInfoNodeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_15));
			}
			ArrayList<DigestAlgAndValueType> values = getDigestsRelated(signaturePolicyIdentifier.getSignaturePolicyId().getSigPolicyHash()
				.getDigestMethod().getAlgorithm());

			if (values != null && values.size() > 0) {
				boolean signaturePolicyIdentifierValid = false;
				for (DigestAlgAndValueType value : values) {
					SignaturePolicyIdentifier policy = createPolicy(schema, value);
					if (signaturePolicyIdentifier.equals(policy)) {
						signaturePolicyIdentifierValid = true;
					}
				}
				if (!signaturePolicyIdentifierValid) {
					throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_27));
				}
			}

		} catch (InvalidInfoNodeException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_16), ex);
			}
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_28, ex.getMessage()));
		}
	}

	/**
	 * Comprueba si la firma contiene: - SigningTime - SigningCertificate -
	 * DataObjectFormat
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Lanzada si la firma no es de tipo enveloped
	 */
	protected void checkNodes(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		//List<DatosNodosFirmados> nodos = rs.getDatosFirma().getDatosNodosFirmados();
		//Element node = null;
		NodeList nl = null;
		boolean hasTime = false;
		boolean hasCert = false;
		boolean hasFormat = false;
		/*
		 * for (DatosNodosFirmados nodo : nodos) { if (nodo.isSignInternal()) {
		 * node = UtilidadTratarNodo.getElementById(signatureNode,
		 * nodo.getId()); if ("SigningTime".equals(node.getLocalName())) {
		 * hasTime = true; } else if
		 * ("SigningCertificate".equals(node.getLocalName())) { hasCert = true;
		 * } else if ("DataObjectFormat".equals(node.getLocalName())) {
		 * hasFormat = true; } } }
		 */
		nl = signatureNode.getElementsByTagName("SigningTime");
		if (nl != null) {
			hasTime = true;
		}
		nl = signatureNode.getElementsByTagName("SigningCertificate");
		if (nl != null) {
			hasCert = true;
		}
		nl = signatureNode.getElementsByTagName("DataObjectFormat");
		if (nl != null) {
			hasFormat = true;
		}
		if (hasTime && hasCert && hasFormat) {
			return;
		}
		throw new PolicyException(I18N.getLocalMessage(ConstantsAgePolicy.I18N_POLICY_AGE_2));
	}

	/**
	 * <p>
	 * Comprueba que el certificado de firma esta dentro del nodo KeyInfo.
	 * </p>
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws UnknownPolicyException lanzada si no se encuentra referencia firmada al KeyInfo o no existe KeyInfo
	 * @throws PolicyException Lanzada cuando el certificado de firma no se
	 * encuentra en el nodo KeyInfo
	 */
	protected void checkCertificateInKeyInfoNode(final Element signatureNode, final ResultadoValidacion rs) throws UnknownPolicyException,
		PolicyException {
		//	que la firma sea enveloped (un reference tiene que ser con uri "", o elemento "Facturae" contener a la firma) y debe tener asociada una transformación adecuada (vía especificación o XPATH)
		List<DatosNodosFirmados> nodos = rs.getDatosFirma().getDatosNodosFirmados();
		for (DatosNodosFirmados nodo : nodos) {
			// Comprueba que el nodo referenciado es un nodo KeyInfo
			String id = nodo.getId();
			if (id != null) {
				Element el = UtilidadTratarNodo.getElementById(signatureNode.getOwnerDocument(), id);
				// Comprueba que el elemento es un KeyInfo
				if ((el != null) && (ConstantesXADES.SCHEMA_DSIG.equals(el.getNamespaceURI()))
					&& (ConstantesXADES.LIBRERIAXADES_KEY_INFO.equals(el.getLocalName()))) {
					// comprueba que el nodo keyinfo es hijo de signatureNode que se trata
					if (el.getParentNode().equals(signatureNode)) {
						// Comprueba que no se le está aplicando ninguna transformada que altere el certificado
						List<TransformProxy> trans = nodo.getTransforms();
						for (TransformProxy transform : trans) {
							if (!TransformProxy.isCanonicalization(transform)) {
								throw new UnknownPolicyException(I18N.getLocalMessage(ConstantsAgePolicy.I18N_POLICY_AGE_4));
							}
						}
						return;
					}
				}
			}
		}
		throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_30));
	}

	/**
	 * <p>
	 * Comprueba que el role de firma (si existe) es aceptado por la policy.
	 * </p>
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la
	 * policy o no se pudo comprobar
	 */
	protected void checkRoles(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		// Comprueba que se ajuste al rol esperado si es una factura
		//TODO Los roles son obligatorios en factura no en normal
		/*
		 * DatosFirma df = rs.getDatosFirma(); if (df != null) {
		 * ArrayList<String> roles = df.getRoles(); if ((roles != null) &&
		 * (roles.size() > 0)) { Iterator<String> it = roles.iterator(); while
		 * (it.hasNext()) { String role = it.next(); if
		 * (!role.matches(MATCH_ROLES)) { throw new
		 * PolicyException(I18N.getLocalMessage
		 * (ConstantsFacturaePolicy.I18N_POLICY_FACTURAE_23)); } } } } else {
		 * throw new
		 * PolicyException(I18N.getLocalMessage(ConstantsFacturaePolicy
		 * .I18N_POLICY_FACTURAE_22)); }
		 */
	}

	/**
	 * <p>
	 * Comprueba los sellos de tiempo de una firma XAdES-XL.
	 * </p>
	 * <p>
	 * Si es una firma XAdES-XL se comprueba lo siguiente:
	 * <ul>
	 * <li>Que los sellos de tiempo sean no posteriores a tres días después de
	 * la firma</li>
	 * <li>Que sean previos a la caducidad del certificado</li>
	 * </ul>
	 * </p>
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la
	 * policy o no se pudo comprobar
	 */
	protected void checkTimestamp(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		//	TODO LARGO: si es firma XL que los sellos de tiempo sean no posteriores a tres días después de la firma y previos a la caducidad del certificado
	}

	/**
	 * Si es una firma XAdES-XL se comprueba que la información OCSP/CRL sea
	 * mínimo 24 horas posterior a la realización de la firma.
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la
	 * policy o no se pudo comprobar
	 */
	protected void checkStatusCertificate(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		// TODO LARGO: si es firma XL que la información OCSP/CRL sea mínimo 24 horas posterior a la realización de la firma
	}

	/**
	 * Se comprueba que el certificado firmante es de confianza.
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la
	 * policy o no se pudo comprobar
	 */
	protected void checkTrustSigningCertificate(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		// chequeo de que el certificado firmante es de confianza
		DatosFirma df = rs.getDatosFirma();
		if (df != null) {
			try {
				truster.isTrusted(df.getCadenaFirma());
			} catch (TrustException ex) {
				throw new PolicyException(I18N.getLocalMessage(ConstantsAgePolicy.I18N_POLICY_AGE_1));
			}
		} else {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_20));
		}
	}

	/**
	 * Se comprueba que la TSA utilizada es de confianza.
	 * @param signatureNode Elemento que es la firma con la política que se
	 * valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la
	 * policy o no se pudo comprobar
	 */
	protected void checkTrustTsa(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException {
		// TODO LARGO: chequeo de que la TSA es de confianza
	}

	/**
	 * <p>
	 * Crea un nodo de política ajustado a la configuración actual.
	 * </p>
	 * @param schema Esquema de la firma XAdES donde irá el nodo de política
	 * @param value Digest que se quiere utilizar en el nodo de política
	 * @return Nodo de política en la forma SignaturePolicyIdentifier
	 * @throws InvalidInfoNodeException Lanzada cuando no se ha podido generar
	 * el nodo de política porque hay algún dato incongruente
	 */
	protected SignaturePolicyIdentifier createPolicy(final XAdESSchemas schema, final DigestAlgAndValueType value) throws InvalidInfoNodeException {
		if (value == null) {
			throw new InvalidInfoNodeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_17));
		}

		ConfigAGE config = getConfig();
		SignaturePolicyIdentifier resultado = new SignaturePolicyIdentifier(schema, false);
		resultado.getSignaturePolicyId().setSigPolicyId(new SigPolicyId(schema, config.getPolicyIdXades(), config.getPolicyDescription()));
		resultado.getSignaturePolicyId().setSigPolicyHash(new SigPolicyHash(schema, value));
		ArrayList<SigPolicyQualifier> lista = new ArrayList<SigPolicyQualifier>();
		SPURI spUri = new SPURI(schema, config.getSpUri());
		spUri.setNamespaceXAdES(value.getNamespaceXAdES());
		lista.add(new SigPolicyQualifier(schema, spUri));
		SigPolicyQualifiers qualifiers = new SigPolicyQualifiers(schema, lista);
		resultado.getSignaturePolicyId().setSigPolicyQualifiers(qualifiers);
		return resultado;
	}

	/**
	 * <p>
	 * Inserta un nodo de política en una firma XAdES (sustituyendo un previo
	 * existente si fuera el caso).
	 * </p>
	 * @param signatureNode Elemento que es la firma a la que se añadirá la
	 * política
	 * @param namespaceDS Namespace de esquema de firma xmlDSig
	 * @param namespaceXAdES Namespace de esquema del XAdES aplicado
	 * @param schema Esquema XAdES aplicado
	 * @throws PolicyException Lanzada cuando no se ha podido crear el nodo de
	 * política
	 * @see es.mityc.javasign.xml.xades.policy.IFirmaPolicy#writePolicyNode(org.w3c.dom.Element,
	 * java.lang.String, java.lang.String,
	 * es.mityc.firmaJava.libreria.xades.XAdESSchemas)
	 */
	public void escribePolicy(final Element signatureNode, final String namespaceDS, final String namespaceXAdES, final XAdESSchemas schema)
		throws PolicyException {
		ConfigAGE config = getConfig();
		// Crea el nodo de política
		SignaturePolicyIdentifier spi;
		try {
			if ((config.getPolicyWriterId() < 0) || (config.getPolicyWriterId() >= config.getHuellas().size())) {
				throw new InvalidInfoNodeException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_18));
			}
			DigestAlgAndValueType hash = config.getHuellas().get(config.getPolicyWriterId());
			hash.setNamespaceXAdES(namespaceXAdES);

			spi = createPolicy(schema, hash);
		} catch (InvalidInfoNodeException ex) {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_GENERAL_19));
		}
		PoliciesTool.insertPolicyNode(signatureNode, namespaceDS, namespaceXAdES, schema, spi);
	}

	/**
	 * <p>
	 * Devuelve un mensaje incluyendo los objetos indicados.
	 * </p>
	 * @param message Mensaje preformateado
	 * @param varargs Objetos a incluir
	 * @return Mensaje formateado
	 */
	protected static String getFormatedMessage(final String message, final Object... varargs) {
		MessageFormat mf = new MessageFormat(message);
		return mf.format(varargs, new StringBuffer(), null).toString();
	}

	/**
	 * <p>
	 * Devuelve la configuración asociada a este validador de políticas.
	 * </p>
	 * @return Configuración asociada
	 */
	protected abstract ConfigAGE getConfig();
}
