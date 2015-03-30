import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class Kaca extends Platno{

	public int stolpec;
	public int vrstica;
	public int trenutnaDolzina;
	public int smer;
	public List<Point> pozicije = new ArrayList<Point>();

	public static int ri(double d) {
		return (int) Math.round(d);
	}

	public Kaca(int vrstica, int stolpec, int trenutnaDolzina, int smer) {
		this.stolpec = stolpec;
		this.vrstica = vrstica;
		this.trenutnaDolzina = trenutnaDolzina;
		this.smer = smer;
		this.pozicije = pozicije;
	}

	public void narisi(Graphics2D g, double wp, double hp) {
		double krajsaStranica = Math.min(wp, hp);
		double x = (wp - krajsaStranica) / 2;
		double y = (hp - krajsaStranica) / 2;
		double steviloKvadratkov = 40;
		double enota = krajsaStranica / steviloKvadratkov;

		g.setColor(Color.lightGray);
		for (int i = 0; i < steviloKvadratkov; i++) {
			for (int a = 0; a < steviloKvadratkov; a++) {
				g.drawLine(ri(x), ri(y + enota), ri(x + enota), ri(y + enota));
				g.drawLine(ri(x + enota), ri(y + enota), ri(x + enota), ri(y));
				x += enota;
			}
			x = (wp - krajsaStranica) / 2;
			y += enota;
		}
		x = (wp - krajsaStranica) / 2;
		y = (hp - krajsaStranica) / 2;
		g.setColor(Color.RED);
		g.drawRect(ri(x), ri(y), ri(krajsaStranica), ri(krajsaStranica));
		g.setColor(Color.PINK);

		for (int i = 0; i < pozicije.size(); i++) {
			Point e = pozicije.get(i);
			double sredinaKvadratka = enota / 5;
			if (i == pozicije.size() - 1) {
				g.setColor(Color.BLUE);
			}
			g.fillArc(ri(x + sredinaKvadratka + (e.getX() + 5) * enota), ri(y
					+ sredinaKvadratka + (e.getY() + 5) * enota),
					ri(enota / 1.5), ri(enota / 1.5), 0, 360);
		}

	}

}
