package net.ooder.esd.engine.config;

import java.util.ArrayList;
import java.util.List;

public class TeamConfig {

    String owner;

    List<String> groups=new ArrayList<String>();

    boolean isCanGust;

    public boolean isCanGust() {
        return isCanGust;
    }

    public void setCanGust(boolean canGust) {
        isCanGust = canGust;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
