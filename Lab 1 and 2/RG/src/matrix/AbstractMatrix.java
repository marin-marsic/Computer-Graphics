package matrix;

import vector.IVector;
import vector.VectorMatrixView;

public abstract class AbstractMatrix implements IMatrix{
	
	public AbstractMatrix() {}

	@Override
	public abstract int getRowsCount();

	@Override
	public abstract int getColsCount();

	@Override
	public abstract double get(int row, int col);

	@Override
	public abstract IMatrix set(int row, int col, double value);

	@Override
	public abstract IMatrix copy();

	@Override
	public abstract IMatrix newInstance(int rows, int cols);

	@Override
	public IMatrix nTranspose(boolean liveView) {
		if (liveView){
			return new MatrixTransponseView(this);
		}
		return new MatrixTransponseView(this.copy());
	}

	@Override
	public IMatrix add(IMatrix other) {
		if (this.getColsCount() != other.getColsCount()) {
			throw new IllegalArgumentException();
		}
		if (this.getRowsCount() != other.getRowsCount()) {
			throw new IllegalArgumentException();
		}
		for (int i = this.getRowsCount() - 1; i >= 0; i--) {
			for (int j = this.getColsCount() - 1; j >= 0; j--) {
				this.set(i, j, this.get(i, j) + other.get(i, j));
			}
		}
		return this;
	}

	@Override
	public IMatrix nAdd(IMatrix other) {
		return this.copy().add(other);
	}

	@Override
	public IMatrix sub(IMatrix other) {
		if (this.getColsCount() != other.getColsCount()) {
			throw new IllegalArgumentException();
		}
		if (this.getRowsCount() != other.getRowsCount()) {
			throw new IllegalArgumentException();
		}
		for (int i = this.getRowsCount() - 1; i >= 0; i--) {
			for (int j = this.getColsCount() - 1; j >= 0; j--) {
				this.set(i, j, this.get(i, j) - other.get(i, j));
			}
		}
		return this;
	}

	@Override
	public IMatrix nSub(IMatrix other) {
		return this.copy().sub(other);
	}

	@Override
	public IMatrix nMultiply(IMatrix other) {
		if (this.getColsCount() != other.getRowsCount()) {
			throw new IllegalArgumentException();
		}
		
		int rows = this.getRowsCount();
		int cols = other.getColsCount();
		double[][] result = new double[rows][cols];
		
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++) {
				for (int k = 0; k < this.getColsCount(); k++){
					result[i][j] += this.get(i, k) * other.get(k, j);
				}
			}
		}
		return new Matrix(rows, cols, result, true);
	}

	@Override
	public double determinant() {
		if (this.getRowsCount() != this.getColsCount()) {
			throw new IllegalArgumentException();
		}
		double[][] matrix = this.toArray();
		return recursiveDeterminant(matrix);
	}
	
	// algoritam preuzet s http://professorjava.weebly.com/matrix-determinant.html
	private double recursiveDeterminant(double[][] matrix) {
		double sum = 0;
		int s;
		if (matrix.length == 1) {
			return (matrix[0][0]);
		}

		for (int i = 0; i < matrix.length; i++) {

			double[][] smaller = new double[matrix.length - 1][matrix.length - 1];
			for (int a = 1; a < matrix.length; a++) {
				for (int b = 0; b < matrix.length; b++) {
					if (b < i) {
						smaller[a - 1][b] = matrix[a][b];
					} else if (b > i) {
						smaller[a - 1][b - 1] = matrix[a][b];
					}
				}
			}
			if (i % 2 == 0) {
				s = 1;
			} else {
				s = -1;
			}
			sum += s * matrix[0][i] * (recursiveDeterminant(smaller));
		}
		return (sum);
	}

	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		if (liveView){
			return new MatrixSubMatrixView(this, row, col);
		}
		return new MatrixSubMatrixView(this.copy(), row, col);
	}

	@Override
	public IMatrix nInvert() {
		int minus = 1;
		double determinant = Math.abs(this.determinant());
		if (Math.abs(determinant - 0) < 0.000001) {
			throw new IllegalArgumentException();
		}	    
		
		IMatrix newMatrix = this.copy();
	    for (int i = 0; i < newMatrix.getRowsCount(); i++) {
	        for (int j = 0; j < newMatrix.getColsCount(); j++) {
	        	if (i % 2 == j % 2){
	        		minus = 1;
	        	} else {
	        		minus = -1;
	        	}
	        	newMatrix.set(i, j, minus * this.subMatrix(i, j, false).determinant());
	        }
	    }

		for (int i = newMatrix.getRowsCount() - 1; i >= 0; i--) {
			for (int j = newMatrix.getColsCount() - 1; j >= 0; j--) {
				newMatrix.set(i, j, newMatrix.get(i, j) * (1 / determinant));
			}
		}
		return newMatrix.nTranspose(true);
	}

	@Override
	public double[][] toArray() {
		double[][] matrix = new double[this.getRowsCount()][this.getColsCount()];
		for (int i = this.getRowsCount() - 1; i >= 0; i--) {
			for (int j = this.getColsCount() - 1; j >= 0; j--) {
				matrix[i][j] = this.get(i, j);
			}
		}
		return matrix;
	}

	@Override
	public IVector toVector(boolean liveView) {
		if (liveView){
			if (getRowsCount() == 1 || getColsCount() == 1){
				return new VectorMatrixView(this);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		if (getRowsCount() == 1 || getColsCount() == 1){
			return new VectorMatrixView(this.copy());
		}
		else {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < this.getRowsCount(); i++) {
			for (int j = 0; j < this.getColsCount(); j++){
				sb.append(String.format("%.3f", this.get(i, j)));
				if (j < this.getRowsCount() - 1){
					sb.append(" ");
				}
			}
			if (i < this.getColsCount() - 1){
				sb.append(" | ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
