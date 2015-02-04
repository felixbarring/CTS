/*
* The MIT License
*
* Copyright 2014 Gustaf Ringius <Gustaf@linux.com>, Felix Bärring <felixbarring@gmail.com>
* Andreas Löfman <lofman.andreas@gmail.com>,  Robert Wennergren <whoisregor@gmail.com>
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class handling all audio processing and control.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class Sound implements ISound{
    
    private Clip clip;
    private FloatControl gainControl;
    private boolean looping = false;
    
    private Sound(String fileName){
        clip = null;
        try {
            clip = AudioSystem.getClip();
            InputStream audioSrc = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioInputStream convertedInputStream = convert(audioStream);
            clip.open(convertedInputStream);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, "No free lines are available", ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, "The file type is not supported", ex);
        } catch (IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, "There was an IO exception", ex);
        }
    }
    
    /**
     * Need this somehow to work with windows.
     *
     * Converts from alaw that doesn't work on stupid windows
     * into PCM that must work on windows.
     *
     * @param stream is the stream to convert encoding of.
     */
    private AudioInputStream convert(AudioInputStream stream){
        AudioFormat format = stream.getFormat();
        AudioInputStream convertedInputStream = stream;
        if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
            AudioFormat tmp = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    format.getSampleSizeInBits() * 2,
                    format.getChannels(),
                    format.getFrameSize() * 2,
                    format.getFrameRate(), true);
            convertedInputStream = AudioSystem.getAudioInputStream(tmp,stream);
        }
        return convertedInputStream;
    }
    
    /**
     * This will return a new soundInstance for the
     * sound file from the parameter filePath and
     * will create a thread for this filePath
     *
     * @param filePath to the sound clip that should be played.
     * @return a running thread with the sound clip
     */
    public static ISound newInstance(String filePath){
        return new Sound(filePath);
    }
    
    @Override
    public void playSound() {
        stopSound();
        clip.start();
    }
    
    @Override
    public void loopSound() {
        if (clip.isRunning()){
            clip.stop();
        }
        looping = true;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    @Override
    public void stopSound() {
        if (clip.isRunning()){
            if (looping){
                looping = false;
            }
            clip.stop();
        }
        clip.setFramePosition(0);
    }
    
    @Override
    public void pauseSound() {
        if (clip.isRunning()){
            clip.stop();
        } else {
            if (looping){
                loopSound();
            } else{
                clip.start();
            }
        }
    }
    
    @Override
    public boolean isPlaying() {
        return clip.isRunning();
    }
    
    @Override
    public boolean isLooping() {
        return looping;
    }
    
    @Override
    public Type[] supportedFileTypes() {
        return AudioSystem.getAudioFileTypes();
    }
    
    @Override
    public void mute() {
        if (clip != null ){
            BooleanControl muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            if (muteControl != null){
                if (muteControl.getValue()){
                    muteControl.setValue(false);
                } else{
                muteControl.setValue(true);
                }
            }
        }
    }
    
    @Override
    public void raiseVolume() {
        if (clip != null){
            FloatControl current = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            current.setValue(+10.0f);
        }
    }
    
    @Override
    public void lowerVolume() {
        if (clip != null){
            FloatControl current = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            current.setValue(-10.0f);
        }
    }
    
}
