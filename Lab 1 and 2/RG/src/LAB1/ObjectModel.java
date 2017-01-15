package LAB1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import vector.IVector;
import vector.Vector;

public class ObjectModel {

	int width;
	int height;

	ArrayList<Tocka> kontrolneTocke = new ArrayList<>();
	ArrayList<Tocka> bSpline = new ArrayList<>();
	ArrayList<Tangenta> tangenteZaCrtanje = new ArrayList<>();
	ArrayList<Tocka> tangente = new ArrayList<>();
	ArrayList<Tocka> vrhovi = new ArrayList<>();
	ArrayList<Trokut> trokuti = new ArrayList<>();
	ArrayList<Tocka> vrhoviTranslatirani = new ArrayList<>();
	Tocka sredisteObjekta = new Tocka(0, 0, 0, 1);
	Tocka osRotacije;
	double kutRotacije;
	int brojSegmenata;
	int trenutnaTocka = 0;
	Tocka ociste;
	Tocka glediste;
	
	boolean crtanjeTangenti = false;
	boolean crtanjeKontrolnogPoligona = false;
	boolean crtanjeKoordinatnogSustava = false;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ObjectModel(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void ucitajObjekt(String fileName) {

		File file = new File(fileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("v")) {
					String[] lineSeparated = line.split("\\s+");
					double x = Double.parseDouble(lineSeparated[1]);
					double y = Double.parseDouble(lineSeparated[2]);
					double z = Double.parseDouble(lineSeparated[3]);
					Tocka t = new Tocka(x, y, z, 1);
					vrhovi.add(t);
				} else if (line.startsWith("f")) {
					String[] lineSeparated = line.split("\\s+");
					int v1 = Integer.parseInt(lineSeparated[1]) - 1;
					int v2 = Integer.parseInt(lineSeparated[2]) - 1;
					int v3 = Integer.parseInt(lineSeparated[3]) - 1;
					Trokut t = new Trokut(v1, v2, v3);
					trokuti.add(t);
				} else {
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Datoteka nije pronađena.");
		} catch (IOException e) {
			System.err.println("Greška prilikom čitanja iz datoteke.");
		}
		
	}

	public void ucitajKontrolneTocke(String fileName) {
		File file = new File(fileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				String[] lineSeparated = line.split("\\s+");
				double x = Double.parseDouble(lineSeparated[0]);
				double y = Double.parseDouble(lineSeparated[1]);
				double z = Double.parseDouble(lineSeparated[2]);
				Tocka t = new Tocka(x, y, z, 1);
				kontrolneTocke.add(t);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Datoteka nije pronađena.");
		} catch (IOException e) {
			System.err.println("Greška prilikom čitanja iz datoteke.");
		}

		brojSegmenata = kontrolneTocke.size() - 3;
	}

	public void normalize() {
		double xmin, xmax, ymin, ymax, zmin, zmax;
		xmin = xmax = vrhovi.get(0).x;
		ymin = ymax = vrhovi.get(0).y;
		zmin = zmax = vrhovi.get(0).z;

		// pronalazak minimalnih i maxmialnih vrijednosti
		int vrhoviSize = vrhovi.size();
		for (int i = 1; i < vrhoviSize; i++) {
			Tocka t = vrhovi.get(i);
			if (xmin > t.x) {
				xmin = t.x;
			}
			if (xmax < t.x) {
				xmax = t.x;
			}
			if (ymin > t.y) {
				ymin = t.y;
			}
			if (ymax < t.y) {
				ymax = t.y;
			}
			if (zmin > t.z) {
				zmin = t.z;
			}
			if (zmax < t.z) {
				zmax = t.z;
			}
		}

		// središte objekta
		double xS, yS, zS;
		xS = (xmin + xmax) / 2;
		yS = (ymin + ymax) / 2;
		zS = (zmin + zmax) / 2;
		
		sredisteObjekta.x = xS;
		sredisteObjekta.y = yS;
		sredisteObjekta.z = zS;

		// maksimalni raspon
		double M = Math.max(xmax - xmin, ymax - ymin);
		M = Math.max(M, zmax - zmin);

		// translacija i skaliranje vrhova
		for (Tocka t : vrhovi) {
			t.x = t.x - xS;
			t.x = (t.x * 5) / (M);

			t.y = t.y - yS;
			t.y = (t.y * 5) / (M);

			t.z = t.z - zS;
			t.z = (t.z * 5) / (M);
		}
	}

	public void incijaliziraj(String object_filename, String bSpline_filename) {
		ucitajKontrolneTocke(bSpline_filename);
		izracunajTockeKrivulje();
		ucitajObjekt(object_filename);
		normalize();
		izracunajNormaleTrokuta();
		izracunajNormaleVrhova();
		pomakniObjektNaprijed();
		pomakniObjektNazad();
	}

	public void pomakniObjektNazad() {
		izracunajKutIOsRotacije();
		trenutnaTocka += bSpline.size() - 1;
		trenutnaTocka %= bSpline.size();
	}

	public void pomakniObjektNaprijed() {
		izracunajKutIOsRotacije();
		trenutnaTocka++;
		trenutnaTocka %= bSpline.size();
	}

	private void izracunajTockeKrivulje() {
		for (int i = 0; i < brojSegmenata; i++) {

			// tocke koje odreduju kontrolni segment
			Tocka r1 = kontrolneTocke.get(i);
			Tocka r2 = kontrolneTocke.get(i + 1);
			Tocka r3 = kontrolneTocke.get(i + 2);
			Tocka r4 = kontrolneTocke.get(i + 3);

			// parametar t mijenjamo od 0 do 1 u sto koraka
			for (int j = 0; j < 100; j++) {
				double t = j / 100.0;

				// tocke krivulje
				double f1 = (-Math.pow(t, 3) + 3 * Math.pow(t, 2) - 3 * t + 1) / 6.0;
				double f2 = (3 * Math.pow(t, 3) - 6 * Math.pow(t, 2) + 4) / 6.0;
				double f3 = (-3 * Math.pow(t, 3) + 3 * Math.pow(t, 2) + 3 * t + 1) / 6.0;
				double f4 = (Math.pow(t, 3)) / 6.0;

				double x = f1 * r1.x + f2 * r2.x + f3 * r3.x + f4 * r4.x;
				double y = f1 * r1.y + f2 * r2.y + f3 * r3.y + f4 * r4.y;
				double z = f1 * r1.z + f2 * r2.z + f3 * r3.z + f4 * r4.z;

				Tocka p = new Tocka(x, y, z, 1);
				bSpline.add(p);

				// vektori tangenti na krivulju za crtanje
				if (j == 0 || (j == 99 && i == brojSegmenata - 1)) {

					double d1 = (-Math.pow(t, 2) + 2 * t - 1) / 2.0;
					double d2 = (3 * Math.pow(t, 2) - 4 * t) / 2.0;
					double d3 = (-3 * Math.pow(t, 2) + 2 * t + 1) / 2.0;
					double d4 = (Math.pow(t, 2)) / 2.0;

					double xd = d1 * r1.x + d2 * r2.x + d3 * r3.x + d4 * r4.x;
					double yd = d1 * r1.y + d2 * r2.y + d3 * r3.y + d4 * r4.y;
					double zd = d1 * r1.z + d2 * r2.z + d3 * r3.z + d4 * r4.z;

					Tocka t1 = new Tocka(p.x - xd / 3, p.y - yd / 3, p.z - zd / 3, 1);
					Tocka t2 = new Tocka(p.x + xd / 3, p.y + yd / 3, p.z + zd / 3, 1);

					tangenteZaCrtanje.add(new Tangenta(t1, t2));
				}

				// vektori tangenti na krivulju
				double d1 = (-Math.pow(t, 2) + 2 * t - 1) / 2.0;
				double d2 = (3 * Math.pow(t, 2) - 4 * t) / 2.0;
				double d3 = (-3 * Math.pow(t, 2) + 2 * t + 1) / 2.0;
				double d4 = (Math.pow(t, 2)) / 2.0;

				double xd = d1 * r1.x + d2 * r2.x + d3 * r3.x + d4 * r4.x;
				double yd = d1 * r1.y + d2 * r2.y + d3 * r3.y + d4 * r4.y;
				double zd = d1 * r1.z + d2 * r2.z + d3 * r3.z + d4 * r4.z;

				tangente.add(new Tocka(xd, yd, zd, 1));

			}
		}
	}

	public void izracunajKutIOsRotacije() {

		Tocka e = tangente.get(trenutnaTocka);
		// vektorski umnozak s x e, s = (0, 0, 1)
		Tocka s = new Tocka(0, 0, 1, 1);
		
		double osx = s.y*e.z - e.y*s.z;
		double osy = -(s.x*e.z - e.x*s.z);
		double osz = s.x*e.y - s.y*e.x;
		osRotacije = new Tocka(osx, osy, osz, 1);
		
		double se = e.x*s.x + e.y*s.y + e.z*s.z;
		double absS = Math.sqrt(s.x*s.x + s.y*s.y + s.z*s.z);
		double absE = Math.sqrt(e.x*e.x + e.y*e.y + e.z*e.z);
		
		// kut izmedu pocetne i ciljne orijentacije
		kutRotacije = Math.toDegrees(Math.acos(se / (absE * absS)));
	}
	
	public void izracunajNormaleTrokuta() {
		for (Trokut t : trokuti) {
			Tocka v1 = vrhovi.get(t.v1);
			Tocka v2 = vrhovi.get(t.v2);
			Tocka v3 = vrhovi.get(t.v3);
			
			t.postaviNormalu(v1, v2, v3);
		}
	}
	
	public void izracunajNormaleVrhova() {
		int size = vrhovi.size();
		for (int i = 0; i < size; i++) {
			Tocka v = vrhovi.get(i);
			
			// odredi bridove koji dijele ovaj vrh
			IVector n = new Vector(new double[] {0, 0, 0});
			int broj = 0;
			
			int tSize = trokuti.size();
			for (Trokut t : trokuti) {
				if (t.v1 == i || t.v2 == i || t.v3 == i){
					broj++;
					n = n.add(t.normala);
				}
			}
			
			try {
				n = n.normalize();
			} catch (Exception e) {
			}
			v.normala = n;
		}
	}

}
