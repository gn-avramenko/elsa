package com.gridnine.elsa.common.meta.domain;

public class SearchableProjectionDescription extends BaseSearchableDescription {

    private String document;

    public SearchableProjectionDescription() {
    }

    public SearchableProjectionDescription(String id) {
        super(id);
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
