package ChessCore;

/**
 *
 * @author Admin
 */
public class QueenFactory implements PieceFactory
{
    @Override
    public Piece constructPiece(Player color) 
    {
        return new Queen(color);
    }
}
