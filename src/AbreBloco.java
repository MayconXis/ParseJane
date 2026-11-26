public interface AbreBloco {

    //a palavra que fecha esse bloco, tipo arquivada ou confessou
    String getFechamento();

    //so a suspeita aceita um alibi dentro dela
    boolean aceitaAlibi();
}
