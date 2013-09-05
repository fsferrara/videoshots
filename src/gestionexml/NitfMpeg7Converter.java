package gestionexml;

import org.w3c.dom.*;

/**
 * Classe che fornisce metodi per la conversione dei
 * file xml secondo le specifiche Nitf in file Mpeg-7
 */
public class NitfMpeg7Converter {
	
	/**
	 * nitf contiene tutte le informazioni estratte dal file XML in formato NITF
	 */
	private NitfMetaData nitf = null;

	/**
	 * Converte un documento xml in formato NITF in uno equivalente in formato Mpeg-7
	 * senza perdita d informazione.
	 *  
	 * @param nitfDoc documento XML in formato NITF da convertire
	 * @param mpegDoc nuovo documento XML in formato Mpeg-7 contenente i dati estratti da nitfDoc
	 * 
	 * @return ritorna un oggetto {@link NitfMetaData} contenete tutte le informazioni estratte dal documento XML in formato NITF
	 */
	public NitfMetaData converti(Document nitfDoc, Document mpegDoc) {
		
		/**
		 * Estrazione dei dati dal documento nitfDoc in un oggetto di tipo NitfMetaData
		 */
		nitf = produceMetaData(nitfDoc);
		
		/**
		 * Creazione del documento XML in formato Mpeg-7
		 */
		produceMpegDom(mpegDoc);
		
		return nitf;
	}
	
	/**
	 * Converte il dato 'duration' dal formato usato nello standard NITF,
	 * al formato usato nello standard Mpeg-7
	 * 
	 * @param duration durata del video nel formato NITF
	 * 
	 * @return dutata del video nel formato Mpeg-7
	 */
	private String convertiDurata(String duration) {
		
		int durata = Integer.parseInt(duration);
		
		int secondi = durata % 60;
		int minuti = ((int) durata / 60) % 60;
		int ore = (int) ((durata / 60) / 60);
		
		String newDuration = "PT" + ore + "H" + minuti + "M" + secondi + "S";
		
		return newDuration;
	}

	/**
	 * Converte il dato 'date' dal formato usato nello standard NITF,
	 * al formato usato nello standard Mpeg-7
	 * 
	 * @param date data nel formato NITF
	 * 
	 * @return data nel formato Mpeg-7
	 */
	private String convertiData(String date) {
		
		String newDate =  date.substring(0, 4) +
						"-" +
						date.substring(4, 6) + 
						"-" +
						date.substring(6, 11) +
						":" +
						date.substring(11, 13) +
						":" +
						date.substring(13, 15);

		return newDate;
	}
	
