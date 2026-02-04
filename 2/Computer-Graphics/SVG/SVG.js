// Referências para os elementos SVG
const svg = document.getElementById("game");
const boat = document.getElementById("boat");
const scoreText = document.getElementById("score");

// Configurações do jogo
const gameWidth = 400;
const gameHeight = 600;
const laneWidth = 100;
const max_Vel = 3; // Velocidade máxima dos obstáculos
let score = 0;

// Estado do jogo
let isPlaying = true;
let obstacles = [];

// Função para mexer o barco
document.addEventListener("keydown", (event) => {
  const boatTransform = boat.getAttribute("transform");
  const translateMatch = boatTransform.match(/translate\(([^,]+),\s*([^)]+)\)/);
  const boatX = parseFloat(translateMatch[1]);
  const boatY = parseFloat(translateMatch[2]);

  if (event.key === "a") {
    if (boatX > 10)
      boat.setAttribute("transform", `translate(${boatX - 20}, ${boatY})`);
  } else if (event.key === "d") {
    if (boatX < 200)
      boat.setAttribute("transform", `translate(${boatX + 20}, ${boatY})`);
  }
});

// Gera um novo obstáculo
function createObstacle() {
  const obstacle = document.createElementNS(
    "http://www.w3.org/2000/svg",
    "use",
  );
  obstacle.setAttribute("href", "#obstacles");
  const lane = Math.floor(Math.random() * 3); // Escolhe aleatoriamente uma das 3 pistas
  const scale = 0.5 + Math.random() * 0.4; // Define a escala aleatória
  obstacle.setAttribute(
    "transform",
    `translate(${150 + lane * laneWidth}, -80) scale(${scale})`,
  );
  svg.appendChild(obstacle);
  obstacles.push(obstacle);
}

// Atualiza os obstáculos
function updateObstacles() {
  obstacles.forEach((obstacle, index) => {
    const transform = obstacle.getAttribute("transform");
    const translateMatch = transform.match(/translate\((.*?), (.*?)\)/);
    const scaleMatch = transform.match(/scale\((.*?)\)/);

    const x = parseFloat(translateMatch[1]);
    const y = parseFloat(translateMatch[2]);
    const scale = scaleMatch ? parseFloat(scaleMatch[1]) : 1;

    const newY = y + max_Vel;

    // Remove obstáculos que saíram da tela
    if (newY > gameHeight) {
      svg.removeChild(obstacle);
      obstacles.splice(index, 1);
      score++;
      scoreText.textContent = `Pontuação: ${score}`;
    } else {
      obstacle.setAttribute(
        "transform",
        `translate(${x}, ${newY}) scale(${scale})`,
      );
    }

    // Limites para as colisoes
    const boatBox = boat.getBoundingClientRect();
    const obstacleBox = obstacle.getBoundingClientRect();

    //verifica se existe colisao
    if (
      boatBox.left < obstacleBox.right &&
      boatBox.right > obstacleBox.left &&
      boatBox.top < obstacleBox.bottom &&
      boatBox.bottom > obstacleBox.top
    ) {
      isPlaying = false;
      alert(`Fim de jogo! Sua pontuação foi: ${score}`);
      location.reload(); // Reinicia o jogo
    }
  });
}

// Loop do jogo
function gameLoop() {
  if (isPlaying) {
    updateObstacles();

    // Cria novos obstáculos a cada intervalo de tempo
    if (Math.random() < 0.01) {
      createObstacle();
    }

    requestAnimationFrame(gameLoop);
  }
}

// Inicializa o jogo
gameLoop();
