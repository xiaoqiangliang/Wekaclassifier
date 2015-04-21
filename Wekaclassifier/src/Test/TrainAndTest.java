package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Scanner;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

//import Test.MessageClassifier;
//import test.MessageClassifier;
//import test.MessageClassifier;
//import test.MessageClassifier;
//import test.MessageClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class TrainAndTest implements Serializable{
	
	
	private Instances m_Data = null;
	private StringToWordVector m_Filter = new StringToWordVector();
	private Classifier m_Classifier = new NaiveBayesMultinomial();
	private boolean m_UpToDate;
	
	
	public TrainAndTest() throws Exception {
		String nameOfDataset = "MessageClassificationProblem";
		// Create vector of attributes.
		FastVector attributes = new FastVector(2);
		// Add attribute for holding messages.
		attributes.addElement(new Attribute("Message", (FastVector)null));
		// Add class attribute.
		FastVector classValues = new FastVector(10);
		/*classValues.addElement("C000008");
		classValues.addElement("C000010");
		classValues.addElement("C000013");
		classValues.addElement("C000014");
		classValues.addElement("C000016");
		classValues.addElement("C000020");
		classValues.addElement("C000022");
		classValues.addElement("C000023");
		classValues.addElement("C000024");*/
		
		
		classValues.addElement("IT");
		classValues.addElement("创业");
		classValues.addElement("电影");
		classValues.addElement("户外");
		classValues.addElement("讲座");
		classValues.addElement("聚会");
		classValues.addElement("亲子");
		classValues.addElement("体育");
		classValues.addElement("演出");
		classValues.addElement("展览");
		
		/*classValues.addElement("class1");
		classValues.addElement("class2");
		classValues.addElement("class3");
		classValues.addElement("class4");
		classValues.addElement("class5");
		classValues.addElement("class6");
		classValues.addElement("class7");
		classValues.addElement("class8");
		classValues.addElement("class9");
		classValues.addElement("class10");*/
		/*FastVector classValues = new FastVector(2);
		classValues.addElement("class1");
		classValues.addElement("class2");*/
		attributes.addElement(new Attribute("Class", classValues));
		// Create dataset with initial capacity of 100, and set index of class.
		m_Data = new Instances(nameOfDataset, attributes, 100);
		m_Data.setClassIndex(m_Data.numAttributes() - 1);
		}
		
	public void updateData(String message, String classValue) throws Exception {
		// Make message into instance.
		Instance instance = makeInstance(message, m_Data);
		// Set class value for instance.
		instance.setClassValue(classValue);
		// Add instance to training data.
		m_Data.add(instance);
		m_UpToDate = false;
		//System.out.println("输入" + instance);
		}
	
	
	public void classifyMessage(String message) throws Exception {
		// Check whether classifier has been built.
		if (m_Data.numInstances() == 0) {
		////throw new Exception("No classifier available.");
		}
		// Check whether classifier and filter are up to date.
		if (!m_UpToDate) {
		 // Initialize filter and tell it about the input format.
		m_Filter.setInputFormat(m_Data);
		// Generate word counts from the training data.
		Instances filteredData = Filter.useFilter(m_Data, m_Filter);
		// Rebuild classifier.
		m_Classifier.buildClassifier(filteredData);
		m_UpToDate = true;
		}
		// Make separate little test set so that message
		// does not get added to string attribute in m_Data.
		Instances testset = m_Data.stringFreeStructure();
		//System.out.println("分类器输入" + testset);
		// Make message into test instance.
		Instance instance = makeInstance(message, testset);
		// Filter instance.
		m_Filter.input(instance);
		Instance filteredInstance = m_Filter.output();
		//System.out.println("经过过滤后 " + filteredInstance);
		// Get index of predicted class value.
		double predicted = m_Classifier.classifyInstance(filteredInstance);
		// Output class value.
		System.err.println("Message classified as : " +m_Data.classAttribute().value((int)predicted));
		//System.out.println("Message classified as : " +m_Data.classAttribute().value((int)predicted));
		}
	
		
		
		
	
	
	private Instance makeInstance(String text, Instances data) {
		// Create instance of length two.
		Instance instance = new Instance(2);
		// Set value for message attribute
		Attribute messageAtt = data.attribute("Message");
		instance.setValue(messageAtt, messageAtt.addStringValue(text));
		// Give instance access to attribute information from the dataset.
		instance.setDataset(data);
		//System.out.println("过滤前" + instance);
		return instance;
		}
	
	public static void TrainTrainAndTestFile (String source, String ModleName, String classvalue)
	{
		
		try {
			
			String messageName = source;
			
			FileReader m = new FileReader(messageName);
			StringBuffer message = new StringBuffer(); int l;
			while ((l = m.read()) != -1) {
			message.append((char)l);
			}
			m.close();
			
			String classValue = classvalue;
			String modelName = ModleName;
		
			TrainAndTest messageCl;
			try {
			ObjectInputStream modelInObjectFile =
			new ObjectInputStream(new FileInputStream(modelName));
			messageCl = (TrainAndTest) modelInObjectFile.readObject();
			modelInObjectFile.close();
			} catch (FileNotFoundException e) {
			messageCl = new TrainAndTest();
			}
			
			messageCl.updateData(message.toString(), classValue);
			// Save message classifier object.
			ObjectOutputStream modelOutObjectFile =
			new ObjectOutputStream(new FileOutputStream(modelName));
			modelOutObjectFile.writeObject(messageCl);
			modelOutObjectFile.close();
			//System.out.println("输入一条训练文本");
			} 
		catch (Exception e) {
			e.printStackTrace();
			}
		
	}
	
	public static void main(String[] arg){
		
		Scanner sc = new Scanner(System.in);
		//System.out.println("输入模型文件路径");
		//String ModleFile = sc.nextLine();
		System.out.println("文本分类：1         训练：2");
		int k = sc.nextInt();
		
		//String option ="";
		String sourceFile = "Taget3433";
		String ModleFile = "ModletestBayes2.modle";
		//String modelName = Utils.getOption('m', options)
		String MidFile = "mid.txt";
		String messages = "mid.txt";//创业
		//String inputs = "开心麻花";

		
		
		switch (k)
		{
		case 1:
		/*	try {
				
				StringReader reader = new StringReader(inputs);
			    FileWriter fw = new FileWriter( MidFile );
			    BufferedWriter bw = new BufferedWriter(fw );
			    
			    IKSegmenter ik = new IKSegmenter(reader,true);
			    Lexeme lex = null;
			    while((lex=ik.next())!=null)
			    {
			 	   bw.write(lex.getLexemeText());
			 	   bw.write(' ');
			    }
			    bw.close();
			    fw.close();
			} catch( IOException e ) {
			    e.printStackTrace();
			}*/
			
		try {
			// Read message file into string.
			String messageName = messages;
			FileReader m = new FileReader(messageName);
			StringBuffer message = new StringBuffer(); int l;
			while ((l = m.read()) != -1) {
			message.append((char)l);
			}
			m.close();
			String modelName = ModleFile;
			
			TrainAndTest messageCl1;
			try {
			ObjectInputStream modelInObjectFile =
			new ObjectInputStream(new FileInputStream(modelName));
			messageCl1 = (TrainAndTest) modelInObjectFile.readObject();
			modelInObjectFile.close();
			} catch (FileNotFoundException e) {
			messageCl1 = new TrainAndTest();
			}
			long startTime1 = System.currentTimeMillis();
			System.out.println(message.toString());
			messageCl1.classifyMessage(message.toString());	
			long endTime1 = System.currentTimeMillis();
			System.out.println("分类时间： " + (endTime1 - startTime1) + "ms");
			ObjectOutputStream modelOutObjectFile =
			new ObjectOutputStream(new FileOutputStream(modelName));
			modelOutObjectFile.writeObject(messageCl1);
			modelOutObjectFile.close();
			} catch (Exception e) {
			e.printStackTrace();
			}	
		break;
		
	case 2:
		 {
			 File[] file = (new File( sourceFile )).listFiles();
	       for (int i = 0; i < file.length; i++){
	    	   String classes = file[i].getName();
	    	   String test1 = file[i].getAbsolutePath();

	    		   if (file[i].isFile()) 
	    		   	{
	    			   TrainTrainAndTestFile(file[i].getAbsolutePath(), ModleFile,classes );
	    			   //System.out.println("可分类"+ file[i].getAbsolutePath());
	    	         }
	    	           if (file[i].isDirectory()) 
	    	           {
	    	        	   System.out.println("文件地址"+file[i].getAbsolutePath());
	    	        	   String Dir = sourceFile + File.separator + file[i].getName();
	    	        	   System.out.println("Dir："+Dir);
	    	        	   File[] file2 = (new File( Dir )).listFiles();
	    	        	   for (int j = 0; j < file2.length; j++){
	    	              TrainTrainAndTestFile(file2[j].getAbsolutePath(),ModleFile,classes);
	    	              System.out.println(file2[j].getAbsolutePath());
	    	              System.out.println(classes);
	    	              }
	    	           }
	    	   }
	       
		 }
	       
	break;
	default:
		System.out.println(" ");
	}
	}}
	

