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

package com.gridnine.platform.elsa.common.core.search;

public class SearchQueryBuilder extends BaseQueryBuilder<SearchQueryBuilder, SearchQuery> {

    public SearchQueryBuilder() {
        super(new SearchQuery());
    }

    public SearchQueryBuilder limit(int limit) {
        query.setLimit(limit);
        return this;
    }

    public SearchQueryBuilder offset(int offset) {
        query.setOffset(offset);
        return this;
    }

    public <T extends FieldNameSupport & SortSupport> SearchQueryBuilder orderBy(T property, SortOrder order) {
        query.getOrders().clear();
        return addOrder(property, order);
    }

    public  SearchQueryBuilder orderBy(String property, SortOrder order) {
        query.getOrders().put(property, order);
        return this;
    }

    public <T extends FieldNameSupport & SortSupport> SearchQueryBuilder addOrder(T property, SortOrder order) {
        query.getOrders().put(property.name, order);
        return this;
    }

    public SearchQueryBuilder preferredFields(FieldNameSupport... fields) {
        query.getPreferredFields().clear();
        for (FieldNameSupport field : fields) {
            query.getPreferredFields().add(field.name);
        }
        return this;
    }

}
