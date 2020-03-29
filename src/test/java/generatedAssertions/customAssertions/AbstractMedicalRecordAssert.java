package generatedAssertions.customAssertions;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.util.Objects;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Visit;

/**
 * Abstract base class for {@link MedicalRecord} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractMedicalRecordAssert<S extends AbstractMedicalRecordAssert<S, A>, A extends MedicalRecord> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractMedicalRecordAssert}</code> to make assertions on actual MedicalRecord.
   * @param actual the MedicalRecord we want to make assertions on.
   */
  protected AbstractMedicalRecordAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual MedicalRecord's description is equal to the given one.
   * @param description the given description to compare the actual MedicalRecord's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual MedicalRecord's description is not equal to the given one.
   */
  public S hasDescription(String description) {
    // check that actual MedicalRecord we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting description of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualDescription = actual.getDescription();
    if (!Objects.areEqual(actualDescription, description)) {
      failWithMessage(assertjErrorMessage, actual, description, actualDescription);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual MedicalRecord's status is equal to the given one.
   * @param status the given status to compare the actual MedicalRecord's status to.
   * @return this assertion object.
   * @throws AssertionError - if the actual MedicalRecord's status is not equal to the given one.
   */
  public S hasStatus(String status) {
    // check that actual MedicalRecord we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting status of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualStatus = actual.getStatus();
    if (!Objects.areEqual(actualStatus, status)) {
      failWithMessage(assertjErrorMessage, actual, status, actualStatus);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual MedicalRecord's visit is equal to the given one.
   * @param visit the given visit to compare the actual MedicalRecord's visit to.
   * @return this assertion object.
   * @throws AssertionError - if the actual MedicalRecord's visit is not equal to the given one.
   */
  public S hasVisit(Visit visit) {
    // check that actual MedicalRecord we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting visit of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Visit actualVisit = actual.getVisit();
    if (!Objects.areEqual(actualVisit, visit)) {
      failWithMessage(assertjErrorMessage, actual, visit, actualVisit);
    }

    // return the current assertion for method chaining
    return myself;
  }

}