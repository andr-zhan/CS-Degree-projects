const LetterColour = 0x22E0FF;

//RENDERER
var renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// CAMERA
var camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 500);
camera.position.set(0, 0, 250);
camera.lookAt(0, 0, 0);
var angle = 0;
var radius = 200;

//cena
var scene = new THREE.Scene();

//Paredes
var fundo2 = new THREE.PlaneGeometry(10000, 10000, 100, 100);
var fundo_material2 = new THREE.MeshLambertMaterial({
    color: 0x9e1111
});
var fundo_mesh2 = new THREE.Mesh(fundo2, fundo_material2);
fundo_mesh2.position.z = -200;
scene.add(fundo_mesh2);

var fundo3 = new THREE.PlaneGeometry(10000, 10000, 100, 100);
var fundo_material3 = fundo_material2;
var fundo_mesh3 = new THREE.Mesh(fundo3, fundo_material3);
fundo_mesh3.rotation.x = Math.PI;
fundo_mesh3.position.y = 200;
fundo_mesh3.position.z = 200;
scene.add(fundo_mesh3);

//Chão
var fundo = new THREE.PlaneGeometry(10000, 10000, 100, 100);
var fundo_material = new THREE.MeshLambertMaterial({
    color: 0xe38f8f
});
var fundo_mesh = new THREE.Mesh(fundo, fundo_material);
fundo_mesh.rotation.x = -90 * Math.PI / 180;
fundo_mesh.position.y = -30;
scene.add(fundo_mesh);

// Heart shape function
function createHeartShape() {
    var heartShape = new THREE.Shape();

    heartShape.moveTo(0, 0);
    heartShape.bezierCurveTo(1, 0, 1, 1, 0, 2); // Top right curve
    heartShape.bezierCurveTo( - 1, 1, -1, 0, 0, 0); // Top left curve
    return heartShape;
}

// Function to create a heart mesh
function createHeart() {
    var heartShape = createHeartShape();
    var heartGeometry = new THREE.ExtrudeGeometry(heartShape, extrudeSettings);
    var heartMaterial = new THREE.MeshLambertMaterial({
        color: 0xFF69B4
    }); // Pink color
    var heartMesh = new THREE.Mesh(heartGeometry, heartMaterial);

    // Position the heart randomly in the scene
    var xPos = Math.random() * 600 - 300; // Random X position
    var yPos = Math.random() * 400 - 200; // Random Y position
    var zPos = Math.random() * 500 - 250; // Random Z position
    heartMesh.position.set(xPos, yPos, zPos);

    // Animate the heart's floating movement
    var targetPosition = new THREE.Vector3(xPos + Math.random() * 100 - 50, yPos + Math.random() * 100 - 50, zPos + Math.random() * 100 - 50);
    var tweenHeart = new TWEEN.Tween(heartMesh.position).to(targetPosition, 2000 + Math.random() * 3000).yoyo(true).repeat(Infinity);
    tweenHeart.start();

    return heartMesh;
}

// Function to add multiple hearts
function addFloatingHearts() {
    var numHearts = 10; // Number of hearts to create
    for (var i = 0; i < numHearts; i++) {
        var heart = createHeart();
        scene.add(heart);
    }
}

//Luz
var light = new THREE.PointLight(0xFFFF33, 1, 600);
light.position.set( - 50, 0, 100);
scene.add(light);

//Definições de Extrusao
var extrudeSettings = {
    steps: 1,
    depth: 1,
    bevelEnabled: true,
    bevelThickness: 1,
    bevelSize: 0,
    bevelOffset: 0,
    bevelSegments: 1
}

/*


Modelos das letras


*/

