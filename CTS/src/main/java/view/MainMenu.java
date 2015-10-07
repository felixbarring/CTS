
/*
 * The MIT License
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

/**
 * @author Felix Bärring <felixbarring@gmail.com>
 */


package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import controller.SoundController;

/**
 * This class is the main logical unit of the Main Menu
 * It combines and controls a big number of various GUI elements needed to make everything come togheter. 
 * @author Felix Bärring <felixbarring@gmail.com>
 */

public class MainMenu implements IFViewMode, IFButtonListener, IFDrawTickAndInput {


	private static MainMenu instance;

	Title title;

	ButtonContainer mainBtnContainer;
	ButtonContainer editMapBtnContainer;
	ButtonContainer aboutBtnContainer;
	ButtonContainer helpBtnContainer;
	ButtonContainer startBtnContainer;
	ButtonContainer settingsBtnContainer;
	Panel mainPanel;
	Panel editMapPanel;
	Panel aboutPanel;
	Panel helpPanel;
	Panel startPanel;
	Panel settingsPanel;

	List<IFDrawTickAndInput> drawAndTickList = new LinkedList<>();

	private GuiDisplay guiDisplay;

	String buttonIds[] = {
			"Start Simulation", "Edit Map", "About", "Help", "Exit",
			"New Map", "Old Map", "Back",
			"Chaotic Traffic Simulator",
			"Back ",
			"Back  ",
			"Generate Map", "Load Map", "Back   ",
			"Settings",
			"Graphics", "Sound", "Back    "
	};

	String startBtn = "Simulation";
	String aboutBtn = "About";
	String helpBtn = "Help";
	// 4
	String exitBtn = "Exit";


	private final int BUTTON_WIDTH = 250;
	private final int BUTTON_HEIGHT = 50;
	private final int BUTTON_SPACING = 5;

	private final int BUTTON_CONTAINER_HIDDEN_X = -300;
	private final int BUTTON_CONTAINER_HIDDEN_Y = 160;

	private final int BUTTON_CONTAINER_VISSIBLE_X = 15;
	private final int BUTTON_CONTAINER_VISSIBLE_Y = 160;

	private final int PANEL_HIDDEN_X = 900;
	private final int PANEL_HIDDEN_Y = 160;

	private final int PANEL_VISSIBLE_X = 280;
	private final int PANEL_VISSIBLE_Y = 160;

	private int PANEL_WIDTH = 505;
	private int PANEL_HEIGHT = 430;

	private int PICTURE_WIDTH;
	private int PICTURE_HEIGHT;

	private int frameWidth;
	private int frameHeight;

	private int offSetWidth;
	private int offSetHeight;

	private double scale;

	BufferedImage menuBuffer;
	BufferedImage menuBuffer2;

	Graphics2D menuG;
	Graphics2D menuG2;

