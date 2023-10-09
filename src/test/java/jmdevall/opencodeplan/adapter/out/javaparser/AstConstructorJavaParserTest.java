package jmdevall.opencodeplan.adapter.out.javaparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Node;
import lombok.Getter;

public class AstConstructorJavaParserTest {

	@Test
	public void nose() {
		AstConstructorJavaParser sut=new AstConstructorJavaParser();
		HashMap<String, Node> forest=sut.listClasses(new File("/home/vicuna/js/nemofinder/src/main/java"));
		
		/*
		for(String file:forest.keySet()) {
			forest.get(file).debugRecursive(0);
		}*/
		
		
		//--------------------
		
	}
	
	@Getter
	public class Rel {
	    Node origen;
	    Node destino;
	    Label tipo;

	    public Rel(Node origen, Node destino, Label tipo) {
	        this.origen = origen;
	        this.destino = destino;
	        this.tipo = tipo;
	    }

	    // Getters y setters omitidos por brevedad
	}
	
	
	/*
	  public List<Rel> findRels() {
	        List<RelMatcher> relaciones = new ArrayList<RelMatcher>();
	        encontrarRelaciones(root, relaciones);
	        return relaciones;
	    }

	    private void encontrarRelaciones(Node nodo, List<Rel> relaciones,RelMatcher rm, Label tipo) {
	        for (Node hijo : nodo.getChildren()) {
	            if (rm.match(nodo, hijo)) {
	                relaciones.add(new Rel(nodo, hijo, tipo));
	            }
	            encontrarRelaciones(hijo, relaciones,rm, tipo);
	        }
	    }
	    
	    private interface RelMatcher {
	    	boolean match(Node n1,Node n2);
	    }
	    
	    */

}
