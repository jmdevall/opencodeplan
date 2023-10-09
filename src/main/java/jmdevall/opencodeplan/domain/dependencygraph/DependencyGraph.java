package jmdevall.opencodeplan.domain.dependencygraph;

import java.util.List;

import jmdevall.opencodeplan.CodePlan;
import jmdevall.opencodeplan.domain.BlockCode;
import jmdevall.opencodeplan.domain.Fragment;
import jmdevall.opencodeplan.domain.Label;
import jmdevall.opencodeplan.domain.repository.Repository;
import lombok.Builder;

/**
 * Dependency analysis [12] is used for tracking syntactic and semantic relations
between code elements. In our case, we are interested in relations between import statements,
methods, classes, field declarations and statements (excluding those that operate only on variables
defined locally within the enclosing method). Formally, a dependency graph D = (𝑁 , 𝐸) where 𝑁
is a set of nodes representing the code blocks mentioned above and 𝐸 is a set of labeled edges
where the edge label gives the relation between the source and target nodes of the edge. Figure 4
illustrates all the relations we track as labeled edges. The relations include (1) syntactic relations
(ParentOf and ChildOf, Construct and ConstructedBy) between a block 𝑐 and the block 𝑝 that
encloses 𝑐 syntactically; a special case being a constructor and its enclosing class related by
Construct and ConstructedBy, (2) import relations (Imports and ImportedBy) between an import
statement and statements that use the imported modules, (3) inheritance relations (BaseClassOf
and DerivedClassOf) between a class and its superclass, (4) method override relations (Overrides
and OverridenBy) between an overriding method and the overriden method, (5) method invocation
relations (Calls and CalledBy) between a statement and the method it calls, (6) object instantiation
relations (Instantiates and InstantiatedBy) between a statement and the constructor of the object it
creates, and (7) field use relations (Uses and UsedBy) between a statement and the declaration of a
field it uses.
 */
public class DependencyGraph {

    Repository r;

    public DependencyGraph(Repository r) {
        this.r = r;
    }

    

    public DependencyGraph updateDependencyGraph(List<Label> labels, Fragment fragment, Fragment newFragment, BlockCode b) {
        // TODO:
        return new DependencyGraph(this.r);
    }

    @Builder
    public static class ClassNode{
        String className;
    }
    
    /*@Builder
    public class MethodNode{
        String methodName;
    }*/
}