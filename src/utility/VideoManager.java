package utility;

import java.util.ArrayList;

import gestionexml.*;

/**
 * Questa classe si occupa di gestire l'inserimento degli shot (taggatura) 
 * recupera la lista dei video presenti nel db e crea l'xml degli shot correlati 
 * ad un video
 * 
 *
 */
public class VideoManager {
	
	private String path;
	
	public VideoManager(String prepath){
		path=prepath+"/file/video";
	}
	
	//Ritorna la lista dei video presenti nel db
	public ArrayList<VideoFile> getVideoFiles(){
		
		
		FileManagement files=new FileManagement(path);
		
		ArrayList<String> xmlList=files.fileList(path, VideoFile.xmlExtension);
		ArrayList<VideoFile> videos=new ArrayList<VideoFile>();
		
		for(String xml:xmlList){
			VideoFile video=new VideoFile(path, xml);
			videos.add(video);
		}
		
		return videos;
		
	}
	
	public String taggaVideo(String fileName, String idParent, String description, String time, String duration, ArrayList<String> keywordList){
		
		VideoFile vd=new VideoFile(path, fileName);
		boolean res;
	
		//CONTROLLO INSERIMENTO TEMPI
		if(time==null)
			return "Il tempo di inizio non e' stato inserito correttamente";
		
		if(duration==null)
			return "La durata non e' stata inserita correttamente";
		
		if (description.indexOf("<")>-1 || description.indexOf(">")>-1 || description.indexOf("\"")>-1)
			return "La descrizione non puo' contentere i seguenti caratteri <, >, \"";
		
		
		//CONTROLLI TEMPO TAG
		double initTag=TemporalDecomposition.parseMediaTimePoint(time);
		double fineTag=initTag + TemporalDecomposition.parseMediaDuration(duration);
		
		idParent=(idParent.equals(""))?null:idParent;
		
		if (idParent==null && (initTag + fineTag > vd.getVideoDurata()))
			return "Lo shot dura piu' del video";
		
		if (idParent!=null){
			
			TemporalDecomposition tDec=vd.getTemporalDecomposition(idParent);
		
			double initSeg=TemporalDecomposition.parseMediaTimePoint(tDec.getMediaTimePoint());
			double fineSeg=TemporalDecomposition.parseMediaDuration(tDec.getMediaDuration())+initSeg;
			
			if (initTag<initSeg)
				return "Lo shot figlio inizia prima dello shot padre";
			
			if (fineTag>fineSeg)
				return "Lo shot figlio finisce dopo lo shot padre";
			
			if((fineTag-initTag) > (fineSeg-initSeg))
				return "Lo shot figlio dura piu' dello shot padre";
		}
		
		
		if (description==null || description.equals("null") || description.equals(""))
			return "la descrizione non puo' essere vuota";
		
		
		res=vd.addTemporalDecomposition(idParent, description, time, duration, keywordList);
		
		if(res)
			return "Shot inserito con successo";
		else
			return "Errore di taggatura";
	}
	
	/**
	 * Restituisce l'xml degli shot con tutte le info relative.
	 * utilizza makeXmlShots(String) per creare singolarmente ogni elemento shot
	 * @param shots
	 * @return
	 */
	public static String makeXmlShots(ArrayList<TemporalDecomposition> shots){
		
		String xml="<shots>";
		
		for (TemporalDecomposition td:shots){
			
			xml= xml+ makeXmlShots(td);
		}
			
		
		return xml+"</shots>";
	}
	
	//Crea l'elemento <shot> con tutte le info al suo interno
	private static String makeXmlShots(TemporalDecomposition shot){
		
		String id=" id=\""+shot.getId()+"\" ";
		String descr=" descrizione=\""+shot.getFreeTextAnnotation()+"\" ";
		
		String xml="<shot"+id+descr+">";
		xml=xml+ "<hrTimePoint>"+TemporalDecomposition.hrMediaTimePoint(shot.getMediaTimePoint())+"</hrTimePoint>";
		xml=xml+ "<hrDuration>"+TemporalDecomposition.hrMediaDuration(shot.getMediaDuration())+"</hrDuration>";
		xml=xml+ "<mediaTimePoint>" + TemporalDecomposition.parseMediaTimePoint(shot.getMediaTimePoint())+ "</mediaTimePoint>";
		xml=xml+ "<mediaDuration>" + TemporalDecomposition.parseMediaDuration(shot.getMediaDuration())+ "</mediaDuration>";
		for(int i=0;i<shot.getChildrenNumber();i++){
			xml=xml+makeXmlShots(shot.getChildren(i));
		}
		
		return xml+"</shot>";
	
	}
	
	
}
