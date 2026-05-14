public class ComparadorDeDocumentos {

    private final ISimilaridadeEstrategia similaridadeEstrategia;

    public ComparadorDeDocumentos(ISimilaridadeEstrategia similaridadeEstrategia) {
        this.similaridadeEstrategia = similaridadeEstrategia;
    }

    public double calcularSimilaridade(Documento docA, Documento docB) {

        return this.similaridadeEstrategia.calcularSimilaridade(docA, docB);
    }
}
