import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jane {

    private Leitor leitor;
    private Map<String, Comando> equipe;
    private List<BlocoAberto> blocosAbertos;
    private List<Erro> erros;
    private Map<String, Integer> aparicoes;

    public Jane() {
        this.leitor = new Leitor();
        this.equipe = new HashMap<String, Comando>();
        this.blocosAbertos = new ArrayList<BlocoAberto>();
        this.erros = new ArrayList<Erro>();
        this.aparicoes = new HashMap<String, Integer>();
    }

    //o Main escala quem faz parte da equipe
    //pra criar um comando novo basta uma classe nova e uma linha dessas, a Jane nao muda
    public void escalar(String chave, Comando comando) {
        equipe.put(chave, comando);
    }

    public List<Erro> getErros() {
        return erros;
    }

    public Map<String, Integer> getAparicoes() {
        return aparicoes;
    }

    //le o caso inteiro, linha por linha
    public void investigar(Caso caso) {
        blocosAbertos.clear();
        erros.clear();
        aparicoes.clear();

        boolean abriu = false;
        boolean fechou = false;
        boolean avisouForaDoCaso = false;
        boolean avisouDepoisDoFim = false;
        int linhaDoFim = 0;

        for (int i = 0; i < caso.getTotalDeLinhas(); i++) {
            int numero = i + 1;
            String[] palavras = leitor.separar(caso.getLinha(i));

            //linha em branco nao conta
            if (palavras.length == 0) {
                continue;
            }

            String primeira = palavras[0];

            //a marca do Red John, o resto da linha eh livre
            if (primeira.equals(":)")) {
                continue;
            }

            //aqui o Leitor entra: cada palavra ganha a categoria dela
            String[] tipos = leitor.classificar(palavras);

            //CASO ABERTO e CASO ENCERRADO sao a moldura do programa
            if (primeira.equals("CASO")) {
                if (palavras.length < 2) {
                    erros.add(new Erro(numero, "CASO do que? falta ABERTO ou ENCERRADO"));
                } else if (palavras[1].equals("ABERTO")) {
                    if (abriu) {
                        erros.add(new Erro(numero, "esse caso ja tinha sido aberto antes"));
                    }
                    abriu = true;
                } else if (palavras[1].equals("ENCERRADO")) {
                    if (!abriu) {
                        erros.add(new Erro(numero, "encerrou um caso que nunca foi aberto"));
                    }
                    fechou = true;
                    linhaDoFim = numero;
                } else {
                    erros.add(new Erro(numero, "depois de CASO vem ABERTO ou ENCERRADO, veio \""
                            + palavras[1] + "\""));
                }
                continue;
            }

            //daqui pra baixo eh linha de comando, entao ela precisa estar dentro do caso
            if (!abriu && !avisouForaDoCaso) {
                erros.add(new Erro(numero, "essa linha esta fora do caso, falta o CASO ABERTO"));
                avisouForaDoCaso = true;
            }
            if (fechou && !avisouDepoisDoFim) {
                erros.add(new Erro(numero, "o caso ja foi encerrado na linha " + linhaDoFim));
                avisouDepoisDoFim = true;
            }

            //o alibi eh o outro caminho da suspeita, entao mexe no bloco do topo
            if (primeira.equals("alibi")) {
                tratarAlibi(numero);
                continue;
            }

            //arquivada e confessou fecham bloco
            if (primeira.equals("arquivada") || primeira.equals("confessou")) {
                tratarFechamento(primeira, numero);
                continue;
            }

            //a atribuicao eh a unica que comeca com o nome da pista, por isso a chave eh a categoria
            String chave = primeira;
            if (tipos[0].equals(Leitor.NOME)) {
                chave = "nome";
            }

            Comando comando = equipe.get(chave);
            if (comando == null) {
                erros.add(new Erro(numero, "ninguem na equipe sabe fazer \"" + primeira + "\""));
                continue;
            }

            //a Jane chama analisar sem saber qual comando eh
            Erro achado = comando.analisar(palavras, tipos, numero);
            if (achado != null) {
                erros.add(achado);
            }
            contar(chave);

            //se esse comando abre bloco, empilha pra conferir o fechamento depois
            if (comando instanceof AbreBloco) {
                AbreBloco bloco = (AbreBloco) comando;
                blocosAbertos.add(new BlocoAberto(primeira, bloco.getFechamento(),
                        numero, bloco.aceitaAlibi()));
            }
        }

        conferirFinal(abriu, fechou, avisouForaDoCaso, caso.getTotalDeLinhas());
    }

    private void tratarAlibi(int numero) {
        if (blocosAbertos.isEmpty()) {
            erros.add(new Erro(numero, "alibi sem nenhuma suspeita aberta"));
            return;
        }
        BlocoAberto topo = blocosAbertos.get(blocosAbertos.size() - 1);
        if (!topo.aceitaAlibi()) {
            erros.add(new Erro(numero, "alibi so entra dentro de suspeita, e o bloco aberto aqui eh o "
                    + topo.getAbertura() + " da linha " + topo.getLinha()));
            return;
        }
        if (topo.jaTeveAlibi()) {
            erros.add(new Erro(numero, "essa suspeita ja tinha um alibi na linha "
                    + topo.getLinhaDoAlibi()));
            return;
        }
        topo.marcarAlibi(numero);
    }

    private void tratarFechamento(String palavra, int numero) {
        if (blocosAbertos.isEmpty()) {
            erros.add(new Erro(numero, "\"" + palavra + "\" sem nenhum bloco aberto"));
            return;
        }
        BlocoAberto topo = blocosAbertos.get(blocosAbertos.size() - 1);
        if (!topo.getFechamento().equals(palavra)) {
            erros.add(new Erro(numero, "\"" + palavra + "\" fechou o " + topo.getAbertura()
                    + " da linha " + topo.getLinha() + ", que termina com \""
                    + topo.getFechamento() + "\""));
        }
        //tira da pilha de qualquer jeito, senao um erro so bagunca o resto do caso
        blocosAbertos.remove(blocosAbertos.size() - 1);
    }

    private void conferirFinal(boolean abriu, boolean fechou,
                               boolean jaAvisou, int totalDeLinhas) {
        //se ja avisou na linha do comando, nao repete a mesma queixa aqui
        if (!abriu) {
            if (!jaAvisou) {
                erros.add(new Erro(1, "faltou o CASO ABERTO no comeco"));
            }
        } else if (!fechou) {
            erros.add(new Erro(totalDeLinhas, "o caso nunca foi encerrado, falta o CASO ENCERRADO"));
        }

        //o que sobrou na pilha eh bloco que ninguem fechou
        for (int i = 0; i < blocosAbertos.size(); i++) {
            BlocoAberto bloco = blocosAbertos.get(i);
            erros.add(new Erro(bloco.getLinha(), "o " + bloco.getAbertura()
                    + " abriu aqui e nunca fechou, falta o \"" + bloco.getFechamento() + "\""));
        }
    }

    private void contar(String chave) {
        Integer quantas = aparicoes.get(chave);
        if (quantas == null) {
            aparicoes.put(chave, 1);
        } else {
            aparicoes.put(chave, quantas + 1);
        }
    }

    //quando o caso esta limpo, a Jane reconta ele organizado, com recuo por bloco
    public void recontar(Caso caso) {
        int nivel = 0;
        for (int i = 0; i < caso.getTotalDeLinhas(); i++) {
            String[] palavras = leitor.separar(caso.getLinha(i));
            if (palavras.length == 0) {
                continue;
            }
            String primeira = palavras[0];

            if (primeira.equals("arquivada") || primeira.equals("confessou")
                    || primeira.equals("alibi")) {
                nivel = nivel - 1;
            }
            if (nivel < 0) {
                nivel = 0;
            }

            System.out.println(espacos(nivel) + juntar(palavras));

            if (primeira.equals("suspeita") || primeira.equals("interroga")
                    || primeira.equals("alibi")) {
                nivel = nivel + 1;
            }
        }
    }

    private String espacos(int nivel) {
        String texto = "";
        for (int i = 0; i < nivel * 4; i++) {
            texto = texto + " ";
        }
        return texto;
    }

    private String juntar(String[] palavras) {
        String texto = "";
        for (int i = 0; i < palavras.length; i++) {
            if (i > 0) {
                texto = texto + " ";
            }
            texto = texto + palavras[i];
        }
        return texto;
    }
}
