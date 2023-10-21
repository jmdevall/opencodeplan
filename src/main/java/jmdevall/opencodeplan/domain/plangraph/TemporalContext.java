package jmdevall.opencodeplan.domain.plangraph;

import java.util.ArrayList;
import java.util.List;

import jmdevall.opencodeplan.domain.BI;

public class TemporalContext {
    private List<Pair<BI, CMI>> contexts = new ArrayList<>();

    public void addContext(BI bi, CMI cmi) {
        contexts.add(new Pair<>(bi, cmi));
    }

    public List<Pair<BI, CMI>> getContexts() {
        return contexts;
    }
}