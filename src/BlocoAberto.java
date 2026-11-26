public class BlocoAberto {

    private String abertura;
    private String fechamento;
    private int linha;
    private boolean aceitaAlibi;
    private boolean jaTeveAlibi;
    private int linhaDoAlibi;

    public BlocoAberto(String abertura, String fechamento, int linha, boolean aceitaAlibi) {
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.linha = linha;
        this.aceitaAlibi = aceitaAlibi;
        this.jaTeveAlibi = false;
        this.linhaDoAlibi = 0;
    }

    public String getAbertura() {
        return abertura;
    }

    public String getFechamento() {
        return fechamento;
    }

    public int getLinha() {
        return linha;
    }

    public boolean aceitaAlibi() {
        return aceitaAlibi;
    }

    public boolean jaTeveAlibi() {
        return jaTeveAlibi;
    }

    public int getLinhaDoAlibi() {
        return linhaDoAlibi;
    }

    public void marcarAlibi(int linhaDoAlibi) {
        this.jaTeveAlibi = true;
        this.linhaDoAlibi = linhaDoAlibi;
    }
}
