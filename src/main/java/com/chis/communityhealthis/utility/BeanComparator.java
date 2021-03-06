package com.chis.communityhealthis.utility;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class BeanComparator {

    private Object oriObj;
    private Object newObj;
    private Diff diff;

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

    private void compare() {
        Javers javers = JaversBuilder.javers().build();
        this.diff = javers.compare(this.oriObj, this.newObj);
    }

    public boolean hasChanges() {
        return this.diff != null && !CollectionUtils.isEmpty(this.diff.getChanges());
    }

    public String toPrettyString() {
        return hasChanges() ? this.diff.prettyPrint() : null;
    }
}
