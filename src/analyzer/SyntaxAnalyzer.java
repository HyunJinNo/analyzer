package analyzer;

import java.io.*;
import java.util.*;

public class SyntaxAnalyzer {
    // SLR Table 의 현재 state 을 나타내는 변수
    private static int state = 0;

    // Right substring 의 다음 Input Symbol 을 나타내는 변수
    private static String nextSymbol = "";

    // Lexical Analysis 을 통해 얻어낸 모든 token 을 담는 변수
    private static Queue<String> symbolTable = new LinkedList<String>();

    // state 정보를 저장하기 위해 사용하는 Stack
    private static Stack<Integer> states = new Stack<Integer>();

    // Left substring 을 나타내는 변수
    private static Stack<String> leftStr = new Stack<String>();

    // Parsing 과정의 Step 을 나타내는 변수
    private static int step = 1;

    private static File file;
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;

    public static void syntaxAnalysis(String filename) {
        // Output.txt 파일에서 한 줄씩 읽어낼 때 사용하는 변수
        String line;

        try {
            File file = new File(filename);
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(filereader);

            while ((line = bufferedReader.readLine()) != null) {
                symbolTable.offer(line.substring(1, line.length() - 1).split(",")[0]);
            }
            symbolTable.offer("$");  // String 의 END 을 표시하는 문자.

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            file = new File("./Result.txt");
            fileWriter = new FileWriter(file, false);
            printWriter = new PrintWriter(fileWriter);

            states.push(state);  // 현재 state 을 stack 에 push.
            nextSymbol = symbolTable.poll();  // 다음 Input Symbol 을 읽어냄.

            // Syntax Analysis 가 성공했는지를 나타내는 변수로
            // true 이면 Accepted, false 이면 rejected 을 나타낸다.
            boolean flag = true;

            Outter: while (!states.isEmpty()) {
                // stack 에 가장 마지막으로 저장되어 있는 state 을 확인함.
                state = states.peek();

                // 현재 Step, State, Left substring, right substring 을 출력함.
                printWriter.println("Step: " + step);
                printWriter.println("State: " + state);
                if (leftStr.isEmpty()) {
                    printWriter.println("Left substring: ");
                } else {
                    printWriter.print("Left substring: ");
                    for (String str : leftStr) {
                        printWriter.print(str + " ");
                    }
                    printWriter.println();
                }
                printWriter.print("Right substring: " + nextSymbol + " ");
                for (String str : symbolTable) {
                    printWriter.print(str + " ");
                }
                printWriter.println("\n");

                // 아래 switch 문은 현재 state 와 다음 Input symbol 에 따라
                // shift and goto 과정을 거칠지, 아니면 reduce and goto 과정을 거칠지를 결정한다.
                // 만약 현재 state 에서 잘못된 Input symbol 을 받았을 경우
                // Lexical Analysis 을 통해 생성된 sequence of token 들이
                // CFG 에서 생성될 수 었는 것이므로
                // 오류를 출력한다.
                switch (state) {
                    case 0:
                    case 2:
                    case 3:
                        switch (nextSymbol) {
                            case "VTYPE":
                                shiftAndGoTo(4);
                                break;
                            case "$":
                                reduce(3);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 1:
                        if ("$".equals(nextSymbol)) {
                            flag = true;
                            break Outter;
                        } else {
                            flag = false;
                        }
                        break;
                    case 4:
                        if ("ID".equals(nextSymbol)) {
                            shiftAndGoTo(7);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 5:
                        if ("$".equals(nextSymbol)) {
                            reduce(1);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 6:
                        if ("$".equals(nextSymbol)) {
                            reduce(2);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 7:
                        switch (nextSymbol) {
                            case "SEMI":
                                shiftAndGoTo(8);
                                break;
                            case "LPAREN":
                                shiftAndGoTo(9);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 8:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "ID":
                            case "RBRACE":
                            case "IF":
                            case "WHILE":
                            case "return":
                            case "$":
                                reduce(4);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 9:
                        switch (nextSymbol) {
                            case "VTYPE":
                                shiftAndGoTo(11);
                                break;
                            case "RPAREN":
                                reduce(7);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 10:
                        if ("RPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(12);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 11:
                        if ("ID".equals(nextSymbol)) {
                            shiftAndGoTo(13);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 12:
                        if ("LBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(14);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 13:
                    case 32:
                        switch (nextSymbol) {
                            case "RPAREN":
                                reduce(9);
                                break;
                            case "COMMA":
                                shiftAndGoTo(16);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 14:
                    case 18:
                    case 58:
                    case 60:
                    case 66:
                        switch (nextSymbol) {
                            case "VTYPE":
                                shiftAndGoTo(23);
                                break;
                            case "ID":
                                shiftAndGoTo(20);
                                break;
                            case "RBRACE":
                            case "return":
                                reduce(11);
                                break;
                            case "IF":
                                shiftAndGoTo(21);
                                break;
                            case "WHILE":
                                shiftAndGoTo(22);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 15:
                        if ("RPAREN".equals(nextSymbol)) {
                            reduce(6);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 16:
                        if ("VTYPE".equals(nextSymbol)) {
                            shiftAndGoTo(24);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 17:
                        if ("return".equals(nextSymbol)) {
                            shiftAndGoTo(26);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 19:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "ID":
                            case "RBRACE":
                            case "IF":
                            case "WHILE":
                            case "return":
                                reduce(12);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 20:
                        if ("ASSIGN".equals(nextSymbol)) {
                            shiftAndGoTo(28);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 21:
                        if ("LPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(29);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 22:
                        if ("LPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(30);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 23:
                        if ("ID".equals(nextSymbol)) {
                            shiftAndGoTo(31);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 24:
                        if ("ID".equals(nextSymbol)) {
                            shiftAndGoTo(32);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 25:
                        if ("RBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(33);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 26:
                    case 29:
                    case 30:
                    case 35:
                    case 50:
                    case 51:
                    case 53:
                        switch (nextSymbol) {
                            case "ID":
                                shiftAndGoTo(36);
                                break;
                            case "LPAREN":
                                shiftAndGoTo(35);
                                break;
                            case "INTEGER":
                                shiftAndGoTo(37);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 27:
                        switch (nextSymbol) {
                            case "RBRACE":
                            case "return":
                                reduce(10);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 28:
                        switch (nextSymbol) {
                            case "ID":
                                shiftAndGoTo(36);
                                break;
                            case "LPAREN":
                                shiftAndGoTo(35);
                                break;
                            case "STRING":
                                shiftAndGoTo(40);
                                break;
                            case "INTEGER":
                                shiftAndGoTo(37);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 31:
                        if ("SEMI".equals(nextSymbol)) {
                            shiftAndGoTo(8);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 33:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "$":
                                reduce(5);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 34:
                        if ("SEMI".equals(nextSymbol)) {
                            shiftAndGoTo(47);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 36:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                            case "ADDSUB":
                            case "MUITIDIV":
                            case "COMPARISON":
                                reduce(23);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 37:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                            case "ADDSUB":
                            case "MULTIDIV":
                            case "COMPARISON":
                                reduce(24);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 38:
                        if ("SEMI".equals(nextSymbol)) {
                            shiftAndGoTo(49);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 39:
                        if ("SEMI".equals(nextSymbol)) {
                            reduce(16);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 40:
                        if ("SEMI".equals(nextSymbol)) {
                            reduce(17);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 41:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                                reduce(19);
                                break;
                            case "ADDSUB":
                                shiftAndGoTo(50);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 42:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                            case "ADDSUB":
                                reduce(21);
                                break;
                            case "MULTIDIV":
                                shiftAndGoTo(51);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 43:
                        if ("RPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(52);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 44:
                        if ("COMPARISON".equals(nextSymbol)) {
                            shiftAndGoTo(53);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 45:
                        if ("RPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(54);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 46:
                        if ("RPAREN".equals(nextSymbol)) {
                            reduce(8);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 47:
                        if ("RBRACE".equals(nextSymbol)) {
                            reduce(26);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 48:
                        if ("RPAREN".equals(nextSymbol)) {
                            shiftAndGoTo(55);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 49:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "ID":
                            case "RBRACE":
                            case "IF":
                            case "WHILE":
                            case "return":
                                reduce(13);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 52:
                        if ("LBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(58);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 54:
                        if ("LBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(60);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 55:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                            case "ADDSUB":
                            case "MULTIDIV":
                            case "COMPARISON":
                                reduce(22);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 56:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                                reduce(18);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 57:
                        switch (nextSymbol) {
                            case "SEMI":
                            case "RPAREN":
                            case "ADDSUB":
                                reduce(20);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 59:
                        if ("RPAREN".equals(nextSymbol)) {
                            reduce(25);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 61:
                        if ("RBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(63);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 62:
                        if ("RBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(64);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 63:
                        if ("ELSE".equals(nextSymbol)) {
                            shiftAndGoTo(65);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 64:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "ID":
                            case "RBRACE":
                            case "IF":
                            case "WHILE":
                            case "return":
                                reduce(15);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                    case 65:
                        if ("LBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(66);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 67:
                        if ("RBRACE".equals(nextSymbol)) {
                            shiftAndGoTo(68);
                        } else {
                            flag = false;
                            break Outter;
                        }
                        break;
                    case 68:
                        switch (nextSymbol) {
                            case "VTYPE":
                            case "ID":
                            case "RBRACE":
                            case "IF":
                            case "WHILE":
                            case "return":
                                reduce(14);
                                break;
                            default:
                                flag = false;
                                break Outter;
                        }
                        break;
                }
            }

            // Syntax Analysis 가 성공했으면 Accepted,
            // 실패했으면 Rejected 을 출력.
            if (flag) {
                System.out.println("Syntax analysis is complete.");
                printWriter.println("Accepted!");
            } else {
                System.out.println("Syntax analysis failed.");
                printWriter.println("------------------------------------------");
                printWriter.println("Rejected: ");
                printWriter.println("Current state is " + state + ", and next input Symbol is " + nextSymbol + ".");
                printWriter.println("According to the SLR parsing table based on the CFG, " +
                        "there isn't ACTION(" + state + ", " + nextSymbol + ").");
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println("The Result file can't be created.");
        }
    }

    // Shift and Goto 역할을 하는 메소드
    private static void shiftAndGoTo(int state) {
        goTo(state);
        leftStr.push(nextSymbol);
        nextSymbol = symbolTable.poll();
    }

    // Goto 역할을 하는 메소드
    private static void goTo(int state) {
        states.push(state);
        step++;
    }

    // Reduce 역할을 하는 메소드
    public static void reduce(int num) {
        step++;

        // 현재 Step, State, Left substring, right substring 을 출력함.
        printWriter.println("Step: " + step);
        printWriter.println("State: " + state);
        if (leftStr.isEmpty()) {
            printWriter.println("Left substring: ");
        } else {
            printWriter.print("Left substring: ");
            for (String str : leftStr) {
                printWriter.print(str + " ");
            }
            printWriter.println();
        }
        printWriter.print("Right substring: " + nextSymbol + " ");
        for (String str : symbolTable) {
            printWriter.print(str + " ");
        }
        printWriter.println("\n");

        // 아래 switch 문은 다음 역할을 수행한다.
        // (1) CFG 에 따라 handle 을 reduce 한다.
        // (2) 이 과정에서 stack 에 저장되어 있던 state 값들을 pop 한다.
        switch (num) {
            case 0:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("CODE");
                break;
            case 1:
            case 2:
                for (int i = 0; i < 2; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("DECL");
                break;
            case 3:
                state = states.peek();
                leftStr.push("DECL");
                break;
            case 4:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("VDECL");
                break;
            case 5:
                for (int i = 0; i < 9; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("FDECL");
                break;
            case 6:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("ARG");
                break;
            case 7:
                state = states.peek();
                leftStr.push("ARG");
                break;
            case 8:
                for (int i = 0; i < 4; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("MOREARGS");
                break;
            case 9:
                state = states.peek();
                leftStr.push("MOREARGS");
                break;
            case 10:
                for (int i = 0; i < 2; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("BLOCK");
                break;
            case 11:
                state = states.peek();
                leftStr.push("BLOCK");
                break;
            case 12:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("STMT");
                break;
            case 13:
                for (int i = 0; i < 4; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("STMT");
                break;
            case 14:
                for (int i = 0; i < 11; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("STMT");
                break;
            case 15:
                for (int i = 0; i < 7; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("STMT");
                break;
            case 16:
            case 17:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("RHS");
                break;
            case 18:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("EXPR");
                break;
            case 19:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("EXPR");
                break;
            case 20:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("TERM");
                break;
            case 21:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("TERM");
                break;
            case 22:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("FACTOR");
                break;
            case 23:
            case 24:
                states.pop();
                leftStr.pop();
                state = states.peek();
                leftStr.push("FACTOR");
                break;
            case 25:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("COND");
                break;
            case 26:
                for (int i = 0; i < 3; i++) {
                    states.pop();
                    leftStr.pop();
                }
                state = states.peek();
                leftStr.push("RETURN");
                break;
        }

        // 아래 switch 문은 reduce 이후
        // 현재 state 와 reduce 된 Symbol 에 따라
        // GOTO 과정을 거쳐서 변화된 state 값을 stack 에
        // push 하는 역할을 수행한다.
        switch (state) {
            case 0:
                switch (leftStr.peek()) {
                    case "DECL":
                        goTo(1);
                        break;
                    case "VDECL":
                        goTo(2);
                        break;
                    case "FDECL":
                        goTo(3);
                        break;
                }
                break;
            case 2:
                switch (leftStr.peek()) {
                    case "CODE":
                        goTo(5);
                        break;
                    case "DECL":
                        goTo(1);
                        break;
                    case "VDECL":
                        goTo(2);
                        break;
                    case "FDECL":
                        goTo(3);
                        break;
                }
                break;
            case 3:
                switch (leftStr.peek()) {
                    case "CODE":
                        goTo(6);
                        break;
                    case "DECL":
                        goTo(1);
                        break;
                    case "VDECL":
                        goTo(2);
                        break;
                    case "FDECL":
                        goTo(3);
                        break;
                }
                break;
            case 9:
                if ("ARG".equals(leftStr.peek())) {
                    goTo(10);
                }
                break;
            case 13:
                if ("MOREARGS".equals(leftStr.peek())) {
                    goTo(15);
                }
                break;
            case 14:
                switch (leftStr.peek()) {
                    case "VDECL":
                        goTo(19);
                        break;
                    case "BLOCK":
                        goTo(17);
                        break;
                    case "STMT":
                        goTo(18);
                        break;
                }
                break;
            case 17:
                if ("RETURN".equals(leftStr.peek())) {
                    goTo(25);
                }
                break;
            case 18:
                switch (leftStr.peek()) {
                    case "VDECL":
                        goTo(19);
                        break;
                    case "BLOCK":
                        goTo(27);
                        break;
                    case "STMT":
                        goTo(18);
                        break;
                }
                break;
            case 26:
                if ("FACTOR".equals(leftStr.peek())) {
                    goTo(34);
                }
                break;
            case 28:
                switch (leftStr.peek()) {
                    case "RHS":
                        goTo(38);
                        break;
                    case "EXPR":
                        goTo(39);
                        break;
                    case "TERM":
                        goTo(41);
                        break;
                    case "FACTOR":
                        goTo(42);
                        break;
                }
                break;
            case 29:
                switch (leftStr.peek()) {
                    case "FACTOR":
                        goTo(44);
                        break;
                    case "COND":
                        goTo(43);
                        break;
                }
                break;
            case 30:
                switch (leftStr.peek()) {
                    case "FACTOR":
                        goTo(44);
                        break;
                    case "COND":
                        goTo(45);
                        break;
                }
                break;
            case 32:
                if ("MOREARGS".equals(leftStr.peek())) {
                    goTo(46);
                }
                break;
            case 35:
                switch (leftStr.peek()) {
                    case "EXPR":
                        goTo(48);
                        break;
                    case "TERM":
                        goTo(41);
                        break;
                    case "FACTOR":
                        goTo(42);
                        break;
                }
                break;
            case 50:
                switch (leftStr.peek()) {
                    case "EXPR":
                        goTo(56);
                        break;
                    case "TERM":
                        goTo(41);
                        break;
                    case "FACTOR":
                        goTo(42);
                        break;
                }
                break;
            case 51:
                switch (leftStr.peek()) {
                    case "TERM":
                        goTo(57);
                        break;
                    case "FACTOR":
                        goTo(42);
                        break;
                }
                break;
            case 53:
                if ("FACTOR".equals(leftStr.peek())) {
                    goTo(59);
                }
                break;
            case 58:
                switch (leftStr.peek()) {
                    case "VDECL":
                        goTo(19);
                        break;
                    case "BLOCK":
                        goTo(61);
                        break;
                    case "STMT":
                        goTo(18);
                        break;
                }
                break;
            case 60:
                switch (leftStr.peek()) {
                    case "VDECL":
                        goTo(19);
                        break;
                    case "BLOCK":
                        goTo(62);
                        break;
                    case "STMT":
                        goTo(18);
                        break;
                }
                break;
            case 66:
                switch (leftStr.peek()) {
                    case "VDECL":
                        goTo(19);
                        break;
                    case "BLOCK":
                        goTo(67);
                        break;
                    case "STMT":
                        goTo(18);
                        break;
                }
                break;
            default:
                break;
        }
    }
}
