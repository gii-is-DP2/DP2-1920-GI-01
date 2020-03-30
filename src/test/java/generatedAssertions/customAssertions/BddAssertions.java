
package generatedAssertions.customAssertions;

/**
 * Entry point for BDD assertions of different data types.
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class BddAssertions {

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static InterventionAssert then(final org.springframework.samples.petclinic.model.Intervention actual) {
		return new InterventionAssert(actual);
	}

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
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicineAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static MedicineAssert then(final org.springframework.samples.petclinic.model.Medicine actual) {
		return new MedicineAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static OwnerAssert then(final org.springframework.samples.petclinic.model.Owner actual) {
		return new OwnerAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.PrescriptionAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static PrescriptionAssert then(final org.springframework.samples.petclinic.model.Prescription actual) {
		return new PrescriptionAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static TrainerAssert then(final org.springframework.samples.petclinic.model.Trainer actual) {
		return new TrainerAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static VetAssert then(final org.springframework.samples.petclinic.model.Vet actual) {
		return new VetAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.VisitAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public static VisitAssert then(final org.springframework.samples.petclinic.model.Visit actual) {
		return new VisitAssert(actual);
	}

	/**
	 * Creates a new <code>{@link BddAssertions}</code>.
	 */
	protected BddAssertions() {
		// empty
	}
}
