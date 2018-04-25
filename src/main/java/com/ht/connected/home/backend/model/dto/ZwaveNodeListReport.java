package com.ht.connected.home.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ZwaveNodeListReport {

    

    public List<NodeListItem> nodelist = new ArrayList<NodeListItem>();

    public List<NodeListItem> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<NodeListItem> nodelist) {
        this.nodelist = nodelist;
    }

}
