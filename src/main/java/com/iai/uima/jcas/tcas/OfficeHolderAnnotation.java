

/* First created by JCasGen Wed Oct 07 14:24:29 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Oct 28 16:27:48 CET 2015
 * XML source: D:/merlin/GitHub/OfficeHolderAnnotor/src/main/resources/desc/SpokenNER.xml
 * @generated */
public class OfficeHolderAnnotation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(OfficeHolderAnnotation.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected OfficeHolderAnnotation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public OfficeHolderAnnotation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public OfficeHolderAnnotation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public OfficeHolderAnnotation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: similarity

  /** getter for similarity - gets 
   * @generated
   * @return value of the feature 
   */
  public double getSimilarity() {
    if (OfficeHolderAnnotation_Type.featOkTst && ((OfficeHolderAnnotation_Type)jcasType).casFeat_similarity == null)
      jcasType.jcas.throwFeatMissing("similarity", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((OfficeHolderAnnotation_Type)jcasType).casFeatCode_similarity);}
    
  /** setter for similarity - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSimilarity(double v) {
    if (OfficeHolderAnnotation_Type.featOkTst && ((OfficeHolderAnnotation_Type)jcasType).casFeat_similarity == null)
      jcasType.jcas.throwFeatMissing("similarity", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((OfficeHolderAnnotation_Type)jcasType).casFeatCode_similarity, v);}    
   
    
  //*--------------*
  //* Feature: mostSimilarPerson

  /** getter for mostSimilarPerson - gets 
   * @generated
   * @return value of the feature 
   */
  public String getMostSimilarPerson() {
    if (OfficeHolderAnnotation_Type.featOkTst && ((OfficeHolderAnnotation_Type)jcasType).casFeat_mostSimilarPerson == null)
      jcasType.jcas.throwFeatMissing("mostSimilarPerson", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((OfficeHolderAnnotation_Type)jcasType).casFeatCode_mostSimilarPerson);}
    
  /** setter for mostSimilarPerson - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMostSimilarPerson(String v) {
    if (OfficeHolderAnnotation_Type.featOkTst && ((OfficeHolderAnnotation_Type)jcasType).casFeat_mostSimilarPerson == null)
      jcasType.jcas.throwFeatMissing("mostSimilarPerson", "com.iai.uima.jcas.tcas.OfficeHolderAnnotation");
    jcasType.ll_cas.ll_setStringValue(addr, ((OfficeHolderAnnotation_Type)jcasType).casFeatCode_mostSimilarPerson, v);}    
  }

    