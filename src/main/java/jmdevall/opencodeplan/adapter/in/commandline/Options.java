package jmdevall.opencodeplan.adapter.in.commandline;

import lombok.Data;

@Data
public class Options {
	private String file;
	private int line;
	private int column;
	
	private String instruction;
}
