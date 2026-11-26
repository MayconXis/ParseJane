import java.util.ArrayList;
import java.util.List;


public class Leitor {

    public static final String PALAVRA = "PALAVRA";
    public static final String NOME = "NOME";
    public static final String NUMERO = "NUMERO";
    public static final String OPERADOR = "OPERADOR";
    public static final String INVALIDA = "INVALIDA";

    private List<String> reservadas;
    private List<String> operadores;

    public Leitor() {
        reservadas = new ArrayList<String>();
        reservadas.add("CASO");
        reservadas.add("ABERTO");
        reservadas.add("ENCERRADO");
        reservadas.add("pista");
        reservadas.add("revela");
        reservadas.add("suspeita");
        reservadas.add("alibi");
        reservadas.add("arquivada");
        reservadas.add("interroga");
        reservadas.add("confessou");

        operadores = new ArrayList<String>();
        operadores.add("=");
        operadores.add("+");
        operadores.add("-");
        operadores.add("*");
        operadores.add("/");
        operadores.add(">");
        operadores.add("<");
        operadores.add(">=");
        operadores.add("<=");
        operadores.add("==");
        operadores.add("!=");
    }

    //quebra a linha em palavras, jogando fora os espacos que sobram
    public String[] separar(String linha) {
        List<String> partes = new ArrayList<String>();
        String atual = "";

        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == ' ' || c == '\t') {
                if (atual.length() > 0) {
                    partes.add(atual);
                    atual = "";
                }
            } else {
                atual = atual + c;
            }
        }
        if (atual.length() > 0) {
            partes.add(atual);
        }

        String[] resultado = new String[partes.size()];
        for (int i = 0; i < partes.size(); i++) {
            resultado[i] = partes.get(i);
        }
        return resultado;
    }

    //devolve um array do mesmo tamanho, dizendo a categoria de cada palavra
    public String[] classificar(String[] palavras) {
        String[] tipos = new String[palavras.length];
        for (int i = 0; i < palavras.length; i++) {
            tipos[i] = tipoDe(palavras[i]);
        }
        return tipos;
    }

    private String tipoDe(String palavra) {
        if (reservadas.contains(palavra)) {
            return PALAVRA;
        }
        if (operadores.contains(palavra)) {
            return OPERADOR;
        }
        if (ehNumero(palavra)) {
            return NUMERO;
        }
        if (ehNome(palavra)) {
            return NOME;
        }
        return INVALIDA;
    }

    //numero inteiro ou com uma virgula decimal so, tipo 10 ou 2.5
    private boolean ehNumero(String palavra) {
        if (palavra.length() == 0) {
            return false;
        }
        int pontos = 0;
        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            if (c == '.') {
                pontos = pontos + 1;
                //ponto no comeco ou no fim nao vale, e dois pontos tambem nao
                if (pontos > 1 || i == 0 || i == palavra.length() - 1) {
                    return false;
                }
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    //nome comeca com letra sem acento ou _, e depois aceita letra, numero e _
    //acento nao entra de proposito, igual na maioria das linguagens
    private boolean ehNome(String palavra) {
        if (palavra.length() == 0) {
            return false;
        }
        char primeira = palavra.charAt(0);
        if (!ehLetra(primeira) && primeira != '_') {
            return false;
        }
        for (int i = 1; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            boolean digito = (c >= '0' && c <= '9');
            if (!ehLetra(c) && !digito && c != '_') {
                return false;
            }
        }
        return true;
    }

    private boolean ehLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
}
