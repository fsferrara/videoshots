package gestionexml;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Facilita l'interrogazione XQuery di documenti XML in formato Mpeg-7
 */
public class Mpeg7XQuerier {
	
	/**
	 * Directory contenente i file XML in formato Mpeg-7
	 */
	String defaultDir = null;
	
	/**
	 * prologo della query per i file XML in formato Mpeg-7
	 */
	final static String prologoQuery = ""
			+ "declare default element namespace \"urn:mpeg:mpeg7:schema:2001\"; "
			+ "declare  namespace mpeg7 = \"urn:mpeg:mpeg7:schema:2001\"; "
			+ "declare  namespace xsi = \"http://www.w3.org/2001/XMLSchema-instance\"; ";
	
	/**
	 * Costruttore
	 */
	public Mpeg7XQuerier(String defaultDirectory) {
		defaultDir = defaultDirectory;
	}
	
	/**
	 * Restituisce l'autore di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return autore del video
	 */
	public String getVideoAuthor(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Creator/Role/Name/text()";
		return getFirstResult(mpegFilePath, query);
	}
	
	/**
	 * Restituisce il genere di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return genere del video
	 */
	public String getVideoGenre(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Classification/Genre/Name/text()";
		return getFirstResult(mpegFilePath, query);
	}
	
	/**
	 * Restituisce la location di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return location del video
	 */
	public String getVideoLocation(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/CreationCoordinates/Location/Name/text()";
		return getFirstResult(mpegFilePath, query);
	}
	
	/**
	 * Restituisce il titolo di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return titolo del video
	 */
	public String getVideoTitle(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Title/text()";
		return getFirstResult(mpegFilePath, query);
	}
	
	/**
	 * Restituisce la descrizione di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return descrizione del video
	 */
	public String getVideoFreeTextAnnotation(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Abstract/FreeTextAnnotation/text()";
		return getFirstResult(mpegFilePath, query);
	}
	
	/**
	 * Restituisce la durata di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return durata del video
	 */
	public String getVideoMediaDuration(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/MediaTime/MediaDuration/text()";
		return getFirstResult(mpegFilePath, query);
	}

	/**
	 * Restituisce l'id di un dato video
	 * @param mpegFilePath file Mpeg-7 contenente la descrizione del video
	 * @return id del video
	 */
	public String getVideoId(String mpegFilePath) {
		String query = "/Mpeg7/Description/MultimediaContent/Video/MediaInformation/MediaProfile/MediaInstance/@id";
		String id = getFirstResult(mpegFilePath, query);
		
		return id.substring(5, id.length()-1);
	}
	
	/**
	 * Esegue una xquery su un file XML e restituisce il primo risultato ottenuto
	 * 
	 * @param mpegFilePath file Mpeg-7 sul quale eseguire la query
	 * @param query XQuery da eseguire
	 * 
	 * @return Stringa contenente il primo risultato, oppure null in caso di insuccesso
	 */
	public String getFirstResult(String mpegFilePath, String query) {
		
		String firstResult = null;
		
		ArrayList<String> results = getAllResult(mpegFilePath, query);
		if (results != null) {
			if (results.size() > 0) {
				firstResult = results.get(0);
			}
		}

		return firstResult;
	}
	
	/**
	 * Esegue una xquery sul database XML e restituisce il primo risultato ottenuto
	 * 
	 * @param query XQuery da eseguire
	 * 
	 * @return Stringa contenente il primo risultato, oppure null in caso di insuccesso
	 */
	public String getFirstResult(String query) {
		
		String firstResult = null;
		
		ArrayList<String> results = getAllResult(query);
		if (results != null) {
			if (results.size() > 0) {
				firstResult = results.get(0);
			}
		}

		return firstResult;
	}
	
	/**
	 * Esegue una xquery su un file XML
	 * 
	 * @param mpegFilePath file Mpeg-7 sul quale eseguire la query
	 * @param query XQuery da eseguire
	 * 
	 * @return ArrayList<String> contenente i risultati della query oppure null
	 */
	public ArrayList<String> getAllResult(String mpegFilePath, String query) {
		
		ArrayList<String> results = getAllResult("doc('" + mpegFilePath + "')" + query);

		return results;
	}
	
