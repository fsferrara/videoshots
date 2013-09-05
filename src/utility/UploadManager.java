package utility;



import java.io.*;
import java.util.*;
import org.apache.commons.fileupload.*;
import gestionexml.*;

/**
 * Questa classe gestisce l'upload dei file, in particolare
 * controlla che i file siano stati inseriti. Inoltre  
 * controlla il formato dei file, memorizza ogni file sul server senza 
 * modificarli; nelle seguenti cartelle:
 * video e jpeg----> "%RealPath%\file\video\" 
 * nitf ----->"%RealPath%\file\nitf" 
 * 
 * in seguito il file nitf è convertito in mpeg7 ed è
 *  memorizzato in "%RealPath%\file\video\" 
 *
 */


public class UploadManager {

	//Messaggi per la descrizione del risultato
	
	private static final String videoPath="/file/video";
	private static final String nitfPath="/file/nitf";
	private static final String succesMsg="Upload effettuato con successo!";
	private static final String noFilesMsg="Non sono stati inseriti tutti i files";
	private static final String badExtentionMsg="Il file inserito non ha un estensione valida";
	private static final String exceptionMsg="Errore nella memorizzazione dei file o nella conversione";
	private static final String fileNitf="fileNitf";
	private static final String fileVideo="fileVideo";
	private static final String fileJpg="fileJpg";
	private static final String noExtFilesMsg="Uno o più file inseriti non è corretto";
	private static final String badFileNameMsg="I nomi dei file inseriti non sono uguali";
		
	    /**
		 * Restituisce il nome del file senza il suo path.
		 */
	public static String getFilteredName(String name){
		
		return name.substring(name.lastIndexOf("\\")+1);
		
		}
	
		/**
		 * 	Restituisce l'estenzione del file
		 */
	public static String getFileExtention(String name){
		
		return name.substring(name.lastIndexOf("."));
		
		}
	
	
	/**
	 *  Restituisce il nome del file senza estensione.
	 *  
	 * @param filteredName Nome filtrato (senza path)
	 * @return
	 */
	public static String getFileName(String filteredName){
		
		return filteredName.substring(0, filteredName.lastIndexOf(".")-1);
	}
	
	/***
	 * Upload di video e immagini
	 * 
	 */
	private void uploadImgVid(String realPath, String name, FileItem item) 
	  throws Exception{
		File fOut=null;
		String path=realPath+videoPath;
		fOut = new File(path,name);
		item.write(fOut);
	}
	
	/***
	 * Upload di file nitf e conversione in file mpeg7

	 */
	private void uploadNitf(String realPath, String name, FileItem item)
	  throws Exception{
		String pathNitf=realPath+nitfPath;
		String pathMpeg7=realPath+videoPath;
		File fOut=null;
		fOut = new File(pathNitf,name);
		item.write(fOut);
		//Conversione file nitf in mpeg7 e copia in video
		NitfReader nitread= new NitfReader();
		
		nitread.convertNitfDocument(pathNitf, name, pathMpeg7, name);
		RelatedFinder rf=new RelatedFinder(realPath + "/file/video");
		rf.writeRelatedMaterial(name);
	}
	/***
	 * 
	 * Questo metodo gestisce l'upload dei file memorizzandoli sul server
	 * e nel caso sono file nitf li memorizza e poi li converte in mpeg7 
	 * e memorizza una copia convertita.
	 * 
	 * Ritorna una stringa contenente un messaggio che descrive l'esito 
	 * delle operazioni.
	 * 
	 * @param items
	 * @param realPath
	 * @return
	 */
	public String manageUpload(List<FileItem> items,String realPath) {
		
		//messaggio finale
		String result=null;
		
				
		for(FileItem item: items){
			//Controllo presenza file
			if(item.getSize()==0){
				result=noFilesMsg;
				return result;
			}
		}
		
		//Controllo nomi file
		Iterator<FileItem> i=items.iterator();
		//nome del primo file
		String fileName=getFileName(getFilteredName(i.next().getName()));
		while(i.hasNext()){
			if(!fileName.equals(getFileName(getFilteredName(i.next().getName()))))
				return badFileNameMsg;
		}
		
		//Controllo estensioni
		for(FileItem item: items){
			
			String name=getFilteredName(item.getName());
			String ext=getFileExtention(name);
			
			if ( !((ext.equals(VideoFile.xmlExtension) 
					&& item.getFieldName().equals(fileNitf))
					|| (ext.equals(VideoFile.vidExtension) 
					&& item.getFieldName().equals(fileVideo))  ||
				    ( ext.equals(VideoFile.imgExtension) 
				    		&& item.getFieldName().equals(fileJpg)))){
				return badExtentionMsg + ":<br/> " + item.getName();
			}
			
		}
		
		for (FileItem item : items) {

			String name = getFilteredName(item.getName());
			String ext = getFileExtention(name);

			try {
				/**
				 * Controllo estensione: se e' file nitf allora va in file/nitf
				 */
				if (ext.equals(VideoFile.xmlExtension)
						&& item.getFieldName().equals(fileNitf)) {
					uploadNitf(realPath, name, item);

				} else { // altrimenti va in video
					uploadImgVid(realPath, name, item);
				}

				result = succesMsg;
			} catch (Exception e) {
				result = exceptionMsg;
			}
		}
			
		return result;
		
	}

}
