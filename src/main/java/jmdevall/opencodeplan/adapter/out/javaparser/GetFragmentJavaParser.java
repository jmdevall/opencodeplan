package jmdevall.opencodeplan.adapter.out.javaparser;


import java.util.Optional;

import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSource;
import jmdevall.opencodeplan.adapter.out.javaparser.cusource.CuSourceSingleFile;
import jmdevall.opencodeplan.application.port.out.parser.GetFragment;
import jmdevall.opencodeplan.domain.Fragment;

public class GetFragmentJavaParser implements GetFragment{

	@Override
	public Fragment getFragment(String code) {
		CuSource cuSource=new CuSourceSingleFile("", code);
		AstConstructorJavaParser acjp=new AstConstructorJavaParser(cuSource);
		
		CuSourceProcessor.process(cuSource, acjp, JavaParserFactory.newDefaultJavaParserWithoutTypeSolver());
		
		String cuname=acjp.getForest().keySet().stream().findFirst().orElseThrow();
		
		
		return Fragment.newFromCuNode(acjp.getForest().get(cuname));
	}

}
