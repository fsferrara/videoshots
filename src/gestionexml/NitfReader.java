package gestionexml;

import java.io.File;
import java.util.ArrayList;

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

/**
 * Si occupa della gestione dei file NITF, e gestisce la loro conversione nel formato Mpeg-7
 */
public class NitfReader {

	/**
	 * Directory contenente i file XML in formato NITF
	 */
    public final static String nitfDirectory = "file/nitf-mpeg";
    
    /**
     * Directory contenente i file XML in formato Mpeg-7
     */
    public final static String mpegDirectory = "file/video";
	
    /**
     * Converte il file XML NITF posizionato nel path nitfDirectory/nitfFileName
     * in un file XML Mpeg7 creandolo nel path mpegDirectory/mpegFileName
     * 
     * @param nitfDirectory cartella dove si trova il file XML NITF
     * @param nitfFileName nome del file XML NITF
     * @param mpegDirectory cartella di destinazione del nuovo file XML Mpeg7
     * @param mpegFileName nome del nuovo file Mpeg7
     */
    public void convertNitfDocument(String nitfDirectory, String nitfFileName, String mpegDirectory, String mpegFileName) {
    	
    	//Il file nitf deve essere indirizzato tramite un url quindi il path assoluto
    	//deve essere preceduto dal prefisso uri
    	String completeNitfFileName = FileManagement.URL_FILE_PREFIX + nitfDirectory + "/" + nitfFileName;
    	
    	//Il file mpeg7 non ha bisogno di essere indirizzato da un url quindi il path assoluto va bene
    	String completeMpegFileName = mpegDirectory + "/" + mpegFileName;
		
		//NITF
		DocumentBuilderFactory nitfFactory = DocumentBuilderFactory.newInstance();
		
		//MPEG7
		File outFile = new File(completeMpegFileName);
		DocumentBuilderFactory mpegFactory = DocumentBuilderFactory.newInstance();
		
		try {
			
			//Apertura documento NITF
			DocumentBuilder nitfBuilder = nitfFactory.newDocumentBuilder();
			Document nitfDoc = nitfBuilder.parse(completeNitfFileName);
			
			//Apertura nuovo documento MPEG7
			DocumentBuilder documentBuilder = mpegFactory.newDocumentBuilder();
			Document mpegDoc = documentBuilder.newDocument();

			//Converto NITF in MPEG7
			NitfMpeg7Converter converter = new NitfMpeg7Converter();
			NitfMetaData nitfMetaData = converter.converti(nitfDoc, mpegDoc);
			nitfMetaData.debugPrint();
			
			//Salvo il file MPEG7 su disco
			Transformer tFormer = TransformerFactory.newInstance().newTransformer();
			tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
			tFormer.setOutputProperty(OutputKeys.INDENT, "yes");
			mpegDoc.normalize();
			Source source = new DOMSource(mpegDoc);
			Result result = new StreamResult(outFile);
			tFormer.transform(source, result);


		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/**
	 * Converte tutti i file NITF, contenuti in nitfDirectory, in file Mpeg-7. Durante
	 * la conversione i file NITF non saranno cancellati, e i file Mpeg7 saranno creati con lo stesso
	 * nome dei file NITF originari
	 * 
	 * @param nitfDirectory cartella contenente i file XML NITF
	 * @param mpegDirectory cartella dove saranno scritti i file XML Mpeg7
	 */
	public void convertAllNitfDocument(String nitfDirectory, String mpegDirectory) {


		String nitfFileName = null;
		String mpegFileName = null;
		
	    FileManagement fm = new FileManagement("file/video");
	    
	    ArrayList<String> listaFile = fm.fileList(nitfDirectory, ".xml");
	    java.util.Iterator<String> listaIterator = listaFile.iterator();
	    while (listaIterator.hasNext()) {
	    	
	    	nitfFileName = listaIterator.next();
	    	mpegFileName = nitfFileName;
			
	    	convertNitfDocument(nitfDirectory, nitfFileName, mpegDirectory, mpegFileName);
	    }
	}

	/**
	 * Metodo statico utile per i test della classe
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		NitfReader nitfReader = new NitfReader();
		nitfReader.convertAllNitfDocument(NitfReader.nitfDirectory, NitfReader.mpegDirectory);
	}
	
}
