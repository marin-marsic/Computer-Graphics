package LAB2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import LAB1.Tocka;
import LAB1.Trokut;
import vector.IVector;
import vector.Vector;

public class Model {

	int width;
	int height;

	ArrayList<Particle> particles = new ArrayList<>();
	Source source = new Source();
	double creationTime = 200;
	double lastCreted = 0;
	int maxToCreate = 11;
	int minToCreate = 3;
	
	Tocka ociste = new Tocka(0, 80, 0.1, 1);
	
	double timeStart;
	double timeOld = System.currentTimeMillis();

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

	public Model(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void update() {
		// novo vrijeme
		double timeNew = System.currentTimeMillis();
		double time = timeNew - timeOld;
		
		int numOfParticles = particles.size();
		for (int i = 0; i < numOfParticles; i++) {
			
			// ako je čestici isteklo vrijeme života, ukloni ju
			Particle p = particles.get(i);
			if (p.lifeTime < timeNew - p.createdAt) {
				particles.remove(i);
				i--;
				numOfParticles--;
				continue;
			}
			
			// inače updateaj česticu
			else {
				p.x += p.speedX*time;
				p.y += p.speedY*time;
				p.z += p.speedZ*time;
				p.transparency = Math.pow((1 - ((timeNew - p.createdAt) / p.lifeTime)), 2) * p.startTtransparency;
				p.size = 0.5 + ((timeNew - p.createdAt) / p.lifeTime);
				
				// rotacija
				Tocka s = new Tocka(0, 1, 0, 1);
				Tocka e = new Tocka(0, 0, 0, 1);
				e.x = p.x - ociste.x;
				e.y = p.y - ociste.y;
				e.z = p.z - ociste.z;
				
				p.osX = s.y*e.z - e.y*s.z;
				p.osY = -(s.x*e.z - e.x*s.z);
				p.osZ = s.x*e.y - s.y*e.x;
				
				double se = e.x*s.x + e.y*s.y + e.z*s.z;
				double absS = Math.sqrt(s.x*s.x + s.y*s.y + s.z*s.z);
				double absE = Math.sqrt(e.x*e.x + e.y*e.y + e.z*e.z);
				
				// kut izmedu pocetne i ciljne orijentacije
				p.kut = Math.toDegrees(Math.acos(se / (absE * absS)));
			}
		}
		
		// kreiraj nove čestice
		if (timeNew - lastCreted > creationTime) {
			Random rnd = new Random();
			lastCreted = timeNew;
			int toCreate = rnd.nextInt(maxToCreate - minToCreate) + minToCreate;
			for (int i = 0; i < toCreate; i++) {
				
				// početna točka čestice - unutar kružnice promjera 1 (dimnjak)
				Particle p = new Particle();
				p.x = 0.5 - rnd.nextDouble();
				p.y = 0.5 - rnd.nextDouble();
				p.z = 0;
				
				// brzina čestice
				p.speedX = p.x * 0.25 * source.speed;
				p.speedY = p.y * 0.25 * source.speed;
				p.speedZ = -1 * source.speed;
				
				// životni vijek čestice je 0.5 - 20.5 sekundi
				p.lifeTime = (rnd.nextDouble() *20 + 0.5) * 1000;
				
				particles.add(p);
			}
		}
			
		timeOld = timeNew;
	}
	
	

}
