package jmdevall.opencodeplan.domain;

public enum Change {

    //Modification Changes
    MMB("Body of method M",
        "MMB", 
        "Recompute the edges incident on the statements in the method body");
    
    
    private String atomicChange;
    private String label;
    private String dependencyGraphUpdate;

    Change(String atomicChange,String label, String dependencyGraphUpdate){
        this.atomicChange=atomicChange;
        this.label=label;
        this.dependencyGraphUpdate=dependencyGraphUpdate;
    }

}
