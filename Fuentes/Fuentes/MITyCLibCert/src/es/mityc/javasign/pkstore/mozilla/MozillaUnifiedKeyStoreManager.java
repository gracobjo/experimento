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
package es.mityc.javasign.pkstore.mozilla;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.security.auth.callback.PasswordCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.jss.util.Password;
import org.mozilla.jss.util.PasswordCallback.GiveUpException;
import org.mozilla.jss.util.PasswordCallbackInfo;

import es.mityc.javasign.i18n.I18nFactory;
import es.mityc.javasign.i18n.II18nManager;
import es.mityc.javasign.pkstore.CertStoreException;
import es.mityc.javasign.pkstore.ConstantsCert;
import es.mityc.javasign.pkstore.IPKStoreManager;
import es.mityc.javasign.pkstore.keystore.KSStore;
import es.mityc.javasign.pkstore.mozilla.IPINDialogConfigurable.MESSAGES_MODE;
import es.mityc.javasign.pkstore.mozilla.unified.MozillaKeyStoreUtilities;
import es.mityc.javasign.pkstore.mozilla.unified.MozillaKeyStoreUtilitiesWindows;

/** Representa a un <i>AOKeyStoreManager</i> para acceso a almacenes de claves de
 * Firefox accedidos v&iacute;a NSS en el que se tratan de forma
 * unificada los m&oacute;dulos internos y externos. */
public final class MozillaUnifiedKeyStoreManager implements IPKStoreManager {

    /** Logger. */
    private static final Log LOG = LogFactory.getLog(MozillaUnifiedKeyStoreManager.class);
    /** Internacionalizador. */
    private static final II18nManager I18N = I18nFactory.getI18nManager(ConstantsCert.LIB_NAME);

    private static Provider nssProvider = null;

	private Map<String, KeyStore> storesByAlias;

	private final List<KeyStore> kss = new ArrayList<KeyStore>();
	
    /** Nombre de los ficheros de biblioteca de los controladores de la FNMT para DNIe y CERES
     * que no tienen implementados el algoritmo SHA1withRSA.
     */
    private static final String[] FNMT_PKCS11_LIBS_WITHOUT_SHA1 = {
    	"DNIe_P11_priv.dll", //$NON-NLS-1$
    	"DNIe_P11_pub.dll", //$NON-NLS-1$
    	"FNMT_P11.dll", //$NON-NLS-1$
    	"UsrPkcs11.dll", //$NON-NLS-1$
    	"UsrPubPkcs11.dll" //$NON-NLS-1$
    };
	

	/** PasswordCallback establecido de forma externa para el acceso al
	 * almac&eacute;n. */
	private PasswordCallback externallPasswordCallback = null;

	public MozillaUnifiedKeyStoreManager() {
        try {
            init(null);
        }
        catch (final Exception e) {
            LOG.error("No se ha podido inicializar el almacen: " + e, e); //$NON-NLS-1$
        }
	}

