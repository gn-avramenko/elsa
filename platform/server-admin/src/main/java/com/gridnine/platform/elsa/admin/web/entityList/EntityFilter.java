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
 *
 *****************************************************************
 * This is generated code, don't modify it manually
 *****************************************************************/

package com.gridnine.platform.elsa.admin.web.entityList;

import com.gridnine.platform.elsa.admin.list.EntityListFilter;
import com.gridnine.platform.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.InCriterion;
import com.gridnine.platform.elsa.common.core.search.SearchCriterion;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.webApp.common.Option;
import com.gridnine.webpeer.core.ui.BaseUiElement;
import com.gridnine.webpeer.core.ui.OperationUiContext;

import java.util.List;
import java.util.UUID;

public class EntityFilter extends EntityFilterSkeleton implements EntityListFilter {

    private final String fieldId;
    private final Class<?> entityClass;
    private final Storage storage;
    private List<Option> commitedValue;

	public EntityFilter(String fieldId, String title, Class<? extends BaseIdentity> entityClass, Storage storage, OperationUiContext operationUiContext) {
		super("filter-%s".formatted(fieldId), new EntityFilterConfiguration(), operationUiContext);
        this.fieldId = fieldId;
        this.entityClass = entityClass;
        this.storage = storage;
        setValue(List.of(), operationUiContext);
        setLimit(10, operationUiContext);
        setTitle(title, operationUiContext);
        setDebounceTime(300, operationUiContext);
        setGetDataServiceHandler((request, context) -> {
            List<? extends EntityReference<? extends BaseIdentity>> items = storage.searchCaptions(entityClass, request.getQuery(), request.getLimit());
            var result = new EntityFilterGetDataResponse();
            result.getItems().addAll(items.stream().map(it -> {
                var opt = new Option();
                opt.setDisplayName(it.getCaption());
                opt.setId(it.getId().toString());
                return opt;
            }).toList());
            return result;
        });
	}

    @Override
    public BaseUiElement getElement() {
        return this;
    }

    @Override
    public void reset(OperationUiContext context) {
        setValue(List.of(), context);
    }

    @Override
    public void commitValue(OperationUiContext context) {
        commitedValue = getValue();
    }

    @Override
    public void restoreCommitedValue(OperationUiContext context) {
        setValue(commitedValue, context);
    }


    @Override
    public SearchCriterion getSearchCriterion() {
        List<Option> values = getValue();
        if(values.isEmpty()){
            return null;
        }
        return new InCriterion<>(fieldId, values.stream().map(it -> new EntityReference<>(UUID.fromString(it.getId()),
                (Class<BaseIdentity>) entityClass, "")).toList());
    }
}