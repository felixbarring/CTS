
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

package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.image.BufferStrategy;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.jcip.annotations.*;
import controller.MainController;
import controller.SoundController;

/**
 * A main class for the GUI of the application.
 * @author Felix Bärring <felixbarring@gmairl.com>
 * 
 */

@ThreadSafe
final class GuiDisplay implements MouseListener, MouseMotionListener, KeyListener, ComponentListener{

	static {
		System.setProperty("sun.java2d.transaccel", "True"); 
		//System.setProperty("sun.java2d.trace", "timestamp,log,count");
		//System.setProperty("sun.java2d.opengl", "True");
		System.setProperty("sun.java2d.d3d", "True");
		System.setProperty("sun.java2d.ddforcevram", "True");
	}

	enum Mode{ Menu, Sim 	}

	enum EventType{ MousePressed, MouseMoved, KeyPressed, SwicthMode }

	@Immutable
	static final class Event{
		final EventType eventType;
		final MouseEvent mouseEvent;
		final KeyEvent keyEvent;

		Event(EventType et, MouseEvent me, KeyEvent ke){
			eventType = et;
			mouseEvent = me;
			keyEvent = ke;
		}
	}

	private BlockingQueue<Event> events = new LinkedBlockingQueue<>();

	private volatile boolean resized = false;

	public GuiDisplay(){ 	try {
		mainLoop();
	} catch (Exception e) {
		e.printStackTrace();
	} } 

	public static void main(String args[]){
		SoundController.getInstance().startMenuMusic();
		new GuiDisplay();
	}

