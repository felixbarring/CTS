
/*
 * The MIT License
 *
 * Copyright 2014 Gustaf Ringius <Gustaf@linux.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */



package view;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import util.IDrawable;
import controller.MainController;
import controller.SoundController;

/**
 * A class that is the graphical representation of the Simulator
 * 
 * @author Felix Bärring <felixbarring@gmail.com>
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-05-11
 *                      Added music handling on lines 329 & 335.
 */
public class Simulation implements IFViewMode{

	private static Simulation instance;

	ButtonContainer menu;
	ButtonContainer menuPaused;

	ButtonContainer currentInUse;
	ButtonContainer notInUse;

	ConfirmPopUp popUp;

	Terminal terminal;

	List<IFDrawTickAndInput> drawAndTickList = new LinkedList<>();

	private int PICTURE_WIDTH;
	private int PICTURE_HEIGHT;

	BufferedImage simulationMenuBuffer; 
	Graphics2D simulationG;
	GuiDisplay guiDisplay;

	String mainMenuBtn = "Main Menu";
	String pauseBtn = "Pause";
	String terminalBtn = "Terminal";

	String resumeBtn = "Resume";

	private final int keyCoolDown = 10;
	private int counter = 0;
	private boolean menuIsVissible = false;
	private boolean toggleAllowed = true;

	private final int BUTTON_WIDTH = 125;
	private final int BUTTON_HEIGHT = 50;
	private final int BUTTON_SPACING = 5;

	private final int MENU_VISSIBLE_X;
	private final int MENU_VISSIBLE_Y;

	private final int MENU_HIDDEN_X;
	private final int MENU_HIDDEN_Y;

	private final int POP_UP_VISSIBLE_X = 100;
	private final int POP_UP_VISSIBLE_Y = 200;

	private final int POP_UP_HIDDEN_X = -800;
	private final int POP_UP_HIDDEN_Y = 200;

	private final int TERMINAL_VISSIBLE_X = 100;
	private final int TERMINAL_VISSIBLE_Y = 30;

	private final int TERMINAL_HIDDEN_X = 1000;
	private final int TERMINAL_HIDDEN_Y = 30;


	private double scale;

	private int offSetWidth;
	private int offSetHeight;

	private int widthFrame;
	private int heightFrame;

