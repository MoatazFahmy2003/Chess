package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import ChessCore.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Admin
 */
public class BoardWindow extends JPanel implements MouseListener {

    private JFrame frame;
    private final ClassicChessGame Game;
    private Square fromSquare = null;
    private Square toSquare = null;
    private Piece p;
    private Move move;

    private Stack<ChessGameMemento> memento;
    
    private ChessBoard board;
    private GameStatus status;
    //private Stack<ChessBoard> board;
    //private Stack<GameStatus> status;

    ArrayList<Integer> coordinates;
    Image imageWhite[];
    Image imageBlack[];
    ImageIcon icon;

    /**
     * Creates new form Flip
     */
    public BoardWindow() {
        initComponents();
        this.setPreferredSize(new Dimension(820, 820));

        frame = new JFrame();
        Game = new ClassicChessGame();
        coordinates = new ArrayList<Integer>();
        
        memento  = new Stack<ChessGameMemento>();
        memento.push (new ChessGameMemento(this.Game.getBoard(),this.Game.getGameStatus(),this.Game.getWhoseTurn()));
        board = memento.peek().getBoard();
        status = memento.peek().getGameStatus();
        
        imageWhite = new Image[6];
        imageBlack = new Image[6];

        icon = new ImageIcon("WhitePawn.png");
        imageWhite[0] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("WhiteRook.png");
        imageWhite[1] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("WhiteKnight.png");
        imageWhite[2] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("WhiteBishop.png");
        imageWhite[3] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("WhiteQueen.png");
        imageWhite[4] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("WhiteKing.png");
        imageWhite[5] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);

