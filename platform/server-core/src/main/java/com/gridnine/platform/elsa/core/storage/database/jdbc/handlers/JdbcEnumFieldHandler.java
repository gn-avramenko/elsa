/*
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gridnine.platform.elsa.core.storage.database.jdbc.handlers;

import com.gridnine.platform.elsa.common.core.model.common.ClassMapper;
import com.gridnine.platform.elsa.common.core.model.common.EnumMapper;
import com.gridnine.platform.elsa.common.core.model.domain.CaptionProvider;
import com.gridnine.platform.elsa.common.core.reflection.ReflectionFactory;
import com.gridnine.platform.elsa.common.core.utils.Pair;
import com.gridnine.platform.elsa.core.storage.database.jdbc.adapter.JdbcDialect;
import com.gridnine.platform.elsa.core.storage.database.jdbc.model.JdbcFieldType;
import com.gridnine.platform.elsa.core.storage.database.jdbc.model.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class JdbcEnumFieldHandler extends BaseJdbcSingleFieldHandler {

    private final Class<Enum<?>> enumClass;

    public JdbcEnumFieldHandler(String fieldName, Class<Enum<?>> enumClass, boolean indexed) {
        super(fieldName, indexed, JdbcFieldType.INT);
        this.enumClass = enumClass;
    }

    @Override
    public Object getModelValue(ResultSet rs, EnumMapper enumMapper, ClassMapper classMapper, CaptionProvider captionProvider, ReflectionFactory factory, JdbcDialect dialect) throws SQLException {
        return JdbcUtils.isNull(rs, fieldName) ? null : factory.safeGetEnum(enumClass, enumMapper.getName(rs.getInt(fieldName), enumClass));
    }

    @Override
    public Map<String, Pair<Object, JdbcFieldType>> getSqlValues(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Collections.singletonMap(fieldName, Pair.of(value == null ? null : enumMapper.getId((Enum<?>) value), JdbcFieldType.INT));
    }

    @Override
    public Pair<Object, JdbcFieldType> getSqlQueryValue(Object value, EnumMapper enumMapper, ClassMapper classMapper, ReflectionFactory factory) {
        return Pair.of(value == null ? null : enumMapper.getId((Enum<?>) value), JdbcFieldType.INT);
    }
}