	/** Inicializa la clase gestora de almacenes de claves.
	 * @return Almac&eacute;n de claves de Firefox correspondiente
	 *         &uacute;nicamente el m&oacute;dulo interno principal
	 * @throws AOKeyStoreManagerException
	 *         Si no puede inicializarse ning&uacute;n almac&eacute;n de
	 *         claves, ni el NSS interno, ni ning&uacute;n PKCS#11 externo
	 *         definido en SecMod */
	public List<KeyStore> init(final PasswordCallback pssCallBack) throws CertStoreException {

		// Se ha detectado que en algunas versiones de Java/OpenJDK, al solicitar un proveedor
		// de seguridad comprobar su existencia, puede afectar negativamente a que este proveedor
		// se cargue en un futuro, asi que guardamos una copia local del proveedor para hacer
		// estas comprobaciones
		LOG.debug("Llamando a getNssProvider");
		final Provider p = getNssProvider();
		LOG.debug("Provider obtenido");
		Enumeration<String> tmpAlias = new Vector<String>(0).elements();
		this.storesByAlias = new Hashtable<String, KeyStore>();

		KeyStore keyStore = null;

		if (p != null) {
			try {
				keyStore = KeyStore.getInstance("PKCS11", p); //$NON-NLS-1$
			}
			catch (final Exception e) {
				LOG.warn("No se ha podido obtener el KeyStore PKCS#11 NSS del proveedor SunPKCS11, se continuara con los almacenes externos: " + e); //$NON-NLS-1$
				keyStore = null;
			}
		}
		if (keyStore != null) {
			try {
				keyStore.load(null, new char[0]);
			}
			catch (final Exception e) {
				try {
				    char[] passwd = new char[0];
				    if(this.externallPasswordCallback != null) {
				        passwd = this.externallPasswordCallback.getPassword();
				    } else {
				        org.mozilla.jss.util.PasswordCallback passDialog = MozillaStoreUtils.getPassHandler(MESSAGES_MODE.AUTO_TOKEN, null, I18N.getLocalMessage(ConstantsCert.I18N_CERT_MOZILLA_8));
				        Password pass = passDialog.getPasswordFirstAttempt(new PasswordCallbackInfo("Firefox", 1));
				        passwd = pass.getChars();
				    }
					keyStore.load(null, passwd);
				}
				catch (final Exception e2) {
					LOG.warn("No se ha podido abrir el almacen PKCS#11 NSS del proveedor SunPKCS11, se continuara con los almacenes externos: " + e2); //$NON-NLS-1$
					keyStore = null;
				}
			}

			if (keyStore != null) {
				try {
					tmpAlias = keyStore.aliases();
				}
				catch (final Exception e) {
					LOG.warn("El almacen interno de Firefox no devolvio certificados, se continuara con los externos: " + e); //$NON-NLS-1$
					keyStore = null;
				}
				while (tmpAlias.hasMoreElements()) {
					this.storesByAlias.put(tmpAlias.nextElement().toString(), keyStore);
				}
			}
		}

		if (keyStore != null) {
			this.kss.add(keyStore);
		}

		// Vamos ahora con los almacenes externos
		final Map<String, String> externalStores = MozillaKeyStoreUtilities.getMozillaPKCS11Modules(true,true);

		if (externalStores.size() > 0) {
			final StringBuilder logStr = new StringBuilder("Encontrados los siguientes modulos PKCS#11 externos instalados en Mozilla / Firefox: "); //$NON-NLS-1$
			for (final String key : externalStores.keySet()) {
				logStr.append("'"); //$NON-NLS-1$
				logStr.append(externalStores.get(key));
				logStr.append("' "); //$NON-NLS-1$
			}
			LOG.info(logStr.toString());
		}
		else {
			LOG.info("No se han encontrado modulos PKCS#11 externos instalados en Firefox"); //$NON-NLS-1$
		}

		KeyStore tmpStore = null;
		for (final String descr : externalStores.keySet()) {
			try {
				tmpStore = initPkcs11(MozillaStoreUtils.getPassHandler(MESSAGES_MODE.AUTO_TOKEN, null, I18N.getLocalMessage(ConstantsCert.I18N_CERT_MOZILLA_8)),
						new String[] {
							externalStores.get(descr), descr.toString()
						}).get(0);
			}
			catch (final Exception ex) {
				LOG.error("No se ha podido inicializar el PKCS#11 '" + descr + "': " + ex); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}

			LOG.info("El almacen externo '" + descr + "' ha podido inicializarse, se anadiran sus entradas"); //$NON-NLS-1$ //$NON-NLS-2$

			if (keyStore == null) {
				keyStore = tmpStore;
			}

			tmpAlias = new Vector<String>(0).elements();
			try {
				tmpAlias = tmpStore.aliases();
			}
			catch (final Exception ex) {
				LOG.warn("Se encontro un error obteniendo los alias del almacen externo '" + descr + "', se continuara con el siguiente: " + ex); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}
			String alias;
			while (tmpAlias.hasMoreElements()) {
				alias = tmpAlias.nextElement().toString();
				this.storesByAlias.put(alias, tmpStore);
				LOG.info("Anadida la entrada '" + alias + "' del almacen externo '" + descr + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			this.kss.add(tmpStore);
		}

		if (this.kss.isEmpty()) {
			throw new CertStoreException("No se ha podido inicializar ningun almacen, interno o externo, de Firefox"); //$NON-NLS-1$
		}

		return this.kss;
	}

	/** Establece la interfaz de entrada de la contrase&ntilde;a del
	 * almac&eacute;n interno de Firefox. Si no se indica o se establece a <code>null</code> se utilizar&aacute; el por defecto.
	 * @param externallPC Interfaz de entrada de contrase&ntilde;a. */
	public void setPasswordCallback(final PasswordCallback externallPC) {
		this.externallPasswordCallback = externallPC;
	}

	/** {@inheritDoc} */
	public String[] getAliases() {
		if (this.kss.isEmpty()) {
			LOG.warn("Se han pedido los alias de un almacen sin inicializar, se intentara inicializar primero"); //$NON-NLS-1$
			try {
				init(null);
			}
			catch (final Exception e) {
				LOG.error("No se ha podido inicializar el almacen, se devolvera una lista de alias vacia: " + e); //$NON-NLS-1$
				return new String[0];
			}
		}
		final String[] tmpAlias = new String[this.storesByAlias.size()];
		int i = 0;
		for (final String al : this.storesByAlias.keySet()) {
			tmpAlias[i] = al;
			i++;
		}
		return tmpAlias.clone();
	}

	/** {@inheritDoc} */
	public X509Certificate[] getCertificateChain(final String alias) {
		final PrivateKeyEntry key = this.getKeyEntry(alias, this.externallPasswordCallback);
		return (key != null) ? (X509Certificate[]) key.getCertificateChain() : null;
	}

	/** {@inheritDoc} */
	public KeyStore.PrivateKeyEntry getKeyEntry(final String alias, final PasswordCallback pssCallback) {

		final KeyStore tmpStore = this.storesByAlias.get(alias);
		if (tmpStore == null) {
			LOG.warn("No hay ningun almacen de Firefox que contenga un certificado con el alias '" + alias + "', se devolvera null"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		final KeyStore.PrivateKeyEntry keyEntry;
		try {
			keyEntry = (KeyStore.PrivateKeyEntry) tmpStore.getEntry(alias, new KeyStore.PasswordProtection((pssCallback != null) ? pssCallback.getPassword() : null));
		}
		catch (final KeyStoreException e) {
			LOG.error("Error al acceder al almacen para obtener la clave privada del certicado '" + alias + "', se devolvera null: " + e); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		catch (final NoSuchAlgorithmException e) {
			LOG.error("No se soporta el algoritmo de la clave privada del certicado '" + alias + "', se devolvera null: " + e); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		catch (final UnrecoverableEntryException e) {
			LOG.error("No se ha podido obtener la clave privada del certicado '" + alias + "', se devolvera null: " + e); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		return keyEntry;
	}

	/** {@inheritDoc} */
	public List<KeyStore> getKeyStores() {
		return this.kss;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Almacen de claves de tipo Firefox unificado"; //$NON-NLS-1$
	}

	/** Obtiene un certificado del keystore activo a partir de su alias.
	 * @param alias
	 *        Alias del certificado.
	 * @return Certificado. */
	public X509Certificate getCertificate(final String alias) {
		if (this.kss.isEmpty()) {
			LOG.warn("El KeyStore actual no esta inicializado, por lo que no se pudo recuperar el certificado '" + alias + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			return null;
		}
		for (final KeyStore keyStore : this.kss) {
			try {
				if (keyStore.containsAlias(alias)) {
					return (X509Certificate) keyStore.getCertificate(alias);
				}
			}
			catch (final Exception e) {
				LOG.info("El KeyStore '" + keyStore + "' no contenia o no pudo recuperar el certificado '" + alias + "', se probara con el siguiente: " + e); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}
		LOG.warn("Ningun KeyStore de Firefox contenia el certificado '" + alias + "', se devolvera null"); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

	/** Carga e instala el proveedor de seguridad para el acceso al almac&eacute;n de NSS. Si
	 * ya estaba cargado, lo recupera directamente.
	 * @return Proveedor para el acceso a NSS. */
	private static Provider getNssProvider() {

		if (nssProvider != null) {
			return nssProvider;
		}

		try {
			nssProvider = MozillaKeyStoreUtilities.loadNSS();
			LOG.debug(nssProvider.getName());
		}
		catch (final Exception e) {
			LOG.error("Error inicializando el proveedor NSS: " + e,e); //$NON-NLS-1$
			nssProvider = null;
		}

		return nssProvider;
	}

    @Override
    public Provider getProvider(X509Certificate cert) {
    	//todo: ver con que provider se obtuvo ese certificado
		LOG.debug("Buscando el provider del certificado" ); //$NON-NLS-1$
    	try
    	{
    		String alias = getCertificateAlias(cert);
    		if ( alias != null ){
    			Provider p = storesByAlias.get(alias).getProvider();
				LOG.debug("Devolviendo provider "+p.getName()); //$NON-NLS-1$
				return p;
    		}
    		else {
    			return getNssProvider();
    		}
    	}
    	catch ( Exception e){
    		return getNssProvider();
    	}
    }
    
    public String getCertificateAlias(X509Certificate cert) {
    	//todo: ver con que provider se obtuvo ese certificado
		LOG.debug("Buscando el alias del certificado" ); //$NON-NLS-1$
    	try
    	{
	    	String [] listaAlias = getAliases();
    		for ( int i = 0 ; i < listaAlias.length; i++ ) {
    			String alias = listaAlias[i];
	    		LOG.debug("Alias actual:"+listaAlias[i]); //$NON-NLS-1$
    			X509Certificate currentCert = getCertificate(alias);
    			if ( currentCert.equals(cert)){
    				LOG.debug("Encontrado cert" ); //$NON-NLS-1$
    				return alias;
    			}
    		}
    		return null;
    	}
    	catch ( Exception e){
    		return null;
    	}
    }    

    @Override
    public List<X509Certificate> getSignCertificates() throws CertStoreException {
        List<X509Certificate> ret = new ArrayList<X509Certificate>();
        if (this.kss.isEmpty()) {
            LOG.warn("Se han pedido los alias de un almacen sin inicializar, se intentara inicializar primero"); //$NON-NLS-1$
            try {
                init(null);
            }
            catch (final Exception e) {
                LOG.error("No se ha podido inicializar el almacen, se devolvera una lista de alias vacia: " + e, e); //$NON-NLS-1$
                return ret;
            }
        }
        for(KeyStore current: this.kss) {
            IPKStoreManager pkStore = new KSStore(current, null, "".toCharArray());
            ret.addAll(pkStore.getSignCertificates());
        }
        return ret;
    }

    @Override
    public List<X509Certificate> getTrustCertificates() throws CertStoreException {
        List<X509Certificate> ret = new ArrayList<X509Certificate>();
        if (this.kss.isEmpty()) {
            LOG.warn("Se han pedido los alias de un almacen sin inicializar, se intentara inicializar primero"); //$NON-NLS-1$
            try {
                init(null);
            }
            catch (final Exception e) {
                LOG.error("No se ha podido inicializar el almacen, se devolvera una lista de alias vacia: " + e); //$NON-NLS-1$
                return ret;
            }
        }
        for(KeyStore current: this.kss) {
            IPKStoreManager pkStore = new KSStore(current, null, "".toCharArray());
            ret.addAll(pkStore.getTrustCertificates());
        }
        return ret;
    }

    @Override
    public CertPath getCertPath(X509Certificate certificate) throws CertStoreException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public PrivateKey getPrivateKey(X509Certificate certificate) throws CertStoreException {
        PrivateKey ret = null;
        if (this.kss.isEmpty()) {
            LOG.warn("Se han pedido los alias de un almacen sin inicializar, se intentara inicializar primero"); //$NON-NLS-1$
            try {
                init(null);
            }
            catch (final Exception e) {
                LOG.error("No se ha podido inicializar el almacen, se devolvera una lista de alias vacia: " + e); //$NON-NLS-1$
                return ret;
            }
        }
        
    	String alias = getCertificateAlias(certificate);     
        LOG.debug("Busca clave privada de certificado con alias: " +alias); //$NON-NLS-1$
        for(KeyStore current: this.kss) {
        	KSStore pkStore = new KSStore(current, null, "".toCharArray()); 
            try {
            	ret = (PrivateKey) pkStore.getPrivateKey(certificate,alias);
            }
            catch ( Exception e ){
                LOG.error("No se pudo acceder al almacen actual: " + e,e); //$NON-NLS-1$
            }
            if(ret != null) {
                break;
            }
        }
        return ret;
    }

    @Override
    public List<X509Certificate> getPublicCertificates() throws CertStoreException {
        List<X509Certificate> ret = new ArrayList<X509Certificate>();
        if (this.kss.isEmpty()) {
            LOG.warn("Se han pedido los alias de un almacen sin inicializar, se intentara inicializar primero"); //$NON-NLS-1$
            try {
                init(null);
            }
            catch (final Exception e) {
                LOG.error("No se ha podido inicializar el almacen, se devolvera una lista de alias vacia: " + e); //$NON-NLS-1$
                return ret;
            }
        }
        for(KeyStore current: this.kss) {
            IPKStoreManager pkStore = new KSStore(current, null, "".toCharArray());
            ret.addAll(pkStore.getPublicCertificates());
        }
        return ret;
    }
    /**
     * Inicializa un almac&eacute;n PKCS#11.
     * @param passwordCallback Callback para la recuperaci&oacute;n de la
     *        contrase&ntilde;a del almac&eacute;n.
     * @param params Parametros adicionales para la configuraci&oacute;n del
     *        almac&eacute;n.
     * @return Array con los almacenes configurados.
     * @throws AOKeyStoreManagerException Cuando ocurre un error durante la inicializaci&oacute;n.
     * @throws IOException Cuando se indique una contrase&ntilde;a incorrecta para la
     *         apertura del almac&eacute;n.
     * @throws es.gob.afirma.keystores.main.common.MissingSunPKCS11Exception Si no se encuentra la biblioteca SunPKCS11 */
    private List<KeyStore> initPkcs11(final org.mozilla.jss.util.PasswordCallback passwordCallback,
                                      final Object[] params) throws CertStoreException, IOException {
        // En el "params" debemos traer los parametros:
        // [0] -p11lib: Biblioteca PKCS#11, debe estar en el Path (Windows) o en el LD_LIBRARY_PATH (UNIX, Linux, Mac OS X)
        // [1] -desc: Descripcion del token PKCS#11 (opcional)
        // [2] -slot: Numero de lector de tarjeta (Sistema Operativo) [OPCIONAL]

        KeyStore ks = null;
        // Anadimos el proveedor PKCS11 de Sun
        if (params == null || params.length < 2) {
            throw new IOException("No se puede acceder al KeyStore PKCS#11 si no se especifica la biblioteca"); //$NON-NLS-1$
        }
        final String p11lib;
        if (params[0] != null) {
            p11lib = params[0].toString();
        }
        else {
            throw new IllegalArgumentException("No se puede acceder al KeyStore PKCS#11 si se especifica una biblioteca nula"); //$NON-NLS-1$
        }

        // Numero de lector
        Integer slot = null;
        if (params.length >= 3 && params[2] instanceof Integer) {
            slot = (Integer) params[2];
        }

        // Agregamos un nombre a cada PKCS#11 para asegurarnos de no se
        // agregan mas de una vez como provider.
        // Si ya se cargo el PKCS#11 anteriormente, se volvera a instanciar.
        final String p11ProviderName = new File(p11lib).getName().replace('.', '_').replace(' ', '_');
        Provider p11Provider = Security.getProvider("SunPKCS11-" + p11ProviderName); //$NON-NLS-1$

        LOG.debug("p11provider:"+p11Provider);
        if (p11Provider == null) {

            Constructor<?> sunPKCS11Contructor;
            try {
                sunPKCS11Contructor = Class.forName("sun.security.pkcs11.SunPKCS11").getConstructor(InputStream.class); //$NON-NLS-1$
            }
            catch (final Exception e) {
                throw new CertStoreException(e);
            }

            final byte[] config = createPKCS11ConfigFile(p11lib, p11ProviderName, slot).getBytes();
            try {
                p11Provider = (Provider) sunPKCS11Contructor.newInstance(new ByteArrayInputStream(config));
            }
            catch (final Exception e) {
                // El PKCS#11 del DNIe a veces falla a la primera pero va
                // correctamente a la segunda
                // asi que reintentamos una vez mas
                try {
                    p11Provider = (Provider) sunPKCS11Contructor.newInstance(new ByteArrayInputStream(config));
                }
                catch (final Exception ex) {
                    throw new CertStoreException("No se ha podido instanciar el proveedor SunPKCS11 para la la biblioteca " + p11lib, ex); //$NON-NLS-1$
                }
            }
            Security.addProvider(p11Provider);
            LOG.debug("Añadido provider:"+p11Provider);
        }
        else {
            LOG.info("El proveedor SunPKCS11 solicitado ya estaba instanciado, se reutilizara esa instancia: " + p11Provider.getName()); //$NON-NLS-1$
        }

        try {
            ks = KeyStore.getInstance("PKCS11", p11Provider);
        }
        catch (final Exception e) {
           Security.removeProvider(p11Provider.getName());
            p11Provider = null;
            throw new CertStoreException("No se ha podido obtener el almacen PKCS#11", e); //$NON-NLS-1$
        }

        try {
            char[] passwd = null;
            if(passwordCallback != null) {
                Password pass = passwordCallback.getPasswordFirstAttempt(new PasswordCallbackInfo("Firefox", 1));
                passwd = pass.getChars();
            }
            ks.load(null,  passwd);
        }
        catch (final IOException e) {
            if (e.getCause() instanceof UnrecoverableKeyException ||
                    e.getCause() instanceof BadPaddingException) {
                throw new CertStoreException("Contrasena invalida: " + e, e); //$NON-NLS-1$
            }
            throw new CertStoreException("No se ha podido obtener el almacen PKCS#11 solicitado", e); //$NON-NLS-1$
        }
        catch (final CertificateException e) {
            Security.removeProvider(p11Provider.getName());
            p11Provider = null;
            throw new CertStoreException("No se han podido cargar los certificados del almacen PKCS#11 solicitado", e); //$NON-NLS-1$
        }
        catch (final NoSuchAlgorithmException e) {
            Security.removeProvider(p11Provider.getName());
            p11Provider = null;
            throw new CertStoreException("No se ha podido verificar la integridad del almacen PKCS#11 solicitado", e); //$NON-NLS-1$
        } catch (GiveUpException e) {
            throw new CertStoreException("Contrasena invalida: " + e, e); //$NON-NLS-1$
        }
        final List<KeyStore> ret = new ArrayList<KeyStore>(1);
        ret.add(ks);
        return ret;
    }

    /** Crea las l&iacute;neas de configuraci&oacute;n para el proveedor PKCS#11
     * de Sun.
     * @param lib Nombre (con o sin ruta) de la biblioteca PKCS#11
     * @param name Nombre que queremos tenga el proveedor. CUIDADO: SunPKCS11
     *            a&ntilde;ade el prefijo <i>SunPKCS11-</i>.
     * @param slot Lector de tarjetas en el que buscar la biblioteca.
     * @return Fichero con las propiedades de configuracion del proveedor
     *         PKCS#11 de Sun para acceder al KeyStore de un token gen&eacute;rico */
    static String createPKCS11ConfigFile(final String lib, final String name, final Integer slot) {

        final StringBuilder buffer = new StringBuilder("library="); //$NON-NLS-1$

        // TODO: Ir uno a uno en el ApplicationPath de Java hasta que
        // encontremos la biblioteca, en vez de mirar directamente en
        // system32 y usr/lib

        // Si la biblioteca no existe directamente es que viene sin Path
        // Mozilla devuelve las bibliotecas sin Path
        if (!new java.io.File(lib).exists()) {
            String sysLibDir = MozillaKeyStoreUtilitiesWindows.getSystemLibDir();
            if (!sysLibDir.endsWith(java.io.File.separator)) {
                sysLibDir += java.io.File.separator;
            }
            buffer.append(sysLibDir);
        }

        buffer.append(lib).append("\r\n") //$NON-NLS-1$
        // Ignoramos la descripcion que se nos proporciona, ya que el
        // proveedor PKCS#11 de Sun
        // falla si llegan espacios o caracteres raros
              .append("name=") //$NON-NLS-1$
              .append(name != null ? name : "AFIRMA-PKCS11") //$NON-NLS-1$
              // El showInfo debe ser false para mantener la compatibilidad con el PKCS#11 de los dispositivos Clauer
              .append("\r\nshowInfo=false\r\n"); //$NON-NLS-1$
              

        if (slot != null) {
            buffer.append("slot=").append(slot).append("\r\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Por un problema con la version 10 del driver de la FNMT para el DNIe y tarjetas CERES
        // debemos deshabilitar el mecanismo del algorimto de firma con SHA1 para que lo emule
        for (final String problematicLib : FNMT_PKCS11_LIBS_WITHOUT_SHA1) {
        	if (problematicLib.equalsIgnoreCase(new java.io.File(lib).getName())) {
        		buffer.append("disabledMechanisms={ CKM_SHA1_RSA_PKCS }\r\n"); //$NON-NLS-1$
        		break;
        	}
        }
        
        LOG.info("Creada configuracion PKCS#11:\r\n" + buffer.toString()); //$NON-NLS-1$
        return buffer.toString();
    }
}
