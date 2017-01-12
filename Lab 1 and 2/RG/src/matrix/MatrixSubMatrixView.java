package matrix;

import java.util.ArrayList;

public class MatrixSubMatrixView extends AbstractMatrix {
	
	private IMatrix original;
	private int[] rowIndexes;
	private int[] colIndexes;
	
	public MatrixSubMatrixView(IMatrix original, int row, int col) {
		this.original = original;
		ArrayList<Integer> rows = new ArrayList<>();
		for (int i = 0; i < original.getRowsCount(); i++) {
			if (i != row) {
				rows.add(i);
			}
		}
		this.rowIndexes = new int[rows.size()];
		for (int i = 0; i < rowIndexes.length; i++) {
			rowIndexes[i] = rows.get(i);
		}

		ArrayList<Integer> cols = new ArrayList<>();
		for (int i = 0; i < original.getColsCount(); i++) {
			if (i != col) {
				cols.add(i);
			}
		}
		this.colIndexes = new int[cols.size()];
		for (int i = 0; i < rowIndexes.length; i++) {
			colIndexes[i] = cols.get(i);
		}
	}
	
	private MatrixSubMatrixView(IMatrix original, int[] rowIndexes, int[] colIndexes) {
		this.original = original;
		this.rowIndexes = rowIndexes;
		this.colIndexes = colIndexes;
	}

	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return colIndexes.length;
	}

	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= this.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= this.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		int originalRow = rowIndexes[row];
		int originalCol = colIndexes[col];
		return original.get(originalRow, originalCol);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		if (row < 0 || row >= this.getRowsCount()) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= this.getColsCount()) {
			throw new IndexOutOfBoundsException();
		}
		int originalRow = rowIndexes[row];
		int originalCol = colIndexes[col];
		original.set(originalRow, originalCol, value);
		return this;
	}

	@Override
	public IMatrix copy() {
		int[] rows = rowIndexes.clone();
		int[] cols = rowIndexes.clone();
		return new MatrixSubMatrixView(original.copy(), rows, cols);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixSubMatrixView(new Matrix(rows, cols), rows, cols);
	}

	
}
