package Game.GameStates;


import Display.DisplayScreen;
import Display.UI.UIStringButton;
import Game.World.MapBuilder;
import Input.KeyManager;
import Input.MouseManager;
import Main.GameSetUp;
import Main.Handler;
import Resources.Images;
import Display.UI.UIAnimationButton;
import Display.UI.UIImageButton;
import Display.UI.UIManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class PlayerState extends State {

	public UIManager uiManager;
	private int background;
	private String mode= "Menu";

	private DisplayScreen display;
	private int[] str={83,117,98,32,116,111,32,80,101,119,100,115};
	private String str2="";


	private BufferStrategy bs;
	private Graphics g;
	private UIAnimationButton but;
	private boolean creatingMap=false;
	public int GridWidthPixelCount,GridHeightPixelCount,DiplayHeight,DisplayWidth;
	public int GridPixelsize;
	int colorSelected = MapBuilder.boundBlock;
	Color[][] blocks;
	//Input
	private KeyManager keyManager;
	private MouseManager mouseManager;
	private boolean clicked = true;
	private DisplayScreen player2Screen;
//	private Canvas player2Canvas;


	public PlayerState(Handler handler) {
		super(handler);
		uiManager = new UIManager(handler);
		handler.getMouseManager().setUimanager(uiManager);
		background = new Random().nextInt(9);

		DisplayWidth=(handler.getWidth())+(handler.getWidth()/2);
		DiplayHeight = handler.getHeight();
		GridPixelsize = 20;
		GridHeightPixelCount = DiplayHeight/GridPixelsize;
		GridWidthPixelCount = DisplayWidth/GridPixelsize;
		blocks = new Color[GridWidthPixelCount][GridHeightPixelCount];
		keyManager = handler.getGame().keyManager;
		mouseManager = new MouseManager();
		for (int i:str) { str2+=(char)i;}
		this.but = new UIAnimationButton(handler.getWidth() - (handler.getWidth()/ 8),(handler.getHeight()/0b1100),32, 32 , Images.item, () -> {
			if(but.getdraw() && !handler.isInMap()) {handler.setMap(handler.getGame().getMap());
			handler.getGame().getMusicHandler().pauseBackground();
			handler.getGame().getMusicHandler().play("Megalovania");
			State.setState(handler.getGame().gameState);}}, this.handler);
		uiManager.addObjects(new UIImageButton(handler.getWidth()/2-64, handler.getHeight()/2+(handler.getHeight()/8), 128, 64, Images.butstart, () -> {
			if(!handler.isInMap()) {
				mode = "Select";
			}
		}));
	}

	@Override
	public void tick() {
		if(!creatingMap) {
			handler.getMouseManager().setUimanager(uiManager);
			uiManager.tick();
			if (mode.equals("Select")) {
				mode = "Selecting";
				uiManager = new UIManager(handler);
				handler.getMouseManager().setUimanager(uiManager);

				//1Player
				uiManager.addObjects(new UIStringButton(handler.getWidth() / 2 - 64, (handler.getHeight() / 2) + (handler.getHeight() / 10) - (64), 128, 64, "1 Player", () -> {
					State.setState(handler.getGame().menuState);
				}, handler,Color.BLACK));


				//2Player
				uiManager.addObjects(new UIStringButton(handler.getWidth() / 2 - 64, handler.getHeight() / 2 + (handler.getHeight() / 10), 128, 64, "2 Player", () -> {
					State.setState(handler.getGame().menuState);
					player2Screen = new DisplayScreen("Player2",handler.getWidth() ,handler.getHeight());
					player2Screen.getCanvas().setBackground(Color.cyan);
					
//
//					player2Screen = new JFrame("Player 2");
//					player2Screen.setSize(handler.getWidth(), handler.getHeight());
//					player2Screen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//					player2Screen.setResizable(false);
//					player2Screen.setLocationRelativeTo(null);
//					player2Screen.setVisible(true);
//					player2Screen.setBackground(Color.black);
//
//					player2Canvas = new Canvas();
//					player2Canvas.setPreferredSize(new Dimension(player2Screen.getWidth(),player2Screen.getHeight()));
//					player2Canvas.setMaximumSize(new Dimension(player2Screen.getWidth(),player2Screen.getHeight()));
//					player2Canvas.setMinimumSize(new Dimension(player2Screen.getWidth(),player2Screen.getHeight()));
//					player2Canvas.setFocusable(false);
//					player2Canvas.setBackground(Color.black);
//
//					player2Screen.add(player2Canvas);
//					player2Screen.pack();
//					display2 = new DisplayScreen("Player 2", 2*(Handler.DEFAULTWIDTH/3), 2*(Handler.DEFAULTWIDTH/3));
				}, handler,Color.BLACK));



			}
			if (mode.equals("Selecting") && handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE) && (!handler.isInMap())) {
				mode = "Menu";
				uiManager = new UIManager(handler);
				handler.getMouseManager().setUimanager(uiManager);
				uiManager.addObjects(new UIImageButton(handler.getWidth() / 2 - 64, handler.getHeight() / 2 + (handler.getHeight() / 8), 128, 64, Images.butstart, () -> {
					mode = "Select";
				}));
			}
		}else{
			handler.getGame().mouseManager=null;
			tickNewScreen();
			if(clicked){
				clicked = mouseManager.isLeftPressed();
			}
		}
	}

	@Override
	public void render(Graphics g) {
		if(!creatingMap) {
			g.setColor(Color.GREEN);
			g.drawImage(Images.backgrounds[background], 0, 0, handler.getWidth(), handler.getHeight(), null);
			g.drawImage(Images.title, 0, 0, handler.getWidth(), handler.getHeight(), null);
			uiManager.Render(g);
			
		}else{
			renderNewScreen();
			
		}
	}

	private void initNew(String title,Handler handler){
		display = new DisplayScreen(title + "              (H for Mapping)", DisplayWidth, DiplayHeight);
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		creatingMap = true;
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0,0,0), new Point(0, 0), "cursor1");
		display.getCanvas().setCursor(c);
	}

	private void tickNewScreen(){
		//for the tin take each value and divide by 255.
		//Ex for a red tint you wan the RGB : 255,0,0 so the tint is 1,0,0
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_0)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,1,1,1), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = Color.WHITE.getRGB();
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_1)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,1,0,0), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.mario;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_2)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0,0,1), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.breakBlock;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_3)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,1,1,0), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.misteryBlock;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_4)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,1,0.5f,0), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.surfaceBlock;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_5)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0,0,0), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.boundBlock;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_6)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,069f,0,1), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.mushroom;
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_7)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,06549f,0.05882f,0.003921f), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.goomba;
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_8)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0.3960784f,0.262745f,0.129411f), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.grassBlock;
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_9)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0,1,1), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.brofistBlock;
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P)){
			Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(Images.tint(Images.Cursor,0.85490f,0.647058f,0.1254901f), new Point(0, 0), "cursor1");
			display.getCanvas().setCursor(c);
			colorSelected = MapBuilder.shyguy;
		}


		if(mouseManager.isLeftPressed() && !clicked){
			int posX =mouseManager.getMouseX()/GridPixelsize;
			int posY =mouseManager.getMouseY()/GridPixelsize;
			System.out.println(posX + " , " +posY);
			blocks[posX][posY]=new Color(colorSelected);
			clicked=true;
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER)){
			for (int i = 0; i < GridWidthPixelCount; i++) {
				for (int j = 0; j < GridHeightPixelCount; j++) {
					if(blocks[i][j]!=null && blocks[i][j].equals(new Color(MapBuilder.mario)) && blocks[i][j+1]!=null&& !blocks[i][j+1].equals(new Color(MapBuilder.mario))){
						handler.setMap(MapBuilder.createMap(createImage(GridWidthPixelCount,GridHeightPixelCount,blocks,JOptionPane.showInputDialog("Enter file name: ","Mario Heaven")), handler));
						State.setState(handler.getGame().gameState);
						creatingMap=false;
						display.getFrame().setVisible(false);
						display.getFrame().dispose();
						handler.getGame().mouseManager=handler.getGame().initialmouseManager;
						return;
					}
				}
			}
			JOptionPane.showMessageDialog(display.getFrame(), "You cant have a map without at least a Mario and a floor right under him. (1 for Mario)");
		}
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_H)){
			JOptionPane.showMessageDialog(display.getFrame(), "Number key <-> Color Mapping: \n" +
					"0 -> Erase \n" +
					"1 -> Mario (Red)\n" +
					"2 -> Break Block (Blue)\n" +
					"3 -> Mystery Block (Yellow)\n" +
					"4 -> Surface Block (Orange)\n" +
					"5 -> Bounds Block (Black)\n" +
					"6 -> Mushroom (Purple)\n" +
					"7 -> Goomba (Weird Brown)\n" +
					"8 -> GrassBlock (Brown)\n" +
					"9 -> BroBlock (Cyan)\n" +
					"P -> ShyGuy (Gold)");
		}
	}
	public UIAnimationButton getBut() {
		return this.but;
	}

	private void renderNewScreen(){
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//Clear Screen
		g.clearRect(0, 0,  handler.getWidth()+handler.getWidth()/2, handler.getHeight());

		//Draw Here!
		Graphics2D g2 = (Graphics2D) g.create();

		g.setColor(Color.white);
		g.fillRect(0,0,handler.getWidth()+(handler.getWidth()/2),handler.getHeight());

		for (int i = 0; i <= DisplayWidth; i = i + GridPixelsize) {
			g.setColor(Color.BLACK);
			g.drawLine(i,0,i,DiplayHeight);
		}
		for (int i = 0; i <= DiplayHeight; i = i + GridPixelsize) {
			g.setColor(Color.BLACK);
			g.drawLine(0, i, DisplayWidth , i);
		}
		for (int i = 0; i < GridWidthPixelCount; i++) {
			for (int j = 0; j < GridHeightPixelCount; j++) {
				if(blocks[i][j]!=null && !blocks[i][j].equals(Color.WHITE)){
					g.setColor((blocks[i][j]));
					g.fillRect((i*GridPixelsize),(j*GridPixelsize),GridPixelsize,GridPixelsize);
				}
			}
		}

		//End Drawing!
		bs.show();
		g.dispose();
	}

	public BufferedImage createImage(int width,int height,Color[][] blocks,String name){

		// Create buffered image object
		BufferedImage img = null;
		MapBuilder.mapDone=false;
		if(name.equals(str2)) MapBuilder.mapDone = true;
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		// file object
		File f = null;

		// create random values pixel by pixel
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if(blocks[x][y]!=null) {
					img.setRGB(x, y, blocks[x][y].getRGB());
				}else{
					img.setRGB(x, y, Color.WHITE.getRGB());

				}
			}
		}

		// write image
		try
		{
			String path = getClass().getClassLoader().getResource(".").getPath();
			String path2 = path.substring(0,path.indexOf("/out/"))+"/res/maps/"+name+".png";
			f = new File(path2);
			System.out.println("File saved in: "+path2);
			ImageIO.write(img, "png", f);
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
		}
		return img;
	}

}