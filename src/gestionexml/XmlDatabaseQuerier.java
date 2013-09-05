package gestionexml;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.basex.core.Context;
import org.basex.core.proc.Close;
import org.basex.core.proc.CreateDB;
import org.basex.core.proc.DropDB;
import org.basex.core.proc.List;
import org.basex.core.proc.Set;
import org.basex.data.XMLSerializer;
import org.basex.io.PrintOutput;
import org.basex.query.QueryProcessor;
import org.basex.query.item.Item;
import org.basex.query.iter.Iter;
import org.basex.util.StringList;

/**
 * Classe utilizzata per eseguire query XQuery sui file XML
 */
public class XmlDatabaseQuerier {
	
	/**
	 * Directory di default nella quale vengono cercati i file XML. Questa cartella
	 * e' attualmente definita all'interno del metodo getInstance().
	 * 
	 * Inoltre tutti i file XML contenuti nella cartella formeranno
	 * un database XML.
	 * 
	 * @see XmlDatabaseQuerier#getInstance()
	 */
	private String defaultDir = null;

	/**
	 * Nome del database creato unendo i file XML di {@link XmlDatabaseQuerier#defaultDir}
	 */
	final static String databaseName = "db_videoshots";
	
	/**
	 * Flag che indica la creazione del database. Serve perche' nel
	 * caso vengano aggiunti nuovi nuovi file alla collezione, allora bisogna aggiornarlo.
	 * 
	 * @see XmlDatabaseQuerier#refreshXmlDatabase()
	 */
	private boolean databaseCreated = false;
	
	/**
	 * Istanza di XmlDatabaseQuerier. Questa classe e' stata scritta seguendo
	 * il pattern singleton, quindi si ha bisogno di questo riferimento.
	 */
	private static XmlDatabaseQuerier xmlDatabaseQuerier = null;
	
	/**
	 * Flag utile a segnalare bug nelle librerie del motore XQuery
	 */
	private boolean lock = false;
	
	/**
	 * Oggetto utile alle librerie di BaseX per costruire il database XML
	 */
    private Context context = null;
    
    /**
	 * Oggetto utile alle librerie di BaseX per costruire il database XML
	 */
    private PrintOutput out = null;

    /**
     * Costruttore della classe. E' dichiarato private per seguire il pattern Singleton.
     * 
     * @param defaultDirectory Directory dalla quale prendere i file XML per creare il database
     */
	private XmlDatabaseQuerier(String defaultDirectory) {
		
		out = new PrintOutput(System.out);
		databaseCreated = false;
		defaultDir = defaultDirectory;
		
		/**
		 * Divetera' true nel caso di bug nelle librerie del motore XQuery
		 */
		lock = false;
		
		/**
		 * Creo/Ricarico il database XML
		 */
		refreshXmlDatabase();
	}
	
	/**
	 * Restituisce l'istanza di XmlDatabaseQuerier. Se non ancora ne esiste una
	 * la crea.
	 * 
	 * @return istanza di XmlDatabaseQuerier
	 */
	public static XmlDatabaseQuerier getInstance(String directory) {
		
		if (xmlDatabaseQuerier == null) {
			try {
				xmlDatabaseQuerier = new XmlDatabaseQuerier(directory);
			} catch (Exception e) {
				xmlDatabaseQuerier = null;
			}
		}
		
		if ((xmlDatabaseQuerier != null) && (xmlDatabaseQuerier.lock)) {
			/*
			 * Qui xmlDatabaseQuerier puo' essere impostato a null per
			 * impedire l'accesso al database XML in caso di problemi
			 */
			xmlDatabaseQuerier = new XmlDatabaseQuerier(directory);
		}
		
		return xmlDatabaseQuerier;
	}
	
	/**
	 * Procedura per la corretta chiusura del database XML
	 */
	protected void finalize() throws Throwable {
	    dropXmlDatabase();
	    out.close();
		super.finalize();
	}
	
	/**
	 * Crea o aggiorna il database XML
	 */
	public void refreshXmlDatabase() {

		try {
			if (databaseCreated) {
				dropXmlDatabase();
			}

			System.out.println("Mpeg7XQuerier(): database creation");

			context = new Context();
			new Set("info", "true").execute(context, out);
			new Set("chop", "true").execute(context, out);
			new CreateDB(defaultDir, databaseName).execute(context, out);
			
			databaseCreated = true;
			
		} catch (Exception e) {
			System.out.println("Possibile Bug di BaseX");
			e.printStackTrace();
			lock = true; //Segnalo l'eccezione incontrata
		}
	}
	
	/**
	 * Elimina il database XML prima di ricrearlo aggiornato
	 * 
	 * @throws Exception
	 */
	public void dropXmlDatabase() throws Exception {
	    new Close().execute(context, out);

	    /**
	     * StringList contenente tutti i database creati dalle librerie di BaseX
	     */
	    StringList databases = List.list(context);
	    Iterator<String> databasesIterator = databases.iterator();
	    
	    /**
	     * Qui cancelliamo tutti i database creati da BaseX perche'
	     * ci crea problemi quando portiamo il progetto su altri computer
	     * con differete versione della libreria
	     */
	    while (databasesIterator.hasNext()) {
	    	new DropDB(databasesIterator.next()).execute(context, out);
	    }
		
	    context.close();
	    databaseCreated = false;
	}
	
	/**
	 * Esegue una query XQuery.
	 * Se contiene l'istruzione "doc('nomeFile.xml')" sarˆ eseguita su uno specifico file XML,
	 * altrimenti sarˆ eseguita sull'intero database.
	 * 
	 * @param query XQuery da eseguire
	 * @return Lista di risultati restituiti dalla query
	 */
	public ArrayList<String> executeXQuery(String query) {

		if (lock)
			return null;
		
		ArrayList<String> results = new ArrayList<String>();
		
		try { 
			
			// Creates a query instance
			QueryProcessor processor = new QueryProcessor(query, context);
			
			// Executes the query.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintOutput po = new PrintOutput(baos);
			XMLSerializer xml = new XMLSerializer(po);
		    Iter iter = processor.iter(); 
		    for(Item item : iter) {
		    	item.serialize(xml);
		    	results.add(baos.toString("UTF-8"));
		    	baos.reset();
		    }

			// Closes the serializer
			xml.close();
			// Closes the query processor
			processor.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	/**
	 * Metodo statico utile per i test della classe
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		XmlDatabaseQuerier xdq = XmlDatabaseQuerier.getInstance("WebContent/file/video");
		xdq.dropXmlDatabase();
		
	}
}
