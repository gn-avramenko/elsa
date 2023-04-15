/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.server.remoting.RemotingBigFileHandler;
import com.gridnine.elsa.server.remoting.RemotingCallContext;

import java.io.File;

public class DownloadVideoHandler implements RemotingBigFileHandler<Void> {

    @Override
    public File getFile(Void request, RemotingCallContext context) throws Exception {
        return new File("/media/data/share/Гудзонский ястреб/Гудзонский ястреб.mkv");
    }
}
