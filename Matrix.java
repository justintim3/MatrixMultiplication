import java.util.Random;

public class Matrix {
	private int[][] array;
	public final int y, x;
	
	public Matrix(int y, int x) {
		array = new int [y][x];
		this.y = y;
		this.x = x;
	}

	public Matrix(Matrix a) { //Copy Constructor
		this.y = a.y;
		this.x = a.x;
		array = new int [this.y][this.x];
		for(int i = 0; i < this.y; i++) {
			for(int j = 0; j < this.x; j++) {
				array[i][j] = a.getValue(i, j);
			}
		}
	}

	public Matrix add(MatrixPool pool, Matrix matrix) {
		Matrix temp = pool.createMatrix(y, x);
		if(y == matrix.y && x == matrix.x){
			for(int i = 0; i < y; i++) {
				for(int j = 0; j < x; j++) {
					temp.replace(i, j, array[i][j] + matrix.getValue(i, j)); 
				}
			}
		} else {
			System.out.println("Incompatible matrices!");
		}
		return temp;
	}

	public Matrix subtract(MatrixPool pool, Matrix matrix) {
		Matrix temp = pool.createMatrix(y, x);
		if(y == matrix.y && x == matrix.x){
			for(int i = 0; i < y; i++) {
				for(int j = 0; j < x; j++) {
					temp.replace(i, j, array[i][j] - matrix.getValue(i, j)); 
				}
			}
		} else {
			System.out.println("Incompatible matrices!");
		}
		return temp;
	}

	public void replace(int y, int x, int value) {
		array[y][x] = value;
	}

	public void copy(Matrix matrix, int yStart, int xStart, int yEnd, int xEnd, int yBegin, int xBegin) {
		for(int i = yStart; i <= yEnd; i++) {
			for(int j = xStart; j <= xEnd; j++) {
				try {
					array[i - yStart + yBegin][j - xStart + xBegin] = matrix.getValue(i, j);
				} catch (ArrayIndexOutOfBoundsException E) {
				}
			}
		}
	}

	public void print() {
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) {
				System.out.print(array[i][j] + "\t");
			}
			System.out.println();
		}
	}

	public Matrix multiply(Matrix matrix) {
		Matrix result = new Matrix(this.y, matrix.x);
		int temp = 0;
		try {
			for(int i = 0; i < this.y; i++) {
				for(int j = 0; j < matrix.x; j++) {
					for(int k = 0; k < this.x; k++) {
						temp += array[i][k] * matrix.getValue(k, j);
					}
					result.replace(i, j, temp);
					temp = 0;
				}
			}
		} catch (ArrayIndexOutOfBoundsException E) {
			System.out.println("Incompatible matrices!");
		}
		return result;
	}

	public int getValue(int y, int x) {
		return array[y][x];
	}

	public void fillAsc() {
		int count = 0;
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) {
				array[i][j] = count;
				count++;
			}
		}
	}

	public void fillRand(int min, int max) {
		Random rand = new Random();
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) {
				array[i][j] = rand.nextInt(max) + min;
			}
		}
	}

	public void shuffle() {
		Random rand = new Random();
		for(int i = y - 1; i > 0; i--){
			for(int j = x - 1; j > 0; j--){
				int m = rand.nextInt(i + 1);
				int n = rand.nextInt(j + 1);

				int temp = array[i][j];
				array[i][j] = array[m][n];
				array[m][n] = temp;
			}
		}
	}

	public boolean equals(Matrix a) { //Compares equality of matrix with parameter matrix
		if(y != a.y || x != a.x) {
			return false;
		}
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) {
				if(this.getValue(i, j) != a.getValue(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
}
