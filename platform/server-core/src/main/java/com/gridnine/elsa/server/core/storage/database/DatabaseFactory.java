/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database;

import com.gridnine.elsa.common.core.model.common.ClassMapper;
import com.gridnine.elsa.common.core.model.common.EnumMapper;
import com.gridnine.elsa.common.core.model.common.IdGenerator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public interface DatabaseFactory {

    Database getPrimaryDatabase();

    ClassMapper getClassMapper();

    EnumMapper getEnumMapper();

    IdGenerator getIdGenerator();

    PlatformTransactionManager getTransactionManager();

    DataSource getFakeDataSource();

}
