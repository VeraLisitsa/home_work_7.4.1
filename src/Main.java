import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static BlockingQueue<String> queueForA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueForB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueForC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        Thread genText = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String letters = "abc";
                int length = 100_000;
                String text = generateText(letters, length);
                try {
                    queueForA.put(text);
                    queueForB.put(text);
                    queueForC.put(text);
                } catch (InterruptedException e) {
                    return;
                }

            }
        });
        genText.start();

        Thread letA = new Thread(() -> {
            textMax(queueForA, 'a');
        });
        letA.start();

        Thread letB = new Thread(() -> {
            textMax(queueForB, 'b');
        });
        letB.start();

        Thread letC = new Thread(() -> {
            textMax(queueForC, 'c');
        });
        letC.start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int countLetters(String text, char letter) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

    public static void textMax(BlockingQueue<String> queue, char letter) {
        String textMax = null;
        int countMax = 0;
        for (int j = 0; j < 10_000; j++) {
            String text;
            try {
                text = queue.take();
            } catch (InterruptedException e) {
                return;
            }
            int countA = countLetters(text, letter);
            if (countA > countMax) {
                countMax = countA;
                textMax = text;
            }
        }
        System.out.println("Текст с максимальным количеством букв " + letter + ": " + textMax);
    }
}