package de.schoolgame.world;

import com.badlogic.gdx.math.Vector2;
import static java.lang.Math.*;

public class Rect {
    public Vector2 p;
    public Vector2 l;
    public Vector2 v;

    public Rect(){
        this.p = new Vector2(0.0f,0.0f);
        this.l = new Vector2(0.0f,0.0f);
        this.v = new Vector2(0.0f,0.0f);
    }
    public Rect(Vector2 p,Vector2 l){
        this.p = p;
        this.l = l;
        this.v = new Vector2(0.0f,0.0f);
    }
    public Rect(Vector2 p,Vector2 l,Vector2 v){
        this.p = p;
        this.l = l;
        this.v = v;
    }

    public Vector2 Mid() {
		return p.add(new Vector2(this.l.x * 0.5f,this.l.y * 0.5f));
	}

    public boolean Overlap(Rect r){
		if(this.p.x<=r.p.x - this.l.x)  return false;
		if(this.p.x>=r.p.x + r.l.x)     return false;
		if(this.p.y<=r.p.y - this.l.y)  return false;
		if(this.p.y>=r.p.y + r.l.y)     return false;
		return true;
	}
	
	public int StaticCollisionSolver(Rect r){
		if(Overlap(r)) {
            Rect ex = new Rect(r.p.sub(new Vector2(this.l.x * 0.5f,this.l.y * 0.5f)),r.l.add(this.l));
			Vector2 d = r.Mid().sub(this.Mid());

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
