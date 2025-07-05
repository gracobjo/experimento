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

import java.net.URI;
import java.net.URISyntaxException;

import org.w3c.dom.Element;

import es.mityc.firmaJava.libreria.xades.DatosFirma;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.errores.PolicyException;
import es.mityc.javasign.i18n.I18nFactory;
import es.mityc.javasign.i18n.II18nManager;
import es.mityc.javasign.trust.ConstantsTrust;
import es.mityc.javasign.trust.TrustAbstract;
import es.mityc.javasign.trust.TrustException;
import es.mityc.javasign.trust.TrustFactory;


/**
 * <p>Política de seguridad ante posibles manipulaciones del contenido firmado.</p>
 * <p>Esta política es altamente restrictiva forzando a que la firma no pueda tener aplicada una transformada que pueda conducir a la manipulación
 * del contenido de lo firmado.</p>
 * 
 */
public class MITyCTrustPolicy implements IValidacionPolicy {
	/** Internacionalizador. */
	private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsPolicy.LIB_NAME);
	
	/** URI identificativa de la política. */
	private static final String POLICY_URI = "self:policy/mityc/trust";

	/** Validador de confianza de los elementos. */
	protected TrustAbstract truster;
	
	/**
	 * <p>Constructor.</p>
	 * @throws InstantiationException Lanzada cuando no se tienen los requisitos necesarios para poder realizar las labores de validación de política
	 */
	public MITyCTrustPolicy() throws InstantiationException {
		// Carga el validador de emisores de certificados
		truster = TrustFactory.getInstance().getTruster(ConstantsTrust.KEY_MITYC);
		if (truster == null) {
			throw new InstantiationException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_MITYC_TRUST_1));
		}
	}

	/**
	 * <p>Devuelve una cadena legible que identifica la política.</p>
	 * @return cadena descriptiva
	 * @see es.mityc.javasign.xml.xades.policy.IValidacionPolicy#getIdentidadPolicy()
	 */
	public String getIdentidadPolicy() {
		return I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_MITYC_TRUST_2);
	}

	/**
	 * <p>Devuelve el resultado de la comprobación de las trasformadas.</p>
	 * @param nodoFirma firma a comprobar
	 * @param resultadoValidacion Resultado de validación de la firma
	 * @return resultado de la validación de la política
	 * @see es.mityc.javasign.xml.xades.policy.IValidacionPolicy#validaPolicy(org.w3c.dom.Element, es.mityc.firmaJava.libreria.xades.ResultadoValidacion)
	 */
	public PolicyResult validaPolicy(final Element nodoFirma, final ResultadoValidacion resultadoValidacion) {
		PolicyResult pr = new PolicyResult();
		try {
			// establece la ID de la política
			pr.setPolicyID(new URI(POLICY_URI));
			checkTrustSigningCertificate(nodoFirma, resultadoValidacion);
			pr.setResult(PolicyResult.StatusValidation.valid);
		} catch (PolicyException ex) {
			pr.setResult(PolicyResult.StatusValidation.invalid);
			pr.setDescriptionResult(ex.getMessage());
		} catch (URISyntaxException ex) {
			pr.setResult(PolicyResult.StatusValidation.unknown);
			pr.setDescriptionResult(ex.getMessage());
		}
		
		return pr;
	}
	
	/**
	 * <p>Se comprueba que el certificado firmante es de confianza.</p>
	 * @param signatureNode Elemento que es la firma con la política que se valida
	 * @param rs Resultado de validación de la firma
	 * @throws PolicyException Si la informacion obtenida no corresponde con la policy o no se pudo comprobar
	 */
	protected void checkTrustSigningCertificate(final Element signatureNode, final ResultadoValidacion rs) throws PolicyException  {
		// chequeo de que el certificado firmante es de confianza
		DatosFirma df = rs.getDatosFirma();
		if (df != null) {
			try {
				truster.isTrusted(df.getCadenaFirma());
			} catch (TrustException ex) {
				throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_MITYC_TRUST_3));
			}
		}
		else {
			throw new PolicyException(I18N.getLocalMessage(ConstantsPolicy.I18N_POLICY_MITYC_TRUST_4));
		}
	}

	/**
	 * <p>Establece el administrador de confianza a emplear.</p>
	 */
	public void setTruster(TrustAbstract truster) {
		this.truster = truster;
	}
}
