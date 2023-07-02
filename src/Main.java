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
            String textMaxA = null;
            int countMax = 0;
            for (int j = 0; j < 10_000; j++) {
                String text = null;
                try {
                    text = queueForA.take();
                } catch (InterruptedException e) {
                    return;
                }
                int countA = countLetters(text, 'a');
                if (countA > countMax) {
                    countMax = countA;
                    textMaxA = text;
                }
            }
            System.out.println("Текст с максимальным количеством букв а: " + textMaxA);
        });
        letA.start();

        Thread letB = new Thread(() -> {
            String textMaxB = null;
            int countMax = 0;
            for (int j = 0; j < 10_000; j++) {
                String text = null;
                try {
                    text = queueForB.take();
                } catch (InterruptedException e) {
                    return;
                }
                int countB = countLetters(text, 'b');
                if (countB > countMax) {
                    countMax = countB;
                    textMaxB = text;
                }
            }
            System.out.println("Текст с максимальным количеством букв b: " + textMaxB);
        });
        letB.start();

        Thread letC = new Thread(() -> {
            String textMaxC = null;
            int countMax = 0;
            for (int j = 0; j < 10_000; j++) {
                String text = null;
                try {
                    text = queueForC.take();
                } catch (InterruptedException e) {
                    return;
                }
                int countC = countLetters(text, 'c');
                if (countC > countMax) {
                    countMax = countC;
                    textMaxC = text;
                }
            }
            System.out.println("Текст с максимальным количеством букв c: " + textMaxC);
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
}