import java.util.ArrayList;
import java.util.LinkedList;
/* Implementacao manual de uma hash table generica.
 *  
 * A classe deve gerenciar um array interno e usar 2 funcoes de dispersao.
 *  
 * O tratamento de colisoes utilizado foi endereçamento fechado com LinkedLists.
 * 
 */

public class HashTable<K, V> {

    public static class Entry<K, V> {
        final K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<Entry<K, V>>[] table; // array de 'LinkedLists' que contem 'Entrys'
    private int size; // Numero total de pares chave-valor armazenados
    private final int capacity; // O tamanho do array 'tabela' (número de buckets)
    private final int hashFunctionChoice;

    // Construtor
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, int hashFunctionChoice) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacidade inicial deve ser maior que zero.");
        }
        this.capacity = initialCapacity;
        this.size = 0;
        this.hashFunctionChoice = hashFunctionChoice;
        this.table = (LinkedList<Entry<K, V>>[]) new LinkedList[capacity];


        /* Essa lógica parte do exemplo de implementação do professor:
         * Precisamos inicializar CADA posição do array com uma lista vazia.
         * Sem isso teremos NullPointerException ao tentar adicionar o primeiro item em um bucket.
         */

        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }

    }
    
    private int hash(K key) {
        int hashCode;

        if (hashFunctionChoice == 1) {
            hashCode = hashFunction1(key);
        } else {
            hashCode = hashFunction2(key);
        }

        return Math.abs(hashCode) % capacity;
    }

    /* FUNÇÃO DE DISPERSÃO 1: Polinomial (Base 31) - A "BOA"
     * Justificativa: Padrao da industria (similar ao Java), rapida e
     * com excelente distribuicao para Strings, pois considera a ORDEM.
     */
    private int hashFunction1(K key) {
        String strKey = (String) key;
        int hash = 0;
        int P = 31;

        for (int i = 0; i < strKey.length(); i++) {
            hash = (P * hash + strKey.charAt(i));
        }
        return hash;
    }
   
    /* FUNÇÃO DE DISPERSÃO 2: Soma Simples - A "RUIM"
     * Justificativa: Implementada para comparação. É uma função de baixa
     * qualidade que NAO considera a ordem (ex: "ato" e "toa" terao o
     * mesmo hash), o que deve gerar muitas colisoes.
     */
    private int hashFunction2(K key) {
        String strKey = (String) key;
        int hash = 0;

        for (int i = 0; i < strKey.length(); i++) {
            hash = hash + strKey.charAt(i);
        }
        return hash;
    }
    
    // Inserir a entry ou atualizar o valor
    public void put(K key, V value) { 
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                // CASO 1: CHAVE ENCONTRADA -> ATUALIZAÇÃO DO VALOR
                entry.value = value;
                return;
            }
        }

        // CASO 2: CHAVE NÃO ENCONTRADA -> INSERÇÃO
        Entry<K, V> newEntry = new Entry<>(key, value);
        bucket.add(newEntry);
        size++;
    }
    
    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }
    
    public boolean remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table[index];

        // Remova o entry SE a chave dele for igual a chave passada
        boolean removed = bucket.removeIf(entry -> entry.key.equals(key));

        if (removed) {
            size--;
        }
        
        return removed;
    }

    public int getSize() {
        return size;
    }
    
    //Método de estatísticas de colisão
    public String getCollisionStats() {
    int totalBuckets = capacity;
    int emptyBuckets = 0;
    int usedBuckets = 0;
    int maxBucketSize = 0;
    int totalCollisions = 0;
    int totalElements = size;

    //percorre todos os buckets da table
    for (int i = 0; i < totalBuckets; i++) {
        LinkedList<Entry<K, V>> bucket = table[i];

        //trata bucket nulo ou vazio como "vazio"
        if (bucket == null || bucket.isEmpty()) {
            emptyBuckets++;
        } else {
            usedBuckets++;
            int bucketSize = bucket.size();

            //atualiza o maior tamanho de bucket encontrado
            if (bucketSize > maxBucketSize) {
                maxBucketSize = bucketSize;
            }

            //colisoes aproximadas: para n elementos no mesmo bucket, n - 1 colisões
            if (bucketSize > 1) {
                totalCollisions += (bucketSize - 1);
            }
        }
    }

    double loadFactor = (totalBuckets == 0)
            ? 0.0
            : (double) totalElements / totalBuckets;

    StringBuilder sb = new StringBuilder();
    sb.append("ESTATÍSTICAS DE TABELA HASH \n");

    sb.append("Função de hash utilizada: ");
    if (hashFunctionChoice == 1) {
        sb.append("Polinomial (base 31)");
    } else if (hashFunctionChoice == 2) {
        sb.append("Soma simples dos caracteres");
    } else {
        sb.append("Desconhecida (código: ").append(hashFunctionChoice).append(")");
    }

    sb.append('\n');
    sb.append("Capacidade (número de buckets): ").append(totalBuckets).append('\n');
    sb.append("Elementos armazenados: ").append(totalElements).append('\n');
    sb.append("Fator de carga (load factor): ")
        .append(String.format("%.4f", loadFactor)).append('\n');
    sb.append("Buckets vazios: ").append(emptyBuckets).append('\n');
    sb.append("Buckets usados: ").append(usedBuckets).append('\n');
    sb.append("Tamanho máximo de bucket: ").append(maxBucketSize).append('\n');
    sb.append("Colisões totais (aprox.): ").append(totalCollisions).append('\n');

    return sb.toString();
    }

    public ArrayList<K> getChaves() {
        ArrayList<K> chaves = new ArrayList<>();

        for (LinkedList<Entry<K, V>> entries : table) {
            if (entries != null) {
                for (Entry<K, V> doc : entries) {
                    K chave = doc.key;
                    chaves.add(chave);
                }
            }
        }

        return chaves;
    }
}