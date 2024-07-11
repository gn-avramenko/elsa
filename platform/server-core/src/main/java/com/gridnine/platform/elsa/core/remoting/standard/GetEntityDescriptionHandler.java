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

package com.gridnine.platform.elsa.core.remoting.standard;

import com.gridnine.platform.elsa.common.core.serialization.meta.ObjectMetadataProvidersFactory;
import com.gridnine.platform.elsa.common.core.serialization.meta.SerializablePropertyType;
import com.gridnine.platform.elsa.common.rest.core.*;
import com.gridnine.platform.elsa.core.remoting.RemotingCallContext;
import com.gridnine.platform.elsa.core.remoting.RestHandler;
import com.gridnine.platform.elsa.server.core.CoreRemotingConstants;
import org.springframework.beans.factory.annotation.Autowired;

public class GetEntityDescriptionHandler implements RestHandler<GetEntityDescriptionRequest, REntityDescription> {

    @Autowired
    private ObjectMetadataProvidersFactory providersFactory;

    @Override
    public String getId() {
        return CoreRemotingConstants.CORE_META_GET_ENTITY_DESCRIPTION;
    }

    @Override
    public REntityDescription service(GetEntityDescriptionRequest request, RemotingCallContext context) {
        var provider = providersFactory.getProvider(request.getEntityId());
        var ed = new REntityDescription();
        ed.setIsAbstract(provider.isAbstract());
        provider.getAllProperties().forEach(prop -> {
            var pd = new REntityPropertyDescription();
            pd.setId(prop.id());
            pd.setClassName(prop.className());
            pd.setType(toType(prop.type()));
            ed.getProperties().add(pd);
        });
        provider.getAllCollections().forEach(coll -> {
            var cd = new REntityCollectionDescription();
            cd.setId(coll.id());
            cd.setElementClassName(coll.elementClassName());
            cd.setElementType(toType(coll.elementType()));
            ed.getCollections().add(cd);
        });
        provider.getAllMaps().forEach(map -> {
            var md = new REntityMapDescription();
            md.setId(map.id());
            md.setKeyClassName(map.keyClassName());
            md.setKeyType(toType(map.keyType()));
            md.setValueClassName(map.valueClassName());
            md.setValueType(toType(map.valueType()));
            ed.getMaps().add(md);
        });
        return ed;
    }

    private RStandardValueType toType(SerializablePropertyType type) {
        return RStandardValueType.valueOf(type.name());
    }

}
