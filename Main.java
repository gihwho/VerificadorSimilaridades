/*
Nome: Antônio Costa Satiro de Souza - RA: 10723636 
Nome: Giovanna Borges Coelho - RA: 10756784
*/

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final String ARQUIVO_STOP_WORDS = "ptbr.txt";
    public static void main(String[] args) {

        // Validação dos argumentos de entrada
        if(args.length < 3) {
            System.out.println("Número de argumentos informados inferior ao esperado! Uso correto: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais].");
            return;
        }

        String diretorioDocumentos = args[0]; // Primeiro argumento referente ao diretório em que estão os arquivos

        File caminho = new File(diretorioDocumentos);

        // Validação do argumento <diretorio_documento> informado
        if(!caminho.exists() || !caminho.isDirectory()) {
            System.out.println("Caminho não encontrado ou não é um diretório!");
            return;
        }

        // Validação do limiar informado
        double limiar = 0.0;
        try {
            limiar = Double.parseDouble(args[1]);
        }
        catch (NumberFormatException ex) {
            System.out.println("Erro! Limiar deve ser um número decimal");
            ex.printStackTrace();
            return;
        }

        String modo = args[2];

        Configuracao configuracao = new Configuracao(caminho, limiar, args);

        try {
            Set<String> stopWords = carregarStopWords();
            ComparadorDeDocumentos comparadorDeDocumentos = new ComparadorDeDocumentos(new SimilaridadeCosseno());

            switch (modo) {
                case "lista": executarModoLista(configuracao, stopWords, comparadorDeDocumentos); break;
                case "topK": executarModoTopK(configuracao, stopWords, comparadorDeDocumentos); break;
                case "busca": executarModoBusca(configuracao, stopWords, comparadorDeDocumentos); break;
            }
        }
        catch (IOException ex) {
            System.out.println("Não foi possível carregar o arquivo com os stopWords!");
            ex.printStackTrace();
        }
        catch (Exception ex) {
            System.out.println("Ocorreu um erro inesperado!");
            ex.printStackTrace();
        }
    }

    private static Set<String> carregarStopWords() throws IOException {
        List<String> linhas = Files.readAllLines(Paths.get(ARQUIVO_STOP_WORDS));
        Set<String> stopWords = new HashSet<>();

        for (String linha : linhas) {
            String palavraLimpa = linha.trim().toLowerCase();
            if (!palavraLimpa.isEmpty()) {
                stopWords.add(palavraLimpa);
            }
        }

        return stopWords;
    }

    private static boolean verificarArquivoValido(File arquivo) {
        return arquivo.isFile() && arquivo.getName().endsWith(".txt");
    }

    public static ArrayList<Documento> recuperarArquivosETransformarParaDocumento(Configuracao configuracao) {
        ArrayList<Documento> documentos = new ArrayList<>();
        File[] arquivos = configuracao.getDiretorio().listFiles();

        if(arquivos != null) {
            for(File arquivo : arquivos) {
                if(verificarArquivoValido(arquivo)){
                    documentos.add(new Documento(arquivo.getName(), arquivo.getPath()));
                }
            }
        }

        return documentos;
    }

    public static void executarModoLista(Configuracao configuracao, Set<String> stopWords, ComparadorDeDocumentos comparadorDeDocumentos) {

        ArrayList<Documento> documentos = recuperarArquivosETransformarParaDocumento(configuracao);

        if(documentos.size() > 0){
            processarDocumentos(documentos, stopWords, comparadorDeDocumentos, null, configuracao.getLimiar());
        }
        else {
            System.out.println("Não foi possível completar a leitura dos arquivos contidos no diretório: " + configuracao.getDiretorio().getPath());
        }
    }

    public static void executarModoTopK(Configuracao configuracao, Set<String> stopWords, ComparadorDeDocumentos comparadorDeDocumentos) {
        int topK = 0;

        try {
            topK = Integer.parseInt(configuracao.getArg(3));
        }
        catch (NumberFormatException ex) {
            System.out.println("Ocorreu um erro ao obter o valor de topK! Um número inteiro deve ser passado como argumento!");
            return;
        }

        ArrayList<Documento> documentos = recuperarArquivosETransformarParaDocumento(configuracao);

        if(documentos.size() > 0) {
            processarDocumentos(documentos, stopWords, comparadorDeDocumentos, topK, configuracao.getLimiar());
        }
        else {
            System.out.println("Não foi possível completar a leitura dos arquivos contidos no diretório: " + configuracao.getDiretorio().getPath());
        }
    }

    public static void executarModoBusca(Configuracao configuracao, Set<String> stopWords, ComparadorDeDocumentos comparadorDeDocumentos) {

        if (configuracao.getArgs().length < 5) {
            System.out.println("Erro: informe dois arquivos para comparar!!");
            return;
        }

        String nomeArquivo1 = configuracao.getArg(3);
        String nomeArquivo2 = configuracao.getArg(4);

        File arquivo1 = new File(configuracao.getDiretorio(), nomeArquivo1);
        File arquivo2 = new File(configuracao.getDiretorio(), nomeArquivo2);

        if (!arquivo1.exists()) {
            System.out.println("Erro: arquivo nao encontrado: " + nomeArquivo1);
            return;
        } else if (!arquivo2.exists()) {
            System.out.println("Erro: arquivo nao encontrado: " + nomeArquivo2);
            return;
        }

        try {
            Documento doc1 = new Documento(arquivo1.getName(), arquivo1.getPath());
            Documento doc2 = new Documento(arquivo2.getName(), arquivo2.getPath());

            doc1.analisar(stopWords);
            doc2.analisar(stopWords);

            double similaridade = comparadorDeDocumentos.calcularSimilaridade(doc1, doc2);

            String saida = "";
            saida += "=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n";
            saida += "Comparando: " + doc1.getNome() + " <-> " + doc2.getNome() + "\n";
            saida += "Similaridade calculada: " + String.format(java.util.Locale.US, "%.2f", similaridade) + "\n";
            saida += "Metrica utilizada: Cosseno\n";

            System.out.println(saida);
            gerarArquivoSaida(saida);

        } catch (IOException e) {
            System.out.println("Erro ao ler os arquivos no modo busca!");
        }
    }

    public static void processarDocumentos(ArrayList<Documento> documentos, Set<String> stopWords,
                                           ComparadorDeDocumentos comparador, Integer topK, double limiar)
    {
        try {
            for(Documento documento : documentos)
                documento.analisar(stopWords);

            ArrayList<ArrayList<Documento>> paresDocumentos = criarParesDocumentos(documentos);

            AVLTree tree = new AVLTree();

            for(ArrayList<Documento> par : paresDocumentos) {
                Documento doc1 = par.get(0);
                Documento doc2 = par.get(1);

                double similaridade = comparador.calcularSimilaridade(doc1, doc2);

                tree.inserir(new Resultado(doc1.getNome(), doc2.getNome(), similaridade));
            }

            processarResultados(documentos.size(), paresDocumentos.size(), limiar, tree, topK);
        }
        catch (Exception ex) {
            throw  new RuntimeException("Ocorreu um erro inesperado! Não foi possível processar os documentos!");
        }
    }

    public static ArrayList<ArrayList<Documento>> criarParesDocumentos(ArrayList<Documento> documentos) {
        ArrayList<ArrayList<Documento>> pares = new ArrayList<>();

        for(int i = 0; i < documentos.size(); i++) {

            for(int j = i + 1; j < documentos.size(); j++) {
                pares.add(
                        new ArrayList<>(List.of(documentos.get(i), documentos.get(j)))
                );
            }
        }

        return pares;
    }

    public static void processarResultados(int totalDocs, int totalParesDocs, double similaridade, AVLTree arvore, Integer topK) {

        ArrayList<String> paresMaiores = arvore.exibirMaiores(similaridade, topK);
        ArrayList<String> paresMenores = arvore.exibirMenores(similaridade);

        String s = String.format("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n" +
                                "Total de documentos processados: %d\n" +
                                "Total de pares comparados: %d\n" +
                                "Função hash utilizada: Polinomial (base 31)\n" +
                                "Métrica de similaridade: Cosseno\n\n" +
                                "Pares com similaridade >= %.2f\n" +
                                "---------------------------------\n" +
                                "%s\n" +
                                "\nPares com menor similaridade\n" +
                                "---------------------------------\n" +
                                "%s", totalDocs, totalParesDocs, similaridade,
                                String.join("\n", paresMaiores),
                                String.join("\n", paresMenores));

        System.out.println(s);
        gerarArquivoSaida(s);
    }

    public static void gerarArquivoSaida(String resultado) {
        Path caminho =  Path.of("resultado.txt");

        try {
             Files.writeString(caminho, resultado);
             System.out.println("\n\nArquivo de saída gerado com sucesso!");
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
