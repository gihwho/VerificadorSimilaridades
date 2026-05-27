public class SimilaridadeCosseno{

    public double calcularSimilaridade(Documento docA, Documento docB) {

        double produtoEscalar = calcularProdutoEscalar(docA.getFrequencias(), docB.getFrequencias());
        double magnitudeA = calcularMagnitude(docA.getFrequencias());
        double magnitudeB = calcularMagnitude(docB.getFrequencias());

        return produtoEscalar / (magnitudeA * magnitudeB);
    }

    private static double calcularProdutoEscalar(HashTable<String, Integer> frequenciasA, HashTable<String, Integer> frequenciasB) {
        double total = 0.0;

        for(String palavra : frequenciasA.getKeys()) {
            Integer freqItemA = frequenciasA.get(palavra);
            Integer freqItemB = frequenciasB.get(palavra);

            if(freqItemA == null){
                freqItemA = 0;
            }
            if(freqItemB == null){
                freqItemB = 0;
            }

            total += freqItemA * freqItemB;
        }
        return total;
    }

    private static  double calcularMagnitude(HashTable<String, Integer> frequencias) {
        double total = 0.0;

        for(String palavra : frequencias.getKeys()) {
            Integer freq = frequencias.get(palavra);

            if(freq == null){
                continue;
            }
            
            total += freq * freq;
        }

        return Math.sqrt(total);
    }
}
