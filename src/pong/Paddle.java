package pong;

public class Paddle {

	public int paddleNumber;
	
	public int x, y, width = 100, height = 500;
	
	public Paddle(int paddleNumb) {
		this.paddleNumber = paddleNumb;
		if (paddleNumber == 1) {
			this.x = width;
			this.y = Pong.pong.height / 2 - this.height / 2;
		} else {
			this.x = Pong.pong.width - width;
			this.y = Pong.pong.height / 2 - this.height / 2;
		}
		
	}
}
