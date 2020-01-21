package com.genolo.venue_reservation_system.model;

import lombok.Data;

import java.util.List;

@Data
public class LoginBean {
    private String id;
    private String userName;
    private String[] roleNames;
    private Integer state;
    private String eMail;
    private String organization;
    private String phoneNumber;
    private List<SysPermissionTree> permissionTree;
}
