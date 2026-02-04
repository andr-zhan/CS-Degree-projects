import java.io.*;
import java.util.*;

public class Guru {

    public static final String FICHEIRO_NIVEIS = "ficheiro_niveis.txt";
    public static final String FICHEIRO_NIVEIS_GERADO = "generated_levels.txt";
    public static final String FICHEIRO_DICIONARIO = "portuguese-large.txt";

    // Classe interna "Nivel" usada para representar um nivel do jogo.
    public static class Nivel {
        String[] palavras;
        String letras;

        Nivel(String letters, String[] words) {
            this.letras = letters;
            this.palavras = words;
        }
    }

    // Metodo usado para ler os niveis a partir do ficheiro que contenha os niveis a serem jogados.
    public static List<Nivel> carregarNiveis(String nomeFicheiro) throws IOException {
        List<Nivel> niveis = new ArrayList<>(); // Lista para armazenar os níveis lidos do arquivo.

        try (BufferedReader br = new BufferedReader(new FileReader(nomeFicheiro))) {
            String linha;

            // Itera pelas linhas do arquivo até que todas sejam lidas.
            while ((linha = br.readLine()) != null) {
                String letras = linha; // A primeira linha de cada nível contém as letras disponíveis.
                List<String> palavras = new ArrayList<>(); // Lista para armazenar as palavras do nível atual.

                // Lê as linhas seguintes, que contêm palavras, até encontrar uma linha em branco ou o fim do ficheiro.
                while ((linha = br.readLine()) != null && !linha.isEmpty()) {
                    palavras.add(linha); // Adiciona a palavra à lista.
                }

                // Adiciona o novo nível à lista de níveis.
                // Cada nível é representado como um objeto da classe Nivel.
                niveis.add(new Nivel(letras, palavras.toArray(new String[0])));
            }
        }
        return niveis;
    }

