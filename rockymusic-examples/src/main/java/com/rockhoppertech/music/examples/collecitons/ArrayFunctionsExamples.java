package com.rockhoppertech.music.examples.collecitons;

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

import com.rockhoppertech.collections.ArrayFunctions;

public class ArrayFunctionsExamples {
    
    public static void main(String[] args) {

        // ragged initialized
        int[][] rag = { { 1, 2 }, { 3, 4, 5 }, { 6 } };
        ArrayFunctions.printArray(rag);

        // dynamically create the rows -- here it is ragged
        int[][] dynamic = new int[5][];
        for (int i = 0; i < dynamic.length; i++) {
            dynamic[i] = new int[i * 2];
        }
        ArrayFunctions.printArray(dynamic);

        // arrays of object references
        ArrayFunctions[] orefs = new ArrayFunctions[5];

        // they are not pointing anywhere
        System.out.println("printarrays orefs should be null");
        ArrayFunctions.printArray(orefs);

        // now they are
        for (int i = 0; i < orefs.length; i++) {
            orefs[i] = new ArrayFunctions();
        }
        System.out.println("printarrays orefs should not be null");
        ArrayFunctions.printArray(orefs);

        // anonymous arrays of each primitive type
        ArrayFunctions.printArray(new int[] { 4, 5, 6 });
        ArrayFunctions.printArray(new short[] { 4, 5, 6 });
        ArrayFunctions.printArray(new long[] { 4, 5, 6 });
        ArrayFunctions.printArray(new double[] { 4.0, 5.0, 6.0 });
        ArrayFunctions.printArray(new float[] { 4.0f, 5.0f, 6.0f });
        ArrayFunctions.printArray(new byte[] { 4, 5, 6 });
        ArrayFunctions.printArray(new char[] { '4', '5', '6' });
        ArrayFunctions.printArray(new boolean[] { true, false, true, true });

        ArrayFunctions.printArray(new int[][] { { 4, 5 }, { 6 }, { 7, 8 } });
        ArrayFunctions.printArray(new int[][][] { { { 4, 5 }, { 1 } },
                { { 2 }, { 3, 4 } } });
    }
}
