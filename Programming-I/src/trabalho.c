#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>

void guardarficheiro(int depositos[],int casas[])//funçao para criar um ficheiro e guardar o estado atual do jogo
{
    char nomeficheiro[100];
    printf("Qual o nome pretendido para o ficheiro\n R:");
    scanf("%s", nomeficheiro);

    FILE *file = fopen(strcat(nomeficheiro,".txt"), "w");
    if (file == NULL) 
    {printf("Erro ao abrir o arquivo.\n");}

    // guardar o tabuleiro num ficheiro
    fprintf(file, "%d\n", depositos[0]);
    for (int i = 11; i >= 6; --i) 
    {
        fprintf(file, "%d ", casas[i]);
    }
    fprintf(file, "\n");
    fprintf(file, "%d\n", depositos[1]);
    for (int i = 0; i <= 5; ++i) 
    {
        fprintf(file, "%d ", casas[i]);
    }
    fprintf(file, "\n");

    fclose(file);
    printf("O estado do tabuleiro foi salvo em '%s'.\n", nomeficheiro);
}

void bolas12243648(int pedrasiniciais, int ultimajogada, int jogada, int casas[])//caso o número de bolas seja 12/24/36/48 ele dará voltas compeltas ao tabuleiro pelo que terá que saltar a casa inicial selecionada e adicionar +1 na casa seguinte
{
    if(pedrasiniciais >= 12){casas[ultimajogada+1] += 1;casas[jogada] = 0;}
    else if (pedrasiniciais >= 24){casas[ultimajogada+1] += 1; casas[ultimajogada+2] += 1;casas[jogada] = 0;}
    else if (pedrasiniciais >= 36){casas[ultimajogada+1] += 1;casas[ultimajogada+2] += 1;casas[ultimajogada+3] += 1;casas[jogada] = 0;}
    else if (pedrasiniciais >= 48){casas[ultimajogada+1] += 1;casas[ultimajogada+2] += 1;casas[ultimajogada+3] += 1;casas[ultimajogada+4] += 1;casas[jogada] = 0;}  
}

void tabuleiro(int casas[], int depositos[])//funçao para mostrar o tabuleiro
{   
    printf (" |---|--|--|--|--|--|--|---| \n");
    printf (" |   |%d |%d |%d |%d |%d |%d |   |\n",casas[11],casas[10],casas[9],casas[8],casas[7],casas[6]);
    printf (" |  %d|-----------------| %d | \n", depositos[0], depositos[1]);
    printf (" |   |%d |%d |%d |%d |%d |%d |   |\n",casas[0],casas[1],casas[2],casas[3],casas[4],casas[5]);
    printf (" |---|--|--|--|--|--|--|---| \n\n");
}

