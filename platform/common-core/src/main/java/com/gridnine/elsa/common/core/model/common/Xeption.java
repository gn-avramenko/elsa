/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.model.common;

public class Xeption extends Error{
    private XeptionType type;
    private String developerMessage;
    private L10nMessage adminMessage;
    private L10nMessage endUserMessage;

    public Xeption(String message, Throwable cause, XeptionType type, String developerMessage, L10nMessage adminMessage, L10nMessage endUserMessage) {
        super(message, cause);
        this.type = type;
        this.developerMessage = developerMessage;
        this.adminMessage = adminMessage;
        this.endUserMessage = endUserMessage;
    }

    public static Xeption forDeveloper(String message, Exception cause){
        return new Xeption(message, cause, XeptionType.FOR_DEVELOPER, message, null, null);
    }

    public XeptionType getType() {
        return type;
    }

    public void setType(XeptionType type) {
        this.type = type;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public L10nMessage getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(L10nMessage adminMessage) {
        this.adminMessage = adminMessage;
    }

    public L10nMessage getEndUserMessage() {
        return endUserMessage;
    }

    public void setEndUserMessage(L10nMessage endUserMessage) {
        this.endUserMessage = endUserMessage;
    }
}
