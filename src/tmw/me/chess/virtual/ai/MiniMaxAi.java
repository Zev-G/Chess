package tmw.me.chess.virtual.ai;

import javafx.application.Platform;
import tmw.me.chess.ui.board.Board;
import tmw.me.chess.virtual.Team;
import tmw.me.chess.virtual.VirtualBoard;
import tmw.me.chess.virtual.VirtualPiece;
import tmw.me.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public abstract class MiniMaxAi extends AiBase  {

    private static final boolean LOG = true;

    private static final int MAX_TABLE_SIZE = 200000;
    private static final boolean USE_TRANSPOSITION_TABLE = true;
    private static final boolean PRUNING = true;
    private static final int DRAW_ARROWS_AT_DEPTH = -1;

    protected final int depth;
    protected boolean runOnSeparateThread = true;

    protected double minWaitTime = 250;

    protected int calculations = 0;
    protected int valueChecks = 0;
    private int savedValueChecks = 0;
    private int quiescentSearches = 0;

    private final ArrayList<String> addOrder = new ArrayList<>();
    private final HashMap<String, Integer> transpositionTable = new HashMap<>();

    private final boolean useQuiescentSearch;

    protected MiniMaxAi(int depth, boolean useQuiescentSearch) {
        super();
        this.depth = depth;
        this.useQuiescentSearch = useQuiescentSearch;
    }

    @Override
    public void move(VirtualBoard board, Team team, Board visualBoard) {
        new Thread(() -> {
            if (transpositionTable.size() > MAX_TABLE_SIZE) {
                if (LOG)
                    System.out.println("Trimming table.");
                while (transpositionTable.size() > MAX_TABLE_SIZE) {
                    String zero = addOrder.get(0);
                    addOrder.remove(zero);
                    transpositionTable.remove(zero);
                }
            }
            if (LOG)
                System.out.println("[" + Calendar.getInstance().getTime().toString() + "] Running minimax...");

            long currentTime = Calendar.getInstance().getTimeInMillis();
            calculations = 0;
            valueChecks = 0;
            MoveAndValue result = negamax(board, depth, -1000000000, 1000000000, team);
            if (LOG)
                System.out.println("Finished. Value: " + result.getValue() + " Move: " + result.getMove());
            long dif = (Calendar.getInstance().getTimeInMillis() - currentTime) + 1;
            if (LOG)
                System.out.println("Took " + ((double) dif / 1000) + " seconds to do " + calculations + " simulated moves.");
//            System.out.println("In that time frame " + valueChecks + " board evaluations were done. And " + savedValueChecks + " were pulled from cache. Cache size: " + transpositionTable.size());


            if (dif < minWaitTime && visualBoard != null) {
                try {
                    Thread.sleep((long) (minWaitTime - dif));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ArrayList<Move> moves = board.genMovesForTeam(team);
            if (result.getMove() != null) {
                for (Move move : moves) {
                    if (move.getPiece().equals(result.getMove().getPiece()) && move.getLoc().equals(result.getMove().getLoc())) {
                        if (visualBoard != null) {
                            Platform.runLater(() -> visualBoard.animateMove(move, visualBoard.getBoardSpotAtSpot(move.getPiece().getLocation()).getPiece()));
                        } else {
                            move.doMove(board, true);
                        }
                        return;
                    }
                }
            } else {
                int random = (int) (Math.random() * (moves.size() - 1));
                Move move = moves.get(random);
                if (visualBoard != null) {
                    Platform.runLater(() -> visualBoard.animateMove(move, visualBoard.getBoardSpotAtSpot(move.getPiece().getLocation()).getPiece()));
                } else {
                    move.doMove(board, true);
                }
                return;
            }
            System.err.println("Didnt find move");
        }).start();

    }

    private MoveAndValue minimax(VirtualBoard position, int depth, int alpha, int beta, Team maximizingPlayer) {
        ArrayList<Move> moves = position.genBiLegalMovesForTeam(maximizingPlayer);
        orderMoves(moves);
        if (moves.size() == 0 || depth == 0) {
             return new MoveAndValue(null, efficientGetSituationValue(position, maximizingPlayer) * maximizingPlayer.getAiNum());
        }
        Move bestMove = null;
        if (maximizingPlayer == Team.WHITE) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                move.doMove(position, false);
                calculations++;
                MoveAndValue eval = minimax(position, depth - 1, alpha, beta, maximizingPlayer.opposite());
                int val = position.getGame().isRepetition(position.toCompactString(maximizingPlayer.opposite())) ? 0 : eval.getValue();
                move.undo(position);
                if (val > maxEval) {
                    if (move.isLegal(position)) {
                        maxEval = val;
                        bestMove = move;
                    } else {
                        continue;
                    }
                }
                if (PRUNING) {
                    alpha = Math.max(alpha, val);
                    if (beta <= alpha)
                        break;
                }
            }
            return new MoveAndValue(bestMove, maxEval);
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : moves) {
                move.doMove(position, false);
                calculations++;
                MoveAndValue eval = minimax(position, depth - 1, alpha, beta, maximizingPlayer.opposite());
                int val = position.getGame().isRepetition(position.toCompactString(maximizingPlayer.opposite())) ? 0 : eval.getValue();
                move.undo(position);
                if (val < minEval) {
                    if (move.isLegal(position)) {
                        minEval = val;
                        bestMove = move;
                    } else {
                        continue;
                    }
                }
                if (PRUNING) {
                    beta = Math.min(beta, val);
                    if (beta <= alpha)
                        break;
                }
            }
            return new MoveAndValue(bestMove, minEval);
        }
    }

    private MoveAndValue negamax(VirtualBoard position, int depth, int alpha, int beta, Team maximizingPlayer) {
        ArrayList<Move> moves = position.genBiLegalMovesForTeam(maximizingPlayer);
        orderMoves(moves);
        if (moves.size() == 0 || depth == 0) {
            if (useQuiescentSearch) {
                return new MoveAndValue(null, searchThroughCaptures(-beta, -alpha, position, maximizingPlayer) * maximizingPlayer.getAiNum());
            } else {
                return new MoveAndValue(null, efficientGetSituationValue(position, maximizingPlayer));
            }
        }
        Move bestMove = null;
        int value = Integer.MIN_VALUE;
        for (Move move : moves) {
            move.doMove(position, false);
            calculations++;
            MoveAndValue eval = negamax(position, depth - 1, beta * -1, alpha * -1, maximizingPlayer.opposite());
            int tempVal = position.getGame().isRepetition(position.toCompactString(maximizingPlayer.opposite())) ? 0 : -eval.getValue();
            move.undo(position);
            if (tempVal >= value) {
                if (move.isLegal(position)) {
                    value = tempVal;
                    bestMove = move;
                } else {
                    continue;
                }
            }
            if (PRUNING) {
                alpha = Math.max(value, alpha);
                if (alpha > beta) {
                    break;
                }
            }
        }
        if (bestMove == null) {
            if (position.isTeamInCheck(maximizingPlayer)) {
                return new MoveAndValue(null, -9999999);
            }
        }
        return new MoveAndValue(bestMove, value);
    }

    private int searchThroughCaptures(int alpha, int beta, VirtualBoard board, Team teamOf) {
        int eval = efficientGetSituationValue(board, teamOf);
        if (eval >= beta) {
            return eval;
        }
        if (eval > alpha)
            alpha = eval;
        List<Move> captureMoves = board.genTakeMovesForTeam(teamOf);
        orderMoves(captureMoves);
        quiescentSearches++;
        for (Move move : captureMoves) {
            move.doMove(board, false);
            eval = -searchThroughCaptures(-beta, -alpha, board, teamOf.opposite());
            move.undo(board);
            if (eval >= beta) {
                return beta;
            }
            if (eval > alpha) {
                alpha = eval;
            }
        }
        return alpha;
    }

    private int efficientGetSituationValue(VirtualBoard board, Team turnOf) {
        if (USE_TRANSPOSITION_TABLE) {
            String boardVal = board.toCompactString(turnOf);
            Integer saved = transpositionTable.get(boardVal);
            if (saved != null) {
                savedValueChecks++;
                return saved;
            }
            valueChecks++;
            saved = situationValue(board, boardVal, turnOf);
            transpositionTable.put(boardVal, saved);
            addOrder.add(boardVal);
            return saved;
        } else {
            return situationValue(board, board.toCompactString(turnOf), turnOf);
        }
    }

    public abstract int situationValue(VirtualBoard board, String compactBoard, Team turnOf);

    private static void orderMoves(List<Move> moves) {
        int[] moveScores = new int[moves.size()];
        for (int i = 0, movesSize = moves.size(); i < movesSize; i++) {
            Move move = moves.get(i);
            int moveScoreGuess = 0;
            VirtualPiece takePiece = move.getTakePiece();
            VirtualPiece movePiece = move.getPiece();
            if (takePiece != null) {
                moveScoreGuess = 10 * takePiece.getPieceType().getVal() - movePiece.getPieceType().getVal();
            }
//            if (movePiece.getPieceType() != PieceType.PAWN) {
                // TODO check if moves to a square attacked by an opponent pawn.
//            }
            moveScores[i] = moveScoreGuess;
        }
        sortMoves(moves, moveScores);
    }

    private static void sortMoves(List<Move> moves, int[] moveScores) {
        for (int i = 0; i < moves.size() - 1; i++) {
            for (int j = i + 1; j > 0; j--) {
                int swapIndex = j - 1;
                if (moveScores[swapIndex] < moveScores[j]) {
                    Move swapMove = moves.get(swapIndex);
                    moves.set(swapIndex, moves.get(j));
                    moves.set(j, swapMove);
                }
            }
        }
    }

    public static class MoveAndValue {

        private final Move move;
        private final int value;


        public MoveAndValue(Move move, int value) {
            this.move = move;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public Move getMove() {
            return move;
        }
    }

}
