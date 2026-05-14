import java.util.ArrayList;

public class BST {
    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }

    protected No raiz;

    public void inserir(double chave, Resultado resultado) {
        raiz = inserirRecursivo(raiz, resultado);
    }

    protected No inserirRecursivo(No no, Resultado resultado) {
        if(no == null) {
            return new No(resultado);
        }

        if(resultado.getSimilaridade() < no.getChave()) {
            No novoNo = inserirRecursivo(no.getEsquerda(), resultado);
            no.setEsquerda(novoNo);
        }
        else if(resultado.getSimilaridade() > no.getChave()) {
            No novoNo = inserirRecursivo(no.getDireita(), resultado);
            no.setDireita(novoNo);
        }
        else {
            no.adicionarPar(resultado);
        }

        return no;
    }

    public void emOrdemReverso() {
        emOrdemReversoRecursivo(this.raiz);
    }

    private void emOrdemReversoRecursivo(No no) {
        if (no != null) {
            emOrdemReversoRecursivo(no.getDireita());

            ArrayList<Resultado> pares = no.getPares();

            for (Resultado par : pares) {
                String s = String.format("%s = %.2f", par.toString(), no.getChave());
                System.out.println(s);
            }

            emOrdemReversoRecursivo(no.getEsquerda());
        }
    }
}
