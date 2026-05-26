public class ComparadorDeArquivos {

    private final SimilaridadeEstrategia similaridadeEstrategia;

    public ComparadorDeArquivos(SimilaridadeEstrategia similaridadeEstrategia) {
        this.similaridadeEstrategia = similaridadeEstrategia;
    }

    public double calcularSimilaridade(Arquivo docA, Arquivo docB) {

        return this.similaridadeEstrategia.calcularSimilaridade(docA, docB);
    }
}
