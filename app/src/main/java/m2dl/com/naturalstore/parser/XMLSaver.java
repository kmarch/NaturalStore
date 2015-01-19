package m2dl.com.naturalstore.parser;


import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import m2dl.com.naturalstore.DataEntryActivity;
import m2dl.com.naturalstore.R;

/**
 * Created by kevin marchois on 11/01/2015.
 * Sauvegarde du chemin parcouru par l'utilisateur sous la forme d'un fichier xml
 */
public class XMLSaver {

    private DataEntryActivity dataEntryActivity;
    private Document doc;
    private Node rootElement;

    /**
     * Initialisation de l'arbre que l'on va sauvegarder par la suite
     * @param dataEntryActivity, activité saisissant les meta données
     */
    public XMLSaver(DataEntryActivity dataEntryActivity) {
        this.dataEntryActivity = dataEntryActivity;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            doc = docBuilder.newDocument();
            rootElement = doc.createElement(
                    dataEntryActivity.getResources().getString(R.string.rootElement));
            doc.appendChild(rootElement);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }


    /**
     * Ajout d'un noeud à l'xml représentant le parcours de l'utilisateur
     * dans la clé de détermination
     * @param nodeName noeud à ajouter
     */
    public void appendNode(String nodeName) {
        Node newNode = doc.createElement(nodeName);
        rootElement.appendChild(newNode);
        rootElement = newNode;

    }

    /**
     * Ajout d'une valeur au fond de l'arbre créer, la valeur sea affecté au
     * dernier neud créer
     * @param value, valeur à ajouter
     */
    public void appendValue(String value) {
        rootElement.setTextContent(value);
    }

    /**
     * Sauvegarde du xml créer
     */
    public void saveXML() {
        //création du fichier s'il n'existe pas
        File file = new File(dataEntryActivity.getFilesDir(), dataEntryActivity.getResources().getString(R.string.xmlFileName));
        FileOutputStream outputStream;
        StringWriter sw = new StringWriter();
        try {
            outputStream =  dataEntryActivity.openFileOutput(
                    dataEntryActivity.getResources().getString(R.string.xmlFileName), Context.MODE_PRIVATE);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            outputStream.write(sw.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
