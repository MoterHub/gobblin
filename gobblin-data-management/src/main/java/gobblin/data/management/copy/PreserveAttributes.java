/*
 * Copyright (C) 2014-2015 LinkedIn Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.data.management.copy;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import com.google.common.base.Strings;


/**
* Configuration for preserving attributes in Gobblin distcp jobs.
*/
@AllArgsConstructor
@EqualsAndHashCode
public class PreserveAttributes {

  /**
   * Attributes that can be preserved.
   */
  public static enum Option {
    REPLICATION('r'),
    BLOCK_SIZE('b'),
    OWNER('u'),
    GROUP('g'),
    PERMISSION('p');

    private final char token;

    Option(char token) {
      this.token = token;
    }
  }

  private int options;

  /**
   * @return true if attribute should be preserved.
   */
  public boolean preserve(Option option) {
    return 0 < (this.options & (1 << option.ordinal()));
  }

  /**
   * Converts this instance of {@link PreserveAttributes} into a String that can be converted to an equivalent
   * {@link PreserveAttributes} using {@link PreserveAttributes#fromMnemonicString}. See the latter for more
   * information.
   * @return a String that can be converted to an equivalent {@link PreserveAttributes} using
   *         {@link PreserveAttributes#fromMnemonicString}
   */
  public String toMnemonicString() {
    int value = options;
    StringBuilder mnemonicString = new StringBuilder();
    for(Option option : Option.values()) {
      if(value % 2  == 1) {
        mnemonicString.append(option.token);
      }
      value >>= 1;
    }
    return mnemonicString.toString();
  }

  /**
   * Parse {@link PreserveAttributes} from a string of the form \[rbugp]*\:
   * * r -> preserve replication
   * * b -> preserve block size
   * * u -> preserve owner
   * * g -> preserve group
   * * p -> preserve permissions
   * Characters not in this character set will be ignored.
   *
   * @param s String of the form \[rbugp]*\
   * @return Parsed {@link PreserveAttributes}
   */
  public static PreserveAttributes fromMnemonicString(String s) {
    if (Strings.isNullOrEmpty(s)) {
      return new PreserveAttributes(0);
    }
    int value = 0;
    for (Option option : Option.values()) {
      if (s.indexOf(option.token) >= 0) {
        value |= 1 << option.ordinal();
      }
    }
    return new PreserveAttributes(value);
  }
}
