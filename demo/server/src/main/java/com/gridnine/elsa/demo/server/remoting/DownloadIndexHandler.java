/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.demo.model.remoting.DownloadIndexRequest;
import com.gridnine.elsa.server.remoting.ContentTypes;
import com.gridnine.elsa.server.remoting.DownloadableResourceWrapper;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingDownloadHandler;

import java.io.File;
import java.io.FileInputStream;

public class DownloadIndexHandler implements RemotingDownloadHandler<DownloadIndexRequest> {

    @Override
    public DownloadableResourceWrapper createResource(DownloadIndexRequest request, RemotingCallContext context) throws Exception {
        var result =new DownloadableResourceWrapper();
        var file = new File("data/index.html");
        result.setContentLength(file.length());
        result.setContentType(ContentTypes.HTML);
        result.setFileName("index.html");
        result.setInputStream(new FileInputStream(file));
        return result;
    }
}
