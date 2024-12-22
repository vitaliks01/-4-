package pr_4_11;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

        CompletableFuture.runAsync(() -> System.out.println("Запуск асинхронної задачі 1"));
        // Ств почат мас випадх чис
        CompletableFuture<int[]> initialArrayFuture = CompletableFuture.supplyAsync(() -> {
            // Вимірюємо час генерації масиву
            long start = System.currentTimeMillis();
            // Генер мас 10 випадк чис від 1 до 50
            int[] array = new Random().ints(10, 1, 50).toArray();
            // Вивед мас та час
            System.out.println("Початковий масив: " + Arrays.toString(array));
            System.out.println("Час генерації масиву: " + (System.currentTimeMillis() - start) + " мс");
            return array;
        });

        // Збільш кожн елемент на 5
        CompletableFuture<int[]> modifiedArrayFuture = initialArrayFuture.thenApplyAsync(array -> {
            // Вим час
            long start = System.currentTimeMillis();
            // Кожен елем збільш на 5
            int[] modifiedArray = Arrays.stream(array).map(x -> x + 5).toArray();
            // Вивед масив та час витрач на обробк
            System.out.println("Масив після збільшення на 5: " + Arrays.toString(modifiedArray));
            System.out.println("Час модифікації масиву: " + (System.currentTimeMillis() - start) + " мс");
            return modifiedArray;
        });

        // Обчисл факторіал сум елемен двох мас
        CompletableFuture<BigInteger> factorialFuture = modifiedArrayFuture.thenCombineAsync(initialArrayFuture, (modifiedArray, initialArray) -> {
            // Вим час
            long start = System.currentTimeMillis();
            // Обчисл сум елемент обох мас
            int sum1 = Arrays.stream(modifiedArray).sum();
            int sum2 = Arrays.stream(initialArray).sum();
            // Обчисл факторіа від сум елемент двох мас
            BigInteger factorial = factorial(sum1 + sum2);
            // Вивед рез факторіал та час на його обчисл
            System.out.println("Факторіал суми (" + (sum1 + sum2) + "): " + factorial);
            System.out.println("Час обчислення факторіалу: " + (System.currentTimeMillis() - start) + " мс");
            return factorial;
        });
        // Заверш обчисл факторіал
        factorialFuture.get();


        CompletableFuture.runAsync(() -> System.out.println("\nЗапуск асинхронної задачі 2"));
        // Генер випадк послід чис
        CompletableFuture<int[]> randomSequenceFuture = CompletableFuture.supplyAsync(() -> {
            // Вимір час
            long start = System.currentTimeMillis();
            // Генерація масиву з 20 випадкових чисел у діапазоні від 1 до 100
            int[] sequence = new Random().ints(20, 1, 100).toArray();
            // Вивед почат послід та час на її ств
            System.out.println("Початкова послідовність: " + Arrays.toString(sequence));
            System.out.println("Час генерації послідовності: " + (System.currentTimeMillis() - start) + " мс");
            return sequence;
        });

        // Обчисл мін сум сусідн елем у послід
        CompletableFuture<Integer> minSumFuture = randomSequenceFuture.thenApplyAsync(sequence -> {
            // Вимір час обчисл мін сум
            long start = System.currentTimeMillis();
            // Обчис мін сум двох сусід елем
            int minSum = IntStream.range(0, sequence.length - 1)
                    .map(i -> sequence[i] + sequence[i + 1])
                    .min()
                    .orElseThrow();
            // Вивед рез мін сум та час на її обчисл
            System.out.println("Мінімальна сума сусідніх елементів: " + minSum);
            System.out.println("Час обчислення мінімальної суми: " + (System.currentTimeMillis() - start) + " мс");
            return minSum;
        });
        minSumFuture.get();

        // Вим заг час вик програм
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("\nЗагальний час виконання: " + totalTime + " мс");
    }

    // Мет для обчисл фактор числ
    private static BigInteger factorial(int n) {
        return IntStream.rangeClosed(1, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }
}
