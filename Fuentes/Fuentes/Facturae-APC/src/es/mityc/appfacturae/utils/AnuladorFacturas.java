package es.mityc.appfacturae.utils;

import java.util.List;

import org.hibernate.SQLQuery;

import es.mityc.appfacturae.hibernate.FacturaeManager;
import es.mityc.appfacturae.ui.dialogs.InformationDialog;
import es.mityc.appfacturae.ui.dialogs.MotivoAnularFacturaDialog;
import es.mityc.appfacturae.ui.windows.MainWindow;
import es.mityc.appfacturae.utils.constants.Constants;
import es.mityc.facturaeface.bean.AnularFactura;
import es.mityc.facturaeface.bean.AnularFacturaResponse;
import es.mityc.facturaeface.bean.Resultado;
import es.mityc.facturaeface.exception.FACeException;

public class AnuladorFacturas {

	public static String obtenerCodigoFace(String id){
		String registerCode = "";
		String query = "SELECT REGISTER_CODE FROM FACE_SENT_RESULT WHERE SERIES_CODE+NUMBER = '"+id+"' ORDER BY DATE DESC";
		SQLQuery sFACeSent = FacturaeManager.getInstance().executeQuery(query);
		List<?> lsFACeSent = sFACeSent.list();
		if(lsFACeSent!=null && lsFACeSent.size()>0 && lsFACeSent.get(0)!=null && lsFACeSent.get(0).toString().length()>0){
			registerCode = lsFACeSent.get(0).toString();     
		}
		return registerCode;
	}
	
	public static AnularFacturaResponse solicitarAnulacion(String codigoFactura, MainWindow mw) throws FACeException{
		MotivoAnularFacturaDialog dialog = new MotivoAnularFacturaDialog(mw, true);
		if(dialog.isAcceptClicked()){
			AnularFacturaResponse afr = FACeUtils.getInstance().cancelInvoice(codigoFactura, dialog.getMotivo());
//			AnularFacturaResponse afr = new AnularFacturaResponse(new Resultado("0", "Factura anulada", null), new AnularFactura());
			if(afr != null){
				mostrarResultadoSolicitarAnulacion(mw, afr);
			} else {
				mostrarResultadoSolicitarAnulacion(mw, new AnularFacturaResponse(new Resultado("", "Error en la plataforma FACe", null), null));
			}
			return afr;
		} else {
			return null;
		}
	}
	
	private static void mostrarResultadoSolicitarAnulacion(MainWindow mw, AnularFacturaResponse afr){
		String iconPath = "";
		if(!"0".equals(afr.getResultado().getCodigo())){
			iconPath = "/images/button_cancel_disabled.jpg";
		} else {
			iconPath = "/images/button_accept.jpg";
		}
		InformationDialog dialog = new InformationDialog(mw, true, Constants.LANG.getString("CancelInvoiceWindowTitle"), afr.getResultado().getDescripcion(), new javax.swing.ImageIcon(ImportExportUtil.class.getResource(iconPath)));
		dialog.setVisible(true);
	}
}
