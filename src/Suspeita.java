public class Suspeita extends Comando implements AbreBloco {

    public Erro analisar(String[] palavras, String[] tipos, int linha) {
        if (palavras.length < 4) {
            return new Erro(linha, "a suspeita precisa de uma comparacao, tipo provas > 2");
        }
        return conferirCondicao(palavras, tipos, 1, palavras.length - 1, linha);
    }

    public String getFechamento() {
        return "arquivada";
    }

    public boolean aceitaAlibi() {
        return true;
    }
}