	public Simulation(GuiDisplay gd, int width, int height, int widthF, int heightF){
		guiDisplay = gd;

		PICTURE_WIDTH = width;
		PICTURE_HEIGHT = height;

		widthFrame = widthF;
		heightFrame = heightF;

		simulationMenuBuffer = new BufferedImage(PICTURE_WIDTH, PICTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		simulationG = (Graphics2D) simulationMenuBuffer.getGraphics();
		simulationG.setBackground(PublicConstants.clear);

		resized(widthF, heightF);

		List<String> arguments = new LinkedList<>();
		arguments.add(mainMenuBtn);
		arguments.add(pauseBtn);
		arguments.add(terminalBtn);

		MENU_VISSIBLE_X = (width-arguments.size()*(BUTTON_WIDTH+BUTTON_SPACING))/2;
		MENU_VISSIBLE_Y = height-55;

		MENU_HIDDEN_X = (width-arguments.size()*(BUTTON_WIDTH+BUTTON_SPACING))/2;
		MENU_HIDDEN_Y = height+55;

		menu = new ButtonContainer(
				MENU_VISSIBLE_X, height+55, BUTTON_WIDTH, BUTTON_HEIGHT, 
				arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Horizontal);
		drawAndTickList.add(menu);

		currentInUse = menu;

		arguments.clear();
		arguments.add(mainMenuBtn);
		arguments.add(resumeBtn);
		arguments.add(terminalBtn);

		menuPaused = new ButtonContainer(
				MENU_VISSIBLE_X, height+55, BUTTON_WIDTH, BUTTON_HEIGHT, 
				arguments.size(), BUTTON_SPACING, arguments, this, 
				ButtonContainer.ButtonContainerType.Horizontal);
		drawAndTickList.add(menuPaused);

		popUp = new ConfirmPopUp(POP_UP_HIDDEN_X, POP_UP_HIDDEN_Y, 600, 150, 100, 50, new LinkedList<String>(
				Arrays.asList("Yes", "No")),
				new LinkedList<String>(Arrays.asList(
						"Nothing of the current session will be saved. Are you sure you want to quit to main menu?")), 
						this);
		drawAndTickList.add(popUp);

		terminal = new Terminal(TERMINAL_HIDDEN_X, TERMINAL_HIDDEN_Y, 600, 540, this);
		drawAndTickList.add(terminal);

		toggleMenu();
	}

	public static Simulation createInstance(GuiDisplay gd, int width, int height, int widthF, int heightF) throws Exception{
		if(instance != null) throw new Exception("It is not allowed to call creatInstance more than once.");
		else{
			return (instance = new Simulation(gd, width, height, widthF, heightF));
		}
	}

	private void toggleMenu(){
		if(!menuIsVissible){
			currentInUse.setTargetVissible(MENU_VISSIBLE_X, MENU_VISSIBLE_Y);
		} else {
			currentInUse.setTargetHidden(MENU_HIDDEN_X, MENU_HIDDEN_Y);
		}
		menuIsVissible = !menuIsVissible;
	}

	private void drawIDrawAble(IDrawable drawAble, Graphics2D g){
		g.setColor(drawAble.getColor());
		switch (drawAble.getType()){
		case IMAGE:
			// Not handled
			break;
		case RECTANGLE:
			g.setStroke(new BasicStroke(2));
			g.drawRect((int)drawAble.getXPos()-(int)(drawAble.getWidth()/2), 
					(int)drawAble.getYPos()-(int)(drawAble.getHeight()/2), 
					(int)drawAble.getWidth(),
					(int)drawAble.getHeight());
			break;
		case CIRCLE:
			g.fillOval((int)(drawAble.getXPos()-drawAble.getRadius()), 
					(int)(drawAble.getYPos()-drawAble.getRadius()), (int)drawAble.getRadius()*2, 
					(int)drawAble.getRadius()*2);
			break;
		case DOT:
			g.fillOval((int)drawAble.getXPos(), (int)drawAble.getYPos(), 3, 3);
			break;
		case LINE:
			g.setStroke(new BasicStroke(7));
			g.drawLine((int)drawAble.getXPos(), (int)drawAble.getYPos(), 
					(int)drawAble.getEndX(), (int)drawAble.getEndY());
			break;
		default:
			break;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setBackground(PublicConstants.BGColor);
		g.clearRect(0, 0, widthFrame, heightFrame);
		g.setColor(PublicConstants.black);

		for (IDrawable d: MainController.getGraphics()){
			drawIDrawAble(d, g);
		}

		simulationG.clearRect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT);
		for(IFDrawTickAndInput d : drawAndTickList){
			d.draw(simulationG);
		}

		g.drawImage(simulationMenuBuffer, offSetWidth, offSetHeight, 
				(int)(PICTURE_WIDTH*scale), (int)(PICTURE_HEIGHT*scale), null);

	}

	@Override
	public void tick() {
		counter++;
		for(IFDrawTickAndInput d : drawAndTickList){
			d.tick();
		}
	}

	@Override
	public void resized(int widthF, int heightF) {
		widthFrame = widthF;
		heightFrame = heightF;
		double scaleWidth = (double)widthFrame/(double)PICTURE_WIDTH;
		double scaleHeight = (double)heightFrame/(double)PICTURE_HEIGHT; 
		if(scaleWidth < scaleHeight){
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		offSetWidth = (int)((widthFrame-(PICTURE_WIDTH*scale))/2.0);
		offSetHeight = (int)((heightFrame-(PICTURE_HEIGHT*scale))/2.0);
	}

	@Override
	public void mouseMoved(int xs, int ys) {
		int x = (int)((xs-offSetWidth)/scale); 
		int y = (int)((ys-offSetHeight)/scale);
		for(IFDrawTickAndInput d : drawAndTickList){
			d.mouseMoved(x, y);
		}
	}

	@Override
	public void mouseClicked(int button, int xs, int ys) {
		int x = (int)((xs-offSetWidth)/scale); 
		int y = (int)((ys-offSetHeight)/scale);
		if(button == 1){
			for(IFDrawTickAndInput d : drawAndTickList){
				d.mouseClicked(button, x, y);
			}
		}
		if(button == 3){
			if(toggleAllowed){
				keyPressed(27);
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		if(key == 27 && counter > keyCoolDown && toggleAllowed){
			SoundController.getInstance().playButtonSound();
			toggleMenu();
			counter = 0;
		} else{
			for(IFDrawTickAndInput d : drawAndTickList){
				d.keyPressed(key);
			}
		}
	}

	@Override
	public void buttonPressed(String id) {
		SoundController.getInstance().playButtonSound();
		if (id.equals(mainMenuBtn)){
			popUp.setTargetVissible(POP_UP_VISSIBLE_X, POP_UP_VISSIBLE_Y);
			toggleMenu();
			toggleAllowed = false;
		} else if (id.equals("Yes")){
			terminal.clear();
			popUp.setTargetHidden(POP_UP_HIDDEN_X, POP_UP_HIDDEN_Y);
			currentInUse = menu;
			toggleMenu();
			guiDisplay.toggleMode();
			toggleAllowed = true;
		}	else if(id.equals("No")){
			popUp.setTargetHidden(POP_UP_HIDDEN_X, POP_UP_HIDDEN_Y);
			toggleMenu();
			toggleAllowed = true;
		} else if(id.equals(terminalBtn)){
			terminal.toggleActive();
			terminal.setTargetVissible(TERMINAL_VISSIBLE_X, TERMINAL_VISSIBLE_Y);
			toggleMenu();
			toggleAllowed = false;
		} else if(id.equals("Close")){
			terminal.toggleActive();
			terminal.setTargetHidden(TERMINAL_HIDDEN_X, TERMINAL_HIDDEN_Y);
			toggleMenu();
			toggleAllowed = true;
		}	else if(id.equals(pauseBtn)){
			MainController.pauseSimulation();
			SoundController.getInstance().pauseSimulationMusic();
			menu.setTargetHidden(MENU_HIDDEN_X, MENU_HIDDEN_Y);
			menuPaused.setTargetVissible(MENU_VISSIBLE_X, MENU_VISSIBLE_Y);
			currentInUse = menuPaused;
		} else if (id.equals(resumeBtn)){
			MainController.resumeSimulation();
			SoundController.getInstance().pauseSimulationMusic();
			menu.setTargetVissible(MENU_VISSIBLE_X, MENU_VISSIBLE_Y);
			menuPaused.setTargetHidden(MENU_HIDDEN_X, MENU_HIDDEN_Y);
			currentInUse = menu;
		}
	}

}
