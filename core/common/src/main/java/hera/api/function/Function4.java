/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.function;

public interface Function4<T1, T2, T3, T4, R> extends Function<R> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t1 the 1st argument
   * @param t2 the 2nd argument
   * @param t3 the 3rd argument
   * @param t4 the 4th argument
   * @return the function result
   */
  R apply(T1 t1, T2 t2, T3 t3, T4 t4);

}
