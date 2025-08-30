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

package com.gridnine.platform.elsa.demo.boot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gridnine.platform.elsa.common.core.boot.ElsaActivator;
import com.gridnine.platform.elsa.common.core.model.domain.EntityReference;
import com.gridnine.platform.elsa.common.core.search.SearchQueryBuilder;
import com.gridnine.platform.elsa.core.storage.Storage;
import com.gridnine.platform.elsa.demo.domain.Country;
import com.gridnine.platform.elsa.demo.domain.CountryFields;
import com.gridnine.platform.elsa.demo.domain.Manager;
import com.gridnine.platform.elsa.demo.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleSiteActivator implements ElsaActivator {
    @Autowired
    private Storage storage;

    @Override
    public void activate() throws Exception {
        if(storage.searchAssets(Country.class, new SearchQueryBuilder().limit(1).build()).isEmpty()){
            {
                var country = new Country();
                country.setName("Russia");
                storage.saveAsset(country, "init");
            }
            {
                var country = new Country();
                country.setName("Japan");
                storage.saveAsset(country, "init");
            }
        }
        if(storage.searchAssets(Organization.class, new SearchQueryBuilder().limit(1).build()).isEmpty()){
            var ruCountry = storage.findUniqueAsset(Country.class, CountryFields.name, "Russia", false).toReference();
            var japCountry = storage.findUniqueAsset(Country.class, CountryFields.name, "Japan", false).toReference();
            var words = new ArrayList<String>();
            try (var is = getClass().getClassLoader().getResourceAsStream("words.json")) {
                var isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                var obj = new Gson().fromJson(isr, JsonObject.class);
                var size = obj.size();
                var idx = 9 + Math.round(Math.random() * (size - 10));
                var arr = obj.getAsJsonArray(String.valueOf(idx));
                arr.forEach(it -> words.add(it.getAsString()));
            }
            generateSampleData(ruCountry, japCountry, words);
        }
    }

    private void generateSampleData(EntityReference<Country> ruCountry, EntityReference<Country> japCountry, List<String> words) {
        int dn = 1;
        for (int n = 0; n < 20; n++) {
            var org = new Organization();
            org.setName(getRandomString(words));
            org.setContacts(getRandomString(words));
            org.setCountry(getRandomBoolean()? ruCountry: japCountry);
            org.setAddress(getRandomString(words));
            storage.saveAsset(org, false, "test data");
            for (int m = 0; m < 20; m++) {
                var manager = new Manager();
                manager.setName(getRandomString(words));
                manager.setContacts(getRandomString(words));
                manager.setEmail(getRandomString(words));
                manager.setOrganization(org.toReference());
                storage.saveAsset(manager, false, "test data");
            }
        }
    }

    private <E extends Enum<E>> E getRandomEnum(Class<E> documentFormatClass) {
        var ordinal = (int) Math.round(Math.random() * (documentFormatClass.getEnumConstants().length - 1));
        return documentFormatClass.getEnumConstants()[ordinal];
    }

    private boolean getRandomBoolean() {
        return Math.random() > 0.5;
    }

    private String getRandomString(List<String> words) {
        return words.get((int) Math.round(Math.random() * (words.size() - 1)));
    }

    @Override
    public double getPriority() {
        return 0;
    }
}