	/**
	 * Produce un documento XML nel formato Mpeg-7, usando i dati contenuti
	 * nell'oggetto {@link NitfMpeg7Converter#nitf}
	 * 
	 * @param doc Documento inizialmente vuoto, alla fine del calcolo conterra' il documento Mpeg-7 generato
	 */
	private void produceMpegDom(Document doc) {

			//Commento e versione del file
			doc.setXmlStandalone(false); //An attribute specifying, as part of the XML declaration, whether this document is standalone. This is false when unspecified.
			Comment comment = doc.createComment("Metadati generati automaticamente dall'applicazione");
            doc.appendChild(comment);
            
			// Element MPEG7
            Element mpeg7 = doc.createElement("Mpeg7");
            mpeg7.setAttribute("xmlns", "urn:mpeg:mpeg7:schema:2001");
            mpeg7.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            mpeg7.setAttribute("xmlns:mpeg7", "urn:mpeg:mpeg7:schema:2001");
            mpeg7.setAttribute("xsi:schemaLocation", "urn:mpeg:mpeg7:schema:2001 Mpeg7-2001.xsd");
            doc.appendChild(mpeg7);
            
            // Element MPEG7->Description
            Element description = doc.createElement("Description");
            description.setAttribute("xsi:type", "ContentEntityType");
            mpeg7.appendChild(description);
            
            // Element MPEG7->Description->MultimediaContent
            Element multimediaContent = doc.createElement("MultimediaContent");
            multimediaContent.setAttribute("xsi:type", "VideoType");
            description.appendChild(multimediaContent);
            
            // Element MPEG7->Description->MultimediaContent->Video
            Element video = doc.createElement("Video");
            multimediaContent.appendChild(video);
            		
			// Element Video->MediaInformation
            Element mediaInformation = doc.createElement("MediaInformation");
            video.appendChild(mediaInformation);
            
            // Element Video->MediaInformation->MediaProfile
            Element mediaProfile = doc.createElement("MediaProfile");
            mediaInformation.appendChild(mediaProfile);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat
            Element mediaFormat = doc.createElement("MediaFormat");
            mediaProfile.appendChild(mediaFormat);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->Content
            Element content = doc.createElement("Content");
            content.setAttribute("href", "MPEG7ContentCS");
            mediaFormat.appendChild(content);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->FileFormat
            Element fileFormat = doc.createElement("FileFormat");
            fileFormat.setAttribute("href", "urn:mpeg:mpeg7:cs:FileFormatCS:2001:3");
            mediaFormat.appendChild(fileFormat);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->FileFormat->Name
            Element name = doc.createElement("Name");
            name.appendChild(doc.createTextNode("mpeg"));
            fileFormat.appendChild(name);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->FileSize
            Element fileSize = doc.createElement("FileSize");
            fileSize.appendChild(doc.createTextNode(nitf.getVideoSize()));
            mediaFormat.appendChild(fileSize);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->VisualCoding
            Element visualCoding = doc.createElement("VisualCoding");
            mediaFormat.appendChild(visualCoding);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->VisualCoding->Format
            Element format = doc.createElement("Format");
            format.setAttribute("colorDomain", "color");
            format.setAttribute("href", "urn:mpeg:mpeg7:cs:VisualCodingFormatCS:2001:1");
            visualCoding.appendChild(format);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaFormat->VisualCoding->Frame
            Element frame = doc.createElement("Frame");
            frame.setAttribute("height", nitf.getVideoHeight());
            frame.setAttribute("rate", nitf.getVideoBitrate());
            frame.setAttribute("width", nitf.getVideoWidth());
            visualCoding.appendChild(frame);
            
			// Element ...->Video->MediaInformation->MediaProfile->MediaInstance
            Element mediaInstance = doc.createElement("MediaInstance");
            mediaInstance.setAttribute("id", "v" + nitf.getIdString());
            mediaProfile.appendChild(mediaInstance);
            
			// Element ...->Video->MediaInformation->MediaProfile->MediaInstance->InstanceIdentifier
            Element instanceIdentifier = doc.createElement("InstanceIdentifier");
//            instanceIdentifier.setAttribute("organization", nitf.getRegSrc());
//            instanceIdentifier.setAttribute("type", "MPEG7ContentSetOnLineId");
//            instanceIdentifier.appendChild(doc.createTextNode("mpeg7/mpeg/"));
            mediaInstance.appendChild(instanceIdentifier);
            
            // Element ...->Video->MediaInformation->MediaProfile->MediaInstance->MediaLocator
            Element mediaLocator = doc.createElement("MediaLocator");
            mediaInstance.appendChild(mediaLocator);
            
			// Element ...->Video->MediaInformation->MediaProfile->MediaInstance->MediaLocator->MediaUri
            Element mediaUri = doc.createElement("MediaUri");
            mediaUri.appendChild(doc.createTextNode(nitf.getVideoDataLocation()));
            mediaLocator.appendChild(mediaUri);
            
			// Element ...->Video->CreationInformation
            Element creationInformation = doc.createElement("CreationInformation");
            video.appendChild(creationInformation);
            
            // Element ...->Video->CreationInformation->Creation
            Element creation = doc.createElement("Creation");
            creationInformation.appendChild(creation);
            
            // Element ...->Video->CreationInformation->Creation->Title
            Element title = doc.createElement("Title");
            title.appendChild(doc.createTextNode(nitf.getTitle()));
            creation.appendChild(title);
            	
            // Element ...->Video->CreationInformation->Creation->Abstract
            Element abstract1 = doc.createElement("Abstract");
            creation.appendChild(abstract1);
            
            // Element ...->Video->CreationInformation->Creation->Abstract->FreeTextAnnotation
            Element freeTextAnnotation1 = doc.createElement("FreeTextAnnotation");
            freeTextAnnotation1.appendChild(doc.createTextNode(nitf.getHl2() + ": " + nitf.getP()));
            abstract1.appendChild(freeTextAnnotation1);
 
            // Element ...->Video->CreationInformation->Creation->Abstract->FreeTextAnnotation
            Element freeTextAnnotation2 = doc.createElement("FreeTextAnnotation");
            freeTextAnnotation2.appendChild(doc.createTextNode(nitf.getHl2() + ": " + nitf.getP()));
            abstract1.appendChild(freeTextAnnotation2);
            
            // Element ...->Video->CreationInformation->Creation->Abstract->KeywordAnnotation
//            Element keywordAnnotation1 = doc.createElement("KeywordAnnotation");
//            abstract1.appendChild(keywordAnnotation1);
            
            // Element ...->Video->CreationInformation->Creation->Abstract->KeywordAnnotation->Keyword
//            Element keyword1 = doc.createElement("Keyword");
//            keyword1.appendChild(doc.createTextNode("obama"));
//            keywordAnnotation1.appendChild(keyword1);
            
            // Element ...->Video->CreationInformation->Creation->Abstract->StructuredAnnotation
            Element structuredAnnotation = doc.createElement("StructuredAnnotation");
            abstract1.appendChild(structuredAnnotation);
            
            // Element ...->Video->CreationInformation->Creation->Abstract->StructuredAnnotation->When            
            Element when = doc.createElement("When");
            Element whenName = doc.createElement("Name");
            whenName.appendChild(doc.createTextNode( nitf.getStoryDate() ));
            when.appendChild(whenName);
            structuredAnnotation.appendChild(when);
            
//            // Element ...->Video->CreationInformation->Creation->Abstract->StructuredAnnotation->WhatAction
//            Element whatAction = doc.createElement("WhatAction");
//            structuredAnnotation.appendChild(whatAction);
//            
//            // Element ...->Video->CreationInformation->Creation->Abstract->StructuredAnnotation->Where
//            Element where = doc.createElement("Where");
//            structuredAnnotation.appendChild(where);
            
            // Element ...->Video->CreationInformation->Creation->Creator
            Element creator = doc.createElement("Creator");
            creation.appendChild(creator);
            
            // Element ...->Video->CreationInformation->Creation->Creator->Role
            Element role = doc.createElement("Role");
            role.setAttribute("href", "urn:mpeg:mpeg7:cs:RoleCS:2001:producer");
            creator.appendChild(role);

            // Element ...->Video->CreationInformation->Creation->Creator->Role->Name
            Element name6 = doc.createElement("Name");
            name6.appendChild(doc.createTextNode(nitf.getByLine()));
            role.appendChild(name6);
            
            // Element ...->Video->CreationInformation->Creation->Creator->Agent
            Element agent = doc.createElement("Agent");
            agent.setAttribute("xsi:type", "OrganizationType");
            creator.appendChild(agent);
            
            // Element ...->Video->CreationInformation->Creation->Creator->Agent->Name
            Element name2 = doc.createElement("Name");
            name2.appendChild(doc.createTextNode(nitf.getDistributor()));
            agent.appendChild(name2);

            // Element ...->Video->CreationInformation->Creation->CreationCoordinates
            Element creationCoordinates = doc.createElement("CreationCoordinates");
            creation.appendChild(creationCoordinates);
            
            // Element ...->Video->CreationInformation->Creation->CreationCoordinates->Location
            Element location1 = doc.createElement("Location");
            creationCoordinates.appendChild(location1);
            
            // Element ...->Video->CreationInformation->Creation->CreationCoordinates->Location->Name
            Element name3 = doc.createElement("Name");
            name3.appendChild(doc.createTextNode(nitf.getLocation()));
            location1.appendChild(name3);
            
            // Element ...->Video->CreationInformation->Creation->CreationCoordinates->Date
            Element date = doc.createElement("Date");
            creationCoordinates.appendChild(date);
            
            // Element ...->Video->CreationInformation->Creation->CreationCoordinates->Date->TimePoint
            Element timePoint = doc.createElement("TimePoint");
            timePoint.appendChild(doc.createTextNode(convertiData(nitf.getDateIssue())));
            date.appendChild(timePoint);
            
            // Element ...->Video->CreationInformation->Creation->CopyrightString
            Element copyrightString = doc.createElement("CopyrightString");
            copyrightString.appendChild(doc.createTextNode(nitf.getDocCopyrightHolder()));
            creation.appendChild(copyrightString);

            // Element ...->Video->CreationInformation->Classification
            Element classification = doc.createElement("Classification");
            creationInformation.appendChild(classification);
            
            // Element ...->Video->CreationInformation->Classification->Genre
            Element genre = doc.createElement("Genre");
            genre.setAttribute("href", "urn:mpeg:TVAnytime_v0.1ContentCS:3.14");
            classification.appendChild(genre);
            
            // Element ...->Video->CreationInformation->Classification->Genre->Name
            Element name4 = doc.createElement("Name");
            name4.appendChild(doc.createTextNode(nitf.getCategory()));
            genre.appendChild(name4);
            
            // Element ...->Video->CreationInformation->Classification->MediaReview
            Element mediaReview = doc.createElement("MediaReview");
            classification.appendChild(mediaReview);
            
            // Element ...->Video->CreationInformation->Classification->MediaReview->Rating
            Element rating = doc.createElement("Rating");
            mediaReview.appendChild(rating);
            
            // Element ...->Video->CreationInformation->Classification->MediaReview->Rating->RatingValue
            Element ratingValue = doc.createElement("RatingValue");
            ratingValue.appendChild(doc.createTextNode("0.0"));
            rating.appendChild(ratingValue);
            
            // Element ...->Video->CreationInformation->Classification->MediaReview->Rating->RatingScheme
            Element ratingScheme = doc.createElement("RatingScheme");
            ratingScheme.setAttribute("best", "5");
            ratingScheme.setAttribute("style", "higherBetter");
            ratingScheme.setAttribute("worst", "0");
            rating.appendChild(ratingScheme);
            
            // Element ...->Video->CreationInformation->Classification->MediaReview->Rating->RatingScheme->Name
            Element name5 = doc.createElement("Name");
            name5.appendChild(doc.createTextNode("Overall"));
            ratingScheme.appendChild(name5);
            
			// Element ...->Video->UsageInformation
            Element usageInformation = doc.createElement("UsageInformation");
            video.appendChild(usageInformation);

            // Element ...->Video->UsageInformation->Availability
            Element availability = doc.createElement("Availability");
            availability.setAttribute("id", "onDemand");
            usageInformation.appendChild(availability);
            
            // Element ...->Video->UsageInformation->Availability->InstanceRef
            Element instanceRef = doc.createElement("InstanceRef");
            instanceRef.setAttribute("href", "MPEG7PublicationTypeCS:4");
            availability.appendChild(instanceRef);
            
            // Element ...->Video->UsageInformation->Availability->Dissemination
            Element dissemination = doc.createElement("Dissemination");
            availability.appendChild(dissemination);

            // Element ...->Video->UsageInformation->Availability->Dissemination->Format
            Element format1 = doc.createElement("Format");
            format1.setAttribute("href", "MPEG7PublicationTypeCS:4");
            dissemination.appendChild(format1);
            
            // Element ...->Video->UsageInformation->Availability->Dissemination->Format->Name
            Element name1 = doc.createElement("Name");
            name1.appendChild(doc.createTextNode("Internet"));
            format1.appendChild(name1);
            
            // Element ...->Video->UsageInformation->Availability->Dissemination->Location
            Element location = doc.createElement("Location");
            dissemination.appendChild(location);
            
            // Element ...->Video->UsageInformation->Availability->Dissemination->Location->Region
            Element region = doc.createElement("Region");
            region.appendChild(doc.createTextNode("it"));
            location.appendChild(region);
            
            // Element ...->Video->UsageInformation->UsageRecord
            Element usageRecord = doc.createElement("UsageRecord");
            usageInformation.appendChild(usageRecord);
            
            // Element ...->Video->UsageInformation->UsageRecord->AvailabilityRef
            Element availabilityRef = doc.createElement("AvailabilityRef");
            availabilityRef.setAttribute("idref", "onDemand");
            usageRecord.appendChild(availabilityRef);
            
            // Element ...->Video->UsageInformation->UsageRecord->Audience
            Element audience = doc.createElement("Audience");
            audience.appendChild(doc.createTextNode("0")); //Contatore delle visualizzazioni del video
            usageRecord.appendChild(audience);
            
			// Element ...->Video->MediaTime
            Element mediaTime = doc.createElement("MediaTime");
            video.appendChild(mediaTime);
            
            // Element ...->Video->MediaTime->MediaTimePoint
            Element mediaTimePoint = doc.createElement("MediaTimePoint");
            mediaTimePoint.appendChild(doc.createTextNode("T00:00:00"));
            mediaTime.appendChild(mediaTimePoint);
            
            // Element ...->Video->MediaTime->MediaDuration
            Element mediaDuration = doc.createElement("MediaDuration");
            mediaDuration.appendChild(doc.createTextNode(convertiDurata(nitf.getVideoTime())));
            mediaTime.appendChild(mediaDuration);
            
            // Element MPEG7->Description->MultimediaContent
            Element iMultimediaContent = doc.createElement("MultimediaContent");
            iMultimediaContent.setAttribute("xsi:type", "ImageType");
            description.appendChild(iMultimediaContent);
            
            // Element MPEG7->Description->MultimediaContent->Image
            Element image = doc.createElement("Image");
            iMultimediaContent.appendChild(image);
            		
            // Element Image->MediaInformation
            Element iMediaInformation = doc.createElement("MediaInformation");
            image.appendChild(iMediaInformation);
            
            // Element Image->MediaInformation->MediaProfile
            Element iMediaProfile = doc.createElement("MediaProfile");
            iMediaInformation.appendChild(iMediaProfile);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat
            Element iMediaFormat = doc.createElement("MediaFormat");
            iMediaProfile.appendChild(iMediaFormat);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->Content
            Element iContent = doc.createElement("Content");
            iContent.setAttribute("href", "urn:mpeg:mpeg7:cs:ContentCS:2001:2");
            iMediaFormat.appendChild(iContent);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->Content->Name
            Element iName2 = doc.createElement("Name");
            iName2.appendChild(doc.createTextNode("visual"));
            iContent.appendChild(iName2);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->FileFormat
            Element iFileFormat = doc.createElement("FileFormat");
            iFileFormat.setAttribute("href", "urn:mpeg:mpeg7:cs:FileFormatCS:2001:3");
            iMediaFormat.appendChild(iFileFormat);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->FileFormat->Name
            Element iName3 = doc.createElement("Name");
            iName3.appendChild(doc.createTextNode("JPEG2000"));
            iFileFormat.appendChild(iName3);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->FileSize
            Element iFileSize = doc.createElement("FileSize");
            iFileSize.appendChild(doc.createTextNode(nitf.getImageSize()));
            iMediaFormat.appendChild(iFileSize);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->VisualCoding
            Element iVisualCoding = doc.createElement("VisualCoding");
            iMediaFormat.appendChild(iVisualCoding);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->VisualCoding->Format
            Element iFormat = doc.createElement("Format");
            iFormat.setAttribute("href", "urn:mpeg:mpeg7:cs:VisualCodingFormatCS:2001:1");
            iFormat.setAttribute("colorDomain", "binary");
            iVisualCoding.appendChild(iFormat);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->VisualCoding->Format->Name
            Element iName4 = doc.createElement("Name");
            iName4.appendChild(doc.createTextNode("JPEG2000"));
            iFormat.appendChild(iName4);
            
            // Element Image->MediaInformation->MediaProfile->MediaFormat->VisualCoding->Frame
            Element iFrame = doc.createElement("Frame");
            iFrame.setAttribute("height", nitf.getImageHeight());
            iFrame.setAttribute("width", nitf.getImageWidth());
            iFrame.setAttribute("rate", nitf.getImageBitrate());
            iVisualCoding.appendChild(iFrame);
            
            // Element Image->MediaInformation->MediaProfile->MediaInstance
            Element iMediaInstance = doc.createElement("MediaInstance");
            iMediaInstance.setAttribute("id", "i" + nitf.getIdString());
            iMediaProfile.appendChild(iMediaInstance);
            
            // Element Image->MediaInformation->MediaProfile->InstanceIdentifier
            Element iInstanceIdentifier = doc.createElement("InstanceIdentifier");
            iMediaInstance.appendChild(iInstanceIdentifier);
            
            // Element Image->MediaInformation->MediaProfile->MediaLocator
            Element iMediaLocator1 = doc.createElement("MediaLocator");
            iMediaInstance.appendChild(iMediaLocator1);
            
            // Element Image->MediaInformation->MediaProfile->MediaLocator->MediaUri
            Element iMediaUri1 = doc.createElement("MediaUri");
            iMediaUri1.appendChild(doc.createTextNode(nitf.getImageDataLocation()));
            iMediaLocator1.appendChild(iMediaUri1);
            
            // Element Image->CreationInformation
            Element iCreationInformation = doc.createElement("CreationInformation");
            image.appendChild(iCreationInformation);
            
            // Element Image->CreationInformation->Creation
            Element iCreation = doc.createElement("Creation");
            iCreationInformation.appendChild(iCreation);
            
            // Element Image->CreationInformation->Creation->Title
            Element iTitle = doc.createElement("Title");
            iTitle.appendChild(doc.createTextNode(nitf.getImageText()));
            iCreation.appendChild(iTitle);
            
            // Element Image->CreationInformation->Creation->Creator
            Element iCreator = doc.createElement("Creator");
            iCreation.appendChild(iCreator);
            
            // Element Image->CreationInformation->Creation->Creator->Role
            Element iRole = doc.createElement("Role");
            iRole.setAttribute("href", "urn:mpeg:mpeg7:cs:RoleCS:AUTHOR");
            iCreator.appendChild(iRole);
            
            // Element Image->CreationInformation->Creation->Creator->Role->Name
            Element iName = doc.createElement("Name");
            iName.appendChild(doc.createTextNode(nitf.getAuthor()));
            iRole.appendChild(iName);
            
            // Element Image->CreationInformation->Creation->Creator->Agent
            Element iAgent = doc.createElement("Agent");
            iAgent.setAttribute("xsi:type", "OrganizationType");
            iCreator.appendChild(iAgent);
            
            // Element Image->CreationInformation->Creation->Creator->Agent->Name
            Element iName1 = doc.createElement("Name");
            iName1.appendChild(doc.createTextNode("MPEG"));
            iAgent.appendChild(iName1);
	}
	
