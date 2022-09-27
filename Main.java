import sum.komponenten.*;
import sum.ereignis.*;
import sum.werkzeuge.*;
import sum.kern.Maus;
import sum.kern.Tastatur;


public class Main{

    protected Maus mouse;
    protected Bildschirm screen;
    protected Buntstift pen;
    protected Tastatur keyboard;

    public Main(){

        mouse = new Maus();
        screen = new Bildschirm();
        pen = new Buntstift();
        keyboard = new Tastatur();
        main();
    }
    
    private void main(){
       
        pen.setzeLinienBreite(10);
        loop();
    }

    private void loop(){
        while(true){
            pen.bewegeBis(mouse.hPosition(), mouse.vPosition());
            if(mouse.doppelKlick()){
                screen.loescheAlles();
            }else if(mouse.istGedrueckt()){
                pen.runter();
            }else{
                pen.hoch();
            }

        }
    }
}