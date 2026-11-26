//pista nome = expressao
public class Pista extends Comando {

    public Erro analisar(String[] palavras, String[] tipos, int linha) {
        if (palavras.length < 4) {
            return new Erro(linha, "a pista precisa de nome, = e valor");
        }
        if (!tipos[1].equals(Leitor.NOME)) {
            return new Erro(linha, "\"" + palavras[1] + "\" nao serve como nome de pista");
        }
        if (!palavras[2].equals("=")) {
            return new Erro(linha, "faltou o = depois de " + palavras[1]);
        }
        return conferirExpressao(palavras, tipos, 3, palavras.length - 1, linha);
    }
}
