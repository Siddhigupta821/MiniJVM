package lexer;

import java.util.ArrayList;
import java.util.List;

public class Lexer{
    private final String source;
    private final List<Token> tokens;
    private int line;
    private int column;
    public int start;
    public int current;
    public int tokenStartColumn;

    public Lexer(String source){
        this.source = source;
        this.tokens = new ArrayList<>();
        this.line = 1;
        this.column = 1;
        this.start = 0;
        this.current = 0;
        this.tokenStartColumn = 1;
    }

    public List<Token> tokenize(){
        while(!isAtEnd()){
            start = current;
            tokenStartColumn = column;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF,"",line,column));
        return tokens;
    }   
    
    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char advance(){
        char c = source.charAt(current++);
        if(c == '\n'){
            line++;
            column = 1;
        }else{
            column++;
        }
        return c;
    }

    private char peek(){
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected)
    {
        if(isAtEnd()) return false;
        if(source.charAt(current)!= expected) return false;
     
        current++;
        column++;
        return true;
    }
    
    private void addToken(TokenType type){
        String lexeme = source.substring(start,current);
        tokens.add(new Token(type,lexeme,line,tokenStartColumn));        
    }

    private void identifier(){
        while(!isAtEnd() && (Character.isLetterOrDigit(peek()) || peek()=='_')){
            advance();
        }
        String text = source.substring(start,current);
        TokenType type = switch(text){
            case "int" -> TokenType.INT;
            case "boolean" -> TokenType.BOOLEAN;
            case "class" -> TokenType.CLASS;
            case "if" -> TokenType.IF;
            case "else" -> TokenType.ELSE;
            case "while" -> TokenType.WHILE;
            case "print" -> TokenType.PRINT;
            case "true" -> TokenType.TRUE;
            case "false" -> TokenType.FALSE;
            default -> TokenType.IDENTIFIER;
        };
        addToken(type);
    }

    private void number(){
        while(!isAtEnd() && Character.isDigit(peek())){
            advance();
        }
        addToken(TokenType.NUMBER);
    }

    private void scanToken(){
        char c = advance();
        switch(c){
            case '(': addToken(TokenType.LEFT_PAREN);break;
            case ')': addToken(TokenType.RIGHT_PAREN);break;
            case '{': addToken(TokenType.LEFT_BRACE);break;
            case '}': addToken(TokenType.RIGHT_BRACE);break;
            case ',': addToken(TokenType.COMMA);break;
            case ';': addToken(TokenType.SEMICOLON);break;
            case '+': addToken(TokenType.ADD);break;
            case '-': addToken(TokenType.SUB);break;
            case '*': addToken(TokenType.MUL);break;

            case '=': addToken(match('=')?TokenType.EQ : TokenType.ASSIGN); break;
            case '!': addToken(match('=')?TokenType.NOT_EQ : TokenType.UNKNOWN); break;
            case '<': addToken(match('=')?TokenType.LT_EQ : TokenType.LT); break;
            case '>': addToken(match('=')?TokenType.GT_EQ : TokenType.GT); break;

            case '/':
                if(match('/')){
                    while(!isAtEnd() && peek() != '\n'){
                        advance();
                    }
                }
                else{
                    addToken(TokenType.DIV);
                }
                break;

            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                break;
            default:
                if (Character.isDigit(c)) {
                    number();
                } else if (Character.isLetter(c) || c == '_') {
                    identifier();
                } else {
                    addToken(TokenType.UNKNOWN);
                }
            break;
        }
    }
}