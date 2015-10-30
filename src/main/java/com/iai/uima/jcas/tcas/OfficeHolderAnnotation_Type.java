
/* First created by JCasGen Wed Oct 07 14:24:29 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Oct 28 16:27:48 CET 2015
 * @generated */
public class OfficeHolderAnnotation_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OfficeHolderAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OfficeHolderAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OfficeHolderAnnotation(addr, OfficeHolderAnnotation_Type.this);
  			   OfficeHolderAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OfficeHolderAnnotation(addr, OfficeHolderAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = OfficeHolderAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
 
  /** @generated */
  final Feature casFeat_similarity;
  /** @generated */
  final int     casFeatCode_similarity;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public double getSimilarity(int addr) {
        if (featOkTst && casFeat_similarity == null)
      jcas.throwFeatMissing("similarity", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_similarity);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSimilarity(int addr, double v) {
        if (featOkTst && casFeat_similarity == null)
      jcas.throwFeatMissing("similarity", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_similarity, v);}
    
  
 
  /** @generated */
  final Feature casFeat_mostSimilarPerson;
  /** @generated */
  final int     casFeatCode_mostSimilarPerson;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getMostSimilarPerson(int addr) {
        if (featOkTst && casFeat_mostSimilarPerson == null)
      jcas.throwFeatMissing("mostSimilarPerson", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_mostSimilarPerson);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMostSimilarPerson(int addr, String v) {
        if (featOkTst && casFeat_mostSimilarPerson == null)
      jcas.throwFeatMissing("mostSimilarPerson", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_mostSimilarPerson, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public OfficeHolderAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_similarity = jcas.getRequiredFeatureDE(casType, "similarity", "uima.cas.Double", featOkTst);
    casFeatCode_similarity  = (null == casFeat_similarity) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_similarity).getCode();

 
    casFeat_mostSimilarPerson = jcas.getRequiredFeatureDE(casType, "mostSimilarPerson", "uima.cas.String", featOkTst);
    casFeatCode_mostSimilarPerson  = (null == casFeat_mostSimilarPerson) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_mostSimilarPerson).getCode();

  }
}



    