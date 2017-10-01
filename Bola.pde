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
    bd.linearDamping = 0.8;
    bd.angularDamping = 0.0;
    bd.bullet = true;

    body = box2d.createBody(bd);

    CircleShape ps = new CircleShape();

    ps.m_radius = box2d.scalarPixelsToWorld(radio);

    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 0.3;
    fd.friction = 0.4;
    fd.restitution = 0.6;

    body.createFixture(fd);

    body.setUserData(this);
  }

  void update()
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

  void applyForce(Vec2 force) {
    Vec2 pos = body.getWorldCenter();
    body.applyForce(force, pos);
  }

  void isDead()
  {
    dead = true;
    killBody();
  }

  void display()
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

  void killBody() {
    box2d.destroyBody(body);
  }
}