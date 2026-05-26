public class ComparadorDeDocumentos {

    private final SimilaridadeCosseno similaridade;

    public ComparadorDeDocumentos(SimilaridadeCosseno similaridade) {
        this.similaridade = similaridade;
    }

    public double calcularSimilaridade(Documento docA, Documento docB) {
        return this.similaridade.calcularSimilaridade(docA, docB);
    }
}
