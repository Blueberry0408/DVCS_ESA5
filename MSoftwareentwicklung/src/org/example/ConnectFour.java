package org.example;

import java.util.Scanner;

/**
 * ConnectFour is a simple two-player connection game in which the players
 * first choose a symbol and then take turns dropping one symbol disc
 * from the top into a seven-column, six-row vertically suspended grid.
 * The pieces fall straight down, occupying the lowest available space
 * within the column. The object of the game is to connect four of one's
 * own discs of the same symbol consecutively in a horizontal, vertical,
 * or diagonal row before the opponent.
 * by Aaliyah Roderer for CCD
 * added comment through GitHub for Pull on IntelliJ
 */
public class ConnectFour {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY_CELL = '-';
    private static final int WINNING_LENGTH = 4;
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private char[][] board;

    /**
     * Constructs a new ConnectFour game.
     * <p>
     * This constructor initializes the game board with empty cells and sets up the initial state.
     * </p>
     */
    public ConnectFour() {
        board = new char[ROWS][COLUMNS];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = EMPTY_CELL;
            }
        }
    }

    private void printColumnNumbers() {
        for (int col = 1; col <= COLUMNS; col++) {
            System.out.print(col + "   ");
        }
        System.out.println();
    }

    private void printBoard() {
        printColumnNumbers();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                System.out.print(board[row][col] + " ");
                if (col < COLUMNS - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
        }
    }

    private boolean isColumnFull(final int col) {
        return board[0][col] != EMPTY_CELL;
    }

    private int dropToken(final int col, final char token) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY_CELL) {
                board[row][col] = token;
                return row;
            }
        }
        return -1;
    }

    private boolean checkWin(final int row, final int col, final char token) {
        return checkHorizontal(row, token)
                || checkVertical(col, token)
                || checkDiagonals(row, col, token);
    }

    private boolean checkHorizontal(final int row, final char token) {
        for (int c = 0; c <= COLUMNS - WINNING_LENGTH; c++) {
            if (areTokensEqual(board[row][c],
                    board[row][c + 1],
                    board[row][c + 2],
                    board[row][c + 3],
                    token)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(final int col, final char token) {
        for (int r = 0; r <= ROWS - WINNING_LENGTH; r++) {
            if (areTokensEqual(board[r][col],
                    board[r + 1][col],
                    board[r + 2][col],
                    board[r + 3][col],
                    token)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals(final int row, final int col, final char token) {
        return checkDiagonalDownRight(row, col, token) || checkDiagonalUpRight(row, col, token);
    }

    private boolean checkDiagonalDownRight(final int row, final int col, final char token) {
        for (int r = 0; r <= ROWS - WINNING_LENGTH; r++) {
            for (int c = 0; c <= COLUMNS - WINNING_LENGTH; c++) {
                if (areTokensEqual(
                        board[r][c],
                        board[r + 1][c + 1],
                        board[r + 2][c + 2],
                        board[r + 3][c + 3],
                        token)) {
                    return true;
                }

            }
        }
        return false;
    }

    private boolean checkDiagonalUpRight(final int row, final int col, final char token) {
        for (int r = 0; r <= ROWS - WINNING_LENGTH; r++) {
            for (int c = WINNING_LENGTH - 1; c < COLUMNS; c++) {
                if (areTokensEqual(board[r][c],
                        board[r + 1][c - 1],
                        board[r + 2][c - 2],
                        board[r + 3][c - 3],
                        token)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean areTokensEqual(final char... tokens) {
        for (char t : tokens) {
            if (t != tokens[0]) {
                return false;
            }
        }
        return tokens[0] == tokens[1];
    }


    private boolean isBoardFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (!isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    private void playGame() {
        Scanner scanner = new Scanner(System.in);

        char currentPlayer = PLAYER_X;
        boolean gameWon = false;

        while (!gameWon) {
            printBoard();
            System.out.println("Spieler " + currentPlayer + ", wähle eine Spalte (1-" + COLUMNS + "): ");

            int chosenColumn = getPlayerMove(scanner);

            if (isValidMove(chosenColumn)) {
                int row = dropToken(chosenColumn, currentPlayer);

                if (checkWin(row, chosenColumn, currentPlayer)) {
                    gameWon = true;
                    printBoard();
                    announceWinner(currentPlayer);
                } else if (isBoardFull()) {
                    gameWon = true;
                    printBoard();
                    announceDraw();
                } else {
                    currentPlayer = switchPlayer(currentPlayer);
                }
            } else {
                System.out.println("Ungültiger Zug. Bitte erneut versuchen.");
            }
        }
        scanner.close();
    }

    private int getPlayerMove(final Scanner scanner) {
        while (true) {
            String input = scanner.next();

            try {
                int move = Integer.parseInt(input) - 1;

                if (isValidMove(move)) {
                    return move;
                } else {
                    System.out.println("Ungültiger Zug. Bitte erneut versuchen.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
            }
        }
    }

    private char switchPlayer(final char currentPlayer) {
        return (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private boolean isValidMove(final int col) {
        return col >= 0 && col < COLUMNS && !isColumnFull(col);
    }

    private void announceWinner(final char currentPlayer) {
        System.out.println("Spieler " + currentPlayer + " gewinnt!");
    }

    private void announceDraw() {
        System.out.println("Es ist unentschieden!");
    }

    /**
     * The main entry point for the ConnectFour application.
     * <p>
     * This method initializes a new ConnectFour game and starts the game loop.
     * </p>
     * @param args The command-line arguments (not used).
     */
    public static void main(final String[] args) {
        ConnectFour connectFour = new ConnectFour();
        connectFour.playGame();
    }

}
