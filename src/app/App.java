package app;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class App {
    // caso deseje mudar o caminho do arquivo binario, utilize essa variavel
    static final String pathBin = "Pessoas.bin";

    static final String pathTXT = "PessoasPAA.txt";

    //indice responsavel por salvar e procurar algum elemento;
    public static Index indice;


    /**
     * Metodo que cria uma pessoa baseada nos dados controlando a posição
     * @param dados RG, nome e data de nascimento
     * @param pos
     * @return
     */
    public static Pessoa criarRegistroPessoa(String[] dados, long pos) {
        Pessoa registroPessoa;
        try {
            int rg = Integer.parseInt(dados[0]);
            String nome = dados[1];
            String dataNasc = dados[2];
            registroPessoa = new Pessoa(rg, nome, dataNasc);
        } catch (Exception e) {
            System.out.println("Ocorreu um erro na posição: " + pos);
            registroPessoa = null;
        }

        return registroPessoa;
    }

    /**
     * Metodo converte o arquivo texto para arquivo binario, e ao mesmo tempo cria o indice;
     * @param pathTXT path do arquivo texto
     * @param arqBin path do arquivo binario
     * @throws IOException
     */
    public static void converterArquivo(String pathTXT, String arqBin) throws IOException {
        File arqEntrada = new File(pathTXT);
        Scanner leitor = new Scanner(arqEntrada);
        int contador = 0;

        Scanner teclado = new Scanner(System.in); // variavel para "pausar" e ver o que está acontecendo

        try{
            File arqSaida = new File(arqBin);
            RandomAccessFile dadosSaida = new RandomAccessFile(arqSaida, "rw");
            indice = new Index(arqBin);

            dadosSaida.setLength(0);
            dadosSaida.writeInt(0);
            System.out.println("\nArquivo binario e indice sendo criados, aguarde...");

            while(leitor.hasNextLine()){
                String linha = leitor.nextLine();
                String [] dados = linha.split(";");
                long pos = dadosSaida.getFilePointer();  //controlando a posição no arquivo
                indice.salvarPessoa(Integer.parseInt(dados[0]), pos);
                Pessoa novoRegistro = criarRegistroPessoa(dados, pos);
                novoRegistro.saveToFile(dadosSaida);
                contador++;
            }
            leitor.close();

            dadosSaida.seek(0);
            dadosSaida.writeInt(contador); // salvando quantos dados existem no cabeçalho do arquivo
            dadosSaida.close();
            indice.escreverPessoas(); //  escrever todas as pessoas no arquivo hash
            System.out.println("\n\nArquivo convertido e indice criado.... prescione <enter>");
            teclado.nextLine();

            
        }catch (FileNotFoundException e){
            System.out.println("Erro encontrado: "+e.getMessage());
        }
        

    }


    /**
     * Metodo para pesquisar uma pessoa baseado no RG
     */
    public static void pesquisarPessoa(Scanner entrada){
        
        try{
            System.out.println("\n\nDigite o RG que deseja procurar: ");
            int RG = Integer.parseInt(entrada.nextLine());
            Pessoa buscada = indice.retornaPessoa(RG);
            if(buscada.RG != -1){
                System.out.println("\nPessoa encontrada: " +buscada);
                System.out.println("\nPrescione <enter>...");

            }else{
                System.out.println("\nPessoa não encontrada...");
                System.out.println("\nPrescione <enter>...");

            }
        }catch(Exception e){
            System.out.println("Ocorreu um erro: "+e.getMessage());
        }

    }

    public static int menu(Scanner leitor){
        System.out.println();
        System.out.println("\tIndice HASH!");
        System.out.println("\n0. Fim do programa");
        System.out.println("1. Criar arquivo binario + indice HASH");
        System.out.println("2. Realizar pesquisa por uma pessoa usando RG");
        System.out.println("\nOpcao:");
        int opcao = Integer.parseInt(leitor.nextLine());
        return opcao;
    }
    public static void main(String[] args) throws Exception {
        int opcao;
        Scanner entrada = new Scanner(System.in);
        Scanner teclado = new Scanner(System.in);
        
        try{
            do{
                opcao = menu(entrada);
                switch(opcao){
                    case 1: 
                        converterArquivo(pathTXT, pathBin);
                        break;
                    case 2: 
                        pesquisarPessoa(entrada);
                        teclado.nextLine();
                        
                        break;
                    default:
                    System.out.println("Adeus!!");
                        break;
                }
            }while(opcao!=0);
        }catch (IOException ex) {
            System.out.println("Ocorreu um erro: "+ex.getMessage());
        }
        entrada.close();
    }
}
