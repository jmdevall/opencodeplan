package jmdevall.opencodeplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;

import ai.serenade.treesitter.Languages;
import ai.serenade.treesitter.Node;
import ai.serenade.treesitter.Parser;
import ai.serenade.treesitter.Tree;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MyTest {
  static {
    // or on a Mac, libjava-tree-sitter.dylib
    //System.load("/home/vicuna/js/tree-sitter/lib-tree-sitter.so");
    System.load("/home/vicuna/js/java-tree-sitter/libjava-tree-sitter.so");
    System.out.println("librería cargarda");
  }
    @Test
    public void nose() throws UnsupportedEncodingException{
        assertTrue(true);

            System.out.println("iniciando test");

        
        try (Parser parser = new Parser()) {
            parser.setLanguage(Languages.java());
                System.out.println("parece que java está soportado");

                try(Tree tree = parser.parseString("class Foo{ \n public void main(String args[]){}\n}")){
              Node root=tree.getRootNode();
              //System.out.println("salida="+root.toString());
               assertEquals(1, root.getChildCount());
            }

            //parser.setLanguage(Languages.python());
/*            try (Tree tree = parser.parseString("def foo(bar, baz):\n  print(bar)\n  print(baz)")) {
              Node root = tree.getRootNode();
              assertEquals(1, root.getChildCount());
              assertEquals("module", root.getType());
              assertEquals(0, root.getStartByte());
              assertEquals(44, root.getEndByte());
          
              Node function = root.getChild(0);
              assertEquals("function_definition", function.getType());
              assertEquals(5, function.getChildCount());
            }
          }
          */
          
    }
  }
}
