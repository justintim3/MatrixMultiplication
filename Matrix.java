import java.util.Random;

public class Matrix {
	private int[][] array;
	public final int y, x;
	
	public Matrix(int y, int x) {
		array = new int [y][x];
		this.y = y;
		this.x = x;
	}
	public Matrix(Matrix a) {	//Copy Constructor
		this.y = a.y;
		this.x = a.x;
		array = new int [this.y][this.x];
		for(int i = 0; i < this.y; i++) {
			for(int j = 0; j < this.x; j++) {
				array[i][j] = a.getValue(i, j);
			}
		}
	}
	public Matrix(Matrix a, int ySize, int xSize, int yStart, int xStart, int yEnd, int xEnd) {	//Copy Constructor
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
		}
		else {
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
		}
		else {
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
				}
				catch (ArrayIndexOutOfBoundsException E) {
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
		}
		catch (ArrayIndexOutOfBoundsException E) {
			System.out.println("Incompatible matrices!");
		}
		return result;
	}
	public static void strassen(Matrix a, Matrix b, Matrix c) {
		/*if(a.x == b.y && a.y == c.y && b.x == c.x) {
			if(a.y == 2 && b.x == 2 && a.y == a.x && b.y == b.x) {*/
			if(a.y == 2) {
				c.replace(0, 0, a.getValue(0, 0) * b.getValue(0, 0) + a.getValue(0, 1) * b.getValue(1, 0));
				c.replace(0, 1, a.getValue(0, 0) * b.getValue(0, 1) + a.getValue(0, 1) * b.getValue(1, 1));
				c.replace(1, 0, a.getValue(1, 0) * b.getValue(0, 0) + a.getValue(1, 1) * b.getValue(1, 0));
				c.replace(1, 1, a.getValue(1, 0) * b.getValue(0, 1) + a.getValue(1, 1) * b.getValue(1, 1));
			}/*
			else if(a.y <= 2 || b.x <= 2) {
				c.copy(a.multiply(b), 0, 0, a.y - 1, b.x - 1, 0, 0);
			}*/
			else {
				int m = a.y;//= nextPowOfTwo(Math.max(Math.max(a.y, a.x), Math.max(b.y, b.x)));
				//Matrix A = new Matrix(a, m, m, 0, 0, a.y - 1, a.x - 1);
				//Matrix B = new Matrix(b, m, m, 0, 0, b.y - 1, b.x - 1);
				Matrix A00 = new Matrix(a, m/2, m/2, 0, 0, m/2 - 1, m/2 - 1);
				Matrix A01 = new Matrix(a, m/2, m/2, 0, m/2, m/2 - 1, m - 1);
				Matrix A10 = new Matrix(a, m/2, m/2, m/2, 0, m - 1, m/2 - 1);
				Matrix A11 = new Matrix(a, m/2, m/2, m/2, m/2, m - 1, m - 1);
				Matrix B00 = new Matrix(b, m/2, m/2, 0, 0, m/2 - 1, m/2 - 1);
				Matrix B01 = new Matrix(b, m/2, m/2, 0, m/2, m/2 - 1, m - 1);
				Matrix B10 = new Matrix(b, m/2, m/2, m/2, 0, m - 1, m/2 - 1);
				Matrix B11 = new Matrix(b, m/2, m/2, m/2, m/2, m - 1, m - 1);
				Matrix P = new Matrix(m/2, m/2);
				Matrix Q = new Matrix(m/2, m/2);
				Matrix R = new Matrix(m/2, m/2);
				Matrix S = new Matrix(m/2, m/2);
				Matrix T = new Matrix(m/2, m/2);
				Matrix U = new Matrix(m/2, m/2);
				Matrix V = new Matrix(m/2, m/2);
				//Matrix C00;
				//Matrix C01;
				//Matrix C10;
				//Matrix C11;
				strassen(A00.add(A11), B00.add(B11), P);
				strassen(A10.add(A11), B00, Q);
				strassen(A00, B01.subtract(B11), R);
				strassen(A11, B10.subtract(B00), S);
				strassen(A00.add(A01), B11, T);
				strassen(A10.subtract(A00), B00.add(B01), U);
				strassen(A01.subtract(A11), B10.add(B11), V);
				
				//C00 = P.add(S.add(V.subtract(T)));
				//C01 = R.add(T);
				//C10 = Q.add(S);
				//C11 = P.add(R.add(U.subtract(Q)));
				c.copy(P.add(S.add(V.subtract(T))), 0, 0, m/2 - 1, m/2 - 1, 0, 0);
				c.copy(R.add(T), 0, 0, m/2 - 1, b.x - m/2 - 1, 0, m/2);
				c.copy(Q.add(S), 0, 0, a.y - m/2 - 1, m/2 - 1, m/2, 0);
				c.copy(P.add(R.add(U.subtract(Q))), 0, 0, a.y - m/2 - 1, b.x - m/2 - 1, m/2, m/2);
			}
		}/*
		else {
			System.out.println("Incompatible matrices!");
		}
	}*/
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
	public boolean equals(Matrix a) {	//Compares equality of matrix with parameter matrix
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