class BloqueNivel
{
  BloqueFisico bound;
  float[] pos = new float[2];

  float sizeh;
  float sizew;

  boolean isBeg, isEnd, isFree, isDeadly, isReboot, dead;

  BloqueNivel(float posX, float posY, float sizeH, float sizeW)
  {
    pos[0] = posX+0.5;
    pos[1] = posY+0.5;

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

  void isBeginning()
  {
    isBeg = true;
    isFree = true;
    if (bound.hasBody)
    {
      bound.killBody();
    }
  }

  void isEnd()
  {
    isEnd = true;
    bound.isEnd = true;
  }

  void isFree()
  {
    isFree = true;
  }

  void isDead()
  {
    dead = true;
    bound.isDead();
  }

  void isDeadly()
  {
    isDeadly = true;
    bound.isDeadly = true;
  }

  void isReboot()
  {
    isReboot = true;
    bound.isReboot = true;
  }

  void display()
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