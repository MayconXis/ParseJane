
public abstract class Comando {

    public abstract Erro analisar(String[] palavras, String[] tipos, int linha);

    //valor eh um numero ou o nome de uma pista
    protected boolean ehValor(String tipo) {
        return tipo.equals(Leitor.NUMERO) || tipo.equals(Leitor.NOME);
    }

    protected boolean ehOperadorDeConta(String palavra) {
        return palavra.equals("+") || palavra.equals("-")
                || palavra.equals("*") || palavra.equals("/");
    }

    protected boolean ehComparacao(String palavra) {
        return palavra.equals(">") || palavra.equals("<")
                || palavra.equals(">=") || palavra.equals("<=")
                || palavra.equals("==") || palavra.equals("!=");
    }

    // valor, depois operador, depois valor, quantas vezes precisar
    //o laco vai alternando entre esperar um valor e esperar um operador
    protected Erro conferirExpressao(String[] palavras, String[] tipos,
                                     int inicio, int fim, int linha) {
        if (inicio > fim) {
            return new Erro(linha, "faltou o valor da conta");
        }

        boolean esperaValor = true;
        for (int i = inicio; i <= fim; i++) {
            if (esperaValor) {
                if (!ehValor(tipos[i])) {
                    return new Erro(linha, "esperava um numero ou o nome de uma pista, veio \""
                            + palavras[i] + "\"");
                }
            } else {
                if (!ehOperadorDeConta(palavras[i])) {
                    return new Erro(linha, "esperava +, -, * ou /, veio \"" + palavras[i] + "\"");
                }
            }
            esperaValor = !esperaValor;
        }

        //se ainda espera valor no fim, a conta terminou num operador
        if (esperaValor) {
            return new Erro(linha, "a conta termina em operador e falta um valor depois dele");
        }
        return null;
    }

    // expressao, comparacao, expressao
    //acha a comparacao e confere os dois lados separados
    protected Erro conferirCondicao(String[] palavras, String[] tipos,
                                    int inicio, int fim, int linha) {
        int posicao = -1;
        for (int i = inicio; i <= fim; i++) {
            if (ehComparacao(palavras[i])) {
                posicao = i;
                break;
            }
        }

        if (posicao == -1) {
            return new Erro(linha, "falta a comparacao (>, <, >=, <=, == ou !=)");
        }

        Erro esquerda = conferirExpressao(palavras, tipos, inicio, posicao - 1, linha);
        if (esquerda != null) {
            return esquerda;
        }
        return conferirExpressao(palavras, tipos, posicao + 1, fim, linha);
    }
}
