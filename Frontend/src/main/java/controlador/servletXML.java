package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.keys.KeyInfo;
import org.w3c.dom.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.xml.security.utils.EncryptionConstants;

/**
 * Servlet encargado de cifrar y descifrar archivos XML usando el algoritmo AES.
 *
 * Este servlet permite el envío de archivos XML desde un formulario web (via
 * POST) y aplica operaciones de cifrado o descifrado, según la acción
 * seleccionada por el usuario. Utiliza la biblioteca Apache XML Security para
 * aplicar criptografía XML estándar.
 *
 * Soporta tres modos de cifrado: - Documento completo - Solo contenido de una
 * etiqueta específica - Elemento completo con su etiqueta
 *
 * El archivo XML resultante se guarda en una carpeta del servidor
 * (`/xmlsEncriptados`) y también se muestra su contenido escapado en una JSP.
 *
 * URL de acceso: /servletXML
 *
 * Este servlet también requiere una clave AES (clave.key) ubicada en la carpeta
 * `Clave` del proyecto raíz. La clave debe tener exactamente 16 caracteres.
 *
 * @author Kenny Alejandro/Lijie Yin
 */
@WebServlet("/servletXML")
@MultipartConfig
public class servletXML extends HttpServlet {

    private static final String AES_ALGORITHM = "AES";
    private static final String ENCRYPTED_FOLDER = "xmlsEncriptados";

    // Inicializa la biblioteca Apache XML Security
    static {
        Init.init();
    }

