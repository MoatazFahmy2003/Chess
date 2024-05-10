package ChessCore;

/**
 *
 * @author Admin
 */
public class BishopFactory implements PieceFactory
{
    @Override
    public Piece constructPiece(Player color) 
    {
        return new Bishop(color);
    }
}
