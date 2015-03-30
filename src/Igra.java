import java.awt.Point;
import java.util.Scanner;

import javax.swing.JFrame;


public class Igra {
	

	
	private static Kaca uporabiPredmet(Kaca kaca, int vrsta) {
		if(vrsta == 1) {
			kaca.trenutnaDolzina++;
		}
		if(vrsta == 2) {
			if(kaca.smer == 0) {
				kaca.smer = 3;
			}
			else {
				kaca.smer--;
			}
		}
		if(vrsta == 3) {
			if(kaca.smer == 3) {
				kaca.smer = 0;
			}
			else {
				kaca.smer++;
			}
		}
		return kaca;
	}
	
	private static Kaca premakniKaco(Kaca kaca) {
		if(kaca.smer == 0) {
			kaca.stolpec++;
		}
		else if(kaca.smer == 1) {
			kaca.vrstica++;
		}
		else if(kaca.smer == 2) {
			kaca.stolpec--;
		}
		else{
			kaca.vrstica--;
		}
		return kaca;
	}
	
	
	
	
	private static Kaca dolzinaKace(Predmet[] tabela, int steviloKorakov) {
		Kaca kaca = new Kaca(0,0,1,0);
		boolean jePojedla = false;
		Point e = new Point(kaca.stolpec, kaca.vrstica);
		kaca.pozicije.add(e);
		boolean jeZrasla = false;
		
		
		while(steviloKorakov > 0) {
			jePojedla = false;
			jeZrasla = false;
			
			Predmet uporabljen = new Predmet(0,0,0);
			
			for(int i = 0; i < tabela.length; i++) {
				if(tabela[i].x == kaca.stolpec && tabela[i].y == kaca.vrstica) {
					uporabljen = tabela[i];
					jePojedla = true;
					break;
				}
			}
			
			if(jePojedla) {
				kaca = uporabiPredmet(kaca, uporabljen.vrsta);
				if(uporabljen.vrsta == 1) {
					jeZrasla = true;
				}
			}
			
			kaca = premakniKaco(kaca);
			
			
			if(kaca.pozicije.size() > 4) {
				for(int x = 0; x < kaca.pozicije.size(); x++) {
					if(kaca.pozicije.get(x).x == kaca.stolpec && kaca.pozicije.get(x).y == kaca.vrstica) {
						kaca.trenutnaDolzina = 0;
						steviloKorakov = 0;
					}
				}
			}
			
			if(jeZrasla) {
				Point tocka = new Point(kaca.stolpec, kaca.vrstica);
				kaca.pozicije.add(tocka);
			}
			else {
				kaca.pozicije.remove(0);
				Point tocka = new Point(kaca.stolpec, kaca.vrstica);
				kaca.pozicije.add(tocka);
			}
			steviloKorakov--;
			
			
		}
		return kaca;
	}
	

	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int steviloPredmetov = sc.nextInt();
		Predmet tabela[] = new Predmet[steviloPredmetov];
		
		for(int i = 0; i < steviloPredmetov; i++) {
			Predmet trenutn = new Predmet(sc.nextInt(), sc.nextInt(), sc.nextInt());
			tabela[i] = trenutn;
		}
		
		int steviloKorakov = sc.nextInt();
		Kaca kaca = dolzinaKace(tabela,steviloKorakov);
		System.out.println(kaca.trenutnaDolzina);
		sc.close();
		
		

		
		kaca.sproziRisanje(args);
	}

}