    /**
     * Maneja las solicitudes POST para aplicar cifrado o descifrado sobre un
     * XML.
     *
     * La acción esperada se indica mediante el parámetro 'action' con valores
     * posibles: "encrypt" o "decrypt".
     *
     * @param request Solicitud HTTP que contiene el archivo y los parámetros.
     * @param response Respuesta HTTP que mostrará el resultado.
     * @throws ServletException En caso de error en el procesamiento del
     * servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("encrypt".equals(action)) {
            handleEncryption(request, response);
        } else if ("decrypt".equals(action)) {
            handleDecryption(request, response);
        }
    }

    /**
     * Procesa el archivo XML recibido y lo cifra de acuerdo al modo indicado.
     *
     * Modos soportados: - document: cifra todo el documento - content: cifra
     * solo el contenido de la etiqueta "metadata" - element: cifra la etiqueta
     * "metadata" completa
     *
     * @param request Solicitud HTTP con archivo XML y modo de cifrado.
     * @param response Respuesta HTTP con el archivo cifrado y vista JSP.
     * @throws ServletException Si falla la obtención del archivo.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void handleEncryption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode");
        String tagName = request.getParameter("tagName"); // ← nuevo
        Part filePart = request.getPart("xmlfile");

        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "No XML file received.");
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
            return;
        }

        if (tagName == null || tagName.trim().isEmpty()) {
            request.setAttribute("error", "No tag name provided.");
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
            return;
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        try (InputStream is = filePart.getInputStream()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(is);

            SecretKey key = loadKeyFromFile(request);

            switch (mode) {
                case "document":
                    encryptEntireDocument(doc, key);
                    break;
                case "content":
                    encryptElementContent(doc, tagName, key);
                    break;
                case "element":
                default:
                    encryptElement(doc, tagName, key);
                    break;
            }

            String baseName = fileName.toLowerCase().endsWith(".xml") ? fileName.substring(0, fileName.length() - 4) : fileName;
            saveAndDisplayXML(request, response, doc, baseName + "_encriptado.xml");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Encryption error: " + e.getMessage());
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
        }
    }

    /**
     * Procesa un archivo XML previamente cifrado y lo descifra usando la clave
     * AES.
     *
     * @param request Solicitud HTTP con el archivo XML cifrado.
     * @param response Respuesta HTTP que mostrará el contenido descifrado.
     * @throws ServletException Si ocurre un error en la solicitud.
     * @throws IOException Si ocurre un error de lectura o escritura.
     */
    private void handleDecryption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("xmlfile");

        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "No XML file received.");
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
            return;
        }

        try (InputStream is = filePart.getInputStream()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(is);

            SecretKey key = loadKeyFromFile(request);
            Node encryptedNode = doc.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS, "EncryptedData").item(0);

            if (encryptedNode != null) {
                XMLCipher cipher = XMLCipher.getInstance();
                cipher.init(XMLCipher.DECRYPT_MODE, key);
                cipher.doFinal(doc, (Element) encryptedNode);
            }

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String baseName = fileName.toLowerCase().endsWith("_encriptado.xml")
                    ? fileName.substring(0, fileName.length() - "_encriptado.xml".length())
                    : fileName;

            saveAndDisplayXML(request, response, doc, baseName + "_desencriptado.xml");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Decryption error: " + e.getMessage());
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
        }
    }

    /**
     * Cifra completamente un elemento XML, incluyendo su etiqueta.
     *
     * @param doc Documento XML.
     * @param tag Nombre de la etiqueta a cifrar.
     * @param key Clave AES para el cifrado.
     * @throws Exception Si ocurre un error al aplicar el cifrado.
     */
    private void encryptElement(Document doc, String tag, SecretKey key) throws Exception {
        Node target = doc.getElementsByTagName(tag).item(0);
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));
        cipher.doFinal(doc, (Element) target, false);
    }

    /**
     * Cifra únicamente el contenido interno (hijos) de una etiqueta XML.
     *
     * @param doc Documento XML.
     * @param tag Nombre de la etiqueta cuyo contenido se desea cifrar.
     * @param key Clave AES para el cifrado.
     * @throws Exception Si ocurre un error al aplicar el cifrado.
     */
    private void encryptElementContent(Document doc, String tag, SecretKey key) throws Exception {
        Element element = (Element) doc.getElementsByTagName(tag).item(0);
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));
        cipher.doFinal(doc, element, true);
    }

    /**
     * Cifra el documento XML completo desde su nodo raíz.
     *
     * @param doc Documento XML completo.
     * @param key Clave AES para el cifrado.
     * @throws Exception Si ocurre un error durante el proceso.
     */
    private void encryptEntireDocument(Document doc, SecretKey key) throws Exception {
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));
        cipher.doFinal(doc, doc.getDocumentElement(), true);
    }

    /**
     * Carga la clave AES desde el archivo 'clave.key' ubicado en la carpeta
     * raíz del proyecto.
     *
     * @param request Objeto HttpServletRequest necesario para obtener la ruta
     * base del servidor.
     * @return Objeto SecretKey creado a partir de la cadena del archivo.
     * @throws Exception Si el archivo no existe, no se puede leer, o la clave
     * no tiene 16 caracteres.
     */
    private SecretKey loadKeyFromFile(HttpServletRequest request) throws Exception {
        String basePath = getServletContext().getRealPath("/");
        String keyPath = basePath.split("target")[0] + "Clave/claveVideo.key";
        String keyStr = Files.readString(Paths.get(keyPath)).trim();
        if (keyStr.length() != 16) {
            throw new Exception("The key must be 16 characters long.");
        }
        return new SecretKeySpec(keyStr.getBytes(), AES_ALGORITHM);
    }

    /**
     * Guarda el documento XML en un archivo en disco y lo convierte a texto
     * escapado para mostrarlo.
     *
     * @param request Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @param doc Documento XML a guardar.
     * @param fileName Nombre del archivo de salida.
     * @throws Exception Si ocurre un error durante la transformación o
     * escritura.
     */
    private void saveAndDisplayXML(HttpServletRequest request, HttpServletResponse response, Document doc, String fileName)
            throws Exception {

        String basePath = getServletContext().getRealPath("/");
        String folderPath = basePath.split("target")[0] + ENCRYPTED_FOLDER;
        File outputDir = new File(folderPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outFile = new File(outputDir, fileName);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Result output = new StreamResult(outFile);
        Source input = new DOMSource(doc);
        transformer.transform(input, output);

        // Convertir a string escapado para mostrar en JSP
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
        String xmlEscaped = sw.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");

        request.setAttribute("xmlResult", xmlEscaped);
        request.setAttribute("xmlFileName", fileName);
        request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
    }
}
