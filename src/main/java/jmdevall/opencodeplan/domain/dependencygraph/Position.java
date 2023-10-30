package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.Comparator;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
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
    
	public int absolute(String texto) {
	    int indice = 0;
	    int lineaActual = 1;
	    int columnaActual = 1;
	   

	    while (indice < texto.length() 
	    		&& (lineaActual < line || (lineaActual == line && columnaActual < column))) {
	        char charAt = texto.charAt(indice);

			if (charAt == '\n' ) {
	            lineaActual++;
	            columnaActual = 1;
	        } else {
	            columnaActual++;
	        }
	        indice++;
	    }

	    return indice;
	}
	
   
}
