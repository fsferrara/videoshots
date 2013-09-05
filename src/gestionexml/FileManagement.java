package gestionexml;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


/**
 * Classe di utilita' per la gestione dell'archivio dei file nel file system.
 */
public class FileManagement {
	
	/**
	 * Prefisso di URL.
	 * Su MAC e' file:// e su windows e' file:/
	 */
	public static String URL_FILE_PREFIX = "file:/";
	
	/**
	 * Directory di default nella quale vengono cercati i file XML.
	 */
	private String defaultDir = null;
	
	/**
	 * Restituisce il nome della directory di default
	 * @return nome della directory di default
	 */
	public String getDefaultDir() {
		return defaultDir;
	}
	
	/**
	 * Costruttore della classe.
	 * @param defaultDirectory direcotry di default
	 */
	public FileManagement(String defaultDirectory) {
		defaultDir = defaultDirectory;
	
		
	}
	
	/**
	 * Cancella una directory e tutto il suo contenuto dal file system.
	 * Usa il metodo privato {@link FileManagement#deleteDirectoryRec(File)} per
	 * funzionare correttamente
	 * 
	 * @param directoryName Path della drectory da cancellare
	 * 
	 * @return true se la directory e' stata cancellata in modo corretto
	 */
	public boolean deleteDirectory(final String directoryName) {
		File dir = new File("directoryName");
		return deleteDirectoryRec(dir);
	}
	
	/**
	 * Cancella una direcory e tutto il suo contenuto in modo ricorsivo
	 * 
	 * @param dir Directory o File da cacellare in modo ricorsivo
	 * 
	 * @return true se la directory e' stata cancellata in modo corretto
	 */
    private boolean deleteDirectoryRec(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDirectoryRec(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        
        return dir.delete();
    }
	
    /**
     * Cancella un file dal file system
     * 
     * @param directory Path della directory che contiene il file
     * @param fileName nome del file da cancellare
     * 
     * @return true se il file e' stato cancellato correttamente
     */
	public boolean deleteFile(final String directory, final String fileName) {
		
		File file = new File(directory, fileName);
		
		if (!file.isDirectory()) {
			return file.delete();
		}
		
		return false;
	}
	
    /**
     * Cancella un file dal file system situato nella directory contenente la collezione di video.
     * 
     * @param fileName nome del file da cancellare
     * 
     * @return true se il file e' stato cancellato correttamente
     */
	public boolean deleteFile(final String fileName) {
		return deleteFile(defaultDir, fileName);
	}
	
	/**
	 * Restituisce la lista dei file e/o directory sul file system che soddisfano i
	 * criteri richiesti
	 * 
	 * @param directory Path della directory dalla quale leggere la lista dei file
	 * @param extension Estensione dei file vhr vogliamo avere in output
	 * 
	 * @return Lista dei file
	 */
	public ArrayList<String> fileList(final String directory, final String extension) {
	    
	    //Filtro sull'estensione del file
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith(extension.toLowerCase());
	        }
	    };
	    
	    return fileList(directory, filter);
		
	}
	
	/**
	 * Restituisce la lista dei file e/o directory sul file system contenuti nella
	 * directory di default
	 * 
	 * @return Lista dei file
	 */
	public ArrayList<String> fileList() {
	    
	    //Filtro nullo sull'estensione del file
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return true;
	        }
	    };
	    
	    return fileList(defaultDir, filter);
		
	}
	
	/**
	 * Restituisce la lista dei file e/o directory sul file system che soddisfano i
	 * criteri richiesti
	 * 
	 * @param directory Path della directory dalla quale leggere la lista dei file
	 * 
	 * @return Lista dei file
	 */
	public ArrayList<String> fileList(final String directory) {
	    
	    //Filtro sull'estensione del file
	    FilenameFilter filter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return true;
	        }
	    };
	    
	    return fileList(directory, filter);
		
	}
	
	/**
	 * Restituisce la lista dei file e/o directory sul file system che soddisfano i
	 * criteri richiesti
	 * 
	 * @param directory Path della directory dalla quale leggere la lista dei file
	 * @param filter filtro da applicare al nome dei file
	 * 
	 * @return Lista dei file
	 */
	private ArrayList<String> fileList(final String directory, FilenameFilter filter) {
		
		ArrayList<String> fileArrayList = new ArrayList<String>();
		
	    File dir = new File(directory);
	    
	    String[] children = dir.list(filter);
	    if (children != null) {
	        for (int i=0; i<children.length; i++) {
	            // Get filename of file or directory
	            fileArrayList.add(children[i]);
	        }
	    }
	    
	    return fileArrayList;
		
	}
	
	/**
	 * Rinomina e/o sposta un file
	 * 
	 * @param oldDirectory vecchia directory
	 * @param oldFileName vecchio nome del file
	 * @param newDirectory directory di destinazione
	 * @param newFileName nuovo nome del file
	 * 
	 * @return true se l'operazione e' andata a buon fine
	 */
	public boolean renameFile(final String oldDirectory, final String oldFileName,
			final String newDirectory, final String newFileName) {

		// File (or directory) with old name
		File oldFile = new File(oldDirectory, oldFileName);

		// File (or directory) with new name
		File newFile = new File(newDirectory, newFileName);

		// Rename file (or directory)
		return oldFile.renameTo(newFile);
	}

	/**
	 * Metodo statico utile per i test della classe
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		/* ESEMPIO D'USO per deleteFile()
		
	    FileManagement fm = new FileManagement("file/video");
	    
	    ArrayList<String> listaFile = fm.fileList("file/video", ".xml");
	    java.util.Iterator<String> listaIterator = listaFile.iterator();
	    while (listaIterator.hasNext()) {
	    	String fileToDelete = listaIterator.next();
	    	if ("extract.xml".equals(fileToDelete)) {
	    		System.out.println(fileToDelete);
		    	fm.deleteFile("file/video", fileToDelete);	    		
	    	}
	    }
		*/
		
		/* ESEMPIO D'USO PER fileList()
		
	    FileManagement fm = new FileManagement("file/video");
	    
	    ArrayList<String> listaFile = fm.fileList("file/video", ".xml");
	    java.util.Iterator<String> listaIterator = listaFile.iterator();
	    while (listaIterator.hasNext()) {
	    	System.out.println(listaIterator.next());
	    }
	    
	    */
		
	}
}
