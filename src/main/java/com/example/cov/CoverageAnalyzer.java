package com.example.cov;

import org.jacoco.core.*;
import org.jacoco.core.analysis.*;
import org.jacoco.core.tools.ExecFileLoader;

import java.util.List;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionData;

import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.analysis.IMethodCoverage;

import java.io.File;
import java.io.IOException;

public class CoverageAnalyzer {
    
    public static void main(String[] args) throws IOException {
        // Path to the JaCoCo execution data file generated after tests run
        String execFilePath = "/home/bekkouche/Documents/Research/FaultLocalizationPFEProject/ArithmeticProject/target/jacoco.exec";  // Adjust this if needed
        ExecFileLoader loader = new ExecFileLoader();
        loader.load(new File(execFilePath));
        
        // Iterate through the execution data and extract coverage details
        for (ExecutionData data : loader.getExecutionDataStore().getContents()) {
            System.out.println("Class: " + data);
        }
    }
}