/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.jms;

@SuppressWarnings("unchecked")
public abstract class BaseJMSItemConfiguration<T extends BaseJMSItemConfiguration<T>> {

    private String id;

    private String brokerId;

    private boolean persistent;

    private int timeToLive;

    private boolean participatesInDistributedTransactions;

    public String getId() {
        return id;
    }

	public T setId(final String value) {
        id = value;
		return (T) this;
    }

    public boolean isPersistent() {
        return persistent;
    }

	public T setPersistent(final boolean value) {
        persistent = value;
		return (T) this;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

	public T setTimeToLive(final int value) {
        timeToLive = value;
		return (T) this;
    }

    public String getBrokerId() {
        return brokerId;
    }

	public T setBrokerId(final String value) {
        brokerId = value;
		return (T) this;
    }

    public boolean isParticipatesInDistributedTransactions() {
        return participatesInDistributedTransactions;
    }

    public T setParticipatesInDistributedTransactions(boolean participatesInDistributedTransactions) {
        this.participatesInDistributedTransactions = participatesInDistributedTransactions;
        return (T) this;
    }
}
