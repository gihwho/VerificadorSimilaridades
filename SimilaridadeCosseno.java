 public class SimilaridadeCosseno implements ISimilaridadeEstrategia {

    @Override
    public double calcularSimilaridade(Documento docA, Documento docB) {

        double produtoEscalar = calcularProdutoEscalar(docA, docB);
        double magnitudeDocA = calcularMagnitude(docA);
        double magnitudeDocB = calcularMagnitude(docB);

        return produtoEscalar / (magnitudeDocA * magnitudeDocB);
    }

    private static double calcularProdutoEscalar(Documento docA, Documento docB) {
        double somatorio = 0.0;

        for(String palavra : docA.getTabelaFrequencias().getChaves()) {
            Integer freqA = docA.getTabelaFrequencias().get(palavra);
            Integer freqB = docB.getTabelaFrequencias().get(palavra);

            somatorio += (freqA == null ? 0 : freqA) * (freqB == null ? 0 : freqB);
        }

        return somatorio;
    }

    private static  double calcularMagnitude(Documento doc) {
        double somatorio = 0.0;

        HashTable<String, Integer> tabelaHash = doc.getTabelaFrequencias();

        for(String palavra : tabelaHash.getChaves()) {
            int frequencia = tabelaHash.get(palavra);

            somatorio += Math.pow(frequencia, 2);
        }

        return Math.sqrt(somatorio);
    }
}
