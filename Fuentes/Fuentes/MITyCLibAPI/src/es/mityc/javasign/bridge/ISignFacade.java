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
package es.mityc.javasign.bridge;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;

import es.mityc.javasign.pkstore.IPKStoreManager;

/**
 *  <p>Fachada de servicios relacionados con firma electrÃ³nica que ha de implementar el sistema de firma electrÃ³nica utilizado.</p>
 *  
 *  <p>Estos servicios son:
 *  <ul>
 *    <li>Acceso a almacÃ©n de certificados</li>
 *    <li>ValidaciÃ³n de certificado</li>
 *    <li>Firma electrÃ³nica</li>
 *    <li>ValidaciÃ³n de firma electrÃ³nica</li>
 *  </ul>
 *  
 *  <p>La clase que implemente el interfaz deberÃ¡ tener disponible un constructor sin parÃ¡metros para ser instanciado por la factorÃ­a.</p>
 *  
 */
public interface ISignFacade {
	
	/**
	 * <p>Inicializa el facade con propiedades de comportamiento que pueden ser requeridas para su correcto funcionamiento.</p>
	 * 
	 * @param props Conjunto de propiedades necesarias para que el facade puede inicializarse
	 * @throws ConfigurationException lanzada cuando falta algÃºn parÃ¡metro de configuraciÃ³n necesario para el facade
	 */
	void init(Properties props) throws ConfigurationException;
	
	/**
	 * <p>Establece el almacÃ©n de certificados a utilizar. Por ejemplo:
	 *      - STORE_EXPLORER()
	 *      - STORE_MOZILLA(Path2Profile)
	 *      - STORE_MACOSX()
	 *      - STORE_MITYC(StreamConf)
	 *      - STORE_CLASS_NAME(ClassName)
	 * </p>
	 *      
	 * @param store .- Nombre del CSP
	 * @param extraProperty.- Propiedad extra para el almacÃ©n seleccionado
	 * @throws ConfigurationException lanzada cuando falta algÃºn parÃ¡metro de configuraciÃ³n necesario para el facade
	 */
	public void setStoreManager(String store, String extraProperty) throws ConfigurationException;
	
	/**
	 * <p>Recupera el almacÃ©n de certigicados configurado.</p>
	 * 
	 * @return AlmacÃ©n de certificados
	 * @throws ConfigurationException lanzada cuando no se ha especificado el almacÃ©n previamente (setStoreManager())
	 */
	public IPKStoreManager getStoreManager() throws ConfigurationException;
	
	/**
	 * <p>Consigue la lista de certificados para firmar disponibles en el almacÃ©n de certificados.</p>
	 * 
	 * @return Lista de certificados obtenida
	 */
	List<X509Certificate> getSignCertificates();
	
	/**
	 * <p>Comprueba la validez del certificado indicado.</p>
	 * 
	 * @param cert Certificado a validar
	 * @throws InvalidCertificateException lanzada cuando el certificado es invalido, desconocido o se ha tenido alguna dificultad en
	 * 	la validaciÃ³n 
	 */
	void validateCert(X509Certificate cert) throws InvalidCertificateException;
	
	/**
	 * <p>Comprueba la validez de la cadena de certificados indicada.</p>
	 * 
	 * @param cert Certificado a validar
	 * @throws InvalidCertificateException lanzada cuando la cadena es no valida, desconocida o se ha tenido alguna dificultad en
	 * 	la validaciÃ³n 
	 */
	void validateCertChain(X509Certificate cert) throws InvalidCertificateException;
	
	/**
	 * <p>Firma el documento XML indicado utilizando el certificado.</p>
	 * 
	 * @param cert Certificado con el que realizar la firma
	 * @param doc documento con el que realizar la firma
	 * @return devuelve el documento con la firma incluida
	 * @throws SigningException lanzada cuando se produce un error al intentar realizar la firma.
	 */
	Document sign(X509Certificate cert, Document doc) throws SigningException;
	
	/**
	 * <p>Valida una firma XML.</p>
	 * 
	 * @param doc Documento XML con la firma a validar
	 * @return Lista de mapas con un conjunto de informaciÃ³n sobre la/s firma/s
	 * @throws InvalidSignatureException lanzada cuando la firma es invÃ¡lida
	 */
	List<Map<String, Object>> validate(Document doc) throws InvalidSignatureException;

}
