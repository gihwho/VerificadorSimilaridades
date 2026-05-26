public class ComparadorDeArquivos {

    private final SimilaridadeCosseno similaridade;

    public ComparadorDeArquivos(SimilaridadeCosseno similaridade) {
        this.similaridade = similaridade;
    }

    public double calcularSimilaridade(Arquivo docA, Arquivo docB) {
        return this.similaridade.calcularSimilaridade(docA, docB);
    }
}
