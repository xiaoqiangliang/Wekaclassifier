package Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;
 
public class Segmenter {
    private String sourceDir;
    private String targetDir;
   
    Segmenter( String source, String target ) {
       sourceDir = source;
       targetDir = target;
    }
   
    public void segment() {
       segmentDir( sourceDir, targetDir );
    }
   
    public void segmentDir( String source, String target ) {
       File[] file = (new File( source )).listFiles();
       for (int i = 0; i < file.length; i++) {
           if (file[i].isFile()) {
              segmentFile(file[i].getAbsolutePath(), target + File.separator + file[i].getName());
           }
           if (file[i].isDirectory()) {
              String _sourceDir = source + File.separator + file[i].getName();
              String _targetDir = target + File.separator + file[i].getName();
              (new File(_targetDir)).mkdirs();
              segmentDir( _sourceDir, _targetDir );
           }
       }
    }
   
    public void segmentFile( String sourceFile, String targetFile ) {
       try {
//           FileReader fr = new FileReader( sourceFile );
           BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),"utf-8"));
   
           FileWriter fw = new FileWriter( targetFile );
           BufferedWriter bw = new BufferedWriter(fw );
           
           /*IKSegmenter ik = new IKSegmenter(br,true);
           Lexeme lex = null;
           while((lex=ik.next())!=null)
           {
        	   bw.write(lex.getLexemeText());
        	   bw.write(' ');
           }*/
          
           Analyzer analyzer = new IKAnalyzer(true); 
           TokenStream tokenStream = analyzer.tokenStream("", br );
           TermAttribute termAtt = (TermAttribute) tokenStream
                  .getAttribute(TermAttribute.class);
 
           while (tokenStream.incrementToken()) {
              bw.write( termAtt.term() );
              bw.write(' ');
           }
 
           bw.close();
           fw.close();
       } catch( IOException e ) {
           e.printStackTrace();
       }
    }
   
    public static void main(String[] args) throws Exception {
    	String src = "Training(3433)";
    	String tgt = "Taget3433";
    	System.out.println(System.getProperty("user.dir"));
    	Segmenter segmenter = new Segmenter(src, tgt);
    	segmenter.segment();
    }
}  