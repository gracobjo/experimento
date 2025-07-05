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
package es.mityc.javasign.pkstore.mozilla.unified;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import es.mityc.javasign.utils.OSTool;

/** Clase para la obtenci&oacute;n de los m&oacute;dulos PKCS#11 instalados en la
 * base de datos <i>secmod.db</i> de Mozilla / Firefox. */

final class AOSecMod {

    private static final String[] SUPPORTED_URI_SCHEMES = new String[] {
        "http", "https", "file", "urn" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    };
    /** Listado de m&oacute;dulos almacenados en el fichero "Secmod.db". */
    private static List<ModuleName> modules = null;

    private AOSecMod() {
        // No permitimos la instanciacion
    }

    /** <pre>
     * struct {
     *   BYTE  commonNameLen[ 2 ];
     *   BYTE  commonName   [ commonNameLen ];
     *   BTTE  libNameLen   [ 2 ];
     *   BYTE  libName      [ libNameLen ];
     * If it is "extended" it also has these members:
     *   BYTE  initStringLen[ 2 ];
     *   BYTE  initString   [ initStringLen ];
     * }
     * </pre> */
    private static ModuleName processNames(final byte[] secmoddb, final int namesOffset) {

        int namesRunningOffset = namesOffset;

        int len = getShort(secmoddb, namesRunningOffset + 0);
        final String commonName = new String(secmoddb, namesRunningOffset + 2, len);

        namesRunningOffset += len + 2;

        len = getShort(secmoddb, namesRunningOffset);
        final String libName = new String(secmoddb, namesRunningOffset + 2, len);

        if (
        		(OSTool.getSO().isWindows() && (libName.endsWith(".DLL") || libName.endsWith(".dll"))) || //$NON-NLS-1$ //$NON-NLS-2$
        		(!OSTool.getSO().isWindows() && (libName.endsWith(".so") || libName.contains(".so.") || libName.endsWith(".dylib"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		) {

            // namesRunningOffset += len + 2;

            final String trueLibName = searchPathForFile(new String[] {
                libName
            });

            if (trueLibName != null) {
                return new ModuleName(trueLibName, commonName);
            }
        }

        throw new UnsupportedOperationException("Intento fallido: " + libName); //$NON-NLS-1$

    }

    /** Obtiene los m&oacute;dulos de seguridad PKCS#11 instalados en la base de
     * datos <i>secmod.db</i>.
     * @param dir
     *        Directorio del perfil del usuario activo de Mozilla / Firefox
     * @return Vector con los m&oacute;dulos encontrados, el vector
     *         estar&aacute; vac&iacute;o si se encuentra alg&uacute;n problema
     *         durante la b&acute;squeda
     * @throws AOException
     *         Cuando ocurre cualquier problema durante el proceso */
    static List<ModuleName> getModules(final String dir) throws IOException {

        if (dir == null || "".equals(dir)) { //$NON-NLS-1$
            throw new IllegalArgumentException("El directorio del perfil de Mozilla no puede ser nulo"); //$NON-NLS-1$
        }

        String profileDir = dir;

        if (modules == null) {

            profileDir = profileDir.replace("\\ ", " "); //$NON-NLS-1$ //$NON-NLS-2$
            if (!profileDir.endsWith("/")) { //$NON-NLS-1$
                profileDir = profileDir + "/"; //$NON-NLS-1$
            }
            final File secmod = new File(profileDir + "secmod.db"); //$NON-NLS-1$
            if (!secmod.exists()) {
                throw new IOException("El directorio del perfil de Mozilla proporcionado no contiene una base de datos de modulos (secmod.db)"); //$NON-NLS-1$
            }
            final byte[] secMod;
            try {
                secMod = MozillaKeyStoreUtilitiesWindows.getDataFromInputStream(loadFile(createURI(secmod.getAbsolutePath())));
            }
            catch (final Exception e) {
                throw new IOException("Error leyendo la base de datos de modulos (secmod.db)", e); //$NON-NLS-1$
            }

            // Obtenemos los modulos PKCS#11 asegurandonos de que no aparecen
            // mas de una vez
            modules = new ArrayList<ModuleName>();
            final Set<String> libs = new HashSet<String>();
            for (int i = 0; i < secMod.length; i++) {
                try {
                    final ModuleName module = processNames(secMod, i);
                    if (!libs.contains(module.getLib())) {
                        libs.add(module.getLib());
                        modules.add(module);
                        // Logger.getLogger("es.gob.afirma").info("La busqueda manual sobre Mozilla secmod.db ha encontrado el siguiente modulo: "
                        // + module);
                    }
                }
                catch (final Exception e) {
                    continue;
                }
            }
        }

        return modules;
    }

    /** M&oacute;dulo de seguridad (PKCS#11) de Mozilla / Firefox. */
    static class ModuleName {

        private final String lib;
        private final String description;

        ModuleName(final String l, final String d) {
            this.lib = l;
            this.description = d;
        }

        /** Obtiene el nombre de la biblioteca PKCS#11 del m&oacute;dulo.
         * @return Nombre de la biblioteca (con la ruta absoluta ioncluida) del
         *         m&oacute;dulo */
        String getLib() {
            return this.lib;
        }

        /** Obtiene la descripci&oacute;n (nombre com&uacute;n) del
         * m&oacute;dulo.
         * @return Descripci&oacute;n del m&oacute;dulo */
        String getDescription() {
            return this.description;
        }

        @Override
        public String toString() {
            // commonName + " (" + type + ", " + libraryName + ", slot " + slot
            // + ")";
            return this.description + " (EXTERNAL, " + this.lib + ", slot 0)"; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /** Obtiene un n&uacute;mero de 16 bits a partir de dos posiciones de un
     * array de octetos.
     * @param src
     *        Array de octetos origen
     * @param offset
     *        Desplazamiento desde el origen para el comienzo del par de
     *        octetos
     * @return N&ueacute;mero entero de 16 bits (sin signo) */
    private static int getShort(final byte[] src, final int offset) {
        return (((src)[offset + 0] << 8) | (src)[offset + 1]);
    }

    /** Busca un fichero (o una serie de ficheros) en el PATH del sistema. Deja
     * de buscar en la primera ocurrencia
     * @param files
     *        Ficheros a buscar en el PATH
     * @return Ruta completa del fichero encontrado en el PATH o <code>null</code> si no se encontr&oacute; nada */
    private static String searchPathForFile(final String[] files) {
        if (files == null || files.length < 1) {
            return null;
        }

        // Si existe el primero con el PATH completo lo devolvemos sin mas
        if (new File(files[0]).exists()) {
            return files[0];
        }

        final StringTokenizer st = new StringTokenizer(getJavaLibraryPath(), File.pathSeparator);
        String libPath;
        while (st.hasMoreTokens()) {
            libPath = st.nextToken();
            if (!libPath.endsWith(File.separator)) {
                libPath = libPath + File.separator;
            }
            File tmpFile;
            for (final String f : files) {
                tmpFile = new File(libPath + f);
                if (tmpFile.exists() && (!tmpFile.isDirectory())) {
                    return libPath + f;
                }
            }
        }
        return null;
    }
    /** Crea una URI a partir de un nombre de fichero local o una URL.
     * @param file Nombre del fichero local o URL
     * @return URI (<code>file://</code>) del fichero local o URL
     * @throws URISyntaxException Si no se puede crear una URI soportada a partir de la cadena de entrada */
    private static URI createURI(final String file) throws URISyntaxException {

        if (file == null || "".equals(file)) { //$NON-NLS-1$
            throw new IllegalArgumentException("No se puede crear una URI a partir de un nulo"); //$NON-NLS-1$
        }

        String filename = file.trim();

        if ("".equals(filename)) { //$NON-NLS-1$
            throw new IllegalArgumentException("La URI no puede ser una cadena vacia"); //$NON-NLS-1$
        }

        // Cambiamos los caracteres Windows
        filename = filename.replace('\\', '/');

        // Realizamos los cambios necesarios para proteger los caracteres no
        // seguros
        // de la URL
        filename =
                filename.replace(" ", "%20") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("<", "%3C") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace(">", "%3E") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("\"", "%22") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("{", "%7B") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("}", "%7D") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("|", "%7C") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("^", "%5E") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("[", "%5B") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("]", "%5D") //$NON-NLS-1$ //$NON-NLS-2$
                        .replace("`", "%60"); //$NON-NLS-1$ //$NON-NLS-2$

        final URI uri = new URI(filename);

        // Comprobamos si es un esquema soportado
        final String scheme = uri.getScheme();
        for (final String element : SUPPORTED_URI_SCHEMES) {
            if (element.equals(scheme)) {
                return uri;
            }
        }

        // Si el esquema es nulo, aun puede ser un nombre de fichero valido
        // El caracter '#' debe protegerse en rutas locales
        if (scheme == null) {
            filename = filename.replace("#", "%23"); //$NON-NLS-1$ //$NON-NLS-2$
            return createURI("file://" + filename); //$NON-NLS-1$
        }

        // Miramos si el esquema es una letra, en cuyo caso seguro que es una
        // unidad de Windows ("C:", "D:", etc.), y le anado el file://
        // El caracter '#' debe protegerse en rutas locales
        if (scheme.length() == 1 && Character.isLetter((char) scheme.getBytes()[0])) {
            filename = filename.replace("#", "%23"); //$NON-NLS-1$ //$NON-NLS-2$
            return createURI("file://" + filename); //$NON-NLS-1$
        }

        throw new URISyntaxException(filename, "Tipo de URI no soportado"); //$NON-NLS-1$

    }

    /** Obtiene el flujo de entrada de un fichero (para su lectura) a partir de
     * su URI.
     * @param uri
     *        URI del fichero a leer
     * @return Flujo de entrada hacia el contenido del fichero
     * @throws AOException
     *         Cuando ocurre cualquier problema obteniendo el flujo
     * @throws IOException Cuando no se ha podido abrir el fichero de datos.
     */
    public static InputStream loadFile(final URI uri) throws IOException {

        if (uri == null) {
            throw new IllegalArgumentException("Se ha pedido el contenido de una URI nula"); //$NON-NLS-1$
        }

        if (uri.getScheme().equals("file")) { //$NON-NLS-1$
            // Es un fichero en disco. Las URL de Java no soportan file://, con
            // lo que hay que diferenciarlo a mano

            // Retiramos el "file://" de la uri
            String path = uri.getSchemeSpecificPart();
            if (path.startsWith("//")) { //$NON-NLS-1$
                path = path.substring(2);
            }
            return new FileInputStream(new File(path));
        }

        // Es una URL
        final InputStream tmpStream = new BufferedInputStream(uri.toURL().openStream());

        // Las firmas via URL fallan en la descarga por temas de Sun, asi que
        // descargamos primero
        // y devolvemos un Stream contra un array de bytes
        final byte[] tmpBuffer = MozillaKeyStoreUtilitiesWindows.getDataFromInputStream(tmpStream);

        return new java.io.ByteArrayInputStream(tmpBuffer);
    }
    /** Recupera la propiedad Path de Java.
     * @return Propiedad en el Path de Java. */
    public static String getJavaLibraryPath() {
        return System.getProperty("java.library.path"); //$NON-NLS-1$;
    }

}
