public class Interroga extends Comando implements AbreBloco {

    public Erro analisar(String[] palavras, String[] tipos, int linha) {
        if (palavras.length < 4) {
            return new Erro(linha, "o interroga precisa de uma comparacao, tipo mentiras > 0");
        }
        return conferirCondicao(palavras, tipos, 1, palavras.length - 1, linha);
    }

    public String getFechamento() {
        return "confessou";
    }

    public boolean aceitaAlibi() {
        return false;
    }
}
