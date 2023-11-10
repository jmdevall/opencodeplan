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
public class LineColPos {
	private int line;
	private int column;
	
	public static LineColPos newPosition(int line, int column) {
		return LineColPos.builder()
				.line(line)
				.column(column)
				.build();
	}

    public static Comparator<LineColPos> comparator() {
        return (LineColPos p1, LineColPos p2) -> {
            if (p1.line == p2.line) {
                return Integer.compare(p1.column, p2.column);
            } else {
                return Integer.compare(p1.line, p2.line);
            }
        };
    }
    
	public int absolute(String text) {
		if(text==null) {
			throw new IllegalArgumentException();
		}
		
	    int indice = 0;
	    int lineaActual = 1;
	    int columnaActual = 1;
	   

	    while (indice < text.length() 
	    		&& (lineaActual < line || (lineaActual == line && columnaActual < column))) {
	        char charAt = text.charAt(indice);

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