void computador(int casas[], int depositos[], int soma)//funçao para simular a jogada do computador
{
    printf("VEZ DO COMPUTADOR\n");

    int jogada, jogadainicial, p = 0;
    for (int i=6; i<=11; i++) {
        if (casas[i] > 1) {p = 1;} // Verifica se há casas com mais que 1 pedra. Se sim, p == 1
    }
    if(soma != 0){// quando soma é diferente de 0 todas as casas com mais de 0 pedras sao validas.
            do{
            jogada = 1 + ( rand() % 6 );// numero aleatorio de 1 a 6
            }while (jogada < 1 || jogada > 6 || casas[jogada+5] == 0 || ((p == 1) && (casas[jogada+5] == 1))); }
            jogadainicial = jogada;
            if (jogada == 1){jogada = 6;}else if(jogada == 2){(jogada = 7);}else if(jogada == 3){(jogada = 8);}else if(jogada == 4){(jogada = 9);}else if (jogada == 5){(jogada = 10);}else if (jogada == 6){(jogada = 11);}// acertar a casa escolhida de acordo com a casa no tabuleiro
    else if(soma == 0){// quando soma = 0, existem jogadas inválidas.
            int jogadavalida = 0;
        do
        {
            do{
              jogada = 1 + ( rand() % 6 );// numero aleatorio de 1 a 6
            }while (jogada < 1 || jogada > 6);
            jogadainicial = jogada;
            if (jogada == 1){jogada = 6;}else if(jogada == 2){(jogada = 7);}else if(jogada == 3){(jogada = 8);}else if(jogada == 4){(jogada = 9);}else if (jogada == 5){(jogada = 10);}else if (jogada == 6){(jogada = 11);}// acertar a casa escolhida de acordo com a casa no tabuleiro
            //Verifica se as casas sao válidas
            if (jogada == 6)
            {   
                if(casas[jogada] < 6){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 7)
            {   
                if(casas[jogada] < 5){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 8)
            {   
                if(casas[jogada] < 4){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 9)
            {   
                if(casas[jogada] < 3){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 10)
            {   
                if(casas[jogada] < 2){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 11)
            {   
                if(casas[jogada] < 1){jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
        } while (jogadavalida == 0);
    }

    int pedras = casas[jogada];
    int pedrasiniciais = pedras;
    casas[jogada] = 0; 

    int ultimajogada;
    for (int i = 1; i < pedras; i++)// adicionar pedras as casas de acordo com a casa escolhida
    {   
        casas[jogada+i] += 1;
        if((jogada+i) == 11 || (jogada+i) == 12){// quando chega a última casa sendo o 11 no array volta ao 0, e continua a adicionar o restante das bolas
            if ((jogada+i) == 12){pedras = pedras;}
            else{pedras -= i;}
            jogada = 0;
            i = -1;
        }
        ultimajogada = (jogada+i);
    }
        //caso o número de bolas seja 12/24/36/48 ele dará voltas compeltas ao tabuleiro pelo que terá que saltar a casa inicial selecionada e adicionar +1 na casa seguinte
        bolas12243648(pedrasiniciais,ultimajogada,jogada,casas);

        //captura das pedras para os depositos
        if (ultimajogada < 6){
        if (casas[ultimajogada] == 2 || casas[ultimajogada] == 3){
        for (int i = 0; (casas[ultimajogada-i] == 2 || casas[ultimajogada-i] == 3) && (ultimajogada-i) < 6; i++)
        {
            depositos[0] += casas[ultimajogada-i];
            casas[ultimajogada-i] = 0;
        }
        }
    }

    printf("\nCasa escolhida - %d\n", jogadainicial);
    tabuleiro(casas, depositos);
}

void jogadorA (int casas[], int depositos[], int soma)//funçao do jogadorA
{
    
    printf("VEZ DO JOGADOR A \n");
    int jogadainicial, jogada, p = 0;
    for (int i=0; i<=5; i++) {
        if (casas[i] > 1) {p = 1;} // Verifica se há casas com mais que 1 pedra. Se sim, p == 1
    }
    if(soma != 0){// quando soma é diferente de 0 todas as casas com mais de 0 pedras sao validas.
        do
        {
            printf("Indique o número da casa em que deseja jogar (1-6), e (0) se desejar guardar o jogo num ficheiro\n R:");
            scanf(" %d", &jogada);printf("\n");
            jogadainicial = jogada;
            if (jogada == 0){guardarficheiro(depositos,casas);}
            else if (jogada > 6 || jogada < 1) {printf("\nOPÇÃO INVÁLIDA!!!\n\n");}
            else if ((p == 1) && (casas[jogada-1] == 1)) {printf("\nCasa com 1 pedra. Escolha outra casa com mais pedras!\n\n");}
            else if (casas[jogada-1] == 0) {printf("\nCasa com 0 pedras. Escolha outra casa!\n\n");}
        }while (jogada > 6 || jogada < 1 || casas[jogada] == 0 || ((p == 1) && (casas[jogada-1] == 1)));}//vai continuar a pedir ao utilizador um input caso seja introduzido casas inválidas
 
    else if(soma == 0){// quando soma = 0, existem jogadas inválidas.
            int jogadavalida = 0;
        do
        {
            printf("Indique o número da casa em que deseja jogar (1-6) de modo a introduzir pedras na cada do adversário, e (0) se desejar guardar o jogo num ficheiro\n R:");
            scanf(" %d", &jogada);printf("\n");
            jogadainicial = jogada;
            if (jogada == 0){guardarficheiro(depositos,casas);}
            else if (jogada > 6 || jogada < 1) {printf("\nOPÇÃO INVÁLIDA!!!\n\n");}
            else if(casas[jogada-1] == 0) {printf("\nCasa com 0 pedras. Escolha outra casa!\n\n");}
           
            //verifica se as casas escolhidas sao válidas
            if ((jogada-1) == 0)
            {   
                if(casas[jogada-1] < 6){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if ((jogada-1) == 1)
            {   
            if(casas[jogada-1] < 5){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if ((jogada-1) == 2)
            {   
                if(casas[jogada-1] < 4){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if ((jogada-1) == 3)
            {   
                if(casas[jogada-1] < 3){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if ((jogada-1) == 4)
            {   
                if(casas[jogada-1] < 2){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if ((jogada-1) == 5)
            {   
                if(casas[jogada-1] < 1){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
        } while (jogadavalida == 0);
    }

    jogada -= 1;
    int pedras = casas[jogada];
    int pedrasiniciais = pedras;
    casas[jogada] = 0;
    int ultimajogada;
    for (int i = 1; i <= pedras; i++)// adicionar pedras as casas de acordo com a casa escolhida
    { 
        casas[jogada+i] += 1;
        if((jogada+i) == 11){// quando chega a última casa sendo o 11 no array volta ao 0, e continua a adicionar o restante das bolas
            pedras -= i;
            jogada = 0;
            i = -1;
        }
        ultimajogada = (i+jogada);
    }
    //caso o número de bolas seja 12/24/36/48 ele dará voltas compeltas ao tabuleiro pelo que terá que saltar a casa inicial selecionada e adicionar +1 na casa seguinte
    bolas12243648(pedrasiniciais,ultimajogada,jogada,casas);

    
    //captura das pedras para os depositos
    if (ultimajogada > 5){
        if (casas[ultimajogada] == 2 || casas[ultimajogada] == 3){
        for (int i = 0; (casas[ultimajogada-i] == 2 || casas[ultimajogada-i] == 3) && (ultimajogada-i) > 5; i++)
        {
            depositos[1] += casas[ultimajogada-i];
            casas[ultimajogada-i] = 0;
        }
        }
    }

    printf("\nCasa escolhida - %d\n", jogadainicial);
    tabuleiro(casas, depositos);
}

void jogadorB (int casas[], int depositos[],int soma)//funçao do jogadorB
{
    printf("VEZ DO JOGADOR B \n");

    int jogada, jogadainicial, p = 0;
    for (int i=6; i<=11; i++) {
        if (casas[i] > 1) {p = 1;} // Verifica se há casas com mais que 1 pedra. Se sim, p == 1
    }
    if(soma != 0){// quando soma é diferente de 0 todas as casas com mais de 0 pedras sao validas.
        do
        {
            do
            {
                printf("Indique o número da casa em que deseja jogar (1-6), e (0) se desejar guardar o jogo num ficheiro\n R:");
                scanf(" %d", &jogada);printf("\n");
                if (jogada == 0){guardarficheiro(depositos,casas);}
                else if (jogada > 6 || jogada < 1) {printf("\nOPÇÃO INVÁLIDA!!!\n\n");}
            } while (jogada > 6 || jogada < 1);
            jogadainicial = jogada;
            if (jogada == 1){jogada = 6;}else if(jogada == 2){(jogada = 7);}else if(jogada == 3){(jogada = 8);}else if(jogada == 4){(jogada = 9);}else if (jogada == 5){(jogada = 10);}else if (jogada == 6){(jogada = 11);}// acertar a casa escolhida de acordo com a casa no tabuleiro
            if ((p == 1) && (casas[jogada] == 1) ) {printf("\nCasa com 1 pedra. Escolha outra casa com mais pedras!\n\n");}
            else if (casas[jogada] == 0) {printf("\nCasa com 0 pedras. Escolha outra casa!\n\n");}
        }while (jogada > 11 || jogada < 6 || casas[jogada] == 0 || ((p == 1) && (casas[jogada] == 1)));
    }
    else if(soma == 0)// quando soma = 0, existem jogadas inválidas.
    {
        int jogadavalida = 0;
        do
        {
            do
            {
                printf("Indique o número da casa em que deseja jogar (1-6) de modo a introduzir pedras na cada do adversário, e (0) se desejar guardar o jogo num ficheiro\n R:");
                scanf(" %d", &jogada);printf("\n");
                if (jogada == 0){guardarficheiro(depositos,casas);}
                else if (jogada > 6 || jogada < 1) {printf("\nOPÇÃO INVÁLIDA!!!\n\n");}

            } while (jogada > 6 || jogada < 1);
            jogadainicial = jogada;
            if (jogada == 1){jogada = 6;}else if(jogada == 2){(jogada = 7);}else if(jogada == 3){(jogada = 8);}else if(jogada == 4){(jogada = 9);}else if (jogada == 5){(jogada = 10);}else if (jogada == 6){(jogada = 11);}// acertar a casa escolhida de acordo com a casa no tabuleiro

            //verifica se as casas escolhidas sao válidas
            if (jogada == 6)
            {   
                if(casas[jogada] < 6){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 7)
            {   
            if(casas[jogada] < 5){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 8)
            {   
                if(casas[jogada] < 4){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 9)
            {   
                if(casas[jogada] < 3){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 10)
            {   
                if(casas[jogada] < 2){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
            else if (jogada == 11)
            {   
                if(casas[jogada] < 1){printf("CASA INVÁLIDA, PEDRAS INSUFICIENTES");jogadavalida = 0;}
                else{jogadavalida = 1;}
            }
        } while (jogadavalida == 0);
    }
    
    int pedras = casas[jogada];
    int pedrasiniciais = pedras;
    casas[jogada] = 0; 

    int ultimajogada;
    for (int i = 1; i <= pedras; i++)// adicionar pedras as casas de acordo com a casa escolhida
    {   
        casas[jogada+i] += 1;
        if((jogada+i) == 11 || (jogada+i) == 12){
            if ((jogada+i) == 12){pedras = pedras-1;}// quando chega a última casa sendo o 11 no array volta ao 0, e continua a adicionar o restante das bolas
            else{pedras -= i+1;} // +1 para compensar a mudança de valor do i para -1
            jogada = 0;
            i = -1;
        }
        ultimajogada = (jogada+i);
    }
        //caso o número de bolas seja 12/24/36/48 ele dará voltas compeltas ao tabuleiro pelo que terá que saltar a casa inicial selecionada e adicionar +1 na casa seguinte
        bolas12243648(pedrasiniciais,ultimajogada,jogada,casas);

        //captura das pedras para os depositos
        if (ultimajogada < 6){
        if (casas[ultimajogada] == 2 || casas[ultimajogada] == 3){
        for (int i = 0; (casas[ultimajogada-i] == 2 || casas[ultimajogada-i] == 3) && (ultimajogada-i) < 6; i++)
        {
            depositos[0] += casas[ultimajogada-i];
            casas[ultimajogada-i] = 0;
        }
        }
    }
    printf("\nCasa escolhida - %d\n", jogadainicial);
    tabuleiro(casas, depositos);
}

int main(int argc, char *argv[])
{
    int casas[12];
    int depositos[2];

    if (argc == 1) {
    for (int i = 0; i < 12; i++) // distribuir pedras entre as casas
    {
        casas[i] = 4;
        if ( i < 1)
        {
            depositos[i] = 0;
        }    
    }
    } else if (argc == 2) {// caso o codigo seja inicilizado com un ficheiro , ele copia o jogo do ficheiro para o jogo atual
    FILE *file = fopen(argv[1], "r");
    if (file == NULL) 
    {printf("Erro ao abrir o arquivo.\n");}
    fscanf(file, "%d\n", &depositos[0]);
    for (int i = 11; i >= 6; --i) 
    {fscanf(file, "%d ", &casas[i]);}
    fscanf(file, "%d\n", &depositos[1]);
    for (int i = 0; i <= 5; ++i)
    {fscanf(file, "%d ", &casas[i]);}
    }

    printf("-----------------------\n");
    printf("  Bem vindo ao Ouri \n");
    printf("-----------------------\n\n");

    int modojogo = 0;
    do//escolha do modo de jogo
    {
        printf("Qual modo de jogo pretende jogar? \n 1 - Jogador VS Jogador\n 2 - Jogador VS Computador\n R: ");
        scanf(" %d", &modojogo);
        printf("\n");
        if (modojogo != 1 && modojogo != 2){
            printf("\nOPÇÃO INVÁLIDA!!!\n\n");
            modojogo = 0;
        }
    }while (modojogo == 0);
    
    tabuleiro(casas, depositos);//tabuleiro inicial

    if (modojogo == 1)//jogador vs jogador
    {
        int fimjogo = 0;
        do{
            int somaA = 0;
            for(int i = 0; i <= 5; i++) {somaA +=casas[i];}
            int somaB = 0;
            for(int i = 6; i <= 11; i++) {somaB += casas[i];}

         if (somaB == 0)
         {
            if (casas[0]<6 && casas[1] < 5 && casas[2]<4 && casas[3]<3 && casas[4]<2 && casas[5]<1)//caso um jogador ficar sem pedras e o adversario nao as conseguir repor acaba o jogo
            {
                printf("Pedras insuficientes para continuar o jogo");
                fimjogo = 1;
            }
            else
            {
                printf("O jogador B ficou sem pedras no seu campo. O jogador A deve obrigatoriamente introduzir pedras no lado adversário.\n");//um jogador fica sem pedras mas ainda é possivel continuar o jogo
                jogadorA(casas,depositos,somaB);
            }
         }
         else if (somaA == 0)
         {
            if(casas[6]<6 && casas[7] < 5 && casas[8]<4 && casas[9]<3 && casas[10]<2 && casas[11]<1)//caso um jogador ficar sem pedras e o adversario nao as conseguir repor acaba o jogo
            {
                printf("Pedras insuficientes para continuar o jogo");
                fimjogo = 1;
            }
            else 
            {
                printf("O jogador A ficou sem pedras no seu campo. O jogador B deve obrigatoriamente introduzir pedras no lado adversário.\n");//um jogador fica sem pedras mas ainda é possivel continuar o jogo
                jogadorB(casas,depositos,somaA);
            }
         }
         else
         {
            jogadorA(casas,depositos,somaA);
            jogadorB(casas,depositos,somaB);
         }    
        }while (depositos[0]< 25 && depositos[1] < 25 && fimjogo == 0);

        //vencedor do jogo
        if (depositos[0] > depositos[1]){
        printf("Jogador A = %d pedras\nJogador B = %d pedras\nVencedor = jogador B", depositos[1],depositos[0]);
        }
        else if(depositos[1] > depositos[0]){
        printf("Jogador A = %d pedras\nJogador B = %d pedras\nVencedor = jogador A", depositos[1],depositos[0]);
        }
    }
    else if (modojogo == 2)//jogador vs jogador
    {
        int fimjogo = 0;
        do{
            int somaA = 0;
            for(int i = 0; i <= 5; i++) {somaA +=casas[i];}
            int somaComputador = 0;
            for(int i = 6; i <= 11; i++) {somaComputador += casas[i];}

         if (somaComputador == 0)
         {
            if (casas[0]<6 && casas[1] < 5 && casas[2]<4 && casas[3]<3 && casas[4]<2 && casas[5]<1)//caso um jogador ficar sem pedras e o adversario nao as conseguir repor acaba o jogo
            {
                printf("Pedras insuficientes para continuar o jogo");
                fimjogo = 1;
            }
            else
            {
                printf("O jogador B ficou sem pedras no seu campo. O jogador A deve obrigatoriamente introduzir pedras no lado adversário.\n");//um jogador fica sem pedras mas ainda é possivel continuar o jogo
                jogadorA(casas,depositos,somaComputador);
            }
         }
         else if (somaA == 0)
         {
            if(casas[6]<6 && casas[7] < 5 && casas[8]<4 && casas[9]<3 && casas[10]<2 && casas[11]<1)//caso um jogador ficar sem pedras e o adversario nao as conseguir repor acaba o jogo
            {
                printf("Pedras insuficientes para continuar o jogo");
                fimjogo = 1;
            }
            else 
            {
                printf("O jogador A ficou sem pedras no seu campo. O jogador B deve obrigatoriamente introduzir pedras no lado adversário.\n");//um jogador fica sem pedras mas ainda é possivel continuar o jogo
                jogadorB(casas,depositos,somaA);
            }
         }
         else
         {
            jogadorA(casas,depositos,somaA);
            computador(casas,depositos,somaComputador);
         }    
        }while (depositos[0]< 25 && depositos[1] < 25 && fimjogo == 0);

        //vencedor do jogo
        if (depositos[0] > depositos[1]){
        printf("Jogador A = %d pedras\nComputador = %d pedras\nVencedor = Computador", depositos[1],depositos[0]);
        }
        else if(depositos[1] > depositos[0]){
        printf("Jogador A = %d pedras\nComputador = %d pedras\nVencedor = jogador A", depositos[1],depositos[0]);
        }
    }
    return 0;
}
