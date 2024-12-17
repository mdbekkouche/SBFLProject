package com.example.cov;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import org.junit.Test;

/**
 * Example usage of the JaCoCo core API. In this tutorial a single target class
 * will be instrumented and executed. Finally the coverage information will be
 * dumped.
 */
public final class CoreTutorial2 {

	/**
	 * The test target we want to see code coverage for.
	 */
	public static class TestTarget implements Runnable {

		public void run() {
			isPrime(7);
		}

		private boolean isPrime(final int n) {
			for (int i = 2; i * i <= n; i++) {
				if ((n ^ i) == 0) {
					return false;
				}
			}
			return true;
		}

	}

	/**
	 * A class loader that loads classes from in-memory data.
	 */
	public static class MemoryClassLoader extends ClassLoader {

		private final Map<String, byte[]> definitions = new HashMap<String, byte[]>();

		/**
		 * Add an in-memory representation of a class.
		 *
		 * @param name
		 *            name of the class
		 * @param bytes
		 *            class definition
		 */
		public void addDefinition(final String name, final byte[] bytes) {
			definitions.put(name, bytes);
		}
		
		@Override
		protected Class<?> loadClass(final String name, final boolean resolve)
				throws ClassNotFoundException {
			final byte[] bytes = definitions.get(name);
			if (bytes != null) {
				return defineClass(name, bytes, 0, bytes.length);
			}
			return super.loadClass(name, resolve);
		}

		public byte[] getDefinitions(String className) {
			
			return definitions.get(className);
		}

	}

	private final PrintStream out;

	/**
	 * Creates a new example instance printing to the given stream.
	 *
	 * @param out
	 *            stream for outputs
	 */
	public CoreTutorial2(final PrintStream out) {
		this.out = out;
	}
	
