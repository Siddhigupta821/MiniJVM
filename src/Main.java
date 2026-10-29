import lexer.Lexer;
import lexer.Token;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = """
            // Test program
            int a = 10;
            int b = 20;
            if (a <= b) {
                print a + b;
            }
            """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
