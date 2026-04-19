import java.util.Scanner;

public class QuizApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int score = 0;

        // Question 1
        System.out.println("1. What is Java?");
        System.out.println("a) Programming Language");
        System.out.println("b) Database");
        System.out.println("c) Operating System");
        System.out.print("Enter answer: ");
        char q1 = sc.next().charAt(0);

        if (q1 == 'a') {
            score++;
        }

        // Question 2
        System.out.println("\n2. Which is used for web development?");
        System.out.println("a) HTML");
        System.out.println("b) CPU");
        System.out.println("c) RAM");
        System.out.print("Enter answer: ");
        char q2 = sc.next().charAt(0);

        if (q2 == 'a') {
            score++;
        }

        // Question 3
        System.out.println("\n3. Which database is commonly used?");
        System.out.println("a) MySQL");
        System.out.println("b) Keyboard");
        System.out.println("c) Monitor");
        System.out.print("Enter answer: ");
        char q3 = sc.next().charAt(0);

        if (q3 == 'a') {
            score++;
        }

        // Result
        System.out.println("\nYour Score: " + score + "/3");

        sc.close();
    }
}