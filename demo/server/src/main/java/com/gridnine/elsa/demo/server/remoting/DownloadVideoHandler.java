/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.server.remoting;

import com.gridnine.elsa.server.remoting.ContentTypes;
import com.gridnine.elsa.server.remoting.DownloadableResourceWrapper;
import com.gridnine.elsa.server.remoting.RemotingCallContext;
import com.gridnine.elsa.server.remoting.RemotingDownloadHandler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

public class DownloadVideoHandler implements RemotingDownloadHandler<Void> {

    @Override
    public DownloadableResourceWrapper createResource(Void request, RemotingCallContext context) throws Exception {
        var result =new DownloadableResourceWrapper();
        var file = new File("/media/data/share/Гудзонский ястреб/Гудзонский ястреб.mkv");
        result.setContentLength(file.length());
        result.setContentType(ContentTypes.VIDEO_MP4);
        result.setFileName("Гудзонский ястреб.mkv");
        result.setFile(file);
        return result;
    }
}
