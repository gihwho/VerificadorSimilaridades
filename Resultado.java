/* Estrutura de dados simples para armazenar o resultado de uma comparacao.
 *  
 * Classe similar a uma struct, armazzena apenas os nomes dos documentos comparados
 * e o valor da similaridade calculada.
 * 
 * (Objetos dessa classe serão armazenados nas listas dentro dos nós da AVL)
 * 
 */

public class Resultado {
    
    // Atributos
    private String doc1;
    private String doc2;
    private double similaridade;

    // Construtor
    public Resultado(String doc1, String doc2, double similaridade) {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.similaridade = similaridade;
    }

    // Metodos get
    public String getDoc1() {
        return doc1;
    }

    public String getDoc2() {
        return doc2;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    // Método de impressao toString - ex: "doc1.txt <-> doc2.txt = 0.64"
    @Override
    public String toString() {
        String similaridadeFormat = String.format("%.2f", similaridade);
        return doc1 + " <-> " + doc2 + " = " + similaridadeFormat;
    }
 }