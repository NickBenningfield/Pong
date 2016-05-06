package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.Timer;
 // Code based on Youtube tutorial by Jaryt Bustard
public class Pong implements ActionListener, KeyListener{
// 31:25 on video
	public static Pong pong;
	public int width = 700, height = 700;
	public Renderer renderer;
	public Paddle player1;
	public Paddle player2;
	public Ball ball;
	public int botDifficulty, botCooldown = 0, botMoves;
	
	public boolean bot = false, selectingDifficulty;
	public boolean w, s, up, down;
	
	public int gameStatus = 0, scoreLimit = 7, playerWon; // 0 = stopped, 1 = paused 2 = playing
	
	public Pong() {
		Timer timer = new Timer(20, this);
		JFrame jframe = new JFrame("Pong");
		renderer = new Renderer();
		jframe.setSize(width+15, height+35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);
		
		timer.start();
	}

	public void start() {
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
		gameStatus = 2;
	}
	
	public void update() {
		if (player1.score >= scoreLimit) {
			playerWon = 1;
			gameStatus = 3;
		} else if (player2.score >= scoreLimit) {
			playerWon = 2;
			gameStatus = 3;
		}
		
		if (w) {
			player1.move(true);
		}
		if (s) {
			player1.move(false);
		}
		if (!bot) {	
			if (up) {
				player2.move(true);
			}
			if (down) {
				player2.move(false);
			}
		} else {
			if (botCooldown > 0) {
				botCooldown--;
				if (botCooldown == 0) {
					botMoves = 0;
				}
			}
			if (botMoves < 10) {
				if (player2.y  + player2.height - player2.height / 3 <= ball.y) {
					player2.move(false);
					botMoves++;
				} else if (player2.y + player2.height / 3 > ball.y) {
					player2.move(true);
					botMoves++;
				}
				if (botDifficulty == 0) {
					botCooldown = 20;	
				} else if (botDifficulty == 1) {
					botCooldown = 15;	
				} else if (botDifficulty == 2) {
					botCooldown = 5;	
				}
			}
		}
		ball.update(player1, player2);
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if (gameStatus == 0) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 40));
			g.drawString("PONG", width / 2 - 75, 50);
			
			if (!selectingDifficulty) {
				g.setFont(new Font("Arial", 1, 30));
				g.drawString("Press Space to Play", width / 2 - 140, height / 2 - 25);
				
				g.drawString("Press Shift to Play with Bot", width / 2 - 200, height / 2 + 25);
				
				g.drawString("<< Score Limit: " + scoreLimit + " >>", width / 2 - 125, height / 2 + 75);
			}
		}
		if (selectingDifficulty) {
			String strBotDiff = botDifficulty == 0 ? "Easy" : (botDifficulty == 1 ? "Medium" : "Hard");
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("<< Bot Difficulty: " + strBotDiff + " >>", width / 2 - 180, height / 2 - 25);
			g.drawString("Press Space to Play", width / 2 - 140, height / 2 + 25);
		}
		if (gameStatus == 2 || gameStatus == 1) {
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(5f));
			g.drawLine(width / 2, 0, width / 2, height);
			
			g.drawOval(width / 2 - 100,  height / 2 - 100, 200, 200);
			
			g.setFont(new Font("Arial", 1, 40));
			g.drawString(String.valueOf(player1.score), width / 2 - 75, 50);
			g.drawString(String.valueOf(player2.score), width / 2 + 75, 50);
			
			
			player1.render(g);
			player2.render(g);
			ball.render(g);			
		}
		if (gameStatus == 1) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PAUSED", width / 2 - 100, height / 2 - 25);
		}
		if (gameStatus == 3) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PONG", width / 2 - 75, 50);
			if (!bot) {
				g.drawString("Player " + playerWon + " Wins!", width / 2 - 165, 200);
			} else {
				g.drawString("The Bot Wins!", width / 2 - 165, 200);
			}
			
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Press Space to Play again", width / 2 - 185, height / 2 - 25);
			g.drawString("Press ESC for Menu", width / 2 - 140, height / 2 + 25);			
		}
	}
	
	public static void main(String[] args){
		pong = new Pong();

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (gameStatus == 2) {
			update();			
		}
		renderer.repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		
		if (id == KeyEvent.VK_W) {
			w = true;
		} else if (id == KeyEvent.VK_S) {
			s = true;
		} else if (id == KeyEvent.VK_UP) {
			up = true;
		} else if (id == KeyEvent.VK_DOWN) {
			down = true;
		} else if (id == KeyEvent.VK_RIGHT) {
			if (selectingDifficulty) {
				if (botDifficulty < 2) {
					botDifficulty++;
				} else {
					botDifficulty = 0;
				}
			} else if (gameStatus == 0) {
				scoreLimit++;
			}
		} else if (id == KeyEvent.VK_LEFT) {
			if (selectingDifficulty) {
				if (botDifficulty > 0) {
					botDifficulty--;
				} else {
					botDifficulty = 2;
				}
			} else if (gameStatus == 0 && scoreLimit > 1) {
				scoreLimit--;
			}
		} else if (id == KeyEvent.VK_SHIFT && gameStatus == 0) {
			bot = true;
			selectingDifficulty = true;
		} else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus == 0 || gameStatus == 3) {
				if (!selectingDifficulty) {
					bot = false;
				} else {
					selectingDifficulty = false;
				}
				start();
			} else if (gameStatus == 1) {
				gameStatus = 2;
			} else if (gameStatus == 2) {
				gameStatus = 1;
			}
		} else if (id == KeyEvent.VK_ESCAPE) {
			gameStatus = 0;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();
		
		if (id == KeyEvent.VK_W) {
			w = false;
		}
		if (id == KeyEvent.VK_S) {
			s = false;
		}
		if (id == KeyEvent.VK_UP) {
			up = false;
		}
		if (id == KeyEvent.VK_DOWN) {
			down = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
