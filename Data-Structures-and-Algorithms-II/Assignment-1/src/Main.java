import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // Leitura do nÃºmero de nÃºmeros disponÃ­veis (N) e do nÃºmero de sonhos (D)
        String[] linha = br.readLine().split(" ");
        int N = Integer.parseInt(linha[0]);
        int D = Integer.parseInt(linha[1]);

        // Guardamos os nÃºmeros disponÃ­veis numa lista
        int[] numeros = new int[N];
        linha = br.readLine().split(" ");
        for (int i = 0; i < N; i++) {
            numeros[i] = Integer.parseInt(linha[i]);
        }

        // Guardamos os tamanhos dos sonhos na ordem em que foram pedidos
        int[] sonhos = new int[D];
        for (int i = 0; i < D; i++) {
            sonhos[i] = Integer.parseInt(br.readLine());
        }

        // Calculamos o desperdÃ­cio mÃ­nimo ao empacotar os sonhos nos nÃºmeros disponÃ­veis
        int desperdicioMinimo = encontrarDesperdicioMinimo(N, D, numeros, sonhos);

        // Imprimimos o resultado final
        System.out.println(desperdicioMinimo);
    }

    private static int encontrarDesperdicioMinimo(int N, int D, int[] numeros, int[] sonhos) {
        // Ordenamos os nÃºmeros para facilitar a busca pelo menor que pode conter os sonhos
        Arrays.sort(numeros);

        int INF = Integer.MAX_VALUE;
        // Criamos um array para armazenar o desperdÃ­cio mÃ­nimo atÃ© cada sonho
        int[] dp = new int[D + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0; // Caso base: 0 sonhos = 0 desperdÃ­cio

        // Percorremos os sonhos e tentamos encaixÃ¡-los nos nÃºmeros disponÃ­veis
        for (int i = 0; i < D; i++) {
            int tamanhoTotal = 0;
            for (int j = i; j < D; j++) {
                tamanhoTotal += sonhos[j]; // Somamos os tamanhos dos sonhos consecutivos

                // Procuramos o menor nÃºmero disponÃ­vel que pode conter este conjunto de sonhos
                int indice = buscaBinaria(numeros, tamanhoTotal);
                if (indice == -1) break; // Se nÃ£o houver nÃºmero suficiente, paramos

                int desperdicio = numeros[indice] - tamanhoTotal; // Calculamos o desperdÃ­cio atual
                dp[j + 1] = Math.min(dp[j + 1], dp[i] + desperdicio); // Atualizamos o desperdÃ­cio mÃ­nimo
            }
        }

        return dp[D]; // O resultado final Ã© o desperdÃ­cio mÃ­nimo possÃ­vel ao embalar todos os sonhos
    }

    private static int buscaBinaria(int[] numeros, int alvo) {
        // ImplementaÃ§Ã£o da busca binÃ¡ria para encontrar o menor nÃºmero que pode acomodar os sonhos
        int esquerda = 0, direita = numeros.length - 1;
        while (esquerda <= direita) {
            int meio = esquerda + (direita - esquerda) / 2;
            if (numeros[meio] >= alvo) {
                direita = meio - 1;
            } else {
                esquerda = meio + 1;
            }
        }
        return esquerda < numeros.length ? esquerda : -1; // Retorna o Ã­ndice do nÃºmero adequado ou -1 se nÃ£o existir
    }
}