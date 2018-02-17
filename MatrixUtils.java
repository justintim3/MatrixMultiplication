import java.util.Random;

public class MatrixUtils {
	public static void add(MatrixPool pool, Matrix a, Matrix b, Matrix c) {
		if(a.y == b.y && a.x == b.x){
			for(int i = 0; i < a.y; i++) {
				for(int j = 0; j < a.x; j++) {
					c.replace(i, j, a.getValue(i, j) + b.getValue(i, j)); 
				}
			}
		} else {
			System.out.println("Incompatible matrices!");
		}
	}

	public static void subtract(MatrixPool pool, Matrix a, Matrix b, Matrix c) {
		if(a.y == b.y && a.x == b.x){
			for(int i = 0; i < a.y; i++) {
				for(int j = 0; j < a.x; j++) {
					c.replace(i, j, a.getValue(i, j) - b.getValue(i, j)); 
				}
			}
		} else {
			System.out.println("Incompatible matrices!");
		}
	}

	public static void copy(Matrix a, Matrix b, int yStart, int xStart, int yEnd, int xEnd, int yBegin, int xBegin) {
		for(int i = yStart; i <= yEnd; i++) {
			for(int j = xStart; j <= xEnd; j++) {
				try {
					b.replace(i - yStart + yBegin, j - xStart + xBegin, a.getValue(i, j));
				} catch (ArrayIndexOutOfBoundsException E) {
				}
			}
		}
	}

	public static void multiply(MatrixPool pool, Matrix a, Matrix b, Matrix c) {
		try {
			int temp;
			for(int i = 0; i < a.y; i++) {
				for(int j = 0; j < b.x; j++) {
					temp = 0;
					for(int k = 0; k < a.x; k++) {
						temp += a.getValue(i, k) * b.getValue(k, j);
					}
					c.replace(i, j, temp);
				}
			}
		} catch (ArrayIndexOutOfBoundsException E) {
			System.out.println("Incompatible matrices!");
		}
	}
	
	public static void strassen(MatrixPool pool, Matrix a, Matrix b, Matrix c) {
		if(a.y == 2) {
			c.replace(0, 0, a.getValue(0, 0) * b.getValue(0, 0) + a.getValue(0, 1) * b.getValue(1, 0));
			c.replace(0, 1, a.getValue(0, 0) * b.getValue(0, 1) + a.getValue(0, 1) * b.getValue(1, 1));
			c.replace(1, 0, a.getValue(1, 0) * b.getValue(0, 0) + a.getValue(1, 1) * b.getValue(1, 0));
			c.replace(1, 1, a.getValue(1, 0) * b.getValue(0, 1) + a.getValue(1, 1) * b.getValue(1, 1));
		} else {
			int m = a.y;
			Matrix A00 = pool.createMatrix(m/2, m/2);
			Matrix A01 = pool.createMatrix(m/2, m/2);
			Matrix A10 = pool.createMatrix(m/2, m/2);
			Matrix A11 = pool.createMatrix(m/2, m/2);
			Matrix B00 = pool.createMatrix(m/2, m/2);
			Matrix B01 = pool.createMatrix(m/2, m/2);
			Matrix B10 = pool.createMatrix(m/2, m/2);
			Matrix B11 = pool.createMatrix(m/2, m/2);

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

			Matrix temp1 = pool.createMatrix(m/2, m/2);
			Matrix temp2 = pool.createMatrix(m/2, m/2);

			add(pool, A00, A11, temp1);
			add(pool, B00, B11, temp2);
			strassen(pool, temp1, temp2, P);

			add(pool, A10, A11, temp1);
			strassen(pool, temp1, B00, Q);

			subtract(pool, B01, B11, temp1);
			strassen(pool, A00, temp1, R);

			subtract(pool, B10, B00, temp1);
			strassen(pool, A11, temp1, S);

			add(pool, A00, A01, temp1);
			strassen(pool, temp1, B11, T);

			subtract(pool, A10, A00, temp1);
			add(pool, B00, B01, temp2);
			strassen(pool, temp1, temp2, U);

			subtract(pool, A01, A11, temp1);
			add(pool, B10, B11, temp2);
			strassen(pool, temp1, temp2, V);

			subtract(pool, V, T, temp1);
			add(pool, S, temp1, temp2);
			add(pool, P, temp2, temp1);
			c.copy(temp1, 0, 0, m/2 - 1,       m/2 - 1,       0,   0);

			add(pool, R, T, temp1);
			c.copy(temp1, 0, 0, m/2 - 1,       b.x - m/2 - 1, 0,   m/2);

			add(pool, Q, S, temp1);
			c.copy(temp1, 0, 0, a.y - m/2 - 1, m/2 - 1,       m/2, 0);

			subtract(pool, U, Q, temp1);
			add(pool, R, temp1, temp2);
			add(pool, P, temp2, temp1);
			c.copy(temp1, 0, 0, a.y - m/2 - 1, b.x - m/2 - 1, m/2, m/2);

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
			pool.add(temp1);
			pool.add(temp2);
		}
	}
}
