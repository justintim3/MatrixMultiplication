
import java.util.function.*;

public class MatrixMultiplication {
	public static void main(String[] args) {
		int start = 2, end = 10;
		int n, ay, ax, by, bx;
		long[] mulTime = new long[end - start + 1];
		long[] straTime = new long[end - start + 1]; 
		BiFunction<Matrix, Matrix, Matrix> multiply = (a, b) -> {
			return a.multiply(b);};
		BiFunction<Matrix, Matrix, Matrix> strassen = (a, b) -> {
			Matrix c = new Matrix(a.y, b.x);
			Matrix.strassen(a, b, c);
			return c;};
		Matrix a, b, c, d;
		//a.fillAsc();
		//b.fillAsc();
		System.out.println("n\tStandard\tStrassen");
		for(int i = start; i <= end; i++) {
			n = (int)Math.pow(2, i);
			ay = n; 
			ax = n; 
			by = ax; 
			bx = n;
			
			a = new Matrix(ay, ax);
			b = new Matrix(by, bx);
			//c = new Matrix(cy, cx);
			
			a.fillRand(0, 100);
			b.fillRand(0, 100);
			a.shuffle();
			b.shuffle();
			
			mulTime[i - start] = timer(multiply, a, b);
			straTime[i - start] = timer(strassen, a, b);
			
			System.out.println(n + "\t" + mulTime[i - start] + "\t\t" + straTime[i - start]);
			/*
			a.print();
			System.out.println();
			b.print();
			
			//c.shuffle();
			System.out.println();
			Matrix.strassen(a, b, c);
			c.print();
			System.out.println();
			d = a.multiply(b);
			d.print();
			System.out.println(c.equals(d));*/
		}

		//System.out.println(a.compare(b));
		//a.print();
		System.out.println();
	}
	public static long timer(BiFunction f, Matrix a, Matrix b) {
		final long startTime = System.currentTimeMillis();
		f.apply(a, b);
		final long endTime = System.currentTimeMillis();
		
		return endTime - startTime;
	}
}