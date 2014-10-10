/**
 * package com.ssi
 * class com.ssi.i18n.messages
 * Created on 2014年10月10日, 下午5:21:21
 * @author williamz
 *
 * Copyright (c) 2013, Synnex and/or its affiliates. All rights reserved.
 * SYNNEX PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.ssi.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <b>Description</b>: 
 * <p><b>Features or change log:</b>
 * <ol>
 * <li>2014年10月10日 下午5:21:21, williamz, C001: </li>
 * <li></li>
 * </ol>
 * @author williamz@synnex.com
 */
public class Messages {
    
    public static Locale currentLocale = new Locale("zh", "CN");
    
    private static final String BUNDLE_NAME = "com.ssi.i18n.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
