/**
 * Copyright 2015 Ministerio de Industria, Energía y Turismo
 *
 * Este fichero es parte de "Facturae-APC".
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
package es.mityc.appfacturae.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mityc.appfacturae.ui.components.ComboOption;
import es.mityc.appfacturae.utils.constants.Constants;
import es.mityc.appfacturae.utils.io.Base64;
import es.mityc.facturaeface.FACeConfig.FACeCertificateProvider;
import es.mityc.facturaeface.FACeConnector;
import es.mityc.facturaeface.bean.AnexoFile;
import es.mityc.facturaeface.bean.AnularFacturaResponse;
import es.mityc.facturaeface.bean.ConsultarAdministracionesResponse;
import es.mityc.facturaeface.bean.ConsultarFacturaResponse;
import es.mityc.facturaeface.bean.ConsultarRelacionesPorAdministracionResponse;
import es.mityc.facturaeface.bean.EnviarFacturaRequest;
import es.mityc.facturaeface.bean.EnviarFacturaResponse;
import es.mityc.facturaeface.bean.FacturaFile;
import es.mityc.facturaeface.bean.OGUTOC;
import es.mityc.facturaeface.bean.UnidadDir3;
import es.mityc.facturaeface.exception.FACeException;
import es.mityc.javasign.pkstore.DefaultPassStoreKS;

public class FACeUtils {

	private static FACeUtils instance = null;
	private static Log logger = LogFactory.getLog(FACeUtils.class);
	private List<UnidadDir3> organosGestor = new ArrayList<UnidadDir3>();
	private Hashtable<String, List<UnidadDir3>> unidadesTramitadoras = new Hashtable<String, List<UnidadDir3>>();
	private Hashtable<String, List<UnidadDir3>> oficinasContables = new Hashtable<String, List<UnidadDir3>>();
	private List<UnidadDir3> administraciones = new ArrayList<UnidadDir3>();

	private FACeUtils() {

	}

	public static FACeUtils getInstance() {
		if (instance == null) {
			instance = new FACeUtils();
		}
		return instance;
	}

	public boolean checkConfig() {
		String faceMail = Constants.CONFIG_PROP.getProperty("FACeEmail");
		String faceCert = Constants.CONFIG_PROP.getProperty("FACeCert");
		String faceProvider = Constants.CONFIG_PROP.getProperty("FACeProvider");
		if (faceMail == null || faceMail.length() == 0 || faceCert == null || faceCert.length() == 0
			|| faceProvider == null || faceProvider.length() == 0) {
			logger.error("Error de configuracion de FACe");
			Constants.DIALOG.showErrorFACe(Constants.LANG.getString("FACeErrorMandatoryParameters"));
			return false;
		}
		return true;
	}

	public List<ComboOption> getAccountingOffices(String managementAgencyId, String codigoAdministracion) {
		List<ComboOption> ret = new ArrayList<ComboOption>();
		synchronized (instance) {
			if (oficinasContables == null || oficinasContables.isEmpty()) {
				loadUnidades(codigoAdministracion);
			}
		}
		List<UnidadDir3> currentList = oficinasContables.get(managementAgencyId);
		if (currentList != null) {
			for (UnidadDir3 current : currentList) {
				ret.add(new ComboOption(current.getNombre(), current.getCodigo()));
			}
			Collections.sort(ret);
		}
		return ret;
	}

	public List<ComboOption> getManagementAgencys(String codigoAdministracion) {
		List<ComboOption> ret = new ArrayList<ComboOption>();
		synchronized (instance) {
			//if (organosGestor == null || organosGestor.isEmpty()) {
				organosGestor = new ArrayList<UnidadDir3>();
				loadUnidades(codigoAdministracion);
			//}
		}
		for (UnidadDir3 currentManagementAgency : organosGestor) {
			ret.add(new ComboOption(currentManagementAgency.getNombre(), currentManagementAgency.getCodigo()));
		}
		Collections.sort(ret);
		return ret;
	}

	public List<ComboOption> getProcessingAuthorityUnits(String managementAgencyId, String codigoAdministracion) {
		List<ComboOption> ret = new ArrayList<ComboOption>();
		synchronized (instance) {
			if (unidadesTramitadoras == null || unidadesTramitadoras.isEmpty()) {
				loadUnidades(codigoAdministracion);
			}
		}
		List<UnidadDir3> currentList = unidadesTramitadoras.get(managementAgencyId);
		if (currentList != null) {
			for (UnidadDir3 currentProcessingAuthorityUnit : currentList) {
				ret.add(new ComboOption(currentProcessingAuthorityUnit.getNombre(), currentProcessingAuthorityUnit
					.getCodigo()));
			}
			Collections.sort(ret);
		}
		return ret;
	}

	public EnviarFacturaResponse sendInvoice(File fOut, String notificationMail, List<String> attachements)
		throws FACeException {
		FACeConnector fc = getFACeConnector();
		if (fc == null) {
			return null;
		}
		try {
			EnviarFacturaRequest request = new EnviarFacturaRequest();
			request.setCorreo(notificationMail);
			FacturaFile fichero_factura = new FacturaFile();
			fichero_factura.setNombre(fOut.getName());
			fichero_factura.setFactura(Base64.encodeFromFile(fOut.getAbsolutePath()));
			request.setFactura(fichero_factura);

			if (attachements != null && attachements.size() > 0) {
				AnexoFile[] ficheros_anexos = new AnexoFile[attachements.size()];
				for (int i = 0; i < attachements.size(); i++) {
					File attachementFile = new File(attachements.get(i));
					if (attachementFile.exists()) {
						AnexoFile currentAttach = new AnexoFile();
						//List mimeList = MimeTypeResolver.resolve(attachementFile);
						//if(mimeList.size() > 0){
						//	MimeType mt = (MimeType)mimeList.get(0);
						//	currentAttach.setMime(mt.getName());
						//} else {
//						TODO: MIME NO ADMITIDO
						currentAttach.setMime("application/pdf");
						//}
						currentAttach.setNombre(attachementFile.getName());
						currentAttach.setAnexo(Base64.encodeFromFile(attachements.get(i)));
						ficheros_anexos[i] = currentAttach;
					}
				}
				request.setAnexos(ficheros_anexos);
			}

			return fc.enviarFactura(request);
		} finally {
			fc.finish();
		}
	}
	
	public AnularFacturaResponse cancelInvoice(String codigoFace, String motivo) throws FACeException{
		
		FACeConnector fc = getFACeConnector();
		if (fc == null) {
			return null;
		}	
		AnularFacturaResponse res = fc.anularFactura(codigoFace, motivo);		
		return res;
	}

	public ConsultarFacturaResponse getInvoiceStatus(String registerCode) throws FACeException {
		FACeConnector fc = getFACeConnector();
		if (fc == null) {
			return null;
		}
		try {
			return fc.consultarFactura(registerCode);
		} finally {
			fc.finish();
		}
	}

	private FACeConnector getFACeConnector() {
		if (checkConfig()) {
			boolean useProxy = false;
			if (!"0.0.0.0".equals(Constants.CONFIG_PROP.getProperty("proxy"))) {
				useProxy = true;
				logger.info("Proxy configuration set: " + Constants.CONFIG_PROP.getProperty("proxy"));
			}
			FACeCertificateProvider certificateProvider = FACeCertificateProvider.valueOf(Constants.CONFIG_PROP
				.getProperty("FACeProvider"));
			char[] password = new char[0];
			if (certificateProvider.equals(FACeCertificateProvider.JAVA)) {
				DefaultPassStoreKS handler = new DefaultPassStoreKS();
				handler.setPINMessage(Constants.LANG.getString("FACePIN"));
				password = handler.getPassword(null, null);
			}
			FACeConnector fc = new FACeConnector(Constants.APP_PROP.getProperty("FACE_URL"),
				Constants.CONFIG_PROP.getProperty("FACeCert"), password, useProxy,
				Constants.CONFIG_PROP.getProperty("proxy"), Constants.CONFIG_PROP.getProperty("proxyPort").toString(),
				new Boolean(Constants.CONFIG_PROP.getProperty("proxyAuth").toString()).booleanValue(),
				Constants.CONFIG_PROP.getProperty("proxyUser").toString(), Constants.CONFIG_PROP
					.getProperty("proxyPwd").toString(), Constants.FACE_KEYSTORE_FILE,
				Constants.FACE_KEYSTORE_PASSWORD, certificateProvider, Integer.parseInt(Constants.CONFIG_PROP
					.getProperty("timeoutFACEMilis")));
			return fc;
		}
		return null;
	}

	private void loadUnidades(String codAdministracion) {
		FACeConnector fc = getFACeConnector();
		if (fc == null) {
			return;
		}
		try {
			Hashtable<String, String> organosGestorIds = new Hashtable<String, String>();
			Hashtable<String, String> unidadesTramitadorasIds = new Hashtable<String, String>();
			ConsultarRelacionesPorAdministracionResponse unidades = fc.consultarUnidadesPorAdministracion(codAdministracion);
			for (OGUTOC currentUnidad : unidades.getRelaciones()) {
				if (organosGestorIds.containsKey(currentUnidad.getOrganoGestor().getCodigo())) {
					if (!unidadesTramitadorasIds.containsKey(currentUnidad.getOrganoGestor().getCodigo() + "#"
						+ currentUnidad.getUnidadTramitadora().getCodigo())) {
						unidadesTramitadoras.get(currentUnidad.getOrganoGestor().getCodigo()).add(
							currentUnidad.getUnidadTramitadora());
						unidadesTramitadorasIds.put(currentUnidad.getOrganoGestor().getCodigo() + "#"
							+ currentUnidad.getUnidadTramitadora().getCodigo(), "");
					}
				} else {
					organosGestorIds.put(currentUnidad.getOrganoGestor().getCodigo(), "");
					organosGestor.add(currentUnidad.getOrganoGestor());

					List<UnidadDir3> unidadesTramitadorasList = new ArrayList<UnidadDir3>();
					unidadesTramitadorasList.add(currentUnidad.getUnidadTramitadora());
					unidadesTramitadorasIds.put(currentUnidad.getOrganoGestor().getCodigo() + "#"
						+ currentUnidad.getUnidadTramitadora().getCodigo(), "");
					unidadesTramitadoras
						.put(currentUnidad.getOrganoGestor().getCodigo(), unidadesTramitadorasList);
				}
				if (unidadesTramitadorasIds.containsKey(currentUnidad.getOrganoGestor().getCodigo() + "#"
					+ currentUnidad.getUnidadTramitadora().getCodigo())) {
					List<UnidadDir3> oficinasContablesList = new ArrayList<UnidadDir3>();
					oficinasContablesList.add(currentUnidad.getOficinaContable());
					oficinasContables.put(currentUnidad.getOrganoGestor().getCodigo() + "#"
						+ currentUnidad.getUnidadTramitadora().getCodigo(), oficinasContablesList);
				} else {
					List<UnidadDir3> unidadesTramitadorasList = new ArrayList<UnidadDir3>();
					unidadesTramitadorasList.add(currentUnidad.getUnidadTramitadora());
					unidadesTramitadorasIds.put(currentUnidad.getOrganoGestor().getCodigo() + "#"
						+ currentUnidad.getUnidadTramitadora().getCodigo(), "");
					unidadesTramitadoras
						.put(currentUnidad.getOrganoGestor().getCodigo(), unidadesTramitadorasList);
				}
			}
		} catch (FACeException e) {
			logger.error(
				"Error cargando organos gestor, unidades tramitadoras y oficinas contables: " + e.getMessage(), e);
			Constants.DIALOG
				.showErrorFACe("Error cargando órganos gestores, unidades tramitadoras y oficinas contables: "
					+ e.getMessage() + ". \n\n\nIMPORTANTE: Puede introducir a continuación los datos de forma manual.");
		} finally {
			fc.finish();
		}
	}

	public List<ComboOption> getAdministrations() {
		List<ComboOption> ret = new ArrayList<ComboOption>();
		if (administraciones == null || administraciones.isEmpty()) {
			FACeConnector fc = getFACeConnector();
			if (fc == null) {
				return null;
			}
			try {
				ConsultarAdministracionesResponse resp = fc.consultarAdministraciones();
				UnidadDir3[] admins = resp.getAdministraciones();
				//Añadimos el codigo al nombre de la administracion para distinguir administraciones duplicadas
				if(admins == null){
					if(resp.getResultado()!=null && StringUtils.isNotBlank(resp.getResultado().getDescripcion())){
						throw new FACeException(resp.getResultado().getDescripcion());
					} else {
						throw new FACeException("No se ha encontrado ninguna administración");
					}
				}
				for (int i = 0; i < admins.length; i++) {
					UnidadDir3 unidad = admins[i];
					unidad.setNombre(unidad.getNombre() + " (" + unidad.getCodigo() + ")");
					administraciones.add(unidad);
				}
			} catch (FACeException e) {
				logger.error("Error cargando administraciones: " + e.getMessage(), e);
				Constants.DIALOG.showErrorFACe("Error cargando administraciones: " + e.getMessage()
					+ ". \n\n\nIMPORTANTE: Puede introducir a continuación los datos de forma manual.");
			} finally {
				fc.finish();
			}
		}
		if (administraciones != null) {
			for (UnidadDir3 current : administraciones) {
				ret.add(new ComboOption(current.getNombre(), current.getCodigo()));
			}
			Collections.sort(ret);
		}

		return ret;
	}

}
