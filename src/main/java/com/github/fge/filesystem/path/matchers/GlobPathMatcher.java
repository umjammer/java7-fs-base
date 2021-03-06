/*
 * Copyright (c) 2014, Francis Galiegue (fgaliegue@gmail.com)
 *
 * This software is dual-licensed under:
 *
 * - the Lesser General Public License (LGPL) version 3.0 or, at your option, any
 *   later version;
 * - the Apache Software License (ASL) version 2.0.
 *
 * The text of both licenses is available under the src/resources/ directory of
 * this project (under the names LGPL-3.0.txt and ASL-2.0.txt respectively).
 *
 * Direct link to the sources:
 *
 * - LGPL 3.0: https://www.gnu.org/licenses/lgpl-3.0.txt
 * - ASL 2.0: http://www.apache.org/licenses/LICENSE-2.0.txt
 */

package com.github.fge.filesystem.path.matchers;

import java.lang.reflect.Method;
import java.security.PrivilegedActionException;
import java.util.Objects;

import vavi.beans.BeanUtil;

public final class GlobPathMatcher
    extends PathMatcherBase
{
    private final RegexPathMatcher matcher;

    public GlobPathMatcher(final String glob)
    {
        try {
            Class<?> clazz = Class.forName("sun.nio.fs.Globs");
            Method method = BeanUtil.getPrivateMethod(clazz, "toUnixRegexPattern", new Class[] { String.class });
            matcher = new RegexPathMatcher(String.class.cast(BeanUtil.invoke(method, null, Objects.requireNonNull(glob))));
        } catch (PrivilegedActionException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected boolean match(final String input)
    {
        return matcher.match(input);
    }
}
