package LAB1;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

/**
 * 
 * Program za prvu laboratorijsku vježbu iz Računalne grafike 2016./2017.<br>
 * <br>
 * 
 * Upute:<br>
 * 
 * <li>Tipka 'P' pokrece animaciju.</li>
 * <li>Tipka 'W' pomice objekt naprijed po krivulji.</li>
 * <li>Tipka 'S' pomice objekt unazad po krivulji.</li>
 * <li>Tipka 'T' pali/gasi iscrtavanje tangente na krivulju.</li>
 * <li>Tipka 'O' pali/gasi iscrtavanje koordinatnogsustava objekta.</li>
 * <li>Tipka 'K' pali/gasi iscrtavanje kontrolnog poligona.</li><br>
 * 
 * @author marin
 *
 */
public class Main2 {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapatibilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapatibilities);
				ObjectModel model = new ObjectModel(840, 760);
				model.incijaliziraj(args[0], args[1]);

				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_W) {
							model.pomakniObjektNaprijed();
							glcanvas.display();
						} else if (e.getKeyCode() == KeyEvent.VK_S) {
							model.pomakniObjektNazad();
							glcanvas.display();
						} else if (e.getKeyCode() == KeyEvent.VK_T) {
							model.crtanjeTangenti = !model.crtanjeTangenti;
							glcanvas.display();
						} else if (e.getKeyCode() == KeyEvent.VK_P) {
							int size = model.bSpline.size();
							for (int i = 0; i < size; i++) {
								model.pomakniObjektNaprijed();
								glcanvas.display();
							}
						} else if (e.getKeyCode() == KeyEvent.VK_K) {
							model.crtanjeKontrolnogPoligona = !model.crtanjeKontrolnogPoligona;
							glcanvas.display();
						} else if (e.getKeyCode() == KeyEvent.VK_O) {
							model.crtanjeKoordinatnogSustava = !model.crtanjeKoordinatnogSustava;
							glcanvas.display();
						}
					}
				});

				// Reagiranje na promjenu velicine platna
				glcanvas.addGLEventListener(new GLEventListener() {

					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x, int y, int width, int height) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						model.width = width;
						model.height = height;
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();
						gl2.glFrustum(-0.8f, 0.8f, -0.8f, 0.8f, 1.5f, 100f);

						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						gl2.glViewport(0, 0, width, height);
					}

					@Override
					public void init(GLAutoDrawable arg0) {

					}

					@Override
					public void dispose(GLAutoDrawable arg0) {
					}

					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glEnable(GL.GL_DEPTH_TEST);
						gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

						gl2.glLoadIdentity();
						GLU glu = new GLU();
						glu.gluLookAt(-15, -15, 60, 0, 0, 40, 0.0f, 1.0f, 0.0f);
						gl2.glPointSize(1.0f);

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

						// crtanje krivulje
						gl2.glColor3f(1.0f, 0.0f, 1.0f);
						gl2.glBegin(GL.GL_LINE_STRIP);
						for (Tocka t : model.bSpline) {
							gl2.glVertex3d(t.x, t.y, t.z);
						}
						gl2.glEnd();

						// crtanje tangenti
						if (model.crtanjeTangenti) {
							gl2.glColor3f(0.0f, 1.0f, 0.5f);
							gl2.glBegin(GL.GL_LINES);
							for (Tangenta t : model.tangenteZaCrtanje) {
								gl2.glVertex3d(t.t1.x, t.t1.y, t.t1.z);
								gl2.glVertex3d(t.t2.x, t.t2.y, t.t2.z);
							}
							gl2.glEnd();
						}

						// crtanje kontrolnog poligona
						if (model.crtanjeKontrolnogPoligona) {
							gl2.glColor3f(0.0f, 1.0f, 0.0f);
							gl2.glBegin(GL.GL_LINE_STRIP);
							for (Tocka t : model.kontrolneTocke) {
								gl2.glVertex3d(t.x, t.y, t.z);
							}
							gl2.glEnd();
						}

						// sjencanje
						gl2.glEnable(GL2.GL_LIGHTING);
						gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] { 0.0f, 0.0f, 0.0f, 1.0f }, 0);

						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] { 10f, 0f, 55f, 1f }, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] { 0.2f, 0.2f, 0.2f, 1f }, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] { 0.8f, 0.8f, 0f, 1f }, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] { 0f, 0f, 0f, 1f }, 0);
						gl2.glEnable(GL2.GL_LIGHT0);

						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[] { 1f, 1f, 1f, 1f }, 0);
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] { 1f, 1f, 1f, 1f }, 0);
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] { 0.01f, 0.01f, 0.01f, 1f }, 0);
						gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);

						gl2.glShadeModel(GL2.GL_SMOOTH);

						// crtanje objekta
						gl2.glColor3f(0.0f, 1.0f, 0.0f);
						Tocka trenutna = model.bSpline.get(model.trenutnaTocka);
						gl2.glTranslated(trenutna.x, trenutna.y, trenutna.z);
						gl2.glRotated(model.kutRotacije, model.osRotacije.x, model.osRotacije.y, model.osRotacije.z);
						for (Trokut t : model.trokuti) {

							gl2.glBegin(GL.GL_TRIANGLES);
							Tocka vrh1 = model.vrhovi.get(t.v1);
							gl2.glNormal3d(vrh1.normala.get(0), vrh1.normala.get(1), vrh1.normala.get(2));
							gl2.glVertex3d(vrh1.x, vrh1.y, vrh1.z);
							// System.out.println(vrh1.x + " " + vrh1.y + " " +
							// vrh1.z);

							Tocka vrh2 = model.vrhovi.get(t.v2);
							gl2.glNormal3d(vrh2.normala.get(0), vrh2.normala.get(1), vrh2.normala.get(2));
							gl2.glVertex3d(vrh2.x, vrh2.y, vrh2.z);
							// System.out.println(vrh1.x + " " + vrh1.y + " " +
							// vrh1.z);

							Tocka vrh3 = model.vrhovi.get(t.v3);
							gl2.glNormal3d(vrh3.normala.get(0), vrh3.normala.get(1), vrh3.normala.get(2));
							gl2.glVertex3d(vrh3.x, vrh3.y, vrh3.z);
							// System.out.println(vrh1.x + " " + vrh1.y + " " +
							// vrh1.z);
							// System.out.println("==============================================");
							gl2.glEnd();
						}

						gl2.glDisable(GL2.GL_LIGHTING);

						// crtanje koordinatnog sustava objekta
						if (model.crtanjeKoordinatnogSustava) {
							gl2.glBegin(GL.GL_LINES);
							Tocka so = model.sredisteObjekta;

							gl2.glColor3f(1.0f, 0.0f, 0.0f);
							gl2.glVertex3d(so.x, so.y, so.z);
							gl2.glVertex3d(so.x + 5, so.y, so.z);

							gl2.glColor3f(0.0f, 1.0f, 0.0f);
							gl2.glVertex3d(so.x, so.y, so.z);
							gl2.glVertex3d(so.x, so.y + 5, so.z);

							gl2.glColor3f(0.0f, 0.0f, 1.0f);
							gl2.glVertex3d(so.x, so.y, so.z);
							gl2.glVertex3d(so.x, so.y, so.z + 5);

							gl2.glEnd();
						}

					}

				});

				final JFrame jframe = new JFrame("RG Labos - kretanje objekta po krivulji");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						jframe.dispose();
						System.exit(0);
					}
				});

				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(model.width, model.height);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}

		});
	}

}
