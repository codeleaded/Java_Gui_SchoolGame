package de.schoolgame.render;

import de.schoolgame.utils.primitives.Vec2f;

public class TransformedView {
    Vec2f Output;
	Vec2f Offset;
	Vec2f StartPan;
	Vec2f Scale;
	float ZoomSpeed;

    public TransformedView(Vec2f Out){
	    this.Output = Out;
	    this.Offset = new Vec2f(0.0f,0.0f);
	    this.StartPan = new Vec2f(0.0f,0.0f);
	    this.Scale = new Vec2f(Out.x,Out.y);
	    this.ZoomSpeed = 0.0f;
    }

    public void Offset(Vec2f o){
    	this.Offset = this.Offset.add(o);
    }
    public void Zoom(Vec2f m){
    	this.Scale = this.Scale.mul(m);
    }
    
    public Vec2f ScreenWorldPos(Vec2f p){
    	p = p.div(this.Scale);
    	p = p.add(this.Offset);
    	return p;
    }
    public Vec2f ScreenWorldLength(Vec2f d){
    	d = d.div(this.Scale);
    	return d;
    }
    public Vec2f WorldScreenPos(Vec2f p){
    	p = p.sub(this.Offset);
    	p = p.mul(this.Scale);
    	return p;
    }
    public Vec2f WorldScreenLength(Vec2f d){
    	d = d.mul(this.Scale);
    	return d;
    }

    public float ScreenWorldX(float x){
    	x = x / this.Scale.x + this.Offset.x;
    	return x;
    }
    public float ScreenWorldLX(float xl){
    	xl = xl / this.Scale.x;
    	return xl;
    }
    public float WorldScreenX(float x){
    	x = (x - this.Offset.x) * this.Scale.x;
    	return x;
    }
    public float WorldScreenLX(float xl){
    	xl = xl * this.Scale.x;
    	return xl;
    }

    public float ScreenWorldY(float y){
    	y = y / this.Scale.y + this.Offset.y;
    	return y;
    }
    public float ScreenWorldLY(float xl){
    	xl = xl / this.Scale.y;
    	return xl;
    }
    public float WorldScreenY(float y){
    	y = (y - this.Offset.y) * this.Scale.y;
    	return y;
    }
    public float WorldScreenLY(float xl){
    	xl = xl * this.Scale.y;
    	return xl;
    }

    /* 
     * State 0: Panning     [PRESSED]
     * State 1: Panning     [DOWN]
     * State 2: Zoom IN     [DOWN]
     * State 3: Zoom OUT    [DOWN]
     */
    void HandlePanZoom(boolean[] States,Vec2f Mouse){
	    if(States[0]) {
	    	this.StartPan = Mouse;
	    }
        if(States[1]) {
	    	this.Offset = this.Offset.sub(Mouse.sub(this.StartPan).div(this.Scale));
	    	this.StartPan = Mouse;
	    }

	    Vec2f MouseWorld_BeforeZoom = ScreenWorldPos(Mouse);
	    if(States[2]) this.Scale = this.Scale.mul(1.01f + (this.ZoomSpeed));
	    if(States[3]) this.Scale = this.Scale.mul(0.99f  - (this.ZoomSpeed));
	    Vec2f MouseWorld_AfterZoom = ScreenWorldPos(Mouse);
	    this.Offset = this.Offset.add(MouseWorld_BeforeZoom.sub(MouseWorld_AfterZoom));
    }

    /* 
     * State 0: Zoom IN     [DOWN]
     * State 1: Zoom OUT    [DOWN]
     */
    void FocusPosition(boolean[] States,Vec2f Focused){
	    if(States[2]) this.Scale = this.Scale.mul(1.01f);
	    if(States[3]) this.Scale = this.Scale.mul(0.99f);

	    Vec2f Out = ScreenWorldLength(this.Output);	
	    this.Offset = Focused.sub(Out.mul(0.5f));
    }
}