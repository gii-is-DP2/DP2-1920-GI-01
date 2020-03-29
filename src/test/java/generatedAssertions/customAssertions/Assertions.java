
package generatedAssertions.customAssertions;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class Assertions {

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static InterventionAssert assertThat(final org.springframework.samples.petclinic.model.Intervention actual) {
		return new InterventionAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static OwnerAssert assertThat(final org.springframework.samples.petclinic.model.Owner actual) {
		return new OwnerAssert(actual);
	}

	/**
	 * Creates a new <code>{@link Assertions}</code>.
	 */
	protected Assertions() {
		// empty
	}
}
