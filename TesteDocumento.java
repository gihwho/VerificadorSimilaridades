import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TesteDocumento {

    public static void main(String[] args) {
        System.out.println("=== INICIO ===");
        String caminhoStopWords = "ptbr.txt";
        String caminhoDocumento = "documentos/doc1.txt";

        try {
            System.out.println("Lendo stop words de: " + caminhoStopWords);
            Set<String> stopWords = carregarStopWords(caminhoStopWords);
            System.out.println("Total de stop words carregadas: " + stopWords.size());

            System.out.println("Lendo e processando documento: " + caminhoDocumento);
            
            Documento doc = new Documento(caminhoDocumento, "");

            HashTable<String, Integer> tabela = doc.getTabelaFrequencias();
            
            System.out.println("\n--- Relatório de Processamento ---");
            System.out.println("Arquivo processado: " );
            System.out.println("Total de palavras únicas (vocabulário): " + tabela.getSize());

            String palavraTeste = "computador"; 
            Integer frequencia = tabela.get(palavraTeste);
            
            if (frequencia != null) {
                System.out.println("A palavra '" + palavraTeste + "' aparece " + frequencia + " vezes");
            } else {
                System.out.println("A palavra '" + palavraTeste + "' não foi encontrada ");
            }
            
            System.out.println("\n--- Collision Stats HashTable ---");
            System.out.println(tabela.getCollisionStats());

        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO: Não foi possível ler um dos arquivos.");
        }
        
        System.out.println("\n=== FIM ===");
    }

    private static Set<String> carregarStopWords(String caminho) throws IOException {
        List<String> linhas = Files.readAllLines(Paths.get(caminho));
        Set<String> stopWords = new HashSet<>();
        
        for (String linha : linhas) {
            String palavraLimpa = linha.trim().toLowerCase();
            if (!palavraLimpa.isEmpty()) {
                stopWords.add(palavraLimpa);
            }
        }
        
        return stopWords;
    }
}
