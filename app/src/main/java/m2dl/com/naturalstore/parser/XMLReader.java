package m2dl.com.naturalstore.parser;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import m2dl.com.naturalstore.DataEntryActivity;
import m2dl.com.naturalstore.R;

/**
 * Created by kevin marchois on 11/01/2015.
 * Récupération de la clé de détermination dans le fichier ressource associé
 */
public class XMLReader {

    private Document doc;

    /**
     * Chargement du xml dans la ressource dans l'attribut doc
     * @param dataEntryActivity,  activité utilisée pour avoir accès aux ressources
     */
    public XMLReader(DataEntryActivity dataEntryActivity) {
        XmlResourceParser xml ;
           xml = dataEntryActivity.getResources().getXml(R.xml.config);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            doc = docBuilder.newDocument();
            xml.next();
            int eventType = xml.getEventType();
            Node parent = doc.createElement(dataEntryActivity.getResources().getString(
                    R.string.rootElement));
            Node child = null;
            doc.appendChild(parent);
            xml.next();
            xml.next();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    child = doc.createElement(xml.getName());
                    parent.appendChild(child);
                    parent = child;
                } else if (eventType == XmlPullParser.END_TAG) {
                    parent = parent.getParentNode();
                } else if (eventType == XmlPullParser.TEXT) {
                    child.setTextContent(xml.getText());
                }
                eventType = xml.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Document getDoc() {
        return doc;
    }
}
