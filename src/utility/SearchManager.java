package utility;

import java.util.ArrayList;

import gestionexml.*;

/**
 * Classe che gestisce la ricerca 
 * Utilizza le classi Mpeg7XQuerier per effettuare le query sui video e sugli shot
 * e RelatedFinder per cercare i video correlati
 */
public class SearchManager {

	private Mpeg7XQuerier query;
	private RelatedFinder rf;
	private String path;
	
	public SearchManager(String path){
		query=new Mpeg7XQuerier(path);
		rf= new RelatedFinder(path);
		this.path=path;
	}
	
	//restituisce l'xml degli autori
	public String getAuthors(){
		return query.getMenuAuthor();
	}
	
	//restituisce l'xml delle categorie
	public String getGenress(){
		return query.getMenuGenre();
	}
	
	//restituisce l'xml degli autori
	public String getLocations(){
		return query.getMenuLocations();
	}
	
	//Restituisce i video cercati per autore genere e locazione
	public ArrayList<VideoFile> getSearchedVideo(String aut, String genre, String loc){
		
		if (aut.length() == 0)
			aut = null;
		
		if (genre.length() == 0)
			genre = null;
		
		if (loc.length() == 0)
			loc = null;
		
		ArrayList<String> idVideos=rf.searchVideo(aut, genre, loc);
		
		
		
		ArrayList<VideoFile> videos=new ArrayList<VideoFile>();
		
		for(String videoId: idVideos){
		String xml=VideoFile.getXmlFileName(videoId);
		VideoFile video=new VideoFile(path, xml);
		videos.add(video);
		}
		
		System.out.println("numero video trovati: "+ videos.size());
		
		return videos;
	}
	
	//Restituisce gli shot cercati per descrizione
	public ArrayList<TemporalDecomposition> getSearchedShots(String descr){
		 return rf.searchShot(descr);
		
	}
	
}
