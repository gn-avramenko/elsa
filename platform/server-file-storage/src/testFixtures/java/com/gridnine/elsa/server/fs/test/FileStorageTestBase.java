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

import com.gridnine.elsa.common.config.Activator;
import com.gridnine.elsa.server.atomikos.ServerAtomikosActivator;
import com.gridnine.elsa.server.atomikos.test.AtomikosTestBase;
import com.gridnine.elsa.server.fs.ServerFileStorageActivator;

import java.io.File;
import java.util.List;

public abstract class FileStorageTestBase extends AtomikosTestBase {

    @Override
    protected void setUp() throws Exception {
        System.setProperty("com.atomikos.icatch.file", System.getProperty("com.atomikos.icatch.file", new File("src/testFixtures/java/com/gridnine/elsa/server/fs/test/jta.properties").getAbsolutePath()));
        System.setProperty("atomikos-log-base-dir", System.getProperty("atomikos-log-base-dir", new File("temp/atomikos").getAbsolutePath()));
        super.setUp();
    }

    @Override
    protected void registerActivators(List<Activator> activators) {
        super.registerActivators(activators);
        activators.add(new ServerFileStorageActivator());
    }
}
