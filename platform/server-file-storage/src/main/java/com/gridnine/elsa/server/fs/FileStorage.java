/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.fs;

import com.atomikos.datasource.ResourceException;
import com.atomikos.datasource.xa.XATransactionalResource;
import com.atomikos.icatch.config.Configuration;
import com.atomikos.icatch.jta.TransactionManagerImp;
import com.gridnine.elsa.common.model.common.CallableWithExceptionAndArgument;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Disposable;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import org.xadisk.additional.XAFileInputStreamWrapper;
import org.xadisk.bridge.proxies.interfaces.XAFileOutputStream;
import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.bridge.proxies.interfaces.XAFileSystemProxy;
import org.xadisk.bridge.proxies.interfaces.XASession;
import org.xadisk.filesystem.exceptions.DirectoryNotEmptyException;
import org.xadisk.filesystem.exceptions.FileNotExistsException;
import org.xadisk.filesystem.exceptions.FileUnderUseException;
import org.xadisk.filesystem.exceptions.InsufficientPermissionOnFileException;
import org.xadisk.filesystem.exceptions.LockingFailedException;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;
import org.xadisk.filesystem.standalone.StandaloneFileSystemConfiguration;

import javax.transaction.xa.XAResource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileStorage implements Disposable {

    public static FileStorage get(){
        return Environment.getPublished(FileStorage.class);
    }
    private XAFileSystem xafs;

    public  FileStorage() throws Exception {
        var configuration = new StandaloneFileSystemConfiguration(new File("temp/xa-disk").getAbsolutePath(), "1");
        xafs = XAFileSystemProxy.bootNativeXAFileSystem(configuration);
        xafs.waitForBootup(-1);
        Configuration.addResource(new XATransactionalResource("file-storage") {
            @Override
            protected XAResource refreshXAConnection() throws ResourceException {
                XAResource xarXADisk = (XAResource) TransactionManager.get().getCurrentContext().getAttributes().get("xa-disk-resource");
                if(xarXADisk == null){
                    XASession xaSession = xafs.createSessionForXATransaction();
                    xarXADisk = xaSession.getXAResource();
                    TransactionManager.get().getCurrentContext().getAttributes().put("xa-disk-resource", xarXADisk);
                    TransactionManager.get().getCurrentContext().getAttributes().put("xa-disk-session", xaSession);
                }
                return xarXADisk;
            }
        });
    }

    public void write(File file, InputStream is) {
        withTransaction((session) -> {
            if(!session.fileExists(file)){
                session.createFile(file, false);
            }
            var os = session.createXAFileOutputStream(file, true);
            copyStream(is, os, standardInputStreamHanlder, xaFileOutputStreamHandler);
            return null;
        });
    }

    public void delete(File file) {
        withTransaction((session) -> {
            deleteFileInternal(session, file);
            session.deleteFile(file);
            return null;
        });
    }

    public void read(File file, RunnableWithExceptionAndArgument<InputStream> handler){
        withTransaction((session) ->{
            try(InputStream is = new XAFileInputStreamWrapper(session.createXAFileInputStream(file))){
                handler.run(is);
            }
            return null;
        });
    }

    private void deleteFileInternal(XASession session, File file) throws LockingFailedException, NoTransactionAssociatedException, FileNotExistsException, DirectoryNotEmptyException, InterruptedException, FileUnderUseException, InsufficientPermissionOnFileException {
        if(file.isDirectory()){
            for(File item: file.listFiles()){
                deleteFileInternal(session, item);
            }
        }
        session.deleteFile(file);
    }

    private <IS, OS> void copyStream(IS is, OS os, InputStreamHandler<IS> ish, OutputStreamHandler<OS> osh) throws Exception {

        try {
            final byte[] buffer = new byte[256];
            int n;
            long count = 0;
            while (-1 != (n = ish.read(is, buffer))) {
                osh.write(os, buffer, 0, n);
                count += n;
            }
            osh.flush(os);
        } finally {
            osh.close(os);
        }
    }

    private <P> P withTransaction(CallableWithExceptionAndArgument<P, XASession> func) {
        return ExceptionUtils.wrapException(() -> {
            XAResource xarXADisk = (XAResource) TransactionManager.get().getCurrentContext().getAttributes().get("xa-disk-resource");
            XASession xaSession = (XASession) TransactionManager.get().getCurrentContext().getAttributes().get("xa-disk-session");
            if(xarXADisk == null){
                xaSession = xafs.createSessionForXATransaction();
                xarXADisk = xaSession.getXAResource();
                TransactionManager.get().getCurrentContext().getAttributes().put("xa-disk-resource", xarXADisk);
                TransactionManager.get().getCurrentContext().getAttributes().put("xa-disk-session", xaSession);
            }
            Objects.requireNonNull(TransactionManagerImp.getTransactionManager ()).getTransaction().enlistResource(xarXADisk);
            return func.call(xaSession);
        });
    }

    @Override
    public void dispose() throws Throwable {
        Configuration.removeResource("file-storage");
        xafs.shutdown();
    }




    interface InputStreamHandler<S> {

        int read(S is, byte[] buffer) throws Exception;

        void close(S is) throws Exception;
    }

    interface OutputStreamHandler<S> {

        void write(S os, byte[] buffer, int start, int length) throws Exception;

        void close(S os) throws Exception;

        void flush(S os) throws Exception;
    }



    private InputStreamHandler<InputStream> standardInputStreamHanlder = new InputStreamHandler<>() {
        @Override
        public int read(InputStream is, byte[] buffer) throws Exception {
            return is.read(buffer);
        }

        @Override
        public void close(InputStream is) throws IOException {
            is.close();
        }
    };
    private OutputStreamHandler<XAFileOutputStream> xaFileOutputStreamHandler = new OutputStreamHandler<>() {
        @Override
        public void write(XAFileOutputStream os, byte[] buffer, int start, int length) throws Exception {
            os.write(buffer, start, length);
        }

        @Override
        public void close(XAFileOutputStream os) throws Exception {
            os.close();
        }

        @Override
        public void flush(XAFileOutputStream os) throws Exception {
            os.flush();
        }
    };
}

