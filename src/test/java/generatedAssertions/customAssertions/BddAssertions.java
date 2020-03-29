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
 * Entry point for BDD assertions of different data types.
 */
@Generated(value="assertj-assertions-generator")
public class BddAssertions {

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.InterventionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static InterventionAssert then(Intervention actual) {
    return new InterventionAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicalRecordAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static MedicalRecordAssert then(MedicalRecord actual) {
    return new MedicalRecordAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.MedicineAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static MedicineAssert then(Medicine actual) {
    return new MedicineAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.OwnerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static OwnerAssert then(Owner actual) {
    return new OwnerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.PrescriptionAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static PrescriptionAssert then(Prescription actual) {
    return new PrescriptionAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.TrainerAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static TrainerAssert then(Trainer actual) {
    return new TrainerAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.springframework.samples.petclinic.model.VetAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  @CheckReturnValue
  public static VetAssert then(Vet actual) {
    return new VetAssert(actual);
  }

  /**
   * Creates a new <code>{@link BddAssertions}</code>.
   */
  protected BddAssertions() {
    // empty
  }
}
