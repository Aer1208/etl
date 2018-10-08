package com.mongohua.etl.model;

/**
 * 菜单表
 * @author xiaohf
 */
public class Menu {

    /**
     * 菜单ID
     */
    private int menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 父菜单ID
     */
    private String parentId;
    /**
     * 菜单URL
     */
    private String url;
    /**
     * 菜单对应的权限
     */
    private String permission;
    /**
     * 菜单类型
     */
    private String permType;
    /**
     * 菜单状态 1： 有效，0：无效
     */
    private int status = 1;

    /**
     * 用户是否包含权限
     */
    private int checked;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }
}
