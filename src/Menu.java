// HENRIQUE DE ALMEIDA DINIZ
// SAMUEL LUIZ DA CUNHA VIANA CRUZ

import java.util.Date;
import java.util.Scanner;

public class Menu {
    // caminho do arquivo .db
    private static final String DB_PATH = "tmp/pokemons.db";

    /* ----
     * MAIN
     * ----
     * inicializa instancia do CRUD com o arquivo .db
     * aciona o arquivo reader e seus metodos
     * entra em loop com as opcoes do CRUD
     */
    public static void main(String[] args) throws Exception {
        System.out.print("\033[H\033[2J");

        Reader.main(args);
        CRUD crud = new CRUD(DB_PATH);
        
        delay(1250);
        Scanner scan = new Scanner (System.in);
        while (true){
            int hp, att, def, id,N,M;
            String name, type1, type2, abilitiesTemp, dateTemp;
            System.out.print("\033[H\033[2J");
            System.out.print("*--------------------------*\n");
            System.out.print("#    OPCOES DE REGISTRO    #\n");
            System.out.print("*--------------------------*\n");
            System.out.print("| 1) Criar                 |\n");
            System.out.print("| 2) Ler                   |\n");
            System.out.print("| 3) Atualizar             |\n");
            System.out.print("| 4) Deletar               |\n");
            System.out.print("|                          |\n");
            System.out.print("| 5) Ordenacao Externa     |\n");
            System.out.print("|                          |\n");
            System.out.print("| 0) Sair                  |\n");
            System.out.print("*--------------------------*\n");
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
                    int i = crud.create(criado);
                    System.out.println("\nID criado: " + i);

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
                    Pokemon atualizado = new Pokemon(id, name, type1, type2, abilities, hp, att, def, date);
                    Boolean upd = crud.update(atualizado);
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
                    Boolean del = crud.delete(idDel);
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