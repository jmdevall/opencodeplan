package jmdevall.opencodeplan.adapter.out.javaparser;

import jmdevall.opencodeplan.application.port.out.parser.Parser;
import jmdevall.opencodeplan.application.port.out.repository.CuSource;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceFactory;
import jmdevall.opencodeplan.application.port.out.repository.cusource.CuSourceFixBug;
import jmdevall.opencodeplan.domain.dependencygraph.Node;

public class ParserJavaParser implements Parser{

	@Override
	public Node parse(String code) {
		CuSource cuSource=new CuSourceFixBug(CuSourceFactory.newFromSingleFile("",code));
		
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(new CuSourceFixBug(cuSource));
		
		CuSourceProcessor.process(cuSource, acjp, JavaParserFactory.newDefaultJavaParserWithoutTypeSolver());
		
		String cuname=acjp.getForest().keySet().stream().findFirst().orElseThrow();
		
		return acjp.getForest().get(cuname);
	}

}