//Parte Superior Letra G
function letterGU() {

    var path_GU = new THREE.Shape();

    //Parte superior do g
    path_GU.moveTo(0, 16);
    path_GU.quadraticCurveTo( - 5, 16, -6, 11);
    path_GU.lineTo( - 6, 7);
    path_GU.quadraticCurveTo( - 4, 3, 0, 3);
    path_GU.quadraticCurveTo(4, 3, 6, 8);
    path_GU.lineTo(6, 11);
    path_GU.quadraticCurveTo(5, 16, 0, 16);

    //Buraco superior do g
    const upperHole = new THREE.Path();
    upperHole.moveTo(0, 13);
    upperHole.quadraticCurveTo( - 2, 13, -2.5, 11);
    upperHole.lineTo( - 2.5, 8);
    upperHole.quadraticCurveTo( - 2, 6, 0, 6);
    upperHole.quadraticCurveTo(2, 6, 2.5, 8);
    upperHole.lineTo(2.5, 11);
    upperHole.quadraticCurveTo(2, 13, 0, 13);
    path_GU.holes.push(upperHole);

    var geometry = new THREE.ExtrudeGeometry(path_GU, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_GU = new THREE.Mesh(geometry, material);

    path_GU.position.set( - 300, 17, 0);

    var targetPositionGU = new THREE.Vector3( - 57, 5, 0);
    var tweenGU = new TWEEN.Tween(path_GU.position).to(targetPositionGU, 1000);
    tweenGU.start();

    scene.add(path_GU);
}

//Parte Inferior do G
function letterGL() {

    var path_GL = new THREE.Shape();

    path_GL.moveTo( - 1, 3);
    path_GL.quadraticCurveTo( - 3, 1, -1, -1);
    path_GL.lineTo(5, -1);
    path_GL.quadraticCurveTo(9, -3, 9, -7);
    path_GL.quadraticCurveTo(9, -14, 0, -13);
    path_GL.quadraticCurveTo( - 6, -13, -6, -7);
    path_GL.quadraticCurveTo( - 5, -4, -2, -3);
    path_GL.bezierCurveTo( - 5, -2, -6, 2, -3, 3.8);

    //Buraco inferior do G
    const lowerHole = new THREE.Path();
    lowerHole.moveTo(0, -4);
    lowerHole.bezierCurveTo(8, -3, 8, -11, 0, -10);
    lowerHole.bezierCurveTo( - 4, -10, -4, -5, 0, -4);
    path_GL.holes.push(lowerHole);

    var geometry = new THREE.ExtrudeGeometry(path_GL, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_GL = new THREE.Mesh(geometry, material);

    path_GL.position.set( - 300, 17, 0);

    var targetPositionGL = new THREE.Vector3( - 57, 5, 0);
    var tweenGL = new TWEEN.Tween(path_GL.position).to(targetPositionGL, 1000);
    tweenGL.start();

    scene.add(path_GL);
}

//Parte "gancho" do G
function letterGC() {

    var path_GC = new THREE.Shape();

    path_GC.moveTo(5, 14);
    path_GC.quadraticCurveTo(7, 17, 10, 16);
    path_GC.quadraticCurveTo(11, 15, 10, 13);
    path_GC.bezierCurveTo(8, 15, 6, 14, 6, 12);

    var geometry = new THREE.ExtrudeGeometry(path_GC, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_GC = new THREE.Mesh(geometry, material);

    path_GC.position.set( - 300, 17, 0);

    var targetPositionGC = new THREE.Vector3( - 57, 5, 0);
    var tweenGC = new TWEEN.Tween(path_GC.position).to(targetPositionGC, 1000);
    tweenGC.start();

    scene.add(path_GC);
}

//Letra G
function letterG() {
    letterGU();
    letterGL();
    letterGC();
}

//Letra L
function letterL() {

    var path_L = new THREE.Shape();

    path_L.moveTo(0, 0);
    path_L.lineTo(2, 2);
    path_L.quadraticCurveTo( - 1, 3, -1, 4);
    path_L.lineTo( - 1, 27);
    path_L.lineTo( - 4, 24);
    path_L.quadraticCurveTo( - 3, 23, -3, 20);
    path_L.lineTo( - 3, 4);
    path_L.quadraticCurveTo( - 3, 1, 0, 0);

    var geometry = new THREE.ExtrudeGeometry(path_L, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_L = new THREE.Mesh(geometry, material);

    path_L.position.set( - 300, -3, 0);

    var targetPositionL = new THREE.Vector3( - 40, -3, 0);
    var tweenL = new TWEEN.Tween(path_L.position).to(targetPositionL, 1000);
    tweenL.start();

    scene.add(path_L);
}

//Letra A
function letterA() {

    var path_A = new THREE.Shape();

    path_A.moveTo(0, 0);
    path_A.quadraticCurveTo(0.5, -2, 3, -3);
    path_A.lineTo(5, -1);
    path_A.quadraticCurveTo(3, -1, 3, 0);
    path_A.lineTo(3, 13);
    path_A.lineTo(4, 14);
    path_A.lineTo( - 2, 17);
    path_A.quadraticCurveTo( - 8, 15, -9, 14);
    path_A.lineTo( - 8, 13);
    path_A.lineTo( - 5, 15);
    path_A.lineTo(0, 14);
    path_A.lineTo(0, 0);

    path_A.moveTo(0, 10);
    path_A.quadraticCurveTo( - 17, 5, -6, -3);
    path_A.quadraticCurveTo( - 2, -2.5, 0, 0);
    path_A.lineTo(0, 2);
    path_A.bezierCurveTo( - 10, -4, -10, 8, 0, 8);

    var geometry = new THREE.ExtrudeGeometry(path_A, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_A = new THREE.Mesh(geometry, material);

    path_A.position.set( - 300, 0, 0);

    var targetPositionA = new THREE.Vector3( - 25, 0, 0);
    var tweenA = new TWEEN.Tween(path_A.position).to(targetPositionA, 1000);
    tweenA.start();

    scene.add(path_A);
}

//Letra D
function letterD() {

    var path_D = new THREE.Shape();

    path_D.moveTo(0, 0);
    path_D.quadraticCurveTo(0.5, -2, 3, -3);
    path_D.lineTo(5, -1);
    path_D.quadraticCurveTo(3, -1, 3, 0);
    path_D.lineTo(3, 24);
    path_D.lineTo( - 2, 22);
    path_D.quadraticCurveTo(0, 21.5, 0, 20);

    path_D.lineTo(0, 14);
    path_D.lineTo( - 5, 16);
    path_D.bezierCurveTo( - 14, 10, -14, 1, -3, -3);
    path_D.quadraticCurveTo( - 1, -2, 0, 0);
    path_D.lineTo(0, 2);
    path_D.bezierCurveTo( - 8, -5, -13, 10, -4, 14);
    path_D.lineTo(0, 12);
    path_D.lineTo(0, 2);

    var geometry = new THREE.ExtrudeGeometry(path_D, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_D = new THREE.Mesh(geometry, material);

    path_D.position.set( - 300, 0, 0);

    var targetPositionD = new THREE.Vector3( - 7, 0, 0);
    var tweenD = new TWEEN.Tween(path_D.position).to(targetPositionD, 1000);
    tweenD.start();

    scene.add(path_D);
}

// Letra I
function letterI() {

    var path_I = new THREE.Shape();

    path_I.moveTo(0, 0);
    path_I.lineTo(1, 0);
    path_I.quadraticCurveTo(2, 1, 1, 2);
    path_I.lineTo(1, 12);
    path_I.quadraticCurveTo(0, 13, -1, 12);
    path_I.lineTo( - 1, 2);
    path_I.quadraticCurveTo( - 2, 1, -1, 0);;

    var geometry = new THREE.ExtrudeGeometry(path_I, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });;
    var path_I = new THREE.Mesh(geometry, material);

    path_I.position.set( - 300, -3, 0);

    var targetPositionI = new THREE.Vector3(4, -3, 0);
    var tweenI = new TWEEN.Tween(path_I.position).to(targetPositionI, 1000);
    tweenI.start();

    scene.add(path_I);

    //Ponto do i
    var dotShape = new THREE.Shape();
    dotShape.moveTo(0, 0);
    dotShape.quadraticCurveTo(1, 0.5, 0, 1); // Top-right curve
    dotShape.quadraticCurveTo( - 1, 0.5, -1, 0); // Top-left curve
    dotShape.quadraticCurveTo( - 1, -0.5, 0, -1); // Bottom-left curve
    dotShape.quadraticCurveTo(1, -0.5, 1, 0); // Bottom-right curve
    var dotGeometry = new THREE.ExtrudeGeometry(dotShape, extrudeSettings);
    var dotMaterial = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var dot = new THREE.Mesh(dotGeometry, dotMaterial);

    dot.position.set( - 300, 19, 0);

    var targetPositionDot = new THREE.Vector3(4, 13, 0);
    var tweenDot = new TWEEN.Tween(dot.position).to(targetPositionDot, 1000);
    tweenDot.start();

    scene.add(dot);
}

// Letra A2
function letterA2() {

    var path_A2 = new THREE.Shape();

    path_A2.moveTo(0, 0);
    path_A2.quadraticCurveTo(0.5, -2, 3, -3);
    path_A2.lineTo(5, -1);
    path_A2.quadraticCurveTo(3, -1, 3, 0);
    path_A2.lineTo(3, 13);
    path_A2.lineTo(4, 14);
    path_A2.lineTo( - 2, 17);
    path_A2.quadraticCurveTo( - 8, 15, -9, 14);
    path_A2.lineTo( - 8, 13);
    path_A2.lineTo( - 5, 15);
    path_A2.lineTo(0, 14);
    path_A2.lineTo(0, 0);

    path_A2.moveTo(0, 10);
    path_A2.quadraticCurveTo( - 17, 5, -6, -3);
    path_A2.quadraticCurveTo( - 2, -2.5, 0, 0);
    path_A2.lineTo(0, 2);
    path_A2.bezierCurveTo( - 10, -4, -10, 8, 0, 8);

    var geometry = new THREE.ExtrudeGeometry(path_A2, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_A2 = new THREE.Mesh(geometry, material);

    path_A2.position.set( - 300, 0, 0);

    var targetPositionA2 = new THREE.Vector3(20, 0, 0);
    var tweenA2 = new TWEEN.Tween(path_A2.position).to(targetPositionA2, 1000);
    tweenA2.start();

    scene.add(path_A2);
}

// Letra I2
function letterI2() {

    var path_I2 = new THREE.Shape();

    path_I2.moveTo(0, 0);
    path_I2.lineTo(1, 0);
    path_I2.quadraticCurveTo(2, 1, 1, 2);
    path_I2.lineTo(1, 12);
    path_I2.quadraticCurveTo(0, 13, -1, 12);
    path_I2.lineTo( - 1, 2);
    path_I2.quadraticCurveTo( - 2, 1, -1, 0);

    var geometry = new THREE.ExtrudeGeometry(path_I2, extrudeSettings);
    var material = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var path_I2 = new THREE.Mesh(geometry, material);

    path_I2.position.set( - 300, -3, 0);

    var targetPositionI2 = new THREE.Vector3(33, -3, 0);
    var tweenI2 = new TWEEN.Tween(path_I2.position).to(targetPositionI2, 1000);
    tweenI2.start();

    scene.add(path_I2);

    //Ponto do I
    var dotShape = new THREE.Shape();
    dotShape.moveTo(0, 0);
    dotShape.quadraticCurveTo(1, 0.5, 0, 1); // Top-right curve
    dotShape.quadraticCurveTo( - 1, 0.5, -1, 0); // Top-left curve
    dotShape.quadraticCurveTo( - 1, -0.5, 0, -1); // Bottom-left curve
    dotShape.quadraticCurveTo(1, -0.5, 1, 0); // Bottom-right curve
    var dotGeometry = new THREE.ExtrudeGeometry(dotShape, extrudeSettings);
    var dotMaterial = new THREE.MeshLambertMaterial({
        color: LetterColour
    });
    var dot = new THREE.Mesh(dotGeometry, dotMaterial);

    dot.position.set( - 300, 19, 0);

    var targetPositionDot = new THREE.Vector3(33, 13, 0); // Position above the 'I'
    var tweenDot = new TWEEN.Tween(dot.position).to(targetPositionDot, 1000);
    tweenDot.start();

    scene.add(dot);
}

//ANIMAÇÃO
function animate(time) {
    requestAnimationFrame(animate);

    if (TWEEN.update(time) == true) {
        TWEEN.update(time);
    }

    else {
        camera.position.x = radius * Math.cos(angle);
        camera.position.z = radius * Math.sin(angle);
        angle += 0.01;
        camera.lookAt(0, 0, 0);
    }

    renderer.render(scene, camera);
}

//Main
function main() {
    //Construção da Palavra: Canelado
    letterG();
    letterL();
    letterA();
    letterD();
    letterI();
    letterA2();
    letterI2();

    animate();
}