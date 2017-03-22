package analysis;
//package analysis;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;
//
//import data.Params;
//import data.SList;
//import data.Trial;
//import gui.MainWindow;
//import ij.IJ;
//import ij.ImagePlus;
//import plugins.AVI_Reader;
//import utils.ImageProcessing;
//import utils.Output;
//import utils.TrackFilters;
//
//public class ChemotaxisAnalysis {
//	
//	private File directory;
//	
//	/**
//	 * Constructor
//	 */
//	public ChemotaxisAnalysis(){}
//	
//
//	public int selectDialog(){
//		Object[] options = {"RatioQ", "Bootstrapping"};
//		int n = JOptionPane.showOptionDialog(null,
//				"Which analysis do you want to apply to the data?",
//				"Choose one analysis",
//				JOptionPane.YES_NO_OPTION,
//				JOptionPane.QUESTION_MESSAGE,
//				null,     //do not use a custom Icon
//				options,  //the titles of buttons
//				options[0]); //default button title
//		return n;
//	}	
//	
//	/**
//	 * 
//	 * @return File[]
//	 */	
//	public File[] getFileNames(){
//		JFileChooser chooser = new JFileChooser();
//		chooser.setCurrentDirectory(new java.io.File("F:\\VIDEOS QUIMIOTAXIS\\Validación voluntarios\\"));
//		//chooser.setCurrentDirectory(new java.io.File("C:\\Users\\Carlos\\Documents\\Vet - Bioquimica\\1 - Zaragoza\\data"));
//		chooser.setDialogTitle("choosertitle");
//		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		chooser.setAcceptAllFileFilterUsed(false);
//		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//		  //System.out.println("Directory: "+chooser.getSelectedFile());
//		  File folder = chooser.getSelectedFile();
//		  File[] listOfFiles = folder.listFiles();
//		  directory = chooser.getSelectedFile();
//		  return listOfFiles;	
//		}
//		return null;
//	}
//	
//	/**
//	 * 
//	 * @param ImagePlus imp
//	 */
//	public SList analyze(ImagePlus imp,String filename){
//		
//		System.out.println("converToGrayScale...");
// 		ImageProcessing.convertToGrayscale(imp);
//		//************************************************************ 
//		// * Auto-Threshold Video
//		//************************************************************
// 		System.out.println("thresholdStack...");
//		ImageProcessing.thresholdStack(imp);
//		//************************************************************ 
//		// * Record particle positions for each frame in an ArrayList
//		//************************************************************
//		System.out.println("detectSpermatozoa...");
//		List[] theParticles = ImageProcessing.detectSpermatozoa(imp);
//		//************************************************************ 
//		// * Now assemble tracks out of the particle lists
//		// * Also record to which track a particle belongs in ArrayLists
//		//************************************************************
//		System.out.println("identifyTracks...");
//		SList theTracks = ImageProcessing.idenfityTracks(theParticles,imp.getStackSize());
//		//************************************************************ 
//		// * Filter the tracks list
//		// * (We have to filter the tracks list because not all of them are valid)
//		//************************************************************
//		System.out.println("filterTracks...");
//		theTracks = TrackFilters.filterTracks(theTracks);
//		//************************************************************
//		// * Average the tracks 
//		//************************************************************
//		System.out.println("averageTracks...");
//		SList avgTracks = TrackFilters.averageTracks(theTracks);	
//		//************************************************************ 
//		// * Calculate output
//		//************************************************************
//		// XY Coordinates
//		String xyCoordsOutput = ""; 
//		if(Params.printXY){
//			xyCoordsOutput = Output.printXYCoords(theTracks);
//			IJ.saveString(xyCoordsOutput,"");		
//		}
//		
//		switch(ChFunctions__.getTrialType(filename)){
//		
//			case 0://control
//					Params.controlTracks.addAll(theTracks);
//					System.out.println("theTracks size: "+theTracks.size()+"; controlTracks size: "+Params.controlTracks.size());
//				break;
//			case 1: //10pM
//					Params.conditionTracks.addAll(theTracks);
//					System.out.println("theTracks size: "+theTracks.size()+"; conditionTracks size: "+Params.conditionTracks.size());
//				break;
//			case 2: //100pM
//					Params.conditionTracks.addAll(theTracks);
//				break;
////			case 3:
////					Params.conditionTracks.addAll(theTracks);
////				break;
//		}		
//		
//		//To calculate Ratio-Q we have to decimate all tracks
////		System.out.println("decimateTracks...");
//		List decimatedTracks = TrackFilters.decimateTracks(avgTracks,Params.decimationFactor);
//		// Chemotaxis ratios
////		System.out.println("CalculateRatioQ...");
//		float ratioQ = ChFunctions__.calculateRatioQ(avgTracks);
////		System.out.println("calculateRatioSL...");
//		float ratioSL = ChFunctions__.calculateRatioSL(avgTracks);
////		System.out.println("setQtResults...");
//		ChFunctions__.setQtResults(filename,ratioQ,ratioSL,theTracks.size());
////		Params.rTable.show("Ratios Quimiotaxis");
//		//************************************************************ 
//		// * Draw tracks at each frame
//		//************************************************************
//		//ImageProcessing.draw(imp,theTracks,avgTracks,ratioQ,ratioSL);
//		System.out.println("Analysis finished for video: "+filename);
//		
//		
////		// Motility results
////		ResultsTable motResults = Output.calculateMotility(theTracks);
////		if(Params.calcMotilityParameters){
////			motResults.show("Motility Resume");
////		}
////		if(Params.calcMeanMotilityParameters){
////			ResultsTable avgMotility = Output.calculateAverageMotility(theTracks.size());
////			avgMotility.show("Motility Average");
////		}
//		
//		return theTracks;
//	}
//	
//	/**
//	 * 
//	 * @param String filename
//	 */
//	public boolean isAVI (String filename){
//		String[] parts = filename.split("\\.");
//		if(parts[1].equals("avi"))
//			return true;
//		else 
//			return false;
//	}
//	
//	/**
//	 * 
//	 * @param MainWindow mw
//	 * @throws IOException 
//	 * @throws ClassNotFoundException 
//	 */
//	public void run(MainWindow mw) throws IOException, ClassNotFoundException{
//		mw.setVisible(false);
//		Map<String, Trial> trials = new HashMap<String, Trial>();	
//		int n = selectDialog();
//		
//		if(n<0){
//			mw.setVisible(true);
//			return;			
//		}
//			
////		//////////////////////////////////////////////
////		File[] listOfFiles = getFileNames();
////		if(listOfFiles==null || listOfFiles.length==0){
////			mw.setVisible(true);
////			return;
////		}
////		for (int i = 0; i < listOfFiles.length; i++) {
////		    if (listOfFiles[i].isFile()) {
////		    	final String filename = listOfFiles[i].getName();
////				//System.out.println("File " + filename);
////				if(isAVI(filename)){
////					System.out.println("Loading video...");
////					
////					System.out.println("filename: "+filename);
////					int trialType = ChFunctions__.getTrialType(filename);
////					String trialID = ChFunctions__.getID(filename);
////					
////					switch(trialType){
////					case 0: //Control
////					case 1: //10pM
////					case 2: //100pM
//////						case 3: //10nM
////						
////						AVI_Reader ar = new  AVI_Reader();
////						ar.run(directory+"\\"+listOfFiles[i].getName());
////						final ImagePlus imp = ar.getImagePlus();
////						SList t = analyze(imp,filename);
////						
//////							if(t==null)
//////								IJ.log("analyze devuelve NULL");
////						
////						Trial tr;
////						if(trials.get(trialID)!=null){
////							tr = trials.get(trialID);
////							tr.ID = trialID;
////						}
////						else 
////							tr = new Trial();
////						switch(trialType){
////							case 0: tr.control=t;break;
////							case 1: tr.p10pM=t;break;
////							case 2: tr.p100pM=t; break;
////							case 3: tr.p10nM=t;break;
////						}
////						
////						int sampleSize = ChFunctions__.calculateSampleSize(t);
////						if((tr.minSampleSize==-1)||(sampleSize<tr.minSampleSize))
////							tr.minSampleSize = sampleSize;
////						trials.put(trialID, tr);
////
////						//new Thread(new Runnable() {public void run() {analyze(imp,filename);}}).start();							
////					}
////
////				}else{
////					System.out.println("The file format is not AVI. Not analyzed");
////				}
////
////		    } else if (listOfFiles[i].isDirectory()) {
////		    	System.out.println("Directory " + listOfFiles[i].getName());
////		    }
////	   }
////		////////////////////////////////////////////////////
////		// READING TRIALS FROM FILE
////		  try {
//////			  FileInputStream streamIn = new FileInputStream("F:\\VIDEOS QUIMIOTAXIS\\Validación voluntarios\\2017-02-24-V2\\TrialsGIVF.ser");
////			  FileInputStream streamIn = new FileInputStream("F:\\VIDEOS QUIMIOTAXIS\\Simulaciones\\Control\\Trials.ser");
//////			  FileInputStream streamIn = new FileInputStream("F:\\VIDEOS QUIMIOTAXIS\\Simulaciones\\Control\\Trials40Control.ser");
//////			  FileInputStream streamIn = new FileInputStream("C:\\Users\\Carlos\\Documents\\Vet - Bioquimica\\1 - Zaragoza\\data\\Simulation\\Trials40Control.ser");
//////			  FileInputStream streamIn = new FileInputStream("F:\\VIDEOS QUIMIOTAXIS\\Validación voluntarios\\2017-02-24-V2\\Trials.ser");
//////			  FileInputStream streamIn = new FileInputStream("F:\\VIDEOS QUIMIOTAXIS\\videos quimiotaxis\\Jorge-Patri\\Analisis quimiotaxis Jorge y Patri\\Trials_1microM.ser");
////			  ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
////			  trials = (HashMap<String, Trial>) objectinputstream.readObject();
////		  } catch (Exception e) {
////		      e.printStackTrace();
////		  } 
////			
////		  Set kSet = trials.keySet();
////		  for (Iterator k=kSet.iterator();k.hasNext();) {
////			  String key= (String)k.next();
////			  Trial trial = (Trial)trials.get(key);
////			  Params.controlTracks.addAll(trial.control);
////			  Params.conditionTracks.addAll(trial.p10pM);
////		  }
//		  
//		  //////////////////////////////////////////////////////////////////
////		  SERIALIZING TRIALS 
////           try
////           {
////        	  FileOutputStream fos =
//////                   new FileOutputStream("C:\\Users\\Carlos\\Documents\\Vet - Bioquimica\\1 - Zaragoza\\data\\Empty\\trials.ser");
//////                   new FileOutputStream("F:\\VIDEOS QUIMIOTAXIS\\Simulaciones\\C y Q\\10 por ciento 0_5 atraccion\\Trials.ser");
////        	  	   new FileOutputStream("F:\\VIDEOS QUIMIOTAXIS\\Simulaciones\\Control\\Trials.ser");
//////        	  	     new FileOutputStream("F:\\VIDEOS QUIMIOTAXIS\\Validacion Quiron\\20-12-2016\\todo\\1\\Trials.ser");
//////        	  		 new FileOutputStream("F:\\VIDEOS QUIMIOTAXIS\\Validación voluntarios\\2017-02-24-V2\\TrialsGIVF.ser");
//////        			 new FileOutputStream("F:\\VIDEOS QUIMIOTAXIS\\videos quimiotaxis\\Jorge-Patri\\Analisis quimiotaxis Jorge y Patri\\Trials_100pM.ser");
////                  
////			  ObjectOutputStream oos = new ObjectOutputStream(fos);
////			  oos.writeObject(trials);
////			  oos.close();
////			  fos.close();
////           }catch(IOException ioe)
////           {
////              ioe.printStackTrace();
////           }
//		//////////////////////////////////////////////////////
//           
//           //SETTING SAMPLE SIZE
//           ChFunctions__.minSampleSize(trials);
//           
//           // OR RATIOS AND CHEMOTAXIS ANALYSIS
//		  double thControl = ChFunctions__.calculateORControlThreshold(trials);
//		  Set keySet = trials.keySet();
//		  for (Iterator k=keySet.iterator();k.hasNext();) {
//			  String key= (String)k.next();
//			  Trial trial = (Trial)trials.get(key);
////			  System.out.println("key: "+key);
//			  double OR = ChFunctions__.OR(trial,"p10pM");
////			  IJ.log(OR+"");
//			  if(OR>(thControl))
//				  IJ.log("POSITIVO: OR["+OR+"] - thControl["+thControl+"] - ID: "+trial.ID);
//			  else
//			  	  IJ.log("NEGATIVO: OR["+OR+"] - thControl["+thControl+"] - ID: "+trial.ID);
//		  }
//          System.out.println("Finish");
//		
//		mw.setVisible(true);
//	}
//}