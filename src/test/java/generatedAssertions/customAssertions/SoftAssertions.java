package generatedAssertions.customAssertions;

/**
 * Entry point for soft assertions of different data types.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class SoftAssertions extends org.assertj.core.api.SoftAssertions {

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public TrainerAssert assertThat(org.springframework.samples.petclinic.model.Trainer actual) {
    return proxy(TrainerAssert.class, org.springframework.samples.petclinic.model.Trainer.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public VetAssert assertThat(org.springframework.samples.petclinic.model.Vet actual) {
    return proxy(VetAssert.class, org.springframework.samples.petclinic.model.Vet.class, actual);
  }

}
