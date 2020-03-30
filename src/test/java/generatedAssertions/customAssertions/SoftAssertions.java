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
 * Entry point for soft assertions of different data types.
 */
@Generated(value="assertj-assertions-generator")
public class SoftAssertions extends org.assertj.core.api.SoftAssertions {

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

}
