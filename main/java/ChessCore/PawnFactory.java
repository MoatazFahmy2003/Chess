package ChessCore;

/**
 *
 * @author Admin
 */
public class PawnFactory implements PieceFactory
{

    @Override
    public Piece constructPiece(Player color) 
    {
        return new Pawn(color);
    }
    
}
