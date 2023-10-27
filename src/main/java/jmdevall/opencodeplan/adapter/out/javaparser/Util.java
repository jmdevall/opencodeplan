package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.Position;
import jmdevall.opencodeplan.domain.dependencygraph.Range;

public class Util {

	public static Position toDomainPosition( com.github.javaparser.Position position) {
		return Position.builder()
		.line(position.line)
		.column(position.column)
		.build();
	}
	
	public static NodeId toNodeId(com.github.javaparser.ast.Node node) {
		
		CompilationUnit compilationUnit = getCompilationUnit(node);
		String filename = getFileNameOfCompilationUnit(compilationUnit);

		Range absoluteNode=Range.builder()
				.begin(toDomainPosition(node.getBegin().get()))
				.end(toDomainPosition(node.getEnd().get()))
				.build();

		/*
		Range relative;
		if(node.hasParentNode()) {
			Range absoluteParent=Range.builder()
					.begin(toDomainPosition(node.getParentNode().get().getBegin().get()))
					.end(toDomainPosition(node.getParentNode().get().getEnd().get()))
					.build();
		   
			relative=absoluteParent.minus(absoluteNode);
		}
		else {
		   
			relative=absoluteNode.minus(absoluteNode);
		}
		*/
		return NodeId.builder()
			.file(filename)
			.range(absoluteNode)
			//.relative(relative)
			.build();
	}
	
	//---------------------
	
	 public static CompilationUnit getCompilationUnit(Node node) {
		//return node.findAncestor(CompilationUnit.class).orElse(null);
		
        Node currentNode = node;
        while (!(currentNode instanceof CompilationUnit)) {
            currentNode = currentNode.getParentNode().orElse(null);
        }
        return (CompilationUnit) currentNode;
	}
	
	


	public static String getPackageDeclarationOrEmptyString(CompilationUnit compilationUnit) {
		Optional<PackageDeclaration> packageDeclaration=compilationUnit.getPackageDeclaration();
		
		if (packageDeclaration.isPresent()) {
		    return packageDeclaration.get().getNameAsString();
		}
		else {
			return "";
		}
	}
	

	public static void getTypeDeclaration(com.github.javaparser.ast.Node node) {
		SymbolResolver symbolResolver = node.getSymbolResolver();
	
		try {
			ResolvedReferenceTypeDeclaration foo;
			foo = symbolResolver.toTypeDeclaration(node);
			//System.out.println("foo "+foo);
			
			Optional<com.github.javaparser.ast.Node> otronode=foo.containerType().get().toAst();
			if(otronode.isPresent()) {
				System.out.println("foo "+otronode.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}

	public static JavaParser newDefaultJavaParser(File rootDir) {
		TypeSolver typeSolver = new CombinedTypeSolver(
	            new ReflectionTypeSolver(),
	            //new JarTypeSolver("jars/library3.jar"),
	            new JavaParserTypeSolver(rootDir)
	            //new JavaParserTypeSolver(new File("generated_code"))
	    );
	 	JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver); 
	
		ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(symbolSolver);
		JavaParser parser=new JavaParser(parserConfiguration);
		return parser;
	}

	public static String getPublicTypeOfCompilationUnit(CompilationUnit cu) {
		List<TypeDeclaration<?>> types = cu.getTypes();

		for (TypeDeclaration<?> type : types) {
		    if (type.isPublic()) {
		        return type.getNameAsString();
		    }
		}
		return "";
	}

	public static String getFileNameOfCompilationUnit(CompilationUnit compilationUnit) {
		
		String packageDeclaration = getPackageDeclarationOrEmptyString(compilationUnit);
		String publicType = getPublicTypeOfCompilationUnit(compilationUnit);


		if(packageDeclaration.isEmpty()) {
			return String.format(".%s",publicType).replace(".", File.separator) + ".java";
		}
		return String.format(".%s.%s",packageDeclaration,publicType).replace(".", File.separator) + ".java";
		
	}

}
