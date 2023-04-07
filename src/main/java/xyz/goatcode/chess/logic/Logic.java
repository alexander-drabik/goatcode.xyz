package xyz.goatcode.chess.logic;

public class Logic {
    /**
     * Validates a chess move
     * @param board Array of all pieces
     * @param org   Current/Original position of a piece
     * @param next  Position to which piece is being moved
     **/
    public static boolean isMoveValid(Short[] board, int org, int next) {
        var squares = getPossibleMoves(board, org);

        return (squares[next] == 2);
    }

    public static short[] getPossibleMoves(Short[] board, int org) {
        var piece = board[org];
        short[] squares = new short[64];
        squares[org] = 1;

        var orgX = org%8;
        var orgY = org/8;
        switch (Math.abs(piece)) {
            // Pawn
            case 6:
                if (piece < 0) {
                    setBoard(squares, org, Direction.TopLeft, 2);
                    setBoard(squares, org, Direction.TopRight, 2);
                    if (orgY > 0) {
                        if (board[org-8] == 0) {
                            setBoard(squares, org, Direction.Top, 2);
                        }
                    }
                } else {
                    setBoard(squares, org, Direction.DownLeft, 2);
                    setBoard(squares, org, Direction.DownRight, 2);
                    if (orgY < 7) {
                        if (board[org+8] == 0) {
                            setBoard(squares, org, Direction.Down, 2);
                        }
                    }
                }
                break;
            // King
            case 1:
                setBoard(squares, org, Direction.Down, 2);
                setBoard(squares, org, Direction.Right, 2);
                setBoard(squares, org, Direction.Top, 2);
                setBoard(squares, org, Direction.Left, 2);
                setBoard(squares, org, Direction.TopLeft, 2);
                setBoard(squares, org, Direction.TopRight, 2);
                setBoard(squares, org, Direction.DownRight, 2);
                setBoard(squares, org, Direction.DownLeft, 2);
                break;
            case 4:
                if (orgY > 1) {
                    if (orgX > 0) {
                        squares[org-17] = 2;
                    }
                    if (orgX < 7) {
                        squares[org-15] = 2;
                    }
                }
                if (orgY > 0) {
                    if (orgX > 1) {
                        squares[org-8-2] = 2;
                    }
                    if (orgX < 6) {
                        squares[org-8+2] = 2;
                    }
                }
                if (orgY < 6) {
                    if (orgX > 0) {
                        squares[org+15] = 2;
                    }
                    if (orgX < 7) {
                        squares[org+17] = 2;
                    }
                }
                if (orgY < 7) {
                    if (orgX > 1) {
                        squares[org+8-2] = 2;
                    }
                    if (orgX < 6) {
                        squares[org+8+2] = 2;
                    }
                }
                for (int i = 0; i < 64; i++) {
                    if (squares[i] == 2 && Math.signum(board[i]) == Math.signum(piece)) {
                        squares[i] = 0;
                    }
                }
                break;
            // Queen
            case 2:
                setBoard(squares, org, Direction.Down, 2);
                setBoard(squares, org, Direction.Right, 2);
                setBoard(squares, org, Direction.Top, 2);
                setBoard(squares, org, Direction.Left, 2);
                setBoard(squares, org, Direction.TopLeft, 2);
                setBoard(squares, org, Direction.TopRight, 2);
                setBoard(squares, org, Direction.DownRight, 2);
                setBoard(squares, org, Direction.DownLeft, 2);

                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 + (i-orgX) * 8;
                    if (index > 63) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 - (i-orgX) * 8;
                    if (index < 0) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 + (i-orgX) * 8;
                    if (index < 0) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 - (i-orgX) * 8;
                    if (index > 63) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgY; i < 8; i++) {
                    if (orgY == i) continue;
                    int index = i * 8 + orgX;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgY; i >= 0; i--) {
                    if (orgY == i) continue;
                    int index = i * 8 + orgX;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                break;
            case 3:
                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 + (i-orgX) * 8;
                    if (index > 63) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 - (i-orgX) * 8;
                    if (index < 0) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 + (i-orgX) * 8;
                    if (index < 0) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8 - (i-orgX) * 8;
                    if (index > 63) {
                        break;
                    }
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                break;
            case 5:
                for (int i = orgX; i < 8; i++) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgX; i >= 0; i--) {
                    if (orgX == i) continue;
                    int index = i + orgY * 8;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgY; i < 8; i++) {
                    if (orgY == i) continue;
                    int index = i * 8 + orgX;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                for (int i = orgY; i >= 0; i--) {
                    if (orgY == i) continue;
                    int index = i * 8 + orgX;
                    squares[index] = 2;
                    if (board[index] != 0) {
                        break;
                    }
                }
                break;
        }

        return squares;
    }

    private static void setBoard(short[] board, int index, Direction direction, int value) {
        var x = index%8;
        var y = index/8;
        int index2 = switch (direction) {
            case Top -> {
                if (y > 0) {
                    yield index - 8;
                }
                yield index;
            }
            case Down -> {
                if (y < 7) {
                    yield index + 8;
                }
                yield index;
            }
            case Left -> {
                if (x > 0) {
                    yield index - 1;
                }
                yield index;
            }
            case Right -> {
                if (x < 7) {
                    yield index + 1;
                }
                yield index;
            }
            case TopLeft -> {
                if (x > 0 && y > 0) {
                    yield index - 9;
                }
                yield index;
            }
            case TopRight -> {
                if (x < 7 && y > 0) {
                    yield index - 7;
                }
                yield index;
            }
            case DownLeft -> {
                if (x > 0 && y < 7) {
                    yield index + 7;
                }
                yield index;
            }
            case DownRight -> {
                if (x > 0 && y < 7) {
                    yield index + 9;
                }
                yield index;
            }
        };

        board[index2] = (short) value;
    }

    /**
     * Moves a piece
     * @param board Array of all pieces
     * @param org   Current/Original position of a piece
     * @param next  Position to which piece is being moved
     **/
    public static void movePiece(Short[] board, int org, int next) {
        var piece = board[org];
        board[org] = 0;
        board[next] = piece;
    }
}
