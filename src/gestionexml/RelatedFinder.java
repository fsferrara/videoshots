package gestionexml;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Trova caratteristiche in comune tra i file XML di tipo Mpeg-7
 */
public class RelatedFinder {
	
	private String defaultDirectory = null;
	
	/**
	 * Costruttore della classe
	 * 
	 * @param defaultDirectory direcotry di default contenente i file XML
	 */
	public RelatedFinder(String defaultDirectory) {
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(defaultDirectory);
		m7xq.refreshMpeg7Database();
		this.defaultDirectory = defaultDirectory;
	}
	
	/**
	 * Restituisce i video correlati con mpegFileName cercandoli nel Database XML.
	 * Due video sono considerati related se hanno almeno due caratteristiche in comune tra:
	 *  autore [//Video/CreationInformation/Creation/Creator/Role/Name]
	 *  categoria (o genere) [//Video/CreationInformation/Classification/Genre/Name]
	 *  location [//Video/CreationInformation/Creation/CreationCoordinates/Location/Name]
	 * 
	 * @param mpegFileName FileName del video per il quale si vogliono cercare i video related
	 * 
	 * @return Lista contenente gli ID dei video related con mpegFileName
	 */
	public ArrayList<String> getRelatedMaterial(String mpegFileName) {
		
		/**
		 * video e' l'oggetto VideoFile per il quale vogliamo cercare i videos correlati (related)
		 */
		VideoFile video = new VideoFile(defaultDirectory, mpegFileName);
		
		/**
		 * Con questa query ci saranno restituiti gli id di tutti i video correlati con l'oggetto video
		 */
		String xquery = "" +
				" for $video in /Mpeg7/Description/MultimediaContent/Video " +
				" let $aut := if ($video/CreationInformation/Creation/Creator/Role/Name/text() = '" + video.getVideoAuthor() + "') then 1 else 0 " +
				" let $gen := if ($video/CreationInformation/Classification/Genre/Name/text() = '" + video.getVideoGenre() + "') then 1 else 0 " +
				" let $loc := if ($video/CreationInformation/Creation/CreationCoordinates/Location/Name/text() = '" + video.getVideoLocation() + "') then 1 else 0 " +
				" where $aut + $gen + $loc gt 1 " +
				" return $video/MediaInformation/MediaProfile/MediaInstance/@id ";
		
		// Eseguo la query
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(defaultDirectory);
		ArrayList<String> results = m7xq.getAllResult(xquery);
		
		return results;
	}
	
	/**
	 * Dato un file XML in formato Mpeg7, cerca tutti i file che hanno almeno due caratteristiche in comune tra:
	 *  autore [//Video/CreationInformation/Creation/Creator/Role/Name]
	 *  categoria (o genere) [//Video/CreationInformation/Classification/Genre/Name]
	 *  location [//Video/CreationInformation/Creation/CreationCoordinates/Location/Name]
	 *  
	 * Quando la ricerca ha successo i due file sono collegati tra loro modficando la descrizione XML di entrambi
	 * aggiungendo un tag <RelatedMaterial>
	 * 
	 * @param mpegFileName
	 */
	public void writeRelatedMaterial(String mpegFileName) {
		
		VideoFile video = new VideoFile(defaultDirectory, mpegFileName);
		VideoFile relatedVideo = null;
		
		ArrayList<String> relatedFileIds = getRelatedMaterial(mpegFileName);
		for (String fileId : relatedFileIds) {
			relatedVideo = new VideoFile(defaultDirectory, VideoFile.getXmlFileName(fileId));
			video.addRelatedMaterial(relatedVideo.getVideoFileName());
			relatedVideo.addRelatedMaterial(video.getVideoFileName());
		}
	}
	
	/**
	 * Chiama il metodo writeRelatedMaterial(String mpegFileName) per tutti i file XML della collezione
	 */
	public void writeAllRelatedMaterial() {

		FileManagement fm = new FileManagement(defaultDirectory);
	    
	    ArrayList<String> listaFile = fm.fileList(defaultDirectory, VideoFile.xmlExtension);
	    for (String MpegFileName : listaFile) {
	    	writeRelatedMaterial(MpegFileName);
	    }
	}
	
