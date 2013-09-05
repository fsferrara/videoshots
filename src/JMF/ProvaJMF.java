package JMF;

import gestionexml.FileManagement;

import  java.awt.*;             // Per GUI

import  java.awt.event.*;       // Per la gestione degli eventi

import  javax.media.*;           // Per le JMF

import  java.net.*;             // Per l'URL

import  java.io.*;              // per l'I/O
 
 
 
 
 
 

public class ProvaJMF extends Frame

        implements ActionListener,ControllerListener {
 
 
 
 

        protected       URL             url_media;      // URL del media da riprodurre

        protected       MenuBar         menubar;        // Barra del menu

        protected       Menu            file;           // Menu relativo ai file

        protected       MenuItem        open;           // MenuItem open

        protected       MenuItem        close;          // MenuItem close

        protected       Player          player;         // E' il player da creare

        protected       Component       visual;         // Il visual component

        protected       Component       control;        // Il control panel

        protected       Canvas          testa;          // Intestazione
 
 
 
 
 
 

        /*

        *Costruttore vuoto

        */

        public ProvaJMF(){

                super("MediaPlayer");                     // Costruttore padre
 
 
                
 

                crea_menu();                              // Creiamo i menu

                setSize(new Dimension(400,300));          // Settaggio delle dimensioni del frame

                setLayout(new BorderLayout(5,5));         // Settiamo il layout

                setBackground(new Color(200,200,200));    // Settiamo un colore diverso per gli insets

                enableEvents(AWTEvent.WINDOW_EVENT_MASK); // Abilitiamo gli eventi della Window

        }// fine costruttore vuoto
 
 
 
 
 
 

        // Permette di creare la MenuBar

        private final void crea_menu(){

                // Creazione dei menu

                menubar = new MenuBar();

                file            = new Menu("File");

                open            = new MenuItem("Open");

                close   = new MenuItem("Close");

                // Registrazione degli ebenti ai menu

                open.addActionListener(this);

                close.addActionListener(this);

                close.setEnabled(false);

                // Composizione

                file.add(open);

                file.add(close);

                menubar.add(file);

                setMenuBar(menubar);

        }// fine crea_menu
 
 
 
 
 
 

        // Gestisce le scelte fatte con i menu

        public void actionPerformed(ActionEvent ae){

                MenuItem sorg= (MenuItem)(ae.getSource());

                if (sorg.equals(open)){

                        manage_open();

                } else if (sorg.equals(close)){

                        manage_close();

                }

        }// fine actionPerformed
 
 
 
 
 
 

        // Rappresenta il metodo principale perchè permette di gestire

        // i cambiamenti di stato del Player

        public synchronized void controllerUpdate(ControllerEvent ce){

                if (player!=null){      // Se esistem il player

                        if (ce instanceof RealizeCompleteEvent){        // Se il player è nello stato Realized
                        	
                        	
                        	//setta quando si deve fermare la riproduzione ma non taglia il video
                        	//player.setStopTime(new Time(2.0));
                                if ((control=player.getControlPanelComponent())!=null)  // Se c'è prendiamo il
                                		
                                        add("South",control);                           // ControlPanel

                                if ((visual=player.getVisualComponent())!=null) // Se esiste prendiamo il VisualPanel

                                        add("Center",visual);           // E lo mettiamo al centro

                                validate();             // Mettiamo a posto il layout

                        }// fine if
                        else if (ce instanceof PrefetchCompleteEvent){
                        	// Se il player è nello stato Prefatched
                        	//Setta quando deve iniziare il video ma non lo taglia.
                        	player.setMediaTime(new Time(20.0));
//                        	System.out.println(player.getMediaTime().getSeconds());
                        	player.start();
                        }
                }       // finr if esterno

        }// fine controllerUpdate
 
 
 
 

        // Permette di creare il player a partire dall'url

        private final void crea_player(){

                        if (url_media!=null){   // Se e' stato creato un url

                                try{

                                        player = Manager.createPlayer(url_media);       // Creiamo il player
                                        player.addControllerListener(this);             // Sentiamo i ControllerListener
                                        
                                        
                                        
                                        
                                        
                                        
                                        player.realize();
                                        
//                                        player.start();
                                        
                                        //player.syncStart(new Time(60));
                                        
                                        
                                        open.setEnabled(false);                         // Disabilitiamo l'open

                                        close.setEnabled(true);                         // Abilitiamo la close

                                }catch(IOException ioe){

                                        System.out.println("ERRORE "+ioe.toString());

                                } catch (NoPlayerException e) {
									e.printStackTrace();
								}

                        }

        }// fine creaplayer
 
 
 
 
 
 

        // Permette di scegliere il file e di ottenere da esso

        // l'url

        private final void manage_open(){

                url_media=null;

                FileDialog fd= new FileDialog(this,"LOAD FILE",FileDialog.LOAD);

                //fd.setFile("*.au;*.wav;*.mid;*.avi;*.mpg");

                fd.setVisible(true);

                if ((fd.getFile()!=null)&&(fd.getDirectory()!=null)){

                        try{

                                String str=fd.getDirectory()+fd.getFile();

                                url_media=new URL(FileManagement.URL_FILE_PREFIX + str);

                                setTitle("MediaPlayer :"+fd.getFile());

                        } catch(MalformedURLException mue){

                                System.out.println("ERRORE "+mue.toString());

                        }       catch(IOException ioe){

                                System.out.println("ERRORE "+ioe.toString());

                        }

                        crea_player();

                }// fine if

                fd.dispose();

        }// fine manage_open
 
 
 
 

        // Chiude il player

        private final void manage_close(){

                player.deallocate();                    // Libera le risorse esclusive

                url_media=null;                                         // mette l'url a null

                removeAll();                                                    // Toglie i Panel

                open.setEnabled(true);          // Riabilita la open

                close.setEnabled(false);        // Ridisabilita la close

                setTitle("MediaPlayer");        // Aggiorna il titolo del Frame

        }// fine manage_close
 
 
 
 

        /**

        * Permette di mettere il Dialog in centro

        */

        public void setVisible(boolean vis){

                Dimension sc=Toolkit.getDefaultToolkit().getScreenSize();       // Dim. Schermo

                int pos_x=((sc.width-getSize().width)/2);               // Posizione x centrata

                int pos_y=((sc.height-getSize().height)/2);     // Posizione y centrata

                setBounds(pos_x,pos_y,getSize().width,getSize().height);

                super.setVisible(vis);

        }// fine setVisible()
 
 
 
 
 
 

 // Gestisce la chiusura

 protected void processWindowEvent(WindowEvent e){

	// System.out.println(e.paramString());
  if ((e.paramString().startsWith("WINDOW_CLOSING"))){
	  
                
                if (player!=null)

                        player.deallocate();
         
                e.getWindow().dispose();
        }       // fine if

        if ((e.paramString()).equals("WINDOW_CLOSED")){

        		e.getWindow().dispose();
        }
 }// fine processWindowEvent
 
 
 
 

        public static void main(String[] str){

                ProvaJMF mp= new ProvaJMF();

                mp.setVisible(true);

        }
 
 
 
 

}// fine MultiMediaPlayer 