package jmdevall.opencodeplan.domain;

import java.util.ArrayList;
import java.util.List;

//TODO: no puedo pasar el bloque porque aun no está parseado y no hay forma de seleccionarlo. Lo mejor sería pasar una posición
//y buscar el nodo por posición
public class DeltaSeeds {
	private List<BI> bis=new ArrayList<BI>();
	
    public boolean isEmpty() {
    	return bis.isEmpty();
    }

    public List<BI> getBIs() {
    	return bis;
    }

}