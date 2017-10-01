package processing.test.labyrinth2d;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ketai.sensors.*; 
import shiffman.box2d.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Labyrinth2D extends PApplet {



KetaiSensor sensor;
float gyroscopeX, gyroscopeY, gyroscopeZ;



Box2DProcessing box2d;





Nivel l;
Bola ball;

boolean nextLevel, beginAgain, rebootLevel, infoScreen, gyroOptionScreen;

float scaleW, scaleH;

int gyroscopeSensitiveLevel;

public void setup()
{
  InitBoxWorld();

  nextLevel = false;
  beginAgain = false;
  infoScreen = false;
  rebootLevel = false;
  gyroOptionScreen = true;

  gyroscopeSensitiveLevel = 1;

  
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

  ball = new Bola(28.8f, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, false);
}

public void InitBoxWorld()
{
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.listenForCollisions();
  box2d.setGravity(0, 0);
}

public void draw()
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
    rect(100, height-height/1.1f, 400, 130);
    fill(255);
    text("Gyro bajo", 100, height-height/1.1f+100);

    if (gyroscopeSensitiveLevel == 1)
      fill(255, 0, 0);
    else
      fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-height/1.5f, 400, 130);
    fill(255);
    text("Gyro medio", 100, height-height/1.5f+100);

    if (gyroscopeSensitiveLevel == 2)
      fill(255, 0, 0);
    else
      fill(0);
    stroke(0);
    rectMode(CORNER);
    rect(100, height-height/2.5f, 400, 130);
    fill(255);
    text("Gyro alto", 100, height-height/2.5f+100);

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
      text("Cuando la bola se vuelva azul, ira mas rapida \n \u00a1ten cuidado!", 0, 0, width, height);
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
      ball = new Bola(28.8f, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);
      infoScreen = true;
    } else if (beginAgain)
    {
      nextLevel = false;
      beginAgain = false;
      rebootLevel = false;
      infoScreen = true;

      l.startAgain();
      ball = new Bola(28.8f, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);

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
      ball = new Bola(28.8f, l.grid[l.posInicial[0]][l.posInicial[1]].pos[0] * l.grid[l.posInicial[0]][l.posInicial[1]].sizew, l.grid[l.posInicial[0]][l.posInicial[1]].pos[1] * l.grid[l.posInicial[0]][l.posInicial[1]].sizeh, l.speedBall);
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

public void onOrientationEvent(float x, float y, float z)
{
  gyroscopeX = x;
  gyroscopeY = y; //+izq -der
  gyroscopeZ = z; //-arriba +abajo
}


public void beginContact(Contact cp) {

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

public void endContact(Contact cp) {
}

public void mouseReleased() {
  if (gyroOptionScreen)
  {
    if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/1.1f && mouseY < height-height/1.1f+130)
    {
      gyroscopeSensitiveLevel = 0;
    } else if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/1.5f && mouseY < height-height/1.5f+130)
    {
      gyroscopeSensitiveLevel = 1;
    } else if (mouseX > 100 && mouseX < 100 + 400 && mouseY > height-height/2.5f && mouseY < height-height/2.5f+130)
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
class BloqueFisico {
  float x, y;
  float w, h;
  Body body;

  boolean hasBody, isEnd, isDeadly, isReboot, dead;

  BloqueFisico(float x_, float y_, float w_, float h_) {
    x = x_;
    y = y_;
    w = w_;
    h = h_;

    BodyDef bd = new BodyDef();
    bd.position.set(box2d.coordPixelsToWorld(x, y));
    bd.type = BodyType.STATIC;
    body = box2d.createBody(bd);
    hasBody = true;
    isEnd = false;
    isDeadly = false;
    isReboot = false;
    dead = false;

    float box2dW = box2d.scalarPixelsToWorld(w/2);
    float box2dH = box2d.scalarPixelsToWorld(h/2);
    PolygonShape ps = new PolygonShape();

    ps.setAsBox(box2dW, box2dH);

    body.createFixture(ps, 1);

    body.setUserData(this);
  }

  public void killBody() {
    box2d.destroyBody(body);
    hasBody = false;
  }

  public void isDead()
  {
    dead = true;
    killBody();
  }

  public void display() {
    if (!dead)
    {
      if (isEnd)
      {
        fill(0, 255, 0);
        stroke(0, 255, 0);
      } else if (isDeadly)
      {
        fill(255, 0, 0);
        stroke(255, 0, 0);
      } else if (isReboot)
      {
        fill(255, 255, 0);
        stroke(255, 255, 0);
      } else
      {
        fill(0);
        stroke(0);
      }
      rectMode(CENTER);
      rect(x, y, w, h);
    }
  }
}
class BloqueNivel
{
  BloqueFisico bound;
  float[] pos = new float[2];

  float sizeh;
  float sizew;

  boolean isBeg, isEnd, isFree, isDeadly, isReboot, dead;

  BloqueNivel(float posX, float posY, float sizeH, float sizeW)
  {
    pos[0] = posX+0.5f;
    pos[1] = posY+0.5f;

    sizeh = sizeH;
    sizew = sizeW;

    bound = new BloqueFisico(pos[0]*sizew, pos[1]*sizeh, sizew, sizeh);
    isFree = false;
    isBeg = false;
    isEnd = false;
    isReboot = false;
    isDeadly = false;
    dead = false;
  }

  public void isBeginning()
  {
    isBeg = true;
    isFree = true;
    if (bound.hasBody)
    {
      bound.killBody();
    }
  }

  public void isEnd()
  {
    isEnd = true;
    bound.isEnd = true;
  }

  public void isFree()
  {
    isFree = true;
  }

  public void isDead()
  {
    dead = true;
    bound.isDead();
  }

  public void isDeadly()
  {
    isDeadly = true;
    bound.isDeadly = true;
  }

  public void isReboot()
  {
    isReboot = true;
    bound.isReboot = true;
  }

  public void display()
  {
    if (!dead)
    {
      if (bound.hasBody)
      {
        bound.display();
      } else
      {
        if (isBeg)
        {
          fill(0, 255, 0);
          stroke(0, 255, 0);
        } else if (isFree)
        {
          fill(0, 0, 255);
          stroke(0, 0, 255);
        }

        rectMode(CENTER);
        rect(pos[0]*sizew, pos[1]*sizeh, sizew, sizeh);
      }
    }
  }
}
class Bola
{
  Body body;
  float radio;

  boolean dead, speed;

  Bola(float radio, float posx, float posy, boolean speed) {
    this.radio = radio;
    dead = false;
    this.speed = speed;

    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(posx, posy));
    //bd.fixedRotation = true;
    bd.linearDamping = 0.8f;
    bd.angularDamping = 0.0f;
    bd.bullet = true;

    body = box2d.createBody(bd);

    CircleShape ps = new CircleShape();

    ps.m_radius = box2d.scalarPixelsToWorld(radio);

    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 0.3f;
    fd.friction = 0.4f;
    fd.restitution = 0.6f;

    body.createFixture(fd);

    body.setUserData(this);
  }

  public void update()
  {
    Vec2 direction = new Vec2(0, 0);

    int giroSensitiveMargin1, giroSensitiveMargin2, giroSensitiveMargin3;

    switch(gyroscopeSensitiveLevel)
    {
    default:
    case 0:

      giroSensitiveMargin1 = 3;
      giroSensitiveMargin2 = 6;
      giroSensitiveMargin3 = 9;

      break;
    case 1:

      giroSensitiveMargin1 = 5;
      giroSensitiveMargin2 = 10;
      giroSensitiveMargin3 = 15;
      break;
    case 2:
      giroSensitiveMargin1 = 7;
      giroSensitiveMargin2 = 14;
      giroSensitiveMargin3 = 21;

      break;
    }

    if (gyroscopeY > giroSensitiveMargin1)
    {
      direction.addLocal(new Vec2(-1, 0));
      if (gyroscopeY > giroSensitiveMargin2)
      {
        direction.addLocal(new Vec2(-1, 0));
        if (gyroscopeY > giroSensitiveMargin3)
        {
          direction.addLocal(new Vec2(-1, 0));
        }
      }
    }

    if (gyroscopeY < -giroSensitiveMargin1)
    {
      direction.addLocal(new Vec2(1, 0));
      if (gyroscopeY < -giroSensitiveMargin2)
      {
        direction.addLocal(new Vec2(1, 0));
        if (gyroscopeY < -giroSensitiveMargin3)
        {
          direction.addLocal(new Vec2(1, 0));
        }
      }
    }

    if (gyroscopeZ < -giroSensitiveMargin1)
    {
      direction.addLocal(new Vec2(0, 1));
      if (gyroscopeZ < -giroSensitiveMargin2)
      {
        direction.addLocal(new Vec2(0, 1));
        if (gyroscopeZ < -giroSensitiveMargin3)
        {
          direction.addLocal(new Vec2(0, 1));
        }
      }
    }

    if (gyroscopeZ > giroSensitiveMargin1)
    {
      direction.addLocal(new Vec2(0, -1));
      if (gyroscopeZ > giroSensitiveMargin2)
      {
        direction.addLocal(new Vec2(0, -1));
        if (gyroscopeZ > giroSensitiveMargin3)
        {
          direction.addLocal(new Vec2(0, -1));
        }
      }
    }

    if (speed)
      direction.mulLocal(400);
    else
      direction.mulLocal(200);

    applyForce(direction);
    display();
  }

  public void applyForce(Vec2 force) {
    Vec2 pos = body.getWorldCenter();
    body.applyForce(force, pos);
  }

  public void isDead()
  {
    dead = true;
    killBody();
  }

  public void display()
  {
    if (!dead)
    {
      Vec2 pos = box2d.getBodyPixelCoord(body);
      float a = body.getAngle();

      pushMatrix();
      translate(pos.x, pos.y);
      rotate(-a);
      if (speed)
        fill(0, 0, 255);
      else
        fill(0);
      stroke(0);
      //strokeWeight(1);
      //ellipse(0, 0, radio*2, radio*2);

      rectMode(RADIUS);
      rect(0, 0, radio, radio);

      // Let's add a line so we can see the rotation
      // line(0, 0, radio, 0);
      popMatrix();
    }
  }

  public void killBody() {
    box2d.destroyBody(body);
  }
}
class Nivel
{
  int h, w, initialH, initialW, direction, difficulty, difficulty2, nivel;
  BloqueNivel[][] grid;
  float[] sizeGrid = new float[2];

  int[] posInicial = new int[2];
  int[] posFinal = new int[2];

  boolean speedBall, deadlyBox, rebootBox;
  int firstTime, firstDeadly, firstSpeedBall, firstReboot, lifes;

  Nivel(int h, int w, float sizeH, float sizeW)
  {
    if (h < 5)
      h = 5;
    if (w < 5)
      w = 5;

    this.h = h;
    this.w = w;

    initialH = this.h;
    initialW = this.w;

    sizeGrid[0] = sizeH;
    sizeGrid[1] = sizeW;

    difficulty = 1;
    difficulty2 = 2;

    direction = 0;
    nivel = 0;

    speedBall = false;
    deadlyBox = false;
    rebootBox = false;

    firstTime = 0; 
    firstReboot = 0;
    firstDeadly = 0;
    firstSpeedBall = 0;
    lifes = 2;
  }

  public void createLevel()
  {
    grid = new BloqueNivel[w][h];
    
    if (firstTime < 2)
      firstTime++;
      
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        grid[i][j] = new BloqueNivel(i, j, sizeGrid[0], sizeGrid[1]);
      }
    }

    createInitialPositions();

    int[] pos = { posInicial[0], posInicial[1] };
    grid[pos[0]][pos[1]].isBeginning();
    grid[posFinal[0]][posFinal[1]].isEnd();

    final int[] UPDIRECTION = { 1, 0 };
    final int[] DOWNDIRECTION = { -1, 0 };
    final int[] LEFTDIRECTION = { 0, 1 };
    final int[] RIGHTDIRECTION = { 0, -1 };
    final int[][] DIRECTIONS = { LEFTDIRECTION, RIGHTDIRECTION, UPDIRECTION, DOWNDIRECTION };

    boolean clockwise;

    while (pos[0] != posFinal[0] || pos[1] != posFinal[1])
    {
      int nextDirection = PApplet.parseInt(random(0, 4));

      float r = random(-50, 50);
      if (r > 0)
        clockwise = true;
      else
        clockwise = false;

      boolean canContinue = false;
      int numberOfTries = 0;

      do
      {
        if ((pos[0] + DIRECTIONS[nextDirection][0] < w-1 && pos[1] + DIRECTIONS[nextDirection][1] < h-1) && (pos[0] + DIRECTIONS[nextDirection][0] > 0 && pos[1] + DIRECTIONS[nextDirection][1] > 0))
        {
          if (grid[pos[0] + DIRECTIONS[nextDirection][0]][pos[1] + DIRECTIONS[nextDirection][1]].isFree == false)
          {
            pos[0] = pos[0] + DIRECTIONS[nextDirection][0];
            pos[1] = pos[1] + DIRECTIONS[nextDirection][1];
            grid[pos[0]][pos[1]].isFree();
            numberOfTries = 0;
            canContinue = true;
          } else
          {
            if (clockwise)
              nextDirection++;
            else
              nextDirection--;

            if (nextDirection > 3)
              nextDirection = 0;
            else if (nextDirection < 0)
              nextDirection = 3;

            numberOfTries++;
          }
        } else
        {
          if (clockwise)
            nextDirection++;
          else
            nextDirection--;

          if (nextDirection > 3)
            nextDirection = 0;
          else if (nextDirection < 0)
            nextDirection = 3;

          numberOfTries++;
        }

        if (numberOfTries == 4)
        { 
          boolean found = false;

          switch(direction)
          {
          case 0:

            for (int i = w-2; i >= 1; i--) {
              if (posFinal[1] > h/2)
              {
                for (int j = 1; j < h-1; j++) {
                  if (grid[i][j].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[i-1][j].isFree)
                        permitido = true;
                    }

                    if ( i+1 < w-1)
                    {
                      if (!grid[i+1][j].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[i][j-1].isFree)
                        permitido = true;
                    }

                    if ( j+1 < h-1)
                    {
                      if (!grid[i][j+1].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = i;
                      pos[1] = j;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              } else
              {
                for (int j = h-2; j >= 1; j--) {
                  if (grid[i][j].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[i-1][j].isFree)
                        permitido = true;
                    }

                    if ( i+1 < w-1)
                    {
                      if (!grid[i+1][j].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[i][j-1].isFree)
                        permitido = true;
                    }

                    if ( j+1 < h-1)
                    {
                      if (!grid[i][j+1].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = i;
                      pos[1] = j;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              }
            }

            break;

          case 1:

            for (int i = 1; i < w-1; i++) {
              if (posFinal[1] > h/2)
              {
                for (int j = 1; j < h-1; j++) {
                  if (grid[i][j].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[i-1][j].isFree)
                        permitido = true;
                    }

                    if ( i+1 < w-1)
                    {
                      if (!grid[i+1][j].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[i][j-1].isFree)
                        permitido = true;
                    }

                    if ( j+1 < h-1)
                    {
                      if (!grid[i][j+1].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = i;
                      pos[1] = j;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              } else
              {
                for (int j = h-2; j >= 1; j--) {
                  if (grid[i][j].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[i-1][j].isFree)
                        permitido = true;
                    }

                    if ( i+1 < w-1)
                    {
                      if (!grid[i+1][j].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[i][j-1].isFree)
                        permitido = true;
                    }

                    if ( j+1 < h-1)
                    {
                      if (!grid[i][j+1].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = i;
                      pos[1] = j;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              }
            }

            break;

          case 2:
            for (int i = h-2; i >= 1; i--) {
              if (posFinal[0] > w/2)
              {
                for (int j = w-2; j >= 1; j--) {
                  if (grid[j][i].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[j][i-1].isFree)
                        permitido = true;
                    }

                    if ( i+1 < h-1)
                    {
                      if (!grid[j][i+1].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[j-1][i].isFree)
                        permitido = true;
                    }

                    if ( j+1 < w-1)
                    {
                      if (!grid[j+1][i].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = j;
                      pos[1] = i;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              } else
              {
                for (int j = 1; j < w-1; j++) {
                  if (grid[j][i].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[j][i-1].isFree)
                        permitido = true;
                    }

                    if ( i+1 < h-1)
                    {
                      if (!grid[j][i+1].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[j-1][i].isFree)
                        permitido = true;
                    }

                    if ( j+1 < w-1)
                    {
                      if (!grid[j+1][i].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = j;
                      pos[1] = i;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              }
            }

            break;

          case 3:

            for (int i = 1; i < h-1; i++) {
              if (posFinal[0] > w/2)
              {
                for (int j = w-2; j >= 1; j--) {
                  if (grid[j][i].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[j][i-1].isFree)
                        permitido = true;
                    }

                    if ( i+1 < h-1)
                    {
                      if (!grid[j][i+1].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[j-1][i].isFree)
                        permitido = true;
                    }

                    if ( j+1 < w-1)
                    {
                      if (!grid[j+1][i].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = j;
                      pos[1] = i;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              } else
              {
                for (int j = 1; j < w-1; j++) {
                  if (grid[j][i].isFree && !found)
                  {
                    boolean permitido = false;

                    if ( i-1 > 0)
                    {
                      if (!grid[j][i-1].isFree)
                        permitido = true;
                    }

                    if ( i+1 < h-1)
                    {
                      if (!grid[j][i+1].isFree)
                        permitido = true;
                    }

                    if ( j-1 > 0)
                    {
                      if (!grid[j-1][i].isFree)
                        permitido = true;
                    }

                    if ( j+1 < w-1)
                    {
                      if (!grid[j+1][i].isFree)
                        permitido = true;
                    }

                    if (permitido)
                    {
                      pos[0] = j;
                      pos[1] = i;
                      found = true;
                      canContinue = true;
                    }
                  }
                }
              }
            }

            break;
          }
        }
      }
      while (!canContinue);
    }

    if (firstReboot == 1)
      firstReboot++;

    if (firstDeadly == 1)
      firstDeadly++;

    if (firstSpeedBall == 1)
      firstSpeedBall++;

    cleanOutsideLevel();

    if (difficulty2 > 5)
      createDeadlyBoxes();

    cleanInsideLevel();

    if (difficulty2 > 4)
      createBorderDeadlyBoxes();

    if (difficulty2 > 8)
      checkSpeedBall();
  }

  public void checkSpeedBall()
  {
    speedBall = false;
    int numberOfTimes = difficulty2;
    do
    {
      switch(PApplet.parseInt(random(0, 50)))
      {
      case 0:
        speedBall = true;
        break;
      }
      numberOfTimes--;
    }
    while (numberOfTimes > 0);

    if (speedBall)
    {
      if (firstSpeedBall < 1)
        firstSpeedBall++;
    }
  }


  public void cleanOutsideLevel()
  {
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {

        boolean borrarBox = true;

        if ( i-1 > 0)
        {
          if (grid[i-1][j] != null)
            if (grid[i-1][j].isFree)
              borrarBox = false;
        }

        if ( i+1 < w)
        {
          if (grid[i+1][j] != null)
            if (grid[i+1][j].isFree)
              borrarBox = false;
        }

        if ( j-1 > 0)
        {
          if (grid[i][j-1] != null)
            if (grid[i][j-1].isFree)
              borrarBox = false;
        }

        if ( j+1 < h)
        {
          if (grid[i][j+1] != null)
            if (grid[i][j+1].isFree)
              borrarBox = false;
        }

        if (borrarBox)
        {
          grid[i][j].isDead();
          grid[i][j] = null;
        }
      }
    }
  }

  public void cleanInsideLevel()
  {
    for (int i = 1; i < w-1; i++) {
      for (int j = 1; j < h-1; j++) {

        if (grid[i][j] != null)
        {
          if (!grid[i][j].isBeg && !grid[i][j].isEnd && grid[i][j].isFree && !grid[i][j].isDeadly)
          {
            grid[i][j].isDead();
            grid[i][j] = null;
          }
        }
      }
    }
  }

  public void createBorderDeadlyBoxes()
  {
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {

        if (grid[i][j] != null)
        {
          if (!grid[i][j].isBeg && !grid[i][j].isEnd)
          {
            boolean turnDeadly = true;

            if ( i-1 > 0 && grid[i-1][j] != null)
            {
              if (grid[i-1][j].isBeg || grid[i-1][j].isEnd)
                turnDeadly = false;
            }

            if ( i+1 < w && grid[i+1][j] != null)
            {
              if (grid[i+1][j].isBeg || grid[i+1][j].isEnd)
                turnDeadly = false;
            }

            if ( j-1 > 0 && grid[i][j-1] != null)
            {
              if (grid[i][j-1].isBeg || grid[i][j-1].isEnd)
                turnDeadly = false;
            }

            if ( j+1 < h && grid[i][j+1] != null)
            {
              if (grid[i][j+1].isBeg || grid[i][j+1].isEnd)
                turnDeadly = false;
            }

            if (turnDeadly)
            {
              turnDeadly = false;
              int numberOfTimes = difficulty2;
              do
              {
                switch(PApplet.parseInt(random(0, 50)))
                {
                case 0:
                  turnDeadly = true;
                  break;
                }
                numberOfTimes--;
              }
              while (numberOfTimes > 0);

              if (turnDeadly)
              {
                int deadlyOrReboot = 0;

                if (difficulty2 > 6)
                  deadlyOrReboot = PApplet.parseInt(random(0, 2));

                if (deadlyOrReboot == 0)
                {
                  grid[i][j].isReboot();
                  if (!rebootBox)
                  {
                    rebootBox = true;
                    if (firstReboot < 1)
                      firstReboot++;
                  }
                } else
                {

                  grid[i][j].isDeadly();

                  if (!deadlyBox)
                  {
                    deadlyBox = true;
                    if (firstDeadly < 1)
                      firstDeadly++;
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public void createDeadlyBoxes()
  {
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {

        if (grid[i][j] != null)
        {
          if (!grid[i][j].isBeg && !grid[i][j].isEnd && grid[i][j].isFree)
          {
            boolean turnDeadly = true;

            if ( i-1 > 0 && grid[i-1][j] != null)
            {
              if (grid[i-1][j].isBeg || grid[i-1][j].isEnd || !grid[i-1][j].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;

            if ( i+1 < w && grid[i+1][j] != null)
            {
              if (grid[i+1][j].isBeg || grid[i+1][j].isEnd || !grid[i+1][j].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;


            if ( j-1 > 0 && grid[i][j-1] != null)
            {
              if (grid[i][j-1].isBeg || grid[i][j-1].isEnd || !grid[i][j-1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;


            if ( j+1 < h && grid[i][j+1] != null)
            {
              if (grid[i][j+1].isBeg || grid[i][j+1].isEnd || !grid[i][j+1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;



            if ( i-1 > 0 && j-1 > 0 && grid[i-1][j-1] != null)
            {
              if (grid[i-1][j-1].isBeg || grid[i-1][j-1].isEnd || !grid[i-1][j-1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;


            if ( i+1 < w && j-1 > 0 && grid[i+1][j-1] != null)
            {
              if (grid[i+1][j-1].isBeg || grid[i+1][j-1].isEnd || !grid[i+1][j-1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;


            if ( i+1 < w && j+1 < h && grid[i+1][j+1] != null)
            {
              if (grid[i+1][j+1].isBeg || grid[i+1][j+1].isEnd || !grid[i+1][j+1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;


            if ( i-1 > 0 && j+1 < h && grid[i-1][j+1] != null)
            {
              if (grid[i-1][j+1].isBeg || grid[i-1][j+1].isEnd || !grid[i-1][j+1].isFree)
                turnDeadly = false;
            } else
              turnDeadly = false;

            if (turnDeadly)
            {
              turnDeadly = false;
              int numberOfTimes = difficulty2;
              do
              {
                switch(PApplet.parseInt(random(0, 50)))
                {
                case 0:
                  turnDeadly = true;
                  break;
                }
                numberOfTimes--;
              }
              while (numberOfTimes > 0);

              if (turnDeadly)
              {
                int deadlyOrReboot = 0;

                if (difficulty2 > 7)
                  deadlyOrReboot = PApplet.parseInt(random(0, 2));

                if (deadlyOrReboot == 0)
                {
                  grid[i][j].isReboot();
                  if (!rebootBox)
                  {
                    rebootBox = true;
                    if (firstReboot < 1)
                      firstReboot++;
                  }
                } else
                {

                  grid[i][j].isDeadly();

                  if (!deadlyBox)
                  {
                    deadlyBox = true;
                    if (firstDeadly < 1)
                      firstDeadly++;
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public void createInitialPositions()
  { 
    direction = PApplet.parseInt(random(0, 4));

    switch(direction)
    {
    case 0:

      posInicial[0] = 1;
      posInicial[1] = PApplet.parseInt(random(1, h-1));

      posFinal[0] = w-2;
      posFinal[1] = PApplet.parseInt(random(1, h-1));

      break;

    case 1:

      posInicial[0] = w-2;
      posInicial[1] = PApplet.parseInt(random(1, h-1));

      posFinal[0] = 1;
      posFinal[1] = PApplet.parseInt(random(1, h-1));

      break;

    case 2:

      posInicial[0] = PApplet.parseInt(random(1, w-1));
      posInicial[1] = 1;

      posFinal[0] = PApplet.parseInt(random(1, w-1));
      posFinal[1] = h-2;

      break;

    case 3:
      posInicial[0] = PApplet.parseInt(random(1, w-1));
      posInicial[1] = h-2;

      posFinal[0] = PApplet.parseInt(random(1, w-1));
      posFinal[1] = 1;

      break;
    }
  }

  public void display()
  {
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        if (grid[i][j] != null)
          grid[i][j].display();
      }
    }
  }

  public void nextLevel()
  { 
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        if (grid[i][j] != null)
        {
          grid[i][j].isDead();
          grid[i][j] = null;
        }
      }
    }

    ball.isDead();
    ball = null;

    difficulty++;
    nivel++;

    if (difficulty%difficulty2==0)
    {
      h += 1;
      w += 1;
      difficulty2++;
      difficulty = 0;
    }

    speedBall = false;
    deadlyBox = false;
    rebootBox = false;

    createLevel();
  }

  public void startAgain()
  { 
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        if (grid[i][j] != null)
          grid[i][j].isDead();
      }
    }

    ball.isDead();
    ball = null;

    h = initialH;
    w = initialW;

    difficulty = 1;
    difficulty2 = 2;
    nivel = 0;
    speedBall = false;
    deadlyBox = false;
    rebootBox = false;

    firstTime = 0;
    firstReboot = 0;
    firstDeadly = 0;
    firstSpeedBall = 0;

    lifes = 2;

    createLevel();
  }
}
  public void settings() {  fullScreen(); }
}
