package vector;

import matrix.IMatrix;

public abstract class AbstractVector implements IVector{

	public AbstractVector() {}
	
	@Override
	public abstract double get(int index);

	@Override
	public abstract IVector set(int index, double value);

	@Override
	public abstract int getDimension();
	
	@Override
	public abstract IVector copy();

	@Override
	public IVector copyPart(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Invalid size for copyPart: "
					+ n);
		}
		IVector newVector = newInstance(n);
		int dimension = this.getDimension();
		for (int i = 0; i < n; i++) {
			if (i < dimension) {
				newVector.set(i, this.get(i));
			} else {
				newVector.set(i, 0);
			}
		}
		return newVector;
	}

	@Override
	public abstract IVector newInstance(int dimension);

	@Override
	public IVector add(IVector other) throws IllegalArgumentException {
		if (this.getDimension() != other.getDimension()) {
			throw new IllegalArgumentException();
		}
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) + other.get(i));
		}
		return this;
	}

	@Override
	public IVector nAdd(IVector other) throws IllegalArgumentException {
		return this.copy().add(other);
	}

	@Override
	public IVector sub(IVector other) throws IllegalArgumentException {
		if (this.getDimension() != other.getDimension()) {
			throw new IllegalArgumentException();
		}
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) - other.get(i));
		}
		return this;
	}

	@Override
	public IVector nSub(IVector other) throws IllegalArgumentException {
		return this.copy().sub(other);
	}

	@Override
	public IVector scalarMultiply(double byValue) {
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) * byValue);
		}
		return this;
	}

	@Override
	public IVector nScalarMultiply(double byValue) {
		return this.copy().scalarMultiply(byValue);
	}

	@Override
	public double norm() {
		double sqrSum = 0;
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			sqrSum += Math.pow((this.get(i)), 2);
		}
		return Math.sqrt(sqrSum);
	}

	@Override
	public IVector normalize() throws IllegalArgumentException {
		double norm = this.norm();
		if (Math.abs(norm - 0) < 0.00000001) {
			throw new IllegalArgumentException("Cannot normalize, norm is 0.");
		}
		
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			this.set(i, this.get(i) / norm);
		}
		return this;
	}

	@Override
	public IVector nNormalize() {
		return this.copy().normalize();
	}

	@Override
	public double cosine(IVector other) throws IllegalArgumentException {
		return this.scalarProduct(other) / (this.norm() * other.norm());
	}

	@Override
	public double scalarProduct(IVector other) throws IllegalArgumentException {
		double scalarSum = 0;
		if (this.getDimension() != other.getDimension()) {
			throw new IllegalArgumentException();
		}
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			scalarSum += this.get(i) * other.get(i);
		}
		return scalarSum;
	}

	@Override
	public IVector nVectorProduct(IVector other) throws IllegalArgumentException {
		
		if (this.getDimension() > 3 || other.getDimension() > 3) {
			throw new IllegalArgumentException();
		}
		
		IVector first = this.copyPart(3);
		IVector second = other.copyPart(3);
		
		double a = first.get(1) * second.get(2) - first.get(2) * second.get(1);
		double b = first.get(0) * second.get(2) - first.get(2) * second.get(0);
		double c = first.get(0) * second.get(1) - first.get(1) * second.get(0);

		IVector newVector = this.copy();
		newVector.set(0, a);
		newVector.set(1, -b);
		newVector.set(2, c);
		return newVector;
	}

	@Override
	public IVector nFromHomogeneus() throws IllegalArgumentException {
		if (this.getDimension() < 2 || this.get(this.getDimension() - 1) == 0) {
			throw new IllegalArgumentException();
		}
		IVector newVector = this.copyPart(this.getDimension() - 1);
		double div = this.get(this.getDimension() - 1);
		for (int i = newVector.getDimension() - 1; i >= 0; i--) {
			newVector.set(i, newVector.get(i) / div);
		}
		return newVector;
	}

	@Override
	public IMatrix toRowMatrix(boolean liveView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IMatrix toColumnMatrix(boolean liveView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] toArray() {
		double[] array = new double[this.getDimension()];
		for (int i = this.getDimension() - 1; i >= 0; i--) {
			array[i] = this.get(i);
		}
		return array;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < this.getDimension(); i++) {
			sb.append(String.format("%.3f", this.get(i)));
			if (i < this.getDimension() - 1){
				sb.append(" ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
