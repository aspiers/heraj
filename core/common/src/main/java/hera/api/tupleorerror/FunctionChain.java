/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.tupleorerror;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class FunctionChain {

  /**
   * Make a sequence of future with ResultOrErrorFutures.
   *
   * @param f0 a function suppling future0
   * @param f1 a function suppling future1
   * @return sequence of future
   */
  public static <R0, R1> Tuple2OrErrorFuture<R0, R1> seqf(Function0<ResultOrErrorFuture<R0>> f0,
      Function0<ResultOrErrorFuture<R1>> f1) {
    try {
      ResultOrErrorFuture<R0> future0 = f0.apply();
      ResultOrErrorFuture<R1> future1 = f1.apply();

      CompletableFuture<Tuple2OrError> deligate =
          AbstractTupleOrErrorFuture.allOf(future0, future1).thenApply(futureVoid -> {
            try {
              ResultOrError<R0> r0 = future0.get();
              if (r0.hasError()) {
                return new Tuple2OrError<>(r0.getError());
              }
              ResultOrError<R1> r1 = future1.get();
              if (r1.hasError()) {
                return new Tuple2OrError<>(r1.getError());
              }
              return success(r0.getResult(), r1.getResult());
            } catch (Throwable e) {
              return new Tuple2OrError<>(e);
            }
          });
      return Tuple2OrErrorFuture.withDeligate(() -> deligate);
    } catch (Throwable e) {
      return Tuple2OrErrorFuture.supply(() -> new Tuple2OrError<>(e));
    }
  }

  /**
   * Make a sequence of future with ResultOrErrorFutures.
   *
   * @param f0 a function suppling future0
   * @param f1 a function suppling future1
   * @param f2 a function suppling future2
   * @return sequence of future
   */
  public static <R0, R1, R2> Tuple3OrErrorFuture<R0, R1, R2> seqf(
      Function0<ResultOrErrorFuture<R0>> f0, Function0<ResultOrErrorFuture<R1>> f1,
      Function0<ResultOrErrorFuture<R2>> f2) {
    try {
      ResultOrErrorFuture<R0> future0 = f0.apply();
      ResultOrErrorFuture<R1> future1 = f1.apply();
      ResultOrErrorFuture<R2> future2 = f2.apply();

      CompletableFuture<Tuple3OrError> deligate =
          AbstractTupleOrErrorFuture.allOf(future0, future1).thenApply(futureVoid -> {
            try {
              ResultOrError<R0> r0 = future0.get();
              if (r0.hasError()) {
                return new Tuple3OrError<>(r0.getError());
              }
              ResultOrError<R1> r1 = future1.get();
              if (r1.hasError()) {
                return new Tuple3OrError<>(r1.getError());
              }
              ResultOrError<R2> r2 = future2.get();
              if (r2.hasError()) {
                return new Tuple3OrError<>(r2.getError());
              }
              return success(r0.getResult(), r1.getResult(), r2.getResult());
            } catch (Throwable e) {
              return new Tuple3OrError<>(e);
            }
          });
      return Tuple3OrErrorFuture.withDeligate(() -> deligate);
    } catch (Throwable e) {
      return Tuple3OrErrorFuture.supply(() -> new Tuple3OrError<>(e));
    }
  }

  /**
   * Make a sequence of future with ResultOrErrorFutures.
   *
   * @param f0 a function suppling future0
   * @param f1 a function suppling future1
   * @param f2 a function suppling future2
   * @param f3 a function suppling future3
   * @return sequence of future
   */
  public static <R0, R1, R2, R3> Tuple4OrErrorFuture<R0, R1, R2, R3> seqf(
      Function0<ResultOrErrorFuture<R0>> f0, Function0<ResultOrErrorFuture<R1>> f1,
      Function0<ResultOrErrorFuture<R2>> f2, Function0<ResultOrErrorFuture<R3>> f3) {
    try {
      ResultOrErrorFuture<R0> future0 = f0.apply();
      ResultOrErrorFuture<R1> future1 = f1.apply();
      ResultOrErrorFuture<R2> future2 = f2.apply();
      ResultOrErrorFuture<R3> future3 = f3.apply();

      CompletableFuture<Tuple4OrError> deligate =
          AbstractTupleOrErrorFuture.allOf(future0, future1).thenApply(futureVoid -> {
            try {
              ResultOrError<R0> r0 = future0.get();
              if (r0.hasError()) {
                return new Tuple4OrError<>(r0.getError());
              }
              ResultOrError<R1> r1 = future1.get();
              if (r1.hasError()) {
                return new Tuple4OrError<>(r1.getError());
              }
              ResultOrError<R2> r2 = future2.get();
              if (r2.hasError()) {
                return new Tuple4OrError<>(r2.getError());
              }
              ResultOrError<R3> r3 = future3.get();
              if (r3.hasError()) {
                return new Tuple4OrError<>(r3.getError());
              }
              return success(r0.getResult(), r1.getResult(), r2.getResult(), r3.getResult());
            } catch (Throwable e) {
              return new Tuple4OrError<>(e);
            }
          });
      return Tuple4OrErrorFuture.withDeligate(() -> deligate);
    } catch (Throwable e) {
      return Tuple4OrErrorFuture.supply(() -> new Tuple4OrError<>(e));
    }
  }

  /**
   * Make a sequence of with ResultOrErrors.
   *
   * @param f0 a function suppling {@code ResultOrError}
   * @param f1 a function suppling {@code ResultOrError}
   * @return sequence of result or error
   */
  public static <R0, R1> Tuple2OrError<R0, R1> seq(Function0<ResultOrError<R0>> f0,
      Function0<ResultOrError<R1>> f1) {
    ResultOrErrorFuture future0 = ResultOrErrorFuture.supply(() -> f0.apply());
    ResultOrErrorFuture future1 = ResultOrErrorFuture.supply(() -> f1.apply());

    Tuple2OrErrorFuture<R0, R1> future = seqf(() -> future0, () -> future1);
    return future.get();
  }

  /**
   * Make a sequence of with ResultOrErrors.
   *
   * @param f0 a function suppling {@code ResultOrError}
   * @param f1 a function suppling {@code ResultOrError}
   * @param f2 a function suppling {@code ResultOrError}
   * @return sequence of result or error
   */
  public static <R0, R1, R2> Tuple3OrError<R0, R1, R2> seq(Function0<ResultOrError<R0>> f0,
      Function0<ResultOrError<R1>> f1, Function0<ResultOrError<R2>> f2) {
    ResultOrErrorFuture future0 = ResultOrErrorFuture.supply(() -> f0.apply());
    ResultOrErrorFuture future1 = ResultOrErrorFuture.supply(() -> f1.apply());
    ResultOrErrorFuture future2 = ResultOrErrorFuture.supply(() -> f2.apply());

    Tuple3OrErrorFuture<R0, R1, R2> future = seqf(() -> future0, () -> future1, () -> future2);
    return future.get();
  }

  /**
   * Make a sequence of with ResultOrErrors.
   *
   * @param f0 a function suppling {@code ResultOrError}
   * @param f1 a function suppling {@code ResultOrError}
   * @param f2 a function suppling {@code ResultOrError}
   * @param f3 a function suppling {@code ResultOrError}
   * @return sequence of result or error
   */
  public static <R0, R1, R2, R3> Tuple4OrError<R0, R1, R2, R3> seq(Function0<ResultOrError<R0>> f0,
      Function0<ResultOrError<R1>> f1, Function0<ResultOrError<R2>> f2,
      Function0<ResultOrError<R3>> f3) {
    ResultOrErrorFuture future0 = ResultOrErrorFuture.supply(() -> f0.apply());
    ResultOrErrorFuture future1 = ResultOrErrorFuture.supply(() -> f1.apply());
    ResultOrErrorFuture future2 = ResultOrErrorFuture.supply(() -> f2.apply());
    ResultOrErrorFuture future3 = ResultOrErrorFuture.supply(() -> f3.apply());
    Tuple4OrErrorFuture<R0, R1, R2, R3> future =
        seqf(() -> future0, () -> future1, () -> future2, () -> future3);
    return future.get();
  }


  public static <T> ResultOrError<T> success(T t) {
    return new ResultOrError<>(t, null);
  }

  public static <T0, T1> Tuple2OrError<T0, T1> success(T0 t0, T1 t1) {
    return new Tuple2OrError<>(t0, t1);
  }

  public static <T0, T1, T2> Tuple3OrError<T0, T1, T2> success(T0 t0, T1 t1, T2 t2) {
    return new Tuple3OrError<>(t0, t1, t2);
  }

  public static <T0, T1, T2, T3> Tuple4OrError<T0, T1, T2, T3> success(T0 t0, T1 t1, T2 t2, T3 t3) {
    return new Tuple4OrError<>(t0, t1, t2, t3);
  }

  public static ResultOrError fail(Throwable error) {
    return new ResultOrError(null, error);
  }

}