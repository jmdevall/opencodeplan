package jmdevall.opencodeplan.adapter.out.javaparser.notused;

import com.github.javaparser.ast.Node;

public class NodeIterator {

    private NodeHandler nodeHandler;

    public NodeIterator(NodeHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    public void explore(Node node) {
        if (nodeHandler.handle(node)) {
            for (Node child : node.getChildNodes()) {
                explore(child);
            }
        }
    }
}