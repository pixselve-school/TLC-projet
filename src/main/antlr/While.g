grammar While;
options { output=AST; }
tokens {
	PROGRAM;
	FUNCTION;
	INPUTS;
	OUTPUTS;
	COMMANDS;
	LET;
	IF;
	WHILE;
	FOR;
	FOREACH;
	NOP;
	NIL;
	CONS;
	HD;
	TL;
	LIST;
	VARS;
	EXPRS;
	EXPR;
	LEXPR;
	SYMB;
}

@header {
    package org.example;
}
@lexer::header{
    package org.example;
}

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

// Grammaire du langage while
program	:	function+ -> ^(PROGRAM function+);
function:	'function' Symbol ':' definition -> ^(FUNCTION Symbol definition);
definition
	:	'read' input '%' commands '%' 'write' output -> input commands output;
input	:	inputSub? -> inputSub?;
inputSub:	Variable (',' Variable)* -> ^(INPUTS Variable+);
output	:	Variable (',' Variable)* -> ^(OUTPUTS Variable+);
commands:	command (';' command)* -> ^(COMMANDS command+);
command	:	'nop' -> NOP
    	| 	vars ':=' exprs -> ^(LET vars exprs)
	|	'if' expression 'then' commands ('else' commands)? 'fi' -> ^(IF expression commands commands?)
	|	'while' expression 'do' commands 'od' -> ^(WHILE expression commands)
	|	'for' expression 'do' commands 'od' -> ^(FOR expression commands)
	|	'foreach' Variable 'in' expression 'do' commands 'od' -> ^(FOREACH Variable expression commands);
exprBase:	'nil' -> NIL
	 | Variable -> Variable
	 | Symbol -> Symbol
	|	'(' 'cons' lexpr ')' -> ^(CONS lexpr)
	| '(' 'list' lexpr ')' -> ^(LIST lexpr)
	|	'(' 'hd' exprBase ')'  -> ^(HD exprBase)
	| '(' 'tl' exprBase ')' -> ^(TL exprBase)
	|	'(' Symbol lexpr? ')' -> ^(SYMB Symbol lexpr?);
vars	:	Variable (',' Variable)* -> ^(VARS Variable+);
exprs	:	expression (',' expression)* -> ^(EXPRS expression+);
expression
	:	exprBase ('=?' exprBase)? -> exprBase+;
lexpr	:	exprBase+ -> exprBase+;
Variable:	Maj (Maj|Min|Dec)*('!'|'?')?;
Symbol	:	Min(Maj|Min|Dec)*('!'|'?')?;

fragment Maj	:	'A'..'Z';
fragment Min	:	'a'..'z';
fragment Dec	:	'0'..'9';
	