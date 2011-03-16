/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.caliper.runner;

import com.google.caliper.util.HelpRequestedException;
import com.google.caliper.util.InvalidCommandException;
import com.google.common.base.Objects;

import java.io.File;
import java.io.PrintWriter;

/**
 * Primary entry point for the caliper benchmark runner application; run with {@code --help} for
 * details.
 */
public final class CaliperMain {
  /**
   * Primary entry point for the caliper benchmark runner application; run with {@code --help} for
   * details. This method is not intended to be invoked programmatically.
   */
  public static void main(String[] args) {
    PrintWriter writer = new PrintWriter(System.out);
    try {
      exitlessMain(args);
      System.exit(0);

    } catch (InvalidCommandException e) {
      writer.println(e.getMessage());
      writer.println();
      ParsedOptions.printUsage(writer);

    } catch (UserCodeException e) {
      e.printStackTrace(writer);

    } catch (InvalidBenchmarkException e) {
      writer.println(e.getMessage());
    }
    System.exit(1);
  }

  public static void exitlessMain(String[] args)
      throws InvalidCommandException, InvalidBenchmarkException {
    PrintWriter writer = new PrintWriter(System.out);

    File rcFile = new File(Objects.firstNonNull(
          System.getenv("CALIPERRC"),
          System.getProperty("user.home") + "/.caliperrc"));
    try {
      new CaliperRun(writer, rcFile, args).execute();
    } catch (HelpRequestedException e) {
      ParsedOptions.printUsage(writer);
    }
  }
}
