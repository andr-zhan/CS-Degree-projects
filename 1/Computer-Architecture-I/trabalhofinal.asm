.data

    buffer_entrada: .space 239600

    buffer_saida: .space 239600

    file_path: .string "/Users/andrezhuzhan/Desktop/trabalho ac/cat_noisy.gray"

    output_path: .string "/Users/andrezhuzhan/Desktop/trabalho ac/cat.gray"

    espaco_sort: .space 9

    msg: .asciz "1 - Media\n2 - Mediana"
.text



.globl main

# Fun√ß√£o para ler a imagem
read_image:
    li   a7, 1024     # System call para abrir arquivo
    la   a0, file_path    # Nome do arquivo de entrada
    li   a1, 0       # Abrir para leitura
    ecall             # Chamada do sistema para abrir o arquivo

    mv   s6, a0       # Salvar descritor de arquivo
    li   a7, 63       # System call para ler do arquivo
    mv   a0, s6       # Descritor de arquivo

    la   a1, buffer_entrada   # Endere√ßo do buffer de entrada
    li   a2, 239600       # Tamanho do buffer
    ecall             # Chamada do sistema para ler do arquivo

    li   a7, 57       # System call para fechar arquivo
    mv   a0, s6       # Descritor de arquivo
    ecall             # Chamada do sistema para fechar o arquivo
    ret

# Fun√ß√£o para escrever a imagem
write_image:

li s9, 3
    li   a7, 1024     # System call para abrir arquivo
    la   a0, output_path    # Nome do arquivo de sa√≠da
    li   a1, 1        # Abrir para escrita
    ecall             # Chamada do sistema para abrir o arquivo

    mv   s6, a0       # Salvar descritor de arquivo
    li   a7, 64       # System call para escrever no arquivo
    mv   a0, s6       # Descritor de arquivo
    la   a1, buffer_saida   # Endere√ßo do buffer de sa√≠da
    li   a2, 239600       # Tamanho do buffer
    ecall             # Chamada do sistema para escrever no arquivo

    li   a7, 57       # System call para fechar arquivo
    mv   a0, s6       # Descritor de arquivo
    ecall             # Chamada do sistema para fechar o arquivo

    ret
    

# Fun√ß√£o para aplicar o filtro de m√©dia
filtro_media:                  

    addi sp, sp, -40           # Alocar espaÁo na stack
    sw ra, 36(sp)              # Save return address
    sw s0, 32(sp)              
    sw s1, 28(sp)              
    sw s2, 24(sp)              
    sw s3, 20(sp)             
    sw s4, 16(sp)              
    sw s5, 12(sp)             
    sw s6, 8(sp)              
    sw s7, 4(sp)            
    sw s8, 0(sp)               

    mv s0, a0 # s0 = entrada
    mv s1, a1 # s1 = saida
    mv s2, a2 # s2 = largura
    mv s3, a3 # s3 = altura
    li s4, 1 # Constante 1
    li s6, 9 # Constante 9
    li t1, 1 # y = 1
for_y_media:
    li t0, 1 # x = 1
    for_x_media:
    li t4, 0 # soma = 0
    li t3, -1 # j = -1
    for_j_media:
    li t2, -1 # i = -1
    for_i_media:
    add t5, t1, t3 # y + j
    mul t5, t5, s2 # (y + j) * largura
    add t5, t5, t0 # (y + j) * largura + x
    add t5, t5, t2 # (y + j) * largura + x + i
    add t5, t5, s0 # entrada[(y + j) * largura + x + i]
    lbu t6, 0(t5) # carrega entrada[(y + j) * largura + x + i]
    add t4, t4, t6 # soma = soma + entrada[(y + j) * largura + x + i]
    addi t2, t2, 1 # i++
    ble t2, s4, for_i_media
    addi t3, t3, 1 # j++
    ble t3, s4, for_j_media
    mul s5, t1, s2 # y * largura
    add s5, s5, t0 # y * largura + x
    add s5, s5, s1 # saida[y * largura + x]
    div t4, t4, s6 # soma / 9
    sb t4, 0(s5) # saida[y * largura + x] = soma / 9
    addi s7, s2, -1 # largura - 1
    addi t0, t0, 1 # x++
    blt t0, s7, for_x_media
addi s8, s3, -1 # altura - 1
addi t1, t1, 1 # y++
blt t1, s8, for_y_media


lw ra, 36(sp)             
lw s0, 32(sp)             
lw s1, 28(sp)             
lw s2, 24(sp)             
lw s3, 20(sp)            
lw s4, 16(sp)             
lw s5, 12(sp)            
lw s6, 8(sp)             
lw s7, 4(sp)            
lw s8, 0(sp)              
addi sp, sp, 40            
ret                        
    
