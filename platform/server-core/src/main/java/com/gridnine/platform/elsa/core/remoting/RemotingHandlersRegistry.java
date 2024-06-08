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

package com.gridnine.platform.elsa.core.remoting;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class RemotingHandlersRegistry {

    private final Map<String, RestHandler<?, ?>> serviceHandlersMap = new HashMap<>();

    private final Map<String, SubscriptionHandler<?, ?>> subscriptionHandlersMap = new HashMap<>();

    private final List<RemotingAdvice> advices = new ArrayList<>();

    public List<RemotingAdvice> getAdvices() {
        return advices;
    }

    @Autowired(required = false)
    private void setHandlers(List<RestHandler<?, ?>> handlers) {
        handlers.forEach(h -> serviceHandlersMap.put(h.getId(), h));
    }

    @Autowired(required = false)
    private void setSubscriptionHandlers(List<SubscriptionHandler<?, ?>> handlers) {
        handlers.forEach(h -> subscriptionHandlersMap.put(h.getId(), h));
    }

    @Autowired(required = false)
    private void setAdvices(List<RemotingAdvice> advices) {
        this.advices.clear();
        this.advices.addAll(advices);
        this.advices.sort(Comparator.comparingDouble(RemotingAdvice::getPriority));
    }

    @SuppressWarnings("unchecked")
    public <RQ, RS> RestHandler<RQ, RS> getServiceHandler(String id) {
        return (RestHandler<RQ, RS>) serviceHandlersMap.get(id);
    }

    @SuppressWarnings("unchecked")
    public <RP, RE> SubscriptionHandler<RP, RE> getSubscriptionHandler(String id) {
        return (SubscriptionHandler<RP, RE>) subscriptionHandlersMap.get(id);
    }
}
