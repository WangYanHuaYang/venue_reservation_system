package com.genolo.venue_reservation_system.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysPermissionTree {
    private String id;

    private String permissionName;

    private String permissionUrl;

    private String parentId;

    private Integer permissionType;

    private Integer sort;

    private String permissionCss;

    private List<SysPermissionTree> child;

    public SysPermissionTree(SysPermission sysPermission){
        this.id=sysPermission.getId();
        this.permissionName=sysPermission.getPermissionName();
        this.permissionUrl=sysPermission.getPermissionUrl();
        this.parentId=sysPermission.getParentId();
        this.permissionType=sysPermission.getPermissionType();
        this.permissionCss=sysPermission.getPermissionCss();
        this.sort=sysPermission.getSort();
    }
}
