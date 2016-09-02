package com.iai.uima.analysis_component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.iai.uima.jcas.tcas.OfficeHolderAnnotation;
import com.iai.utils.LevenshteinDistance;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import eu.eumssi.uima.ts.SourceMeta;

public class OfficeHolderAnnotator extends JCasAnnotator_ImplBase {
	
	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue = "en")
	private String language;
	
	public static final String PARAM_SIMILARITY_THRESHOLD = "similarityThreshold";
	@ConfigurationParameter(name = PARAM_SIMILARITY_THRESHOLD, defaultValue = "0.4f")
	private float similarityThreshold;
	
	public static final String PARAM_BIGRAM_THRESHOLD = "bigramThreshold";
	@ConfigurationParameter(name = PARAM_BIGRAM_THRESHOLD, defaultValue = "0.7f")
	private float bigramThreshold;
	
	public static final String PARAM_TERM_THRESHOLD = "termTreshold";
	@ConfigurationParameter(name = PARAM_TERM_THRESHOLD, defaultValue = "0.55f")
	private float termTreshold;
	
	public static final String PARAM_OFFCE_TO_HOLDER_FILE = "office2holderLocation";
	@ConfigurationParameter(name = PARAM_OFFCE_TO_HOLDER_FILE, defaultValue = "/maps/office_holders.json")
	private String officeHolderMapLocation;
	
	public static final String PARAM_OFFCE_HOLDERS_LAST_NAME_FILE = "office2holderLastNameLocation";
	@ConfigurationParameter(name = PARAM_OFFCE_HOLDERS_LAST_NAME_FILE, defaultValue = "/maps/office_holders_last_name.json")
	private String officeHoldersLastNameMapLocation;
	
	public static final String PARAM_OFFICE_HOLDER_TERM_TO_NAME_FILE = "office2Term2holderLocation";
	@ConfigurationParameter(name = PARAM_OFFICE_HOLDER_TERM_TO_NAME_FILE, defaultValue = "/maps/office_term.json")
	private String officeHolderTermToNameMapLocation;
	
	public static final String PARAM_BIGRAM_MODEL_FILE = "bigramModelFile";
	@ConfigurationParameter(name = PARAM_BIGRAM_MODEL_FILE, defaultValue = "/maps/w2_.tsv")
	private String bigramModelFile;

	JSONObject office2holder;
	JSONObject fullName2lastName;
	JSONObject office2term2holder;
	
	List<String[]> bigramModel;
	
	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {

		super.initialize(aContext);
		
	    File f_oh = new File(getClass().getResource(officeHolderMapLocation).getPath());
	    File f_ohln = new File(getClass().getResource(officeHoldersLastNameMapLocation).getPath());
	    File f_ot = new File(getClass().getResource(officeHolderTermToNameMapLocation).getPath());
	    File f_bm = new File(getClass().getResource(bigramModelFile).getPath());
	    
	    TsvParserSettings settings = new TsvParserSettings();
	    TsvParser parser = new TsvParser(settings);
	    
	    try {
			 office2holder = new JSONObject(
					 				new JSONTokener(
					 						new FileReader(f_oh)));
			 
		     fullName2lastName = new JSONObject(
		    		 				new JSONTokener(
		    		 						new FileReader(f_ohln)));
		     
		     office2term2holder= new JSONObject(
		 							new JSONTokener(
		 									new FileReader(f_ot)));
		     
		     bigramModel = parser.parseAll(new FileReader(f_bm));
		     
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	    
	}
	
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		String documentText = aJCas.getDocumentText();
		float maxSimilarity = 0.0f;
	    boolean nameAnnotation = false;
				
		for (String office : office2holder.keySet()){
		  //String office = office.replace('_', ' ');
			Matcher matcher = Pattern.compile(office,Pattern.CASE_INSENSITIVE).matcher(documentText);
			
			while (matcher.find()){
				
				OfficeHolderAnnotation annotation = new OfficeHolderAnnotation(aJCas);
				int begin = matcher.end()+1;
				int end;
				annotation.setBegin(begin);
				annotation.setSimilarity(0.0);
				
				JSONArray officeHolder = (JSONArray) office2holder.get(office);
				maxSimilarity = 0.0f;
			    nameAnnotation = false;
			    String maxA = "";
				for (Object o : officeHolder){
					String person = (String) o;
					
					end = begin+person.length();

					String a = documentText.substring(begin, end);
				   	
					int sub = 0;
					int add = 0;
					
					while (documentText.charAt(end+add)!=' '){
						add++;
					}
					while (documentText.charAt(end-sub)!=' '){
						sub++;
					}
					
					if (add >= sub){
						end = end-sub;
						a = documentText.substring(begin, end);
					}
					else {
						end = end+add;
						a = documentText.substring(begin, end);
					}
					
					String[] nameAsArray = a.split(" ");
				
					
					String s = "";
					for (String s_prime : nameAsArray){
						if (!(s.equals("<eps>") || s.equals("<unk>")))
							s+=s_prime +" ";
					}
					
					s=s.trim();
					int levinstein = LevenshteinDistance.computeLevenshteinDistance(s, person);
					float similarity = 1.0f - (float)levinstein / Math.max((float)person.length(), (float)s.length());
					
					if (levinstein == 0){
						annotation.setSimilarity(1);
						annotation.setMostSimilarPerson(person);
						annotation.setEnd(end);
						annotation.addToIndexes();
						break;
					}
					else if (similarity > maxSimilarity){
						maxSimilarity = similarity;
						annotation.setSimilarity(similarity);
						annotation.setMostSimilarPerson(person);
						annotation.setEnd(end);
						maxA = a;
								
					}
				}
				if (annotation.getSimilarity() > similarityThreshold){
//				if (annotation.getSimilarity() > 0.2f){
					
					boolean uncommonWord = true;
					String [] maxNameAsArray = maxA.split(" ");
					nameAnnotation = true;
//					if (annotation.getSimilarity() < bigramThreshold){
//					   uncommonWord = detectedUncommonWordSequence(maxNameAsArray);
//					
//					   if (	uncommonWord == false ){
// 				         nameAnnotation = false;
//					   }
//					}else 
						if (annotation.getSimilarity() < termTreshold){
						JSONObject jsonOffice = (JSONObject) office2term2holder.get(office);
						// XXX Find the publication date in the jCas
						SourceMeta meta = JCasUtil.selectSingle(aJCas, SourceMeta.class);
						
						if (meta.getDatePublished() != null) {
							DateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
							int year = 0;
							try {
								Date date = df.parse(meta.getDatePublished());
								Calendar cal = Calendar.getInstance();
							    cal.setTime(date);
							    year = cal.get(Calendar.YEAR);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							JSONArray holders;
							try {
								holders = (JSONArray)jsonOffice.get(Integer.valueOf(year).toString());
							} catch (JSONException e) {
								continue;
							}
							
							System.out.println(holders);
							for (int i=0;i<holders.length();i++) {
								if (holders.get(i).equals(annotation.getMostSimilarPerson())) {
									System.out.println(holders.get(i));
									nameAnnotation = true;
								}
							}
						}
						}
				}
				if (nameAnnotation == true){
					annotation.addToIndexes();
				}
			}
				
		}
	}
	
	private boolean detectedUncommonWordSequence(String[] wordSequnce){
		boolean found=false;
		if (wordSequnce.length >= 2){
			int i;
			for (i=0;i<wordSequnce.length-1;i++){
				found=false;
				for (String[] line : bigramModel){
					if (	wordSequnce[i].equals(line[1])
						&& 	wordSequnce[i+1].equals(line[2])){
						found = true;
						break;
					}
			}
			   if (found == false){
				   return true;
			   }
		    }
		}
		else {
			return true;
		}
		return false;
	}
	
	
}
