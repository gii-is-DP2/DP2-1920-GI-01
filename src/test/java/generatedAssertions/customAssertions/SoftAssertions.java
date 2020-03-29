
package generatedAssertions.customAssertions;

/**
 * Entry point for soft assertions of different data types.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class SoftAssertions extends org.assertj.core.api.SoftAssertions {

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.MedicalRecordAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public MedicalRecordAssert assertThat(final org.springframework.samples.petclinic.model.MedicalRecord actual) {
		return this.proxy(MedicalRecordAssert.class, org.springframework.samples.petclinic.model.MedicalRecord.class, actual);
	}

}
