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

package sound;

import javax.sound.sampled.AudioFileFormat.Type;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public interface ISound {
    
    /**
     * Increases the volume of the Sound.
     * This works but different systems have different Master gain
     * which makes this throw exception if a too high or low value
     * was used.
     */
     void raiseVolume();
    
    /**
     * Decreases the volume of the Sound.
     * This works but different systems have different Master gain
     * which makes this throw exception if a too high or low value
     * was used.
     */
     void lowerVolume();
    
    /**
     * Method used to mute and also unmute the sound.
     */
     void mute();
     void playSound();
     void loopSound();
     void stopSound();
     void pauseSound();
     boolean isPlaying();
     boolean isLooping();
     Type[] supportedFileTypes();
    
}
