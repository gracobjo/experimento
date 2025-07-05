/**
 *  This file has been modified by Ministerio de Industria, Energía y Turismo 
 **/
 
package adsi.org.apache.xml.security.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import adsi.org.apache.xml.security.exceptions.XMLSecurityException;

public interface ElementChecker {
	 /**
	  * Check that the elemnt is the one expect
	  *
	  * @throws XMLSecurityException
	  */
	   public void guaranteeThatElementInCorrectSpace(ElementProxy expected, Element actual)
	           throws XMLSecurityException;
	   
	   public boolean isNamespaceElement(Node el, String type, String ns);
}
