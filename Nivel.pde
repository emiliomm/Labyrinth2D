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

  void createLevel()
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
      int nextDirection = int(random(0, 4));

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

  void checkSpeedBall()
  {
    speedBall = false;
    int numberOfTimes = difficulty2;
    do
    {
      switch(int(random(0, 50)))
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


  void cleanOutsideLevel()
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

  void cleanInsideLevel()
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

  void createBorderDeadlyBoxes()
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
                switch(int(random(0, 50)))
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
                  deadlyOrReboot = int(random(0, 2));

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

  void createDeadlyBoxes()
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
                switch(int(random(0, 50)))
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
                  deadlyOrReboot = int(random(0, 2));

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

  void createInitialPositions()
  { 
    direction = int(random(0, 4));

    switch(direction)
    {
    case 0:

      posInicial[0] = 1;
      posInicial[1] = int(random(1, h-1));

      posFinal[0] = w-2;
      posFinal[1] = int(random(1, h-1));

      break;

    case 1:

      posInicial[0] = w-2;
      posInicial[1] = int(random(1, h-1));

      posFinal[0] = 1;
      posFinal[1] = int(random(1, h-1));

      break;

    case 2:

      posInicial[0] = int(random(1, w-1));
      posInicial[1] = 1;

      posFinal[0] = int(random(1, w-1));
      posFinal[1] = h-2;

      break;

    case 3:
      posInicial[0] = int(random(1, w-1));
      posInicial[1] = h-2;

      posFinal[0] = int(random(1, w-1));
      posFinal[1] = 1;

      break;
    }
  }

  void display()
  {
    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        if (grid[i][j] != null)
          grid[i][j].display();
      }
    }
  }

  void nextLevel()
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

  void startAgain()
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