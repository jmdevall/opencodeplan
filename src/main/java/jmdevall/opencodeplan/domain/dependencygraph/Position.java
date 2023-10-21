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
}
