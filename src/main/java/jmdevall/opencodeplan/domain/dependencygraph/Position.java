package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.Comparator;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Position {
	private int line;
	private int column;
	
	public static Position newPosition(int line, int column) {
		return Position.builder()
				.line(line)
				.column(column)
				.build();
	}

    public static Comparator<Position> comparator() {
        return (Position p1, Position p2) -> {
            if (p1.line == p2.line) {
                return Integer.compare(p1.column, p2.column);
            } else {
                return Integer.compare(p1.line, p2.line);
            }
        };
    }
    
    /*
	public int absolute(String texto) {
	    int indice = 0;
	    int lineaActual = 1;
	    int columnaActual = 1;

	    while (indice < texto.length() && (lineaActual < line || (lineaActual == line && columnaActual < column))) {
	        if (texto.charAt(indice) == '\n' ) {
	            lineaActual++;
	            columnaActual = 1;
	        } else {
	            columnaActual++;
	        }
	        indice++;
	    }

	    return indice;
	}
	*/
    /*
	public int absolute(String texto) {
	    int indice = 0;
	    int lineaActual = 1;
	    int columnaActual = 1;

	    while (indice < texto.length() && (lineaActual < line || (lineaActual == line && columnaActual < column))) {
	        if (texto.charAt(indice) == '\r' && indice + 1 < texto.length() && texto.charAt(indice + 1) == '\n') {
	            lineaActual++;
	            columnaActual = 1;
	            indice += 2;
	        } else if (texto.charAt(indice) == '\n') {
	            lineaActual++;
	            columnaActual = 1;
	            indice++;
	        } else {
	            columnaActual++;
	            indice++;
	        }
	    }

	    return indice;
	}
	*/
	
	
	public int absolute(String texto) {
	    int indice = 0;
	    int lineaActual = 1;
	    int columnaActual = 1;

	    while (indice < texto.length() && (lineaActual < line || (lineaActual == line && columnaActual < column))) {
	        if (texto.charAt(indice) == '\r') {
	            if (indice + 1 < texto.length() && texto.charAt(indice + 1) == '\n') {
	                lineaActual++;
	                columnaActual = 1;
	                indice += 2;
	            } else {
	                lineaActual++;
	                columnaActual = 1;
	                indice++;
	            }
	        } else if (texto.charAt(indice) == '\n') {
	            lineaActual++;
	            columnaActual = 1;
	            indice++;
	        } else {
	            columnaActual++;
	            indice++;
	        }
	    }

	    return indice;
	}

}
