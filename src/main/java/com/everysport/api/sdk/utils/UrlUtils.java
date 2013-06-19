package com.everysport.api.sdk.utils;
/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public abstract class UrlUtils {

    public static String encode(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String decode(final String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void addParam(final String name, final String value,
                                final StringBuilder uri) {
        if (uri.length() > 0)
            uri.append('&');
        uri.append(encode(name)).append('=');
        if (value != null)
            uri.append(encode(value));
    }

    public static void addParams(final Map<String, String> params,
                                 final StringBuilder uri) {
        if (params == null || params.isEmpty())
            return;
        for (Map.Entry<String, String> param : params.entrySet())
            addParam(param.getKey(), param.getValue(), uri);
    }

    public static String getParam(final URI uri, final String name) {
        final String query = uri.getRawQuery();
        if (query == null || query.length() == 0)
            return null;
        final String[] params = query.split("&"); //$NON-NLS-1$
        for (String param : params) {
            final String[] parts = param.split("="); //$NON-NLS-1$
            if (parts.length != 2)
                continue;
            if (!name.equals(parts[0]))
                continue;
            return decode(parts[1]);
        }
        return null;
    }
}
