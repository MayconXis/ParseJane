import java.util.ArrayList;
import java.util.List;

public class Caso {

    private String nome;
    private List<String> linhas;

    public Caso(String nome) {
        this.nome = nome;
        this.linhas = new ArrayList<String>();
    }

    //escreve mais uma linha no caso
    public void escrever(String linha) {
        linhas.add(linha);
    }

    public String getNome() {
        return nome;
    }

    public int getTotalDeLinhas() {
        return linhas.size();
    }

    public String getLinha(int indice) {
        return linhas.get(indice);
    }
}
