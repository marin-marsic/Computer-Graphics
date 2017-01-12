package LAB1;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;

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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

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
public class Main {

	static {
		GLProfile.initSingleton();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {

			int[] textures = new int[1];
			Texture texture;

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

						GL2 gl = arg0.getGL().getGL2();
						File f = new File("bla.png");
						
						try {
				            InputStream stream = getClass().getResourceAsStream("smoke.bmp");
				            texture = TextureIO.newTexture(stream, false, "bmp");
				        }
				        catch (IOException exc) {
				            exc.printStackTrace();
				            System.exit(1);
				        }
						
//						byte[] fileContent = null;
//						try {
//							fileContent = Files.readAllBytes(f.toPath());
//						} catch (IOException e) {
//							e.printStackTrace();
//							System.exit(0);
//						}
//
//						ByteBuffer buf = ByteBuffer.wrap(fileContent);
//						System.out.println(buf.capacity());
//						gl.glGenTextures(1, textures, 0);
//						gl.glBindTexture(GL.GL_TEXTURE_2D, textures[0]);
//
//						gl.glTexImage2D(textures[0], 0, GL.GL_RGB, 70, 54, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buf);
//
						gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
						gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
						gl.glEnable(GL.GL_TEXTURE_2D);
						
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
						glu.gluLookAt(0, 10, -30, 0, 0, 40, 0.0f, 1.0f, 0.0f);
						gl2.glPointSize(1.0f);

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
						
						gl2.glDepthMask(false);
						gl2.glEnable(GL.GL_BLEND); 
						gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

						// crtanje kontrolnog poligona
//						gl2.glBindTexture(GL.GL_TEXTURE_2D, texture);
						texture.enable(gl2);
						texture.bind(gl2);
						gl2.glColor4d(1, 1, 1, 0.5);
						gl2.glBegin(GL2.GL_QUADS);
				        // Front Face
				        gl2.glTexCoord2f(0.0f, 0.0f);
				        gl2.glVertex3f(-1.0f, -1.0f, 1.0f);
				        gl2.glTexCoord2f(1.0f, 0.0f);
				        gl2.glVertex3f(1.0f, -1.0f, 1.0f);
				        gl2.glTexCoord2f(1.0f, 1.0f);
				        gl2.glVertex3f(1.0f, 1.0f, 1.0f);
				        gl2.glTexCoord2f(0.0f, 1.0f);
				        gl2.glVertex3f(-1.0f, 1.0f, 1.0f);
						gl2.glEnd();
						
						gl2.glColor4d(1, 1, 1, 0.2);
						gl2.glBegin(GL2.GL_QUADS);
				        // Front Face
				        gl2.glTexCoord2f(0.0f, 0.0f);
				        gl2.glVertex3f(-0.5f, -0.5f, 1.0f);
				        gl2.glTexCoord2f(1.0f, 0.0f);
				        gl2.glVertex3f(1.5f, -0.5f, 1.0f);
				        gl2.glTexCoord2f(1.0f, 1.0f);
				        gl2.glVertex3f(1.5f, 1.5f, 1.0f);
				        gl2.glTexCoord2f(0.0f, 1.0f);
				        gl2.glVertex3f(-0.5f, 1.5f, 1.0f);
						gl2.glEnd();
						
						gl2.glDisable(GL.GL_BLEND); 
						gl2.glDepthMask(true);
						

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
