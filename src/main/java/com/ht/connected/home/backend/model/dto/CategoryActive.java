package com.ht.connected.home.backend.model.dto;

public class CategoryActive {
    public enum gateway{
        manager,
        zwave,
        ir
    }
    public enum ir{}
    public enum zwave{
        alive,
        certi,
        init
    }
    public enum manager{
        alive,
        init,
        noti
    }
}

