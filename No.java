import java.util.ArrayList;

class No {
    public double getChave() {
        return chave;
    }

    public void setChave(double chave) {
        this.chave = chave;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public ArrayList<Resultado> getPares() {
        return this.pares;
    }

    public void adicionarPar(Resultado res) {
        this.pares.add(res);
    }

    public No getDireita() {
        return direita;
    }

    public void setDireita(No direita) {
        this.direita = direita;
    }

    public No getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(No esquerda) {
        this.esquerda = esquerda;
    }

    private double chave;
    private int altura;
    private ArrayList<Resultado> pares;
    private No direita;
    private No esquerda;

    public No(Resultado res) {
        this.chave = res.getSimilaridade();
        this.altura = 1;
        this.pares = new ArrayList<>();
        this.pares.add(res);
    }
}