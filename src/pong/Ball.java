package pong;

import java.awt.Graphics;

public class Ball {

	public int x, y, width, height;
	public int motionX, motionY;
	
	public Ball() {
		
	}
	
	public void update(Paddle paddle1, Paddle paddle2) {
		if (checkCollision(paddle1) == 1) {
			
		}
	}
	
	public int checkCollision(Paddle paddle) {
		if (paddle.x > x || paddle.x < x + width) {
			if (paddle.y > y + height || paddle.y + height < y) {
				return 1; //hit
			} else {
				return 2; //wall
			}
		}
		return 0; //nothing
	}
	
	public void render(Graphics g) {
		
	}
}
