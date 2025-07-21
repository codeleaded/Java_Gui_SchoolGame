package de.schoolgame.render.renderer;

import de.schoolgame.primitives.Rect;
import de.schoolgame.primitives.Vec2f;

public class GuiComponent  {
    public static final int TYPE_RED = 0;
    public static final int TYPE_PURPLE = 1;
    public static final int TYPE_BLUE = 2;
    public static final int TYPE_GREEN = 3;
    public static final int TYPE_LIGHTGREEN = 4;
    public static final int TYPE_YELLOW = 5;
    public static final int TYPE_ORANGE = 6;
    public static final int TYPE_BLACK = 7;

    public static final int ID_SQUARE = 0;
    public static final int ID_SQUAREBOX = 1;
    public static final int ID_CIRCLE = 2;
    public static final int ID_STAR = 3;
    public static final int ID_CIRCLEBOX = 4;

    public static final int COUNT_X = 10;
    public static final int COUNT_Y = 8;

    
    public static Rect getRect(int id,int type){
        switch(id){
            case ID_SQUARE:     return new Rect(new Vec2f(0.0f,type),new Vec2f(1.0f,1.0f));
            case ID_SQUAREBOX:  return new Rect(new Vec2f(1.0f,type),new Vec2f(3.0f,1.0f));
            case ID_CIRCLE:     return new Rect(new Vec2f(4.0f,type),new Vec2f(1.0f,1.0f));
            case ID_STAR:       return new Rect(new Vec2f(5.0f,type),new Vec2f(1.0f,1.0f));
            case ID_CIRCLEBOX:  return new Rect(new Vec2f(6.0f,type),new Vec2f(4.0f,1.0f));
        }
        return new Rect();
    }
}
