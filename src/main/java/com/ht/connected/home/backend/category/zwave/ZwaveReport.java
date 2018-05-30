package com.ht.connected.home.backend.category.zwave;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZwaveReport {
    
    @JsonProperty("nodelist")
    public List<Zwave> nodelist;

    public List<Zwave> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<Zwave> nodelist) {
        this.nodelist = nodelist;
    }

}
