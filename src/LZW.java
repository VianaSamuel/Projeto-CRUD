import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class LZW {
    public static void compress(String source, String destin) throws Exception {

        // ARQUIVO ORIGINAL PARA STRING
        String originString = "";

        // LEITURA
        try {
            RandomAccessFile raf = new RandomAccessFile(source, "rw");
            
            // read globalId
            originString += raf.readInt() + "~";
            
            while(raf.getFilePointer() < raf.length() - 1) {
                // read lapide
                originString += raf.readBoolean() + "~";
               
                // read size
                originString += raf.readInt() + "~";

                // read Id
                originString += raf.readInt() + "~";
       
                // read name, type1, type2
                for(int x = 0; x < 3; x++) {
                    originString += raf.readUTF() + "~";
                }
                
                // read abilities number
                int abilitiesNumber = raf.readInt();
                originString += abilitiesNumber + "~";

                // read abilities
                for(int i = 0; i < abilitiesNumber; i++) {   
                    originString += raf.readUTF() + "~";
                }

                // read hp, att, def
                for(int x = 0; x < 3; x++) {
                    originString += raf.readInt() + "~";
                }

                // read date
                originString += raf.readLong() + "~";
            }
            raf.close();
        } catch(Exception e) { e.printStackTrace(); }

        /* ---------------
         * CRIA DICIONARIO
         * ---------------
         * com base no arquivo ORIGINAL
         */
        originString = originString.replaceAll(" ", "\\^");
        ArrayList<String> dictionary = new ArrayList<String>();
        for(int i = 0; i < originString.length(); i++) {
            String s = Character.toString(originString.charAt(i));
            if(!dictionary.contains(s)) dictionary.add(s);
        }

        /* ---------------------------
         * CRIA TRADUCAO DO DICIONARIO
         * ---------------------------
         * com base no arquivo do DICIONARIO
         */
        ArrayList<Integer> output = new ArrayList<Integer>();
        for(int i = 0; i < originString.length(); i++) {
            String s = Character.toString(originString.charAt(i));

            while(true) {
                if(i == originString.length() - 1) break;
                s += originString.charAt(i + 1);
                if(dictionary.contains(s)) {
                    if(i == originString.length() - 2) {
                        output.add(dictionary.indexOf(s));
                        break;
                    } else i++;
                } else {
                    dictionary.add(s);
                    
                    if(i == originString.length() - 2) output.add(dictionary.indexOf(s));
                    else output.add(dictionary.indexOf(s.substring(0, s.length() - 1)));
                    break;
                }
            }

            if(i == originString.length() - 1) break;
        }

        // CRIA ARQUIVO COMPRIMIDO
        RandomAccessFile raf = new RandomAccessFile(destin, "rw");

        raf.writeInt(dictionary.size());
        for(String str : dictionary) raf.writeUTF(str);
        raf.writeInt(output.size());

        if(dictionary.size() < 256) {
            for(int i : output) raf.writeByte(i);
        } else if(dictionary.size() < 65536) {
            for(int i : output) raf.writeShort(i);
        } else {
            for(int i : output) raf.writeInt(i);
        }

        raf.close();
        new File(source).delete();
    }

    public static void decompress(String source, String destin) {
        ArrayList<String> dictionary = new ArrayList<String>();
        ArrayList<Integer> output = new ArrayList<Integer>();

        // LER DICIONARIO E TRADUCAO
        try {
            RandomAccessFile raf = new RandomAccessFile(source, "rw");

            int dictionarySize = raf.readInt();
            for(int i = 0; i < dictionarySize; i++) dictionary.add(raf.readUTF());
            int outputSize = raf.readInt();

            if(dictionarySize < 256) {
                for(int i = 0; i < outputSize; i++) output.add((int)raf.readByte());
            } else if(dictionarySize < 65536) {
                for(int i = 0; i < outputSize; i++) output.add((int)raf.readShort());
            } else {
                for(int i = 0; i < outputSize; i++) output.add(raf.readInt());
            }

            raf.close();
        } catch(Exception e) { e.printStackTrace(); }

        // CRIAR STRING DE DESCOMPACTACAO
        String file = "";
        for(int i : output) file += dictionary.get(i);
        file = file.replaceAll("\\^", " ");

        // CRIAR ARQUIVO DE DESCOMPACTACAO
        String args[] = file.split("~");
        try {
            
            RandomAccessFile raf = new RandomAccessFile(destin, "rw");

            raf.writeInt(Integer.parseInt(args[0]));
            for(int i = 1; i < args.length; i++) {
                // write cabecalho
                raf.writeBoolean(Boolean.parseBoolean(args[i]));
                raf.writeInt(Integer.parseInt(args[++i]));
                raf.writeInt(Integer.parseInt(args[++i]));

                // read name, type1, type2
                for(int x = 0; x < 3; x++) {raf.writeUTF(args[++i]);}

                // write abilities
                int num_abilities = Integer.parseInt(args[++i]);
                raf.writeInt(num_abilities);
                for(int j = 0; j < num_abilities; j++) raf.writeUTF(args[++i]);

                // write hp,att,def
                for(int x = 0; x < 3; x++) raf.writeInt(Integer.parseInt(args[++i]));

                // write date
                raf.writeLong(Long.parseLong(args[++i]));
            }
            raf.close();
        } catch(Exception e) { e.printStackTrace(); }
    }
}