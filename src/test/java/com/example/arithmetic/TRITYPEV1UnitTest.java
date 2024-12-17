package com.example.arithmetic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.example.cov.TritypeCorrect;
import com.example.cov.Tritypev1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class TRITYPEV1UnitTest {

    private final int i;
    private final int j;
    private final int k;

	private static int cpt;
	public static String[] testCasesNames = new String[125];
	
    public TRITYPEV1UnitTest(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }
    
    @Parameters
    public static Collection<Object[]> data() {
    	cpt=0;
    	for (int i = 0; i < testCasesNames.length; i++) {
            testCasesNames[i] = "TestCase_" + (i + 1); // Example naming: TestCase_1, TestCase_2, ...
        }
    	List<Object[]> data = new ArrayList<>();
        for (int i = 5; i < 10 ; i++) {
        	for (int j = 5; j < 10 ; j++) {
        		for (int k = 5; k < 10 ; k++) {
        			// Create a combination of 3 numbers
                    Object[] combination = new Object[] {i, j, k};
                    // Add the combination to the list
                    data.add(combination);
        		}
    		}
		}
        
        return data;
    }

    @Test
    public void testTRITYPE() {
    	//ExecutionDataCollector.setCurrentTestCase(testCasesNames[cpt]);
    	System.out.println(testCasesNames[cpt]);
    	System.out.println(i+","+ j+","+k);
    	
        Tritypev1 tritype = new Tritypev1();
        int res = tritype.tritype(i,j,k);
        int expectedRes = TritypeCorrect.tritype(i, j, k);
        
        
        //TestCaseProcessor.processTestCase(testCasesNames[cpt], "Tritypev1.java", "CPLEX", i, j, k, expectedRes, res);

        cpt++;
        assertEquals(expectedRes, res);
    }
}
