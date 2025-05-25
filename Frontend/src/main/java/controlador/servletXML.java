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

@WebServlet("/servletXML")
@MultipartConfig
public class servletXML extends HttpServlet {

    private static final String AES_ALGORITHM = "AES";
    private static final String ENCRYPTED_FOLDER = "xmlsEncriptados";

    static {
        Init.init();
    }

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

    private void handleEncryption(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mode = request.getParameter("mode");
        Part filePart = request.getPart("xmlfile");

        if (filePart == null || filePart.getSize() == 0) {
            request.setAttribute("error", "No XML file received.");
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
                    encryptElementContent(doc, "metadata", key);
                    break;
                case "element":
                default:
                    encryptElement(doc, "metadata", key);
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
            String baseName = fileName.toLowerCase().endsWith("_encriptado.xml") ? fileName.substring(0, fileName.length() - "_encriptado.xml".length()) : fileName;
            saveAndDisplayXML(request, response, doc, baseName + "_desencriptado.xml");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Decryption error: " + e.getMessage());
            request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
        }
    }

    private void encryptElement(Document doc, String tag, SecretKey key) throws Exception {
        Node target = doc.getElementsByTagName(tag).item(0);
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));
        cipher.doFinal(doc, (Element) target, true);
    }

    private void encryptElementContent(Document doc, String tag, SecretKey key) throws Exception {

        Element element = (Element) doc.getElementsByTagName(tag).item(0);

        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));

        // false → encripta solo el contenido, no la etiqueta del elemento
        cipher.doFinal(doc, element, false);
    }

    private void encryptEntireDocument(Document doc, SecretKey key) throws Exception {
        XMLCipher cipher = XMLCipher.getInstance(XMLCipher.AES_128);
        cipher.init(XMLCipher.ENCRYPT_MODE, key);
        EncryptedData ed = cipher.getEncryptedData();
        ed.setKeyInfo(new KeyInfo(doc));
        cipher.doFinal(doc, doc.getDocumentElement(), true);
    }

    private SecretKey loadKeyFromFile(HttpServletRequest request) throws Exception {
        String basePath = getServletContext().getRealPath("/");
        String keyPath = basePath.split("target")[0] + "Clave/clave.key";
        String keyStr = Files.readString(Paths.get(keyPath)).trim();
        if (keyStr.length() != 16) {
            throw new Exception("The key must be 16 characters long.");
        }
        return new SecretKeySpec(keyStr.getBytes(), AES_ALGORITHM);
    }

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

        // Convert to string for display
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
        String xmlEscaped = sw.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");

        request.setAttribute("xmlResult", xmlEscaped);
        request.setAttribute("xmlFileName", fileName);
        request.getRequestDispatcher("vista/xml.jsp").forward(request, response);
    }

}
