package com.genolo.venue_reservation_system.model;

import lombok.Data;

import java.util.List;

@Data
public class SysPermissionTree {
    private String id;

    private String permissionName;

    private String permissionUrl;

    private String parentId;

    private Integer permissionType;

    private String css;

    private List<SysPermissionTree> child;

    public SysPermissionTree(SysPermission sysPermission){
        this.id=sysPermission.getId();
        this.permissionName=sysPermission.getPermissionName();
        this.permissionUrl=sysPermission.getPermissionUrl();
        this.parentId=sysPermission.getParentId();
        this.permissionType=sysPermission.getPermissionType();
        this.css=sysPermission.getPermissionCss();
    }
}
