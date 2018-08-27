package com.ht.connected.home.backend.update.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UPSerialNumbersDto {

    public static final int MAX_LABEL_NUMBER_MONTH = Integer.parseInt("FFFF", 16);

    private Integer modelNumber;

    private Integer productionYear;

    private Integer productionMonth;

    private Integer labelNumber;

    public UPSerialNumbersDto(String serial) {
        this.modelNumber = Integer.parseInt(serial.substring(0, 2), 16);
        this.productionYear = Integer.valueOf(serial.substring(2, 4));
        this.productionMonth = Integer.parseInt(serial.substring(4, 5), 16);
        this.labelNumber = Integer.parseInt(serial.substring(5), 16);
    }

    public Integer getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(Integer modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Integer getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(Integer productionYear) {
        this.productionYear = productionYear;
    }

    public Integer getProductionMonth() {
        return productionMonth;
    }

    public void setProductionMonth(Integer productionMonth) {
        this.productionMonth = productionMonth;
    }

    public Integer getLabelNumber() {
        return labelNumber;
    }

    public void setLabelNumber(Integer labelNumber) {
        this.labelNumber = labelNumber;
    }

    @Override
    public String toString() {
        String objString = null;
        try {
            objString = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return objString;
    }

}
