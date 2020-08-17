package com.warys.scrooge.domain.common.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Slf4j
public class BeanUtil {

    private static final String PROJECT_PACKAGE_NAME = "com.warys.scrooge";

    public static void copyBean(Object orig, Object dest) {
        copyBean(orig, dest, new String[0]);
    }

    private BeanUtil() {
    }

    private static void copyBean(Object orig, Object dest, String... ignoredFields) {
        final var ignoredFieldsList = Lists.newArrayList(ignoredFields);
        try {
            for (String key : BeanUtils.describe(orig).keySet()) {
                if (!"class".equals(key) && !ignoredFieldsList.contains(key)) {
                    Object value = PropertyUtils.getProperty(orig, key);
                    if (!Objects.isNull(value)) {
                        if (value.getClass().getPackage().getName().contains(PROJECT_PACKAGE_NAME)) {
                            makeRecursiveCall(dest, key, value);
                        } else {
                            copyProperty(dest, key, value);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Error occurred during mapping : {}", e.getMessage());
        }
    }

    private static void makeRecursiveCall(Object dest, String key, Object orig) {
        try {
            final Object currentInstance = PropertyUtils.getProperty(dest, key);
            if (null != currentInstance) {
                copyBean(orig, currentInstance);
            } else {
                final Object newInstance = PropertyUtils.getPropertyType(dest, key).getConstructor().newInstance();
                BeanUtils.setProperty(dest, key, newInstance);
                copyBean(orig, newInstance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Error occurred during recursive call : {}", e.getMessage());
        }
    }

    private static void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.error("Error occurred during mapping : {}", e.getMessage());
        }
    }
}
