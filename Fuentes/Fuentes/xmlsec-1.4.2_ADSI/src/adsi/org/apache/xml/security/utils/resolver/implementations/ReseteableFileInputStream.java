/**
 * 
 */
package adsi.org.apache.xml.security.utils.resolver.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This InputStream simulates FileInputStream with resetable capability (reset equivalent to new FileInputStream).
 * 
 * @author dsantose
 */
public class ReseteableFileInputStream extends InputStream {
	
	private FileInputStream fis;
	private String filename;

	/**
	 * @param buf
	 */
	public ReseteableFileInputStream(String filename) throws FileNotFoundException {
		super();
		this.filename = filename;
		fis = new FileInputStream(filename);
	}

	@Override
	public int available() throws IOException {
		return fis.available();
	}

	@Override
	public void close() throws IOException {
		fis.close();
	}

	@Override
	public int read() throws IOException {
		return fis.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return fis.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return fis.read(b);
	}

	@Override
	public long skip(long n) throws IOException {
		return fis.skip(n);
	}

	@Override
	public synchronized void mark(int readlimit) {
		super.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void reset() throws IOException {
		fis.close();
		fis = new FileInputStream(filename);
	}
	
	



}
