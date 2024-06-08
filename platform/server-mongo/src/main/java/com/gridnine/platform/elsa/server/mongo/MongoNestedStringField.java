/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo;

import com.gridnine.platform.elsa.common.core.search.*;

public class MongoNestedStringField extends FieldNameSupport implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String> {

    public MongoNestedStringField(String name) {
        super(name);
    }
}
