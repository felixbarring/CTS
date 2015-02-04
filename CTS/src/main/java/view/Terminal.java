
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

/**
 * @author Felix Bärring <felixbarring@gmail.com>
 */

package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import controller.MainController;
import controller.SoundController;

public class Terminal extends TargetMoveItem {

	public final int WIDTH;
	public final int HEIGHT; 

	public final int BUTTON_WIDTH = 100;
	public final int BUTTON_HEIGHT = 50;

	BufferedImage backGround;

	TextArea textArea;
	InputTextArea input;

	Button button;

	Trie trie = new Trie();

	// Commands
	private final String COMMANDS = "> COMMANDS";
	private final String SPAWNSPEEDSLOW = "> SPAWNSPEEDSLOW";
	private final String SPAWNSPEEDNORMAL = "> SPAWNSPEEDNORMAL";
	private final String SPAWNSPEEDFAST = "> SPAWNSPEEDFAST";
	private final String SPAWNSPEEDLUDICROUS = "> SPAWNSPEEDLUDICROUS";
	private final String CLEARCARS = "> CLEARCARS";
	private final String CLOSE = "> CLOSE";
	private final String SET_YELLOW = "> YELLOWLIGHTS";
	private final String REM_YELLOW = "> REMYELLOWLIGHTS";
	private final String TOGGLE_SOUND = "> TOGGLESOUND";

	private final int SLOW_SPAWN = 50;
	private final int NORMAL_SPAWN = 5;
	private final int FAST_SPAWN = 3;
	private final int LUDICROUS_SPAWN = 0;

	private boolean isActive = false;

	public Terminal(double x, double y, int w, int h, IFViewMode vm){

		speed = 10;
		xLocation = x;
		yLocation = y;
		WIDTH = w;
		HEIGHT = h;

		textArea = new TextArea(
				new LinkedList<String>(Arrays.asList("")),
				10, 10, 10, 10, WIDTH-20, HEIGHT-(20+BUTTON_HEIGHT+5+50+15));

		movableAndDrawable.add(textArea);

		input = new InputTextArea(
				new LinkedList<String>(Arrays.asList("> ")),
				10, HEIGHT-(20+BUTTON_HEIGHT+5+50), 10, HEIGHT-(20+BUTTON_HEIGHT+5+50),
				WIDTH-20, 50, this);
		movableAndDrawable.add(input);

		button = new Button(
				WIDTH-(10+BUTTON_WIDTH), HEIGHT-(20+BUTTON_HEIGHT)+10,
				WIDTH-(10+BUTTON_WIDTH), HEIGHT-(20+BUTTON_HEIGHT)+10, "Close", BUTTON_WIDTH, BUTTON_HEIGHT
				);
		button.addListener(vm);
		movableAndDrawable.add(button);

		backGround = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) backGround.getGraphics();
		g.setColor(PublicConstants.normalColor);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(PublicConstants.highlightColor);
		g.fillRect(5, 5, WIDTH-10, HEIGHT-10);
		g.dispose();

		trie.addString(COMMANDS);
		trie.addString(SPAWNSPEEDSLOW);
		trie.addString(SPAWNSPEEDNORMAL);
		trie.addString(SPAWNSPEEDFAST);
		trie.addString(SPAWNSPEEDLUDICROUS);
		trie.addString(CLEARCARS);
		trie.addString(CLOSE);
		trie.addString(SET_YELLOW);
		trie.addString(REM_YELLOW);
		trie.addString(TOGGLE_SOUND);

