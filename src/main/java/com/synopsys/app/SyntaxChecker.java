package com.synopsys.app;

import org.apache.log4j.Logger;

public class SyntaxChecker {

	final static Logger logger = Logger.getLogger(Calculator.class);
	/* Initial Syntax checking of an expression */
	public static void checkInitialSyntax(String expr) {

		try {
			if (expr.matches("[a-zA-z]+")) {

			} else if (isNumeric(expr)) {

			} else if (expr.startsWith(Constants.ADD)) {
				singleExpressionSyntaxCheck(expr, Constants.ADD);

			} else if (expr.startsWith(Constants.SUB)) {
				singleExpressionSyntaxCheck(expr, Constants.SUB);

			} else if (expr.startsWith(Constants.MULT)) {
				singleExpressionSyntaxCheck(expr, Constants.MULT);

			} else if (expr.startsWith(Constants.DIV)) {
				singleExpressionSyntaxCheck(expr, Constants.DIV);

			} else if (expr.startsWith(Constants.LET)) {
				letExpressionSyntaxCheck(expr, Constants.LET);

			} else {
				throw new IllegalArgumentException("unknown operation provided -- need Constants.ADD/Constants.SUB/mult/div/let");
			}

			if (!checkMatchedParantheses(expr))
				throw new IllegalArgumentException("Paranthesis not matching");

		} catch (Exception e) {
			logger.error(e);
			return;
		}

	}

	/* Check if the expression is numeric in nature */
	public static boolean isNumeric(String expr) {
		String eval = expr;
		if (expr.startsWith("-")) {
			eval = expr.substring(1, expr.length());
		}

		for (Character c : eval.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	/*  Method checks for matching parentheses in given expression */
	private static boolean checkMatchedParantheses(String expr) {
		try {
			int paranCounter = 0;
			for (int i = 0; i < expr.length(); i++) {

				if (expr.charAt(i) == '(')
					paranCounter++;

				if (expr.charAt(i) == ')') {
					if (paranCounter == 0)
						throw new IllegalArgumentException(Constants.illegalArgumentMessage);
					paranCounter--;
				}

			}

			return paranCounter == 0;
		} catch (Exception e) {
			logger.error(e);
		}
		return false;
	}
	
	/*  Syntax check for each of the two expressions for Constants.ADD/Constants.SUB/mult/div */
	public static void singleExpressionSyntaxCheck(String expression, String operation) {

		checkBeginParentheses(expression, operation.length());

		int commaPos = checkMatchedParansAndReturnNextDelim(expression, operation.length() + 1, ',');
		String expr1 = expression.substring(operation.length() + 1, commaPos);
		checkInitialSyntax(expr1);

		int endPos = checkMatchedParansAndReturnNextDelim(expression, commaPos + 1, ')');
		assert (endPos == expression.length() - 1);
		String expr2 = expression.substring(commaPos + 1, endPos);
		checkInitialSyntax(expr2);
	}

	/*  Syntax check for each of the let expression */
	public static void letExpressionSyntaxCheck(String expression, String op) {
		checkBeginParentheses(expression, op.length());
		
		int commaPos = checkMatchedParansAndReturnNextDelim(expression, op.length() + 1, ',');
		String varName = expression.substring(op.length() + 1, commaPos);
		if(logger.isDebugEnabled()){
			logger.debug("let label = " + varName);
		}
		checkInitialSyntax(varName);
		
		
		int secondCommaPos = checkMatchedParansAndReturnNextDelim(expression, commaPos + 1, ',');
		String valueExprName = expression.substring(commaPos + 1, secondCommaPos);
		if(logger.isDebugEnabled()){
			logger.debug("let valueExprName = " + valueExprName);
		}
		checkInitialSyntax(valueExprName);
		
		
		int endPos = checkMatchedParansAndReturnNextDelim(expression, secondCommaPos + 1, ')');
		String exprName = expression.substring(secondCommaPos + 1, endPos);
		if(logger.isDebugEnabled()){
			logger.debug("let exprName = " + exprName);
		}
		checkInitialSyntax(exprName);
		
		
	}
	
	/*  To check if there is a parentheses at the beginning */
	private static void checkBeginParentheses(String expression, int prefix) {
		try {
			if (!expression.startsWith("(", prefix)) {
				throw new IllegalArgumentException(Constants.illegalArgumentMessage);
			}
		} catch (Exception e) {
			logger.error(e);
			return;
		}
	}

	/* Method checks for matching parentheses starting at input prefix
	 * 
	 * @param expr - input string
	 * 
	 * @param prefix
	 * 
	 * @return index after the parentheses match E.g.: Constants.ADD(Constants.ADD(1,2), 3) then
	 * prefix: 4 and should return: 12
	 */
	public static int checkMatchedParansAndReturnNextDelim(String expression, int prefix, Character delimiter) {

		if (logger.isDebugEnabled()) {
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
					if (paranCounter == 0)
						throw new IllegalArgumentException(Constants.illegalArgumentMessage);
					paranCounter--;
				}

			}

			if (paranCounter > 0)
				throw new IllegalArgumentException(Constants.illegalArgumentMessage);

		} catch (Exception e) {
			logger.error(e);
		}
		return i;
	}
	
	
	
}
