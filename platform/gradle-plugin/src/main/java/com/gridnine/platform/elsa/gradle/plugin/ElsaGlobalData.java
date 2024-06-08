/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.gradle.plugin;

import com.gridnine.platform.elsa.gradle.codegen.domain.JavaDomainCodeGenRecord;
import com.gridnine.platform.elsa.gradle.codegen.remoting.JavaRemotingCodeGenRecord;
import com.gridnine.platform.elsa.gradle.codegen.remoting.OpenApiCodeGenRecord;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElsaGlobalData {
    private final Map<String, OpenApiCodeGenRecord> openApiRecords;

    private final Map<String, JavaDomainCodeGenRecord> domainRecords;

    private final Map<String, List<JavaRemotingCodeGenRecord>> remotingRecords;

    private JavaDomainCodeGenRecord currentDomainRecord;

    private JavaRemotingCodeGenRecord currentRemotingRecord;

    public ElsaGlobalData(Map<String, Object> globalMap) {
        Map<String, OpenApiCodeGenRecord> oar = (Map<String, OpenApiCodeGenRecord>) globalMap.get("open-api");
        if(oar == null){
            oar = new LinkedHashMap<>();
            globalMap.put("open-api", oar);
        }
        this.openApiRecords = oar;

        Map<String, JavaDomainCodeGenRecord> dr = (Map<String, JavaDomainCodeGenRecord>) globalMap.get("domain");
        if(dr == null){
            dr = new LinkedHashMap<>();
            globalMap.put("domain", dr);
        }
        this.domainRecords = dr;

        Map<String, List<JavaRemotingCodeGenRecord>> rr = (Map<String, List<JavaRemotingCodeGenRecord>>) globalMap.get("remoting");
        if(rr == null){
            rr = new LinkedHashMap<>();
            globalMap.put("remoting", rr);
        }
        this.remotingRecords = rr;
    }


    public Map<String, OpenApiCodeGenRecord> getOpenApiRecords() {
        return openApiRecords;
    }

    public Map<String, JavaDomainCodeGenRecord> getDomainRecords() {
        return domainRecords;
    }

    public Map<String, List<JavaRemotingCodeGenRecord>> getRemotingRecords() {
        return remotingRecords;
    }

    public void setCurrentDomainRecord(JavaDomainCodeGenRecord currentDomainRecord) {
        this.currentDomainRecord = currentDomainRecord;
    }

    public JavaDomainCodeGenRecord getCurrentDomainRecord() {
        return currentDomainRecord;
    }

    public void setCurrentRemotingRecord(JavaRemotingCodeGenRecord currentRemotingRecord) {
        this.currentRemotingRecord = currentRemotingRecord;
    }

    public JavaRemotingCodeGenRecord getCurrentRemotingRecord() {
        return currentRemotingRecord;
    }
}
