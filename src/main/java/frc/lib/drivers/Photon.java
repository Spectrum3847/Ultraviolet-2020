package frc.lib.drivers;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;



/**
 * Used to command a Teensy running the photon LED control library.
 * Commands the Photon library are just strings of comma seperated numbers with a semicolon at the end
 * 2 = SetAnimation=  2, StripNumber, Animation, Color1, Color2, rate, fade; 
 *              You can leave off values and defaults will be set for the rest of the values
 * 3 = SetNumberOfLeds = 3, StripNumber, NumberOfLEDsInStrip
 *  
 * Color Chart = 
    Red (0..)
    Orange (32..)
    Yellow (64..)
    Green (96..)
    Aqua (128..)
    Blue (160..)
    Purple (192..)
    Pink(224..)
    White(255) - This isn't normally on the HUE chart but used in this library to make animaitons easier
 * @author Spectrum3847
 */
public class Photon {

    private SerialPort usb;

    //Set your default values here. You can set default colors that match your team colors, refer to the Color chart Above
    private int kDefaultAnimation = 4;
    private int kDefaultColor1 = 192;
    private int kDeafultColor2 = 255;
    private int kDefaultRate = 2;
    private int kDefaultFade = 150;


    private int kAnimation = kDefaultAnimation;
    private int kColor1 = kDefaultColor1;
    private int kColor2 = kDeafultColor2;
    private int kRate = kDefaultRate;
    private int kFade = kDefaultFade;

    public static enum Color
	{
        RED,ORANGE,YELLOW,GREEN,AQUA,BLUE,PURPLE,PINK,WHITE;
    }

    public static enum Animation
    {
        OFF, SOLID, SOLID_DUAL, BLINK, BLINK_DUAL, SIREN,
        PULSE_TO_BLACK, PULSE_TO_WHITE, FADE_ALTERNATE, PULSE_DUAL,
        CYLON, CYLON_DUAL, BOUNCE_BAR, BOUNCE_BAR_DUAL, CYLON_MIDDLE, CYLON_MIDDLE_DUAL,
        TRACER, TRACER_ALTERNATE, WIPE_FWD, WIPE_REV, WIPE_FWD_REV, WIPE_DOWN, WIPE_UP_DOWN, WIPE_UP_DOWN_DUAL,
        WIPE_OUT, WIPE_IN, WIPE_IN_OUT, WIPE_IN_OUT_BACK,
        PERCENTAGE, RAINBOW, JUGGLE, SPARKLES
        ;
    }

    public Photon(){
        try{
            usb = new SerialPort(115200, Port.kUSB);
        } catch(Exception e){
            //Robot.printWarning("PHOTON NOT FOUND: NO LEDs :(");
            usb = null;
        }
    }

    //USe this constructor to setup default colors when you construct it
    public Photon(int Color1, int Color2){
        this();
        kDefaultColor1 = Color1;
        kDeafultColor2 = Color2;
    }

    //Build out more construcitons to set default colors, etc

    public void writeString(String s){
        if (usb != null){
            usb.writeString(s);
        }
    }

    //Set how many LEDs are in one of the strips, call this for each strip that you want to use.
    public void SetNumberOfLEDs(int StripNum, int LEDcount){
        writeString("3," + StripNum +","+ LEDcount + ";");
    }

    //Set a new animation on a strip, must include a strip number all other values are optional and can be set seperatly.
    public void setAnimation(int StripNum, Integer... vals){//int Animation, int Color1, int Color2, int rate, int fade){
        Integer Animation = vals.length > 0 ? vals[0] : this.kAnimation;
        Integer Color1 = vals.length > 1 ? vals[1] : this.kColor1;
        Integer Color2 = vals.length > 2 ? vals[2] : this.kColor2;
        Integer Rate = vals.length > 3 ? vals[3] : this.kRate;
        Integer Fade = vals.length > 4 ? vals[4] : this.kFade;
        writeString("2," + StripNum +","+ Animation +","+ Color1 +","+ Color2 +","+ Rate +","+ Fade + ";");
    }

