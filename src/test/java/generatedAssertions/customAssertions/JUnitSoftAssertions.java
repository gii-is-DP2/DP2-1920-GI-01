
package generatedAssertions.customAssertions;

/**
 * Like {@link SoftAssertions} but as a junit rule that takes care of calling
 * {@link SoftAssertions#assertAll() assertAll()} at the end of each test.
 * <p>
 * Example:
 *
 * <pre>
 * <code class='java'> public class SoftlyTest {
 *
 *     &#064;Rule
 *     public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();
 *
 *     &#064;Test
 *     public void soft_bdd_assertions() throws Exception {
 *       softly.assertThat(1).isEqualTo(2);
 *       softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
 *       // no need to call assertAll(), this is done automatically.
 *     }
 *  }</code>
 * </pre>
 */
@javax.annotation.Generated(value = "assertj-assertions-generator")
public class JUnitSoftAssertions extends org.assertj.core.api.JUnitSoftAssertions {

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

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.MedicineAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public MedicineAssert assertThat(final org.springframework.samples.petclinic.model.Medicine actual) {
		return this.proxy(MedicineAssert.class, org.springframework.samples.petclinic.model.Medicine.class, actual);
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

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.PrescriptionAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public PrescriptionAssert assertThat(final org.springframework.samples.petclinic.model.Prescription actual) {
		return this.proxy(PrescriptionAssert.class, org.springframework.samples.petclinic.model.Prescription.class, actual);
	}

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public TrainerAssert assertThat(final org.springframework.samples.petclinic.model.Trainer actual) {
		return this.proxy(TrainerAssert.class, org.springframework.samples.petclinic.model.Trainer.class, actual);
	}

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public VetAssert assertThat(final org.springframework.samples.petclinic.model.Vet actual) {
		return this.proxy(VetAssert.class, org.springframework.samples.petclinic.model.Vet.class, actual);
	}

	/**
	 * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.VisitAssert}</code>.
	 *
	 * @param actual
	 *            the actual value.
	 * @return the created "soft" assertion object.
	 */
	@org.assertj.core.util.CheckReturnValue
	public VisitAssert assertThat(final org.springframework.samples.petclinic.model.Visit actual) {
		return this.proxy(VisitAssert.class, org.springframework.samples.petclinic.model.Visit.class, actual);
	}
  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.PetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public PetAssert assertThat(org.springframework.samples.petclinic.model.Pet actual) {
    return proxy(PetAssert.class, org.springframework.samples.petclinic.model.Pet.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.RehabAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public RehabAssert assertThat(org.springframework.samples.petclinic.model.Rehab actual) {
    return proxy(RehabAssert.class, org.springframework.samples.petclinic.model.Rehab.class, actual);
  }

}
