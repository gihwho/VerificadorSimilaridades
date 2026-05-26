import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.stream.Collectors;
 

public class HashTable<C, V> {
 
    
    public static class Entrada<C, V> {
        final C chave;
        V valor;
 
        Entrada(C chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }
 
    private LinkedList<Entrada<C, V>>[] tabela;
    private int tamanho;                  
    private final int capacidade;         
    private final int escolhaFuncao; 

    @SuppressWarnings("unchecked")
    public HashTable(int capacidadeInicial, int escolhaFuncao) {
        if (capacidadeInicial <= 0)
            throw new IllegalArgumentException("Capacidade deve ser maior que zero.");
 
        this.capacidade   = capacidadeInicial;
        this.tamanho      = 0;
        this.escolhaFuncao = escolhaFuncao;
        this.tabela = (LinkedList<Entrada<C, V>>[]) new LinkedList[capacidade];

        for (int i = 0; i < capacidade; i++)
            tabela[i] = new LinkedList<>();
    }

    // função principal para escolha de caso de hash
    private int hash(C chave) {
        
        int codigo;
        switch (escolhaFuncao) {
            case 1:
                codigo = hashCaso1(chave);
                break;
            default: 
                codigo = hashCaso2(chave);
                break;
        }
        return (codigo & 0x7FFFFFFF) % capacidade;
    }

    // considera a ORDEM
    private int hashCaso1(C chave) {
        String s = (String) chave;
        int hash = 0;
 
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            hash = 37 * hash + (c * c); 
        }
        return hash;
    }

    // NAO considera a ordem
    private int hashCaso2(C chave) {
        String s = (String) chave;
        int hash = 0;
 
        for (int i = 0; i < s.length(); i++){
            hash = ( hash * s.charAt(i)) % this.tamanho;
        }
        return hash;
    }

    // Inserir nova entrada ou atualizar o valor
    public void put(C chave, V valor) {
        int indice = hash(chave);
        LinkedList<Entrada<C, V>> bucket = tabela[indice];
 
        boolean encontrado = false;
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).chave.equals(chave)) {
                
                bucket.get(i).valor = valor;
                encontrado = true;
                break;
            }
        }
 
        if (!encontrado) {
            bucket.add(new Entrada<>(chave, valor));
            tamanho++;
        }
    }

    // retorna valor por meio da chave
    public V get(C chave) {
        int indice = hash(chave);
        LinkedList<Entrada<C, V>> bucket = tabela[indice];
 
        int i = bucket.size() - 1;
        while (i >= 0) {
            Entrada<C, V> entrada = bucket.get(i);
            if (entrada.chave.equals(chave)){
                return entrada.valor;
            }
            i--;
        }
        return null;
    }
 
    // remove entrada por meio da chave
    public boolean remove(C chave) {
        int indice = hash(chave);
        LinkedList<Entrada<C, V>> bucket = tabela[indice];
 
        int posicao = -1;
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).chave.equals(chave)) {
                posicao = i;
                break;
            }
        }
 
        if (posicao == -1)
            return false;
 
        bucket.remove(posicao); 
        tamanho--;
        return true;
    }
 
    // retorna tamanho
    public int getTamanho() {
        return tamanho;
    }

    // retorna todas as chaves
    public ArrayList<C> getChaves() {
        ArrayList<C> chaves = new ArrayList<>(tamanho);
 
        for (LinkedList<Entrada<C, V>> bucket : tabela) {
            if (bucket == null) continue;         
            for (Entrada<C, V> entrada : bucket)
                chaves.add(entrada.chave);
        }
 
        return chaves;
    }
}