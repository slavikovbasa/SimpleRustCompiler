5 semester System Programming 2 CourseWork - Simple Rust compiler

Subset of a Rust programming language syntax, including arithmetic and logic operations, control constructions and loops, is compiled via next stages:
1. Lexer, implemented as a seperate module.
2. Parser, implemented as a recursive descent parser in a seperate module.
3. Semantic analysis, which is performed in a parsing module during AST building.
4. Code generator, which does its work in a Main class, during AST traverse.

Input and output file names are passed as command-line arguments to the program.