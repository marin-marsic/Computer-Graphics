package LAB2;

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

import LAB1.Tocka;
import LAB1.Trokut;

/**
 * 
 * Program za drugu laboratorijsku vježbu iz Računalne grafike 2016./2017.<br>
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

			Texture texture;

			@Override
			public void run() {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapatibilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapatibilities);
				Model model = new Model(840, 760);
				

				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_S) {
							while (true) {
								if (e.getKeyCode() == KeyEvent.VK_W) {
									
								} else if (e.getKeyCode() == KeyEvent.VK_A) {
									
								} else if (e.getKeyCode() == KeyEvent.VK_S) {
									
								} else if (e.getKeyCode() == KeyEvent.VK_D) {
									
								} else if (e.getKeyCode() == KeyEvent.VK_P) {
									break;
								}
								model.update();
								glcanvas.display();
							}
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
						gl2.glFrustum(-0.8f, 0.8f, -0.8f, 0.8f, 1.5f, 1000f);

						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();
						gl2.glViewport(0, 0, width, height);
					}
					
					@Override
					public void init(GLAutoDrawable arg0) {

						try {
				            InputStream stream = getClass().getResourceAsStream("smoke.gif");
				            texture = TextureIO.newTexture(stream, false, "gif");
				        }
				        catch (IOException exc) {
				            exc.printStackTrace();
				            System.exit(1);
				        }
						
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
						Tocka o = model.ociste;
						glu.gluLookAt(o.x, o.y, o.z, 0, 0, 0, 0.0f, 1.0f, 0.0f);
						gl2.glPointSize(1.0f);

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
						
						gl2.glDepthMask(false);
						gl2.glEnable(GL.GL_BLEND); 
						gl2.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

						// crtanje kontrolnog poligona
						texture.enable(gl2);
						texture.bind(gl2);
						gl2.glBegin(GL2.GL_QUADS);
						for (Particle p : model.particles) {
//							gl2.glPushMatrix();
//							gl2.glTranslated(p.x, p.y, p.z);
//							gl2.glRotated(p.kut, p.osX, p.osY, p.osZ);
							gl2.glColor4d(p.transparency*2 + 0.5, 0.4*(1-p.transparency), 0.1*(1-p.transparency), p.transparency);
							
					        gl2.glTexCoord2f(0.0f, 0.0f);
					        double d = p.size;
					        gl2.glVertex3d(p.x-d, 0, p.z-d);
//					        gl2.glVertex3d(-d, 0, -d);
					        
					        gl2.glTexCoord2f(1.0f, 0.0f);
					        gl2.glVertex3d(p.x+d, 0, p.z-d);
//					        gl2.glVertex3d(d, 0, -d);
					        
					        gl2.glTexCoord2f(1.0f, 1.0f);
					        gl2.glVertex3d(p.x+d, 0, p.z+0.6);
//					        gl2.glVertex3d(d, 0, d);
					        
					        gl2.glTexCoord2f(0.0f, 1.0f);
					        gl2.glVertex3d(p.x-d, 0, p.z+d);
//					        gl2.glVertex3d(-d, 0, d);
					        
//					        gl2.glRotated(-p.kut, p.osX, p.osY, p.osZ);
//					    	gl2.glTranslated(-p.x, -p.y, -p.z);
//					        gl2.glPopMatrix();;
						}
						gl2.glEnd();
						gl2.glDisable(GL.GL_BLEND); 
						gl2.glDepthMask(true);
						texture.disable(gl2);
						
						gl2.glBegin(GL2.GL_QUADS);
						gl2.glColor4f(0.5f, 0.5f, 0.5f, 1f);
						
				        gl2.glVertex3d(-1, 0, -0.5);
				        gl2.glVertex3d(1, 0, -0.5);
				        gl2.glVertex3d(1, 0, 13);
				        gl2.glVertex3d(-1, 0, 13);
				        
				        gl2.glVertex3d(-1, 0, 8);
				        gl2.glVertex3d(-11, 0, 8);
				        gl2.glVertex3d(-11, 0, 13);
				        gl2.glVertex3d(-1, 0, 13);
				        
						gl2.glEnd();
						
						gl2.glBegin(GL2.GL_TRIANGLES);
						gl2.glColor4f(0.5f, 0.5f, 0.5f, 1f);
						
				        gl2.glVertex3d(-11, 0, 8);
				        gl2.glVertex3d(-11, 0, 6);
				        gl2.glVertex3d(-6, 0, 8);
				        
				        gl2.glVertex3d(-6, 0, 8);
				        gl2.glVertex3d(-6, 0, 6);
				        gl2.glVertex3d(-1, 0, 8);
				        
						gl2.glEnd();
						
						
						

					}

				});

				final JFrame jframe = new JFrame("RG Labos - čestice dima");
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
