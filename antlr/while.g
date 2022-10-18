grammar while;

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
program	:	function (program)?;
function:	'function' Symbol ':' definition;
definition
	:	'read' input '%' commands '%' 'write' output;
input	:	(inputSub)?;
inputSub:	Variable (',' inputSub)?;
output	:	Variable (',' output)?;
commands:	command (';' commands)?;
command	:	'nop'
    | vars ':=' exprs
	|	'if' expression 'then' commands ('else' commands)? 'fi'
	|	'while' expression 'do' commands 'od'
	|	'for' expression 'do' commands 'od'
	|	'foreach' Variable 'in' expression 'do' commands 'od';
exprBase:	'nil' | Variable | Symbol
	|	'(' 'cons' lexpr ')' | '(' 'list' lexpr ')'
	|	'(' 'hd' exprBase ')' | '(' 'tl' exprBase ')'
	|	'(' Symbol lexpr ')';
vars	:	Variable (',' vars)?;
exprs	:	expression (',' exprs)?;
expression
	:	exprBase ('=?' exprBase)?;
lexpr	:	(exprBase lexpr)?;
Variable:	Maj (Maj|Min|Dec)*('!'|'?')?;
Symbol	:	Min(Maj|Min|Dec)*('!'|'?')?;

fragment Maj	:	'A'..'Z';
fragment Min	:	'a'..'z';
fragment Dec	:	'0'..'9';
	