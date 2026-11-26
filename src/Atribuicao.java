//nome = expressao
//e o unico comando que nao comeca com palavra reservada, comeca com o nome da pista
public class Atribuicao extends Comando {

    public Erro analisar(String[] palavras, String[] tipos, int linha) {
        if (palavras.length < 3) {
            return new Erro(linha, "faltou o = e o novo valor de " + palavras[0]);
        }
        if (!palavras[1].equals("=")) {
            return new Erro(linha, "depois de " + palavras[0] + " esperava o =, veio \""
                    + palavras[1] + "\"");
        }
        return conferirExpressao(palavras, tipos, 2, palavras.length - 1, linha);
    }
}
