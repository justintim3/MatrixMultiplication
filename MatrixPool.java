import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MatrixPool {
	private HashMap<Long, Queue<Matrix>> pool;
	
	public MatrixPool() {
		pool = new HashMap<Long, Queue<Matrix>>();
	}

	public void add(Matrix a) {
		long key = (((long) a.y << 32) | (long) a.x);
		if(pool.get(key) == null) {
			pool.put(key, new LinkedList<Matrix>());
		}
		pool.get(key).add(a);
	}

	public Matrix createMatrix(int y, int x) {
		long key = (((long) y << 32) | (long) x);

		Queue<Matrix> p = pool.get(key);
		if(p != null && p.peek() != null) {
			return p.poll();
		}
		return new Matrix(y, x);
	}

	public Matrix peek(int y, int x) {
		long key = (((long) y << 32) | (long) x);

		if(pool.get(key) == null) {
			pool.put(key, new LinkedList<Matrix>());
		}
		return pool.get(key).peek();
	}

	public Matrix poll(int y, int x) {
		long key = (((long) y << 32) | (long) x);

		Queue<Matrix> p = pool.get(key);
		if(p != null && p.peek() != null) {
			return p.poll();
		}
		return null;
	}

	public static void main(String[] args) {
	}
}
