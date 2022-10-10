import java.awt.Color;
import sum.komponenten.Radioknopf;

public class ColorOption extends Radioknopf {
  Color color;

  public ColorOption(final double pLinks,
                     final double pOben,
                     final double pBreite,
                     final double pHoehe,
                     final String pAufschrift,
                     Color newColor) {
    super(pLinks, pOben, pBreite, pHoehe, pAufschrift);

    color = newColor;
  }

  @Override
  protected void knopfGeklickt() {
    Main.pickColor(color);
  }
}
