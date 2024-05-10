package ChessCore;

/**
 *
 * @author Admin
 */
public class KnightFactory implements PieceFactory
{
    @Override
    public Piece constructPiece(Player color) 
    {
        return new Knight(color);
    }
}
