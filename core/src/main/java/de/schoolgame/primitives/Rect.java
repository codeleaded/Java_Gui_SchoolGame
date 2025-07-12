package de.schoolgame.primitives;

import java.io.*;
import java.util.Objects;

import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Rect implements Externalizable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Vec2f pos;
    public Vec2f size;

    public Rect(){
        this.pos = new Vec2f(0.0f, 0.0f);
        this.size = new Vec2f(0.0f, 0.0f);
    }

    public Rect(Vec2f pos, Vec2f size) {
        this.pos = pos;
        this.size = size;
    }

    public Vec2f end() {
        return new Vec2f(pos.x + size.x, pos.y + size.y);
    }

    public Vec2f mid() {
        return pos.add(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f));
	}

    public boolean overlap(Rect r){
        if (this.pos.x < r.pos.x - this.size.x) return false;
        if (this.pos.x > r.pos.x + r.size.x) return false;
        if (this.pos.y < r.pos.y - this.size.y) return false;
        return !(this.pos.y > r.pos.y + r.size.y);
    }

    public boolean contains(Vec2f v){
        return v.x >= pos.x && v.x <= pos.x + size.x &&
            v.y >= pos.y && v.y <= pos.y + size.y;
    }

    public Vec2f GetDelta(Rect r){
        final Vec2f d = r.mid().sub(this.mid());
        final Vec2f newl = r.size.add(this.size);
        d.x /= newl.x;
        d.y /= newl.y;
        return d;
    }

    public Direction getDirection(Rect r){
        if(overlap(r)) {
            Rect ex = new Rect(r.pos.sub(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f)), r.size.add(this.size));
            Vec2f delta = GetDelta(r);

            if(abs(delta.x) > abs(delta.y)) {
                if(delta.x > 0) {
                    return Direction.LEFT;
                }else {
                    return Direction.RIGHT;
                }
            }else {
                if(delta.y > 0) {
                    return Direction.DOWN;
                }else {
                    return Direction.UP;
                }
            }
        }
        return Direction.NONE;
    }

	public Direction staticCollisionSolver(Rect r) {
        Direction d = getDirection(r);
        Rect ex = new Rect(r.pos.sub(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f)), r.size.add(this.size));
        Vec2f delta = GetDelta(r);

        switch (d) {
            case LEFT:
                this.pos.x = ex.pos.x - this.size.x / 2;
                break;
            case RIGHT:
                this.pos.x = ex.pos.x + ex.size.x - this.size.x / 2;
                break;
            case DOWN:
                this.pos.y = ex.pos.y - this.size.y / 2;
                break;
            case UP:
                this.pos.y = ex.pos.y + ex.size.y - this.size.y / 2;
                break;
        }

        return d;
    }


    public Vec2f getDirection(Direction d){
    	if(d==Direction.RIGHT)  return new Vec2f( 1.0f, 0.0f);
    	if(d==Direction.LEFT)   return new Vec2f(-1.0f, 0.0f);
    	if(d==Direction.UP)     return new Vec2f( 0.0f, 1.0f);
    	if(d==Direction.DOWN)   return new Vec2f( 0.0f,-1.0f);
    	return new Vec2f(0.0f,0.0f);
    }

    public ContactWrapper Rect_Ray_NearIntersection(Vec2f ray_origin,Vec2f ray_dir,Vec2f target_p,Vec2f target_l){
    	ContactWrapper cw = new ContactWrapper();

        if(ray_dir.x == 0.0f && ray_dir.y == 0.0f)
            return cw;

    	if(ray_dir.y==0.0f){
    		if(ray_origin.y > target_p.y && ray_origin.y < target_p.y + target_l.y){
    			if(ray_dir.x > 0.0f){
    				if(target_p.x > ray_origin.x && target_p.x < ray_origin.x + ray_dir.x){
    					cw.cp = new Vec2f( target_p.x,ray_origin.y );
                        cw.d = Direction.RIGHT;
    					return cw;
    				}
    			}else{
    				if(target_p.x < ray_origin.x && target_p.x > ray_origin.x + ray_dir.x){
    					cw.cp = new Vec2f( target_p.x + target_l.x,ray_origin.y );
                        cw.d = Direction.LEFT;
    					return cw;
    				}
    			}
    		}else{
    			return cw;
    		}
    	}
    	if(ray_dir.x==0.0f){
    		if(ray_origin.x > target_p.x && ray_origin.x < target_p.x + target_l.x){
                if(ray_dir.y > 0.0f){
    				if(target_p.y > ray_origin.y && target_p.y < ray_origin.y + ray_dir.y){
    					cw.cp = new Vec2f( ray_origin.x,target_p.y );
                        cw.d = Direction.DOWN;
    					return cw;
    				}
    			}else{
    				if(target_p.y < ray_origin.y && target_p.y > ray_origin.y + ray_dir.y){
    					cw.cp = new Vec2f( ray_origin.x,target_p.y + target_l.y );
                        cw.d = Direction.UP;
    					return cw;
    				}
    			}
    		}else{
    			return cw;
    		}
    	}

    	Vec2f invdir = new Vec2f().div(ray_dir);
    	Vec2f t_near = target_p.sub(ray_origin).mul(invdir);
    	Vec2f t_far = target_p.add(target_l.sub(ray_origin)).mul(invdir);

    	if (t_near.x > t_far.x){
            float swap = t_near.x;
            t_near.x = t_far.x;
            t_far.x = swap;
        }
    	if (t_near.y > t_far.y) {
            float swap = t_near.y;
            t_near.y = t_far.y;
            t_far.y = swap;
        }
    	if (t_near.x > t_far.y || t_near.y > t_far.x) return cw;

    	cw.t = Math.max(t_near.x,t_near.y);
    	float t_hit_far = Math.min(t_far.x,t_far.y);

    	if (t_hit_far < 0.0f)
    		return cw;

    	cw.cp = ray_dir.mul(cw.t).add(ray_origin);
    	if (t_near.x > t_near.y)
    		if (invdir.x < 0.0f){
                cw.d = Direction.RIGHT;
                return cw;
            }else{
                cw.d = Direction.LEFT;
                return cw;
            }
    	else if (t_near.x < t_near.y)
    		if (invdir.y < 0.0f){
                cw.d = Direction.UP;
                return cw;
            }else{
                cw.d = Direction.DOWN;
                return cw;
            }

    	return cw;
    }

    public ContactWrapper RI_Solver(Vec2f target, Rect collisionObject){
        final Vec2f middlePos = pos.add(size.mul(0.5f));
        final Vec2f middleTarget = target.add(size.mul(0.5f));
        final Vec2f vector = middleTarget.sub(middlePos);

        final Vec2f p_ex = collisionObject.pos.sub(size.mul(0.5f));
        final Vec2f l_ex = size.add(collisionObject.size);

        ContactWrapper cw = Rect_Ray_NearIntersection(middlePos, vector, p_ex, l_ex);
        if (cw.d != Direction.NONE && cw.t >= 0.0f && cw.t <= 1.0f) {
            System.out.println(pos+" "+target+" "+cw.cp);

            Vec2f n = getDirection(cw.d);
            Vec2f c_m = cw.cp.sub(size.mul(0.5f));
            Vec2f vel = new Vec2f(
                vector.x * Math.abs(n.y) * (1.0f - cw.t),
                vector.y * Math.abs(n.x) * (1.0f - cw.t)
            );
            cw.cp = c_m.add(vel);
            return cw;
        }
        return null;
    }


    public boolean compareInt(Rect r) {
        return (int)pos.x == (int)r.pos.x &&
               (int)pos.y == (int)r.pos.y &&
               (int)size.x == (int)r.size.x &&
               (int)size.y == (int)r.size.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return Objects.equals(pos, rect.pos) && Objects.equals(size, rect.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, size);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(pos);
        out.writeObject(size);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pos = (Vec2f) in.readObject();
        size = (Vec2f) in.readObject();
    }
}