	/**
	 * Run this example.
	 * @param javaTestClass 
	 * @param javaClass 
	 * @param pathJavaProg 
	 *
	 * @throws Exception
	 *             in case of errors
	 */
	public void execute(String pathJavaProg, String javaClass, String javaTestClass) throws Exception {
		
		//final String targetName = TestTarget.class.getName();
		
		//final String targetName = "com.example.cov.Tritypev1";
				
		//final String targetName = "com.example.cov.ArithmeticOperations";
		final String targetName = javaClass;		
		
		final String testName = "com.example.arithmetic.ArithmeticOperationsTest"; // Update with the full name of the test class

		 
		// For instrumentation and runtime we need a IRuntime instance
		// to collect execution data:
		final IRuntime runtime = new LoggerRuntime();

		// The Instrumenter creates a modified version of our test target class
		// that contains additional probes for execution data recording:
		final Instrumenter instr = new Instrumenter(runtime);
		InputStream original = getTargetClass(targetName);
		final byte[] instrumented = instr.instrument(original, targetName);
		original.close();
		
		// Now we're ready to run our instrumented class and need to startup the
		// runtime first:
		final RuntimeData data = new RuntimeData();
		runtime.startup(data);

		// In this tutorial we use a special class loader to directly load the
		// instrumented class definition from a byte[] instances.
		final MemoryClassLoader memoryClassLoader = new MemoryClassLoader();
		memoryClassLoader.addDefinition(targetName, instrumented);
		final Class<?> targetClass = memoryClassLoader.loadClass(targetName);
		
		saveClassToFile(targetClass, memoryClassLoader, "target/classes/");
		
		//System.exit(0);
		
        // Dynamically load test classes
        String testClassesPath = "target/test-classes/";
        //URL[] urls = { new URL("file://" + System.getProperty("user.dir") + "/" + testClassesPath) };
        URL[] urls = {
                new URL("file://" + System.getProperty("user.dir") + "/" + testClassesPath),
                new URL("file://" + /*System.getProperty("user.dir") +*/ "/home/bekkouche/.m2/repository/junit/junit/4.13.2/junit-4.13.2.jar"),
                new URL("file://" + /*System.getProperty("user.dir") +*/ "/home/bekkouche/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar")
            };
        System.out.println(urls[1].getFile());
        ClassLoader testClassLoader = new URLClassLoader(urls, CoreTutorial.class.getClassLoader());
		
        // Load and execute the test class dynamically
        //Class<?> testClass = testClassLoader.loadClass("com.example.arithmetic.ArithmeticOperationsTest");
        Class<?> testClass = testClassLoader.loadClass(javaTestClass);
        
        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        
        for (Method method : testClass.getMethods()) {
        	
        	if (method.isAnnotationPresent(Test.class)) {
	            Method testMethod = testClass.getMethod(method.getName());
	            System.out.println(method.getName());
	            try {
	            	testMethod.invoke(testInstance);
	                System.out.println("Test passed for params: ");
	            } catch (Exception e) {
	                System.err.println("Test failed for params: ");
	                //e.printStackTrace();
	            }
        	}
   		}
        
		// Here we execute our test target class through its Runnable interface:
		//final Runnable targetInstance = (Runnable) targetClass.newInstance();
		//targetInstance.run();

		// At the end of test execution we collect execution data and shutdown
		// the runtime:
		final ExecutionDataStore executionData = new ExecutionDataStore();
		final SessionInfoStore sessionInfos = new SessionInfoStore();
		
		data.collect(executionData, sessionInfos, false);
		runtime.shutdown();
        
		/**************/
		//String javaFilePath = "src/main/java/com/example/cov/ArithmeticOperations.java"; // Path to the .java file
		String javaFilePath = pathJavaProg;
		String outputDirectory = "target/classes/"; // Directory to store compiled .class file

        // Compile the file
        compileJavaFile(javaFilePath, outputDirectory);
        /*************/
		
		// Together with the original class definition we can calculate coverage
		// information:
		final CoverageBuilder coverageBuilder = new CoverageBuilder();
		final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
		
		original = getTargetClass(targetName);
		analyzer.analyzeClass(original, targetName);
		original.close();
				
		// Let's dump some metrics and line coverage information:
		for (final IClassCoverage cc : coverageBuilder.getClasses()) {
			out.printf("Coverage of class %s%n", cc.getName());

			printCounter("instructions", cc.getInstructionCounter());
			printCounter("branches", cc.getBranchCounter());
			printCounter("lines", cc.getLineCounter());
			printCounter("methods", cc.getMethodCounter());
			printCounter("complexity", cc.getComplexityCounter());
			
			for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
				out.printf(cc.getLine(i).getInstructionCounter().toString()); //.getCoveredCount()
				out.printf("Line %s: %s%n", Integer.valueOf(i),
						getColor(cc.getLine(i).getStatus()));
			}
		}
	}

	private InputStream getTargetClass(final String name) {
		final String resource = '/' + name.replace('.', '/') + ".class";
		return getClass().getResourceAsStream(resource);
	}

	private void printCounter(final String unit, final ICounter counter) {
		final Integer missed = Integer.valueOf(counter.getMissedCount());
		final Integer total = Integer.valueOf(counter.getTotalCount());
		out.printf("%s of %s %s missed%n", missed, total, unit);
	}

	private String getColor(final int status) {
		switch (status) {
		case ICounter.NOT_COVERED:
			return "red";
		case ICounter.PARTLY_COVERED:
			return "yellow";
		case ICounter.FULLY_COVERED:
			return "green";
		}
		return "";
	}

	/**
	 * Entry point to run this examples as a Java application.
	 *
	 * @param args
	 *            list of program arguments
	 * @throws Exception
	 *             in case of errors
	 */
	public static void main(final String[] args) throws Exception {
		//new CoreTutorial2(System.out).execute("src/main/java/com/example/cov/ArithmeticOperations.java", "com.example.cov.ArithmeticOperations", "com.example.arithmetic.ArithmeticOperationsTest");
		//new CoreTutorial2(System.out).execute("src/main/java/com/library/service/UserService.java", "com.library.service.UserService", "com.library.service.UserServiceTest");
		new CoreTutorial2(System.out).execute("src/main/java/com/library/service/LibraryService.java", "com.library.service.LibraryService", "com.library.service.LibraryServiceTest");
	}
	
	public static void saveClassToFile(Class<?> targetClass, MemoryClassLoader memoryClassLoader, String outputDir) throws IOException {
        // Get the class name
        String className = targetClass.getName();
        
        // Retrieve the bytecode from the custom class loader
        byte[] classBytes = memoryClassLoader.getDefinitions(className);
        
        if (classBytes == null) {
            throw new IllegalArgumentException("Bytecode for class " + className + " not found in MemoryClassLoader.");
        }

        // Generate the output file path
        Path classFilePath = Paths.get(outputDir, className.replace('.', '/') + ".class");
        Files.createDirectories(classFilePath.getParent()); // Ensure directories exist

        // Write the bytecode to the file
        try (FileOutputStream fos = new FileOutputStream(classFilePath.toFile())) {
            fos.write(classBytes);
        }

        System.out.println("Class file saved to: " + classFilePath);
    }
	
    public static void compileJavaFile(String javaFilePath, String outputDirectory) {
        // Get the Java Compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if (compiler == null) {
            System.err.println("Java Compiler is not available. Make sure you're running with a JDK, not a JRE.");
            return;
        }

        // Prepare the options for the compiler
        String[] options = {
            "-d", outputDirectory // Specify output directory for compiled .class files
        };

        // Compile the file
        int result = compiler.run(null, null, null, options[0], options[1], javaFilePath);

        // Check the result
        if (result == 0) {
            System.out.println("Compilation successful. Output files are in: " + outputDirectory);
        } else {
            System.err.println("Compilation failed.");
        }
    }

}
