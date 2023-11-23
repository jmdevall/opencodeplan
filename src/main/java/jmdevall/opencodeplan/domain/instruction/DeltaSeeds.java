package jmdevall.opencodeplan.domain.instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jmdevall.opencodeplan.domain.BI;
import jmdevall.opencodeplan.domain.dependencygraph.DependencyGraph;
import jmdevall.opencodeplan.domain.dependencygraph.Node;


public class DeltaSeeds {
	private List<Seed> seeds=new ArrayList<Seed>();
	
    public boolean isEmpty() {
    	return seeds.isEmpty();
    }

    public List<Seed> getSeeds() {
    	return seeds;
    }
    
    public void addSeed(Seed bi){
    	this.seeds.add(bi);
    }
    
    public List<BI> getBis(DependencyGraph d){
    	ArrayList<BI> ret=new ArrayList<BI>();
    	for(Seed seed:seeds) {
    		Optional<Node> b=search(d,seed.getBlock());
    		if(b.isPresent()) {
    			ret.add(BI.builder()
    					.b(b.get())
    					.i(seed.getInstruction())
    					.build());
    		}
    	}
    	return ret;
    		
    }
    
    private Optional<Node> search(DependencyGraph d,NodeSearchDescriptor descriptor){
    	List<Node> cus=d.getForest().values().stream().collect(Collectors.toList());
    	for(Node cu:cus) {
    		Optional<Node> node=cu.toStream().filter(n->descriptor.match(n)).findAny();
    		if(node.isPresent()) {
    			return node;
    		}
    	}
    	return Optional.empty();
    }

}