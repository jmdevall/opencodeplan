package jmdevall.opencodeplan.domain.plangraph;

import java.util.ArrayList;
import java.util.List;

public class TemporalContext {
    private List<TemporalChange> changes = new ArrayList<>();

    public void addChange(TemporalChange temporalChange) {
        changes.add(temporalChange);
    }

    public List<TemporalChange> getChanges() {
        return changes;
    }
}