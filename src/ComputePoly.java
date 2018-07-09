// version 1.2

public class ComputePoly
{
    private Poly TargetPoly;
//    private Poly SourcePoly;

    public ComputePoly()
    {
        TargetPoly = new Poly();
    }

    public void compute(Poly SourcePoly, int op)
    {
        this.TargetPoly.calc(SourcePoly, op);
    }

    public void print()
    {
        this.TargetPoly.print();
    }
}
