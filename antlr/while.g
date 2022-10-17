grammar while;

ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

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

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

CHAR:  '\'' ( ESC_SEQ | ~('\''|'\\') ) '\''
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
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

Maj	:	'A'..'Z';
Min	:	'a'..'z';
Dec	:	'0'..'9';
	