package com.example.cov;


import java.net.URL;
import java.net.URLClassLoader;
public class Test {
	
	public static void main(String[] args) {
        try {
            // Dynamically load test classes
            String testClassesPath = "target/test-classes/";
            URL[] urls = { new URL("file://" + System.getProperty("user.dir") + "/" + testClassesPath) };
            ClassLoader testClassLoader = new URLClassLoader(urls, CoreTutorial.class.getClassLoader());

            // Load TRITYPEV1UnitTest
            Class<?> testClass = testClassLoader.loadClass("com.example.arithmetic.TRITYPEV1UnitTest");
            System.out.println("Loaded class: " + testClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
