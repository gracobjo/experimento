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
package es.mityc.appfacturae.hibernate.auxClass;

import es.mityc.appfacturae.utils.io.StringUtil;

public class Tax {
	
	protected int id;
	protected String taxTypeCode;
    protected String taxRate;
    protected Double equivalenceSurcharge;
	
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTaxTypeCode() {
		return taxTypeCode;
	}
	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}
	public String getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(String taxRate) {
		if(taxRate!=null){
			this.taxRate = taxRate.replace(",", ".");
		} else {
			this.taxRate = taxRate;
		}
	}
	public void setTaxRate(double taxRate) {
		this.taxRate = StringUtil.disableScientificNotation(taxRate, -1).replace(",", ".");
	}
	public Double getEquivalenceSurcharge() {
		return equivalenceSurcharge;
	}
	public void setEquivalenceSurcharge(Double equivalenceSurcharge) {
		this.equivalenceSurcharge = equivalenceSurcharge;
	}
    
}
