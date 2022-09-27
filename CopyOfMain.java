import sum.komponenten.*;
import sum.ereignis.*;
import sum.werkzeuge.*;
import sum.kern.Maus;
import sum.kern.Tastatur;


public class CopyOfMain{

    protected Maus mouse;
    protected Bildschirm screen;
    protected Buntstift pen;
    protected Knopf button;
    protected Tastatur keyboard;

    public CopyOfMain(){

        mouse = new Maus();
        screen = new Bildschirm(900, 900);
        pen = new Buntstift();
        keyboard = new Tastatur();
        button = new Knopf(100, 450, 10, 10, "Hello World");
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