	/**
	 * Esegue una xquery sul XML creato da tutti i file XML della collezione
	 * 
	 * @param query XQuery da eseguire
	 * 
	 * @return ArrayList<String> contenente i risultati della query oppure null
	 */
	public ArrayList<String> getAllResult(String query) {
		
		XmlDatabaseQuerier xdq = XmlDatabaseQuerier.getInstance(defaultDir);
		
		ArrayList<String> results = xdq.executeXQuery(prologoQuery + query);

		return results;
	}
	
	
	/**
	 * Restiuisce le principali informazioni di un video, in una struttura XML
	 */
	public String getVideoInfo(String mpegFilePath) {
		
		String query = "" +
				" let $videoAuthor := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Creator/Role/Name/text() " +
				" let $videoGenre := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Classification/Genre/Name/text() " +
				" let $videoLocation := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/CreationCoordinates/Location/Name/text() " +
				" let $videoId := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/MediaInformation/MediaProfile/MediaInstance/@id " +
				" let $videoTitle := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Title/text() " +
				" let $videoFreeTextAnnotation := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Abstract/FreeTextAnnotation/text() " +
				" let $videoMediaDuration := doc('" + mpegFilePath + "')/Mpeg7/Description/MultimediaContent/Video/MediaTime/MediaDuration/text() " +
				" return <VideoInfo> " +
				"     <VideoAuthor>{$videoAuthor}</VideoAuthor> " +
				"     <VideoGenre>{$videoGenre}</VideoGenre> " +
				"     <VideoLocation>{$videoLocation}</VideoLocation> " +
				"     <VideoId>{$videoId}</VideoId> " +
				"     <VideoTitle>{$videoTitle}</VideoTitle> " +
				"     <VideoFreeTextAnnotation>{$videoFreeTextAnnotation}</VideoFreeTextAnnotation> " +
				"     <VideoMediaDuration>{$videoMediaDuration}</VideoMediaDuration> " +
				"   </VideoInfo> " +
				"";
		
		String result = getFirstResult(query);
		
		if (result != null)
			result = result.replace("xmlns=\"urn:mpeg:mpeg7:schema:2001\"", "");

		return result;
	}
	
	
	/**
	 * Restituisce la lista, in formato XML, di tutti gli autori
	 * @return xml che descrive il menu
	 */
	public String getMenuAuthor() {
		String query = "" +
				" <menuAutori> " +
				" { for $aut in fn:distinct-values(/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/Creator/Role/Name/text()) " +
				" return <autore> {$aut} </autore> } " +
				" </menuAutori> ";
		String result=getFirstResult(query);
		if (result!=null)
			return result.replace("xmlns=\"urn:mpeg:mpeg7:schema:2001\"", "");
		else
			return "";
	}
	
	/**
	 * Restituisce la lista, in formato XML, di tutti le categorie (genre)
	 * @return xml che descrive il menu
	 */
	public String getMenuGenre() {
		String query = "" +
				" <menuGeneri> " +
				" { for $gen in fn:distinct-values(/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Classification/Genre/Name/text()) " +
				" return <genere codice=\"{$gen}\"> {$gen} </genere> } " +
				" </menuGeneri> ";
		
		String result=getFirstResult(query);
		if (result!=null)
			return result.replace("xmlns=\"urn:mpeg:mpeg7:schema:2001\"", "");
		else
			return "";
	}

	/**
	 * Restituisce la lista, in formato XML, di tutti le locations
	 * @return xml che descrive il menu
	 */
	public String getMenuLocations() {
		String query = "" +
				" <menuLocation> " +
				" { for $loc in fn:distinct-values(/Mpeg7/Description/MultimediaContent/Video/CreationInformation/Creation/CreationCoordinates/Location/Name/text()) " +
				" return <location> {$loc} </location> } " +
				" </menuLocation> ";
		
		String result=getFirstResult(query);
		if (result!=null)
			return result.replace("xmlns=\"urn:mpeg:mpeg7:schema:2001\"", "");
		else
			return "";
	}
	