	private MainMenu(GuiDisplay gd, int width, int height, int widthF, int heightF){
		guiDisplay = gd;

		PICTURE_WIDTH = width;
		PICTURE_HEIGHT = height;
		frameWidth = widthF;
		frameHeight = heightF;

		menuBuffer = new BufferedImage(PICTURE_WIDTH, PICTURE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		menuBuffer2 = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_RGB);

		menuG = (Graphics2D) menuBuffer.getGraphics();
		menuG.setBackground(PublicConstants.BGColor);

		menuG2 = (Graphics2D) menuBuffer2.getGraphics();
		menuG2.setBackground(PublicConstants.highlightColor);

		resized(widthF, heightF);
		title = new Title(this);

		List<String> arguments = new LinkedList<>();
		arguments.add(startBtn);
		// arguments.add(buttonIdseditMapBtn);
		arguments.add(buttonIds[14]);
		arguments.add(aboutBtn);
		arguments.add(helpBtn);
		arguments.add(exitBtn);

		mainBtnContainer = new ButtonContainer(
				BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y, 
				BUTTON_WIDTH, BUTTON_HEIGHT, arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Vertical);
		mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
		drawAndTickList.add(mainBtnContainer);

		arguments.clear();
		arguments.add(buttonIds[9]);
		aboutBtnContainer = new ButtonContainer(
				BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y, 
				BUTTON_WIDTH, BUTTON_HEIGHT, arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Vertical);
		drawAndTickList.add(aboutBtnContainer);

		arguments.clear();
		arguments.add(buttonIds[10]);
		helpBtnContainer = new ButtonContainer(
				BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y, 
				BUTTON_WIDTH, BUTTON_HEIGHT, arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Vertical);
		drawAndTickList.add(helpBtnContainer);

		arguments.clear();
		arguments.add(buttonIds[11]);
		arguments.add(buttonIds[12]);
		arguments.add(buttonIds[13]);
		startBtnContainer = new ButtonContainer(
				BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y, 
				BUTTON_WIDTH, BUTTON_HEIGHT, arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Vertical);
		drawAndTickList.add(startBtnContainer);

		arguments.clear();
		arguments.add(buttonIds[15]);
		arguments.add(buttonIds[16]);
		arguments.add(buttonIds[17]);
		settingsBtnContainer = new ButtonContainer(
				BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y, 
				BUTTON_WIDTH, BUTTON_HEIGHT, arguments.size(), BUTTON_SPACING, 
				arguments, this, ButtonContainer.ButtonContainerType.Vertical);
		drawAndTickList.add(settingsBtnContainer);

		RelativeLocation lal[] = {new TextArea(new LinkedList<String>(Arrays.asList(
				"This is a traffic simulator made by four dudes named Andreas Löfman, Felix Bärring, Gustaf Ringius and Robert Wennergren. "
						+ "We would like to thank Painis Cupcake for not eating us and also praise the almighty Gaben."))
						,(int)PANEL_HIDDEN_X, (int)PANEL_HIDDEN_Y, 10, 30, PANEL_WIDTH-20, PANEL_HEIGHT-40)};

		mainPanel = new Panel(PANEL_HIDDEN_X, PANEL_HIDDEN_Y, PANEL_WIDTH, PANEL_HEIGHT, "Main Menu", lal);
		drawAndTickList.add(mainPanel);
		mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);

		RelativeLocation lal3[] = {new TextArea(
				new LinkedList<String>(Arrays.asList(
						"Simulates the chaos that one could find in traffic by spawning "
								+ "cars with personalities that can change based on environmental variables that the user "
								+ "controls and give snickers to the driver if they are getting too hungry and stressed and turn into a diva.",
								"",
								"This project uses and displays the concurrency of the world in a fun and chaotic way where chaos is actually "
										+ "seen as the objective to acheive by the user of the program. \n"
										+ "Music by Rune-Bertil's/Uncle & The Bacon"
						)),
						(int)PANEL_HIDDEN_X, (int)PANEL_HIDDEN_Y, 10, 30, PANEL_WIDTH-20, PANEL_HEIGHT-40)};

		aboutPanel = new Panel(PANEL_HIDDEN_X, PANEL_HIDDEN_Y, PANEL_WIDTH, PANEL_HEIGHT, "About", lal3);
		drawAndTickList.add(aboutPanel);

		RelativeLocation lal4[] = {new TextArea(
				new LinkedList<String>(Arrays.asList("If you need help you better send a mail to felixbarring@gmail.com or lofman.andreas@gmail.com")),
				(int)PANEL_HIDDEN_X, (int)PANEL_HIDDEN_Y, 10, 30, PANEL_WIDTH-20, PANEL_HEIGHT-40)};

		helpPanel = new Panel(PANEL_HIDDEN_X, PANEL_HIDDEN_Y, PANEL_WIDTH, PANEL_HEIGHT, "Help", lal4);
		drawAndTickList.add(helpPanel);

		RelativeLocation lal5[] = {new TextArea(
				new LinkedList<String>(Arrays.asList("Choose wheter you want to generate a new map or load an existing one")),
				(int)PANEL_HIDDEN_X, (int)PANEL_HIDDEN_Y, 10, 30, PANEL_WIDTH-20, PANEL_HEIGHT-40)};

		startPanel = new Panel(PANEL_HIDDEN_X, PANEL_HIDDEN_Y, PANEL_WIDTH, PANEL_HEIGHT, "Start Simulation", lal5);
		drawAndTickList.add(startPanel);

		RelativeLocation lal6[] = {new TextArea(
				new LinkedList<String>(Arrays.asList("The settings are in a future verison.")),
				(int)PANEL_HIDDEN_X, 
				(int)PANEL_HIDDEN_Y, 10, 30, PANEL_WIDTH-20, PANEL_HEIGHT-40)};

