package com.chis.communityhealthis.utility;

import java.util.HashMap;
import java.util.Map;

public class RoleConstant {

    private Map<String, String> map;

    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
    public static final String ADMIN = "ROLE_ADMIN";

    public RoleConstant() {
        initRoleMap();
    }

    private void initRoleMap() {
        this.map = new HashMap<>();
        this.map.put(SUPER_ADMIN, "Super Admin");
        this.map.put(ADMIN, "Admin");
    }

    public String getRoleFullName(String key) {
        return this.map.getOrDefault(key, null);
    }
}