	/**
	 * Effettua la ricerca nel Database XML in base ai seguenti criteri:
	 *  autore [//Video/CreationInformation/Creation/Creator/Role/Name]
	 *  categoria (o genere) [//Video/CreationInformation/Classification/Genre/Name]
	 *  location [//Video/CreationInformation/Creation/CreationCoordinates/Location/Name]
	 *  
	 * Se sono specificati piu' criteri di ricerca, essi sono messi in AND, restituendo in
	 * output solo i video che soddisfatno tutti i criteri di ricerca
	 * 
	 * @param autore Dato che si vuole cercare; null ignora questo campo
	 * @param genre Dato che si vuole cercare; null ignora questo campo
	 * @param location Dato che si vuole cercare; null ignora questo campo
	 * 
	 * @return  Lista contenente gli ID dei video che soddisfano i criteri di ricerca
	 */
	public ArrayList<String> searchVideo(String autore, String genre, String location) {

		/*
		 * Query da eseguire costruita in modo dinamico, mettendo in AND i vari criteri di ricerca
		 */
		String xquery = " "
				+ " for $video in /Mpeg7/Description/MultimediaContent/Video "
				+ " where true() ";
		
		if (autore != null)
			xquery = xquery + " and $video/CreationInformation/Creation/Creator/Role/Name[text() eq '" + autore + "'] ";

		if (genre != null)
			xquery = xquery + " and $video/CreationInformation/Classification/Genre/Name[text() eq '" + genre + "'] ";

		if (location != null)
			xquery = xquery + " and $video/CreationInformation/Creation/CreationCoordinates/Location/Name[text() eq '" + location + "'] ";

		xquery = xquery + " return $video/MediaInformation/MediaProfile/MediaInstance/@id ";

		// Eseguo la query
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(defaultDirectory);
		ArrayList<String> results = m7xq.getAllResult(xquery);

		// In results vi saranno gli id dei video rilevanti per la ricerca
		return results;
	}
	
	/**
	 * Effettua la ricerca nelle descrizioni degli shot video.
	 * 
	 * @param descrizione striga che si sta cercando
	 * 
	 * @return lista degli shot che soddisfano i criteri di ricerca
	 */
	public ArrayList<TemporalDecomposition> searchShot(String descrizione) {
		
		//Query da eseguire
		String xquery = " " +
				" for $video in /Mpeg7/Description/MultimediaContent/Video " +
				" for $td in $video//TemporalDecomposition " +
				" where fn:contains($td/VideoSegment/TextAnnotation/FreeTextAnnotation/text(), '" + descrizione + "') " +
				" return ($video/MediaInformation/MediaProfile/MediaInstance/@id, $td) ";
		
		//Creiamo un nuovo oggetto per eseguire le query
		Mpeg7XQuerier m7xq = new Mpeg7XQuerier(defaultDirectory);
		ArrayList<String> results = m7xq.getAllResult(xquery);
		
		/*
		 * In questo ArrayList vi saranno tutti gli shot rilevanti per la ricerca
		 */
		ArrayList<TemporalDecomposition> tds = new ArrayList<TemporalDecomposition>();
		
		for (int i=0 ; i<results.size() ; i=i+2) {
			/*
			 * I risultati di questa ricerca vanno esaminati due per volta: sono delle
			 * coppie <idVideo, alberoShot>
			 */
			String videoId = results.get(i);
			String tdTree = results.get(i+1);
			
			/*
			 * Con il metodo statico Mpeg7XQuerier.createTemporalDecomposition(videoId, tdTree)
			 * creiamo un oggetto di TemporalDecomposition da poter includere nei risultati
			 */
			TemporalDecomposition td = Mpeg7XQuerier.createTemporalDecomposition(videoId, tdTree);
			tds.add(td);
		}
		
		return tds;
	}
	
	/**
	 * Metodo statico utile per i test della classe
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		RelatedFinder rf = new RelatedFinder("WebContent/file/video");
		ArrayList<String> results = rf.searchVideo("Red", null, "Roma");
		Iterator<String> resultsIterator = results.iterator();
		
		String videoCorrelato = null;
		while (resultsIterator.hasNext()) {
			videoCorrelato = VideoFile.getXmlFileName(resultsIterator.next());
			System.out.println(videoCorrelato);
		}
	}
	
}
