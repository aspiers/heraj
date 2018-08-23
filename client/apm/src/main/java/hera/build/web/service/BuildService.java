/*
 * @copyright defined in LICENSE.txt
 */

package hera.build.web.service;

import static java.util.stream.Collectors.toList;

import hera.BuildResult;
import hera.build.web.model.BuildSummary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

public class BuildService extends AbstractService {

  @Getter
  protected LiveUpdateService liveUpdateService = new LiveUpdateService();

  protected final List<String> uuids = new ArrayList<>();

  protected final Map<String, BuildResult> uuid2buildResult = new HashMap<>();

  /**
   * Save build result for web request.
   *
   * @param buildResult build result
   */
  public void save(final BuildResult buildResult) {
    uuid2buildResult.put(buildResult.getUuid(), buildResult);
    uuids.add(buildResult.getUuid());
    try {
      liveUpdateService.notifyChange(new BuildSummary(buildResult));
    } catch (final Throwable ex) {
      logger.trace("Ignore exception: {}", ex.getClass());
    }
  }

  public Optional<BuildResult> get(final String uuid) {
    return Optional.ofNullable(uuid2buildResult.get(uuid));
  }

  /**
   * List build summaries.
   *
   * @param from starting point
   * @param requestSize needed size
   *
   * @return build summaries
   */
  public List<BuildSummary> list(final String from, final int requestSize) {
    int fromIndex = (null == from) ? 0 : uuids.indexOf(from);
    int toIndex = Math.min(fromIndex + requestSize, uuids.size());
    if (fromIndex < 0) {
      return null;
    } else {
      return uuids.subList(fromIndex, toIndex).stream()
          .map(uuid2buildResult::get)
          .map(BuildSummary::new)
          .collect(toList());
    }
  }
}