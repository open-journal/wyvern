package wyvern.tools.errors;

public enum ErrorMessage {
	// Type errors
	ACTUAL_FORMAL_TYPE_MISMATCH("Actual argument to function does not match function type", 0),
	TYPE_CANNOT_BE_APPLIED("Type %ARG cannot be applied to an argument", 1),
	TYPE_NOT_DEFINED("Type %ARG is not defined", 1),
	VARIABLE_NOT_DECLARED("Variable %ARG has no type in the context", 1),
	TYPE_NOT_DECLARED("Type %ARG has no declaration in the context", 1),
	OPERATOR_DOES_NOT_APPLY("Operator %ARG cannot be applied to type %ARG", 2),
	OPERATOR_DOES_NOT_APPLY2("Operator %ARG cannot be applied to types %ARG and %ARG", 3),
	MUST_BE_LITERAL_CLASS("The Name %ARG must refer to a class declaration currently in scope", 1),
	
	// Syntax errors
	UNEXPECTED_INPUT("Unexpected input", 0),
	INDENT_DEDENT_MISMATCH("Expected dedent to match earlier indent", 0),
	EXPECTED_TOKEN_NOT_EOF("Expected an expression but reached end of file", 0),
	MISMATCHED_PARENTHESES("No matching close parenthesis", 0),
	
	// Evaluation errors
	VALUE_CANNOT_BE_APPLIED("The value %ARG cannot be applied to an argument", 1),
	CANNOT_INVOKE("Cannot invoke operations on the value %ARG", 1),
	
	;// end of error list
	
	private ErrorMessage(String message, int numArgs) {
		this.errorMessage = message;
		this.numArgs = numArgs;
	}
	
	public String getErrorMessage() {
		assert numArgs == 0;
		return errorMessage;
	}
	
	public String getErrorMessage(String argument) {
		assert numArgs == 1;
		return errorMessage.replaceFirst("%ARG", argument);
	}
	
	public String getErrorMessage(String arg1, String arg2) {
		assert numArgs == 2;
		String str = errorMessage.replaceFirst("%ARG", arg1);
		return str.replaceFirst("%ARG", arg2);
	}
	
	public String getErrorMessage(String arg1, String arg2, String arg3) {
		assert numArgs == 3;
		String str = errorMessage.replaceFirst("%ARG", arg1);
		str = str.replaceFirst("%ARG", arg2);
		return str.replaceFirst("%ARG", arg3);
	}
	
	public int numberOfArguments() {
		return numArgs;
	}
	
	private String errorMessage;
	private int numArgs;
}
