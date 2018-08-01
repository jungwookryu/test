package com.ht.connected.home.backend.device.category;

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
        init,
        status
    }
    public enum manager{
        alive,
        init,
        noti
    }
}

