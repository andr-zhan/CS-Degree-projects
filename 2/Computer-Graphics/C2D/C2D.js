const canvas = document.getElementById("animationCanvas");
const ctx = canvas.getContext("2d");

// Carregar o fundo da cena
function drawBackground() {
    ctx.fillStyle = "cyan"; // Céu
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "green"; // praia
    ctx.fillRect(0, canvas.height - 100, canvas.width, 100);

    ctx.fillStyle = "black"; // Estrada
    ctx.fillRect(0, canvas.height - 150, canvas.width, 50);

    ctx.fillStyle = "white"; // Estrada
    for(let i = 0; i < canvas.width; i += 50) {
    ctx.fillRect(i, canvas.height - 130, 20, 5);
    }

    for(let i = 130; i < canvas.width; i += 250) { //arvore
        ctx.fillStyle = "brown"; // tronco
        ctx.fillRect(i, canvas.height - 270, 20, 120);

        ctx.fillStyle = "green"; // folhas
        ctx.beginPath();
        ctx.arc(i-15, canvas.height - 245, 15, 0, Math.PI * 2);
        ctx.arc(i+10, canvas.height - 245, 15, 0, Math.PI * 2);
        ctx.arc(i+35, canvas.height - 245, 15, 0, Math.PI * 2);
        ctx.fill();

        ctx.beginPath();
        ctx.arc(i-10, canvas.height - 265, 15, 0, Math.PI * 2);
        ctx.arc(i+10, canvas.height - 265, 15, 0, Math.PI * 2);
        ctx.arc(i+30, canvas.height - 265, 15, 0, Math.PI * 2);
        ctx.fill();

        ctx.beginPath();
        ctx.arc(i-5, canvas.height - 280, 15, 0, Math.PI * 2);
        ctx.arc(i+10, canvas.height - 280, 15, 0, Math.PI * 2);
        ctx.arc(i+25, canvas.height - 280, 15, 0, Math.PI * 2);
        ctx.fill();
    }

}

// Desenhar o carro
function drawCar(x, y) {

    ctx.fillStyle = "#1E90FF"; // Corpo do carro
    ctx.fillRect(x, y - 30, 60, 30);
    
    ctx.fillStyle = "#1E90FF"; // Cabeça do carro
    ctx.fillRect(x + 25, y - 60, 35, 30);

    ctx.fillStyle = "white"; // Janela
    ctx.fillRect(x + 40, y - 50, 20, 20);
    
    ctx.fillStyle = "red"; // luz
    ctx.fillRect(x, y - 30, 5, 10);

    // Roda 1
    ctx.fillStyle = "black"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x + 50, y, 10, 0, Math.PI * 2); // Roda 1
    ctx.fill();

    ctx.fillStyle = "white"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x + 50, y, 3, 0, Math.PI * 2); // Roda 1
    ctx.fill();

    // Roda 2
    ctx.fillStyle = "black"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x + 10, y, 10, 0, Math.PI * 2); // Roda 2
    ctx.fill();

    ctx.fillStyle = "white"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x + 10, y, 3, 0, Math.PI * 2); // Roda 1
    ctx.fill();
}

// Desenhar o reboque
function drawReboque(x, y) {
    // Corpo do reboque
    ctx.fillStyle = "lightgrey"; // Cor do reboque (cinza claro)
    ctx.fillRect(x - 100, y - 30, 80, 30); // Desenha o corpo do reboque
    
    ctx.fillStyle = "grey"; // Cor do reboque (cinza claro)
    ctx.fillRect(x - 20, y - 15, 18, 5); // Desenha o corpo do reboque

    // Roda 1
    ctx.fillStyle = "black"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x - 90, y, 10, 0, Math.PI * 2); // Roda 1
    ctx.fill();

    ctx.fillStyle = "white"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x - 90, y, 3, 0, Math.PI * 2); // Roda 1
    ctx.fill();

    // Roda 2
    ctx.fillStyle = "black"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x - 30, y, 10, 0, Math.PI * 2); // Roda 2
    ctx.fill();

    ctx.fillStyle = "white"; // Cor das rodas
    ctx.beginPath();
    ctx.arc(x - 30, y, 3, 0, Math.PI * 2); // Roda 1
    ctx.fill();
}

// Parâmetros de animação
let carX = 0; // Posição inicial do carro
const carY = canvas.height - 160; // Posição vertical do carro
let reboqueX = 30; // Posição inicial do reboque
let reboqueY = canvas.height - 160; // Posição vertical do reboque
const reboqueFollowSpeed = 0.05; // Velocidade de seguimento do reboque

// Função de animação
function animate() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawBackground();

    // Atualizar posição do carro
    carX += 2;
    if (carX > canvas.width) carX = -80;

    // Atualizar posição do reboque para seguir o carro
    reboqueX += (carX + 40 - reboqueX) * reboqueFollowSpeed;
    reboqueY += (carY - reboqueY) * reboqueFollowSpeed;

    // Desenhar o carro e o reboque
    drawCar(carX, carY);
    drawReboque(reboqueX, reboqueY); // Chamada da função que desenha o reboque

    requestAnimationFrame(animate);
}

// Iniciar a animação
animate();
