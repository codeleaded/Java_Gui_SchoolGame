package de.schoolgame.render.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.schoolgame.primitives.Recti;
import de.schoolgame.primitives.Vec2i;

public abstract class Widget {
    protected Recti rect;

    public Widget(Vec2i pos, Vec2i size) {
        this.rect = new Recti(pos,size);
    }

    public abstract void render(SpriteBatch sb, ShapeRenderer sr);
    public abstract void dispose();

    public boolean onClick() {
        return false;
    }

    public boolean onHover(boolean hovered) {
        return false;
    }

    public Recti getRect() {
        return rect;
    }

    public Vec2i getPos() {
        return rect.pos;
    }

    public Vec2i getSize() {
        return rect.size;
    }

    public void setRect(Recti rect) {
        this.rect = rect;
    }

    public void setPos(Vec2i pos) {
        this.rect.pos = pos;
    }

    public void setSize(Vec2i size) {
        this.rect.size = size;
    }
}
