package de.schoolgame.utils.primitives;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Rect {
    public Vec2f p;
    public Vec2f l;
    public Vec2f v;

    public Rect(){
        this.p = new Vec2f(0.0f, 0.0f);
        this.l = new Vec2f(0.0f, 0.0f);
        this.v = new Vec2f(0.0f, 0.0f);
    }

    public Rect(Vec2f p, Vec2f l) {
        this.p = p;
        this.l = l;
        this.v = new Vec2f(0.0f, 0.0f);
    }

    public Rect(Vec2f p, Vec2f l, Vec2f v) {
        this.p = p;
        this.l = l;
        this.v = v;
    }

    public Vec2f Mid() {
        return p.add(new Vec2f(this.l.x * 0.5f, this.l.y * 0.5f));
	}

    public boolean Overlap(Rect r){
		if(this.p.x<=r.p.x - this.l.x)  return false;
		if(this.p.x>=r.p.x + r.l.x)     return false;
		if(this.p.y<=r.p.y - this.l.y)  return false;
        return !(this.p.y >= r.p.y + r.l.y);
    }

	public int StaticCollisionSolver(Rect r){
		if(Overlap(r)) {
            Rect ex = new Rect(r.p.sub(new Vec2f(this.l.x * 0.5f, this.l.y * 0.5f)), r.l.add(this.l));
            Vec2f d = r.Mid().sub(this.Mid());

            d.x /= ex.l.x;
            d.y /= ex.l.y;

			if(abs(d.x) > abs(d.y)) {
				if(d.x > 0) {
					this.p.x = ex.p.x - this.l.x/2;
                    return 1;
				}else {
					this.p.x = ex.p.x + ex.l.x - this.l.x/2;
                    return 2;
				}
			}else {
				this.v.y = 0.0f;
				if(d.y > 0) {
					this.p.y = ex.p.y - this.l.y/2;
                    return 3;
				}else {
					this.p.y = ex.p.y + ex.l.y - this.l.y/2;
                    return 4;
				}
			}
		}
		return 0;
	}
}
