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
package es.mityc.appfacturae.ui.components;

public class IndividualRow {
	
	public Object[] attributes;
	
	public IndividualRow(Object[] attributes) {
		super();
		this.attributes = attributes;
	}
	
	public String toString(){
		String itemName = attributes[2].toString();
		if (attributes[3] != null)
			itemName = itemName + " " + attributes[3].toString();
		itemName = itemName + ", " + attributes[1].toString() + " ("+attributes[0].toString()+")";
		return itemName;
	}
}
