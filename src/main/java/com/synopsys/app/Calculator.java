

package com.synopsys.app;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;

public class Calculator {
	
	final static Logger logger = Logger.getLogger(Calculator.class);
	public static Map<String, LinkedList<Integer>> variableMap;
	
	/* Constructor*/
	public Calculator() {
		Calculator.variableMap = new HashMap<String, LinkedList<Integer>>();
	}
	
	/* Removing unnecessary spaces from the input string */
	public String getCorrectInput(String str){
		return str.replaceAll("\\s", "");
	}
	
	/* Main Driver method */
	public static void main(String[] args) {

		/* Input arguments length check conditions */
		try {
			if (args.length < 1 || args.length > 1) {
				throw new IllegalArgumentException(Constants.illegalArgumentMessage);
			}
		} catch (Exception e) {
			logger.error(e);
			return;
		}

		Calculator calcObj = new Calculator();
		String input = calcObj.getCorrectInput(args[0]);
		
		SyntaxChecker.checkInitialSyntax(input);
		logger.info("Final Value: "+ ExpressionEvaluator.getCalculatedValue(input));
		System.out.println(ExpressionEvaluator.getCalculatedValue(input));
		
	}

	
}
