package sample.chess.virtual.ai;

import javafx.application.Platform;
import sample.chess.ui.Board;
import sample.chess.ui.extra.Arrow;
import sample.chess.virtual.Team;
import sample.chess.virtual.VirtualBoard;
import sample.chess.virtual.moves.Move;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class MiniMaxAi extends AiBase  {

    private static final boolean LOG = true;

    private static final int MAX_TABLE_SIZE = 200000;
    private static final boolean USE_TRANSPOSITION_TABLE = true;
    private static final boolean PRUNING = true;
    private static final int DRAW_ARROWS_AT_DEPTH = -1;

    protected final int depth;
    protected boolean runOnSeparateThread = true;

    protected double minWaitTime = 500;

    protected int calculations = 0;
    protected int valueChecks = 0;
    private int savedValueChecks = 0;

    private final ArrayList<String> addOrder = new ArrayList<>();
    private final HashMap<String, Integer> transpositionTable = new HashMap<>();

    protected MiniMaxAi(int depth) {
        super();
        this.depth = depth;
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
            MoveAndValue result = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, team);
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
        moves.sort((o1, o2) -> {
            int o1Val = 0;
            if (o1.getTakePiece() != null) {
                o1Val = o1.getTakePiece().getPieceType().getVal();
            }
            int o2Val = 0;
            if (o2.getTakePiece() != null) {
                o2Val = o2.getTakePiece().getPieceType().getVal();
            }
            return o2Val - o1Val;
        });
        if (moves.size() == 0 || depth == 0) {
            return new MoveAndValue(null, efficientGetSituationValue(position, maximizingPlayer));
        }
        Move bestMove = null;
        if (maximizingPlayer == Team.WHITE) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : moves) {
                AtomicReference<Arrow> arrow = null;
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
                AtomicReference<Arrow> arrow = null;
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
