public class Revela extends Comando {

    public Erro analisar(String[] palavras, String[] tipos, int linha) {
        if (palavras.length < 2) {
            return new Erro(linha, "revela sem nada pra mostrar");
        }
        return conferirExpressao(palavras, tipos, 1, palavras.length - 1, linha);
    }
}
