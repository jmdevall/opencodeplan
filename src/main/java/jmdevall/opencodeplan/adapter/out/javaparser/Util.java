package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import jmdevall.opencodeplan.domain.dependencygraph.NodeId;
import jmdevall.opencodeplan.domain.dependencygraph.LineColPos;
import jmdevall.opencodeplan.domain.dependencygraph.LineColRange;

public class Util {

	public static LineColPos toDomainPosition( com.github.javaparser.Position position) {
		return LineColPos.builder()
		.line(position.line)
		.column(position.column)
		.build();
	}
	
	public static NodeId toNodeId(com.github.javaparser.ast.Node node) {
		
		CompilationUnit compilationUnit = getCompilationUnit(node);
		String filename = getFileNameOfCompilationUnit(compilationUnit);

		LineColRange absoluteNode=LineColRange.builder()
				.begin(toDomainPosition(node.getBegin().get()))
				.end(toDomainPosition(node.getEnd().get()))
				.build();

		return NodeId.builder()
			.file(filename)
			.range(absoluteNode)
			.build();
	}
	
	//---------------------
	
	 public static CompilationUnit getCompilationUnit(Node node) {
		
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
			
//			Optional<com.github.javaparser.ast.Node> otronode=foo.containerType().get().toAst();
//			if(otronode.isPresent()) {
//				System.out.println("foo "+otronode.get());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
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
