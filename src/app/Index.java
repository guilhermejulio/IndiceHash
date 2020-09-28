package app;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Index {

    // Inicialmente posição == posição no arquivo de dados
    // quando a posição dos dados é gravada no arquivo hash
    // a posição é trocada para posição na hash
    // LOGO --> RG chave --> Posição no arquivo hash --> Posição no arquivo original
    ElementoHash[] ElementosHash;

    RandomAccessFile ArquivoHash;
    final String path = "ArquivoHash.bin"; // para modificar o caminho do arquivo hash altere
    private String pathDados;

    /**
     * Construtor do indice
     * @param pathDados
     * @throws IOException
     */
    public Index(String pathDados) throws IOException {
        ElementosHash = new ElementoHash[800077];
        lotarIndex();
        this.pathDados = pathDados;
    }

    /**
     * Metodo auxiliar para lotar o array com elementos em branco (separadores)
     * @throws IOException
     */
    private void lotarIndex() throws IOException {
        for (int i = 0; i < ElementosHash.length; i++) {
            ElementosHash[i] = new ElementoHash(-1, 0);
        }
    }

    /**
     * Metodo que realiza o hash de uma pessoa, salva a posição de onde veio [ARQUIVO ORIGINAL] 
     * @param RG
     * @param posDados
     * @throws IOException
     */
    public void salvarPessoa(int RG, long posDados) throws IOException {
        // Com o hash da pessoa, eu salvo no vetor
        // ElementosHash == [ELEMENTO HASH] ==> RG / POSIÇÃO NO ARQUIVO DE DADOS
        int pos = hash(RG);
        ElementosHash[pos] = new ElementoHash(RG, posDados);
    }

    /**
     * Metodo que escreve todas as pessoas salvaS do vetor de Elementos para o Arquivo HASH
     * @throws IOException
     */
    public void escreverPessoas() throws IOException {
        ArquivoHash = new RandomAccessFile(new File(path), "rw");
        ArquivoHash.setLength(0);

        long posHash;

        for(int i=0; i<ElementosHash.length;i++){
            posHash =ArquivoHash.getFilePointer();
            ElementosHash[i].saveToFile(ArquivoHash,posHash);
        }
        ArquivoHash.close();
    }

    /**
     * Metodo que retorna o registro de uma pessoa baseado na chave [RG]
     * @param RG
     * @return
     * @throws IOException
     */
    public Pessoa retornaPessoa(int RG) throws IOException {
        RandomAccessFile dados = new RandomAccessFile(new File(pathDados), "r");
        ArquivoHash = new RandomAccessFile(new File(path), "rw");
        int RGseparado = separarRG(RG);
        int hash = (int) ((Math.pow(RGseparado, 2) % ElementosHash.length));
        boolean sucesso = false;

        while (!sucesso) {

            if(hash >= ElementosHash.length) return Pessoa.separador(); //se percorreu tudo e não achou
            else{
                if (ElementosHash[hash].RG == RG) {
                    sucesso = true;
                } else {
                    hash++;
                }
            }
        }

        long posDados = retornaPosDados(ElementosHash[hash].pos);
        

        Pessoa aux = Pessoa.readFromFile(dados, posDados);

        return aux;

    
    }

    /**
     * Metodo que retorna a posição de uma pessoa do arquivo binario, baseado no HASH
     * @param posHash
     * @return
     * @throws IOException
     */
    public long retornaPosDados(long posHash) throws IOException {
        ArquivoHash.seek(posHash);
        ArquivoHash.readInt();
        return ArquivoHash.readLong();
    }

    /**
     * Meotodo que realiza o hash de um RG
     * metodo de HASH ==> Elevar o meio do RG a 2 não ultrapassando o tamanho do vetor
     * @param RG
     * @return
     */
    public int hash(int RG) {
        boolean sucesso = false;
        int RGseparado = separarRG(RG);
        int hash = (int) ((Math.pow(RGseparado, 2) % ElementosHash.length));

        while (!sucesso) {

            if (ElementosHash[hash].RG == -1) {
                sucesso = true;
            } else {
                hash++;
            }
        }

        return hash;

    }

    /**
     * Metodo auxiliar para separar o RG, pegando os digitos do meio
     * @param RG
     * @return
     */
    private int separarRG(int RG) {
        String numero = String.valueOf(RG);
        String n = "";
        for (int i = 2; i <= 5; i++) {
            n += String.valueOf(numero.charAt(i));
        }

        return Integer.parseInt(n);
    }


}
