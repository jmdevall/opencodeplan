package jmdevall.opencodeplan.adapter.out.javaparser;

import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public class ParserJavaParser implements Parser{

	@Override
	public Node parse(String code) {
		CuSource cuSource=new CuSourceSingleFile("", code);
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(cuSource);
		
		CuSourceProcessor.process(cuSource, acjp, JavaParserFactory.newDefaultJavaParserWithoutTypeSolver());
		
		String cuname=acjp.getForest().keySet().stream().findFirst().orElseThrow();
		
		return acjp.getForest().get(cuname);
	}

}
