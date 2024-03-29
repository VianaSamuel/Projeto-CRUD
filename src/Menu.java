// HENRIQUE DE ALMEIDA DINIZ
// SAMUEL LUIZ DA CUNHA VIANA CRUZ

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;

public class Menu {
    // caminho dos arquivos
    private static final String DB_PATH = "tmp/pokemons.db";
    private static final String BT_PATH = "tmp/Bplus.db";
    private static final String H_PATH = "tmp/Hash.db";
    private static final String HB_PATH = "tmp/HashB.db";
    private static final String teste_path = "tmp/LZW.db";

       public static void criarArquivoCriptografadoCesar(String resp, String nomeArq) throws IOException {
        RandomAccessFile arq = new RandomAccessFile(nomeArq, "rw");
        byte ba[];
        ba = resp.getBytes();
        arq.write(ba);
        arq.close();
    }
  /*   public static HashMap<Character, Integer> makeFrequency(String filename) {
        var frequency = new HashMap<Character, Integer>();
        try {
           RandomAccessFile raf = new RandomAccessFile(DB_PATH, "rw");
        while (raf.getFilePointer() < raf.length()) {
                char c = (char) raf.readByte();
                frequency.merge(c, 1, Integer::sum);
            }
            raf.seek(0);
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frequency;
    }
 */
    /* ----
     * MAIN
     * ----
     * inicializa instancia do CRUD com o arquivo .db
     * aciona o arquivo reader e seus metodos
     * entra em loop com as opcoes do CRUD
     */
    public static String fileToString(RandomAccessFile arq) throws IOException, ParseException {
        String resp = "";
        boolean valido = true;
        int len = 0;
        byte ba[];
        long posIni = 4;

        while (true) {
            try {
                arq.seek(posIni);
                valido = arq.readBoolean();// ler lapide -- se TRUE pokemon existe , caso FALSE pokemon apagado
                len = arq.readInt(); // ler tamanho do registro
                ba = new byte[len]; // cria um vetor de bytes com o tamanho do registro
                arq.read(ba); // Ler registro
                Pokemon pokemonTemp = new Pokemon();
                pokemonTemp.fromByteArray(ba);
                resp += pokemonTemp.toString();
                posIni = arq.getFilePointer();
            } catch (EOFException e) {
                return resp;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        System.out.print("\033[H\033[2J");
        
        ArvoreBmais index= new ArvoreBmais(8, BT_PATH);
        Hash index2 =new Hash(45, HB_PATH, H_PATH);

        Reader.main(args);
        CRUD crud = new CRUD(DB_PATH);
        RandomAccessFile ras=new RandomAccessFile(DB_PATH, "rw");
        RandomAccessFile ran=new RandomAccessFile("tmp/LZW.db", "rw");
        crud.create(null, index, index2);

        delay(1250);
        Scanner scan = new Scanner (System.in);
        while (true){
            int hp, att, def, id,N,M;
            String name, type1, type2, abilitiesTemp, dateTemp;
            System.out.print("\033[H\033[2J");
            System.out.print("*------------------------------*\n");
            System.out.print("#     OPCOES DE REGISTRO       #\n");
            System.out.print("*------------------------------*\n");
            System.out.print("| 1)  Criar                    |\n");
            System.out.print("| 2)  Ler                      |\n");
            System.out.print("| 3)  Atualizar                |\n");
            System.out.print("| 4)  Deletar                  |\n");
            System.out.print("|                              |\n");
            System.out.print("| 5)  Ordenacao Externa        |\n");
            System.out.print("| 6)  Buscar na Arvore         |\n");
            System.out.print("| 7)  Buscar no Hash           |\n");
            System.out.print("|                              |\n");
            System.out.print("| 8)  LZW                      |\n");
            System.out.print("| 9)  Huffman                  |\n");
            System.out.print("| 10) KMP                      |\n");
            System.out.print("| 11) Força Bruta              |\n");
            System.out.print("|                              |\n");
            System.out.print("| 12) Crip. Cifra de Cesar     |\n");
            System.out.print("| 13) Descrip. Cifra Cesar     |\n");
            System.out.print("| 14) Criptografia DES         |\n");
            System.out.print("|                              |\n");
            System.out.print("| 0) Sair                      |\n");
            System.out.print("*------------------------------*\n");
            System.out.print("Digite uma opcao: ");
            String opt = scan.next();

            switch (opt){
                case "1":
                    Scanner scanLinhaA = new Scanner (System.in).useDelimiter("\\n");

                    System.out.print("Digite o nome: ");
                    name = scan.next();
                    System.out.print("Digite o tipo 1: ");
                    type1 = scan.next();
                    System.out.print("Digite o tipo 2: ");
                    type2 = scan.next();
                    System.out.print("Digite as habilidades, separadas por virgulas: ");
                    abilitiesTemp = scanLinhaA.next();
                    System.out.print("Digite o HP: ");
                    hp = scan.nextInt();
                    System.out.print("Digite o ataque: ");
                    att = scan.nextInt();
                    System.out.print("Digite a defesa: ");
                    def = scan.nextInt();
                    System.out.print("Digite a data, no formato (dd/mm/aaaa): ");
                    dateTemp = scan.next();

                    Date date = Tratamentos.trataDatas(dateTemp);
                    String[] abilities = abilitiesTemp.split(",");

                    Pokemon criado = new Pokemon(name, type1, type2, abilities, hp, att, def, date);
                    int i = crud.create(criado,index,index2);
                    System.out.println("\nID criado: " + (i-1));

                    waitForEnter();
                    break;
                    
                case "2":
                    System.out.print("Digite o ID do Pokemon a ser lido: ");
                    int idRead = scan.nextInt();
                    
                    System.out.println();
                    Pokemon lido = crud.read(idRead);
                    if (lido != null){
                        System.out.println("+---------------------+");
                        System.out.println("| POKEMON ENCONTRADO! |");
                        System.out.println("+---------------------+------------");
                        System.out.println(lido.toString());
                        System.out.println("-----------------------------------");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;

                case "3":
                    Scanner scanLinhaB = new Scanner (System.in).useDelimiter("\\n");

                    System.out.print("Digite o ID do pokemon que deseja atualizar: ");
                    id = scan.nextInt();
                    System.out.print("Digite o novo nome: ");
                    name = scan.next();
                    System.out.print("Digite o novo tipo 1: ");
                    type1 = scan.next();
                    System.out.print("Digite o novo tipo 2: ");
                    type2 = scan.next();
                    System.out.print("Digite as novas habilidades, separadas por virgulas: ");
                    abilitiesTemp = scanLinhaB.next();
                    System.out.print("Digite o novo HP: ");
                    hp = scan.nextInt();
                    System.out.print("Digite o novo ataque: ");
                    att = scan.nextInt();
                    System.out.print("Digite a nova defesa: ");
                    def = scan.nextInt();
                    System.out.print("Digite a nova data, no formato (dd/mm/aaaa): ");
                    dateTemp = scan.next();

                    date = Tratamentos.trataDatas(dateTemp);
                    abilities = abilitiesTemp.split(",");

                    System.out.println();
                    Pokemon atualizado = new Pokemon((id+1), name, type1, type2, abilities, hp, att, def, date);
                    Boolean upd = crud.update(atualizado,index,index2);
                    if (upd){
                        System.out.println("*---------------------------------*");
                        System.out.println("| POKEMON ATUALIZADO COM SUCESSO! |");
                        System.out.println("*---------------------------------*");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;
                    
                case "4":
                    System.out.print("Digite o ID do Pokemon a ser deletado: ");
                    int idDel = scan.nextInt();

                    System.out.println();
                    Boolean del = crud.delete(idDel,index,index2);
                    if (del){
                        System.out.println("*-------------------------------*");
                        System.out.println("| POKEMON DELETADO COM SUCESSO! |");
                        System.out.println("*-------------------------------*");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;

                case "5":
                    System.out.print("Digite a quantidade de registros: ");
                    N = scan.nextInt();
                    System.out.print("Digite a quantidade de caminhos: ");
                    M = scan.nextInt();
                    Intercalacoes.iBComum(N, M);
                    waitForEnter();
                    break;

                case "6":
                    CRUD.ler(ras,index);
                    waitForEnter();
                    break;

                case "7":
                    CRUD.lerHash(ras, index2);
                    waitForEnter();
                    break;

                case "8":
                    System.out.println("> Digite o nome do arquivo de saída: ");
                    String fileName = "tmp/";
                    fileName += scan.next();

                   // LZW.compress(DB_PATH, fileName);
                   // LZW.decompress(fileName, teste_path);

                    System.out.println("\n>>> Arquivo compactado com sucesso!");
                    System.out.println("\n>>> Arquivo descompactado com sucesso!");
                     waitForEnter();
                    break;

                case "9":
                    //var frequency = makeFrequency(DB_PATH);
                   // var tree = new Huffman(frequency);
                   // tree.traverse(Huffman.root, "");
                    try {
                        RandomAccessFile source = new RandomAccessFile(DB_PATH, "rw");
                        RandomAccessFile dest = new RandomAccessFile("tmp/huff_comp.bin", "rw");
                        RandomAccessFile desc = new RandomAccessFile("tmp/huff_descomp.bin", "rw");
                        Huffman.compress(source, dest);
                        Huffman.decompress(dest, desc);
            
                        source.seek(0);
                        dest.seek(0);
                        source.close();
                        dest.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("\n>>> Arquivo compactado com sucesso!");
                    System.out.println("\n>>> Arquivo descompactado com sucesso!");
                     waitForEnter();
                    break;
                case "10":
                 long start = System.currentTimeMillis();
                            int resp = 0;
                            System.out.println("----------------KMP-----------------------");
                             RandomAccessFile source = new RandomAccessFile(DB_PATH, "rw");
                            String arqString = fileToString(source);
                            System.out.println("Digite o padrao: ");
                            Scanner sc2 = new Scanner(System.in);
                            String padrao = sc2.nextLine();
                            resp = BuscadorPadrao.executaKMP(arqString, padrao);
                            while (resp != -1) {
                                arqString = arqString.substring(resp + 1);
                                resp = BuscadorPadrao.executaKMP(arqString, padrao);
                            }
                            long end = System.currentTimeMillis() - start;
                            System.out.println("Tempo decorrido(ms): " + end);
                             waitForEnter();
                            break;
                case "11":
                 start = System.currentTimeMillis();
                            resp = 0;
                            System.out.println("----------------FORÇA BRUTA-----------------------");
                             RandomAccessFile source2 = new RandomAccessFile(DB_PATH, "rw");
                            arqString = fileToString(source2);
                            System.out.println("Digite o padrao: ");
                            sc2 = new Scanner(System.in);
                            padrao = sc2.nextLine();
                            resp = BuscadorPadrao.forcaBruta(padrao, arqString);
                            while (resp != -1) {
                                arqString = arqString.substring(resp + 1);
                         
                                resp = BuscadorPadrao.forcaBruta(padrao, arqString);
                            }
                            end = System.currentTimeMillis() - start;
                            System.out.println("Tempo decorrido(ms): " + end);
                                 waitForEnter();
                            break;
                case "12":
                String respC = "";
                 RandomAccessFile source3 = new RandomAccessFile(DB_PATH, "rw");
                respC = Criptografia.criptografaCesar(fileToString(source3));
                criarArquivoCriptografadoCesar(respC, "tmp/dadosCriptografadoCesar.db");
                 waitForEnter();
                    break;

                case "13":
                  RandomAccessFile source4 = new RandomAccessFile(DB_PATH, "rw");
                  RandomAccessFile arq2 = new RandomAccessFile("tmp/dadosCriptografadoCesar.db", "rw");
                            respC = arq2.readUTF();
                            respC = Criptografia.descriptografaCesar(Criptografia.criptografaCesar(fileToString(source4)));
                            criarArquivoCriptografadoCesar(respC, "tmp/dadosDescriptografadoCesar.db");
                              waitForEnter();
                            break;
                case"14":
                 RandomAccessFile source5 = new RandomAccessFile(DB_PATH, "rw");
                DES.mostraAlgoritimo(fileToString(source5));
                 waitForEnter();
                     break;

                case "0":
                    System.exit(1);
                
                default:
                    break;
            
            }
        }
    }

    //=====WAIT=====//
    public static void waitForEnter(){
        Scanner s = new Scanner(System.in);
        System.out.println("Pressione Enter para continuar...");
        s.nextLine();
    }
   
    //=====DELAY=====//
    public static void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}