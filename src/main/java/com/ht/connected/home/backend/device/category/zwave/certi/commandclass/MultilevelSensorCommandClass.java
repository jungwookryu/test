package com.ht.connected.home.backend.device.category.zwave.certi.commandclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Multilevel Sensor Command Class
 *
 * @author ijlee
 */
public class MultilevelSensorCommandClass extends CommandClass {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final byte SENSOR_MULTILEVEL_GET = 0x04;
    public static final byte SENSOR_MULTILEVEL_REPORT = 0x05;

    public static final byte ID = 0x31;
    public static final int INT_ID = (byte)0x31;
    
    public static final int INT_SENSOR_MULTILEVEL_GET = (byte)0x04;
    public static final int INT_SENSOR_MULTILEVEL_REPORT = (byte)0x05;
    
    public static final String genericKey = "21";
    public static final String functionCode ="31";
    public int type;
    public String typeName;
    public int scaleCode;
    public String label;
    public int labelCode;
    public String labelName;
    private List<Double> values = new ArrayList<>();

    @Override    public byte getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "COMMAND_CLASS_SENSOR_MULTILEVEL";
    }

   
    public List<Double> getValues() {
        return values;
    }

    public void setTypeAndScale(Properties p, int t, int s) {
        type = Type.values()[t].ordinal();
        typeName = p.getProperty(Integer.toString(t), "no signed type");
        labelCode = s; 
        label = p.getProperty(Integer.toString(t)+"."+Integer.toString(s), "no signed label");
    }

    public enum Type {
            Reserved,
            Air_Temperature,
            General_Purpose,
            Luminance,
            Power,
            Humidity,
            Velocity,
            Direction,
            Atmospheric_Pressure,
            Barometric_Pressure,
            Solar,
            Dew_point,
            Rain_rate,
            Tide_level,
            Weight,
            Voltage,
            Current,
            Carbon_Dioxide_CO2_level,
            Air_flow,
            Tank_capacity,
            Distance,
            Rotation,
            Water_Temperature,
            Soil_Temperature,
            Seismic_Intensity,
            Seismic_Magnitude,
            Ultraviolet,
            Electrical_Resistivity,
            Electrical_Conductivity,
            Loudness,
            Moisture,
            Frequency,
            Time,
            Target_Temperature,
            Particulate_Matter_2_5,
            Formaldehyde_CH2O_level,
            Radon_Concentration,
            Methane_Density_CH4,
            Volatile_Organic_Compound,
            Carbon_Monoxide_CO_level,
            Soil_Humidity,
            Soil_Reactivity,
            Soil_Salinity,
            Heart_Rate,
            Blood_Pressure,
            Muscle_Mass,
            Fat_Mass,
            Bone_Mass,
            Total_Body_Water,_TBW,
            Basic_Metabolic_Rate,_BMR,
            Body_Mass_Index,_BMI,
            Acceleration_X_axis,
            Acceleration_Y_axis,
            Acceleration_Z_axis,
            Smoke_Density,
            Water_Flow,
            Water_Pressure,
            RF_Signal_Strength,
            Particulate_Matter,
            Respiratory_Rate
    }

    @Override
    public String getDeviceType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNicknameType() {

        return null;
    }

    @Override
    public String getFunctionType() {
        return functionCode;
    }

    @Override
    public String getGenericKey() {
        
        return genericKey;
    }

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return functionCode;
    }
}
