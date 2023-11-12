package jmdevall.opencodeplan.domain.plangraph;

import java.util.Arrays;
import java.util.List;

import jmdevall.opencodeplan.domain.dependencygraph.DependencyLabel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
BodyOfMethod,
SignatureOfMethod,
Field,
DeclarationOfClass,
SignatureOfConstructor,

Import //?
*/

//CMI="Change May Impact"

@Getter
public enum CMI {
	
	  MMB(ChangeType.MODIFICATION, NodeTypeTag.BodyOfMethod,
	     "Body of Method M"
		,"MMB"
		,"Recompute the edges incident on the statements in the method body."
		,"If an escaping object is modified then Rel(D,M,CalledBy) else Nill"
	    ,Arrays.asList(new CMIRelation(WhatD.D,DependencyLabel.CALLED_BY)))
	    
	, MMS(ChangeType.MODIFICATION, NodeTypeTag.SignatureOfMethod,
		"Signature of Method M"
		,"MMS"
		,"Recompute the edges incident on the method."
	    ,"Rel(D, M, CalledBy), Rel(D, M, Overrides), Rel(D, M, OverriddenBy), Rel(D′ , M, Overrides), Rel(D′ , M, OverriddenBy)"
	    ,Arrays.asList(
	    		new CMIRelation(WhatD.D,DependencyLabel.CALLED_BY)
	    		,new CMIRelation(WhatD.D,DependencyLabel.OVERRIDES)
	    		,new CMIRelation(WhatD.D,DependencyLabel.OVERRIDEN_BY)
	    		,new CMIRelation(WhatD.DP,DependencyLabel.OVERRIDES)
	    		,new CMIRelation(WhatD.DP,DependencyLabel.OVERRIDEN_BY)
		));
/*	    		
    , MF(ChangeType.MODIFICATION, NodeTypeTag.Field,
    	"Field F in class C"
    	,"MF"
    	,"Recompute the edges incident on the field."
    	,"Rel(D, F, UsedBy), Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
    , MC(ChangeType.MODIFICATION, NodeTypeTag.DeclarationOfClass,
    	 "Declaration of class C"
    	 ,"MC"
    	 ,"Recompute the edges incident on the class."
    	 ,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf), Rel(D′ , C, BaseClassOf), Rel(D′ , C, DerivedClassOf)")
    , MCC(ChangeType.MODIFICATION, NodeTypeTag.SignatureOfConstructor,
    	  "Signature of Constructor of Class C"
    	 ,"MCC"
    	 ,"No change."
    	 ,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
    , MI(ChangeType.MODIFICATION, NodeTypeTag.Import,
    		"Import/Using statement I"
    	,"MI"
    	,"Recompute the edges incident on the import statement."
    	,"Rel(D, I, ImportedBy)")
///--------------------------
	, AM(ChangeType.ADD, NodeTypeTag.SignatureOfMethod,
			"Methos M in class C"
		,"AM"
		,"Add new node and edges by analizing the method. If C.M overrides a base class method B.M then redirect the Calls/CalledBy edges from B.M to C.M if the receiver object is of type C."
		,"Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf), Rel(D′ , M, CalledBy)")
	, AF(ChangeType.ADD, NodeTypeTag.Field,
		"Field F in class c"
		,"AF"
		,"Add new node and edges by analyzing the field declaration."
		,"Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, AC(ChangeType.ADD, NodeTypeTag.DeclarationOfClass,
		"Declaration of class C"
		,"AC"
		,"Add new node and edges by analyzing the class declaration."
		,"Nil")
	, ACC(ChangeType.ADD, NodeTypeTag.SignatureOfConstructor,
		 "Constructor of class C"
		,"ACC"
		,"Add new node and edges by analyzing the constructor."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, AI(ChangeType.ADD, NodeTypeTag.Import,
		"Import/Using statement I"
		,"AI"
		,"Add new node and edges by analyzing the import statement."
		,"NIl")
//---------------------------
	
	, DM(ChangeType.DELETION, NodeTypeTag.SignatureOfMethod,
		"Method M in class C"
		,"DM"
		,"Remove the node for M and edges in cident on M. If C.M overrides a base class method B.M then redirect the Calls/CalledBy edges from C.M to B.M if the receiver object is of type C."
		,"Rel(D, M, CalledBy), Rel(D, M, Overrides), Rel(D, M, OverriddenBy)")
	, DF(ChangeType.DELETION, NodeTypeTag.Field,
		"Field F in class C"
		,"DF"
		,"Remove the node of the field and edges incident on it."
		,"Rel(D, F, UsedBy), Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, DC(ChangeType.DELETION, NodeTypeTag.DeclarationOfClass,
		"Declaration of class C"
		,"DC"
		,"Remove the node of the class and edges incident on it."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, DCC(ChangeType.DELETION, NodeTypeTag.SignatureOfConstructor,
		"Constructor of Class C"
		,"DCC"
		,"Remove the edges incident on the class due to object instatiations using the constructor."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)");
	*/
	private ChangeType changeType;
	private NodeTypeTag nodeTypeTag;
	
	private String atomicChange;
	private String label;
	private String desc;
	private String analisis;
	private List<CMIRelation> formalChangeMayImpact;
	
	CMI(ChangeType changeType, NodeTypeTag nodeTypeTag,
		String atomicChange,String label, String desc, String analisis
		,List<CMIRelation> formalChangeMayImpact){
		this.changeType=changeType;
		this.nodeTypeTag=nodeTypeTag;
		this.atomicChange=atomicChange;
		this.label=label;
		this.desc=desc;
		this.analisis=analisis;
		this.formalChangeMayImpact=formalChangeMayImpact;
	}
	
	private static enum WhatD{
		D, DP
	}
	
	private static class CMIRelation{
		private WhatD dgc; //¿old dependency graph or new?
		private DependencyLabel dependencyLabel;
		
		public CMIRelation(WhatD dgc, DependencyLabel dependencyLabel) {
			super();
			this.dgc = dgc;
			this.dependencyLabel = dependencyLabel;
		}
		
		
	}

}
