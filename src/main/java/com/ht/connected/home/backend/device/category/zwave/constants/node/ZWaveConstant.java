package com.ht.connected.home.backend.device.category.zwave.constants.node;

import java.util.List;

/**
 * Class that represents a multi-channel node endpoint. Multi-instance/Multi-channel Z-Wave devices can have multiple endpoints which act like "child nodes" with their own identify and command
 * classes.
 * @author Dan Noguerol
 */
public class ZWaveConstant {
    
    enum ProtocalVersion {
        PROTOCAL_5_02Patch2("5.02 Patch2", 2, "64") 
        , PROTOCAL_5_02Patch1("5.02 Patch1", 2, "51") 
        , PROTOCAL_5_02("5.02", 2, "48")
        , PROTOCAL_5_01("5.01", 2, "36") 
        , PROTOCAL_5_00Patch1("5.00Patch1", 2, "22") 
        , PROTOCAL_5_00Beta1("5.00Beta1", 2, "16")
        , PROTOCAL_4_50Beta1("4.50Beta1", 2, "74")
        , PROTOCAL_4_30Beta1("4.30Beta1", 2, "30")
        , PROTOCAL_4_34("4.34", 2, "39.39")
        , PROTOCAL_4_28("4.28", 2, "67")
        , PROTOCAL_4_27("4.27", 2, "40") 
        , PROTOCAL_4_26("4.26", 2, "32") 
        , PROTOCAL_4_25("4.25", 2, "31")
        , PROTOCAL_4_24Patch1("4.24Patch1", 2, "28")
        , PROTOCAL_4_24("4.24", 2, "24")
        , PROTOCAL_4_23("4.23", 2, "17")
        , PROTOCAL_4_22("4.22", 2, "09")
        , PROTOCAL_4_21("4.21", 2, "06")
        , PROTOCAL_4_20("4.20", 1, "97")
        , PROTOCAL_4_11("4.11", 1, "91")
        , PROTOCAL_4_10("4.10", 1, "78")
        , PROTOCAL_4_07("4.07", 2, "27")
        , PROTOCAL_4_06("4.06", 2, "23")
        , PROTOCAL_4_05("4.05", 2, "07")
        , PROTOCAL_4_04("4.04", 1, "99")
        , PROTOCAL_4_03("4.03", 1, "81")
        , PROTOCAL_4_02("4.02", 1, "9" )
        , PROTOCAL_4_01("4.01", 1, "68")
        , PROTOCAL_4_00("4.00", 1, "59")
        , PROTOCAL_3_40("3.40", 1, "53")
        , PROTOCAL_3_31("3.31", 1, "44")
        , PROTOCAL_3_30("3.30", 1, "37")
        , PROTOCAL_3_22("3.22", 1, "39")
        , PROTOCAL_3_21("3.21", 1, "25")
        , PROTOCAL_3_20("3.20", 1, "21");
        
        private String protocal;
        private List<String> lstProtocal;
        private int version;
        private String subVersion;

        ProtocalVersion(String protocal, List<String> lstProtocal) {
            this.protocal = protocal;
            this.lstProtocal = lstProtocal;
        }

        ProtocalVersion(String protocal, int version, String subVersion) {
            this.protocal = protocal;
            this.version = version;
            this.subVersion = subVersion;
        }

        public String getProtocal() {
            return protocal;
        }

        public int getVersion() {
            return version;
        }

        public String getSubVersion() {
            return subVersion;
        }
        
        public static int findByVersion(String protocal) {
            if(null!=findByProtocalVersion(protocal)) {
                return findByProtocalVersion(protocal).getVersion();
            }
            return (Integer) null;
        }
        
        public static String findBySubVersion(String protocal) {
            if(null!=findByProtocalVersion(protocal)) {
                return findByProtocalVersion(protocal).getSubVersion();
            }
            return (String) null;
        }
        public static ProtocalVersion findByProtocalVersion(String protocal) {
            ProtocalVersion[] lstProtocal = ProtocalVersion.values();
            for(int i=0; i < lstProtocal.length; i++) {
                if(protocal.equals(lstProtocal[i].getProtocal())){
                    return lstProtocal[i];
                }
            }
            return (ProtocalVersion) null;
        }
    }
    /**
    #define COMMAND_CLASS_SWITCH_ALL                                                         0x27
    #define COMMAND_CLASS_SWITCH_BINARY                                                      0x25
    #define COMMAND_CLASS_SWITCH_BINARY_V2                                                   0x25
    #define COMMAND_CLASS_SWITCH_MULTILEVEL                                                  0x26
    #define COMMAND_CLASS_SWITCH_MULTILEVEL_V2                                               0x26
    #define COMMAND_CLASS_SWITCH_MULTILEVEL_V3                                               0x26
    #define COMMAND_CLASS_SWITCH_MULTILEVEL_V4                                               0x26
    */
    enum Generic_Specific_Type{
        
        GENERIC_TYPE_SWITCH_BINARY("0x10" ,"Binary Switch");
        
        private String genericKey;
        private String genericName;
       
        Generic_Specific_Type(String genericKey, String genericName){
            this.genericKey = genericKey;
            this.genericName = genericName;
        }
        enum SpecificType{
            
           SPECIFIC_TYPE_NOT_USED("0x00","Specific Device Class Not Used") 
          ,SPECIFIC_TYPE_POWER_SWITCH_BINARY("0x01","On/Off Power Switch Device Type")
          ,SPECIFIC_TYPE_SCENE_SWITCH_BINARY("0x03","Binary Scene Switch")
          ,SPECIFIC_TYPE_POWER_STRIP("0x04","Power Strip Device Type")
          ,SPECIFIC_TYPE_SIREN("0x05","Siren Device Type")
          ,SPECIFIC_TYPE_VALVE_OPEN_CLOSE("0x06","Valve (open/close) Device Type")
          ,SPECIFIC_TYPE_COLOR_TUNABLE_BINARY("0x02","")
          ,SPECIFIC_TYPE_IRRIGATION_CONTROLLER("0x07","");
            
            String classSpecificTypeKey;
            String classSpecificTypeNicname;
    
            SpecificType(String classSpecificTypeKey, String classSpecificTypeNicname){
                this.classSpecificTypeKey = classSpecificTypeKey;
                this.classSpecificTypeNicname = classSpecificTypeNicname;
            }
           
            public String getClassSpecificTypeKey() {
                return classSpecificTypeKey;
            }
            public void setClassSpecificTypeKey(String classSpecificTypeKey) {
                this.classSpecificTypeKey = classSpecificTypeKey;
            }
            public String getClassSpecificTypeNicname() {
                return classSpecificTypeNicname;
            }
            public void setClassSpecificTypeNicname(String classSpecificTypeNicname) {
                this.classSpecificTypeNicname = classSpecificTypeNicname;
            }
        }
        enum SwitchControl{
            SWITCH_ALL_ON
            
            
        }
    }
}
