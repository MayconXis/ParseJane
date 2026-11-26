import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Jane jane = new Jane();

        jane.escalar("pista", new Pista());
        jane.escalar("nome", new Atribuicao());
        jane.escalar("revela", new Revela());
        jane.escalar("suspeita", new Suspeita());
        jane.escalar("interroga", new Interroga());

        Exemplos exemplos = new Exemplos();
        List<Caso> casos = exemplos.todos();

        int limpos = 0;
        int comFalha = 0;

        for (int i = 0; i < casos.size(); i++) {
            Caso caso = casos.get(i);
            jane.investigar(caso);
            List<Erro> erros = jane.getErros();

            System.out.println("=========================================");
            System.out.println("CASO: " + caso.getNome());

            if (erros.isEmpty()) {
                limpos = limpos + 1;
                System.out.println("caso sem falhas");
                System.out.println("-- como a Jane leu o caso --");
                jane.recontar(caso);
                mostrarAparicoes(jane.getAparicoes());
            } else {
                comFalha = comFalha + 1;
                System.out.println(erros.size() + " falha(s) encontrada(s):");
                for (int j = 0; j < erros.size(); j++) {
                    System.out.println("  " + erros.get(j));
                }
            }
            System.out.println();
        }

        System.out.println("=========================================");
        System.out.println("RELATORIO FINAL");
        System.out.println("casos analisados: " + casos.size());
        System.out.println("sem falhas: " + limpos);
        System.out.println("com falha: " + comFalha);
    }

    private static void mostrarAparicoes(Map<String, Integer> aparicoes) {
        if (aparicoes.isEmpty()) {
            return;
        }
        System.out.print("-- comandos usados: ");
        boolean primeiro = true;
        for (Map.Entry<String, Integer> item : aparicoes.entrySet()) {
            if (!primeiro) {
                System.out.print(", ");
            }
            System.out.print(item.getKey() + " x" + item.getValue());
            primeiro = false;
        }
        System.out.println();
    }
}
