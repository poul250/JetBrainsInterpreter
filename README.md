# JetBrainsInterpreter
Интерпретатор для модельного языка программирования.<br>
Язык задан следующей грамматикой:<br>
\<character\>  ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z" | "_"<br>
\<digit\>   ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"<br>
\<number\> ::= \<digit\> | \<digit\> \<number\><br>
\<identifier\> ::= \<character\> | \<identifier\> \<character\><br>
\<operation\> ::= "+" | "-" | "*" | "/" | "%" | ">" | "<" | "="<br>
\<constant-expression\> ::= "-" \<number\> | \<number\><br>
<br>
\<binary-expression\> ::= "(" \<expression\> \<operation\> \<expression\>  ")"<br>
\<argument-list\> ::= \<expression\> | \<expression\> "," \<argument-list\><br>
\<call-expression\> ::= \<identifier\> "(" \<argument-list\> ")"<br>
\<if-expression\> ::= "[" \<expression\> "]?(" \<expression\> "):("\<expression\>")"<br>
<br>
\<expression\> ::= \<identifier\>
                  | \<constant-expression\>
                  | \<binary-expression\>
                  | \<if-expression\>
                  | \<call-expression\><br>
<br>
\<parameter-list\> ::= \<identifier\> | \<identifier\> "," \<parameter-list\><br>
<br>
\<function-definition\> ::= \<identifier\>"(" \<parameter_list\> ")" "={" \<expression\> "}"<br>
<br>
\<function-definition-list\> : ""
                             | \<function-definition\> \<EOL\>
                             | \<function-definition\> \<EOL\> \<function-definition-list\><br>
<br>
\<program\> ::= \<function-definition-list\> \<expression\><br><br>
\<EOL\> - символ перевода строки --- \\n, программа не содержит других пробельных символов(пробел, табуляция, и т.д.);
<br>
<br>
Семантика языка задается следующим образом:<br>
1) Все переменные имеют тип 32-битный Integer;<br>
2) Гаранитруется, что вычисления не приводят к переполнению;<br>
3) Все арифметические операции аналогичны соответствующим операциям для 32-битного int в языка Java;<br>
4) Операции сравнения возвращают 1 если сравнение истинно и 0 если ложно;<br>
5) \<if-expression\> исполняет второе выражение, если первое выражение не равно 0; иначе исполняет третье;<br>
6) \<call-expression\> вызывает функцию с соответствующим именем<br>
7) Выражение вычисляются слева направо;<br>
