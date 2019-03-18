package sample.PrincipalWindow.ExportXML;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sample.Database.Repository;
import sample.Object.Conversation;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ExportXMLWindow {
    public static final String xmlFilePath = "xmlfile";

    @FXML
    private TextField idSalon;

    public void OnClickExport() throws TransformerException, ParserConfigurationException {


            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            // root element
           Element root = document.createElement(idSalon.getText());
            document.appendChild(root);

            // employee element
            Element message = document.createElement("Messages");
            List<Conversation> conversations = Repository.getMessageSalonSorted(idSalon.getText());
            for(Conversation conver : conversations) {
                String contenuMessage = conver.getContenu();
                message.appendChild(document.createTextNode("\n " + conver.getEnvoyeur() + " : "+contenuMessage));

            }
        root.appendChild(message);
            //you can also use staff.setAttribute("id", "1") for this





            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        }


}
