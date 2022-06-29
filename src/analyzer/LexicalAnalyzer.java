package analyzer;

import java.io.*;
import java.util.ArrayList;

public class LexicalAnalyzer {
    // DFA transition graph 의 state 을 나타내는 변수.
    public static String state = "T0";

    // Input 파일의 한 줄을 읽을 때마다 사용하는 index 변수.
    public static int i = 0;

    // Token list 을 나타내는 변수.
    public static ArrayList<String> symbolTable = new ArrayList<String>();

    // Token value 을 나타내는 변수.
    public static String tokenValue = "";

    // Variable type token 을 확인하는 메소드.
    public static boolean isType(String s) {
        int temp = i;  // 문자열의 시작 index 을 저장하는 변수.
        boolean flag = true;  // Token 을 검사할 때 반복을 계속해야 할지를 나타내는 flag.
        while (flag) {
            try {
                switch (s.charAt(i)) {
                    case 'a':
                        if (state.equals("T7")) {
                            state = "T11";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'c':
                        if (state.equals("T0")) {
                            state = "T3";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'h':
                        if (state.equals("T3")) {
                            state = "T7";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'i':
                        if (state.equals("T0")) {
                            state = "T1";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'n':
                        if (state.equals("T1")) {
                            state = "T5";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'r':
                        if (state.equals("T11")) {
                            state = "T13";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 't':
                        if (state.equals("T5")) {
                            state = "T9";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'A':
                        if (state.equals("T8")) {
                            state = "T12";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'C':
                        if (state.equals("T0")) {
                            state = "T4";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'H':
                        if (state.equals("T4")) {
                            state = "T8";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'I':
                        if (state.equals("T0")) {
                            state = "T2";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'N':
                        if (state.equals("T2")) {
                            state = "T6";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'R':
                        if (state.equals("T12")) {
                            state = "T14";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'T':
                        if (state.equals("T6")) {
                            state = "T10";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    default:
                        flag = false;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        // 최종 state 가 final state 인지를 검사.
        switch (state) {
            case "T9":
            case "T10":
            case "T13":
            case "T14":
                return true;
            default:
                state = "T0";
                i = temp;
                return false;
        }
    }

    // Signed Integer token 을 확인하는 메소드.
    public static boolean isSignedInteger(String s) {
        int temp = i;  // 문자열의 시작 index 을 저장하는 변수.
        tokenValue = "";
        boolean flag = true;  // Token 을 검사할 때 반복을 계속해야 할지를 나타내는 flag.
        while (flag) {
            try {
                if (s.charAt(i) == '-') {
                    if (state.equals("T0")) {
                        state = "T1";
                        i++;
                    } else {
                        flag = false;
                    }
                } else if (s.charAt(i) == '0') {
                    switch (state) {
                        case "T0":
                            state = "T2";
                            i++;
                            break;
                        case "T3":
                        case "T4":
                            state = "T4";
                            i++;
                            break;
                        default:
                            flag = false;
                            break;
                    }
                } else if (s.charAt(i) >= '1' && s.charAt(i) <= '9') {
                    switch (state) {
                        case "T0":
                        case "T1":
                            state = "T3";
                            i++;
                            break;
                        case "T3":
                        case "T4":
                            state = "T4";
                            i++;
                            break;
                        default:
                            flag = false;
                            break;
                    }
                } else {
                    flag = false;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        // 최종 state 가 final state 인지를 검사.
        switch (state) {
            case "T2":
            case "T3":
            case "T4":
                tokenValue = s.substring(temp, i);
                boolean isOperator = false;  // 부호 -가 Arithmetic Operator 인지 나타내는 변수.
                if (tokenValue.charAt(0) == '-') {
                    for (int j = symbolTable.size() - 1; j >= 0; j--) {
                        if (symbolTable.get(j).startsWith("ID", 1)
                                || symbolTable.get(j).startsWith("INTEGER", 1)) {
                            isOperator = true;
                            break;
                        } else if (!symbolTable.get(j).equals("<WHITESPACE>")) {
                            break;
                        }
                    }
                    if (isOperator) {
                        state = "T0";
                        i = temp;
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            default:
                state = "T0";
                i = temp;
                return false;
        }
    }

    // String literal 을 확인하는 메소드.
    public static boolean isLiteralString(String s) {
        int temp = i;  // 문자열의 시작 index 을 저장하는 변수.
        if (s.charAt(i) == '"') {
            state = "T1";
            i++;
        } else {
            return false;
        }

        tokenValue = "";
        boolean flag = true;  // Token 을 검사할 때 반복을 계속해야 할지를 나타내는 flag.
        while (flag) {
            try {
                if (s.charAt(i) == '"') {
                    state = "T2";
                    flag = false;
                } else if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                    switch (state) {
                        case "T1":
                        case "T3":
                        case "T4":
                        case "T5":
                            state = "T3";
                            i++;
                            break;
                        default:
                            flag = false;
                            break;
                    }
                } else if ((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
                        || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z')) {
                    switch (state) {
                        case "T1":
                        case "T3":
                        case "T4":
                        case "T5":
                            state = "T4";
                            i++;
                            break;
                        default:
                            flag = false;
                            break;
                    }
                } else if (s.charAt(i) == ' ') {
                    switch (state) {
                        case "T1":
                        case "T3":
                        case "T4":
                        case "T5":
                            state = "T5";
                            i++;
                            break;
                        default:
                            flag = false;
                            break;
                    }
                } else {
                    flag = false;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        // 최종 state 가 final state 인지를 검사.
        if (state.equals("T2")) {
            tokenValue = s.substring(temp + 1, i);
            return true;
        } else {
            state = "T0";
            i = temp;
            return false;
        }
    }

    // Identifier 을 확인하는 메소드.
    public static boolean isIdentifier(String s) {
        int temp = i;  // 문자열의 시작 index 을 저장하는 변수.
        tokenValue = "";
        boolean flag = true;  // Token 을 검사할 때 반복을 계속해야 할지를 나타내는 flag.
        while (flag) {
            try {
                if ((s.charAt(i) >= 'A' && s.charAt(i) <= 'Z')
                        || (s.charAt(i) >= 'a' && s.charAt(i) <= 'z')) {
                    if (state.equals("T0")) {
                        state = "T1";
                    } else {
                        state = "T2";
                    }
                    i++;
                } else if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                    if (state.equals("T0")) {
                        flag = false;
                    } else {
                        state = "T3";
                        i++;
                    }
                } else {
                    flag = false;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        // 최종 state 가 final state 인지를 확인.
        if (state.equals("T0")) {
            state = "T0";
            i = temp;
            return false;
        } else {
            tokenValue = s.substring(temp, i);
            switch (tokenValue) {
                case "int":
                case "INT":
                case "char":
                case "CHAR":
                case "if":
                case "IF":
                case "else":
                case "ELSE":
                case "while":
                case "WHILE":
                case "return":
                case "RETURN":
                    state = "T0";
                    i = temp;
                    return false;
                default:
                    return true;
            }
        }
    }

    // IF, ELSE, WHILE, RETURN 키워드인지 확인하는 메소드.
    public static boolean isKeyword(String s) {
        int temp = i;
        boolean flag = true;
        while (flag) {
            try {
                switch (s.charAt(i)) {
                    case 'e':
                        switch (state) {
                            case "T0":
                                state = "T3";
                                i++;
                                break;
                            case "T7":
                                state = "T15";
                                i++;
                                break;
                            case "T17":
                                state = "T23";
                                i++;
                                break;
                            case "T25":
                                state = "T29";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'f':
                        if (state.equals("T1")) {
                            state = "T9";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'h':
                        if (state.equals("T5")) {
                            state = "T13";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'i':
                        switch (state) {
                            case "T0":
                                state = "T1";
                                i++;
                                break;
                            case "T13":
                                state = "T19";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'l':
                        switch (state) {
                            case "T3":
                                state = "T11";
                                i++;
                                break;
                            case "T19":
                                state = "T25";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'n':
                        if (state.equals("T31")) {
                            state = "T33";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'r':
                        switch (state) {
                            case "T0":
                                state = "T7";
                                i++;
                                break;
                            case "T27":
                                state = "T31";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 's':
                        if (state.equals("T11")) {
                            state = "T17";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 't':
                        if (state.equals("T15")) {
                            state = "T21";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'u':
                        if (state.equals("T21")) {
                            state = "T27";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'w':
                        if (state.equals("T0")) {
                            state = "T5";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'E':
                        switch (state) {
                            case "T0":
                                state = "T4";
                                i++;
                                break;
                            case "T8":
                                state = "T16";
                                i++;
                                break;
                            case "T18":
                                state = "T24";
                                break;
                            case "T26":
                                state = "T30";
                                break;
                            default:
                                break;
                        }
                        break;
                    case 'F':
                        if (state.equals("T2")) {
                            state = "T10";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'H':
                        if (state.equals("T6")) {
                            state = "T14";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'I':
                        switch (state) {
                            case "T0":
                                state = "T2";
                                i++;
                                break;
                            case "T14":
                                state = "T20";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'L':
                        switch (state) {
                            case "T4":
                                state = "T12";
                                i++;
                                break;
                            case "T20":
                                state = "T26";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'N':
                        if (state.equals("T32")) {
                            state = "T34";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'R':
                        switch (state) {
                            case "T0":
                                state = "T8";
                                i++;
                                break;
                            case "T28":
                                state = "T32";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case 'S':
                        if (state.equals("T12")) {
                            state = "T18";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'T':
                        if (state.equals("T16")) {
                            state = "T22";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'U':
                        if (state.equals("T22")) {
                            state = "T28";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case 'W':
                        if (state.equals("T0")) {
                            state = "T6";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    default:
                        flag = false;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        switch (state) {
            case "T9":
            case "T10":
            case "T23":
            case "T24":
            case "T29":
            case "T30":
            case "T33":
            case "T34":
                return true;
            default:
                state = "T0";
                i = temp;
                return false;
        }
    }

    // Arithmetic Operator 인지 확인하는 메소드.
    public static boolean isArithmeticOperator(char c) {
        switch (c) {
            case '+':
                state = "T1";
                return true;
            case '-':
                state = "T2";
                return true;
            case '*':
                state = "T3";
                return true;
            case '/':
                state = "T4";
                return true;
            default:
                return false;
        }
    }

    // Assignment Operator 인지 확인하는 메소드.
    public static boolean isAssignmentOperator(char c) {
        return c == '=';
    }

    // Comparison Operator 인지 확인하는 메소드.
    public static boolean isComparisonOperator(String s) {
        int temp = i;
        boolean flag = true;
        while (flag) {
            try {
                switch (s.charAt(i)) {
                    case '<':
                        if (state.equals("T0")) {
                            state = "T1";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case '>':
                        if (state.equals("T0")) {
                            state = "T2";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    case '=':
                        switch (state) {
                            case "T0":
                                state = "T3";
                                i++;
                                break;
                            case "T1":
                                state = "T5";
                                i++;
                                break;
                            case "T2":
                                state = "T6";
                                i++;
                                break;
                            case "T3":
                                state = "T7";
                                i++;
                                break;
                            case "T4":
                                state = "T8";
                                i++;
                                break;
                            default:
                                flag = false;
                                break;
                        }
                        break;
                    case '!':
                        if (state.equals("T0")) {
                            state = "T4";
                            i++;
                        } else {
                            flag = false;
                        }
                        break;
                    default:
                        flag = false;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                flag = false;
            }
        }

        switch (state) {
            case "T1":
            case "T2":
            case "T5":
            case "T6":
            case "T7":
            case "T8":
                return true;
            default:
                state = "T0";
                i = temp;
                return false;
        }
    }

    // Semicolon 인지 확인하는 메소드
    public static boolean isSemicolon(char c) {
        return c == ';';
    }

    // Left brace 인지 확인하는 메소드.
    public static boolean isLbrace(char c) {
        return c == '{';
    }

    // Right brace 인지 확인하는 메소드.
    public static boolean isRbrace(char c) {
        return c == '}';
    }

    // Left parenthesis 인지 확인하는 메소드.
    public static boolean isLparen(char c) {
        return c == '(';
    }

    // Right parenthesis 인지 확인하는 메소드.
    public static boolean isRparen(char c) {
        return c == ')';
    }

    // Comma 인지 확인하는 메소드.
    public static boolean isComma(char c) {
        return c == ',';
    }

    // Whitespace 인지 확인하는 메소드.
    public static boolean isWhitespace(char c) {
        return (c == '\t' || c == '\n' || c == ' ');
    }

    public static boolean lexicalAnalysis(String filename) {
        String line = "";  // Input 파일을 읽을 때 한 줄을 나타내는 변수.
        boolean isError = false;  // Input 파일에 오류가 있는지 나타내는 변수.
        int lineNumber = 0;  // 오류가 있을 경우 오류의 line number 을 나타내는 변수.

        try {
            File file = new File(filename);
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(filereader);

            // input 파일의 line 한 줄을 반복해서 읽음.
            Outter: while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                i = 0;
                while (i < line.length()) {
                    state = "T0";  // 시작 state = T0
                    if (isWhitespace(line.charAt(i))) {
                        symbolTable.add("<WHITESPACE>");
                        i++;
                    } else if (isComma(line.charAt(i))) {
                        symbolTable.add("<COMMA>");
                        i++;
                    } else if (isLparen(line.charAt(i))) {
                        symbolTable.add("<LPAREN>");
                        i++;
                    } else if (isRparen(line.charAt(i))) {
                        symbolTable.add("<RPAREN>");
                        i++;
                    } else if (isLbrace(line.charAt(i))) {
                        symbolTable.add("<LBRACE>");
                        i++;
                    } else if (isRbrace(line.charAt(i))) {
                        symbolTable.add("<RBRACE>");
                        i++;
                    } else if (isSemicolon(line.charAt(i))) {
                        symbolTable.add("<SEMI>");
                        i++;
                    } else if (isComparisonOperator(line)) {
                        switch (state) {
                            case "T1":
                                symbolTable.add("<COMPARISON, <>");
                                break;
                            case "T2":
                                symbolTable.add("<COMPARISON, >>");
                                break;
                            case "T5":
                                symbolTable.add("<COMPARISON, <=>");
                                break;
                            case "T6":
                                symbolTable.add("<COMPARISON, >=>");
                                break;
                            case "T7":
                                symbolTable.add("<COMPARISON, ==>");
                                break;
                            case "T8":
                                symbolTable.add("<COMPARISON, !=>");
                                break;
                            default:
                                break;
                        }
                    } else if (isAssignmentOperator(line.charAt(i))) {
                        symbolTable.add("<ASSIGN>");
                        i++;
                    }  else if (isSignedInteger(line)) {
                        switch (state) {
                            case "T2":
                            case "T3":
                            case "T4":
                                symbolTable.add("<INTEGER, " + tokenValue + ">");
                                break;
                            default:
                                break;
                        }
                        tokenValue = "";
                    } else if (isArithmeticOperator(line.charAt(i))) {
                        switch (state) {
                            case "T1":
                                symbolTable.add("<ADDSUB, +>");
                                break;
                            case "T2":
                                symbolTable.add("<ADDSUB, ->");
                                break;
                            case "T3":
                                symbolTable.add("<MULTIDIV, *>");
                                break;
                            case "T4":
                                symbolTable.add("<MULTIDIV, />");
                                break;
                            default:
                                break;
                        }
                        i++;
                    } else if (isLiteralString(line)) {
                        if (state.equals("T2")) {
                            symbolTable.add("<STRING, " + tokenValue + ">");
                        }
                        tokenValue = "";
                        i++;
                    } else if (isIdentifier(line)) {
                        switch (state) {
                            case "T1":
                            case "T2":
                            case "T3":
                                symbolTable.add("<ID, " + tokenValue + ">");
                                break;
                            default:
                                break;
                        }
                        tokenValue = "";
                    } else if (isType(line)) {
                        switch (state) {
                            case "T9":
                            case "T10":
                                symbolTable.add("<VTYPE, int>");
                                break;
                            case "T13":
                            case "T14":
                                symbolTable.add("<VTYPE, char>");
                                break;
                            default:
                                break;
                        }
                    } else if (isKeyword(line)) {
                        switch (state) {
                            case "T9":
                            case "T10":
                                symbolTable.add("<IF>");
                                break;
                            case "T23":
                            case "T24":
                                symbolTable.add("<ELSE>");
                                break;
                            case "T29":
                            case "T30":
                                symbolTable.add("<WHILE>");
                                break;
                            case "T33":
                            case "T34":
                                symbolTable.add("<return>");
                                break;
                            default:
                                break;
                        }
                    } else {
                        isError = true;
                        break Outter;
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            // 파일이 존재하지 않아 input 파일을 읽을 수 없음.
            System.out.println("The file is NOT present.");
            return false;
        }

        try {
            File file = new File("./Output.txt");
            FileWriter fileWriter = new FileWriter(file, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            if (isError) {  // 오류가 난 위치와 이유를 설명함.
                System.out.println("Lexical analysis failed.");
                printWriter.println("Error:");
                printWriter.println();
                printWriter.print(lineNumber);
                printWriter.println(": " + line);
                printWriter.print("==> Invalid character or Unidentified token.");
                printWriter.close();
                return false;
            } else {  // Whitespace 을 제외한 token list 을 출력함.
                for (String token : symbolTable) {
                    if (!token.equals("<WHITESPACE>")) {
                        printWriter.println(token);
                    }
                }
                System.out.println("Lexical analysis is complete.");
                printWriter.close();
                return true;
            }
        } catch (IOException e) {
            System.out.println("The file can't be created.");
            return false;
        }
    }
}
