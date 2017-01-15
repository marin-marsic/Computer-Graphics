package LAB1;

import vector.IVector;
import vector.Vector;

public class Trokut {
	
	public int v1;
	public int v2;
	public int v3;
	
	public IVector normala;

	public Trokut(int v1, int v2, int v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	public void postaviNormalu(Tocka v1, Tocka v2, Tocka v3) {
		double[] vec1elements = { v2.x - v1.x, v2.y - v1.y, v2.z - v1.z };
		IVector vector1 = new Vector(vec1elements);
		vector1 = vector1.nNormalize();

		double[] vec2elements = { v3.x - v1.x, v3.y - v1.y, v3.z - v1.z };
		IVector vector2 = new Vector(vec2elements);
		vector2 = vector2.nNormalize();

		normala = vector1.nVectorProduct(vector2);
		try {
			normala = normala.nNormalize();
		} catch (Exception e) {
		}

	}

}
