package vector;

import matrix.IMatrix;

public class VectorMatrixView extends AbstractVector {
	
	private IMatrix original;
	private int dimension;
	private boolean rowMatrix;
	
	public VectorMatrixView(IMatrix original) {
		if (original.getRowsCount() != 1 && original.getColsCount() != 1) {
			throw new IllegalArgumentException();
		}
		this.original = original;
		if (original.getRowsCount() == 1){
			this.rowMatrix = true;
			this.dimension = original.getColsCount();
		} else {
			this.rowMatrix = false;
			this.dimension = original.getRowsCount();
		}
	}

	@Override
	public double get(int index) {
		if (index < 0 || index >= this.getDimension()) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}
		if (rowMatrix) {
			return original.get(0, index);
		}
		return original.get(index, 0);
	}

	@Override
	public IVector set(int index, double value) {
		if (index < 0 || index >= this.getDimension()) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}
		if (rowMatrix) {
			original.set(0, index, value);
		}
		original.set(index, 0, value);
		return this;
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() {
		return new VectorMatrixView(original.copy());
	}

	@Override
	public IVector newInstance(int dimension) {
		double[] vector = new double[dimension];
		return new Vector(vector);
	}

}
