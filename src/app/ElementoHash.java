package app;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Classe usada em indice HASH
 * representa 1 elemento, no caso 1 pessoa.
 */
public class ElementoHash {
    int RG;
    long pos; //posição no arquivo de dados


    public ElementoHash(int RG, long pos){
        this.RG = RG;
        this.pos = pos;
    }


    public boolean saveToFile(RandomAccessFile file,long posHash) throws IOException{
        file.writeInt(this.RG);
        file.writeLong(this.pos); //posição da pessoa no arquivo original "Pessoas.bin"
        this.pos = posHash; //trocando a posição para a posição no arquivo hash 
        return true;
    }


}
