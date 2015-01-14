/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author hynra : http://hynra.com
 */
 public class Midlet extends MIDlet {
    Display d;
    MainScene gc;

    public void startApp() {
        d = Display.getDisplay(this);
        gc = new MainScene(this);
        d.setCurrent(gc);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }
}