	private DisplayMode getBestDisplayMode(GraphicsDevice device) {
		DisplayMode BEST_DISPLAY_MODES[] = device.getDisplayModes();
		for (int x = 0; x < BEST_DISPLAY_MODES.length; x++) {
			DisplayMode[] modes = device.getDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == BEST_DISPLAY_MODES[x].getWidth()
						&& modes[i].getHeight() == BEST_DISPLAY_MODES[x].getHeight()
						&& modes[i].getBitDepth() == BEST_DISPLAY_MODES[x].getBitDepth()
						) {
					return BEST_DISPLAY_MODES[x];
				}
			}
		}
		return null;
	}

	/*****************************************
	 *	All the resources are confined to one thread.
	 *	The event dispatch thread, or any other for that matter, cannot cause any harm.
	 *	When the EDT sends an event they will be stored in the thread safe 
	 *	LinkedBlockingQueue and later handled by the main thread running the main loop.
	 *	Thread safety is delegated to LinkedBlockingQueue.
	 * @throws Exception 
	 *****************************************/
	private void mainLoop() throws Exception {

		Mode mode = Mode.Menu;

		final int SMALLEST_ALLOWED_WIDTH = 400;
		final int SMALLEST_ALLOWED_HEIGHT = 300;

		int frameWidth = 800;
		int frameHeight = 600;

		Frame frame = new Frame();
		Canvas canvas = new Canvas();
		BufferStrategy bufferStrategy;

		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		//for (DisplayMode mode : device.getDisplayModes()){ System.out.println(mode.getWidth() + " " + mode.getHeight()); }
		if (PublicConstants.USE_FULLSCREEN){
			try {
				DisplayMode displayMode = getBestDisplayMode(device);
				frame.setUndecorated(true);
				frame.setIgnoreRepaint(true);
				frame.setResizable(false);
				device.setFullScreenWindow(frame);
				if (displayMode != null &&
						device.isDisplayChangeSupported()){
					device.setDisplayMode(displayMode);
					frameWidth = displayMode.getWidth();
					frameHeight = displayMode.getHeight();
				} else  {
					// MURE TO DU HERE
					System.out.println("Unable to find a good displayMode");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 

		canvas.setFocusable(true);
		canvas.setSize(frameWidth, frameHeight);

		frame.setSize(frameWidth, frameHeight);
		frame.setTitle("Chaotic Traffic Simulator");
		frame.setIgnoreRepaint(true);
		frame.setVisible(true);
		frame.add(canvas);
		frame.pack();

		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();

		canvas.setFocusable(true);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);

		frame.addComponentListener(this);


		IFViewMode mainMenu = MainMenu.createInstance(
				this, PublicConstants.PICTURE_WIDTH, PublicConstants.PICTURE_HEIGHT, frameWidth, frameHeight);
		IFViewMode simulation = new Simulation(
				this, PublicConstants.PICTURE_WIDTH, PublicConstants.PICTURE_HEIGHT, frameWidth, frameHeight);
		IFViewMode currentMode = mainMenu;

		final int DESIRED_LOOP_TIME = 10;

		mainMenu.resized(frameWidth, frameHeight);
		simulation.resized(frameWidth, frameHeight);

		while(true){
			long startTime = System.currentTimeMillis();
			if(resized){
				frameWidth = frame.getWidth();
				frameHeight = frame.getHeight();
				if(!PublicConstants.USE_FULLSCREEN){ // Dessa förbannade border som räknas med i storleken 
					frameHeight = frame.getHeight()-28;
				}
				if(frameWidth < SMALLEST_ALLOWED_WIDTH || frameHeight < SMALLEST_ALLOWED_HEIGHT){
					frameWidth = SMALLEST_ALLOWED_WIDTH;
					frameHeight = SMALLEST_ALLOWED_HEIGHT;
					if(!PublicConstants.USE_FULLSCREEN){ // Dessa förbannade border som räknas med i storleken 
						frameHeight = SMALLEST_ALLOWED_HEIGHT+28;
					}
					frame.setSize(frameWidth, frameHeight);
				}
				mainMenu.resized(frameWidth, frameHeight);
				simulation.resized(frameWidth, frameHeight);
			}

			Event event;
			while((event = events.poll()) != null){
				if(event.eventType.equals(EventType.SwicthMode)){
					if (mode.equals(Mode.Menu)){
						MainController.generatMap(frameWidth,frameHeight);
						currentMode = simulation;
						mode = Mode.Sim;
						SoundController.getInstance().stopMenuMusic();
						SoundController.getInstance().startSimulationMusic();
					} else {
						MainController.endSimulation();
						currentMode = mainMenu;
						mode = Mode.Menu;
						SoundController.getInstance().stopSimulationMusic();
						SoundController.getInstance().startMenuMusic();
					}
				} else if(event.eventType.equals(EventType.KeyPressed)){
					currentMode.keyPressed(event.keyEvent.getKeyCode());
				} else if(event.eventType.equals(EventType.MouseMoved)){
					currentMode.mouseMoved(event.mouseEvent.getX(), event.mouseEvent.getY());
				} else {
					currentMode.mouseClicked(event.mouseEvent.getButton(), 
							event.mouseEvent.getX(), event.mouseEvent.getY());
				}
			}

			currentMode.tick();

			Graphics2D buffer = (Graphics2D) bufferStrategy.getDrawGraphics();
			buffer.setPaint(Color.BLACK);

			buffer.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
			currentMode.draw(buffer);

			bufferStrategy.show();

			//System.out.println(System.currentTimeMillis()-startTime);
			long sleepTime = DESIRED_LOOP_TIME-(System.currentTimeMillis()-startTime);
			//System.out.println(" s "+sleepTime);
			if (sleepTime > 0){
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	void toggleMode(){
		events.add(new Event(EventType.SwicthMode, null, null));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public synchronized void mousePressed(MouseEvent e) {
		events.add(new Event(EventType.MousePressed, e, null));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public synchronized void mouseMoved(MouseEvent e) {
		events.add(new Event(EventType.MouseMoved, e, null));
	}

	@Override
	public synchronized void keyPressed(KeyEvent e) {
		events.add(new Event(EventType.KeyPressed, null, e));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		resized = true;
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}

}

