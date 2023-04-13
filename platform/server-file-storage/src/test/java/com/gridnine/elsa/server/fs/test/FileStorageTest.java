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

package com.gridnine.elsa.server.fs.test;

import com.gridnine.elsa.common.search.SearchQuery;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocument;
import com.gridnine.elsa.common.test.model.domain.TestDomainDocumentProjection;
import com.gridnine.elsa.common.utils.IoUtils;
import com.gridnine.elsa.server.auth.AuthContext;
import com.gridnine.elsa.server.fs.FileStorage;
import com.gridnine.elsa.server.storage.Storage;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FileStorageTest extends FileStorageTestBase {

    @Test
    public void testConcurrency() throws ExecutionException, InterruptedException {
        AuthContext.setCurrentUser("system");
        var file = new File("temp/xadisk/");
        if(file.exists()){
            IoUtils.deleteDirectory(file);
        }
        file.mkdirs();
        var executor = Executors.newFixedThreadPool(10);
        var results = new ArrayList<Future<Void>>();
        for(int n =0;n < 10; n++){
            File testFile = new File(file, "test-%s.txt".formatted(n));
            results.add(executor.submit(() ->{
                TransactionManager.get().withTransaction((tc) -> {
                    var doc = new TestDomainDocument();
                    doc.setStringProperty("test");
                    Storage.get().saveDocument(doc, "version1");
                    FileStorage.get().write(testFile, new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
                });
                return null;
            }));
        }
        for(Future<Void> res: results){
            res.get();
        }
        for(int n =0;n < 10; n++){
            File testFile = new File(file, "test-%s.txt".formatted(n));
            Assertions.assertTrue(testFile.exists());
        }
    }
    @Test
    public void testTransaction() {
        AuthContext.setCurrentUser("system");
        var file = new File("temp/xadisk/");
        if(file.exists()){
            IoUtils.deleteDirectory(file);
        }
        file.mkdirs();
        File testFile = new File(file, "test.txt");
        var docsSize = Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size();

        try {
            TransactionManager.get().withTransaction((tc) -> {
                var doc = new TestDomainDocument();
                doc.setStringProperty("test");
                Storage.get().saveDocument(doc, "version1");
                FileStorage.get().write(testFile, new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
                throw new Exception("test");
            });
        } catch (Throwable e) {
            Assertions.assertEquals(docsSize, Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size());
            Assertions.assertFalse(testFile.exists());
        }
        TransactionManager.get().withTransaction((tc) -> {
            var doc = new TestDomainDocument();
            doc.setStringProperty("test");
            Storage.get().saveDocument(doc, "version1");
            FileStorage.get().write(testFile, new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8)));
        });
        Assertions.assertEquals(docsSize + 1, Storage.get().searchDocuments(TestDomainDocumentProjection.class, new SearchQuery()).size());
        Assertions.assertTrue(testFile.exists());
    }

}
