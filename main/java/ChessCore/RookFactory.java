package ChessCore;

/**
 *
 * @author Admin
 */
public class RookFactory implements PieceFactory
{

    @Override
    public Piece constructPiece(Player color) 
    {
        return new Rook(color);
    }
    
}
