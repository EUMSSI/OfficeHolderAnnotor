package com.iai.uima.analysis_component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.iai.uima.jcas.tcas.OfficeHolderAnnotation;
import com.iai.utils.LevenshteinDistance;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class OfficeHolderAnnotator extends JCasAnnotator_ImplBase {
	
	public static final String PARAM_LANGUAGE = "language";
	@ConfigurationParameter(name = PARAM_LANGUAGE, defaultValue = "en")
	private String language;
	
	public static final String PARAM_SIMILARITY_THRESHOLD = "similarityThreshold";
	@ConfigurationParameter(name = PARAM_SIMILARITY_THRESHOLD, defaultValue = "0.5f")
	private float similarityThreshold = 0.4f;
	
	public static final String PARAM_BIGRAM_THRESHOLD = "similarityThreshold";
	@ConfigurationParameter(name = PARAM_BIGRAM_THRESHOLD, defaultValue = "0.7f")
	private float bigramThreshold = 0.7f;
	
	public static final String PARAM_TERM_THRESHOLD = "similarityThreshold";
	@ConfigurationParameter(name = PARAM_TERM_THRESHOLD, defaultValue = "0.25f")
	private float termTreshold = 0.25f;
	
	public static final String PARAM_OFFCE_TO_HOLDER_MAP_LOCATION = "office2holderLocation";
	@ConfigurationParameter(name = PARAM_OFFCE_TO_HOLDER_MAP_LOCATION, defaultValue = "/maps/office_holders.json")
	private String officeHolderMapLocation;
	
	public static final String PARAM_OFFCE_HOLDERS_LAST_NAME_MAP_LOCATION = "office2holderLastNameLocation";
	@ConfigurationParameter(name = PARAM_OFFCE_HOLDERS_LAST_NAME_MAP_LOCATION, defaultValue = "/maps/office_holders_last_name.json")
	private String officeHoldersLastNameMapLocation;
	
	public static final String PARAM_OFFICE_HOLDER_TERM_TO_NAME_MAP_LOCATION = "office2Term2holderLocation";
	@ConfigurationParameter(name = PARAM_OFFCE_HOLDERS_LAST_NAME_MAP_LOCATION, defaultValue = "/maps/office_term.json")
	private String officeHolderTermToNameMapLocation;
	
	JSONObject office2holder;
	JSONObject fullName2lastName;
	JSONObject office2term2holder;
	
	List<String[]> bigramModel;
	
	public void initialize(UimaContext context) throws ResourceInitializationException {
	    System.out.println(getClass().getResource(officeHolderMapLocation).getPath());
	    
	    File f_oh = new File(getClass().getResource(officeHolderMapLocation).getPath());
	    File f_ohln = new File(getClass().getResource(officeHoldersLastNameMapLocation).getPath());
	    File f_ot = new File(getClass().getResource(officeHolderTermToNameMapLocation).getPath());
	    
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
		     bigramModel = parser.parseAll(new FileReader("input/maps/w2_.tsv"));
		     
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	    
	}
	
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		String documentText = aJCas.getDocumentText();
		
		for (String office : office2holder.keySet()){
			String _office = office.replace('_', ' ');
			Matcher matcher = Pattern.compile(_office).matcher(documentText);
			
			while (matcher.find()){
				
				OfficeHolderAnnotation annotation = new OfficeHolderAnnotation(aJCas);
				int begin = matcher.end()+1;
				int end;
				annotation.setBegin(begin);
				annotation.setSimilarity(0.0);
				
				JSONArray officeHolder = (JSONArray) office2holder.get(office);
				
				for (Object o : officeHolder){
					String person = (String) o;
					
					end = begin+person.length();

					String a = documentText.substring(begin, end);
					
					int sub = 0;
					int add = 0;
					if (person.equals("john f kennedy"))
						System.out.println(a);
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
					boolean detectedName = true;
					if (similarity <= bigramThreshold)
						detectedName = detectedUncommonWordSequence(nameAsArray);

					if (	detectedName
						&&	similarity >= similarityThreshold
						&&	annotation.getSimilarity() < similarity ){
						annotation.setSimilarity(similarity);
						annotation.setMostSimilarPerson(person);
						annotation.setEnd(end);
					}
					else if (similarity <= termTreshold){
//						JSONObject jsonOffice = (JSONObject) office2term2holder.get(_office);
//						// XXX Find the publication date in the jCas
//						String theOne = ((JSONArray) jsonOffice.get("2000")).getString(0);
//						annotation.setSimilarity(similarity);
//						annotation.setMostSimilarPerson(person);
//						annotation.setEnd(matcher.end()+theOne.length()+1);
					}
				}
				if (annotation.getSimilarity() >= similarityThreshold){
					annotation.addToIndexes();
				}
			}
				
		}
	}
	
	private boolean detectedUncommonWordSequence(String[] wordSequnce){
		if (wordSequnce.length >= 2){
			int i;
			for (String[] line : bigramModel)
				for (i=0;i<wordSequnce.length-1;i++)
					if (	wordSequnce[i].equals(line[1])
						&& 	wordSequnce[i+1].equals(line[2]))
						return false;
		}
		return true;
	}
	
	
}