	/**
	 * Dato un documento XML in formato NITF, estrapola le informazioni memorizzandole in un
	 * oggetto {@link NitfMetaData}.
	 * Per il funzionamento si serve dei metodi {@link NitfMpeg7Converter#elaboraHead(Node, NitfMetaData)}
	 * e {@link NitfMpeg7Converter#elaboraBody(Node, NitfMetaData)}
	 * 
	 * @param nitfDoc Documento XML nel formato NITF
	 * @return oggetto {@link NitfMetaData} contenente i dati estratti da nitfDoc
	 */
	private NitfMetaData produceMetaData(Document nitfDoc) {
		NitfMetaData nitf = new NitfMetaData();

			NodeList nodes = nitfDoc.getChildNodes();
			
			for (int i=0 ; i < nodes.getLength() ; i++) {
				Node nodeNitf = nodes.item(i);
				if (nodeNitf.getNodeType() == Node.ELEMENT_NODE) {
					
					NodeList liv1 = nodeNitf.getChildNodes();
					
					for (int j=0 ; j < liv1.getLength() ; j++) {
						Node nodeLiv1 = liv1.item(j);
						if (nodeLiv1.getNodeType() == Node.ELEMENT_NODE) {
							
							if (nodeLiv1.getNodeName().equals("head")) {
								nitf= elaboraHead(nodeLiv1, nitf);
							}
							
							if (nodeLiv1.getNodeName().equals("body")) {
								nitf = elaboraBody(nodeLiv1, nitf);
							}
							
							
						}
					}
					
				}
			}
			return nitf;
	}

