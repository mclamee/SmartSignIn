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

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ssi.main.SSIConfig;

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
    
    private static final String BUNDLE_NAME = "com.ssi.i18n.messages"; //$NON-NLS-1$

    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(SSIConfig.get("system.locale.language"), SSIConfig.get("system.locale.country")));

    private Messages() {
    }

    public static String getString(String key) {
        try {
            return new String(RESOURCE_BUNDLE.getString(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        } catch (UnsupportedEncodingException e) {
        	return '!' + key + '!';
		}
    }
    
    public static void refresh() {
    	RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(SSIConfig.get("system.locale.language"), SSIConfig.get("system.locale.country")));
    }
    
}