        icon = new ImageIcon("BlackPawn.png");
        imageBlack[0] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("BlackRook.png");
        imageBlack[1] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("BlackKnight.png");
        imageBlack[2] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("BlackBishop.png");
        imageBlack[3] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("BlackQueen.png");
        imageBlack[4] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
        icon = new ImageIcon("BlackKing.png");
        imageBlack[5] = icon.getImage().getScaledInstance(75, 75, Image.SCALE_FAST);
    }

    public void initialization(BoardWindow b) {
        this.frame.setSize(820, 820);
        this.frame.setTitle("Chess Game");

        this.frame.getContentPane().add(b);
        this.frame.addMouseListener(b);

        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics chess) {
        
        super.paintComponent(chess);

        BoardFile file[];
        BoardRank rank[];

        chess.fillRect(100, 100, 600, 600);

        for (int i = 100; i <= 550; i += 150) {
            for (int j = 100; j <= 550; j += 150) {
                chess.clearRect(i, j, 75, 75);
            }
        }

        for (int i = 175; i <= 625; i += 150) {
            for (int j = 175; j <= 625; j += 150) {
                chess.clearRect(i, j, 75, 75);
            }
        }
        
        if (null != Game.getGameStatus() && GameStatus.IN_PROGRESS != Game.getGameStatus()
                && GameStatus.WHITE_UNDER_CHECK != Game.getGameStatus()
                && GameStatus.BLACK_UNDER_CHECK != Game.getGameStatus()) {
            String validEnd[] = {"UNDO", ""};

            switch (Game.getGameStatus()) {
                case WHITE_WON:
                    validEnd[1] = "White Won";
                    break;
                case BLACK_WON:
                    validEnd[1] = "Black Won";
                    break;
                case STALEMATE:
                    validEnd[1] = "Stalemate";
                    break;
                case INSUFFICIENT_MATERIAL:
                    validEnd[1] = "Insufficient Material";
                    break;
                default:
                    break;
            }

            int endType = JOptionPane.showOptionDialog(null, "Choose ", "End",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, validEnd, validEnd[1]);

            switch (endType) {
                case 1:
                    this.frame.setVisible(false);
                    JOptionPane.showMessageDialog(null, validEnd[1] + "!!!");
                    System.exit(0);
                    break;
                case 0:
                    this.restoreMemento();
                    this.highlightValidMoves(null);
                    this.repaint();
                    break;
                default:
                    break;
            }
        }

        file = BoardFile.values();
        rank = BoardRank.values();
        if (Game.getWhoseTurn() == Player.WHITE) {
            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    Square square = new Square(file[i], rank[j]);
                    if (Game.getBoard().getPieceAtSquare(square) == null) {
                        continue;
                    } else if (Game.getBoard().getPieceAtSquare(square).getOwner() == Player.WHITE) {
                        p = Game.getBoard().getPieceAtSquare(square);
                        int x = this.convertFileToX(file[i]);
                        int y = this.covertRankToY(rank[j]);
                        if (p instanceof Pawn) {
                            chess.drawImage(this.imageWhite[0], x, y, this);
                        } else if (p instanceof Rook) {
                            chess.drawImage(this.imageWhite[1], x, y, this);
                        } else if (p instanceof Knight) {
                            chess.drawImage(this.imageWhite[2], x, y, this);
                        } else if (p instanceof Bishop) {
                            chess.drawImage(this.imageWhite[3], x, y, this);
                        } else if (p instanceof Queen) {
                            chess.drawImage(this.imageWhite[4], x, y, this);
                        } else if (p instanceof King) {
                            chess.drawImage(this.imageWhite[5], x, y, this);
                            if (Game.getGameStatus() == GameStatus.WHITE_UNDER_CHECK) {
                                Graphics2D transparency = (Graphics2D) chess;
                                transparency.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                                chess.setColor(Color.RED);
                                chess.fillRect(x, y, 75, 75);
                            }
                        }
                    } else {
                        p = Game.getBoard().getPieceAtSquare(square);
                        int x = this.convertFileToX(file[i]);
                        int y = this.covertRankToY(rank[j]);
                        if (p instanceof Pawn) {
                            chess.drawImage(this.imageBlack[0], x, y, this);
                        } else if (p instanceof Rook) {
                            chess.drawImage(this.imageBlack[1], x, y, this);
                        } else if (p instanceof Knight) {
                            chess.drawImage(this.imageBlack[2], x, y, this);
                        } else if (p instanceof Bishop) {
                            chess.drawImage(this.imageBlack[3], x, y, this);
                        } else if (p instanceof Queen) {
                            chess.drawImage(this.imageBlack[4], x, y, this);
                        } else if (p instanceof King) {
                            chess.drawImage(this.imageBlack[5], x, y, this);
                            if (Game.getGameStatus() == GameStatus.BLACK_UNDER_CHECK) {
                                Graphics2D transparency = (Graphics2D) chess;
                                transparency.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                                chess.setColor(Color.RED);
                                chess.fillRect(x, y, 75, 75);
                            }
                        }
                    }
                }
            }
        }

        if (Game.getWhoseTurn() == Player.BLACK) {
            for (int i = 7; i >= 0; i--) {
                for (int j = 7; j >= 0; j--) {
                    Square square = new Square(file[i], rank[j]);
                    if (Game.getBoard().getPieceAtSquare(square) == null) {
                        continue;
                    } else if (Game.getBoard().getPieceAtSquare(square).getOwner() == Player.WHITE) {
                        p = Game.getBoard().getPieceAtSquare(square);
                        int x = this.convertFileToXFlip(file[i]);
                        int y = this.covertRankToYFlip(rank[j]);
                        if (p instanceof Pawn) {
                            chess.drawImage(this.imageWhite[0], x, y, this);
                        } else if (p instanceof Rook) {
                            chess.drawImage(this.imageWhite[1], x, y, this);
                        } else if (p instanceof Knight) {
                            chess.drawImage(this.imageWhite[2], x, y, this);
                        } else if (p instanceof Bishop) {
                            chess.drawImage(this.imageWhite[3], x, y, this);
                        } else if (p instanceof Queen) {
                            chess.drawImage(this.imageWhite[4], x, y, this);
                        } else if (p instanceof King) {
                            chess.drawImage(this.imageWhite[5], x, y, this);
                            if (Game.getGameStatus() == GameStatus.WHITE_UNDER_CHECK) {
                                Graphics2D transparency = (Graphics2D) chess;
                                transparency.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                                chess.setColor(Color.RED);
                                chess.fillRect(x, y, 75, 75);
                            }
                        }
                    } else {
                        p = Game.getBoard().getPieceAtSquare(square);
                        int x = this.convertFileToXFlip(file[i]);
                        int y = this.covertRankToYFlip(rank[j]);
                        if (p instanceof Pawn) {
                            chess.drawImage(this.imageBlack[0], x, y, this);
                        } else if (p instanceof Rook) {
                            chess.drawImage(this.imageBlack[1], x, y, this);
                        } else if (p instanceof Knight) {
                            chess.drawImage(this.imageBlack[2], x, y, this);
                        } else if (p instanceof Bishop) {
                            chess.drawImage(this.imageBlack[3], x, y, this);
                        } else if (p instanceof Queen) {
                            chess.drawImage(this.imageBlack[4], x, y, this);
                        } else if (p instanceof King) {
                            chess.drawImage(this.imageBlack[5], x, y, this);
                            if (Game.getGameStatus() == GameStatus.BLACK_UNDER_CHECK) {
                                Graphics2D transparency = (Graphics2D) chess;
                                transparency.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                                chess.setColor(Color.RED);
                                chess.fillRect(x, y, 75, 75);
                            }
                        }
                    }
                }
            }
        }

        if (!this.coordinates.isEmpty()) {
            Graphics2D transparency = (Graphics2D) chess;
            transparency.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            for (int i = 0; i < this.coordinates.size(); i += 2) {
                chess.setColor(Color.GREEN);
                chess.fillRect(this.coordinates.get(i), this.coordinates.get(i + 1), 75, 75);
            }
        }
        //System.out.println(this.board.size());
        if (this.move != null) {
            if (isPawnPromotion(this.p, this.toSquare)) {
                System.out.println("Promotion");
                PawnPromotion promotionTo = PawnPromotion.None;
                String validPromotions[] = {"Rook", "Knight", "Bishop", "Queen"};

                int promotionType = JOptionPane.showOptionDialog(null, "Promote To", "Promotion Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, validPromotions, validPromotions[3]);

                switch (promotionType) {
                    case 0:
                        promotionTo = PawnPromotion.Rook;
                        System.out.println("Pawn Promoted To Rook!!");
                        break;
                    case 1:
                        promotionTo = PawnPromotion.Knight;
                        System.out.println("Pawn Promoted To Knight!!");
                        break;
                    case 2:
                        promotionTo = PawnPromotion.Bishop;
                        System.out.println("Pawn Promoted To Bishop!!");
                        break;
                    case 3:
                        promotionTo = PawnPromotion.Queen;
                        System.out.println("Pawn Promoted To Queen!!");
                        break;
                    default:
                        break;
                }

                this.move = new Move(this.fromSquare, this.toSquare, promotionTo);
            }
            if (fromSquare != null && toSquare != null && this.Game.getBoard().getPieceAtSquare(fromSquare) != null) {
                Game.makeMove(move);
                this.createMemento();
            }

            this.move = null;
            this.fromSquare = null;
            this.toSquare = null;
            this.Game.setPromotion(PawnPromotion.None);
            this.repaint();

        }

    }

    public int convertFileToX(BoardFile file) {
        int x = 0;
        if (file != null) {
            switch (file) {
                case A ->
                    x = 100;
                case B ->
                    x = 175;
                case C ->
                    x = 250;
                case D ->
                    x = 325;
                case E ->
                    x = 400;
                case F ->
                    x = 475;
                case G ->
                    x = 550;
                case H ->
                    x = 625;
                default ->
                    x = 0;
            }
        }
        return x;
    }

    public int covertRankToY(BoardRank rank) {
        int y = 0;

        if (rank != null) {
            switch (rank) {
                case FIRST ->
                    y = 625;
                case SECOND ->
                    y = 550;
                case THIRD ->
                    y = 475;
                case FORTH ->
                    y = 400;
                case FIFTH ->
                    y = 325;
                case SIXTH ->
                    y = 250;
                case SEVENTH ->
                    y = 175;
                case EIGHTH ->
                    y = 100;
                default ->
                    y = 0;
            }
        }
        return y;
    }

    public int convertFileToXFlip(BoardFile file) {
        int x = 0;
        if (file != null) {
            switch (file) {
                case H ->
                    x = 100;
                case G ->
                    x = 175;
                case F ->
                    x = 250;
                case E ->
                    x = 325;
                case D ->
                    x = 400;
                case C ->
                    x = 475;
                case B ->
                    x = 550;
                case A ->
                    x = 625;
                default ->
                    x = 0;
            }
        }
        return x;
    }

    public int covertRankToYFlip(BoardRank rank) {
        int y = 0;

        if (rank != null) {
            switch (rank) {
                case EIGHTH ->
                    y = 625;
                case SEVENTH ->
                    y = 550;
                case SIXTH ->
                    y = 475;
                case FIFTH ->
                    y = 400;
                case FORTH ->
                    y = 325;
                case THIRD ->
                    y = 250;
                case SECOND ->
                    y = 175;
                case FIRST ->
                    y = 100;
                default ->
                    y = 0;
            }
        }
        return y;
    }

    @SuppressWarnings("empty-statement")
    public boolean highlightValidMoves(Square square) {
        int x = 0;
        int y = 0;

        if (square == null) {
            this.coordinates.clear();
            return false;
        }

        if (this.Game.getWhoseTurn() == Player.WHITE && this.Game.getBoard().getPieceAtSquare(square) instanceof Pawn
                && square.getRank() == BoardRank.SEVENTH) {
            this.Game.setPromotion(PawnPromotion.Queen);
        }
        if (this.Game.getWhoseTurn() == Player.BLACK && this.Game.getBoard().getPieceAtSquare(square) instanceof Pawn
                && square.getRank() == BoardRank.SECOND) {
            this.Game.setPromotion(PawnPromotion.Queen);
        }

        this.coordinates.clear();
        for (int i = 0; i < Game.getAllValidMovesFromSquare(square).size(); i++) {
            Square current = Game.getAllValidMovesFromSquare(square).get(i);
           
            if (Game.getWhoseTurn() == Player.WHITE) {
                x = this.convertFileToX(current.getFile());
                y = this.covertRankToY(current.getRank());
                //System.out.println(x + " " + y);
                this.coordinates.add(x);
                this.coordinates.add(y);
            } else if (Game.getWhoseTurn() == Player.BLACK) {
                x = this.convertFileToXFlip(current.getFile());
                y = this.covertRankToYFlip(current.getRank());
                //System.out.println(x + " " + y);
                this.coordinates.add(x);
                this.coordinates.add(y);
            }
        }
        
        this.repaint();
        return !this.coordinates.isEmpty();
    }

    public boolean isPawnPromotion(Piece p, Square toSquare) {
        
        if (!(p instanceof Pawn)) {
            return false;
        }
        if (p.getOwner() == Player.WHITE && toSquare.getRank() == BoardRank.EIGHTH) {
            return true;
        } else if (p.getOwner() == Player.BLACK && toSquare.getRank() == BoardRank.FIRST) {
            return true;
        }

        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setText("UNDO");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(314, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(214, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.highlightValidMoves(null);

        if (this.restoreMemento() == 0) {
            System.out.println("No Boards in stack");
            return;
        }
        this.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String[] args) {
        BoardWindow b = new BoardWindow();
        b.initialization(b);

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        BoardFile file = null;
        BoardRank rank = null;

        if (Game.getWhoseTurn() == Player.WHITE) {
            if (e.getX() >= 105 && e.getX() <= 180) {
                file = BoardFile.A;
            } else if (e.getX() > 180 && e.getX() <= 255) {
                file = BoardFile.B;
            } else if (e.getX() > 255 && e.getX() <= 330) {
                file = BoardFile.C;
            } else if (e.getX() > 330 && e.getX() <= 405) {
                file = BoardFile.D;
            } else if (e.getX() > 405 && e.getX() <= 480) {
                file = BoardFile.E;
            } else if (e.getX() > 480 && e.getX() <= 555) {
                file = BoardFile.F;
            } else if (e.getX() > 555 && e.getX() <= 630) {
                file = BoardFile.G;
            } else if (e.getX() > 630 && e.getX() <= 705) {
                file = BoardFile.H;
            }

            if (e.getY() <= 730 && e.getY() >= 655) {
                rank = BoardRank.FIRST;
            } else if (e.getY() < 655 && e.getY() >= 580) {
                rank = BoardRank.SECOND;
            } else if (e.getY() < 580 && e.getY() >= 505) {
                rank = BoardRank.THIRD;
            } else if (e.getY() < 505 && e.getY() >= 430) {
                rank = BoardRank.FORTH;
            } else if (e.getY() < 430 && e.getY() >= 355) {
                rank = BoardRank.FIFTH;
            } else if (e.getY() < 355 && e.getY() >= 280) {
                rank = BoardRank.SIXTH;
            } else if (e.getY() < 280 && e.getY() >= 205) {
                rank = BoardRank.SEVENTH;
            } else if (e.getY() < 205 && e.getY() >= 130) {
                rank = BoardRank.EIGHTH;
            }
            if (this.highlightValidMoves(new Square(file, rank))) {
                this.fromSquare = new Square(file, rank);
            } else {
                this.toSquare = new Square(file, rank);
                this.move = new Move(this.fromSquare, this.toSquare);
            }

        } else if (Game.getWhoseTurn() == Player.BLACK) {
            if (e.getX() >= 105 && e.getX() <= 180) {
                file = BoardFile.H;
            } else if (e.getX() > 180 && e.getX() <= 255) {
                file = BoardFile.G;
            } else if (e.getX() > 255 && e.getX() <= 330) {
                file = BoardFile.F;
            } else if (e.getX() > 330 && e.getX() <= 405) {
                file = BoardFile.E;
            } else if (e.getX() > 405 && e.getX() <= 480) {
                file = BoardFile.D;
            } else if (e.getX() > 480 && e.getX() <= 555) {
                file = BoardFile.C;
            } else if (e.getX() > 555 && e.getX() <= 630) {
                file = BoardFile.B;
            } else if (e.getX() > 630 && e.getX() <= 705) {
                file = BoardFile.A;
            }

            if (e.getY() <= 730 && e.getY() >= 655) {
                rank = BoardRank.EIGHTH;
            } else if (e.getY() < 655 && e.getY() >= 580) {
                rank = BoardRank.SEVENTH;
            } else if (e.getY() < 580 && e.getY() >= 505) {
                rank = BoardRank.SIXTH;
            } else if (e.getY() < 505 && e.getY() >= 430) {
                rank = BoardRank.FIFTH;
            } else if (e.getY() < 430 && e.getY() >= 355) {
                rank = BoardRank.FORTH;
            } else if (e.getY() < 355 && e.getY() >= 280) {
                rank = BoardRank.THIRD;
            } else if (e.getY() < 280 && e.getY() >= 205) {
                rank = BoardRank.SECOND;
            } else if (e.getY() < 205 && e.getY() >= 130) {
                rank = BoardRank.FIRST;
            }
            if (this.highlightValidMoves(new Square(file, rank))) {
                this.fromSquare = new Square(file, rank);
            } else {
                this.toSquare = new Square(file, rank);
                this.move = new Move(this.fromSquare, this.toSquare);
            }
        }
    }

    public void createMemento() {
        System.out.println("move");
        this.memento.push(new ChessGameMemento(new ChessBoard(this.Game.getBoard()), this.Game.getGameStatus(), this.Game.getWhoseTurn()));
        System.out.println(this.memento.get(this.memento.size() - 1).getCurrentTurn());
        System.out.println(this.memento.size());
    }

    public int restoreMemento() {
        System.out.println("undo");
        System.out.println(this.memento.size());
        if (this.memento.size() <= 1) {
            return 0;
        }
        this.memento.pop();
        
        this.Game.setBoard(memento.peek().getBoard());
        this.Game.setGameStatus(memento.peek().getGameStatus());
        this.Game.setWhoseTurn(memento.peek().getCurrentTurn());
        
        System.out.println(this.memento.size());
        return this.memento.size();
    }

    private static class ChessGameMemento {

        private ChessBoard board;
        private GameStatus status;
        private Player currentTurn;

        private ChessGameMemento(ChessBoard board, GameStatus status, Player currentTurn) {
            this.board = new ChessBoard(board);
            this.status = status;
            this.currentTurn = currentTurn;
        }

        private ChessBoard getBoard() {
            return this.board;
        }

        private GameStatus getGameStatus() {
            return this.status;
        }

        private Player getCurrentTurn() {
            return this.currentTurn;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}