# FunÁ„o para ordenar o array no caso do utilizador usar o filtro da mediana
bubble_sort:               
 
    addi sp, sp, -12       # Alocar espaaÁo na stack
    sw s0, 0(sp)           
    sw s1, 4(sp)           
    sw s2, 8(sp)

    mv s0, a1              # s0 = tamanho array
    mv s1, a0              # s1 = array

    addi s2, s0, -1        # s2 = tamanho - 1
    li t1, 0               # i = 0
    for_i_bubblesort:
    li t0, 0               # j = 0
    sub t3, s2, t1         # t3 = tamanho - 1 - i
    for_j_bubblesort:
    add t4, s1, t0         # Calcular o endereÁo de array[j]
    lbu t5, 0(t4)          # Carregar array[j] em t5
    lbu t6, 1(t4)          # Carregar array[j+1] em t6
    bge t6, t5, nao_troca  

    # troca para ordenar
    sb t6, 0(t4)           # array[j] = array[j+1]
    sb t5, 1(t4)           # array[j+1] = antigo array[j]
    
    nao_troca:             # n„o troca a ordenaÁ„o
    addi t0, t0, 1         # j++
    blt t0, t3, for_j_bubblesort

    addi t1, t1, 1         # i++
    blt t1, s2, for_i_bubblesort


    mv a0, s1              # array ordenado

    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    addi sp, sp, 12
    ret

# FunÁ„o para aplicar o filtro da mediana
filtro_mediana:

 addi sp, sp, -52 # Alocar espaÁo na stack
 sw s0, 0(sp) 
 sw s1, 4(sp) 
 sw s2, 8(sp) 
 sw s3, 12(sp) 
 sw s4, 16(sp) 
 sw s5, 20(sp) 
 sw s6, 24(sp) 
 sw s7, 28(sp) # y
 sw s8, 32(sp) # x
 sw s9, 36(sp) # k
 sw s10, 40(sp) # j
 sw s11, 44(sp) # i
 sw ra, 48(sp) # Save return address
 
 mv s0, a0 # s0 = buffer_entrada
 mv s1, a1 # s1 = buffer_saida
 mv s2, a2 # s2 = largura
 mv s3, a3 # s3 = altura
 la s4, espaco_sort # s4 = array
 li t0, 1 # constante 1
 
 addi s5, s3, -1 # altura - 1
 addi s6, s2, -1 # largura - 1
 
  li s7, 1 # y = 1
  for_y_mediana:

  li s8, 1 # x = 1
  for_x_mediana:
  li s9, 0 # k = 0
  li s10, -1 # j = -1
  for_j_mediana:
  li s11, -1 # i = -1
  for_i_mediana:
  add t1, s7, s10 # y + j
  mul t1, t1, s2 # (y + j) * largura
  add t1, t1, s8 # (y + j) * largura  + x
  add t1, t1, s11 # (y + j) * largura  + x + i
  add t1, t1, s0
  lbu t2, 0(t1)

  add t3, s4, s9 # array[k]
  sb t2, 0(t3) # array[k] = entrada[(y + j) * largura  + x + i]

  addi s9, s9, 1 # k++

  addi s11, s11, 1 # i++
  ble s11, t0, for_i_mediana

  addi s10, s10, 1 # j++
  ble s10, t0, for_j_mediana

  mv a0, s4 # definir o array no a0 do bubble_sort
  li a1, 9 # tamanho do array
  jal ra, bubble_sort # salta para a funÁ„o bubble_sort para ordenar o array
  mv s4, a0 # carrega o array ordenado para s4

  mul t4, s7, s2 # y * largura
  add t4, t4, s8 # y * largura + x
  add t4, t4, s1 # saida[y * largura + x]

  lbu t5, 4(s4) # carrega a mediana
  sb t5, 0(t4) # guarda a mediana em saida[y * largura + x]

  addi s8, s8, 1 # x++
  blt s8, s6, for_x_mediana

  addi s7, s7, 1 # y++
  blt s7, s5, for_y_mediana

 
 lw s0, 0(sp)
 lw s1, 4(sp)
 lw s2, 8(sp)
 lw s3, 12(sp)
 lw s4, 16(sp)
 lw s5, 20(sp)
 lw s6, 24(sp)
 lw s7, 28(sp)
 lw s8, 32(sp)
 lw s9, 36(sp)
 lw s10, 40(sp)
 lw s11, 44(sp)
 lw ra, 48(sp)
 addi sp, sp, 52
 ret   

# FunÁ„o main
main:

 la a0, msg # Apresenta a mensagem "1 - Media, 2 - Mediana"
 li a7, 51 # Pede ao utilizador  para escolher entre a media ou mediana
 ecall
 addi  t2, a0, 0 # Carrega o numero escolhido pelo utilizador para t2
    
 jal read_image             # Ler imagem de entrada
 la a0, buffer_entrada      # Endere√ßo do buffer de entrada
 la a1, buffer_saida        # Endere√ßo do buffer de sa√≠da
 li a2, 599                 # Largura da imagem
 li a3, 400                 # Altura da imagem
    
    
 li t0, 1 # Constante 1
 beq t2, t0, CORRE_MEDIA # Se escolheu 1, usa filtro da media
 CORRE_MEDIANA:
 jal filtro_mediana
 jal write_image # Escreve a imagem final
 li a7, 93 # Sai do programa
 ecall

 CORRE_MEDIA:
 jal filtro_media
 jal write_image # Escreve a imagem final
 li a7, 93 # Sai do programa
 ecall
