import ketai.sensors.*;

KetaiSensor sensor;
float gyroscopeX, gyroscopeY, gyroscopeZ;

import shiffman.box2d.*;

Box2DProcessing box2d;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

Nivel l;
Bola ball;

boolean nextLevel, beginAgain, rebootLevel, infoScreen, gyroOptionScreen;

float scaleW, scaleH;

int gyroscopeSensitiveLevel;

void setup()
{
  InitBoxWorld();

  nextLevel = false;
  beginAgain = false;
  infoScreen = false;
  rebootLevel = false;
  gyroOptionScreen = true;

  gyroscopeSensitiveLevel = 1;

  fullScreen();
  sensor = new KetaiSensor(this);
  sensor.start();
  orientation(LANDSCAPE);

  if (width > 1920)
    scaleW = width/1920.0f;
  else
    scaleW = 1920.0f/width;

  if (height > 1080)
    scaleH = height/1080.0f;
  else
    scaleH = 1080.0f/height;

  l = new Nivel(5, 5, 128, 128);
  l.createLevel();
  infoScreen = true;

  ball = new Bola(28.8, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, false);
}

void InitBoxWorld()
{
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.listenForCollisions();
  box2d.setGravity(0, 0);
}

void draw()
{
  textSize(70);

  if (gyroOptionScreen)
  {
    background(255, 255, 255);
    textAlign(LEFT);

    if (gyroscopeSensitiveLevel == 0)
      fill(255, 0, 0);
    else
      fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-height/1.1, 400, 130);
    fill(255);
    text("Gyro bajo", 100, height-height/1.1+100);

    if (gyroscopeSensitiveLevel == 1)
      fill(255, 0, 0);
    else
      fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-height/1.5, 400, 130);
    fill(255);
    text("Gyro medio", 100, height-height/1.5+100);

    if (gyroscopeSensitiveLevel == 2)
      fill(255, 0, 0);
    else
      fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-height/2.5, 400, 130);
    fill(255);
    text("Gyro alto", 100, height-height/2.5+100);

    fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-200, 330, 130);
    fill(255);
    text("Continuar", 100, height-100);
  } else if (infoScreen)
  {
    background(255, 105, 180);
    textAlign(CENTER, CENTER);
    rectMode(CORNER);
    fill(255, 255, 255);
    if (l.firstTime == 1)
      text("Coloca el movil paralelo al suelo. \n Mueve la bola orientando el movil. \n LLega hasta el bloque verde", 0, 0, width, height);
    else if (l.firstReboot == 1)
      text("La bola no debe tocar los bloques amarillos \n o se reiniciara el nivel. \n Aunque no perderas ninguna vida", 0, 0, width, height);
    else if (l.firstDeadly == 1)
      text("La bola no debe tocar los bloques rojos o \n perderas una vida. \n Con 0 vidas, pierdes la partida", 0, 0, width, height);
    else if (l.firstSpeedBall == 1)
      text("Cuando la bola se vuelva azul, ira mas rapida \n ¡ten cuidado!", 0, 0, width, height);
    else if (rebootLevel)
    {
      background(255, 255, 255);
      fill(0, 0, 0);
      text("Toca para volver a intentarlo", 0, 0, width, height);
    } else
    {
      background(255, 255, 255);
      fill(0, 0, 0);
      text("Toca para pasar al siguiente nivel", 0, 0, width, height);
    }
  } else
  {
    background(255, 255, 255);

    if (!nextLevel && !beginAgain && !rebootLevel)
    {
      Vec2 pos = box2d.getBodyPixelCoord(ball.body);

      pushMatrix();
      translate(-pos.x +width/2, -pos.y +height/2);

      box2d.step();
      l.display();
      ball.update();
      scale(scaleW, scaleH);
      popMatrix();
    } else if (nextLevel)
    {
      nextLevel = false;
      beginAgain = false;
      rebootLevel = false;

      l.nextLevel();
      ball = new Bola(28.8, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);
      infoScreen = true;
    } else if (beginAgain)
    {
      nextLevel = false;
      beginAgain = false;
      rebootLevel = false;
      infoScreen = true;

      l.startAgain();
      ball = new Bola(28.8, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);

      if (l.firstDeadly==1 || l.firstSpeedBall==1)
      {
        infoScreen = true;
      }
    } else if (rebootLevel)
    {
      nextLevel = false;
      beginAgain = false;
      rebootLevel = false;

      if (l.firstSpeedBall == 1)
        l.firstSpeedBall = 2;

      ball.isDead();
      ball = null;
      ball = new Bola(28.8, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);
    }

    textAlign(LEFT);
    fill(0);
    text(l.nivel, 100, 100);
    text("Vidas: " + l.lifes, width-300, 100);

    fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-200, 200, 130);
    fill(255);
    text("Gyro", 100, height-100);
  }
}

