package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

import com.rockhoppertech.music.PatternFactory;

import static javax.swing.JOptionPane.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class PatternFactoryExample {
    
    public static void main(String[] args) {

        String s = (String) JOptionPane.showInputDialog(null, "Limit",
                "Chooser", QUESTION_MESSAGE);
        int limit = 7;
        if (s != null) {
            limit = Integer.parseInt(s);
        }

        Integer[] nums = new Integer[limit - 1];
        for (int i = 2; i < nums.length + 2; i++) {
            nums[i - 2] = i;
        }
        Integer size = (Integer) showInputDialog(null, "Pattern size",
                "Chooser", QUESTION_MESSAGE, null, nums, nums[0]);

        int w = JOptionPane.showConfirmDialog(null,
                "Weed Repeats?", "choose one", JOptionPane.YES_NO_OPTION);

        List<int[]> list = null;
        list = PatternFactory.getPatterns(limit, size, w == YES_OPTION);
        printArray(list);
    }

    /**
     * @param patterns
     */
    private static void printArray(List<int[]> patterns) {
        int num = 0;
        for (int[] a : patterns) {
            System.out.printf("%d:\t%s%n", num++, ArrayUtils.toString(a));
        }
    }
}
