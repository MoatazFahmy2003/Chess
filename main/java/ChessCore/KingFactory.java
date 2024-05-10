package ChessCore;

/**
 *
 * @author Admin
 */
public class KingFactory implements PieceFactory
{
    @Override
    public Piece constructPiece(Player color) 
    {
        return new King(color);
    }
}
