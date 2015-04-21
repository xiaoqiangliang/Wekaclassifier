package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.io.IOException;
 import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class InputStringTest {
	
	/*public static ArrayList<String> getStringSet(String s)throws IOException{
		         ArrayList<String> ali = new ArrayList<String>();
		        
		         StringReader reader = new StringReader(s);
		        IKSegmenter ik = new IKSegmenter(reader , true);
		         Lexeme lexeme = null;
		         while ((lexeme = ik.next()) != null) {
		             ali.add(lexeme.getLexemeText() + " ");
		         }
		         System.out.println("用户键盘的输入是："+ ali);
		         return ali;
		     }
	
	public static void main(String[] args) throws IOException {
		         String s = "头痛是一种病";
		        
		         ArrayList<String> aList = InputStringTest.getStringSet(s);
		         while(!aList.isEmpty()){
		             System.out.println(aList.get(0));
		            aList.remove(0);
		         }
		     }*/
	
	public static void main(String args[]){
		String targetFile = "mid.txt";
		String inputs = "电脑城我能从我那我一么吃完了么地产网破灭明晚才，么吴莫愁上档次，哦从莫文蔚";
		try {
		
			StringReader reader = new StringReader(inputs);
		    FileWriter fw = new FileWriter( targetFile );
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
		}


		}
		
	}
	





