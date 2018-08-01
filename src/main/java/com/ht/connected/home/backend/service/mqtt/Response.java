package com.ht.connected.home.backend.service.mqtt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ht.connected.home.backend.device.category.ir.IRValue;

public class Response {
    @JsonProperty(value = "format")
    private String format;
    
    @JsonProperty(value = "rptcnt")
    private int rptcnt;
    
    @JsonProperty(value = "gap")
    private int gap;
    
    @JsonProperty(value = "value")
    private List<IRValue> value;

    public Response() {
    }

    public Response(String response){
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap map = objectMapper.convertValue(response, HashMap.class);
        if(map.get("format")!=null) {
            this.format = (String)map.getOrDefault("format", "");
        }
        if(map.get("rptcnt")!=null) {
            this.rptcnt = (int)map.getOrDefault("rptcnt",0);
        }
        if(map.get("gap")!=null) {
            this.gap = (int)map.getOrDefault("gap",0);
        }
        if(map.get("value")!=null) {
            this.value = (List<IRValue>)map.getOrDefault("value",new ArrayList<>());
        }
    }
    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the rptcnt
     */
    public int getRptcnt() {
        return rptcnt;
    }

    /**
     * @param rptcnt the rptcnt to set
     */
    public void setRptcnt(int rptcnt) {
        this.rptcnt = rptcnt;
    }

    /**
     * @return the gap
     */
    public int getGap() {
        return gap;
    }

    /**
     * @param gap the gap to set
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * @return the value
     */
    public List<IRValue> getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(List<IRValue> value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Response [format=" + format + ", rptcnt=" + rptcnt + ", gap=" + gap + ", value=" + value + "]";
    }
    
    
}
