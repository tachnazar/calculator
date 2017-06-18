

package com.synopsys.app;

import org.apache.log4j.Logger;

public class Calculator {
	
	private static final String ADD = "add";
	private static final String SUB = "sub";
	private static final String MULT = "mult";
	private static final String DIV = "div";
	private static final String LET = "let";
	private static final String illegalArgumentMessage = "Input arguments are not in proper order. Correct the input format: java Calculator \"add(1, 2)\"";

	final static Logger logger = Logger.getLogger(Calculator.class);
	
	private boolean checkMatchedParans(String expr) {
		try {
			int paranCounter = 0;
			for (int i = 0; i < expr.length(); i++) {
			
				if (expr.charAt(i) == '(') 
					paranCounter++;
			
				if (expr.charAt(i) == ')') {
					if(paranCounter == 0) 
						throw new IllegalArgumentException(illegalArgumentMessage);
					paranCounter--;
				}
			
			}
		
			return paranCounter == 0;
		} catch (Exception e){
			logger.error(e);
		}
		return false;
	}
	
	private static boolean isNumeric(String expr) {
		String eval = expr;
		if (expr.startsWith("-")) {
			eval = expr.substring(1,expr.length());
		}

	 for (Character c: eval.toCharArray()) {
		 	if (!Character.isDigit(c)) {
		 		return false;
		 	}
	 }
		
	return true;
}
	
    private static int checkMatchedParansAndReturnNextDelim(String expression, int prefix, Character delimiter) {
		
		if(logger.isDebugEnabled()){
			logger.debug("String expession = " + expression);
			logger.debug("prefix value = " + prefix);
			logger.debug("delimiter = " + delimiter);
		}
		int i = prefix;
		try {
			int paranCounter = 0;
			for (; i < expression.length(); i++) {
			
				if (paranCounter == 0 && expression.charAt(i) == delimiter)
					return i;
			
				if (expression.charAt(i) == '(') 
					paranCounter++;
			
				if (expression.charAt(i) == ')') {
					if(paranCounter == 0) 
						throw new IllegalArgumentException(illegalArgumentMessage);
					paranCounter--;
				}
			
			}
		
			if(paranCounter > 0) 
				throw new IllegalArgumentException(illegalArgumentMessage);
			
			} catch (Exception e){
				logger.error(e);
			}
			return i;
	}
	
	private void singleExpressionSyntaxCheck(String expression, String operation) {
		
		int prefix = operation.length();
		
		try {
			if (!expression.startsWith("(", prefix)) {
				throw new IllegalArgumentException(illegalArgumentMessage);
			}
		} catch (Exception e){
			logger.error(e);
			return;
		}
		
		int commaPos = checkMatchedParansAndReturnNextDelim(expression, operation.length() + 1, ',');
		String expr1 = expression.substring(operation.length() + 1, commaPos);
		checkSyntaxInExpression(expr1);
		
		int endPos = checkMatchedParansAndReturnNextDelim(expression, commaPos + 1, ')');
		assert (endPos == expression.length() - 1);
		String expr2 = expression.substring(commaPos + 1, endPos);
		checkSyntaxInExpression(expr2);
	}
	
	
	private void checkSyntaxInExpression(String expr) {
		
		try{
		 	if(expr.matches("[a-zA-z]+")) {
		 		
		 	} else if(isNumeric(expr)) {
		 		
		 	} else if (expr.startsWith(ADD)) {
		 		singleExpressionSyntaxCheck(expr, ADD);
				
		 	} else if (expr.startsWith(SUB)) {
		 		System.out.println("Inside ADD");
				
		 	} else if (expr.startsWith(MULT)) {
		 		System.out.println("Inside ADD");
				
		 	} else if (expr.startsWith(DIV)) {
		 		System.out.println("Inside ADD");
				
		 	} else if (expr.startsWith(LET)) {
		 		System.out.println("Inside ADD");
				
			} else {
				throw new IllegalArgumentException("unknown operation provided -- need add/sub/mult/div/let");
			}
			
			if (!checkMatchedParans(expr)) 
				throw new IllegalArgumentException("Paranthesis not matching");
			
		} catch (Exception e){
			logger.error(e);
			return;
		}
			
	
			
	}
	
	
	public static void main(String[] args) {
		
		/*Input arguments length check conditions*/
		try {
			if (args.length < 1 || args.length > 1) {
				throw new IllegalArgumentException(illegalArgumentMessage);
			}
		} catch (Exception e){
			logger.error(e);
			return;
		}
		
		Calculator calcObj = new Calculator();
		/* Removing unnecessary spaces from the input string*/
		String input = args[0].replaceAll("\\s", "");
		calcObj.checkSyntaxInExpression(input);
		
		
	}
	
}
