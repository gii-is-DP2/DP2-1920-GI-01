
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

}
