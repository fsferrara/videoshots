package servlet;

import gestionexml.TemporalDecomposition;
import gestionexml.VideoFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.*;

import utility.SearchManager;
import utility.UploadManager;
import utility.VideoManager;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet che indirizza verso la pagina jsp richiesta
 */
public class Controller extends HttpServlet {
    Dispatcher disp=new Dispatcher();
    
    
    /**
     * Ritorna il percorso delle cartelle web
     * @return
     */
    
    private String getPath(){
    	ServletContext c= this.getServletContext();
        String path= c.getRealPath("");
        
        return path;
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String pagina = request.getParameter("pag");
       
        
        //Carica pagina principale con la lista dei video presenti nel Db
    	if (pagina.equals("home")){
    		
    		VideoManager video=new VideoManager(getPath());
            request.setAttribute("videos", video.getVideoFiles());
    		
    	}
    
    	/* Carica pagina di visualizzazione video: 
    	 * Carica il video
    	 * Se si sta visualizzando uno shot carica lo shot
    	 * Carica gli shot correlati
    	 */
    	if (pagina.equals("showVideo")){
    		
    		
    		String idVideo= request.getParameter("idVideo");
    		VideoFile video= new VideoFile(getPath()+"/file/video", VideoFile.getXmlFileName(idVideo));
    		String idShot= request.getParameter("idShot");
    		if (idShot!=null && !idShot.equals("null")){
    			TemporalDecomposition tDec=video.getTemporalDecomposition(idShot);
    			request.setAttribute("shot", tDec);
    		}
    		request.setAttribute("video", video);
    		request.setAttribute("xmlShots",VideoManager.makeXmlShots(video.getTemporalDecomposition()));
    			
    	}
    	
    	//Carica la pagina di ricerca creando i menu a tendina di autori location e genere
    	if (pagina.equals("search")){

    		SearchManager sm=new SearchManager(getPath()+"/file/video");
    		
    		//ritorna l'xml dei menu a tendina
    		request.setAttribute("authors", sm.getAuthors());
    		request.setAttribute("genres", sm.getGenress());
    		request.setAttribute("locations", sm.getLocations());
    	}
    	
    	//Trova i video cercati e mostra la lista
    	if (pagina.equals("findVideo")){
    		SearchManager sm= new SearchManager(getPath()+"/file/video");
    		String aut=(String)request.getParameter("aut");
    		String genre=(String)request.getParameter("genre");
    		String loc=(String)request.getParameter("loc");
    		request.setAttribute("videos", sm.getSearchedVideo(aut, genre, loc));
    	}
    	
    	//Effettua la taggatura
    	if (pagina.equals("doTag")){
    		
    		
    		String ore=request.getParameter("ore");
    		String min=request.getParameter("min");
    		String sec=request.getParameter("sec");
    		String inizio=null;
    		String durata=null;
    		
    		//Eccezione se il formato inserito nelle caselle ore minuti e secondi non è valido
    		try{
    			inizio=TemporalDecomposition.creaMediaTimePoint(Integer.parseInt(ore), Integer.parseInt(min), Integer.parseInt(sec));
    		
    			durata=TemporalDecomposition.creaMediaDuration(request.getParameter("durata"));
    		}catch (Exception e) {
    				inizio=null;
    				durata=null;
			}
    		String descr=request.getParameter("descrizione");
    		String nomeVideo=request.getParameter("nomeVideo");
    		String parent=request.getParameter("parent");
    		VideoManager video=new VideoManager(getPath());
    		

    		String resMsg = null;
    		if ( nomeVideo != null) {
    			resMsg=video.taggaVideo(nomeVideo, parent, descr, inizio, durata, null);
    		}
    		else {
    			resMsg = "Errore";
    		}
    		request.setAttribute("result", resMsg);
    		request.setAttribute("idVideo", request.getParameter("idVideo"));
    		pagina="resultTag";
    	}
    	
    	//Carica la pagina di inserimento shot
    	if (pagina.equals("tag")){
    		
    		
    		String idVideo= request.getParameter("idVideo");
    		VideoFile video= new VideoFile(getPath()+"/file/video", VideoFile.getXmlFileName(idVideo));
    		request.setAttribute("video", video);
    		
    		request.setAttribute("xmlShots",VideoManager.makeXmlShots(video.getTemporalDecomposition()));
    		
    	}
    	
    	//Trova gli shot cercati e visualizza la lista
    	if (pagina.equals("findShots")){
    		
    		SearchManager sm= new SearchManager(getPath()+"/file/video");
    		String descr=(String)request.getParameter("descrizione");
    		ArrayList<TemporalDecomposition> tDec=sm.getSearchedShots(descr);
    		request.setAttribute("shots", tDec);
    		Map<String,VideoFile> videoMap=new HashMap<String, VideoFile>();
    		for(TemporalDecomposition td:tDec){
    			videoMap.put(td.getVideoId(), new VideoFile(getPath()+"/file/video", VideoFile.getXmlFileName(td.getVideoId())));
    		}
    		request.setAttribute("videoMap", videoMap);
    		
    	}
    	
    	//Effettua l'upload
        if(pagina.equals("uploadReq")){
        
        UploadManager u = new UploadManager();    
    	try{
    		
    	   if(!ServletFileUpload.isMultipartContent(request)){
    			throw new Exception();
		   	   	}
    	DiskFileItemFactory factory= new DiskFileItemFactory();
    	ServletFileUpload upload=new ServletFileUpload(factory);
    	
    	upload.setHeaderEncoding("ISO-8859-1");
    	request.setCharacterEncoding("ISO-8859-1");
    	
    	List<FileItem> items = upload.parseRequest(request);
		
    	String result=u.manageUpload(items,getPath());
    	request.setAttribute("result", result);
    	pagina="resultUpload";
    	
    	} catch (Exception e) {
			e.printStackTrace();
		} 	
    	
        }
        
        //richiama il dispatcher che indirizzerà alla pagina giusta
        disp.dispatch(pagina,request,response);
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
