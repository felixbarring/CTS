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

package controller;

import java.util.ArrayList;
import java.util.List;

import sound.ISound;
import sound.Sound;


/**
 * Controller class that controls the Sound in the application
 * by using URL;s to different sound files and start and
 * stop different sounds.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class SoundController {
    
    private final static SoundController INSTANCE = new SoundController();
    private final List<ISound> sounds = new ArrayList<>();
    
    private SoundController(){
        sounds.add(Sound.newInstance("sweet.wav"));
        sounds.add(Sound.newInstance("afterburner.wav"));
        sounds.add(Sound.newInstance("button.wav"));
    }
    
    public static SoundController getInstance(){
        return INSTANCE;
    }
    
    public void startMenuMusic(){
        sounds.get(0).loopSound();
    }
    
    public void startSimulationMusic(){
        sounds.get(1).loopSound();
    }
    
    public void stopMenuMusic(){
        sounds.get(0).stopSound();
    }
    
    public void stopSimulationMusic(){
        sounds.get(1).stopSound();
    }
    
    public void pauseSimulationMusic(){
        sounds.get(1).pauseSound();
    }
    
    public void playButtonSound(){
        sounds.get(2).playSound();
    }
    /**
     * This mutes all the sounds in the application and 
     * unmutes if already muted.
     */
    public void muteAll(){
        for (ISound s : sounds){
            s.mute();
        }
    }
    /**
     * This mutes the music in the application and 
     * unmutes the music if already muted.
     */
    public void muteMusic(){
        sounds.get(0).mute();
        sounds.get(1).mute();
    }
}
