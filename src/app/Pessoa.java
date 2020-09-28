package app;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Pessoa implements Comparable{
    protected int RG;
    protected String nome;
    protected String dataNasc;

    /***
     * Construtor da classe pessoa
     * @param rg RG
     * @param nome Nome
     * @param nasc Data de nascimento
     */
    public Pessoa(int rg, String nome, String nasc){
        this.RG = rg;
        this.nome = nome;
        this.dataNasc = nasc;
    }

    /**
     *  Retorna uma pessoa separador, usado em ordenção e indice
     * @return
     */
    public static Pessoa separador(){
        return new Pessoa(-1, "","");
    }

    /**
     * Metodo para uma pessoa em um arquivo
     * @param file
     * @return
     * @throws IOException
     */
    public boolean saveToFile(RandomAccessFile file) throws IOException{
        file.seek(file.length());
        file.writeInt(this.RG);
        file.writeUTF(this.nome);
        file.writeUTF(this.dataNasc);
        return true;
    }

    /**
     * Salvar uma pessoa no arquivo baseado em ua posição
     * @param file
     * @param pos
     * @return
     * @throws IOException
     */
    public boolean saveToFile(RandomAccessFile file, long pos) throws IOException{
        file.seek(pos);
        file.writeInt(this.RG);
        file.writeUTF(this.nome);
        file.writeUTF(this.dataNasc);
        return true;
    }

    /**
     * Metodo que faz a leitura de uma pessoa de um arquivo
     * @param dados
     * @return
     * @throws IOException
     */
    public static Pessoa readFromFile(RandomAccessFile dados) throws IOException{
        Pessoa nova = Pessoa.separador();

        int rg = dados.readInt();
        String nome = dados.readUTF();
        String nasc = dados.readUTF();
        nova = new Pessoa(rg,nome,nasc);

        return nova;
    }

    /***
     * Metodo que le uma pessoa de um arquivo baseado na posição
     * @param dados
     * @param pos
     * @return
     * @throws IOException
     */
    public static Pessoa readFromFile(RandomAccessFile dados, long pos) throws IOException{
        Pessoa nova = Pessoa.separador();
        dados.seek(pos);
        int rg = dados.readInt();
        String nome = dados.readUTF();
        String nasc = dados.readUTF();
        nova = new Pessoa(rg,nome,nasc);

        return nova;
    }

    @Override
    public int compareTo(Object o) { //-1 se for menor; 1 se for maior ou 0 em empate
        Pessoa outra = (Pessoa)o;

        //verifica primeiro se a comparação esta sendo feita com um separador;
        //pois sem essa verificação, o compareTo retorna que o separador é menor que uma pessoa.
          
       if(this.RG != -1 && outra.RG == -1) return -1;
       else if(this.RG == -1 && outra.RG != -1) return 1;
        if(this.nome.compareTo(outra.nome) <0) return -1;
        else if(this.nome.compareTo(outra.nome) >0) return 1;

        return 0;
    }

    public String toString(){
        return "Nome: "+nome+" RG: "+RG+" Data de nascimento: "+dataNasc;
    }

    public boolean EUmSeparador(){
        if(this.RG == -1){
            return true;
        }
        
        return false;
    }
}
