# RedJohn

Analisador sintatico escrito em Java para a linguagem **RedJohn**, criada pelo grupo.
O programa confere se um codigo esta escrito certo, sem executar nada.

Trabalho de Programacao Orientada a Objetos I, IFG Campus Inhumas.
Maycon Douglas, Felype de Moura, Maria Eduarda Mendonca.

## A linguagem

O tema vem da serie The Mentalist. Cada comando usa o que a equipe do CBI faz:

| No codigo | O que faz | Em uma linguagem comum |
|---|---|---|
| `CASO ABERTO` ... `CASO ENCERRADO` | comeco e fim do programa | begin ... end |
| `pista x = 10` | cria uma variavel | int x = 10 |
| `x = x + 1` | muda o valor | atribuicao |
| `revela x` | mostra na tela | print |
| `suspeita x > 5` ... `arquivada` | decisao | if ... end |
| `alibi` | o outro caminho | else |
| `interroga x > 0` ... `confessou` | repeticao | while ... end |
| `:)` | comentario | // |

Exemplo:

```
CASO ABERTO
:) interrogatorio do suspeito
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

A especificacao completa esta em `gramatica.txt`.

## Como o analisador funciona

Tres partes, cada uma com uma responsabilidade:

1. **Leitor** separa a linha em palavras e diz o que cada palavra eh
   (PALAVRA, NOME, NUMERO, OPERADOR ou INVALIDA).
2. **Comandos** (`Pista`, `Atribuicao`, `Revela`, `Suspeita`, `Interroga`)
   recebem as palavras ja classificadas e conferem so a ordem da propria linha.
3. **Jane** le o caso linha por linha, escolhe o comando pela primeira palavra,
   guarda os blocos abertos numa pilha e junta os erros.

Nenhum comando conhece a Jane, entao as dependencias so vao num sentido.
A Jane eh a unica classe que guarda estado.

O analisador nao para no primeiro erro. Como cada linha eh conferida sozinha,
ele junta todas as falhas e mostra de uma vez, com a assinatura do Red John:

```
:) Red John esteve na linha 5: "arquivada" fechou o interroga da linha 3, que termina com "confessou"
```

## Classes

| Classe | Responsabilidade |
|---|---|
| `Main` | monta a equipe, roda os exemplos e imprime |
| `Caso` | o programa: nome e lista de linhas |
| `Leitor` | separa e classifica as palavras |
| `Comando` (abstrata) | contrato do comando e os auxiliares de conta e condicao |
| `Pista`, `Atribuicao`, `Revela` | comandos de uma linha so |
| `Suspeita`, `Interroga` | comandos que abrem bloco |
| `AbreBloco` (interface) | diz qual palavra fecha o bloco e se aceita alibi |
| `BlocoAberto` | bloco que abriu e ainda nao fechou |
| `Jane` | le o caso, despacha as linhas e controla a pilha |
| `Erro` | linha e mensagem da falha |
| `Exemplos` | os 17 casos de teste |

## POO usada

- **Abstracao**: `Comando` define o que todo comando faz sem dizer como.
- **Heranca**: os cinco comandos herdam de `Comando` e reaproveitam os auxiliares.
- **Polimorfismo**: a Jane chama `analisar()` sem saber qual comando esta chamando.
- **Interface**: `AbreBloco` so para quem abre bloco, porque nem todo comando abre.
- **Encapsulamento**: atributos privados, acesso por getters.
- **Modificadores**: `private` no estado, `protected` nos auxiliares, `public` no contrato.
- **Colecoes**: `Map` para a equipe e para a contagem, `ArrayList` para a pilha e os erros.
- **Arrays**: as palavras e as categorias de cada linha.

Nao usamos excecoes, leitura de arquivo, GUI nem threads, porque esses assuntos
ainda nao entraram na materia. Os erros sao objetos guardados numa lista, nao `throw`.

## Como rodar

```
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Saida esperada: 17 casos analisados, 7 sem falhas e 10 com falha.
