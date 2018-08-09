/*
 * @copyright defined in LICENSE.txt
 */

package hera;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@ToString
public class ProjectFile {
  @Getter
  @Setter
  protected String name;

  @Getter
  @Setter
  protected String source;

  @Getter
  @Setter
  protected String target;

  @Getter
  @Setter
  protected List<ProjectFile> dependencies;

  @Getter
  @Setter
  protected List<String> tests;
}
