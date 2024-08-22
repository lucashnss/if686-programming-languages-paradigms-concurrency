import java.util.Random;

public class ProdutorConsumidor {
    private Integer buffer;
    private final Object lock = new Object();
    private boolean produzido = false;

    public void produzir() {
        Random random = new Random();
        while (true) {
            synchronized (lock) {
                while (produzido) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                buffer = random.nextInt(100);
                System.out.println("Produtor produziu: " + buffer);
                produzido = true;
                lock.notifyAll();
            }
            try {
                Thread.sleep(500); // Simula o tempo de produção
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void consumirPar() {
        while (true) {
            synchronized (lock) {
                while (!produzido || buffer % 2 != 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Consumidor de par consumiu: " + buffer);
                produzido = false;
                lock.notifyAll();
            }
        }
    }

    public void consumirImpar() {
        while (true) {
            synchronized (lock) {
                while (!produzido || buffer % 2 == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                System.out.println("Consumidor de ímpar consumiu: " + buffer);
                produzido = false;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        ProdutorConsumidor pc = new ProdutorConsumidor();

        Thread produtor = new Thread(pc::produzir);
        Thread consumidorPar = new Thread(pc::consumirPar);
        Thread consumidorImpar = new Thread(pc::consumirImpar);

        produtor.start();
        consumidorPar.start();
        consumidorImpar.start();
    }
}
