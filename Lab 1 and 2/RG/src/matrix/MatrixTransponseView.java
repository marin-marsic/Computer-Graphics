package matrix;

public class MatrixTransponseView extends AbstractMatrix{
	
	private IMatrix original;
	
	public MatrixTransponseView(IMatrix original) {
		this.original = original;
	}

	@Override
	public int getRowsCount() {
		return original.getColsCount();
	}

	@Override
	public int getColsCount() {
		return original.getRowsCount();
	}

	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= original.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= original.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		return original.get(col, row);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		if (row < 0 || row >= original.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= original.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		original.set(col, row, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixTransponseView(original.copy());
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixTransponseView(new Matrix(cols, rows));
	}

}
