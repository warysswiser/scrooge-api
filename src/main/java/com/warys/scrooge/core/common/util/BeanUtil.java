package com.warys.scrooge.core.common.util;

import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class BeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);
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
                            copyBean(value, PropertyUtils.getProperty(dest, key));
                        } else {
                            copyProperty(dest, key, value);
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("Error occurred during mapping : {}", e.getMessage());
        }
    }

    private static void copyProperty(Object bean, String name, Object value) {
        try {
            BeanUtils.copyProperty(bean, name, value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.error("Error occurred during mapping : {}", e.getMessage());
        }
    }
}
