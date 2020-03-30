package generatedAssertions.customAssertions;

import javax.annotation.Generated;

import org.assertj.core.util.CheckReturnValue;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Vet;

/**
 * Like {@link SoftAssertions} but as a junit rule that takes care of calling
 * {@link SoftAssertions#assertAll() assertAll()} at the end of each test.
 * <p>
 * Example:
 * <pre><code class='java'> public class SoftlyTest {
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
 *  }</code></pre>
 */
@Generated(value="assertj-assertions-generator")
public class JUnitSoftAssertions extends org.assertj.core.api.JUnitSoftAssertions {

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public InterventionAssert assertThat(Intervention actual) {
    return proxy(InterventionAssert.class, Intervention.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.MedicalRecordAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public MedicalRecordAssert assertThat(MedicalRecord actual) {
    return proxy(MedicalRecordAssert.class, MedicalRecord.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.MedicineAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public MedicineAssert assertThat(Medicine actual) {
    return proxy(MedicineAssert.class, Medicine.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public OwnerAssert assertThat(Owner actual) {
    return proxy(OwnerAssert.class, Owner.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.PrescriptionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public PrescriptionAssert assertThat(Prescription actual) {
    return proxy(PrescriptionAssert.class, Prescription.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public TrainerAssert assertThat(Trainer actual) {
    return proxy(TrainerAssert.class, Trainer.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @CheckReturnValue
  public VetAssert assertThat(Vet actual) {
    return proxy(VetAssert.class, Vet.class, actual);
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
