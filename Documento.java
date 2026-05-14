import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set; // Set para as stop words
import java.text.Normalizer;

public class Documento {
    private String nome;
    private String caminho;
    private HashTable<String, Integer> tabelaFrequencias;

    private static final String ARQUIVO_STOP_WORDS = "ptbr.txt";

    public String getCaminho() {
        return caminho;
    }

    public String getNome() {
        return nome;
    }

    public HashTable<String, Integer> getTabelaFrequencias() {
        return tabelaFrequencias;
    }

    public Documento(String nome, String caminho) {
        this.nome = nome;
        this.caminho = caminho;
    }

    /**
     * Metodo principal
     * Define a "trilha" de processamento
     */
    public void processarArquivo(Set<String> stopWords) throws IOException {
        // Passo 1: Ler
        String textoBruto = lerTextoBruto(caminho);

        // Passo 2: Limpar
        String textoNormalizado = normalizar(textoBruto);


        // Passo 3: Quebrar
        String[] tokens = tokenizar(textoNormalizado);

        tabelaFrequencias = new HashTable<>(tokens.length, 1);

        // Passo 4: Contar
        popularTabela(tokens, stopWords);
    }

    /**
     * PASSO 1: Leitura
     * LE todo o conteduo do arquivo para uma unica String.
     */
    private String lerTextoBruto(String caminhoArquivo) throws IOException {
        return Files.readString(Paths.get(caminhoArquivo));
    }

    /**
     * PASSO 2: Normalizacao
     * Converte para minusculas
     * Remove ACENTOS (transforma 'á' em 'a', 'õ' em 'o', etc)
     * Remove pontuacao e simbolos
     */
    private String normalizar(String textoBruto) {

        String texto = textoBruto.toLowerCase();
        
        // 2. Normaliza para decompor os acentos (Forma NFD)
        // Isso separa o 'a' do '´' em "á"
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        
        // 3. Remove os acentos
        texto = texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
        // 4. Agora sim, remove tudo que nao for letra (a-z), numero (0-9) ou espaço
        return texto.replaceAll("[^a-z0-9\\s]", "");
    }

    /**
     * PASSO 3: Tokenizacao
     */
    private String[] tokenizar(String textoNormalizado) {
        return textoNormalizado.trim().split("\\s+");
    }

    /**
     * PASSO 4: Contagem
     * Popula a tabela hash, ignorando stop words.
     */
    private void popularTabela(String[] tokens, Set<String> stopWords) {
        for (String token : tokens) {
            // Ignora tokens vazios ou muito curtos
            if (token.isEmpty()) {
                continue;
            }
            
            if (stopWords.contains(token)) {
                continue; // Pula para a próxima palavra
            }
            
            // Lógica de contagem na HashTable
            Integer frequenciaAtual = tabelaFrequencias.get(token);
            
            if (frequenciaAtual == null) {
                // Primeira vez que vemos a palavra
                tabelaFrequencias.put(token, 1);
            } else {
                // Palavra já existe, incrementa a contagem
                tabelaFrequencias.put(token, frequenciaAtual + 1);
            }
        }
    }
}