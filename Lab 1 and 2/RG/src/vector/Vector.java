package vector;

public class Vector extends AbstractVector{
	
	private double[] elements;
	private int dimension;
	private boolean readOnly;
	
	public Vector(double[] elements) {
		this.elements = elements;
		this.dimension = elements.length;
		this.readOnly = false;
	}
	
	public Vector(boolean readOnly, boolean usable, double[] elements) {
		this.readOnly = readOnly;
		this.dimension = elements.length;
		
		if (usable) {
			this.elements = elements;
		} else {
			double[] array = new double[elements.length];
			for (int i = elements.length - 1; i >= 0; i--) {
				array[i] = elements[i];
			}
			this.elements = array;
		}
	}

	@Override
	public double get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= dimension) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}
		return elements[index];
	}

	@Override
	public IVector set(int index, double value) {
		if (readOnly) {
			throw new IllegalArgumentException("Cannot modify this vector.");
		}
		if (index < 0 || index >= dimension) {
			throw new IndexOutOfBoundsException("Index out of bounds: " + index);
		}
		elements[index] = value;
		return this;
	}

	@Override
	public int getDimension() {
		return dimension;
	}

	@Override
	public IVector copy() {
		return new Vector(false, false, elements);
	}

	@Override
	public IVector newInstance(int dimension) {
		double[] elements = new double[dimension];
		return new Vector(elements);
	}
	
	public static IVector parseSimple(String string) throws NumberFormatException{
		String[] splitString = string.split("\\s+");
		double[] array = new double[splitString.length];
		
		for (int i = splitString.length - 1; i >= 0; i--) {
			array[i] = Double.parseDouble(splitString[i]);
		}
		return new Vector(array);
	}

}
