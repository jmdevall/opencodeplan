package jmdevall.opencodeplan.domain.plangraph;

import lombok.Getter;

@Getter
public enum CMI {
	
	MMB("Body of Method M"
		,"MMB"
		,"Recompute the edges incident on the statements in the method body."
		,"If an escaping object is modified then Rel(D,M,CalledBy) else Nill")
	, MMS("Signature of Mehotd M"
		,"MMS"
		,"Recompute the edges incident on the method."
	    ,"Rel(D, M, CalledBy), Rel(D, M, Overrides), Rel(D, M, OverriddenBy), Rel(D′ , M, Overrides), Rel(D′ , M, OverriddenBy)")
    , MF("Field F in class C"
    	,"MF"
    	,"Recompute the edges incident on the field."
    	,"Rel(D, F, UsedBy), Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
    , MC("Declaration of class C"
    	 ,"MC"
    	 ,"Recompute the edges incident on the class."
    	 ,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf), Rel(D′ , C, BaseClassOf), Rel(D′ , C, DerivedClassOf)")
    , MCC("Signature of Constructor of Class C"
    	 ,"MCC"
    	 ,"No change."
    	 ,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
    , MI("Import/Using statement I"
    	,"MI"
    	,"Recompute the edges incident on the import statement."
    	,"Rel(D, I, ImportedBy)"),
///--------------------------
	AM("Methos M in class C"
		,"AM"
		,"Add new node and edges by analizing the method. If C.M overrides a base class method B.M then redirect the Calls/CalledBy edges from B.M to C.M if the receiver object is of type C."
		,"Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf), Rel(D′ , M, CalledBy)")
	, AF("Field F in class c"
		,"AF"
		,"Add new node and edges by analyzing the field declaration."
		,"Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, AC("Declaration of class C"
		,"AC"
		,"Add new node and edges by analyzing the class declaration."
		,"Nil")
	, ACC("Constructor of class C"
		,"ACC"
		,"Add new node and edges by analyzing the constructor."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, AI("Import/Using statement I"
		,"AI"
		,"Add new node and edges by analyzing the import statement."
		,"NIl")
//---------------------------
	
	, DM("Method M in class C"
		,"DM"
		,"Remove the node for M and edges in cident on M. If C.M overrides a base class method B.M then redirect the Calls/CalledBy edges from C.M to B.M if the receiver object is of type C."
		,"Rel(D, M, CalledBy), Rel(D, M, Overrides), Rel(D, M, OverriddenBy)")
	, DF("Field F in class C"
		,"DF"
		,"Remove the node of the field and edges incident on it."
		,"Rel(D, F, UsedBy), Rel(D, C, ConstructedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, DC("Declaration of class C"
		,"DC"
		,"Remove the node of the class and edges incident on it."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)")
	, DCC("Constructor of Class C"
		,"DCC"
		,"Remove the edges incident on the class due to object instatiations using the constructor."
		,"Rel(D, C, InstantiatedBy), Rel(D, C, BaseClassOf), Rel(D, C, DerivedClassOf)");
	
	private String atomicChange;
	private String label;
	private String desc;
	private String analisis;
	
	CMI(String atomicChange,String label, String desc, String analisis){
		this.atomicChange=atomicChange;
		this.label=label;
		this.desc=desc;
		this.analisis=analisis;
	}

}
