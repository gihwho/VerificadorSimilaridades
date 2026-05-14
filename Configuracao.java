import java.io.File;

public class Configuracao {
    private File diretorio;
    private Double limiar;
    private String[] args;

    public File getDiretorio() {
        return diretorio;
    }

    public Double getLimiar() {
        return limiar;
    }

    public String getArg(int index) {
        if(index > 0 && index < args.length) {
            return args[index];
        }

        System.out.println("Argumento não encontrado!");
        return "";
    }

    public String[] getArgs() {
        return args;
    }

    public Configuracao(File diretorio, Double limiar, String[] args) {
        this.diretorio = diretorio;
        this.limiar = limiar;
        this.args = args;
    }
}