import java.util.ArrayList;
import java.util.List;


public class Exemplos {

    public List<Caso> todos() {
        List<Caso> lista = new ArrayList<Caso>();

        //----- casos que passam -----

        lista.add(montar("ok_01_completo", new String[] {
                "CASO ABERTO",
                ":) interrogatorio do suspeito",
                "pista mentiras = 3",
                "pista provas = 0",
                "interroga mentiras > 0",
                "    mentiras = mentiras - 1",
                "    provas = provas + 1",
                "confessou",
                "suspeita provas >= 3",
                "    revela provas",
                "alibi",
                "    revela 0",
                "arquivada",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_02_so_suspeita", new String[] {
                "CASO ABERTO",
                "pista provas = 4",
                "suspeita provas > 2",
                "    revela provas",
                "arquivada",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_03_so_interroga", new String[] {
                "CASO ABERTO",
                "pista mentiras = 2",
                "interroga mentiras > 0",
                "    mentiras = mentiras - 1",
                "confessou",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_04_suspeita_dentro_de_interroga", new String[] {
                "CASO ABERTO",
                "pista mentiras = 3",
                "pista provas = 0",
                "interroga mentiras > 0",
                "    provas = provas + 1",
                "    suspeita provas == 2",
                "        revela provas",
                "    arquivada",
                "    mentiras = mentiras - 1",
                "confessou",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_05_interroga_dentro_de_suspeita", new String[] {
                "CASO ABERTO",
                "pista provas = 5",
                "pista tempo = 3",
                "suspeita provas > 4",
                "    interroga tempo > 0",
                "        tempo = tempo - 1",
                "    confessou",
                "alibi",
                "    revela 0",
                "arquivada",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_06_caso_vazio", new String[] {
                "CASO ABERTO",
                "CASO ENCERRADO"
        }));

        lista.add(montar("ok_07_contas_longas", new String[] {
                "CASO ABERTO",
                ":) a conta pode ter quantos operadores precisar",
                "pista total = 2 + 3 * 4 - 1",
                "pista media = total / 2",
                "revela total + media",
                "CASO ENCERRADO"
        }));

        //----- casos com erro -----

        lista.add(montar("erro_01_sem_caso_aberto", new String[] {
                "pista provas = 1",
                "revela provas",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_02_sem_caso_encerrado", new String[] {
                "CASO ABERTO",
                "pista provas = 1",
                "revela provas"
        }));

        lista.add(montar("erro_03_confessou_sem_interroga", new String[] {
                "CASO ABERTO",
                "pista provas = 1",
                "confessou",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_04_bloco_fechado_errado", new String[] {
                "CASO ABERTO",
                "pista mentiras = 2",
                "interroga mentiras > 0",
                "    mentiras = mentiras - 1",
                "arquivada",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_05_bloco_nunca_fecha", new String[] {
                "CASO ABERTO",
                "pista mentiras = 2",
                "interroga mentiras > 0",
                "    mentiras = mentiras - 1",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_06_alibi_sem_suspeita", new String[] {
                "CASO ABERTO",
                "pista provas = 1",
                "alibi",
                "    revela provas",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_07_dois_alibi", new String[] {
                "CASO ABERTO",
                "pista provas = 1",
                "suspeita provas > 0",
                "    revela provas",
                "alibi",
                "    revela 0",
                "alibi",
                "    revela 1",
                "arquivada",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_08_nome_com_acento", new String[] {
                "CASO ABERTO",
                ":) nome com acento nao vale, igual na maioria das linguagens",
                "pista acusacao = 1",
                "pista suspeição = 2",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_09_expressao_incompleta", new String[] {
                "CASO ABERTO",
                "pista provas = 1",
                "provas = provas +",
                "CASO ENCERRADO"
        }));

        lista.add(montar("erro_10_varios_erros", new String[] {
                "CASO ABERTO",
                ":) esse caso tem falha em quase toda linha",
                "pista 2provas = 1",
                "provas provas + 1",
                "suspeita provas",
                "    revela",
                "confessou",
                "alibi",
                "CASO ENCERRADO"
        }));

        return lista;
    }

    private Caso montar(String nome, String[] linhas) {
        Caso caso = new Caso(nome);
        for (int i = 0; i < linhas.length; i++) {
            caso.escrever(linhas[i]);
        }
        return caso;
    }
}
