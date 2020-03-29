package generatedAssertions.customAssertions;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class Assertions {

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static TrainerAssert assertThat(org.springframework.samples.petclinic.model.Trainer actual) {
    return new TrainerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static VetAssert assertThat(org.springframework.samples.petclinic.model.Vet actual) {
    return new VetAssert(actual);
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }
}
