/**
 * 
 */
package com.rockhoppertech.music.cmd;

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

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class CommandLine {
	private static final Logger logger = LoggerFactory
			.getLogger(CommandLine.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String cmd = scanner.nextLine();
			logger.debug("cmd '{}'", cmd);
			if (cmd.startsWith("q")) {
				logger.debug("quitting '{}'", cmd);
				System.exit(0);
			}
			
			if (cmd.startsWith("play")) {
				
				
			}
		}
		scanner.close();

	}

}
