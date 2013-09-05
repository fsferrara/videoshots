package gestionexml;

/**
 * Classe contenitore capace di memorizzare tutte le informazioni estratte
 * da un file XML in formato NITF.
 * 
 * Tutti gli attributi sono manipolati con i metodi getter e setter.
 */
public class NitfMetaData {
	
	/**
	 * Attributi contenuti nell'head
	 */
	private String title;
	private String xmlVersion;
	private String category;
	private String product;
	private String author;
	private String source;
	
	/**
	 * Attributi del documento
	 */
	private String regSrc;
	private String idString;
	private String urgency;
	private String dateIssue;
	private String duKey;
	private String docCopyrightHolder;
	
	/**
	 * Attributi dell'head del body
	 */
	private String hl1;
	private String hl2;
	private String byLine;
	private String distributor;
	private String location;
	private String storyDate;
	
	/**
	 * Attributi del content del body
	 */
	private String p;
	
	/**
	 * Attributi del video collegato
	 */
	private String videoDataLocation;
	private String videoMimeType;
	private String videoWidth;
	private String videoHeight;
	private String videoSize;
	private String videoTime;
	private String videoBitrate;
	private String videoText;
	
	/**
	 * Attributi dell'immagine collegata
	 */
	private String imageDataLocation;
	private String imageMimeType;
	private String imageWidth;
	private String imageHeight;
	private String imageSize;
	private String imageTime;
	private String imageBitrate;
	private String imageText;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getXmlVersion() {
		return xmlVersion;
	}
	public void setXmlVersion(String xmlVersion) {
		this.xmlVersion = xmlVersion;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRegSrc() {
		return regSrc;
	}
	public void setRegSrc(String regSrc) {
		this.regSrc = regSrc;
	}
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getDateIssue() {
		return dateIssue;
	}
	public void setDateIssue(String dateIssue) {
		this.dateIssue = dateIssue;
	}
	public String getDuKey() {
		return duKey;
	}
	public void setDuKey(String duKey) {
		this.duKey = duKey;
	}
	public String getDocCopyrightHolder() {
		return docCopyrightHolder;
	}
	public void setDocCopyrightHolder(String docCopyrightHolder) {
		this.docCopyrightHolder = docCopyrightHolder;
	}
	public String getHl1() {
		return hl1;
	}
	public void setHl1(String hl1) {
		this.hl1 = hl1;
	}
	public String getHl2() {
		return hl2;
	}
	public void setHl2(String hl2) {
		this.hl2 = hl2;
	}
	public String getByLine() {
		return byLine;
	}
	public void setByLine(String byLine) {
		this.byLine = byLine;
	}
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStoryDate() {
		return storyDate;
	}
	public void setStoryDate(String storyDate) {
		this.storyDate = storyDate;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getVideoDataLocation() {
		return videoDataLocation;
	}
	public void setVideoDataLocation(String videoDataLocation) {
		this.videoDataLocation = videoDataLocation;
	}
	public String getVideoMimeType() {
		return videoMimeType;
	}
	public void setVideoMimeType(String videoMimeType) {
		this.videoMimeType = videoMimeType;
	}
	public String getVideoWidth() {
		return videoWidth;
	}
	public void setVideoWidth(String videoWidth) {
		this.videoWidth = videoWidth;
	}
	public String getVideoHeight() {
		return videoHeight;
	}
	public void setVideoHeight(String videoHeight) {
		this.videoHeight = videoHeight;
	}
	public String getVideoSize() {
		return videoSize;
	}
	public void setVideoSize(String videoSize) {
		this.videoSize = videoSize;
	}
	public String getVideoTime() {
		return videoTime;
	}
	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}
	public String getVideoBitrate() {
		return videoBitrate;
	}
	public void setVideoBitrate(String videoBitrate) {
		this.videoBitrate = videoBitrate;
	}
	public void setVideoText(String videoText) {
		this.videoText = videoText;
	}
	public String getVideoText() {
		return videoText;
	}
	public String getImageDataLocation() {
		return imageDataLocation;
	}
	public void setImageDataLocation(String imageDataLocation) {
		this.imageDataLocation = imageDataLocation;
	}
	public String getImageMimeType() {
		return imageMimeType;
	}
	public void setImageMimeType(String imageMimeType) {
		this.imageMimeType = imageMimeType;
	}
	public String getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(String imageWidth) {
		this.imageWidth = imageWidth;
	}
	public String getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(String imageHeight) {
		this.imageHeight = imageHeight;
	}
	public String getImageSize() {
		return imageSize;
	}
	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}
	public String getImageTime() {
		return imageTime;
	}
	public void setImageTime(String imageTime) {
		this.imageTime = imageTime;
	}
	public String getImageBitrate() {
		return imageBitrate;
	}
	public void setImageBitrate(String imageBitrate) {
		this.imageBitrate = imageBitrate;
	}
	public void setImageText(String imageText) {
		this.imageText = imageText;
	}
	public String getImageText() {
		return imageText;
	}
	
	/**
	 * Procedura utile al debug. Stampa sullo standard output i dati dell'istanza
	 */
	public void debugPrint(){
		System.out.println("DATI STRUTTURA TEMPORANEA");
		System.out.println("titolo: " + getTitle());
		System.out.println("source: " + getSource());
		System.out.println("doc Copyright Holder: " + getDocCopyrightHolder());
		System.out.println("hl1: " + getHl1());
		System.out.println("hl2: " + getHl2());
		System.out.println("byline: " + getByLine());
		System.out.println("location: " + getLocation());
		System.out.println("story date: " + getStoryDate());
		System.out.println("body->p: " + getP());
		System.out.println("body->video: ");
		System.out.println("--dataLocation" + getVideoDataLocation());
		System.out.println("--height" + getVideoHeight());
		System.out.println("--width" + getVideoWidth());
		System.out.println("body->image: ");
		System.out.println("--dataLocation" + getImageDataLocation());
		System.out.println("--height" + getImageHeight());
		System.out.println("--width" + getImageWidth());		
	}
}
