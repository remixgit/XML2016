package xquery;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;










import rs.ac.uns.ftn.amandman.Amandman;
import rs.ac.uns.ftn.pravniakt.Propis;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;

import database.Util;
import database.Util.ConnectionProperties;

/**
 * 
 * [PRIMER 4]
 * 
 * Primer demonstrira čitanje DOM stabla XML dokumenta i njegovih metapodataka
 * (podaci o kolekcijama) iz MarkLogic XMLDB upotrebom DOMHandle klase Java
 * API-ja. Primer koristi standardni Transformer API za prikaz DOM stabla
 * učitanog XML dokumenta na standardni izlaz.
 * 
 */
public class XMLReader {

	private static DatabaseClient client;
	
	private static TransformerFactory transformerFactory;
	
	static {
		transformerFactory = TransformerFactory.newInstance();
	}
	
	public static Document run(ConnectionProperties props, String path) throws FileNotFoundException {
		
		System.out.println("[INFO] " + XMLReader.class.getSimpleName());
		
		// Initialize the database client
		if (props.database.equals("")) {
			System.out.println("[INFO] Using default database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.user, props.password, props.authType);
		} else {
			System.out.println("[INFO] Using \"" + props.database + "\" database.");
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password, props.authType);
		}
		
		// Create a document manager to work with XML files.
		XMLDocumentManager xmlManager = client.newXMLDocumentManager();

		// A handle to receive the document's content.
		DOMHandle content = new DOMHandle();
		
		// A metadata handle for metadata retrieval
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		
		// A document URI identifier. 
		String docId = path;
		
		// Write the document to the database
		System.out.println("[INFO] Retrieving \"" + docId + "\" from "
				+ (props.database.equals("") ? "default" : props.database)
				+ " database.");
		
		xmlManager.read(docId, metadata, content);

		// Retrieving a document node form DOM handle.
		Document doc = content.get();
		client.release();
		return doc;

	}
	
	public static Propis getPropis(String docId) {
		Propis propis = null;
		try {
			
			Document doc = XMLReader.run(Util.loadProperties(), docId);
			JAXBContext context;
			context = JAXBContext.newInstance("rs.ac.uns.ftn.pravniakt");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			propis = (Propis) unmarshaller.unmarshal(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return propis;
	}
	
	public static String getPropisText(String docId) {
		Propis propis = XMLReader.getPropis(docId);
		JAXBContext context;
		StringWriter sw = null;
		try {
			context = JAXBContext.newInstance("rs.ac.uns.ftn.pravniakt");
			Marshaller marshaller = context.createMarshaller();
			sw = new StringWriter();
			marshaller.marshal(propis, sw);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		if(sw != null) {
			return sw.toString();
		} else {
			return null;
		}
	}
	
	public static Amandman getAmandman(String docId) {
		Amandman amandman = null;
		System.out.println("[INFO] cita amndman" + docId);
		try {
			Document doc = XMLReader.run(Util.loadProperties(), docId);
			JAXBContext context;
			context = JAXBContext.newInstance("rs.ac.uns.ftn.amandman");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			amandman = (Amandman) unmarshaller.unmarshal(doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return amandman;
	}
	
	public static String getAmandmanText(String docId) {
		Amandman amandman = XMLReader.getAmandman(docId);
		JAXBContext context;
		StringWriter sw = null;
		try {
			context = JAXBContext.newInstance("rs.ac.uns.ftn.amandman");
			Marshaller marshaller = context.createMarshaller();
			sw = new StringWriter();
			marshaller.marshal(amandman, sw);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		if(sw != null) {
			return sw.toString();
		} else {
			return null;
		}
	}
	
	private static void transform(Node node, OutputStream out) {
		try {

			// Kreiranje instance objekta zaduzenog za serijalizaciju DOM modela
			Transformer transformer = transformerFactory.newTransformer();

			// Indentacija serijalizovanog izlaza
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Nad "source" objektom (DOM stablo) vrši se transformacija
			DOMSource source = new DOMSource(node);

			// Rezultujući stream (argument metode) 
			StreamResult result = new StreamResult(out);

			// Poziv metode koja vrši opisanu transformaciju
			transformer.transform(source, result);
			
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
