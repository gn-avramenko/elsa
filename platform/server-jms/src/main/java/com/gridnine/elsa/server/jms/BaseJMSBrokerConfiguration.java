/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

public class BaseJMSBrokerConfiguration<E extends BaseJMSBrokerConfiguration<E>> {

    private String id;

    private boolean participatesInDistributedTransactions;

    @SuppressWarnings("unchecked")
    public E setId(final String value) {
        id = value;
        return (E) this;
    }

    public String getId() {
        return id;
    }

    public boolean isParticipatesInDistributedTransactions() {
        return participatesInDistributedTransactions;
    }

    public E setParticipatesInDistributedTransactions(boolean participatesInDistributedTransactions) {
        this.participatesInDistributedTransactions = participatesInDistributedTransactions;
        return (E) this;
    }
}
