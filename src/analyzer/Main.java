package analyzer;

public class Main {
    public static void main(String[] args) {
        if (LexicalAnalyzer.lexicalAnalysis(args[0])) {
            SyntaxAnalyzer.syntaxAnalysis("./Output.txt");
        }
    }
}
