package jmdevall.opencodeplan.adapter.out.javaparser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.SymbolResolver;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import javassist.compiler.ast.MethodDecl;
import jmdevall.opencodeplan.adapter.out.file.FileUtil;
import jmdevall.opencodeplan.adapter.out.file.direxplorer.DirExplorer;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.Node;
import jmdevall.opencodeplan.domain.NodeId;
import jmdevall.opencodeplan.domain.Position;
import jmdevall.opencodeplan.domain.Rel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AstConstructorJavaParser {
    
    public HashMap<String,Node> listClasses(File rootDir) {
    	HashMap<String,Node> forest=new HashMap<String,Node>();
    	
    	
    	
   	 	TypeSolver typeSolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                //new JarTypeSolver("jars/library3.jar"),
                new JavaParserTypeSolver(rootDir)
                //new JavaParserTypeSolver(new File("generated_code"))
        );
   	 	JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver); 
   	 
    	ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(symbolSolver);
    	JavaParser parser=new JavaParser(parserConfiguration);
    	
    	List<Rel> rels=new ArrayList<Rel>();
        new DirExplorer(
            (level, path, file) -> path.endsWith(".java"),
            
            (level, subpathFromRoot, file) -> {
               String contenido= FileUtil.readFile(file.getAbsolutePath());
               
               ParseResult<CompilationUnit> compilationUnit = parser.parse(contenido);
               if(compilationUnit.isSuccessful()) {
            	   com.github.javaparser.ast.Node rootnode=compilationUnit.getResult().get().findRootNode();
                   Node domainNode=toDomainNode(rootnode, subpathFromRoot);
                   forest.put(subpathFromRoot, domainNode);
                   
                   //ChildParentRelFinder rf=new ChildParentRelFinder(subpathFromRoot);
                   ImportsRelFinder rf=new ImportsRelFinder(subpathFromRoot);
                   rf.visit(compilationUnit.getResult().get(), rels);
                   
                   
               }else {
            	   //
            	   log.error("error de compilacion");
               }
               
            }).explore(rootDir);
        
        for(Rel rel:rels) {
        	System.out.println(rel);
        }
        
        return forest;
    }

    

 


	private Position toDomainPosition( com.github.javaparser.Position position) {
    	return Position.builder()
    	.line(position.line)
    	.column(position.column)
    	.build();
    }
    
    private jmdevall.opencodeplan.domain.Node toDomainNode(com.github.javaparser.ast.Node node, String file){
    	ArrayList<Node> children=new ArrayList<Node>();
    	
    	//getTypeDeclaration(node);
    	//findRelsParentChildren(node,file);
    	//obtenerTipoDeExpresion(node,file);
    	
    	
        for(com.github.javaparser.ast.Node child: node.getChildNodes()) {
        	Node childomain=toDomainNode(child, file);
            children.add(childomain);
        }
       
        Node domainNode=Node.builder()
    		.id(toNodeId(node, file))
    		.type(node.getMetaModel().getTypeName())
    		.content(node.toString())
    		.children(children)
    	    .build();

        return domainNode;
    }



	private NodeId toNodeId(com.github.javaparser.ast.Node node, String file) {
		return NodeId.builder()
			.file(file)
			.begin(toDomainPosition(node.getBegin().get()))
			.end(toDomainPosition(node.getEnd().get()))
			.build();
	}



	private void getTypeDeclaration(com.github.javaparser.ast.Node node) {
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
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}
	
	private void findParentRelations(com.github.javaparser.ast.Node root,String file) {
			// TODO Auto-generated method stub
	    	
    	new VoidVisitorAdapter<List<Rel>>() {
    	
		};
	}
	
	private class ChildParentRelFinder extends VoidVisitorAdapter<List<Rel>>{
		
		private String file;
		
		public ChildParentRelFinder(String file) {
			super();
			this.file = file;
		}


		@Override
		public void visit(MethodDeclaration n, List<Rel> arg) {
			super.visit(n, arg);
			
			Optional<com.github.javaparser.ast.Node> parent=n.getParentNode();
			if(parent.isPresent()) {
				Rel childToParent=Rel.builder()
				.origin(toNodeId(n, file))
				.destiny(toNodeId(n.getParentNode().get(),file))
				.label(Label.CHILD_OF)
				.build();
				
				Rel parentToChild=Rel.builder()
				.origin(toNodeId(n.getParentNode().get(),file))
				.destiny(toNodeId(n, file))
				.label(Label.PARENT_OF)
				.build();

				arg.addAll(Arrays.asList(childToParent,parentToChild));
			}
		}
	}


	
	
	private class ImportsRelFinder extends VoidVisitorAdapter<List<Rel>>{
		
		private String file;
		
		private List<ImportDeclaration> importDeclarations;
		private MethodDeclaration currentMethod;
		
		public ImportsRelFinder(String file) {
			super();
			this.file = file;
			this.importDeclarations=new ArrayList<ImportDeclaration>();
		}

		
		
		@Override
		public void visit(ImportDeclaration n, List<Rel> arg) {
			importDeclarations.add(n);
			
			super.visit(n, arg);
		}

		

		@Override
		public void visit(FieldAccessExpr n, List<Rel> arg) {
			System.out.println("fieldAccessExpre "+file+" "+n);
			// Get the name of the field access expression
		    String fieldName = n.getName().asString();

		    // Check if the field name is in the list of imports
		    for (ImportDeclaration importDeclaration : importDeclarations) {
		        String importName = importDeclaration.getNameAsString();
		        if (importName.endsWith("." + fieldName)) {
		            System.out.println("Field access " + fieldName + " uses import " + importName);
		        }
		    }
		}



		@Override
		public void visit(MethodDeclaration n, List<Rel> arg) {
			currentMethod=n;
			
			super.visit(n, arg);
		}



		@Override
		public void visit(ClassOrInterfaceDeclaration n, List<Rel> arg) {
			n.getex
			// TODO Auto-generated method stub
			super.visit(n, arg);
		}
		
		
		
	}

	

	private void obtenerTipoDeExpresion(com.github.javaparser.ast.Node node,String file) {
		// https://tomassetti.me/wp-content/uploads/2017/12/JavaParser-JUG-Milano.pdf
		
	    if (node instanceof Expression) {
	    	SymbolResolver symbolResolver = node.getSymbolResolver();
	    	System.out.println("siiii");
	    	Expression es=(Expression)node;
	    	ResolvedType foo=es.calculateResolvedType();
	    	System.out.println("expresion ["+node.toString()+"] "+file+" "+node.getBegin().get()+" "+node.getEnd().get()+" "+" "+foo.toString());
	    	
	    }
		
	}
    

}
