
import java.util.function.*;

public class MatrixMultiplication {
	public static void main(String[] args) {
		MatrixPool pool = new MatrixPool();
		int start = 5, end = 13;
		int n, ay, ax, by, bx;
		long[] mulTime = new long[end - start + 1];
		long[] straTime = new long[end - start + 1]; 

		Function<MatrixPool, Function<Matrix, Function<Matrix, Matrix>>> multiply = p -> a -> b -> {
			Matrix c = p.createMatrix(a.y, b.x);
			MatrixUtils.multiply(p, a, b, c);
			return c;
		};

		Function<MatrixPool, Function<Matrix, Function<Matrix, Matrix>>> strassen = p -> a -> b -> {
			Matrix c = p.createMatrix(a.y, b.x);
			MatrixUtils.strassen(p, a, b, c);
			return c;
		};

		Matrix a, b, c, d;
		System.out.printf("%10s %15s %15s %15s %15s\n", "n", "Standard", "Strassen", "Standard Growth", "Strassen Growth");
		for(int i = start; i <= end; i++) {
			n = (int)Math.pow(2, i);
			ay = n; 
			ax = n; 
			by = ax; 
			bx = n;
			
			a = new Matrix(ay, ax);
			b = new Matrix(by, bx);
			//c = new Matrix(ay, bx);
			//d = new Matrix(ay, bx);
			
			a.fillRand(0, 100);
			b.fillRand(0, 100);

			//MatrixUtils.multiply(pool, a, b, c);
			//MatrixUtils.strassen(pool, a, b, d);

			//System.out.println(c.equals(d));

			mulTime[i - start] = timer(multiply, pool, a, b);
			straTime[i - start] = timer(strassen, pool, a, b);
			
			System.out.printf("%10d %15d %15d", n, mulTime[i - start], straTime[i - start]);
			if(i > start) {
				System.out.printf(" %15.2f %15.2f", (1.0 * mulTime[i - start] / mulTime[i - start - 1]) , (1.0 * straTime[i - start] / straTime[i - start - 1]));
			}
			System.out.println();
		}
		System.out.println();
	}
	public static long timer(Function<MatrixPool, Function<Matrix, Function<Matrix, Matrix>>> f, MatrixPool pool, Matrix a, Matrix b) {
		final long startTime = System.currentTimeMillis();
		f.apply(pool).apply(a).apply(b);
		final long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}
}