    public void setAnimation(int StripNum, Animation a, Color c1, Color c2, int rate, int fade){
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2), rate, fade);
    }

    public void setAnimation(int StripNum, Animation a, Color c1, Color c2, int rate){
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2), rate);
    }   

    public void setAnimation(int StripNum, Animation a, Color c1, Color c2){
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2));
    } 

    public void setAnimation(int StripNum, Animation a, Color c1){
        setAnimation(StripNum, getAnimation(a), getColor(c1));
    } 

    public void setAnimation(int StripNum, Animation a, Color c1, int rate, int fade){ //Allows only a single color to be set
        setAnimation(StripNum, getAnimation(a), getColor(c1), kColor2, rate, fade);
    } 

    public void setAnimation(int StripNum, Animation a, Color c1, int rate){ //Allows only a single color to be set but you can adjust the rate
        setAnimation(StripNum, getAnimation(a), getColor(c1), kColor2, rate);
    } 

    public void setAnimation(int StripNum, Animation a){
        setAnimation(StripNum, getAnimation(a));
    } 
    
    //Reset all the values to your default values, useful if you want an animation to use default values, but they may have been already changed.
    public void resetValue(){
        kAnimation = kDefaultAnimation;
        kColor1 = kDefaultColor1;
        kColor2 = kDeafultColor2;
        kRate = kDefaultRate;
        kFade = kDefaultFade;
    }

    public int getColor(Color c){
        switch (c) {
            case RED:
                return 0;
            case ORANGE:
                return 32;
            case YELLOW:
                return 64;
            case GREEN:
                return 96;
            case AQUA:
                return 128;
            case BLUE:
                return 160;
            case PURPLE:
                return 192;
            case PINK:
                return 224;
            case WHITE:
                return 255;
            default:
                return 0; //Default to RED
        }
    }

    public int getAnimation(Animation a){
        switch(a){
            case OFF:
                return 0;
            case SOLID:
                return 1;
            case SOLID_DUAL:
                return 2;
            case BLINK:
                return 3;
            case BLINK_DUAL:
                return 4;
            case SIREN:
                return 5;
            case PULSE_TO_BLACK:
                return 10;
            case PULSE_TO_WHITE:
                return 11;
            case FADE_ALTERNATE:
                return 12;
            case PULSE_DUAL:
                return 13;
            case CYLON:
                return 20;
            case CYLON_DUAL:
                return 21;
            case BOUNCE_BAR:
                return 22;
            case BOUNCE_BAR_DUAL:
                return 23;
            case CYLON_MIDDLE:
                return 24;
            case CYLON_MIDDLE_DUAL:
                return 25;
            case TRACER:
                return 30;
            case TRACER_ALTERNATE:
                return 31;
            case WIPE_FWD:
                return 32;
            case WIPE_REV:
                return 33;
            case WIPE_FWD_REV:
                return 34;
            case WIPE_DOWN:
                return 35;
            case WIPE_UP_DOWN:
                return 36;
            case WIPE_UP_DOWN_DUAL:
                return 37;
            case WIPE_OUT:
                return 40;
            case WIPE_IN:
                return 41;
            case WIPE_IN_OUT:
                return 42;
            case WIPE_IN_OUT_BACK:
                return 43;
            case RAINBOW:
                return 96;
            case PERCENTAGE:
                return 97;
            case JUGGLE:
                return 98;
            case SPARKLES:
                return 99;
            default:
                return 3; //Default to Blink
        }
    }

    
    public void setColor1(int c){
        kColor1 = c;
    }

    public void setColor1(Color c){
        kColor1 = getColor(c);
    }

    public void setColor2(int c){
        kColor2 = c;
    }

    public void setColor2(Color c){
        kColor2 = getColor(c);
    }
    
    public void setAnimation(int a){
        kAnimation = a;
    }

    public void setAnimation(Animation a){
        kAnimation = getAnimation(a);
    }

    public void setRate(int r){
        kRate = r;
    }

    public void setFade(int f){
        kFade = f;
    }
}
