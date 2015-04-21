package Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//import Test.MessageClassifier;
//import test.MessageClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Trainning implements Serializable{
	
	
	private Instances m_Data = null;
	private StringToWordVector m_Filter = new StringToWordVector();
	private Classifier m_Classifier = new J48();
	private boolean m_UpToDate;
	
	
	public Trainning() throws Exception {
		String nameOfDataset = "MessageClassificationProblem";
		// Create vector of attributes.
		FastVector attributes = new FastVector(2);
		// Add attribute for holding messages.
		attributes.addElement(new Attribute("Message", (FastVector)null));
		// Add class attribute.
		FastVector classValues = new FastVector(10);
		classValues.addElement("class1");
		classValues.addElement("class2");
		classValues.addElement("class3");
		classValues.addElement("class4");
		classValues.addElement("class5");
		classValues.addElement("class6");
		classValues.addElement("class7");
		classValues.addElement("class8");
		classValues.addElement("class9");
		classValues.addElement("class10");
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
		System.out.println("分类器输入" + testset);
		// Make message into test instance.
		Instance instance = makeInstance(message, testset);
		// Filter instance.
		m_Filter.input(instance);
		Instance filteredInstance = m_Filter.output();
		System.out.println("经过过滤后 " + filteredInstance);
		// Get index of predicted class value.
		double predicted = m_Classifier.classifyInstance(filteredInstance);
		// Output class value.
		System.err.println("Message classified as : " +
		m_Data.classAttribute().value((int)predicted));
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
	
	public static void TrainTrainningFile (String source, String ModleName, String classvalue){
		
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
		
			Trainning messageCl;
			try {
			ObjectInputStream modelInObjectFile =
			new ObjectInputStream(new FileInputStream(modelName));
			messageCl = (Trainning) modelInObjectFile.readObject();
			modelInObjectFile.close();
			} catch (FileNotFoundException e) {
			messageCl = new Trainning();
			}
			
			messageCl.updateData(message.toString(), classValue);
			// Save message classifier object.
			ObjectOutputStream modelOutObjectFile =
			new ObjectOutputStream(new FileOutputStream(modelName));
			modelOutObjectFile.writeObject(messageCl);
			modelOutObjectFile.close();
			System.out.println("输入一条训练文本");
			} 
		catch (Exception e) {
			e.printStackTrace();
			}
		
	}
	
	public static void main(String[] arg){
		
		//String option ="";
		String sourceFile = "Taget";
		String ModleFile = "Modletest5.modle";
		//String modelName = Utils.getOption('m', options);
		
		 File[] file = (new File( sourceFile )).listFiles();
	       for (int i = 0; i < file.length; i++){
	    	   String classes = file[i].getName();
	    	   String test1 = file[i].getAbsolutePath();
	    	   //String Name = sourceFile + File.separator + file[i].getName();
	    	   //System.out.println(classes);
	    	   
	    		   //TrainTrainningFile(Name,ModleFile,classes);
	    		   //System.out.println("process1");

	    		   if (file[i].isFile()) {
	    			   TrainTrainningFile(file[i].getAbsolutePath(), ModleFile,classes );
	    			   System.out.println("可分类"+ file[i].getAbsolutePath());
	    	           }
	    	           if (file[i].isDirectory()) {
	    	        	   System.out.println("文件地址"+file[i].getAbsolutePath());
	    	        	   String Dir = sourceFile + File.separator + file[i].getName();
	    	        	   System.out.println("Dir："+Dir);
	    	        	   File[] file2 = (new File( Dir )).listFiles();
	    	        	   for (int j = 0; j < file2.length; j++){
	    	        		   
	    	              //String Dir = sourceFile + File.separator + file[i].getName();
	    	              //String _targetDir = target + File.separator + file[i].getName();
	    	              //(new File(_targetDir)).mkdirs();
	    	              TrainTrainningFile(file2[j].getAbsolutePath(),ModleFile,classes);
	    	              System.out.println(file2[j].getAbsolutePath());
	    	              System.out.println(classes);
	    	              }
	    	           }
	    	   }
	       }
	}

