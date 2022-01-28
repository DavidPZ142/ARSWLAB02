package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;


public class PrimeFinderThread extends Thread{


	int a,b;
    boolean booleano;
	private List<Integer> primes;
	private Object pivote;
	int need;


	public PrimeFinderThread(int a, int b, Object pivote) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.pivote = pivote;


	}

        @Override
	public void run(){
			long inicial = System.currentTimeMillis();
            for (int i= a;i < b;i++){
				if (i == need){
					inicial = System.currentTimeMillis();
					System.out.println("Actualizo el tiempo");
				}
                if (isPrime(i)){
                    primes.add(i);
                }
				long comparador = System.currentTimeMillis();
                long comparado = comparador - inicial;
                if (comparado > 5000){

                	synchronized (pivote){
                		try{

							//System.out.println(primes);
							System.out.println(primes);
							System.out.println("Ya pasaron 5 segundos.");
							System.out.println(comparado);
							//System.out.println(i);
							need = i+1;
							pivote.wait();

						}
                		catch(InterruptedException e){
                			e.printStackTrace();
						}
					}

				}

            }

	}

	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}



	public List<Integer> getPrimes() {
		return primes;
	}

	public void detenerHilo(){
	    booleano = true;


    }

    public void reanudarHilo(){
	    booleano = false;
	    notify();

    }
	
}
