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

	public Matrix(Matrix a, int ySize, int xSize, int yStart, int xStart, int yEnd, int xEnd) { //Copy Constructor
		y = ySize;
		x = xSize;
		array = new int [y][x];
		for(int i = yStart; i <= yEnd; i++) {
			for(int j = xStart; j <= xEnd; j++) {
				array[i - yStart][j - xStart] = a.getValue(i, j);
			}
		}
	}

	public Matrix add(Matrix matrix) {
		Matrix temp = new Matrix(y, x);
		if(this.compareDim(matrix)){
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

	public Matrix subtract(Matrix matrix) {
		Matrix temp = new Matrix(y, x);
		if(this.compareDim(matrix)){
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
	
	public static void strassen(MatrixPool pool, Matrix a, Matrix b, Matrix c) {
		if(a.y == 2) {
			c.replace(0, 0, a.getValue(0, 0) * b.getValue(0, 0) + a.getValue(0, 1) * b.getValue(1, 0));
			c.replace(0, 1, a.getValue(0, 0) * b.getValue(0, 1) + a.getValue(0, 1) * b.getValue(1, 1));
			c.replace(1, 0, a.getValue(1, 0) * b.getValue(0, 0) + a.getValue(1, 1) * b.getValue(1, 0));
			c.replace(1, 1, a.getValue(1, 0) * b.getValue(0, 1) + a.getValue(1, 1) * b.getValue(1, 1));
		} else {
			int m = a.y;
			Matrix A00 = pool.createMatrix(m / 2, m / 2);
			Matrix A01 = pool.createMatrix(m / 2, m / 2);
			Matrix A10 = pool.createMatrix(m / 2, m / 2);
			Matrix A11 = pool.createMatrix(m / 2, m / 2);
			Matrix B00 = pool.createMatrix(m / 2, m / 2);
			Matrix B01 = pool.createMatrix(m / 2, m / 2);
			Matrix B10 = pool.createMatrix(m / 2, m / 2);
			Matrix B11 = pool.createMatrix(m / 2, m / 2);

			A00.copy(a, 0,   0,   m/2 - 1, m/2 - 1, 0, 0);
			A01.copy(a, 0,   m/2, m/2 - 1, m - 1  , 0, 0);
			A10.copy(a, m/2, 0,   m - 1,   m/2 - 1, 0, 0);
			A11.copy(a, m/2, m/2, m - 1,   m - 1  , 0, 0);
			B00.copy(b, 0,   0,   m/2 - 1, m/2 - 1, 0, 0);
			B01.copy(b, 0,   m/2, m/2 - 1, m - 1  , 0, 0);
			B10.copy(b, m/2, 0,   m - 1,   m/2 - 1, 0, 0);
			B11.copy(b, m/2, m/2, m - 1,   m - 1  , 0, 0);

			Matrix P = pool.createMatrix(m/2, m/2);
			Matrix Q = pool.createMatrix(m/2, m/2);
			Matrix R = pool.createMatrix(m/2, m/2);
			Matrix S = pool.createMatrix(m/2, m/2);
			Matrix T = pool.createMatrix(m/2, m/2);
			Matrix U = pool.createMatrix(m/2, m/2);
			Matrix V = pool.createMatrix(m/2, m/2);

			strassen(pool, A00.add(A11), B00.add(B11), P);
			strassen(pool, A10.add(A11), B00, Q);
			strassen(pool, A00, B01.subtract(B11), R);
			strassen(pool, A11, B10.subtract(B00), S);
			strassen(pool, A00.add(A01), B11, T);
			strassen(pool, A10.subtract(A00), B00.add(B01), U);
			strassen(pool, A01.subtract(A11), B10.add(B11), V);
			
			c.copy(P.add(S.add(V.subtract(T))), 0, 0, m/2 - 1,       m/2 - 1,       0,   0);
			c.copy(R.add(T),                    0, 0, m/2 - 1,       b.x - m/2 - 1, 0,   m/2);
			c.copy(Q.add(S),                    0, 0, a.y - m/2 - 1, m/2 - 1,       m/2, 0);
			c.copy(P.add(R.add(U.subtract(Q))), 0, 0, a.y - m/2 - 1, b.x - m/2 - 1, m/2, m/2);

			pool.add(A00);
			pool.add(A01);
			pool.add(A10);
			pool.add(A11);
			pool.add(B00);
			pool.add(B01);
			pool.add(B10);
			pool.add(B11);
			pool.add(P);
			pool.add(Q);
			pool.add(R);
			pool.add(S);
			pool.add(T);
			pool.add(U);
			pool.add(V);
		}
	}

	private static int nextPowOfTwo(int n) {
		return (int)Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
	}

	public int getValue(int y, int x) {
		return array[y][x];
	}

	public int[][] getArray() {
		return array;
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

	public boolean compareDim(Matrix a) {
		return this.y == a.y && this.x == a.x;
	}

	public boolean equals(Matrix a) { //Compares equality of matrix with parameter matrix
		if(!this.compareDim(a)) {
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
