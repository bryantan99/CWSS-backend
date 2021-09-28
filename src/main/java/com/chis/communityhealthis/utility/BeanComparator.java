package com.chis.communityhealthis.utility;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;

import java.util.List;

public class BeanComparator {

    private Object oriObj;
    private Object newObj;
    private Diff diff;
    private List<ValueChange> changeList;

    public BeanComparator(Object oriObj, Object newObj) {
        this.oriObj = oriObj;
        this.newObj = newObj;
        compare();
    }

    public Object getOriObj() {
        return oriObj;
    }

    public void setOriObj(Object oriObj) {
        this.oriObj = oriObj;
    }

    public Object getNewObj() {
        return newObj;
    }

    public void setNewObj(Object newObj) {
        this.newObj = newObj;
    }

    public Diff getDiff() {
        return diff;
    }

    public void setDiff(Diff diff) {
        this.diff = diff;
    }

    public List<ValueChange> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<ValueChange> changeList) {
        this.changeList = changeList;
    }

    private void compare() {
        Javers javers = JaversBuilder.javers().build();
        this.diff = javers.compare(this.oriObj, this.newObj);
        this.changeList = diff.getChangesByType(ValueChange.class);
    }

    public boolean hasChanges() {
        return this.changeList.size() > 0;
    }

    public String toPrettyString() {
        return hasChanges() ? this.diff.prettyPrint() : null;
    }
}
