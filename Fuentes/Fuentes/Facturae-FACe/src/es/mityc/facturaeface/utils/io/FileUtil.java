/**
 * Copyright 2015 Ministerio de Industria, Energía y Turismo
 *
 * Este fichero es parte de "Facturae-FAce".
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
package es.mityc.facturaeface.utils.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;

public class FileUtil {

	/** 
	 * Devuelve el tamaño, en bytes, del fichero especificado.
	 * @param pathname Ruta de acceso y nombre del fichero.
	 * @return El tamaño del fichero. 
	 */
	public static int getFileSize(String pathname) {

		int size;
		File file;

		file = new File(pathname);
		size = (int) file.length();

		return size;

	}

	/** 
	 * Lee el contenido de un fichero fuente.
	 * @param pathname Ruta de acceso y nombre del fichero fuente.
	 * @return El contenido del fichero fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static byte[] readBytesFromFile(String pathname) throws Exception {
		int size;
		byte[] data;
		File file;
		FileInputStream fis = null;
		try {
			size = getFileSize(pathname);
			data = new byte[size];
			file = new File(pathname);
			fis = new FileInputStream(file);
			fis.read(data);
			fis.close();
			fis = null;
			return data;
		} catch (Exception e) {
			try {
				if (fis != null)
					fis.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}

	}

	/** 
	 * Lee el contenido de un fichero fuente.
	 * @param file File
	 * @return El contenido del fichero fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static byte[] readBytesFromFile(File file) throws Exception {
		int size;
		byte[] data;
		FileInputStream fis = null;
		try {
			size = (int) file.length();
			data = new byte[size];
			fis = new FileInputStream(file);
			fis.read(data);
			fis.close();
			fis = null;
			return data;
		} catch (Exception e) {
			try {
				if (fis != null)
					fis.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}

	}

	/** 
	 * Lee el contenido de un fichero fuente.
	 * @param pathname Ruta de acceso y nombre del fichero fuente.
	 * @return El contenido del fichero fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static byte[] readBytesFromResourceFile(String pathname) throws Exception {
		int size;
		byte[] data;
		InputStream fis = null;
		try {
			//fis = ClassLoader.getSystemResourceAsStream(pathname);
			File file = new File(pathname);
			fis = new BufferedInputStream(new FileInputStream(file));
			size = fis.available();
			data = new byte[size];
			fis.read(data);
			fis.close();
			fis = null;
			return data;
		} catch (Exception e) {
			try {
				if (fis != null)
					fis.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}

	}

	/** 
	 * Lee el contenido de un fichero fuente.
	 * @param pathname Ruta de acceso y nombre del fichero fuente.
	 * @return El contenido del fichero fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static char[] readCharsFromFile(String pathname) throws Exception {
		int size;
		char[] data;
		File file;
		FileReader fr = null;
		try {
			size = getFileSize(pathname);
			data = new char[size];
			file = new File(pathname);
			fr = new FileReader(file);
			fr.read(data);
			fr.close();
			fr = null;
			return data;
		} catch (Exception e) {
			try {
				if (fr != null)
					fr.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}

	}

	/** 
	 * Lee el contenido de un fichero fuente.
	 * @param pathname Ruta de acceso y nombre del fichero fuente.
	 * @return El contenido del fichero fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static String readStringFromFile(String pathname) throws Exception {
		String data;
		char[] chars;
		chars = readCharsFromFile(pathname);
		data = new String(chars);
		return data;
	}

	/** 
	 * Escribe en un fichero destino unos datos fuente.
	 * @param pathname Ruta de acceso y nombre del fichero destino.
	 * @param data Datos fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static void writeBytesToFile(String pathname, byte[] data) throws Exception {
		File file;
		FileOutputStream fos = null;
		try {
			file = new File(pathname);
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
			fos.close();
			fos = null;
		} catch (Exception e) {
			try {
				if (fos != null)
					fos.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}
	}

	/** 
	 * Escribe en un fichero destino unos datos fuente.
	 * @param pathname Ruta de acceso y nombre del fichero destino.
	 * @param data Datos fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static void writeCharsToFile(String pathname, char[] data) throws Exception {
		File file;
		FileWriter fw = null;
		try {
			file = new File(pathname);
			fw = new FileWriter(file);
			fw.write(data);
			fw.flush();
			fw.close();
			fw = null;
		} catch (Exception e) {
			try {
				if (fw != null)
					fw.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}

	}

	/** 
	 * Escribe en un fichero destino unos datos fuente.
	 * @param pathname Ruta de acceso y nombre del fichero destino.
	 * @param data Datos fuente.
	 * @throws Exception si se produce algún error. 
	 */
	public static void writeStringToFile(String pathname, String data) throws Exception {
		File file;
		FileWriter fw = null;
		try {
			file = new File(pathname);
			fw = new FileWriter(file);
			fw.write(data);
			fw.flush();
			fw.close();
			fw = null;
		} catch (Exception e) {
			try {
				if (fw != null)
					fw.close();
				throw e;
			} catch (Exception e1) {
				throw e;
			}
		}
	}

}
