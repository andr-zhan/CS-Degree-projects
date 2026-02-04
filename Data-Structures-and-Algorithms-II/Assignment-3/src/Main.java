import java.util.*;

public class Main {
    static final int NONE = -1;
    static final int INFINITY = Integer.MAX_VALUE;

    // Classe que representa uma aresta da rede de fluxo
    static class Edge {
        private int destination;
        private int capacity;
        private int flow;

        public Edge(int destination, int capacity) {
            this.destination = destination;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int destination() { return destination; }
        public int capacity() { return capacity; }
        public void capacity(int capacity) { this.capacity = capacity; }
        public int flow() { return flow; }
        public void flow(int flow) { this.flow = flow; }
    }

    // Classe que representa a rede de fluxo
    static class FlowNetwork {
        private int source, sink; // NÃ³ de origem e nÃ³ de destino
        private int nodes; // NÃºmero total de nÃ³s
        private List<Edge>[] adjacents; // Lista de adjacÃªncia que guarda as arestas

        @SuppressWarnings("unchecked")
        public FlowNetwork(int nodes, int source, int sink) {
            this.nodes = nodes;
            this.source = source;
            this.sink = sink;
            adjacents = new List[nodes];
            for (int i = 0; i < nodes; i++) {
                adjacents[i] = new ArrayList<>();
            }
        }

        // Adiciona uma aresta com capacidade dada e uma aresta reversa com capacidade 0
        public void addEdge(int from, int to, int capacity) {
            adjacents[from].add(new Edge(to, capacity));
            adjacents[to].add(new Edge(from, 0));
        }

        // ConstrÃ³i a rede residual a partir da rede original
        private FlowNetwork buildResidualNetwork() {
            FlowNetwork r = new FlowNetwork(nodes, source, sink);
            for (int u = 0; u < nodes; u++) {
                for (Edge e : adjacents[u]) {
                    int v = e.destination();
                    int cap = e.capacity() - e.flow();
                    if (cap > 0) r.addEdge(u, v, cap); // Aresta direta com capacidade residual
                    if (e.flow() > 0) r.addEdge(v, u, e.flow()); // Aresta reversa com o fluxo atual
                }
            }
            return r;
        }

        // Atualiza a capacidade residual apÃ³s o aumento de fluxo
        private void updateResidualCapacity(int from, int to, int capacity, int flow) {
            for (Edge e : adjacents[from]) {
                if (e.destination() == to) {
                    e.capacity(capacity - flow);
                    break;
                }
            }
            for (Edge e : adjacents[to]) {
                if (e.destination() == from) {
                    e.capacity(flow);
                    break;
                }
            }
        }

        // Aumenta o fluxo ao longo do caminho encontrado
        private void incrementFlow(int[] p, int increment, FlowNetwork r) {
            int v = sink;
            int u = p[v];
            while (u != NONE) {
                boolean uv = false;
                for (Edge e : adjacents[u]) {
                    if (e.destination() == v) {
                        e.flow(e.flow() + increment); // Aumenta fluxo direto
                        r.updateResidualCapacity(u, v, e.capacity(), e.flow());
                        uv = true;
                        break;
                    }
                }
                if (!uv) {
                    for (Edge e : adjacents[v]) {
                        if (e.destination() == u) {
                            e.flow(e.flow() - increment); // Diminui fluxo na aresta reversa
                            r.updateResidualCapacity(v, u, e.capacity(), e.flow());
                            break;
                        }
                    }
                }
                v = u;
                u = p[v];
            }
        }

        // Procura um caminho aumentante usando BFS e devolve a capacidade mÃ­nima do caminho
        private int findPath(int[] p) {
            int[] cf = new int[nodes]; // Capacidade residual atÃ© cada nÃ³
            Queue<Integer> q = new LinkedList<>();
            for (int u = 0; u < nodes; ++u) p[u] = NONE;
            cf[source] = INFINITY;
            q.add(source);
            while (!q.isEmpty()) {
                int u = q.remove();
                if (u == sink) break;
                for (Edge e : adjacents[u]) {
                    int v = e.destination();
                    if (e.capacity() > 0 && cf[v] == 0) {
                        cf[v] = Math.min(cf[u], e.capacity());
                        p[v] = u;
                        q.add(v);
                    }
                }
            }
            return cf[sink]; // Retorna a capacidade do caminho atÃ© ao sink
        }

        // Algoritmo de Edmonds-Karp para encontrar o fluxo mÃ¡ximo
        public int edmondsKarp() {
            FlowNetwork r = buildResidualNetwork(); // ComeÃ§a com a rede residual
            int flowValue = 0;
            int[] p = new int[nodes];
            int increment;
            while ((increment = r.findPath(p)) > 0) {
                incrementFlow(p, increment, r);
                flowValue += increment;
            }
            return flowValue;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // NÃºmero de participantes
        int M = in.nextInt(); // NÃºmero de declaraÃ§Ãµes de interesse

        int S = 2 * N;         // NÃ³ de origem (source)
        int T = 2 * N + 1;     // NÃ³ de destino (sink)
        int size = 2 * N + 2;  // NÃºmero total de nÃ³s na rede

        FlowNetwork fn = new FlowNetwork(size, S, T);

        // Liga o nÃ³ source a todos os participantes (lado de saÃ­da)
        for (int i = 0; i < N; i++) fn.addEdge(S, i, 1);

        // Liga todos os participantes (lado de entrada) ao sink
        for (int i = 0; i < N; i++) fn.addEdge(N + i, T, 1);

        // Adiciona as arestas com base nas preferÃªncias: a -> N + b
        for (int i = 0; i < M; i++) {
            int a = in.nextInt();
            int b = in.nextInt();
            fn.addEdge(a, N + b, 1); // a gosta da carta de b
        }

        // Calcula o fluxo mÃ¡ximo e verifica se todos conseguem trocar de carta
        int maxflow = fn.edmondsKarp();
        System.out.println(maxflow == N ? "YES" : "NO");
    }
}