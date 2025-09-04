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

public class TestOrganizationConfiguration extends TestBaseEditorConfiguration{
    private TestStandardEditorTextFieldConfiguration name;

    private TestStandardEditorTextFieldConfiguration contacts;

    private TestStandardEditorTextFieldConfiguration address;

    private TestAutocompleteFieldConfiguration country;
    public void setName(TestStandardEditorTextFieldConfiguration name) {
        this.name = name;
    }

    public TestStandardEditorTextFieldConfiguration getName() {
        return name;
    }

    public TestStandardEditorTextFieldConfiguration getContacts() {
        return contacts;
    }

    public void setContacts(TestStandardEditorTextFieldConfiguration contacts) {
        this.contacts = contacts;
    }

    public TestStandardEditorTextFieldConfiguration getAddress() {
        return address;
    }

    public void setAddress(TestStandardEditorTextFieldConfiguration address) {
        this.address = address;
    }

    public TestAutocompleteFieldConfiguration getCountry() {
        return country;
    }

    public void setCountry(TestAutocompleteFieldConfiguration country) {
        this.country = country;
    }
}
