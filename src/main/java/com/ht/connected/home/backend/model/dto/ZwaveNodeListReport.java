package com.ht.connected.home.backend.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ZwaveNodeListReport {

    public static class CmdClsListItem {
        public String secure = "";
        public String cmd_class = "";
        public String ver = "";
        public String real_ver = "";
        public List<String> rpt_cmd = new ArrayList<String>();

        public String getSecure() {
            return secure;
        }

        public void setSecure(String secure) {
            this.secure = secure;
        }

        public String getCmd_class() {
            return cmd_class;
        }

        public void setCmd_class(String cmd_class) {
            this.cmd_class = cmd_class;
        }

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getReal_ver() {
            return real_ver;
        }

        public void setReal_ver(String real_ver) {
            this.real_ver = real_ver;
        }

        public List<String> getRpt_cmd() {
            return rpt_cmd;
        }

        public void setRpt_cmd(List<String> rpt_cmd) {
            this.rpt_cmd = rpt_cmd;
        }

    }

    public static class EndPointListitem {
        public String epid = "";
        public String generic = "";
        public String specific = "";
        public String nickname = "";
        public List<CmdClsListItem> cmd_cls = new ArrayList<CmdClsListItem>();

        public String getEpid() {
            return epid;
        }

        public void setEpid(String epid) {
            this.epid = epid;
        }

        public String getGeneric() {
            return generic;
        }

        public void setGeneric(String generic) {
            this.generic = generic;
        }

        public String getSpecific() {
            return specific;
        }

        public void setSpecific(String specific) {
            this.specific = specific;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public List<CmdClsListItem> getCmd_cls() {
            return cmd_cls;
        }

        public void setCmd_cls(List<CmdClsListItem> cmd_cls) {
            this.cmd_cls = cmd_cls;
        }

    }

    public static class NodeListItem {
        public String nodeId = "";
        public String homeid = "";
        public String security = "";
        public String basic = "";
        public String generic = "";
        public String specific = "";
        public String lib_type = "";
        public String proto_ver = "";
        public String app_ver = "";
        public String crc_cap = "";
        public String s2_key_valid = "";
        public String s2_grnt_keys = "";
        public List<EndPointListitem> endpoint = new ArrayList<EndPointListitem>();

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getHomeid() {
            return homeid;
        }

        public void setHomeid(String homeid) {
            this.homeid = homeid;
        }

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public String getBasic() {
            return basic;
        }

        public void setBasic(String basic) {
            this.basic = basic;
        }

        public String getGeneric() {
            return generic;
        }

        public void setGeneric(String generic) {
            this.generic = generic;
        }

        public String getSpecific() {
            return specific;
        }

        public void setSpecific(String specific) {
            this.specific = specific;
        }

        public String getLib_type() {
            return lib_type;
        }

        public void setLib_type(String lib_type) {
            this.lib_type = lib_type;
        }

        public String getProto_ver() {
            return proto_ver;
        }

        public void setProto_ver(String proto_ver) {
            this.proto_ver = proto_ver;
        }

        public String getApp_ver() {
            return app_ver;
        }

        public void setApp_ver(String app_ver) {
            this.app_ver = app_ver;
        }

        public String getCrc_cap() {
            return crc_cap;
        }

        public void setCrc_cap(String crc_cap) {
            this.crc_cap = crc_cap;
        }

        public String getS2_key_valid() {
            return s2_key_valid;
        }

        public void setS2_key_valid(String s2_key_valid) {
            this.s2_key_valid = s2_key_valid;
        }

        public String getS2_grnt_keys() {
            return s2_grnt_keys;
        }

        public void setS2_grnt_keys(String s2_grnt_keys) {
            this.s2_grnt_keys = s2_grnt_keys;
        }

        public List<EndPointListitem> getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(List<EndPointListitem> endpoint) {
            this.endpoint = endpoint;
        }

    }

    public List<NodeListItem> nodelist = new ArrayList<NodeListItem>();

    public List<NodeListItem> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<NodeListItem> nodelist) {
        this.nodelist = nodelist;
    }

}
