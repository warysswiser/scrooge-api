package com.warys.scrooge.domain.common.util;

public class Patcher {

    public static void patch(Object orig, Object dest) {
        BeanUtil.copyBean(orig, dest);
    }
}
