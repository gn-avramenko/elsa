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

package com.gridnine.platform.elsa.demo.ui.components.test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gridnine.webpeer.core.utils.GsonSerializable;

public class TestEntityListColumnDescription implements GsonSerializable {
    private String title;
    private String id;
    private TestEntityListColumnType type;
    private String customSubtype;
    private boolean sortable;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TestEntityListColumnType getType() {
        return type;
    }

    public void setType(TestEntityListColumnType type) {
        this.type = type;
    }

    public String getCustomSubtype() {
        return customSubtype;
    }

    public void setCustomSubtype(String customSubtype) {
        this.customSubtype = customSubtype;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @Override
    public JsonElement serialize() throws Exception {
        var result = new JsonObject();
        result.addProperty("title", title);
        result.addProperty("id", id);
        result.addProperty("type", type.name());
        result.addProperty("customSubtype", customSubtype);
        result.addProperty("sortable", sortable);
        return result;
    }
}
