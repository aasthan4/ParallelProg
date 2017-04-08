import java.util.Random;
class coinFlip implements Runnable {
	int id ;//name of thread
	Random rand;
	private static int num_threads, num_iters;
	static int [] num_heads = new int [1000];
	public void run() {
		for (int j=0; j<(num_iters/num_threads); j++) {
		    if (this.rand.nextInt(2) ==0)
				++num_heads[this.id];
		}
	}
		coinFlip(int id) {
			this.id=id; 
			this.rand = new Random();
		}

	public static void main(String[] args){
		int res=0;
		long time_start=System.currentTimeMillis();
		num_threads = Integer.parseInt(args[0]);
		num_iters = Integer.parseInt(args[1]);
		Thread[] coinFlip = new Thread[num_threads];
		for (int i=0; i!=num_threads; i++) {
			coinFlip[i]=new Thread(new coinFlip(i));
			coinFlip[i].start();
		}
		for (int i=0; i!=num_threads; i++) {
			try {
				coinFlip[i].join();
				res+=num_heads[i];
			}
			catch (InterruptedException e) {
				System.out.println("Thread interrupted.  Exception: " + e.toString() +
                           " Message: " + e.getMessage()) ;
				return;
			}
		}
		long time_end = System.currentTimeMillis();
		long time = time_end-time_start; 
		System.out.println("No of heads = " + res+ "Time : " +
				time);
	}
}

