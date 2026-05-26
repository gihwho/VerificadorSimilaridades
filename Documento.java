import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set; // Set para as stop words
import java.text.Normalizer;

public class Documento {
    private String nome;
    private String caminho;
    private HashTable<String, Integer> frequencias;

    private static final String ARQUIVO_STOP_WORDS = "ptbr.txt";

    public String getCaminho() {
        return caminho;
    }

    public String getNome() {
        return nome;
    }

    public HashTable<String, Integer> getFrequencias() {
        return frequencias;
    }

    public Documento(String nome, String caminho) {
        this.nome = nome;
        this.caminho = caminho;
    }

    
    public void analisar(Set<String> stopWords) throws IOException {
        
        String[] palavras = fragmentar(limpar(carregarTexto(caminho)));

        frequencias = new HashTable<>(palavras.length, 1);

        contarOcorrencias(palavras, stopWords);
    }

    // adquire texto baseado no caminho do arquivo
    private String carregarTexto(String caminhoArquivo) throws IOException {
        return Files.readString(Paths.get(caminhoArquivo));
    }

    // limpeza do texto original para analise
    private String limpar(String textoOriginal) {
        return removeSimbolos(removeAcentos(textoOriginal.toLowerCase()));   
    }

    // helper de limpar()
    private String removeAcentos(String texto){
        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return texto.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // helper de limpar()
    private String removeSimbolos(String texto){
        return texto.replaceAll("[^a-z0-9\\s]", "");
    }

    private String[] fragmentar(String textoNormalizado) {
        return textoNormalizado.trim().split("\\s+");
    }

    private void contarOcorrencias(String[] tokens, Set<String> stopWords) {
        for (String token : tokens) {

            if (token.length() < 2) {
                continue;
            }
            
            if (stopWords.contains(token)) {
                continue;
            }
            
            int frequencia = getOrDefault(token);
            frequencias.put(token, frequencia + 1);
        }
    }

    private int getOrDefault(String token){

        int valor = frequencias.get(token);

        if(Integer.valueOf(valor).equals(null)){
            return 0;
        }

        return valor;
    }
}