    // Metodo usado para salvar o progresso do jogo.
    public static void salvarProgresso(int nivelAtual, int moedas, Map<Integer, Set<String>> progresso, String nomeFicheiroProgresso) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFicheiroProgresso))) {
            // Escreve o nível atual no arquivo como a primeira linha.
            writer.write(nivelAtual + "\n");

            // Escreve a quantidade de moedas na segunda linha.
            writer.write(moedas + "\n");

            // Itera sobre o mapa que contém o progresso dos níveis.
            for (Map.Entry<Integer, Set<String>> entry : progresso.entrySet()) {
                // Escreve o número do nível seguido pelas palavras adivinhadas nesse nível.
                // O formato é: "número_do_nível:palavra1,palavra2,..."
                writer.write(entry.getKey() + ":" + String.join(",", entry.getValue()) + "\n");
            }

            System.out.println("Progresso salvo com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao salvar o progresso: " + e.getMessage());
        }
    }

    // Metodo usado para carregar o ultimo progresso salvo.
    public static Map<Integer, Set<String>> carregarProgresso(BufferedReader reader, int totalLevels) throws IOException {
        Map<Integer, Set<String>> progresso = new HashMap<>(); // Mapa para armazenar o progresso do jogador.
        String linha;

        // Lê cada linha do BufferedReader.
        while ((linha = reader.readLine()) != null) {
            // Divide a linha em duas partes: número do nível e palavras associadas.
            String[] partes = linha.split(":");

            // Converte a primeira parte (antes dos ":") para o número do nível.
            int nivel = Integer.parseInt(partes[0]);

            // Divide a segunda parte (depois dos ":") em palavras, separadas por vírgulas.
            Set<String> palavras = new HashSet<>(Arrays.asList(partes[1].split(",")));

            // Adiciona o nível e as suas palavras ao mapa de progresso.
            progresso.put(nivel, palavras);
        }

        return progresso;
    }

    // Metodo usado para conceder uma dica ao utilizador.
    public static void darDica(Nivel nivel, Set<String> palavrasAdivinhadas) {
        // Percorre a lista de palavras do nível.
        for (String palavra : nivel.palavras) {
            // Verifica se a palavra ainda não foi adivinhada.
            if (!palavrasAdivinhadas.contains(palavra)) {
                System.out.println("Dica: Uma das palavras começa com a letra '" + palavra.charAt(0) + "'");
                return;
            }
        }
    }

    // Metodo usado para a execução do jogo.
    // Metodo usado para a execução do jogo.
    public static void jogarGuru(List<Nivel> niveis, Set<String> dicionario, String nomeFicheiroProgresso) {
        Scanner scanner = new Scanner(System.in);
        int nivelAtual = 0; // Nível em que o utilizador está.
        int moedas = 0; // Total de moedas do utilizador.
        Map<Integer, Set<String>> progresso = new HashMap<>(); // Progresso do jogador: palavras adivinhadas por nível.
        Set<String> palavrasExtraValidas = new HashSet<>(); // Armazena palavras válidas não listadas nos níveis, mas presentes no dicionario.

        // Verifica se um arquivo de progresso já existe.
        File ficheiroProgresso = new File(nomeFicheiroProgresso);
        if (ficheiroProgresso.exists()) {
            System.out.print("Progresso salvo encontrado! Deseja carregar o progresso? (sim/não): ");
            String escolha = scanner.nextLine().trim().toUpperCase();
            if (escolha.equalsIgnoreCase("sim")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(ficheiroProgresso))) {
                    nivelAtual = Integer.parseInt(reader.readLine()); // Carrega o nível atual do jogador.
                    moedas = Integer.parseInt(reader.readLine()); // Carrega as moedas acumuladas.
                    progresso = carregarProgresso(reader, niveis.size()); // Carrega as palavras já adivinhadas.
                    reader.close();
                    System.out.println("Progresso carregado com sucesso!");
                } catch (IOException e) {
                    System.err.println("Erro ao carregar o progresso: " + e.getMessage());
                }
            }
        }

        // Loop principal que percorre os níveis do jogo.
        while (nivelAtual < niveis.size()) {
            Nivel nivel = niveis.get(nivelAtual); // Obtém o nível atual.
            Set<String> palavrasAdivinhadas = progresso.getOrDefault(nivelAtual, new HashSet<>()); // Palavras já adivinhadas no nível atual.

            System.out.println("\nNível " + (nivelAtual + 1));
            System.out.println("Letras disponíveis: " + nivel.letras); // Exibe as letras disponíveis para formar palavras.
            System.out.println("Número de palavras: " + nivel.palavras.length); // Exibe o número total de palavras no nível.

            // Loop interno para jogar o nível atual até que todas as palavras sejam adivinhadas.
            while (palavrasAdivinhadas.size() < nivel.palavras.length) {
                System.out.println("\nPalavras já adivinhadas: " + palavrasAdivinhadas);
                System.out.print("Digite uma palavra, 'dica' para comprar uma dica, ou 'salvar' para salvar o progresso: ");
                String input = scanner.nextLine().trim().toUpperCase();

                // O utilizador solicita uma dica.
                if (input.equalsIgnoreCase("dica")) {
                    if (moedas >= 100) {
                        moedas -= 100; // Deduz 100 moedas pelo custo da dica.
                        darDica(nivel, palavrasAdivinhadas);
                    } else {
                        System.out.println("Moedas insuficientes para obter uma dica! Moedas atuais: " + moedas + ".");
                    }

                    // O utilizador deseja salvar o progresso atual.
                } else if (input.equalsIgnoreCase("salvar")) {
                    salvarProgresso(nivelAtual, moedas, progresso, nomeFicheiroProgresso);

                    // O utilizador digita uma palavra que está na lista de palavras do nível.
                } else if (Arrays.asList(nivel.palavras).contains(input)) {
                    if (palavrasAdivinhadas.add(input)) { // Adiciona a palavra ao conjunto de palavras adivinhadas.
                        moedas += 10; // Concede 10 moedas como recompensa.
                        progresso.put(nivelAtual, palavrasAdivinhadas); // Atualiza o progresso do nível.
                        System.out.println("Correto! Você ganhou 10 moedas. Moedas atuais: " + moedas);
                    } else {
                        System.out.println("Você já adivinhou essa palavra.");
                    }

                    // O utilizador digita uma palavra válida (do dicionário), mas que não está no nível.
                } else if (dicionario.contains(input)) {
                    // Verificar se a palavra pode ser formada com as letras do nível:
                    Map<Character, Integer> numeroLetras = new HashMap<>();
                    // Contar a frequência de cada letra disponível no nível
                    for (char c : nivel.letras.toCharArray()) {
                        // Incrementa a contagem do caractere c no mapa.
                        // Se o caractere ainda não estava no mapa, ele é adicionado com valor inicial 1.
                        numeroLetras.put(c, numeroLetras.getOrDefault(c, 0) + 1);
                    }

                    // Apenas considera a palavra como válida se ela puder ser formada com as letras do nível.
                    if (formaPalavra(input, numeroLetras)) {
                        if (palavrasExtraValidas.add(input)) { // Adiciona a palavra ao conjunto de palavras extras válidas.
                            moedas += 10; // Concede 10 moedas como recompensa por uma palavra válida.
                            System.out.println("Palavra válida, mas não listada no nível. Você ganhou 10 moedas. Moedas atuais: " + moedas);
                        } else {
                            System.out.println("Você já encontrou essa palavra válida anteriormente.");
                        }
                    } else {
                        System.out.println("A palavra é válida, mas não pode ser formada com as letras disponíveis no nível.");
                    }

                    // Palavra inválida ou incorreta.
                } else {
                    System.out.println("Palavra incorreta ou inválida. Tente novamente.");
                }
            }

            // Quando o utilizador completa o nível.
            System.out.println("Parabéns! Você completou o nível " + (nivelAtual + 1));
            progresso.put(nivelAtual, palavrasAdivinhadas); // Atualiza o progresso do jogador.
            nivelAtual++; // Avança para o próximo nível.
        }

        // Quando todos os níveis são concluídos.
        System.out.println("\nVocê completou todos os níveis! Total de moedas: " + moedas);
        scanner.close();
    }

    // Metodo usado para ler e carregar o ficheiro que contém o dicionario.
    public static Set<String> carregarDicionario(String nomeFicheiro) throws IOException {
        Set<String> dicionario = new HashSet<>(); // Conjunto que armazena as palavras únicas do dicionário.

        // Tenta abrir o ficheiro do dicionario.
        try (BufferedReader br = new BufferedReader(new FileReader(nomeFicheiro))) {
            String linha;

            // Lê o arquivo linha por linha.
            while ((linha = br.readLine()) != null) {
                dicionario.add(linha.trim().toUpperCase());
                // Adiciona a palavra ao conjunto:
                // `trim()` remove espaços em branco no início e no fim da linha.
            }
        }

        return dicionario;
    }

    // Metodo usado para gerar novos niveis a partir das palavras presentes no dicionario.
    public static void gerarNiveisPeloDicionario(Set<String> dicionario, String nomeFicheiroOutput) throws IOException {
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFicheiroOutput))) {
            for (int i = 0; i < 5; i++) { // Gera 5 níveis.
                Set<String> palavrasDoNivel = new HashSet<>(); // Armazena as palavras selecionadas para o nível atual.
                Map<Character, Integer> contagemLetras = new HashMap<>(); // Contém a contagem de cada letra da palavra principal.

                // Selecionar uma palavra principal (palavraPrincipal) aleatória com no mínimo 4 caracteres.
                List<String> palavrasValidas = dicionario.stream()
                        .filter(word -> word.length() >= 4)
                        .toList();
                String palavraPrincipal = palavrasValidas.get(random.nextInt(palavrasValidas.size())); // Escolhe aleatoriamente uma palavra válida.
                palavrasDoNivel.add(palavraPrincipal); // Adiciona a palavra principal à lista de palavras do nível.

                // Cada letra da palavra principal é adicionada ao mapa, com a sua quantidade total.
                for (char c : palavraPrincipal.toCharArray()) {
                    contagemLetras.put(c, contagemLetras.getOrDefault(c, 0) + 1);
                }

                // Procurar palavras adicionais que podem ser formadas com as letras da palavra principal.
                for (String palavra : dicionario) {
                    // Verifica se a palavra ainda não foi adicionada ao nível e se pode ser formada com as letras disponíveis.
                    if (!palavrasDoNivel.contains(palavra) && formaPalavra(palavra, contagemLetras)) {
                        palavrasDoNivel.add(palavra); // Adiciona a palavra ao conjunto de palavras do nível.
                    }
                    if (palavrasDoNivel.size() >= 5) break; // Limita o número de palavras do nível a 5.
                }

                // Armazenar as letras da palavra principal numa lista.
                List<Character> letrasEmbaralhadas = new ArrayList<>();
                for (char c : palavraPrincipal.toCharArray()) {
                    letrasEmbaralhadas.add(c);
                }
                Collections.shuffle(letrasEmbaralhadas); // Embaralha as letras aleatoriamente.

                // Escrever as letras embaralhadas e as palavras no ficheiro.
                StringBuilder stringLetrasEmbaralhadas = new StringBuilder();
                for (char c : letrasEmbaralhadas) {
                    stringLetrasEmbaralhadas.append(c).append(" ");
                }
                writer.write(stringLetrasEmbaralhadas.toString().trim() + "\n"); // Escreve as letras embaralhadas no ficheiro.
                for (String palavra : palavrasDoNivel) {
                    writer.write(palavra + "\n"); // Escreve cada palavra do nível no ficheiro.
                }
                writer.write("\n"); // Adiciona uma linha em branco entre os níveis.
            }
        }
    }

    // Metodo usado para verificar se uma palavra eh valida.
    public static boolean formaPalavra(String palavra, Map<Character, Integer> letrasDisponiveis) {
        // Cria uma cópia temporária do mapa de letras disponíveis para evitar modificar o original.
        Map<Character, Integer> tempCounts = new HashMap<>(letrasDisponiveis);

        // Itera por cada caractere da palavra que queremos verificar.
        for (char c : palavra.toCharArray()) {
            // Verifica se a letra atual não está disponível ou se a quantidade disponível é zero.
            if (tempCounts.getOrDefault(c, 0) == 0) {
                return false; // A palavra não pode ser formada.
            }

            // Caso a letra esteja disponível, reduz a sua quantidade no mapa temporário.
            tempCounts.put(c, tempCounts.get(c) - 1);
        }

        // Todos os caracteres da palavra foram encontrados.
        return true;
    }

    // Main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            int escolha = -1;
            while (escolha != 1 && escolha != 3) {
                System.out.println("Escolha uma das três opções:\n \n1 - Jogar níveis existentes\n2 - Gerar novos níveis\n3 - Jogar níveis gerados");
                System.out.print("Digite a sua escolha: ");

                if (scanner.hasNextInt()) {
                    escolha = scanner.nextInt();
                    scanner.nextLine();

                    if (escolha == 1) {
                        List<Nivel> niveis = carregarNiveis(FICHEIRO_NIVEIS);
                        Set<String> dicionario = carregarDicionario(FICHEIRO_DICIONARIO);
                        jogarGuru(niveis, dicionario, "save_game_original.txt");
                    } else if (escolha == 2) {
                        Set<String> dicionario = carregarDicionario(FICHEIRO_DICIONARIO);
                        gerarNiveisPeloDicionario(dicionario, FICHEIRO_NIVEIS_GERADO);
                        System.out.println("Níveis gerados com sucesso! Agora selecione a opção 3 para jogar os níveis gerados.");
                    } else if (escolha == 3) {
                        List<Nivel> niveis = carregarNiveis(FICHEIRO_NIVEIS_GERADO);
                        Set<String> dicionario = carregarDicionario(FICHEIRO_DICIONARIO);
                        jogarGuru(niveis, dicionario, "save_game_generated.txt");
                    } else {
                        System.out.println("Opção inválida! Tente novamente.");
                        escolha = -1; // Reinicia o loop.
                    }
                } else {
                    System.out.println("Entrada inválida! Por favor, insira um número válido.");
                    scanner.next(); // Limpa a entrada inválida.
                }
            }
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
