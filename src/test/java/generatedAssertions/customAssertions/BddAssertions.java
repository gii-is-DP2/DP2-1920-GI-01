
package generatedAssertions.customAssertions;

/**
 * Entry point for BDD assertions of different data types.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class BddAssertions {

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicalRecordAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static MedicalRecordAssert then(final org.springframework.samples.petclinic.model.MedicalRecord actual) {
		return new MedicalRecordAssert(actual);
	}

	/**
	 * Creates a new <code>{@link BddAssertions}</code>.
	 */
	protected BddAssertions() {
		// empty
	}
}
