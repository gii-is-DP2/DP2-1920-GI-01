
package generatedAssertions.customAssertions;

/**
 * Entry point for soft assertions of different data types.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class SoftAssertions extends org.assertj.core.api.SoftAssertions {

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public InterventionAssert assertThat(final org.springframework.samples.petclinic.model.Intervention actual) {
		return this.proxy(InterventionAssert.class, org.springframework.samples.petclinic.model.Intervention.class, actual);
	}

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public OwnerAssert assertThat(final org.springframework.samples.petclinic.model.Owner actual) {
		return this.proxy(OwnerAssert.class, org.springframework.samples.petclinic.model.Owner.class, actual);
	}

}
