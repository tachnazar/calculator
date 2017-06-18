

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
	
	/* 1. Main method */
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
		/* Removing unnecessary spaces from the input string */
		String input = args[0].replaceAll("\\s", "");
		calcObj.checkInitialSyntax(input);
		System.out.println(ExpressionEvaluator.getCalculatedValue(input));
		
	}

	/* 2. Initial Syntax checking of an expression */
	private void checkInitialSyntax(String expr) {

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

	/* 3. Check if the expression is numeric in nature */
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

	/* 4. Method checks for matching parentheses in given expression */
	private boolean checkMatchedParantheses(String expr) {
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
	
	/* 5. Syntax check for each of the two expressions for Constants.ADD/Constants.SUB/mult/div */
	private void singleExpressionSyntaxCheck(String expression, String operation) {

		checkBeginParentheses(expression, operation.length());

		int commaPos = checkMatchedParansAndReturnNextDelim(expression, operation.length() + 1, ',');
		String expr1 = expression.substring(operation.length() + 1, commaPos);
		checkInitialSyntax(expr1);

		int endPos = checkMatchedParansAndReturnNextDelim(expression, commaPos + 1, ')');
		assert (endPos == expression.length() - 1);
		String expr2 = expression.substring(commaPos + 1, endPos);
		checkInitialSyntax(expr2);
	}

	/* 6. Syntax check for each of the let expression */
	private void letExpressionSyntaxCheck(String expression, String op) {
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
	
	/* 7. To check if there is a parentheses at the beginning */
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

	/* 8. Method checks for matching parentheses starting at input prefix
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
