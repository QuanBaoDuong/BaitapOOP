package game.object;

public abstract class MovableObject extends GameObject {
        protected int dx;
        protected int dy;

        MovableObject(int x,int y,int width,int height,int dx,int dy) {
            super(x,y,width,height);
            this.dx=dx;
            this.dy=dy;
        }
        public abstract void move();
}
