package jmdevall.opencodeplan.adapter.out.javaparser;

import com.github.javaparser.ast.Node;

public interface NodeHandler {
    boolean handle(Node node);
}