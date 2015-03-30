import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * Objekt tega razreda predstavlja grafi"cno platno.  ``Platno'' je skupen
 * izraz za risalno plo"s"co in pomnilni"sko sliko.  Podrazred naj
 * implementira metodo `narisi', ki je namenjena risanju na platno.
 */
public abstract class Platno {

    /** Vrne zaokro"zitev "stevila d na najbli"zje celo "stevilo. */
    public static int ri(double d) {
        return (int) Math.round(d);
    }

    /** Vrne zaokro"zitev "stevila d navzgor. */
    public static int ci(double d) {
        return (int) Math.round(d);
    }

    /** Vrne zaokro"zitev "stevila d navzdol. */
    public static int fi(double d) {
        return (int) Math.round(d);
    }

    /** privzeta "sirina platna, "ce velikost ni podana */
    private static final int PRIVZETA_SIRINA_PLATNA = 800;

    /** privzeta vi"sina platna, "ce velikost ni podana */
    private static final int PRIVZETA_VISINA_PLATNA = 600;

    /**
     * Objekt tega na"stevnega razreda je ena od konstant, ki predstavlja tip
     * platna:
     * <ul>
     * <li> ZASLON: risalna plo"s"ca
     * <li> PNG: pomnilni"ska slika v rastrskem formatu PNG
     * <li> EPS, PDF, SVG: pomnilni"ska slika v vektorskem formatu EPS, PDF ali SVG
     * </ul>
     */
    protected static enum Tip {
        ZASLON, PNG, EPS, PDF, SVG;

        // slovar za pretvorbo kon"cnice datoteke v objekt razreda Tip
        private static final Map<String, Tip> KONCNICA_2_TIP =
            new HashMap<String, Tip>() {{
                this.put("png", PNG);
                this.put("eps", EPS);
                this.put("pdf", PDF);
                this.put("svg", SVG);
            }};

        /** Vrne tip na podlagi kon"cnice datoteke. */
        public static Tip izKoncnice(String koncnica) {
            return KONCNICA_2_TIP.get(koncnica);
        }
    }

    /**
     * Nari"se sliko na platno this.  To metodo pokli"ce ogrodje, ko je "cas
     * zanjo.
     *
     * @param g objekt, s pomo"cjo katerega lahko ri"semo na platno
     * @param wPlatno "sirina platna (dejansko celo "stevilo, a zaradi
     *                enostavnosti pretvorjeno v double)
     * @param hPlatno vi"sina platna (dejansko celo "stevilo, a zaradi
     *                enostavnosti pretvorjeno v double)
     */
    protected abstract void narisi(Graphics2D g, double wPlatno, double hPlatno);

    /** Iz podanih argumentov ukazne vrstice izlu"s"ci parametre platna. */
    public void sproziRisanje(String[] args) {
        try {
            int wPlatno = PRIVZETA_SIRINA_PLATNA;
            int hPlatno = PRIVZETA_VISINA_PLATNA;
            Tip tipPlatna = Tip.ZASLON;
            File datoteka = null;

            // obdelaj argumente ukazne vrstice
            for (int i = 0;  i < args.length;  i++) {
                if (args[i].indexOf(".") >= 0) {
                    String[] imeKoncnica = args[i].split("\\.");
                    tipPlatna = Tip.izKoncnice(imeKoncnica[imeKoncnica.length - 1]);
                    if (tipPlatna == null) {
                        throw new RuntimeException();
                    }
                    datoteka = new File(args[i]);

                } else if (args[i].indexOf("x") >= 0) {
                    String[] wh = args[i].split("x");
                    wPlatno = Integer.parseInt(wh[0]);
                    hPlatno = Integer.parseInt(wh[1]);

                } else {
                    throw new RuntimeException();
                }
            }

            // V odvisnosti od tipa platna ustvari okno in plo"s"co oziroma
            // pomnilni"sko sliko.  V primeru izdelave pomnilni"ske slike
            // pokli"ci metodo `narisi' in shrani izdelano sliko v datoteko.
            // V primeru izdelave okna pa se bo metoda `narisi' poklicala
            // preko metode paintComponent risalne plo"s"ce.
            switch (tipPlatna) {
                case ZASLON:
                    this.ustvariOknoInPlosco(wPlatno, hPlatno);
                    break;

                case PNG:
                    this.izdelajPNG(wPlatno, hPlatno, datoteka);
                    break;

                case EPS:
                case PDF:
                case SVG:
                    this.izdelajVektorskoSliko(wPlatno, hPlatno, tipPlatna, datoteka);
                    break;

                default:
                    throw new RuntimeException();
            }

        } catch (RuntimeException ex) {
            // nekaj je narobe; izpi"si navodila za uporabo
            System.err.println("java ... [<slika>.{png|eps|pdf|svg}] [<sirina>x<visina>]");
            System.err.println();
            System.err.println("java ...");
            System.err.println("    sliko nariše v okno velikosti 800 krat 600");
            System.err.println();
            System.err.println("java ... 640x480");
            System.err.println("    sliko nariše v okno velikosti 640 krat 480");
            System.err.println();
            System.err.println("java ... primer.png");
            System.err.println("    izdela sliko primer.png velikosti 800 krat 600");
            System.err.println();
            System.err.println("java ... primer.png 640x480");
            System.err.println("    izdela sliko primer.png velikosti 640 krat 480");
        }
    }

    /**
     * Izdela pomnilni"sko sliko, pokli"ce metodo `narisi' in shrani
     * narisano v podano datoteko PNG.
     */
    private void izdelajPNG(int wSlika, int hSlika, File datoteka) {
        BufferedImage slika = new BufferedImage(wSlika, hSlika, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = slika.createGraphics();
        this.narisi(g, wSlika, hSlika);
        g.dispose();

        try {
            ImageIO.write(slika, "png", datoteka);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Izdela pomnilni"sko sliko, pokli"ce metodo `narisi' in shrani
     * narisano v podano datoteko EPS, PDF ali SVG.
     */
    private void izdelajVektorskoSliko(int wSlika, int hSlika, Tip tipPlatna, File datoteka) {
        VectorGraphics2D g = null;

        switch (tipPlatna) {
            case EPS:
                g = new EPSGraphics2D(0, 0, wSlika, hSlika);
                break;

            case PDF:
                g = new PDFGraphics2D(0, 0, wSlika, hSlika);
                break;

            case SVG:
                g = new SVGGraphics2D(0, 0, wSlika, hSlika);
                break;
        }
        this.narisi(g, wSlika, hSlika);

        try(FileWriter writer = new FileWriter(datoteka)) {
            writer.write(g.toString());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Ustvari in prika"ze okno in risalno plo"s"co.
     */
    private void ustvariOknoInPlosco(final int wOkno, final int hOkno) {

        // Swingove komponente lahko izdelujemo in posodabljamo samo takrat,
        // ko te"ce dogodkovna nit (EDT).  Metoda invokeLater po"caka, da ta
        // nit pride na vrsto.  Ko se to zgodi, izvede metodo run objekta tipa
        // Runnable, ki ga prejme kot parameter.

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // izdelaj okno
                JFrame okno = new JFrame("Moja umetnina");
                okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // okno postavi na sredino zaslona
                Dimension dZaslon = Toolkit.getDefaultToolkit().getScreenSize();
                int xOkno = (dZaslon.width - wOkno) / 2;
                int yOkno = (dZaslon.height - hOkno) / 2;
                okno.setBounds(xOkno, yOkno, wOkno, hOkno);

                // izdelaj plo"s"co in jo poka"zi na okno
                okno.setLayout(new BorderLayout());
                JPanel risalnaPlosca = Platno.this.new RisalnaPlosca();
                okno.add(risalnaPlosca, BorderLayout.CENTER);

                // prika"zi okno
                okno.setVisible(true);
            }
        });
    }

    /**
     * Objekt tega razreda predstavlja risalno plo"s"co.  Ker je razred
     * nestati"cen, njegov objekt ob izdelavi prejme referenco na objekt
     * razreda Platno, s katerim je bil izdelan.  Do tega objekta lahko
     * dostopamo z izrazom Platno.this.
     */
    private class RisalnaPlosca extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Platno.this.narisi((Graphics2D) g, this.getWidth(), this.getHeight());
        }
    }
}
