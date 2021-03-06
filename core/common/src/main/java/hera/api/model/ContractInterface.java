/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import static hera.util.ValidationUtils.assertNotNull;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.exception.HerajException;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ApiAudience.Public
@ApiStability.Unstable
@ToString
@EqualsAndHashCode
public class ContractInterface {

  @Getter
  protected final ContractAddress address;

  @Getter
  protected final String version;

  @Getter
  protected final String language;

  @Getter
  protected final List<ContractFunction> functions;

  @Getter
  protected final List<StateVariable> stateVariables;

  /**
   * ContractInterface constructor.
   * 
   * @param contractAddress a contract address
   * @param version a contract version
   * @param language a contract language
   * @param functions a contract functions to invoke
   * @param stateVariables a contract state variables
   */
  @ApiAudience.Private
  public ContractInterface(final ContractAddress contractAddress, final String version,
      final String language, final List<ContractFunction> functions,
      final List<StateVariable> stateVariables) {
    assertNotNull(contractAddress, "Contract address must not null");
    assertNotNull(version, "Version must not null");
    assertNotNull(language, "Language must not null");
    assertNotNull(functions, "Functions must not null");
    assertNotNull(stateVariables, "State variables must not null");
    this.address = contractAddress;
    this.version = version;
    this.language = language;
    this.functions = unmodifiableList(functions);
    this.stateVariables = unmodifiableList(stateVariables);
  }

  /**
   * Find a contract function with the given function name.
   *
   * @param functionName function name to find
   * @return {@code ContractFunction} if found. Otherwise, null
   */
  public ContractFunction findFunction(final String functionName) {
    ContractFunction foundFunction = null;
    for (final ContractFunction contractFunction : functions) {
      if (contractFunction.getName().equals(functionName)) {
        foundFunction = contractFunction;
        break;
      }
    }
    return foundFunction;
  }

  public ContractInvocationWithNothing newInvocationBuilder() {
    return new ContractInterface.InvocationBuilder(this);
  }

  public interface ContractInvocationWithNothing {

    ContractInvocationWithReady function(String functionName);
  }

  public interface ContractInvocationWithReady extends hera.util.Builder<ContractInvocation> {

    ContractInvocationWithReady args(Object... args);

    ContractInvocationWithReady amount(Aer amount);

    ContractInvocationWithReady delegateFee(boolean delegateFee);
  }

  @RequiredArgsConstructor
  protected static class InvocationBuilder
      implements ContractInvocationWithNothing, ContractInvocationWithReady {

    @NonNull
    protected final ContractInterface contractInterface;

    protected ContractFunction function;

    protected Object[] args = new Object[0];

    protected Aer amount = Aer.EMPTY;

    protected boolean delegateFee = false;

    @Override
    public ContractInvocationWithReady function(final String functionName) {
      this.function = contractInterface.findFunction(functionName);
      if (null == this.function) {
        throw new HerajException(
            "Cannot find function from interface [name: " + functionName + "]");
      }
      return this;
    }

    @Override
    public ContractInvocationWithReady args(final Object... args) {
      if (null != args) {
        this.args = args;
      }
      return this;
    }

    @Override
    public ContractInvocationWithReady amount(final Aer amount) {
      this.amount = amount;
      return this;
    }

    @Override
    public ContractInvocationWithReady delegateFee(final boolean delegateFee) {
      if (delegateFee && !this.function.isFeeDelegation()) {
        throw new HerajException("Target function cannot delegate fee");
      }
      this.delegateFee = delegateFee;
      return this;
    }

    @Override
    public hera.api.model.ContractInvocation build() {
      return new ContractInvocation(contractInterface.getAddress(), function, asList(args), amount,
          delegateFee);
    }

  }

}