		clear();

	}

	@Override
	public void draw(Graphics2D g){
		g.drawImage(backGround, (int)xLocation, (int)yLocation, null);
		super.draw(g);
	}

	@Override
	public void keyPressed(int key){
		if(isActive){
			super.keyPressed(key);
		}
	}

	public void toggleActive(){
		isActive = !isActive;
	}

	// Used by InputTextArea
	public void stringCreated(String str) {
		if(trie.stringExists(str)){
			textArea.addString("Executing command: "+str);
			if(str.equals(COMMANDS)){
				for(String s : trie.strings){
					textArea.addString(s);
				}
			} else if(str.equals(SPAWNSPEEDSLOW)){
				MainController.setSpawnDelay(SLOW_SPAWN);
			} else if(str.equals(SPAWNSPEEDNORMAL)){
				MainController.setSpawnDelay(NORMAL_SPAWN);
			} else if(str.equals(SPAWNSPEEDFAST)){
				MainController.setSpawnDelay(FAST_SPAWN);
			} else if(str.equals(SPAWNSPEEDLUDICROUS)){
				MainController.setSpawnDelay(LUDICROUS_SPAWN);
			} else if(str.equals(CLEARCARS)){
				MainController.clear();
			} else if(str.equals(CLOSE)){
				button.activate();
			}

			else if(str.equals(CLEARCARS)){
				MainController.clear();
			} else if(str.equals(SET_YELLOW)){
				MainController.setLightsYellow();
			} else if(str.equals(REM_YELLOW)){
				MainController.removeYellowLights();
			} else if(str.equals(TOGGLE_SOUND)){
				SoundController.getInstance().muteMusic();
			}else if(str.equals(SPAWNSPEEDLUDICROUS)){
				MainController.setSpawnDelay(0);
			}
		} else {
			textArea.addString("Unknown Command: "+str);
		}
	}

	// Used by InputTextArea
	public void completeSequence(String s){
		String str = trie.getFirstWordWithSequense(s);
		if(str != null){
			input.replaceStringBuilder(str);
		}
	}

	public void clear(){
		textArea.clear();
		textArea.addString("> This is a terminal."); 
		textArea.addString("> Type COMMANDS to see all commands that are available");
		input.replaceStringBuilder("> ");
	}

	private final static class Trie {

		TrieNode empty = new TrieNode('\n', false, null);

		List<String> strings = new ArrayList<>();

		void addString(String s){
			strings.add(s);
			char[] chars = s.toCharArray();
			TrieNode dummy = empty;
			for(int i = 0; i+1 < chars.length; i++){
				dummy = dummy.addNode(chars[i], false);
			}
			dummy.addNode(chars[chars.length-1], true);
		}

		boolean stringExists(String s){
			char[] chars = s.toCharArray();
			TrieNode dummy = empty;
			int i;
			for(i = 0; i < chars.length && (dummy = dummy.getChildWithChar(chars[i])) != null; i++);
			return i == chars.length && dummy.endCharacther;
		}

		String getFirstWordWithSequense(String s){
			char[] chars = s.toCharArray();
			TrieNode dummy = empty;
			TrieNode dummy2;
			int i;
			for(i = 0; i < chars.length && (dummy = dummy.getChildWithChar(chars[i])) != null; i++);
			if(i == chars.length){
				while ((dummy2 = dummy.getSingelChild()) != null){
					if(dummy2.endCharacther){
						dummy = dummy2;
						break;
					}
					dummy = dummy2;
				}
				return dummy.buildStringToTop();
			}
			return null;
		}

		private final static class TrieNode{
			char c;
			boolean endCharacther;
			TrieNode previous;
			List<TrieNode> children = new ArrayList<>();

			TrieNode(char ch, boolean end, TrieNode prev){
				c = ch;
				endCharacther = end;
				previous = prev;
			}

			// Optimize this with a sorted list...
			TrieNode addNode(char ch, boolean end){
				// Check if it already exists
				for(TrieNode tn : children){
					if(tn.c == ch){
						return tn;
					}
				}
				TrieNode newNode = new TrieNode(ch, end, this);
				children.add(newNode);
				return newNode;
			}

			// Search for it
			TrieNode getChildWithChar(char c){
				for(TrieNode child : children){
					if(child.c == c){
						return child;
					}
				}
				return null;
			}

			TrieNode getSingelChild(){
				if(children.size() == 1){
					return children.get(0);
				} else {
					return null;
				}
			}

			String buildStringToTop(){
				StringBuilder sb = new StringBuilder();
				TrieNode dummy = this;
				sb.append(c);
				while((dummy = dummy.previous).c != '\n'){
					sb.append(dummy.c);
				}
				sb.reverse();
				return sb.toString();
			}

		}

	}

}

