package gestionexml;

import java.util.ArrayList;


/**
 * Classe contenitore capace di memorizzare tutte le informazioni estratte
 * da un tag <TemporalDecomposition>
 * 
 * Tutti gli attributi sono manipolati con i metodi getter e setter.
 */
public class TemporalDecomposition {

	private String id = null;

	private String videoId = null;
	
	private String freeTextAnnotation = null;
	private ArrayList<String> keywords = null;

	private String mediaTimePoint = null;
	private String mediaDuration = null;

	private ArrayList<TemporalDecomposition> children = null;

	/**
	 * Costruttore della classe
	 * 
	 * @param id id dello shot
	 * @param videoId id del video contenente lo shot 
	 * @param description descrizione dello shot
	 * @param time tempo di partenza dello shot
	 * @param duration durata dello shot
	 */
	TemporalDecomposition(String id, String videoId, String description, String time, String duration) {
		this.id = id;
		this.videoId = videoId;
		this.freeTextAnnotation = description;
		this.mediaTimePoint = time;
		this.mediaDuration = duration;
		
		this.keywords = new ArrayList<String>();
		this.children = new ArrayList<TemporalDecomposition>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	public String getFreeTextAnnotation() {
		return freeTextAnnotation;
	}

	public void setFreeTextAnnotation(String freeTextAnnotation) {
		this.freeTextAnnotation = freeTextAnnotation;
	}

	public String getMediaTimePoint() {
		return mediaTimePoint;
	}

	public void setMediaTimePoint(String mediaTimePoint) {
		this.mediaTimePoint = mediaTimePoint;
	}

	public String getMediaDuration() {
		return mediaDuration;
	}

	public void setMediaDuration(String mediaDuration) {
		this.mediaDuration = mediaDuration;
	}

	/**
	 * Restituisce la keyword contenuta all'indice index
	 * @param index indice
	 * @return keyword
	 */
	public String getKeywords(int index) {
		return keywords.get(index);
	}
	
	/**
	 * 
	 * @return numero di keyword
	 */
	public int getKeywordsNumber() {
		return keywords.size();
	}

	/**
	 * Aggiunge una keyword alla descrizione XML dello shot
	 * @param keyword
	 */
	public void addKeywords(String keyword) {
		this.keywords.add(keyword);
	}
	
	/**
	 * Restituisce lo shot figlio contenuto all'indice index
	 * @param index indice
	 * @return shot figlio
	 */
	public TemporalDecomposition getChildren(int index) {
		return children.get(index);
	}
	
	/**
	 * Restituisce il numero di shot figli
	 * @return numero di shot  figli
	 */
	public int getChildrenNumber() {
		return children.size();
	}

	/**
	 * Aggiunge uno shot figlio
	 * @param temporalDecomposition shot figlio
	 */
	public void addChildren(TemporalDecomposition temporalDecomposition) {
		this.children.add(temporalDecomposition);
	}

	
	/**
	 * Converte la durata data in secondi 'seconds',
	 * al formato usato nello standard Mpeg-7
	 * 
	 * @param seconds durata del video data in secondi
	 * 
	 * @return dutata del video nel formato Mpeg-7
	 */
	public static String creaMediaDuration(String seconds) {
		
		int durata = Integer.parseInt(seconds);
		
		int secondi = durata % 60;
		int minuti = ((int) durata / 60) % 60;
		int ore = (int) ((durata / 60) / 60);
		
		String newDuration = "PT" + ore + "H" + minuti + "M" + secondi + "S";
		
		return newDuration;
	}
	
	public static double parseMediaDuration(String mediaDuration) {		
		return (double) parseIntMediaDuration(mediaDuration);
	}
	
	public static double parseMediaTimePoint(String mediaTimePoint) {
		return (double) parseIntMediaTimePoint(mediaTimePoint);
	}
	
	private static int parseIntMediaDuration(String mediaDuration) {
		int secondi = 0;
		
		String S = mediaDuration.substring(mediaDuration.indexOf('M')+1, mediaDuration.length()-1);
		String M = mediaDuration.substring(mediaDuration.indexOf('H')+1, mediaDuration.indexOf('M'));
		String H = mediaDuration.substring(mediaDuration.indexOf('T')+1, mediaDuration.indexOf('H'));
		
		secondi = secondi + Integer.parseInt(S);
		secondi = secondi + (Integer.parseInt(M) * 60);
		secondi = secondi + (Integer.parseInt(H) * 3600);
		
		return secondi;
	}
	
	private static int parseIntMediaTimePoint(String mediaTimePoint) {
		int secondi = 0;
		
		String S = mediaTimePoint.substring(7);
		String M = mediaTimePoint.substring(4, 6);
		String H = mediaTimePoint.substring(1, 3);
		
		secondi = secondi + Integer.parseInt(S);
		secondi = secondi + (Integer.parseInt(M) * 60);
		secondi = secondi + (Integer.parseInt(H) * 3600);
		
		return secondi;
	}
	
	public static String hrMediaDuration(String mediaDuration) {
		int secondi = parseIntMediaDuration(mediaDuration);

		String s = Integer.toString(secondi % 60);
		String m = Integer.toString( (secondi / 60) % 60 );
		String h = Integer.toString( (int) ((secondi / 60) / 60) );
		
		return h + " ore " + m + " minuti " + s + " secondi ";
	}
	
	public static String hrMediaTimePoint(String mediaTimePoint) {
		int secondi = parseIntMediaTimePoint(mediaTimePoint);

		String s = Integer.toString(secondi % 60);
		String m = Integer.toString( (secondi / 60) % 60 );
		String h = Integer.toString( (int) ((secondi / 60) / 60) );
		
		return h + " ore " + m + " minuti " + s + " secondi ";
	}
	
	public static String creaMediaTimePoint(String seconds) {
		
		int time = Integer.parseInt(seconds);
		
		int secondi = time % 60;
		int minuti = ((int) time / 60) % 60;
		int ore = (int) ((time / 60) / 60);
		
		return creaMediaTimePoint(ore, minuti, secondi);
	}
	
	public static String creaMediaTimePoint(String minutes, String seconds) {
		
		int sTime = Integer.parseInt(seconds);
		int mTime = Integer.parseInt(minutes);
		
		int secondi = sTime % 60;
		mTime = mTime + ((int) sTime / 60);
		int minuti = mTime % 60;
		int ore = (int) (mTime / 60);
		
		return creaMediaTimePoint(ore, minuti, secondi);
	}
	
	public static String creaMediaTimePoint(String hours, String minutes, String seconds) {
		
		int sTime = Integer.parseInt(seconds);
		int mTime = Integer.parseInt(minutes);
		int hTime = Integer.parseInt(hours);
		
		int secondi = sTime % 60;
		mTime = mTime + ((int) sTime / 60);
		int minuti = mTime % 60;
		hTime = hTime + ((int) mTime / 60);
		int ore = (int) hTime;
		
		return creaMediaTimePoint(ore, minuti, secondi);
	}
	
	public static String creaMediaTimePoint(int ore, int minuti, int secondi) {
		String S = ((secondi>9) ? "" : "0");
		S = S + String.valueOf(secondi);
		
		String M = ((minuti>9) ? "" : "0");
		M = M + String.valueOf(minuti);
		
		String H = ((ore>9) ? "" : "0");
		H = H + String.valueOf(ore);
		
		
		return "T" + H + ":" + M + ":" + S;	
	}
	
	/**
	 * Metodo utilizzato per stampare informazioni di debug
	 */
	public void printDebugInformation() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("ID = " + id);
		System.out.println("videoId = " + videoId);
		System.out.println("freeTextAnnotation = " + freeTextAnnotation);
		System.out.println("mediaTimePoint = " + mediaTimePoint);
		System.out.println("mediaDuration = " + mediaDuration);
		for (String kw : keywords) {
			System.out.println("Keyword = " + kw);
		}
		System.out.println("numero di figli TemporalDecomposition = " + children.size());
		for (TemporalDecomposition td : children) {
			td.printDebugInformation();
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");		
	}
	
	/**
	 * Metodo statico utile per i test della classe
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String res = TemporalDecomposition.creaMediaTimePoint("60","159");
		System.out.println(res);
	}
}
