import java.util.*;

public class Main {

    private static int nodes;                    // NÃºmero total de nÃ³s (habitantes)
    private static List<Integer>[] adjacents;    // Lista de adjacÃªncia representando a Ã¡rvore
    private static int maxDistance;              // DistÃ¢ncia mÃ¡xima encontrada em uma das DFS (serÃ¡ a resposta)
    private static int farthestNode;             // NÃ³ mais distante encontrado durante a DFS

    // EnumeraÃ§Ã£o para representar as cores dos nÃ³s durante a DFS
    // WHITE = nÃ£o visitado, GREY = sendo visitado, BLACK = totalmente visitado
    enum Colour {
        WHITE, GREY, BLACK
    }

    // FunÃ§Ã£o que realiza uma DFS a partir de um nÃ³ de inÃ­cio
    public static int dfs(int startNode) {
        Colour[] colour = new Colour[nodes];    // Cor de cada nÃ³
        int[] distance = new int[nodes];        // DistÃ¢ncia atÃ© o nÃ³ a partir do inÃ­cio
        int[] p = new int[nodes];               // Predecessor de cada nÃ³
        int[] d = new int[nodes];               // Tempo de descoberta
        int[] f = new int[nodes];               // Tempo de finalizaÃ§Ã£o
        int[] time = new int[1];                // Tempo atual (usado como referÃªncia)

        // InicializaÃ§Ã£o dos arrays
        for (int u = 0; u < nodes; u++) {
            colour[u] = Colour.WHITE;
            p[u] = -1;
            distance[u] = 0;
        }

        time[0] = 0;
        maxDistance = 0;
        farthestNode = startNode;

        // ComeÃ§a a DFS
        dfsVisit(startNode, colour, p, d, f, time, distance);

        // Retorna o nÃ³ mais distante encontrado
        return farthestNode;
    }

    // FunÃ§Ã£o recursiva de DFS (profundidade)
    private static void dfsVisit(int u, Colour[] colour, int[] p, int[] d, int[] f, int[] time, int[] distance) {
        time[0]++;           // Incrementa tempo ao descobrir o nÃ³
        d[u] = time[0];      // Armazena tempo de descoberta
        colour[u] = Colour.GREY; // Marca como sendo visitado

        // Para cada vizinho do nÃ³ atual
        for (int v : adjacents[u]) {
            if (colour[v] == Colour.WHITE) { // Se ainda nÃ£o foi visitado
                p[v] = u;                    // Define pai
                distance[v] = distance[u] + 1; // Atualiza distÃ¢ncia
                dfsVisit(v, colour, p, d, f, time, distance); // Visita recursivamente

                // Atualiza o nÃ³ mais distante encontrado, se necessÃ¡rio
                if (distance[v] > maxDistance) {
                    maxDistance = distance[v];
                    farthestNode = v;
                }
            }
        }

        // Finaliza o nÃ³ atual
        colour[u] = Colour.BLACK;
        time[0]++;
        f[u] = time[0]; // Tempo de finalizaÃ§Ã£o
    }

    // FunÃ§Ã£o principal do programa
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Verifica se hÃ¡ entrada
        if (!sc.hasNextInt()) return;

        nodes = sc.nextInt(); // LÃª o nÃºmero de habitantes (nÃ³s)

        // Inicializa a lista de adjacÃªncia
        @SuppressWarnings("unchecked")
        List<Integer>[] temp = new ArrayList[nodes];
        adjacents = temp;

        for (int i = 0; i < nodes; i++) {
            adjacents[i] = new ArrayList<>();
        }

        // LÃª as conexÃµes (arestas)
        for (int i = 0; i < nodes - 1; i++) {
            int u = sc.nextInt() - 1; // transforma para Ã­ndice base 0
            int v = sc.nextInt() - 1;

            // ValidaÃ§Ã£o de seguranÃ§a
            if (u < 0 || v < 0 || u >= nodes || v >= nodes) continue;

            // Adiciona arestas bidirecionais
            adjacents[u].add(v);
            adjacents[v].add(u);
        }

        // Caso especial: apenas um habitante
        if (nodes == 1) {
            System.out.println(0);
            return;
        }

        // Primeira DFS para encontrar o nÃ³ mais distante de qualquer ponto
        int farthestFromStart = dfs(0);

        // Segunda DFS a partir desse nÃ³ distante, para encontrar o "diÃ¢metro" da Ã¡rvore
        dfs(farthestFromStart);

        // Imprime a maior menor distÃ¢ncia entre dois habitantes (diÃ¢metro)
        System.out.println(maxDistance);
    }
}