void onOrientationEvent(float x, float y, float z)
{
  gyroscopeX = x;
  gyroscopeY = y; //+izq -der
  gyroscopeZ = z; //-arriba +abajo
}


void beginContact(Contact cp) {

  if (!nextLevel && !beginAgain && !infoScreen && !rebootLevel && !gyroOptionScreen)
  {
    Fixture f1 = cp.getFixtureA();
    Fixture f2 = cp.getFixtureB();

    Body b1 = f1.getBody();
    Body b2 = f2.getBody();

    Object o1 = b1.getUserData();
    if (o1.getClass() == Bola.class) {
      Object o2 = b2.getUserData();
      if (o2.getClass() == BloqueFisico.class) {
        BloqueFisico b = (BloqueFisico) o2;
        if (b.isEnd)
        {
          nextLevel = true;
        } else if (b.isDeadly)
        {
          l.lifes--;
          if (l.lifes == 0)
            beginAgain = true;
          else
          {
            rebootLevel = true;
            infoScreen = true;
            l.firstDeadly++;
          }
        } else if (b.isReboot)
        {
          rebootLevel = true;
          infoScreen = true;
          l.firstReboot++;
        }
      }
    } else if (o1.getClass() == BloqueFisico.class) {
      BloqueFisico boundary = (BloqueFisico) o1;
      if (boundary.isEnd)
      {
        Object o2 = b2.getUserData();
        if (o2.getClass() == Bola.class) {
          nextLevel = true;
        }
      } else if (boundary.isDeadly)
      {
        Object o2 = b2.getUserData();
        if (o2.getClass() == Bola.class) {
          l.lifes--;
          if (l.lifes == 0)
            beginAgain = true;
          else
          {
            rebootLevel = true;
            infoScreen = true;
            l.firstDeadly++;
          }
        }
      } else if (boundary.isReboot)
      {
        Object o2 = b2.getUserData();
        if (o2.getClass() == Bola.class) {
          rebootLevel = true;
          infoScreen = true;
          l.firstReboot++;
        }
      }
    }
  }
}

void endContact(Contact cp) {
}

void mouseReleased() {
  if (gyroOptionScreen)
  {
    if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/1.1 && mouseY < height-height/1.1+130)
    {
      gyroscopeSensitiveLevel = 0;
    } else if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/1.5 && mouseY < height-height/1.5+130)
    {
      gyroscopeSensitiveLevel = 1;
    } else if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/2.5 && mouseY < height-height/2.5+130)
    {
      gyroscopeSensitiveLevel = 2;
    } else if (mouseX > 100 && mouseX < 100 + 330 && mouseY > height-200 && mouseY < height-200+130)
    {
      gyroOptionScreen = false;
    }
  } else if (infoScreen)
    infoScreen = false;
  else if (mouseX > 100 && mouseX < 100 + 170 && mouseY > height-200 && mouseY < height-200+130)
  {
    gyroOptionScreen = true;
  }
}