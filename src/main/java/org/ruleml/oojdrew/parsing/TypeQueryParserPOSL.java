// OO jDREW - An Object Oriented extension of the Java Deductive Reasoning Engine for the Web
// Copyright (C) 2008 Ben Craig
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

package org.ruleml.oojdrew.parsing;

import java.io.IOException;
import java.util.ArrayList;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.ruleml.oojdrew.util.DefiniteClause;
import org.ruleml.oojdrew.util.LUBGLBStructure;
import org.ruleml.oojdrew.util.SubsumesStructure;
import org.ruleml.oojdrew.util.Term;
	
public class TypeQueryParserPOSL {

	private String queryContents;
	private boolean validPredicate;
	private String[] varnames;
	
	
	//defining the valid predicates for type querying
	public final static String LUB =  "lub";
	public final static String GLB =  "glb";
	public final static String SUBSUMES = "subsumes";
	public final static String SUBSUMESPLUS = "subsumesPlus";
	
	//predicate used for querying
	private String predicate = "";
	
	/**
	 * Constructor for a TypeQueryParserPOSL
	 * 
	 * @param contents - the query to be parsed
	 */
	public TypeQueryParserPOSL(String contents){
	
		queryContents = contents;
	
	}
	
	/**
	 * Access Method to get the predicate name
	 * 
	 * @return the predicate name
	 */
	public String getPredicate(){
		return predicate;
	}
	
	/**
	 * Access method to see if a predicate is valid or not
	 * 
	 * @return true if the predicate is valid false otherwise
	 */
	public boolean getValidPredicate(){
		return validPredicate;
	}
	
	/**
	 * This method will parse the Query and determine if the predicate is valid or not
	 * 	
	 * @return Term[] - the terms of the predicate that need to be further parsed
	 * @throws RuleMLTypeQueryException
	 * @throws ParseException
	 * @throws ParsingException
	 * @throws ValidityException
	 * @throws IOException
	 */
	public Term[] parseForPredicate()  throws Exception, POSLTypeQueryException, ParseException, ParsingException, ValidityException, IOException{
					
		//dc will contain the query
		DefiniteClause dc = null;
		//parsing the giving query
      	POSLParser pp = new POSLParser();
        dc = pp.parseQueryStringT(queryContents); 
    	//Determining if the Query is valid
       	//I.E no rules are allowed in a query
    	Term[] atoms = dc.atoms;
    	if(atoms.length != 1){
    		throw new POSLTypeQueryException("Rules cannot be used for type Queries");
       	}
    	//Determining what the predicate is
    	//Valid Predicates are GLB(Greater Lower Bound) LUB(Least Upper Bound)
    	//Subsumes(direct subclass of) subsumesPlus(transitive closure).
    	Term t1 = atoms[0];
    	predicate = t1.getSymbolString();

    	if(predicate.equalsIgnoreCase(LUB)  ||
  	 			predicate.equalsIgnoreCase(GLB) ||
  	 			predicate.equalsIgnoreCase(SUBSUMES) ||
  	 			predicate.equalsIgnoreCase(SUBSUMESPLUS)){
  	 	
  	 	   validPredicate = true;
  	 		
  	 	} else{
  	 		
  	 		validPredicate = false;
  	 		throw new POSLTypeQueryException("Only LUB, GLB, Subsumes, and SubsumesPlus are valid predcates");
  	 	}
    	
    	Term[] terms = t1.getSubTerms();
    	varnames = dc.variableNames;
  	 	return terms;
 	 	
	}
	
	/**
	 * This method will determine the structure of the subsumes Query
	 * 
	 * @param terms[] - the terms that need to be parsed into a subsumes structure
	 * @return subsumesStructure - structure used to query a subsumes or subsumesPlus predicate
	 * @throws RuleMLTypeQueryException
	 */
	public SubsumesStructure parseElementsSubsumesAndSubsumesPlus(Term[] terms) throws POSLTypeQueryException{
		
		if(terms.length != 3){
			throw new POSLTypeQueryException("Subsumes and Subsumes plus must have 2 arguments");
		}
		
		String superName = terms[1].toPOSLString(varnames,true);
		String subName = terms[2].toPOSLString(varnames,true);
		boolean superVar = false;
		boolean subVar = false;
		
		if(superName.length() == 1 || subName.length() == 1){
			throw new POSLTypeQueryException("Anonymous are not allowed");
		}
	
		
		String term1Sub = superName.substring(0,1);
    	String term2Sub = subName.substring(0,1);
    	    
    	if(term1Sub.equals("?"))
    	   	superVar = true;
    	if(term2Sub.equals("?"))
    	   	subVar = true;
		
    	String superVarName = superName;
    	String subVarName = subName;
    	
    	if(superVar){
    		superVarName = superName.substring(1,superName.length()-2);
    	}
    	   	   	   	
    	if(subVar){
    		subVarName = subName.substring(1,subName.length()-2);
    	 }
    	
		if(superVar && subVar && superName.equalsIgnoreCase(subName)){
			throw new POSLTypeQueryException("Cannot have duplicate variable names in subsumes or subsumesPlus");
		}
    	
		return new SubsumesStructure(superVarName, subVarName,  superVar, subVar);
	}
	
	/**
	 * This method will determine the structure of the LUB or GLB Query
	 * 
	 * @param terms - the terms that need to be parsed into a LUB or GLB structure
	 * @return LUBGLBStructure - structure used to query a LUB or GLB predicate
	 * @throws RuleMLTypeQueryException
	 */
	public LUBGLBStructure parseElementsGLBandLUB(Term[] terms) throws POSLTypeQueryException{

		ArrayList<String> classes = new ArrayList<String>();
		boolean resultVarUsed = false;

		String resultVarName  = terms[1].toPOSLString(varnames,true);
        String term1Sub = resultVarName.substring(0,1);

        if(term1Sub.equals("?"))
        	resultVarUsed = true;

        if(resultVarUsed)
        	resultVarName = resultVarName.substring(1,resultVarName.length()-2);


        if(resultVarUsed){
			
			for(int i = 2; i < terms.length;i++)
				classes.add(terms[i].toPOSLString(varnames,true));
			
		}else {
			
			for(int i = 1; i < terms.length;i++)
				classes.add(terms[i].toPOSLString(varnames,true));
		}
		
		return new LUBGLBStructure(classes, resultVarUsed, resultVarName);
	}		
}