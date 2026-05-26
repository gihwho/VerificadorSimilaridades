import java.util.ArrayList;

public class BST {

    protected No raiz;

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }

    protected No raiz;

    public void inserir(Resultado resultado) {
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

    public void impressaoReverseOrdem() {
        impressaoReverseOrdemRecursivo(this.raiz);
    }

    private void impressaoReverseOrdemRecursivo(No no) {
        if (no != null) {
            impressaoReverseOrdemRecursivo(no.getDireita());

            ArrayList<Resultado> pares = no.getPares();

            for (Resultado par : pares) {
                String s = String.format("%s = %.2f", par.toString(), no.getChave());
                System.out.println(s);
            }

            impressaoReverseOrdemRecursivo(no.getEsquerda());
        }
    }
}
