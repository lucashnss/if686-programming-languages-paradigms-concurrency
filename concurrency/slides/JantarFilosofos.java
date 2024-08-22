import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class JantarFilosofos {
    private static final int NUM_FILOSOFOS = 5;
    private static final Semaphore[] garfos = new Semaphore[NUM_FILOSOFOS];

    static {
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            garfos[i] = new Semaphore(1);
        }
    }

    static class Filosofo implements Runnable {
        private final int id;

        public Filosofo(int id) {
            this.id = id;
        }

        private void pensar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está pensando...");
            Thread.sleep(10);
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está comendo...");
            Thread.sleep(10);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    pensar();
                    // Joga uma moeda para decidir se vai tentar comer
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        int garfoEsquerdo = id;
                        int garfoDireito = (id + 1) % NUM_FILOSOFOS;

                        // Para evitar deadlock, pegamos sempre o garfo de menor id primeiro
                        if (id % 2 == 0) {
                            garfos[garfoEsquerdo].acquire();
                            garfos[garfoDireito].acquire();
                        } else {
                            garfos[garfoDireito].acquire();
                            garfos[garfoEsquerdo].acquire();
                        }

                        comer();

                        garfos[garfoEsquerdo].release();
                        garfos[garfoDireito].release();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread[] filosofos = new Thread[NUM_FILOSOFOS];

        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i] = new Thread(new Filosofo(i));
            filosofos[i].start();
        }

        // Permite que a simulação rode por algum tempo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Interrompe todos os filósofos
        for (Thread filosofo : filosofos) {
            filosofo.interrupt();
        }

        // Aguarda a interrupção completa
        for (Thread filosofo : filosofos) {
            try {
                filosofo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Jantar dos filósofos encerrado.");
    }
}