		settingsPanel = new Panel(PANEL_HIDDEN_X, PANEL_HIDDEN_Y, PANEL_WIDTH, PANEL_HEIGHT, "Settings", lal6);
		drawAndTickList.add(settingsPanel);

	}

	public static MainMenu createInstance(GuiDisplay gd, int width, int height, int widthF, int heightF) throws Exception{
		if(instance != null) throw new Exception("It is not allowed to call creatInstance more than once.");
		else{
			return (instance = new MainMenu(gd, width, height, widthF, heightF));
		}
	}

	@Override
	public void draw(Graphics2D g){
		menuG.clearRect(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT);
		title.draw(menuG);
		for(IFDrawTickAndInput d : drawAndTickList){
			d.draw(menuG);
		}
		g.drawImage(menuBuffer, offSetWidth, offSetHeight, (int)(PICTURE_WIDTH*scale), (int)(PICTURE_HEIGHT*scale), null);
	}

	@Override
	public void tick(){
		title.tick();
		for(IFDrawTickAndInput t : drawAndTickList){
			t.tick();
		}
	}

	@Override
	public void resized(int frameW, int frameH){
		frameWidth = frameW;
		frameHeight = frameH;
		double scaleWidth = (double)frameWidth/(double)PICTURE_WIDTH;
		double scaleHeight = (double)frameHeight/(double)PICTURE_HEIGHT; 
		if(scaleWidth < scaleHeight){
			scale = scaleWidth;
		} else {
			scale = scaleHeight;
		}
		offSetWidth = (int)((frameWidth-(PICTURE_WIDTH*scale))/2.0);
		offSetHeight = (int)((frameHeight-(PICTURE_HEIGHT*scale))/2.0);
	}

	@Override
	public void mouseClicked(int button, int xs, int ys){
		if(button == 1){
			// Only accept mouse clicks inside the actual view
			if(xs < offSetWidth || xs > PICTURE_WIDTH*scale+offSetWidth ||
					ys < offSetHeight || ys > PICTURE_HEIGHT*scale+offSetHeight ){
				return;
			}
			int x = (int)((xs-offSetWidth)/scale); 
			int y = (int)((ys-offSetHeight)/scale);

			for(IFDrawTickAndInput il : drawAndTickList){
				il.mouseClicked(button, x, y);
			}
		}
	}

	@Override
	public void mouseMoved(int xs, int ys){
		int x = (int)((xs-offSetWidth)/scale); 
		int y = (int)((ys-offSetHeight)/scale);

		for(IFDrawTickAndInput il : drawAndTickList){
			il.mouseMoved(x, y);
		}
	}

	@Override
	public void keyPressed(int key){}

	// Consider making the id an int instead
	@Override
	public void buttonPressed(String id){

		SoundController.getInstance().playButtonSound();

		if(id.equals(startBtn)){ // To Start
			mainBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			startBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			mainPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
			startPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
		} else if(id.equals(buttonIds[13])){ // Back from start
			mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			startBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
			startPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
		} 
		else if(id.equals(exitBtn)){ // Exit
			System.exit(0);
		} else if(id.equals(aboutBtn)){ // About
			mainBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			aboutBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			mainPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
			aboutPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
		} else if(id.equals(buttonIds[9])){ // Back from about
			mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			aboutBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
			aboutPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
		} else if(id.equals(helpBtn)){ // Help
			mainBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			helpBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			mainPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
			helpPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
		} else if(id.equals(buttonIds[10])){ // Back from help
			mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			helpBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
			helpPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
		} else if(id.equals(buttonIds[14])){ // Help
			mainBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			settingsBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			mainPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
			settingsPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
		} else if(id.equals(buttonIds[17])){ // Back from help
			mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			settingsBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
			settingsPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
		} else if(id.equals(buttonIds[11])){ // Generat Map
			guiDisplay.toggleMode();
			mainBtnContainer.setTargetVissible(BUTTON_CONTAINER_VISSIBLE_X, BUTTON_CONTAINER_VISSIBLE_Y);
			startBtnContainer.setTargetHidden(BUTTON_CONTAINER_HIDDEN_X, BUTTON_CONTAINER_HIDDEN_Y);
			mainPanel.setTargetVissible(PANEL_VISSIBLE_X, PANEL_VISSIBLE_Y);
			startPanel.setTargetHidden(PANEL_HIDDEN_X, PANEL_HIDDEN_Y);
		}
	}

}
