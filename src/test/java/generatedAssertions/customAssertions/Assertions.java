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
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
@Generated(value="assertj-assertions-generator")
public class Assertions {

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static InterventionAssert assertThat(Intervention actual) {
    return new InterventionAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicalRecordAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static MedicalRecordAssert assertThat(MedicalRecord actual) {
    return new MedicalRecordAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicineAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static MedicineAssert assertThat(Medicine actual) {
    return new MedicineAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static OwnerAssert assertThat(Owner actual) {
    return new OwnerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.PrescriptionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static PrescriptionAssert assertThat(Prescription actual) {
    return new PrescriptionAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static TrainerAssert assertThat(Trainer actual) {
    return new TrainerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static VetAssert assertThat(Vet actual) {
    return new VetAssert(actual);
  }
  
  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.PetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static PetAssert assertThat(org.springframework.samples.petclinic.model.Pet actual) {
    return new PetAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.RehabAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public static RehabAssert assertThat(org.springframework.samples.petclinic.model.Rehab actual) {
    return new RehabAssert(actual);
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }
}
