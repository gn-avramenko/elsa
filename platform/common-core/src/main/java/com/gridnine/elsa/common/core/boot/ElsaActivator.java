/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.boot;

import com.gridnine.elsa.common.core.model.common.HasPriority;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;

public interface ElsaActivator extends HasPriority {
    void activate() throws Exception;

    static void performActivation(ConfigurableApplicationContext ctx) {
        ctx.getBeanFactory().getBeansOfType(ElsaActivator.class).values().stream()
                .sorted(Comparator.comparing(HasPriority::getPriority)).forEach((b) -> ExceptionUtils.wrapException(b::activate));
    }
}
