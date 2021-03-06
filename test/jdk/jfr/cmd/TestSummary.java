/*
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package jdk.jfr.cmd;

import java.nio.file.Path;

import jdk.jfr.EventType;
import jdk.jfr.consumer.RecordingFile;
import jdk.testlibrary.OutputAnalyzer;
import jdk.testlibrary.jfr.ExecuteHelper;

/*
 * @test
 * @summary Test jfr info
 * @key jfr
 * @library /lib/testlibrary
 * @run main/othervm jdk.jfr.cmd.TestSummary
 */
public class TestSummary {

    public static void main(String[] args) throws Exception {
        Path f = ExecuteHelper.createProfilingRecording().toAbsolutePath();
        String file = f.toAbsolutePath().toString();

        OutputAnalyzer output = ExecuteHelper.run("summary");
        output.shouldContain("Missing file");

        output = ExecuteHelper.run("summary", "--wrongOption", file);
        output.shouldContain("Too many arguments");

        output = ExecuteHelper.run("summary", file);
        try (RecordingFile rf = new RecordingFile(f)) {
            for (EventType t : rf.readEventTypes()) {
                output.shouldContain(t.getName());
            }
        }
        output.shouldContain("Version");
    }
}
