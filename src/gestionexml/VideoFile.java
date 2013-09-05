package gestionexml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Ogni istanza di questa classe e' associata ad un
 * video della collezione.
 * Per ciascun video abbiamo un file xml, una immagine,
 * e il file video.
 */
public class VideoFile {

	/**
	 * Costante contenente l'estensione di un file XML
	 */
	public final static String xmlExtension = ".xml";
	
	/**
	 * Costante contenente l'estensione di un file video
	 */
	public final static String vidExtension = ".mov";
	
	/**
	 * Costante contenente l'estensione di un file immagine
	 */
	public final static String imgExtension = ".jpg";
	
	/**
	 * Directory contenente la collezione video
	 */
	private String fileDirectory = null;
	
	private String xmlFileName = null;
	private String videoFileName = null;
	private String imageFileName = null;
	private String xmlFilePath = null;
	private String xmlFileUrl = null;
	private String videoFilePath = null;
	private String imageFilePath = null;
	
	private String videoAuthor = null;
	private String videoGenre = null;
	private String videoLocation = null;
	private String videoId = null;
	private String videoTitle = null;
	private String videoFreeTextAnnotation = null;
	private String videoMediaDuration = null;

	private double videoDurata = 0;

	

	/**
	 * Costruttore: il parametro e' obbligatorio
	 * 
	 * @param fileName nome del video da associare all'istanza
	 */
	public VideoFile(String directory, String fileName) {
		
		/**
		 * pureName e' il nome del video privo di estensione. In questo modo
		 * in input possiamo avere sia il nome del file xml, sia quello del video stesso.
		 */
		String pureName = fileName.substring(0, fileName.length()-4);
		
		/**
		 * Dal pureName possiamo ricavarci i tre nomi dei file associati al video
		 */
		xmlFileName = pureName + xmlExtension;
		videoFileName = pureName + vidExtension;
		imageFileName = pureName + imgExtension;
		
		/**
		 * m7xq e' un oggetto che ci permette di interrogare file XML in formato mpeg7
		 * con facilita'.
		 */
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(directory);
		
		/**
		 * La directory dei file video e' conosciuta da m7xq
		 */
		fileDirectory = directory;
		
		/**
		 * Si sfrutta il nome dei file, e il nome della directory per
		 * calcolare i vari path di sistema relativi al video
		 */
		xmlFilePath = fileDirectory + "/" + xmlFileName;
		videoFilePath = fileDirectory + "/" + videoFileName;
		imageFilePath = fileDirectory + "/" + imageFileName;
		
		xmlFileUrl = FileManagement.URL_FILE_PREFIX + xmlFilePath;
		
		
		/**
		 * Interrogazione XQuery su xmlFilePath per ottenere gli attributi del video
		 */
		String videoInfo = m7xq.getVideoInfo(xmlFilePath);
		
		/*
		 * Estraggo qui i dati dall'albero
		 */
		Document doc = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(videoInfo)));
			builder = factory.newDocumentBuilder();		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		videoAuthor = doc.getElementsByTagName("VideoAuthor").item(0).getFirstChild().getTextContent();
		videoGenre = doc.getElementsByTagName("VideoGenre").item(0).getFirstChild().getTextContent();
		videoLocation = doc.getElementsByTagName("VideoLocation").item(0).getFirstChild().getTextContent();
		videoId = doc.getElementsByTagName("VideoId").item(0).getAttributes().item(0).toString();
		videoTitle = doc.getElementsByTagName("VideoTitle").item(0).getFirstChild().getTextContent();
		videoFreeTextAnnotation = doc.getElementsByTagName("VideoFreeTextAnnotation").item(0).getFirstChild().getTextContent();
		videoMediaDuration = doc.getElementsByTagName("VideoMediaDuration").item(0).getFirstChild().getTextContent();
		
		/*
		videoAuthor = m7xq.getVideoAuthor(xmlFilePath);
		videoGenre = m7xq.getVideoGenre(xmlFilePath);
		videoLocation = m7xq.getVideoLocation(xmlFilePath);
		videoId = m7xq.getVideoId(xmlFilePath);
		videoTitle = m7xq.getVideoTitle(xmlFilePath);
		videoFreeTextAnnotation = m7xq.getVideoFreeTextAnnotation(xmlFilePath);
		videoMediaDuration = m7xq.getVideoMediaDuration(xmlFilePath);
		*/
		
		videoId = pulisciVideoId(videoId);
		videoDurata = parseIntMediaDuration(videoMediaDuration);
	}
	
	private double parseIntMediaDuration(String mediaDuration) {
		int secondi = 0;
		
		String S = mediaDuration.substring(mediaDuration.indexOf('M')+1, mediaDuration.length()-1);
		String M = mediaDuration.substring(mediaDuration.indexOf('H')+1, mediaDuration.indexOf('M'));
		String H = mediaDuration.substring(mediaDuration.indexOf('T')+1, mediaDuration.indexOf('H'));
		
		secondi = secondi + Integer.parseInt(S);
		secondi = secondi + (Integer.parseInt(M) * 60);
		secondi = secondi + (Integer.parseInt(H) * 3600);
		
		return (double) secondi;
	}
	
	/**
	 * Procedura utile al debug. Stampa sullo standard output i dati dell'istanza di VideoFile
	 */
	public void printVideoInformation() {
		System.out.println("  xml filename = " + xmlFileName);
		System.out.println("video filename = " + videoFileName);
		System.out.println("image filename = " + imageFileName);
		System.out.println("  xml filepath = " + xmlFilePath);
		System.out.println("video filepath = " + videoFilePath);
		System.out.println("image filepath = " + imageFilePath);
		System.out.println("  video author = " + videoAuthor);
		System.out.println("   video genre = " + videoGenre);
		System.out.println("video location = " + videoLocation);
	}
	
	/**
	 * Restituisce il nome del file xml di un video ricavandolo dal suo id
	 * 
	 * @param videoId Id del video memorizzato nell'attributo /Mpeg7/Description/MultimediaContent/Video/MediaInformation/MediaProfile/MediaInstance/@id
	 * @return FileName di un file XML del video con id videoId
	 */
	public static String getXmlFileName(String videoId) {

		String xmlFileName = null;
		
		if (videoId.substring(1, 4).equals("id=")) {
			xmlFileName = videoId.substring(6,videoId.length()-1) + xmlExtension;
		}
		else {
			if (videoId.substring(0, 3).equals("id=")) {
				xmlFileName = videoId.substring(5,videoId.length()-1) + xmlExtension;
			}
			else {
				xmlFileName = videoId.substring(1,videoId.length()) + xmlExtension;
			}
		}
		
		return xmlFileName;
	}
	
	/**
	 * Pulisce l'attributo id eliminando i caratteri superflui dalla stringa.
	 * Ad esempio l'id:
	 *     id="v104"
	 * diventera'
	 *     v104
	 * 
	 * @param videoId Id del video memorizzato nell'attributo /Mpeg7/Description/MultimediaContent/Video/MediaInformation/MediaProfile/MediaInstance/@id
	 * @return id pulito da caratteri superflui
	 */
	public static String pulisciVideoId(String videoId) {

		String newVideoId = null;
		
		if (videoId.substring(1, 4).equals("id=")) {
			newVideoId = videoId.substring(5,videoId.length()-1);
		}
		else {
			if (videoId.substring(0, 3).equals("id=")) {
				newVideoId = videoId.substring(4,videoId.length()-1);
			}
			else {
				newVideoId = videoId;
			}
		}
		
		return newVideoId;
	}
	
	/**
	 * Restituisce in output tutti i //RelatedMaterial//MediaUri/text() relativi
	 * al video.
	 * 
	 * @return Lista dei RelatedMaterial
	 */
	public ArrayList<String> getRelatedVideo() {
		
		ArrayList<String> relatedVideo = new ArrayList<String>();
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		
		relatedVideo = m7xq.getAllResult(this.xmlFilePath, "//RelatedMaterial/MediaLocator/MediaUri/text()");
		
		
		return relatedVideo;
	}
	
	/**
	 * Restituisce la lista dei file dei video related, costruita leggendo le informazioni
	 * della descrizione XMl del video
	 * 
	 * @return lista di VideoFile correlati con il video
	 */
	public ArrayList<VideoFile> getRelatedVideoFile() {
		ArrayList<VideoFile> videoFiles = new ArrayList<VideoFile>();
		
		ArrayList<String> videos = getRelatedVideo();
		for (String fileName : videos) {
			videoFiles.add(new VideoFile(fileDirectory, fileName));			
		}
		
		return videoFiles;
	}
	
	/**
	 * Controlla se c'e' gia' un //RelatedMaterial//MediaUri/text() uguale
	 * a mediaUriText nella descrizione del video.
	 * 
	 * @return true se mediaUriText e' gia' un RelatedMaterial del video
	 */
	private boolean isRelatedMaterial(String mediaUriText) {
		
		if (this.getVideoFileName().equals(mediaUriText)) {
			return true;
		}
		
		ArrayList<String> relatedVideo = new ArrayList<String>();
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		
		relatedVideo = m7xq.getAllResult(this.xmlFilePath, "//RelatedMaterial/MediaLocator/MediaUri[text() eq '" + mediaUriText + "']");
		
		boolean founded = false;
		
		if (relatedVideo.size() > 0)
			founded = true;
		
		return founded;
	}
	
	/**
	 * Aggiunge al file XML del video, il tag <RelatedMaterial> con il contenuto madiaUriText.
	 * Non ha effetto se mediaUriText e' gia' un related material del video.
	 * 
	 * @param mediaUriText indirizzo del 
	 * @return
	 */
	public boolean addRelatedMaterial(String mediaUriText) {
		
		/**
		 *  Controlla se il file mediaUriText e' gia' contenuto nei RelatedMaterial
		 *  o se mediaUriText e' proprio il video legato all'oggetto istansta di questa classe.
		 *  In entrambi i casi non e' lecito aggiungere mediaUriText tra il RelatedMaterial
		 */
		if (isRelatedMaterial(mediaUriText))
			return false;
		
		try {
			
			/**
			 *  Apro il file XML da modificare con le API DOM.
			 */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFileUrl);
			
			
			/**
			 *  Aggiungo il tag <RelatedMaterial> nel file XML aperto con DOM
			 */
			NodeList classificationNodeList = doc.getElementsByTagName("CreationInformation");
			
			Node creationInformation = null; //qui sara memorizzato l'elemento CreationInformatio figlio di Video
			boolean trovatoVideo = false; //true se trovato l'elemento
			int i=0;
			while (!trovatoVideo && i<classificationNodeList.getLength()) { //fin quando non trovo l'elemento
				creationInformation = classificationNodeList.item(i); // prendo il figlio i-esimo 
				if (creationInformation.getNodeType() == Node.ELEMENT_NODE) { // se e' un nodo di tipo elemento
					if (creationInformation.getParentNode().getNodeName().equals("Video")) { // se il padre e' <Video>
						trovatoVideo=true; // Ho trovato l'elemento che cercavo
					}
				}
			}
			
			Element relatedMaterial = doc.createElement("RelatedMaterial");
			creationInformation.appendChild(relatedMaterial);
			
			Element mediaLocator = doc.createElement("MediaLocator");
			relatedMaterial.appendChild(mediaLocator);
			
			Element mediaUri = doc.createElement("MediaUri");
			mediaUri.appendChild(doc.createTextNode(mediaUriText));
			mediaLocator.appendChild(mediaUri);
			
			
			/**
			 *  Scrivo il file XML modificato sul disco; questa operazione deve essere seguita
			 *  da un refresh del database XML.
			 */
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			doc.normalize();
			Source source = new DOMSource(doc);
			Result result = new StreamResult(xmlFilePath);
			tFormer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/**
		 * Ricarico il database XML, in seguito ad una modifica dei file costituenti il databse.
		 */
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		m7xq.refreshMpeg7Database();
				
		return true;
	}
	
	/**
	 * Restituisce il prossimo id libero per i tag <VideoSegment>
	 * E' utile per taggare i video senza preoccuparsi di gestire gli id.
	 * @return
	 */
	private String nextVideoSegmentId() {
		
		int max = 0;
		
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		ArrayList<String> ids = m7xq.getAllResult(this.xmlFilePath, "//VideoSegment/@id");
		Iterator<String> idsIterator = ids.iterator();
		int n = 0;
		String currentId = null;
		while (idsIterator.hasNext()) {
			currentId = idsIterator.next();
			n = Integer.parseInt(currentId.substring(8, currentId.length()-1));
			if (n>max) {
				max = n;
			}
		}
		
		
		return "Seg" + Integer.toString(max+1);
	}
	
	/**
	 * Aggiunge un tag TemporalDecomposition alla descrizione del video.
	 * 
	 * @param idParent id del segmento video padre. Se e' null si inserisce il tag come primo TemporalDecomposition
	 * @param description Descrizione del tag
	 * @param time Tempo di inizio della parte di video taggata
	 * @param duration Durata della parte di video taggata
	 * @param keywordList Lista di kayword del tag (opzionale)
	 * 
	 * @return true se l'operazione e' andata a buon fine
	 */
	public boolean addTemporalDecomposition(String idParent, String description, String time, String duration, ArrayList<String> keywordList) {
		
		String newId = nextVideoSegmentId();
		
		try {
			
			// APRO IL FILE XML
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.parse(FileManagement.URL_FILE_PREFIX + xmlFilePath);
			
			
			// AGGIUNGO IL TAG <TemporalDecomposition> E TUTTO IL SUO SOTTO-ALBERO
			
			/**
			 *  Qui sara memorizzato il nodo padre, che dovra' contenere il
			 *  tag <TemporalDecomposition>.
			 *  Esso corrisponde con <Video> se il segmento di video taggato
			 *  non e' figlio di nessun altro.
			 */
			Node parentNode = null;
			boolean parentFounded = false; //true se trovato l'elemento
			
			if (idParent == null) { // Si deve trovare il nodo <Video>
				NodeList videoNodeList = doc.getElementsByTagName("Video");
				int i=0;
				while (!parentFounded && i<videoNodeList.getLength()) { //fin quando non trovo l'elemento
					parentNode = videoNodeList.item(i); // prendo il figlio i-esimo 
					i++;
					if (parentNode.getNodeType() == Node.ELEMENT_NODE) { // se e' un nodo di tipo elemento
						parentFounded=true; // Ho trovato l'elemento che cercavo
					}
				}
			}
			else { // Si deve trovare il nodo <VideoSegment> con id idParent
				NodeList videoSegmentNodeList = doc.getElementsByTagName("VideoSegment");
				int i=0;
				while (!parentFounded && i<videoSegmentNodeList.getLength()) { //fin quando non trovo l'elemento
					parentNode = videoSegmentNodeList.item(i);
					i++;
					if (parentNode.getNodeType() == Node.ELEMENT_NODE) { // se e' un nodo di tipo elemento
						if (parentNode.getAttributes().getNamedItem("id").getNodeValue().equals(idParent)) {
							parentFounded = true; // Ho trovato l'elemento che cercavo
						}
					}
				}
			}
			
			if (!parentFounded) {
				return false;
			}

			// Element ...->Video->TemporalDecomposition
	        Element temporalDecomposition = doc.createElement("TemporalDecomposition");
	        parentNode.appendChild(temporalDecomposition);
	        
	        // Element ...->Video->TemporalDecomposition->VideoSegment
	        Element videoSegment = doc.createElement("VideoSegment");
	        videoSegment.setAttribute("id", newId);
	        temporalDecomposition.appendChild(videoSegment);
	        
	        // Element ...->Video->TemporalDecomposition->VideoSegment->TextAnnotation
	        Element textAnnotation = doc.createElement("TextAnnotation");
	        videoSegment.appendChild(textAnnotation);
	        
	        // Element ...->Video->TemporalDecomposition->VideoSegment->TextAnnotation->FreeTextAnnotation
	        Element freeTextAnnotation = doc.createElement("FreeTextAnnotation");
	        freeTextAnnotation.appendChild(doc.createTextNode(description));
	        textAnnotation.appendChild(freeTextAnnotation);
	        
	        if (keywordList!=null && keywordList.size() > 0) {
		        // Element ...->Video->TemporalDecomposition->VideoSegment->TextAnnotation->KeywordAnnotation
		        Element keywordAnnotation = doc.createElement("KeywordAnnotation");
		        textAnnotation.appendChild(keywordAnnotation);
		        
		        Iterator<String> keywordIterator = keywordList.iterator();
		        while (keywordIterator.hasNext()) {
			        // Element ...->Video->TemporalDecomposition->VideoSegment->TextAnnotation->KeywordAnnotation->Keyword
			        Element keyword = doc.createElement("Keyword");
			        keyword.appendChild(doc.createTextNode(keywordIterator.next()));
			        keywordAnnotation.appendChild(keyword);
		        }
	        }
	        
	        // Element ...->Video->TemporalDecomposition->VideoSegment->MediaTime
	        Element mediaTime1 = doc.createElement("MediaTime");
	        videoSegment.appendChild(mediaTime1);

	        // Element ...->Video->TemporalDecomposition->VideoSegment->MediaTime->MediaTimePoint
	        Element mediaTimePoint1 = doc.createElement("MediaTimePoint");
	        mediaTimePoint1.appendChild(doc.createTextNode(time));
	        mediaTime1.appendChild(mediaTimePoint1);
	        
	        // Element ...->Video->TemporalDecomposition->VideoSegment->MediaTime->MediaDuration
	        Element mediaDuration1 = doc.createElement("MediaDuration");
	        mediaDuration1.appendChild(doc.createTextNode(duration));
	        mediaTime1.appendChild(mediaDuration1);
	        
	        
	        
			// SCRIVO IL FILE SUL FISCO
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			doc.normalize();
			Source source = new DOMSource(doc);
			Result result = new StreamResult(xmlFilePath);
			tFormer.transform(source, result);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		m7xq.refreshMpeg7Database();
		
		return true;
	}
	
	/**
	 * Restituisce la lista degli shot del video
	 * @return lista degli shot del video
	 */
	public ArrayList<TemporalDecomposition> getTemporalDecomposition() {
		
		String xquery = "" +
				" for $video in doc('" + xmlFilePath + "')//Video " +
				" for $td in $video/child::node()[./name() eq 'TemporalDecomposition'] " +
				" return $td ";
		
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		ArrayList<String> results = m7xq.getAllResult(xquery);
		
		ArrayList<TemporalDecomposition> tds = new ArrayList<TemporalDecomposition>();
		
		for (String tdTree : results) {
			TemporalDecomposition td = Mpeg7XQuerier.createTemporalDecomposition(videoId, tdTree);
			tds.add(td);
		}
		
		return tds;
	}
	
	/**
	 * Restituisce lo shot video con l'id
	 * 
	 * @param id id dello shot ch si vuole cercare
	 * 
	 * @return shot cercato
	 */
	public TemporalDecomposition getTemporalDecomposition(String id) {
		
		String xquery = "" +
				" for $td in //TemporalDecomposition[./VideoSegment/@id eq '"+id+"'] " +
				" return $td ";
		
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(fileDirectory);
		ArrayList<String> results = m7xq.getAllResult(xquery);
		
		TemporalDecomposition td = null;
		
		if (results.size() > 0) {
			String tdTree = results.get(0);
			td = Mpeg7XQuerier.createTemporalDecomposition(videoId, tdTree);
		}
		
		return td;
	}

	public String getFileDirectory() {
		return fileDirectory;
	}

	public String getXmlFileName() {
		return xmlFileName;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public String getXmlFilePath() {
		return xmlFilePath;
	}

	public String getVideoFilePath() {
		return videoFilePath;
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public String getVideoAuthor() {
		return videoAuthor;
	}

	public String getVideoGenre() {
		return videoGenre;
	}

	public String getVideoLocation() {
		return videoLocation;
	}
	
	public String getVideoTitle() {
		return videoTitle;
	}
	
	public String getVideoId() {
		return videoId;
	}

	public String getVideoFreeTextAnnotation() {
		return videoFreeTextAnnotation;
	}
	
	public String getVideoMediaDuration() {
		return videoMediaDuration;
	}

	public double getVideoDurata() {
		return videoDurata;
	}
	
	/**
	 * Metodo statico utile per i test della classe
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		VideoFile videoFile = new VideoFile("file/video", "20090201_video_15213221.xml");
		//videoFile.printVideoInformation();
		
//		videoFile.addRelatedMaterial("20090201_video_17483841.xml");
//		ArrayList<String> al = videoFile.getRelatedVideo();
//		for (int i=0; i<al.size(); i++) {
//			System.out.println(al.get(i));
//		}
		
//		ArrayList<String> keywords = new ArrayList<String>();
//		keywords.add("qui");
//		keywords.add("quo");
//		keywords.add("qua");
//		videoFile.addTemporalDecomposition("Seg4", "eee", "T00:00:24", "PT9S13389N25000F", keywords);
		
		for (TemporalDecomposition td : videoFile.getTemporalDecomposition())
			td.printDebugInformation();
		
//		System.out.println(videoFile.nextVideoSegmentId());
		
	}
}
