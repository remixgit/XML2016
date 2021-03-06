package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.DocumentPatchBuilder;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.marker.DocumentPatchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.MatchLocation;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.util.EditableNamespaceContext;

import database.Util;
import database.XMLWriterUriTemplate;
import jaxb.XMLValidation;
import play.exceptions.JavaExecutionException;
import play.mvc.Controller;
import rs.ac.uns.ftn.amandman.Amandman;
import rs.ac.uns.ftn.pravniakt.Propis;
import util.FileUtil;
import xquery.XMLReader;
import xslfo.XSLFOTransformer;
import xslfo.XSLTransformer;

public class AmendmentServices extends Controller {
	
	private static String collectionName;
	private static DatabaseClient client;
	private final static String COLLECTION = "/amendments";
	
    public static void saveAmendment(){
    	
    	System.out.println("SAVE Amendment");
    	String requestBody = params.get("body");
   
    	JSONObject obj = new JSONObject(requestBody);
    	try{
    		String text =obj.getString("text");
    		
        	try {
    			FileUtil.writeFile(Application.projectPath+"/XML2016/data/temp.xml", text);
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        
        	XMLValidation isValid = new XMLValidation();
        	boolean xmlValid = isValid.test(Application.projectPath+"/XML2016/data/amandman.xsd","amandman");
        	if(xmlValid)
        		System.out.println("XML JE VALIDAN");
        	else 
        		renderJSON(new JSONObject("{'error':'XML dokument nije validan.'}"));

        	if(xmlValid){
        		try {
    				String uri = XMLWriterUriTemplate.run(Util.loadProperties(),"amendments");
    				//renderJSON(new JSONObject("{'error':'dsasadsadsadsd'"));
    				System.out.println("[URI OF SAVED AMENDMENT]: " + uri);
    				
    				try {
    					System.out.println("[INFO] preparuje act za pisanje");
						prepareAct(uri);
						System.out.println("[INFO] zavrsio prepareAct()");
					} catch (JAXBException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				renderJSON(new JSONObject("{'success':'"+uri+"'}"));
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}    		
    	}catch(JavaExecutionException ex){
    		renderJSON((new JSONObject("{'error':'Morate uneti tekst!'}")));
    	}

	}
    
    private static void prepareAct(String uri) throws JAXBException, FileNotFoundException{
    	
    	Amandman amandman = xquery.XMLReader.getAmandman(uri);
    	System.out.println("[INFO] zavrsio citanje amandmana()");
    	Propis propis = xquery.XMLReader.getPropis("/acts/"+amandman.getOAkta()+".xml");
    	
    	propis.setBrojAmandmana(propis.getBrojAmandmana()+1);
    	JAXBContext context = JAXBContext.newInstance("rs.ac.uns.ftn.pravniakt");
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(propis, sw);
		System.out.println(sw.toString());
		
    	FileUtil.writeFile(Application.projectPath + "/XML2016/data/temp.xml", sw.toString());
    	XMLValidation isValid = new XMLValidation();
		boolean xmlValid = isValid.test(Application.projectPath + "/XML2016/data/akt.xsd", "act");
		
		if (xmlValid){
			try {
				XMLWriterUriTemplate.run(Util.loadProperties(), "acts");
				System.out.println("[URI of saved XML]: " + uri);
				Act.prepareRDF("/acts/"+propis.getOznaka()+".xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
    }
    
    
    
    public static void getAmendment(String uri){
    	// String bodyParams = params.get("body");
    			// String url = Http.Request.current().path;
    			String docUri = "/amendments/" + uri + ".xml";
    			Document doc;
    			try {
    				doc = xquery.XMLReader.run(Util.loadProperties(), docUri);
    				JAXBContext context;
    				context = JAXBContext.newInstance("rs.ac.uns.ftn.amandman");
    				Unmarshaller unmarshaller = context.createUnmarshaller();
    				Amandman amandman = (Amandman) unmarshaller.unmarshal(doc);
    				System.out.println("prosao");
    				renderJSON(amandman);
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (JAXBException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    }
	
	public static void changeStateOfAct() {
		//DELUJE SUVISNO OVA METODA
		String actNum = "8505319148387349153";
		String state = "nacelo";
		
		try {
			Document doc = XMLReader.run(Util.loadProperties(), "/acts/8505319148387349153.xml");
			JAXBContext context;
			try {
				context = JAXBContext.newInstance("rs.ac.uns.ftn.pravniakt");
				Unmarshaller unmarshaller = context.createUnmarshaller();
				Amandman amandman = (Amandman) unmarshaller.unmarshal(doc);
				System.out.println("prosao");
				//amandman.setStatus(state);
				
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void listAmendments() {

		ArrayList<LinkedHashMap<String, String>> documentsURIs = new ArrayList<LinkedHashMap<String, String>>();
		try {
			client = DatabaseClientFactory.newClient(Util.loadProperties().host, Util.loadProperties().port,
					Util.loadProperties().database, Util.loadProperties().user, Util.loadProperties().password,
					Util.loadProperties().authType);
			// Initialize query manager
			QueryManager queryManager = client.newQueryManager();

			// Query definition is used to specify Google-style query string
			StringQueryDefinition queryDefinition = queryManager.newStringDefinition();

			// Search within a specific collection
			queryDefinition.setCollections(COLLECTION);
			queryManager.setPageLength(500);
			SearchHandle results = queryManager.search(queryDefinition, new SearchHandle());
			
			// Serialize search results to the standard output
			MatchDocumentSummary matches[] = results.getMatchResults();
		
			MatchDocumentSummary result = null;
			MatchLocation locations[];
			String text;
			System.out.println("Length: " + matches.length);
			System.out.println("Results" + results.getTotalResults());
			for (int i = 0; i < matches.length; i++) {
				result = matches[i];
				System.out.println((i + 1) + ". RESULT DETAILS: ");
				System.out.println("Result URI: " + result.getUri());

				Document doc = xquery.XMLReader.run(Util.loadProperties(), result.getUri());
				JAXBContext context;
				context = JAXBContext.newInstance("rs.ac.uns.ftn.amandman");
				Unmarshaller unmarshaller = context.createUnmarshaller();
				Amandman amandman = (Amandman) unmarshaller.unmarshal(doc);
				System.out.println("prosao");
				String oznaka = amandman.getOznaka()+"";
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("oznaka", oznaka);
				map.put("uri", prepareURI(result.getUri()));
				map.put("oznakaAkta", amandman.getOAkta()+"");
				documentsURIs.add(map);
			}
			System.out.println(new JSONObject(documentsURIs));
			if (documentsURIs.isEmpty())
				renderJSON(new JSONObject("{'failure':'Lista je pazna'}"));
			else
				renderJSON(documentsURIs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String prepareURI(String uri){
		uri = uri.replace(".", "/");
		String[] splitted = uri.split("/");
		System.out.println(splitted[2]);

		return splitted[2];
	}
	
	
	public static void updateAmendment(String uri) {
		//promena statusa amandmanu, samo to radi ova metoda
		String docUri = "/amendments/" + uri + ".xml";
		try {
			client = DatabaseClientFactory.newClient(Util.loadProperties().host, Util.loadProperties().port, Util.loadProperties().database, Util.loadProperties().user, Util.loadProperties().password,
					Util.loadProperties().authType);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create a document manager to work with XML files.
		XMLDocumentManager xmlManager = client.newXMLDocumentManager();

		// Define a URI value for a document.
		String docId = docUri;

		// Defining namespace mappings
		EditableNamespaceContext namespaces = new EditableNamespaceContext();
		namespaces.put("b", "http://www.ftn.uns.ac.rs/amandman");
		namespaces.put("fn", "http://www.w3.org/2005/xpath-functions");

		// Assigning namespaces to patch builder
		DocumentPatchBuilder patchBuilder = xmlManager.newPatchBuilder();
		patchBuilder.setNamespaces(namespaces);

		String patch = "\t<b:status>povucen</b:status>\n";

		// Defining XPath context
		//
		String contextXPath1 = "/b:amandman/b:status";

		patchBuilder.replaceFragment(contextXPath1, patch);
		DocumentPatchHandle patchHandle = patchBuilder.build();

		System.out.println("[INFO] Inserting nodes to \"" + docId + "\".");
		xmlManager.patch(docId, patchHandle);

		// Release the client
		client.release();
	}
	
	public static void genPdfAma(String uri) {
		
		System.out.println("url " + uri);
		
		String text = XMLReader.getAmandmanText("/amendments/"+uri+".xml");
		
		try {
			XSLFOTransformer trans = new XSLFOTransformer();
			String path = trans.transofrmAma(text, uri);
			JSONObject obj = new JSONObject();
			obj.put("path", path);
			renderJSON(obj);

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void genXHTML(String uri) throws IOException {
		
		String docId = "/amendments/" + uri + ".xml";
		String text = XMLReader.getAmandmanText(docId);
		String response = XSLTransformer.transformAma(text);

		System.out.println("response " + response);
		String path = Application.projectPath+"/XML2016/public/tmp/xhtml/tmp.html";
		// Use relative path for Unix systems
		File f = new File(path);

		f.getParentFile().mkdirs(); 
		f.createNewFile();
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter(f));
		    writer.write(response);

		}
		catch ( IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
		JSONObject obj = new JSONObject();
		obj.put("html", "http://localhost:9000/tmp/xhtml/tmp.html");
		renderHtml(response);
		//renderJSON(obj);
		System.out.println("xhtml is genereted.");
		
		
		
	}
	
}
