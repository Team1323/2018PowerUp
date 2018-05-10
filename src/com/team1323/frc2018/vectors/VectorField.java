import java.util.function;

public class VectorField {
  public VectorField(Function<Translation2d,Translation2d> field) {
    field_ = field;
  }
  protected Function<Translation2d,Translation2d> field_;
  public getVector(Translation2d here) { return field_(here); }
}
