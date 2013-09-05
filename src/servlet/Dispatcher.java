package servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import utility.UploadManager;


/**
 *  Classe utilizzata dal Controller per reindirizzare alla
 *  pagina richiesta
 */
class Dispatcher{
	/**
	 * Il main non serve l'ho messo solo x provare a creare un file...
	 * se creo il file da servlet non lo crea nella cartella giusta, se lo
	 * faccio da qui va bene...
	 */
	public static void main(String[] arg){
		File fOut = new File("file","ciao");
		System.out.println(fOut.getAbsolutePath()+"\n"+"\n"+fOut.getPath());
		try{
			FileOutputStream fos =new FileOutputStream(fOut);
			fos.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
    
    /**
     * Metodo che verifica l'operazione richiesta e richiama la risorsa web collegata
     * Il parametro op individua l'operazione da effettuare (p.e. login), mentre i parametri 
     * req e res rappresentano la richiesta al Controller e la risposta del Controller
     */
    void dispatch( String op, HttpServletRequest req, HttpServletResponse res) throws IOException,
 ServletException {

		if (op == null) {
			forward("/include/nontrovata.html", req, res);
		} else if (op.equals("uploadReq")) {

			/**
			 * Il parametro "uploadReq" identifica la richiesta di upload
			 * bisogna creare il MultipartRequest e l'UploadManager, e poi
			 * richiamare il metodo manageUpload a cui passare il
			 * MultipartRequest come argomento.
			 * 
			 * bisogna aggiungere un bean e modificare la pagina di upload in
			 * modo che dopo un upload appaia almeno una scritta tipo
			 * "upload avvenuto con successo"
			 */

			forward("upload.jsp", req, res);
		} else if (op.equals("upload")) {
			forward("upload.jsp", req, res);
		} else if (op.equals("resultUpload")) {
			forward("resultUpload.jsp", req, res);
		} else if (op.equals("home") || op.equals("findVideo")) {
			forward("home.jsp", req, res);
		} else if (op.equals("showVideo")) {
			forward("showVideo.jsp", req, res);
		} else if (op.equals("search")) {
			forward("search.jsp", req, res);
		} else if (op.equals("tag")) {
			forward("tag.jsp", req, res);
		} else if (op.equals("resultTag")) {
			forward("resultTag.jsp", req, res);
		} else if (op.equals("searchShots")) {
			forward("searchShots.jsp", req, res);
		} else if (op.equals("findShots")) {
			forward("shotsList.jsp", req, res);
		} else {
			forward("/include/nontrovata.html", req, res);
		}
	}

    /**
     * Esegue un forward standard alla risorsa indicata dal parametro resourceURI.
     * I parametri req e res rappresentano la richiesta al Controller e la risposta del Controller
     */
    void forward( String resourceURI, HttpServletRequest req, HttpServletResponse res) 
            throws IOException, ServletException {
                
        RequestDispatcher dispatcher = req.getRequestDispatcher( resourceURI );
        dispatcher.forward( req, res );
    }
}