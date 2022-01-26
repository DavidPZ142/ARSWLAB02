package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{


	int a,b;
    boolean booleano;
	private List<Integer> primes;
	
	public PrimeFinderThread(int a, int b) {
		super();
		this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
		this.booleano = false;



	}

        @Override
	public void run(){
			long inicial = System.currentTimeMillis();

            for (int i= a;i < b;i++){


                if (isPrime(i)){
                    primes.add(i);
                    /*System.out.println(i);*/
                }
				long comparador = System.currentTimeMillis();
                long comparado = comparador - inicial;
                if (comparado > 5000){
                	/*CAso de salida con un objeto XD*/
					/*Deberia reinicia el inicial*/
				}

            }
            System.out.println(primes);
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
