# ParseJane

ParseJane is a syntax checker written in Java for **RedJohn**, a small programming language our team designed for the Object-Oriented Programming I course at the Federal Institute of Goiás (IFG), Inhumas campus, Brazil. It reads a RedJohn program and reports every syntax error it finds, without running the program.

Team: Maycon Douglas, Felype de Moura and Maria Eduarda Mendonça.

The code, the keywords and the error messages are in Portuguese. This README explains them in English. The original Portuguese README is in [README.pt-BR.md](README.pt-BR.md), and the full language specification is in [gramatica.txt](gramatica.txt).

## The language

The theme comes from the TV series *The Mentalist*. Each keyword is something the CBI team does in the show.

| RedJohn | Literal meaning | Usual equivalent |
|---|---|---|
| `CASO ABERTO` ... `CASO ENCERRADO` | case opened ... case closed | begin ... end |
| `pista x = 10` | clue | variable declaration |
| `x = x + 1` | | assignment |
| `revela x` | reveal | print |
| `suspeita x > 5` ... `arquivada` | suspicion ... filed away | if ... end |
| `alibi` | alibi | else |
| `interroga x > 0` ... `confessou` | interrogate ... confessed | while ... end |
| `:)` | Red John's smiley | comment |

Example:

```
CASO ABERTO
:) interrogating the suspect
pista mentiras = 3
pista provas = 0
interroga mentiras > 0
    mentiras = mentiras - 1
    provas = provas + 1
confessou
suspeita provas >= 3
    revela provas
alibi
    revela 0
arquivada
CASO ENCERRADO
```

`mentiras` means lies and `provas` means evidence.

## Grammar (EBNF)

```
caso         -> "CASO ABERTO" { linha } "CASO ENCERRADO"
linha        -> comentario | pista | atribuicao | revela | suspeita | interroga
comentario   -> ":)" (free text until the end of the line)
pista        -> "pista" NOME "=" expressao
atribuicao   -> NOME "=" expressao
revela       -> "revela" expressao
suspeita     -> "suspeita" condicao { linha } [ "alibi" { linha } ] "arquivada"
interroga    -> "interroga" condicao { linha } "confessou"
condicao     -> expressao opComparacao expressao
expressao    -> valor { opConta valor }
valor        -> NOME | NUMERO
opComparacao -> ">" | "<" | ">=" | "<=" | "==" | "!="
opConta      -> "+" | "-" | "*" | "/"
```

A `NOME` (name) starts with an unaccented letter or `_`, followed by letters, digits or `_`. A `NUMERO` is an integer or a decimal with one point, such as `10` or `2.5`.

Design choices:

- One command per line and no semicolons. The line break ends the command, as in Python.
- Each block closes with its own keyword (`arquivada` for `suspeita`, `confessou` for `interroga`), as Bash does with `if ... fi` and `while ... done`. Because of that, the parser can tell when a block was closed with the wrong word.
- Conditions and expressions have no parentheses, so a simple loop checks an expression without recursion.
- Tokens are separated by spaces, so the lexer splits a line by looking only at whitespace.

## How it works

The checker has three parts, each with one job:

1. **`Leitor`** (reader, the lexer) splits a line into tokens and classifies each one as a keyword (`PALAVRA`), name (`NOME`), number (`NUMERO`), operator (`OPERADOR`) or invalid token (`INVALIDA`).
2. **The commands** (`Pista`, `Atribuicao`, `Revela`, `Suspeita`, `Interroga`) receive the classified tokens and check the order of their own line. An expression is checked by a loop that alternates between expecting a value and expecting an operator. A condition is split at the comparison operator, and each side is checked as an expression.
3. **`Jane`** (the parser) reads the program line by line, picks the command from the first token, keeps the open blocks on a stack and collects the errors.

The commands do not know about `Jane`, so dependencies go in one direction only. `Jane` is the only class that keeps state. Adding a new command takes one new class and one line in `Main`.

ParseJane does not stop at the first error. Each line is checked on its own, so it collects every problem and prints them together, each one signed by Red John:

```
:) Red John esteve na linha 5: "arquivada" fechou o interroga da linha 3, que termina com "confessou"
```

In English: *Red John was on line 5: "arquivada" closed the interroga from line 3, which ends with "confessou".*

### What it checks

- The program starts with `CASO ABERTO`, ends with `CASO ENCERRADO`, and has no command outside them.
- Every block is closed, and with the keyword that matches its opening.
- `alibi` appears only inside a `suspeita`, at most once per `suspeita`.
- Names have no accents and do not start with a digit. Only existing keywords are accepted.
- Declarations, assignments, expressions and conditions have their tokens in the right order, and an expression never ends with an operator.

ParseJane does not evaluate anything, and it does not check whether a variable was declared before use or whether types match. That would be semantic analysis, which was outside the scope of the assignment.

## Classes

| Class | Responsibility |
|---|---|
| `Main` | registers the commands, runs the test programs and prints the report |
| `Caso` | a program: its name and its lines |
| `Leitor` | splits and classifies the tokens (lexer) |
| `Comando` (abstract) | the command contract, plus shared helpers for expressions and conditions |
| `Pista`, `Atribuicao`, `Revela` | single-line commands |
| `Suspeita`, `Interroga` | commands that open a block |
| `AbreBloco` (interface) | which keyword closes the block and whether it accepts `alibi` |
| `BlocoAberto` | a block that was opened and is not closed yet |
| `Jane` | reads the program, dispatches each line and manages the block stack (parser) |
| `Erro` | the line and the message of an error |
| `Exemplos` | the 17 test programs |

## Object-oriented design

- **Abstraction**: `Comando` defines what every command does, not how.
- **Inheritance**: the five commands extend `Comando` and reuse its helpers.
- **Polymorphism**: `Jane` calls `analisar()` without knowing which command it is calling.
- **Interface**: `AbreBloco` is implemented only by the commands that open a block.
- **Encapsulation**: private fields, accessed through getters.
- **Collections**: `Map` for the registered commands and the usage count, `ArrayList` for the block stack and the errors.

We did not use exceptions, file input, a GUI or threads, because the course had not covered them yet. Errors are objects stored in a list instead of being thrown.

## Running

```
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

`Main` runs the 17 test programs in `Exemplos.java`: 7 valid programs and 10 with errors, from a missing `CASO ABERTO` to a program with six errors at once. The final report should show:

```
casos analisados: 17
sem falhas: 7
com falha: 10
```

(17 programs analysed, 7 without errors, 10 with errors.) Tested with JDK 21.
