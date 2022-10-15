import sum.komponenten.Radioknopf;

// Extends Radioknopf for overriding click method
public class ModeOption extends Radioknopf {

  byte mode;

  public ModeOption(final double pLinks,
                    final double pOben,
                    final double pBreite,
                    final double pHoehe,
                    final String pAufschrift,
                    final byte newMode) {
    super(pLinks, pOben, pBreite, pHoehe, pAufschrift);
    mode = newMode;
  }
  @Override
  protected void knopfGeklickt() {
    Main.pickMode(mode);
  }
}
