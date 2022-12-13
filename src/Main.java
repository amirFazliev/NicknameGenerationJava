import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger intThreeSymbol = new AtomicInteger(0);
    static AtomicInteger intFourSymbol = new AtomicInteger(0);
    static AtomicInteger intFiveSymbol = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread threadThree = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 3 && text.charAt(0) == text.charAt(1) && text.charAt(0) == text.charAt(2)) {
                    intThreeSymbol.getAndIncrement();
                }
            }
        });

        Thread threadFour = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 4) {
                    StringBuilder textEnd = new StringBuilder();
                    textEnd.append(text.charAt(3));
                    textEnd.append(text.charAt(2));
                    if (text.substring(0, 2).equals(textEnd.toString())) {
                        intFourSymbol.getAndIncrement();
                    }
                }
            }
        });

        Thread threadFive = new Thread(() -> {
            for (String text : texts) {
                if (text.length() == 5) {
                    AtomicInteger charNumber = new AtomicInteger(0);
                    for (int i = 1; i< text.length(); i++) {
                        if (text.charAt(charNumber.get()) < text.charAt(i)) {
                            break;
                        }
                        charNumber.getAndIncrement();
                    }
                    if (text.length()-1 == charNumber.get()) {
                        intFiveSymbol.getAndIncrement();
                    }
                }
            }
        });

        threadThree.start();
        threadFour.start();
        threadFive.start();

        threadThree.join();
        threadFour.join();
        threadFive.join();

        System.out.println("Красивых слов с длиной 3: " + intThreeSymbol + " шт");
        System.out.println("Красивых слов с длиной 4: " + intFourSymbol + " шт");
        System.out.println("Красивых слов с длиной 5: " + intFiveSymbol + " шт");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}