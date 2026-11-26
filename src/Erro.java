public class Erro {

    private int linha;
    private String mensagem;

    public Erro(int linha, String mensagem) {
        this.linha = linha;
        this.mensagem = mensagem;
    }

    public int getLinha() {
        return linha;
    }

    public String getMensagem() {
        return mensagem;
    }

    //a marca que o Red John deixa na cena do crime
    public String toString() {
        return ":) Red John esteve na linha " + linha + ": " + mensagem;
    }
}
