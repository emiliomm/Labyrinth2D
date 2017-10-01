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

  void killBody() {
    box2d.destroyBody(body);
    hasBody = false;
  }

  void isDead()
  {
    dead = true;
    killBody();
  }

  void display() {
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