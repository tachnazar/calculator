package com.synopsys.app;

import java.util.LinkedList;


import org.apache.log4j.Logger;

public class ExpressionEvaluator {

	final static Logger logger = Logger.getLogger(ExpressionEvaluator.class);
	

	/*
	 * Returns two exprs from the given expr
	 * Eg: In is "add(expr1, expr2)",
	 * out is array of [expr1, expr2]
	 */
	private static String[] getTwoExpr(String expr, String op) {
		
		String[] exprArr = new String[2]; 
		
		int commaPos = SyntaxChecker.checkMatchedParansAndReturnNextDelim(expr, op.length() + 1, ',');
		String expr1 = expr.substring(op.length() + 1, commaPos);
		exprArr[0] = expr1;
		
		int endPos = SyntaxChecker.checkMatchedParansAndReturnNextDelim(expr, commaPos + 1, ')');
		String expr2 = expr.substring(commaPos + 1, endPos);
		exprArr[1] = expr2;
		
		return exprArr;
	}
	
	/*
	 * Returns two exprs from the given expr
	 * Eg: In is "let(label, expr1, expr2)",
	 * out is array of [label, expr1, expr2]
	 */
	private static String[] get3LetExpr(String expr, String op) {
		String[] exprArr = new String[3]; 
		
		int commaPos = SyntaxChecker.checkMatchedParansAndReturnNextDelim(expr, op.length() + 1, ',');
		String label = expr.substring(op.length() + 1, commaPos);
		exprArr[0] = label;
		if(logger.isDebugEnabled()){
			logger.debug("let label = " + label);
		}
		
		int secondCommaPos = SyntaxChecker.checkMatchedParansAndReturnNextDelim(expr, commaPos + 1, ',');
		String expr1 = expr.substring(commaPos + 1, secondCommaPos);
		exprArr[1] = expr1;
		
		if(logger.isDebugEnabled()){
			logger.debug("let expr1 = " + expr1);
		}
		
		int endPos = SyntaxChecker.checkMatchedParansAndReturnNextDelim(expr, secondCommaPos + 1, ')');
		String expr2 = expr.substring(secondCommaPos + 1, endPos);
		exprArr[2] = expr2;
		if(logger.isDebugEnabled()){
			logger.debug("let expr2 = " + expr2);
		}
		
		return exprArr;
	
	}
	
	
	
	
	
   /* Expression evaluator*/
   public static int getCalculatedValue(String expr) {
	
	try {
		if(expr.matches("[a-zA-z]+")) {
			if (Calculator.variableMap.containsKey(expr)) {
				return Calculator.variableMap.get(expr).peek();
			} else {
				throw new IllegalArgumentException("The variable in let is not found");
				}
	 		
	 	} else if(SyntaxChecker.isNumeric(expr)) {
	 		if(logger.isDebugEnabled()){
				logger.debug("Expression is a number: Expr0 = " + Integer.parseInt(expr));
			}
			return Integer.parseInt(expr);
			
		} else if (expr.startsWith(Constants.ADD)) {
			
			String[] exprs = getTwoExpr(expr, Constants.ADD);
			if(logger.isDebugEnabled()){
				logger.debug("ADD Expression: Expr0 = " + exprs[0] + ", Expr1 = " + exprs[1]);
			}
			return getCalculatedValue(exprs[0]) + getCalculatedValue(exprs[1]);
			 
		} else if (expr.startsWith(Constants.SUB)) {
			
			String[] exprs = getTwoExpr(expr, Constants.SUB);
			if(logger.isDebugEnabled()){
				logger.debug("SUB Expression: Expr0 = " + exprs[0] + ", Expr1 = " + exprs[1]);
			}
			return getCalculatedValue(exprs[0]) - getCalculatedValue(exprs[1]);
		
		} else if(expr.startsWith(Constants.MULT)) {
			
			String[] exprs = getTwoExpr(expr, Constants.MULT);
			if(logger.isDebugEnabled()){
				logger.debug("MULT Expression: Expr0 = " + exprs[0] + ", Expr1 = " + exprs[1]);
			}
			return getCalculatedValue(exprs[0]) * getCalculatedValue(exprs[1]);
		
		} else if(expr.startsWith(Constants.DIV)) {
			
			String[] exprs = getTwoExpr(expr, Constants.DIV);
			if(logger.isDebugEnabled()){
				logger.debug("DIV Expression: Expr0 = " + exprs[0] + ", Expr1 = " + exprs[1]);
			}
			return getCalculatedValue(exprs[0]) / getCalculatedValue(exprs[1]);
		
		} else if(expr.startsWith(Constants.LET)) {
			String[] exprs = get3LetExpr(expr, Constants.LET);
			String label = exprs[0];
			String expr1 = exprs[1];
			String expr2 = exprs[2];
			if(logger.isDebugEnabled()){
				logger.debug("Let Expression: Label = " + label + ", Expr1 = " + expr1 + ", Expr2 = " + expr2);
			}
			int valExpr1 = getCalculatedValue(expr1);
			LinkedList<Integer> currStack;
			if (!Calculator.variableMap.containsKey(label)) {
				currStack = new LinkedList<Integer>();
				Calculator.variableMap.put(label, currStack);
			}
			Calculator.variableMap.get(label).push(valExpr1);
			
			
			int valExpr2 = getCalculatedValue(expr2);
			
			LinkedList<Integer> prevStack = Calculator.variableMap.get(label);
			prevStack.pop();
			if (prevStack.isEmpty()) {
				Calculator.variableMap.remove(label);
			}
			
			return valExpr2;
		
		}else {
			
		} 
		
	} catch (Exception e){
		logger.error(e);
	}
		
		return 0;
	}
	
}
