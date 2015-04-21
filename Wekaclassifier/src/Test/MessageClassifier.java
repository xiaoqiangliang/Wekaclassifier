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

public class MessageClassifier implements Serializable{
	
	
	private Instances m_Data = null;
	private StringToWordVector m_Filter = new StringToWordVector();
	private Classifier m_Classifier = new J48();
	private boolean m_UpToDate;
	
	
	public MessageClassifier() throws Exception {
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
		//System.out.println("����" + instance);
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
		System.out.println("����������" + testset);
		// Make message into test instance.
		Instance instance = makeInstance(message, testset);
		// Filter instance.
		m_Filter.input(instance);
		Instance filteredInstance = m_Filter.output();
		System.out.println("�������˺� " + filteredInstance);
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
		//System.out.println("����ǰ" + instance);
		return instance;
		}
	
	public static void TrainMessageClassifierFile (String source, String ModleName, String classvalue){
		
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
		
			MessageClassifier messageCl;
			try {
			ObjectInputStream modelInObjectFile =
			new ObjectInputStream(new FileInputStream(modelName));
			messageCl = (MessageClassifier) modelInObjectFile.readObject();
			modelInObjectFile.close();
			} catch (FileNotFoundException e) {
			messageCl = new MessageClassifier();
			}
			
			messageCl.updateData(message.toString(), classValue);
			// Save message classifier object.
			ObjectOutputStream modelOutObjectFile =
			new ObjectOutputStream(new FileOutputStream(modelName));
			modelOutObjectFile.writeObject(messageCl);
			modelOutObjectFile.close();
			System.out.println("����һ��ѵ���ı�");
			} 
		catch (Exception e) {
			e.printStackTrace();
			}
		
	}
	
	public static void main(String[] options) {
		try {
		// Read message file into string.
		String messageName = Utils.getOption('t', options);
		if (messageName.length() == 0) {
		throw new Exception("Must provide name of message file.");
		}
		FileReader m = new FileReader(messageName);
		StringBuffer message = new StringBuffer(); int l;
		while ((l = m.read()) != -1) {
		message.append((char)l);
		}
		m.close();
		// Check if class value is given.
		String classValue = Utils.getOption('c', options);
		// If model file exists, read it, otherwise create new one.
		String modelName = Utils.getOption('m', options);
		if (modelName.length() == 0) {
		throw new Exception("Must provide name of model file.");
		}
		MessageClassifier messageCl;
		try {
		ObjectInputStream modelInObjectFile =
		new ObjectInputStream(new FileInputStream(modelName));
		messageCl = (MessageClassifier) modelInObjectFile.readObject();
		modelInObjectFile.close();
		} catch (FileNotFoundException e) {
		messageCl = new MessageClassifier();
		}
		// Check if there are any options left
		Utils.checkForRemainingOptions(options);
		// Process message.
		if (classValue.length() != 0) {
		messageCl.updateData(message.toString(), classValue);
		} else {
		messageCl.classifyMessage(message.toString());
		}
		// Save message classifier object.
		ObjectOutputStream modelOutObjectFile =
		new ObjectOutputStream(new FileOutputStream(modelName));
		modelOutObjectFile.writeObject(messageCl);
		modelOutObjectFile.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
	}