	/**
	 * Parte del calcolo relativo alla HEAD del documento NITF.
	 * 
	 * @see NitfMpeg7Converter#produceMetaData(Document)
	 */
	private NitfMetaData elaboraHead(Node head, NitfMetaData nitf) {
		
		NodeList nodes = head.getChildNodes();

		for (int i=0 ; i < nodes.getLength() ; i++) {
			
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				if (node.getNodeName().equals("title")) {
					String title = node.getFirstChild().getTextContent();
					nitf.setTitle(title);
				}
				
				if (node.getNodeName().equals("meta")){
					String name = node.getAttributes().getNamedItem("name").getNodeValue();
					//System.out.println("ATTR" + name);
					
					if(name.equals("apcom-xmlversion")){
						String xmlVersion = node.getAttributes().getNamedItem("content").getNodeValue();
						nitf.setXmlVersion(xmlVersion);
					}
					
					if(name.equals("apcom-category")){
						String category = node.getAttributes().getNamedItem("content").getNodeValue();
						nitf.setCategory(category);
					}
					
					if(name.equals("apcom-product")){
						String product = node.getAttributes().getNamedItem("content").getNodeValue();
						nitf.setProduct(product);
					}
					
					if(name.equals("apcom-author")){
						String author = node.getAttributes().getNamedItem("content").getNodeValue();
						nitf.setAuthor(author);
					}
					
					if(name.equals("apcom-source")){
						String source = node.getAttributes().getNamedItem("content").getNodeValue();
						nitf.setSource(source);
					}
					
				}
				

				if (node.getNodeName().equals("docdata")){
					
					NodeList nodesLiv2 = node.getChildNodes();

					for (int j=0 ; j < nodesLiv2.getLength() ; j++) {
						
						Node nodeLiv2 = nodesLiv2.item(j);
						
						if (nodeLiv2.getNodeType() == Node.ELEMENT_NODE) {
							
							if (nodeLiv2.getNodeName().equals("doc-id")) {
								String regSrc = nodeLiv2.getAttributes().getNamedItem("regsrc").getNodeValue();
								nitf.setRegSrc(regSrc);
								String idString = nodeLiv2.getAttributes().getNamedItem("id-string").getNodeValue();
								nitf.setIdString(idString);
							}
							
							if (nodeLiv2.getNodeName().equals("urgency")) {
								String urgency = nodeLiv2.getAttributes().getNamedItem("ed-urg").getNodeValue();
								nitf.setUrgency(urgency);
							}
							
							if (nodeLiv2.getNodeName().equals("date.issue")) {
								String dateIssue = nodeLiv2.getAttributes().getNamedItem("norm").getNodeValue();
								nitf.setDateIssue(dateIssue);
							}
							
							if (nodeLiv2.getNodeName().equals("du-key")) {
								String duKey = nodeLiv2.getAttributes().getNamedItem("key").getNodeValue();
								nitf.setDuKey(duKey);	
							}
							
							if (nodeLiv2.getNodeName().equals("doc.copyright")) {
								String docCopyrightHolder = nodeLiv2.getAttributes().getNamedItem("holder").getNodeValue();
								nitf.setDocCopyrightHolder(docCopyrightHolder);
							}
							
						}
					}
				}
			}
		}
		return nitf;
		
	}
	
	/**
	 * Parte del calcolo relativo al BODY del documento NITF.
	 * 
	 * @see NitfMpeg7Converter#produceMetaData(Document)
	 */
	private NitfMetaData elaboraBody(Node body, NitfMetaData nitf) {

		NodeList nodes = body.getChildNodes();

		for (int i=0 ; i < nodes.getLength() ; i++) {
			
			Node node = nodes.item(i);
			
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				if (node.getNodeName().equals("body.head")) {
					NodeList nodesLiv2 = node.getChildNodes();

					for (int j=0 ; j < nodesLiv2.getLength() ; j++) {
						
						Node nodeLiv2 = nodesLiv2.item(j);
						
						if (nodeLiv2.getNodeType() == Node.ELEMENT_NODE) {
							
							if (nodeLiv2.getNodeName().equals("hedline")) {
								
								NodeList nodesLiv3 = nodeLiv2.getChildNodes();

								for (int k=0 ; k < nodesLiv3.getLength() ; k++) {
									
									Node nodeLiv3 = nodesLiv3.item(k);
									
									if (nodeLiv3.getNodeType() == Node.ELEMENT_NODE) {
										
										if (nodeLiv3.getNodeName().equals("hl1")) {
											String hl1 = nodeLiv3.getFirstChild().getTextContent();
											nitf.setHl1(hl1);
										}
										
										if (nodeLiv3.getNodeName().equals("hl2")) {
											String hl2 = nodeLiv3.getFirstChild().getTextContent();
											nitf.setHl2(hl2);
										}
									}										
								}
										
							}
							
							if (nodeLiv2.getNodeName().equals("byline")) {
								String byLine = nodeLiv2.getFirstChild().getTextContent();
								nitf.setByLine(byLine);								
							}
							
							if (nodeLiv2.getNodeName().equals("distributor")) {
								String distributor = nodeLiv2.getFirstChild().getTextContent();
								nitf.setDistributor(distributor);
							}
							
							if (nodeLiv2.getNodeName().equals("dateline")) {
								NodeList nodesLiv3 = nodeLiv2.getChildNodes();

								for (int k=0 ; k < nodesLiv3.getLength() ; k++) {
									
									Node nodeLiv3 = nodesLiv3.item(k);
									
									if (nodeLiv3.getNodeType() == Node.ELEMENT_NODE) {
										
										if (nodeLiv3.getNodeName().equals("location")) {
											String location = nodeLiv3.getFirstChild().getTextContent();
											nitf.setLocation(location);
										}
										
										if (nodeLiv3.getNodeName().equals("story.date")) {
											String storyDate = nodeLiv3.getFirstChild().getTextContent();
											nitf.setStoryDate(storyDate);
										}
									}										
								}
							}
						}
					}
					
				}
				
				if (node.getNodeName().equals("body.content")) {
					NodeList nodesLiv2 = node.getChildNodes();

					for (int j=0 ; j < nodesLiv2.getLength() ; j++) {
						
						Node nodeLiv2 = nodesLiv2.item(j);
						
						if (nodeLiv2.getNodeType() == Node.ELEMENT_NODE) {
							
							if (nodeLiv2.getNodeName().equals("block")) {
								NodeList nodesLiv3 = nodeLiv2.getChildNodes();

								for (int k=0 ; k < nodesLiv3.getLength() ; k++) {
									
									Node nodeLiv3 = nodesLiv3.item(k);
									
									if (nodeLiv3.getNodeType() == Node.ELEMENT_NODE) {
										
										if (nodeLiv3.getNodeName().equals("p")) {
											String p = nodeLiv3.getFirstChild().getTextContent();
											nitf.setP(p);
										}
										
										if (nodeLiv3.getNodeName().equals("media")) {
											String mediaType = nodeLiv3.getAttributes().getNamedItem("media-type").getNodeValue();
											
											if(mediaType.equals("video")){
												NodeList nodesLiv4 = nodeLiv3.getChildNodes();

												for (int z=0 ; z < nodesLiv4.getLength() ; z++) {
													
													Node nodeLiv4 = nodesLiv4.item(z);
													
													if (nodeLiv4.getNodeType() == Node.ELEMENT_NODE) {
														
														if (nodeLiv4.getNodeName().equals("media-reference")) {
															String videoDataLocation = nodeLiv4.getAttributes().getNamedItem("data-location").getNodeValue();
															String videoMimeType = nodeLiv4.getAttributes().getNamedItem("mime-type").getNodeValue();
															String videoWidth = nodeLiv4.getAttributes().getNamedItem("width").getNodeValue();
															String videoHeight = nodeLiv4.getAttributes().getNamedItem("height").getNodeValue();
															String videoSize = nodeLiv4.getAttributes().getNamedItem("size").getNodeValue();
															String videoTime = nodeLiv4.getAttributes().getNamedItem("time").getNodeValue();
															String videoBitrate = nodeLiv4.getAttributes().getNamedItem("bitrate").getNodeValue();
															String videoText = nodeLiv4.getFirstChild().getTextContent();
															
															nitf.setVideoDataLocation(videoDataLocation);
															nitf.setVideoMimeType(videoMimeType);
															nitf.setVideoWidth(videoWidth);
															nitf.setVideoHeight(videoHeight);
															nitf.setVideoSize(videoSize);
															nitf.setVideoTime(videoTime);
															nitf.setVideoBitrate(videoBitrate);
															nitf.setVideoText(videoText);
														
														}
													}
												}	
											}
											
											if(mediaType.equals("image")){
												NodeList nodesLiv4 = nodeLiv3.getChildNodes();

												for (int z=0 ; z < nodesLiv4.getLength() ; z++) {
													
													Node nodeLiv4 = nodesLiv4.item(z);
													
													if (nodeLiv4.getNodeType() == Node.ELEMENT_NODE) {
														
														if (nodeLiv4.getNodeName().equals("media-reference")) {
															String imageDataLocation = nodeLiv4.getAttributes().getNamedItem("data-location").getNodeValue();
															String imageMimeType = nodeLiv4.getAttributes().getNamedItem("mime-type").getNodeValue();
															String imageWidth = nodeLiv4.getAttributes().getNamedItem("width").getNodeValue();
															String imageHeight = nodeLiv4.getAttributes().getNamedItem("height").getNodeValue();
															String imageSize = nodeLiv4.getAttributes().getNamedItem("size").getNodeValue();
															String imageTime = nodeLiv4.getAttributes().getNamedItem("time").getNodeValue();
															String imageBitrate = nodeLiv4.getAttributes().getNamedItem("bitrate").getNodeValue();
															String imageText = nodeLiv4.getFirstChild().getTextContent();
															
															nitf.setImageDataLocation(imageDataLocation);
															nitf.setImageMimeType(imageMimeType);
															nitf.setImageWidth(imageWidth);
															nitf.setImageHeight(imageHeight);
															nitf.setImageSize(imageSize);
															nitf.setImageTime(imageTime);
															nitf.setImageBitrate(imageBitrate);
															nitf.setImageText(imageText);
														}
													}
												}	
											}

										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		return nitf;
	}
}
