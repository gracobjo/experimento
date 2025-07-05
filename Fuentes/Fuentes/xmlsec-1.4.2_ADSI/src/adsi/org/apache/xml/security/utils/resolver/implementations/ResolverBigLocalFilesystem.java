/**
 * 
 */
package adsi.org.apache.xml.security.utils.resolver.implementations;

import org.apache.xml.utils.URI;
import org.w3c.dom.Attr;

import adsi.org.apache.xml.security.signature.XMLSignatureInput;
import adsi.org.apache.xml.security.utils.resolver.ResourceResolverException;

/**
 * This ResourceResolver allows to access to filesystem without bytereaded in signature management (improves memory management with some
 * signatures sign/validation when not accesing such files through XmlSignature structure).
 * 
 * @author dsantose
 */
public class ResolverBigLocalFilesystem extends ResolverLocalFilesystem {

	/**
	 * 
	 */
	public ResolverBigLocalFilesystem() {
		super();
	}
	
	public XMLSignatureInput engineResolve(Attr uri, String BaseURI)
	throws ResourceResolverException {

		try {
			URI uriNew = getNewURI(uri.getNodeValue(), BaseURI);

			// if the URI contains a fragment, ignore it
			URI uriNewNoFrag = new URI(uriNew);

			uriNewNoFrag.setFragment(null);

			String fileName =
				ResolverLocalFilesystem
				.translateUriToFilename(uriNewNoFrag.toString());
			ReseteableFileInputStream inputStream = new ReseteableFileInputStream(fileName);
			XMLSignatureInput result = new XMLSignatureInput(inputStream);

			result.setSourceURI(uriNew.toString());

			return result;
		} catch (Exception e) {
			throw new ResourceResolverException("generic.EmptyMessage", e, uri,
					BaseURI);
		}
	}


}