	/**
	 * Aggiorna il Database XML.
	 * Questo metodo deve essere chiamato ogni qualcota si effettuano modifiche
	 * ai file XML fecenti parte del Database XML
	 */
	public void refreshMpeg7Database() {
		XmlDatabaseQuerier xdq = XmlDatabaseQuerier.getInstance(defaultDir);
		xdq.refreshXmlDatabase();
	}
	
	private static TemporalDecomposition createTemporalDecomposition(String videoId, Node temporalDecomposition) {

		String annotation = null;
		String time = null;
		String duration = null;
		String id = null;
		
		NodeList nodeList = temporalDecomposition.getChildNodes();
		Node tmp=null;
		int j=0;
		Node videoSegment = null;
		while (j<nodeList.getLength()) {
			tmp = nodeList.item(j++);
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("VideoSegment"))) {
				videoSegment = tmp;
			}	
		}
		
		id = videoSegment.getAttributes().getNamedItem("id").getNodeValue();
		
		ArrayList<TemporalDecomposition> tds = new ArrayList<TemporalDecomposition>();
		Node textAnnotation = null;
		Node mediaTime = null;
		nodeList = videoSegment.getChildNodes();
		j=0;
		while (j<nodeList.getLength()) {
			tmp = nodeList.item(j++);
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("TextAnnotation"))) {
				textAnnotation=tmp;
			}
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("MediaTime"))) {
				mediaTime=tmp;
			}
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("TemporalDecomposition"))) {
				tds.add(createTemporalDecomposition(videoId, tmp));
			}
		}
		
		//Processo textAnnotation
		nodeList = textAnnotation.getChildNodes();
		j=0;
		Node freeTextAnnotation = null;
		while (j<nodeList.getLength()) {
			tmp = nodeList.item(j++);
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("FreeTextAnnotation"))) {
				freeTextAnnotation=tmp;
			}
		}
		
		annotation = freeTextAnnotation.getFirstChild().getTextContent();
		
		//Processo mediaTime
		nodeList = mediaTime.getChildNodes();
		j=0;
		Node mediaTimePoint = null;
		Node mediaDuration = null;
		while (j<nodeList.getLength()) {
			tmp = nodeList.item(j++);
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("MediaTimePoint"))) {
				mediaTimePoint=tmp;
			}
			if ((tmp.getNodeType() == Node.ELEMENT_NODE) && (tmp.getNodeName().equals("MediaDuration"))) {
				mediaDuration=tmp;
			}
		}
		
		time = mediaTimePoint.getFirstChild().getTextContent();
		duration = mediaDuration.getFirstChild().getTextContent();
		
		TemporalDecomposition td = new TemporalDecomposition(id, videoId, annotation, time, duration);
		for (TemporalDecomposition tdTmp : tds)
			td.addChildren(tdTmp);
		
		return td;
	}
	
	/**
	 * Crea un oggetto TemporalDecomposition, prendendo i dati dall'albero XML tdXmlTree
	 * 
	 * @param videoId id del video, e' un'informazione che va scritta in TemporalDecomposition
	 * @param tdXmlTree descrizione XML di uno shot
	 * 
	 * @return oggetto TemporalDecomposition contenente le informazioni estratte dall'XML
	 */
	public static TemporalDecomposition createTemporalDecomposition(String videoId, String tdXmlTree) {

		Document doc = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(tdXmlTree)));
			builder = factory.newDocumentBuilder();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if (doc == null)
			return null;

		return createTemporalDecomposition(videoId, doc.getFirstChild());
	}
	
	/**
	 * Metodo statico utile per i test della classe
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier("WebContent/file/video");
		m7xq.refreshMpeg7Database();
		
	}
}
