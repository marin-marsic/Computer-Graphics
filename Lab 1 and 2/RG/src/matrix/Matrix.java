package matrix;

public class Matrix extends AbstractMatrix{
	
	private double[][] elements;
	private int rows;
	private int cols;
	
	public Matrix(int rows, int cols) {
		double[][] array = new double[rows][cols];
		this.elements = array;
		this.rows = rows;
		this.cols = cols;
	}
	
	public Matrix(int rows, int cols, double[][] elements, boolean usable) {
		this.rows = rows;
		this.cols = cols;
		
		if (usable) {
			this.elements = elements;
		} else {
			double[][] array = new double[rows][cols];
			for (int i = rows - 1; i >= 0; i--) {
				for (int j = cols - 1; j >= 0; j--) {
					array[i][j] = elements[i][j];
				}
			}
		}
	}

	@Override
	public int getRowsCount() {
		return rows;
	}

	@Override
	public int getColsCount() {
		return cols;
	}

	@Override
	public double get(int row, int col) {
		if (row < 0 || row >= rows) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= cols) {
			throw new IndexOutOfBoundsException();
		}
		return elements[row][col];
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		if (row < 0 || row >= rows) {
			throw new IndexOutOfBoundsException();
		}
		if (col < 0 || col >= cols) {
			throw new IndexOutOfBoundsException();
		}
		elements[row][col] = value;
		return this;
	}

	@Override
	public IMatrix copy() {
		IMatrix newMatrix = newInstance(rows, cols);
		for (int i = rows - 1; i >= 0; i--) {
			for (int j = cols - 1; j >= 0; j--) {
				newMatrix.set(i, j, elements[i][j]);
			}
		}
		return newMatrix;
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new Matrix(rows, cols);
	}
	
	public static IMatrix parseSimple(String string) {
		String[] rows = string.split("\\|");
		double[][] matrix = new double[rows.length][rows[0].split("\\s+").length];
		for (int i = matrix.length - 1; i >= 0; i--) {
			String[] rowSplit = rows[i].trim().split("\\s+");
			if (rowSplit.length != matrix[0].length){
				throw new IllegalArgumentException();
			}
			for (int j = matrix[0].length - 1; j >= 0; j--) {
				matrix[i][j] = Double.parseDouble(rowSplit[j]);
			}
		}
		return new Matrix(matrix.length, matrix[0].length, matrix, true);
	}

}
