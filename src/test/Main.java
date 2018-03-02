package test;

import java.io.*;

class Main {

	public static void main(String [] args) throws IOException {

		System.out.println("Starting tests");

		Test test = new Test();
		test.run();

		System.out.println("Tests ended.  Use Ctrl-C to stop the internal server instance and exit.");
	}

}
