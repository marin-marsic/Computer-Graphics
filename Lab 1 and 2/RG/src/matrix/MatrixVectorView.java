package matrix;

import vector.IVector;

public class MatrixVectorView extends AbstractMatrix{
	
	private IVector original;
	private boolean asRowMatrix;

	public MatrixVectorView(IVector original, boolean asRowMatrix) {
		this.original = original;
		this.asRowMatrix = asRowMatrix;
	}
	
	@Override
	public int getRowsCount() {
		if (asRowMatrix) {
			return 1;
		}
		return original.getDimension();
	}

	@Override
	public int getColsCount() {
		if (asRowMatrix) {
			return original.getDimension();
		}
		return 1;
	}

	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= this.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= this.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (asRowMatrix) {
			return original.get(col);
		}
		return original.get(row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		if (row < 0 || row >= this.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= this.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (asRowMatrix) {
			original.set(col, value);
		}
		original.set(row, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixVectorView(original.copy(), asRowMatrix);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}

}
