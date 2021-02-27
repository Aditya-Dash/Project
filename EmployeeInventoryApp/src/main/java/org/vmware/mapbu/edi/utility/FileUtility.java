/**
 * Employee Data Inventory Application - Utility functions used in the application
 * @author Aditya Ranjan Dash
 *
 */

package org.vmware.mapbu.edi.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUtility {

	public static void generateTestDataFile(String path, int number) throws IOException {
		File file = new File(path + "\\" + "TestData.txt");
		if (!file.exists()) {
			file.createNewFile();
		}

		int max = 80;
		int min = 18;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));) {
			int count = 0;
			
			do {
				int age = (int) ((Math.random() * (max - min)) + min);
				bw.write("Aditya" + count + " " + age + "\n");
				count++;
			} while (count < number);
		}
		System.out.println("Done");
	}

	public static void main(String[] args) throws IOException {
		//if(args.length < 2)
			//System.out.println("Usage java -jar <jarfilename>.jar org.vmware.mapbu.edi.utility.FileUtility G:\\ 1");
		//generateTestDataFile(args[0], args[1]);
		generateTestDataFile("G:\\", 10);
	}

	public static String generateUniqueTaskId() {
		return UUID.randomUUID().toString();
	}

	public static int tempIdGenerator() {
		AtomicInteger seq = new AtomicInteger();
		int nextVal = seq.incrementAndGet();
		return nextVal;
	}
}
