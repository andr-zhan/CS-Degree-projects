import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;

// Classe principal que implementa a interface gráfica do jogo "Palavra GURU"
public class GuruGUI {
    private JFrame frame; // Janela principal da aplicação
    private JPanel panel; // Painel principal que contém os componentes
    private JTextArea outputArea; // Área de texto para exibir mensagens e resultados
    private JTextField inputField; // Campo de texto para entrada do usuário
    private JButton submeterButton, dicaButton, salvarButton; // Botões para interações
    private JLabel nivelLabel, letrasLabel, moedasLabel; // Labels para exibir informações do jogo
    private int nivelAtual;
    private int moedas;
    private Guru.Nivel nivelAtualData; // Dados do nível atual
    private Set<String> palavrasAdivinhadas; // Conjunto de palavras já adivinhadas no nível atual
    private Set<String> dicionario;

    private final List<Guru.Nivel> niveis; // Lista de todos os níveis do jogo

    // Construtor que inicializa os dados e configura a interface gráfica
    public GuruGUI(List<Guru.Nivel> niveis, Set<String> dicionario) {
        this.niveis = niveis;
        this.dicionario = dicionario;
        this.nivelAtual = 0;
        this.moedas = 0;

        inicializarGUI(); // Inicializa os componentes gráficos
        carregarNivel(); // Carrega os dados do primeiro nível
    }

    // Metodo que inicializa os componentes da interface gráfica
    private void inicializarGUI() {
        frame = new JFrame("Palavra GURU");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha a aplicação ao fechar a janela
        frame.setSize(600, 400);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        outputArea = new JTextArea(); // Cria a área de texto para exibir mensagens
        outputArea.setEditable(false); // Torna a área de texto apenas para leitura
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputArea); // Adiciona um scroll à área de texto

        inputField = new JTextField(); // Campo de texto para entrada do utilizador
        submeterButton = new JButton("Submeter"); // Botão para submeter palavras
        dicaButton = new JButton("Dica (Custo: 100 Coins)"); // Botão para pedir dicas
        salvarButton = new JButton("Salvar Progresso"); // Botão para salvar o progresso

        // Painel inferior que contém o campo de entrada e o botão "Submeter"
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(submeterButton, BorderLayout.EAST);

        // Painel superior que exibe informações sobre o nível, letras e moedas
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3)); // Divide em 3 colunas
        nivelLabel = new JLabel("Level: 1"); // Label para exibir o nível atual
        letrasLabel = new JLabel("Letters: "); // Label para exibir as letras do nível atual
        moedasLabel = new JLabel("Coins: 0"); // Label para exibir o número de moedas
        topPanel.add(nivelLabel);
        topPanel.add(letrasLabel);
        topPanel.add(moedasLabel);

        // Painel lateral com os botões "Dica" e "Salvar Progresso"
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(1, 2)); // Divide em 2 colunas
        actionsPanel.add(dicaButton);
        actionsPanel.add(salvarButton);

        // Adiciona os painéis à janela principal
        panel.add(topPanel, BorderLayout.NORTH); // Painel superior
        panel.add(scrollPane, BorderLayout.CENTER); // Área de texto no centro
        panel.add(bottomPanel, BorderLayout.SOUTH); // Painel inferior
        panel.add(actionsPanel, BorderLayout.EAST); // Painel lateral direito

        frame.add(panel); // Adiciona o painel principal à janela
        frame.setVisible(true);

        addEventListeners(); // Adiciona os listeners para os botões
    }

    // Metodo para configurar os eventos dos botões
    private void addEventListeners() {
        // Evento para o botão "Submeter"
        submeterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submeterPalavra(inputField.getText().trim().toUpperCase()); // Submete a palavra digitada
                inputField.setText(""); // Limpa o campo de entrada
            }
        });

        // Evento para o botão "Dica"
        dicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (moedas >= 100) {
                    moedas -= 100; // Deduz 100 moedas
                    darDica();
                    atualizarLabels(); // Atualiza os rótulos
                } else {
                    outputArea.append("Não tens moedas suficientes para comprar uma dica! Moedas atuais: " + moedas + ".\n");
                }
            }
        });

        // Evento para o botão "Salvar Progresso"
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarProgresso();
            }
        });
    }

    // Metodo para carregar os dados de um nível
    private void carregarNivel() {
        if (nivelAtual >= niveis.size()) { // Verifica se todos os níveis foram completados
            outputArea.append("Parabéns! Completaste todos os níveis!\n");
            return;
        }

        nivelAtualData = niveis.get(nivelAtual); // Obtém os dados do nível atual
        palavrasAdivinhadas = new java.util.HashSet<>(); // Inicializa o conjunto de palavras adivinhadas

        nivelLabel.setText("Level: " + (nivelAtual + 1)); // Atualiza o rótulo do nível
        letrasLabel.setText("Letters: " + nivelAtualData.letras); // Exibe as letras do nível
        moedasLabel.setText("Coins: " + moedas); // Exibe a quantidade de moedas

        outputArea.append("Nivel " + (nivelAtual + 1) + " carregado. Letras: " + nivelAtualData.letras + "\n");
    }

    // Metodo para conceder uma dica ao utilizador
    private void darDica() {
        for (String word : nivelAtualData.palavras) {
            if (!palavrasAdivinhadas.contains(word)) {
                outputArea.append("Dica: Uma das palavras começa com a letra '" + word.charAt(0) + "'\n");
                return;
            }
        }
    }

    // Metodo para atualizar os rótulos
    private void atualizarLabels() {
        moedasLabel.setText("Coins: " + moedas);
    }

    // Metodo para processar a palavra submetida pelo utilizador
    private void submeterPalavra(String word) {
        if (word.isEmpty()) return; // Ignora palavras vazias

        // Verifica se a palavra está na lista de palavras do nível atual
        if (java.util.Arrays.asList(nivelAtualData.palavras).contains(word)) {
            if (palavrasAdivinhadas.add(word)) { // Verifica se a palavra ainda não foi adivinhada
                moedas += 10;
                outputArea.append("Correto! Você ganhou 10 moedas! Moedas atuais: " + moedas + ".\n");
                atualizarLabels(); // Atualiza os rótulos

                // Verifica se todas as palavras do nível foram adivinhadas
                if (palavrasAdivinhadas.size() == nivelAtualData.palavras.length) {
                    nivelAtual++;
                    outputArea.append("Nível completo! Passando ao próximo nível.\n");
                    carregarNivel(); // Carrega o próximo nível
                }
            } else {
                outputArea.append("Já acertaste essa palavra!\n");
            }
        } else if (dicionario.contains(word)) { // Verifica se a palavra é válida, mas não faz parte do nível
            moedas += 10;
            outputArea.append("Palavra válida, mas não listada no nível! Você ganhou 10 moedas! Moedas atuais: " + moedas + ".\n");
            atualizarLabels(); // Atualiza os rótulos
        } else { // Palavra inválida
            outputArea.append("Palavra inválida! Tenta novamente.\n");
        }
    }

    // Metodo para salvar o progresso do jogo
    private void salvarProgresso() {
        outputArea.append("Progress saved!\n");
    }

    public static void main(String[] args) {
        try {
            // Carrega os dados dos níveis e o dicionário
            List<Guru.Nivel> niveis = Guru.carregarNiveis("ficheiro_niveis.txt");
            Set<String> dicionario = Guru.carregarDicionario("portuguese-large.txt");

            // Inicializa a interface gráfica na thread de eventos do Swing
            SwingUtilities.invokeLater(() -> new GuruGUI(niveis, dicionario));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading game files: " + e.getMessage());
        }
    }
}
